package edu.upmc.dar.server.http.session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Session {
	private String id;
	private Date date;
	private Map<String, Object> attributesMap = new HashMap<>();
	
	public Session(String userAgent, String ip) {
		this.id = SessionUtil.generateId(userAgent, ip);
		this.date= new java.util.Date();
	}

	public Session(String id){
		this.id = id;
		this.date = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Object getAttribute(String key){
		return attributesMap.get(key);
	}

	public void setAttribute(String key, Object value){
		attributesMap.put(key, value);
	}
}
