package com.sail.cot.util.pdf;

import java.util.List;

import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderFacdetailCopy;
import com.sail.cot.domain.VPurchaseOrder;
import com.sail.cot.domain.vo.CotArtWordVO;

public interface POService {
	public VPurchaseOrder getCotPOVO(Integer orderId);
	public List<CotOrderFacdetail> getDetailList(Integer orderId);
	public List<CotOrderFacdetailCopy> getDetailCopyList(Integer orderId);
	public List<CotArtWordVO> getArtWordList(Integer orderId);
}
