package com.content.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTest {
	@Test
	public void demo01(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(2);
		config.setMaxTotal(20);
		Set<HostAndPort> set = new HashSet<>();
		set.add(new HostAndPort("192.168.199.128", 7001));
		set.add(new HostAndPort("192.168.199.128", 7002));
		set.add(new HostAndPort("192.168.199.128", 7003));
		set.add(new HostAndPort("192.168.199.128", 7004));
		set.add(new HostAndPort("192.168.199.128", 7005));
		set.add(new HostAndPort("192.168.199.128", 7006));
		set.add(new HostAndPort("192.168.199.128", 7007));
		set.add(new HostAndPort("192.168.199.128", 7008));
		JedisCluster cluster = new JedisCluster(set,config);
		cluster.set("address", "nanjing");
		String address = cluster.get("address");
		System.out.println(address);
	}
}
