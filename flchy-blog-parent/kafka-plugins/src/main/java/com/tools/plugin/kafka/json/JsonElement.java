package com.tools.plugin.kafka.json;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JsonElement<K, V, Y> implements java.io.Serializable{
	private static final long serialVersionUID = 5757165876896778414L;
	private Map<K, V> headers = new HashMap<K, V>();
	private Y body;

	public void addHeader(K k, V v) {
		headers.put(k, v);
	}

	public void setHeader(K k, V v) {
		addHeader(k, v);
	}

	public void setBody(Y data) {
		body = data;
	}

	public Y getBody() {
		return body;
	}

	public Object getHeader(K key) {
		return headers.get(key);
	}

	public void setHeaders(Map<K, V> headers) {
		this.headers = headers;
	}


	public Map<K, V> getHeaders() {
		return headers;
	}

}
