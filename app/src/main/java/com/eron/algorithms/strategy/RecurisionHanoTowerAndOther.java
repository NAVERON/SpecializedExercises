package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 递归类算法 
 * 汉诺塔计算 斐波那契数列问题  等递归问题解决 
 * @author eron 
 *
 */
public class RecurisionHanoTowerAndOther {
	
	private static final Logger log = LoggerFactory.getLogger(RecurisionHanoTowerAndOther.class);

	public static void main(String[] args) {
		// 测试使用汉诺塔算法 
		log.info("汉诺塔计算=========================");
		RecurisionHanoTowerAndOther base = new RecurisionHanoTowerAndOther();
		base.hanoiTowerMove(12, "A", "B", "C");
		
		log.info("斐波那契数列计算----------------------");
		Integer result = base.fibonacciSequence(12);
		log.info("斐波那契额最终结果 ===> {}", result);
		
		log.info("跳台阶问题........................");
		result = base.jumpFloor(3);
		log.info("跳台阶最终结果 ---> {}", result);
		
		
		log.info("自定义实现幂次乘~~~~~~~~~~~~~~~~~~~~~~~");
		Double finalResult = base.customePower(3.0D, 4);
		Double finalResult2 = base.customePower2(3.0D, 4);
		log.info("次方的结果 : {}, {}", finalResult, finalResult2);
		
		
	}
	
	// 直接含义 : 把 n 个圆盘从from移动到to圆盘上 
	public void hanoiTowerMove(Integer n, String from, String buffer, String to) {
		
		if(n == 1) {
			log.info("执行 {} -> {}", from, to);
			return;
		}
		
		// n-1个圆盘从from到buffer 
		hanoiTowerMove(n-1, from, to, buffer);
		// 将一个圆盘从from到to 
		hanoiTowerMove(1, from, buffer, to);
		// 将n-1个圆盘从buffer到to
		hanoiTowerMove(n-1, buffer, from, to);
		
	}
	
	// 斐波那契数列计算  求给定的n的斐波那契额数列 
	public Integer fibonacciSequence(Integer n) {  // 可以使用动态规划的思想保存中间的计算结果 
		
		if(n < 0) {
			throw new IllegalArgumentException();
		}
		
		if(n == 0) return 0;
		if(n == 1) return 1;
		
		Integer result = fibonacciSequence(n-1) + fibonacciSequence(n-2);
		log.info("输出序列 : {}", result);
		
		return result;
	}
	
	// 跳台阶问题  类似的还有加减操作的情况递归
	public Integer jumpFloor(Integer n) {
		
		if(n <= 0) {
			return 0;
		}
		
		if(n == 1) {
			return 1;
		}
		
		if(n == 2) {
			return 2;
		}
		
		return jumpFloor(n-1) + jumpFloor(n-2);
	}
	
	// 计算double树的整数次幂 
	public Double customePower(Double base, Integer exponent) {
		
		if(base - 0 < 0.001) {  // 判断是否为 0  double类型直接比较 0 会有误差 
			return 0.0D;
		}
		
		if(exponent < 0) {  // 确保指数为正数
			base = 1 / base;
			exponent = -1 * exponent;
		}
		
		Double result = 1.0D;
		while(exponent != 0) {
			if( (exponent & 1) == 1 ) {  // 奇数判断, 分离出一个base 
				result *= base;
			}
			
			base = base * base;
			exponent = exponent >> 1;  // 除以2   二分法 
		}
		
		return result;
	}
	public Double customePower2(Double base, Integer exponent) {
		
		if(base - 0 < 0.001) {
			return base;
		}
		
		if(exponent < 0) {
			base = 1 / base;
			exponent = -1 * exponent;
		}
		
		Double result = 1.0 * quickMul(base, exponent);
		
		return result;
	}
	// 次方 使用递归  递归方法体 前提, exponent不能为负, 已经经过参数检查了的 
	public Double quickMul(Double base, Integer exponent) {
		
		if(exponent == 0) {
			return 1.0D;
		}
		Double result = quickMul(base, exponent >> 1);
		log.info("当前状态 : {}, {} -> {}", base, exponent, result);
		if ( (exponent & 1) == 1) { // 奇数
			result = result * result * base;
		} else {
			result = result * result;
		}
		
		return result;
	}
	
	
	
}












