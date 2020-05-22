package com.example.employeemanagement;

public class UploadFile {
    String file_name,file_type,file_url;
    String doc_name,doc_uri,Name,Gmail;
    String header,footer;

    public UploadFile() {
    }

    public UploadFile(String header, String footer) {
        this.header = header;
        this.footer = footer;
    }

    public UploadFile(String file_name, String file_type, String file_url) {
        this.file_name = file_name;
        this.file_type = file_type;
        this.file_url = file_url;
    }

    public UploadFile(String doc_name, String doc_uri, String name, String gmail) {
        this.doc_name = doc_name;
        this.doc_uri = doc_uri;
        Name = name;
        Gmail = gmail;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGmail() {
        return Gmail;
    }

    public void setGmail(String gmail) {
        Gmail = gmail;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDoc_uri() {
        return doc_uri;
    }

    public void setDoc_uri(String doc_uri) {
        this.doc_uri = doc_uri;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
