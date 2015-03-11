/**
 * 
 */
package com.sail.cot.seq;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustSeq;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotFacSeq;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotSeq;
import com.sail.cot.domain.CotSeqCfg;
import com.sail.cot.util.SystemUtil;

/**
 * <p>
 * Title: 旗行办公自动化系统（OA）
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * <p>
 * Create Time: May 18, 2010 4:21:28 PM
 * </p>
 * <p>
 * Class Name: Sequece.java
 * </p>
 * 
 * @author achui
 * 
 */
public class Sequece {
	private CotBaseDao cotBaseDao;
	// 存放配置表的信息，其中key为配置表的key字段，value为这个配置表对象
	private Map<String, CotSeqCfg> cfgMap = new HashMap<String, CotSeqCfg>();
	// 存放配置表中自定义对象的内容
	private Map<String, Object> objMap = new HashMap<String, Object>();

	public CotBaseDao getCotBaseDao() {
		if (cotBaseDao == null)
			cotBaseDao = (CotBaseDao) SystemUtil.getService("CotBaseDao");
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public Sequece() {

	}

	/**
	 * @param reloadMap
	 */
	public Sequece(boolean reloadMap) {
		List<CotSeqCfg> list = new ArrayList<CotSeqCfg>();
		if (cfgMap.isEmpty() || reloadMap) {
			list = (List<CotSeqCfg>) this.getCotBaseDao().getRecords(
					"CotSeqCfg");
			for (CotSeqCfg cfg : list) {
				cfgMap.put(cfg.getKey(), cfg);
				// 生成用户自定义对象缓存
				if (cfg.getType().equals("CustObj")) {
					objMap.put(cfg.getObj(), null);
				}
			}
		}
	}

	/**
	 * 描述： 初始化预置对象，通过获取配置表的信息将所需的配置项出入缓存
	 * 
	 * @param reloadMap
	 *            是否需要重新加载 返回值：void
	 */
	public void initSequece(boolean reloadMap) {
		List<CotSeqCfg> list = new ArrayList<CotSeqCfg>();
		if (cfgMap.isEmpty() || reloadMap) {
			list = (List<CotSeqCfg>) this.getCotBaseDao().getRecords(
					"CotSeqCfg");
			for (CotSeqCfg cfg : list) {
				cfgMap.put(cfg.getKey(), cfg);
				// 生成用户自定义对象缓存
				if (cfg.getType().equals("CustObj")) {
					objMap.put(cfg.getObj(), null);
				}
			}
		}
	}

	/**
	 * 描述：获取对应单据的单号
	 * 
	 * @param idMap
	 *            存放对象对应的ID，key值为对象名，如CotCustomer,CotOrder等，value为ID值
	 * @param type
	 *            单据类型，如order，orderfac等
	 * @param currdate
	 *            对应单据的下单时间
	 * @return 对应单据的单号 返回值：String
	 */
	private String getAllNo(Map idMap, String type, String currdate) {
		String strSql = " from CotSeq obj where obj.type='" + type + "'";
		List list = this.getCotBaseDao().find(strSql);
		String number = "";
		if (list.size() > 0) {
			CotSeq seq = (CotSeq) list.get(0);
			// 获取单号配置表达式
			String express = seq.getSeqCfg();
			// 分解单号表达式
			number = this
					.decodeExpress(express, idMap, seq.getType(), currdate);
		}
		return number;
	}

	/**
	 * 描述：初始化参数idMap中对应的对象，通过ID获取整个对象的值，放入缓存objMap中，共后面调用
	 * 
	 * @param idMap
	 *            存放对象对应的ID，key值为对象名，如CotCustomer,CotOrder等，value为ID值 返回值：void
	 */
	private void initObj(Map idMap) {
		Iterator iteator = idMap.keySet().iterator();
		while (iteator.hasNext()) {
			String idKey = (String) iteator.next();
			Integer id = (Integer) idMap.get(idKey);
			// idMap中的key要是CotSeqCfg表中obj字段所对应的值
			Object object = objMap.get(idKey);
			if (object == null) {
				try {
					Class clzz = Class.forName("com.sail.cot.domain." + idKey);
					// 实例化对象
					object = this.getCotBaseDao().getById(clzz, id);
					objMap.put(idKey, object);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 分解单号表达式
	/**
	 * 描述：对配置表的表达式进行分解，分为3部分分解 1、对之定义对象进行分解，通过objMap获取相应的值 2、分解时间相关的部分 3、分解序号部分
	 * 4、分解其他规制
	 * 
	 * @param express
	 *            配置表达式
	 * @param idMap
	 *            存放各种Id的Map，idMap中的key要是CotSeqCfg表中obj字段所对应的值
	 * @param type
	 *            用于判断是要分解那种类型的单号，取值目前有 price，order，spilit，orderfac等
	 * @param currdate
	 *            对应单据的下单时间
	 * @return 返回分解后的单据 返回值：String
	 */
	private String decodeExpress(String express, Map idMap, String type,
			String currdate) {
		// 初始化对象
		if (idMap != null) {
			this.initObj(idMap);
		}

		String returnVal = express;
		String[] arr = SystemUtil.stringsBetween(express, "[", "]");
		if (arr == null)
			return "";
		for (String key : arr) {
			System.out.println(key);
			CotSeqCfg cfg = cfgMap.get("[" + key + "]");
			System.out.println(cfg.getType());
			Object object = objMap.get(cfg.getObj());
			// 1、先完成自定义对象的分解，用直接替换的方式完成
			if (object != null && "CustObj".equals(cfg.getType())) {
				try {
					String val = BeanUtils.getProperty(object, cfg
							.getAttribute());
					returnVal = returnVal.replace("[" + key + "]", val);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 2、分解时间相关部分
			if ("Date".equals(cfg.getType())) {
				Calendar calendar = new java.util.GregorianCalendar();
				int i = Calendar.YEAR;
				// 获取参数类表
				String[] args = cfg.getArgs().split(",");
				// 获取参数类型
				String[] argsType = null;
				if (cfg.getArgsType() != null)
					argsType = cfg.getArgsType().split(",");
				// 判断是否需要对时间进行计算，通过Method属性是否为空进行判断
				if (cfg.getMethod() != null) {
					try {
						Object[] objects = this.getObjArgs(cfg);
						if (objects != null)
							MethodUtils.invokeMethod(calendar, cfg.getMethod(),
									objects);
						String invokeVal = String.format(cfg.getExpress(),
								calendar);
						returnVal = returnVal.replace("[" + key + "]",
								invokeVal);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					String invokeVal = String
							.format(cfg.getExpress(), calendar);
					returnVal = returnVal.replace("[" + key + "]", invokeVal);
				}
			}
			// 3、分解序列号部分
			else if ("SEQ".equals(cfg.getType())) {
				List resList = this.getNewSeqObj(type);
				if (resList != null) {
					Integer currseq = (Integer) resList.get(0);
					String val = String.format(cfg.getExpress(), currseq);
					returnVal = returnVal.replace("[" + key + "]", val);
				}
			}
			// 4、分解部分特殊序列号，有特殊的分解在这里处理
			// 含有客户序列号的，一定要有Customer对象
			else if ("KHSEQ".equals(cfg.getType())) {
				CotCustomer customer = (CotCustomer) objMap.get("CotCustomer");
				System.out.println(customer);
				System.out.println(currdate);
				if (customer != null && currdate != null
						&& !"".equals(currdate)) {
					Integer currseq = updateCustSeq(customer.getId(), express,
							type, currdate);
					String val = String.format(cfg.getExpress(), currseq);
					returnVal = returnVal.replace("[" + key + "]", val);
				}
			}
			// 5、分解部分特殊序列号，有特殊的分解在这里处理
			// 含有客户序列号的，一定要有Customer对象
			else if ("CHSEQ".equals(cfg.getType())) {
				CotFactory factory = (CotFactory) objMap.get("CotFactory");
				if (factory != null) {
					Integer currseq = updateFacSeq(factory.getId(), express,
							type, currdate);
					String val = String.format(cfg.getExpress(), currseq);
					returnVal = returnVal.replace("[" + key + "]", val);
				}
			}
		}
		return returnVal;
	}

	// 获取全局序列号
	/**
	 * 描述：
	 * 
	 * @param type
	 *            标识是需要获取那种类型的序列号，取值有order，orderfac，given，等
	 * @param currDate
	 *            当前时间
	 * @return 序列号 返回值：List，数据的第一个存放当前序列号，第2个对象存放CotSeq对象
	 */
	private List getNewSeqObj(String type) {
		List returnList = new ArrayList();
		String sql = " from CotSeq obj where obj.type = '" + type + "'";
		List list = this.getCotBaseDao().find(sql);
		if (list.size() == 0)
			return null;
		CotSeq seq = (CotSeq) list.get(0);
		String orderno = seq.getSeqCfg();
		// 当前序列号
		Integer currSeq = seq.getCurrentSeq();
		int zeroType = seq.getZeroType();
		// 判断是否同一天，如果不是同一天，则归0
		Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
		Calendar today = Calendar.getInstance(); // 获取当前时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 设置历史事件
		currcal.setTime(seq.getHisDay());

		// if(orderno.indexOf("2KHSEQ")>=0)
		// currSeq = currSeq+1;
		// if(orderno.indexOf("3KHSEQ")>=0)
		// currSeq = currSeq+1;
		// if(orderno.indexOf("4KHSEQ")>=0)
		// currSeq = currSeq+1;
		if (orderno.indexOf("2SEQ") >= 0)
			currSeq = currSeq + 1;
		else if (orderno.indexOf("3SEQ") >= 0)
			currSeq = currSeq + 1;
		else if (orderno.indexOf("4SEQ") >= 0)
			currSeq = currSeq + 1;
		else if (orderno.indexOf("5SEQ") >= 0)
			currSeq = currSeq + 1;
		if (orderno.indexOf("2SEQ") >= 0 && currSeq > 99)
			currSeq = 0;
		else if (orderno.indexOf("3SEQ") >= 0 && currSeq > 999)
			currSeq = 0;
		else if (orderno.indexOf("4SEQ") >= 0 && currSeq > 9999)
			currSeq = 0;
		else if (orderno.indexOf("5SEQ") >= 0 && currSeq > 99999)
			currSeq = 0;
		// if(orderno.indexOf("2KHSEQ") >= 0 && (currSeq + 1) > 99)
		// currSeq = 0;
		// if(orderno.indexOf("3KHSEQ") >= 0 && (currSeq + 1) > 999)
		// currSeq = 0;
		// if(orderno.indexOf("4KHSEQ") >= 0 && (currSeq + 1) > 9999)
		// currSeq = 0;
		switch (zeroType) {
		case 1: // 按年归档
		{
			if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
				currSeq = 1;
			break;
		}
		case 2: // 按月归档
		{
			if (today.get(Calendar.MONTH) != currcal.get(Calendar.MONTH))
				currSeq = 1;
			break;
		}
		case 3: // 按日归档
		// {
		// if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
		// currSeq = 1;
		// break;
		// }
		}
		seq.setHisDay(today.getTime());
		List arrayList = new ArrayList();
		arrayList.add(seq);
		this.getCotBaseDao().updateRecords(arrayList);// 保存历史时间
		returnList.add(currSeq);
		returnList.add(seq);
		return returnList;
	}

	// 保存完后，更新当前序列号的值
	/**
	 * 描述： 当单据保存完后，更新当前序列号
	 * 
	 * @param type
	 *            单据类型，如order,orderfac等 返回值：void
	 */
	public void saveSeq(String type) {
		List resList = this.getNewSeqObj(type);
		Calendar calendar = Calendar.getInstance();
		if (resList == null)
			return;
		Integer currSeq = (Integer) resList.get(0);
		CotSeq seq = (CotSeq) resList.get(1);
		seq.setHisDay(calendar.getTime());
		seq.setCurrentSeq(currSeq);
		List arrayList = new ArrayList();
		arrayList.add(seq);
		this.getCotBaseDao().updateRecords(arrayList);

	}

	// 保存完后，更新当前序列号的值
	/**
	 * 描述： 当单据保存完后，更新当前客户序列号
	 * 
	 * @param type
	 *            单据类型，如order,orderfac等 返回值：void
	 */
	public void saveCustSeq(Integer custId, String type, String currDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt = df.parse(currDate);
			currDate = df.format(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List resList = this.getNewSeqObj(type);
		if (resList == null)
			return;

		// Integer currSeq = (Integer)resList.get(0);
		// System.out.println(currSeq);

		Integer currSeq = null;

		CotSeq seq = (CotSeq) resList.get(1);
		// 获取单号配置信息
		String seqcfg = seq.getSeqCfg();
		// 存在客户序列号
		if (seqcfg.indexOf("KHSEQ") > -1) {

			CotCustSeq custSeq = null;
			try {
				String strSql = " from CotCustSeq obj where obj.custId="
						+ custId + " and obj.currDate='" + currDate + "'";

				System.out.println("strSql:" + strSql);
				List list = this.getCotBaseDao().find(strSql);
				if (list == null || list.size() == 0)
					custSeq = null;
				else
					custSeq = (CotCustSeq) list.get(0);
				String tempseq = BeanUtils.getProperty(custSeq, type + "Seq");
				currSeq = Integer.parseInt(tempseq);
			} catch (Exception ex) {
				custSeq = null;
			}
			if (custSeq == null)
				return;
			Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
			Calendar today = Calendar.getInstance(); // 获取当前时间
			currcal.setTime(custSeq.getCurrDate());
			String key = type + "Seq";
			System.out.println(seq.getZeroType());
			try {
				switch (seq.getZeroType()) {
				case 1: // 按年归档
				{

					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
						BeanUtils.setProperty(custSeq, key, 0);
					else
						BeanUtils.setProperty(custSeq, key, currSeq + 1);
					break;
				}
				case 2: // 按月归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR) || (today.get(Calendar.YEAR) == currcal.get(Calendar.YEAR) && today.get(Calendar.MONTH) != currcal
							.get(Calendar.MONTH)))
						BeanUtils.setProperty(custSeq, key, 0);
					else
						BeanUtils.setProperty(custSeq, key, currSeq + 1);
					break;
				}
				case 3: // 按日归档
					// {
					// if(today.get(Calendar.DATE) !=
					// currcal.get(Calendar.DATE))
					// BeanUtils.setProperty(custSeq, key, 0);
					// else
					// BeanUtils.setProperty(custSeq, key, currSeq + 1);
					// break;
					// }
				default: // 默认系统
					BeanUtils.setProperty(custSeq, key, currSeq + 1);
				}
				if (seqcfg.indexOf("2KHSEQ") >= 0 && (currSeq + 1) > 99)
					BeanUtils.setProperty(custSeq, key, 0);
				if (seqcfg.indexOf("3KHSEQ") >= 0 && (currSeq + 1) > 999)
					BeanUtils.setProperty(custSeq, key, 0);
				if (seqcfg.indexOf("4KHSEQ") >= 0 && (currSeq + 1) > 9999)
					BeanUtils.setProperty(custSeq, key, 0);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			List res = new ArrayList();
			res.add(custSeq);
			this.getCotBaseDao().updateRecords(res);
			System.out
					.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++seq");
		}

	}

	/**
	 * 描述： 当单据保存完后，更新当前厂家序列号
	 * 
	 * @param type
	 *            单据类型，ele 返回值：void
	 */
	public void saveFacSeq(Integer facId, String type) {
		List resList = this.getNewSeqObj(type);
		if (resList == null)
			return;

		Integer currSeq = null;

		CotSeq seq = (CotSeq) resList.get(1);
		// 获取单号配置信息
		String seqcfg = seq.getSeqCfg();
		// 存在客户序列号
		if (seqcfg.indexOf("CHSEQ") > -1) {
			CotFacSeq facSeq = null;
			try {
				String strSql = " from CotFacSeq obj where obj.facId=" + facId;
				List list = this.getCotBaseDao().find(strSql);
				if (list == null || list.size() == 0)
					facSeq = null;
				else
					facSeq = (CotFacSeq) list.get(0);
				String tempseq = BeanUtils.getProperty(facSeq, type + "Seq");
				currSeq = Integer.parseInt(tempseq);
			} catch (Exception ex) {
				ex.printStackTrace();
				facSeq = null;
			}
			if (facSeq == null)
				return;
			Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
			Calendar today = Calendar.getInstance(); // 获取当前时间
			currcal.setTime(facSeq.getCurrDate());
			String key = type + "Seq";
			try {
				switch (seq.getZeroType()) {
				case 1: // 按年归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
						BeanUtils.setProperty(facSeq, key, 1);
					else
						BeanUtils.setProperty(facSeq, key, currSeq + 1);
					break;
				}
				case 2: // 按月归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR) || (today.get(Calendar.YEAR) == currcal.get(Calendar.YEAR) && today.get(Calendar.MONTH) != currcal
							.get(Calendar.MONTH)))
						BeanUtils.setProperty(facSeq, key, 1);
					else
						BeanUtils.setProperty(facSeq, key, currSeq + 1);
					break;
				}
				case 3:
				default: // 默认系统
					BeanUtils.setProperty(facSeq, key, currSeq + 1);
				}
				if (seqcfg.indexOf("2CHSEQ") >= 0 && (currSeq + 1) > 99)
					BeanUtils.setProperty(facSeq, key, 0);
				if (seqcfg.indexOf("3CHSEQ") >= 0 && (currSeq + 1) > 999)
					BeanUtils.setProperty(facSeq, key, 0);
				if (seqcfg.indexOf("4CHSEQ") >= 0 && (currSeq + 1) > 9999)
					BeanUtils.setProperty(facSeq, key, 0);
				if (seqcfg.indexOf("5CHSEQ") >= 0 && (currSeq + 1) > 99999)
					BeanUtils.setProperty(facSeq, key, 0);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			List res = new ArrayList();
			facSeq.setCurrDate(today.getTime());
			res.add(facSeq);
			this.getCotBaseDao().updateRecords(res);
		}
	}

	/**
	 * 描述： 更新客户序列号表，返回当前客户序列号
	 * 
	 * @param custId
	 *            客户ID
	 * @param abstractNo
	 *            单号
	 * @param type
	 *            单据类型，order，orderfac等
	 * @param currDate
	 *            对应单据的下单时间
	 * @return 返回值：Integer
	 */
	private Integer updateCustSeq(Integer custId, String abstractNo,
			String type, String currDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt = df.parse(currDate);
			currDate = df.format(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String strSql = " from  CotCustSeq obj where obj.custId=" + custId
				+ " and obj.currDate='" + currDate + "'";
		System.out.println(strSql);
		List seq = this.getCotBaseDao().find(strSql);
		CotCustSeq custSeq = null;
		if (seq == null || seq.size() == 0)
			custSeq = null;
		else
			custSeq = (CotCustSeq) seq.get(0);
		Integer currentCustSeq = 0;
		if (custSeq == null) // 不存在记录是，添加一条记录
		{
			custSeq = new CotCustSeq();
			custSeq.setCustId(custId);
			custSeq.setGivenSeq(0);
			custSeq.setOrderfacSeq(0);
			custSeq.setOrderoutSeq(0);
			custSeq.setOrderSeq(0);
			custSeq.setPriceSeq(0);
			custSeq.setSignSeq(0);
			custSeq.setSplitSeq(0);
			custSeq.setFinacegivenSeq(0);
			custSeq.setFinacerecvSeq(0);
			custSeq.setFincaeaccountdealSeq(0);
			custSeq.setFincaeaccountrecvSeq(0);
			custSeq.setBacktaxSeq(0);
			custSeq.setAuditSeq(0);
			custSeq.setPackingSeq(0);
			custSeq.setAccessSeq(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				custSeq.setCurrDate(new java.sql.Date(sdf.parse(currDate)
						.getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List res = new ArrayList();
			res.add(custSeq);
			try {
				this.getCotBaseDao().saveRecords(res);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (custSeq != null) // 数据库中存在记录
		{
			// 判断是否同一天，如果不是同一天，则归0
			custSeq = this.updateCustSeqToZero(custSeq, type);
			try {
				String currSeq = BeanUtils.getProperty(custSeq, type + "Seq");
				if (currSeq != null)
					currentCustSeq = new Integer(currSeq);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (currentCustSeq == null) {
			currentCustSeq = 0;
		}
		currentCustSeq = currentCustSeq + 1;
		if (abstractNo.indexOf("2KHSEQ") >= 0 && currentCustSeq > 99) {
			currentCustSeq = 1;
		}
		if (abstractNo.indexOf("3KHSEQ") >= 0 && currentCustSeq > 999) {
			currentCustSeq = 1;
		}
		if (abstractNo.indexOf("4KHSEQ") >= 0 && currentCustSeq > 9999) {
			currentCustSeq = 1;
		}
		return currentCustSeq;
		// return null;
	}

	/**
	 * 描述： 更新厂家序列号表，返回当前客户序列号
	 * 
	 * @param custId
	 *            客户ID
	 * @param abstractNo
	 *            单号
	 * @param type
	 *            单据类型，order，orderfac等
	 * @param currDate
	 *            对应单据的下单时间
	 * @return 返回值：Integer
	 */
	private Integer updateFacSeq(Integer facId, String abstractNo, String type,
			String currDate) {
		String strSql = " from  CotFacSeq obj where obj.facId=" + facId;
		List seq = this.getCotBaseDao().find(strSql);
		CotFacSeq facSeq = null;
		if (seq == null || seq.size() == 0)
			facSeq = null;
		else
			facSeq = (CotFacSeq) seq.get(0);
		if (facSeq == null) // 不存在记录是，添加一条记录
		{
			facSeq = new CotFacSeq();
			facSeq.setFacId(facId);
			facSeq.setEleSeq(0);
			facSeq.setCurrDate(new Date());
			List res = new ArrayList();
			res.add(facSeq);
			try {
				this.getCotBaseDao().saveRecords(res);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		Integer currentCustSeq = 0;
		if (facSeq != null) // 数据库中存在记录
		{
			// 判断是否同一天，如果不是同一天，则归0
			facSeq = this.updateFacSeqToZero(facSeq, type);
			try {
				String currSeq = BeanUtils.getProperty(facSeq, type + "Seq");
				if (currSeq != null)
					currentCustSeq = new Integer(currSeq);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (currentCustSeq == null) {
			currentCustSeq = 0;
		}
		currentCustSeq = currentCustSeq + 1;
		if (abstractNo.indexOf("2CHSEQ") >= 0 && currentCustSeq > 99) {
			currentCustSeq = 1;
		}
		if (abstractNo.indexOf("3CHSEQ") >= 0 && currentCustSeq > 999) {
			currentCustSeq = 1;
		}
		if (abstractNo.indexOf("4CHSEQ") >= 0 && currentCustSeq > 9999) {
			currentCustSeq = 1;
		}
		if (abstractNo.indexOf("5CHSEQ") >= 0 && currentCustSeq > 99999) {
			currentCustSeq = 1;
		}
		return currentCustSeq;
		// return null;
	}

	/**
	 * 描述：客户序列号的归0处理
	 * 
	 * @param customerSeq
	 *            客户序列号对象
	 * @param type
	 *            单据类型
	 * @return 返回值：CotCustSeq
	 */
	private CotCustSeq updateCustSeqToZero(CotCustSeq customerSeq, String type) {

		Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
		Calendar today = Calendar.getInstance(); // 获取当前时间
		currcal.setTime(customerSeq.getCurrDate());
		List resList = this.getNewSeqObj(type);
		if (resList == null)
			return null;
		CotSeq seq = (CotSeq) resList.get(1);
		// 根据类型获取归0方式
		String typeSeq = type + "Seq";// 生成可以进行反射的熟悉
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put(typeSeq, seq.getZeroType());
		Iterator iterator = map.keySet().iterator();
		try {
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				int zeroType = Integer.parseInt(map.get(key).toString());
				switch (zeroType) {
				case 1: // 按年归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
						BeanUtils.setProperty(customerSeq, key, 0);
					break;
				}
				case 2: // 按月归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR) || (today.get(Calendar.YEAR) == currcal.get(Calendar.YEAR) && today.get(Calendar.MONTH) != currcal
							.get(Calendar.MONTH)))
						BeanUtils.setProperty(customerSeq, key, 0);
					break;
				}
				case 3: // 按日归档
				// {
				// if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
				// BeanUtils.setProperty(customerSeq, key, 0);
				// break;
				// }
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		customerSeq.setCurrDate(customerSeq.getCurrDate());
		List res = new ArrayList();
		res.add(customerSeq);
		this.getCotBaseDao().updateRecords(res);
		return customerSeq;
		// return null;
	}

	/**
	 * 描述：厂家序列号的归0处理
	 * 
	 * @param customerSeq
	 *            客户序列号对象
	 * @param type
	 *            单据类型
	 * @return 返回值：CotCustSeq
	 */
	private CotFacSeq updateFacSeqToZero(CotFacSeq facSeq, String type) {

		Calendar currcal = Calendar.getInstance(); // 获取历史比对时间
		Calendar today = Calendar.getInstance(); // 获取当前时间
		currcal.setTime(facSeq.getCurrDate());
		List resList = this.getNewSeqObj(type);
		if (resList == null)
			return null;
		CotSeq seq = (CotSeq) resList.get(1);
		// 根据类型获取归0方式
		String typeSeq = type + "Seq";// 生成可以进行反射的熟悉
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put(typeSeq, seq.getZeroType());
		Iterator iterator = map.keySet().iterator();
		try {
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				int zeroType = Integer.parseInt(map.get(key).toString());
				switch (zeroType) {
				case 1: // 按年归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
						BeanUtils.setProperty(facSeq, key, 0);
					break;
				}
				case 2: // 按月归档
				{
					if (today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR) || (today.get(Calendar.YEAR) == currcal.get(Calendar.YEAR) && today.get(Calendar.MONTH) != currcal
							.get(Calendar.MONTH)))
						BeanUtils.setProperty(facSeq, key, 0);
					break;
				}
				case 3: // 按日归档
				// {
				// if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
				// BeanUtils.setProperty(customerSeq, key, 0);
				// break;
				// }
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		facSeq.setCurrDate(facSeq.getCurrDate());
		List res = new ArrayList();
		res.add(facSeq);
		this.getCotBaseDao().updateRecords(res);
		return facSeq;
		// return null;
	}

	/**
	 * 描述： 根据配置表中的配置参数，获取数据库配置的参数列表
	 * 
	 * @param cfg
	 *            配置表的配置信息对象
	 * @return 返回值：Object[]
	 */
	private Object[] getObjArgs(CotSeqCfg cfg) {
		Object[] objects = null;
		try {
			if (cfg == null)
				return null;
			if (cfg.getArgsType() == null)
				return null;
			// 获取参数类表
			String[] args = cfg.getArgs().split(",");
			objects = new Object[args.length];
			// 获取参数类型
			String[] argsType = null;
			if (cfg.getArgsType() != null)
				argsType = cfg.getArgsType().split(",");
			int i = 0;
			for (String classType : argsType) {
				Class argType = Class.forName(classType);
				// 生成构造函数所需要的参数
				Class[] constructor = { java.lang.String.class };
				Constructor typeConstructor = argType
						.getConstructor(constructor);
				Object type = typeConstructor.newInstance(args[i]);
				objects[i] = type;// 参数列表
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;

	}

	/**
	 * 描述： 获取订单单号
	 * 
	 * @param idMap
	 * @param currdate
	 * @return 返回值：String
	 */
	public String getOrderNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "order", currdate);
	}

	/**
	 * 描述： 获取新货号
	 * 
	 * @param idMap
	 * @param currdate
	 * @return 返回值：String
	 */
	public String getEleNo(Map idMap) {
		return this.getAllNo(idMap, "ele", null);
	}

	/**
	 * 描述： 获取厂家单号
	 * 
	 * @return 返回值：String
	 */
	public String getFactoryNo() {
		return this.getAllNo(null, "factoryNo", null);
	}

	/**
	 * 描述： 获取应收帐款单号
	 * 
	 * @return 返回值：String
	 */
	public String getFinaceAccountrecvNo(Map idMap) {
		return this.getAllNo(idMap, "fincaeaccountrecv", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sail.cot.seq.SequeceInter#getCustNo()
	 */
	public String getCustNo() {
		return this.getAllNo(null, "cust", null);
	}

	/**
	 * 描述： 获取报价单单号
	 * 
	 * @return 返回值：String
	 */
	public String getPriceNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "price", currdate);
	}

	/**
	 * 描述： 获取送样单号
	 * 
	 * @return 返回值：String
	 */
	public String getGivenNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "given", currdate);
	}

	/**
	 * 描述： 获取征样单号
	 * 
	 * @return 返回值：String
	 */
	public String getSignNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "sign", currdate);
	}

	/**
	 * 描述： 获取生产合同单号
	 * 
	 * @return 返回值：String
	 */
	public String getOrderFacNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "orderfac", null);
	}

	/**
	 * 描述： 获取生产合同分解单号
	 * 
	 * @return 返回值：String
	 */
	public String getAutoOrderFacNo(Map idMap) {
		return this.getAllNo(idMap, "autoOrderfacNo", null);
	}

	/**
	 * 描述： 获取出货单号
	 * 
	 * @return 返回值：String
	 */
	public String getOrderOutNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "orderout", currdate);
	}

	/**
	 * 描述： 获取排载单号
	 * 
	 * @return 返回值：String
	 */
	public String getContainerNo(String currdate) {
		return this.getAllNo(null, "split", null);
	}

	/**
	 * 描述： 获取收款单编号
	 * 
	 * @return 返回值：String
	 */
	public String getFinaceRecNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "finacerecv", currdate);
	}

	/**
	 * 描述： 获取付款单编号
	 * 
	 * @return 返回值：String
	 */
	public String getFinaceGivenNo(Map idMap) {
		return this.getAllNo(idMap, "finacegiven", null);
	}

	/**
	 * 描述： 获取应付款单编号
	 * 
	 * @return 返回值：String
	 */
	public String getFinaceNeeGivenNo(Map idMap) {
		return this.getAllNo(idMap, "fincaeaccountdeal", null);
	}

	/**
	 * 描述： 获取应收款单编号
	 * 
	 * @return 返回值：String
	 */
	public String getFinaceNeeRecGivenNo(Map idMap) {
		return this.getAllNo(idMap, "fincaeaccountrecv", null);
	}

	/**
	 * 描述： 获取配件单号
	 * 
	 * @return 返回值：String
	 */
	public String getFitingNo() {
		return this.getAllNo(null, "access", null);
	}

	/**
	 * 描述： 获取包材单号
	 * 
	 * @return 返回值：String
	 */
	public String getPackingorderNo() {
		return this.getAllNo(null, "packing", null);
	}

	public static void main(String[] args) {
		Calendar currcal = Calendar.getInstance();
		String abc = String.format("%1$tY-%1$tm", currcal);
		System.out.println(abc);
	}

	/**
	 * 描述： 获取条形码单号
	 * 
	 * @return 返回值：String
	 */
	public String getBarcodeNo() {
		return this.getAllNo(null, "barcode", null);
	}
	
	/**
	 * 描述： 获取询盘单号
	 * 
	 * @param idMap
	 * @param currdate
	 * @return 返回值：String
	 */
	public String getPanNo(Map idMap, String currdate) {
		return this.getAllNo(idMap, "pan", currdate);
	}
}
