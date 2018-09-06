package com.sd.his.wrapper;

public class FamilyHistoryWrapper {

    private Long id;
    private String name;
    private String relation;
    private String ethnicGroup;
    private String status;
    private Long patientId;

    public FamilyHistoryWrapper() {
    }

    //fh.id,fh.name,fh.relation,fh.status,fh.patientId

    public FamilyHistoryWrapper(Long id, String name, String relation, String status, String ethnicGroup, Long patientId) {
        this.id = id;
        this.name = name;
        this.relation = relation;
        this.ethnicGroup = ethnicGroup;
        this.status = status;
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEthnicGroup() {
        return ethnicGroup;
    }

    public void setEthnicGroup(String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

