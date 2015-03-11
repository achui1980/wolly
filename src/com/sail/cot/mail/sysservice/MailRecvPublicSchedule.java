/**
 * 
 */
package com.sail.cot.mail.sysservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.sail.cot.domain.CotMailCfg;
import com.sail.cot.domain.vo.MailSysServiceVo;
import com.sail.cot.email.service.MailCfgService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 7, 2010 10:58:29 AM </p>
 * <p>Class Name: MailRecvPublicSchedule.java </p>
 * @author achui
 *
 */
public class MailRecvPublicSchedule {
	Logger logger = Log4WebUtil.getLogger(MailRecvSchedule.class);
	static String MAIL_RECV_PUBLIC_GROUP = "MAIL_RECV_PUBLIC_GROUP";
	static String MAIL_TRIGGER_PUBLIC_GROUP = "MAIL_TRIGGER_PUBLIC_GROUP";
	MailCfgService cfgService ;

	public MailCfgService getCfgService() {
		if(cfgService == null){
			cfgService = (MailCfgService)SystemUtil.getService("MailCfgService");
		}
		return cfgService;
	}
	/**
	 * 描述：获取定时任务配置对象
	 * @return
	 * @throws SchedulerException
	 * 返回值：Scheduler
	 */
	public Scheduler createScheduler() throws SchedulerException{
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		return scheduler;
	}
	/**
	 * 描述： 启动邮件接收服务；
	 * @return
	 * 返回值：boolean
	 */
	public boolean startSchedule(){
		try {
			Scheduler scheduler = this.createScheduler();
			scheduler.start();
			List<CotMailCfg> cfgList = this.getMailList();
			List<List> jobAndtriList = this.createJobsAndTris(cfgList);
			List<JobDetail> jobDetailList = jobAndtriList.get(0);
			List<Trigger> triggerList = jobAndtriList.get(1);
			for(int i=0; i<jobDetailList.size();i++){
				JobDetail jobDetail = jobDetailList.get(i);
				Trigger trigger = triggerList.get(i);
				scheduler.scheduleJob(jobDetail, trigger);
			}
		} catch (SchedulerException e) {
			logger.error("创建计划任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 描述：重启计划任务
	 * @return
	 * 返回值：boolean
	 */
	public boolean restartSchedule(){
		try{
			Scheduler scheduler = this.createScheduler();
			String[] jobNames = scheduler.getJobNames(MAIL_RECV_PUBLIC_GROUP);
			//scheduler.pauseJobGroup(MAIL_RECV_PUBLIC_GROUP);
			//移除相应的任务
			for (String jobName : jobNames) {
				scheduler.deleteJob(jobName, MAIL_RECV_PUBLIC_GROUP);
			}
			//重建任务
			List<CotMailCfg> cfgList = this.getMailList();
			List<List> jobAndtriList = this.createJobsAndTris(cfgList);
			List<JobDetail> jobDetailList = jobAndtriList.get(0);
			List<Trigger> triggerList = jobAndtriList.get(1);
			for(int i=0; i<jobDetailList.size();i++){
				JobDetail jobDetail = jobDetailList.get(i);
				Trigger trigger = triggerList.get(i);
				scheduler.scheduleJob(jobDetail, trigger);
			}
		}catch (SchedulerException e) {
			logger.error("重建创建计划任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 描述： 启动或停止某个任务
	 * @param jobName 任务名称，用配置表的ID识别
	 * @param flag 判断启动或者暂停的标识 P(ause):暂停，R(esume)：启动
	 * @return
	 * 返回值：boolean
	 */
	public boolean pauseOrresumeJobByName(String jobName,String flag){
		try {
			Scheduler scheduler = this.createScheduler();
			if("P".equals(flag))
				scheduler.pauseJob(jobName, MAIL_RECV_PUBLIC_GROUP);
			else if("R".equals(flag))
				scheduler.resumeJob(jobName, MAIL_RECV_PUBLIC_GROUP);
		} catch (SchedulerException e) {
			logger.error("暂停计划任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * 描述：获取所有计划任务
	 * @return
	 * 返回值：List
	 */
	public List getAllJobs(){
		List res = new ArrayList();
		try {
			Scheduler scheduler = this.createScheduler();
			String[] triggerNames = scheduler.getTriggerNames(MAIL_TRIGGER_PUBLIC_GROUP);
			for (String triggerName : triggerNames) {
				Trigger trigger = scheduler.getTrigger(triggerName, MAIL_TRIGGER_PUBLIC_GROUP);
				JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobName(), MAIL_RECV_PUBLIC_GROUP);
				MailSysServiceVo vo = new MailSysServiceVo();
				if(trigger.getStartTime() != null)
					vo.setFireTime(new Timestamp(trigger.getStartTime().getTime()));
				vo.setJobname(jobDetail.getName());
				if(trigger.getNextFireTime() != null)
					vo.setNextFireTime(new Timestamp(trigger.getNextFireTime().getTime()));
				if(trigger.getPreviousFireTime() != null)
					vo.setPreviousFireTime(new Timestamp(trigger.getPreviousFireTime().getTime()));
				vo.setState(scheduler.getTriggerState(triggerName, MAIL_TRIGGER_PUBLIC_GROUP));
				JobDataMap map = jobDetail.getJobDataMap();
				if(map != null){
					CotMailCfg cfg = (CotMailCfg)map.get("cfg");
					vo.setMail(cfg.getDefaultHost());
					vo.setMailAccount(cfg.getDefaultAccount());
				}
				res.add(vo);
			}
		} catch (SchedulerException e) {
			logger.error("获取计划列表任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return null;
		}catch (Exception e) {
			logger.error("其他异常，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return res;
	}
	
	/**
	 * 描述：根据员工的邮箱创建任务
	 * @param cfgList 公共邮箱配置列表
	 * @return
	 * 返回值：任务和触发器的集合
	 */
	private List<List> createJobsAndTris(List<CotMailCfg> cfgList){
		List<List> list = new ArrayList<List>();
		List<JobDetail> jobList = new ArrayList<JobDetail>();
		List<Trigger> triggerList = new ArrayList<Trigger>();
		for (CotMailCfg cfg : cfgList) {
			JobDetail jobDetail = new JobDetail("publicjob",MAIL_RECV_PUBLIC_GROUP,MailRecvPublicJob.class);
			//配置相关参数
			jobDetail.setJobDataMap(this.getMailCfgMap(cfg));
			int interval = cfg.getDefaultMintues() == null? 30:cfg.getDefaultMintues().intValue();
			//配置定时任务触发时间
			Trigger trigger = TriggerUtils.makeMinutelyTrigger(interval);
			trigger.setName("publictrigger");
			trigger.setGroup(MAIL_TRIGGER_PUBLIC_GROUP);
			trigger.setStartTime(new Date());
			triggerList.add(trigger);
			jobList.add(jobDetail);
		}
		list.add(jobList);
		list.add(triggerList);
		return list;
	}
	//获取邮件接收列表
	public List<CotMailCfg> getMailList(){
		List<CotMailCfg> cfgList = new ArrayList<CotMailCfg>();
		cfgList.add(this.getCfgService().getCfg());
		return cfgList;
	}
	/**
	 * 描述：暂停或继续或有任务
	 * @param flag 判断启动或者暂停的标识 P(ause):暂停，R(esume)：启动
	 * @return
	 * 返回值：
	 */
	public boolean pauseOrresumAllJobs(String flag) {
		try {
			Scheduler scheduler = this.createScheduler();
			String[] jobNames = scheduler.getJobNames(MAIL_RECV_PUBLIC_GROUP);
			for(String jobName : jobNames){
				this.pauseOrresumeJobByName(jobName, flag);
			}
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logger.error("暂停或继续列表任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 描述： 获取当期触发器运行的状态
	 * @param empId
	 * @return
	 * 返回值：int
	 * 
  		STATE_COMPLETE 2  完成
 		STATE_NORMAL 0    执行中
		STATE_PAUSED 1    暂停
	 */
	public int getPublicJobState(){
		try {
			Scheduler scheduler = this.createScheduler();
			return scheduler.getTriggerState("publictrigger", MAIL_TRIGGER_PUBLIC_GROUP);
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	private JobDataMap getMailCfgMap(CotMailCfg mailCfg){
		JobDataMap map = new JobDataMap();
		map.put("cfg", mailCfg);
		return map;
	}
	public void stopAll() {
		try {
			Scheduler scheduler = this.createScheduler();
			scheduler.shutdown();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
	}
}
