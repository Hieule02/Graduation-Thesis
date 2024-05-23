package com.DoAn.ShoeShop.repository;

import com.DoAn.ShoeShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "from User u where u.isDeleted = false and u.email = :email")
    Optional<User> findByEmail(String email);
    @Query(value = "from User u where u.isDeleted = false ")
    List<User> findAllNotDeleted();
    @Query(value = "select count(1) from User u where u.isDeleted = false")
    int countCustomer();
}
