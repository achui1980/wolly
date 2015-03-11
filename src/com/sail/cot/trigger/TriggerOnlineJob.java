/**
 * 
 */
package com.sail.cot.trigger;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sail.cot.listener.LoginOutListener;
import com.sail.cot.service.system.CotLoginInfoService;
import com.sail.cot.util.ContextUtil;

/**
 * <p>Title: 旗航ERP管理系统（QHERP）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 25, 2010 11:24:22 AM </p>
 * <p>Class Name: TriggerOnlineJob.java </p>
 * @author achui
 *
 */
public class TriggerOnlineJob implements Job {

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	private CotLoginInfoService cotLoginInfoService;

	public CotLoginInfoService getCotLoginInfoService() {
		if (cotLoginInfoService == null) {
			cotLoginInfoService = (CotLoginInfoService) ContextUtil.getBean("CotLoginInfoService");
		}
		return cotLoginInfoService;
	}
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		JobDetail jobDetail = jobCtx.getJobDetail(); 
		JobDataMap dataMap = jobDetail.getJobDataMap();
		ServletContext application =  (ServletContext)dataMap.get("application");
		ConcurrentHashMap<String, Long> onlineMap = (ConcurrentHashMap<String, Long>)application.getAttribute("onLineMap");
		if(onlineMap != null){
			System.out.println("登录线程触发... ...");
			Iterator<String> iterator = onlineMap.keySet().iterator();
			while(iterator.hasNext()){
				String sessionId = iterator.next();
				Long lastLoginTime = onlineMap.get(sessionId);
				Long currTime = System.currentTimeMillis();
				long diff = currTime - lastLoginTime;
				//时间戳超过1分钟，表示客户端已经有一分钟没有响应，
				//客户端可能已经退出，或者发生异常，从数据库中删除记录
				//并清除该map的登录记录
				if(diff >= 1000*60){
					//删除数据库的登录记录
					System.out.println("Longin exception paser... ...");
					this.getCotLoginInfoService().deleteLoginInfos(sessionId);
					//删除Map中的登录记录
					onlineMap.remove(sessionId);
					LoginOutListener.getSessionMap().remove(sessionId);
				}
			}
		}
		
	}

}
