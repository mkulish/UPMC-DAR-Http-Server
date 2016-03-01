package edu.upmc.dar.server.http.session;

import edu.upmc.dar.server.http.common.Cookie;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.util.MD5Util;

import java.util.*;

public class SessionUtil {
	private static Map<String, Session> sessionsMap = new HashMap<>();
	
	public static synchronized void checkSession(HttpRequest request) {
		String userAgent = request.getHeader().getParamsMap().get("User-Agent");
		String ip = request.getIp().getHostAddress();

		Cookie sessionCookie = request.getCookie("sessionToken");
		String token = (sessionCookie != null) ? sessionCookie.getValue() : null;

		Session session = null;
		if(token != null){
			session = sessionsMap.get(generateId(userAgent, ip, token));
		}

		if (session != null) {
			System.out.println("Found the session with token " + token + " for " + ip + " with user-agent '" + userAgent + "'");

			if (new Date().after(session.getExpirationDate())) {
				//Session expired
				System.out.println("Session expired");
				session = null;
			} else {
				//Updating the session datetime
				session.updateExpirationDate();
				request.setSession(session);
			}
		}

		if (session == null) {
			request.setSessionExpired(true);
			session = new Session(userAgent, ip);
			sessionsMap.put(session.getId(), session);

			System.out.println("Created the session with token " + session.getToken() + " for " + ip + " with user-agent '" + userAgent + "'");
		}
		request.setSession(session);
	}

	public static String generateId(String userAgent, String ip, String token){
		return MD5Util.md5(userAgent + "-&%$^" + ip + "^^ololo^^@@@ù*" + token);
	}
	
	public synchronized void printSessions() {
		for (Session session : sessionsMap.values()) {
			System.out.println(session.getId());
		}
	}
}
