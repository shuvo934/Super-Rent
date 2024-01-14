package com.shuvo.rft.superrent.login;

public class UserInfoList {
    private String user_id;
    private String user_name;
    private String password;
    private String description;
    private String user_grp;
    private String status;
    private String employee_id;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
    private String location;
    private String user_type;
    private String emp_id;

    public UserInfoList(String user_id, String user_name, String password, String description, String user_grp, String status, String employee_id, String create_by, String create_date, String update_by, String update_date, String location, String user_type, String emp_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
        this.description = description;
        this.user_grp = user_grp;
        this.status = status;
        this.employee_id = employee_id;
        this.create_by = create_by;
        this.create_date = create_date;
        this.update_by = update_by;
        this.update_date = update_date;
        this.location = location;
        this.user_type = user_type;
        this.emp_id = emp_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_grp() {
        return user_grp;
    }

    public void setUser_grp(String user_grp) {
        this.user_grp = user_grp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
