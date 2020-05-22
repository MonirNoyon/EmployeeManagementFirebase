package com.example.employeemanagement;

public class UploadAttendence {

    String attend_type;
    String appointment,id,visiting_card,t_shirt,wechat_account,v2_account,job_confirmation,
            bank_account,tin_number,email,emp_type,type;

    public UploadAttendence() {
    }

    public UploadAttendence(String appointment, String id, String visiting_card, String t_shirt, String wechat_account, String v2_account,
                            String job_confirmation, String bank_account, String tin_number,String email,String emp_type) {
        this.appointment = appointment;
        this.id = id;
        this.visiting_card = visiting_card;
        this.t_shirt = t_shirt;
        this.wechat_account = wechat_account;
        this.v2_account = v2_account;
        this.job_confirmation = job_confirmation;
        this.bank_account = bank_account;
        this.tin_number = tin_number;
        this.email = email;
        this.emp_type = emp_type;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisiting_card() {
        return visiting_card;
    }

    public void setVisiting_card(String visiting_card) {
        this.visiting_card = visiting_card;
    }

    public String getT_shirt() {
        return t_shirt;
    }

    public void setT_shirt(String t_shirt) {
        this.t_shirt = t_shirt;
    }

    public String getWechat_account() {
        return wechat_account;
    }

    public void setWechat_account(String wechat_account) {
        this.wechat_account = wechat_account;
    }

    public String getV2_account() {
        return v2_account;
    }

    public void setV2_account(String v2_account) {
        this.v2_account = v2_account;
    }

    public String getJob_confirmation() {
        return job_confirmation;
    }

    public void setJob_confirmation(String job_confirmation) {
        this.job_confirmation = job_confirmation;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(String tin_number) {
        this.tin_number = tin_number;
    }

    public UploadAttendence(String attend_type,String type) {
        this.attend_type = attend_type;
        this.type = type;
    }

    public String getAttend_type() {
        return attend_type;
    }

    public void setAttend_type(String attend_type) {
        this.attend_type = attend_type;
    }
}
