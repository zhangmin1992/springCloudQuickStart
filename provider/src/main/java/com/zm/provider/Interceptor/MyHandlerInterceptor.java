//package com.zm.provider.Interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//public class MyHandlerInterceptor implements HandlerInterceptor {
//
//	@Override
//	public void afterCompletion(HttpServletRequest arg0,
//			HttpServletResponse arg1, Object arg2, Exception arg3)
//			throws Exception {
//		System.out.println("afterCompletion被调用");
//		
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
//			Object arg2, ModelAndView arg3) throws Exception {
//		System.out.println("postHandle被调用");
//		
//	}
//
//	@Override
//	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
//			Object arg2) throws Exception {
//		System.out.println("preHandle被调用");
//		return true;
//	}
//
//}
