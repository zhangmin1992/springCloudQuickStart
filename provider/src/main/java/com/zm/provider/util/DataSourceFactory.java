package com.zm.provider.util;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载配置文件的工厂
 * @author yp-tc-m-7129
 *
 */
public class DataSourceFactory {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);
	
	/**
	 * 单例模式
	 */
	private static DataSourceFactory instance;
	
	private DataSourceFactory() {}
	
	public static synchronized DataSourceFactory getInstance() {
		if (instance == null) {
            instance = new DataSourceFactory();
        }
        return instance;
	}
	
	private static Map<String, DataSource> datasources = new HashMap();
	
	private static String SYSPROP_DBCONFIGPATH = "dbconfigpath";
	
	
	
	public DataSource buildDataSource(String name) throws Exception {
        DataSource ds = datasources.get(name);
        if(ds == null){
        	logger.info("not find datasource ,ready to create datasource : " + name);
            ds = creatDataSource(name);
            datasources.put(name, ds);
        }else{
            logger.info("load datasource : " + name);
        }
        return ds;
    }
	
	 private DataSource creatDataSource(String name){
        logger.info("create datasource : "+name);
        String dbConfigPath = System.getProperty(SYSPROP_DBCONFIGPATH);
        logger.info("create datasource dbConfigPath= "+dbConfigPath);
        InputStream is = null;

        //如果没有设置dbConfigPath，则从classpath中加载配置文件
        if(dbConfigPath==null){
            logger.info("no dbConfigPath found in system properties, load dbconf from classpath");
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbconf/"+name+".properties");
        }
        //否则从指定的dbConfigPath中加载配置文件
        else{
            if(!dbConfigPath.endsWith("/")){
                dbConfigPath += "/";
            }
            logger.info("load dbconf from : " + dbConfigPath+name+".properties");
            try {
                is = new FileInputStream(new File(dbConfigPath+name+".properties"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("load dbconf file fail : "+e.getMessage(), e);
            }
        }

        try {
            Properties prop = new Properties();
            prop.load(is);
            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
