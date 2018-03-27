package com.hyb.redis;

/**
 * 用户实体类
 * Created by HuangYibo on 2018/3/26.
 */
public class User {
    /**
     *  用户id
     */
    private String userId;

    /**
     *  用户账号
     */
    private String EmpCode;

    /**
     *  用户名称
     */
    private String EmpName;

    /**
     *  用户角色
     */
    private String role;

    /**
     *  职位
     */
    private String title;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
