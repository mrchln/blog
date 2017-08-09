package com.zuobiao.analysis.privilege.config.response;

import java.io.Serializable;
import java.util.Map;

public class VisitsResult implements Serializable {
	private static final long serialVersionUID = 94864736703726013L;
	private Map<String, Object> result;
	
	private String adoptToken;

	public VisitsResult() {

	}

	public VisitsResult(Map<String, Object> result) {
		this.result = result;
	}

	public VisitsResult(Map<String, Object> result, String adoptToken) {
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
