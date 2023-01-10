package com.hozan.univer.service;

import com.hozan.univer.AbstractTest;
import com.hozan.univer.model.Account;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;


public class AccountServiceTests extends AbstractTest{

	@Test
	public void contextLoads() {
	}
	@Autowired
	private AccountService service;

	@Before
	public void setUp() {
		service.evictCache();
	}

	@After
	public void tearDown() {
		// clean up after each test method
	}


	@Test
	public void testByUsername() {

		Long id = 8L;

		Account entity = service.getByUsername("admin").get();


		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());

	}


		@Test
	public void testFindAll() {

		Collection<Account> list = service.getAll().get();

		Assert.assertNotNull("failure - expected not null", list);
		Assert.assertEquals("failure - expected list size", 3, list.size());

	}

	@Test
	public void testFindOne()
	{

		Long id = 7L;

		Account entity = service.getById(id).get();


		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
	}

	@Test
	public void testFindOneNotFound() {

		Long id = Long.MAX_VALUE;

		Account entity = service.getById(id).get();
		Assert.assertNull("failure - expected null", entity);
	}

	@Test
	public void testCreate() {

		Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());


		Account entity = new Account("nameTest", "exemplu@test.com","testUsername", "test", date );
		Account createdEntity = service.create(entity).get();

		Assert.assertNotNull("failure - expected not null", createdEntity);
		Assert.assertNotNull("failure - expected id attribute not null",
				createdEntity.getId());
		Assert.assertEquals("failure - expected text attribute match", "test",
				createdEntity.getUsername());

		Collection<Account> list = service.getAll().get();

		Assert.assertEquals("failure - expected size", 4, list.size());

	}

	@Test
	public void testCreateWithId() {

		Exception exception = null;

		Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());
		Account entity = new Account("nameTest2", "exemplu@test2.com","testUsername2", "test2", date );
		entity.setId(Long.MAX_VALUE);

		try {
			service.create(entity);
		} catch (EntityExistsException e) {
			exception = e;
		}

		Assert.assertNotNull("failure - expected exception", exception);
		Assert.assertTrue("failure - expected EntityExistsException",
				exception instanceof EntityExistsException);

	}

	@Test
	public void testUpdate() {

		Long id = 9L;

		Account entity = service.getById(id).get();

		Assert.assertNotNull("failure - expected not null", entity);

		String updatedUsername = entity.getUsername() + " test";

		entity.setUsername(updatedUsername);
		Account updatedEntity = service.update(entity).get();

		Assert.assertNotNull("failure - expected not null", updatedEntity);
		Assert.assertEquals("failure - expected id attribute match", id,
				updatedEntity.getId());
		Assert.assertEquals("failure - expected text attribute match",
				updatedUsername, updatedEntity.getUsername());

	}

	@Test
	public void testUpdateNotFound() {

		Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());
		Account entity = new Account("nameTest2", "exemplu@test2.com","testUsername2", "test2", date );
		entity.setId(Long.MAX_VALUE);

		Exception exception = null;


		try {
			service.update(entity);
		} catch (NoResultException e) {
			exception = e;
		}

		Assert.assertNotNull("failure - expected exception", exception);
		Assert.assertTrue("failure - expected NoResultException",
				exception instanceof NoResultException);

	}

	@Test
	public void testDelete() {

		Long id = 7L;
		Account entity = service.getById(id).get();

		Assert.assertNotNull("failure - expected not null", entity);

		service.remove(id);

		Collection<Account> list = service.getAll().get();
		Assert.assertEquals("failure - expected size", 2, list.size());
		Account deletedEntity = service.getById(id).get();
		Assert.assertNull("failure - expected null", deletedEntity);
	}

}
