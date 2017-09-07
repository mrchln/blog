package dic;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.tools.plugin.kafka.send.ProducerHolder;

import kafka.serializer.StringEncoder;

public class kafkaProducer extends Thread {
	private String topic;

	public kafkaProducer(String topic) {
		super();
		this.topic = topic;
	}

	@Override
	public void run() {
		int i = 0;
		while (true) {
			ProducerHolder.send(topic,  "message: " + i++);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
		new kafkaProducer("testnie").start();// 使用kafka集群中创建好的主题 test

	}
}
