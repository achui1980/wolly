package com.sail.cot.util.pdf.impl;

import java.util.List;

import com.jason.core.Application;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustContact;
import com.sail.cot.domain.CotOrder;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.VProformaInvoice;
import com.sail.cot.util.pdf.PIService;

public class PIServiceImpl implements PIService{
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)Application.getInstance().getContainer().getComponent("CotBaseDao");
		}
		return cotBaseDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<CotOrderDetail> getDetailList(Integer orderId) {
		if(orderId==null)
			return null;
		String hql = "from CotOrderDetail c where c.orderId = ? order by c.sortNo";
		List<CotOrderDetail> orderList = this.getCotBaseDao().queryForLists(hql, new Object[]{orderId});
		return orderList;
	}

	public VProformaInvoice getCotPIVO(Integer orderId) {
		if(orderId==null)
			return null;
		VProformaInvoice pInvoice = (VProformaInvoice) this.getCotBaseDao().getById(VProformaInvoice.class, orderId);
		CotOrder cotOrder = (CotOrder)this.getCotBaseDao().getById(CotOrder.class, orderId);
		if(cotOrder.getContactId() != null){
			CotCustContact contact = (CotCustContact)this.getCotBaseDao().getById(CotCustContact.class, cotOrder.getContactId());
			if(contact != null){
				pInvoice.setContact(contact.getContactPerson());
			}
		}
		return pInvoice;
		
	}
	
	
}
