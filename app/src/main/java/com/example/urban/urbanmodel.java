package com.example.urban;

public class urbanmodel {
    private String Name, area,phoneNumber,Specialization,count;

    public urbanmodel() {
    }

    public urbanmodel(String Name, String area, String phoneNumber, String Specialization,String count) {
        this.Name = Name;
        this.area = area;
        this.phoneNumber = phoneNumber;
        this.Specialization = Specialization;
        this.count=count;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


}
