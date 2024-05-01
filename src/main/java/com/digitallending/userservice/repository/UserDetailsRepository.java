package com.digitallending.userservice.repository;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.entity.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Page<UserDetails> findByOnBoardingStatusAndRoleNot(UserOnBoardingStatus status, Role role, Pageable pageable);

    Page<UserDetails> findByRole(Role role, Pageable pageable);

    Page<UserDetails> findByRoleNot(Role role, Pageable pageable);

    List<UserDetails> findAllByRoleAndOnBoardingStatus(Role role, UserOnBoardingStatus userOnBoardingStatus);

    @Query(value = "SELECT ud.role , COUNT(*) from user_details as ud group by ud.role", nativeQuery = true)
    List<Object[]> findRoleTypes();

    // Method to update loanApplicationStatus by loanApplicationId
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_details SET on_boarding_status = :status WHERE user_id = :user_id", nativeQuery = true)
    void setStatusByUserId(@Param("user_id") UUID userId, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE user_details SET email = :email WHERE user_id = :user_id", nativeQuery = true)
    void setEmailByUserId(@Param("user_id") UUID userId, @Param("email") String email);
    boolean existsByUserIdAndRole(UUID userId, Role role);

}
