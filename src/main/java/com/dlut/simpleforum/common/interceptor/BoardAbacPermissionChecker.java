package com.dlut.simpleforum.common.interceptor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.simpleforum.common.enums.ABACPermission;
import com.dlut.simpleforum.common.enums.ABACPermission.ResourceType;
import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.repository.BoardRepository;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Component
public class BoardAbacPermissionChecker implements AbacPermissionChecker {

	private static List<ABACPermission> supportsPermissions = List.of(ABACPermission.values())
			.stream()
			.takeWhile((permission) -> permission.getResourceType().equals(ResourceType.BOARD))
			.toList();

	private final BoardRepository boardRepository;

	@Override
	public boolean supports(ABACPermission abacPermission) {
		return supportsPermissions.contains(abacPermission);
	}

	@Transactional(readOnly = true)
	@Override
	public void checkPermission(Long resourceId) {
		SessionUser sessionUser = SessionManager.getSessionUser();
		Board board = boardRepository.findById(resourceId).orElseThrow();
		if (!board.getModerator().getUid().equals(sessionUser.getUid())) {
			throw new IllegalArgumentException("");
		}
	}
}
