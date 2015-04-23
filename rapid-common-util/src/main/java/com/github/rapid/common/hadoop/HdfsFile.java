package com.github.rapid.common.hadoop;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;

/**
 * 
 * @author badqiu
 *
 */
public class HdfsFile extends File{
	private static final long serialVersionUID = 1L;
	
	private FileSystem fs;
	private Path path;
	
	public HdfsFile(FileSystem fs,String path) {
		this(fs,new Path(toRootDirIfEmpty(path)));
	}

	public HdfsFile(FileSystem fs, Path path) {
		super(path.getName());
		if(fs == null) 
			throw new IllegalArgumentException("fs must be not null");
		if(path == null)
			throw new IllegalArgumentException("path must be not null");
		this.fs = fs;
		this.path = path;
	}
	
	public HdfsFile(FileSystem fs,HdfsFile parent, String child) {
		this(fs,new Path(parent.getHdfsPath(),toRootDirIfEmpty(child)));
	}

	public HdfsFile(FileSystem fs,String parent, String child) {
		this(fs,new Path(toRootDirIfEmpty(parent),toRootDirIfEmpty(child)));
	}

	public Path getHdfsPath() {
		return this.path;
	}

	private static String toRootDirIfEmpty(String child) {
		return "".equals(child) ? "/" : child;
	}
	
	@Override
	public boolean canExecute() {
		try {
			return FsAction.EXECUTE.implies(fs.getFileStatus(path).getPermission().getUserAction());
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean canRead() {
		try {
			return fs.exists(this.path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean canWrite() {
		try {
			return fs.exists(this.path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	private void handleIOException(IOException e) {
		throw new HdfsIOException(e);
	}

	@Override
	public int compareTo(File pathname) {
		return path.compareTo(new Path(pathname.getName()));
	}

	@Override
	public boolean createNewFile() throws IOException {
		return fs.createNewFile(path);
	}

	@Override
	public boolean delete() {
		try {
			return fs.delete(path, true);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public void deleteOnExit() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
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
		return this;
	}

	@Override
	public String getAbsolutePath() {
		return path.getName();
	}

	@Override
	public File getCanonicalFile() throws IOException {
		return this;
	}

	@Override
	public String getCanonicalPath() throws IOException {
		return this.path.getName();
	}

	@Override
	public long getFreeSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return path.getName();
	}

	@Override
	public String getParent() {
		return path.getParent().getName();
	}

	@Override
	public File getParentFile() {
		return new HdfsFile(this.fs,path.getParent().getName());
	}

	@Override
	public String getPath() {
		return path.toString();
	}

	@Override
	public long getTotalSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getUsableSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public boolean isAbsolute() {
		return path.isAbsolute();
	}

	@Override
	public boolean isDirectory() {
		try {
			return fs.getFileStatus(path).isDir();
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean isFile() {
		try {
			return fs.isFile(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean isHidden() {
		return path.getName().startsWith(".") || path.getName().startsWith("_");
	}

	@Override
	public long lastModified() {
		try {
			return fs.getFileStatus(path).getModificationTime();
		} catch (IOException e) {
			handleIOException(e);
			return 0;
		}
	}

	@Override
	public long length() {
		try {
			return fs.getFileStatus(path).getLen();
		} catch (IOException e) {
			handleIOException(e);
			return 0;
		}
	}

	@Override
	public String[] list() {
		FileStatus[] status;
		try {
			status = fs.listStatus(path);
			if(status == null) return new String[0];
			String[] results = new String[status.length];
			for(int i = 0; i < results.length; i++) {
				results[i] = status[i].getPath().getName();
			}
			return results;
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public String[] list(FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public File[] listFiles() {
		FileStatus[] status;
		try {
			status = fs.listStatus(path);
			HdfsFile[] results = new HdfsFile[status.length];
			for(int i = 0; i < results.length; i++) {
				results[i] = new HdfsFile(fs,status[i].getPath());
			}
			return results;
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public File[] listFiles(FileFilter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public File[] listFiles(FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean mkdir() {
		try {
			return fs.mkdirs(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean mkdirs() {
		try {
			return fs.mkdirs(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean renameTo(File dest) {
		try {
			return fs.rename(path, new Path(dest.getName()));
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setExecutable(boolean executable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setLastModified(long time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadable(boolean readable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadable(boolean readable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadOnly() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setWritable(boolean writable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setWritable(boolean writable) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	protected void finalize() throws Throwable {
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
	
}
