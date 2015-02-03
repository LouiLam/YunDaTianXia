package com.tlz.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ReflectUtils {
	
	public static void setBoolean(Object receiver, String fieldName, boolean trueOrFalse) {
		if (receiver == null || fieldName == null) return;
		
		try {
			Field field = getDeclaredField(receiver.getClass(), fieldName);
			field.setBoolean(receiver, trueOrFalse);
		} catch (Exception e) {
			Flog.e(e);
		}
	}
	
	public static Field getDeclaredField(Class<?> cls, String fieldName) throws Exception {
		if (cls == null) return null;
		
		Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);

		return field;
	}
	
	public static Object invokeConstructor(Class<?> cls) throws Exception {
		return invokeConstructor(cls, 0);
	}
	
	public static Object invokeConstructor(Class<?> cls, int index) throws Exception {
		return invokeConstructor(cls, index);
	}
	
	public static Object invokeConstructor(Class<?> cls, int index, Object...constructorArgs) throws Exception {
		Constructor<?>[] cs = cls.getDeclaredConstructors();
		return cs[index].newInstance(constructorArgs);
	}
	
	public static Object invokeMethod(Object receiver, String methodName, Object... methodArgs) throws Exception {
		if (receiver == null) return null;
		
		Class<?>[] argsClass = null;
		
		if (methodArgs != null && methodArgs.length != 0) {
			int length = methodArgs.length;
			argsClass = new Class[length];
			for (int i=0; i<length; i++) {
				argsClass[i] = getBaseTypeClass(methodArgs[i].getClass());
			}
		}

		Method method = receiver.getClass().getMethod(methodName, argsClass);
		
		return method.invoke(receiver, methodArgs);
	}
	
	public static Object invokeStaticMethod(Object receiver, String methodName, Object... methodArgs) throws Exception {
		if (receiver == null) return null;
		
		return invokeStaticMethod(receiver.getClass(), methodName, methodArgs);
	}
	
	public static Object invokeStaticMethod(String className, String methodName, Object... methodArgs) throws Exception {
		Class<?> cls = Class.forName(className);
		return invokeStaticMethod(cls, methodName, methodArgs);
	}

	public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... methodArgs) throws Exception {
		
		Class<?>[] argsClass = null;
		
		if (methodArgs != null && methodArgs.length != 0) {
			int length = methodArgs.length;
			argsClass = new Class[length];
			for (int i=0; i<length; i++) {
				argsClass[i] = getBaseTypeClass(methodArgs[i].getClass());
			}
		}
	
		Method method = cls.getMethod(methodName, argsClass);
		return method.invoke(null, methodArgs);
	}
	
	private static Class<?> getBaseTypeClass(Class<?> cls) {
		if ("Boolean".equals(cls.getSimpleName())) {
			cls = Boolean.TYPE;
		} else if ("Integer".equals(cls.getSimpleName())) {
			cls = Integer.TYPE;
		} else if ("Float".equals(cls.getSimpleName())) {
			cls = Float.TYPE;
		} else if ("Double".equals(cls.getSimpleName())) {
			cls = Double.TYPE;
		} else if ("Long".equals(cls.getSimpleName())) {
			cls = Long.TYPE;
		} else if ("Short".equals(cls.getSimpleName())) {
			cls = Short.TYPE;
		} else if ("Character".equals(cls.getSimpleName())) {
			cls = Character.TYPE;
		} else if ("Byte".equals(cls.getSimpleName())) {
			cls = Byte.TYPE;
		}
		return cls;
	}

	
	private ReflectUtils(){/*Do not new me*/};
}
