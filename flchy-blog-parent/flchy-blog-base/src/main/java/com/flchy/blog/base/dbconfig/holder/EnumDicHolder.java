package com.flchy.blog.base.dbconfig.holder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.cache.EnumDicCache;
import com.flchy.blog.base.dbconfig.entity.EnumDicEntity;
import com.flchy.blog.base.dbconfig.service.IEnumDicService;

@Component
public class EnumDicHolder {
	private static final Logger logger = LoggerFactory.getLogger(EnumDicHolder.class);
	/** 缓存区域中的element的key */
    private static final String cacheNodeKey = "enumDicInfo";
	
	/** ENUMGROUP_FIELDNAME:枚举分组 关系配置表缓存 */
	private static final String ENUMGROUP_FIELDNAME = "enumGroup";

	@Autowired
	private static IEnumDicService enumDicService;

	/**
	 * 刷新枚举值缓存数据。<br/> 
	 * 详细描述：调用枚举缓存方法，刷新缓存<br/> 
	 * 使用方式：对外提供调用refreshCache()方法
	 */
	public static void refreshCache() {
		EnumDicCache.getInstance().initialize();
	}

	/**
	 * 根据枚举分组，返回当前枚举描述对象。<br/> 
	 * 详细描述：根据枚举分组，返回缓存中枚举描述对象，若有对象不是空，或数据大于0则返回数据信息，否则返回空。<br/>
	 * 使用方式：对外提供调用getEnumsAllDesc(enumGroup)方法，返回枚举描述信息。
	 * 
	 * @param enumGroup 枚举分组。
	 * @return 经过枚举分组的过滤后得到的枚举中文数组。
	 */
	@SuppressWarnings("unchecked")
	public static String[] getEnumsAllDesc(String enumGroup) {
		final List<EnumDicEntity> child = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (child != null && child.size() > 0) {
			String[] strs = new String[child.size()];
			for (int i = 0; i < child.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) child.get(i);
				strs[i] = String.valueOf(e.getEnumTxt());
			}
			return strs;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，返回当前枚举值对象。<br/> 详细描述：根据枚举分组，返回缓存中枚举值对象，若有对象不是空，或数据大于0则返回数据信息，否则返回空。<br/>
	 * 使用方式：对外提供调调用getEnumsAllValue(enumGroup)方法，返回枚举值信息。
	 * 
	 * @param enumGroup 枚举分组。
	 * @return 经过枚举分组的过滤后得到的枚举值数组。
	 */
	@SuppressWarnings("unchecked")
	public static String[] getEnumsAllValue(String enumGroup) {
		final List<EnumDicEntity> child = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (child != null && child.size() > 0) {
			String[] strs = new String[child.size()];
			for (int i = 0; i < child.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) child.get(i);
				strs[i] = String.valueOf(e.getEnumValue());
			}
			return strs;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，返回当前枚举值对象。<br/> 详细描述：根据枚举分组，返回缓存中枚举值对象，若有对象不是空，或数据大于0则返回数据信息，否则从数据库里查询。<br/>
	 * 使用方式：对外提供调调用getEnumsValueByCodeRealTime(enumGroup，code)方法，返回枚举值信息。
	 * 
	 * @param enumGroup，code。
	 * @return 经过枚举分组的过滤后得到的枚举值数组。
	 */
	public static String getEnumsValueByCodeRealTime(String enumGroup, String code) {
		EnumDicBean enumDicBean = new EnumDicBean();
		enumDicBean.setEnumCode(enumGroup);
		List<EnumDicEntity> enumList = enumDicService.selectEnumsListByObj(enumDicBean);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumCode()).equalsIgnoreCase(code)) {
					str = String.valueOf(e.getEnumValue());
					break;
				}
			}
			return str;
		}
		return null;
	}

	/**
	 * 根据枚举分组，返回当前所有枚举信息。<br/> 详细描述：根据枚举分组，返回缓存中枚举信息，若对象不为空，返回对象，否则返回null。<br/> 使用方式：调用getEnums(enumGroup)方法，返回当前枚举信息。
	 * 
	 * @param enumGroup 枚举分组。
	 * @return 经过枚举分组的过滤后得到的枚举字典实体集合。
	 */
	@SuppressWarnings("unchecked")
	public static List<EnumDicEntity> getEnums(String enumGroup) {
		final List<EnumDicEntity> child = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (child != null) {
			return child;
		} else
			return null;
	}

	/**
	 * 根据枚举分组，以map形式返回当前所有枚举信息。<br/> 详细描述：根据枚举分组，从缓存中查询枚举信息，并把枚举信息循环取出放到map中，以map形式返回当前所有枚举信息。<br/>
	 * 使用方式：调用getEnumsForMap(enumGroup)方法，以map形式返回值。
	 * 
	 * @param enumGroup 枚举分组。
	 * @return 经过枚举分组的过滤后得到的枚举字典Map。
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getEnumsForMap(String enumGroup) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		Map<String, String> map = new TreeMap<String, String>();
		for (EnumDicEntity systemEnum : enumList) {
			map.put(systemEnum.getEnumValue(), systemEnum.getEnumTxt());
		}
		return map;
	}

	/**
	 * 根据枚举分组，枚举编码，以map形式返回当前所有枚举信息。<br/> 详细描述：根据枚举分组，枚举编码，从缓存中查询枚举信息，并把枚举信息以枚举值，枚举信息，key，value的形式放到map中。<br/>
	 * 使用方式：调用getEnumsForMap(enumGroup，code)方法，以map形式返回值。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param code 枚举编码数组。
	 * @return 经过枚举分组和枚举编码数组的过滤后得到的枚举字典Map。
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getEnumsForMap(String enumGroup, String[] code) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		Map<String, String> map = new TreeMap<String, String>();
		for (EnumDicEntity systemEnum : enumList) {
			for (String c : code) {
				if (String.valueOf(systemEnum.getEnumCode()).equalsIgnoreCase(c))
					map.put(systemEnum.getEnumValue(), systemEnum.getEnumTxt());
			}
		}
		return map;
	}

	/**
	 * 根据枚举分组，枚举值，返回当前所有枚举描述。<br/> 详细描述：根据枚举分组，枚举值，返回当前所有枚举描述。判断从页面传来的枚举值和从缓存中查询出来的枚举值是否相同，
	 * 若相同则返回枚举值对应的枚举中文，否则返回null。<br/> 使用方式：调用getEnumsDescByValue(enumGroup，value)方法。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param value 枚举值。
	 * @return 经过枚举分组和枚举值的过滤后得到的枚举中文。
	 */
	@SuppressWarnings("unchecked")
	public static String getEnumsDescByValue(String enumGroup, String value) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumValue()).equals(value)) {
					str = String.valueOf(e.getEnumTxt());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}

	/**
	 * @Title: getRemarkByValue
	 * @Description: 根据枚举组，枚举值得到枚举的描述信息，调用时需注意此方法返回的是枚举的desc描述信息，不是枚举的txt中文名称信息。
	 * @param enumGroup 枚举组
	 * @param value 枚举值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getRemarkByValue(String enumGroup, String value) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumValue()).equals(value)) {
					str = String.valueOf(e.getRemark());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，枚举编码，获取枚举描述信息。<br/> 详细描述：根据枚举分组，枚举值，返回当前所有枚举描述。判断从页面传来的枚举编码和从缓存中查询出来的枚举编码是否相同，
	 * 若相同则返回枚举编码对应的枚举中文，否则返回null。<br/> 使用方式：调用getEnumsDescByCode(enumGroup，code)方法。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param code 枚举编码。
	 * @return 经过枚举分组和枚举编码的过滤后得到的枚举中文。
	 */
	@SuppressWarnings("unchecked")
	public static String getEnumsDescByCode(String enumGroup, String code) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumCode()).equalsIgnoreCase(code)) {
					str = String.valueOf(e.getEnumTxt());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，枚举值降序，获取枚举值信息。<br/> 详细描述：根据枚举分组，枚举值降序，获取枚举值信息。判断从页面传来的枚举升降序和查询出来的枚举中文是否相同， 若相同则返回枚举中文对应的枚举值，否则返回null。<br/>
	 * 使用方式：调用getEnumsValueByDesc(enumGroup，desc)方法。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param desc 枚举中文。
	 * @return 经过枚举分组和枚举中文的过滤后得到的枚举值。
	 */
	@SuppressWarnings("unchecked")
	public static String getEnumsValueByDesc(String enumGroup, String desc) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumTxt()).equalsIgnoreCase(desc)) {
					str = String.valueOf(e.getEnumValue());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，枚举编码，返回枚举值信息。<br/> 详细描述：根据枚举分组，枚举编码，返回枚举值信息。判断从页面传来的枚举编码和查询出来的枚举编码是否相同， 若相同则返回枚举编码对应的枚举值，否则返回null。<br/>
	 * 使用方式：调用getEnumsValueByCode(enumGroup，code)方法。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param code 枚举编码。
	 * @return 经过枚举分组和枚举编码的过滤后得到的枚举值。
	 */
	@SuppressWarnings("unchecked")
	public static String getEnumsValueByCode(String enumGroup, String code) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumCode()).equalsIgnoreCase(code)) {
					str = String.valueOf(e.getEnumValue());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}

	/**
	 * 根据枚举分组，枚举值，返回枚举编码信息。<br/> 详细描述：根据枚举分组，枚举值，返回枚举编码信息。判断从页面传来的枚举值和查询出来的枚举值是否相同， 若相同则返回枚举值对应的枚举编码，否则返回null。<br/>
	 * 使用方式：调用getEnumsCodeByValue(enumGroup，value)方法。
	 * 
	 * @param enumGroup 枚举分组。
	 * @param value 枚举值。
	 * @return 经过枚举分组和枚举值的过滤后得到的枚举编码。
	 */
	@SuppressWarnings("unchecked")
	public static String getEnumsCodeByValue(String enumGroup, String value) {
		final List<EnumDicEntity> enumList = (List<EnumDicEntity>) EnumDicCache.getInstance().getCacheValue(cacheNodeKey, ENUMGROUP_FIELDNAME, enumGroup);
		if (enumList != null && enumList.size() > 0) {
			String str = null;
			for (int i = 0; i < enumList.size(); i++) {
				EnumDicEntity e = (EnumDicEntity) enumList.get(i);
				if (String.valueOf(e.getEnumValue()).equals(value)) {
					str = String.valueOf(e.getEnumCode());
					break;
				}
			}
			return str;
		} else {
			return null;
		}
	}
}
