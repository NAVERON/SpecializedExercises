/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

/**
 * 验证ip地址正确性问题 
 * 可使用java 自带的  IpAdress方法
 * 可使用  正则表达式 
 * @author ERON_AMD
 */
public class IpAdressChedk {
    
    public static void main(String[] args) {
        //  . 分割  ipv4    ipv6 冒号分割  
        //  ipv4     10 进制, 0-255      不能以0开头     是数字字符, 且范围0-255 
        //   ipv6     长度不超过4个, 16进制  
        String ip = "123:23:34:43";
        if("IPV4".equals(isValidIPV4(ip))){
            System.out.println("IPV4");
        }else if("IPV6".equals(isValidIPV4(ip))){
            System.out.println("IPV6");
        }else{
            System.out.println("Neither");
        }
    }
    
    public static String isValidIPV4(String ip){  // 判断ipv4
        String[] ipSplit = ip.split("\\.", -1);
        for(String num : ipSplit){
            if(num.length() <= 0 || num.length() > 3){
                return "Neither";
            }
            if(num.charAt(0) == '0' && num.length() != 1){
                return "Neither";
            }
            for(char x : num.toCharArray()){
                if(!Character.isDigit(x)){
                    return "Neither";
                }
            }
            
            if(Integer.parseInt(num) > 255){
                return "Neither";
            }
        }
        
        return "IPV4";
    }
    
    public static String isValidIPV6(String ip){
        String[] ipSplit = ip.split(":", -1);
        String hex = "0123456789abcdefABCDEF";
        
        for(String num : ipSplit){
            if(num.length() <= 0 || num.length() > 4){
                return "Neither";
            }
            for(char x : num.toCharArray()){
                if(hex.indexOf(x) == -1){
                    return "Neither";
                }
            }
        }
        
        return "IPV6";
    }
}
