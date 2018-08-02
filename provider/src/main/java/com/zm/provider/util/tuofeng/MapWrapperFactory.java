package com.zm.provider.util.tuofeng;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.stereotype.Component;

public class MapWrapperFactory implements ObjectWrapperFactory {

	//判断当object 是 Map 类型时，返回 true
	@Override
	public boolean hasWrapperFor(Object object) {
		boolean result =  object != null && object instanceof Map;
		return result;
	}

	//返回一个可以处理 key 为驼峰的 Wrapper
	@Override
	public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
		return new CustomWrapper(metaObject, (Map) object);
	}

}
