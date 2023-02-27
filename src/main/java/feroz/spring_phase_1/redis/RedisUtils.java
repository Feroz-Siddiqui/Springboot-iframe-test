package feroz.spring_phase_1.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Faker;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtils {
	static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	private static RedisUtils redisDB;
	private static JedisPool pool;
	private static Object staticLock = new Object();
    private static JedisPoolConfig config;

	private RedisUtils() throws JedisConnectionException {
		pool = getPoolInstance();
	}

	public static JedisPool getPoolInstance() {
		if (pool == null) { // avoid synchronization lock if initialization has already happened
			synchronized (staticLock) {
				if (pool == null) { // don't re-initialize if another thread beat us to it.
					JedisPoolConfig poolConfig = getPoolConfig();
					
					pool = new JedisPool(poolConfig, "0.0.0.0", 6379, 10000,"test123");

				}
			}
		}
		return pool;
	}

	
	  public static String getPoolCurrentUsage()
	    {
	        JedisPool jedisPool = getPoolInstance();
	        JedisPoolConfig poolConfig = getPoolConfig();

	        int active = jedisPool.getNumActive();
	        int idle = jedisPool.getNumIdle();
	        int total = active + idle;
	        String log = String.format(
	                "JedisPool: Active=%d, Idle=%d, Waiters=%d, total=%d, maxTotal=%d, minIdle=%d, maxIdle=%d",
	                active,
	                idle,
	                jedisPool.getNumWaiters(),
	                total,
	                poolConfig.getMaxTotal(),
	                poolConfig.getMinIdle(),
	                poolConfig.getMaxIdle()
	        );

	        return log;
	    }
	private static JedisPoolConfig getPoolConfig() {
		if(config ==null) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

		// Each thread trying to access Redis needs its own Jedis instance from the
		// pool.
		// Using too small a value here can lead to performance problems, too big and
		// you have wasted resources.
		int maxConnections = 3000;
		jedisPoolConfig.setMaxTotal(maxConnections);
		jedisPoolConfig.setMaxIdle(maxConnections);

		// Using "false" here will make it easier to debug when your
		// maxTotal/minIdle/etc settings need adjusting.
		// Setting it to "true" will result better behavior when unexpected load hits in
		// production
		jedisPoolConfig.setBlockWhenExhausted(true);

		// This controls the number of connections that should be maintained for bursts
		// of load.
		// Increase this value when you see pool.getResource() taking a long time to
		// complete under burst scenarios
		jedisPoolConfig.setMinIdle(50);
		jedisPoolConfig.setMaxWaitMillis(2000);

		jedisPoolConfig.setTestOnBorrow(true);
		RedisUtils.config = jedisPoolConfig;
		}
        return config;

	}

	public static synchronized RedisUtils getInstance() {
		if (redisDB == null) {
			redisDB = new RedisUtils();
		}
		return redisDB;
	}

	public String insertData(String key, String value) {
		String insertedId = "";

		insertedId = insertData(key, value, 24 * 60 * 60l);

		return insertedId;
	}

	public String insertData(String key, String value, long ex) {
		String insertedId = "";
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setex(key, (int) ex, value);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return insertedId;
	}

	public String retrieveData(String key) {
		String value = "";
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return value;
	}

	public static void main(String[] args) {
		
		long starTime = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(3300);
		Faker faker = Faker.instance();
		List<Callable<String>> tasks = new ArrayList<>();

		for (int i = 0; i < 200000; i++) {
			tasks.add(new RedisThread(faker));
		}

		try {
			executorService.invokeAll(tasks);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		if (executorService.isShutdown()) {

		}
		
		System.out.println(System.currentTimeMillis() - starTime);

	}
	
	  

}
