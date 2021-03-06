package com.gdufe.service.Impl;

import java.util.Date;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.gdufe.dao.SeckillMapper;
import com.gdufe.dao.SuccessKilledMapper;
import com.gdufe.dao.cache.RedisDao;
import com.gdufe.dto.Exposer;
import com.gdufe.dto.SeckillExecution;
import com.gdufe.entity.Seckill;
import com.gdufe.entity.SuccessKilled;
import com.gdufe.enums.SeckillStatEnum;
import com.gdufe.exception.RepeatKillException;
import com.gdufe.exception.SeckillCloseException;
import com.gdufe.exception.SeckillException;
import com.gdufe.service.SeckillService;
/**
 * @author song
 * 
 */
@Service
public class SeckillServiceImpl implements SeckillService{
	//日志对象
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillMapper seckillDao;
	
	@Autowired
	private SuccessKilledMapper successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	private final String salt = "jglhlghlsghel/;lkk";
	@Override
	public List<Seckill> getAllSeckill() {
		return seckillDao.queryAll(0,4);
	}

	@Override
	public Seckill getById(long seckillId) {//快到接口暴露时间点，会发生高并发
		return redisDao.getOrPutSeckill(seckillId, id -> seckillDao.queryById(id));
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		/**
		 * 
		 * 缓存优化1 ：将商品放到redis缓存中去
		 *  
		 * get from cache
		 * if null
		 * 	  get db
		 *	  put cache
		 */
		Seckill seckill = getById(seckillId);//调用本类的getById方法  高并发发生接口
		
		//如果seckill为空 
		if(seckill==null){
			return new Exposer(false, seckillId);
		}
		
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		//如果秒杀还没开始或者已经结束了
		if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
			return new Exposer(false,seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMd5(seckillId);
		return new Exposer(true, md5,seckillId);
		
		
	}

	//生成MD5
	private String getMd5(long seckillId){
		String base = seckillId+"/"+salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	 //秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    @Override
    @Transactional//更新数据库需要添加事务管理
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    public SeckillExecution executeseckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            //秒杀数据被重写了
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存+增加购买明细
        Date nowTime = new Date();

        try {
        	 //否则更新了库存，秒杀成功,增加明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //看是否该明细被重复插入，即用户是否重复秒杀
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {

                //减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新库存记录，说明秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }

            }


        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所以编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }

    }
}


