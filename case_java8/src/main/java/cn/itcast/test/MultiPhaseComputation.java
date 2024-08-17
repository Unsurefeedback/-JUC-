package cn.itcast.test;

/**
 * @author WeiHanQiang
 * @date 2024/08/17 15:44
 **/
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
// 关于 CyclicBarrier 的使用方式
public class MultiPhaseComputation {
    private static final int NUM_THREADS = 4;
    private static final int NUM_PHASES = 3;

    public static void main(String[] args) {
        // CyclicBarrier，用于在每个阶段结束时同步线程
        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
            System.out.println("All threads have reached the barrier. Proceeding to the next phase...");
        });

        for (int i = 0; i < NUM_THREADS; i++) {
            new Thread(new Worker(barrier, NUM_PHASES)).start();
        }
    }

    static class Worker implements Runnable {
        private final CyclicBarrier barrier;
        private final int numPhases;

        public Worker(CyclicBarrier barrier, int numPhases) {
            this.barrier = barrier;
            this.numPhases = numPhases;
        }

        @Override
        public void run() {
            try {
                for (int phase = 1; phase <= numPhases; phase++) {
                    // 模拟每个阶段的计算任务
                    System.out.println(Thread.currentThread().getName() + " is working on phase " + phase);
                    Thread.sleep((long) (Math.random() * 1000)); // 模拟计算时间

                    // 等待所有线程完成当前阶段的工作
                    barrier.await();

                    // 在每个阶段结束后的操作（如数据汇总等）
                    System.out.println(Thread.currentThread().getName() + " finished phase " + phase);
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
