package com.flchy.blog.base.response;

import java.io.Serializable;
import java.util.Map;

public class VisitsMapResult implements Serializable {
	private static final long serialVersionUID = 94864736703726013L;
	private Map<String, Object> result;
	
	private String adoptToken;

	public VisitsMapResult() {

	}

	public VisitsMapResult(Map<String, Object> result) {
		this.result = result;
	}

	public VisitsMapResult(Map<String, Object> result, String adoptToken) {
		this.result = result;
		this.adoptToken = adoptToken;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getAdoptToken() {
		return adoptToken;
	}

	public void setAdoptToken(String adoptToken) {
		this.adoptToken = adoptToken;
	}

}
