package com.eron.designpattern;

/**
 * 实现枚举类型的轮询实现 比如一些情况下需要方便转换运行状态, 且只会从一种状态转变为另一种状态, 需要这种模式
 * 不容易出错, 方便修改 
 * @author eron
 *
 */
public class CyclizedStatusEnum {

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
		
		public Integer getCount() {
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
		
		public String toString() {
			return "order : " + this.order + "; name : " + this.name;
		}
	}
	
	public static void main(String[] args) {
		BusinessStatus x = BusinessStatus.WAIT;
		
		// 循环一周验证 
		for(int i = 0; i < 15; i++) {
			System.out.println(x.nextStatus().toString());
			x = x.nextStatus();
		}
		
	}
	
}












