package com.gdufe.dao;

import com.gdufe.entity.Seckill;
import com.gdufe.entity.SeckillExample;

import java.util.Date;
import java.util.List;


import org.apache.ibatis.annotations.Param;

public interface SeckillMapper {
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime")Date killTime);//手写方法
    long countByExample(SeckillExample example);

    int deleteByExample(SeckillExample example);

    int deleteByPrimaryKey(Long seckillId);

    int insert(Seckill record);

    int insertSelective(Seckill record);

    List<Seckill> selectByExample(SeckillExample example);

    Seckill selectByPrimaryKey(Long seckillId);

    int updateByExampleSelective(@Param("record") Seckill record, @Param("example") SeckillExample example);

    int updateByExample(@Param("record") Seckill record, @Param("example") SeckillExample example);

    int updateByPrimaryKeySelective(Seckill record);

    int updateByPrimaryKey(Seckill record);
}