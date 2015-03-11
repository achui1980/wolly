/**
 * 
 */
package com.sail.cot.seq;

import java.net.URL;
import java.util.Map;

import com.sail.cot.util.SystemUtil;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Aug 11, 2009 2:46:37 PM </p>
 * <p>Class Name: GenAllSeq.java </p>
 * @author achui
 *
 */
public class GenAllSeq {

	URL url = BaseSeq.class.getResource("/sysconfig.properties");
	String filepath = url.getPath();
	//BaseSeq baseSeq = null;
	
	private BaseSeq getBaseSeq(String type)
	{
		BaseSeq baseSeq = null;
		String seqNo = null;
		String zeroType = null;
		String currSeq = null;
		String hisdate = null;
		String res = null;
		if("finacerecvNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "finacerecvNo");
			zeroType = SystemUtil.getProperty(filepath, "finacerecvZeroType");
			currSeq = SystemUtil.getProperty(filepath, "finacerecvNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "finacerecvHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap, "finacerecvHisdate","finacerecvSeq");
		}
		else if("finacegivenNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "finacegivenNo");
			zeroType = SystemUtil.getProperty(filepath, "finacegivenZeroType");
			currSeq = SystemUtil.getProperty(filepath, "finacegivenNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "finacegivenHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"finacegivenHisdate","finacegivenSeq");
		}
		else if("fincaeaccountdealNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "fincaeaccountdealNo");
			zeroType = SystemUtil.getProperty(filepath, "fincaeaccountdealZeroType");
			currSeq = SystemUtil.getProperty(filepath, "fincaeaccountdealNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "fincaeaccountdealHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"fincaeaccountdealHisdate","fincaeaccountdealSeq");
		}
		else if("fincaeaccountrecvNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "fincaeaccountrecvNo");
			zeroType = SystemUtil.getProperty(filepath, "fincaeaccountrecvZeroType");
			currSeq = SystemUtil.getProperty(filepath, "fincaeaccountrecvNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "fincaeaccountrecvHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"fincaeaccountrecvHisdate", "fincaeaccountrecvSeq");
		}
		else if("backtaxNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "backtaxNo");
			zeroType = SystemUtil.getProperty(filepath, "backtaxZeroType");
			currSeq = SystemUtil.getProperty(filepath, "backtaxNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "backtaxHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"backtaxHisdate", "backtaxSeq");
		}
		else if("auditNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "auditNo");
			zeroType = SystemUtil.getProperty(filepath, "auditZeroType");
			currSeq = SystemUtil.getProperty(filepath, "auditNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "auditHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"auditHisdate", "auditSeq");
		}
		else if("packingNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "packingNo");
			zeroType = SystemUtil.getProperty(filepath, "packingZeroType");
			currSeq = SystemUtil.getProperty(filepath, "packingNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "packingHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"packingHisdate", "packingSeq");
		}
		else if("accessNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "accessNo");
			zeroType = SystemUtil.getProperty(filepath, "accessZeroType");
			currSeq = SystemUtil.getProperty(filepath, "accessNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "accessHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"accessHisdate", "accessSeq");
		}
		else if("custNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "custNo");
			zeroType = SystemUtil.getProperty(filepath, "custZeroType");
			currSeq = SystemUtil.getProperty(filepath, "custNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "custHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"custHisdate", "custSeq");
		}
		else if("facNo".equals(type))
		{
			seqNo = SystemUtil.getProperty(filepath, "facNo");
			zeroType = SystemUtil.getProperty(filepath, "facZeroType");
			currSeq = SystemUtil.getProperty(filepath, "facNoSequence");
			hisdate = SystemUtil.getProperty(filepath, "facHisdate");
			baseSeq = new BaseSeq(seqNo,zeroType,new Integer(currSeq),hisdate);
			//res = baseSeq.getNo(idMap,"facHisdate", "facSeq");
		}
		return baseSeq;
	}
	public String getAllSeqByType(String type,Map idMap)
	{
		//String filepath = "E:/工作/proj/COT_SYSTEM/code/src/sysconfig.properties";
		String seqNo = null;
		String zeroType = null;
		String currSeq = null;
		String hisdate = null;
		String res = null;
		BaseSeq baseSeq = this.getBaseSeq(type);
		if("finacerecvNo".equals(type))
		{
			res = baseSeq.getNo(idMap, "finacerecvHisdate","finacerecvSeq");
		}
		else if("finacegivenNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"finacegivenHisdate","finacegivenSeq");
		}
		else if("fincaeaccountdealNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"fincaeaccountdealHisdate","fincaeaccountdealSeq");
		}
		else if("fincaeaccountrecvNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"fincaeaccountrecvHisdate", "fincaeaccountrecvSeq");
		}
		else if("backtaxNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"backtaxHisdate", "backtaxSeq");
		}
		else if("auditNo".equals(type))
		{
			
			res = baseSeq.getNo(idMap,"auditHisdate", "auditSeq");
		}
		else if("packingNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"packingHisdate", "packingSeq");
		}
		else if("accessNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"accessHisdate", "accessSeq");
		}
		else if("custNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"custHisdate", "custSeq");
		}
		else if("facNo".equals(type))
		{
			res = baseSeq.getNo(idMap,"facHisdate", "facSeq");
		}
		return res;
	}
	public void saveSeq(String type)
	{
		BaseSeq baseSeq = this.getBaseSeq(type);
		if("finacerecvNo".equals(type))
			baseSeq.saveSeq("finacerecvHisdate", "finacerecvNoSequence");
		else if("finacegivenNo".equals(type))
			baseSeq.saveSeq("finacegivenHisdate", "finacegivenNoSequence");
		else if("fincaeaccountdealNo".equals(type))
			baseSeq.saveSeq("fincaeaccountdealHisdate", "fincaeaccountdealNoSequence");
		else if("fincaeaccountrecvNo".equals(type))
			baseSeq.saveSeq("fincaeaccountrecvHisdate", "fincaeaccountrecvNoSequence");
		else if("backtaxNo".equals(type))
			baseSeq.saveSeq("backtaxHisdate", "backtaxNoSequence");
		else if("auditNo".equals(type))
			baseSeq.saveSeq("auditHisdate", "auditNoSequence");
		else if("packingNo".equals(type))
			baseSeq.saveSeq("packingHisdate", "packingNoSequence");
		else if("accessNo".equals(type))
			baseSeq.saveSeq("accessHisdate", "accessNoSequence");
		else if("custNo".equals(type))
			baseSeq.saveSeq("custHisdate", "custNoSequence");
		else if("facNo".equals(type))
			baseSeq.saveSeq("facNoHisdate", "facNoSequence");
		
	}
	
	public static void main(String[] args)
	{
		GenAllSeq seq = new GenAllSeq();
		String test = seq.getAllSeqByType("finacerecvNo", null);
		System.out.println("----"+test);
		seq.saveSeq("finacerecvNo");
	}
}
