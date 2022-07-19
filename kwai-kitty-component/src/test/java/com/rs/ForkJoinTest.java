package com.rs;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * fork-join 框架包含3个模块
 * 线程池：
 * 任务对象：
 * 执行任务的线程：
 *
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
public class ForkJoinTest {


    @Test
    public void test() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        SumRecursiveTask task = new SumRecursiveTask(1, 10000l);
        Long result = forkJoinPool.invoke(task);
        System.out.println("all sum=" + result);
    }

    /**
     * 使用fork-join计算1-10000的和
     * 当一个任务的计算数量>3000的时候拆分任务，数量<3000的时候就计算
     */
    @Data
    @AllArgsConstructor
    class SumRecursiveTask extends RecursiveTask<Long> {
        private static final long THRESHOLD = 3000;  // 定义一个拆分临界值
        private long start;
        private long end;

        @Override
        protected Long compute() {
            long gap = end - start;
            if (gap <= THRESHOLD) {  // 任务不用拆分 可以计算
                long sum = 0;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                System.out.println("计算：" + start + "->" + end + ",sum=" + sum);
                return sum;
            } else {  // 任务需要继续拆分
                long middle = (start + end) / 2;
                System.out.println("拆分：左边" + start + "->" + middle + ",右边" + (middle+1) + "->" + end);
                SumRecursiveTask leftTask = new SumRecursiveTask(start, middle);
                leftTask.fork();
                SumRecursiveTask rightTask = new SumRecursiveTask(middle + 1, end);
                rightTask.fork();
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
