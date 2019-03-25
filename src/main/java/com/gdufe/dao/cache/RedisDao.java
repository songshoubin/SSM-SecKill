package com.gdufe.dao.cache;

import java.util.UUID;
import java.util.function.Function;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.gdufe.entity.Seckill;
import com.gdufe.utils.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 这个序列化用了第三方的jar包
 * protostuff
 * 相比如java自身的序列化，他序列的时间更加短
 * 字节更加少
 * 
 * @author song
 */

public class RedisDao {
	
	public RedisDao() {
		// TODO Auto-generated constructor stub
	}

	private JedisPool jedisPool;
	
	private RuntimeSchema<Seckill> schema = 
			RuntimeSchema.createFrom(Seckill.class);

	
	public RedisDao(JedisPoolConfig config, String ip, int port) {
		jedisPool = new JedisPool(config, ip, port, 1000);
	}

	public Seckill getSeckill(long seckillId) {
		/**
		 * 获取redis缓存
		 * 拿到的是一串二进制数组，然后进行反序列化
		 * 采用自定义的序列化
		 * protostuff
		 */
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					// seckill被反序列化
					return seckill;
				}
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} 
		
		return null;
	}

	/**
	 * 运用redis实现的分布式锁，如果redis中没有数据，则从数据库中取出数据
	 * @param seckillId
	 * @param getDataFromDb
	 * @return
	 */
	public Seckill getOrPutSeckill(Long seckillId, Function<Long, Seckill> getDataFromDb){
		Jedis jedis = jedisPool.getResource();
		String lockKey = "seckill:locks:getSeckill:"+seckillId;//锁住数据库表中的某个id
		String requestId = UUID.randomUUID().toString();//生成UUID唯一值
		try {
			while(true){//循环直到获取数据
				Seckill seckill = getSeckill(seckillId);//从redis中获取seckill
				if(seckill!=null)
					return seckill;
				//从数据库中获取seckill
				//尝试获取锁
				Boolean getLock = JedisUtil.tryLock(jedis, lockKey, requestId, 1000);
				if(getLock){
					seckill = getDataFromDb.apply(seckillId);
					putSeckill(seckill);//将seckill存放在redis中
					return seckill;
				}
				// 获取不到锁，睡一下，等会再出发。sleep的时间需要斟酌，主要看业务处理速度
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			JedisUtil.releaseLock(jedis, lockKey, requestId);
			jedis.close();
		}
		return null;
	}
	/**
	 * 从数据库获取到seckill并将其存进redis中
	 * @param seckill
	 * @return
	 */
	public String putSeckill(Seckill seckill) {
		// set Object(Seckill) -> 序列号 -> byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 超时缓存
				int timeout = 60 * 60; //3600s 即一个小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}