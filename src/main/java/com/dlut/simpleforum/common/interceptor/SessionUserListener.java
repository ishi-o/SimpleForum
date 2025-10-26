package com.dlut.simpleforum.common.interceptor;

import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@Component
@AllArgsConstructor
public class SessionUserListener {
	private final SessionManager sessionManager;

	@EventListener
	public void sessionDestroyed(SessionDestroyedEvent se) {
		HttpSession session = se.getSession();
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		sessionManager.removeActiveUser(sessionUser.getUid());
	}
}
