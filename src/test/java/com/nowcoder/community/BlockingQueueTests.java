package com.nowcoder.community;

import org.junit.jupiter.api.MethodOrderer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

//定义生产者线程，实现Runnable接口创建线程
class Producer implements Runnable {

    private BlockingQueue<Integer> queue;
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    //重写run方法
    @Override
    public void run() {
        try{
            for(int i = 0; i < 100; i++) {
                Thread.sleep(10);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产："+queue.size());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}

//定义消费者线程，实现Runnable接口创建线程
class Consumer implements Runnable {

    private BlockingQueue<Integer> queue;
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    //重写run方法
    @Override
    public void run() {
        try{
            while(true) {
                Thread.sleep(new Random().nextInt(100));
                queue.take();
                System.out.println(Thread.currentThread().getName() + "消费："+queue.size());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
