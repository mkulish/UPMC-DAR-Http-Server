package edu.upmc.dar.server.http.session;

import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.util.MD5Util;

import java.util.*;

public class SessionUtil {
	private static Map<String, Session> sessionsMap = new HashMap<>();
	
	public static synchronized void checkSession(HttpRequest request) {
		String userAgent = request.getHeader().getParamsMap().get("User-Agent");
		String ip = request.getIp().getHostAddress();
		String sessionId = generateId(userAgent, ip);

		Session session = sessionsMap.get(sessionId);

		if (session != null) {
			System.out.println("Found the session " + sessionId + " for " + ip + " with user-agent '" + userAgent + "'");

			Calendar sessionExpirationDate = Calendar.getInstance();
			sessionExpirationDate.setTime(session.getDate());
			sessionExpirationDate.add(Calendar.SECOND, 10);

			if (new Date().after(sessionExpirationDate.getTime())) {
				//Session expired
				System.out.println("Session expired");

				request.setSessionExpired(true);
				session = null;
			} else {
				//Updating the session datetime
				session.setDate(new Date());
				request.setSession(session);
			}
		}

		if (session == null) {
			System.out.println("Created the session " + sessionId + " for " + ip + " with user-agent '" + userAgent + "'");

			session = new Session(sessionId);
			sessionsMap.put(sessionId, session);
		}
		request.setSession(session);
	}

	public static String generateId(String userAgent, String ip){
		return MD5Util.md5(userAgent + ":" + ip);
	}
	
	public synchronized void printSessions() {
		for (Session session : sessionsMap.values()) {
			System.out.println(session.getId());
		}
	}
}
