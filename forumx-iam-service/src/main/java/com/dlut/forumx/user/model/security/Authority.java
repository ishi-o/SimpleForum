package com.dlut.forumx.user.model.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authority implements GrantedAuthority {
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return null;
	}

}
