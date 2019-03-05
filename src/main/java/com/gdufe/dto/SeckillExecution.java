package com.gdufe.dto;
/**
 * 封装执行后结果
 * @author song
 *
 */

import com.gdufe.entity.SuccessKilled;
import com.gdufe.enums.SeckillStatEnum;
/**
 * 
 * @author song
 *
 */
public class SeckillExecution{
	private long seckillId;
	
	//秒杀执行结果状态
	private int state;
	//状态标示
	private String stateInfo;
	//秒杀成功对象
	private SuccessKilled successKilled;
	
	//秒杀成功返回所有信息
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
        this.successKilled = successKilled;
    }

    //秒杀失败
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getInfo();
    }


	public long getSeckillId() {
		return seckillId;
	}


	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}
	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	
	
}
