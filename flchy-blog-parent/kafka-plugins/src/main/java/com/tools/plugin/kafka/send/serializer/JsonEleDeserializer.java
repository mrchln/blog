package com.tools.plugin.kafka.send.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.tools.plugin.kafka.json.JsonElement;
import com.tools.plugin.kafka.util.BeanUtil;

/**
 * 指定value反序列化处理类;value.deserializer=com.tools.plugin.kafka.send.serializer.JsonEleDeserializer
 * @author KingXu
 *
 */
public class JsonEleDeserializer implements Deserializer<JsonElement<String, Object, Object>> {

	@Override
	public void configure(Map<String, ?> paramMap, boolean paramBoolean) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement<String, Object, Object> deserialize(String topic, byte[] bytes) {
		return (JsonElement<String, Object, Object>) BeanUtil.byteToObject(bytes);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
}
