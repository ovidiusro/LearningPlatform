package com.hozan.univer.util;


/*
@Component
public class DataBaseSeeder implements CommandLineRunner {



    private final AccountRepository accountRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepo;
    private final FileRepository fileRepo;

    @Autowired
    public DataBaseSeeder(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, GroupRepository groupRepo, FileRepository fileRepo) {
        this.accountRepo = accountRepository;
        this.roleRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepo = groupRepo;
        this.fileRepo = fileRepo;
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        deleteAll();

        File file = new File();
        file.setName("file1");
        file.setSummary("summary1");


        Role sysAdminRole = new  Role("ROLE_SYSADMIN", "System Admin");
        Role adminRole = new Role("ROLE_ADMIN", "Admin");
        Role userRole  = new Role("ROLE_USER", "User");

        Group javaGroup = new Group("javaGroup","group for java dev");
        Group phpGroup = new Group("phpGroup","group for php dev");
        Group pythonGroup = new Group("pythoneroup","group for python dev");

        Account account;
        Collection<Account> accounts = new ArrayList<>();
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant());

        account =  new Account("ovidiu","hozan.ovidiu@gmail.com","sysadmin",passwordEncoder.encode("sysadmin"),date );
        account.setRoles(Arrays.asList(sysAdminRole,adminRole,userRole));
        account.setOwnGroups(Arrays.asList(javaGroup, phpGroup));
        account.startFollowingGroup(pythonGroup);
        accounts.add(account);

        account =  new Account("dan","hozan.dan@gmail.com","admin",passwordEncoder.encode("admin"),date );
        account.setRoles(Arrays.asList(adminRole,userRole));
        account.addOwnGroup(pythonGroup);
        account.setFollowingGroups(Arrays.asList(javaGroup,phpGroup));
        accounts.add(account);

        account =  new Account("beni","hozan.beni@gmail.com","user",passwordEncoder.encode("user"),date );
        account.setRoles(Arrays.asList(userRole));
        account.setFollowingGroups(Arrays.asList(javaGroup,phpGroup,pythonGroup));
        accounts.add(account);

        fileRepo.save(file);
        groupRepo.saveAll(Arrays.asList(javaGroup,phpGroup,pythonGroup));
        roleRepo.saveAll(Arrays.asList(adminRole,sysAdminRole,userRole));
        accountRepo.saveAll(accounts);



    }
     void deleteAll(){
        accountRepo.deleteAll();
        roleRepo.deleteAll();
        groupRepo.deleteAll();
     }

    Role createRoleIfNotFound(String code, String label) {

         Role role = new Role(code,label);
        return role;
    }

    Group createGroupIfNotFound(String name, String description) {
           Group group = new Group(name,description);
        return group;
    }

}
*/