package com.tools.plugin.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.alibaba.fastjson.JSON;
import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.receive.ConsumerHolder;

public class TestConsumer {

	static int nThreads = 1;
	static ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);

	public static void main(String[] s) {
		for (int i = 0; i < nThreads; i++) {
			threadPool.execute(new ConsumerThreadWorker("allTopic"));
		}
	}
}

class ConsumerThreadWorker extends Thread {
	private String typeName;

	public ConsumerThreadWorker() {
	}

	public ConsumerThreadWorker(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public void run() {
		switch (typeName) {
		case "allTopic":
			allTopic();
			break;
		case "partitionTopic":
			partitionTopic();
			break;
		case "allJsonTopic":
			allJsonTopic();
			break;
		default:
			System.out.println("default");
			break;
		}
	}

	public void allTopic() {
		KafkaConsumer<String, String> consumer = ConsumerHolder.getStringConsumer("zblog-gather-topic");
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("fetched from partition " + record.partition() + ", offset: " + record.offset() + ", message: " + record.value());
			}
		}
	}

	public void partitionTopic() {
		KafkaConsumer<String, String> consumer = ConsumerHolder.getStringConsumer("test-api");
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			// 按分区读取数据
			for (TopicPartition partition : records.partitions()) {
				List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
				for (ConsumerRecord<String, String> record : partitionRecords) {
					System.out.println(record.offset() + ": " + record.value());
				}
			}
		}
	}

	public void allJsonTopic() {

		Map<String, Object> mapProps = new HashMap<>();
		mapProps.put("value.deserializer", "com.tools.plugin.kafka.send.serializer.JsonEleDeserializer");

		KafkaConsumer<String, JsonElement<String, Object, Object>> consumer = ConsumerHolder.getJsonEleConsumer("test-api", mapProps);
		while (true) {
			ConsumerRecords<String, JsonElement<String, Object, Object>> records = consumer.poll(1000);
			for (ConsumerRecord<String, JsonElement<String, Object, Object>> record : records) {
				System.out.println("fetched from partition " + record.partition() + ", offset: " + record.offset() + ", message: " + JSON.toJSONString(record.value()));
			}
		}
	}
}