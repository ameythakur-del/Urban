package com.example.urban;

public class urbanmodel {
    private String Name, area,phoneNumber,Specialization;

    public urbanmodel() {
    }

    public urbanmodel(String Name, String area, String phoneNumber, String Specialization) {
        this.Name = Name;
        this.area = area;
        this.phoneNumber = phoneNumber;
        this.Specialization = Specialization;

    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }



}
