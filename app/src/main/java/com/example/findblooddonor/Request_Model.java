package com.example.findblooddonor;

public class Request_Model {
    String contact_person_name, patient_name, district,
            blood_group, village, hospital_name,
            required_date, contact, additional_info, post_date,required_unit,user_phone;

    public Request_Model() {
    }

    public Request_Model(String contact_person_name, String patient_name, String district, String blood_group, String village, String hospital_name, String required_date, String contact, String additional_info, String post_date,String required_unit,String user_phone) {
        this.contact_person_name = contact_person_name;
        this.patient_name = patient_name;
        this.district = district;
        this.blood_group = blood_group;
        this.village = village;
        this.hospital_name = hospital_name;
        this.required_date = required_date;
        this.contact = contact;
        this.additional_info = additional_info;
        this.post_date = post_date;
        this.required_unit = required_unit;

    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getRequired_date() {
        return required_date;
    }

    public void setRequired_date(String required_date) {
        this.required_date = required_date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }


    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getRequired_unit() {
        return required_unit;
    }

    public void setRequired_unit(String required_unit) {
        this.required_unit = required_unit;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}