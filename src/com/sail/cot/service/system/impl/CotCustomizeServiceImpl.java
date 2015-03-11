package com.sail.cot.service.system.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCustomizeField;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotMailExecute;
import com.sail.cot.domain.vo.CotCustomizeVO;
import com.sail.cot.service.system.CotCustomizeService;



public class CotCustomizeServiceImpl implements CotCustomizeService{
	private CotBaseDao baseDao;
	public CotBaseDao getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public void saveCotCustomize(List list,String type,Integer empsId){
		System.out.println(list.size());
		String strSql = " from CotCustomizeField obj where obj.empId = "+empsId+" and obj.type='"+type+"'";
		List<CotCustomizeField> rs = this.getBaseDao().find(strSql);
		System.out.println(rs.size());
		if(rs.size()>0){
			List ids=new ArrayList();
			for(CotCustomizeField customize :rs){
				int id=customize.getId();
				ids.add(id);
			}
			try {
				this.getBaseDao().deleteRecordByIds(ids, "CotCustomizeField");
			} catch (DAOException e) {
				e.printStackTrace();
			}	
		}
		List<CotCustomizeField> res=new ArrayList<CotCustomizeField>();
		for(int i=0;i<list.size();i++){
			String fieldName =(String) list.get(i);
			CotCustomizeField cotCustomizeField =new CotCustomizeField();
			cotCustomizeField.setEmpId(empsId);
			cotCustomizeField.setFieldName(fieldName);
			cotCustomizeField.setType(type);
			res.add(cotCustomizeField);
		}
		this.getBaseDao().saveOrUpdateRecords(res);
	}
	
	public List<String> getCotCustomizeFields(Integer empId,String type){
		String strSql = " from CotCustomizeField obj where obj.empId = "+empId+" and obj.type='"+type+"'";
		List<CotCustomizeField> res = this.getBaseDao().find(strSql);
		List<String> arr=new ArrayList<String>();
		//CotCustomizeField cotCustomizeField;
		for(int i=0;i<res.size();i++){
			CotCustomizeField cotCustomizeField=res.get(i);
			arr.add(cotCustomizeField.getFieldName());
		}
		return arr;
	}
	
	//返回map
	public Map getCotCustomizeFieldMap(String type){
		HttpSession session =WebContextFactory.get().getSession();
		CotEmps emp = (CotEmps) session.getAttribute("emp");
		Integer empId =emp.getId();
		String strSql = " from CotCustomizeField obj where obj.empId = "+empId+" and obj.type='"+type+"'";
		List<CotCustomizeField> res = this.getBaseDao().find(strSql);
		Map map=new HashMap();
		for(CotCustomizeField customize : res){
			String key=customize.getFieldName();
			map.put(key, true);
		}
		return map;
	}
	//读取XML
	public Map readXML(){
		Map map =new HashMap();
		Map<String, List<CotCustomizeVO>> treeMap=new HashMap();
		String url=CotCustomizeServiceImpl.class.getResource("/customzifields.xml").getPath();
		if(url==null) return null;
		File file=new File(url);
		InputStreamReader io = null;
		try {
			io = new InputStreamReader(new FileInputStream(file),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader(false);
		Document doc = null;
		try {
			doc = reader.read(io);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root=doc.getRootElement();
		
		//根节点
		for(java.util.Iterator i=root.elementIterator();i.hasNext();){
			Element modelset=(Element) i.next();
			//modlset节点
			for(java.util.Iterator y=modelset.elementIterator();y.hasNext();){
				Element model =(Element) y.next();
				
				//model节点
				for(java.util.Iterator u=model.elementIterator();u.hasNext();){
					List<CotCustomizeVO> list=new ArrayList<CotCustomizeVO>();
					Element fieldset=(Element) u.next();
					String title = null;
					if(fieldset.getName().endsWith("fieldset")){
						//fieldset节点
						for(java.util.Iterator p=fieldset.elementIterator();p.hasNext();){
							CotCustomizeVO customizeVO=new CotCustomizeVO();
							Element field =(Element) p.next();
							String name=field.getStringValue();
							customizeVO.setName(name);
							//循环属性
							for(java.util.Iterator a =field.attributeIterator();a.hasNext();){
								Attribute attr=(Attribute) a.next();
								String value =attr.getValue();
								customizeVO.setValue(value);
							}
							list.add(customizeVO);
						}
					}else{
						//title
						title=fieldset.getStringValue();
						String id = null;
						//循环title属性
						for(java.util.Iterator b =fieldset.attributeIterator();b.hasNext();){
							Attribute attrB=(Attribute) b.next();
							id=attrB.getValue();
						}
						map.put(id, title);
					}
					treeMap.put(title, list);
				}
				
			}	
		}
		System.out.println(map.size());
		return map;
	}
	
	//读取XML
	public Map readXMLDate(){
		Map map=new HashMap();
		String url=CotCustomizeServiceImpl.class.getResource("/customzifields.xml").getPath();
		if(url==null) return null;
		File file=new File(url);
		InputStreamReader io = null;
		try {
			io = new InputStreamReader(new FileInputStream(file),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader(false);
		Document doc = null;
		try {
			doc = reader.read(io);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root=doc.getRootElement();
		
		//根节点
		for(java.util.Iterator i=root.elementIterator();i.hasNext();){
			//modlset节点
			Element modelset=(Element) i.next();
			for(java.util.Iterator y=modelset.elementIterator();y.hasNext();){
				//model节点
				Element model =(Element) y.next();
				List<CotCustomizeVO> list =null;
				String title=null;
				String id=null;
				for(java.util.Iterator u=model.elementIterator();u.hasNext();){
					//fieldset节点
					Element fieldset=(Element) u.next();
					list = new ArrayList<CotCustomizeVO>();
					if(fieldset.getName().endsWith("fieldset")){
						for(java.util.Iterator p=fieldset.elementIterator();p.hasNext();){
							//field节点
							Element field =(Element) p.next();
							CotCustomizeVO customizeVO=new CotCustomizeVO();
							customizeVO.setName(field.getStringValue());
							customizeVO.setValue(field.attributeValue("value"));
							list.add(customizeVO);
						}
					}else{
						title =fieldset.getStringValue();
						id=fieldset.attributeValue("id");
					}
				}		
				map.put(id, list);
			}	
		}
		System.out.println(map.size());
		return map;
	}
	
	
	//读取XML的数据
	public Map initPanel(){
		Map paraMap=this.readXMLDate();
		System.out.println(paraMap.size());
		Iterator it = paraMap.entrySet().iterator(); 
		while (it.hasNext()) 
		{ 
		Map.Entry pairs = (Map.Entry)it.next(); 
		System.out.println(pairs.getKey() + " = " + pairs.getValue()); 
		} 
		
		return paraMap;
	}
}
