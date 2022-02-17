/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.designpattern;

/**
 *
 * @author ERON_AMD
 */
public class AbstractFactoryPattern {
    // 创建工厂 FactoryCreater
    // 工厂创建产品  ProductCreater
    
    static abstract class AbstractFactory {
        public Shape getShape(String shapeType){
            return null;
        }
        
        public Color getColor(String colorType) {
            return null;
        }
    }
    
    interface Shape {
        public void draw();
    }
    // 很多shape的实现
    
    interface Color {
        public void fill();
    }
    //很多color的是心啊
    
    static class ShapeFactory extends AbstractFactory {

        @Override
        public Color getColor(String colorType) {
            return super.getColor(colorType); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Shape getShape(String SahpeType) {
            return super.getShape(SahpeType); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    static class ColorFactory extends AbstractFactory {

        @Override
        public Color getColor(String colorType) {
            return super.getColor(colorType); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Shape getShape(String SahpeType) {
            return super.getShape(SahpeType); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    static class GenerateFactory {
        public static AbstractFactory getFactory(String mark){
            if(mark.equals("shape")){
                return new ShapeFactory();
            }else if (mark.equals("color")){
                return new ColorFactory();
            }else {
                return null;
            }
        }
    }
    
    public static void main(String[] args) {
        Shape testShape = GenerateFactory.getFactory("shape").getShape("circle");
        Color testColor = GenerateFactory.getFactory("color").getColor("blue");
        
        testShape.draw();
        testColor.fill();
    }
}
