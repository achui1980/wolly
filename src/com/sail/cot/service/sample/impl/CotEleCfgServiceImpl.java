package com.sail.cot.service.sample.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotBoxPacking;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.sample.CotEleCfgService;
import com.sail.cot.util.Log4WebUtil;

public class CotEleCfgServiceImpl implements CotEleCfgService {

	private CotBaseDao cotBaseDao;
	public CotBaseDao getCotBaseDao(){
		return cotBaseDao;
	}
	public void setCotBaseDao(CotBaseDao cotBaseDao){
		this.cotBaseDao = cotBaseDao;
	}
	private Logger logger = Log4WebUtil.getLogger(CotEleCfgServiceImpl.class);
	
	public void addEleCfg(CotEleCfg cotEleCfg) {
		if(cotEleCfg.getCfgFlag()==1){
			this.updateDefaultFlag(cotEleCfg);
		}			
		this.getCotBaseDao().create(cotEleCfg);	
	}

	public boolean delEleCfg(Integer id) {
		
		List<CotEleCfg> list =new ArrayList<CotEleCfg>();
		list = this.getCotBaseDao().find("from CotEleCfg obj where obj.id ='"+id+"'");
		CotEleCfg cotEleCfg = list.get(0);
		if (this.findRecord()==true) {
			return false;
		}else {
			if (cotEleCfg.getCfgFlag()==1) {
					List<CotEleCfg> res = new ArrayList<CotEleCfg>();
					res = this.getCotBaseDao().find("from CotEleCfg obj where obj.cfgFlag =0");
					CotEleCfg cfg = res.get(0);
					cfg.setCfgFlag(1L);
					
					try {
						this.getCotBaseDao().deleteRecordById(cotEleCfg.getId(), "CotEleCfg");
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.getCotBaseDao().update(cfg);
				   
			}else {
				try {
					this.getCotBaseDao().deleteRecordById(cotEleCfg.getId(), "CotEleCfg");
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
	}
	
	public void deleteEleCfg(List<?> eleCfgList) {
		// TODO Auto-generated method stub

			List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i <eleCfgList.size(); i++) {
				CotEleCfg cotEleCfg = (CotEleCfg) eleCfgList.get(i);
				ids.add(cotEleCfg.getId());
			}
			try {
				this.getCotBaseDao().deleteRecordByIds(ids, "CotEleCfg");
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("删除样品默认数据异常",e);
			}
	}

	public CotEleCfg getEleCfgById(Integer Id) {
		// TODO Auto-generated method stub
		return (CotEleCfg) this.getCotBaseDao().getById(CotEleCfg.class, Id);
	}

	public List<?> getEleCfgList() {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords("CotEleCfg");
	}

	public List<?> getList(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().findRecords(queryInfo);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		// TODO Auto-generated method stub
		try {
			return this.getCotBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
		return 0;
	}

	public void modifyEleCfg(CotEleCfg cotEleCfg) {
		List<CotEleCfg> records = new ArrayList<CotEleCfg>();
		cotEleCfg.setCfgFlag(1L);
		records.add(cotEleCfg);
		this.getCotBaseDao().saveOrUpdateRecords(records);
		//this.getCotBaseDao().update(cotEleCfg);		
	}
	public boolean findRecord() {
		// TODO Auto-generated method stub
		boolean isHaveRecord = false;
		List record = this.getCotBaseDao().find("from CotEleCfg obj");
		if (record.size()==1) {
			isHaveRecord = true;
		}
		return isHaveRecord;
	}
	public Map getEleFacMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotFactory");
		for (int i = 0; i < res.size(); i++) {
			CotFactory cotFactory = (CotFactory)res.get(i);
			retur.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return retur;
	}
	public Map getEleTypeMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotTypeLv1");
		for (int i = 0; i < res.size(); i++) {
			CotTypeLv1 cotTypeLv1 = (CotTypeLv1)res.get(i);
			retur.put(cotTypeLv1.getId().toString(), cotTypeLv1.getTypeName());
		}
		return retur;
	}

	public Map getCurrencyMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotCurrency");
		for(int i=0; i<res.size(); i++)
		{
			CotCurrency cotCurrency = (CotCurrency)res.get(i);
			retur.put(cotCurrency.getId().toString(),cotCurrency.getCurNameEn());
			
		}
		return retur;
	}
	
	public List<?> getObjList(String objName) {
		// TODO Auto-generated method stub
		return this.getCotBaseDao().getRecords(objName);
	}
	
	//查找所有包装类型
	public List<?> getBoxTypeList(){
		return this.getCotBaseDao().getRecords("CotBoxType");
	}
	
	//通过编号查找包装类型
	public CotBoxType getBoxTypeById(Integer id){
		return (CotBoxType) this.getCotBaseDao().getById(CotBoxType.class, id);
	}
	
	//通过编号查找包装类型名称
	@SuppressWarnings("unchecked")
	public String getBoxNameById(Integer id){
		List<CotBoxPacking> list  = this.getCotBaseDao().find("from CotBoxPacking obj where obj.id="+id);
		if(list!=null&&list.size()>0){
			CotBoxPacking cotBoxPacking = (CotBoxPacking) list.get(0);
			String value = cotBoxPacking.getValue();
			String valueEn = cotBoxPacking.getValueEn();
			if(valueEn==null){
				valueEn = "";
			}
			String composeBoxName = value+"("+valueEn+")";
			return composeBoxName;
		}
		return null;
	}
	
	public Map getBoxTypeMap() {
		// TODO Auto-generated method stub
		Map retur = new HashMap();
		List res = this.getCotBaseDao().getRecords("CotBoxType");
		for(int i=0; i<res.size(); i++)
		{
			CotBoxType cotBoxType = (CotBoxType)res.get(i);
			retur.put(cotBoxType.getId().toString(),cotBoxType.getTypeName() );
			
		}
		return retur;
	}
	public void updateDefaultFlag(CotEleCfg cotEleCfg) {
		if(cotEleCfg.getCfgFlag()==0){
			List<CotEleCfg> res = new ArrayList<CotEleCfg>();
			res = this.getCotBaseDao().find("from CotEleCfg obj where obj.cfgFlag =0");
			if (res.size()!=0) {
				CotEleCfg cfg = res.get(0);
				cfg.setCfgFlag(1L);
				this.getCotBaseDao().update(cfg);
			}else{
				cotEleCfg.setCfgFlag(1L);
			}
		}else{
			Long cfgFlag = 0L;
			List<CotEleCfg> res = new ArrayList<CotEleCfg>();
			res = this.getCotBaseDao().find("from CotEleCfg obj where obj.cfgFlag = 1");
			if (res.size()!=0) {
				CotEleCfg cfg = (CotEleCfg) res.get(0);
				cfg.setCfgFlag(cfgFlag);
				this.getCotBaseDao().update(cfg);
			}
		}
	}

	public boolean IsDefault(Integer Id) {
		 
		boolean Isdef = false;
		List<?> list = this.getCotBaseDao().find("select obj.id from CotEleCfg obj where obj.cfgFlag = 1 and obj.id='"+Id+"'");
		if(list.size()!= 0)
		{
			Isdef = true;
		}
		return Isdef;
	}
	public boolean checkCalculation(String str) {
		Evaluator evaluator = new Evaluator();
		try {
			if(!str.equals("")){
				evaluator.evaluate(str);
			}
			return true;
		} catch (EvaluationException ee) {
			System.out.println(ee);
			return false;
		}
	}
	
	//根据厂家编号获取简称
	public String getFacShortName(Integer id){
		String hql = "select obj.shortName from CotFactory obj where obj.id="+id;
		List<?> list = this.getCotBaseDao().find(hql);
		if(list.get(0)==null){
			return "";
		}else{
			return list.get(0).toString();
		}
	}
	
	public CotEleCfg getEleCfg(){
		String hql = " from CotEleCfg as e";
		List<CotEleCfg> res = new ArrayList<CotEleCfg>();
		res = this.getCotBaseDao().find(hql);
		if (res.size() !=0 ) {
			CotEleCfg cotEleCfg = res.get(0);
			return cotEleCfg;
		}else {
			return null;
		}
	}
}
