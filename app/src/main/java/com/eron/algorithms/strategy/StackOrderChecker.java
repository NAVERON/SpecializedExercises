package com.eron.algorithms.strategy;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据已知的压栈判断是否可能出现某种出栈情况
 * @author eron 
 * 使用辅助栈判断 
 */
public class StackOrderChecker {
	
	private static final Logger log = LoggerFactory.getLogger(StackOrderChecker.class);

	public static void main(String[] args) {
		StackOrderChecker checker = new StackOrderChecker();
		
		// 输入压栈list  检测出栈list 
		Integer[] pushStack = new Integer[] {1,2,3,4,5}; 
		Integer[] popStack = new Integer[] {4,5,3,1,2}; 
		
		Boolean result = checker.checkValidOrder(pushStack, popStack);
		log.info("最终比对结果 : {}", result);
	}
	
	
	public Boolean checkValidOrder(Integer[] pushStack, Integer[] popStack) {
		// 检查输入的两个参数合法性 
		Stack<Integer> assistantStack = new Stack<Integer>();
		
		Integer indexOfPop = 0;  // 待检测对象当前的位置 
		for(int i = 0; i < pushStack.length; i++) {
			assistantStack.push(pushStack[i]);
			
			while( !assistantStack.isEmpty() && assistantStack.peek().equals(popStack[indexOfPop]) ) {
				assistantStack.pop();
				
				indexOfPop++;
			}
			
		}
		
		return assistantStack.isEmpty();  // 查看辅助stack是否全部match  如果全部match表示是正常的入栈出栈 
		
	}
	
}









