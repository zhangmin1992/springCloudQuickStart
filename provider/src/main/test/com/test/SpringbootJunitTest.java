package com.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zm.provider.ProviderApplication;

/**
 * 测试启动类
 * @RunWith 引入spring对junit4的支持
 * @SpringBootTest 指定自己编写的启动类
 * @author yp-tc-m-7129
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProviderApplication.class)
public class SpringbootJunitTest {
}
