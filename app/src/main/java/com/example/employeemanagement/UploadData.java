package com.example.employeemanagement;

public class UploadData {

    private String name, category, employeeid, joindate, birthdate, bloodgroup, mobile, email,
            password, address, nid,uploadfile,uploadurl,shop,workplace,status,designition,acount_number;
    private String profile, signeture, qrcode;

    public UploadData(){}

    public UploadData(String name, String category, String employeeid, String joindate, String birthdate, String bloodgroup,String designition,
                      String mobile, String email, String password, String address, String nid, String shop, String workplace, String profile, String signeture,
                      String qrcode,String status,String acount_number) {
        this.name = name;
        this.category = category;
        this.employeeid = employeeid;
        this.joindate = joindate;
        this.birthdate = birthdate;
        this.bloodgroup = bloodgroup;
        this.designition = designition;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.address = address;
        this.nid = nid;
        this.shop = shop;
        this.workplace = workplace;
        this.profile = profile;
        this.signeture = signeture;
        this.qrcode = qrcode;
        this.status = status;
        this.acount_number = acount_number;
    }

    public UploadData(String name, String category, String employeeid, String joindate, String birthdate, String bloodgroup,
                      String mobile, String email, String password, String address, String nid, String profile, String signeture, String qrcode) {
        if (name.trim().equals("")) {
            name = "no name";
        }
        this.name = name;
        this.category = category;
        this.employeeid = employeeid;
        this.joindate = joindate;
        this.birthdate = birthdate;
        this.bloodgroup = bloodgroup;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.address = address;
        this.nid = nid;
        this.profile = profile;
        this.signeture = signeture;
        this.qrcode = qrcode;
    }

    public String getAcount_number() {
        return acount_number;
    }

    public void setAcount_number(String acount_number) {
        this.acount_number = acount_number;
    }

    public String getStatus() {
        return status;
    }

    public String getDesignition() {
        return designition;
    }

    public void setDesignition(String designition) {
        this.designition = designition;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }

    public String getUploadurl() {
        return uploadurl;
    }

    public void setUploadurl(String uploadurl) {
        this.uploadurl = uploadurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSigneture() {
        return signeture;
    }

    public void setSigneture(String signeture) {
        this.signeture = signeture;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
