package com.sd.his.wrapper.response;

import com.sd.his.model.Zone;

public class OrganizationChecker {
    long id;
    private String dateFormat;
    private String timeFormat;
    private String  zone;
    private String zoneTime;



    private String currency;

    public OrganizationChecker(Long id, String dateFormat, String timeFormat, String zone,String zoneTime,String currency) {
        this.id = id;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.zone = zone;
        this.zoneTime=zoneTime;
        this.currency=currency;
    }

    public OrganizationChecker(String dateFormat, String timeFormat, String zone,String zoneTime,String currency) {
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.zone = zone;
        this.zoneTime=zoneTime;
        this.currency=currency;
    }


    public long getId() {
        return id;
    }

    public void setId(long  id) {
        this.id = id;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZoneTime() {
        return zoneTime;
    }

    public void setZoneTime(String zoneTime) {
        this.zoneTime = zoneTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
