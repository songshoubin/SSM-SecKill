package com.gdufe.daoTest;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdufe.dao.SeckillMapper;
import com.gdufe.entity.Seckill;
import com.gdufe.entity.SeckillExample;
import com.gdufe.entity.SeckillExample.Criteria;

/**
 * Created by codingBoy on 16/11/27.
 * 配置spring和junit整合，这样junit在启动时就会加载spring容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillMapper seckillDao;


    @Test
    public void queryById() throws Exception {
        long seckillId=1001L;
        Seckill seckill=seckillDao.selectByPrimaryKey(seckillId);
        if(seckill!=null){
        System.out.println(seckill.getName());
        System.out.println(seckill.toString());
        }
        else{
        	System.out.println("空指针");
        }
    }

    @Test
    public void queryAll() throws Exception {
    	//使用Example动态查询
    	SeckillExample example = new SeckillExample(); 
    	//如果1005没有则不输出
    	Criteria criteria = example.createCriteria().andSeckillIdBetween(1001L, 1005L);
    	
        List<Seckill> seckills=seckillDao.selectByExample(example);
        for (Seckill seckill : seckills)
        {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
    	/*//使用Example动态查询
    	SeckillExample example = new SeckillExample(); 
    	long seckillId=1001;
    	Seckill seckill = new Seckill();
    	seckill.setSeckillId(seckillId);
        Date date=new Date();
    	Criteria criteria = example.createCriteria();
    	criteria.andSeckillIdEqualTo(seckillId);
        criteria.andStartTimeLessThan(date);
        criteria.andEndTimeGreaterThan(date);*/
    	long seckillId=1001;
    	Date date = new Date();
        int updateCount=seckillDao.reduceNumber(seckillId, date);
        System.out.println(updateCount);

    }


}