package org.flchy.blog.privilege.controller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Path("/")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class TokenPortalController {
	@Path("")
	public Object login() {
		return null;
	}

}
