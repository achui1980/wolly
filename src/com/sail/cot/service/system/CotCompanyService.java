package com.sail.cot.service.system;

import java.util.List;
 

import com.jason.core.exception.DAOException;
import com.jason.core.exception.ServiceException;
import com.sail.cot.domain.CotCompany; 
import com.sail.cot.query.QueryInfo;

public interface CotCompanyService {

    
    public int getRecordCount(QueryInfo queryInfo);
	
	public List<?> getList(QueryInfo queryInfo);
	
	void saveCotCompanyList(List<CotCompany> cotCompanyList) throws ServiceException;
	
	void updateCotCompanyByList(List<CotCompany> cotCompanyList) throws ServiceException;
	
	boolean updateCotCompany(CotCompany cotCompany);
	
	int deleteCotCompany(CotCompany cotCompany)throws ServiceException;
	
	int deleteCotCompanyList(List<CotCompany> cotCompanyList) throws ServiceException;
	
	public boolean saveCotCompany(CotCompany cotCompany,String logoPath);
	
	public CotCompany getCompanyById(Integer Id);
	
	public List<?> getCompanyList();
	
	boolean findDeptRecordsCount(String companyId);
	
	Integer findIsExistdefault(); 
	
	boolean findCompanyIsdefault(Integer Id);
	
	//更新数据路中的companyLogo字段
	public void updatePicImg(String filePath,Integer Id);
	
	//删除数据库中的公司logo
	public boolean deletePicImg(Integer Id);
	
	//通过路径删除公司logo图片
	public boolean deleteLogoByPath(String logoPath);
	
	//根据id查找companyLogo
	public byte[] getCompanyLogoById(Integer Id);
	
	public String getJsonData(QueryInfo queryInfo) throws DAOException;
	
	public Integer saveOrUpdateCompany(CotCompany company, String picPath);
	
	// 查询公司简称是否存在
	public Integer findIsExistShortName(String shortName, String id);
}
