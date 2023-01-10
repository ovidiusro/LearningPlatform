package com.hozan.univer.service;

import com.hozan.univer.AbstractTest;
import com.hozan.univer.model.Role;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceTests  extends AbstractTest {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RoleService service;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		// clean up after each test method
	}

	@Test
    public void findRoleByCodeTest() {
	    String code = "ROLE_ADMIN";

		Role entity = service.getByCode(code);

		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected code attribute match", code, entity.getCode());
    }
}
