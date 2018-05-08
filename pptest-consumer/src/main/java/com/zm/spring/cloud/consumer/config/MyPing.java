//package com.zm.spring.cloud.consumer.config;
//
//import com.netflix.loadbalancer.IPing;
//import com.netflix.loadbalancer.Server;
//
///**
// * 只是输出ping信息，输出根据规则ping到了哪台服务器
// * @author yp-tc-m-7129
// *
// */
//public class MyPing implements IPing {
//
//	public boolean isAlive(Server server) {
//		System.out.println("自定义Ping类，服务器信息：" + server.getHostPort());
//		return true;
//	}
//}
//
