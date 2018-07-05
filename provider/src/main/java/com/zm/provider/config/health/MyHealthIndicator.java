//package com.zm.provider.config.health;
//
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.stereotype.Component;
//import org.springframework.boot.actuate.health.Status;
//
///**
// * 健康检查-模拟数据库或者redis mq等不可用的情况
// * 超过10次心跳服务就挂掉，挂掉后的80次心跳就恢复，但是eureka并不知道，还需要状态eureka，需要类MyHealthHealthCheckHandler
// * @author yp-tc-m-7129
// *
// */
//@Component
//public class MyHealthIndicator implements HealthIndicator {
//
//	/**
//	 * 失效心跳数
//	 */
//	private static Integer errorCount = 50;
//	
//	/**
//	 * 健康心跳数
//	 */
//	private static Integer count = 10;
//	
//	/**
//	 * 不可用的心跳次数计数
//	 */
//	private int healthIndicatorErrorCount;
//
//	/**
//	 * 心跳计数器
//	 */
//	private int healthIndicatorCount;
//	
//	/**
//	 * 服务是否可用，false表示可用
//	 */
//	private boolean hasError = false;
//
//	@Override
//	public Health health() {
//		if(!hasError){
//			healthIndicatorCount++;
//			//每检测5次，就返回DOWN
//			if(healthIndicatorCount % count ==0){
//				hasError = true;
//			}
//		}else{
//			//DOWN计数10次就UP
//			healthIndicatorErrorCount++;
//			if(healthIndicatorErrorCount > errorCount){
//				hasError=false;
//				healthIndicatorErrorCount=0;
//			}
//		}
//
//		if(hasError){
//			return new Health.Builder(Status.DOWN).build();
//		}
//		return new Health.Builder(Status.UP).build();
//	}
//}
