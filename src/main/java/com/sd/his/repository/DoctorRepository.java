package com.sd.his.repository;


import com.sd.his.model.Doctor;
import com.sd.his.model.User;
import com.sd.his.wrapper.response.StaffResponseWrapper;
import com.sd.his.wrapper.response.StaffWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/*
 * @author    : waqas kamran
 * @Date      : 17-Apr-18
 * @version   : ver. 1.0.0
 *
 * ________________________________________________________________________________________________
 *
 *  Developer				Date		     Version		Operation		Description
 * ________________________________________________________________________________________________
 *
 *
 * ________________________________________________________________________________________________
 *
 * @Project   : HIS
 * @Package   : com.sd.his.*
 * @FileName  : UserAuthAPI
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    //Long uId, Long pId, String username, UserTypeEnum userType , String firstName, String lastName, String email, String primaryBranch

    @Query("SELECT new com.sd.his.wrapper.response.StaffWrapper(du.id,dr.id,du.username,du.userType,dr.firstName,dr.lastName,dr.email,br.name) FROM Doctor dr INNER JOIN dr.user du INNER JOIN dr.branchDoctors branchDr INNER JOIN branchDr.branch  br  WHERE du.active = TRUE AND branchDr.primaryBranch=TRUE")
    List<StaffWrapper> findAllByActive(Pageable pageable);

    @Query("SELECT new com.sd.his.wrapper.response.StaffResponseWrapper(du.id,dr.id,du.userType,dr.firstName,dr.lastName,du.username,dr.email,br.name,dr.homePhone,dr.cellPhone,du.active,br.id,dr.accountExpiry," +
            "dr.checkUpInterval,dr.vacation,dr.vacationFrom,dr.vacationTO,dr) FROM Doctor dr INNER JOIN dr.user du INNER JOIN dr.branchDoctors branchCr INNER JOIN branchCr.branch br WHERE dr.id =:id AND du.active = TRUE AND branchCr.primaryBranch=TRUE ")
    StaffResponseWrapper findAllByIdAndStatusActive(@Param("id") Long id);

    Doctor findByUser(User user);
    List<Doctor> findAllByUserIn(List<User> ids);

    List<Doctor> findAllByIdIn(List<Long> ids);
}

