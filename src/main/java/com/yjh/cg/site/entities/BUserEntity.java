package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.nio.file.attribute.UserPrincipal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User Entity
 *
 * Created by yjh on 15-10-9.
 */
@Entity
@Table(name = "b_user", schema = "", catalog = "cg")
public class BUserEntity extends BAuditedEntity
        implements Authentication, UserPrincipal {

    private Date birthday;
    private String cooperation;
    private String department;
    private String email;
    private Long headImg;
    private String idNumber;
    private byte[] password;
    private String phone;
    private BUserRole role;
    private String school;
    private Integer sex;
    private Long teacherId;
    private String username;
    private String qq;
    private String realName;
    private List<BCourseEntity> courses;

    private boolean authenticated;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @Transient
    public Object getCredentials() {
        return password;
    }

    @Override
    @Transient
    public Object getDetails() {
        return username;
    }

    @Override
    @Transient
    public Object getPrincipal() {
        return username;
    }

    @Override
    @Transient
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Arrays.asList();
    }

    @Override
    @Transient
    public String getName() {
        return username;
    }

    @Transient
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "b_course_user",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    public List<BCourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(List<BCourseEntity> courses) {
        this.courses = courses;
    }

    @Basic
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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
    @Column(name = "password")
    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
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
    @Enumerated(EnumType.STRING)
    public BUserRole getRole() {
        return role;
    }

    public void setRole(BUserRole role) {
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
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BUserEntity &&
                ((BUserEntity)other).username.equals(this.username);
    }

    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    protected BUserEntity clone()
    {
        try {
            return (BUserEntity)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString()
    {
        return this.username;
    }
}
