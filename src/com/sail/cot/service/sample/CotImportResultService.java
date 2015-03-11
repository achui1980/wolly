package com.sail.cot.service.sample;

import java.util.List;

import com.sail.cot.domain.CotImportresult;
import com.sail.cot.query.QueryInfo;

public interface CotImportResultService {
	
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);

	public void deleteCotImportresult(List<CotImportresult>  importresultList);
	
	public void deleteImportresultById(Integer Id);
	
	CotImportresult getImportresultById(Integer Id);
}
