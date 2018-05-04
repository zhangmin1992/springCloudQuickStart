package com.privider.config;

import java.util.List;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

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
		System.out.println("这是自定义服务器定规则类，输出服务器信息：");
		for(Server s : servers) {
			System.out.println("        " + s.getHostPort());
		}
		return servers.get(0);
	}
}
