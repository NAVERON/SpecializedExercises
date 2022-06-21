package com.eron.structures;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 结合之前的经验 标准化build创建对象的写法 作为一种基本的结构 
 * @author eron 
 * WARNING : 需要确认是否可行, 待验证 
 */
public class BuilderStandardTemplate {

    private static final Logger log = LoggerFactory.getLogger(BuilderStandardTemplate.class);
    private static final BuilderStandardTemplate buildOuter = new BuilderStandardTemplate();  // 专用于创建内部类实例 
    
    // 对象属性变量 
    private String name;  // 名称 
    private Integer id;  // 唯一生成id 
    private Long aliveTimes; // 生存时常 s 
    private Float price;  // 价格 
    
    private BuilderStandardTemplate() {  // 不允许外部使用构造器创建 
        // 随机生成id 
        this.id = UUID.randomUUID().hashCode();
    }
    
    public static InnerClass createBuilder() {
        return buildOuter.new InnerClass();
    }
    
    @Override
    public String toString() {
        return "BuilderStandardTemplate [name=" + name + ", id=" + id 
                + ", aliveTimes=" + aliveTimes + ", price="
                + price + "]";
    }

    /**
     * 创建内部类 
     * @author eron 
     * 
     */
    private class InnerClass {
        // 内部类中创建外部类实例 设置属性并最终返回 这样不用重写一遍属性 
        BuilderStandardTemplate generateObj = new BuilderStandardTemplate();
        
        private InnerClass name(String name) {
            generateObj.name = name;
            return this;
        }
        private InnerClass aliveTime(Long time) {
            generateObj.aliveTimes = time;
            return this;
        }
        private InnerClass price(Float price) {
            generateObj.price = price;
            return this;
        }
        private BuilderStandardTemplate build() {
            return generateObj;
        }
    }
    
    public static void main(String[] args) {
        // 测试创建 
        BuilderStandardTemplate obj = BuilderStandardTemplate.createBuilder() // 外部的类只能使用这个方法创建 
                                        .name("eron").price(1.5F).aliveTime(12345L)  // 设置属性 
                                        .build();
        
        BuilderStandardTemplate obj2 = BuilderStandardTemplate.createBuilder()
                                        .name("test").price(1.0F).aliveTime(9090L)
                                        .build();
        log.info("输出当前对象的属性 --> \n{}\n{}", obj.toString(), obj2.toString());
        
    }
    
    
}









