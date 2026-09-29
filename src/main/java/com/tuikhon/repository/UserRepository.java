package com.tuikhon.repository;

import com.tuikhon.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository interface for {@link UserEntity} persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Finds active (non-deleted) user by email address.
     *
     * @param email The email string to search for.
     * @return Optional containing UserEntity if found.
     */
    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);

    /**
     * Checks if active (non-deleted) user exists by email address.
     *
     * @param email The email string to check.
     * @return true if exists, false otherwise.
     */
    boolean existsByEmailAndIsDeletedFalse(String email);

    /**
     * Finds active (non-deleted) user by user UUID.
     *
     * @param userId The UUID to search for.
     * @return Optional containing UserEntity if found.
     */
    Optional<UserEntity> findByUserIdAndIsDeletedFalse(UUID userId);

    /**
     * Finds active (non-deleted) user by phone number.
     *
     * @param phoneNumber The phone number string to search for.
     * @return Optional containing UserEntity if found.
     */
    Optional<UserEntity> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);
}
