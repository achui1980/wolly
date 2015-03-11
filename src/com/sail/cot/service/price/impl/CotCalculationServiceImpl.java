package com.sail.cot.service.price.impl;

import java.util.ArrayList;
import java.util.List;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCalculation;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.price.CotCalculationService;

 
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class CotCalculationServiceImpl implements CotCalculationService {
	
    private CotBaseDao cotBaseDao;
	
	public CotBaseDao getCotBaseDao() {
		return cotBaseDao;
	}

	public void setCotBaseDao(CotBaseDao cotBaseDao) {
		this.cotBaseDao = cotBaseDao;
	}

	public void addCalculation(CotCalculation cotCalculation) {
		 
		this.getCotBaseDao().create(cotCalculation);
	}

	public Integer deleteCalculation(List<CotCalculation> CalculationList) {
		 
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < CalculationList.size(); i++) {
			CotCalculation cotCalculation = (CotCalculation) CalculationList.get(i);
			ids.add(cotCalculation.getId());
		}
		try {
			this.getCotBaseDao().deleteRecordByIds(ids, "CotCalculation");
			return 0;
		} catch (DAOException e) {
			 
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean modifyCalculation(CotCalculation cotCalculation) {
		//获取旧的对内表达式
		CotCalculation oldCotCalculation = this.getCalculationById(cotCalculation.getId());
		String oldCalculation = oldCotCalculation.getExpessionIn();
		//String oldCalName = oldCotCalculation.getCalName();
		String hql = " from CotCalculation as obj where obj.id <>"+cotCalculation.getId()+" and obj.expessionIn LIKE '%"+oldCalculation + "%'";
		List<CotCalculation> res = new ArrayList<CotCalculation>();
		res = this.getCotBaseDao().find(hql);
		List<CotCalculation> reslist = new ArrayList<CotCalculation>();
		reslist.add(cotCalculation);
		try {
			//先保存修改公式
			this.getCotBaseDao().updateRecords(reslist);
			//修改关联的公式
			for (int i = 0; i < res.size(); i++) {
				List<CotCalculation> list = new ArrayList<CotCalculation>();
				CotCalculation newCotCalculation = (CotCalculation)res.get(i);
				String oldExpession = newCotCalculation.getExpessionIn();
				newCotCalculation.setExpessionIn(this.replace(oldExpession, oldCalculation, cotCalculation.getExpessionIn()));
				list.add(newCotCalculation);
				this.getCotBaseDao().updateRecords(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}   
	//替换表达式
	public String replace(String strOriginal,String strOld,String strNew)   
	{   
	   int i=0;   
	   StringBuffer strBuffer=new StringBuffer(strOriginal);   
	   while((i=strOriginal.indexOf(strOld,i))>=0)   
	   {     
	      strBuffer.delete(i,i+strOld.length());   
	      strBuffer.insert(i,strNew);
	      i=i+strNew.length();   
	      strOriginal=strBuffer.toString();   
	   }     
	   return strOriginal;   
	}  

	public CotCalculation getCalculationById(Integer id) {
		 
		return (CotCalculation) this.getCotBaseDao().getById(CotCalculation.class, id);
	}

	public List<?> getList(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		 
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}

	public List<?> getCalculationList(){
		return this.getCotBaseDao().getRecords("CotCalculation");
	}

	public boolean checkCalculation(String str) {
		Evaluator evaluator = new Evaluator();
		try {
			evaluator.evaluate(str);
			return true;
		} catch (EvaluationException ee) {
			System.out.println(ee);
			return false;
		}
	}

	public boolean findExistByName(String name) {
		QueryInfo queryInfo = new QueryInfo();
		boolean isExist = false;
		queryInfo.setSelectString("from CotCalculation obj where obj.calName='"+name+"'");
		queryInfo.setCountQuery("select count(*) from CotCalculation obj where obj.calName='"+name+"'");
		try {
			int count = this.getCotBaseDao().getRecordsCount(queryInfo);
			if(count > 0)
				isExist =  true;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return isExist;
	}
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return getCotBaseDao().getJsonData(queryInfo);
	}
}
