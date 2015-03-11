package com.sail.cot.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.jason.core.exception.DAOException;
import com.sail.cot.dao.CotBaseDao;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotOrderPc;
import com.sail.cot.domain.CotOrg;
import com.sail.cot.query.QueryInfo;
import com.sail.cot.service.CotOrgService;
import com.sail.cot.service.order.impl.CotOrderServiceImpl;
import com.sail.cot.util.SystemUtil;

public class CotOrgServiceImpl implements CotOrgService {

	private CotBaseDao orgDao;

	public CotBaseDao getOrgDao() {
		return orgDao;
	}

	public void setOrgDao(CotBaseDao orgDao) {
		this.orgDao = orgDao;
	}

	public String getJsonData(QueryInfo queryInfo) throws DAOException {
		return this.getOrgDao().getJsonData(queryInfo);
	}

	public Integer saveOrg(CotOrg e) throws DAOException {
		WebContext ctx = WebContextFactory.get();
		CotEmps cotEmps = (CotEmps) ctx.getSession().getAttribute("emp");
		if (e.getId() == null) {
			e.setEmpId(cotEmps.getId());
			e.setUploadTime(new Date());
			List imgList = new ArrayList();
			imgList.add(e);
			this.getOrgDao().saveRecords(imgList);
		} else {
			String classPath = CotOrderServiceImpl.class.getResource("/")
					.toString();
			String systemPath = classPath.substring(6, classPath.length() - 16);
			CotOrg old = (CotOrg) this.getOrgDao().getById(CotOrg.class,
					e.getId());
			// 如果pdf文件名不一致,删除旧的pdf
			if (!old.getFilePath().equals(e.getFilePath())) {
				File file = new File(systemPath + "artWorkPdf/order/"
						+ old.getFilePath());
				file.delete();
				old.setUploadTime(new Date());
				old.setFilePath(e.getFilePath());
			}
			e.setEmpId(cotEmps.getId());
			old.setRemark(e.getRemark());
			List list = new ArrayList();
			list.add(old);
			this.getOrgDao().saveOrUpdateRecords(list);
		}
		return e.getId();
	}

	public Boolean deleteOrgs(List<Integer> ids) throws DAOException {
		// 获取系统路径
		String classPath = CotOrderServiceImpl.class.getResource("/")
				.toString();
		String systemPath = classPath.substring(6, classPath.length() - 16);
		List files = new ArrayList();
		for (int i = 0; i < ids.size(); i++) {
			Integer id = (Integer) ids.get(i);
			CotOrg cotOrg = (CotOrg) this.getOrgDao().getById(CotOrg.class, id);
			files.add(cotOrg.getFilePath());
		}
		this.getOrgDao().deleteRecordByIds(ids, "CotOrg");
		// 删除文件
		for (int i = 0; i < files.size(); i++) {
			File file = new File(systemPath + "artWorkPdf/order/"
					+ (String) files.get(i));
			file.delete();
		}
		return true;
	}

	public CotOrg getOrgById(Integer id) {
		CotOrg cotOrg = (CotOrg) this.getOrgDao().getById(CotOrg.class, id);
		return cotOrg;
	}
}
