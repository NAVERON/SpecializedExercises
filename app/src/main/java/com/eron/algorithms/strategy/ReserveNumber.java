package com.eron.algorithms.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 反转数字 如输入 123 -》 321， -123 -》 -321 
 * 范围不能超过32位最大 
 * @author eron
 *
 */
public class ReserveNumber {

	public static void main(String[] args) {
		ReserveNumber reverseNumber = new ReserveNumber();
		
		Boolean status = reverseNumber.isPalamonNumber(1221);
		System.out.println("结果 : " + status);
		
		Integer result2 = reverseNumber.FunctionAToi("+123");
		System.out.println("字符串转数字 : " + result2);
	}
	
	// 反转 一个数字 
	public Integer reserveNumber(Integer number) {
		Integer x = 0;
		Integer finalInteger = 0;
		while (number > 0) {
			if(finalInteger < Integer.MIN_VALUE / 10 || finalInteger > Integer.MAX_VALUE / 10) {
				return number;
			}
			
			x = number % 10;
			number /= 10;
			
			finalInteger = finalInteger * 10 + x; 
		}
		
		return finalInteger;
	}
	
	// 回文数字的判断 
	public Boolean isPalamonNumber(Integer number) {
		// 判断 number是不是负数 负数不可能是回文 
		if(number < 0) return Boolean.FALSE;
		
		int div = 1;
		while(number / div >= 10) {
			div *= 10;
		}
		System.out.println("div : " + div);
		
		while(number > 0) {
			int left = number / div;
			int right = number % 10;
			if(left != right) {
				return Boolean.FALSE;
			}
			
			number = (number % div) / 10;
			div /= 100;  // 左右都要缩减1位 
		}
		
		return Boolean.TRUE;
	}
	
	
	// 字符串转换成数字的方法  将字符串转换成一个 32 位有符号整数 
	public Integer FunctionAToi(String numberString) {
		Automation automation = new Automation();
		for(char x : numberString.toCharArray()) {
			automation.get(x);
		}
		
		Integer ans = (int) (automation.sign * automation.ans);
		return ans;
	}
	
	// 自动机处理方法 字符串 -> 数字  确定有限状态机（deterministic finite automaton, DFA）
	// LeetCode 自动状态机 解决办法 
	public static class Automation {
		private Integer sign = 1;  // 表示正负号 
		private Long ans = 0L;
		private String state = "start"; // 状态机 当前状态 
		
		// 状态机转化规则 
		private Map<String, String[]> autoStatus = new HashMap<String, String[]>() {{
			put("start", new String[]{"start", "signed", "in_number", "end"});
	        put("signed", new String[]{"end", "end", "in_number", "end"});
	        put("in_number", new String[]{"end", "end", "in_number", "end"});
	        put("end", new String[]{"end", "end", "end", "end"});
		}};
		
		public void get(char x) {
			// 获取单个字符 并判断 
			
	        state = autoStatus.get(state)[getCharType(x)];
	        if ("in_number".equals(state)) {
	            ans = ans * 10 + x - '0';
	            ans = sign == 1 ? Math.min(ans, (long) Integer.MAX_VALUE) : Math.min(ans, -(long) Integer.MIN_VALUE);
	        } else if ("signed".equals(state)) {
	            sign = x == '+' ? 1 : -1;  // 正负号 
	        }
		}
		
		public Integer getCharType(char x) {
			// 判断char的分类  数字/空格/正负 ? 
			if (x == ' ') {  // start 
				return 0;
			}
			if(x == '+' || x == '-' ) {  // signed
				return 1;
			}
			if(Character.isDigit(x)) { // in_number 
				return 2;
			}
			
			return 3;
		}
	}
	
	
}















