package com.sd.his.repository;


import com.sd.his.model.Cashier;
import com.sd.his.model.Nurse;
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
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    @Query("SELECT new com.sd.his.wrapper.response.StaffWrapper(du.id,nr.id,du.username,du.userType,nr.firstName,nr.lastName,nr.email,br.name) FROM Nurse nr INNER JOIN nr.user du INNER JOIN nr.branchNurses branchNr INNER JOIN branchNr.branch  br  WHERE du.active = TRUE AND branchNr.primaryBranch=TRUE")
    List<StaffWrapper> findAllByActive(Pageable pageable);

    @Query("SELECT new com.sd.his.wrapper.response.StaffResponseWrapper(du.id,nr.id,du.userType,nr.firstName,nr.lastName,du.username,nr.email,br.name,nr.homePhone,nr.cellPhone,du.active,br.id,nr.accountExpiry,nr.managePatientRecords,nr.managePatientInvoices) FROM Nurse nr INNER JOIN nr.user du INNER JOIN nr.branchNurses branchCr INNER JOIN branchCr.branch br WHERE nr.id =:id AND du.active = TRUE AND branchCr.primaryBranch=true ")
    StaffResponseWrapper findAllByIdAndStatusActive(@Param("id") Long id);

    Nurse findByUser(User user);

}
