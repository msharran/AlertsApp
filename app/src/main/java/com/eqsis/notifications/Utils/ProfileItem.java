package com.eqsis.notifications.Utils;

public class ProfileItem {
    public String getUserName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPhone() {
        return phone;
    }

    private String name;
    private String dob;
    private String gender;

    public ProfileItem(String name, String dob, String gender, String status, String maritalStatus, String email, String phone) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.status = status;
        this.maritalStatus = maritalStatus;
        this.email = email;
        this.phone = phone;
    }

    private String status;
    private String maritalStatus;
    private String email;
    private String phone;
}
