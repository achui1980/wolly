/**
 * 
 */
package com.sail.cot.mail.sysservice;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sail.cot.domain.vo.MailSysServiceVo;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Jul 6, 2010 4:54:41 PM </p>
 * <p>Class Name: MailRecvScheduleTest.java </p>
 * @author achui
 *
 */
public class MailRecvScheduleTest {

	/**
	 * 描述：
	 * @throws java.lang.Exception
	 * 返回值：void
	 */
	MailRecvSchedule schedule = null;
	@Before
	public void setUp() throws Exception {
		 schedule = new MailRecvSchedule();
		 if(schedule == null){
			 schedule = new MailRecvSchedule();
			 schedule.startSchedule();
		 }
		 
	}

	/**
	 * 描述：
	 * @throws java.lang.Exception
	 * 返回值：void
	 */
	@After
	public void tearDown() throws Exception {
		//schedule.stopAll();
	}

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#stratSchedule()}.
	 */

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#restartSchedule()}.
	 */
	@Test
	public void testRestartSchedule() {
		//schedule.stratSchedule();
		//fail("Not yet implemented");
		schedule.startSchedule();
	}

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#pauseOrresumeJobByName(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPauseOrresumeJobByName() {
		//fail("Not yet implemented");
		schedule.pauseOrresumeJobByName("1", "P");
		schedule.pauseOrresumeJobByName("1", "R");
	}

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#getExecuteJobList()}.
	 */
	@Test
	public void testGetExecuteJobList() {
		List list = schedule.getExecuteJobList();
		for(int i=0; i<list.size();i++){
			MailSysServiceVo vo = (MailSysServiceVo)list.get(i);
			System.out.println(vo.toString());
		}
	}

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#getAllJobs()}.
	 */
	@Test
	public void testGetAllJobs() {
		//fail("Not yet implemented");
		List list = schedule.getAllJobs();
		for(int i=0; i<list.size();i++){
			MailSysServiceVo vo = (MailSysServiceVo)list.get(i);
			System.out.println(vo.toString());
		}
	}

	/**
	 * Test method for {@link com.sail.cot.mail.sysservice.MailRecvSchedule#createScheduler()}.
	 */
	@Test
	public void testCreateScheduler() {
		//fail("Not yet implemented");
	}

}
