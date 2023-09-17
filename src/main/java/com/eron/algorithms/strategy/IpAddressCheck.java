/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证ip地址正确性问题
 * 可使用java 自带的  IpAdress方法
 * 可使用  正则表达式
 * <p>
 * 一般存储ip地址的时候可以把ip地址转换成int类型  减少存储空间, 取出的时候直接转换回来即可
 * mysql数据库中有 转换ip的函数直接使用也可以   inet_aton   inet_ntoa
 *
 * @author ERON_AMD
 */
public class IpAddressCheck {

    private static final Logger log = LoggerFactory.getLogger(IpAddressCheck.class);

    public static void main(String[] args) {
        //  . 分割  ipv4    ipv6 冒号分割  
        //  ipv4     10 进制, 0-255      不能以0开头     是数字字符, 且范围0-255 
        //   ipv6     长度不超过4个, 16进制  
        String ip = "123:23:34:43";
        if ("IPV4".equals(isValidIPV4(ip))) {
            System.out.println("IPV4");
        } else if ("IPV6".equals(isValidIPV4(ip))) {
            System.out.println("IPV6");
        } else {
            System.out.println("Neither");
        }


        // 测试ip地址转换 
        IpAddressCheck ipUtils = new IpAddressCheck();

        log.info("地址转换 ipv4 =========================");
        String originIP = "192.168.101.52";
        Integer number = ipUtils.convertIPToInteger(originIP);
        log.info("ip -> integer : {}", number);
        String parsedIP = ipUtils.parseIntegerToIP(number);
        log.info("integer -> ip : {}", parsedIP);

    }

    public static String isValidIPV4(String ip) {  // 判断ipv4
        String[] ipSplit = ip.split("\\.", -1);
        for (String num : ipSplit) {
            if (num.length() <= 0 || num.length() > 3) {
                return "Neither";
            }
            if (num.charAt(0) == '0' && num.length() != 1) {
                return "Neither";
            }
            for (char x : num.toCharArray()) {
                if (!Character.isDigit(x)) {
                    return "Neither";
                }
            }

            if (Integer.parseInt(num) > 255) {
                return "Neither";
            }
        }

        return "IPV4";
    }

    public static String isValidIPV6(String ip) {
        String[] ipSplit = ip.split(":", -1);
        String hex = "0123456789abcdefABCDEF";

        for (String num : ipSplit) {
            if (num.length() <= 0 || num.length() > 4) {
                return "Neither";
            }
            for (char x : num.toCharArray()) {
                if (hex.indexOf(x) == -1) {
                    return "Neither";
                }
            }
        }

        return "IPV6";
    }

    /**
     * ipv4 地址转换
     * 从ip地址转换为integer
     * 从integer 转换为ip地址
     */
    public Integer convertIPToInteger(String ip) {  // 形式如 192.168.101.11 
        // 先将ip转换为 byte[]
        String[] ipAddress = ip.split("\\.");
        Arrays.asList(ipAddress).forEach((x) -> log.info("ip 分割 : {}", x));

        Byte[] ipAddressBytes = new Byte[ipAddress.length];
        ipAddressBytes[0] = (byte) (Integer.parseInt(ipAddress[0]));
        ipAddressBytes[1] = (byte) (Integer.parseInt(ipAddress[1]));
        ipAddressBytes[2] = (byte) (Integer.parseInt(ipAddress[2]));
        ipAddressBytes[3] = (byte) (Integer.parseInt(ipAddress[3]));

        // 再将byte[] 转换为 integer
        Integer convertResult = ipAddressBytes[3] & 0xFF;
        convertResult |= (ipAddressBytes[2] << 8) & 0xFF00;
        convertResult |= (ipAddressBytes[1] << 16) & 0xFF0000;
        convertResult |= (ipAddressBytes[0] << 24) & 0xFF000000;

        log.info("最终结果 {} : {}", convertResult, Integer.toBinaryString(convertResult));

        return convertResult;
    }

    public String parseIntegerToIP(Integer address) {

        StringBuilder parseResult = new StringBuilder();
        parseResult.append(((address & 0xFF000000) >> 24) & 0xFF).append(".");  // 注意 这里需要再次 & 0xFF 否则会认为是signedInteger
        parseResult.append((address & 0xFF0000) >> 16).append(".");
        parseResult.append((address & 0xFF00) >> 8).append(".");
        parseResult.append((address & 0xFF));

        // Integer有方法可以直接转换为二进制   ==  验证可行
        String addressBinary = Integer.toBinaryString(address);
        log.info("输入的integer显示为二进制序列String -> {}", addressBinary);
        for (int i = 0; i < addressBinary.length(); i = i + 8) {
            log.info("切取二进制字符串 -> {}", Integer.parseInt(addressBinary.substring(i, i + 8), 2));
        }

        return parseResult.toString();
    }


}












