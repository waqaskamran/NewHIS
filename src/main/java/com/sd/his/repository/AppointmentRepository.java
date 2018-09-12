package com.sd.his.repository;

import com.amazonaws.services.opsworks.model.App;
import com.sd.his.model.Appointment;
import com.sd.his.model.Branch;
import com.sd.his.model.Doctor;
import com.sd.his.wrapper.AppointmentWrapper;
import com.sd.his.wrapper.response.BranchResponseWrapper;
import com.sd.his.wrapper.response.DashboardResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
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
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    /*   @Query("SELECT NEW  com.sd.his.wrapper.AppointmentWrapper(a.id, a.name, a.notes, a.reason, a.color, a.status, a.type," +
                " a.duration, a.followUpReminder, a.followUpReasonReminder, a.startedOn, a.endedOn, a.createdOn, a.updatedOn, " +
                "a.recurring, a.recurringDays, a.firstAppointmentOn, a.lastAppointmentOn, a.patient.id,a.patient.username, " +
                "a.patient.profile.firstName, a.patient.profile.lastName, a.branch.id, a.branch.name, a.room.id, a.room.examName) " +
                "FROM Appointment a WHERE a.deleted =FALSE ")
        List<AppointmentWrapper> findAllPaginatedAppointments(Pageable pageable);*/
    @Query("SELECT NEW  com.sd.his.wrapper.AppointmentWrapper(a.id,a.appointmentId, a.name, a.notes, a.reason, a.color, a.status, a.type," +
            " a.duration, a.followUpReminder, a.followUpReasonReminder,a.schdeulledDate, a.startedOn, a.endedOn, a.createdOn, a.updatedOn, " +
            "a.recurring, a.firstAppointmentOn, a.lastAppointmentOn, a.patient.firstName,a.patient.lastName,a.patient.id,a.branch.id, a.branch.name,a.room.id,a.room.roomName,a.doctor.firstName,a.doctor.lastName,a.doctor.id ,a.followUpDate) " +
            "FROM Appointment a")
    List<AppointmentWrapper> findAllAppointments();

    @Query("SELECT NEW  com.sd.his.wrapper.AppointmentWrapper(a.id,a.appointmentId ,a.name, a.notes, a.reason, a.color, a.status, a.type," +
            " a.duration, a.followUpReminder, a.followUpReasonReminder,a.schdeulledDate, a.startedOn, a.endedOn, a.createdOn, a.updatedOn, " +
            "a.recurring, a.firstAppointmentOn, a.lastAppointmentOn, a.patient.firstName,a.patient.lastName,a.patient.id,a.branch.id, a.branch.name,a.room.id,a.room.roomName,a.doctor.firstName,a.doctor.lastName,a.doctor.id,a.followUpDate) " +
            "FROM Appointment a WHERE a.doctor.id =?1 or a.branch.id =?2")
    List<AppointmentWrapper> findAllAppointmentsByDoctor(Long doctorId, Long branchId);

    @Query("SELECT NEW  com.sd.his.wrapper.AppointmentWrapper(a.id,a.appointmentId ,a.name, a.notes, a.reason, a.color, a.status, a.type," +
            " a.duration, a.followUpReminder, a.followUpReasonReminder,a.schdeulledDate, a.startedOn, a.endedOn, a.createdOn, a.updatedOn, " +
            "a.recurring, a.firstAppointmentOn, a.lastAppointmentOn, a.patient.firstName,a.patient.lastName,a.patient.id,a.branch.id, a.branch.name,a.room.id,a.room.roomName,a.doctor.firstName,a.doctor.lastName,a.doctor.id,a.followUpDate) " +
            "FROM Appointment a WHERE a.patient.id =?1 ")
    List<AppointmentWrapper> findAllAppointmentsByPatient(Long patientId);

    @Query("SELECT NEW  com.sd.his.wrapper.response.DashboardResponseWrapper(a.id,a.appointmentId,a.patient.id,a.patient.firstName,a.patient.lastName,a.schdeulledDate ,a.doctor.firstName,a.doctor.lastName,a.branch.name,a.reason,a.schdeulledDate,a.room.roomName, a.status, a.branch.id,a.doctor.id,a.room.id )" +
            "FROM Appointment a")
    List<DashboardResponseWrapper> findAllAppointmentsByPatientAndDoctor();

    @Query("SELECT NEW  com.sd.his.wrapper.AppointmentWrapper(a.id,a.appointmentId, a.name, a.notes, a.reason, a.color, a.status, a.type," +
            " a.duration, a.followUpReminder, a.followUpReasonReminder,a.schdeulledDate, a.startedOn, a.endedOn, a.createdOn, a.updatedOn, " +
            "a.recurring, a.firstAppointmentOn, a.lastAppointmentOn, a.patient.firstName,a.patient.lastName,a.patient.id,a.branch.id, a.branch.name,a.room.id,a.room.roomName,a.doctor.firstName,a.doctor.lastName,a.doctor.id,a.followUpDate) " +
            "FROM Appointment a WHERE a.id =?1 ")
    AppointmentWrapper findAllAppointmentById(Long apptId);

    @Query("SELECT NEW com.sd.his.wrapper.AppointmentWrapper(a.id,a.appointmentId, a.name, a.status, " +
            " a.schdeulledDate,  " +
            " a.patient.firstName,a.patient.lastName,a.doctor.firstName,a.doctor.lastName, a.patient.id) " +
            "FROM Appointment a WHERE a.id =?1 ")
    AppointmentWrapper findAppointmentById(Long apptId);


    //@Query("SELECT cd FROM Appointment cd WHERE cd.schdeulledDate LIKE  CONCAT('%',:date,'%') ")
    List<Appointment>  findBySchdeulledDateBetween(Date date1,Date date2);
    Appointment findByAppointmentId(String id);
    List<Appointment> findByDoctorAndBranch(Doctor doctor, Branch branch);

    }

