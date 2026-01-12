package com.dlut.simpleforum.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validator;

/**
 */
@SpringBootTest
public class UserValidationTest {

	private final Validator validator;

	@Autowired
	public UserValidationTest(Validator validator) {
		this.validator = validator;
	}

	@Test
	public void testUsernameValidation() {
	}
}
