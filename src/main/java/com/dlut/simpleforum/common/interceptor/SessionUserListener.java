package com.dlut.simpleforum.common.interceptor;

import org.springframework.stereotype.Component;

import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Component
public class SessionUserListener implements HttpSessionListener {

	private final SessionManager sessionManager;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		session.setAttribute("sessionUser", SessionUser.createDefaultSessionUser());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		sessionManager.removeActiveUser(sessionUser.getUid());
	}
}
