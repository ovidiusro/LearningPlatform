/*
package com.hozan.platform.util;

import com.hozan.platform.model.Account;
import com.hozan.platform.model.Role;
import com.hozan.platform.repository.AccountRepository;
import com.hozan.platform.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class DataBaseSeeder implements CommandLineRunner {


    private final AccountRepository accountRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataBaseSeeder(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepo = accountRepository;
        this.roleRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {

        Role sysAdminRole = new  Role("ROLE_SYSADMIN", "System Admin");
        Role adminRole = new Role("ROLE_ADMIN", "Admin");
        Role userRole  = new Role("ROLE_USER", "User");

        Account account;
        Collection<Account> accounts = new ArrayList<>();
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());

        account =  new Account("","","sysadmin",passwordEncoder.encode("psysadmin"),date );
        account.setRoles(Arrays.asList(sysAdminRole,adminRole,userRole));
        accounts.add(account);

        account =  new Account("","","admin",passwordEncoder.encode("padmin"),date );
        account.setRoles(Arrays.asList(adminRole,userRole));
        accounts.add(account);

        account =  new Account("","","user",passwordEncoder.encode("puser"),date );
        account.setRoles(Arrays.asList(userRole));
        accounts.add(account);

        roleRepo.saveAll(Arrays.asList(adminRole,sysAdminRole,userRole));
        accountRepo.saveAll(accounts);

    }
}
*/