package edu.upmc.dar.server.http.session;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Session {
	public static final int EXPIRY_IN_MINUTES = 10;
	private static int tokenCounter = 0;

	private String token;
	private Date expirationDate;
	private String id;
	private Map<String, Object> attributesMap = new HashMap<>();
	
	public Session(String userAgent, String ip) {
		this.token = "id" + tokenCounter++;
		this.id = SessionUtil.generateId(userAgent, ip, token);
		updateExpirationDate();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void updateExpirationDate() {
		Calendar sessionExpirationDate = Calendar.getInstance();
		sessionExpirationDate.setTime(new Date());
		sessionExpirationDate.add(Calendar.MINUTE, EXPIRY_IN_MINUTES);
		this.expirationDate = sessionExpirationDate.getTime();
	}

	public Object getAttribute(String key){
		return attributesMap.get(key);
	}

	public void setAttribute(String key, Object value){
		attributesMap.put(key, value);
	}
}
