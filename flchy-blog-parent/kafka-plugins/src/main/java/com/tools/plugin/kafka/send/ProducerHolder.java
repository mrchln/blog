package com.tools.plugin.kafka.send;

import java.util.Map;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.util.KafkaUtil;

public class ProducerHolder {

	/**
	 * 字符串消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param timestamp :消息的时间戳
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, Long timestamp, String key, String value, Map<String, Object> mapProps) {
		Producer<String, String> producer = KafkaUtil.getStringProducer(mapProps);
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, partition, timestamp, key, value);
		producer.send(record);
	}
	
	/**
	 * 字符串消息发送
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param timestamp :消息的时间戳
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, Long timestamp, String key, String value) {
		send( topic,  partition,  timestamp,  key,  value, null);
	}
	
	/**
	 * 字符串消息发送
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, String key, String value) {
		send(topic, partition, null, key, value);
	}

	/**
	 * 字符串消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, String key, String value, Map<String, Object> mapProps) {
		send(topic, partition, null, key, value, mapProps);
	}
	
	/**
	 * 字符串消息发送
	 * @param topic     ：topic主题
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String key, String value) {
		send(topic, null, null, key, value);
	}
	
	/**
	 * 字符串消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String key, String value, Map<String, Object> mapProps) {
		send(topic, null, null, key, value,  mapProps);
	}

	/**
	 * 字符串消息发送
	 * @param topic     ：topic主题
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String value) {
		send(topic, null, null, null, value);
	}
	
	/**
	 * 字符串消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String value, Map<String, Object> mapProps) {
		send(topic, null, null, null, value, mapProps);
	}
	
	/**
	 * JsonElement消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param timestamp :消息的时间戳
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, Long timestamp, String key, JsonElement<String, Object, Object> value, Map<String, Object> mapProps) {
		Producer<String, JsonElement<String, Object, Object>> producer = KafkaUtil.getJsonEleProducer(mapProps);
		ProducerRecord<String, JsonElement<String, Object, Object>> record = new ProducerRecord<String, JsonElement<String, Object, Object>>(topic, partition, timestamp, key, value);
		producer.send(record);
	}
	
	/**
	 * JsonElement消息发送
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param timestamp :消息的时间戳
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, Long timestamp, String key, JsonElement<String, Object, Object> value) {
		send(topic, partition, timestamp, key, value, null);
	}

	/**
	 * JsonElement消息发送
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, String key, JsonElement<String, Object, Object> value) {
		send(topic, partition, null, key, value);
	}
	
	/**
	 * JsonElement消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param partition :指定消息分片
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, Integer partition, String key, JsonElement<String, Object, Object> value, Map<String, Object> mapProps) {
		send(topic, partition, null, key, value, mapProps);
	}

	/**
	 * JsonElement消息发送
	 * @param topic     ：topic主题
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String key, JsonElement<String, Object, Object> value) {
		send(topic, null, null, key, value);
	}
	
	/**
	 * JsonElement消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param key       ：分片内容
	 * @param value     ：消息内容
	 */
	public static void send(String topic, String key, JsonElement<String, Object, Object> value, Map<String, Object> mapProps) {
		send(topic, null, null, key, value, mapProps);
	}

	/**
	 * JsonElement消息发送
	 * @param topic     ：topic主题
	 * @param value     ：消息内容
	 */
	public static void send(String topic, JsonElement<String, Object, Object> value) {
		send(topic, null, null, null, value);
	}
	
	/**
	 * JsonElement消息发送;自定义属性
	 * @param topic     ：topic主题
	 * @param value     ：消息内容
	 */
	public static void send(String topic, JsonElement<String, Object, Object> value, Map<String, Object> mapProps) {
		send(topic, null, null, null, value, mapProps);
	}

}
