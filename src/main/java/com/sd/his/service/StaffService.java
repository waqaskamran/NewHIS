package com.sd.his.service;

import com.sd.his.enums.DutyShiftEnum;
import com.sd.his.enums.ModuleEnum;
import com.sd.his.enums.ProfileStatusTypeEnum;

import static com.sd.his.enums.UserTypeEnum.*;

import com.sd.his.enums.UserTypeEnum;
import com.sd.his.model.*;
import com.sd.his.repository.*;
import com.sd.his.utill.HISCoreUtil;
import com.sd.his.wrapper.UserWrapper;
import com.sd.his.wrapper.request.StaffRequestWrapper;
import com.sd.his.wrapper.response.DashboardResponseWrapper;
import com.sd.his.wrapper.response.StaffResponseWrapper;
import com.sd.his.wrapper.response.StaffWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StaffService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    BranchCashierRepository branchCashierRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    CashierRepository cashierRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    HISUtilService hisUtilService;
    @Autowired
    ReceptionistRepository receptionistRepository;
    @Autowired
    BranchReceptionistRepository branchReceptionistRepository;
    @Autowired
    BranchNurseRepository branchNurseRepository;
    @Autowired
    NurseWithDoctorRepository nurseWithDoctorRepository;
    @Autowired
    DutyShiftRepository dutyShiftRepository;
    @Autowired
    BranchDoctorRepository branchDoctorRepository;
    @Autowired
    BranchService branchService;
    @Autowired
    StaffService staffService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    MedicalServiceRepository medicalServiceRepository;
    @Autowired
    DoctorMedicalServiceRepository  doctorMedicalServiceRepository;
    @Autowired
    NurseDepartmentRepository nurseDepartmentRepository;


    List<StaffWrapper> finalStaffList = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Transactional
    public User saveUser(StaffRequestWrapper createRequest) {
        String usertype = createRequest.getUserType();
        Branch branch = branchRepository.findOne(createRequest.getPrimaryBranch());
        //  UserDutyShift userDutyShift = new UserDutyShift();
        UserRole userRole = null;//new UserRole();
        // String brName = branch.getName();
        User user = null;

        if (usertype.equalsIgnoreCase("CASHIER") ) {
            user = new User();
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(createRequest.getPassword()));
            user.setUsername(createRequest.getUserName());
            user.setUserType("CASHIER");
            userRepository.save(user);

            Role role = roleRepository.findByName(createRequest.getUserType().toUpperCase());
            /*List<UserRole> recAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {*/
            if(role!=null) {
                //for (Role userAllowRole : allRoles) {
                userRole = new UserRole();
                    /*userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);*/
                userRole.setRole(role);
                userRole.setUser(user);
                //}
                userRoleRepository.save(userRole);//recAssignedRole);
            }

            /*List<Role> allRoles = roleRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedRoles()));
            List<UserRole> cashAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {
                for (Role userAllowRole : allRoles) {
                    UserRole userRole1 = new UserRole();
                    userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    cashAssignedRole.add(userRole1);
                }
                userRoleRepository.save(cashAssignedRole);
            }*/

            Cashier cashier = new Cashier();
            cashier.setProfileId(hisUtilService.getPrefixId(ModuleEnum.PROFILE));
            cashier.setCellPhone(createRequest.getCellPhone());
            cashier.setHomePhone(createRequest.getHomePhone());
            cashier.setEmail(createRequest.getEmail());
            cashier.setFirstName(createRequest.getFirstName());
            cashier.setLastName(createRequest.getLastName());
            cashier.setStatus(ProfileStatusTypeEnum.ACTIVE);
            cashier.setUser(user);
            cashier.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
            List<Branch> allowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
            List<BranchCashier> cashierVisitBranchesData = new ArrayList<>();
            cashierRepository.save(cashier);
            if (!HISCoreUtil.isListEmpty(allowBranches)) {
                for (Branch userVisitBr : allowBranches) {
                    if(userVisitBr.getId() == branch.getId())
                        continue;
                    BranchCashier userVisitBranches = new BranchCashier();
                    userVisitBranches.setBranch(userVisitBr);
                    userVisitBranches.setCashier(cashier);
                    userVisitBranches.setPrimaryBranch(false);
                    cashierVisitBranchesData.add(userVisitBranches);
                }
                branchCashierRepository.save(cashierVisitBranchesData);
            }

            BranchCashier branchCashier = new BranchCashier();
            branchCashier.setBranch(branch);
            branchCashier.setPrimaryBranch(true);
            branchCashier.setCashier(cashier);
            branchCashierRepository.save(branchCashier);

            // cashier.setBranchCashiers(cashierVisitBranchesData);

            return user;
        }

        if (usertype.equalsIgnoreCase(RECEPTIONIST.name())) {
            user = new User();
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(createRequest.getPassword()));
            user.setUsername(createRequest.getUserName());
            user.setUserType("RECEPTIONIST");
            userRepository.save(user);

            Role role = roleRepository.findByName(createRequest.getUserType().toUpperCase());
            /*List<UserRole> recAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {*/
            if(role!=null) {
                //for (Role userAllowRole : allRoles) {
                userRole = new UserRole();
                    /*userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);*/
                userRole.setRole(role);
                userRole.setUser(user);
                //}
                userRoleRepository.save(userRole);//recAssignedRole);
            }

            /*List<Role> allRoles = roleRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedRoles()));
            List<UserRole> recAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {
                for (Role userAllowRole : allRoles) {
                    UserRole userRole1 = new UserRole();
                    userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);
                }
                userRoleRepository.save(recAssignedRole);
            }*/

            Receptionist receptionist = new Receptionist();
            receptionist.setProfileId(hisUtilService.getPrefixId(ModuleEnum.PROFILE));
            receptionist.setCellPhone(createRequest.getCellPhone());
            receptionist.setHomePhone(createRequest.getHomePhone());
            receptionist.setEmail(createRequest.getEmail());
            receptionist.setStatus(ProfileStatusTypeEnum.ACTIVE);
            receptionist.setFirstName(createRequest.getFirstName());
            receptionist.setLastName(createRequest.getLastName());
            receptionist.setUser(user);
            receptionist.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
            List<Long> listbr = Arrays.asList(createRequest.getSelectedVisitBranches());
            List<Branch> allowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
            List<BranchReceptionist> receptionistVisitBranchesData = new ArrayList<>();
            receptionistRepository.save(receptionist);
            if (!HISCoreUtil.isListEmpty(allowBranches)) {
                for (Branch userVisitBr : allowBranches) {
                    BranchReceptionist userVisitBranches = new BranchReceptionist();
                    userVisitBranches.setBranch(userVisitBr);
                    userVisitBranches.setReceptionist(receptionist);
                    userVisitBranches.setPrimaryBranch(false);
                    receptionistVisitBranchesData.add(userVisitBranches);
                }
                branchReceptionistRepository.save(receptionistVisitBranchesData);
            }

            BranchReceptionist branchReceptionist = new BranchReceptionist();
            branchReceptionist.setBranch(branch);
            branchReceptionist.setPrimaryBranch(true);
            branchReceptionist.setReceptionist(receptionist);
            branchReceptionistRepository.save(branchReceptionist);

            // cashier.setBranchCashiers(cashierVisitBranchesData);
            return user;
        }

        if (usertype.equalsIgnoreCase(NURSE.name())) {
            user = new User();
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(createRequest.getPassword()));
            user.setUsername(createRequest.getUserName());
            user.setUserType("NURSE");
            userRepository.save(user);
            /*List<Role> allRoles = roleRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedRoles()));
            List<UserRole> recAssignedRole = new ArrayList<>();*/
            Role role = roleRepository.findByName(createRequest.getUserType().toUpperCase());
            /*List<UserRole> recAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {*/
            if(role!=null) {
                //for (Role userAllowRole : allRoles) {
                userRole = new UserRole();
                    /*userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);*/
                userRole.setRole(role);
                userRole.setUser(user);
                //}
                userRoleRepository.save(userRole);//recAssignedRole);
            }
            /*if (role!=null) {//!HISCoreUtil.isListEmpty(allRoles)) {
                //for (Role userAllowRole : allRoles) {
                    UserRole userRole1 = new UserRole();
                    userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);
                //}
                userRoleRepository.save(recAssignedRole);
            }*/
            Nurse nurse = new Nurse();
            nurse.setUser(user);
            nurse.setProfileId(hisUtilService.getPrefixId(ModuleEnum.PROFILE));
            nurse.setCellPhone(createRequest.getCellPhone());
            nurse.setHomePhone(createRequest.getHomePhone());
            nurse.setEmail(createRequest.getEmail());
            nurse.setStatus(ProfileStatusTypeEnum.ACTIVE);
            nurse.setFirstName(createRequest.getFirstName());
            nurse.setLastName(createRequest.getLastName());
            nurse.setUser(user);
            nurse.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
            nurse.setManagePatientInvoices(createRequest.isManagePatientInvoices());
            nurse.setManagePatientRecords(createRequest.isManagePatientRecords());

            /*List<ClinicalDepartment> clinicalDepartments = clinicalDepartmentRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedDepartment()));
            List<DepartmentUser> departmentUserListData = new ArrayList<>();
            for (ClinicalDepartment clinicalDepartment : clinicalDepartments) {
                DepartmentUser departmentUser = new DepartmentUser();
                departmentUser.setClinicalDepartment(clinicalDepartment);
                departmentUser.setUser(user);
                departmentUser.setCreatedOn(System.currentTimeMillis());
                departmentUser.setUpdatedOn(System.currentTimeMillis());
                departmentUser.setDeleted(false);
                departmentUserListData.add(departmentUser);
            }
            departmentUserRepository.save(departmentUserListData);
*/
            List<Branch> allowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
            List<BranchNurse> nurseVisitBranchesData = new ArrayList<>();

            nurseRepository.save(nurse);

            //nurse departments portion
            List<Department> selectedDept = departmentRepository.findAllByIdIn( Arrays.asList(createRequest.getSelectedDepartment()) );
            List<NurseDepartment> nurseDepartmentList = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(selectedDept)) {
                for (Department dept : selectedDept) {
                    NurseDepartment nurseDept = new NurseDepartment();
                    nurseDept.setDepartment(dept);
                    nurseDept.setNurse(nurse);
                    nurseDepartmentList.add(nurseDept);
                }

            }
            nurseDepartmentRepository.save(nurseDepartmentList);
            //branch nurse portion
            BranchNurse branchNurse = new BranchNurse();
            branchNurse.setBranch(branch);
            branchNurse.setPrimaryBranch(true);
            branchNurse.setNurse(nurse);
            branchNurseRepository.save(branchNurse);
            if (!HISCoreUtil.isListEmpty(allowBranches)) {
                for (Branch userVisitBr : allowBranches) {
                    if (userVisitBr.getId() == branch.getId())
                        continue;
                    BranchNurse userVisitBranches = new BranchNurse();
                    userVisitBranches.setBranch(userVisitBr);
                    userVisitBranches.setNurse(nurse);
                    userVisitBranches.setPrimaryBranch(false);
                    nurseVisitBranchesData.add(userVisitBranches);
                }
                branchNurseRepository.save(nurseVisitBranchesData);
            }
            List<User> doctorsList = userRepository.findAllByIdIn(Arrays.asList(createRequest.getDutyWithDoctors()));
            List<Doctor> doctors = doctorRepository.findAllByUserIn(doctorsList);
            List<NurseWithDoctor> dutyWithDoctorsData = new ArrayList<>();
            for (Doctor docUser : doctors) {
                NurseWithDoctor dutyWithDoctor1 = new NurseWithDoctor();
                dutyWithDoctor1.setNurse(nurse);
                dutyWithDoctor1.setDoctor(docUser);
                dutyWithDoctorsData.add(dutyWithDoctor1);
            }
            nurseWithDoctorRepository.save(dutyWithDoctorsData);
            return user;
        }

        if (usertype.equalsIgnoreCase(DOCTOR.name())) {

            user = new User();
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(createRequest.getPassword()));
            user.setUsername(createRequest.getUserName());
            user.setUserType("DOCTOR");
            userRepository.save(user);
            //commenting becz roles are removed from staff creation page
            //List<Role> allRoles = roleRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedRoles()));
            Role role = roleRepository.findByName(createRequest.getUserType().toUpperCase());
            /*List<UserRole> recAssignedRole = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allRoles)) {*/
            if(role!=null) {
                //for (Role userAllowRole : allRoles) {
                    userRole = new UserRole();
                    /*userRole1.setRole(userAllowRole);
                    userRole1.setUser(user);
                    recAssignedRole.add(userRole1);*/
                    userRole.setRole(role);
                    userRole.setUser(user);
                //}
                userRoleRepository.save(userRole);//recAssignedRole);
            }

            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setFirstName(createRequest.getFirstName());
            doctor.setLastName(createRequest.getLastName());
            doctor.setProfileId(hisUtilService.getPrefixId(ModuleEnum.PROFILE));
            doctor.setCellPhone(createRequest.getCellPhone());
            doctor.setHomePhone(createRequest.getHomePhone());
            doctor.setCheckUpInterval(createRequest.getInterval());
            doctor.setEmail(createRequest.getEmail());
            doctor.setStatus(ProfileStatusTypeEnum.ACTIVE);
            doctor.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
            doctor.setVacation(createRequest.isVacation());
            doctor.setVacationFrom(HISCoreUtil.convertToDate(createRequest.getDateFrom()));
            doctor.setVacationTO(HISCoreUtil.convertToDate(createRequest.getDateTo()));
            List<String> daysList = Arrays.asList(createRequest.getSelectedWorkingDays());
            doctor.setWorkingDays(daysList);

            //doctor department portion
            Department selectedDocDept = null;
            if(createRequest.getSelectedDepartment().length>0) {
                selectedDocDept = departmentRepository.findOne((createRequest.getSelectedDepartment())[0]);
            }
            doctor.setDepartment(selectedDocDept);
            doctorRepository.save(doctor);
//            profile.setWorkingDays(daysList);
            //duty shift portion
            DutyShift dutyShift = new DutyShift();
            dutyShift.setShiftName(DutyShiftEnum.SHIFT1);
            dutyShift.setStartTime(HISCoreUtil.convertToTime(createRequest.getFirstShiftFromTime()));
            dutyShift.setEndTime(HISCoreUtil.convertToTime(createRequest.getFirstShiftToTime()));
            dutyShift.setDoctor(doctor);
            dutyShiftRepository.save(dutyShift);

            if (!HISCoreUtil.isNull(createRequest.getSecondShiftFromTime())) {
                DutyShift dutyShift2 = new DutyShift();
                dutyShift2.setShiftName(DutyShiftEnum.SHIFT2);
                dutyShift2.setStartTime(HISCoreUtil.convertToTime(createRequest.getSecondShiftFromTime()));
                dutyShift2.setEndTime(HISCoreUtil.convertToTime(createRequest.getSecondShiftToTime()));
                dutyShift2.setDoctor(doctor);
                dutyShiftRepository.save(dutyShift2);
            }

            List<Branch> allowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
            List<BranchDoctor> nurseVisitBranchesData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(allowBranches)) {
                for (Branch userVisitBr : allowBranches) {
                    if (userVisitBr.getId() == branch.getId())
                        continue;
                    BranchDoctor userVisitBranches = new BranchDoctor();
                    userVisitBranches.setBranch(userVisitBr);
                    userVisitBranches.setDoctor(doctor);
                    userVisitBranches.setPrimaryBranch(false);
                    nurseVisitBranchesData.add(userVisitBranches);
                }
                branchDoctorRepository.save(nurseVisitBranchesData);
            }

            //doctor services portion

            List<MedicalService> medicalServicesList =  medicalServiceRepository.findAllByIdIn( Arrays.asList(createRequest.getSelectedServices()) );
            List<DoctorMedicalService> doctorMedicalServiceList = new ArrayList<>();
            for(MedicalService ms : medicalServicesList){
                DoctorMedicalService dms = new DoctorMedicalService();
                dms.setDoctor(doctor);
                dms.setMedicalService(ms);
                doctorMedicalServiceList.add(dms);
            }
            doctorMedicalServiceRepository.save(doctorMedicalServiceList);

            //branch doctor portion
            BranchDoctor branchDoctor = new BranchDoctor();
            branchDoctor.setBranch(branch);
            branchDoctor.setPrimaryBranch(true);
            branchDoctor.setDoctor(doctor);
            branchDoctorRepository.save(branchDoctor);
            /*List<MedicalService> medicalServiceslist = medicalServicesRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedServices()));
            List<UserMedicalService> userDetailsServicesData = new ArrayList<>();
            for (MedicalService mdService : medicalServiceslist) {
                UserMedicalService userMedicalService = new UserMedicalService();
                userMedicalService.setMedicalService(mdService);
                userMedicalService.setUser(user);
                userDetailsServicesData.add(userMedicalService);
            }
            userMedicalServiceRepository.save(userDetailsServicesData);
                */

            /*List<ClinicalDepartment> clinicalDepartments = clinicalDepartmentRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedDepartment()));
            List<DepartmentUser> docDepartmentUser = new ArrayList<>();
            for (ClinicalDepartment clinicalDepartment : clinicalDepartments) {
                DepartmentUser departmentUser = new DepartmentUser();
                departmentUser.setClinicalDepartment(clinicalDepartment);
                departmentUser.setUser(user);
                departmentUser.setCreatedOn(System.currentTimeMillis());
                departmentUser.setUpdatedOn(System.currentTimeMillis());
                departmentUser.setDeleted(false);
                docDepartmentUser.add(departmentUser);
            }
            departmentUserRepository.save(docDepartmentUser);
*/
            return user;
        }
        return null;
    }

    public User findById(long id) {
        return userRepository.findOne(id);
    }

    public StaffResponseWrapper findByIdAndResponse(long id, String userType) {
        StaffResponseWrapper staffResponseWrapper = null;
        if (userType.equalsIgnoreCase("CASHIER")) {
            staffResponseWrapper = cashierRepository.findAllByIdAndStatusActive(id);
            staffResponseWrapper.setStaffBranches(branchCashierRepository.getCashierBranches(id));
            return staffResponseWrapper;//cashierRepository.findAllByIdAndStatusActive(id);
        }
        if (userType.equalsIgnoreCase("RECEPTIONIST")) {
            //return cashierRepository.findAllByIdAndStatusActive(id);
            staffResponseWrapper = receptionistRepository.findAllByIdAndStatusActive(id);
            staffResponseWrapper.setStaffBranches(branchReceptionistRepository.getReceptionistBranches(id));
            return staffResponseWrapper;//cashierRepository.findAllByIdAndStatusActive(id);
        }
        if (userType.equalsIgnoreCase("DOCTOR")) {
            staffResponseWrapper = doctorRepository.findAllByIdAndStatusActive(id);
            staffResponseWrapper.setStaffBranches( branchDoctorRepository.getDoctorBranches(id) );
            staffResponseWrapper.setDoctorMedicalSrvcList(doctorMedicalServiceRepository.getDoctorMedicalServices(id));
            return staffResponseWrapper;
        }
        if (userType.equalsIgnoreCase("NURSE")) {
            staffResponseWrapper = nurseRepository.findAllByIdAndStatusActive(id);
            Nurse nurse = nurseRepository.findOne(id);
            //branchService.getAllActiveBranches();
            staffResponseWrapper.setStaffBranches( branchNurseRepository.getNurseBranches(id) );
            staffResponseWrapper.setNurseDepartmentList( nurseDepartmentRepository.getNurseDepartments(id) );
            //staffService.findByRole("DOCTOR");
            staffResponseWrapper.setDutyWithDoctors(nurseWithDoctorRepository.findNurseWithDoctors(id));

            return staffResponseWrapper;
        }

      /* User user = userRepository.findAllById(id);
        String userType = user.getUserType();
        UserResponseWrapper userResponseWrapper = new UserResponseWrapper(user);

        userResponseWrapper.setProfile(user.getProfile());
        BranchUser branchUser = branchUserRepository.findByUser(user);
       //  if (branchUser.isPrimaryBranch()) {
        BranchWrapper branchWrapper = new BranchWrapper(branchUser);
        userResponseWrapper.setBranch(branchWrapper);
        //}
        List<Branch> visitBranchesList = new ArrayList<>();
        for (UserVisitBranches userVisitBranches : user.getUserVisitBranches()) {
            visitBranchesList.add(userVisitBranches.getBranch());
            userResponseWrapper.setVisitBranches(visitBranchesList);
        }
        if (userType.equalsIgnoreCase("CASHIER")) {

        }

        *//*if (userType.equalsIgnoreCase("doctor")) {
            UserDutyShift userDutyShift = userDutyShiftRepository.findByUser(user);
            Vacation vacation = vacationRepository.findByUser(user);
            userResponseWrapper.setDutyShift(userDutyShift.getDutyShift());
            userResponseWrapper.setVacation(vacation);
            List<ClinicalDepartment> clinicalDepartmentList = new ArrayList<>();
            for (DepartmentUser departmentUser : user.getDepartmentsActive()) {
                clinicalDepartmentList.add(departmentUser.getClinicalDepartment());
               userResponseWrapper.setClinicalDepartments(clinicalDepartmentList);
           }

        }
        if (userType.equalsIgnoreCase("nurse")) {
            List<ClinicalDepartment> nurseDepartList = new ArrayList<>();
            List<DutyWithDoctor> nurseDutyWithDoctorsList = new ArrayList<>();
            for (DepartmentUser nurseDeptUser : user.getDepartmentsActive()) {
                nurseDepartList.add(nurseDeptUser.getClinicalDepartment());
                userResponseWrapper.setClinicalDepartments(nurseDepartList);
            }
            for (DutyWithDoctor nurseDutyWithDoctor : user.getDoctor()) {
                nurseDutyWithDoctorsList.add(nurseDutyWithDoctor);
                userResponseWrapper.setDutyWithDoctors(nurseDutyWithDoctorsList);
            }
        }
*//*
        return userResponseWrapper;*/
        return null;
    }

    public List<Role> getAllActiveRoles() {
        return roleRepository.findAllByActiveTrue();
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public int totalUser() {
        return userRepository.countAllByActiveTrue();
    }

    /* public List<StaffResponseWrapper> findAllStaff(int offset, int limit) {

         Pageable pageable = new PageRequest(offset, limit);
         List<StaffResponseWrapper> users = new ArrayList<>();
         //  BranchWrapper branches = null;
         List<User> allUser = userRepository.findAllByActiveTrue(pageable);
         for (User user : allUser) {
             StaffResponseWrapper userWrapper = new StaffResponseWrapper(user);
             *//*for (BranchUser branchUser : user.getBranches()) {
                if (branchUser.isPrimaryBranch()) {
                    BranchWrapper branchWrapper = new BranchWrapper(branchUser);
                    branches = branchWrapper;
                }
            }
            userWrapper.setBranch(branches);
*//*            users.add(userWrapper);
        }
        return users;
    }*/
    public List<StaffWrapper> findAllStaff(int offset, int limit) {

        Pageable pageable = new PageRequest(offset, limit);
        List<StaffWrapper> drStaffList = doctorRepository.findAllByActive(pageable);
        List<StaffWrapper> crStaffList = cashierRepository.findAllByActive(pageable);
        List<StaffWrapper> rtStaffList = receptionistRepository.findAllByActive(pageable);
        List<StaffWrapper> nrStaffList = nurseRepository.findAllByActive(pageable);
        finalStaffList = Stream.of(drStaffList, crStaffList, rtStaffList, nrStaffList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return finalStaffList;
    }

    public int countAllStaff() {
        logger.info("size:o" + finalStaffList.size());
        return finalStaffList.size();
    }

    @Transactional
    public User updateStaffData(StaffRequestWrapper createRequest, User alreadyExistsUser) {
        switch (alreadyExistsUser.getUserType().toUpperCase()) {
            case "DOCTOR":
                Doctor doctor = doctorRepository.findByUser(alreadyExistsUser);
                alreadyExistsUser.setActive(createRequest.isActive());
                userRepository.save(alreadyExistsUser);
                    doctor.getDoctorMedicalServices().clear();
                doctor.setEmail(createRequest.getEmail());
                doctor.setHomePhone(createRequest.getHomePhone());
                doctor.setCellPhone(createRequest.getCellPhone());
                doctor.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
                doctor.setLastName(createRequest.getLastName());
                doctor.setFirstName(createRequest.getFirstName());
                doctor.setVacation(createRequest.isVacation());
                doctor.setStatus(ProfileStatusTypeEnum.ACTIVE);
                doctor.setVacationFrom(HISCoreUtil.convertToDate(createRequest.getDateFrom()));
                doctor.setVacationTO(HISCoreUtil.convertToDate(createRequest.getDateTo()));
                doctor.setCheckUpInterval(createRequest.getInterval());
                //doctor working days
                List<String> daysList = new LinkedList<String>( Arrays.asList(createRequest.getSelectedWorkingDays()) );
               if(!HISCoreUtil.isListEmpty(daysList)){
                    doctor.setWorkingDays(daysList);
               }

                //doctor department portion
                Department selectedDocDept = null;
                if(createRequest.getSelectedDepartment().length>0) {
                    //departmentRepository.findOne( (createRequest.getSelectedDepartment())[0] );
                    selectedDocDept = departmentRepository.findOne((createRequest.getSelectedDepartment())[0]);
                }
                doctor.setDepartment(selectedDocDept);
                doctorRepository.save(doctor);
                //doctor visit branches
                List<MedicalService> medicalServiceList = medicalServiceRepository.findAllByIdIn( Arrays.asList( createRequest.getSelectedServices() ) );
                List<DoctorMedicalService> doctorMedicalServiceList = new ArrayList<>();
                if(!HISCoreUtil.isListEmpty(medicalServiceList)){
                    doctorMedicalServiceRepository.deleteDoctorMedicalServiceByDoctor_Id(doctor.getId());
                    for(MedicalService ms : medicalServiceList){
                        DoctorMedicalService dms = new DoctorMedicalService();
                        dms.setDoctor(doctor);
                        dms.setMedicalService(ms);
                        doctorMedicalServiceList.add(dms);
                    }
                }
                doctorMedicalServiceRepository.save(doctorMedicalServiceList);
                Branch branchDoc = branchRepository.findOne(createRequest.getPrimaryBranch());
                BranchDoctor branchDoctor = branchDoctorRepository.findByDoctorAndPrimaryBranchTrue(doctor);
                branchDoctor.setBranch(branchDoc);
                branchDoctorRepository.save(branchDoctor);
                List<Branch> allowBranchesDoc = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
                List<BranchDoctor> nurseVisitBranchesData = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(allowBranchesDoc)) {
                    branchDoctorRepository.deleteAllByDoctorAndPrimaryBranchFalse(doctor);
                    for (Branch userVisitBr : allowBranchesDoc) {
                        if (userVisitBr.getId() == branchDoc.getId())
                            continue;
                        BranchDoctor userVisitBranches = new BranchDoctor();
                        userVisitBranches.setBranch(userVisitBr);
                        userVisitBranches.setDoctor(doctor);
                        userVisitBranches.setPrimaryBranch(false);
                        nurseVisitBranchesData.add(userVisitBranches);
                    }
                    branchDoctorRepository.save(nurseVisitBranchesData);
                }

                dutyShiftRepository.deleteAllByDoctor(doctor);

                if (!HISCoreUtil.isNull(createRequest.getFirstShiftFromTime())) {
                    DutyShift dutyShift = new DutyShift();//dutyShiftRepository.findByDoctorAndShiftName(doctor, DutyShiftEnum.SHIFT1);
                    dutyShift.setShiftName(DutyShiftEnum.SHIFT1);
                    dutyShift.setStartTime(HISCoreUtil.convertToTime(createRequest.getFirstShiftFromTime()));
                    dutyShift.setEndTime(HISCoreUtil.convertToTime(createRequest.getFirstShiftToTime()));
                    dutyShift.setDoctor(doctor);
                    dutyShiftRepository.save(dutyShift);
                }

                if (!HISCoreUtil.isNull(createRequest.getSecondShiftFromTime())) {
//                    dutyShiftRepository.deleteAllByDoctorAndShiftName(doctor,DutyShiftEnum.EVENING);
                    DutyShift dutyShift2 = new DutyShift();//dutyShiftRepository.findByDoctorAndShiftName(doctor, DutyShiftEnum.SHIFT2);
                    dutyShift2.setShiftName(DutyShiftEnum.SHIFT2);
                    dutyShift2.setStartTime(HISCoreUtil.convertToTime(createRequest.getSecondShiftFromTime()));
                    dutyShift2.setEndTime(HISCoreUtil.convertToTime(createRequest.getSecondShiftToTime()));
                    dutyShift2.setDoctor(doctor);
                    dutyShiftRepository.save(dutyShift2);
                }

                break;
            case "RECEPTIONIST":
                Receptionist receptionist = receptionistRepository.findByUser(alreadyExistsUser);
                alreadyExistsUser.setActive(createRequest.isActive());
                userRepository.save(alreadyExistsUser);
                receptionist.setEmail(createRequest.getEmail());
                receptionist.setHomePhone(createRequest.getHomePhone());
                receptionist.setCellPhone(createRequest.getCellPhone());
                receptionist.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
                receptionist.setLastName(createRequest.getLastName());
                receptionist.setFirstName(createRequest.getFirstName());
                receptionistRepository.save(receptionist);
                Branch primaryBranchReceptionist = branchRepository.findOne(createRequest.getPrimaryBranch());
                BranchReceptionist branchReceptionist = branchReceptionistRepository.findByReceptionistAndPrimaryBranchTrue(receptionist);
                branchReceptionist.setBranch(primaryBranchReceptionist);
                branchReceptionistRepository.save(branchReceptionist);

                List<Branch> allowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
                List<BranchReceptionist> receptionistVisitBranchesData = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(allowBranches)) {
                    branchReceptionistRepository.deleteAllByReceptionistAndPrimaryBranchFalse(receptionist);
                }
                if (!HISCoreUtil.isListEmpty(allowBranches)) {
                    for (Branch userVisitBr : allowBranches) {
                        if (userVisitBr.getId() == primaryBranchReceptionist.getId())
                            continue;
                        BranchReceptionist userVisitBranches = new BranchReceptionist();
                        userVisitBranches.setBranch(userVisitBr);
                        userVisitBranches.setReceptionist(receptionist);
                        userVisitBranches.setPrimaryBranch(false);
                        receptionistVisitBranchesData.add(userVisitBranches);
                    }
                    branchReceptionistRepository.save(receptionistVisitBranchesData);
                }

                break;
            case "CASHIER":
                Cashier cashier = cashierRepository.findByUser(alreadyExistsUser);
                alreadyExistsUser.setActive(createRequest.isActive());
                userRepository.save(alreadyExistsUser);
                cashier.setEmail(createRequest.getEmail());
                cashier.setHomePhone(createRequest.getHomePhone());
                cashier.setCellPhone(createRequest.getCellPhone());
                cashier.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
                cashier.setLastName(createRequest.getLastName());
                cashier.setFirstName(createRequest.getFirstName());
                cashierRepository.save(cashier);

                Branch primaryBranchCashier = branchRepository.findOne(createRequest.getPrimaryBranch());
                BranchCashier branchCashier = branchCashierRepository.findByCashierAndPrimaryBranchTrue(cashier);
                branchCashier.setBranch(primaryBranchCashier);
                branchCashierRepository.save(branchCashier);

                List<Branch> cashierAllowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
                List<BranchCashier> cashierVisitBranchesData = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(cashierAllowBranches)) {
                    branchCashierRepository.deleteAllByCashierAndPrimaryBranchFalse(cashier);
                }
                if (!HISCoreUtil.isListEmpty(cashierAllowBranches)) {
                    for (Branch userVisitBr : cashierAllowBranches) {
                        if (userVisitBr.getId() == branchCashier.getId())
                            continue;
                        BranchCashier userVisitBranches = new BranchCashier();
                        userVisitBranches.setBranch(userVisitBr);
                        userVisitBranches.setCashier(cashier);
                        userVisitBranches.setPrimaryBranch(false);
                        cashierVisitBranchesData.add(userVisitBranches);
                    }
                    branchCashierRepository.save(cashierVisitBranchesData);
                }
                break;
            case "NURSE":
                Nurse nurse = nurseRepository.findByUser(alreadyExistsUser);
                alreadyExistsUser.setActive(createRequest.isActive());
                userRepository.save(alreadyExistsUser);
                nurse.setEmail(createRequest.getEmail());
                nurse.setHomePhone(createRequest.getHomePhone());
                nurse.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
                nurse.setCellPhone(createRequest.getCellPhone());
                nurse.setAccountExpiry(HISCoreUtil.convertToDate(createRequest.getAccountExpiry()));
                nurse.setLastName(createRequest.getLastName());
                nurse.setFirstName(createRequest.getFirstName());
                nurse.setManagePatientInvoices(createRequest.isManagePatientInvoices());
                nurse.setManagePatientRecords(createRequest.isManagePatientRecords());
                nurseRepository.save(nurse);

                //nurse department portion
                List<Department> selectedDept = departmentRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedDepartment()));
                List<NurseDepartment> nurseDepartmentList = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(selectedDept)) {
                    nurseDepartmentRepository.deleteAllByNurse_Id(nurse.getId());
                    for (Department dept : selectedDept) {
                        NurseDepartment nurseDept = new NurseDepartment();
                        nurseDept.setDepartment(dept);
                        nurseDept.setNurse(nurse);
                        nurseDepartmentList.add(nurseDept);
                    }

                }
                nurseDepartmentRepository.save(nurseDepartmentList);

                //nurse branch portion
                Branch primaryBranchNurse = branchRepository.findOne(createRequest.getPrimaryBranch());
                BranchNurse branchNurse = branchNurseRepository.findByNurseAndPrimaryBranchTrue(nurse);
                branchNurse.setBranch(primaryBranchNurse);
                branchNurseRepository.save(branchNurse);

                List<Branch> nurseAllowBranches = branchRepository.findAllByIdIn(Arrays.asList(createRequest.getSelectedVisitBranches()));
                List<BranchNurse> nurseAllowBranchesData = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(nurseAllowBranches)) {
                    branchNurseRepository.deleteAllByNurseAndPrimaryBranchFalse(nurse);
                }
                if (!HISCoreUtil.isListEmpty(nurseAllowBranches)) {
                    for (Branch userVisitBr : nurseAllowBranches) {
                        if (userVisitBr.getId() == branchNurse.getId())
                            continue;
                        BranchNurse userVisitBranches = new BranchNurse();
                        userVisitBranches.setBranch(userVisitBr);
                        userVisitBranches.setNurse(nurse);
                        userVisitBranches.setPrimaryBranch(false);
                        nurseAllowBranchesData.add(userVisitBranches);
                    }
                    branchNurseRepository.save(nurseAllowBranchesData);
                }

                //nurse with doctor logic
                //List<User> doctorsList = userRepository.findAllByIdIn(Arrays.asList(createRequest.getDutyWithDoctors()));

                List<Doctor> doctorList = doctorRepository.findAllByIdIn(Arrays.asList(createRequest.getDutyWithDoctors()));
                List<NurseWithDoctor> dutyWithDoctorsData = new ArrayList<>();
                if (!HISCoreUtil.isListEmpty(doctorList)) {
                    nurseWithDoctorRepository.deleteAllByNurse_Id(nurse.getId());
                    for (Doctor docUser : doctorList) {
                        NurseWithDoctor dutyWithDoctor1 = new NurseWithDoctor();
                        dutyWithDoctor1.setNurse(nurse);
                        dutyWithDoctor1.setDoctor(docUser);
                        dutyWithDoctorsData.add(dutyWithDoctor1);
                    }
                }
                nurseWithDoctorRepository.save(dutyWithDoctorsData);

                break;

        }

        /*if (userType.equalsIgnoreCase(UserTypeEnum.RECEPTIONIST.toString())) {
            alreadyExistsUser.setUsername(userCreateRequest.getUserName());
            alreadyExistsUser.setActive(userCreateRequest.isActive());
            alreadyExistsUser.setEmail(userCreateRequest.getEmail());

            Profile updateProfile = alreadyExistsUser.getProfile();
            updateProfile.setCellPhone(userCreateRequest.getCellPhone());
            updateProfile.setFirstName(userCreateRequest.getFirstName());
            updateProfile.setLastName(userCreateRequest.getLastName());
            updateProfile.setType(userCreateRequest.getUserType());
            updateProfile.setHomePhone(userCreateRequest.getHomePhone());
            updateProfile.setSendBillingReport(userCreateRequest.isSendBillingReport());
            updateProfile.setUseReceptDashBoard(userCreateRequest.isUseReceptDashboard());
            updateProfile.setOtherDoctorDashBoard(userCreateRequest.isOtherDoctorDashBoard());
            updateProfile.setOtherDashboard(userCreateRequest.getOtherDashboard());
            updateProfile.setAccountExpiry(userCreateRequest.getAccountExpiry());
            updateProfile.setActive(userCreateRequest.isActive());
            updateProfile.setAllowDiscount(userCreateRequest.getAllowDiscount());
            updateProfile.setUpdatedOn(System.currentTimeMillis());
            alreadyExistsUser.setProfile(updateProfile);

            List<Branch> recepVisitBranchesReceptionist = branchRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedVisitBranches()));
            List<UserVisitBranches> recepVisitBranchesData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(recepVisitBranchesReceptionist)) {
                userVisitBranchesRepository.deleteByUser(alreadyExistsUser);
            }
            for (Branch userVisitBr : recepVisitBranchesReceptionist) {
                UserVisitBranches recepVisitBranches = new UserVisitBranches();
                recepVisitBranches.setBranch(userVisitBr);
                recepVisitBranches.setUser(alreadyExistsUser);
            }
            branchUser.setBranch(primaryBranch);
            branchUserRepository.save(branchUser);
            userVisitBranchesRepository.save(recepVisitBranchesData);
            userRepository.saveAndFlush(alreadyExistsUser);
            return alreadyExistsUser;
        }

        if (userType.equalsIgnoreCase(UserTypeEnum.NURSE.toString())) {
            alreadyExistsUser.setUsername(userCreateRequest.getUserName());
            alreadyExistsUser.setActive(userCreateRequest.isActive());
            alreadyExistsUser.setEmail(userCreateRequest.getEmail());

            Profile updateProfile = alreadyExistsUser.getProfile();

            updateProfile.setCellPhone(userCreateRequest.getCellPhone());
            updateProfile.setFirstName(userCreateRequest.getFirstName());
            updateProfile.setLastName(userCreateRequest.getLastName());
            updateProfile.setHomePhone(userCreateRequest.getHomePhone());
            updateProfile.setSendBillingReport(userCreateRequest.isSendBillingReport());
            updateProfile.setUseReceptDashBoard(userCreateRequest.isUseReceptDashboard());
            updateProfile.setOtherDoctorDashBoard(userCreateRequest.isOtherDoctorDashBoard());
            updateProfile.setAccountExpiry(userCreateRequest.getAccountExpiry());
            updateProfile.setActive(userCreateRequest.isActive());
            updateProfile.setUpdatedOn(System.currentTimeMillis());
            updateProfile.setManagePatientRecords(userCreateRequest.isManagePatientRecords());
            updateProfile.setManagePatientInvoices(userCreateRequest.isManagePatientInvoices());
            updateProfile.setOtherDashboard(userCreateRequest.getOtherDashboard());

            alreadyExistsUser.setProfile(updateProfile);
            branchUser.setBranch(primaryBranch);
            branchUserRepository.save(branchUser);
            userRepository.save(alreadyExistsUser);
            List<ClinicalDepartment> clinicalDepartments = clinicalDepartmentRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedDepartment()));
            List<DepartmentUser> departmentUserListData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(clinicalDepartments)) {
                departmentUserRepository.deleteByUser(alreadyExistsUser);
            }
            for (ClinicalDepartment clinicalDepartment : clinicalDepartments) {
                DepartmentUser departmentUser = new DepartmentUser();
                departmentUser.setClinicalDepartment(clinicalDepartment);
                departmentUser.setUser(alreadyExistsUser);
                departmentUser.setCreatedOn(System.currentTimeMillis());
                departmentUser.setUpdatedOn(System.currentTimeMillis());
                departmentUser.setDeleted(false);
                departmentUserListData.add(departmentUser);
            }
            departmentUserRepository.save(departmentUserListData);

            List<Branch> userVisitBranches = branchRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedVisitBranches()));
            List<UserVisitBranches> userVisitBranchesData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(userVisitBranches)) {
                userVisitBranchesRepository.deleteByUser(alreadyExistsUser);
            }
            for (Branch userVisitBr : userVisitBranches) {
                UserVisitBranches userVisitBranches1 = new UserVisitBranches();
                userVisitBranches1.setBranch(userVisitBr);
                userVisitBranches1.setUser(alreadyExistsUser);
                userVisitBranchesData.add(userVisitBranches1);
            }
            userVisitBranchesRepository.save(userVisitBranchesData);

            List<User> doctors = userRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getDutyWithDoctors()));
            List<DutyWithDoctor> listOFData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(doctors)) {
                dutyWithDoctorRepository.deleteByNurse(alreadyExistsUser);
            }
            for (User docUser : doctors) {
                DutyWithDoctor dutyWithDoctor1 = new DutyWithDoctor();
                dutyWithDoctor1.setNurse(alreadyExistsUser);
                dutyWithDoctor1.setDoctor(docUser);
                listOFData.add(dutyWithDoctor1);
            }
            dutyWithDoctorRepository.save(listOFData);


            return alreadyExistsUser;
        }

        if (userType.equalsIgnoreCase(UserTypeEnum.DOCTOR.toString())) {
            Vacation vacation = vacationRepository.findByUser(alreadyExistsUser);
            alreadyExistsUser.setUsername(userCreateRequest.getUserName());
            alreadyExistsUser.setActive(userCreateRequest.isActive());
            alreadyExistsUser.setEmail(userCreateRequest.getEmail());

            Profile updateProfile = alreadyExistsUser.getProfile();

            updateProfile.setCellPhone(userCreateRequest.getCellPhone());
            updateProfile.setHomePhone(userCreateRequest.getHomePhone());
            updateProfile.setSendBillingReport(userCreateRequest.isSendBillingReport());
            updateProfile.setUseReceptDashBoard(userCreateRequest.isUseReceptDashboard());
            updateProfile.setOtherDoctorDashBoard(userCreateRequest.isOtherDoctorDashBoard());
            updateProfile.setAccountExpiry(userCreateRequest.getAccountExpiry());
            updateProfile.setFirstName(userCreateRequest.getFirstName());
            updateProfile.setLastName(userCreateRequest.getLastName());
            updateProfile.setActive(userCreateRequest.isActive());
            updateProfile.setUpdatedOn(System.currentTimeMillis());
            updateProfile.setOtherDashboard(userCreateRequest.getOtherDashboard());
            List<String> daysList = new ArrayList<>(Arrays.asList(userCreateRequest.getSelectedWorkingDays()));
            updateProfile.setWorkingDays(daysList);
            branchUser.setBranch(primaryBranch);
            branchUserRepository.save(branchUser);
            alreadyExistsUser.setProfile(updateProfile);
            userRepository.save(alreadyExistsUser);
            vacation.setVacation(userCreateRequest.isVacation());
            vacation.setStatus(userCreateRequest.isVacation());
            vacation.setStartDate(HISCoreUtil.convertDateToMilliSeconds(userCreateRequest.getDateFrom()));
            vacation.setEndDate(HISCoreUtil.convertDateToMilliSeconds(userCreateRequest.getDateTo()));

            vacationRepository.saveAndFlush(vacation);

            UserDutyShift userDutyShift = userDutyShiftRepository.findByUser(alreadyExistsUser);
            DutyShift dutyShift = userDutyShift.getDutyShift();
            dutyShift.setEndTimeShift2(userCreateRequest.getSecondShiftToTime());
            dutyShift.setStartTimeShift2(userCreateRequest.getSecondShiftFromTime());
            dutyShift.setEndTimeShift1(userCreateRequest.getFirstShiftToTime());
            dutyShift.setStartTimeShift1(userCreateRequest.getFirstShiftFromTime());
            dutyShiftRepository.save(dutyShift);


            List<MedicalService> medicalServiceslist = medicalServicesRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedServices()));
            List<UserMedicalService> userDetailsServicesData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(medicalServiceslist)) {
                userMedicalServiceRepository.deleteByUser(alreadyExistsUser);
            }
            for (MedicalService mdService : medicalServiceslist) {
                UserMedicalService userMedicalService = new UserMedicalService();
                userMedicalService.setMedicalService(mdService);
                userMedicalService.setUser(alreadyExistsUser);
                userDetailsServicesData.add(userMedicalService);
            }
            userMedicalServiceRepository.save(userDetailsServicesData);

            List<Branch> docVisitBranches = branchRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedVisitBranches()));
            List<UserVisitBranches> docVisitBranchesData = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(docVisitBranches)) {
                userVisitBranchesRepository.deleteByUser(alreadyExistsUser);
            }
            for (Branch userVisitBr : docVisitBranches) {
                UserVisitBranches docVisitBranch = new UserVisitBranches();
                docVisitBranch.setBranch(userVisitBr);
                docVisitBranch.setUser(alreadyExistsUser);
                docVisitBranchesData.add(docVisitBranch);
            }
            userVisitBranchesRepository.save(docVisitBranchesData);

            List<ClinicalDepartment> doctorClinicalDepartments = clinicalDepartmentRepository.findAllByIdIn(Arrays.asList(userCreateRequest.getSelectedDepartment()));
            List<DepartmentUser> docDepartmentUser = new ArrayList<>();
            if (!HISCoreUtil.isListEmpty(doctorClinicalDepartments)) {
                departmentUserRepository.deleteByUser(alreadyExistsUser);
            }
            for (ClinicalDepartment clinicalDepartment : doctorClinicalDepartments) {
                DepartmentUser departmentUser = new DepartmentUser();
                departmentUser.setClinicalDepartment(clinicalDepartment);
                departmentUser.setUser(alreadyExistsUser);
                departmentUser.setCreatedOn(System.currentTimeMillis());
                departmentUser.setUpdatedOn(System.currentTimeMillis());
                departmentUser.setDeleted(false);
                docDepartmentUser.add(departmentUser);
            }
            departmentUserRepository.save(docDepartmentUser);
            return alreadyExistsUser;

        }*/
        return alreadyExistsUser;
    }

    @Transactional
    public User deleteUser(User user) {
        //  user.setDeleted(true);
        String userType = user.getUserType();
        if (userType.equalsIgnoreCase("CASHIER")) {
            Cashier cashier = cashierRepository.findByUser(user);
            userRepository.delete(user);
            branchCashierRepository.deleteAllByCashier(cashier);
            cashierRepository.delete(cashier);
            return user;
        }
        if (userType.equalsIgnoreCase("RECEPTIONIST")) {
            Receptionist receptionist = receptionistRepository.findByUser(user);
            branchReceptionistRepository.deleteAllByReceptionist(receptionist);
            userRepository.delete(user);
            receptionistRepository.delete(receptionist);
            return user;
        }
        if (userType.equalsIgnoreCase("NURSE")) {
            Nurse nurse = nurseRepository.findByUser(user);
            branchNurseRepository.deleteAllByNurse(nurse);
            userRepository.delete(user);
            nurseWithDoctorRepository.deleteAllByNurse(nurse);
            nurseRepository.delete(nurse);
        //    userRoleRepository.deleteAllByUser(user);
           ;
            return user;
        }
        if (userType.equalsIgnoreCase("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUser(user);
            branchDoctorRepository.deleteAllByDoctor(doctor);
            //    userRoleRepository.deleteAllByUser(user);
            dutyShiftRepository.deleteAllByDoctor(doctor);
            nurseWithDoctorRepository.deleteAllByDoctor(doctor);
            userRepository.delete(user);
            doctorRepository.delete(doctor);

            return user;
        }
        //userRepository.delete(user);
        return null;
    }

    public List<StaffResponseWrapper> findByRole(String role) {
        List<StaffResponseWrapper> staffRespWrapper = new ArrayList<>();
        List<User> userList = userRepository.findAllByUserRoles_role_name(role);
        StaffResponseWrapper staffWraper = new StaffResponseWrapper();
        switch(role){
            case "DOCTOR":
                List<Doctor> doctorList =doctorRepository.findAllByUserIn(userList);
                for (Doctor  doctor : doctorList){
                    staffRespWrapper.add(new StaffResponseWrapper(doctor));
                }
                break;
            default:
                break;
        }
        return staffRespWrapper;
    }

    public List<StaffWrapper> searchByNameOrRole(String name, String userType, int offset, int limit) {
        Pageable pageable = new PageRequest(offset, limit);
        if(userType!=null && !userType.isEmpty())
            userType = userType.toLowerCase();
        List<StaffWrapper> drStaffList = doctorRepository.findAllBySearchCriteria(name, userType, pageable);
        List<StaffWrapper> crStaffList = cashierRepository.findAllBySearchCriteria(name, userType, pageable);
        List<StaffWrapper> rtStaffList = receptionistRepository.findAllBySearchCriteria(name, userType, pageable);
        List<StaffWrapper> nrStaffList = nurseRepository.findAllBySearchCriteria(name, userType, pageable);

        finalStaffList = Stream.of(drStaffList, crStaffList, rtStaffList, nrStaffList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        return finalStaffList;
    }


}
