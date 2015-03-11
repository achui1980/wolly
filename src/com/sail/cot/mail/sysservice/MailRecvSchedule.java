/**
 * 
 */
package com.sail.cot.mail.sysservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.vo.MailSysServiceVo;
import com.sail.cot.service.system.CotEmpsService;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 6, 2010 11:31:56 AM </p>
 * <p>Class Name: MailRecvSchedule.java </p>
 * @author achui
 *
 */
public class MailRecvSchedule {
	Logger logger = Log4WebUtil.getLogger(MailRecvSchedule.class);
	static String MAIL_RECV_GROUP = "MAIL_RECV_GROUP";
	static String MAIL_TRIGGER_GROUP = "MAIL_TRIGGER_GROUP";
	CotEmpsService empsService ;
	
	public CotEmpsService getEmpsService() {
		if(empsService == null){
			empsService = (CotEmpsService)SystemUtil.getService("CotEmpsService");
		}
		return empsService;
	}

	public void setEmpsService(CotEmpsService empsService) {
		this.empsService = empsService;
	}
	//获取邮件接收列表
	public List<CotEmps> getMailList(){
		List<CotEmps> empList = new ArrayList<CotEmps>();
//		for(int i=0; i<3;i++){
//			CotEmps emps = new CotEmps();
//			emps.setId(i+1);
//			empList.add(emps);
//		}
//		return empList;
		return this.getEmpsService().getMailEmpsList();
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
			List<CotEmps> empList = this.getMailList();
			List<List> jobAndtriList = this.createJobsAndTris(empList);
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
			String[] jobNames = scheduler.getJobNames(MAIL_RECV_GROUP);
	//		scheduler.pauseJobGroup(MAIL_RECV_GROUP);
			//移除相应的任务
			for (String jobName : jobNames) {
				scheduler.deleteJob(jobName, MAIL_RECV_GROUP);
			}
			//重建任务
			List<CotEmps> empList = this.getMailList();
			List<List> jobAndtriList = this.createJobsAndTris(empList);
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
	 * @param jobName 任务名称，用员工的ID识别
	 * @param flag 判断启动或者暂停的标识 P(ause):暂停，R(esume)：启动
	 * @return
	 * 返回值：boolean
	 */
	public boolean pauseOrresumeJobByName(String jobName,String flag){
		try {
			Scheduler scheduler = this.createScheduler();
			if("P".equals(flag))
				scheduler.pauseJob(jobName, MAIL_RECV_GROUP);
			else if("R".equals(flag))
				scheduler.resumeJob(jobName, MAIL_RECV_GROUP);
		} catch (SchedulerException e) {
			logger.error("暂停计划任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * 描述： 获取当前正在执行的任务列表
	 * @return
	 * 返回值：List
	 */
	public List getExecuteJobList(){
		List res = new ArrayList();
		try {
			Scheduler scheduler = this.createScheduler();
			List<JobExecutionContext> list = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext jobCxt : list) {
				MailSysServiceVo vo = new MailSysServiceVo();
				if(jobCxt.getFireTime() != null)
					vo.setFireTime(new Timestamp(jobCxt.getFireTime().getTime()));
				vo.setJobname(jobCxt.getJobDetail().getName());
				if(jobCxt.getNextFireTime() != null)
					vo.setNextFireTime(new Timestamp(jobCxt.getNextFireTime().getTime()));
				if(jobCxt.getPreviousFireTime() != null)
					vo.setPreviousFireTime(new Timestamp(jobCxt.getPreviousFireTime().getTime()));
				vo.setState(Trigger.STATE_NORMAL);
				res.add(vo);
			}
		} catch (SchedulerException e) {
			logger.error("获取执行中计划任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			logger.error("其他异常，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return res;
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
			String[] triggerNames = scheduler.getTriggerNames(MAIL_TRIGGER_GROUP);
			for (String triggerName : triggerNames) {
				Trigger trigger = scheduler.getTrigger(triggerName, MAIL_TRIGGER_GROUP);
				JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobName(), MAIL_RECV_GROUP);
				MailSysServiceVo vo = new MailSysServiceVo();
				if(trigger.getStartTime() != null)
					vo.setFireTime(new Timestamp(trigger.getStartTime().getTime()));
				vo.setJobname(jobDetail.getName());
				if(trigger.getNextFireTime() != null)
					vo.setNextFireTime(new Timestamp(trigger.getNextFireTime().getTime()));
				if(trigger.getPreviousFireTime() != null)
					vo.setPreviousFireTime(new Timestamp(trigger.getPreviousFireTime().getTime()));
				vo.setState(scheduler.getTriggerState(triggerName, MAIL_TRIGGER_GROUP));
				JobDataMap map = jobDetail.getJobDataMap();
				if(map != null){
					CotEmps emps = (CotEmps)map.get("emp");
					vo.setMail(emps.getEmpsMail());
					vo.setMailAccount(emps.getEmpsMailHost());
					vo.setEmpId(emps.getId());
					//vo.setMsg(map.get("msg").toString());
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
	 * 描述：根据员工的邮箱创建任务
	 * @param empList 获取的员工列表
	 * @return
	 * 返回值：任务和触发器的集合
	 */
	private List<List> createJobsAndTris(List<CotEmps> empList){
		List<List> list = new ArrayList<List>();
		List<JobDetail> jobList = new ArrayList<JobDetail>();
		List<Trigger> triggerList = new ArrayList<Trigger>();
		int i=5;
		for (CotEmps cotEmps : empList) {
			JobDetail jobDetail = new JobDetail(cotEmps.getId().toString(),MAIL_RECV_GROUP,MailReciveJob.class);
			//配置相关参数
			jobDetail.setJobDataMap(this.getMailCfgMap(cotEmps));
			int interval = cotEmps.getEmpsMintues() == null? 30:cotEmps.getEmpsMintues().intValue();
			//配置定时任务触发时间
			Trigger trigger = TriggerUtils.makeMinutelyTrigger(interval);
		//	Trigger trigger =TriggerUtils.makeSecondlyTrigger(interval);
			
			trigger.setName("emptrigger_"+cotEmps.getId());
			trigger.setGroup(MAIL_TRIGGER_GROUP);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, i);
			//每个人物启动有时间间隔，间隔5秒
			trigger.setStartTime(calendar.getTime());
			triggerList.add(trigger);
			jobList.add(jobDetail);
			i = i+5;
		}
		list.add(jobList);
		list.add(triggerList);
		return list;
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
			String[] jobNames = scheduler.getJobNames(MAIL_RECV_GROUP);
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
	 * 描述：重启指定任务
	 * @param jobName 需要重启的任务名
	 * @param empId 指定员工
	 * @return
	 * 返回值：
	 */
	public boolean restartJobByName(String jobName,Integer empId){
		try {
			Scheduler scheduler = this.createScheduler();
			//删除原有任务
			scheduler.deleteJob(jobName, MAIL_RECV_GROUP);
			CotEmps cotEmps = this.getEmpsService().getEmpsById(empId);
			//获取相关配置
			JobDetail jobDetail = new JobDetail(cotEmps.getId().toString(),MAIL_RECV_GROUP,MailReciveJob.class);
			jobDetail.setJobDataMap(this.getMailCfgMap(cotEmps));
			int interval = cotEmps.getEmpsMintues() == null? 30:cotEmps.getEmpsMintues().intValue();
			//配置定时任务触发时间
			Trigger trigger = TriggerUtils.makeMinutelyTrigger(interval);
			trigger.setName("emptrigger_"+cotEmps.getId());
			trigger.setGroup(MAIL_TRIGGER_GROUP);
			trigger.setStartTime(new Date());
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logger.error("重启单一任务任务失败，失败原因:"+e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
		return true;
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
	private JobDataMap getMailCfgMap(CotEmps emp){
		JobDataMap map = new JobDataMap();
		map.put("emp", emp);
		return map;
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
	public int getJobStateByEmpId(Integer empId){
		try {
			Scheduler scheduler = this.createScheduler();
			return scheduler.getTriggerState("emptrigger_"+empId, MAIL_TRIGGER_GROUP);
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static void main(String[] args) throws InterruptedException{
		MailRecvSchedule schedule = new MailRecvSchedule();
		schedule.startSchedule();
		while(true){
			List list = schedule.getAllJobs();
			for(int i=0; i<list.size();i++){
				MailSysServiceVo vo = (MailSysServiceVo)list.get(i);
				System.out.println(vo.toString());
			}
			Thread.sleep(1*1000);
		}
	}
}
