package com.dlut.forumx.user.service;

public interface KeycloakSyncService {
	void handleUserCreated(String keycloakId, String username, String email);

	void handleUserUpdated(String keycloakId, String username, String email);

	void handleUserDeleted(String keycloakId);
}
