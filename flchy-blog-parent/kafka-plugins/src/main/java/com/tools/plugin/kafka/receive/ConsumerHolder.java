package com.tools.plugin.kafka.receive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.util.KafkaUtil;

public class ConsumerHolder {

	/**
	 * 默认消费者,设置属性;消息为字符串
	 * @param topic
	 * @return
	 */
	public static KafkaConsumer<String, String> getStringConsumer(String topic){
        return getStringConsumer(topic, null);
	}
	
	/**
	 * 自定义消费者,设置属性;消息为字符串
	 * @param topic
	 * @return
	 */
	public static KafkaConsumer<String, String> getStringConsumer(String topic, Map<String,Object> mapProps){
		KafkaConsumer<String, String> consumer = KafkaUtil.getStringConsumer(mapProps);    
        consumer.subscribe(Arrays.asList(topic));    
        consumer.seekToBeginning(new ArrayList<TopicPartition>()); 
        return consumer;
	}
	
	
	/**
	 * 默认消费者,设置属性;消息为JsonElement
	 * @param topic
	 * @return
	 */
	public static KafkaConsumer<String, JsonElement<String, Object, Object>> getJsonEleConsumer(String topic){
        return getJsonEleConsumer(topic, null);
	}
	
	/**
	 * 自定义消费者,设置属性;消息为JsonElement
	 * @param topic
	 * @return
	 */
	public static KafkaConsumer<String, JsonElement<String, Object, Object>> getJsonEleConsumer(String topic, Map<String, Object> mapProps) {
		KafkaConsumer<String, JsonElement<String, Object, Object>> consumer = KafkaUtil.getJsonEleConsumer(mapProps);
		consumer.subscribe(Arrays.asList(topic));
		consumer.seekToBeginning(new ArrayList<TopicPartition>());
		return consumer;
	}
	
}
