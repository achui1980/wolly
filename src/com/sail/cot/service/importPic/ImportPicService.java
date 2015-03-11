package com.sail.cot.service.importPic;
 
import java.util.List;
 
import com.sail.cot.domain.CotOrderPic;  
import com.sail.cot.domain.CotPicture;
import com.sail.cot.domain.CotPricePic;  

public interface ImportPicService {

	// 新建文件
	public  void newFile(String filePathAndName, String fileContent);
	
	// 删除文件
	public void delFile(String filePathAndName);
	
	// 删除文件夹
	public  void delFolder(String folderPath);
	
	// 删除目录下所有文件
	public  void delAllFile(String path);
	
	// 获取目录下所有文件夹名称（主单编号）
	public List<String> getFolderNameList(String oldPath);
	
	// 通过主单id获取报价明细id的集合
	public List<Integer> getPriceDetailIdsByMainId(Integer mainId);
	
	// 通过主单id获取订单明细id的集合
	public List<Integer> getOrderDetailIdsByMainId(Integer mainId);
		
	// 通过明细id获取报价图片对象
	public CotPricePic getPricePicByFkId(Integer fkId);
	
	// 通过明细id获取订单图片对象
	public CotOrderPic getOrderPicByFkId(Integer fkId);

	// 根据主单编号获取明细id的集合
	public List<Integer> getDetailIdsByNo(String No, String type);
	 
	// 获取图片字节对象
	public byte[] getPicImg(String folderPath, String eleId);

	// 导入图片(临时)
	public int updatePic(String path, String type);
	
	//通过报价单id获取编号
	public String getPriceNoById(Integer mainId);
	 
	//通过订单id获取编号
    public String getOrderNoById(Integer mainId);
	
	// 导入图片
	public List<?> updatePicture(Integer mainId,String type)throws Exception;
	
	// 获取未导入图片信息
	public List<CotPicture> getUnImportPicList();
	//删除图片
	public void deletePic(List list);
}
