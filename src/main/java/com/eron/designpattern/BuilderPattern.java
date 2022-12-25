/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.designpattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class BuilderPattern {
    
    private static final Logger log = LoggerFactory.getLogger(BuilderPattern.class);
    
    private final int size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean bacon;
    
    private BuilderPattern (Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.bacon = builder.bacon;
    }
    
    public static Builder createBuilder() {
        return new BuilderPattern.Builder(0);
    }
    
    public static Builder createBuilder(int size) {  // 重写
        return new BuilderPattern.Builder(size);
    }
    
    public static class Builder {
        
        //required
        private int size;
        
        //optional
        private boolean cheese;
        private boolean pepperoni;
        private boolean bacon;
        
        private Builder(int size){
            this.size = size;
        }
        
        private Builder size(int size) {
            this.size = size;
            return this;
        }
        
        private Builder cheese (boolean cheese){
            this.cheese = cheese;
            return this;
        }
        
        private Builder pepperoni (boolean pepperoni){
            this.pepperoni = pepperoni;
            return this;
        }
        
        private Builder bacon (boolean bacon) {
            this.bacon = bacon;
            return this;
        }
        
        public BuilderPattern build() {
            if (this.size == 0){  // 检查必须项, 一般设置为不可能的项， 或者非法的初始值
                throw new IllegalStateException("必须参数需要初始化");
            }
            
            return new BuilderPattern(this);
        }
    }
    
    @Override
    public String toString(){
        return "result : " + this.size + ", " + this.pepperoni + ", " + this.cheese + ", " + this.bacon ;
    }
    
    public static void main(String[] args) {
        BuilderPattern test = new BuilderPattern.Builder(2).pepperoni(true).cheese(false).build();
        log.info(test.toString());
        BuilderPattern test2 = BuilderPattern.createBuilder(3).build();
        log.info(test2.toString());
    }
    
    
}






