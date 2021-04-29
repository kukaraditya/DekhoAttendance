package com.example.dekhoattendance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeavetypeModel {


    private String id;
    private String leaveType;
    private String num_of_leave;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getNum_of_leave() {
        return num_of_leave;
    }

    public void setNum_of_leave(String num_of_leave) {
        this.num_of_leave = num_of_leave;
    }
}
