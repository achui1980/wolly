package com.sail.cot.service.pan.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotContact;
import com.sail.cot.domain.CotCurrency;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotElementsNew;
import com.sail.cot.domain.CotFactory;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotPan;
import com.sail.cot.domain.CotPanContact;
import com.sail.cot.domain.CotPanDetail;
import com.sail.cot.domain.CotPanEle;
import com.sail.cot.domain.CotPanOtherPic;
import com.sail.cot.domain.CotPanPic;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.seq.Sequece;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.pan.CotPanService;
import com.sail.cot.service.sample.CotExportRptService;
import com.sail.cot.service.system.CotSeqService;
import com.sail.cot.service.system.impl.CotSeqServiceImpl;
import com.sail.cot.util.ContextUtil;
import com.sail.cot.util.KiCrypt;
import com.sail.cot.util.ListSort;
import com.sail.cot.util.SystemUtil;
import com.zhao.mail.SendMailService;
import com.zhao.mail.entity.MailCfg;
import com.zhao.mail.entity.MailPerson;
import com.zhao.mail.entity.imp.MailObjectDefault;
import com.zhao.mail.impl.SendMailDefault;

/**
 * *********************************************
 * 
 * @Copyright :(C),2008-2010
 * @CompanyName :厦门市旗航软件有限公司(www.xmqh.net)
 * @Version :1.0
 * @Date :2011-6-24
 * @author : azan
 * @class :CotPanServiceImpl.java
 * @Description :询盘
 */
public class CotPanServiceImpl implements CotPanService {
	private CotBaseDao baseDao;

	public CotBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(CotBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return getBaseDao().getJsonData(queryInfo);
	}

	public CotPan getPanById(Integer panId) {
		return (CotPan) this.getBaseDao().getById(CotPan.class, panId);
	}
	
	public CotFactory getFacById(Integer facId) {
		return (CotFactory) this.getBaseDao().getById(CotFactory.class, facId);
	}
	
	public CotContact getContactById(Integer conId) {
		return (CotContact) this.getBaseDao().getById(CotContact.class, conId);
	}

	public Integer getUsdId() {
		List list = this.getBaseDao().getRecords("CotCurrency");
		Integer usdId = null;
		for (int i = 0; i < list.size(); i++) {
			CotCurrency cotCurrency = (CotCurrency) list.get(i);
			if (cotCurrency.getCurNameEn() != null
					&& cotCurrency.getCurNameEn().equals("USD")) {
				usdId = cotCurrency.getId();
				break;
			}
		}
		return usdId;
	}

	public Integer findIsExistPanNo(String priceNo, String id) {
		String hql = "select obj.id from CotPan obj where obj.prsNo=?";
		List<?> res =this.getBaseDao().queryForLists(hql,new String[]{priceNo});
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

	public Integer saveOrUpdatePan(CotPan cotPan, String addTime)
			throws Exception {
		List list = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		cotPan.setAddTime(sdf.parse(addTime));
		Integer pId = cotPan.getId();
		list.add(cotPan);
		this.getBaseDao().saveOrUpdateRecords(list);
		// 保存单号
		if (pId == null) {
			CotSeqService cotSeqService = new CotSeqServiceImpl();
			cotSeqService.saveSeq("pan");
		}
		return cotPan.getId();
	}

	public CotElePic getElePicByFkId(Integer fkId) {
		String hql = "from CotElePic obj where obj.fkId=" + fkId;
		List list = this.getBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			return (CotElePic) list.get(0);
		}
		return null;
	}

	public CotElePic getPricePicByFkId(Integer fkId) {
		String hql = "from CotPricePic obj where obj.fkId=" + fkId;
		List list = this.getBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			CotElePic cotElePic = new CotElePic();
			CotPricePic cotPricePic = (CotPricePic) list.get(0);
			cotElePic.setPicImg(cotPricePic.getPicImg());
			cotElePic.setPicSize(cotPricePic.getPicSize());
			cotElePic.setPicName(cotPricePic.getPicName());
			return cotElePic;
		}
		return null;
	}

	public CotElePic getOrderPicByFkId(Integer fkId) {
		String hql = "from CotOrderPic obj where obj.fkId=" + fkId;
		List list = this.getBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			CotElePic cotElePic = new CotElePic();
			CotOrderPic cotOrderPic = (CotOrderPic) list.get(0);
			cotElePic.setPicImg(cotOrderPic.getPicImg());
			cotElePic.setPicSize(cotOrderPic.getPicSize());
			cotElePic.setPicName(cotOrderPic.getPicName());
			return cotElePic;
		}
		return null;
	}

	public CotElePic getOrderFacPicByFkId(Integer fkId) {
		String hql = "from CotOrderfacPic obj where obj.fkId=" + fkId;
		List list = this.getBaseDao().find(hql);
		if (list != null && list.size() > 0) {
			CotElePic cotElePic = new CotElePic();
			CotOrderfacPic cotOrderfacPic = (CotOrderfacPic) list.get(0);
			cotElePic.setPicImg(cotOrderfacPic.getPicImg());
			cotElePic.setPicSize(cotOrderfacPic.getPicSize());
			cotElePic.setPicName(cotOrderfacPic.getPicName());
			return cotElePic;
		}
		return null;
	}

	// 获得暂无图片的图片字节
	public byte[] getZwtpPic() {
		// 获得系统路径
		String classPath = CotPanServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		File picFile = new File(systemPath + "common/images/zwtp.png");
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[((int) picFile.length())];
			while (in.read(b) != -1) {
			}
			in.close();
			return b;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public void addList(List<?> list) {
		try {
			this.getBaseDao().saveRecords(list);
			List listPic = new ArrayList();
			// 拷贝样品档案图片到询盘图片表
			for (Object object : list) {
				CotPanEle detail = (CotPanEle) object;
				CotPanPic cotPanPic = new CotPanPic();
				CotElePic cotElePic = null;
//				if (detail.getElementId() != null) {
//					if (detail.getSrcType().equals("ele")) {
//						cotElePic = this.getElePicByFkId(detail.getElementId());
//					}
//					if (detail.getSrcType().equals("price")) {
//						cotElePic = this.getPricePicByFkId(detail
//								.getElementId());
//					}
//					if (detail.getSrcType().equals("orderfac")) {
//						cotElePic = this.getOrderFacPicByFkId(detail
//								.getElementId());
//					}
//				} else {
//					cotElePic = new CotElePic();
//					cotElePic.setPicImg(this.getZwtpPic());
//					cotElePic.setPicSize(12055);
//					cotElePic.setPicName(detail.getEleId());
//				}
				cotPanPic.setEleId(detail.getEleId());
				cotPanPic.setFkId(detail.getId());
				cotPanPic.setPicImg(cotElePic.getPicImg());
				cotPanPic.setPicName(cotElePic.getPicName());
				cotPanPic.setPicSize(cotElePic.getPicSize());
				listPic.add(cotPanPic);
			}
			this.getBaseDao().saveRecords(listPic);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdateList(List<?> list) {
		try {
			this.getBaseDao().saveOrUpdateRecords(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deletePans(List<Integer> ids) throws Exception {
		boolean flag = true;
		try {
			String str="";
			for (int i = 0; i < ids.size(); i++) {
				str+=ids.get(i)+",";
			}
			//删除供应商发过来的附件
			String hql ="select obj.fileUrl from CotPan p, CotPanEle e,CotPanDetail obj where e.panId=p.id and obj.panId=e.id and p.id in ("+str.substring(0,str.length()-1)+")";
			List fileList=this.getBaseDao().find(hql);
			String classPath = CotPanServiceImpl.class.getResource("/").toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			for (int i = 0; i < fileList.size(); i++) {
				File file=new File(systemPath+(String) fileList.get(i));
				if(file.exists())
					file.delete();
			}
			
			this.getBaseDao().deleteRecordByIds(ids, "CotPan");
		} catch (DAOException e) {
			e.printStackTrace();
			flag = false;
			throw new Exception(e.toString());
		}
		return flag;
	}

	public void deletePanDetails(List<Integer> ids) throws Exception {
		try {
			this.getBaseDao().deleteRecordByIds(ids, "CotPanEle");
		} catch (DAOException e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
	}

	public void deletePriceDetails(List<Integer> ids) throws Exception {
		try {
			this.getBaseDao().deleteRecordByIds(ids, "CotPanDetail");
		} catch (DAOException e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
	}

	public CotElementsNew findEleByEleId(String eleId) {
		String hql = "from CotElementsNew obj where obj.eleId=?";
		List list = this.getBaseDao()
				.queryForLists(hql, new Object[] { eleId });
		if (list != null && list.size() > 0) {
			CotElementsNew cotElementsNew = (CotElementsNew) list.get(0);
			if (cotElementsNew != null) {
				cotElementsNew.setPicImg(null);
				cotElementsNew.setCotPictures(null);
				cotElementsNew.setCotFile(null);
				cotElementsNew.setChilds(null);
				cotElementsNew.setCotPriceFacs(null);
				cotElementsNew.setCotEleFittings(null);
				cotElementsNew.setCotElePrice(null);
				cotElementsNew.setCotElePacking(null);
				return cotElementsNew;
			} else {
				return null;
			}
		}
		return null;
	}

	public void updatePicImg(String filePath, Integer detailId) {
		List<CotPanPic> imgList = new ArrayList<CotPanPic>();
		// 图片操作类
		CotOpImgService impOpService = (CotOpImgService) SystemUtil
				.getService("CotOpImgService");
		CotPanPic panPic = impOpService.getPanPic(detailId);
		File picFile = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(picFile);
			byte[] b = new byte[(int) picFile.length()];
			while (in.read(b) != -1) {
			}
			in.close();
			panPic.setPicImg(b);
			panPic.setPicSize(b.length);
			imgList.add(panPic);
			if (filePath.indexOf("common/images/zwtp.png") < 0) {
				picFile.delete();
			}
			impOpService.saveOrUpdateImg(imgList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveOtherPans(String fdsIds, List<Integer> pIds)
			throws Exception {
		String[] fds = fdsIds.split(",");
		List detailList = new ArrayList();
		// Date valDate=null;
		for (int i = 0; i < pIds.size(); i++) {
			Integer pId = pIds.get(i);
			CotPanEle cotPanEle = (CotPanEle) this.getBaseDao().getById(
					CotPanEle.class, pId);
			// if(i==0){
			// CotPan cotPan = (CotPan) this.getBaseDao().getById(CotPan.class,
			// cotPanEle.getPanId());
			// valDate = cotPan.getValDate();
			// }
			for (int j = 0; j < fds.length; j++) {
				Integer factoryId = Integer.parseInt(fds[j]);
				// 判断该货号是否已经对该厂报过价
				String hql = "select obj.id from CotPanDetail obj where obj.factoryId="
						+ factoryId + " and obj.panId=" + pId;
				List list = this.getBaseDao().find(hql);
				if (list == null || list.size() == 0) {
					CotPanDetail panDetail = new CotPanDetail();
					panDetail.setFactoryId(factoryId);
					panDetail.setPanId(pId);
					panDetail.setState(0);
					// panDetail.setValDate(valDate);
//					panDetail.setWeight(cotPanEle.getWeight());
//					panDetail.setFilling(cotPanEle.getFilling());
					panDetail.setConstruction(cotPanEle.getConstruction());
//					panDetail.setPacking(cotPanEle.getPacking());
//					panDetail.setBoxObcount(cotPanEle.getBoxObcount());
					panDetail.setCcyId(cotPanEle.getCurrencyId());
//					panDetail.setCarton(cotPanEle.getCarton());
					detailList.add(panDetail);
				}
			}
		}
		this.getBaseDao().saveRecords(detailList);
	}

	// 加密
	public String kiEncrypt(String str) {
		String skiKey = KiCrypt.KEY_STRING;
		// 位数补到8位
		KiCrypt crypt = new KiCrypt();
		int aa = 8 - str.length();
		String sNo = "";
		for (int i = 0; i < aa; i++) {
			sNo = sNo + "0";
		}
		sNo = sNo + str;
		sNo = crypt.KiEncrypt(skiKey, sNo);
		return sNo;
	}

	public boolean sendMails(List<String> list,Integer companyType) throws Exception {
		// 获得公司的发邮件参数 companyType==1时是wolly公司 companyType==2时是HHO公司 
		String hql = "select obj.companyEmail,obj.account,obj.mailPwd,obj.smtpHost,obj.smtpIss,obj.smtpPort from CotCompany obj where obj.id="+companyType;
		List listCom = this.getBaseDao().find(hql);
		if (listCom == null || listCom.size() == 0) {
			return false;
		}
		Object[] object = (Object[]) listCom.get(0);
		String companyEmail=(String) object[0];
		String account=(String) object[1];
		String mailPwd=(String) object[2];
		String smtpHost=(String) object[3];
		Long smtpIss=(Long) object[4];
		Long smtpPort=(Long) object[5];
		
		if (companyEmail == null
				|| companyEmail.equals("")
				||account == null
				|| account.equals("")
				|| mailPwd == null
				|| mailPwd.equals("")
				|| smtpHost == null
				||smtpHost.equals("")) {
			return false;
		}
		List listPan = new ArrayList();
		String fIs = "";

		// 询盘主单
		Integer panId = null;
		Iterator<?> it = list.iterator();
		int k = 1;
		int chk =0;
		while (it.hasNext()) {
			String obj = (String) it.next();
			String[] temp = obj.split(",");
			System.out.println("第" + k + "封");
			System.out.println(temp[0]);// 联系人名称
			System.out.println(temp[1]);// 联系人邮箱
			System.out.println(temp[2]);// 询盘单号
			System.out.println(temp[3]);// 厂家编号
			System.out.println(temp[4]);// 有效期
			System.out.println(temp[5]);// 联系人id
			System.out.println(temp[6]);// 登录名
			System.out.println(temp[7]);// 询盘主单id
			System.out.println("==============");

			CotContact tact = (CotContact) this.getBaseDao().getById(
					CotContact.class, Integer.parseInt(temp[5]));
			
			// 保存到发送邮件的表
			CotPanContact panContact = new CotPanContact();
			panContact.setContactId(Integer.parseInt(temp[5]));
			panContact.setPanId(Integer.parseInt(temp[7]));
			panContact.setMailUrl(temp[1]);
			panContact.setState(0);// 默认联系人未查看邮件,为1时为已查看
			panContact.setSendTime(new Date());
			List listMail = new ArrayList();
			listMail.add(panContact);
			this.getBaseDao().saveRecords(listMail);

			panId = Integer.parseInt(temp[7]);
			fIs += temp[3] + ",";

			// 加密厂家编号
			String factoryNo = this.kiEncrypt(temp[3]);
			// 加密询盘主单id
			String panIdNo = this.kiEncrypt(temp[7]);
			// 加密联系人id
			String contactIdNo = this.kiEncrypt(temp[5]);
			// 加密联系人帐号
			// String userName = this.kiEncrypt(temp[6]);
			// 加密有效期(把有效期换成时间戳)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = sdf.parse(temp[4]+" 23:59:59");
			Long dt = date.getTime();
			String valD = dt.toString();
			// 截掉后面5个0
			String dateNo = valD.substring(0, valD.length() - 3);
			String valDateNo = this.kiEncrypt(dateNo);

			// 账号设置
			MailCfg cfg = new MailCfg();
			cfg.setEmailAccount(account);// 邮箱账号
			cfg.setEmailPassword(mailPwd);// 密码
			// cfg.setPopServerUrl("POP或POP3服务地址");
			cfg.setSmtpServerUrl(smtpHost);// SMTP服务地址
			cfg.setSmtpAuthentication(true);
			cfg.setPopSSL(false);
			cfg.setSmtpSSL((smtpIss == null || smtpIss == 0) ? false : true);
			cfg.setPopPort(110);
			cfg.setSmtpPort(smtpPort == null ? 25 : smtpPort.intValue());
			// 发件服务类
			SendMailService<MailObjectDefault> sendMailService = new SendMailDefault<MailObjectDefault>();
			MailObjectDefault mailObject = new MailObjectDefault();
			MailPerson mailPerson = new MailPerson();
			mailPerson.setEmailUrl(companyEmail); // 发件人账号
			mailPerson.setName("Wolly&Co."); // 发件人名
			mailObject.setSender(mailPerson); // 
			mailObject
					.setSubject("You have received an inquiry from Wolly&Co."); // 主题
			String reUrl = "http://mail.wolly.dk:1864/WollyPrs";
//			String txtUrl="http://192.168.1.136:8787/WollyPrs";
			String txtUrl = "http://localhost:5050/WollyPrs";
			// String txtUrl=reUrl;
//			String txtUrl="http://xmqhoa.gnway.net:8090/WollyPrs";
//			String txtUrl="http://222.76.185.22:8090/WollyPrs";
//			String txtUrl="http://mail.wolly.dk:1864/WollyPrs";
			String url = txtUrl + "/validateAction.do?method=validateUrl"
					+ "&factoryNo=" + factoryNo + "&contactIdNo=" + contactIdNo
					+ "&panIdNo=" + panIdNo
					 +"&pcId="+panContact.getId()
					+ "&valDateNo=" + valDateNo;
			mailObject
					.setBody("Dear Supplier<br/><br/>"
							+ "Please fill in your quotation based on our inquiry no later than： "
							+ temp[4]
							+ "<br/>"
							+ "By clicking below link you will be able to see our inquiry.<br/><br/> "
							+ "URL：<a href="
							+ url
							+ ">"
							+ reUrl
							+ "</a><br/>"
							+ "UserName："
							+ temp[6]
							+ "<br/>"
							+ "Password："
							+ tact.getLoginPwd()
							+ "<br/><br/>"
							+ "You can go in and out of the link doing the quotation more than once by clicking “Save” only.<br/>"
							+ "When you have clicked “Complete” you cannot make any changes and the link will become inactive.<br/>"
							+ "Only after clicking “Complete” Wolly will be able to see your quotation.BUT DO NOT REPLY ON THIS E-MAIL ADDRESS."
							+ "<br/><br/>1.请直接点击链接做报价， 完成后点击保存“Save” 。<br/>" 
							+ "建议使用GOOGLE谷歌浏览器打开链接,否则速度很慢,google浏览器下载地址:<a href=http://www.google.cn/chrome/intl/zh-CN/landing_chrome.html?hl=zh-CN&brand=CHMI>http://www.google.cn/chrome/intl/zh-CN/landing_chrome.html?hl=zh-CN&brand=CHMI。</a><br/>"
								+ "2.操作完成后点击\"Complete\",点击\"Complete\"后报价单就不能再做任何更改,链接也无法再打开。请确保所有的报价已经完成后再点击\"Complete\"按钮。<br/>"
								+ "3.点击完成“Complete”， 我们就可以看到你们的报价。 不用回复邮件。<br/>"
								+ "4.本软件由厦门市旗航软件提供技术支持,网址:<a href=http://www.xmqh.net>http://www.xmqh.net</a>。"
								+ "<br/><br/>Best Regard<br/><br/>Wolly&Co.");
			mailObject.setIsNotification(false); // 是否回执，false为否

			// 接收人设置
			MailPerson mPerson = new MailPerson();
			mPerson.setEmailUrl(temp[1]);// 收件人邮箱
			mPerson.setName(temp[0]);// 收件人名
			List<MailPerson> mList = new ArrayList<MailPerson>();
			mList.add(mPerson);
			mailObject.setTo(mList);
			// 发送
			try {
				sendMailService.sendMail(mailObject, null, cfg);
				chk++;
			} catch (Exception e) {
				e.printStackTrace();
				//如果发送失败要删除邮件记录
				this.getBaseDao().deleteRecordById(panContact.getId(), "CotPanContact");
				chk--;
			}
		}
		if(chk>0){
			String kql = "select d.id from CotPan p,CotPanEle ele,CotPanDetail d where p.id="
				+ panId
				+ " "
				+ "and ele.panId=p.id and d.state=0 and d.panId=ele.id and d.factoryId in ("
				+ fIs.substring(0, fIs.length() - 1) + ")";
			List listPanDetail = this.getBaseDao().find(kql);
			if(listPanDetail!=null && listPanDetail.size()>0){
				// 修改cotpandetail的邮件状态
				String uphql = "update CotPanDetail obj set obj.state=1 where obj.id in (:ids)";
				QueryInfo queryInfo = new QueryInfo();
				queryInfo.setSelectString(uphql);
				Map map = new HashMap();
				map.put("ids", listPanDetail);
				this.getBaseDao().executeUpdate(queryInfo, map);
			}
			return true;
		}else{
			return false;
		}
	}
	public boolean sendMailToEmps(List<String> list,String prsNo,Integer companyType) {
		// 获得公司的发邮件参数 companyType==1时是wolly公司 companyType==2时是HHO公司 
		String hql = "select obj.companyEmail,obj.account,obj.mailPwd,obj.smtpHost,obj.smtpIss,obj.smtpPort from CotCompany obj where obj.id="+companyType;
		List listCom = this.getBaseDao().find(hql);
		if (listCom == null || listCom.size() == 0) {
			return false;
		}
		Object[] object = (Object[]) listCom.get(0);
		String companyEmail=(String) object[0];
		String account=(String) object[1];
		String mailPwd=(String) object[2];
		String smtpHost=(String) object[3];
		Long smtpIss=(Long) object[4];
		Long smtpPort=(Long) object[5];
		
		if (companyEmail == null
				|| companyEmail.equals("")
				||account == null
				|| account.equals("")
				|| mailPwd == null
				|| mailPwd.equals("")
				|| smtpHost == null
				||smtpHost.equals("")) {
			return false;
		}

		List listPan = new ArrayList();
		String fIs = "";

		// 询盘主单
		Integer panId = null;
		Iterator<?> it = list.iterator();
		int k = 1;
		int chk =0;
		while (it.hasNext()) {
			String str = (String)it.next();
			String email = str.split(",")[1];
			String empName = str.split(",")[0];
			// 账号设置
			MailCfg cfg = new MailCfg();
			cfg.setEmailAccount(account);// 邮箱账号
			cfg.setEmailPassword(mailPwd);// 密码
			// cfg.setPopServerUrl("POP或POP3服务地址");
			cfg.setSmtpServerUrl(smtpHost);// SMTP服务地址
			cfg.setSmtpAuthentication(true);
			cfg.setPopSSL(false);
			cfg.setSmtpSSL((smtpIss == null || smtpIss == 0) ? false : true);
			cfg.setPopPort(110);
			cfg.setSmtpPort(smtpPort == null ? 25 : smtpPort.intValue());
			// 发件服务类
			SendMailService<MailObjectDefault> sendMailService = new SendMailDefault<MailObjectDefault>();
			MailObjectDefault mailObject = new MailObjectDefault();
			MailPerson mailPerson = new MailPerson();
			mailPerson.setEmailUrl(companyEmail); // 发件人账号
			mailPerson.setName("Wolly&Co."); // 发件人名
			mailObject.setSender(mailPerson); // 
			mailObject
					.setSubject("You have received an inquiry from Wolly&Co."); // 主题
			mailObject
					.setBody("Dear "+empName+"<br/><br/>"
							+"You have received an inquiry from Wolly&Co.<br/>"
							+"inquiry No:"+prsNo
					);
			mailObject.setIsNotification(false); // 是否回执，false为否

			// 接收人设置
			MailPerson mPerson = new MailPerson();
			mPerson.setEmailUrl(email);// 收件人邮箱
			mPerson.setName(empName);// 收件人名
			List<MailPerson> mList = new ArrayList<MailPerson>();
			mList.add(mPerson);
			mailObject.setTo(mList);
			// 发送
			try {
				sendMailService.sendMail(mailObject, null, cfg);
				chk++;
			} catch (Exception e) {
				e.printStackTrace();
				//如果发送失败要删除邮件记录
				chk--;
			}
		}
		if(chk > 0) return true;
		return false;
	}
	public void modPanHide(String ids,String facIds) throws Exception {
		//查询需要隐藏的厂家报价明细
		String hql="select d.id from CotPan p,CotPanEle e,CotPanDetail d " +
				"where e.panId=p.id and d.panId=e.id and d.factoryId in ("+facIds.substring(0,facIds.length()-1)+") and p.id in ("+ids.substring(0,ids.length()-1)+")";
		List list = this.getBaseDao().find(hql);
		String uphql = "update CotPanDetail obj set obj.hideStatus=1 where obj.id in (:ids)";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(uphql);
		Map map = new HashMap();
		map.put("ids", list);
		this.getBaseDao().executeUpdate(queryInfo, map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.sail.cot.service.pan.CotPanService#findAlreadySendMails(java.lang.Integer)
	 */
	public List findAlreadySendMails(Integer panId) {
		String hql="select distinct obj.factoryId from CotPanDetail obj,CotPanEle e " +
				"where obj.state<>3 and e.panId="+panId+" and obj.panId=e.id "; 
		List list = this.getBaseDao().find(hql);
		return list;
	}

	public void updatePanState(Integer pId) throws Exception {
		String uphql = "update CotPan obj set obj.state=4 where obj.id=:id";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(uphql);
		Map map = new HashMap();
		map.put("id", pId);
		this.getBaseDao().executeUpdate(queryInfo, map);
	}

	public List findFactoryByPanId(Integer pId) {
		String hql = "select distinct d.factoryId from CotPanDetail d,CotPanEle e where d.panId=e.id and e.panId="
				+ pId;
		List list = this.getBaseDao().find(hql);
		return list;
	}

	public boolean saveAllEMail(Integer pId) throws Exception {
		// 过滤出cotpanele中不重复的,未发过邮件的厂家
		// 发给厂家中可以发盘点邮件的联系人
		String hql = "select distinct d.factoryId from CotPanDetail d,CotPanEle e,CotPan p where e.panId=p.id and d.panId=e.id and d.state=0 and p.id="
				+ pId;
		List list = this.getBaseDao().find(hql);
		List<String> listRes = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (list != null && list.size() > 0) {
			CotPan cotPan = (CotPan) this.getBaseDao().getById(CotPan.class,
					pId);
			for (int i = 0; i < list.size(); i++) {
				Integer obj = (Integer) list.get(i);
				String sql = "select c from CotFactory f,CotContact c where c.factoryId=f.id and c.mainFlag=1 "
						+ "and c.contactEmail is not null and c.contactEmail!='' and f.id="
						+ obj;
				List listCon = this.getBaseDao().find(sql);
				for (int j = 0; j < listCon.size(); j++) {
					String str = "";
					CotContact cotContact = (CotContact) listCon.get(j);
					str += cotContact.getContactPerson() + ",";
					str += cotContact.getContactEmail() + ",";
					str += cotPan.getPriceNo() + ",";
					str += cotContact.getFactoryId() + ",";
					str += sdf.format(cotPan.getValDate()) + ",";
					str += cotContact.getId() + ",";
					str += cotContact.getLoginName() + ",";
					str += cotPan.getId();
					listRes.add(str);
				}
			}
			if (listRes.size() == 0) {
				return false;
			} else {
//				this.sendMails(listRes);
			}
			return true;
		}
		return false;
	}

	// wolly选定一条报价信息
	public void updateEleByChoose(Integer dId) throws DAOException {
		CotPanDetail panDetail = (CotPanDetail) this.getBaseDao().getById(
				CotPanDetail.class, dId);
		String uphql = "update CotPanEle obj set obj.factoryId=:factoryId,obj.price=:price,obj.ccyId=:ccyId,obj.state=1 where obj.id=:id";
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setSelectString(uphql);
		Map map = new HashMap();
		map.put("factoryId", panDetail.getFactoryId());
		map.put("price", panDetail.getPrice());
		map.put("ccyId", panDetail.getCcyId());
		map.put("id", panDetail.getPanId());
		this.getBaseDao().executeUpdate(queryInfo, map);

		// 修改cotpandetail的状态为wolly确认
		panDetail.setState(4);
		List list = new ArrayList();
		list.add(panDetail);
		this.getBaseDao().updateRecords(list);
	}

	// 根据Quotation勾选的记录生成一张新的Inquiry
	public Integer saveNewInquiry(String ids,String facIds,Integer flag) throws Exception {
		CotPan cotPan = new CotPan();
		cotPan.setTypePan(flag);
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		cotPan.setAddPerson(empId);
		cotPan.setEmpId(empId);
		cotPan.setUrgency(0);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currdate = df.format(date);
		Sequece seq = new Sequece(false);
		Map map = new HashMap();
		map.put("CotEmps", empId);
		String priceNo = seq.getPanNo(map, currdate);
		cotPan.setPriceNo(priceNo);
		cotPan.setAddTime(date);
		//设置有效期为当前日期后10天
		Calendar calShipH10 = Calendar.getInstance();
		calShipH10.setTime(date);
		int h = calShipH10.get(Calendar.DATE);
		calShipH10.set(Calendar.DATE, h + 10);
		cotPan.setValDate(calShipH10.getTime());
		cotPan.setStatus(0);
		
		List list = new ArrayList();
		list.add(cotPan);
		this.getBaseDao().saveOrUpdateRecords(list);
		// 保存明细
		//查询需要隐藏的厂家报价明细
		String hql="select e from CotPan p,CotPanEle e,CotPanDetail d " +
				"where e.panId=p.id and d.panId=e.id and d.factoryId in ("+facIds.substring(0,facIds.length()-1)+") and p.id in ("+ids.substring(0,ids.length()-1)+")";
		
		List detailList = this.getBaseDao().find(hql);
		Iterator<?> itDe = detailList.iterator();
		while (itDe.hasNext()) {
			CotPanEle panEle = (CotPanEle) itDe.next();
			
			CotPanEle cotPanELe = new CotPanEle();
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			BeanUtils.copyProperties(cotPanELe, panEle);
			cotPanELe.setPanId(cotPan.getId());
			cotPanELe.setId(null);
//			cotPanELe.setPrice(null);
//			cotPanELe.setCcyId(null);
			cotPanELe.setModDate(null);
			cotPanELe.setModPerson(null);
			List listDetail = new ArrayList();
			listDetail.add(cotPanELe);
			this.getBaseDao().saveRecords(listDetail);
			//拷贝图片
			String picHql = "from CotPanPic obj where obj.fkId="+ panEle.getId();
			List picList = this.getBaseDao().find(picHql);
			CotPanPic cotPanPic = (CotPanPic) picList.get(0);
			CotPanPic newPic =new CotPanPic();
			newPic.setEleId(cotPanPic.getEleId());
			newPic.setPicImg(cotPanPic.getPicImg());
			newPic.setPicName(cotPanPic.getPicName());
			newPic.setPicSize(cotPanPic.getPicSize());
			newPic.setFkId(cotPanELe.getId());
			List listPic = new ArrayList();
			listPic.add(newPic);
			this.getBaseDao().saveRecords(listPic);
			
			//拷贝其他图片
			String picOtherHql = "from CotPanOtherPic obj where obj.fkId="+ panEle.getId();
			List picOtherList = this.getBaseDao().find(picOtherHql);
			for (int i = 0; i < picOtherList.size(); i++) {
				CotPanOtherPic panOtherPic = (CotPanOtherPic) picOtherList.get(i);
				CotPanOtherPic cotPanOtherPic =new CotPanOtherPic();
				cotPanOtherPic.setEleId(panOtherPic.getEleId());
				cotPanOtherPic.setPicImg(panOtherPic.getPicImg());
				cotPanOtherPic.setPicName(panOtherPic.getPicName());
				cotPanOtherPic.setPicSize(panOtherPic.getPicSize());
				cotPanOtherPic.setFkId(cotPanELe.getId());
				List listOtherPic = new ArrayList();
				listOtherPic.add(cotPanOtherPic);
				this.getBaseDao().saveRecords(listOtherPic);
			}
			
		}
		// 保存单号
		if (cotPan.getId()!=null) {
			CotSeqService cotSeqService = new CotSeqServiceImpl();
			cotSeqService.saveSeq("pan");
		}
		return cotPan.getId();
	}
	
	// 上传其他图片
	public void saveOtherPic(String filePath, Integer eId) throws Exception {
		CotPanOtherPic picOther = new CotPanOtherPic();
		CotPanEle elements = (CotPanEle) this.getBaseDao().getById(CotPanEle.class, eId);
		Integer s = filePath.lastIndexOf('\\');
		String picName = filePath.substring(s + 1, filePath.length());

		File picFile = new File(filePath);
		FileInputStream in;
		in = new FileInputStream(picFile);
		byte[] b = new byte[(int) picFile.length()];
		while (in.read(b) != -1) {
		}
		in.close();
		picOther.setPicImg(b);
		picOther.setPicSize(b.length);
		picOther.setEleId(elements.getEleId());
		picOther.setPicName(picName.substring(0, picName.length() - 4));
		picOther.setFkId(elements.getId());
		if (filePath.indexOf("common/images/zwtp.png") < 0) {
			picFile.delete();
		}
		this.getBaseDao().create(picOther);
	}
	
	// 根据图片编号获得图片
	public CotPanOtherPic getPicById(Integer pictureId) {
		return (CotPanOtherPic) this.getBaseDao().getById(CotPanOtherPic.class,
				pictureId);
	}
	
	// 通过图片Id删除图片
	public Boolean deletePictureByPicId(Integer picId) {
		try {
			this.getBaseDao().deleteRecordById(picId, "CotPanOtherPic");
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 得到总记录数
	public Integer getRecordCount(QueryInfo queryInfo) {
		try {
			return this.getBaseDao().getRecordsCount(queryInfo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<?> getList(QueryInfo queryInfo) throws DAOException {
		List<?> list = this.getBaseDao().findRecords(queryInfo);
		List<CotPan> listVo = new ArrayList<CotPan>();
		Map<String,CotPan> map = new HashMap<String,CotPan>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[])list.get(i);
			CotPan cotPan = (CotPan)obj[0];
			map.put((Integer)obj[1]+"_"+cotPan.getId(), cotPan);
		}
		Iterator<?> it = map.keySet().iterator();
		while(it.hasNext()){
			String factoryId = (String)it.next();
			String[] temp = factoryId.split("_");
			CotPan cotPan = map.get(factoryId);
			CotPan cotPanNew = new CotPan(cotPan,Integer.parseInt(temp[0]));
			listVo.add(cotPanNew);
		}
		//id倒序排
		ListSort listSort = new ListSort();
		listSort.setField("id");
		listSort.setFieldType("Integer");
		listSort.setTbName("CotPan");
		listSort.setType(false);
		Collections.sort(listVo, listSort);
		return listVo;
	}
	
	public boolean sendMailToFacContact(List<String> mailList,Integer companyType,String jsonParam){
		//生成报表附件
		CotExportRptService reportService = (CotExportRptService)ContextUtil.getBean("CotRptService");
		JSONObject json = JSONObject.fromObject(jsonParam);
		String exportPath = ContextUtil.getRealPath()+"/"+"reportfile/"+"tmp/inquery_"+json.getString("prsNo")+".xls";
		String rptXMLPath = ContextUtil.getRealPath()+"/"+"reportfile/"+"prs_fac_contact_mail.jrxml";
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String queryString = "";
		String ids = StringUtils.join(json.getJSONArray("ids"), ",");
		queryString += " obj.panId="+json.getInt("panId");
		queryString += " and obj.id in("+ids+")";
		paramMap.put("STR_SQL", queryString);
		List<CotCurrency> curList = this.getBaseDao().getRecords("CotCurrency");
		Map curMap = new HashMap();
		for(CotCurrency currency : curList){
			curMap.put(currency.getId(), currency.getCurNameEn());
		}
		paramMap.put("exlSheet", "false");
		paramMap.put("currencyMap", curMap);
		int res = reportService.exportRptToXLS(rptXMLPath, exportPath, paramMap);
		if(res == -1)
			return false;
		//发送邮件
		// 获得公司的发邮件参数 companyType==1时是wolly公司 companyType==2时是HHO公司 
		String hql = "select obj.companyEmail,obj.account,obj.mailPwd,obj.smtpHost,obj.smtpIss,obj.smtpPort from CotCompany obj where obj.id="+companyType;
		List listCom = this.getBaseDao().find(hql);
		if (listCom == null || listCom.size() == 0) {
			return false;
		}
		Object[] object = (Object[]) listCom.get(0);
		String companyEmail=(String) object[0];
		String account=(String) object[1];
		String mailPwd=(String) object[2];
		String smtpHost=(String) object[3];
		Long smtpIss=(Long) object[4];
		Long smtpPort=(Long) object[5];
		
		if (companyEmail == null
				|| companyEmail.equals("")
				||account == null
				|| account.equals("")
				|| mailPwd == null
				|| mailPwd.equals("")
				|| smtpHost == null
				||smtpHost.equals("")) {
			return false;
		}

		List listPan = new ArrayList();
		String fIs = "";

		// 询盘主单
		Integer panId = null;
		Iterator<String> it = mailList.iterator();
		int k = 1;
		int chk =0;
		while (it.hasNext()) {
			String email = it.next();
			// 账号设置
			MailCfg cfg = new MailCfg();
			cfg.setEmailAccount(account);// 邮箱账号
			cfg.setEmailPassword(mailPwd);// 密码
			// cfg.setPopServerUrl("POP或POP3服务地址");
			cfg.setSmtpServerUrl(smtpHost);// SMTP服务地址
			cfg.setSmtpAuthentication(true);
			cfg.setPopSSL(false);
			cfg.setSmtpSSL((smtpIss == null || smtpIss == 0) ? false : true);
			cfg.setPopPort(110);
			cfg.setSmtpPort(smtpPort == null ? 25 : smtpPort.intValue());
			// 发件服务类
			SendMailService<MailObjectDefault> sendMailService = new SendMailDefault<MailObjectDefault>();
			MailObjectDefault mailObject = new MailObjectDefault();
			MailPerson mailPerson = new MailPerson();
			mailPerson.setEmailUrl(companyEmail); // 发件人账号
			mailPerson.setName("Wolly&Co."); // 发件人名
			mailObject.setSender(mailPerson); // 
			mailObject.setSubject("You have received an inquiry from Wolly&Co."); // 主题
			mailObject
					.setBody("Dear <br/><br/>"
							+"You have received an inquiry from Wolly&Co.<br/><br/>"
							+"Inquiry NO is "+json.getString("prsNo")+".<br/><br/>"
							+"See detail in the Attachment"
					);
			mailObject.setIsNotification(false); // 是否回执，false为否

			// 接收人设置
			MailPerson mPerson = new MailPerson();
			mPerson.setEmailUrl(email);// 收件人邮箱
			//mPerson.setName(empName);// 收件人名
			List<MailPerson> mList = new ArrayList<MailPerson>();
			mList.add(mPerson);
			mailObject.setTo(mList);
			// 发送
			try {
				EmailAttachment attachment = new EmailAttachment();
				attachment.setDescription("inquery_"+json.getString("prsNo"));
				attachment.setName("inquery_"+json.getString("prsNo")+".xls");
				attachment.setPath(exportPath);
				List<EmailAttachment> attList = new ArrayList<EmailAttachment>();
				attList.add(attachment);
				sendMailService.sendMail(mailObject, attList, cfg);
				chk++;
			} catch (Exception e) {
				e.printStackTrace();
				//如果发送失败要删除邮件记录
				chk--;
			}
		}
		if(chk > 0) return true;
		return false;
	} 
	
	public void saveNewPanEle(String eleId,Integer panId) throws DAOException{
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		CotPanEle cotPanEle=new CotPanEle();
		cotPanEle.setEleNameEn(eleId);
		cotPanEle.setPanId(panId);
		cotPanEle.setModPerson(empId);
		cotPanEle.setModDate(new Date());
		List list=new ArrayList();
		list.add(cotPanEle);
		this.getBaseDao().saveRecords(list);
		
		CotPanPic newPic =new CotPanPic();
		newPic.setEleId(eleId);
		newPic.setPicImg(this.getZwtpPic());
		newPic.setPicSize(12055);
		newPic.setPicName(eleId);
		newPic.setFkId(cotPanEle.getId());
		List listPic = new ArrayList();
		listPic.add(newPic);
		this.getBaseDao().saveRecords(listPic);
	}
	
	public CotPanEle getPanEleById(Integer panId) {
		return (CotPanEle) this.getBaseDao().getById(CotPanEle.class, panId);
	}
	
	public void updatePanEle(CotPanEle cotPanEle) throws DAOException{
		WebContext ctx = WebContextFactory.get();
		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
		cotPanEle.setModPerson(empId);
		cotPanEle.setModDate(new Date());
		List list=new ArrayList();
		list.add(cotPanEle);
		this.getBaseDao().updateRecords(list);
	}
	
	public void deletePanEles(List ids) throws DAOException{
		String str="";
		for (int i = 0; i < ids.size(); i++) {
			str+=ids.get(i)+",";
		}
		//删除供应商发过来的附件
		String hql ="select obj.fileUrl from CotPanEle e,CotPanDetail obj where obj.panId=e.id and e.id in ("+str.substring(0,str.length()-1)+")";
		List fileList=this.getBaseDao().find(hql);
		String classPath = CotPanServiceImpl.class.getResource("/").toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		for (int i = 0; i < fileList.size(); i++) {
			File file=new File(systemPath+(String) fileList.get(i));
			if(file.exists())
				file.delete();
		}
		this.getBaseDao().deleteRecordByIds(ids, "CotPanEle");
	}
	public void savePanDetail(List<CotPanDetail> list){
		Integer id=null;
		for(int i=0;i<list.size();i++){
			CotPanDetail detail = list.get(i);
			detail.setModDate(new Date(System.currentTimeMillis()));
			id=detail.getPanId();
			list.set(i, detail);
		}
		this.getBaseDao().saveOrUpdateRecords(list);
		//修改主单的状态
		CotPanEle cotPanEle=(CotPanEle)this.getBaseDao().getById(CotPanEle.class, id);
		CotPan cotPan=(CotPan)this.getBaseDao().getById(CotPan.class, cotPanEle.getPanId());
		cotPan.setStatus(1);
		List panList=new ArrayList();
		panList.add(cotPan);
		this.getBaseDao().updateRecords(panList);
	}
	
	public CotPanDetail getPanDetailById(Integer id){
		return (CotPanDetail)this.getBaseDao().getById(CotPanDetail.class, id);
	}
	
	public void savePanEleFromEles(List ids,Integer panId) throws DAOException{
		String str="";
		for (int i = 0; i < ids.size(); i++) {
			str+=ids.get(i)+",";
		}
		List panList=new ArrayList();
		String hql = "from CotElementsNew obj where obj.id in ("+str.substring(0,str.length()-1)+")";
		List list =this.getBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotElementsNew cotElementsNew = (CotElementsNew) list.get(i);
			CotPanEle cotPanEle=new CotPanEle();
			cotPanEle.setPanId(panId);
			cotPanEle.setEleId(cotElementsNew.getEleId());
			cotPanEle.setEleNameEn(cotElementsNew.getEleNameEn());
			cotPanEle.setSize(cotElementsNew.getEleInchDesc());
			cotPanEle.setCanPrice(cotElementsNew.getPriceFac()==null?null:cotElementsNew.getPriceFac().doubleValue());
			cotPanEle.setCanCurId(cotElementsNew.getPriceFacUint());
			panList.add(cotPanEle);
			
		}
		this.getBaseDao().saveRecords(panList);
		List listPic=new ArrayList();
		for (int i = 0; i < panList.size(); i++) {
			CotPanEle cotPanEle=(CotPanEle) panList.get(i);
			CotElementsNew cotElementsNew = (CotElementsNew) list.get(i);
			CotPanPic cotPanPic = new CotPanPic();
			CotElePic cotElePic = this.getElePicByFkId(cotElementsNew.getId());
			cotPanPic.setEleId(cotElementsNew.getEleId());
			cotPanPic.setFkId(cotPanEle.getId());
			cotPanPic.setPicImg(cotElePic.getPicImg());
			cotPanPic.setPicName(cotElePic.getPicName());
			cotPanPic.setPicSize(cotElePic.getPicSize());
			listPic.add(cotPanPic);
		}
		this.getBaseDao().saveRecords(listPic);
	}
	
	public void savePanEleFromPos(List ids,Integer panId) throws DAOException{
		String str="";
		for (int i = 0; i < ids.size(); i++) {
			str+=ids.get(i)+",";
		}
		List panList=new ArrayList();
		String hql = "from CotOrderFacdetail obj where obj.id in ("+str.substring(0,str.length()-1)+")";
		List list =this.getBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) list.get(i);
			CotPanEle cotPanEle=new CotPanEle();
			cotPanEle.setPanId(panId);
			cotPanEle.setEleId(cotOrderFacdetail.getEleId());
			cotPanEle.setEleNameEn(cotOrderFacdetail.getEleNameEn());
			cotPanEle.setSize(cotOrderFacdetail.getEleInchDesc());
			cotPanEle.setCanPrice(cotOrderFacdetail.getPriceFac()==null?null:cotOrderFacdetail.getPriceFac().doubleValue());
			cotPanEle.setCanCurId(cotOrderFacdetail.getPriceFacUint());
			panList.add(cotPanEle);
			
		}
		this.getBaseDao().saveRecords(panList);
		List listPic=new ArrayList();
		for (int i = 0; i < panList.size(); i++) {
			CotPanEle cotPanEle=(CotPanEle) panList.get(i);
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) list.get(i);
			CotPanPic cotPanPic = new CotPanPic();
			CotElePic cotElePic = this.getOrderFacPicByFkId(cotOrderFacdetail.getId());
			cotPanPic.setEleId(cotOrderFacdetail.getEleId());
			cotPanPic.setFkId(cotPanEle.getId());
			cotPanPic.setPicImg(cotElePic.getPicImg());
			cotPanPic.setPicName(cotElePic.getPicName());
			cotPanPic.setPicSize(cotElePic.getPicSize());
			listPic.add(cotPanPic);
		}
		this.getBaseDao().saveRecords(listPic);
	}
	
	public void updatePanEleState(Integer panEleId,Integer state) throws DAOException{
		CotPanEle cotPanEle=(CotPanEle) this.getBaseDao().getById(CotPanEle.class, panEleId);
		cotPanEle.setState(state);
		List list=new ArrayList();
		list.add(cotPanEle);
		this.getBaseDao().updateRecords(list);
		//修改主单的状态
		CotPan cotPan=(CotPan) this.getBaseDao().getById(CotPan.class, cotPanEle.getPanId());
		if(state==1){
			cotPan.setStatus(2);
			List listPan=new ArrayList();
			listPan.add(cotPan);
			this.getBaseDao().updateRecords(listPan);
		}
		else{
			//查询其他询盘明细是否有被确认过
			String hql="select count(*) from CotPanEle obj where obj.state is not null and obj.state=1 and obj.panId="+cotPanEle.getPanId();
			List query=this.getBaseDao().find(hql);
			Integer count=(Integer) query.get(0);
			if(count==0){
				cotPan.setStatus(1);
				List listPan=new ArrayList();
				listPan.add(cotPan);
				this.getBaseDao().updateRecords(listPan);
			}
		}
	}
	
	public void savePIFromPanEles(List ids,Integer panId) throws DAOException{
		String str="";
		for (int i = 0; i < ids.size(); i++) {
			str+=ids.get(i)+",";
		}
		List panList=new ArrayList();
		String hql = "from CotOrderFacdetail obj where obj.id in ("+str.substring(0,str.length()-1)+")";
		List list =this.getBaseDao().find(hql);
		for (int i = 0; i < list.size(); i++) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) list.get(i);
			CotPanEle cotPanEle=new CotPanEle();
			cotPanEle.setPanId(panId);
			cotPanEle.setEleId(cotOrderFacdetail.getEleId());
			cotPanEle.setEleNameEn(cotOrderFacdetail.getEleNameEn());
			cotPanEle.setSize(cotOrderFacdetail.getEleInchDesc());
			cotPanEle.setCanPrice(cotOrderFacdetail.getPriceFac()==null?null:cotOrderFacdetail.getPriceFac().doubleValue());
			cotPanEle.setCanCurId(cotOrderFacdetail.getPriceFacUint());
			panList.add(cotPanEle);
			
		}
		this.getBaseDao().saveRecords(panList);
		List listPic=new ArrayList();
		for (int i = 0; i < panList.size(); i++) {
			CotPanEle cotPanEle=(CotPanEle) panList.get(i);
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) list.get(i);
			CotPanPic cotPanPic = new CotPanPic();
			CotElePic cotElePic = this.getOrderFacPicByFkId(cotOrderFacdetail.getId());
			cotPanPic.setEleId(cotOrderFacdetail.getEleId());
			cotPanPic.setFkId(cotPanEle.getId());
			cotPanPic.setPicImg(cotElePic.getPicImg());
			cotPanPic.setPicName(cotElePic.getPicName());
			cotPanPic.setPicSize(cotElePic.getPicSize());
			listPic.add(cotPanPic);
		}
		this.getBaseDao().saveRecords(listPic);
	}
	
	public List getPanEleIds(Integer panId){
		String hql="select obj from CotPanEle obj,CotPan e where obj.panId=e.id and e.id="+panId;
		return this.getBaseDao().find(hql);
	}
	
	public void updatePanEles(List<CotPanEle> list) throws DAOException{
//		WebContext ctx = WebContextFactory.get();
//		Integer empId = (Integer) ctx.getSession().getAttribute("empId");
//		cotPanEle.setModPerson(empId);
//		cotPanEle.setModDate(new Date());
//		List list=new ArrayList();
//		list.add(cotPanEle);
		this.getBaseDao().updateRecords(list);
	}
}
