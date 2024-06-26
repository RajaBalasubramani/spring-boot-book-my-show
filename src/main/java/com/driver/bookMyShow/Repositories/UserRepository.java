package com.driver.bookMyShow.Repositories;

import com.driver.bookMyShow.Models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Integer> {

    AuthUser findByEmailId(String emailId);;
}
