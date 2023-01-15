package com.hozan.platform.service;

import com.hozan.platform.model.Role;

public interface RoleService {
    Role getByCode(String code);
}
