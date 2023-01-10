package com.hozan.univer.repository;

import com.hozan.univer.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long>{
    Optional<Group> findByName(String name);
    Optional<Group> findGroupById(Long ID);

}
