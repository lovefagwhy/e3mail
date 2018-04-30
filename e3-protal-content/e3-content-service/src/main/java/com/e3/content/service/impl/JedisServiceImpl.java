package com.e3.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3.content.service.JedisService;

import redis.clients.jedis.JedisCluster;
@Service
public class JedisServiceImpl implements JedisService {
	// 注入集群对象
		@Autowired
		private JedisCluster jedisCluster;

		@Override
		public String set(String key, String value) {
			String str = jedisCluster.set(key, value);
			return str;
		}

		@Override
		public String get(String key) {
			String str = jedisCluster.get(key);
			return str;
		}

		@Override
		public Long hset(String key, String field, String value) {
			Long hset = jedisCluster.hset(key, field, value);
			return hset;
		}

		@Override
		public String hget(String key, String field) {
			String str = jedisCluster.hget(key, field);
			return str;
		}

		@Override
		public Long hdel(String key, String fields) {
			Long hdel = jedisCluster.hdel(key, fields);
			return hdel;
		}

		@Override
		public Long expire(String key, int seconds) {
			Long expire = jedisCluster.expire(key, seconds);
			return expire;
		}

		@Override
		public Long ttl(String key) {
			Long ttl = jedisCluster.ttl(key);
			return ttl;
		}
}
