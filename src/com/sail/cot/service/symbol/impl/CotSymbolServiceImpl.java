/**
 * 
 */
package com.sail.cot.service.symbol.impl;

import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.service.symbol.CotSymbolService;

/**
 * 订单管理模块
 * 
 * @author qh-chzy
 * 
 */
public class CotSymbolServiceImpl implements CotSymbolService {

	private CotBaseDao symbolDao;

	public CotBaseDao getSymbolDao() {
		return symbolDao;
	}

	public void setSymbolDao(CotBaseDao symbolDao) {
		this.symbolDao = symbolDao;
	}
	
}
