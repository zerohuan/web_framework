package com.yjh.cg.site.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * User Entity
 *
 * Created by yjh on 15-10-9.
 */
@Entity
@Table(name = "b_user", schema = "", catalog = "cg")
public class BUserEntity {
    private long id;
    private Timestamp birthday;
    private String cooperation;
    private Timestamp createTime;
    private String department;
    private String email;
    private Long headImg;
    private String idNumber;
    private Timestamp modifyTime;
    private String password;
    private String phone;
    private String role;
    private String school;
    private Integer sex;
    private Long teacherId;
    private String username;
    private String qq;
    private String realName;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "birthday")
    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "cooperation")
    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "department")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "headImg")
    public Long getHeadImg() {
        return headImg;
    }

    public void setHeadImg(Long headImg) {
        this.headImg = headImg;
    }

    @Basic
    @Column(name = "idNumber")
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Basic
    @Column(name = "modify_time")
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "school")
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "teacherId")
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "qq")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Basic
    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BUserEntity that = (BUserEntity) o;

        if (id != that.id) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (cooperation != null ? !cooperation.equals(that.cooperation) : that.cooperation != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (department != null ? !department.equals(that.department) : that.department != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (headImg != null ? !headImg.equals(that.headImg) : that.headImg != null) return false;
        if (idNumber != null ? !idNumber.equals(that.idNumber) : that.idNumber != null) return false;
        if (modifyTime != null ? !modifyTime.equals(that.modifyTime) : that.modifyTime != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (teacherId != null ? !teacherId.equals(that.teacherId) : that.teacherId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (qq != null ? !qq.equals(that.qq) : that.qq != null) return false;
        if (realName != null ? !realName.equals(that.realName) : that.realName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (cooperation != null ? cooperation.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (headImg != null ? headImg.hashCode() : 0);
        result = 31 * result + (idNumber != null ? idNumber.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (qq != null ? qq.hashCode() : 0);
        result = 31 * result + (realName != null ? realName.hashCode() : 0);
        return result;
    }
}
