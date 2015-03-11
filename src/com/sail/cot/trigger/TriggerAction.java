/**
 * 
 */
package com.sail.cot.trigger;



import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 19, 2009 3:18:15 PM </p>
 * <p>Class Name: TriggerAction.java </p>
 * @author achui
 *
 */
public class TriggerAction {
	
	public void startJob(int dayOfWeek,int hour,int minute) throws SchedulerException{

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDetail job = new JobDetail("statistic",Scheduler.DEFAULT_GROUP,TriggerJob.class);
		//每周执行一次
		Trigger triggerAction = TriggerUtils.makeWeeklyTrigger(dayOfWeek, hour, minute);
		//Trigger triggerAction = TriggerUtils.makeMinutelyTrigger(1);
		triggerAction.setName("triggerStatistic");
		scheduler.scheduleJob(job, triggerAction);
		//延迟10秒执行
		scheduler.start();
	};

}
