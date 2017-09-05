package com.tools.plugin.kafka.topic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.security.JaasUtils;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;

/**
 * topic持有工具类
 * 
 * @author KingXu
 *
 */
public class TopicHolder {
	public static final String NAME = "/zookeeper.properties";

	public static Properties getProperties() {
		Properties props = new Properties();
		try {
			InputStream ips = TopicHolder.class.getResourceAsStream(NAME);
			BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
			props.load(ipss);
			return props;
		} catch (IOException e) {
			System.out.println("读properties文件出错:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 创建自定义分区、副本的topic
	 * @param topic
	 * @param partitions
	 * @param replicationFactor
	 */
	public static void createTopic(String topic, int partitions, int replicationFactor) {
		Properties props = getProperties();
		ZkUtils zkUtils = ZkUtils.apply(props.getProperty("zookeeper.connect"), 30000, 30000, JaasUtils.isZkSecurityEnabled());
		AdminUtils.createTopic(zkUtils, topic, partitions, replicationFactor, new Properties(), RackAwareMode.Enforced$.MODULE$);
		zkUtils.close();
	}

	/**
	 * 删除topic
	 * @param topic
	 */
	public static void deleteTopic(String topic) {
		Properties props = getProperties();
		ZkUtils zkUtils = ZkUtils.apply(props.getProperty("zookeeper.connect"), 30000, 30000, JaasUtils.isZkSecurityEnabled());
		AdminUtils.deleteTopic(zkUtils, topic);
		zkUtils.close();
	}

	/**
	 * 查询topic
	 * @param topic
	 */
	public static Properties fetchTopic(String topic) {
		Properties props = getProperties();
		ZkUtils zkUtils = ZkUtils.apply(props.getProperty("zookeeper.connect"), 30000, 30000, JaasUtils.isZkSecurityEnabled());
		// 获取topic的topic属性属性
		Properties topicProps = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
		zkUtils.close();
		return topicProps;
	}

	/**
	 * 添加topic 的属性
	 * @param topic
	 * @param propsKey
	 * @param propsVal
	 */
	public static void addTopicConfig(String topic, String propsKey, String propsVal) {
		Properties props = getProperties();
		ZkUtils zkUtils = ZkUtils.apply(props.getProperty("zookeeper.connect"), 30000, 30000, JaasUtils.isZkSecurityEnabled());
		Properties topicProps = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
		// 增加topic级别属性
		topicProps.put(propsKey, propsVal);
		// 修改topic 的属性
		AdminUtils.changeTopicConfig(zkUtils, topic, topicProps);
		zkUtils.close();
	}

	/**
	 * 删除topic 的属性
	 * @param topic
	 * @param propsKey
	 */
	public static void deleteTopicConfig(String topic, String propsKey) {
		Properties props = getProperties();
		ZkUtils zkUtils = ZkUtils.apply(props.getProperty("zookeeper.connect"), 30000, 30000, JaasUtils.isZkSecurityEnabled());
		Properties topicProps = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
		// 删除topic级别属性
		topicProps.remove(propsKey);
		// 修改topic 的属性
		AdminUtils.changeTopicConfig(zkUtils, topic, topicProps);
		zkUtils.close();
	}
	
	
	public static void main(String[] arges){
		createTopic("zblog-gather-topic",10,3);
		
		//deleteTopic("testapi");
		
		/*Properties props= fetchTopic("testapi");
		Iterator it = props.entrySet().iterator();
		while(it.hasNext()){
		    Map.Entry entry=(Map.Entry)it.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println(key + " = " + value);
		}*/
	}
}
