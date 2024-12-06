package com.example.demo.repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    // Method to check if a user with the given email exists using native query
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM users WHERE email = :email", nativeQuery = true)
    boolean existsByEmailNative(@Param("email") String email);

    // Method to find a user by email and password using native query
    @Query(value = "SELECT * FROM users WHERE email = :email AND password = :password", nativeQuery = true)
    User authenticateByEmailNative(@Param("email") String email, @Param("password") String password);
}
