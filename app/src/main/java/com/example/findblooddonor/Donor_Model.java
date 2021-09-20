package com.example.findblooddonor;

public class Donor_Model {

    String name,contact,email,village,blood_group,district,dob,gender,district_bloodgroup;
    public Donor_Model() {
    }

    public Donor_Model(String name, String contact, String email, String village, String blood_group, String district, String dob, String gender) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.village = village;
        this.blood_group = blood_group;
        this.district = district;
        this.dob = dob;
        this.gender = gender;
       this.district_bloodgroup=district+blood_group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict_bloodgroup() {
        return district_bloodgroup;
    }

    public void setDistrict_bloodgroup(String district_bloodgroup) {
        this.district_bloodgroup = district_bloodgroup;
    }
}
