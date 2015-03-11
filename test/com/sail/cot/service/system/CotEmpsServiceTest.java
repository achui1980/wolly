/**
 * 
 */
package com.sail.cot.service.system;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sail.cot.ConstantVal;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.service.system.CotEmpsService;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Apr 9, 2010 11:16:13 AM </p>
 * <p>Class Name: CotEmpsServiceTest.java </p>
 * @author achui
 *
 */
public class CotEmpsServiceTest {
	private CotEmpsService empsService;
	/**
	 * 描述：
	 * @throws java.lang.Exception
	 * 返回值：void
	 */
	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = ConstantVal.getSpringContext();
		empsService = (CotEmpsService)ctx.getBean("CotEmpsService");
	}

	/**
	 * 描述：
	 * @throws java.lang.Exception
	 * 返回值：void
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.sail.cot.service.system.CotEmpsService#getEmpsById(java.lang.Integer)}.
	 */
	@Test
	public void testGetEmpsById() {
		CotEmps emps = empsService.getEmpsById(22);
		assertNotNull(emps);
		//assertEquals("admin", emps.getEmpNameCn());
	}

}
