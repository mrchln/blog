package com.flchy.blog;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

public class JerseyConfig  extends ResourceConfig{
	  public JerseyConfig() {  
          register(RequestContextFilter.class);  
          register(JacksonFeature.class);  
          register(MultiPartFeature.class);
          //配置restful package.  
          packages("com.flchy.blog");  
       } 
}
