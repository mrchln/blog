package com.flchy.blog.base.datasource.dbconfig.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.datasource.dbconfig.service.ITestService;

@Path("/")
@Component
@Produces(MediaType.APPLICATION_JSON) 
public class TestController  {
	@Autowired
	private ITestService iTestService;
	
	@Path("tt")
	@GET
	public Object test(){
		com.flchy.blog.base.datasource.dbconfig.entity.test t=new com.flchy.blog.base.datasource.dbconfig.entity.test();
		t.setName("sssssss");
		int a= iTestService.insertAll(t);
		return t;
	}
	
}
