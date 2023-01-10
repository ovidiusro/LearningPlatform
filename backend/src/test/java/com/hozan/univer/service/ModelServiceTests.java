package com.hozan.univer.service;

import com.hozan.univer.AbstractTest;
import com.hozan.univer.model.Group;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

public class ModelServiceTests extends AbstractTest{

    @Test
    public void contextLoads() {
    }
    @Autowired
    private GroupService service;

    @Before
    public void setUp() {
        service.evictCache();
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }


    @Test
    public void testByName() {

        Group entity = service.getByName("javaGroup").get();


        Assert.assertNotNull("failure - expected not null", entity);

    }


    @Test
    public void testFindAll() {

        Collection<Group> list = service.getAll();

        Assert.assertNotNull("failure - expected not null", list);
        Assert.assertEquals("failure - expected list size", 3, list.size());

    }

    @Test
    public void testFindOne()
    {

        Long id = 4L;

        Group entity = service.getById(id).get();


        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
    }

    @Test
    public void testFindOneNotFound() {

        Long id = Long.MAX_VALUE;

        Group entity = service.getById(id).get();
        Assert.assertNull("failure - expected null", entity);
    }

    @Test
    public void testCreate() {


        Group entity = new Group("testGroup","group for testing");

        Group createdEntity = service.create(entity).get();

        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdEntity.getId());
        Assert.assertEquals("failure - expected text attribute match", "testGroup",
                createdEntity.getName());

        Collection<Group> list = service.getAll();

        Assert.assertEquals("failure - expected size", 4, list.size());

    }

    @Test
    public void testCreateWithId() {

        Exception exception = null;

        Group entity = new Group("groupTest2","test group service");
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

        Long id = 4L;

        Group entity = service.getById(id).get();

        Assert.assertNotNull("failure - expected not null", entity);

        String updatedName = entity.getName() + " test";

        entity.setName(updatedName);
        Group updatedEntity = service.update(entity).get();

        Assert.assertNotNull("failure - expected not null", updatedEntity);
        Assert.assertEquals("failure - expected id attribute match", id,
                updatedEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                updatedName, updatedEntity.getName());

    }

    @Test
    public void testUpdateNotFound() {

        Group entity = new Group("testGroup","test");
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

        Long id = 4L;
        Group entity = service.getById(id).get();

        Assert.assertNotNull("failure - expected not null", entity);

        service.remove(id);

        Collection<Group> list = service.getAll();
        Assert.assertEquals("failure - expected size", 2, list.size());
        Group deletedEntity = service.getById(id).get();
        Assert.assertNull("failure - expected null", deletedEntity);
    }

}