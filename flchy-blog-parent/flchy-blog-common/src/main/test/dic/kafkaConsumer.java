package dic;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.tools.plugin.kafka.receive.ConsumerHolder;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;  

public class kafkaConsumer  extends Thread{
	private String topic;  
    
    public kafkaConsumer(String topic){  
        super();  
        this.topic = topic;  
    }  
      
      
    @Override  
    public void run() {  
    	KafkaConsumer<String, String>   consumer = ConsumerHolder.getStringConsumer(topic);
    	System.out.println(consumer.toString());
    	while (true) {
			//移除并返回队列头部元素，如果队列为空则返回null
			ConsumerRecords<String, String> records = consumer.poll(1);

			for (ConsumerRecord<String, String> record : records) {
				System.out.println(record.toString());
				System.out.println(record.value());
			}
		}
//        ConsumerConnector consumer = createConsumer();  
//        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();  
//        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据  
//         Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);  
//         KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据  
//         ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();  
//         while(iterator.hasNext()){  
//             String message = new String(iterator.next().message());  
//             System.out.println("接收到: " + message);  
//         }  
    }  
  
    private ConsumerConnector createConsumer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", "66.112.210.234:2181");//声明zk  
        properties.put("group.id", "group1");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据  
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));  
     }  
      
      
    public static void main(String[] args) {  
        new kafkaConsumer("testnie").start();// 使用kafka集群中创建好的主题 test   
          
    }  
}
