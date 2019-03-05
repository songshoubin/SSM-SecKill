package com.gdufe.entity;

import java.util.Date;
/**
 * 
 * @author song
 *
 */
public class SuccessKilled {
    private Byte state;

    private Date creatTime;

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
    
    public String toString() {
        return "SuccessKilled{" +
                "state=" + state +
                ", createTime=" + creatTime +
                '}';
    }
}