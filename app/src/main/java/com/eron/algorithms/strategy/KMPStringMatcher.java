package com.eron.algorithms.strategy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 经典字符串匹配算法  http://jakeboxer.com/blog/2009/12/13/the-knuth-morris-pratt-algorithm-in-my-own-words/ 
 * @author other
 * resource : https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm 
 */
public class KMPStringMatcher {

    private static final Logger log = LoggerFactory.getLogger(KMPStringMatcher.class);

	public static void main(String[] args) {
		// 测试kml算法
		String text = "ababdhfj";
		String pattern = "bdh";
		
		KMP matcher = new KMP(pattern);
		int res = matcher.match(text);
		log.info("匹配结果 --> {}", res);
		
		KMPStringMatcher matcher2 = new KMPStringMatcher();
		int x = matcher2.search(text, pattern);
		log.info("一维数组 KMP算法实现 --> {}", x);
		
	}
	
	/**
	 * 只能匹配 256 ascii 编码模式匹配  ascii 0位统一为0, 所以实际上只有128个ascii编码 2^8 
	 * @author eron
	 *
	 */
	private static class KMP {
	    
	    private final int[][] matcher;
	    private final String pattern;
        
	    public KMP(String pattern) {
	        this.matcher = this.buildPattern(pattern);
	        this.pattern = pattern;
	    }
	    
	    /**
	     * 执行匹配 
	     * @param origin 原始字符串 
	     * @param pattern 匹配字符串 
	     */
	    public int match(String origin) {
	        // 匹配计算方法 
	        int M = origin.length();
	        int N = this.pattern.length();
            
	        int j = 0;
	        for(int i = 0; i < M; i++) {
	            log.info("当前匹配 --> {}", origin.charAt(i));
	            j = this.matcher[j][origin.charAt(i)];
	            log.info("获取的j --> {}", j);
	            if(j == N) return i - N + 1;
	        }
            
	        return -1;
	    }
	    
	    private int[][] buildPattern(String pattern) { 
	        int L = pattern.length();
	        int[][] x = new int[L][256];
            
	        x[0][pattern.charAt(0)] = 1;
	        int s = 0;
            
	        for(int i = 0; i < L; i++) {
	            for(int j = 0; j < 256; j++) {
	                if (pattern.charAt(i) == j) {  // 迭代更新过程中能够计算出公共前缀的影子 s索引位置
	                    x[i][j] = i + 1;
	                } else {
	                    x[i][j] = x[s][j];
	                }
	            }
	            
	            s = x[s][pattern.charAt(i)];
	        }
	        
	        return x;
	    }
	    
	}
	
	// KMP table  get the array that stores the longest subarray whose prefix is also its suffix 
	public Integer[] particalMatchTable(String pattern) {  // 前缀后缀公共最长字串  also known as "failure function" 
		char[] patternChars = pattern.toCharArray();
		
		Integer[] checkerTable = new Integer[patternChars.length + 1];
		checkerTable[0] = -1; // 初始化第一个值 
		int pos = 1, cnd = 0;  // pos 待匹配字符串索引   cnd -> checker 索引 
		
		for(char x : patternChars) {
			if (patternChars[pos] == patternChars[cnd]) {
				checkerTable[pos] = checkerTable[cnd];
			} else {
				checkerTable[pos] = cnd;
				while(cnd >= 0 && patternChars[pos] != patternChars[cnd]) {
					cnd = checkerTable[cnd];
				}
			}
			
			pos += 1;
			cnd += 1;
		}
		checkerTable[pos] = cnd;
		
		return checkerTable;
	}
	
	// KMP 算法摘录  需要理解
	public int search(String str, String pattern) {
		char[] strs = str.toCharArray();
		char[] patterns = pattern.toCharArray();

		int L = strs.length, N = patterns.length;
		int i = 0, j = 0; // i: str pointer, j: pattern pointer

		// 特殊情况处理 
		if(N < 1) return 0;
		if(L < N) return -1;

		int[] lps = lps(pattern); // get the array that stores the longest subarray whose prefix is also its suffix
		while(i < L) {
		    // 如果两个char相等 前进1位
			if(strs[i] == patterns[j]) { // same value found, move both str and pattern pointers to their right
			    ++i; 
			    ++j;
			    if(j == N) return i-N; // whole match found
			}
			// 判断回溯位置 
			else if(j > 0) j = lps[j-1]; // move pattern pointer to a previous safe location
			// 如果j在起始位置 表示第一个字符就不匹配 
			else ++i; // restart searching at next str pointer
		}
		return -1;
	}

	// next指针数组生成 关键**
	private int[] lps(String pattern) {
		int j=0, i=1, L=pattern.length();  // 两个指针

		int[] res = new int[L];

		char[] chars = pattern.toCharArray();

		while(i<L) {
			if(chars[i]==chars[j]){
                res[i++] = ++j;  // j++; res[i] = j; i++;
            } else {
				int temp = i-1;  // 上一个可以判断公共前后缀的信息 
				while(temp>0) {
					int prevLPS = res[temp];
					if(chars[i]==chars[prevLPS]) {  // 表示前后缀匹配+1 i的位置就是新增相同char的索引 
						res[i++] = prevLPS+1;
						j = prevLPS;
						break;
					}
					else temp = prevLPS-1;  // 如果不匹配 找前部分前缀的对称位置 res[temp - 1] 获取前面的位置, 因为对称性 
				}
				if(temp<=0) {
					res[i++] = 0;
					j = 0;
				}
			}
		}
		return res;
	}
	
}













