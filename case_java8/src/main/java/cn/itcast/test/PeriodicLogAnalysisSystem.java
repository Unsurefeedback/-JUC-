package cn.itcast.test;

/**
 * @author WeiHanQiang
 * @date 2024/08/17 15:50
 **/
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicLogAnalysisSystem {

    // 假设有4台服务器
    private static final int NUM_SERVERS = 4;

    public static void main(String[] args) {
        // 创建定时任务执行器
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(NUM_SERVERS);

        // 初始化CyclicBarrier，所有线程到达屏障后进行汇总分析
        CyclicBarrier barrier = new CyclicBarrier(NUM_SERVERS, () -> {
            System.out.println("所有服务器完成日志收集，Proceeding to aggregation...");
            performAggregation();  // 执行汇总分析
        });

        // 定期调度日志处理任务
        Runnable logProcessingTask = () -> {
            for (int i = 0; i < NUM_SERVERS; i++) {
                new Thread(new LogProcessor(barrier)).start();
            }
        };

        // 每隔5秒执行一次日志处理任务
        scheduler.scheduleAtFixedRate(logProcessingTask, 0, 5, TimeUnit.SECONDS);
    }

    // 汇总分析的示例方法
    private static void performAggregation() {
        System.out.println("进行实际的汇总分析或存储操作...");
        // 进行实际的汇总分析或存储操作
        // ...
    }

    // 日志处理器的实现
    static class LogProcessor implements Runnable {
        private final CyclicBarrier barrier;

        public LogProcessor(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                // 模拟日志收集和处理
                System.out.println(Thread.currentThread().getName() + " 正在收集日志");
                Thread.sleep((long) (Math.random() * 3000)); // 模拟处理时间

                // 到达屏障，等待其他服务器完成处理
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " 完成日志收集");

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
