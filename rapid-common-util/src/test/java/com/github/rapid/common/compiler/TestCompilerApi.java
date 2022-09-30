package com.github.rapid.common.compiler;
import java.lang.reflect.*;
import java.io.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.util.*;
import java.net.*;

public class TestCompilerApi
{
    private static void compilerJava() throws Exception
    {
    	String javaClassContent = newHelloWorldJavaProgram().toString();
    	String className = "HelloWorld";

    	boolean success = javaCompiler(className, javaClassContent);
    	
        System.out.println("javaCompiler Success: " + success);
        // 如果成功，通过reflection执行这段Java程序
        if (success){
            System.out.println("-----输出-----");
            Class<?> clazz = Class.forName(className);
			Method method = clazz.getDeclaredMethod("main", new Class[]{ String[].class });
			
			method.invoke(null, new Object[]{ null });
            System.out.println("-----输出 -----");
        }
        
    }

	private static boolean javaCompiler(String className, String javaClassContent) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaFileObject file = new JavaSourceFromString(className, javaClassContent.toString());
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null,diagnostics, null, null, compilationUnits);
        boolean success = task.call();
		return success;
	}
    
	private static StringWriter newHelloWorldJavaProgram() {
		// 定义一个StringWriter类，用于写Java程序
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        // 开始写Java程序
        out.println("public class HelloWorld {");
        out.println(" public static void main(String args[]) {");
        out.println(" System.out.println(\"Hello, World\");");
        out.println(" }");
        out.println("}");
        out.close();
		return writer;
	}
	
    public static void main(String args[]) throws Exception
    {
        compilerJava();
    }
}

// 用于传递源程序的JavaSourceFromString类
class JavaSourceFromString extends SimpleJavaFileObject
{
    final String javaCode;
    
    JavaSourceFromString(String className, String javaCode)
    {
    	super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.javaCode = javaCode;
    }
    
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
    {
        return javaCode;
    }
}