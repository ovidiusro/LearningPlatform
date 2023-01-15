package com.hozan.platform.service;

import com.hozan.platform.model.Account;
import com.hozan.platform.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(propagation =  Propagation.SUPPORTS, readOnly=true)
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepo = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<Collection<Account>> getAll() {
        logger.info("< getAll ");


        Collection<Account> accounts = accountRepo.findAll();

        logger.info("> getAll ");
        return Optional.of(accounts);

    }


    @Override
    @Cacheable(value = "accountCache",unless = "#username != null")
    public Optional<Account> getByUsername(String username) {
        logger.info("< getByUsername");

        Optional<Account> account = accountRepo.findAccountByUsername(username);

        logger.info("> getByUsername");
        return  account;
    }

    @Override
    @Cacheable(value = "accountCache",unless = "#email != null")
    public Optional<Account> getByEmail(String email) {
        logger.info("< getByEmail");

        Optional<Account> account = accountRepo.findAccountByEmail(email);

        logger.info("> getByEmail");
        return account;
    }


    @Override
    @CachePut(value = "accountCache", unless = "#id != null")
    public Optional<Account> getById(Long id) {
        logger.info("< getById id:{} ",id);

        Optional<Account> account = accountRepo.findAccountById(id);

        logger.info("> getById id:{} ",id);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "accountCache", unless = "#result.id != null ", key="#result.id")
    public Optional<Account> create(Account account) {
        logger.info("< create ");

        if (account.getId() != null) {
            logger.error(
                    "Attempted to create a account, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new model.");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account savedAccount = accountRepo.save(account);
        logger.info("> create ");
        return Optional.of(savedAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(value = "accountCache", unless = "#result.id != null ", key="#account.id")
    public Optional<Account> update(Account account) {
        logger.info("< update id:{} ", account.getId());

       Optional<Account> accountToUpdate = accountRepo.findAccountById(account.getId());

        if(!accountToUpdate.isPresent()) {
            logger.error(
                    "Attempted to update a account, but the model does not exist.");
            throw new NoResultException("Requested model not found.");
        }

        accountToUpdate = Optional.of(accountRepo.save(account));

        logger.info("> update id:{} ", account.getId());
        return accountToUpdate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CacheEvict(value = "accountCache",key = "#id")
    public void remove(Long id) {
        logger.info("< remove id:{} ", id);

        accountRepo.deleteById(id);

        logger.info("> remove id:{} ", id);
    }

    @Override
    @CacheEvict(value="accountCache", allEntries = true)
    public void evictCache() {
        logger.info("< evict cache");
        logger.info("> evict cache");
    }

}
