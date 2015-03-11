package com.sail.cot.service.sample.impl;

import java.io.File;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.dao.sample.CotReportDao;
import com.sail.cot.domain.CotBoxType;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotEleCfg;
import com.sail.cot.domain.CotEleFittings;
import com.sail.cot.domain.CotEleOther;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElePrice;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotTypeLv1;
import com.sail.cot.domain.CotTypeLv2;
import com.sail.cot.domain.vo.CotMsgVO;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotElePriceService;
import com.sail.cot.service.sample.CotReportService;
import com.sail.cot.util.SystemDicUtil;
import com.sail.cot.util.SystemUtil;

public class CotElePriceServiceImpl implements CotElePriceService {
	private CotBaseDao elePriceDao;
	private CotReportDao reportDao;
	private SystemDicUtil systemDicUtil = new SystemDicUtil();

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getElePriceDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getElePriceDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 得到objName的集合
	public List<?> getDicList(String objName) {
		return systemDicUtil.getDicListByName(objName);
	}

	// 根据币种换算价格
	public float updatePrice(Float price, Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId == null || newCurId == null) {
			return 0;
		}
		Float obj = 0f;
		Float rmb = 0f;
		List<?> listCur = this.getDicList("currency");
		// 取得厂家报价的人民币值
		for (int j = 0; j < listCur.size(); j++) {
			CotCurrency cur = (CotCurrency) listCur.get(j);
			if (cur.getId().intValue() == oldCurId.intValue()) {
				rmb = price * cur.getCurRate();
				break;
			}
		}
		for (int j = 0; j < listCur.size(); j++) {
			CotCurrency cur = (CotCurrency) listCur.get(j);
			if (cur.getId().intValue() == newCurId.intValue()) {
				obj = rmb / cur.getCurRate();
				break;
			}
		}
		DecimalFormat nbf = new DecimalFormat("0.000");
		obj = Float.parseFloat(nbf.format(obj));

		return obj;
	}

	// 保存,返回最新生产价
	public Float saveOrUpdateElePrice(CotElePrice elePrice, Double oldPrice,
			Integer oldCur) {
		try {
			CotCurrency currency = (CotCurrency) this.getElePriceDao().getById(
					CotCurrency.class, elePrice.getPriceUnit());
			Double temp = elePrice.getPriceAmount() * currency.getCurRate();
			// 该成本已存在时,计算出RMB差额
			if (elePrice.getId() != null) {
				CotCurrency cur = (CotCurrency) this.getElePriceDao().getById(
						CotCurrency.class, oldCur);
				Double ac = oldPrice * cur.getCurRate();
				temp = temp - ac;
			}
			// 将成本价格累加到该货号上
			CotElementsNew elementsNew = (CotElementsNew) this.getElePriceDao()
					.getById(CotElementsNew.class, elePrice.getEleId());
			Float fac = 0f;
			if (elementsNew != null) {
				elementsNew.setPicImg(null);
				elementsNew.setCotPictures(null);
				elementsNew.setCotFile(null);
				elementsNew.setChilds(null);
				elementsNew.setCotPriceFacs(null);
				elementsNew.setCotEleFittings(null);
				elementsNew.setCotElePrice(null);
				elementsNew.setCotElePacking(null);
				if (elementsNew.getElePrice() != null) {
					elementsNew.setElePrice(elementsNew.getElePrice()
							+ temp.floatValue());
				} else {
					elementsNew.setElePrice(temp.floatValue());
				}

				// 取得样品默认配置对象
				CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
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
					evaluator.putVariable("packingPrice", elementsNew
							.getPackingPrice().toString());
				}
				// 根据公式算出RMB价格
				String result = evaluator.evaluate(cotEleCfg
						.getExpessionFacIn());
				if (elementsNew.getPriceFacUint() != null) {
					CotCurrency cur = (CotCurrency) this.getElePriceDao()
							.getById(CotCurrency.class,
									elementsNew.getPriceFacUint());
					fac = Float.parseFloat(result) / cur.getCurRate();
					elementsNew.setPriceFac(fac);
				}

				List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
				listEle.add(elementsNew);
				this.getElePriceDao().updateRecords(listEle);
			}

			List<CotElePrice> list = new ArrayList<CotElePrice>();
			list.add(elePrice);

			this.getElePriceDao().saveOrUpdateRecords(list);
			return fac;
		} catch (Exception e) {
			e.printStackTrace();
			return -1f;
		}
	}

	// 删除,返回最新生产价
	public Float deleteElePrices(List<Integer> ids) {
		try {
			Integer eleId = 0;
			Double temp = 0.0;
			Float fac = 0f;
			// 去掉该货号中的RMB样品成本
			for (int i = 0; i < ids.size(); i++) {
				CotElePrice elePrice = (CotElePrice) this.getElePriceDao()
						.getById(CotElePrice.class, ids.get(i));
				CotCurrency currency = (CotCurrency) this.getElePriceDao()
						.getById(CotCurrency.class, elePrice.getPriceUnit());
				temp += elePrice.getPriceAmount() * currency.getCurRate();
				eleId = elePrice.getEleId();
			}
			// 将成本价格累加到该货号上
			CotElementsNew elementsNew = (CotElementsNew) this.getElePriceDao()
					.getById(CotElementsNew.class, eleId);
			if (elementsNew != null) {
				elementsNew.setPicImg(null);
				elementsNew.setCotPictures(null);
				elementsNew.setCotFile(null);
				elementsNew.setChilds(null);
				elementsNew.setCotPriceFacs(null);
				elementsNew.setCotEleFittings(null);
				elementsNew.setCotElePrice(null);
				elementsNew.setCotElePacking(null);

				if (elementsNew.getElePrice() != null) {
					if (elementsNew.getElePrice() - temp.floatValue() > 0) {
						elementsNew.setElePrice(elementsNew.getElePrice()
								- temp.floatValue());
					} else {
						elementsNew.setElePrice(0f);
					}
				}
				// 取得样品默认配置对象
				CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
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
					evaluator.putVariable("packingPrice", elementsNew
							.getPackingPrice().toString());
				}
				// 根据公式算出RMB价格
				String result = evaluator.evaluate(cotEleCfg
						.getExpessionFacIn());
				if (elementsNew.getPriceFacUint() != null) {
					CotCurrency cur = (CotCurrency) this.getElePriceDao()
							.getById(CotCurrency.class,
									elementsNew.getPriceFacUint());
					fac = Float.parseFloat(result) / cur.getCurRate();
					elementsNew.setPriceFac(fac);
				}

				List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
				listEle.add(elementsNew);
				this.getElePriceDao().updateRecords(listEle);
			}
			this.getElePriceDao().deleteRecordByIds(ids, "CotElePrice");
			return fac;
		} catch (Exception e) {
			e.printStackTrace();
			return -1f;
		}
	}

	// 查询所有币种
	public Map<?, ?> getCurrencyMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId().toString(), cotCurrency.getCurNameEn());
		}
		return map;
	}

	// 判断成本名称是否重复
	public boolean checkIsExist(String priceName, Integer id, Integer mainId) {
		String hql = "select obj.id from CotElePrice obj where obj.priceName='"
				+ priceName + "' and obj.eleId=" + mainId;
		List<?> res = this.getElePriceDao().find(hql);
		if (res.size() == 0) {
			return false;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.intValue() == id.intValue()) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	// 根据编号获得对象
	public CotElePrice getCotElePriceById(Integer id) {
		return (CotElePrice) this.getElePriceDao().getById(CotElePrice.class,
				id);
	}

	// 得到objName的集合
	public List<?> getList(String objName) {
		return this.getElePriceDao().getRecords(objName);
	}

	// 根据文件路径导入
	public List<?> saveReport(String filename, Boolean isCover) {
		// 选取Excel文件
		Workbook workbook = null;
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 查询所有样品编号
		Map<String, Integer> mapEleId = this.getReportDao().getAllParentEleId();

		// 新建map用于储存excel中的货号
		Map<Integer, Double> mapEId = new HashMap<Integer, Double>();

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
		if (sheet.getRows() > 2003) {
			CotMsgVO cotMsgVO = new CotMsgVO();
			cotMsgVO.setFlag(1);
			cotMsgVO.setMsg("导入失败！一次最多只能导入2000条样品！");
			msgList.add(cotMsgVO);
			return msgList;
		}
		for (int i = 3; i < sheet.getRows(); i++) {
			// 新建样品成本
			CotElePrice cotElePrice = new CotElePrice();
			// 是否有成功标识
			boolean isSuccess = true;
			for (int j = 0; j < sheet.getColumns(); j++) {
				// 表头
				Cell headCtn = sheet.getCell(j, 1);
				Cell row = sheet.getCell(j, i);
				String rowCtn = row.getContents();
				if (headCtn.getContents().equals("ele_id")) {
					// 验证货号
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "样品货号必须填写");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						if (rowCtn.trim().getBytes().length > 100) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, 0,
									"样品货号长度不能大于100位");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						} else {
							Object eId = mapEleId.get(rowCtn.trim()
									.toLowerCase());
							if (eId == null) {
								CotMsgVO cotMsgVO = new CotMsgVO(i, 0,
										"样品货号不存在");
								msgList.add(cotMsgVO);
								isSuccess = false;
								break;
							} else {
								cotElePrice.setEleId((Integer) eId);
							}
						}
					}
				}
				if (headCtn.getContents().equals("price_name")) {
					// 验证成本名称
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "成本名称必须填写");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						if (rowCtn.trim().getBytes().length > 50) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, 0,
									"成本名称长度不能大于50位");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						} else {
							cotElePrice.setPriceName(rowCtn.trim());
						}
					}
				}
				if (headCtn.getContents().equals("price_amount")) {
					// 验证价格
					if (rowCtn == null || rowCtn.trim().equals("")) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "价格必须填写");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						Double price = Double.parseDouble(rowCtn.trim());
						if (price > 0 && price < 1000) {
							cotElePrice.setPriceAmount(price);
							cotElePrice.setPriceUnit(cotEleCfg
									.getElePriceFacUnit());
						} else {
							CotMsgVO cotMsgVO = new CotMsgVO(i, 0,
									"价格必须大于0并且小于1000");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						}

					}
				}
				if (headCtn.getContents().equals("remark") && rowCtn != null
						&& !rowCtn.trim().equals("")) {
					if (rowCtn.trim().getBytes().length <= 500) {
						cotElePrice.setRemark(rowCtn.trim());
					} else {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "备注长度不能大于500位");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}

				}
			}
			if (isSuccess == true) {
				// 判断该名称在该货号是否重复
				if (cotElePrice.getPriceName() != null
						&& cotElePrice.getEleId() != null) {
					CotElePrice old = this.getReportDao().checkElePriceName(
							cotElePrice.getPriceName(), cotElePrice.getEleId());
					if (old != null) {
						if (!isCover) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, 1,
									"该货号已存在此成本名称");
							msgList.add(cotMsgVO);
							continue;
						} else {
							// 计算新旧rmb差额
							CotCurrency currency = (CotCurrency) this
									.getReportDao().getById(CotCurrency.class,
											cotElePrice.getPriceUnit());

							coverNum++;
							Double temp = cotElePrice.getPriceAmount()
									* currency.getCurRate();
							// 该成本已存在时,计算出RMB差额
							CotCurrency cur = (CotCurrency) this
									.getElePriceDao().getById(
											CotCurrency.class,
											old.getPriceUnit());
							Double ac = old.getPriceAmount() * cur.getCurRate();
							temp = temp - ac;

							// 累加每个货号的成本
							Double priceTemp = mapEId.get(cotElePrice
									.getEleId());
							if (priceTemp == null) {
								priceTemp = 0.0;
							}
							mapEId
									.put(cotElePrice.getEleId(), priceTemp
											+ temp);
							// 更新
							old.setPriceAmount(cotElePrice.getPriceAmount());
							old.setPriceUnit(cotElePrice.getPriceUnit());
							List list = new ArrayList();
							list.add(old);
							this.getReportDao().updateRecords(list);
						}
					} else {
						this.getReportDao().create(cotElePrice);
						// 增加成功条数
						successNum++;
						// 累加每个货号的成本
						Double priceTemp = mapEId.get(cotElePrice.getEleId());
						if (priceTemp == null) {
							priceTemp = 0.0;
						}
						CotCurrency currency = (CotCurrency) this
								.getReportDao().getById(CotCurrency.class,
										cotElePrice.getPriceUnit());
						mapEId.put(cotElePrice.getEleId(), priceTemp
								+ cotElePrice.getPriceAmount()
								* currency.getCurRate());
					}
				}
			}
		}

		// 定义jeavl对象,用于计算字符串公式
		Evaluator evaluator = null;
		// 更新样品档案的成本字段
		Iterator<?> it = mapEId.keySet().iterator();
		while (it.hasNext()) {
			Integer eId = (Integer) it.next();
			CotElementsNew cotElements = (CotElementsNew) this.getReportDao()
					.getById(CotElementsNew.class, eId);
			cotElements = (CotElementsNew) SystemUtil.deepClone(cotElements);
			Float temp = cotElements.getElePrice();
			if (temp == null) {
				temp = 0f;
			}
			Double price = temp + mapEId.get(eId);
			cotElements.setElePrice(price.floatValue());
			// 根据总成本和总配件价格计算出厂价
			evaluator = new Evaluator();
			// 定义FOB公式的变量
			evaluator.putVariable("elePrice", cotElements.getElePrice()
					.toString());
			if (cotElements.getEleFittingPrice() == null) {
				evaluator.putVariable("eleFittingPrice", "0");
			} else {
				evaluator.putVariable("eleFittingPrice", cotElements
						.getEleFittingPrice().toString());
			}
			if (cotElements.getPackingPrice() == null) {
				evaluator.putVariable("packingPrice", "0");
			} else {
				evaluator.putVariable("packingPrice", cotElements
						.getPackingPrice().toString());
			}
			try {
				if (cotEleCfg.getExpessionFacIn() != null) {
					// 根据公式算出RMB价格
					String result = evaluator.evaluate(cotEleCfg
							.getExpessionFacIn());
					if (cotElements.getPriceFacUint() != null) {
						CotCurrency cur = (CotCurrency) this.getElePriceDao()
								.getById(CotCurrency.class,
										cotElements.getPriceFacUint());
						Float fac = Float.parseFloat(result) / cur.getCurRate();
						DecimalFormat sf = new DecimalFormat("000.000");
						String facString = sf.format(fac);
						fac = new Float(facString);
						System.out.println(facString.toString());
						cotElements.setPriceFac(fac);
					}
				}
			} catch (EvaluationException e) {
				e.printStackTrace();
			}
			List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
			listTemp.add(cotElements);
			this.getReportDao().updateRecords(listTemp);
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
		return msgList;
	}

	// 根据文件路径和行号导入excel
	public List<?> updateOneReport(String filename, Integer rowNum,
			Boolean isCover) {
		// 选取Excel文件
		Workbook workbook = null;
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
		// 定义成功条数
		int successNum = 0;
		// 定义覆盖条数
		int coverNum = 0;
		List<CotMsgVO> msgList = new ArrayList<CotMsgVO>();
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 查询所有样品编号
		Map<String, Integer> mapEleId = this.getReportDao().getAllParentEleId();

		// 新建样品成本
		CotElePrice cotElePrice = new CotElePrice();
		// 是否有成功标识
		boolean isSuccess = true;
		// 判断是否有删除最后一行
		boolean isDel = false;

		for (int j = 0; j < sheet.getColumns(); j++) {
			// 表头
			Cell headCtn = sheet.getCell(j, 1);
			Cell row = sheet.getCell(j, i);
			String rowCtn = row.getContents();
			if (headCtn.getContents().equals("ele_id")) {
				// 验证货号
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "样品货号必须填写");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					if (rowCtn.trim().getBytes().length > 100) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "样品货号长度不能大于100位");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						Object eId = mapEleId.get(rowCtn.trim().toLowerCase());
						if (eId == null) {
							CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "样品货号不存在");
							msgList.add(cotMsgVO);
							isSuccess = false;
							break;
						} else {
							cotElePrice.setEleId((Integer) eId);
						}
					}
				}
			}
			if (headCtn.getContents().equals("price_name")) {
				// 验证成本名称
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "成本名称必须填写");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					if (rowCtn.trim().getBytes().length > 50) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "成本名称长度不能大于50位");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					} else {
						cotElePrice.setPriceName(rowCtn.trim());
					}
				}
			}
			if (headCtn.getContents().equals("price_amount")) {

				// 验证价格
				if (rowCtn == null || rowCtn.trim().equals("")) {
					CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "价格必须填写");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				} else {
					Double price = Double.parseDouble(rowCtn.trim());
					if (price > 0 && price < 1000) {
						cotElePrice.setPriceAmount(price);
						cotElePrice
								.setPriceUnit(cotEleCfg.getElePriceFacUnit());
					} else {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 0,
								"价格必须大于0并且小于1000");
						msgList.add(cotMsgVO);
						isSuccess = false;
						break;
					}
				}
			}
			if (headCtn.getContents().equals("remark") && rowCtn != null
					&& !rowCtn.trim().equals("")) {
				if (rowCtn.trim().getBytes().length <= 500) {
					cotElePrice.setRemark(rowCtn.trim());
				} else {
					CotMsgVO cotMsgVO = new CotMsgVO(i, 0, "备注长度不能大于500位");
					msgList.add(cotMsgVO);
					isSuccess = false;
					break;
				}

			}
		}
		if (isSuccess == true) {
			// 判断该名称在该货号是否重复
			if (cotElePrice.getPriceName() != null
					&& cotElePrice.getEleId() != null) {
				CotElePrice old = this.getReportDao().checkElePriceName(
						cotElePrice.getPriceName(), cotElePrice.getEleId());
				CotCurrency currency = (CotCurrency) this.getReportDao()
						.getById(CotCurrency.class, cotElePrice.getPriceUnit());
				Double temp = cotElePrice.getPriceAmount()
						* currency.getCurRate();
				if (old != null) {
					if (!isCover) {
						CotMsgVO cotMsgVO = new CotMsgVO(i, 1, "该货号已存在此成本名称");
						msgList.add(cotMsgVO);
					} else {
						// 计算新旧rmb差额
						coverNum++;
						// 该成本已存在时,计算出RMB差额
						CotCurrency cur = (CotCurrency) this.getElePriceDao()
								.getById(CotCurrency.class, old.getPriceUnit());
						Double ac = old.getPriceAmount() * cur.getCurRate();
						temp = temp - ac;
						// 更新
						old.setPriceAmount(cotElePrice.getPriceAmount());
						old.setPriceUnit(cotElePrice.getPriceUnit());
						List list = new ArrayList();
						list.add(old);
						this.getReportDao().updateRecords(list);
					}
				} else {
					this.getReportDao().create(cotElePrice);
					// 增加成功条数
					successNum++;
				}

				// 定义jeavl对象,用于计算字符串公式
				Evaluator evaluator = null;
				CotElementsNew cotElements = (CotElementsNew) this
						.getReportDao().getById(CotElementsNew.class,
								cotElePrice.getEleId());
				cotElements = (CotElementsNew) SystemUtil
						.deepClone(cotElements);
				Float tempEle = cotElements.getElePrice();
				if (tempEle == null) {
					tempEle = 0f;
				}
				Double price = tempEle + temp;
				cotElements.setElePrice(price.floatValue());
				// 根据总成本和总配件价格计算出厂价
				evaluator = new Evaluator();
				// 定义FOB公式的变量
				evaluator.putVariable("elePrice", cotElements.getElePrice()
						.toString());
				if (cotElements.getEleFittingPrice() == null) {
					evaluator.putVariable("eleFittingPrice", "0");
				} else {
					evaluator.putVariable("eleFittingPrice", cotElements
							.getEleFittingPrice().toString());
				}
				if (cotElements.getPackingPrice() == null) {
					evaluator.putVariable("packingPrice", "0");
				} else {
					evaluator.putVariable("packingPrice", cotElements
							.getPackingPrice().toString());
				}
				try {
					if (cotEleCfg.getExpessionFacIn() != null) {
						// 根据公式算出RMB价格
						String result = evaluator.evaluate(cotEleCfg
								.getExpessionFacIn());
						if (cotElements.getPriceFacUint() != null) {
							CotCurrency cur = (CotCurrency) this
									.getElePriceDao().getById(
											CotCurrency.class,
											cotElements.getPriceFacUint());
							Float fac = Float.parseFloat(result)
									/ cur.getCurRate();
							cotElements.setPriceFac(fac);
						}
					}
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
				List<CotElementsNew> listTemp = new ArrayList<CotElementsNew>();
				listTemp.add(cotElements);
				this.getReportDao().updateRecords(listTemp);
			}
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
		return msgList;
	}

	// 保存
	public void addList(List<?> list) {
		try {
			this.getElePriceDao().saveRecords(list);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	// 修改
	public void updateList(List<?> list, Map<Integer, String[]> map) {
		try {
			this.getElePriceDao().updateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void deleteList(List<Integer> ids) {
		try {
			this.getElePriceDao().deleteRecordByIds(ids, "CotElePrice");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 根据样品编号计算生产价 先根据新的成本总和加上样品中的配件总和
	public Float[] modifyPriceFacByEleId(Integer id) {
		Float[] temp = new Float[3];
		// 取得样品默认配置对象
		CotEleCfg cotEleCfg = this.getReportDao().getDefaultEleCfg();
		// 得到内存的币种
		List listCur = this.getDicList("currency");

		CotElementsNew elementsNew = (CotElementsNew) this.getElePriceDao().getById(CotElementsNew.class, id);
		elementsNew.setPicImg(null);
		elementsNew.setCotPictures(null);
		elementsNew.setCotFile(null);
		elementsNew.setChilds(null);
		elementsNew.setCotPriceFacs(null);
		elementsNew.setCotEleFittings(null);
		elementsNew.setCotElePrice(null);
		elementsNew.setCotElePacking(null);
		// 计算出所有成本的RMB值
		Double allRmbMoney = 0.0;
		String hql = "from CotElePrice obj where obj.eleId=" + id;
		List list = this.getElePriceDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotElePrice cotElePrice = (CotElePrice) list.get(i);
			Double mo = cotElePrice.getPriceAmount();
			allRmbMoney += mo;
		}
		// 存储新的配件总和
		elementsNew.setElePrice(allRmbMoney.floatValue());
		temp[0] = allRmbMoney.floatValue();
		if (elementsNew.getEleFittingPrice() == null) {
			temp[1] = 0f;
		} else {
			temp[1] = elementsNew.getEleFittingPrice();
		}
		if (elementsNew.getPriceFac() == null) {
			temp[2] = 0f;
		} else {
			temp[2] = elementsNew.getPriceFac();
		}
		if (cotEleCfg.getExpessionFacIn() != null) {
			String result = null;
			try {
				Evaluator evaluator = new Evaluator();
				// 定义FOB公式的变量
				if (elementsNew.getEleFittingPrice() == null) {
					evaluator.putVariable("eleFittingPrice", "0");
				} else {
					evaluator.putVariable("eleFittingPrice", elementsNew.getEleFittingPrice().toString());
				}
				if (elementsNew.getElePrice() == null) {
					evaluator.putVariable("elePrice", "0");
				} else {
					evaluator.putVariable("elePrice",allRmbMoney.toString());
				}
				if (elementsNew.getPackingPrice() == null) {
					evaluator.putVariable("packingPrice", "0");
				} else {
					evaluator.putVariable("packingPrice", elementsNew.getPackingPrice().toString());
				}
				result = evaluator.evaluate(cotEleCfg.getExpessionFacIn());
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
			if(result!=null){
				// 根据公式算出RMB价格,样品必须要有生产币种
				float rate = 0f;
				for (int j = 0; j < listCur.size(); j++) {
					CotCurrency cur = (CotCurrency) listCur.get(j);
					if (cur.getId().intValue() == elementsNew.getPriceFacUint().intValue()) {
						rate = cur.getCurRate();
						break;
					}
				}
				Float fac = Float.parseFloat(result) / rate;
				elementsNew.setPriceFac(fac);
				temp[2] = fac;
			}
		}

		List<CotElementsNew> listEle = new ArrayList<CotElementsNew>();
		listEle.add(elementsNew);
		this.getElePriceDao().updateRecords(listEle);
		return temp;

	}

	public CotBaseDao getElePriceDao() {
		return elePriceDao;
	}

	public void setElePriceDao(CotBaseDao elePriceDao) {
		this.elePriceDao = elePriceDao;
	}

	public CotReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(CotReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.sample.CotElePriceService#getJsonData(com.sail.cot.query.QueryInfo)
	 */
	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		
		return this.getElePriceDao().getJsonData(queryInfo);
	}

}
