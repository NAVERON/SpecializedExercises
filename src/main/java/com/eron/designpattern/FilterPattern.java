package com.eron.designpattern;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 过滤器模式  通用层层筛选集合
public class FilterPattern {

    /**
     * 过滤起的实现方式有一点缺陷, 知呢个针对某一类行的对戏那个过滤, 没有通用型
     *
     * @author eron
     */
    public static abstract class Person {
        public String name;
        public String sex;
        public int age;
    }

    public static class Male extends Person {
        private String mark = "male";
    }

    public static class Female extends Person {
        private String wearing = "nothing";
    }

    public interface Filter { // 单个过滤
        public boolean apply(Person person);
    }

    public static class MaleFilter implements Filter {

        String sex = "male"; // 过滤器中的条件

        public MaleFilter(String sex) {
            this.sex = sex;
        }

        @Override
        public boolean apply(Person person) {
            // 检查是否为男性/女性
            return false;
        }

    }

    public static class SexFilter implements Filter {

        int age = 10; // 过滤器的条件, 可以初始化的时候传入过滤条件,达到改变条件的多样性

        public SexFilter(int minAge) {
            this.age = minAge;
        }

        @Override
        public boolean apply(Person person) {
            return false;
        }

    }

    public static class AndFilter implements Filter {

        Filter firstFilter = null;
        Filter secondFilter = null;

        public AndFilter(Filter first, Filter second) {
            this.firstFilter = first;
            this.secondFilter = second;
        }

        @Override
        public boolean apply(Person person) {
            return firstFilter.apply(person) && secondFilter.apply(person);
        }
    }

    public static class OrFilter implements Filter {
        // 组合过滤器一样的思路, 传入多个过滤器, 同时满足才会返回true, 否则去除对象
        @Override
        public boolean apply(Person person) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(new Male(), new Female());

        FilterPattern filterMain = new FilterPattern();

        List<Person> testList = filterMain.applyFilter(persons, new MaleFilter("male"));
    }

    public List<Person> applyFilter(List<Person> persons, Filter filter) {
        return persons.stream().filter(x -> filter.apply(x)).collect(Collectors.toList());
    }

}
