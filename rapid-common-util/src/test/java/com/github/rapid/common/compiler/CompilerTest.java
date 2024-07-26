package com.github.rapid.common.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

//编译器类只有jdk6里面才有
public class CompilerTest {
	static String filename="c:/JavaTest.java";
	
	//window下面的换行是\r\t 回车换行
	public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {
		//得到系统当前的路径,即是java工程的路径(E:\workspace\base)
		System.out.println(System.getProperty("user.dir"));
		
		//在程序里对java文件进行编译，也应该就是动态编译了
		
		writejavaFile();
		//得到系统当前的java编译器，也就是javac
		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		//不能是jre的运行环境，因为这个是纯净版本，不包含javac等，需要使用jdk才行,否则会是null
		//先得到一个文件管理对象
		//该对象的第一个参数是诊断监听器,
		StandardJavaFileManager javafile=javac.getStandardFileManager(null, null, null);
		//编译单元，可以有多个
		Iterable units=javafile.getJavaFileObjects(filename);
		//编译任务
		CompilationTask t = javac.getTask(null, javafile, null, null, null, units);
		t.call();
		javafile.close();
		
		URL[] urls=new URL[]{new URL("file:/c:/")};
		URLClassLoader classload=new URLClassLoader(urls);
		Class clazz=classload.loadClass("JavaTest");
		Method method=clazz.getMethod("main", String[].class);
		
		//注意，调用Method类的方法invoke(Object,Object), main方法是类Run的静态方法，调用时是不需要对象实例的。
		//Java中通过反射调用其他类中的main方法时要注意的问题
		//http://www.cnblogs.com/duancanmeng/archive/2012/04/14/2524614.html
		method.invoke(clazz.newInstance(),(Object)new String[]{});
		
	}
	//创建java文件
	public static void writejavaFile() throws IOException{
		File file=new File(filename);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw=new FileWriter(file);
		fw.write(javaClassContent());
		fw.close();
	}
	//用字符串连接成一个java类
	public static String javaClassContent(){
		String rt="\r\n";
		String java=new String();
		java+="public class JavaTest{"+rt;
		java+=" public static void main(String[] args){"+rt;
		java+="		System.out.println(\"hello world\");"+rt;
		java+="		show();"+rt;
		java+="	}"+rt;
		java+="	public static void show(){"+rt;
		java+="		for(int i=0;i<4;i++){"+rt;
		java+="			System.out.println(\"i=:\"+i);"+rt;
		java+="		}"+rt;
		java+="	}"+rt;
		java+="}";
		return java;
	}
	/**
	 * 运行结果：
	 * hello world
	 * i=:0
	 * i=:1
	 * i=:2
	 * i=:3
	 */
}
