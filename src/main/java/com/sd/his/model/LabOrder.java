package com.sd.his.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * @author    : Tahir Mehmood
 * @Date      : 31-Jul-2018
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
 * @Package   : com.sd.his.model
 * @FileName  : User
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
@Entity
@Table(name = "LABORDER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //add some data
    @Column(name = "STATUS")
    private String status;

    @Column(name = "COMMENTS")
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TEST")
    private Date dateTest;

    @JsonIgnore
    @Column(name = "DOCTOR_SIGNOFF")
    private Boolean doctorSignOff;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "APPOINTMENT_ID")
    private Appointment appointment;

    @JsonIgnore
    @OneToMany(mappedBy = "labOrder")
    private List<LabTest> labTests;

    public LabOrder() {
    }
    @JsonIgnore
    public List<LabTest> getLabTests() {
        return labTests;
    }

    public void setLabTests(List<LabTest> labTests) {
        this.labTests = labTests;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @JsonIgnore
    public Date getDateTest() {
        return dateTest;
    }

    public void setDateTest(Date dateTest) {
        this.dateTest = dateTest;
    }

    @JsonIgnore
    public Boolean getDoctorSignOff() {
        return doctorSignOff;
    }

    public void setDoctorSignOff(Boolean doctorSignOff) {
        this.doctorSignOff = doctorSignOff;
    }

    @JsonIgnore
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @JsonIgnore
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
