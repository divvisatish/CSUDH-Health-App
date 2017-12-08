package com.csudh.healthapp.csudhhealthapp;

/**
 * Created by Darshit on 11/22/2017.
 */

public class NotificationVO {

    private String uid;
    private int bloodTypeId;
    private String bloodTypeName;
    private String comments;
    private int activeFlag;
    private String crtDate;
    private String lstUpdtDate;
    private int requestTypeId;
    private String requestTypeName;
    private String toSendUid;

    public String getToSendUid() {
        return toSendUid;
    }

    public void setToSendUid(String toSendUid) {
        this.toSendUid = toSendUid;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public String getLstUpdtDate() {
        return lstUpdtDate;
    }

    public void setLstUpdtDate(String lstUpdtDate) {
        this.lstUpdtDate = lstUpdtDate;
    }

    public int getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(int activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBloodTypeId() {
        return bloodTypeId;
    }

    public void setBloodTypeId(int bloodTypeId) {
        this.bloodTypeId = bloodTypeId;
    }

    public String getBloodTypeName() {
        return bloodTypeName;
    }

    public void setBloodTypeName(String bloodTypeName) {
        this.bloodTypeName = bloodTypeName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
