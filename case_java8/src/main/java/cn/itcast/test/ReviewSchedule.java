package cn.itcast.test;

/**
 * @author WeiHanQiang
 * @date 2024/08/17 14:44
 **/
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ReviewSchedule {

    public static void main(String[] args) {
        // 参数设置
        int days = 100;
        int[] reviewCounts = new int[days];

        // 计算每天的复习内容数量
        for (int i = 1; i <= days; i++) {
            if (i - 1 >= 0) {
                reviewCounts[i - 1]++;
            }
            if (i - 7 >= 0) {
                reviewCounts[i - 7]++;
            }
            if (i - 14 >= 0) {
                reviewCounts[i - 14]++;
            }
        }

        // 创建数据集
        XYSeries series = new XYSeries("Number of Reviews");
        for (int i = 0; i < days; i++) {
            series.add(i + 1, reviewCounts[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // 创建图表
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Number of Reviews per Day",
                "Day",
                "Number of Reviews",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // 创建和显示应用程序窗口
        JFrame frame = new JFrame("Review Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}


