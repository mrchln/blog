package com.tools.plugin.wxchat.holder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;


public class PropertiesHolder {
	private static Logger logger = LoggerFactory.getLogger(PropertiesHolder.class);
	private static Map<String, Object> appProperties = new HashMap<String, Object>();
	private static final String rootPackage = "proper/";

	static {
		try {
			loadProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PropertiesHolder() {
	}

	public static void loadProperties() throws Exception {
		// Spring获取路径
		Resource[] resources = getResources();
		// 获取jar文件路径
		if (null == resources || resources.length <= 0) {
			return;
		}
		// 加载properties
		setLocations(resources);
	}

	private static Resource[] getResources() throws Exception {
		PathMatchingResourcePatternResolver resolover = new PathMatchingResourcePatternResolver();
		Assert.notNull(rootPackage, rootPackage + " path is null");
		try {
			boolean isTestEnvironment = isTestEnvironment();
			Resource[] resource = null;
			if (isTestEnvironment) {
				resource = resolover.getResources("classpath*:" + rootPackage + "local/*.properties");
			} else {
				resource = resolover.getResources("classpath*:" + rootPackage + "deploy/*.properties");
			}
			Resource[] locations = getRootResource();
			if (null == resource || null == locations) {
				return null;
			}
			List<Resource> resourceList = Arrays.asList(resource);
			List<Resource> locationsList = Arrays.asList(locations);
			List<Resource> allProperList = new ArrayList<Resource>();
			if (null != resourceList && !resourceList.isEmpty()) {
				allProperList.addAll(resourceList);
			}
			if (null != locationsList && !locationsList.isEmpty()) {
				allProperList.addAll(locationsList);
			}
			if (null != allProperList && !allProperList.isEmpty()) {
				Resource[] allResource = allProperList.toArray(new Resource[allProperList.size()]);
				return allResource;
			}
			return resource;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取通用路径
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Resource[] getRootResource() throws IOException {
		PathMatchingResourcePatternResolver resolover = new PathMatchingResourcePatternResolver();
		Assert.notNull(rootPackage,rootPackage+" path is null");
		Resource[] locations = resolover.getResources("classpath*:" + rootPackage + "*.properties");
		return locations;
	}

	/**
	 * 判断是否测试环境
	 * 
	 * @return
	 * @throws IOException
	 */
	private static boolean isTestEnvironment() throws IOException {
		Resource[] locations = getRootResource();
		if (null == locations || locations.length <= 0) {
			return true;
		}
		Properties tmpPro = new Properties();
		for (Resource res : locations) {
			try {
				tmpPro.load(new InputStreamReader(res.getInputStream(), "utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!tmpPro.isEmpty()) {
			String environment = tmpPro.getProperty("web.environment.istest");
			if (!isNullOrEmpty(environment)) {
				return Boolean.parseBoolean(environment);
			}
		}
		return true;
	}

	
	/**
	 * 判断字符串是否为空或为空值。<br/>
	 * 详细描述：判断字符串是否为空或为空值。<br/>
	 * 使用方式：通过本类的类名直接调用该方法，并传入所需参数。<br/>
	 * 
	 * @param str 字符串。<br/>
	 * @return flag true表示参数是为空或为空值，false则表示不为空或空值。<br/>
	 */
	public static boolean isNullOrEmpty(String str) {
		boolean flag = false;
		if (null == str || "".equals(str.trim()) || "n/a".equals(str.trim().toLowerCase())  || "null".equals(str.trim().toLowerCase()) || "undefined".equals(str.trim().toLowerCase())) {
			flag = true;
		}
		return flag;
	}
	/**
	 * 详细描述：接收一个Resource类型的数组作为参数，将locations标签下值都会被解析成Resource，
	 * 而这个resource本身则包含了访问这个resource的方法，在这里resource代表的则是properties文件。
	 * 
	 * @param locations Resource类型的数组。
	 * @throws IOException
	 * @see #setLocations(Resource[])
	 */
	public static void setLocations(Resource[] locations) throws IOException {
		// 对properties文件进行排序,先读取jar包中的属性文件,jar包外的属性配置可覆盖Jar包中的属性配置
		Arrays.sort(locations, new Comparator<Resource>() {
			@Override
			public int compare(Resource rs1, Resource rs2) {
				try {
					if (ResourceUtils.JAR_FILE_EXTENSION.equals("." + rs1.getURL().getProtocol())) {
						return -1;
					} else {
						return 1;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		Map<String, Object> tmpMap = new HashMap<>();
		// 加载Resource到Map
		for (Resource resource : locations) {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			Map<String, Object> propertiesMap = new HashMap<String, Object>((Map) properties);
			tmpMap.putAll(propertiesMap);
		}
		for (Object key : tmpMap.keySet()) {
			String keyStr = key.toString();
			String value  = String.valueOf(tmpMap.get(keyStr));
			if (keyStr.endsWith(".encrypted")) {
				String newKey   = keyStr.substring(0, keyStr.lastIndexOf(".encrypted"));
				String newValue = value;
				appProperties.put(newKey, newValue);
			} else {
				appProperties.put(keyStr, value);
			}
		}
		logger.info("加载Properties配置文件:" + StringUtils.arrayToDelimitedString(locations, ","));
		System.out.println("加载Properties配置文件:" + StringUtils.arrayToDelimitedString(locations, ","));
	}


	/**
	 * 获取属性对象。<br/>
	 * 详细描述：通过属性key，获取属性对象。<br/>
	 * 使用方式：可在java代码中直接调用此静态方法。
	 * 
	 * @param key 属性key。
	 * @return 返回查询到的属性对象。
	 */
	public static Object getPropertyObj(String key) {
		if (appProperties == null) {
			logger.error("未初始化加载属性文件，请检查Spring配置");
			return null;
		}
		return appProperties.get(key);
	}

	/**
	 * 获取String类型属性值。<br/>
	 * 详细描述：通过属性key，获取属性值。<br/>
	 * 使用方式：java代码中可直接调用此静态方法。
	 * 
	 * @param key 属性key。
	 * @return 返回String类型的属性值。
	 */
	public static String getProperty(String key) {
		return String.valueOf(getPropertyObj(key));
	}

	/**
	 * 获取int类型属性值。<br/>
	 * 详细描述：通过属性key，获取属性值。<br/>
	 * 使用方式：java代码中可直接调用此静态方法。
	 * 
	 * @param key 属性key。
	 * @return 返回int类型的属性值。
	 */
	public static int getIntProperty(String key) {
		return Integer.valueOf(getProperty(key));
	}

	/**
	 * 获取boolean类型属性值。 <br/>
	 * 详细描述：通过属性key，获取属性值。<br/>
	 * 使用方式：java代码中可直接调用此静态方法。
	 * 
	 * @param key 属性key。
	 * @return 返回boolean类型的属性值。
	 */
	public static boolean getBooleanProperty(String key) {
		String val = getProperty(key);
		if (val != null && "true".equalsIgnoreCase(val)) {
			return true;
		}
		return false;
	}


	/**
	 * 根据前缀获取符合条件的所有properties属性值。
	 * 详细描述：根据前缀进行匹配，找出符合该前缀条件的所有的properties的属性值，以map返回。<br/>
	 * 使用方式：PropertiesHolder.getPropertiesByPrefix("前缀")方式调用。
	 * 
	 * @param prefix properties的key前缀。
	 * @return 符合参数条件的map。
	 */
	public static Map<String, Object> getPropertiesByPrefix(String prefix) {
		if (appProperties == null) {
			logger.error("未初始化加载属性文件，请检查Spring配置");
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Set<String> set = appProperties.keySet();
		for (String key : set) {
			if (key.startsWith(prefix)) {
				resultMap.put(key, getPropertyObj(key));
			}
		}
		return resultMap;
	}

	/**
	 * 根据前缀获取符合条件的所有properties属性值。
	 * 详细描述：根据前缀进行匹配，找出符合该前缀条件的所有的properties的属性值，以map返回。<br/>
	 * 使用方式：PropertiesHolder.getPropertiesBySuffix("后缀")方式调用。
	 * 
	 * @param Suffix properties的key后缀。
	 * @return 符合参数条件的map。
	 */
	public static Map<String, Object> getPropertiesBySuffix(String suffix) {
		if (appProperties == null) {
			logger.error("未初始化加载属性文件，请检查Spring配置");
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Set<String> set = appProperties.keySet();
		for (String key : set) {
			if (key.endsWith(suffix)) {
				resultMap.put(key, getPropertyObj(key));
			}
		}
		return resultMap;
	}
}
