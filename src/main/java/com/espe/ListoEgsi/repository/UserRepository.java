package com.espe.ListoEgsi.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.espe.ListoEgsi.domain.model.entity.setting.User;

import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * find by id User
     */
    User findByIdUser(UUID id);

    /**
     * find by id username
     */
    Optional<User> findByUsername(String username);
    /**
     * find by id (non-deleted)
     */
    @Query(value = "SELECT * FROM users u WHERE u.id_user = :userId AND u.is_deleted = 0", nativeQuery = true)
    Optional<User> findByIdAndNotDeleted(@Param("userId") String userId);

     /**
     * Soft delete user by ID
     */
    @Modifying
    @Query(value = "UPDATE users SET is_deleted = 1 WHERE id_user = :userId", nativeQuery = true)
    void softDeleteById(@Param("answerId") String userId);
    

}
