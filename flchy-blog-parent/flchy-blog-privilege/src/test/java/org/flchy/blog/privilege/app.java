package org.flchy.blog.privilege;

import java.net.URI;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.flchy.blog.privilege.controller.AppTest;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class app {
	public static void main(String[] args) {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(81).build();
		ResourceConfig config = new ResourceConfig(AppTest.class);
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
//		new WebTarget().getConfiguration()
	}
}
