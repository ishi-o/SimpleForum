package com.dlut.simpleforum.common.interceptor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dlut.simpleforum.common.enums.ABACPermission;
import com.dlut.simpleforum.common.enums.ABACPermission.ResourceType;
import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.repository.PostRepository;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Component
public class PostAbacPermissionChecker implements AbacPermissionChecker {
	private static final List<ABACPermission> supportPermissions = List.of(
			ABACPermission.values())
			.stream()
			.takeWhile((permission) -> permission.getResourceType().equals(ResourceType.POST))
			.toList();

	private final PostRepository postRepository;

	@Override
	public boolean supports(ABACPermission abacPermission) {
		return supportPermissions.contains(abacPermission);
	}

	@Override
	public void checkPermission(Long resourceId) {
		SessionUser sessionUser = SessionManager.getSessionUser();
		Post post = postRepository.findById(resourceId).orElseThrow();
		if (!post.getAuthor().getUid().equals(sessionUser.getUid())) {
			throw new IllegalArgumentException("");
		}
	}
}
