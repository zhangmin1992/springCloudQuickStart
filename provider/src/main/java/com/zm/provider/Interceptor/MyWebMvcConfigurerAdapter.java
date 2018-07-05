//package com.zm.provider.Interceptor;
//
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * 在启动类入口或者其兄弟口添加这个被spring容器管理的类
// * @author yp-tc-m-7129
// *
// */
//@SpringBootConfiguration
//public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter{
//
//	/**
//     * 注册 拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new MyHandlerInterceptor()).addPathPatterns("/**");
//    }
//}
