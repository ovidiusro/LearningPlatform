package com.hozan.univer.web;


import com.hozan.univer.AbstractControllerTest;
import com.hozan.univer.model.Group;
import com.hozan.univer.service.GroupService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for the AccountController using Spring MVC Mocks.
 *
 * These tests utilize the Spring MVC Mock objects to simulate sending actual
 * HTTP requests to the Controller component. This test ensures that the
 * RequestMappings are configured correctly. Also, these tests ensure that the
 * request and response bodies are serialized as expected.
 *
 */
@Transactional
public class GroupControllerTest extends AbstractControllerTest {

    @Autowired
    private GroupService groupService;

    @Before
    public void setUp() {
        super.setUp();
        groupService.evictCache();
    }

    @Test
    public void testGetAllGroups() throws Exception {

        String uri = "/api/group/all";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

    }

    @Test
    public void testGetGroup() throws Exception {

        String uri = "/api/group/{id}";
        Long id = 4L;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

    }

    @Test
    public void testGetGroupNotFound() throws Exception {

        String uri = "/api/group/{id}";
        Long id = Long.MAX_VALUE;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 404", 404, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    @Test
    public void testCreateGroup() throws Exception {

        String uri = "/api/group";

        Group group = new Group("test","test");
        String inputJson = super.mapToJson(group);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 201", 201, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Group createdGroup = super.mapFromJson(content, Group.class);

        Assert.assertNotNull("failure - expected user not null",
                createdGroup);
        Assert.assertNotNull("failure - expected user.id not null",
                createdGroup.getId());
        Assert.assertEquals("failure - expected greeting.text match", "test",
                createdGroup.getName());

    }

    @Test
    public void testUpdateGroup() throws Exception {

        String uri = "/api/group";
        Long id = 4L;
        Group group= groupService.getById(id).get();

        String updatedName= group.getName() + " test";
        group.setName(updatedName);

        String inputJson = super.mapToJson(group);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Group updatedGroup = super.mapFromJson(content, Group.class);

        Assert.assertNotNull("failure - expected user not null",
                updatedGroup);
        Assert.assertEquals("failure - expected user.id unchanged",
                group.getId(), updatedGroup.getId());
        Assert.assertEquals("failure - expected updated user username match",
                group.getName(), updatedGroup.getName());
    }

    @Test
    public void testDeleteGroup() throws Exception {

        String uri = "/api/group/{id}";
        Long id = 4L;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

        Group deletedGroup = groupService.getById(id).get();

        Assert.assertNull("failure - expected user to be null",
                deletedGroup);

    }

}
