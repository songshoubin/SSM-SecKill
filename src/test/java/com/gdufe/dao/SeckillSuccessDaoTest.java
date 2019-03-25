package com.gdufe.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdufe.entity.SuccessKilled;

/**
 * 
 * @author song
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SeckillSuccessDaoTest {

	@Autowired
	private SuccessKilledMapper successKilledMapper;
	
	 	@Test
	    public void insertSuccessKilled() throws Exception {

	        long seckillId=1001L;
	        long userPhone=13476191877L;
	        int insertCount=successKilledMapper.insertSuccessKilled(seckillId,userPhone);
	        System.out.println("insertCount="+insertCount);
	    }

	    @Test
	    public void queryByIdWithSeckill() throws Exception {
	    	
	        long seckillId=1001L;
	        long userPhone=13476191877L;
	        SuccessKilled successKilled=successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
	        System.out.println(successKilled.toString());

	    }

}
