/**
 * 
 */
package com.sail.cot.mail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Title: 工艺品管理系统</p>
 * <p>Description: 由spring管理的线程池类，返回的ExecutorService就是给我们来执行线程的  
 *                 如果不交给spring管理也是可以的</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Oct 16, 2008 5:49:45 PM </p>
 * <p>Class Name: EasyMailExecutorPool.java </p>
 * @author achui
 *
 */

public class EasyMailExecutorPool implements InitializingBean {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	//线程池大小，spring配置文件中配置   
    private int poolSize;   
    private ExecutorService service;   
  
    public ExecutorService getService() {   
        return service;   
    }   
  
    public int getPoolSize() {   
        return poolSize;   
    }   
  
    public void setPoolSize(int poolSize) {   
        this.poolSize = poolSize;   
    }   

    /**  
     * 在 bean 被初始化成功之后初始化线程池大小  
     */ 
    
	public void afterPropertiesSet() throws Exception {
		System.out.println("---after");
		service = Executors.newFixedThreadPool(poolSize);   
		
	}

}
