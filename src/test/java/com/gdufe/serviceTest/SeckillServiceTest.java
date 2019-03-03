package com.gdufe.serviceTest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdufe.dto.Exposer;
import com.gdufe.dto.SeckillExecution;
import com.gdufe.entity.Seckill;
import com.gdufe.exception.RepeatKillException;
import com.gdufe.exception.SeckillCloseException;
import com.gdufe.service.SeckillService;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by codingBoy on 16/11/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:applicationContext.xml"})

public class SeckillServiceTest {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills=seckillService.getAllSeckill();
        System.out.println(seckills);

    }

    @Test
    public void getById() throws Exception {

        long seckillId=1001;
        Seckill seckill=seckillService.getById(seckillId);
        System.out.println(seckill);
    }

    @Test//完整逻辑代码测试，注意可重复执行
    public void testSeckillLogic() throws Exception {
        long seckillId=1002;
        //开放接口
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed())
        {

            System.out.println(exposer);

            long userPhone=13476191875L;
            String md5=exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeseckill(seckillId, userPhone, md5);
                System.out.println(seckillExecution);
            }catch (RepeatKillException e)
            {
                e.printStackTrace();
            }catch (SeckillCloseException e1)
            {
                e1.printStackTrace();
            }
        }else {
            //秒杀未开启
            System.out.println(exposer);
        }
    }


}