package com.zm.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class PermisFilter extends ZuulFilter {
    
	/**
	 * 过滤器的类型，表示在路由转发之前执行过滤
	 */
	@Override
    public String filterType() {
        return "pre";
    }
	
	/**
	 * 过滤执行顺序，0最先执行
	 */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前请求地址来决定要不要对该地址进行过滤
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤url的规则
     */
    @Override
    public Object run() {
    	//得到请求上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String login = request.getParameter("login");
        if (login == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.addZuulResponseHeader("content-type","text/html;charset=utf-8");
            ctx.setResponseBody("非法访问");
        }else {
        	//发生异常需要异常处理
        	//int i = 9 /0;
        	try {
        		int i = 9 /0;
			} catch (Exception e) {
				ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            ctx.set("error.exception", e);
				ctx.set("error.message", "拦截器异常请稍后重试");
				try {
					throw new ZuulException(e,402,e.getCause().getMessage());
				} catch (ZuulException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
        }
        return null;
    }
}
