package com.github.rapid.common.util.holder;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BeanValidatorHolderTest {

	@Test(expected=ConstraintViolationException.class)
	public void test() {
		BeanValidatorHolder.validateWithException(new ValidBean());
	}

	@Test(expected = ConstraintViolationException.class)
	public void test2() {
		BeanValidatorHolder.validateWithException(new ValidBean());
	}
	
	public static class ValidBean {
		
		@NotBlank @Length(min=5,max=50)
		private String username;
		
		
		@Min(1) @Max(130)
		private int age;
		
	}
}
