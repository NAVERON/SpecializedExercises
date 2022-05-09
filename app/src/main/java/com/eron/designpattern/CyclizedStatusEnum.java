package com.eron.designpattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现枚举类型的轮询实现 比如一些情况下需要方便转换运行状态, 且只会从一种状态转变为另一种状态, 需要这种模式
 * 不容易出错, 方便修改 
 * @author eron
 *
 */
public class CyclizedStatusEnum {
	
	private static final Logger log = LoggerFactory.getLogger(CyclizedStatusEnum.class);

	public static enum BusinessStatus {
		
		DEFAULT(-1, "ERROR"), 
		RUNNING(0, "running"), 
		IDLE(1, "idle"), 
		CONDITION(2, "condition"), 
		WAIT(3, "wait"), 
		SIGNAL(4, "signal"), 
		PROCESS(5, "process") 
		;
		
		private Integer order;
		private String name;
		
		BusinessStatus(Integer order, String name) {
			this.order = order;
			this.name = name;
		}
		
		public static Integer getCount() {
			Integer count = -1;  // 去除默认值 
			count += BusinessStatus.values().length;
			return count;
		}
		
		public BusinessStatus nextStatus() {
			Integer currentOrder = this.order;
			Integer nextOrder = ( currentOrder + 1 ) % this.getCount();
			
			return getByOrder(nextOrder);
		}
		
		public BusinessStatus getByOrder(Integer order) {
			BusinessStatus checked = BusinessStatus.DEFAULT;
			for(BusinessStatus status : BusinessStatus.values()) {
				if(status.order.compareTo(order) == 0) {
					checked = status;
					break;
				}
			}
			return checked;
		}
		
		// 判断是否是最后一个状态 
		public static Boolean isTail(BusinessStatus curStatus) {
			Boolean isTail = false;
			
			// 找到index比较order或者枚举数量  根据具体的业务场景 
			if(Integer.compare(curStatus.order + 1, BusinessStatus.getCount()) == 0) {  // 这种情况必须是order刚好是从0开始的索引 实际需要具体判断 
				isTail = true;
			}
			
			return isTail;
		}
		
		public String toString() {
			return "order : " + this.order + "; name : " + this.name;
		}
	}
	
	public static void main(String[] args) {
		BusinessStatus x = BusinessStatus.WAIT;
		
		log.info("当前enum数量 : {}", BusinessStatus.getCount());
		
		// 循环一周验证 
		for(int i = 0; i < 15; i++) {
			log.info("当前是否结尾 : {}", BusinessStatus.isTail(x));
			log.info("当前状态 -> {}, 下一个状态 -> {}", x.toString(), x.nextStatus().toString());
			x = x.nextStatus();
		}
		
	}
	
}












