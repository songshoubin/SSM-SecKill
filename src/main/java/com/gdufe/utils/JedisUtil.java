package com.gdufe.utils;

import java.util.Collections;

import redis.clients.jedis.Jedis;

/**
 * redis实现分布式锁
 * @author song
 *
 */
public class JedisUtil {

	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";
	private static final Long RELEASR_SUCCESS = 1L;
	
	/**
	 * 获取分布式锁
	 * @param jedis
	 * @param lockKey
	 * @param requstId
	 * @param expireTime
	 * @return
	 */
	public static boolean tryLock(Jedis jedis ,String lockKey,String requestId,int expireTime){
		String result = jedis .set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime);
		return LOCK_SUCCESS.equals(result);
	}
	
	/**
	 * 释放锁
	 * 用简单的Lua脚本代码确保原子性
	 * @param jedis
	 * @param lockKey
	 * @param requestId
	 * @return
	 */
	public static boolean releaseLock(Jedis jedis,String lockKey,String requestId){
		String script = "if redis.call('get',KEYS[1]==ARGV[1]) then return redis.call('del',KEYS[1]) else return 0 end";
		Object result = jedis.eval(script,Collections.singletonList(lockKey),Collections.singletonList(requestId));
		if(RELEASR_SUCCESS.equals(result))
			return true;
		else
			return false;
	}
}
