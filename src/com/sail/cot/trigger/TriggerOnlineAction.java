/**
 * 
 */
package com.sail.cot.trigger;

import javax.servlet.ServletContext;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>Title: 旗航ERP管理系统（QHERP）</p>
 * <p>Description:对在线人数进行扫描，如果超过规定时间，就表明可能异常退出或者退出函数没有触发，通过该线程进行删除</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 25, 2010 11:23:58 AM </p>
 * <p>Class Name: TriggerOnlineAction.java </p>
 * @author achui
 *
 */
public class TriggerOnlineAction {
	public void startJob(int second,ServletContext application) throws SchedulerException{

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDetail job = new JobDetail("onlineEmp",Scheduler.DEFAULT_GROUP,TriggerOnlineJob.class);
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("application", application);
		job.setJobDataMap(dataMap);
		//每个一分钟执行一次
		Trigger triggerAction = TriggerUtils.makeSecondlyTrigger(second);
		//Trigger triggerAction = TriggerUtils.makeMinutelyTrigger(1);
		triggerAction.setName("triggerOnlineEmp");
		scheduler.scheduleJob(job, triggerAction);
		//延迟20秒执行
		scheduler.startDelayed(20);
	};
}
