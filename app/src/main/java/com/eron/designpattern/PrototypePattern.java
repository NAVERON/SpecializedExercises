package com.eron.designpattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 一种创建模式
 * 主要是复制一个相同的对戏那个
 * 改进的原型模式 有缓存功能, 因为每个对戏那个都是从缓存对象复制过来的
 * @author eron
 *
 */
public class PrototypePattern {
    
    public static void main(String[] args) {
	List<Shape> shapes = new ArrayList();
	List<Shape> copys = new ArrayList();
	
	Circle test1 = new Circle();
	test1.x = 10;
	test1.y = 3;
	test1.color = "red";
	test1.radis = 5;
	
	shapes.add(test1);
	Circle test2 = (Circle) test1.clone();
	shapes.add(test2);
	
	Rectangle test3 = new Rectangle();
	test3.x = 100;
	test3.y = 35;
	test3.color = "blue";
	test3.height = 65;
	test3.width = 90;
	shapes.add(test3);
    }
    
    public static abstract class Shape {
	
	public int x ;
	public int y ;
	public String color ;
	
	public Shape (){}
	
	public Shape (Shape target) {
	    if (target != null) {
		this.x = target.x;
		this.y = target.y;
		this.color = target.color;
	    }
	}
	
	public abstract Shape clone();

	@Override
	public boolean equals(Object obj) {
	    if (! (obj instanceof Shape)) return false;
	    Shape other = (Shape) obj;
	    return this.x == other.x && this.y == other.y && Objects.equals(this.color, other.color);
	}
	
    }
    
    public static class Circle extends Shape {
	public int radis ;
	
	public Circle () {}
	public Circle (Circle target) {
	    super(target);
	    if(target != null) {
		this.radis = target.radis;
	    }
	}

	@Override
	public Shape clone() {
	    return new Circle(this);
	}
	@Override
	public boolean equals(Object obj) {
	    if ( !(obj instanceof Circle) || !super.equals(obj) ) return false;
	    Circle other = (Circle) obj;
	    return this.radis == other.radis;
	}
	
    }
    
    public static class Rectangle extends Shape {
	public int width ;
	public int height ;
	
	public Rectangle () {}
	public Rectangle (Rectangle target) {
	    super(target);
	    if (target != null) {
		this.width = target.width;
		this.height = target.height;
	    }
	}
	
	@Override
	public Shape clone() {
	    return new Rectangle(this);
	}
	@Override
	public boolean equals(Object obj) {
	    if ( !(obj instanceof Rectangle) || !super.equals(obj) ) return false;
	    Rectangle other = (Rectangle) obj;;
	    return this.width == other.width && this.height == other.height;
	}
    }
    
    public static class BundleShapeCache {
	
	public Map<String, Shape> caches = new HashMap();
	
	public BundleShapeCache() {
	    Circle test1 = new Circle();
	    test1.x = 10;
	    test1.y = 3;
	    test1.color = "red";
	    test1.radis = 5;

	    Circle test2 = (Circle) test1.clone();

	    Rectangle test3 = new Rectangle();
	    test3.x = 100;
	    test3.y = 35;
	    test3.color = "blue";
	    test3.height = 65;
	    test3.width = 90;
	    
	    this.caches.put("test1", test1);
	    this.caches.put("test2", test2);
	    this.caches.put("test3", test3);
	}
	
	public Shape put(String key, Shape shape) {
	    this.caches.put(key, shape);
	    return shape;
	}
	
	public Shape get(String key) {
	    return this.caches.get(key);
	}
    }

}
