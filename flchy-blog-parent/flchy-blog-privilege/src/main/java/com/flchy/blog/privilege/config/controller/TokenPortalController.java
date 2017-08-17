package com.flchy.blog.privilege.config.controller;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.flchy.blog.privilege.config.service.IInfoUserService;

@Path("/")
@Controller
@Produces(MediaType.APPLICATION_JSON)
public class TokenPortalController {
	@Autowired
	private IInfoUserService iInfoUserService;

	@POST
	@Path("login")
	public Object login(Map<String, Object> map) {
//		iInfoUserService
		System.out.println(map);
		return true;
	}

}
