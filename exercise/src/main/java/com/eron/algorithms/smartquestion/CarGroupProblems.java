package com.eron.algorithms.smartquestion;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 车队问题 
 * 给出一系列车的位置和速度 终点位置--> 所有均在一维平面上 
 * 求最终会形成几个车队 
 * @author eron 
 * 
 */
public class CarGroupProblems {
    private static final Logger log = LoggerFactory.getLogger(CarGroupProblems.class);
    public static void main(String[] args) {
        Integer[] position = new Integer[] {3};// {10,8,0,5,3};
        Integer[] speed = new Integer[] {3}; //{2,4,1,1,3};
        Integer target = 12;
        
        CarGroupProblems carGroupProblems = new CarGroupProblems();
        carGroupProblems.solveCars(position, speed, target);
        
        // 求相遇时间 
        log.info("计算相遇时间...");
        Integer[][] carArrays = new Integer[][] {
//            {1, 2}, 
//            {2, 1},
//            {4, 3}, 
//            {7, 2},
            {3, 4}, 
            {5, 4},
            {6, 3},
            {9, 1}
        };
        carGroupProblems.solveCars2(carArrays);
    }
    
    // 解决车队数量问题 
    public void solveCars(Integer[] position, Integer[] speed, Integer target) {
        List<Car> cars = this.buildCars(position, speed, target);
        Collections.sort(cars);
        cars.forEach(car -> log.info("car info : {}", car.toString()));
        int ans = 0;
        for(int i = 0; i < cars.size() - 1; i++) {
            if(cars.get(i).time < cars.get(i+1).time) {
                ans++;
            }else {
                cars.set(i+1, cars.get(i));
            }
        }
        ans++;
        log.info("最终车队数量 --> {}", ans);
    }
    public List<Car> buildCars(Integer[] position, Integer[] speed, Integer target) {
        List<Car> cars = new ArrayList<>(position.length);
        for(int i = 0; i < position.length; i++) {
            cars.add( new Car( position[i], speed[i], (float) ((target - position[i])/speed[i]) ) );
        }
        return cars;
    }
    private static class Car implements Comparable<Car>{
        Float time;
        Integer speed;
        Integer pos;
        public Car(Integer pos, Integer speed, Float time) {
            this.pos = pos;
            this.speed = speed;
            this.time = time;
        }
        @Override 
        public int compareTo(Car o) {  // 按照位置 降序 
            return (int) (o.pos - this.pos);
        }
        @Override
        public String toString() {
            return "Car [time=" + time + ", speed=" + speed + ", pos=" + pos + "]";
        }
    }
    
    // 构建cars list  给出的原始数据是 car[i] = [pos_i, speed_i], 保证所有i位置 < i+1 位置 
    // 因为没有终点距离 只要后面的比前面的速度快，就一定能追上 
    // t = (a2.pos - a1.pos) / (a2.speed - a1.speed) 
    public void solveCars2(Integer[][] carsArray) {
        int n = carsArray.length;  // 车辆数量 
        Double[] ans = new Double[n]; ans[n - 1] = -1D;
        Deque<Integer> stack = new LinkedList<>(); stack.push(n-1);  // 插入栈顶 
        
        for(int i = n - 2; i >= 0; i--) {  // n-2 因为第一个前面没有车 
            while(!stack.isEmpty()) {
                Integer topPos = carsArray[stack.peek()][0];
                Integer topSpeed = carsArray[stack.peek()][1];
                // 当前车追上 栈顶车辆 所需要的时间 > 栈顶车辆追上它右面的车的时间 , 表示看栈顶右边的车即可 
                // 前面的车速快 追不上 
                Double t = carsArray[i][1] - topSpeed > 0.001 ? (topPos - carsArray[i][0]) / (double)(carsArray[i][1] - topSpeed) : Integer.MAX_VALUE; 
                Boolean checkSpeed = carsArray[i][1] <= topSpeed;  // 当前车速度 <= 栈顶 
                Boolean checkAns = ans[stack.peek()] > 1e-9 && t > ans[stack.peek()];  // 栈顶追不上更右边的 且 当前追上的时间比栈顶的时间大  
                if(checkSpeed || checkAns) {  // 两种情况 追不上 + 能追上但是前面的已经合并到它的右车队了 
                    stack.pop();
                }else {
                    break;
                }
            }
            
            if(stack.isEmpty()) {
                ans[i] = -1D;
            }else {
                // 栈顶有值 计算时间 
                ans[i] = (carsArray[stack.peek()][0] - carsArray[i][0]) / (double)(carsArray[i][1] - carsArray[stack.peek()][1]);
            }
            stack.push(i);
        }
        
        Arrays.asList(ans).forEach(x -> {
            log.info("输出 -> {}", x);
        });
    }
}








