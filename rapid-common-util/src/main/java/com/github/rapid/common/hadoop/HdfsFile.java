package com.github.rapid.common.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.AccessControlException;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HDFS文件系统操作的封装类，继承自Java File类
 * 
 * @author badqiu
 */
public class HdfsFile extends File {
    private static final long serialVersionUID = 1L;
    
    private final FileSystem fs;
    private final Path path;
    private static final AtomicBoolean shutdownHookRegistered = new AtomicBoolean(false);
    
    public HdfsFile(FileSystem fs, String path) {
        this(fs, new Path(toRootDirIfEmpty(path)));
    }

    public HdfsFile(FileSystem fs, Path path) {
        super(path.toString());
        if (fs == null) 
            throw new IllegalArgumentException("fs must be not null");
        if (path == null)
            throw new IllegalArgumentException("path must be not null");
        this.fs = fs;
        this.path = path;
        
        // 注册关闭钩子
        registerShutdownHook();
    }
    
    public HdfsFile(FileSystem fs, HdfsFile parent, String child) {
        this(fs, new Path(parent.getHdfsPath(), toRootDirIfEmpty(child)));
    }

    public HdfsFile(HdfsFile parent, String child) {
        this(parent.getFileSystem(), new Path(parent.getHdfsPath(), toRootDirIfEmpty(child)));
    }
    
    public HdfsFile(FileSystem fs, String parent, String child) {
        this(fs, new Path(toRootDirIfEmpty(parent), toRootDirIfEmpty(child)));
    }

    public Path getHdfsPath() {
        return this.path;
    }

    private static String toRootDirIfEmpty(String child) {
        return child == null || child.isEmpty() ? "/" : child;
    }
    
    private void registerShutdownHook() {
        if (shutdownHookRegistered.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (fs != null) {
                        fs.close();
                    }
                } catch (IOException e) {
                    // 静默处理关闭异常
                }
            }));
        }
    }
    
    /**
     * 处理IO异常，将HDFS异常转换为运行时异常
     */
    private void handleIOException(IOException e) {
        throw new HdfsIOException(e);
    }
    
    @Override
    public boolean canExecute() {
        try {
            FileStatus status = fs.getFileStatus(path);
            FsPermission permission = status.getPermission();
            // 检查用户执行权限
            return permission.getUserAction().implies(FsAction.EXECUTE) ||
                   permission.getGroupAction().implies(FsAction.EXECUTE) ||
                   permission.getOtherAction().implies(FsAction.EXECUTE);
        } catch (AccessControlException e) {
            // 权限不足
            return false;
        } catch (IOException e) {
            // 文件不存在或其他IO异常
            return false;
        }
    }

    @Override
    public boolean canRead() {
        try {
            if (!fs.exists(path)) {
                return false;
            }
            FileStatus status = fs.getFileStatus(path);
            FsPermission permission = status.getPermission();
            // 检查用户读权限
            return permission.getUserAction().implies(FsAction.READ) ||
                   permission.getGroupAction().implies(FsAction.READ) ||
                   permission.getOtherAction().implies(FsAction.READ);
        } catch (AccessControlException e) {
            // 权限不足
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean canWrite() {
        try {
            if (!fs.exists(path)) {
                // 如果文件不存在，检查父目录是否有写权限
                Path parent = path.getParent();
                if (parent == null) {
                    parent = new Path("/");
                }
                if (fs.exists(parent)) {
                    FileStatus status = fs.getFileStatus(parent);
                    FsPermission permission = status.getPermission();
                    return permission.getUserAction().implies(FsAction.WRITE) ||
                           permission.getGroupAction().implies(FsAction.WRITE) ||
                           permission.getOtherAction().implies(FsAction.WRITE);
                }
                return false;
            }
            // 文件存在，检查文件写权限
            FileStatus status = fs.getFileStatus(path);
            FsPermission permission = status.getPermission();
            return permission.getUserAction().implies(FsAction.WRITE) ||
                   permission.getGroupAction().implies(FsAction.WRITE) ||
                   permission.getOtherAction().implies(FsAction.WRITE);
        } catch (AccessControlException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public int compareTo(File other) {
        if (other instanceof HdfsFile) {
            HdfsFile hdfsOther = (HdfsFile) other;
            // 比较同一个文件系统的路径
            if (this.fs == hdfsOther.fs) {
                return this.path.compareTo(hdfsOther.path);
            }
            // 不同文件系统，比较URI
            return this.path.toUri().toString().compareTo(hdfsOther.path.toUri().toString());
        }
        // 与普通文件比较路径字符串
        return this.getAbsolutePath().compareTo(other.getAbsolutePath());
    }

    @Override
    public boolean createNewFile() throws IOException {
        try {
            if (fs.exists(path)) {
                return false;
            }
            // 创建空文件
            return fs.createNewFile(path);
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean delete() {
        try {
            return fs.delete(path, true); // 递归删除
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    /**
     * 标记文件在JVM退出时删除
     * 注意：由于HDFS是分布式文件系统，此方法可能不保证在JVM异常退出时执行
     */
    @Override
    public void deleteOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (fs.exists(path)) {
                    fs.delete(path, false);
                }
            } catch (IOException e) {
                // 静默处理
            }
        }));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        HdfsFile other = (HdfsFile) obj;
        
        // 比较文件系统和路径
        if (fs != other.fs && (fs == null || !fs.equals(other.fs))) {
            return false;
        }
        
        return path != null ? path.equals(other.path) : other.path == null;
    }

    @Override
    public boolean exists() {
        try {
            return fs.exists(path);
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public File getAbsoluteFile() {
        return this; // HDFS路径总是绝对的
    }

    @Override
    public String getAbsolutePath() {
        return path.toUri().getPath();
    }

    @Override
    public File getCanonicalFile() throws IOException {
        try {
            Path canonicalPath = fs.resolvePath(path);
            return new HdfsFile(fs, canonicalPath);
        } catch (IOException e) {
            handleIOException(e);
            return this;
        }
    }

    @Override
    public String getCanonicalPath() throws IOException {
        try {
            Path canonicalPath = fs.resolvePath(path);
            return canonicalPath.toUri().getPath();
        } catch (IOException e) {
            handleIOException(e);
            return getAbsolutePath();
        }
    }

    @Override
    public long getFreeSpace() {
        try {
            return fs.getStatus().getRemaining();
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override
    public long getTotalSpace() {
        try {
            return fs.getStatus().getCapacity();
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override
    public long getUsableSpace() {
        return getFreeSpace(); // 在HDFS中，可用空间通常等于剩余空间
    }

    @Override
    public String getParent() {
        Path parent = path.getParent();
        return parent == null ? null : parent.toString();
    }

    @Override
    public File getParentFile() {
        Path parent = path.getParent();
        return parent == null ? null : new HdfsFile(fs, parent);
    }

    @Override
    public String getPath() {
        return path.toString();
    }

    @Override
    public int hashCode() {
        int result = fs != null ? fs.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isAbsolute() {
        return path.isAbsolute();
    }

    @Override
    public boolean isDirectory() {
        try {
            return fs.getFileStatus(path).isDirectory();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile() {
        try {
            return !fs.getFileStatus(path).isDirectory();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isHidden() {
        String name = path.getName();
        return name.startsWith(".") || name.startsWith("_");
    }

    @Override
    public long lastModified() {
        try {
            return fs.getFileStatus(path).getModificationTime();
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override
    public long length() {
        try {
            return fs.getFileStatus(path).getLen();
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override
    public String[] list() {
        try {
            FileStatus[] statuses = fs.listStatus(path);
            if (statuses == null || statuses.length == 0) {
                return new String[0];
            }
            
            String[] result = new String[statuses.length];
            for (int i = 0; i < statuses.length; i++) {
                result[i] = statuses[i].getPath().getName();
            }
            return result;
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }

    @Override
    public String[] list(FilenameFilter filter) {
        try {
            FileStatus[] statuses = fs.listStatus(path);
            if (statuses == null || statuses.length == 0) {
                return new String[0];
            }
            
            List<String> filtered = new ArrayList<>();
            for (FileStatus status : statuses) {
                String name = status.getPath().getName();
                if (filter == null || filter.accept(this, name)) {
                    filtered.add(name);
                }
            }
            
            return filtered.toArray(new String[0]);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }

    @Override
    public File[] listFiles() {
        try {
            FileStatus[] statuses = fs.listStatus(path);
            if (statuses == null || statuses.length == 0) {
                return new File[0];
            }
            
            File[] result = new File[statuses.length];
            for (int i = 0; i < statuses.length; i++) {
                result[i] = new HdfsFile(fs, statuses[i].getPath());
            }
            return result;
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }

    @Override
    public File[] listFiles(FileFilter filter) {
        try {
            FileStatus[] statuses = fs.listStatus(path);
            if (statuses == null || statuses.length == 0) {
                return new File[0];
            }
            
            List<File> filtered = new ArrayList<>();
            for (FileStatus status : statuses) {
                HdfsFile file = new HdfsFile(fs, status.getPath());
                if (filter == null || filter.accept(file)) {
                    filtered.add(file);
                }
            }
            
            return filtered.toArray(new File[0]);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {
        try {
            FileStatus[] statuses = fs.listStatus(path);
            if (statuses == null || statuses.length == 0) {
                return new File[0];
            }
            
            List<File> filtered = new ArrayList<>();
            for (FileStatus status : statuses) {
                String name = status.getPath().getName();
                if (filter == null || filter.accept(this, name)) {
                    filtered.add(new HdfsFile(fs, status.getPath()));
                }
            }
            
            return filtered.toArray(new File[0]);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }

    @Override
    public boolean mkdir() {
        try {
            if (fs.exists(path)) {
                return false;
            }
            return fs.mkdirs(path);
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean mkdirs() {
        try {
            if (fs.exists(path)) {
                return false;
            }
            return fs.mkdirs(path);
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean renameTo(File dest) {
        try {
            Path destPath;
            if (dest instanceof HdfsFile) {
                destPath = ((HdfsFile) dest).path;
            } else {
                destPath = new Path(dest.getAbsolutePath());
            }
            return fs.rename(path, destPath);
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        try {
            FileStatus status = fs.getFileStatus(path);
            FsPermission current = status.getPermission();
            
            FsPermission newPermission = new FsPermission(
                current.getUserAction(),
                current.getGroupAction(),
                current.getOtherAction()
            );
            
            fs.setPermission(path, newPermission);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setExecutable(boolean executable) {
        return setExecutable(executable, true);
    }

    @Override
    public boolean setLastModified(long time) {
        try {
            fs.setTimes(path, time, -1); // 只设置修改时间，不设置访问时间
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        try {
            FileStatus status = fs.getFileStatus(path);
            FsPermission current = status.getPermission();
            
            FsPermission newPermission = new FsPermission(
                current.getUserAction(),
                current.getGroupAction(),
                current.getOtherAction()
            );
            
            fs.setPermission(path, newPermission);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setReadable(boolean readable) {
        return setReadable(readable, true);
    }

    @Override
    public boolean setReadOnly() {
        try {
            FsPermission readOnlyPermission = new FsPermission(
                FsAction.READ_EXECUTE,  // 用户：读和执行
                FsAction.READ_EXECUTE,  // 组：读和执行
                FsAction.READ_EXECUTE   // 其他：读和执行
            );
            fs.setPermission(path, readOnlyPermission);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        try {
            FileStatus status = fs.getFileStatus(path);
            FsPermission current = status.getPermission();
            
            FsPermission newPermission = new FsPermission(
                current.getUserAction(),
                current.getGroupAction(),
                current.getOtherAction()
            );
            
            fs.setPermission(path, newPermission);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public boolean setWritable(boolean writable) {
        return setWritable(writable, true);
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public URI toURI() {
        return path.toUri();
    }

    @Override
    public URL toURL() throws MalformedURLException {
        return path.toUri().toURL();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new HdfsFile(this.fs, this.path);
    }

    @Override
    protected void finalize() throws Throwable {
        // 清理资源，但不关闭FileSystem，因为可能被其他实例共享
        super.finalize();
    }
    
    public FileSystem getFileSystem() {
        return fs;
    }
    
    public InputStream open() {
        try {
            return fs.open(path);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }
    
    public OutputStream create() {
        try {
            return fs.create(path, true);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }
    
    public OutputStream append() {
        try {
            return fs.append(path);
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }
    
    /**
     * 获取文件块位置信息
     */
    public BlockLocation[] getFileBlockLocations() throws IOException {
        try {
            if (fs.exists(path)) {
                FileStatus status = fs.getFileStatus(path);
                if (status.isFile()) {
                    return fs.getFileBlockLocations(status, 0, status.getLen());
                }
            }
            return new BlockLocation[0];
        } catch (IOException e) {
            handleIOException(e);
            return new BlockLocation[0];
        }
    }
    
    /**
     * 获取文件权限
     */
    public FsPermission getPermission() throws IOException {
        try {
            return fs.getFileStatus(path).getPermission();
        } catch (IOException e) {
            handleIOException(e);
            return null;
        }
    }
    
    /**
     * 设置文件权限
     */
    public boolean setPermission(FsPermission permission) throws IOException {
        try {
            fs.setPermission(path, permission);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }
    
    /**
     * 设置文件所有者
     */
    public boolean setOwner(String username, String groupname) throws IOException {
        try {
            fs.setOwner(path, username, groupname);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }
    
    /**
     * 复制到本地文件系统
     */
    public boolean copyToLocalFile(File localFile) throws IOException {
        try {
            Path localPath = new Path(localFile.getAbsolutePath());
            fs.copyToLocalFile(path, localPath);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }
    
    /**
     * 从本地文件系统复制
     */
    public boolean copyFromLocalFile(File localFile) throws IOException {
        try {
            Path localPath = new Path(localFile.getAbsolutePath());
            fs.copyFromLocalFile(localPath, path);
            return true;
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }
}

