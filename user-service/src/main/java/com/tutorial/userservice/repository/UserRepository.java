package com.tutorial.userservice.repository;

import com.tutorial.userservice.entity.Userx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Userx,Integer> {
}
