package com.sd.his.repository;

import com.sd.his.model.PatientVital;
import com.sd.his.model.Patient_Order;
import com.sd.his.model.VitalSetup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PatientVitalRepository  extends JpaRepository<PatientVital, Long> {

    PatientVital findByName(String name);


  /*  @Query("SELECT new com.sd.his.model.PatientVital(patientVital) FROM PatientVital patientVital")
    List<PatientVital> getAll();*/



    @Query("SELECT new com.sd.his.wrapper.PatientVitalWrapper(patientVital.id,patientVital.name, patientVital.unit, patientVital.standardValue, patientVital.currentValue, patientVital.status, patientVital.patient) FROM PatientVital patientVital where patientVital.status = 'true' and patientVital.patient.id=:patientId ")
    List<PatientVital> getAllVitalPatient();



    @Query("SELECT new com.sd.his.wrapper.PatientVitalWrapper(patientVital.id,patientVital.name, patientVital.unit, patientVital.standardValue, patientVital.currentValue, patientVital.status, patientVital.patient) FROM PatientVital patientVital")
    List<PatientVital> getAll();


    @Query("Select new com.sd.his.wrapper.PatientVitalWrapper(patientVital.id,patientVital.name, patientVital.unit, patientVital.standardValue, patientVital.currentValue, patientVital.status, patientVital.patient) FROM PatientVital patientVital where  patientVital.patient.id=:patientId")
    List<PatientVital> getPaginatedOrder(Pageable pageable, @Param("patientId") Long patientId);

}