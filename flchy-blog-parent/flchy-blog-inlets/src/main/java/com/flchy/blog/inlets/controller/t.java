package com.flchy.blog.controller;

import java.lang.reflect.Method;

import javax.ws.rs.Path;


public class t {
	public static void main(String[] args) {
	
		
		Method[] methods=Test.class.getMethods();
		for (Method method : methods) {
			if(method.isAnnotationPresent(Path.class)){ 
				Path name = method.getAnnotation(Path.class); 
				System.out.println(name.value());
			}
		}
	}
}
