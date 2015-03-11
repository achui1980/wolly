package com.sail.cot.util.pdf;

import java.util.List;

import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.VInvoice;

public interface InService {
	public VInvoice getCotInVO(Integer orderId,boolean isInvoice);
	public List<CotOrderOutdetail> getDetailList(Integer orderId,boolean isInvoice);
	
	public boolean isWolly(Integer orderId,boolean flag);
}
