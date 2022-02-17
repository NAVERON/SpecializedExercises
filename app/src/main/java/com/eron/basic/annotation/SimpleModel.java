/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.basic.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class SimpleModel {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleModel.class);
    
    public static void main(String[] args) {
        log.info("custom annotation test");
        
        Field[] fields = ExportModel.class.getDeclaredFields();
        String info = generateInfo(fields);
        
        log.info("最终输出信息 : {}", info);
    }
    
    public static String generateInfo(Field[] fields){
        List<String> infos = new ArrayList<String>();
        
        for(int i = 0; i < fields.length; i++){
            log.info("生命field名称 : {}", fields[i].getName());
            
            Annotation[] annotations = fields[i].getDeclaredAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof TestAnnotation){
                    infos.add(fields[i].getName());
                }
            }
        }
        
        log.info("final list values : {}, and size : {}", infos, infos.size());
        return StringUtils.join(infos, ",");
    }

    public static class ExportModel {

        @TestAnnotation
        private String name;
        @TestAnnotation
        private String addressString;
        @TestAnnotation
        private int age;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddressString() {
            return addressString;
        }

        public void setAddressString(String addressString) {
            this.addressString = addressString;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
    
    
}
