package com.flchy.blog.privilege.config.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public Object login(@NotNull String loginName, 
          @NotNull  String loginPass ) {
//		iInfoUserService
		System.out.println(loginName);
		return null;
	}

}
