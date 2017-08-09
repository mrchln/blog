package com.flchy.blog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;  

@Path("/")
@Component
@Produces(MediaType.APPLICATION_JSON)  
public class Test {
	
	@GET
	@Path("/test")
	public Object  test(){
		  Map<String,Object> map = new HashMap<String,Object>();  
	       map.put("code","1");  
	       map.put("codeMsg", "success");  
	       return map;
	}

}
