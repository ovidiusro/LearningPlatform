package com.hozan.univer.web;

import com.hozan.univer.AbstractControllerTest;
import com.hozan.univer.model.Account;
import com.hozan.univer.service.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
public class AccountControllerTest extends AbstractControllerTest {

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        super.setUp();
        accountService.evictCache();
    }

    @Test
    public void testGetAllAccounts() throws Exception {

        String uri = "/api/account/all";

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
    public void testGetAccount() throws Exception {

        String uri = "/api/account/{id}";
        Long id = 7L;

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
    public void testGetAccountNotFound() throws Exception {

        String uri = "/api/account/{id}";
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
    public void testCreateAccount() throws Exception {

        String uri = "/api/account";
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());

        Account user = new Account("test","test","test","test", date);
        String inputJson = super.mapToJson(user);

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

        Account createdAccount = super.mapFromJson(content, Account.class);

        Assert.assertNotNull("failure - expected user not null",
                createdAccount);
        Assert.assertNotNull("failure - expected user.id not null",
                createdAccount.getId());
        Assert.assertEquals("failure - expected greeting.text match", "test",
                createdAccount.getUsername());

    }

    @Test
    public void testUpdateAccount() throws Exception {

        String uri = "/api/account";
        Long id = 7L;
        Account account = accountService.getById(id).get();

        String updatedUsername = account.getUsername() + " test";
        account.setUsername(updatedUsername);

        String inputJson = super.mapToJson(account);
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

        Account updatedAccount = super.mapFromJson(content, Account.class);

        Assert.assertNotNull("failure - expected user not null",
                updatedAccount);
        Assert.assertEquals("failure - expected user.id unchanged",
                account.getId(), updatedAccount.getId());
        Assert.assertEquals("failure - expected updated user username match",
                account.getUsername(), updatedAccount.getUsername());
    }

    @Test
    public void testDeleteAccount() throws Exception {

        String uri = "/api/account/{id}";
        Long id = 7L;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

        Account deletedAccount = accountService.getById(id).get();

        Assert.assertNull("failure - expected user to be null",
                deletedAccount);

    }

}
