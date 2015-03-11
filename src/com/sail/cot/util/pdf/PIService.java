package com.sail.cot.util.pdf;

import java.util.List;

import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.VProformaInvoice;

public interface PIService {
	public VProformaInvoice getCotPIVO(Integer orderId);
	public List<CotOrderDetail> getDetailList(Integer orderId);
}
