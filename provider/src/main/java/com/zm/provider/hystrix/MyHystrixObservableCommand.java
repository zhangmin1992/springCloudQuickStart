package com.zm.provider.hystrix;

import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.zm.provider.entity.Book;
import com.zm.provider.util.HttpClientUtils;


public class MyHystrixObservableCommand extends HystrixObservableCommand<Book>{

	private long[] bookids;
	
	public MyHystrixObservableCommand(long[] ids) {
		super(HystrixCommandGroupKey.Factory.asKey("MyHystrixObservableCommand"));
		this.bookids = ids;
	}

	@Override
	protected Observable<Book> construct() {
		return Observable.create(new Observable.OnSubscribe<Book>() {

			public void call(Subscriber<? super Book> observer) {
				try {
					for(long id : bookids ) {
						String url = "http://127.0.0.1:8081/getBookInfoById?id=" + id;
						String response = HttpClientUtils.sendGetRequest(url);
						Book book = JSONObject.parseObject(response, Book.class);  
					    observer.onNext(book);
					}
					observer.onCompleted();
				} catch (Exception e) {
					System.out.println(" have happend unkowned exception "+ e);
					observer.onError(e);  
				}
			}
			
		}).subscribeOn(Schedulers.io());
	}

}
