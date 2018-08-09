package com.test;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.zm.provider.dao.TestInsertEntityDao;
import com.zm.provider.entity.TestInsertEntity;

/**
 * 集合我在以往项目中的逻辑错误
 * @author yp-tc-m-7129
 *
 */
public class TestMyErrror extends SpringbootJunitTest {

	@Autowired
	private TestInsertEntityDao testInsertEntityDao;
	
	@Test
	public void testLeLock() throws Exception {
		
		long result = testInsertEntityDao.updateInfo("1", "zm", "newname");
		if(result !=1) {
			System.out.println("没成功！");
			try {
				throw new Exception("乐观锁异常");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删不干净，每次分页页数都有变化，下面的才可以真正的分页删除
	 */
	@Test
	public void testBatchDelete() {
		int count = 22;
		int pagesize = 5;
		int totalNum =  (count-1/pagesize)+1;
		/*for (int pageNum = 1; pageNum <=totalNum; pageNum++) {
			List<TestInsertEntity> list = testInsertEntityDao.getBatchInfo(pageNum,pagesize);
			if(list!= null && list.size()>0) {
				testInsertEntityDao.batchDelete(list);
			}
		}*/
		/*for(int i=0;i<20;i++) {
			TestInsertEntity entity = new TestInsertEntity();
			entity.setId(UUID.randomUUID().toString());
			entity.setName("zm"+UUID.randomUUID().toString());
			testInsertEntityDao.insetTestInfo(entity);
		}*/
		
		List<TestInsertEntity> list = testInsertEntityDao.getBatchInfo(1,pagesize);
		while(list.size() > 0 ) {
			testInsertEntityDao.batchDelete(list);
			list = testInsertEntityDao.getBatchInfo(1,pagesize);
		}
	}
}

