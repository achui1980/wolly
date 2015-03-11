package com.sail.cot.util.pdf.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;

import com.jason.core.Application;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotOrderOut;
import com.sail.cot.domain.CotOrderOutDel;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.CotOrderOutdetailDel;
import com.sail.cot.domain.VCeditNode;
import com.sail.cot.domain.VInvoice;
import com.sail.cot.util.ReflectionUtils;
import com.sail.cot.util.pdf.InService;

public class InServiceImpl implements InService {
	private CotBaseDao cotBaseDao;

	public CotBaseDao getCotBaseDao() {
		if (cotBaseDao == null) {
			cotBaseDao = (CotBaseDao) Application.getInstance().getContainer()
					.getComponent("CotBaseDao");
		}
		return cotBaseDao;
	}

	@SuppressWarnings("unchecked")
	public List<CotOrderOutdetail> getDetailList(Integer orderId,
			boolean isInvoice) {
		if (orderId == null)
			return null;
		
		String[] propEle = ReflectionUtils
				.getDeclaredFields(CotOrderOutdetailDel.class);
		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		SqlDateConverter dateConverter = new SqlDateConverter();
		convertUtils.register(dateConverter, Date.class);
		// 因为要注册converter,所以不能再使用BeanUtils的静态方法了，必须创建BeanUtilsBean实例
		BeanUtilsBean beanUtils = new BeanUtilsBean(convertUtils,
				new PropertyUtilsBean());

		String hql = "from "
				+ (isInvoice ? "CotOrderOutdetail" : "CotOrderOutdetailDel")
				+ " c where c.orderId = ? order by c.sortNo";

		if (isInvoice) {
			return this.getCotBaseDao().queryForLists(hql,
					new Object[] { orderId });
		} else {
			List<CotOrderOutdetail> orderList = new ArrayList<CotOrderOutdetail>();
			List<CotOrderOutdetailDel> orderDelList = this.getCotBaseDao()
					.queryForLists(hql, new Object[] { orderId });
			CotOrderOutdetail orOutdetail = null;
			for (int i = 0; i < orderDelList.size(); i++) {
				orOutdetail = new CotOrderOutdetail();
				try {
					// BeanUtils.copyProperties(orOutdetail,
					// orderDelList.get(i));

					for (int j = 0; j < propEle.length; j++) {
//						if (!propEle[j].equals("picImg")) {
							String value = beanUtils.getProperty(orderDelList
									.get(i), propEle[j]);
							if (value != null) {
								beanUtils.setProperty(orOutdetail, propEle[j],
										value);
							}
//						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				orderList.add(orOutdetail);
			}
			return orderList;
		}
	}
	
	/**
	 * 
	 * @Description：判断出货主单或作废主单的公司是否是wollly 如果是则返回true
	 * @Flow：
	 * @Example：
	 * @Files：
	 * @author:azan
	 * @Create:2012-1-9 上午09:50:20
	 * @param orderId
	 * @param flag
	 * @return boolean【】
	 */
	public boolean isWolly(Integer orderId,boolean flag) {
		if(flag){
			CotOrderOut cotOrderOut = (CotOrderOut)this.getCotBaseDao().getById(CotOrderOut.class,
					orderId);
			if(cotOrderOut.getCompanyId()==1){
				return true;
			}else{
				return false;
			}
		}else{
			CotOrderOutDel cotOrderOut = (CotOrderOutDel)this.getCotBaseDao().getById(CotOrderOutDel.class,
					orderId);
			if(cotOrderOut.getCompanyId()==1){
				return true;
			}else{
				return false;
			}
		}
	}

	public VInvoice getCotInVO(Integer orderId, boolean isInvoice) {
		if (orderId == null)
			return null;
		VInvoice invoice = null;
		if (isInvoice)
			invoice = (VInvoice) this.getCotBaseDao().getById(VInvoice.class,
					orderId);
		else
			invoice = (VCeditNode) this.getCotBaseDao().getById(
					VCeditNode.class, orderId);
		return invoice;
	}

}
