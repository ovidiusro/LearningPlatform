package com.hozan.platform.repository;

import com.hozan.platform.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long>{
    Optional<Group> findByName(String name);
    Optional<Group> findGroupById(Long ID);

}
