//package com.zm.config.hystrix;
//
//import com.netflix.config.ConfigurationManager;
//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandMetrics.HealthCounts;
//import com.netflix.hystrix.HystrixCommandProperties;
//
//public class CloseTest {
//
//	public static void main(String[] args) throws Exception {
//        // 10秒内有3个请求就满足第一个开启断路器的条件
//        ConfigurationManager.getConfigInstance().setProperty(
//                "hystrix.command.default.metrics.rollingStats.timeInMilliseconds", 10000);
//        ConfigurationManager.getConfigInstance().setProperty(
//                "hystrix.command.default.circuitBreaker.requestVolumeThreshold", 3);
//        // 请求的失败率，默认值为50%
//        ConfigurationManager.getConfigInstance().setProperty(
//                "hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50);
//        // 设置休眠期，断路器打开后，这段时间不会再执行命令，默认值为5秒，此处设置为3秒
//        ConfigurationManager.getConfigInstance().setProperty(
//                "hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 3000);
//        // 该值决定是否执行超时
//        boolean isTimeout = true;        
//        for(int i = 0; i < 10; i++) {
//            // 执行的命令全部都会超时
//            MyCommand c = new MyCommand(isTimeout);
//            c.execute();        
//            // 输出健康状态等信息
//            HealthCounts hc = c.getMetrics().getHealthCounts();
//            System.out.println("断路器状态：" + c.isCircuitBreakerOpen() + 
//                    ", 请求总数：" + hc.getTotalRequests());
//            if(c.isCircuitBreakerOpen()) {
//                // 断路器打开，让下一次循环成功执行命令
//                isTimeout = false;
//                System.out.println("=====  断路器打开了，等待休眠期结束   =====");
//                // 休眠期会在3秒后结束，此处等待4秒，确保休眠期结束
//                Thread.sleep(4000);
//            }    
//        }
//    }
//
//    /**
//     * 模拟超时的命令
//     * @author 杨恩雄
//     *
//     */
//    static class MyCommand extends HystrixCommand<String> {
//        
//        private boolean isTimeout;
//        
//        // 设置超时的时间为500毫秒
//        public MyCommand(boolean isTimeout) {
//            super(
//                    Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
//                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//                            .withExecutionTimeoutInMilliseconds(500))
//                 );
//            this.isTimeout = isTimeout;
//        }
//
//        protected String run() throws Exception {
//            // 让外部决定是否超时
//            if(isTimeout) {
//                // 模拟处理超时
//                Thread.sleep(800);
//            } else {
//                Thread.sleep(200);
//            }
//            return "";
//        }
//
//        @Override
//        protected String getFallback() {
//            return "";
//        }
//    }
//}
