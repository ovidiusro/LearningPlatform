package com.hozan.univer.web.api;


import com.hozan.univer.exception.InternalException;
import com.hozan.univer.model.Account;
import com.hozan.univer.model.File;
import com.hozan.univer.model.Group;
import com.hozan.univer.payload.TokenResponse;
import com.hozan.univer.security.UserAuthenticationService;
import com.hozan.univer.service.AccountService;
import com.hozan.univer.service.FileContentService;
import com.hozan.univer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/account")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController extends BaseController{

    private AccountService accountService;
    private UserAuthenticationService userAuthenticationService;
    private FileService fileService;
    private FileContentService fileContentService;

    @Autowired
    public AccountController(AccountService accountService,
                             UserAuthenticationService userAuthenticationService,
                             FileService fileService,
                             FileContentService fileContentService) {

        this.accountService = accountService;
        this.userAuthenticationService = userAuthenticationService;
        this.fileService = fileService;
        this.fileContentService = fileContentService;
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Account> updateAccount(@RequestBody Account bodyAccount, @PathVariable(value = "id") Long id ){
        logger.info("< updateAccount: {}", id);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();


        Optional<Account> account = accountService.getById(id);
        if(!account.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(!bodyAccount.getName().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateName.class);
            if(constraintViolations.isEmpty()){
                account.get().setName(bodyAccount.getName());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        if(!bodyAccount.getUsername().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateUsername.class);
            if(constraintViolations.isEmpty()){
                account.get().setUsername(bodyAccount.getUsername());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        if(!bodyAccount.getPassword().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidatePassword.class);
            if(constraintViolations.isEmpty()){
                account.get().setPassword(bodyAccount.getPassword());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        if(!bodyAccount.getEmail().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateEmail.class);
            if(constraintViolations.isEmpty()){
                account.get().setEmail(bodyAccount.getEmail());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        if(!bodyAccount.getProfession().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateProfession.class);
            if(constraintViolations.isEmpty()){
                account.get().setProfession(bodyAccount.getProfession());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyAccount.getAge().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateAge.class);
            if(constraintViolations.isEmpty()){
                account.get().setAge(bodyAccount.getAge());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyAccount.getTeachAt().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateTeachAt.class);
            if(constraintViolations.isEmpty()){
                account.get().setTeachAt(bodyAccount.getTeachAt());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyAccount.getStudyAt().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateStudyAt.class);
            if(constraintViolations.isEmpty()){
                account.get().setStudyAt(bodyAccount.getStudyAt());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyAccount.getDescription().isEmpty()){
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(bodyAccount, Account.ValidateDescription.class);
            if(constraintViolations.isEmpty()){
                account.get().setDescription(bodyAccount.getDescription());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        Optional<Account> updatedAccount = accountService.update(account.get());

        logger.info("> updateAccount: {}", id);
        return new  ResponseEntity<Account>(updatedAccount.get(),HttpStatus.OK);
    }

    @GetMapping(value = "/{accountId}/ownGroups", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOwnGroups(@PathVariable("accountId")Long accountId){
        logger.info("< getOwnGroups accountId:{}", accountId);

        Optional<Account> account = accountService.getById(accountId);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Group> groups = account.get().getOwnGroups();

        logger.info("> getOwnGroups accountId:{}", accountId);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(value = "/{accountId}/followGroups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFollowGroups(@PathVariable("accountId")Long accountId){
        logger.info("< getFollowGroups accountId:{}", accountId);

        Optional<Account> account = accountService.getById(accountId);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Group> groups = account.get().getFollowingGroups();

        logger.info("> getFollowGroups accountId:{}", accountId);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(value = "/avatar/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAvatar(@PathVariable("userId")Long accountId){
        logger.info("< getAvatar accountId:{}", accountId);

        Optional<Account> account = accountService.getById(accountId);

        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<File> file = fileService.getById(account.get().getAvatar().getId());
        if (!file.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<InputStream> inputStream = fileContentService.getFileContent(file.get());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.get().getContentLength());
        headers.set("Content-Type", file.get().getMimeType());
        logger.info("> getAvatar accountId:{}", accountId);
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/avatar")
    public ResponseEntity<Group> setAvatar(@RequestParam("accountId")Long accountId, @RequestParam("fileId")Long fileId){
        logger.info("< setAvatar  accountId:{} fileId id:{}", accountId, fileId);

        Optional<Account> account = accountService.getById(accountId);
        Optional<File> avatar = fileService.getById(fileId);
        if(!account.isPresent()){
            throw new EntityNotFoundException("Avatar with this ID don't exist.");
        }
        if(!avatar.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }
        account.get().setAvatar(avatar.get());
        accountService.update(account.get());

        logger.info("> setAccount accountId:{} fileId:{}", accountId, fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "check-token")
    public ResponseEntity<Void> checkToken(){
        logger.info("<> checkToken ");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping (value = "/sign-in",  produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TokenResponse> signIn(@Validated(Account.SignInForm.class) @RequestBody  Account account) {
        logger.info("< sign-in ");

         String tokenStr  = userAuthenticationService.login(account.getUsername(),account.getPassword());

        if(tokenStr.isEmpty()){
            throw new BadCredentialsException("Invalid credentials.");
        }
        TokenResponse tokenResponse = new TokenResponse(tokenStr);

        logger.info("< sign-in ");
        return new ResponseEntity<TokenResponse>(tokenResponse,HttpStatus.OK);
    }

    @GetMapping(value = "/sign-out", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> signOut(Principal principal) {
        logger.info("< sign-out ");

        Optional<Account> account = accountService.getByUsername(principal.getName());

        if(!account.isPresent()){
           new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        userAuthenticationService.logout(account.get());

        logger.info("> sign-out ");
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Principal> getUser(Principal principal) {
        logger.info("<> getUser ");

        return new ResponseEntity<>(principal,HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Collection<Account>> getAllAccounts(){
        logger.info("< getAll ");

        Optional<Collection<Account>> accounts = accountService.getAll();

        if(!accounts.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("> getAll ");
        return new  ResponseEntity<>(accounts.get(),HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Account> getById(@PathVariable(value = "id") @NotNull Long id){
        logger.info("< getById id:{}", id);

        Optional<Account> account = accountService.getById(id);

        if(!account.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getById id:{}", id);
        return new  ResponseEntity<Account>(account.get(),HttpStatus.OK);
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Account> getByUsername(@PathVariable(value = "username") String username){
        logger.info("< getByUsername username:{}", username);

        Optional<Account> account = accountService.getByUsername(username);

        if(!account.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getByUsername username:{}", username);
        return new  ResponseEntity<>(account.get(),HttpStatus.OK);
    }
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Account> getByEmail(@PathVariable(value = "email") @Email String email){
        logger.info("< getByUsername email:{}", email);

        Optional<Account> account = accountService.getByEmail(email);

        if(!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("> getByUsername email:{}", email);
        return new  ResponseEntity<>(account.get(),HttpStatus.OK);
    }

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody @Validated(Account.SignUpForm.class) Account account){
        logger.info("< createAccount ");

        Optional<Account> createdAccount = accountService.create(account);

        if(!createdAccount.isPresent()){
           throw new InternalException("The user was not created.");
        }

        logger.info("> createAccount ");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}" )
    public ResponseEntity<Account> deleteAccount(@PathVariable("id") @NotNull Long id){
        logger.info("< deleteAccount id:{} ", id);

        accountService.remove(id);

        logger.info("> deleteAccount id:{}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}