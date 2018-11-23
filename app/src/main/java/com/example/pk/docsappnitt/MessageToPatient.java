package com.example.pk.docsappnitt;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageToPatient {
    private String doctorName,docId,ptName,ptId,ptGender,ptAge,ptAddress,ptMobile,ptBloodGroup,ptProblem,PharmaName,PharmId;
    private String DiagName,DiagId,Remarks;
    private ArrayList<String> DiagTests;
    private HashMap<String,MedicineClass>MedicineHashMap;
    private String subject,date,time;

    public MessageToPatient() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getPtGender() {
        return ptGender;
    }

    public void setPtGender(String ptGender) {
        this.ptGender = ptGender;
    }

    public String getPtAge() {
        return ptAge;
    }

    public void setPtAge(String ptAge) {
        this.ptAge = ptAge;
    }

    public String getPtAddress() {
        return ptAddress;
    }

    public void setPtAddress(String ptAddress) {
        this.ptAddress = ptAddress;
    }

    public String getPtMobile() {
        return ptMobile;
    }

    public void setPtMobile(String ptMobile) {
        this.ptMobile = ptMobile;
    }

    public String getPtBloodGroup() {
        return ptBloodGroup;
    }

    public void setPtBloodGroup(String ptBloodGroup) {
        this.ptBloodGroup = ptBloodGroup;
    }

    public String getPtProblem() {
        return ptProblem;
    }

    public void setPtProblem(String ptProblem) {
        this.ptProblem = ptProblem;
    }

    public String getPharmaName() {
        return PharmaName;
    }

    public void setPharmaName(String pharmaName) {
        PharmaName = pharmaName;
    }

    public String getPharmId() {
        return PharmId;
    }

    public void setPharmId(String pharmId) {
        PharmId = pharmId;
    }

    public HashMap<String, MedicineClass> getMedicineHashMap() {
        return MedicineHashMap;
    }

    public void setMedicineHashMap(HashMap<String, MedicineClass> medicineHashMap) {
        MedicineHashMap = medicineHashMap;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDiagName() {
        return DiagName;
    }

    public void setDiagName(String diagName) {
        DiagName = diagName;
    }

    public String getDiagId() {
        return DiagId;
    }

    public void setDiagId(String diagId) {
        DiagId = diagId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public ArrayList<String> getDiagTests() {
        return DiagTests;
    }

    public void setDiagTests(ArrayList<String> diagTests) {
        DiagTests = diagTests;
    }
}
