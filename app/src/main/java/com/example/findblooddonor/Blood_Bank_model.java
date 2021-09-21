package com.example.findblooddonor;

public class Blood_Bank_model {
    String name,district,phone;

    public Blood_Bank_model(String name, String district, String phone) {
        this.name = name;
        this.district = district;
        this.phone = phone;

    }

    public Blood_Bank_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
