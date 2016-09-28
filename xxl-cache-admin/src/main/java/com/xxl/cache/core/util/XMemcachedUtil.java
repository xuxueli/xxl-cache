package com.xxl.cache.core.util;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import static com.xxl.cache.core.util.PropertiesUtil.DEFAULT_CONFIG;

/**
 * Memcached客户端工具类(Base on xmemcached)
 * @author xuxueli $2015-4-14 20:13:11
 *
 * 	<!-- xmemcached -->
 * 	<dependency>
 *		<groupId>com.googlecode.xmemcached</groupId>
 * 		<artifactId>xmemcached</artifactId>
 * 		<version>2.0.0</version>
 * 	</dependency>
 *
 * 	NIO,连接池,节点主备配置,客户端分布式配置,权重
 */
public final class XMemcachedUtil {
	private static Logger logger = LogManager.getLogger();
	
	// MemcachedClient
	private static final int DEFAULT_EXPIRE_TIME = 7200;	// 2H
	private static MemcachedClient memcachedClient;
	private static ReentrantLock INSTANCE_INIT_LOCK = new ReentrantLock(true);
	private static MemcachedClient getInstance() {
        if (memcachedClient == null) {
            // 构建分布式权重client
            try {
                if (INSTANCE_INIT_LOCK.tryLock(2, TimeUnit.SECONDS)) {
					try {
						// 构建分布式权重client
						Properties prop = PropertiesUtil.loadProperties(DEFAULT_CONFIG);
						// client地址
						String serverAddress = PropertiesUtil.getString(prop, "xmemcached.address");
						serverAddress = serverAddress.replaceAll(",", " ");

						// 连接池：高负载下nio单连接有瓶颈,设置连接池可分担memcached请求负载,从而提高系统吞吐量
						MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddressMap(serverAddress));
						builder.setConnectionPoolSize(5);	// 设置连接池大小，即客户端个数 NIO
						builder.setFailureMode(true);		// 宕机报警
						builder.setSessionLocator(new KetamaMemcachedSessionLocator());	//  分布策略:一致性哈希

						// 客户端
						try {
							memcachedClient = builder.build();
							memcachedClient.setPrimitiveAsString(true);	//
							memcachedClient.setConnectTimeout(3000L);	// 连接超时
							memcachedClient.setOpTimeout(1500L);		// 全局等待时间
							//memcachedClient.addStateListener(new MemcachedListener());
						} catch (IOException e) {
							logger.error("", e);
						}
						logger.info(">>>>>>>>>>> xxl-cache, JedisUtil.ShardedJedisPool init success.");
					} finally {
						INSTANCE_INIT_LOCK.unlock();
					}
                }
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
		if (memcachedClient == null) {
			throw new NullPointerException(">>>>>>>>>>> xxl-cache, XMemcachedUtil.memcachedClient is null.");
		}
		return memcachedClient;
	}
	
	/**
	 * 存储 (默认7200秒)
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 */
	public static void set(String key, Object value) {
		try {
			getInstance().set(key, DEFAULT_EXPIRE_TIME, value);
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 存储
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 * @param expTime	:expire时间 (单位秒,超过这个时间,memcached将这个数据替换出,0表示永久存储(默认是一个月))
	 */
	public static void set(String key, Object value, int expTime) {
		try {
			getInstance().set(key, expTime, value);
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
	}

	/**
	 * Cas	原子性Set
	 * @param key
	 * @param value
	 * @param expTime
	 * @param cas
	 */
	public static boolean cas(String key, Object value, int expTime, long cas) {
		try {
			return getInstance().cas(key, expTime, value, cas);
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
		return false;
	}
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		try {
			Object value = getInstance().get(key);
			return value;
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
		
		return null;
	}

	/**
	 * 查询		和Cas匹配使用,可查询缓存Cas版本
	 * @param key
	 * @return
     */
	public static GetsResponse<Object> gets(String key){
		try {
			GetsResponse<Object> response = getInstance().gets(key);
			return response;
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 删除
	 * @param key
	 */
	public static boolean delete(String key) {
		try {
			return getInstance().delete(key);
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
		return false;
	}

	/**
	 * 递增
	 * @param key
	 */
	public static void incr(String key) {
		try {			
			getInstance().incr(key, 1);
		} catch (TimeoutException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (MemcachedException e) {
			logger.error("", e);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		String key = "key02";

		List<String> list = new ArrayList<String>();
		list.add("jack");
		list.add("rose");

		set(key, list);
		System.out.println(get(key));

		GetsResponse<Object> val = gets(key);
		System.out.println(val);

		System.out.println(cas(key, "val2222", DEFAULT_EXPIRE_TIME, 99L));
		System.out.println(get(key));

		System.out.println(cas(key, "val2222", DEFAULT_EXPIRE_TIME, val.getCas()));
		System.out.println(get(key));
	}

}
