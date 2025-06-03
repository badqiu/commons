package com.github.rapid.common.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.Assert;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 处理静态资源的serlvet
 * @author badqiu
 *
 */
public class StaticServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private String localPath = null;
	
	private ServletContext servletContext;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		localPath = config.getInitParameter("localPath");
		Assert.hasLength(localPath, "'localPath' must be not blank");
		this.servletContext = config.getServletContext();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getRequestURI();
		setResponseHeaders(resp, path);
		writeFile2Response(resp, path);
	}

	private void writeFile2Response(HttpServletResponse resp, String path)
			throws FileNotFoundException, IOException {
		File inputFile = new File(localPath,path);
		InputStream input = new FileInputStream(inputFile);
		OutputStream output = resp.getOutputStream();
		try {
			IOUtils.copy(input, output);
		}finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	private void setResponseHeaders(HttpServletResponse resp, String path) {
		String mimeType = servletContext.getMimeType(new File(path).getName());
		if(mimeType == null) {
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + new File(path).getName() + "\"");
		}else 
			resp.setHeader("Content-Type", mimeType);
			if(!mimeType.contains("text")) {
				resp.setHeader("Content-Disposition", "attachment; filename=\"" + new File(path).getName() + "\"");
		}
	}
}
