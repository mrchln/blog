package com.flchy.blog.base.response;

import java.io.Serializable;
import java.util.List;
/**
 * 返回List
 * 
 * @ClassName: VisitsListResult.java 
 * @Description:  
 * @author nieqs
 */
public class VisitsListResult implements Serializable {

	private static final long serialVersionUID = 4963955924590432862L;
	private List<?> result;
	
	private String adoptToken;
	public VisitsListResult() {
		
	}
	public VisitsListResult(List<?> result) {
		this.result = result;
	}
	public VisitsListResult(List<?> result, String adoptToken) {
		this.result = result;
		this.adoptToken = adoptToken;
	}

	public List<?> getResult() {
		return result;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

	public String getAdoptToken() {
		return adoptToken;
	}

	public void setAdoptToken(String adoptToken) {
		this.adoptToken = adoptToken;
	}
	

	

}
