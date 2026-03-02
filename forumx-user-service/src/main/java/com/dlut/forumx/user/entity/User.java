package com.dlut.forumx.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String keycloakId;
	private String username;
	private String email;
	private String phone;
	private String nickname;
	private String avatar;
	private String bio;
	private Integer gender;
	private LocalDate birthday;
	private String location;
	private String website;
	private Integer status;
	private LocalDateTime lastLoginTime;
	private String lastLoginIp;
	private LocalDateTime registerTime;
	private LocalDateTime updateTime;
}
