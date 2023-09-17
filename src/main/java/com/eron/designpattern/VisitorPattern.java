/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.designpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ERON_AMD
 */
public class VisitorPattern {

    interface Person {
        public void feed(Animal animal);
    }

    interface Animal {
        public void accept(Person person);
    }

    static class Owner implements Person {

        @Override
        public void feed(Animal animal) {
            animal.accept(this);
        }

    }

    static class OtherPerson implements Person {

        @Override
        public void feed(Animal animal) {
            animal.accept(this);
        }

    }

    static class Dog implements Animal {

        @Override
        public void accept(Person person) {
            person.feed(this);
        }

    }

    static class Cat implements Animal {

        @Override
        public void accept(Person person) {
            person.feed(this);
        }

    }

    static class Home {
        private List<Animal> animals = new ArrayList<Animal>();

        public void add(Animal animal) {
            this.animals.add(animal);
        }

        public void action(Person person) {
            for (Animal animal : animals) {
                animal.accept(person);
            }
        }
    }

    public static void main(String[] args) {  // 指令控制在home, Person帮助做home需要的一部分, home执行目标animal, 不自己执行, 通过Person执行 
        Home home = new Home();
        home.add(new Dog());
        home.add(new Cat());

        Owner owner = new Owner();
        home.action(owner);

        OtherPerson other = new OtherPerson();
        home.action(other);
    }
}
