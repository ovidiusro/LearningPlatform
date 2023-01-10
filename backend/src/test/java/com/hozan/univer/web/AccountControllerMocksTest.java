package com.hozan.univer.web;

import com.hozan.univer.AbstractControllerTest;
import com.hozan.univer.model.Account;
import com.hozan.univer.model.Role;
import com.hozan.univer.service.AccountService;
import com.hozan.univer.web.api.AccountController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GreetingController using Mockito mocks and spies.
 *
 * These tests utilize the Mockito framework objects to simulate interaction
 * with back-end components. The web methods are invoked directly
 * bypassing the Spring MVC mappings. Back-end components are mocked and
 * injected into the web. Mockito spies and verifications are performed
 * ensuring web behaviors.
 *
 */
public class AccountControllerMocksTest extends AbstractControllerTest{

    @Mock
    private AccountService accountService;


    /**
     * A UserController instance with <code>@Mock</code> components injected
     * into it.
     */
    @InjectMocks
    private AccountController accountController;

    /**
     * Setup each test method. Initialize Mockito mock and spy objects. Scan for
     * Mockito annotations.
     */
    @Before
    public void setUp() {
        // Initialize Mockito annotated components
        MockitoAnnotations.initMocks(this);
        // Prepare the Spring MVC Mock components for standalone testing
        setUp(accountController);
    }

    @Test
    public void testGetAllUsers() throws Exception {

        // Create some test data
        Collection<Account> list = getEntityListStubData();

        // Stub the userService.getAll() method return value
        when(accountService.getAll()).thenReturn(Optional.of(list));

        // Perform the behavior being tested
        String uri = "/api/account/all";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the userrService.getAll method was invoked once
        verify(accountService, times(1)).getAll();

        // Perform standard JUnit assertions on the response
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

    }

    @Test
    public void testGetUser() throws Exception {

        Long id = 5L;
        Account entity = getEntityStubData();

        when(accountService.getById(id)).thenReturn(Optional.of(entity));

        // Perform the behavior being tested
        String uri = "/api/account/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the userService.getById() method was invoked once
        verify(accountService, times(1)).getById(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);
    }

    @Test
    public void testGetUserNotFound() throws Exception {

        Long id = Long.MAX_VALUE;

        when(accountService.getById(id)).thenReturn(null);

        String uri = "/api/account/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(accountService, times(1)).getById(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 404", 404, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    @Test
    public void testCreateUser() throws Exception {

        Account entity = getEntityStubData();
        when(accountService.create(any(Account.class))).thenReturn(Optional.of(entity));

        // Perform the behavior being tested
        String uri = "/api/account";
        String inputJson = super.mapToJson(entity);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(accountService, times(1)).create(any(Account.class));

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 201", 201, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Account createdEntity = super.mapFromJson(content, Account.class);

        Assert.assertNotNull("failure - expected model not null",
                createdEntity);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                entity.getUsername(), createdEntity.getUsername());
    }

    @Test
    public void testUpdateUser() throws Exception {

        Account entity = getEntityStubData();
        entity.setUsername(entity.getUsername() + " test");

        when(accountService.update(any(Account.class))).thenReturn(Optional.of(entity));

        // Perform the behavior being tested
        String uri = "/api/account";
        String inputJson = super.mapToJson(entity);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();


        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(accountService, times(1)).update(any(Account.class));


        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Account updatedEntity = super.mapFromJson(content, Account.class);

        Assert.assertNotNull("failure - expected model not null",
                updatedEntity);
        Assert.assertEquals("failure - expected id attribute unchanged",
                entity.getId(), updatedEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                entity.getUsername(), updatedEntity.getUsername());

    }

    @Test
    public void testDeleteUser() throws Exception {

        Long id = 1L;
        String uri = "/api/account/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(accountService, times(1)).remove(id);

        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }


    private Collection<Account> getEntityListStubData() {
        Role sysAdminRole = new Role("ROLE_SYSADMIN", "System Admin");
        Role adminRole = new Role("ROLE_ADMIN", "Admin");
        Role userRole  = new Role("ROLE_USER", "User");

        Account account;
        Collection<Account> accounts = new ArrayList<>();
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());

        account =  new Account(1L,"sysadmin","psysadmin",date );
        account.setRoles(Arrays.asList(sysAdminRole,adminRole,userRole));
        accounts.add(account);

        account =  new Account(2L,"admin","padmin",date );
        account.setRoles(Arrays.asList(adminRole,userRole));
        accounts.add(account);

        account =  new Account(3L,"user","puser",date );
        account.setRoles(Arrays.asList(userRole));
        accounts.add(account);

        return accounts;
    }

    private Account getEntityStubData() {
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());
        Role userRole  = new Role("ROLE_USER", "User");

        Account account =  new Account(1L,"test","ptest",date );
        account.setRoles(Arrays.asList(userRole));

        return account;
    }
}
