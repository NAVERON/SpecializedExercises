/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.basic.annotation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  在实际的项目中  自定义注解需要配合自己的实现来做， 一般放在拦截器里实现 
 * @author ERON_AMD
 */
public class AnotherAnnotationTest {

    private static final Logger log = LoggerFactory.getLogger(AnotherAnnotationTest.class);

    public static void main(String[] args) throws IntrospectionException, InstantiationException, 
    				IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        log.info("实现注解的实现将Person实例注入PersonoDAO");

        PropertyDescriptor descriptor = new PropertyDescriptor("person", PersonDAO.class);
        log.info("out descibe personDAO person obj : {}", descriptor);
        Person person = (Person) descriptor.getPropertyType().newInstance();  // 获取这个属性的所属类Class

        Method writeMethod = descriptor.getWriteMethod();  // 在personDAO中找到设置person对象值的方法
        Annotation annotation = writeMethod.getAnnotation(TestUsage.class);  // 从写person的方法上获取对应的注解

        Method[] annotationMethods = annotation.getClass().getMethods();  // 获取注解定义的变量(注解中没有变量，只有定义的方法)
        log.info("输出获取到的注解的信息 : {}", annotationMethods.length);
        for (Method method : annotationMethods) {  //注解里面的方法 
            String annoName = method.getName();  // 获取注解定义中的方法名称，在注解中，不存在属性定义，只有方法
            log.info("注解的方法名称 : {}", annoName);
            try {
                PropertyDescriptor personDescriptor = new PropertyDescriptor(annoName, Person.class);
                Method annoMethod = personDescriptor.getWriteMethod();  // Person类中的  setxxx方法

                Object obj = method.invoke(annotation);  // 执行注解中的方法    方法.invoke(方法存在与哪个类) -> 参数类中method执行
                annoMethod.invoke(person, obj);  // 执行person对象的设置方法，设置name和age两个属性           参数 第一个是对象， 后面是方法参数 
            } catch (Exception e) {
                log.error("没有在注解中找到对应的方法 : {}", annoName);
                continue;
            }
        }
        PersonDAO personDAO = new PersonDAO();
        writeMethod.invoke(personDAO, person);  // 调用 personDAO中的stePerson方法

        // 输出将注解信息注入person对象的最终数据信息
        log.info("从personDAO中获取person对象的信息, name : {}, age : {}", personDAO.getPerson().getName(), personDAO.getPerson().getAge());
    }

    public static class PersonDAO {

        private Person person;

        public Person getPerson() {
            return person;
        }

        @TestUsage(name = "wangyulong", age = 1)
        public void setPerson(Person person) {
            this.person = person;
        }

    }

    public static class Person {

        String name;
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }
}
