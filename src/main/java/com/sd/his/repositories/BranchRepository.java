package com.sd.his.repositories;

import com.sd.his.model.Branch;
import com.sd.his.response.BranchResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Branch findByName(String name);

    Branch findById(long id);

    @Query("SELECT new com.sd.his.response.BranchResponseWrapper(b.id,b.name, b.country,b.city,b.noOfRooms,pu.username) FROM Branch b INNER JOIN b.users bu JOIN bu.user pu WHERE b.active = TRUE ")
    List<BranchResponseWrapper> findAllByNameAndActiveTrue(Pageable pageable);

    List<Branch> findByNameIgnoreCaseContainingAndActiveTrueOrClinicalDepartments_clinicalDpt_nameIgnoreCaseContaining(String name, String department, Pageable pageable);

    List<Branch> findAllByActiveTrueOrderByNameAsc(Pageable pageable);

    List<Branch> findAllByActiveTrue();

    @Query("SELECT new com.sd.his.response.BranchResponseWrapper(b.id,b.name, b.country,b.city,b.noOfRooms) FROM Branch b WHERE b.active = TRUE ")
    List<BranchResponseWrapper> findBranchWrapperAllByActiveTrue();

    List<Branch> findAllByIdIn(List<Long> ids);

}

