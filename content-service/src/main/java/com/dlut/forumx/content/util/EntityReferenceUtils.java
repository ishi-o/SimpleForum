package com.dlut.forumx.content.util;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

/**
 * @author Ishi_O
 * @since
 */
@Component
public class EntityReferenceUtils {
	private static EntityManager ENTITY_MANAGER;

	private EntityReferenceUtils(EntityManager entityManager) {
		ENTITY_MANAGER = entityManager;
	}

	public static <T> T getReferenceById(Class<T> clazz, Object id) {
		if (id == null) {
			return null;
		}
		return ENTITY_MANAGER.getReference(clazz, id);
	}
}
