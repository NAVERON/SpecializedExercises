package com.eron.algorithms.strategy;

import java.util.Arrays;


// 两个排序好的数组  求中位数 
// 偶数 奇数 
public class MedianOfTwoArray {
	
	public static void main(String[] args) {
		
		MedianOfTwoArray medianOfTwoArray = new MedianOfTwoArray();
		// 给定的两个数组是已经排序好的 
		Integer[] arr1 = {1, 4, 6, 7, 9, 25};
		Integer[] arr2 = {1, 3, 6, 8, 14, 22, 32, 43};
		
		medianOfTwoArray.bruteForce(arr1, arr2);
		
		Double result = medianOfTwoArray.binarySearch(arr1, arr2);
		System.out.println("二分法结果 " + result);
	}

	// 暴力解决 求中位数. 先合并， 后直接计算 时间复杂度 O(m+n), 空间复杂度 O(m+n) // 额外需要m+n数组空间 
	public void bruteForce(Integer[] arr1, Integer[] arr2) {
		int len1 = arr1.length;
		int len2 = arr2.length;
		
		boolean isEven = (len1 + len2) % 2 == 0 ? true : false;  // 偶数还是奇数 
		int index1 = 0, index2 = 0, index3 = 0;
		// 自己的思考有点麻烦, 使用两个指针指向 
		// 换一种思路， 指针向前走并填充到新生的一个数组中, 最后直接计算即可 
		
		Integer[] combine = new Integer[len1 + len2];
		
		while (index1 < len1 && index2 < len2) {  // 合并其中一个 直到有一个全部合并完成 
			if(arr1[index1] <= arr2[index2]) {
				combine[index3++] = arr1[index1++];
			} else {
				combine[index3++] = arr2[index2++];
			}
		}
		
		while (index1 < len1) {
			combine[index3++] = arr1[index1++];
		}
		while (index2 < len2) {
			combine[index3++] = arr2[index2++];
		}
		
		// 合并后的数组 
		System.out.println(MedianOfTwoArray.arrToString(combine));
		int combineLength = combine.length;
		
		System.out.println("1 " + combine[combineLength/2 - 1]);
		System.out.println("2 " + combine[combineLength/2]);
		System.out.println("isEven ? " + isEven);
		
		System.out.println("final => " + (combine[combineLength/2 - 1] + combine[combineLength/2])/2.0F);
		
		// Float Or Double 在精确平均值 中使用浮点数 
		Double result = (double) (isEven ? (combine[combineLength/2 - 1] + combine[combineLength/2])/2.0D : combine[combineLength/2]);
		
		System.out.println("最终的中位数是 : " + result);
	}
	
	// 每次划分后 (maxLeftA <= minRightB && maxLeftB <= minRightA) 
	// 无限逼近 分割线临界的4个值求取 
	public Double binarySearch(Integer[] arr1, Integer[] arr2) {
		System.out.println("==============================");
		Double result = 0D;
		// 二分法 查找中位数 
//		奇数：
//		median = max(maxLeftA, maxLeftB)
//		偶数：
//		median = (max(maxLeftA, maxLeftB) + min(minRightA, minRightB)) / 2
		
		/**
		 * 过程 
		 * 1 短数组放前面， 长数组放后面 
		 * 2 遍历短数组， i++, 长数组指针 bIndex = (m+n+1)/2 - aIndex  
		 * 3 找到邻近的4个值, 判断
		 * 4 左右边界 变化，最终缩小到临界 
		 */
		
		Integer len1 = arr1.length;
		Integer len2 = arr2.length;
		
		boolean isEven = (len1 + len2) % 2 == 0 ? true : false;  // 偶数还是奇数 
		
		if(len1 > len2) { // arr1 始终选择短的那个， 减少长短判断 
			MedianOfTwoArray.reverseArray(arr1, arr2);
		}
		
		Integer low = 0;
		Integer high = len1;
		
		while(low <= high) {
			System.out.println("当前 [low, high] : " + low + ", " + high);
			
			int partation1 = (high - low) / 2 + low;
			int partation2 = (len1 + len2 + 1) / 2 - partation1;  // 这样计算就可以保证 i + j = 中间的数量 
			
			int maxLeftA = partation1 == 0 ? Integer.MIN_VALUE : arr1[partation1 - 1];
			int minRightA = partation1 == len1 ? Integer.MAX_VALUE : arr1[partation1];
			int maxLeftB = partation2 == 0 ? Integer.MIN_VALUE : arr2[partation2 - 1];
			int minRightB = partation2 == len2 ? Integer.MAX_VALUE : arr2[partation2];
			
			if(maxLeftA <= minRightB && minRightA >= maxLeftB) {
				// 如果找到临界 
				// 如果是偶数 
				System.out.println("找到时 分界线附近的变量 : [maxLeft1, minRight1, maxLeft2, minRight2] " 
									+ maxLeftA + ", " + minRightA 
									+ ", " + maxLeftB + ", " + minRightB);
				if(isEven) {
					result = Double.sum( Math.max(maxLeftA, maxLeftB), Math.min(minRightA, minRightB) ) / 2;
					System.out.println("结果 " + result);
				}else {
					result = Double.max(maxLeftA, maxLeftB);
					System.out.println("结果 " + result);
				}
				break;
			} else if ( maxLeftA > minRightB ) {
				// partation1 靠右了/太大了， 中位数在左边的地方 
				high = partation1 - 1;  // 缩小查找范围 
			} else {
				low = partation1 + 1;
			}
		}
		
		return result;
	}
	
	public static String arrToString(Object[] objArr) {
		StringBuilder sb = new StringBuilder();
		
		Arrays.asList(objArr).forEach(obj -> {
			sb.append(obj.toString() + ", ");
		});
		
		return sb.toString();
	}
	
	public static void reverseArray(Integer[] arr1, Integer[] arr2) {
		
		System.out.println("近来 : " + MedianOfTwoArray.arrToString(arr1));
		// 交换两个数组 
//		Integer[] arr1Swap = arr2.clone();
//		Integer[] arr2Swap = arr1.clone();
		
		Integer[] tmp = arr1;  // 交换指针更方便 
		arr1 = arr2;
		arr2 = tmp;
		
//		arr1 = arr1Swap;
//		arr2 = arr2Swap;
		
		System.out.println("出来 " + MedianOfTwoArray.arrToString(arr1));
	}
}










