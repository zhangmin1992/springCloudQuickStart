//package com.zm.spring.cloud.consumer.config;
//
//import java.util.List;
//
//import com.netflix.loadbalancer.ILoadBalancer;
//import com.netflix.loadbalancer.IRule;
//import com.netflix.loadbalancer.Server;
//
///**
// * 自定义规则返回服务器的第一个，
// * 但是lb.getAllServers();的返回值有时候是8082第一个有时候是8083第一个
// * @author yp-tc-m-7129
// *
// */
//public class MyRule implements IRule {
//
//	private ILoadBalancer lb;
//
//	public void setLoadBalancer(ILoadBalancer lb) {
//		this.lb = lb;
//	}
//
//	public ILoadBalancer getLoadBalancer() {
//		return this.lb;
//	}
//	
//	public Server choose(Object key) {
//		List<Server> servers = lb.getAllServers();
//		System.out.println("这是自定义服务器定规则类，输出服务器信息：");
//		for(Server s : servers) {
//			System.out.println("        " + s.getHostPort());
//		}
//		return servers.get(0);
//	}
//}
