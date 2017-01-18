package ru.codewar.logicconveyor;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class MultiThreadPerfomanceTests {

    class Logic {
        private AtomicInteger index = new AtomicInteger(0);
        private AtomicInteger threadsCount = new AtomicInteger(0);
        private ArrayList<Double> array;
        private int totalObjects;
        private int actionsPerObject;

        Logic(int totalObjects, int actionsPerObject) {
            this.totalObjects = totalObjects;
            this.actionsPerObject = actionsPerObject;
        }

        public void init() {
            array = new ArrayList<>(totalObjects);
            for(int i = 0; i < totalObjects; i++) {
                array.add(i, (double)i);
            }
        }

        public void proceed()
        {
            threadsCount.getAndIncrement();
            long startTime = System.currentTimeMillis();
            for(int i = index.getAndIncrement(); i < totalObjects; i = index.getAndIncrement()) {
                Double value = array.get(i);
                for(int j = 0; j < actionsPerObject; j++)
                    value = Math.sin(value) + Math.cos(value);
                array.set(i, value);
            }
            System.out.print((System.currentTimeMillis() - startTime) + "\t");
            if(threadsCount.decrementAndGet() == 0) {
                index.set(0);
                System.out.print("\n");
            }
        }
    }

    class Worker extends Thread {
        private Logic logic;
        private CyclicBarrier barrier;
        private int totalProceeds;

        public Worker(Logic logic, CyclicBarrier barrier, int totalProceeds)
        {
            this.logic = logic;
            this.barrier = barrier;
            this.totalProceeds = totalProceeds;
        }

        private void waitOnBarrier() {
            try {
                barrier.await();
            } catch (Exception exp) {}
        }

        @Override
        public void run() {
            for(int i = 0; i < totalProceeds; i++) {
                waitOnBarrier();
                logic.proceed();
            }
        }
    }

    @Test
    public void simpleTest() {
        int totalThreads = 4;
        int totalObjects = 500000;
        int actionsPerObject = 30;
        int totalProceeds = 10;

        Logic logic = new Logic(totalObjects, actionsPerObject);
        logic.init();

        CyclicBarrier barrier = new CyclicBarrier(totalThreads);
        Worker workers[] = new Worker[totalThreads];
        for(int i = 0; i < totalThreads; i++) {
            workers[i] = new Worker(logic, barrier, totalProceeds);
            if(i > 0)
                workers[i].start();
        }

        long startTime = System.currentTimeMillis();
        workers[0].run();
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

}
