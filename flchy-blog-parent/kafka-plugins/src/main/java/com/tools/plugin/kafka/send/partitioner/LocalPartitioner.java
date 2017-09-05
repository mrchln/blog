package com.tools.plugin.kafka.send.partitioner;

import java.util.List;
import java.util.Map;

import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

/**
 * 自定义分区类，设置属性：partitioner.class=com.tools.plugin.kafka.send.partitioner.LocalPartitioner
 * @author KingXu
 */
public class LocalPartitioner implements org.apache.kafka.clients.producer.Partitioner {

	public LocalPartitioner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure(Map<String, ?> paramMap) {
		// TODO Auto-generated method stub
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		int partitionNum = 0;
		try {
			partitionNum = Integer.parseInt((String) key);
		} catch (Exception e) {
			partitionNum = key.hashCode();
		}
		return Math.abs(partitionNum % numPartitions);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
