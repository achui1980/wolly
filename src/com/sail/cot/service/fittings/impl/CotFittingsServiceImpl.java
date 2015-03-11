/**
 * 
 */
package com.sail.cot.service.fittings.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotFittings;
import com.sail.cot.domain.CotFittingsPic;
import com.sail.cot.domain.CotPriceOut;
import com.sail.cot.domain.CotTypeLv3;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.fittings.CotFittingsService;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.impl.CotElementsServiceImpl;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 配件管理模块
 */
public class CotFittingsServiceImpl implements CotFittingsService {
	private CotBaseDao fittingsDao;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();
//	private GenAllSeq seq = new GenAllSeq();

	private CotReportDao reportDao;

	private Logger logger = Log4WebUtil.getLogger(CotFittingsServiceImpl.class);

	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getFittingsDao().findRecords(queryInfo);
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public int getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getFittingsDao().getRecordsCount(queryInfo);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}

	// 保存明细
	public Boolean addList(List<?> details) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
		Integer eleId = 0;
		float allPrice = 0f;
		for (int i = 0; i < details.size(); i++) {
			CotEleFittings eleFittings = (CotEleFittings) details.get(i);
			eleId = eleFittings.getEleId();
			//TODO:为何要累加
			allPrice = eleFittings.getFitTotalPrice().floatValue();
		
			// 将成本价格累加到该货号上
			CotElementsNew elementsNew = (CotElementsNew) this.getFittingsDao()
					.getById(CotElementsNew.class, eleId);
			//elementsNew = (CotElementsNew)SystemUtil.deepClone(elementsNew);
			if (elementsNew != null) {
				//TODO:为什么要置为null
				elementsNew.setPicImg(null);
				elementsNew.setCotPictures(null);
				elementsNew.setCotFile(null);
				elementsNew.setChilds(null);
				elementsNew.setCotPriceFacs(null);
				elementsNew.setCotEleFittings(null);
				elementsNew.setCotElePrice(null);
				elementsNew.setCotElePacking(null);
				
				if (elementsNew.getEleFittingPrice() != null) {
					elementsNew.setEleFittingPrice(elementsNew
							.getEleFittingPrice()
							+ allPrice);
				} else {
					elementsNew.setEleFittingPrice(allPrice);
				}
				
				if(cotEleCfg.getExpessionFacIn()!=null){
					Evaluator evaluator = new Evaluator();
					// 定义FOB公式的变量
					if (elementsNew.getElePrice() == null) {
						evaluator.putVariable("elePrice", "0");
					} else {
						evaluator.putVariable("elePrice", elementsNew.getElePrice()
								.toString());
					}
					if (elementsNew.getEleFittingPrice() == null) {
						evaluator.putVariable("eleFittingPrice", "0");
					} else {
						evaluator.putVariable("eleFittingPrice", elementsNew
								.getEleFittingPrice().toString());
					}
					if (elementsNew.getPackingPrice() == null) {
						evaluator.putVariable("packingPrice", "0");
					} else {
						evaluator.putVariable("packingPrice", elementsNew.getPackingPrice()
								.toString());
					}
					// 根据公式算出RMB价格
					String result;
					try {
						result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
						if (elementsNew.getPriceFacUint() != null) {
							CotCurrency cur = (CotCurrency) this.getFittingsDao()
									.getById(CotCurrency.class,
											elementsNew.getPriceFacUint());
							Float fac = Float.parseFloat(result) / cur.getCurRate();
							elementsNew.setPriceFac(fac);
						}
					} catch (EvaluationException e) {
						e.printStackTrace();
					}				
				}
				listEle.add(elementsNew);				
			}
		}
		try {
			this.getFittingsDao().updateRecords(listEle);
			this.getFittingsDao().saveRecords(details);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 根据编号获得对象
	public CotFittings getFittingById(Integer id) {
		return (CotFittings) this.getFittingsDao().getById(CotFittings.class,
				id);
	}

	// 判断材料编号是否存在
	public boolean findIsExistFitNo(String fitNo, Integer eId) {
		List<Integer> res = new ArrayList<Integer>();
		res = this.getFittingsDao().find(
				"select c.id from CotFittings c where c.fitNo='" + fitNo + "'");
		if (res.size() != 1) {
			return false;
		} else {
			if (!res.get(0).toString().equals(eId.toString())) {
				return true;
			} else {
				return false;
			}
		}
	}

	// 保存
	public Integer saveFittings(CotFittings e, String picPath) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 样品编辑时间
		e.setAddTime(new Date(System.currentTimeMillis()));
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 保存样品
		List list = new ArrayList();
		list.add(e);

		if (e.getId() == null) {
			// 获得tomcat路径
			String classPath = CotFittingsServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			File picFile = new File(systemPath + picPath);
			FileInputStream in;
			byte[] b = new byte[(int) picFile.length()];
			try {
				in = new FileInputStream(picFile);

				while (in.read(b) != -1) {
				}
				in.close();
				if (!"common/images/zwtp.png".equals(picPath)) {
					// 删除上传的图片
					picFile.delete();
				}
			} catch (Exception e1) {
				logger.error("设置样品图片错误!");
			}
			this.getFittingsDao().saveOrUpdateRecords(list);
			// 添加图片
			CotOpImgService impOpService = (CotOpImgService) SystemUtil
					.getService("CotOpImgService");
			CotFittingsPic fittingsPic = new CotFittingsPic();
			fittingsPic.setEleId(e.getFitNo());
			fittingsPic.setFkId(e.getId());
			fittingsPic.setPicImg(b);
			fittingsPic.setPicSize(b.length);
			fittingsPic.setPicName(e.getFitNo());
			List imgList = new ArrayList();
			imgList.add(fittingsPic);
			impOpService.saveImg(imgList);
		} else {
			this.getFittingsDao().saveOrUpdateRecords(list);
		}
		return e.getId();
	}

	// 查询所有厂家
	public Map<?, ?> getFactoryNameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("factory");
		for (int i = 0; i < list.size(); i++) {
			CotFactory cotFactory = (CotFactory) list.get(i);
			map.put(cotFactory.getId().toString(), cotFactory.getShortName());
		}
		return map;
	}

	// 查询所有配件类型
	public Map<?, ?> getTypeLv3NameMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("typelv3");
		for (int i = 0; i < list.size(); i++) {
			CotTypeLv3 cotTypeLv3 = (CotTypeLv3) list.get(i);
			map.put(cotTypeLv3.getId().toString(), cotTypeLv3.getTypeName());
		}
		return map;
	}

	// 删除
	public Boolean deleteFittings(List<Integer> ids) {
		try {
			this.getFittingsDao().deleteRecordByIds(ids, "CotFittings");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 更改默认图片
	public void updateDefaultPic(String filePath, Integer fkId)
			throws Exception {
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotFittingsPic cotFittingsPic = impOpService.getFitPicImgById(fkId);
		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		cotFittingsPic.setPicImg(b);
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		List<CotFittingsPic> imgList = new ArrayList<CotFittingsPic>();
		imgList.add(cotFittingsPic);
		impOpService.modifyImg(imgList);
	}

	// 删除配件图片
	public boolean deletePicImg(Integer Id) {
		String classPath = CotElementsServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		String filePath = systemPath + "common/images/zwtp.png";
		CotFittingsPic cotFittingsPic = impOpService.getFitPicImgById(Id);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) (picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			cotFittingsPic.setPicImg(b);
			List<CotFittingsPic> imgList = new ArrayList<CotFittingsPic>();
			imgList.add(cotFittingsPic);
			impOpService.modifyImg(imgList);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除主样品图片错误!");
			return false;
		}
	}

	// 查找含有该配件的样品编号字符串
	public String getEleFitIds(Integer fitId) {
		String hql = "select obj.eleId from CotEleFittings obj where obj.fittingId="
				+ fitId;
		List list = this.getFittingsDao().find(hql);
		String ids = "";
		for (int i = 0; i < list.size(); i++) {
			Integer id = (Integer) list.get(i);
			ids += id + ",";
		}
		return ids;
	}

	// 同步更新样品档案中的单价,并更新总价
	public void updateEleFitting(Integer fitId, Double price) {
		String hql = "from CotEleFittings obj where obj.fittingId=" + fitId;
		List list = this.getFittingsDao().find(hql);
		List newList = new ArrayList();
		List eleList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			CotEleFittings eleFittings = (CotEleFittings) list.get(i);
			eleFittings.setFitPrice(price);
			double total = eleFittings.getFitCount()
					* eleFittings.getFitUsedCount() * price;
			eleFittings.setFitTotalPrice(total);
			newList.add(eleFittings);
			eleList.add(eleFittings.getEleId());
			
		}
		this.getFittingsDao().updateRecords(newList);
		List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
		//更新所有引用该配件的货号的生产价
		for (int i = 0; i < eleList.size(); i++) {
			CotElementsNew ele = this.getPriceFacByEleId((Integer)eleList.get(i));
			if(ele!=null){
				listEle.add(ele);
			}
		}
		this.getFittingsDao().updateRecords(listEle);
	}
	
	// 同步更新样品档案中的厂家
	public void updateEleFittingFac(Integer fitId, Integer facId) throws DAOException {
		String hql = "update CotEleFittings obj set obj.facId=:facId where obj.fittingId=:fittingId";
		Map map = new HashMap();
		map.put("facId", facId);
		map.put("fittingId", fitId);
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(hql);
		this.getFittingsDao().executeUpdate(queryInfo, map);
	}

	// 保存配件报价记录
	public boolean savePriceOut(CotPriceOut cotPriceOut, String addTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (addTime != null && !"".equals(addTime)) {
				cotPriceOut.setAddTime(new Date(sdf.parse(addTime).getTime()));// 报价时间
			}

			List<CotPriceOut> list = new ArrayList<CotPriceOut>();
			list.add(cotPriceOut);
			this.getFittingsDao().saveOrUpdateRecords(list);
			return true;
		} catch (Exception e) {
			logger.error("保存配件报价记录出错！");
			return false;
		}
	}
	
	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}
	
	// 根据样品编号计算生产价
	public CotElementsNew getPriceFacByEleId(Integer id) {
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 得到内存的币种
		List listCur = this.getDicList("currency");
		try {
			CotElementsNew elementsNew = (CotElementsNew) this.getFittingsDao()
					.getById(CotElementsNew.class, id);
			elementsNew.setPicImg(null);
			elementsNew.setCotPictures(null);
			elementsNew.setCotFile(null);
			elementsNew.setChilds(null);
			elementsNew.setCotPriceFacs(null);
			elementsNew.setCotEleFittings(null);
			elementsNew.setCotElePrice(null);
			elementsNew.setCotElePacking(null);
			// 计算出所有成本的RMB值
			// Double allRmbMoney = 0.0;
			BigDecimal allRmbMoney = new BigDecimal("0");
			String hql = "from CotEleFittings obj where obj.eleId=" + id;
			List list = this.getFittingsDao().find(hql);
			for (int i = 0; i < list.size(); i++) {
				CotEleFittings eleFittings = (CotEleFittings) list.get(i);
				Double mo = eleFittings.getFitTotalPrice();
				allRmbMoney = allRmbMoney.add(new BigDecimal(mo.toString()));
			}
			// 存储新的配件总和
			elementsNew.setEleFittingPrice(allRmbMoney.floatValue());

			Evaluator evaluator = new Evaluator();
			// 定义FOB公式的变量
			if (elementsNew.getEleFittingPrice() == null) {
				evaluator.putVariable("eleFittingPrice", "0");
			} else {
				evaluator
						.putVariable("eleFittingPrice", allRmbMoney.toString());
			}
			if (elementsNew.getElePrice() == null) {
				evaluator.putVariable("elePrice", "0");
			} else {
				evaluator.putVariable("elePrice", elementsNew.getElePrice()
						.toString());
			}
			if (elementsNew.getPackingPrice() == null) {
				evaluator.putVariable("packingPrice", "0");
			} else {
				evaluator.putVariable("packingPrice", elementsNew
						.getPackingPrice().toString());
			}
			// 根据公式算出RMB价格,样品必须要有生产币种
			float rate = 0f;
			String result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
			for (int j = 0; j < listCur.size(); j++) {
				CotCurrency cur = (CotCurrency) listCur.get(j);
				if (cur.getId().intValue() == elementsNew.getPriceFacUint()
						.intValue()) {
					rate = cur.getCurRate();
					break;
				}
			}
			Float fac = Float.parseFloat(result) / rate;
			elementsNew.setPriceFac(fac);

			return elementsNew;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据编号获得配件报价记录
	public CotPriceOut getCotPriceOutById(Integer id) {
		return (CotPriceOut) this.getFittingsDao().getById(CotPriceOut.class,
				id);
	}

	// 删除配件报价记录
	public Boolean deletePriceOuts(List<?> priceOutIds) {
		try {
			this.getFittingsDao().deleteRecordByIds(priceOutIds, "CotPriceOut");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除配件报价记录出错");
			return false;
		}
		return true;
	}

	// 根据文件路径导入
	public List<?> saveReport(String filename, Boolean isCover) {
		CotOpImgService opImgImpl = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();

		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		// 得到暂无图片的字节
		byte[] zwtpImg = this.getReportDao().getZwtpPic();
		// 查询所有配件编号
		Map<String, Integer> mapFitNo = this.getReportDao().getAllFitNo();

		// 查询所有厂家简称
		Map<String, Integer> mapShortName = this.getReportDao()
				.getAllFactoryShortName();
		// 查询所有配件类别
		Map<String, Integer> mapTypeName = this.getReportDao().getTypeLv3Name();
		WebContext ctx = WebContextFactory.get();
		try {
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setEncoding("ISO-8859-1");
			setting.setLocale(locale);
			// 获得tomcat路径
			String classPath = CotFittingsServiceImpl.class.getResource("/")
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
		if (sheet.getRows() > 2003) {
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(1);
			cotMsgVO.setMsg("导入失败！一次最多只能导入2000条样品！");
			msgList.add(cotMsgVO);
			return msgList;
		}
		for (int i = 4; i < sheet.getRows(); i++) {
			// 新建样品对象
			CotFittings cotFittings = new CotFittings();
			// 是否有成功标识
			boolean isSuccess = true;

			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();
				if (headCtn.getContents().equals("fit_no")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件编号不能为空");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					Object fitId = mapFitNo.get(rowCtn.trim().toLowerCase());
					if (fitId == null) {
						if (rowCtn.trim().getBytes().length > 50) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"配件编号长度不能大于50位");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setFitNo(rowCtn.trim());
					} else {
						// 根据上传策略判断是否覆盖已存在的记录
						if (isCover) {
							cotFittings = (CotFittings) this
									.getReportDao()
									.getById(CotFittings.class, (Integer) fitId);
							coverNum++;
						} else {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件编号已经存在");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("fit_name")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件名称必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"配件名称长度不能大于100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setFitName(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("fac_id")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "供应商必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"供应商长度不能大于100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						Object factoryId = mapShortName.get(rowCtn.trim()
								.toLowerCase());
						if (factoryId == null) {
							// 定义厂家
							CotFactory cotFactory = new CotFactory();
							cotFactory.setFactoryName(rowCtn.trim());
							cotFactory.setShortName(rowCtn.trim());
//							String facNo = seq.getAllSeqByType("facNo",
//									null);
							CotSeqService cotSeqService = new CotSeqServiceImpl();
							String facNo =cotSeqService.getFacNo();
							cotFactory.setFactroyTypeidLv1(2);
							cotFactory.setFactoryNo(facNo);
							try {
								this.getReportDao().create(cotFactory);
								// 将新类别添加到已有的map中
								mapShortName.put(rowCtn.trim().toLowerCase(),
										cotFactory.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"保存供应商异常");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						cotFittings.setFacId(mapShortName.get(rowCtn.trim()
								.toLowerCase()));
					}
				}
				if (headCtn.getContents().equals("fit_price")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购价格必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						try {
							double price = Double.parseDouble(rowCtn.trim());
							if (price > 100000) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j,
										"采购价格不能大于100000");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
							cotFittings.setFitPrice(price);
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购价格必须为数字");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("buy_unit")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购单位必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"采购单位长度不能大于100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setBuyUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("use_unit")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "领用单位必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"领用单位长度不能大于100");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setUseUnit(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("fit_trans")) {
					if (rowCtn == null || rowCtn.trim().equals("")) {
						if (cotFittings.getId() == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率必须填");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					} else {
						if (rowCtn.trim().getBytes().length > 1000000) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率只能小于1000000");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						try {
							cotFittings.setFitTrans(Double.parseDouble(rowCtn
									.trim()));
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率必须为数字");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
				}
				if (headCtn.getContents().equals("type_lv3_id")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().getBytes().length > 20) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "类别名称长度不超过20位");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						Object typeId = mapTypeName.get(rowCtn.trim()
								.toLowerCase());
						if (typeId == null) {
							// 定义厂家
							CotTypeLv3 typeLv3 = new CotTypeLv3();
							typeLv3.setTypeName(rowCtn.trim());
							try {
								this.getReportDao().create(typeLv3);
								// 将新类别添加到已有的map中
								mapTypeName.put(rowCtn.trim().toLowerCase(),
										typeLv3.getId());
							} catch (Exception e) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存类别错误");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							}
						}
						cotFittings.setTypeLv3Id(mapTypeName.get(rowCtn.trim()
								.toLowerCase()));
					}
				}
				if (headCtn.getContents().equals("fit_min_count")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					try {
						long min = Long.parseLong(rowCtn.trim());

						if (min > 99999) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"最小采购量最多只能5位整数");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setFitMinCount(min);
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "最小采购量必须为整数");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
				if (headCtn.getContents().equals("fit_desc") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					if (rowCtn.trim().getBytes().length > 500) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "规格描述长度不超过500");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotFittings.setFitDesc(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("fit_quality_stander")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "品质标准长度不超过100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotFittings.setFitQualityStander(rowCtn.trim());
					}
				}
				if (headCtn.getContents().equals("fit_remark")
						&& rowCtn != null && !rowCtn.trim().equals("")) {
					if (rowCtn.trim().getBytes().length > 500) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "备注长度不超过500");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotFittings.setFitRemark(rowCtn.trim());
					}
				}

			}
			if (isSuccess == true) {
				cotFittings.setAddTime(now);
				if (cotFittings.getId() == null) {
					this.getReportDao().create(cotFittings);
					// 添加样品默认图片为("zwtp")
					CotFittingsPic fittingsPic = new CotFittingsPic();
					fittingsPic.setEleId(cotFittings.getFitNo());
					fittingsPic.setFkId(cotFittings.getId());
					fittingsPic.setPicImg(zwtpImg);
					fittingsPic.setPicName(cotFittings.getFitNo());
					fittingsPic.setPicSize(new Integer(zwtpImg.length));
					List<CotFittingsPic> imgList = new ArrayList<CotFittingsPic>();
					imgList.add(fittingsPic);
					opImgImpl.saveImg(imgList);
				} else {
					List<CotFittings> listTemp = new ArrayList<CotFittings>();
					listTemp.add(cotFittings);
					this.getReportDao().updateRecords(listTemp);
					this.updateEleFitting(cotFittings.getId(), cotFittings
							.getFitPrice());
				}
				mapFitNo.put(cotFittings.getFitNo().toLowerCase(), cotFittings
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
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		return msgList;
	}

	// 根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum,
			Boolean isCover) {
		CotOpImgService opImgImpl = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		// 选取Excel文件
		Workbook workbook = null;
		try {
			// 获得tomcat路径
			String classPath = CotFittingsServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			// 设置本地时间格式
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			workbook = Workbook.getWorkbook(new File(systemPath + "upload/"
					+ filename),setting);
		} catch (Exception e) {
			return null;
		}
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int i = rowNum;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 当前时间
		Date now = new java.sql.Date(System.currentTimeMillis());
		// 得到暂无图片的字节
		byte[] zwtpImg = this.getReportDao().getZwtpPic();
		// 查询所有配件编号
		Map<String, Integer> mapFitNo = this.getReportDao().getAllFitNo();

		// 查询所有厂家简称
		Map<String, Integer> mapShortName = this.getReportDao()
				.getAllFactoryShortName();
		// 查询所有配件类别
		Map<String, Integer> mapTypeName = this.getReportDao().getTypeLv3Name();
		WebContext ctx = WebContextFactory.get();

		// 判断是否有删除最后一行
		boolean isDel = false;

		// 新建样品对象
		CotFittings cotFittings = new CotFittings();
		// 是否有成功标识
		boolean isSuccess = true;

		for (int j = 0; j < sheet.getColumns(); j++) {
			// 表头
			Cell headCtn = sheet.getCell(j, 1);
			Cell row = sheet.getCell(j, i);
			String rowCtn = row.getContents();
			if (headCtn.getContents().equals("fit_no")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件编号不能为空");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
				Object fitId = mapFitNo.get(rowCtn.trim().toLowerCase());
				if (fitId == null) {
					if (rowCtn.trim().getBytes().length > 50) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件编号长度不能大于50位");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotFittings.setFitNo(rowCtn.trim());
				} else {
					// 根据上传策略判断是否覆盖已存在的记录
					if (isCover) {
						cotFittings = (CotFittings) this.getReportDao()
								.getById(CotFittings.class, (Integer) fitId);
						coverNum++;
					} else {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件编号已经存在");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("fit_name")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件名称必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "配件名称长度不能大于100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotFittings.setFitName(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("fac_id")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "供应商必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "供应商长度不能大于100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					Object factoryId = mapShortName.get(rowCtn.trim()
							.toLowerCase());
					if (factoryId == null) {
						// 定义厂家
						CotFactory cotFactory = new CotFactory();
						cotFactory.setFactoryName(rowCtn.trim());
						cotFactory.setShortName(rowCtn.trim());
						try {
							this.getReportDao().create(cotFactory);
							// 将新类别添加到已有的map中
							mapShortName.put(rowCtn.trim().toLowerCase(),
									cotFactory.getId());
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存供应商异常");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					cotFittings.setFacId(mapShortName.get(rowCtn.trim()
							.toLowerCase()));
				}
			}
			if (headCtn.getContents().equals("fit_price")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购价格必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					try {
						double price = Double.parseDouble(rowCtn.trim());
						if (price > 100000) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j,
									"采购价格不能大于100000");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
						cotFittings.setFitPrice(price);
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购价格必须为数字");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("buy_unit")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购单位必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "采购单位长度不能大于100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotFittings.setBuyUnit(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("use_unit")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "领用单位必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "领用单位长度不能大于100");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotFittings.setUseUnit(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("fit_trans")) {
				if (rowCtn == null || rowCtn.trim().equals("")) {
					if (cotFittings.getId() == null) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率必须填");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				} else {
					if (rowCtn.trim().getBytes().length > 1000000) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率只能小于1000000");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					try {
						cotFittings.setFitTrans(Double.parseDouble(rowCtn
								.trim()));
					} catch (Exception e) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "换算率必须为数字");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("type_lv3_id") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if (rowCtn.trim().getBytes().length > 20) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "类别名称长度不超过20位");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					Object typeId = mapTypeName
							.get(rowCtn.trim().toLowerCase());
					if (typeId == null) {
						// 定义厂家
						CotTypeLv3 typeLv3 = new CotTypeLv3();
						typeLv3.setTypeName(rowCtn.trim());
						try {
							this.getReportDao().create(typeLv3);
							// 将新类别添加到已有的map中
							mapTypeName.put(rowCtn.trim().toLowerCase(),
									typeLv3.getId());
						} catch (Exception e) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, j, "保存类别错误");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}
					}
					cotFittings.setTypeLv3Id(mapTypeName.get(rowCtn.trim()
							.toLowerCase()));
				}
			}
			if (headCtn.getContents().equals("fit_min_count") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				try {
					long min = Long.parseLong(rowCtn.trim());

					if (min > 99999) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, j, "最小采购量最多只能5位整数");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
					cotFittings.setFitMinCount(min);
				} catch (Exception e) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "最小采购量必须为整数");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}
			}
			if (headCtn.getContents().equals("fit_desc") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if (rowCtn.trim().getBytes().length > 500) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "规格描述长度不超过500");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					cotFittings.setFitDesc(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("fit_quality_stander")
					&& rowCtn != null && !rowCtn.trim().equals("")) {
				if (rowCtn.trim().getBytes().length > 100) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "品质标准长度不超过100");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					cotFittings.setFitQualityStander(rowCtn.trim());
				}
			}
			if (headCtn.getContents().equals("fit_remark") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if (rowCtn.trim().getBytes().length > 500) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, j, "备注长度不超过500");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					cotFittings.setFitRemark(rowCtn.trim());
				}
			}

		}
		if (isSuccess == true) {
			cotFittings.setAddTime(now);
			if (cotFittings.getId() == null) {
				this.getReportDao().create(cotFittings);
				// 添加样品默认图片为("zwtp")
				CotFittingsPic fittingsPic = new CotFittingsPic();
				fittingsPic.setEleId(cotFittings.getFitNo());
				fittingsPic.setFkId(cotFittings.getId());
				fittingsPic.setPicImg(zwtpImg);
				fittingsPic.setPicName(cotFittings.getFitNo());
				fittingsPic.setPicSize(new Integer(zwtpImg.length));
				List<CotFittingsPic> imgList = new ArrayList<CotFittingsPic>();
				imgList.add(fittingsPic);
				opImgImpl.saveImg(imgList);
			} else {
				List<CotFittings> listTemp = new ArrayList<CotFittings>();
				listTemp.add(cotFittings);
				this.getReportDao().updateRecords(listTemp);
				this.updateEleFitting(cotFittings.getId(), cotFittings
						.getFitPrice());
			}
			mapFitNo.put(cotFittings.getFitNo().toLowerCase(), cotFittings
					.getId());
			// 增加成功条数
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
		}
		// 获取系统常用数据字典
		SystemDicUtil dicUtil = new SystemDicUtil();
		Map map = dicUtil.getSysDicMap();
		ctx.getSession().setAttribute("sysdic", map);
		return msgList;
	}

	public CotBaseDao getFittingsDao() {
		return fittingsDao;
	}

	public void setFittingsDao(CotBaseDao fittingsDao) {
		this.fittingsDao = fittingsDao;
	}

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.fittings.CotFittingsService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		// TODO Auto-generated method stub
		return this.getReportDao().getJsonData(queryInfo);
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.fittings.CotFittingsService#findIsExistByFitNo(java.lang.String)
	 */
	public boolean findIsExistByFitNo(String fitNo) {
		List res = this.getFittingsDao().find(
				"select c.id from CotFittings c where c.fitNo='" + fitNo + "'");
		if(res.size() == 0)
			return false;
		else {
			return true;
		}
	}

}
