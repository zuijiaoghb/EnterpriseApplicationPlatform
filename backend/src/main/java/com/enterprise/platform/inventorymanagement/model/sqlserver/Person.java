package com.enterprise.platform.inventorymanagement.model.sqlserver;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * Person实体类 - 对应数据库中的Person表
 * 用于存储人员基本信息
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "cPersonCode", length = 20, nullable = false)
    private String personCode;

    @Column(name = "cPersonName", length = 40)
    private String personName;

    @Column(name = "cDepCode", length = 12, nullable = false)
    private String departmentCode;

    @Column(name = "cPersonProp", length = 20)
    private String personProperty;

    @Column(name = "fCreditQuantity")
    private Double creditQuantity;

    @Column(name = "iCreDate")
    private Integer createDate;

    @Column(name = "cCreGrade", length = 6)
    private String createGrade;

    @Column(name = "iLowRate")
    private Double lowRate;

    @Column(name = "cOfferGrade", length = 20)
    private String offerGrade;

    @Column(name = "iOfferRate")
    private Double offerRate;

    @Version
    @Column(name = "pubufts",insertable = false,updatable = false)
    private byte[] timestamp;

    @Column(name = "cPersonEmail", length = 100)
    private String personEmail;

    @Column(name = "cPersonPhone", length = 100)
    private String personPhone;

    @Column(name = "dPValidDate")
    private LocalDateTime validDate;

    @Column(name = "dPInValidDate")
    private LocalDateTime invalidDate;

    // 构造函数
    public Person() {}

    public Person(String personCode, String personName, String departmentCode) {
        this.personCode = personCode;
        this.personName = personName;
        this.departmentCode = departmentCode;
    }

    // Getter和Setter方法
    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getPersonProperty() {
        return personProperty;
    }

    public void setPersonProperty(String personProperty) {
        this.personProperty = personProperty;
    }

    public Double getCreditQuantity() {
        return creditQuantity;
    }

    public void setCreditQuantity(Double creditQuantity) {
        this.creditQuantity = creditQuantity;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public String getCreateGrade() {
        return createGrade;
    }

    public void setCreateGrade(String createGrade) {
        this.createGrade = createGrade;
    }

    public Double getLowRate() {
        return lowRate;
    }

    public void setLowRate(Double lowRate) {
        this.lowRate = lowRate;
    }

    public String getOfferGrade() {
        return offerGrade;
    }

    public void setOfferGrade(String offerGrade) {
        this.offerGrade = offerGrade;
    }

    public Double getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(Double offerRate) {
        this.offerRate = offerRate;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public LocalDateTime getValidDate() {
        return validDate;
    }

    public void setValidDate(LocalDateTime validDate) {
        this.validDate = validDate;
    }

    public LocalDateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(LocalDateTime invalidDate) {
        this.invalidDate = invalidDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personCode='" + personCode + '\'' +
                ", personName='" + personName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", personEmail='" + personEmail + '\'' +
                ", personPhone='" + personPhone + '\'' +
                '}';
    }
}