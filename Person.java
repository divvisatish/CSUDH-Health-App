package com.csudh.healthapp.csudhhealthapp;

/**
 * Created by Darshit on 11/11/2017.
 */

public class Person {

    private String firstName;
    private String lastName;
    private String personId;
    private String userName;
    private String emailId;
    private String bloodTypeName;
    private int bloodTypeId;
    private NotificationVO[] notificationVO;
    private int activeFlag;
    private String crtDate;
    private String lstUpdtDate;
    private String dateOfBirth;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public NotificationVO[] getNotificationVO() {
        return notificationVO;
    }

    public void setNotificationVO(NotificationVO[] notificationVO) {
        this.notificationVO = notificationVO;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
