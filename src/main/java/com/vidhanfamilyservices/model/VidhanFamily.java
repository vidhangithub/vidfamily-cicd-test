package com.vidhanfamilyservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class VidhanFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyMemberId;
    private String familyMemberName;
    private String passportNumber;
    private LocalDate dob;
    public VidhanFamily() {
    }

    public VidhanFamily(Long familyMemberId, String familyMemberName, String passportNumber, LocalDate dob) {
        this.familyMemberId = familyMemberId;
        this.familyMemberName = familyMemberName;
        this.passportNumber = passportNumber;
        this.dob = dob;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public String getFamilyMemberName() {
        return familyMemberName;
    }

    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
