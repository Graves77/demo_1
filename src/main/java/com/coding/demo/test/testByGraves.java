package com.coding.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class testByGraves implements Runnable{
    private int i ;
    @Override
    public void run() {
        for(;i<50;i++){
            System.out.println(Thread.currentThread().getName() + " ---run--- " + i);
        }
    }
    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            System.out.println(Thread.currentThread().getName() + " ---main--- " + i);
            if(i==20){
                testByGraves runnableTest = new testByGraves() ;
                new Thread(runnableTest,"线程1").start() ;
                new Thread(runnableTest,"线程2").start() ;
            }
        }
    }
}
