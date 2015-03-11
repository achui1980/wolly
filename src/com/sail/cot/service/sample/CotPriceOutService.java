package com.sail.cot.service.sample;

import java.util.List;
import java.util.Map;
import com.sail.cot.domain.CotElementsNew;

public interface CotPriceOutService {

	public CotElementsNew getElementsNewById(Integer Id);
	 
	void addOrUpdatePriceOut(CotElementsNew cotElementsNew);
	
	void delPriceOut(CotElementsNew cotElementsNew);
	 
	List<?> getCotCurrencyList();
	
	Map<?, ?> getMap();
	
	public boolean findRecord();
}
