package com.sail.cot.util.pdf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.jason.core.Application;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotGivenType;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderFacdetailCopy;
import com.sail.cot.domain.VPurchaseOrder;
import com.sail.cot.domain.vo.CotArtWordVO;
import com.sail.cot.util.pdf.POService;

public class POServiceImpl implements POService{
	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		if(cotBaseDao == null){
			cotBaseDao = (CotBaseDao)Application.getInstance().getContainer().getComponent("CotBaseDao");
		}
		return cotBaseDao;
	}
	
	@SuppressWarnings("unchecked")
	public List<CotOrderFacdetail> getDetailList(Integer orderId) {
		if(orderId==null)
			return null;
		String hql = "from CotOrderFacdetail c where c.orderId = ? order by c.sortNo";
		List<CotOrderFacdetail> orderList = this.getCotBaseDao().queryForLists(hql, new Object[]{orderId});
		return orderList;
	}
	
	@SuppressWarnings("unchecked")
	public List<CotArtWordVO> getArtWordList(Integer orderId){
		if(orderId==null)
			return null;
		String hql = "select c.id,c.eleNameEn,a.artWork,c.barcode,c.custNo,a.sizeInch,a.artText,a.remark,a.awStr,a.comment,c.eleUnit" +
				" from CotOrderFacdetail c,CotArtWork a where" +
				" c.id = a.eleId" +
				" and c.orderId = ? order by c.sortNo";
		List<Object[]> res = this.getCotBaseDao().queryForLists(hql, new Object[]{orderId});
		List<CotArtWordVO> orderList = new ArrayList<CotArtWordVO>();
		
		//获得数据字典
		List list=this.getCotBaseDao().getRecords("CotGivenType");
		Map<Integer,String> map = new HashMap<Integer,String>();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			CotGivenType cotGivenType = (CotGivenType) it.next();
			map.put(cotGivenType.getId(),cotGivenType.getGivenName());
		}
		
		CotArtWordVO awVo; 
		for (Object[] objects : res) {
			awVo = new CotArtWordVO();
			awVo.setId((Integer)objects[0]);
			awVo.setEleNameEn((String)objects[1]);
			awVo.setArtWork((String)objects[2]);
			awVo.setBarcode((String)objects[3]);
			awVo.setCustNo((String)objects[4]);
			awVo.setEleInchDesc((String)objects[5]);
			awVo.setArtText((String)objects[6]);
			awVo.setRemark((String)objects[7]);
			awVo.setAwStr((String)objects[8]);
			awVo.setComment((String)objects[9]);
			awVo.setEleUnit((String)objects[10]);
			orderList.add(awVo);
		}
		return orderList;
	}

	public VPurchaseOrder getCotPOVO(Integer orderId) {
		if(orderId==null)
			return null;
		VPurchaseOrder pOrder = (VPurchaseOrder) this.getCotBaseDao().getById(VPurchaseOrder.class, orderId);
		return pOrder;
	}

	@Override
	public List<CotOrderFacdetailCopy> getDetailCopyList(Integer orderId) {
		if(orderId==null)
			return null;
		String hql = "from CotOrderFacdetailCopy c where c.orderId = ? order by c.sortNo";
		List<CotOrderFacdetailCopy> orderCopyList = this.getCotBaseDao().queryForLists(hql, new Object[]{orderId});
		return orderCopyList;
	}
	
}
