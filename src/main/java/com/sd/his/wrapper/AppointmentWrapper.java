package com.sd.his.wrapper;

import com.sd.his.enums.AppointmentStatusTypeEnum;
import com.sd.his.enums.AppointmentTypeEnum;
import com.sd.his.utill.DateTimeUtil;
import com.sd.his.utill.HISCoreUtil;
import com.sd.his.utill.JSONUtil;

import java.util.Date;
import java.util.List;

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
 * @Package   : com.sd.his.wrapper
 * @FileName  : AppointmentWrapper
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
public class AppointmentWrapper {

    private Long id;
    private String appointmentId;
    private String title;
    private String appointmentStartedOn;
    private String appointmentEndedOn;
    private Boolean draggable;
    private String patient;
    private String notes;
    private String reason;
    private String color;
    private String colorHash;
    private String status;//AppointmentStatusTypeEnum
    private Integer duration; //minutes
    private Boolean followUpReminder;
    private String followUpReason;
    private Long startedOn;
    private Long ended;
    private Long createdOn;
    private Long updatedOn;
    private boolean recurringAppointment;
    private String recurseEvery;
    private Long recurringPeriod;
    private String gender;
    private String age;
    private String cellPhone;
    private String firstAppointment;
    private String lastAppointment;
    private String followUpDate;
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private Long roomId;
    private Long doctorId;
    private String email;
    private List<String> selectedRecurringDays;
    private List<String> appointmentType;
    private String examName;
    private String branchName;
    private String type;
    private Long firstAppointmentOn;
    private Long lastAppointmentOn;
    private Long branchId;
    private String scheduleDate;
    private String scheduleDateAndTime;
    private String docFirstName;
    private String docLastName;
    private int appointmentConvertedTime;
    private int appointmentEndedConvertedTime;
    private Date compareDate;
    private String newPatient;
    private Date dateOfBirth;
    private Long serviceId;
    private String serviceName;
    private Double receivedAmount;
    private Double patientAdvanceDeposit;







    public AppointmentWrapper() {
    }

    public AppointmentWrapper(Long id, String name, String notes) {
    }

    public AppointmentWrapper(Long id,String appointmentId, String title, String notes, String reason, String color, AppointmentStatusTypeEnum status, String appointmentType, Integer duration,
                              Boolean followUpReminder, String followUpReasonReminder, Date scheduleDate, Date startedOn, Date endedOn, Date createdOn, Date updatedOn,
                              Boolean recurring, Date firstAppointmentOn, Date lastAppointmentOn, String firstName, String lastName, Long patientId,
                              Long branchId, String branchName, Long roomId, String roomName, String docFirstName, String docLastName, Long docId,Date followUpDate,Long serviceId,String serviceName
    ) {
        //Long patientId,Long branchId, String branchName, Long roomId,
        this.id = id;
        this.appointmentId=appointmentId;
        this.title = title;
        this.appointmentConvertedTime = convertAppointmentTime(startedOn);
        this.appointmentEndedConvertedTime = convertAppointmentTime(startedOn) + duration;
        this.appointmentEndedOn = HISCoreUtil.convertTimeToString(endedOn);
        this.scheduleDate = HISCoreUtil.convertDateToString(scheduleDate);
        this.draggable = draggable;
        this.patient = firstName + " " + lastName;
        this.notes = notes;
        this.reason = reason;
        this.color = color;
        this.docFirstName = docFirstName;
        this.docLastName = docLastName;
        this.doctorId = docId;
        this.status = status.name();
        this.followUpReminder = followUpReminder;
        this.duration = duration;
        this.compareDate = scheduleDate;
        this.appointmentType = JSONUtil.convertJsonToList(appointmentType);
        this.followUpReason=followUpReasonReminder;
        this.recurringPeriod = recurringPeriod;
       /* this.firstAppointmentOn = firstAppointmentOn;
        this.lastAppointmentOn = lastAppointmentOn;*/
        this.patientId = patientId;
        this.patientFirstName = firstName;
        this.patientLastName = lastName;
        this.branchId = branchId;
        this.roomId = roomId;
        this.examName = roomName;
        this.branchName = branchName;
        this.scheduleDateAndTime = HISCoreUtil.convertDateAndTimeToStringWithPMAndAM(scheduleDate);
        this.followUpDate =HISCoreUtil.convertDateAndTimeToString(followUpDate);
        this.serviceId=serviceId;
        this.serviceName=serviceName;
    }



    public AppointmentWrapper(Long id,String appointmentId, String title, AppointmentStatusTypeEnum status,Date scheduleDate, String firstName, String lastName, String docFirstName, String docLastName, Long patientId)
    {
        //Long patientId,Long branchId, String branchName, Long roomId,
        this.id = id;
        this.appointmentId=appointmentId;
        this.title = title;
        this.scheduleDate = HISCoreUtil.convertDateToString(scheduleDate);
        this.draggable = draggable;
        this.patient = firstName + " " + lastName;
        this.notes = notes;
        this.reason = reason;
        this.color = color;
        this.docFirstName = docFirstName;
        this.docLastName = docLastName;

        this.status = status.name();
        this.followUpReminder = followUpReminder;
        this.duration = duration;
        this.compareDate = scheduleDate;

        this.recurringPeriod = recurringPeriod;
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.branchId = branchId;
        this.roomId = roomId;

        this.branchName = branchName;
        this.scheduleDateAndTime = HISCoreUtil.convertDateAndTimeToString(scheduleDate);
    }



    @Override
    public String toString() {
        return "AppointmentWrapper{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                '}';
    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNewPatient() {
        return newPatient;
    }

    public void setNewPatient(String newPatient) {
        this.newPatient = newPatient;
    }

    public Date getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(Date compareDate) {
        this.compareDate = compareDate;
    }

    public int getAppointmentEndedConvertedTime() {
        return appointmentEndedConvertedTime;
    }

    public void setAppointmentEndedConvertedTime(int appointmentEndedConvertedTime) {
        this.appointmentEndedConvertedTime = appointmentEndedConvertedTime;
    }

    static int convertAppointmentTime(Date startedOn) {
        return DateTimeUtil.convertAppointmentTime(HISCoreUtil.convertTimeToString(startedOn));
    }

    public String getDocFirstName() {
        return docFirstName;
    }

    public String getColorHash() {
        return colorHash;
    }

    public int getAppointmentConvertedTime() {
        return appointmentConvertedTime;
    }

    public void setAppointmentConvertedTime(int appointmentConvertedTime) {
        this.appointmentConvertedTime = appointmentConvertedTime;
    }

    public void setColorHash(String colorHash) {
        this.colorHash = colorHash;
    }

    public void setDocFirstName(String docFirstName) {
        this.docFirstName = docFirstName;
    }

    public String getDocLastName() {
        return docLastName;
    }

    public void setDocLastName(String docLastName) {
        this.docLastName = docLastName;
    }

    public String getScheduleDateAndTime() {
        return scheduleDateAndTime;
    }

    public void setScheduleDateAndTime(String scheduleDateAndTime) {
        this.scheduleDateAndTime = scheduleDateAndTime;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getFirstAppointmentOn() {
        return firstAppointmentOn;
    }

    public void setFirstAppointmentOn(Long firstAppointmentOn) {
        this.firstAppointmentOn = firstAppointmentOn;
    }

    public Long getLastAppointmentOn() {
        return lastAppointmentOn;
    }

    public void setLastAppointmentOn(Long lastAppointmentOn) {
        this.lastAppointmentOn = lastAppointmentOn;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getRecurseEvery() {
        return recurseEvery;
    }

    public void setRecurseEvery(String recurseEvery) {
        this.recurseEvery = recurseEvery;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppointmentStartedOn() {
        return appointmentStartedOn;
    }

    public void setAppointmentStartedOn(String appointmentStartedOn) {
        this.appointmentStartedOn = appointmentStartedOn;
    }

    public String getAppointmentEndedOn() {
        return appointmentEndedOn;
    }

    public void setAppointmentEndedOn(String appointmentEndedOn) {
        this.appointmentEndedOn = appointmentEndedOn;
    }

    public Boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
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


    public Boolean getFollowUpReminder() {
        return followUpReminder;
    }

    public void setFollowUpReminder(Boolean followUpReminder) {
        this.followUpReminder = followUpReminder;
    }

    public String getFollowUpReason() {
        return followUpReason;
    }

    public void setFollowUpReason(String followUpReason) {
        this.followUpReason = followUpReason;
    }

    public Long getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Long startedOn) {
        this.startedOn = startedOn;
    }

    public Long getEnded() {
        return ended;
    }

    public void setEnded(Long ended) {
        this.ended = ended;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public boolean isRecurringAppointment() {
        return recurringAppointment;
    }

    public void setRecurringAppointment(boolean recurringAppointment) {
        this.recurringAppointment = recurringAppointment;
    }

    public Long getRecurringPeriod() {
        return recurringPeriod;
    }

    public void setRecurringPeriod(Long recurringPeriod) {
        this.recurringPeriod = recurringPeriod;
    }

    public String getFirstAppointment() {
        return firstAppointment;
    }

    public void setFirstAppointment(String firstAppointment) {
        this.firstAppointment = firstAppointment;
    }

    public String getLastAppointment() {
        return lastAppointment;
    }

    public void setLastAppointment(String lastAppointment) {
        this.lastAppointment = lastAppointment;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<String> getSelectedRecurringDays() {
        return selectedRecurringDays;
    }

    public void setSelectedRecurringDays(List<String> selectedRecurringDays) {
        this.selectedRecurringDays = selectedRecurringDays;
    }

    public List<String> getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(List<String> appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(Double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Double getPatientAdvanceDeposit() {
        return patientAdvanceDeposit;
    }

    public void setPatientAdvanceDeposit(Double patientAdvanceDeposit) {
        this.patientAdvanceDeposit = patientAdvanceDeposit;
    }
}
