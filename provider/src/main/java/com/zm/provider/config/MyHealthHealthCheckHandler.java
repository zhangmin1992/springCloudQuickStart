//package com.zm.provider.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.health.Status;
//import org.springframework.stereotype.Component;
//
//import com.netflix.appinfo.HealthCheckHandler;
//import com.netflix.appinfo.InstanceInfo.InstanceStatus;
//
///**
// * 更新eureka状态的健康检查器
// * @author yp-tc-m-7129
// *
// */
//@Component
//public class MyHealthHealthCheckHandler implements HealthCheckHandler {
//
//	@Autowired
//	private MyHealthIndicator myHealthIndicator;
//	
//	@Override
//	public InstanceStatus getStatus(InstanceStatus InstanceInfo) {
//		Status status = myHealthIndicator.health().getStatus();
//		if(status == status.UP) {
//			return InstanceInfo.UP;
//		}
//		return InstanceInfo.DOWN;
//	}
//
//}
