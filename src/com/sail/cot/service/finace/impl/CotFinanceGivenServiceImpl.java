/**
 * 
 */
package com.sail.cot.service.finace.impl;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotCustomer;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotFinaceAccountdeal;
import com.sail.cot.domain.CotFinaceOther;
import com.sail.cot.domain.CotFinacegiven;
import com.sail.cot.domain.CotFinacegivenDetail;
import com.sail.cot.domain.CotPayType;
import com.sail.cot.query.QueryInfo;

import com.sail.cot.service.finace.CotFinanceGivenService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.Log4WebUtil;
import com.sail.cot.util.SystemUtil;

/**
 * 财务管理模块 付款记录
 * 
 * @author qh-chzy
 * 
 */
public class CotFinanceGivenServiceImpl implements CotFinanceGivenService {

	private CotBaseDao financeGivenDao;
	private Logger logger = Log4WebUtil
			.getLogger(CotFinanceGivenServiceImpl.class);

	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getFinanceGivenDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 根据条件查询样品记录
	public List<?> getList(QueryInfo queryInfo) {
		try {
			return this.getFinanceGivenDao().findRecords(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("查询出错");
		}
		return null;
	}

	// 保存
	public Boolean addList(List<?> details) {
		try {
			this.getFinanceGivenDao().saveRecords(details);
			return true;
		} catch (DAOException e) {
			logger.error("保存冲帐明细出错!");
			return false;
		}
	}

	// 根据币种换算价格
	public Double updatePrice(HttpServletRequest request, Double price,
			Integer oldCurId, Integer newCurId) {
		if (price == null || oldCurId == null || newCurId == null) {
			return 0.0;
		}
		Double obj = 0.0;
		Double rmb = 0.0;

		Map dicMap = (Map) request.getSession().getAttribute("sysdic");
		if (dicMap == null) {
			return 0.0;
		}
		List<?> listCur = (List) dicMap.get("currency");
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
		obj = Double.parseDouble(nbf.format(obj));

		return obj;
	}

	// 保存
	public Boolean addRecvList(HttpServletRequest request, List<?> details) {
		// DecimalFormat df = new DecimalFormat("#.000");
		List<CotFinaceAccountdeal> list = new ArrayList<CotFinaceAccountdeal>();
		for (int i = 0; i < details.size(); i++) {
			CotFinacegivenDetail recv = (CotFinacegivenDetail) details.get(i);
			CotFinaceAccountdeal ac = (CotFinaceAccountdeal) this
					.getFinanceGivenDao().getById(CotFinaceAccountdeal.class,
							recv.getDealId());
			// 币种转换
			double mon = this.updatePrice(request, recv.getCurrentAmount(),
					recv.getCurrencyId(), ac.getCurrencyId());
			ac.setRealAmount(ac.getRealAmount() + mon);
			ac.setRemainAmount(ac.getRemainAmount() - mon);

			if (ac.getZhRemainAmount() == null) {
				ac.setZhRemainAmount(0.0);
			}

			// 如果是预付货款,则未流转金额要累加上去
			if (recv.getFinaceName().equals("预付货款")) {
				ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
			} else {
				ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
			}

			// 如果未流转金额为0,状态改为1
			if (ac.getZhRemainAmount() == 0) {
				ac.setStatus(1);
			}
			list.add(ac);
		}

		try {
			this.getFinanceGivenDao().saveRecords(details);
			this.getFinanceGivenDao().updateRecords(list);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("保存冲帐明细出错!");
			return false;
		}
	}

	// 更新
	public Boolean updateList(HttpServletRequest request, List<?> details,
			Map<Integer, Double> map) {
		try {
			List<CotFinaceAccountdeal> accountList = new ArrayList<CotFinaceAccountdeal>();
			// 先算出本次冲帐的差额,再修改应付帐款的已冲和剩余金额,对应修改状态
			for (int i = 0; i < details.size(); i++) {
				CotFinacegivenDetail finacerecvDetail = (CotFinacegivenDetail) details
						.get(i);
				Double newCur = finacerecvDetail.getCurrentAmount();
				Double oldCur = map.get(finacerecvDetail.getId());
				Double chaCur = newCur - oldCur;

				// 将货款转回应付帐
				CotFinaceAccountdeal ac = (CotFinaceAccountdeal) this
						.getFinanceGivenDao().getById(CotFinaceAccountdeal.class,
								finacerecvDetail.getDealId());
				// 币种转换
				double mon = this.updatePrice(request, chaCur, finacerecvDetail
						.getCurrencyId(), ac.getCurrencyId());
				double acCha = ac.getRemainAmount() - mon;
				if (acCha >= 0) {
					ac.setRealAmount(ac.getRealAmount() + mon);
					ac.setRemainAmount(ac.getRemainAmount() - mon);
					if (acCha == 0) {
						ac.setStatus(1);
					} else {
						ac.setStatus(0);
					}
					accountList.add(ac);
					finacerecvDetail.setRealAmount(finacerecvDetail
							.getRealAmount()
							+ chaCur);
				}
			}
			this.getFinanceGivenDao().updateRecords(details);
			this.getFinanceGivenDao().updateRecords(accountList);
		} catch (Exception e) {
			logger.error("更新其他费用异常", e);
		}
		return true;
	}

	// 删除
	public Boolean deleteList(HttpServletRequest request, List<?> details) {
		List<Integer> ids = new ArrayList<Integer>();
		List<CotFinaceAccountdeal> accountList = new ArrayList<CotFinaceAccountdeal>();
		Integer mainId = 0;
		float allAmou = 0f;
		for (int i = 0; i < details.size(); i++) {
			CotFinacegivenDetail finacerecvDetail = (CotFinacegivenDetail) this
					.getFinanceGivenDao().getById(CotFinacegivenDetail.class,
							(Integer) details.get(i));

			mainId = finacerecvDetail.getFinaceGivenid();
			allAmou += finacerecvDetail.getCurrentAmount();
			ids.add(finacerecvDetail.getId());
			// 将货款转回应付帐
			CotFinaceAccountdeal ac = (CotFinaceAccountdeal) this
					.getFinanceGivenDao().getById(CotFinaceAccountdeal.class,
							finacerecvDetail.getDealId());
			// 币种转换
			double mon = this.updatePrice(request, finacerecvDetail
					.getCurrentAmount(), finacerecvDetail.getCurrencyId(), ac
					.getCurrencyId());
			ac.setRealAmount(ac.getRealAmount() - mon);
			ac.setRemainAmount(ac.getRemainAmount() + mon);
			if (ac.getFinaceName().equals("预付货款")) {
				ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
			} else {
				ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
			}
			ac.setStatus(0);
			accountList.add(ac);
		}
		// 修改主单的未冲帐金额
		// List<CotFinacegiven> recvList = new ArrayList<CotFinacegiven>();
		// CotFinacegiven recv = (CotFinacegiven) this.getFinanceGivenDao().getById(
		// CotFinacegiven.class, mainId);
		// recv = (CotFinacegiven) SystemUtil.deepClone(recv);
		// recv.setRemainAmount(recv.getRemainAmount() + allAmou);
		// recvList.add(recv);

		// 修改溢付款的总额
		// String hql = "from CotFinaceOther obj where obj.finaceName='溢付款' and
		// obj.fkId="
		// + recv.getId() + " and obj.factoryId=" + recv.getCustId();
		// List list = this.getFinanceGivenDao().find(hql);
		// List listOther = new ArrayList();
		try {
			// if (list.size() > 0) {
			// CotFinaceOther finaceOther = (CotFinaceOther) list.get(0);
			// finaceOther.setAmount(finaceOther.getAmount() + allAmou);
			// listOther.add(finaceOther);
			// this.getFinanceGivenDao().updateRecords(listOther);
			// }
			this.getFinanceGivenDao().deleteRecordByIds(ids,
					"CotFinacegivenDetail");
			this.getFinanceGivenDao().updateRecords(accountList);
			// this.getFinanceGivenDao().updateRecords(recvList);
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
	}

	// 删除其他费用
	public Boolean deleteByIds(List<?> ids) {
		try {
			this.getFinanceGivenDao().deleteRecordByIds(ids, "CotFinaceOther");
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除其他费用异常", e);
			return false;
		}
		return true;
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

	// 查询所有员工
	public Map<?, ?> getEmpsMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getFinanceGivenDao().getRecords("CotEmps");
		for (int i = 0; i < list.size(); i++) {
			CotEmps cotEmps = (CotEmps) list.get(i);
			map.put(cotEmps.getId().toString(), cotEmps.getEmpsName());
		}
		return map;
	}

	// 查询所有客户
	public Map<?, ?> getCustNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getFinanceGivenDao().getRecords("CotCustomer");
		for (int i = 0; i < list.size(); i++) {
			CotCustomer cotCustomer = (CotCustomer) list.get(i);
			map.put(cotCustomer.getId().toString(), cotCustomer
					.getCustomerShortName());
		}
		return map;
	}

	// 查询付款方式
	public Map<?, ?> getPayTypeNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<?> list = this.getFinanceGivenDao().getRecords("CotPayType");
		for (int i = 0; i < list.size(); i++) {
			CotPayType payType = (CotPayType) list.get(i);
			map.put(payType.getId().toString(), payType.getPayName());
		}
		return map;
	}

	// 删除主单记录
	public int deleteFinacerecvs(List<Integer> list) {
		String ids = "";
		for (int i = 0; i < list.size(); i++) {
			ids += list.get(i) + ",";
		}
		WebContext ctx = WebContextFactory.get();
		String hql = "from CotFinacegivenDetail obj where obj.finaceGivenid in ("
				+ ids.substring(0, ids.length() - 1) + ")";
		List<?> detailList = this.getFinanceGivenDao().find(hql);
		List<CotFinaceAccountdeal> accountList = new ArrayList<CotFinaceAccountdeal>();
		int res = 0;
		try {
			for (int i = 0; i < detailList.size(); i++) {
				CotFinacegivenDetail finacerecvDetail = (CotFinacegivenDetail) detailList
						.get(i);
				// 将货款转回应付帐
				CotFinaceAccountdeal ac = (CotFinaceAccountdeal) this
						.getFinanceGivenDao().getById(CotFinaceAccountdeal.class,
								finacerecvDetail.getDealId());
				// 币种转换
				double mon = this.updatePrice(ctx.getHttpServletRequest(),
						finacerecvDetail.getCurrentAmount(), finacerecvDetail
								.getCurrencyId(), ac.getCurrencyId());
				// 如果含预付货款,判断是否已流转出金额,有不能删除
				if (ac.getFinaceName().equals("预付货款")) {
					// 查询该预付货款是否有流转记录
					String hqlLiu = "select obj.id from CotFinaceOther obj where source='orderRecv' and obj.outFlag="
							+ ac.getId();
					List listLiu = this.getFinanceGivenDao().find(hqlLiu);
					if (listLiu.size() == 0) {
						ac.setRealAmount(ac.getRealAmount() - mon);
						ac.setRemainAmount(ac.getRemainAmount() + mon);
						ac.setZhRemainAmount(ac.getZhRemainAmount() - mon);
						ac.setStatus(0);
						accountList.add(ac);
					}
				} else {
					ac.setRealAmount(ac.getRealAmount() - mon);
					ac.setRemainAmount(ac.getRemainAmount() + mon);
					ac.setZhRemainAmount(ac.getZhRemainAmount() + mon);
					ac.setStatus(0);
					accountList.add(ac);
				}

			}
			// 删除溢付款
			List listYi = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				// 先查询该单的溢付款
				String hqlYi = "select obj.id from CotFinaceOther obj where obj.fkId="
						+ list.get(i)
						+ " and obj.finaceName='溢付款' and obj.factoryId is not null";
				List temp = this.getFinanceGivenDao().find(hqlYi);
				if (temp.size() > 0) {
					listYi.add(temp.get(0));
				}
			}
			this.getFinanceGivenDao().deleteRecordByIds(list, "CotFinacegiven");
			this.getFinanceGivenDao().updateRecords(accountList);
			if (listYi.size() > 0) {
				this.getFinanceGivenDao().deleteRecordByIds(listYi,
						"CotFinaceOther");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error("删除付款记录异常", e);
			res = -1;
		}
		return res;
	}

	// 根据id获取付款记录信息
	public CotFinacegiven getFinacegivenById(Integer id) {
		CotFinacegiven temp = (CotFinacegiven) this.getFinanceGivenDao().getById(
				CotFinacegiven.class, id);
		temp.setCotFinacegivenDetails(null);
		CotFinacegiven clone = (CotFinacegiven) SystemUtil.deepClone(temp);
		return clone;

	}

	// 根据id获取冲帐记录信息
	public CotFinacegivenDetail getFinacegivenDetailById(Integer id) {
		return (CotFinacegivenDetail) this.getFinanceGivenDao().getById(
				CotFinacegivenDetail.class, id);

	}

	// 根据id获取应付帐记录
	public CotFinaceAccountdeal getRecvById(Integer id, Integer newCur) {
		CotFinaceAccountdeal ac = (CotFinaceAccountdeal) this
				.getFinanceGivenDao().getById(CotFinaceAccountdeal.class, id);
		CotFinaceAccountdeal acClone = (CotFinaceAccountdeal) SystemUtil
				.deepClone(ac);
		WebContext ctx = WebContextFactory.get();
		Double newAmount = this.updatePrice(ctx.getHttpServletRequest(),
				acClone.getAmount(), acClone.getCurrencyId(), newCur);
		acClone.setAmount(newAmount);
		return acClone;
	}

	// 查询所有币种
	public Map<Integer, CotCurrency> getCurrencyObjMap(
			HttpServletRequest request) {
		Map<Integer, CotCurrency> map = new HashMap<Integer, CotCurrency>();
		Map<?, ?> dicMap = (Map<?, ?>) request.getSession().getAttribute(
				"sysdic");
		List<?> list = (List<?>) dicMap.get("currency");
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			map.put(cotCurrency.getId(), cotCurrency);
		}
		return map;
	}

	// 保存或者更新付款记录
	public Integer saveOrUpdateRecv(CotFinacegiven cotFinacerecv,
			String finaceRecvDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cotFinacerecv.setAddTime(new Date(System.currentTimeMillis()));// 添加时间
			if (finaceRecvDate != null && !"".equals(finaceRecvDate)) {
				cotFinacerecv.setFinaceRecvDate(new Date(sdf.parse(
						finaceRecvDate).getTime()));// 下单时间
			}
			WebContext ctx = WebContextFactory.get();
			Integer empId = (Integer) ctx.getSession().getAttribute("empId");
			cotFinacerecv.setAddPerson(empId);
			if (cotFinacerecv.getRealAmount() == null) {
				cotFinacerecv.setRealAmount(0.0);
			}
			if (cotFinacerecv.getRemainAmount() == null) {
				cotFinacerecv.setRemainAmount(cotFinacerecv.getAmount());
			}
			// 已冲帐金额
			cotFinacerecv.setRealAmount(cotFinacerecv.getAmount()
					- cotFinacerecv.getRemainAmount());
			Integer temp = cotFinacerecv.getId();
			List<CotFinacegiven> list = new ArrayList<CotFinacegiven>();
			list.add(cotFinacerecv);

			this.getFinanceGivenDao().saveOrUpdateRecords(list);

			if (temp == null) {
				// 更新全局序列号
//				GenAllSeq genAllSeq = new GenAllSeq();
//				genAllSeq.saveSeq("finacegivenNo");
				CotSeqService cotSeqService=new CotSeqServiceImpl();
				cotSeqService.saveSeq("finacegiven");
			}

			return cotFinacerecv.getId();
		} catch (Exception e) {
			logger.error("保存付款记录出错！");
			return null;
		}
	}

	// 保存主单金额
	public boolean saveFinacerecvByMoney(String remainAmount, Integer id) {
		try {
			CotFinacegiven finacerecv = this.getFinacegivenById(id);
			finacerecv.setRemainAmount(Double.parseDouble(remainAmount));
			// 已冲帐金额
			finacerecv.setRealAmount(finacerecv.getAmount()
					- finacerecv.getRemainAmount());
			List<CotFinacegiven> listRecv = new ArrayList<CotFinacegiven>();
			listRecv.add(finacerecv);
			this.getFinanceGivenDao().updateRecords(listRecv);

			WebContext ctx = WebContextFactory.get();
			Integer empId = (Integer) ctx.getSession().getAttribute("empId");

			// 查询该付款记录是否已存在溢付款,没有的新建
			String hql = " from CotFinaceOther obj where obj.finaceName='溢付款' and obj.fkId="
					+ finacerecv.getId()
					+ " and obj.factoryId="
					+ finacerecv.getFactoryId();
			List list = this.getFinanceGivenDao().find(hql);
			List listNew = new ArrayList();
			CotFinaceOther finaceOther = null;
			if (list.size() == 0) {
				finaceOther = new CotFinaceOther();
				finaceOther.setAmount(finacerecv.getRemainAmount());
				finaceOther.setBusinessPerson(empId);
				finaceOther.setCurrencyId(finacerecv.getCurrencyId());
				finaceOther.setFactoryId(finacerecv.getFactoryId());
				finaceOther.setFinaceName("溢付款");
				finaceOther.setFlag("M");
				finaceOther.setOrderNo(finacerecv.getFinaceNo());
				finaceOther.setFkId(finacerecv.getId());
			} else {
				finaceOther = (CotFinaceOther) list.get(0);
				finaceOther.setAmount(finacerecv.getRemainAmount());
			}
			listNew.add(finaceOther);
			this.getFinanceGivenDao().saveOrUpdateRecords(listNew);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 生成单号
//	public String createFinaceNo(Integer factoryId) {
//		Map<String, Integer> idMap = new HashMap<String, Integer>();
//		idMap.put("CH", factoryId);
//		GenAllSeq seq = new GenAllSeq();
//		String finaceNo = seq.getAllSeqByType("finacegivenNo", idMap);
//		return finaceNo;
//	}

	// 查询付款单单号是否存在
	public Integer findIsExistFinaceNo(String finaceNo, String id) {
		String hql = "select obj.id from CotFinacegiven obj where obj.finaceNo='"
				+ finaceNo + "'";
		List<?> res = this.getFinanceGivenDao().find(hql);
		if (res.size() == 0) {
			return null;
		}
		if (res.size() == 1) {
			Integer oldId = (Integer) res.get(0);
			if (oldId.toString().equals(id)) {
				return null;
			} else {
				return 1;
			}
		}
		return 2;
	}

	// 生成溢收账
	public Double setYiMonry(Integer mainId) {
		CotFinacegiven finacerecv = this.getFinacegivenById(mainId);

		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		// 查询该付款记录是否已存在溢付款,没有的新建
		String hql = " from CotFinaceOther obj where obj.finaceName='溢付款' and obj.fkId="
				+ finacerecv.getId()
				+ " and obj.factoryId="
				+ finacerecv.getFactoryId();
		try {
			List list = this.getFinanceGivenDao().find(hql);
			List listNew = new ArrayList();
			CotFinaceOther finaceOther = null;
			if (list.size() == 0) {
				finaceOther = new CotFinaceOther();
				finaceOther.setAmount(finacerecv.getRemainAmount());
				finaceOther.setBusinessPerson(empId);
				finaceOther.setCurrencyId(finacerecv.getCurrencyId());
				finaceOther.setFactoryId(finacerecv.getFactoryId());
				finaceOther.setFinaceName("溢付款");
				finaceOther.setFlag("M");
				finaceOther.setOrderNo(finacerecv.getFinaceNo());
				finaceOther.setFkId(finacerecv.getId());
			} else {
				finaceOther = (CotFinaceOther) list.get(0);
				finaceOther.setAmount(finacerecv.getRemainAmount());
			}
			listNew.add(finaceOther);
			this.getFinanceGivenDao().saveOrUpdateRecords(listNew);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return finacerecv.getRemainAmount();
	}

	// flag=0为可以删除,1为溢付款转到出货,2为预付货款有金额流到出货
	public Integer findIsYiOut(Integer id, Integer factoryId) {
		// 先查询该单的溢付款
		String hql = "select obj.id from CotFinaceOther obj where obj.fkId="
				+ id + " and obj.finaceName='溢付款' and obj.factoryId=" + factoryId;
		List list = this.getFinanceGivenDao().find(hql);
		Integer flag = 0;
		if (list.size() > 0) {
			Integer yiId = (Integer) list.get(0);
			String hqlOut = "select obj.id from CotFinaceOther obj where obj.outFlag="
					+ yiId + " and obj.source='yiDeal'";
			List listOut = this.getFinanceGivenDao().find(hqlOut);
			if (listOut.size() > 0) {
				flag = 1;
			}
		}
		// 查询该单的冲帐明细是否含有预付货款,并且该预付货款有金额流到出货
		if (flag == 0) {
			// 先查询该单的冲帐明细是否有预付货款
			String sql = "select obj.dealId from CotFinacegivenDetail obj,CotFinacegiven p where obj.finaceGivenid=p.id and obj.finaceName='预付货款' and p.id="
					+ id;
			List listYu = this.getFinanceGivenDao().find(sql);
			if (listYu.size() > 0) {
				for (int i = 0; i < listYu.size(); i++) {
					Integer recvId = (Integer) listYu.get(i);
					// 查询该应付帐是否有金额流到出货
					String tempHql = "select obj.id from CotFinaceOther obj where obj.source='FacDeal' and obj.outFlag="
							+ recvId;
					List listOther = this.getFinanceGivenDao().find(tempHql);
					if (listOther.size() > 0) {
						flag = 2;
						break;
					}
				}
			}
		}
		return flag;
	}

	// 查询应付帐的未流转金额
	public Float getZhRemainAmountByRecv(Integer recvId) {
		CotFinaceAccountdeal finaceAccountrecv = (CotFinaceAccountdeal) this
				.getFinanceGivenDao().getById(CotFinaceAccountdeal.class, recvId);
		if (finaceAccountrecv != null) {
			Double d = finaceAccountrecv.getZhRemainAmount();
			return d.floatValue();
		} else {
			return -1f;
		}
	}


	public CotBaseDao getFinanceGivenDao() {
		return financeGivenDao;
	}

	public void setFinanceGivenDao(CotBaseDao financeGivenDao) {
		this.financeGivenDao = financeGivenDao;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getFinanceGivenDao().getJsonData(queryInfo);
	}

}
