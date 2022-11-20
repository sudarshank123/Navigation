package com.example.navigation;

public class ReadWriteUserDetails {

    public String doB, gender, mobile, email, aadhaarNumber;

    //Constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails( String textDoB, String textGender, String textMobile, String textEmail, String textAadhaarNumber ){
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
        this.email = textEmail;
        this.aadhaarNumber = textAadhaarNumber;


    }
}
