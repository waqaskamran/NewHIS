package com.sd.his.model;

import com.sd.his.enums.AppointmentStatusTypeEnum;
import com.sd.his.enums.AppointmentTypeEnum;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*
 * @author    : Irfan Nasim
 * @Date      : 08-Jun-18
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
 * @FileName  : Branch
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
@Entity
@Table(name = "APPOINTMENT")
public class Appointment extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @NaturalId
    @Column(name = "APPOINTMENT_ID", unique = true, nullable = false, updatable = false)
    private String appointmentId;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "COLOR")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AppointmentStatusTypeEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private AppointmentTypeEnum type;

    @Column(name = "DURATION") //minutes
    private Integer duration;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "FOLLOW_UP_REMINDER")
    private Boolean followUpReminder;

    @Column(name = "FOLLOW_UP_REMINDER_REASON")
    private String followUpReasonReminder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCHEDULE_DATE")
    private Date schdeulledDate;

    @Temporal(TemporalType.TIME)
    @Column(name = "STARTED_ON")
    private Date startedOn;

    @Temporal(TemporalType.TIME)
    @Column(name = "ENDED_ON")
    private Date endedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRANCH_ID")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXAM_ROOM_ID")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    @Column(name = "IS_RECURRING")
    private Boolean recurring;

    @ElementCollection
    @Column(name = "RECURRING_DAYS")
    private String recurringDays; //should be save json of days

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_APPOINTMENT_ON")
    private Date firstAppointmentOn;

    @Temporal(TemporalType.DATE)
    @Column(name = "FOLLOW_UP_DATE")
    private Date followUpDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_APPOINTMENT_ON")
    private Date lastAppointmentOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public AppointmentStatusTypeEnum getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatusTypeEnum status) {
        this.status = status;
    }

    public AppointmentTypeEnum getType() {
        return type;
    }

    public void setType(AppointmentTypeEnum type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getFollowUpReminder() {
        return followUpReminder;
    }

    public void setFollowUpReminder(Boolean followUpReminder) {
        this.followUpReminder = followUpReminder;
    }

    public String getFollowUpReasonReminder() {
        return followUpReasonReminder;
    }

    public void setFollowUpReasonReminder(String followUpReasonReminder) {
        this.followUpReasonReminder = followUpReasonReminder;
    }

    public Date getSchdeulledDate() {
        return schdeulledDate;
    }

    public void setSchdeulledDate(Date schdeulledDate) {
        this.schdeulledDate = schdeulledDate;
    }

    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    public Date getEndedOn() {
        return endedOn;
    }

    public void setEndedOn(Date endedOn) {
        this.endedOn = endedOn;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringDays() {
        return recurringDays;
    }

    public void setRecurringDays(String recurringDays) {
        this.recurringDays = recurringDays;
    }

    public Date getFirstAppointmentOn() {
        return firstAppointmentOn;
    }

    public void setFirstAppointmentOn(Date firstAppointmentOn) {
        this.firstAppointmentOn = firstAppointmentOn;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Date getLastAppointmentOn() {
        return lastAppointmentOn;
    }

    public void setLastAppointmentOn(Date lastAppointmentOn) {
        this.lastAppointmentOn = lastAppointmentOn;
    }
}
