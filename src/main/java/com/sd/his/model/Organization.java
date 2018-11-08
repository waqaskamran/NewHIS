package com.sd.his.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * @author    : Tahir Mehmood
 * @Date      : 16-Jul-18
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
 * @FileName  : Organization
 *
 * Copyright ©
 * SolutionDots,
 * All rights reserved.
 *
 */
@Entity
@Table(name = "ORGANIZATION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "DURATION_OF_EXAM")
    private Long durationOFExam;

//    @Column(name = "TIMEZONE")
//    private String timezone; time zone will be updated with time zone id

//    @Column(name = "DURATION_FOLLOW_UP")
//    private Long durationFollowUp;

    @Column(name = "OFFICE_PHONE")
    private String officePhone;

//    @Column(name = "HOME_PHONE")
//    private String homePhone;

//    @Column(name = "CELL_PHONE")
//    private String cellPhone;

    //Dateformat

    @Column(name = "WEBSITE")
    private String website;

//    @Column(name = "SPECIALTY")
//    private String specialty; Multi specialty table and should have multiple element collection

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS", columnDefinition = "boolean default true", nullable = false)
    private Boolean status;
    //CITY //COUNTRY //STATE  should be updated accroding to static table data

    @Column(name = "FAX")
    private String fax;

    @Column(name = "ADDRESS")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST)
    private List<S3Bucket> bucketList;

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Prefix> prefixList;

    @JsonIgnore
    @OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Branch> branches;

    public Organization(){}
    public Organization(String companyName, String officePhone, String website, String email, String fax, String address,Boolean status) {
        this.companyName = companyName;
        this.officePhone = officePhone;
        this.website = website;
        this.email = email;
        this.fax = fax;
        this.address = address;
        this.status = status;
   //     this.prefixList = prefixList;
   //     this.branches = branches;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getDurationOFExam() {
        return durationOFExam;
    }

    public void setDurationOFExam(Long durationOFExam) {
        this.durationOFExam = durationOFExam;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<S3Bucket> getBucketList() {
        return bucketList;
    }

    public void setBucketList(List<S3Bucket> bucketList) {
        this.bucketList = bucketList;
    }

    public List<Prefix> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<Prefix> prefixList) {
        this.prefixList = prefixList;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
}
