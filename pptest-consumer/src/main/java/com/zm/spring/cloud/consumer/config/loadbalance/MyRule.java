package com.zm.spring.cloud.consumer.config.loadbalance;

import java.util.List;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

/**
 * 自定义规则返回服务器的第一个，
 * 但是lb.getAllServers();的返回值有时候是8082第一个,有时候是8083第一个
 * @author yp-tc-m-7129
 *
 */
public class MyRule implements IRule {

	private ILoadBalancer lb;

	public void setLoadBalancer(ILoadBalancer lb) {
		this.lb = lb;
	}

	public ILoadBalancer getLoadBalancer() {
		return this.lb;
	}
	
	public Server choose(Object key) {
		List<Server> servers = lb.getAllServers();
		System.out.println("这是自定义服务提供者负载均衡规则类，输出提供者信息：");
		for(Server s : servers) {
			System.out.println("        " + s.getHostPort());
			if(s.getPort() == 8082) {
				return s;
			}
		}
		return servers.get(0);
	}
}
