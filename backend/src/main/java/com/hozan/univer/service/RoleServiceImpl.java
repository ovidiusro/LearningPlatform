package com.hozan.univer.service;

import com.hozan.univer.model.Role;
import com.hozan.univer.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation =  Propagation.SUPPORTS, readOnly=true)
public class RoleServiceImpl implements RoleService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RoleRepository roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role getByCode(String code) {
        logger.info("> findRoleByCode code:{}", code);

         Role role = roleRepo.findByCode(code);

        logger.info("< findRoleByCode code:{}", code);
         return role;
    }
}
