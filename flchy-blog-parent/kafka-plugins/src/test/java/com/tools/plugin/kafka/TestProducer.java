package com.tools.plugin.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.alibaba.fastjson.JSON;
import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.send.ProducerHolder;
import com.tools.plugin.kafka.util.KafkaUtil;

public class TestProducer {

	static int nThreads = 1;
	static ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);

	public static void main(String[] s) {
		for (int i = 0; i < nThreads; i++) {
			threadPool.execute(new ProducerThreadWorker("jsonSend"));
		}
	}
}

class ProducerThreadWorker extends Thread {
	private String typeName;

	public ProducerThreadWorker() {
	}

	public ProducerThreadWorker(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public void run() {
		switch (typeName) {
		case "stringSendUnBack":
			stringSendUnBack();
			break;
		case "stringSendBack":
			stringSendBack();
			break;
		case "jsonSend":
			jsonSend();
			break;
		default:
			System.out.println("default");
			break;
		}
	}

	public void stringSendUnBack() {
		try {
			int i = 0;
			while (true) {
				Map<String,Object> props = new HashMap<>();
				props.put("partitioner.class", "com.tools.plugin.kafka.send.partitioner.LocalPartitioner");
				ProducerHolder.send("test-api", String.valueOf(i), "this is message" + i,props);
				i++;
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stringSendBack() {
		try {
			Producer<String, String> producer = KafkaUtil.getStringProducer();
			int i = 0;
			while (true) {
				ProducerRecord<String, String> record = new ProducerRecord<String, String>("test-api", String.valueOf(i), "this is message" + i);
				producer.send(record, new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null)
							e.printStackTrace();
						System.out.println("message send to partition " + metadata.partition() + ", offset: " + metadata.offset());
					}
				});
				i++;
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jsonSend() {
		try {
			for(int i =0 ;i<10; i++){
				
				JsonElement<String, Object, Object> x = new JsonElement<String, Object, Object>();
				x.setHeaders(null);
				x.setBody(("你好 " + i).getBytes());

				Map<String, Object> mapProps = new HashMap<>();
				mapProps.put("value.serializer", "com.tools.plugin.kafka.send.serializer.JsonEleSerializer");
				mapProps.put("partitioner.class", "com.tools.plugin.kafka.send.partitioner.LocalPartitioner");

				ProducerHolder.send("zblog-gather-topic", String.valueOf(i), x, mapProps);
				i++;
				System.out.println("=====:"+JSON.toJSONString(x));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}