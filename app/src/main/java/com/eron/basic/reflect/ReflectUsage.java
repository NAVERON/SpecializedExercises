package com.eron.basic.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射的基本使用 
 * @author eron
 *
 */
public class ReflectUsage {
	
	private static final Logger log = LoggerFactory.getLogger(ReflectUsage.class);

	public static void main(String[] args) {
		ReflectUsage reflectUsage = new ReflectUsage();
		
		try {
			reflectUsage.reflectPersonA();
			
			reflectUsage.reflectPersonB();
			
			reflectUsage.reflectPersonC();
			
		} catch (ClassNotFoundException | NoSuchFieldException 
				| SecurityException | IllegalArgumentException 
				| IllegalAccessException | NoSuchMethodException 
				| InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	
	public void reflectPersonA() throws ClassNotFoundException, NoSuchFieldException, 
								SecurityException, NoSuchMethodException, 
								InstantiationException, IllegalAccessException, 
								IllegalArgumentException, InvocationTargetException {
		log.info("==================111");
		
		// 第一种方法获取类 
		Class<?> clazz1 = Class.forName("com.eron.basic.reflect.PersonForReflect");
		
		// 测试类属性 变量
		Field[] fieldsOfPublic = clazz1.getFields();
		Field[] fields = clazz1.getDeclaredFields();
		
		log.info("输出公共属性");
		Arrays.asList(fieldsOfPublic).forEach(field -> {
			log.info("fieldsOfPublic -> {}", field.getName());
		});
		log.info("输出全部属性");
		Arrays.asList(fields).forEach(field -> {
			log.info("fields -> {}", field.getName());
		});
		log.warn("输出单个属性 -> {}", clazz1.getField("count"));
		
		// 测试类构造函数 
		Constructor<?>[] constructors = clazz1.getConstructors();
		Arrays.asList(constructors).forEach(constructor -> {
			log.info("constructor -> {}", constructor.toString());
		});
		// 获取特定的构造函数 
		Constructor<?> emptyConstructor = clazz1.getConstructor();
		log.info("empty param : {}", emptyConstructor.toString());
		
		Constructor<?> nameParamConstructor = clazz1.getConstructor(String.class);
		log.info("name constructor : {}", nameParamConstructor.toString());
		
		Object a = nameParamConstructor.newInstance("hello world");
		log.warn("输出反射构造的对象 ==> {}", a.toString());
		
		Method[] methods = clazz1.getDeclaredMethods();
		Arrays.asList(methods).forEach(method -> {
			log.info("method => {}, name => {}", method.getParameterCount(), method.getName());
		});
		
	}
	
	public void reflectPersonB() throws NoSuchMethodException, SecurityException, 
						IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		log.info("=======================222");
		
		// 第二种方法 
		Class<?> clazz2 = PersonForReflect.class;
		log.info("clazz2 => {}", clazz2);
		Field[] allFields = clazz2.getDeclaredFields();
		Arrays.asList(allFields).forEach(field -> {
			log.info("PersonForReflect.class fields => {}", field.getName());
		});
		
		// 获取特定的方法
		Method getNameMethod = clazz2.getDeclaredMethod("getName");
		PersonForReflect x = new PersonForReflect("HELLO");
		String reflectName = (String) getNameMethod.invoke(x); // = x.getName();
		log.info("执行反射获取的方法结果 : {}", reflectName);
		
		// 查看类实现的接口 
		Class<?>[] interfaces = clazz2.getInterfaces();
		Arrays.asList(interfaces).forEach(api -> {
			log.info("打印接口 : {}", api.getName());
		});
		
		Class<?> father = clazz2.getSuperclass();
		log.info("super class : {}", father.getName());
		
		
	}
	
	public void reflectPersonC() throws NoSuchFieldException, SecurityException, 
					IllegalArgumentException, IllegalAccessException, NoSuchMethodException {
		
		log.info("====================333");
		// 第三种方法 
		PersonForReflect person = new PersonForReflect("wangyulong");
		Class<?> clazz3 = person.getClass();
		log.info("clazz3 => {}", clazz3);
		
		Field nameField = clazz3.getDeclaredField("name");
		nameField.setAccessible(true);  // 因为name是private, 需要设置 
		//nameField.set(clazz3, "eron");
		log.info("输出name : {}", nameField.getName());
		log.info("输出的那个前对象数据 : {}", person.toString());
		
		Method idMethod = clazz3.getDeclaredMethod("getId");
		Annotation[] annotations = idMethod.getAnnotations();
		Arrays.asList(annotations).forEach(annotation -> {
			log.info("annotation type : <{}>, others => {}", annotation.annotationType(), annotation.toString());
		});
		
	}
	
}








