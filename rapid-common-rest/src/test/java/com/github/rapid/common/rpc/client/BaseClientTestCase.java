package com.github.rapid.common.rpc.client;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.github.rapid.common.rpc.tools.JettyServer;

public class BaseClientTestCase extends Assert {
	static Server server;
	
	@BeforeClass
	public static void superSetUp() throws Exception {
		server = JettyServer.buildNormalServer(26060, "/");
		new Thread() {
			public void run() {
				try {
					server.start();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}.start();
		Thread.sleep(4000);
	}
	
	@AfterClass
	public static void superTearDown() throws Exception {
		server.stop();
	}
}
