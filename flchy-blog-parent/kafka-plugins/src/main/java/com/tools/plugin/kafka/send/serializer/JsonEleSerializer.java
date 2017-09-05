package com.tools.plugin.kafka.send.serializer;


import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.util.BeanUtil;

/**
 * 指定value序列化处理类;value.class=com.tools.plugin.kafka.send.serializer.JsonEleSerializer
 * @author KingXu
 *
 */
public class JsonEleSerializer implements Serializer<JsonElement<String, Object, Object>> {

	
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void configure(Map<String, ?> paramMap, boolean isKey) {
		// TODO Auto-generated method stub
	}

	@Override
	public byte[] serialize(String topic, JsonElement<String, Object, Object> jsonElement) {
		return BeanUtil.objectToByte(jsonElement);
	}

}
