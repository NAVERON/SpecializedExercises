package com.eron.designpattern;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Point2D;
/**
 * Java经典模板方法  使用函数方法传入代替 继承 + 方法重写
 * @author eron
 *
 */
public class TemplatePattern {

	public static void main(String[] args) {
		// 测试用例 
		CookProcessTemplate cooker = new CookProcessTemplate();
		
		// 做两道不同的菜  根据业务实现需要增加 
		cooker.eggFriedRice();
		cooker.shreddedPotatoes();
		
	}
	
	// 当前没有模板方法的完全思路, 以后再实现  
	// 路径生成模板 提供一个起始地点, 路径生成算法 
	// 使用函数编成可以不像之前那样需要继承和重写方法实现, 直接提供不同的函数实现即可 
	public static class CookProcessTemplate {  // 实现炒菜的模板类 
		
		private static final Logger log = LoggerFactory.getLogger(CookProcessTemplate.class);
		
		public final void wokTools() {  // 公共的 
			log.info("准备炒菜的前期准备");
		}
		
		public final void placeToPlate() {
			log.info("把做好的饭放到盘子上");
		}
		
		public final void cooking(Supplier<Point2D> start, Consumer<Point2D> deal) {
			log.info("实际的处理中, 根据不同的制作方法应当实现不同的处理过程");
			
			Point2D init = start.get();
			log.info("获取到初始数据 : {}", init.toString());
			deal.accept(init);  // 对获取的数据进一步处理 
			log.info("使用传入的consumer 函数处理完成...");
		}
		
		// 可以传入多个处理函数 根据需要变化 对外暴露的接口/直接使用此方法作为门面 
		@SafeVarargs
		public final void make(Supplier<Point2D> start, Consumer<Point2D> deal, Consumer<String> ...unknow) {
			log.info("=================================");
			
			this.wokTools();
			this.cooking(start, deal);
			this.placeToPlate();
			
			log.info("----------------------------------");
		}
		
		// 自己调用make 并传入自定义处理函数 
		public void eggFriedRice() {
			Supplier<Point2D> startPosition = () -> new Point2D(123, 111);
			Consumer<Point2D> dealPosition = (x) -> log.info("deal start position -> {}", x);
			Consumer<String> other = (x) -> log.info("输入数据进行处理 -> {}", x);  // 额外的处理 
			
			this.make(startPosition, dealPosition, other);
		}
		// 自定义实现 案例2 
		public void shreddedPotatoes() {
			Supplier<Point2D> first = () -> new Point2D(145, 999);
			Consumer<Point2D> checkout = new CustomComsumer();
			
			this.make(first, checkout);
		}
		
	}
	
	public static class CustomComsumer implements Consumer<Point2D>{  // 自定义实现消费接口 

		private static Logger log = LoggerFactory.getLogger(CustomComsumer.class);
		
		@Override
		public void accept(Point2D t) {
			log.info("实现接口对象, 计算距离 : {}", t.distance(123, 222));
		}
		
	}
	
}









