/**
 * 
 */
package com.sail.cot.seq;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustSeq;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.vo.CotCfgNo;
import com.sail.cot.service.system.impl.SetNoServiceImpl;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 11, 2009 9:37:08 AM </p>
 * <p>Class Name: AbstractSeq.java </p>
 * @author achui
 *
 */
public class BaseSeq {
	
	private String seqNo;	  //单号规则表达式
	private String seqOutExp; //单号的外部表达式 如yyyy-MM-dd等
	private String zeroType;
	private int	   curseq;   //当前序列号
	private String curdate;  //当前时间
	private String strSeq;   //序列号表达式 如4SEQ，3SEQ等
	private String hisdate;	 //历史时间
	
	URL url = BaseSeq.class.getResource("/sysconfig.properties");
	String filepath = url.getPath();
	//String filepath = "E:/工作/proj/COT_SYSTEM/code/src/sysconfig.properties";
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao() {
		if(cotBaseDao == null)
			cotBaseDao=(CotBaseDao)SystemUtil.getService("CotBaseDao");
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}
	public BaseSeq(){
		
	}
	public BaseSeq(String innerExp,String zeroType,int curseq,String hisdate)
	{
		seqNo = innerExp;
		seqOutExp = this.backReplace(seqNo);
		this.zeroType = zeroType;
		this.curseq = curseq;
		this.hisdate = hisdate;
	}
	
	private String backReplace(String str){
		str = str.replace("%1$tY","[YYYY]");
		str = str.replace("%1$ty","[YY]");
		str = str.replace("%1$tm","[MM]");
		str = str.replace("%1$td","[DD]");
		str = str.replace("%2$02d","[2SEQ]");
		str = str.replace("%2$03d","[3SEQ]");
		str = str.replace("%2$04d","[4SEQ]");
		str = str.replace("_KH%3$02d","[2KHSEQ]");
		str = str.replace("_KH%3$03d","[3KHSEQ]");
		str = str.replace("_KH%3$04d","[4KHSEQ]");
		
		return str;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getSeqOutExp() {
		return seqOutExp;
	}
	public void setSeqOutExp(String seqOutExp) {
		this.seqOutExp = seqOutExp;
	}
	public String getZeroType() {
		return zeroType;
	}
	public void setZeroType(String zeroType) {
		this.zeroType = zeroType;
	}
	public int getCurseq() {
		return curseq;
	}
	public void setCurseq(int curseq) {
		this.curseq = curseq;
	}
	public String getCurdate() {
		curdate = this.getCurrentDate();
		return curdate;
	}
	public void setCurdate(String curdate) {
		
		this.curdate = curdate;
	}
	public String getStrSeq() {
		return strSeq;
	}
	public void setStrSeq(String strSeq) {
		this.strSeq = strSeq;
	}
	//是否按照厂家Id生成
	public boolean hasFac()
	{
		boolean res = false;
		String[] arr = SystemUtil.stringsBetween(this.seqOutExp, "[", "]");
		for(String flag : arr)
		{
			if("CH".equals(flag))
			{
				res = true;
				break;
			}
		}
		return res;
	}
	//是否按照客户Id生成
	public boolean hasCust()
	{
		boolean res = false;
		String[] arr = SystemUtil.stringsBetween(this.seqOutExp, "[", "]");
		for(String flag : arr)
		{
			if("KH".equals(flag))
			{
				res = true;
				break;
			}
		}
		return res;
	}
	//是否按照员工Id生成
	public boolean hasEmp()
	{
		boolean res = false;
		String[] arr = SystemUtil.stringsBetween(this.seqOutExp, "[", "]");
		for(String flag : arr)
		{
			if("EMP".equals(flag))
			{
				res = true;
				break;
			}
		}
		return res;
	}
	//是否含有客户序列号
	public boolean hasKHSeq()
	{
		boolean res = false;
		String[] arr = SystemUtil.stringsBetween(this.seqOutExp, "[", "]");
		for(String flag : arr)
		{
			if("KHSEQ".equals(flag))
			{
				res = true;
				break;
			}
		}
		return res;
	}
	public boolean hasOrderNo()
	{
		boolean res = false;
		String[] arr = SystemUtil.stringsBetween(this.seqOutExp, "[", "]");
		for(String flag : arr)
		{
			if("ORDERNO".equals(flag))
			{
				res = true;
				break;
			}
		}
		return res;
	}
	
	private String getCurrentDate()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");      
		Calendar rightNow = Calendar.getInstance();
		Date now = rightNow.getTime();
		String today = sdf.format(now).toString();
		return today;
	}
	/**
	 * 描述：
	 * @param idMap		对应的id，可以是厂家id，客户id，员工id等,用Map表示 KH：客户ID，CH：厂家ID，EMP：员工ID
	 * @param hisdate	对应单据生成的历史时间
	 * @param toZeroField	需要归0的属性
	 * @return
	 * 返回值：String 	 按规则生成的单号
	 */
	public String getNo(Map idMap,String timeType,String toZeroField)
	{
		String name = null;
		Integer id = null;
		String no = this.getSeqNo();
		if(this.hasCust())
		{
			id = (Integer)idMap.get("KH");
			CotCustomer customer = 	(CotCustomer)this.getCotBaseDao().getById(CotCustomer.class, id);
			name = customer.getCustomerNo();
			no = no.replace("[KH]", name);
		}		
		if(this.hasFac())
		{
			id = (Integer)idMap.get("CH");
			CotFactory fac = 	(CotFactory) this.getCotBaseDao().getById(CotFactory.class, id);
			name = fac.getFactoryNo();
			no = no.replace("[CH]", name);
		}
		if(this.hasEmp())
		{
			id = (Integer)idMap.get("EMP");
			CotEmps emp = 	(CotEmps) this.getCotBaseDao().getById(CotEmps.class, id);
			name = emp.getEmpsId();
			no = no.replace("[EMP]", name);
		}
		if(hasOrderNo())
		{
			String orderno = (String)idMap.get("ORDERNO");
			no = no.replace("[ORDERNO]", orderno);
		}

		Integer currseq = this.getCurrentSeq(timeType);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(format.parse(this.getCurdate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		no = String.format(no,cal,currseq);
		if(this.hasKHSeq())
		{
			id = (Integer)idMap.get("KH");
			Integer custSeq = this.getCustSeq(id,toZeroField);
			no = String.format(no,cal,currseq,custSeq);
		}
		return no;
	}
	
	private Integer getCustSeq(Integer custId,String toZeroField){
		
		String strSql = " from  CotCustSeq obj where obj.custId="+custId+" and obj.currDate='"+this.getCurdate()+"'";
		List seq = this.getCotBaseDao().find(strSql);
		CotCustSeq custSeq = null;
		if(seq == null || seq.size() == 0)
			custSeq = null;
		else
			custSeq = (CotCustSeq)seq.get(0);
		Integer currentCustSeq =0;
		if(custSeq == null) //不存在记录是，添加一条记录
		{
			String[] fields = ReflectionUtils.getDeclaredFields(CotCustSeq.class);
			custSeq = new CotCustSeq();
			for(String field : fields)
			{
				if(field.equals("currDate"))
					continue;
				try {
					BeanUtils.setProperty(custSeq, field, 0);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			custSeq.setCustId(custId);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			try {
				custSeq.setCurrDate(new java.sql.Date(sdf.parse(this.getCurdate()).getTime()));
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
		if(custSeq != null) //数据库中存在记录
		{
			//判断是否同一天，如果不是同一天，则归0
			custSeq = this.updateCustSeqToZero(custSeq, toZeroField);
			String tmpSeq = null;
			try {
				tmpSeq = BeanUtils.getProperty(custSeq, toZeroField);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tmpSeq == null)
				currentCustSeq = 0;
			else
				currentCustSeq = new Integer(tmpSeq);
		}
		if(currentCustSeq == null)
			currentCustSeq = 0;
		currentCustSeq = currentCustSeq+1;
		if(this.getSeqOutExp().indexOf("2SEQ")>=0 && currentCustSeq > 99)
			currentCustSeq = 1;
		if(this.getSeqOutExp().indexOf("3SEQ")>=0 && currentCustSeq > 999)
			currentCustSeq = 1;
		if(this.getSeqOutExp().indexOf("4SEQ")>=0 && currentCustSeq > 999)
			currentCustSeq = 1;
		return currentCustSeq;
	}
	
	private Integer getCurrentSeq(String timeType)
	{
		HashMap map = new HashMap();
		Calendar currcal = Calendar.getInstance(); //获取历史比对时间
		Calendar today = Calendar.getInstance(); //获取当前时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			currcal.setTime(format.parse(this.hisdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer seq = new Integer(this.curseq);
		seq = seq+1;
		if(this.seqOutExp.indexOf("2SEQ")>=0 && seq > 99)
			seq = 0;
		else if(seqOutExp.indexOf("3SEQ")>=0 && seq > 999)
			seq = 0;
		else if(seqOutExp.indexOf("4SEQ")>=0 && seq > 9999)
			seq = 0;
		int zero = Integer.parseInt(this.zeroType);
		switch(zero)
		{
			case 1: //按年归档
			{
				if(today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
					seq = 1;
				break;
			}
			case 2: //按月归档
			{
				if(today.get(Calendar.MONTH) != currcal.get(Calendar.MONTH))
					seq = 1;
				break;
			}
			case 3: //按日归档
//			{
//				if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
//					currSeq = 1;
//				break;
//			}	
		}
		map.put(timeType, this.getCurrentDate());
		SystemUtil.setPropertyFile(map, filepath);//保存历史时间
		return seq;
	}

	public String getHisdate() {
		return hisdate;
	}

	public void setHisdate(String hisdate) {
		this.hisdate = hisdate;
	}
	
	//根据归0方式将客户序列号表中的相应数据归零,置归0 toZeroField字段，toZeroField对应CotCustSeq中的属性
	private CotCustSeq updateCustSeqToZero(CotCustSeq customerSeq,String toZeroField) {
		
		Calendar currcal = Calendar.getInstance(); //获取历史比对时间
		Calendar today = Calendar.getInstance(); //获取当前时间
		currcal.setTime(customerSeq.getCurrDate());
	
		try
		{
			int zeroType = Integer.parseInt(this.zeroType);
				switch(zeroType)
				{
					case 1: //按年归档
					{
						if(today.get(Calendar.YEAR) != currcal.get(Calendar.YEAR))
							BeanUtils.setProperty(customerSeq, toZeroField, 0);
						break;
					}
					case 2: //按月归档
					{
						if(today.get(Calendar.MONTH) != currcal.get(Calendar.MONTH))
							BeanUtils.setProperty(customerSeq, toZeroField, 0);
						break;
					}
					case 3: //按日归档
//					{
//						if(today.get(Calendar.DATE) != currcal.get(Calendar.DATE))
//							BeanUtils.setProperty(customerSeq, key, 0);
//						break;
//					}	
				}

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		customerSeq.setCurrDate(customerSeq.getCurrDate());
		List res = new ArrayList();
		res.add(customerSeq);
		this.getCotBaseDao().updateRecords(res);
		return customerSeq;
	}
	public void saveSeq(String timeType,String sequence)
	{
		Integer currSeq = this.getCurrentSeq(timeType);
		HashMap map = new HashMap();	
		map.put(sequence, currSeq.toString());
		map.put(timeType, this.getCurrentDate());
		map.put("currentDate", this.getCurrentDate());
		SystemUtil.setPropertyFile(map, filepath);
	}
}
