package com.enterprise.platform.inventorymanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Person数据传输对象 - 用于前后端数据交互
 * 
 * @author Enterprise Platform
 * @version 1.0
 * @since 2025
 */
public class PersonDTO {

    @NotBlank(message = "人员代码不能为空")
    @Size(max = 20, message = "人员代码长度不能超过20个字符")
    private String personCode;

    @Size(max = 40, message = "人员名称长度不能超过40个字符")
    private String personName;

    @NotBlank(message = "部门代码不能为空")
    @Size(max = 12, message = "部门代码长度不能超过12个字符")
    private String departmentCode;

    @Size(max = 20, message = "人员属性长度不能超过20个字符")
    private String personProperty;

    @DecimalMin(value = "0.0", message = "信用数量不能小于0")
    private Double creditQuantity;

    private Integer createDate;

    @Size(max = 6, message = "信用等级长度不能超过6个字符")
    private String createGrade;

    @DecimalMin(value = "0.0", message = "最低比率不能小于0")
    @DecimalMax(value = "100.0", message = "最低比率不能大于100")
    private Double lowRate;

    @Size(max = 20, message = "报价等级长度不能超过20个字符")
    private String offerGrade;

    @DecimalMin(value = "0.0", message = "报价比率不能小于0")
    @DecimalMax(value = "100.0", message = "报价比率不能大于100")
    private Double offerRate;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String personEmail;

    @Pattern(regexp = "^1[3-9]\\d{9}$|^\\d{3,4}-\\d{7,8}$", message = "电话号码格式不正确")
    @Size(max = 100, message = "电话长度不能超过100个字符")
    private String personPhone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invalidDate;

    // 部门名称（用于显示）
    private String departmentName;

    // 构造函数
    public PersonDTO() {}

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "personCode='" + personCode + '\'' +
                ", personName='" + personName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", personEmail='" + personEmail + '\'' +
                ", personPhone='" + personPhone + '\'' +
                '}';
    }
}