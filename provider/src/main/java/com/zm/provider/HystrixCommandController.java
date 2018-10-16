package com.zm.provider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rx.Observable;
import rx.Observer;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import com.zm.provider.entity.Book;
import com.zm.provider.hystrix.MyHystrixCommand;
import com.zm.provider.hystrix.MyHystrixObservableCommand;

@RestController
@RequestMapping("/hystrix")
public class HystrixCommandController {

	private static final Logger logger = LoggerFactory.getLogger(HystrixCommandController.class);
	
	 /**
	  * 一次性获取好多条数据
	  * @return
	  */
	 @RequestMapping(value = "/getBookInfo", method = RequestMethod.GET)
	 public String getBookInfo() {
		 long[] ids = new long[]{11L,12L,13L};
		 HystrixObservableCommand<Book> getBookInfoCommand = new MyHystrixObservableCommand(ids);
		 Observable<Book> observable = getBookInfoCommand.observe();
		 observable.subscribe(new Observer<Book>() {
			@Override
			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("发生了未知异常"+ e);
			}

			@Override
			public void onNext(Book book) {
				 System.out.println("查询到的数据是" + JSONObject.toJSONString(book));
			}
		 });
		
		 return "ok";
	 }
	 
	 /**
	  * 单条同步获取数据
	  * @return
	  */
	 @RequestMapping(value = "/test", method = RequestMethod.GET)
	 public String test() {
		 HystrixCommand<String> comand = new MyHystrixCommand("ccc");
		 String result = comand.execute();
		 return result;
	 }
	 
	 /**
	  * 异步单条获取数据
	  * @return
	  */
	 @RequestMapping(value = "/testAsyc", method = RequestMethod.GET)
	 public String testAsyc() {
		Future<String> bookFuture = new MyHystrixCommand("qqq").queue();
		try {
			return bookFuture.get(1, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	 }
}
