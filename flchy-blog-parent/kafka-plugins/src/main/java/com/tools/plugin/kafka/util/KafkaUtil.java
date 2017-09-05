package com.tools.plugin.kafka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.topic.TopicHolder;

public class KafkaUtil {

	public static final String NAME_PRODUCER = "/producer.properties";
	public static final String NAME_CONSUMER = "/consumer.properties";
	public static final String NAME_DEFAULT ="default";

	private static Map<String, KafkaProducer<String, String>> strKafkaProducer= new HashMap<>();
	private static Map<String, KafkaConsumer<String, String>> strKafkaConsumer= new HashMap<>();

	private static Map<String, KafkaProducer<String, JsonElement<String, Object, Object>>> jsonKafkaProducer= new HashMap<>();
	private static Map<String, KafkaConsumer<String, JsonElement<String, Object, Object>>> jsonKafkaConsumer= new HashMap<>();

	public static Properties getProperties(String path) {
		Properties props = new Properties();
		try {
			InputStream ips = TopicHolder.class.getResourceAsStream(path);
			BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
			props.load(ipss);
			return props;
		} catch (IOException e) {
			System.out.println("读properties文件出错:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 生产者,默认属性;消息为字符串
	 * @return
	 */
	public static KafkaProducer<String, String> getStringProducer() {
		return getStringProducer(null);
	}
	
	/**
	 * 生产者,自定义属性;消息为字符串
	 * @param mapProps
	 * @return
	 */
	public static KafkaProducer<String, String> getStringProducer(Map<String, Object> mapProps) {
		KafkaProducer<String, String> kafkaProducer = null;
		if (MapUtil.isNullOrEmpty(mapProps)) {
			kafkaProducer = strKafkaProducer.get(NAME_DEFAULT);
			if (null == kafkaProducer) {
				Properties props = getProperties(NAME_PRODUCER);
				kafkaProducer = new KafkaProducer<String, String>(props);
				strKafkaProducer.put(NAME_DEFAULT, kafkaProducer);
			}
		} else {
			kafkaProducer = strKafkaProducer.get(mapProps.toString());
			if (null == kafkaProducer) {
				Properties props = getProperties(NAME_PRODUCER);
				for (Map.Entry<String, Object> entry : mapProps.entrySet()) {
					if (!StringUtil.isNullOrEmpty(String.valueOf(entry.getValue()))) {
						props.put(entry.getKey(), entry.getValue());
					}
				}
				kafkaProducer = new KafkaProducer<String, String>(props);
				strKafkaProducer.put(mapProps.toString(), kafkaProducer);
			}
		}
		return kafkaProducer;
	}

	/**
	 * 消费者,默认属性;消息为字符串
	 * @return
	 */
	public static KafkaConsumer<String, String> getStringConsumer() {
		return getStringConsumer(null);
	}
	
	/**
	 * 消费者,自定义属性;消息为字符串
	 * @param mapProps
	 * @return
	 */
	public static KafkaConsumer<String, String> getStringConsumer(Map<String, Object> mapProps) {
		KafkaConsumer<String, String> kafkaConsumer = null;
		if (MapUtil.isNullOrEmpty(mapProps)) {
			kafkaConsumer = strKafkaConsumer.get(NAME_DEFAULT);
			if (null == kafkaConsumer) {
				Properties props = getProperties(NAME_CONSUMER);
				kafkaConsumer = new KafkaConsumer<String, String>(props);
				strKafkaConsumer.put(NAME_DEFAULT, kafkaConsumer);
			}
		} else {
			kafkaConsumer = strKafkaConsumer.get(mapProps.toString());
			if (null == kafkaConsumer) {
				Properties props = getProperties(NAME_CONSUMER);
				for (Map.Entry<String, Object> entry : mapProps.entrySet()) {
					if (!StringUtil.isNullOrEmpty(String.valueOf(entry.getValue()))) {
						props.put(entry.getKey(), entry.getValue());
					}
				}
				kafkaConsumer = new KafkaConsumer<String, String>(props);
				strKafkaConsumer.put(mapProps.toString(), kafkaConsumer);
			}
		}
		return kafkaConsumer;
	}

	/**
	 * 生产者,默认属性;消息为JsonElement
	 * @return
	 */
	public static KafkaProducer<String, JsonElement<String, Object, Object>> getJsonEleProducer() {
		return getJsonEleProducer(null);
	}
	
	/**
	 * 生产者,自定义属性;消息为JsonElement
	 * @param mapProps
	 * @return
	 */
	public static KafkaProducer<String, JsonElement<String, Object, Object>> getJsonEleProducer(Map<String, Object> mapProps) {
		KafkaProducer<String, JsonElement<String, Object, Object>> kafkaProducer = null;
		if (MapUtil.isNullOrEmpty(mapProps)) {
			kafkaProducer = jsonKafkaProducer.get(NAME_DEFAULT);
			if (null == kafkaProducer) {
				Properties props = getProperties(NAME_PRODUCER);
				kafkaProducer = new KafkaProducer<String, JsonElement<String, Object, Object>>(props);
				jsonKafkaProducer.put(NAME_DEFAULT, kafkaProducer);
			}
		} else {
			kafkaProducer = jsonKafkaProducer.get(mapProps.toString());
			if (null == kafkaProducer) {
				Properties props = getProperties(NAME_PRODUCER);
				for (Map.Entry<String, Object> entry : mapProps.entrySet()) {
					if (!StringUtil.isNullOrEmpty(String.valueOf(entry.getValue()))) {
						props.put(entry.getKey(), entry.getValue());
					}
				}
				kafkaProducer = new KafkaProducer<String, JsonElement<String, Object, Object>>(props);
				jsonKafkaProducer.put(mapProps.toString(), kafkaProducer);
			}
		}
		return kafkaProducer;
		
	}

	/**
	 * 消费者,默认属性;消息为JsonElement
	 * @return
	 */
	public static KafkaConsumer<String, JsonElement<String, Object, Object>> getJsonEleConsumer() {
		return getJsonEleConsumer(null);
	}
	
	/**
	 * 消费者,自定义属性;消息为JsonElement
	 * @param mapProps
	 * @return
	 */
	public static KafkaConsumer<String, JsonElement<String, Object, Object>> getJsonEleConsumer(Map<String, Object> mapProps) {
		KafkaConsumer<String, JsonElement<String, Object, Object>> kafkaConsumer = null;
		if (MapUtil.isNullOrEmpty(mapProps)) {
			kafkaConsumer = jsonKafkaConsumer.get(NAME_DEFAULT);
			if (null == kafkaConsumer) {
				Properties props = getProperties(NAME_CONSUMER);
				kafkaConsumer = new KafkaConsumer<String, JsonElement<String, Object, Object>>(props);
				jsonKafkaConsumer.put(NAME_DEFAULT, kafkaConsumer);
			}
		} else {
			kafkaConsumer = jsonKafkaConsumer.get(mapProps.toString());
			if (null == kafkaConsumer) {
				Properties props = getProperties(NAME_CONSUMER);
				for (Map.Entry<String, Object> entry : mapProps.entrySet()) {
					if (!StringUtil.isNullOrEmpty(String.valueOf(entry.getValue()))) {
						props.put(entry.getKey(), entry.getValue());
					}
				}
				kafkaConsumer = new KafkaConsumer<String, JsonElement<String, Object, Object>>(props);
				jsonKafkaConsumer.put(mapProps.toString(), kafkaConsumer);
			}
		}
		return kafkaConsumer;
	}
}