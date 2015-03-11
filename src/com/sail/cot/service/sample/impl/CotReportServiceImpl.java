/**
 * 
 */
package com.sail.cot.service.sample.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotImportresult;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.vo.CotMsgVO;

import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotReportService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 样品管理模块
 * 
 * @author qh-chzy
 * 
 */
public class CotReportServiceImpl implements CotReportService {

	private CotReportDao reportDao;
//	private GenAllSeq seq = new GenAllSeq();
	private Logger logger = Log4WebUtil.getLogger(CotReportServiceImpl.class);

	// 根据文件路径导入
	public List<?> saveReport(String filename, Boolean isCover){
		CotOpImgService opImgImpl = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		// 查询所有样品编号
		Map<String, Integer> mapEleId = this.getReportDao().getAllEleId();
		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有产品类别
		Map<String, Integer> mapEleType = this.getReportDao().getAllEleType();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao()
				.getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao()
				.getAllBoxTypeByEn();
		// 查询所有币种
		Map<String, Integer> mapCurrency = this.getReportDao().getAllCurrency();
		// 查询所有厂家简称
		Map<String, Integer> mapShortName = this.getReportDao()
				.getAllFactoryShortName();
		// 查询所有海关编码
		Map<String, Integer> mapHsCode = this.getReportDao().getAllHsId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		WebContext ctx = WebContextFactory.get();
		Float[] cubes = this.getReportDao().getContainerCube();
		// 得到暂无图片的字节
		byte[] zwtpImg = this.getReportDao().getZwtpPic();
		
		//查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			// 获得tomcat路径
			String classPath = CotReportServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			workbook = Workbook.getWorkbook(new File(systemPath + "upload/"
					+ filename), setting);

		} catch (Exception e) {
			return null;
		}

		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		// 限制一次性只能导入2000条记录
		if (sheet.getRows() > 2004) {
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(1);
			cotMsgVO.setMsg("Import failed! Only import a maximum of 2,000 samples！");
			msgList.add(cotMsgVO);
			return msgList;
		}
		for (int i = 4; i < sheet.getRows(); i++) {
			// 新建样品对象
			CotElementsNew cotElements = new CotElementsNew();
			// 定义厂家
			CotFactory cotFactory = new CotFactory();
			// 是否有成功标识
			boolean isSuccess = true;
			// 用于计算CBM和CUFT
			float boxObL = 0f;
			float boxObW = 0f;
			float boxObH = 0f;

			// 产品长宽高
			float boxL = 0f;
			float boxW = 0f;
			float boxH = 0f;
			float boxLInch = 0f;
			float boxWInch = 0f;
			float boxHInch = 0f;

			// 单重
			float boxWeigth = 0f;
			float gWeigh = 0f;
			float nWeigh = 0f;

			String eleSize = "";
			String eleSizeInch = "";
			boolean isChangeL=false;
			// 厂价
			Float priceFac = null;
			// 外销价
			Float priceOut = null;

			Integer hsIdTemp = 0;
			float tuiLv = 0f;
			// 用于计算装箱数
			long boxObCount = 0;

			// 标识是否是子货号
			boolean isChild = false;

			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();
				//如果没转换成数字cell,默认的最长小数位是3位
				if (row.getType() == CellType.NUMBER
						|| row.getType() == CellType.NUMBER_FORMULA) {
					NumberCell nc = (NumberCell) row;
					rowCtn =df.format(nc.getValue());
				}
				if (headCtn.getContents().equals("ELE_ID")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					// Integer flag = this.getReportDao().isExistEleId(
					// rowCtn.trim());
					Object eleId = mapEleId.get(rowCtn.trim().toLowerCase());
					if (eleId == null) {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
							"The length of Article No. can not exceed 100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotElements.setEleId(rowCtn.trim().toUpperCase());
						if (cotEleCfg != null) {
							// 设置样品默认值
							cotElements = this.getReportDao().setDefault(
									cotEleCfg, cotElements, false);
						}
					} else {
						// 根据上传策略判断是否覆盖已存在的记录
						if (isCover) {
							cotElements = (CotElementsNew) this.getReportDao()
									.getById(CotElementsNew.class,
											(Integer) eleId);
							cotElements = (CotElementsNew) SystemUtil
									.deepClone(cotElements);
							coverNum++;
						} else {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. already exists");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if(cotElements.getBoxObCount()!=null){
						boxObCount=cotElements.getBoxObCount();
					}
				}

				if (headCtn.getContents().equals("HS_ID") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					// Integer flag = this.getReportDao().isExistHsId(
					// rowCtn.trim());
					Object hsId = mapHsCode.get(rowCtn.trim().toLowerCase());
					if (hsId == null) {
						CotEleOther cotEleOther = new CotEleOther();
						cotEleOther.setHscode(rowCtn.trim());
						cotEleOther.setCnName(rowCtn.trim());
						if (tuiLv != 0) {
							cotEleOther.setTaxRate(tuiLv);
						}
						try {
							this.getReportDao().create(cotEleOther);
							// 将新类别添加到已有的map中
							mapHsCode.put(rowCtn.trim().toLowerCase(), cotEleOther.getId());
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					cotElements.setEleHsid(mapHsCode.get(rowCtn.trim().toLowerCase()));
					hsIdTemp = mapHsCode.get(rowCtn.trim().toLowerCase());
				}
				if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleName(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV2") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleTypenameLv2(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_NAME_EN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setEleNameEn(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FLAG")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						if (rowCtn.trim().equals("套件")) {
							cotElements.setEleFlag(1l);
						}
						if (rowCtn.trim().equals("单件")) {
							cotElements.setEleFlag(0l);
						}
						if (rowCtn.trim().equals("组件")) {
							cotElements.setEleFlag(3l);
						}
					} else {
						cotElements.setEleFlag(0l);
					}
				}
				if (headCtn.getContents().equals("ELE_PARENT")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					Object eleId = mapEleId.get(rowCtn.trim().toLowerCase());
					if (eleId == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotElements.setEleParentId((Integer) eleId);
						cotElements.setEleParent(rowCtn.trim());
						cotElements.setEleFlag(2l);
						isChild = true;
					}
				}
				if (headCtn.getContents().equals("FACTORY_NO")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setFactoryNo(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleFrom(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FACTORY")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setEleFactory(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_COL") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleCol(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BARCODE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					if(rowCtn.trim().length()>30){
						cotElements.setBarcode(rowCtn.trim().substring(0,30));
					}else{
						cotElements.setBarcode(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_COMPOSETYPE")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setEleComposeType(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setEleUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setEleGrade(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_FOR_PERSON")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setEleForPerson(rowCtn.trim());
				}
				if (headCtn.getContents().equals("ELE_PRO_TIME")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (row.getType() == CellType.DATE) {
						DateCell dc = (DateCell) row;
						cotElements.setEleProTime(new java.sql.Date(dc
								.getDate().getTime()));
					} else {
						try {
							java.util.Date date = sdf.parse(rowCtn.trim());

							cotElements.setEleProTime(new java.sql.Date(date
									.getTime()));
						} catch (ParseException e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("ELE_SIZE_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSize = rowCtn.trim();
				}
				if (headCtn.getContents().equals("ELE_INCH_DESC")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					eleSizeInch = rowCtn.trim();
				}
				if (headCtn.getContents().equals("ELE_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if(rowCtn.trim().length()>500){
						cotElements.setEleRemark(rowCtn.trim().substring(0, 500));
					}else{
						cotElements.setEleRemark(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						//去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");
						Integer typeId = mapTypeCn.get(temp.toLowerCase());
						if (typeId == null) {
							typeId = mapTypeEn.get(temp.toLowerCase());
						}
						if (typeId == null) {
							// 新增一条材质,中英文名相同
							CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
							cotTypeLv1.setTypeName(temp);
							cotTypeLv1.setTypeEnName(temp);
							List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
							listType.add(cotTypeLv1);
							try {
								this.getReportDao().saveRecords(listType);
								typeId = cotTypeLv1.getId();
								mapTypeCn.put(temp.toLowerCase(),typeId);
							} catch (DAOException e) {
								e.printStackTrace();
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存样品材质异常!");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						} 
						cotElements.setEleTypenameLv1(temp);
						cotElements.setEleTypeidLv1(typeId);
					}
				}

				if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object typeId = mapEleType.get(rowCtn.trim().toLowerCase());
						if (typeId == null) {
							CotTypeLv2 cotTypeLv2 = new CotTypeLv2();
							cotTypeLv2.setTypeName(rowCtn.trim());
							try {
								this.getReportDao().create(cotTypeLv2);
								// 将新类别添加到已有的map中
								mapEleType.put(rowCtn.trim().toLowerCase(), cotTypeLv2
										.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存产品分类异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						cotElements.setEleTypeidLv2(mapEleType.get(rowCtn
								.trim().toLowerCase()));
					}

				}

				// 厂家简称不存在时新建厂家
				if (headCtn.getContents().equals("SHORT_NAME")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						//去掉厂家名称中的回车换行
						String t = rowCtn.trim().replaceAll("\r", "");
						String temp = t.replaceAll("\n", "");
						
						Object factoryId = mapShortName.get(temp.toLowerCase());
						
						if (factoryId == null) {
							cotFactory.setFactoryName(temp);
							cotFactory.setShortName(temp);
							cotFactory.setFactroyTypeidLv1(1);
							try {
								CotSeqService cotSeqServiceImpl=new CotSeqServiceImpl();
								String facNo =cotSeqServiceImpl.getFacNo();
//								String facNo = seq.getAllSeqByType("facNo", null);
								cotFactory.setFactoryNo(facNo);
								this.getReportDao().create(cotFactory);
								cotSeqServiceImpl.saveSeq("factoryNo");
								//seq.saveSeq("facNo");
								// 将新类别添加到已有的map中
								mapShortName.put(temp.toLowerCase(), cotFactory
										.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						cotElements.setFactoryId(mapShortName
								.get(temp.toLowerCase()));
					}
				}
				// -----------------------------包装信息
				try {
					if (headCtn.getContents().equals("ELE_MOD")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						cotElements.setEleMod(elemod.intValue());
					}
					if (headCtn.getContents().equals("ELE_UNITNUM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						cotElements.setEleUnitNum(elemod.intValue());
					}
					if (headCtn.getContents().equals("cube") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						cotElements.setCube(Float.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_L") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxL = Float.parseFloat(dfTwo.format(num));
						} else {
							boxL = num;
						}
						boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						cotElements.setBoxL(boxL);
						cotElements.setBoxLInch(boxLInch);
					}
					if (headCtn.getContents().equals("BOX_L_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxLInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxLInch = num;
						}
						boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
						cotElements.setBoxL(boxL);
						cotElements.setBoxLInch(boxLInch);

					}
					if (headCtn.getContents().equals("BOX_W") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxW = Float.parseFloat(dfTwo.format(num));
						} else {
							boxW = num;
						}
						boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						cotElements.setBoxW(boxW);
						cotElements.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_W_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxWInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxWInch = num;
						}
						boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
						cotElements.setBoxW(boxW);
						cotElements.setBoxWInch(boxWInch);
					}
					if (headCtn.getContents().equals("BOX_H") && rowCtn != null
							&& !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxH = Float.parseFloat(dfTwo.format(num));
						} else {
							boxH = num;
						}
						boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
						cotElements.setBoxH(boxH);
						cotElements.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_H_INCH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						isChangeL=true;
						float num = Float.parseFloat(rowCtn.trim());
						int temp = rowCtn.trim().lastIndexOf(".");
						if (temp > -1) {
							boxHInch = Float.parseFloat(dfTwo.format(num));
						} else {
							boxHInch = num;
						}
						boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
						cotElements.setBoxH(boxH);
						cotElements.setBoxHInch(boxHInch);
					}
					if (headCtn.getContents().equals("BOX_HANDLEH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxHandleH(Float.parseFloat(rowCtn
								.trim()));
					}
					if (headCtn.getContents().equals("BOX_PB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxPbL(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxPbLInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxPbW(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxPbWInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_PB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxPbH(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxPbHInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}
					if (headCtn.getContents().equals("BOX_PB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							cotElements
									.setBoxPbCount(elemod.longValue());
						}
					}
					
					if (headCtn.getContents().equals("BOX_IB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxIbL(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxIbLInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxIbW(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxIbWInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_IB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxIbH(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxIbHInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxMbL(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxMbLInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxMbW(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxMbWInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_MB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						cotElements.setBoxMbH(Float.parseFloat(rowCtn.trim()));
						cotElements.setBoxMbHInch(Float.parseFloat(rowCtn
								.trim()) / 2.54f);
					}

					if (headCtn.getContents().equals("BOX_OB_L")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObL = Float.parseFloat(rowCtn.trim());
						cotElements.setBoxObL(boxObL);
						cotElements.setBoxObLInch(boxObL / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						cotElements.setBoxCbm(cbm);
						cotElements.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							cotElements.setBox20Count(new Float(count20));
							cotElements.setBox40Count(new Float(count40));
							cotElements.setBox40hqCount(new Float(count40hq));
							cotElements.setBox45Count(new Float(count45));
						}

					}

					if (headCtn.getContents().equals("BOX_OB_W")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObW = Float.parseFloat(rowCtn.trim());
						cotElements.setBoxObW(boxObW);
						cotElements.setBoxObWInch(boxObW / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						cotElements.setBoxCbm(cbm);
						cotElements.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							cotElements.setBox20Count(new Float(count20));
							cotElements.setBox40Count(new Float(count40));
							cotElements.setBox40hqCount(new Float(count40hq));
							cotElements.setBox45Count(new Float(count45));
						}
					}

					if (headCtn.getContents().equals("BOX_OB_H")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxObH = Float.parseFloat(rowCtn.trim());
						cotElements.setBoxObH(boxObH);
						cotElements.setBoxObHInch(boxObH / 2.54f);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						cotElements.setBoxCbm(cbm);
						cotElements.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							cotElements.setBox20Count(new Float(count20));
							cotElements.setBox40Count(new Float(count40));
							cotElements.setBox40hqCount(new Float(count40hq));
							cotElements.setBox45Count(new Float(count45));
						}
					}
					// 根据单重和外箱数计算单箱毛净重
					if (headCtn.getContents().equals("BOX_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						boxWeigth = Float.parseFloat(rowCtn.trim());
						cotElements.setBoxWeigth(Float
								.parseFloat(rowCtn.trim()));
					}
					if (headCtn.getContents().equals("BOX_GROSS_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						gWeigh = Float.parseFloat(rowCtn.trim());
					}
					if (headCtn.getContents().equals("BOX_NET_WEIGTH")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						nWeigh = Float.parseFloat(rowCtn.trim());
					}
					
					if (headCtn.getContents().equals("BOX_IB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							cotElements.setBoxIbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_MB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							cotElements.setBoxMbCount(elemod.longValue());
						}
					}
					if (headCtn.getContents().equals("BOX_OB_COUNT")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							Double elemod = Double.parseDouble(rowCtn.trim());
							boxObCount = elemod.longValue();
							cotElements.setBoxObCount(boxObCount);
							float cbm = Float.parseFloat(df.format(boxObL
									* boxObW * boxObH * 0.000001F));
							// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

							if (boxObCount != 0 && cbm != 0) {
								int count20 = (int) ((cubes[0] / cbm) * boxObCount);
								int count40 = (int) ((cubes[2] / cbm) * boxObCount);
								int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
								int count45 = (int) ((cubes[3] / cbm) * boxObCount);
								cotElements.setBox20Count(new Float(count20));
								cotElements.setBox40Count(new Float(count40));
								cotElements
										.setBox40hqCount(new Float(count40hq));
								cotElements.setBox45Count(new Float(count45));
							}
						}
					}

					if (headCtn.getContents().equals("LI_RUN")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float priceProfit = Float.parseFloat(rowCtn.trim());
						cotElements.setLiRun(priceProfit);
					}

					if (headCtn.getContents().equals("TUI_LV")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						tuiLv = Float.parseFloat(rowCtn.trim());
						cotElements.setTuiLv(tuiLv);
						if (hsIdTemp != 0) {
							CotEleOther cotEleOther = (CotEleOther) this
									.getReportDao().getById(CotEleOther.class,
											hsIdTemp);
							cotEleOther.setTaxRate(tuiLv);
							List list = new ArrayList();
							list.add(cotEleOther);
							this.getReportDao().updateRecords(list);
						}
					}

					if (headCtn.getContents().equals("PRICE_FAC")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceFac = Float.parseFloat(rowCtn.trim());
						if(priceFac<0){
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("PRICE_OUT")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						priceOut = Float.parseFloat(rowCtn.trim());
						if(priceOut<0){
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					if (headCtn.getContents().equals("BOX_CBM")
							&& rowCtn != null && !rowCtn.trim().equals("")) {
						float cbm = Float.parseFloat(rowCtn.trim());
						float cuft = Float.parseFloat(df.format(cbm * 35.315f));
						cotElements.setBoxCbm(cbm);
						cotElements.setBoxCuft(cuft);
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							cotElements.setBox20Count(new Float(count20));
							cotElements.setBox40Count(new Float(count40));
							cotElements.setBox40hqCount(new Float(count40hq));
							cotElements.setBox45Count(new Float(count45));
						}
					}
					if (headCtn.getContents().equals("BOX_TYPE_ID")) {
						if (rowCtn != null && !rowCtn.trim().equals("")) {
							String[] box = mapBoxTypeCn.get(rowCtn.trim().toLowerCase());
							if (box == null) {
								box = mapBoxTypeEn.get(rowCtn.trim().toLowerCase());
							}
							if (box == null) {
								// 新增一条包装类型,中英文名相同
								CotBoxType cotBoxType = new CotBoxType();
								cotBoxType.setTypeName(rowCtn.trim());
								cotBoxType.setTypeNameEn(rowCtn.trim());
								List<CotBoxType> listType = new ArrayList<CotBoxType>();
								listType.add(cotBoxType);
								try {
									this.getReportDao().saveRecords(listType);
									box = new String[5];
									box[0]=cotBoxType.getId().toString();
									box[1]=null;
									box[2]=null;
									box[3]=null;
									box[4]=null;
									mapBoxTypeCn.put(rowCtn.trim().toLowerCase(), box);
								} catch (DAOException e) {
									e.printStackTrace();
									CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"Save Packing Way exception!");
									msgList.add(cotMsgVO);
									isSuccess = false;
									break;
								}
							} 
							if(box[0]!=null){
								cotElements.setBoxTypeId(Integer.parseInt(box[0]));
							}
							if(box[1]!=null){
								cotElements.setBoxIbTypeId(Integer.parseInt(box[1]));
							}
							if(box[2]!=null){
								cotElements.setBoxMbTypeId(Integer.parseInt(box[2]));
							}
							if(box[3]!=null){
								cotElements.setBoxObTypeId(Integer.parseInt(box[3]));
							}
							if(box[4]!=null){
								cotElements.setBoxPbTypeId(Integer.parseInt(box[4]));
							}
							
						}
					}
				} catch (Exception e) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxUint(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxTDesc(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxBDesc(rowCtn.trim());
				}
				
				if (headCtn.getContents().equals("BOX_REMARK")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxRemark(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_REMARK_CN")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxRemarkCn(rowCtn.trim());
				}

				// ---------------------报价信息
				if (headCtn.getContents().equals("PRICE_UINT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim().toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
							"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotElements.setPriceFacUint(mapCurrency.get(rowCtn
								.trim().toLowerCase()));
					}
				}

				if (headCtn.getContents().equals("PRICE_UNIT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Object curId = mapCurrency.get(rowCtn.trim().toLowerCase());
						if (curId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
							"Currency does not exist");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotElements.setPriceOutUint(mapCurrency.get(rowCtn
								.trim().toLowerCase()));
					}
				}
			}
			if (isSuccess == true) {
				try {
					// 判断是否是子货号
					if (isChild) {
						cotElements.setEleFlag(2l);
					}
					// 保存样品信息
					cotElements.setEleAddTime(now);
					// 设置中英文规格
					if (!eleSize.equals("")) {
						cotElements.setEleSizeDesc(eleSize);
					} else {
						if(isChangeL)
						cotElements.setEleSizeDesc(boxL + "*" + boxW + "*"
								+ boxH);
					}
					if (!eleSizeInch.equals("")) {
						cotElements.setEleInchDesc(eleSizeInch);
					} else {
						if(isChangeL)
						cotElements.setEleInchDesc(boxLInch + "*" + boxWInch
								+ "*" + boxHInch);
					}

					// 设置毛净重
					if (gWeigh == 0) {
						Float cfgGross = 0f;
						if (cotEleCfg!=null && cotEleCfg.getGrossNum() != null) {
							cfgGross = cotEleCfg.getGrossNum();
						}
						float grossWeight = boxWeigth * boxObCount/1000 + cfgGross;
						cotElements.setBoxGrossWeigth(grossWeight);
					} else {
						cotElements.setBoxGrossWeigth(gWeigh);
					}
					if (nWeigh == 0) {
						float netWeight = boxWeigth * boxObCount/1000;
						cotElements.setBoxNetWeigth(netWeight);
					} else {
						cotElements.setBoxNetWeigth(nWeigh);
					}
					
					//计算包材价格
					cotElements=this.getReportDao().calPrice(boxPackMap, cotElements);
					
					//1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
					if(priceFac==null && priceOut==null){
						if(isCover){
							if(cotElements.getPriceFac()!=null){
								priceFac=cotElements.getPriceFac();
							}
							if(cotElements.getPriceOut()!=null){
								priceOut = cotElements.getPriceOut();
							}
						}else{
							if(cotEleCfg!=null){
								priceFac=this.sumPriceFac(cotEleCfg.getExpessionFacIn(),cotElements);
								priceOut=this.sumPriceOut(cotEleCfg.getExpessionIn(),priceFac,cotElements);
							}
						}
					}
					//2.excel有厂价没有外销价,只计算外销价
					if(priceFac!=null && priceOut==null){
						if(isCover){
							if(cotElements.getPriceOut()!=null){
								priceOut = cotElements.getPriceOut();
							}
						}else{
							if(cotEleCfg!=null){
								priceOut=this.sumPriceOut(cotEleCfg.getExpessionIn(),priceFac,cotElements);
							}
						}
					}
					//3.只有外销价
					if(priceFac==null && priceOut!=null){
						if(isCover){
							if(cotElements.getPriceFac()!=null){
								priceFac=cotElements.getPriceFac();
							}
						}else{
							if(cotEleCfg!=null){
								priceFac=this.sumPriceFac(cotEleCfg.getExpessionFacIn(),cotElements);
							}
						}
					}
					
					cotElements.setPriceFac(priceFac);
					cotElements.setPriceOut(priceOut);
					

					if (cotElements.getId() == null) {
						cotElements.setPicName("zwtp");
						this.getReportDao().create(cotElements);
						// 添加样品默认图片为("zwtp")
						CotElePic cotElePic = new CotElePic();
						cotElePic.setEleId(cotElements.getEleId());
						cotElePic.setFkId(cotElements.getId());
						cotElePic.setPicImg(zwtpImg);
						cotElePic.setPicName(cotElements.getEleId());
						cotElePic.setPicSize(new Integer(zwtpImg.length));
						List<CotElePic> imgList = new ArrayList<CotElePic>();
						imgList.add(cotElePic);
						opImgImpl.saveImg(imgList);
					} else {
						List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
						listTemp.add(cotElements);
						this.getReportDao().updateRecords(listTemp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					CotMsgVO cotMsgVO = new CotMsgVO(i, 0,  "Please check It exceeds the maximum length of!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				mapEleId.put(cotElements.getEleId().toLowerCase(), cotElements
						.getId());
				// 增加成功条数
				successNum++;
			}
		}
		// 增加影响行数
		CotMsgVO cotMsgVO = new CotMsgVO();
		cotMsgVO.setFlag(0);
		cotMsgVO.setSuccessNum(successNum);
		// 覆盖条数减去失败条数
		if (isCover) {
			coverNum = coverNum - msgList.size();
		}
		cotMsgVO.setCoverNum(coverNum);
		cotMsgVO.setFailNum(msgList.size());
		msgList.add(cotMsgVO);
		// 将错误信息存入系统日志
		this.saveErrorMsgToFile(msgList);
		//获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		return msgList;
	}

	// 将生成的错误信息保存到文本文件中
	public CotImportresult saveErrorMsgToFile(List<CotMsgVO> list) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String impFilename = "样品导入-" + sdf.format(System.currentTimeMillis())
				+ ".txt";
		// 获得tomcat路径
		String classPath = CotReportServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		// 新建文件夹importResult
		File importResult = new File(systemPath + "importResult/");
		// 新建文件
		File write = new File(systemPath + "importResult/" + impFilename);
		BufferedWriter bw;
		try {
			if (!importResult.exists()) {
				importResult.mkdirs();
			}
			bw = new BufferedWriter(new FileWriter(write));
			for (int i = 0; i < list.size(); i++) {
				CotMsgVO cotMsgVO = list.get(i);
				if (cotMsgVO.getFlag() != null && cotMsgVO.getFlag() == 1) {
					bw.write(cotMsgVO.getMsg() + "\r\n");
				} else if (cotMsgVO.getFlag() != null
						&& cotMsgVO.getFlag() == 0) {
					bw.write("导入成功" + cotMsgVO.getSuccessNum() + "条,导入失败"
							+ cotMsgVO.getFailNum() + "条\r\n");
				} else {
					bw.write("第" + (cotMsgVO.getRowNum() + 1) + "行,第"
							+ (cotMsgVO.getColNum() + 1) + "列的错误信息:"
							+ cotMsgVO.getMsg() + "\r\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("创建导入错误信息文件异常");
		}
		// 生成导入结果对象
		CotImportresult cotImportresult = new CotImportresult();
		cotImportresult.setImpType("样品导入");
		Timestamp timestamp = new Timestamp(
				new Long(System.currentTimeMillis()));
		cotImportresult.setImpTime(timestamp);
		cotImportresult.setImpFilename(impFilename);
		cotImportresult.setImpFilepath("importResult/" + impFilename);
		this.getReportDao().create(cotImportresult);
		return cotImportresult;
	}

	// 根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum,
			Boolean isCover) {
		CotOpImgService opImgImpl = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 格式化样品包装数据
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 取得默认的20/40/40HQ/45的柜体积,数据字典没设置的话默认为24/54/68/86)
		WebContext ctx = WebContextFactory.get();
		Float[] cubes = this.getReportDao().getContainerCube();
		// 查询所有样品材质(中文KEY)
		Map<String, Integer> mapTypeCn = this.getReportDao().getAllTypeCn();
		// 查询所有样品材质(英文KEY)
		Map<String, Integer> mapTypeEn = this.getReportDao().getAllTypeEn();
		// 查询所有包装类型(中文KEY)
		Map<String, String[]> mapBoxTypeCn = this.getReportDao()
				.getAllBoxTypeByCn();
		// 查询所有包装类型(英文KEY)
		Map<String, String[]> mapBoxTypeEn = this.getReportDao()
				.getAllBoxTypeByEn();
		// 得到暂无图片的字节
		byte[] zwtpImg = this.getReportDao().getZwtpPic();
		
		//查询所有包装材料
		Map boxPackMap = this.getReportDao().getAllBoxPacking();
		
		try {
			// 获得tomcat路径
			String classPath = CotReportService.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setEncoding("ISO-8859-1");
			setting.setLocale(locale);
			workbook = Workbook.getWorkbook(new File(systemPath + "upload/"
					+ filename),setting);
		} catch (Exception e) {
			return null;
		}
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int i = rowNum;
		// 新建样品对象
		CotElementsNew cotElements = new CotElementsNew();
		// 定义厂家
		CotFactory cotFactory = new CotFactory();
		// 是否有成功标识
		boolean isSuccess = true;
		// 用于计算CBM和CUFT
		float boxObL = 0f;
		float boxObW = 0f;
		float boxObH = 0f;

		// 产品长宽高
		float boxL = 0f;
		float boxW = 0f;
		float boxH = 0f;
		float boxLInch = 0f;
		float boxWInch = 0f;
		float boxHInch = 0f;

		// 单重
		float boxWeigth = 0f;
		float gWeigh = 0f;
		float nWeigh = 0f;

		// 厂价
		Float priceFac = null;
		// 外销价
		Float priceOut = null;

		String eleSize = "";
		String eleSizeInch = "";
		boolean isChangeL=false;
		// 判断是否有删除最后一行
		boolean isDel = false;
		// 用于计算装箱数
		long boxObCount = 0;
		Integer hsIdTemp = 0;

		// 判断是否是子货号
		boolean isChild = false;
		float tuiLv = 0f;
		for (int j = 0; j < sheet.getColumns(); j++) {
			// 表头
			Cell headCtn = sheet.getCell(j, 1);
			Cell row = null;
			try {
				row = sheet.getCell(j, i);
			} catch (Exception e) {
				isDel = true;
				isSuccess = false;
				break;
			}

			String rowCtn = row.getContents();
			//如果没转换成数字cell,默认的最长小数位是3位
			if (row.getType() == CellType.NUMBER
					|| row.getType() == CellType.NUMBER_FORMULA) {
				NumberCell nc = (NumberCell) row;
				rowCtn =df.format(nc.getValue());
			}
			if (headCtn.getContents().equals("ELE_ID")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. can not be empty");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				Integer flag = this.getReportDao().isExistEleId(
						rowCtn.trim().toLowerCase());
				if (flag == null) {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The length of Article No. can not exceed 100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotElements.setEleId(rowCtn.trim().toUpperCase());
					if (cotEleCfg != null) {
						// 设置样品默认值
						cotElements = this.getReportDao().setDefault(cotEleCfg,
								cotElements, false);
					}
				} else {
					// 根据上传策略判断是否覆盖已存在的记录
					if (isCover) {
						cotElements = (CotElementsNew) this.getReportDao()
								.getById(CotElementsNew.class, (Integer) flag);
						cotElements = (CotElementsNew) SystemUtil
								.deepClone(cotElements);
						coverNum++;
					} else {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Article No. already exists");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if(cotElements.getBoxObCount()!=null){
					boxObCount=cotElements.getBoxObCount();
				}
			}
			if (headCtn.getContents().equals("HS_ID") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				Integer flag = this.getReportDao().isExistHsId(rowCtn.trim());
				if (flag == null) {
					CotEleOther cotEleOther = new CotEleOther();
					cotEleOther.setHscode(rowCtn.trim());
					cotEleOther.setCnName(rowCtn.trim());
					if (tuiLv != 0) {
						cotEleOther.setTaxRate(tuiLv);
					}
					try {
						this.getReportDao().create(cotEleOther);
						cotElements.setEleHsid(cotEleOther.getId());
						hsIdTemp = cotEleOther.getId();
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存海关编码异常");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					cotElements.setEleHsid(flag);
					hsIdTemp = flag;
				}
			}

			if (headCtn.getContents().equals("ELE_NAME") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleName(rowCtn.trim());
			}

			if (headCtn.getContents().equals("ELE_TYPENAME_LV2") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleTypenameLv2(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_NAME_EN") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleNameEn(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FLAG")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().equals("套件")) {
						cotElements.setEleFlag(1l);
					}
					if (rowCtn.trim().equals("单件")) {
						cotElements.setEleFlag(0l);
					}
					if (rowCtn.trim().equals("组件")) {
						cotElements.setEleFlag(3l);
					}
				} else {
					cotElements.setEleFlag(0l);
				}
			}
			if (headCtn.getContents().equals("ELE_PARENT") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				Integer flag = this.getReportDao().isExistEleId(
						rowCtn.trim().toLowerCase());
				if (flag == null) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Parent Article No. does not exist!");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					cotElements.setEleParentId(flag);
					cotElements.setEleParent(rowCtn.trim());
					cotElements.setEleFlag(2l);
					isChild = true;
				}
			}
			if (headCtn.getContents().equals("FACTORY_NO") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setFactoryNo(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_DESC") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleDesc(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FROM") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleFrom(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FACTORY") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleFactory(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_COL") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleCol(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BARCODE") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if(rowCtn.trim().length()>30){
					cotElements.setBarcode(rowCtn.trim().substring(0,30));
				}else{
					cotElements.setBarcode(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_COMPOSETYPE")
					&& rowCtn != null && !rowCtn.trim().equals("")) {
				cotElements.setEleComposeType(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_UNIT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setEleUnit(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_GRADE") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setEleGrade(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_FOR_PERSON")
					&& rowCtn != null && !rowCtn.trim().equals("")) {
				cotElements.setEleForPerson(rowCtn.trim());
			}
			if (headCtn.getContents().equals("ELE_PRO_TIME") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if (row.getType() == CellType.DATE) {
					DateCell dc = (DateCell) row;
					cotElements.setEleProTime(new java.sql.Date(dc.getDate()
							.getTime()));
				} else {
					try {
						Date date = (Date) sdf.parse(rowCtn.trim());
						cotElements.setEleProTime(new java.sql.Date(date
								.getTime()));
					} catch (ParseException e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Date format is incorrect");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("ELE_SIZE_DESC") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				eleSize = rowCtn.trim();
			}
			if (headCtn.getContents().equals("ELE_INCH_DESC") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				eleSizeInch = rowCtn.trim();
			}
			if (headCtn.getContents().equals("ELE_REMARK") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if(rowCtn.trim().length()>500){
					cotElements.setEleRemark(rowCtn.trim().substring(0, 500));
				}else{
					cotElements.setEleRemark(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("ELE_TYPENAME_LV1")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					//去掉厂家名称中的回车换行
					String t = rowCtn.trim().replaceAll("\r", "");
					String temp = t.replaceAll("\n", "");
					
					Integer typeId = mapTypeCn.get(temp.toLowerCase());
					if (typeId == null) {
						typeId = mapTypeEn.get(temp.toLowerCase());
					}
					if (typeId == null) {
						// 新增一条材质,中英文名相同
						CotTypeLv1 cotTypeLv1 = new CotTypeLv1();
						cotTypeLv1.setTypeName(temp);
						cotTypeLv1.setTypeEnName(temp);
						List<CotTypeLv1> listType = new ArrayList<CotTypeLv1>();
						listType.add(cotTypeLv1);
						try {
							this.getReportDao().saveRecords(listType);
							typeId = cotTypeLv1.getId();
							mapTypeCn.put(temp.toLowerCase(),typeId);
						} catch (DAOException e) {
							e.printStackTrace();
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"保存样品材质异常!");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} 
					cotElements.setEleTypenameLv1(temp);
					cotElements.setEleTypeidLv1(typeId);
				}
			}

			if (headCtn.getContents().equals("ELE_TYPEID_LV2")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					Integer flag = this.getReportDao().isExistEleTypeName(
							rowCtn.trim());
					CotTypeLv2 cotTypeLv2 = null;
					if (flag == null) {
						cotTypeLv2 = new CotTypeLv2();
						cotTypeLv2.setTypeName(rowCtn.trim());
						try {
							this.getReportDao().create(cotTypeLv2);
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存产品分类异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						cotTypeLv2 = (CotTypeLv2) this.getReportDao().getById(
								CotTypeLv2.class, flag);
					}
					cotElements.setEleTypeidLv2(cotTypeLv2.getId());
				}
			}

			if (headCtn.getContents().equals("SHORT_NAME")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					//去掉厂家名称中的回车换行
					String t = rowCtn.trim().replaceAll("\r", "");
					String temp = t.replaceAll("\n", "");
					
					Integer factoryFlag = this.getReportDao()
							.isExistFactoryShortName(temp.toLowerCase());
					if (factoryFlag == null) {
						cotFactory.setFactoryName(temp);
						cotFactory.setShortName(temp);
						cotFactory.setFactroyTypeidLv1(1);
						try {
							CotSeqService cotSeqServiceImpl=new CotSeqServiceImpl();
							String facNo =cotSeqServiceImpl.getFacNo();
						//	String facNo = seq.getAllSeqByType("facNo", null);
							cotFactory.setFactoryNo(facNo);
							this.getReportDao().create(cotFactory);
							cotSeqServiceImpl.saveSeq("factoryNo");
						//	seq.saveSeq("facNo");
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Save Supplier exception");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotElements.setFactoryId(cotFactory.getId());
					} else {
						cotElements.setFactoryId(factoryFlag);
					}
				}

			}
			// -----------------------------包装信息
			try {
				if (headCtn.getContents().equals("ELE_MOD") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					Double elemod = Double.parseDouble(rowCtn.trim());
					cotElements.setEleMod(elemod.intValue());
				}
				if (headCtn.getContents().equals("ELE_UNITNUM")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					Double elemod = Double.parseDouble(rowCtn.trim());
					cotElements.setEleUnitNum(elemod.intValue());
				}

				if (headCtn.getContents().equals("cube") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setCube(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_L") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxL = Float.parseFloat(dfTwo.format(num));
					} else {
						boxL = num;
					}
					boxLInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					cotElements.setBoxL(boxL);
					cotElements.setBoxLInch(boxLInch);
				}
				if (headCtn.getContents().equals("BOX_L_INCH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxLInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxLInch = num;
					}
					boxL = Float.parseFloat(dfTwo.format(num * 2.54f));
					cotElements.setBoxL(boxL);
					cotElements.setBoxLInch(boxLInch);

				}
				if (headCtn.getContents().equals("BOX_W") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxW = Float.parseFloat(dfTwo.format(num));
					} else {
						boxW = num;
					}
					boxWInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					cotElements.setBoxW(boxW);
					cotElements.setBoxWInch(boxWInch);
				}
				if (headCtn.getContents().equals("BOX_W_INCH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxWInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxWInch = num;
					}
					boxW = Float.parseFloat(dfTwo.format(num * 2.54f));
					cotElements.setBoxW(boxW);
					cotElements.setBoxWInch(boxWInch);
				}
				if (headCtn.getContents().equals("BOX_H") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxH = Float.parseFloat(dfTwo.format(num));
					} else {
						boxH = num;
					}
					boxHInch = Float.parseFloat(dfTwo.format(num / 2.54f));
					cotElements.setBoxH(boxH);
					cotElements.setBoxHInch(boxHInch);
				}
				if (headCtn.getContents().equals("BOX_H_INCH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					isChangeL=true;
					float num = Float.parseFloat(rowCtn.trim());
					int temp = rowCtn.trim().lastIndexOf(".");
					if (temp > -1) {
						boxHInch = Float.parseFloat(dfTwo.format(num));
					} else {
						boxHInch = num;
					}
					boxH = Float.parseFloat(dfTwo.format(num * 2.54f));
					cotElements.setBoxH(boxH);
					cotElements.setBoxHInch(boxHInch);
				}
				if (headCtn.getContents().equals("BOX_HANDLEH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxHandleH(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_PB_L")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxPbL(Float.parseFloat(rowCtn.trim()));
					cotElements.setBoxPbLInch(Float.parseFloat(rowCtn
							.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_PB_W")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxPbW(Float.parseFloat(rowCtn.trim()));
					cotElements.setBoxPbWInch(Float.parseFloat(rowCtn
							.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_PB_H")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					cotElements.setBoxPbH(Float.parseFloat(rowCtn.trim()));
					cotElements.setBoxPbHInch(Float.parseFloat(rowCtn
							.trim()) / 2.54f);
				}
				if (headCtn.getContents().equals("BOX_PB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						cotElements
								.setBoxPbCount(elemod.longValue());
					}
				}
				
				if (headCtn.getContents().equals("BOX_IB_L") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxIbL(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxIbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_IB_W") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxIbW(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxIbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_IB_H") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxIbH(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxIbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_L") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxMbL(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxMbLInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_W") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxMbW(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxMbWInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_MB_H") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					cotElements.setBoxMbH(Float.parseFloat(rowCtn.trim()));
					cotElements
							.setBoxMbHInch(Float.parseFloat(rowCtn.trim()) / 2.54f);
				}

				if (headCtn.getContents().equals("BOX_OB_L") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					boxObL = Float.parseFloat(rowCtn.trim());
					cotElements.setBoxObL(boxObL);
					cotElements.setBoxObLInch(boxObL / 2.54f);

					float cbm = Float.parseFloat(df.format(boxObL * boxObW
							* boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					cotElements.setBoxCbm(cbm);
					cotElements.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						cotElements.setBox20Count(new Float(count20));
						cotElements.setBox40Count(new Float(count40));
						cotElements.setBox40hqCount(new Float(count40hq));
						cotElements.setBox45Count(new Float(count45));
					}

				}

				if (headCtn.getContents().equals("BOX_OB_W") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					boxObW = Float.parseFloat(rowCtn.trim());
					cotElements.setBoxObW(boxObW);
					cotElements.setBoxObWInch(boxObW / 2.54f);

					float cbm = Float.parseFloat(df.format(boxObL * boxObW
							* boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					cotElements.setBoxCbm(cbm);
					cotElements.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						cotElements.setBox20Count(new Float(count20));
						cotElements.setBox40Count(new Float(count40));
						cotElements.setBox40hqCount(new Float(count40hq));
						cotElements.setBox45Count(new Float(count45));
					}
				}

				if (headCtn.getContents().equals("BOX_OB_H") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					boxObH = Float.parseFloat(rowCtn.trim());
					cotElements.setBoxObH(boxObH);
					cotElements.setBoxObHInch(boxObH / 2.54f);

					float cbm = Float.parseFloat(df.format(boxObL * boxObW
							* boxObH * 0.000001F));
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					cotElements.setBoxCbm(cbm);
					cotElements.setBoxCuft(cuft);
					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						cotElements.setBox20Count(new Float(count20));
						cotElements.setBox40Count(new Float(count40));
						cotElements.setBox40hqCount(new Float(count40hq));
						cotElements.setBox45Count(new Float(count45));
					}
				}
				if (headCtn.getContents().equals("BOX_WEIGTH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					boxWeigth = Float.parseFloat(rowCtn.trim());
					cotElements.setBoxWeigth(Float.parseFloat(rowCtn.trim()));
				}
				if (headCtn.getContents().equals("BOX_GROSS_WEIGTH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					gWeigh = Float.parseFloat(rowCtn.trim());
				}
				if (headCtn.getContents().equals("BOX_NET_WEIGTH")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					nWeigh = Float.parseFloat(rowCtn.trim());
				}
				
				if (headCtn.getContents().equals("BOX_IB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						cotElements
								.setBoxIbCount(elemod.longValue());
					}
				}
				if (headCtn.getContents().equals("BOX_MB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						cotElements
								.setBoxMbCount(elemod.longValue());
					}
				}
				if (headCtn.getContents().equals("BOX_OB_COUNT")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						Double elemod = Double.parseDouble(rowCtn.trim());
						boxObCount = elemod.longValue();
						cotElements
								.setBoxObCount(boxObCount);

						float cbm = Float.parseFloat(df.format(boxObL * boxObW
								* boxObH * 0.000001F));
						// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装

						if (boxObCount != 0 && cbm != 0) {
							int count20 = (int) ((cubes[0] / cbm) * boxObCount);
							int count40 = (int) ((cubes[2] / cbm) * boxObCount);
							int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
							int count45 = (int) ((cubes[3] / cbm) * boxObCount);
							cotElements.setBox20Count(new Float(count20));
							cotElements.setBox40Count(new Float(count40));
							cotElements.setBox40hqCount(new Float(count40hq));
							cotElements.setBox45Count(new Float(count45));
						}
					}
				}

				if (headCtn.getContents().equals("LI_RUN") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					float priceProfit = Float.parseFloat(rowCtn.trim());
					cotElements.setLiRun(priceProfit);
				}

				if (headCtn.getContents().equals("TUI_LV") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					tuiLv = Float.parseFloat(rowCtn.trim());
					cotElements.setTuiLv(tuiLv);
					if (hsIdTemp != 0) {
						CotEleOther cotEleOther = (CotEleOther) this
								.getReportDao().getById(CotEleOther.class,
										hsIdTemp);
						cotEleOther.setTaxRate(tuiLv);
						List list = new ArrayList();
						list.add(cotEleOther);
						this.getReportDao().updateRecords(list);
					}
				}

				if (headCtn.getContents().equals("PRICE_FAC") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					priceFac = Float.parseFloat(rowCtn.trim());
					if(priceFac<0){
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0!");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if (headCtn.getContents().equals("PRICE_OUT") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					priceOut = Float.parseFloat(rowCtn.trim());
					if(priceOut<0){
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "Priced at not less than 0");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if (headCtn.getContents().equals("BOX_CBM") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					float cbm = Float.parseFloat(rowCtn.trim());
					float cuft = Float.parseFloat(df.format(cbm * 35.315f));
					cotElements.setBoxCbm(cbm);
					cotElements.setBoxCuft(cuft);

					// 外箱数不为0时计算装箱数,计算装箱数(20/40/40HQ/45的柜体积分别为24/54/68/86),计算公式=柜体积/cbm*外包装
					if (boxObCount != 0 && cbm != 0) {
						int count20 = (int) ((cubes[0] / cbm) * boxObCount);
						int count40 = (int) ((cubes[2] / cbm) * boxObCount);
						int count40hq = (int) ((cubes[1] / cbm) * boxObCount);
						int count45 = (int) ((cubes[3] / cbm) * boxObCount);
						cotElements.setBox20Count(new Float(count20));
						cotElements.setBox40Count(new Float(count40));
						cotElements.setBox40hqCount(new Float(count40hq));
						cotElements.setBox45Count(new Float(count45));
					}
				}
				if (headCtn.getContents().equals("BOX_TYPE_ID")) {
					if (rowCtn != null && !rowCtn.trim().equals("")) {
						String[] box = mapBoxTypeCn.get(rowCtn.trim().toLowerCase());
						if (box == null) {
							box = mapBoxTypeEn.get(rowCtn.trim().toLowerCase());
						}
						if (box == null) {
							// 新增一条包装类型,中英文名相同
							CotBoxType cotBoxType = new CotBoxType();
							cotBoxType.setTypeName(rowCtn.trim());
							cotBoxType.setTypeNameEn(rowCtn.trim());
							List<CotBoxType> listType = new ArrayList<CotBoxType>();
							listType.add(cotBoxType);
							try {
								this.getReportDao().saveRecords(listType);
								box = new String[5];
								box[0]=cotBoxType.getId().toString();
								box[1]=null;
								box[2]=null;
								box[3]=null;
								box[4]=null;
								mapBoxTypeCn.put(rowCtn.trim().toLowerCase(), box);
							} catch (DAOException e) {
								e.printStackTrace();
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"Save Packing Way exception!");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						} 
						if(box[0]!=null){
							cotElements.setBoxTypeId(Integer.parseInt(box[0]));
						}
						if(box[1]!=null){
							cotElements.setBoxIbTypeId(Integer.parseInt(box[1]));
						}
						if(box[2]!=null){
							cotElements.setBoxMbTypeId(Integer.parseInt(box[2]));
						}
						if(box[3]!=null){
							cotElements.setBoxObTypeId(Integer.parseInt(box[3]));
						}
						if(box[4]!=null){
							cotElements.setBoxPbTypeId(Integer.parseInt(box[4]));
						}
						
					}
				}
			} catch (Exception e) {
				CotMsgVO cotMsgVO = new CotMsgVO(i, j, "The number of cell values can only be!");
				msgList.add(cotMsgVO);
				isSuccess = false;
				break;
			}
			if (headCtn.getContents().equals("BOX_UINT") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setBoxUint(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_TDESC") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setBoxTDesc(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_BDESC") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setBoxBDesc(rowCtn.trim());
			}
			
			if (headCtn.getContents().equals("BOX_REMARK") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				cotElements.setBoxRemark(rowCtn.trim());
			}
			if (headCtn.getContents().equals("BOX_REMARK_CN")
					&& rowCtn != null && !rowCtn.trim().equals("")) {
				cotElements.setBoxRemarkCn(rowCtn.trim());
			}
			// ---------------------报价信息
			if (headCtn.getContents().equals("PRICE_UINT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 判断该币种是否存在
					Integer flag = this.getReportDao().isExistCurUnit(
							rowCtn.trim());
					if (flag == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Currency does not exist");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotElements.setPriceFacUint(flag);
					}
				}

			}

			if (headCtn.getContents().equals("PRICE_UNIT")) {
				if (rowCtn != null && !rowCtn.trim().equals("")) {
					// 判断该币种是否存在
					Integer flag = this.getReportDao().isExistCurUnit(
							rowCtn.trim());
					if (flag == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j,
								"Currency does not exist");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotElements.setPriceOutUint(flag);
					}
				}
			}
		}
		if (isSuccess == true) {
			try {
				// 判断是否是子货号
				if (isChild) {
					cotElements.setEleFlag(2l);
				}
				// 保存样品信息
				cotElements.setEleAddTime(now);
				// 设置中英文规格
				if (!eleSize.equals("")) {
					cotElements.setEleSizeDesc(eleSize);
				} else {
					if(isChangeL)
					cotElements.setEleSizeDesc(boxL + "*" + boxW + "*" + boxH);
				}
				if (!eleSizeInch.equals("")) {
					cotElements.setEleInchDesc(eleSizeInch);
				} else {
					if(isChangeL)
					cotElements.setEleInchDesc(boxLInch + "*" + boxWInch + "*"
							+ boxHInch);
				}
				// 设置毛净重
				if (gWeigh == 0) {
					Float cfgGross = 0f;
					if (cotEleCfg!=null && cotEleCfg.getGrossNum() != null) {
						cfgGross = cotEleCfg.getGrossNum();
					}
					float grossWeight = boxWeigth * boxObCount/1000 + cfgGross;
					cotElements.setBoxGrossWeigth(grossWeight);
				} else {
					cotElements.setBoxGrossWeigth(gWeigh);
				}
				if (nWeigh == 0) {
					float netWeight = boxWeigth * boxObCount/1000;
					cotElements.setBoxNetWeigth(netWeight);
				} else {
					cotElements.setBoxNetWeigth(nWeigh);
				}
				
				//计算包材价格
				cotElements=this.getReportDao().calPrice(boxPackMap, cotElements);
				
				//1.excel都没有厂价和外销价(新增时重新计算,覆盖时不计算)
				if(priceFac==null && priceOut==null){
					if(isCover){
						if(cotElements.getPriceFac()!=null){
							priceFac=cotElements.getPriceFac();
						}
						if(cotElements.getPriceOut()!=null){
							priceOut = cotElements.getPriceOut();
						}
					}else{
						priceFac=this.sumPriceFac(cotEleCfg.getExpessionFacIn(),cotElements);
						priceOut=this.sumPriceOut(cotEleCfg.getExpessionIn(),priceFac,cotElements);
					}
				}
				//2.excel有厂价没有外销价,只计算外销价
				if(priceFac!=null && priceOut==null){
					if(isCover){
						if(cotElements.getPriceOut()!=null){
							priceOut = cotElements.getPriceOut();
						}
					}else{
						priceOut=this.sumPriceOut(cotEleCfg.getExpessionIn(),priceFac,cotElements);
					}
				}
				//3.只有外销价
				if(priceFac==null && priceOut!=null){
					if(isCover){
						if(cotElements.getPriceFac()!=null){
							priceFac=cotElements.getPriceFac();
						}
					}else{
						priceFac=this.sumPriceFac(cotEleCfg.getExpessionFacIn(),cotElements);
					}
				}
				
				cotElements.setPriceFac(priceFac);
				cotElements.setPriceOut(priceOut);
				
				if (cotElements.getId() == null) {
					cotElements.setPicName("zwtp");
					this.getReportDao().create(cotElements);
					// 添加样品默认图片为("zwtp")
					CotElePic cotElePic = new CotElePic();
					cotElePic.setEleId(cotElements.getEleId());
					cotElePic.setFkId(cotElements.getId());
					cotElePic.setPicImg(zwtpImg);
					cotElePic.setPicName(cotElements.getEleId());
					cotElePic.setPicSize(new Integer(zwtpImg.length));
					List<CotElePic> imgList = new ArrayList<CotElePic>();
					imgList.add(cotElePic);
					opImgImpl.saveImg(imgList);
				} else {
					List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
					listTemp.add(cotElements);
					this.getReportDao().updateRecords(listTemp);
				}
			} catch (Exception e) {
				CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "Please check It exceeds the maximum length of!");
				msgList.add(cotMsgVO);
				isSuccess = false;
				return msgList;
			}
			successNum++;
		}
		if (!isDel) {
			// 增加影响行数
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(0);
			cotMsgVO.setSuccessNum(successNum);
			// 覆盖条数减去失败条数
			if (isCover) {
				coverNum = coverNum - msgList.size();
			}
			cotMsgVO.setCoverNum(coverNum);
			cotMsgVO.setFailNum(msgList.size());
			msgList.add(cotMsgVO);
			// 将错误信息存入系统日志
			this.saveErrorMsgToFile(msgList);
		}
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		return msgList;
	}
	
	//计算生产价
	public Float sumPriceFac(String expessionFacIn,CotElementsNew cotElements){
		DecimalFormat dfTwo = new DecimalFormat("#.00");
		if (expessionFacIn == null || expessionFacIn.trim().equals("")) {
			return 0f;
		}
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		Float elePrice = 0f;
		if(cotElements.getElePrice()!=null){
			elePrice=cotElements.getElePrice();
		}
		Float fitPrice = 0f;
		if(cotElements.getEleFittingPrice()!=null){
			fitPrice=cotElements.getEleFittingPrice();
		}
		Float packPrice = 0f;
		if(cotElements.getPackingPrice()!=null){
			packPrice=cotElements.getPackingPrice();
		}
		evaluator.putVariable("elePrice", elePrice.toString());
		evaluator.putVariable("eleFittingPrice", fitPrice.toString());
		evaluator.putVariable("packingPrice", packPrice.toString());
		try {
			String result = evaluator.evaluate(expessionFacIn);
			return Float.parseFloat(dfTwo.format(Float.parseFloat(result)));
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}
		
	}
	
	//计算外销价
	public Float sumPriceOut(String expessionIn,Float priceFac,CotElementsNew cotElements){
		DecimalFormat df = new DecimalFormat("#.00");
		if (expessionIn == null || expessionIn.trim().equals("")) {
			return 0f;
		}
		// 利润率
		Float lirun = 0f;
		if (cotElements.getLiRun() != null) {
			lirun = cotElements.getLiRun();
		}
		// 汇率
		Float rate = 0f;
		if (cotElements.getPriceOutUint() != null) {
			rate = this.getReportDao().getCurRate(
					cotElements.getPriceOutUint());
		}
		// 退税率
		Float tui = 0f;
		if (cotElements.getTuiLv() != null) {
			tui = cotElements.getTuiLv();
		}
		// CBM
		Float cb = 0f;
		if (cotElements.getBoxCbm() != null) {
			cb = cotElements.getBoxCbm();
		}
		// 外包装
		Long bc = 0l;
		if (cotElements.getBoxObCount() != null) {
			bc = cotElements.getBoxObCount();
		}
		
		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = new Evaluator();
		evaluator.putVariable("priceProfit", lirun.toString());
		evaluator.putVariable("priceFac", priceFac.toString());
		evaluator.putVariable("priceRate", rate.toString());
		evaluator.putVariable("tuiLv", tui.toString());
		evaluator.putVariable("cbm", cb.toString());
		evaluator.putVariable("boxObCount", bc.toString());
		Float res = null;
		try {
			String result = evaluator.evaluate(expessionIn);
			res = Float.parseFloat(df.format(Float.parseFloat(result)));
			return res;
		} catch (EvaluationException e) {
			e.printStackTrace();
			return 0f;
		}
	}
	

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

}
