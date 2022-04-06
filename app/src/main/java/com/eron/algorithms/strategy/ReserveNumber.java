package com.eron.algorithms.strategy;


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
	
}















