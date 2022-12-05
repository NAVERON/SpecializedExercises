package com.eron.algorithms.pid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 * 调用实现 传入参数绘制函数曲线 
 * @author wangy 
 * 一个不错的笔记 : https://slash-honeydew-c53.notion.site/PID-Control-21b8234467974e86a04f94209f56eda5 
 *
 */
public class PIDEquationPlot extends StackPane {

    private static final Logger log = LoggerFactory.getLogger(PIDEquationPlot.class);
    private String PLOT_TITTLE = "PID Controller";
    
    // 设置基本参数 
    private long time = 0;  // 表达当前的时间 
    private float dt = 0.1F;  // 时间间隔 
    
    private PIDController pidController = new PIDController();  // 包装内部实现 

    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart linechart = new LineChart(xAxis, yAxis);
    private XYChart.Series<Double, Double> chartProcess = new XYChart.Series<>();  // 被控制量数据 
    private XYChart.Series<Double, Double> chartErrors = new XYChart.Series<>();  // 误差统计 
    private XYChart.Series<Double, Double> chartControl = new XYChart.Series<>();  // 控制量 
    
    // reference data handler 
    private ObservableList<XYChart.Data<Double, Double>> processData = this.chartProcess.getData(); // FXCollections.observableList(new LinkedList<>());
    private ObservableList<XYChart.Data<Double, Double>> controlData = this.chartControl.getData();// FXCollections.observableList(new LinkedList<>());
    private ObservableList<XYChart.Data<Double, Double>> errorsData = this.chartErrors.getData();
    
    public PIDEquationPlot() {
        // initial line chart properties 
        
        this.initComponent();
    }
    public PIDEquationPlot(double kp, double ki, double kd, double target) {
        this.initPIDController(kp, ki, kd, target);
        this.initComponent();
    }
    public void initComponent() {
        this.setPrefSize(800, 600);
        this.linechart.setTitle(this.PLOT_TITTLE);
        this.linechart.setAnimated(false);
        
        this.chartControl.setName("ControlValue");
        this.chartProcess.setName("ProcessValue");
        this.chartErrors.setName("EachErrors");
        
        this.linechart.getData().add(this.chartControl);
        this.linechart.getData().add(this.chartProcess);
        this.linechart.getData().add(this.chartErrors);
        
        this.getChildren().add(this.linechart);
        
        this.update();
    }
    public void initPIDController(double kp, double ki, double kd, double target) {
        this.pidController = new PIDController(kp, ki, kd);
        this.pidController.setTarget(target);
    }
    
    public void setKpParam(double kp) {
        this.pidController.kp = kp;
        this.update();
    }
    public void setKiParam(double ki) {
        this.pidController.ki = ki;
        this.update();
    }
    public void setKdParam(double kd) {
        this.pidController.kd = kd;
        this.update();
    }
    public void setTarget(double target) {
        this.pidController.target = target;
        this.update();
    }
    
    // update plot
    public void update() {
        this.processData.clear();
        this.controlData.clear();
        this.errorsData.clear();
        
//        Random rd = new Random();
//        for(int i = 0; i < 50; i++) {
//            if(this.processData.size() > 50) {
//                this.processData.remove(0);
//            }
//            if(this.controlData.size() > 50) {
//                this.controlData.remove(0);
//            }
//            this.processData.add(new XYChart.Data(i, rd.nextDouble()));
//            this.controlData.add(new XYChart.Data(i, rd.nextDouble()));
//        }
        
        log.info("进入主更新 循环, 控制加速度 acc, 使距离s 尽快到达设定值 ");
        double s = 0, v = 0, acc = 0;
        
        s = 0; v = 0; acc = 0;
        int i = 0;  // 限制次数 防止某些情况造成无限循环 
        // 终止条件 --> 控制量为 0, 被控制量 到target 
        
        // 全量控制循环 
//        while( !(this.pidController.isControlActionStop() && this.pidController.isReachTarget() && Math.abs(v) < 0.5) && i < 300 ) {
//            // 根据当前状态 更新距离 速度 
//            
//            s += v + 1.0/2.0 * acc;
//            v += acc;
//            
//            acc = this.pidController.pidFullCompute(s);
//            
//            this.processData.add(new XYChart.Data(i, s));
//            this.controlData.add(new XYChart.Data(i, acc));
//            this.errorsData.add(new XYChart.Data(i, this.pidController.getCurError()));
//            i++;
//            
//            log.info("日志数据-->\ns = {}, v = {}, acc = {}, \ni = {}\nerror = {}", 
//                    s, v, acc, i, this.pidController.getCurError());
//            
//            log.info("pid 内部信息数据 --> {}", this.pidController);
//        }
//        this.pidController.clearProcessVariables();
        
        // 增量控制循环 
        s = 0; v = 0; acc = 0;
        i = 0;
        while( !(this.pidController.isControlActionStop() && this.pidController.isReachTarget() && Math.abs(v) < 0.5) && i < 300 ) {
            // 根据当前状态 更新距离 速度 
            
            s += v + 1.0/2.0 * acc;
            v += acc;
            
            acc += this.pidController.pidIncrementalCompute(s);
            
            this.processData.add(new XYChart.Data(i, s));
            this.controlData.add(new XYChart.Data(i, acc));
            this.errorsData.add(new XYChart.Data(i, this.pidController.getCurError()));
            i++;
            
            log.info("日志数据-->\ns = {}, v = {}, acc = {}, \ni = {}\nerror = {}", 
                    s, v, acc, i, this.pidController.getCurError());
            
            log.info("pid 内部信息数据 --> {}", this.pidController);
        }
        
        this.pidController.clearProcessVariables();
    }
    
    private class PIDController {
        // 内部实现 pid 算法细节 
        public double kp = 0, ki = 0, kd = 0;
        public double target = 0;
        private long controlMin = 0, controlMax = 0;  // 控制量和被控制量 数值范围限定 
        private long processMin = 0, processMax = 0;  // 取余 限制范围 
        
        private double curProcessVal = 0, curControlVal = 0;
        private double curError = 0, lastError = 0, integral = 0, derivative = 0;
        
        public PIDController() {
            this(0.8, 0.01, 1.3);
        }
        public PIDController(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
            
            this.setControlRange(-10, 10);
            this.setProcessRange(-200, 200);
        }
        public void setTarget(double target) {
            this.target = target;
        }
        public void resetParams(double kp, double ki, double kd, double target) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
            
            this.target = target;
        }
        public void setControlRange(long min, long max) {
            this.controlMax = max;
            this.controlMin = min;
        }
        public void setProcessRange(long min, long max) {  // 白控制量不需要设置范围 会根据实际的计算得出 
            this.processMax = max;
            this.processMin = min;
        }
        public void clearProcessVariables() {
            this.curError = 0;
            this.lastError = 0;
            this.preError = 0;
            this.integral = 0;
            this.derivative = 0;
            
            this.curProcessVal = 0;  // 控制量 被控制量 
            this.curControlVal = 0;
        }
        public double getCurProcessVal() {
            return this.curProcessVal;
        }
        public double getCurControlVal() {
            return this.curControlVal;
        }
        public double getCurError() {
            return this.curError;
        }
        public boolean isControlActionStop() {
            return Math.abs(this.curControlVal) < 0.2;
        }
        public boolean isReachTarget() {
            return Math.abs(this.curProcessVal - this.target) < 0.2;
        }
        
        // 全量 pid 控制 
        public double pidFullCompute(double measurement) {
            this.curProcessVal = measurement;
            this.curError = this.target - this.curProcessVal;
            
            this.integral += this.curError;
            this.derivative = this.curError - this.lastError;
            
            // compute next control value 
            this.curControlVal = this.kp * this.curError
                                + this.ki * this.integral
                                + this.kd * this.derivative;
            
            // update last error 
            this.lastError = this.curError;
            
            return this.curControlVal > this.controlMax ? this.controlMax 
                    : this.curControlVal < this.controlMin ? this.controlMin : this.curControlVal;
        }
        
        private double preError = 0;
        // 增量 pid 控制 
        public double pidIncrementalCompute(double measurement) {
            this.curProcessVal = measurement;
            this.curError = this.target - this.curProcessVal;
            
            this.integral = this.curError;
            this.derivative = this.curError - 2 * this.lastError + this.preError;
            this.curControlVal = this.kp * (this.curError - this.lastError)
                                + this.ki * this.integral 
                                + this.kd * this.derivative;
            
            // 更新error 
            this.preError = this.lastError;
            this.lastError = this.curError;
            
            return this.curControlVal > this.controlMax ? this.controlMax 
                    : this.curControlVal < this.controlMin ? this.controlMin : this.curControlVal;
        }
        
        @Override
        public String toString() {
            return "PIDController [kp=" + kp + ", ki=" + ki + ", kd=" + kd + ", target=" + target 
                    + ", curError=" + curError + ", lastError=" + lastError + "]";
        }
        
        
        
    }
    
    
}










