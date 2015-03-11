package com.sail.cot.service.sample;

import java.util.List;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.vo.ImportMsgVO;

public interface ImportPictureService {

	 
	
	public  void newFile(String filePathAndName, String fileContent);
	
	public void delFile(String filePathAndName);
	
	public  void delFolder(String folderPath);
	
	public  void delAllFile(String path);
	
	public  void copyFolder(String newPath, String oldPath, boolean isCover,
			boolean isAdd, int coverSum, int addSum, int crossSum,String crossMsg,
			List<CotElePic> cotPicCoverList, List<CotElePic> cotPicAddList,
			List<CotElementsNew> cotEleList, List<ImportMsgVO> msgList,
			ImportMsgVO importMsgVO);
	
	public List<?> moveFile( boolean isCover, boolean isAdd) ;

	public int isExistFile();
	
	public boolean findExistEleId(String eleId);
	
	public boolean findExistPicName(String picName);
	
	public Integer getIdByEleId(String eleId);
	
	public void getFileNameList(String oldPath,List<CotPicture> res,String systemPath);
	
	public  List<?> getUnImportPicList();
	
	public  void checkPic(String oldPath,Integer maxLength,List<?> res) ;
	
	public  String getCheckMessage(Integer maxLength);
	
	public  boolean delWrongFile(Integer maxLength);
	
	public void openUploadFolder(boolean isNet); 
	
	public Integer getVersion();
	
	//获取服务器的上传路径
	public String getUploadPath(boolean isNet);
	
	//设置新增样品为默认样品配置
	public CotElementsNew setEleCfgToAddEle(CotElementsNew cotElementsNew);
	
	public void deletePic(List list);
}
