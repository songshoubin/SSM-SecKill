package com.gdufe.service;

import java.util.List;

import com.gdufe.dto.Exposer;
import com.gdufe.dto.SeckillExecution;
import com.gdufe.entity.Seckill;

/**
 * 设计业务接口
 * 站在使用者角度
 * 
 * @author song
 *
 */

public interface SeckillService {
	//查询所有秒杀记录
	List<Seckill> getAllSeckill();
	
	//查询单个秒杀记录
	Seckill getById(long seckillId);
	
	//开启秒杀地址
	Exposer exportSeckillUrl(long seckillId);
	//执行秒杀操作
	SeckillExecution executeseckill(long seckillId,long userPhone,String md5);
	
}
