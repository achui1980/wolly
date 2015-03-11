package com.sail.cot.servlet;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipOutputStream;

import com.jason.core.Application;
import com.sail.cot.domain.CotElePic;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotGivenDetail;
import com.sail.cot.domain.CotGivenPic;
import com.sail.cot.domain.CotOrderDetail;
import com.sail.cot.domain.CotOrderFacdetail;
import com.sail.cot.domain.CotOrderPic;
import com.sail.cot.domain.CotOrderfacPic;
import com.sail.cot.domain.CotPriceDetail;
import com.sail.cot.domain.CotPricePic;
import com.sail.cot.domain.CotSignPic;
import com.sail.cot.service.img.CotOpImgService;
import com.sail.cot.service.sample.CotElementsService;
import com.sail.cot.util.SystemUtil;

public class DownPicsServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private Object getService(String strSerivce) {
		return Application.getInstance().getContainer()
				.getComponent(strSerivce);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		downFile(request, response);
	}
	
	private CotPriceDetail getPriceMapValue(HttpServletRequest request,String rdm) {
		Object obj = SystemUtil.getObjBySession(request.getSession(), "price");
		Map<String, CotPriceDetail> priceMap = (HashMap<String, CotPriceDetail>) obj;
		if (priceMap != null) {
			CotPriceDetail cotPriceDetail = (CotPriceDetail) priceMap.get(rdm);
			return cotPriceDetail;
		}
		return null;
	}
	
	private CotOrderDetail getOrderMapValue(HttpServletRequest request,String rdm) {
		Object obj = SystemUtil.getObjBySession(request.getSession(), "order");
		Map<String, CotOrderDetail> orderMap = (HashMap<String, CotOrderDetail>) obj;
		if (orderMap != null) {
			CotOrderDetail orderDetail = (CotOrderDetail) orderMap.get(rdm);
			return orderDetail;
		}
		return null;
	}
	
	public CotOrderFacdetail getOrderFacMapValue(HttpServletRequest request,String eleId) {
		Object obj = SystemUtil.getObjBySession(request.getSession(), "orderfac");
		HashMap<String, CotOrderFacdetail> orderFacMap = (HashMap<String, CotOrderFacdetail>) obj;
		if (orderFacMap != null) {
			CotOrderFacdetail cotOrderFacdetail = (CotOrderFacdetail) orderFacMap
					.get(eleId.toLowerCase());
			return cotOrderFacdetail;
		}
		return null;
	}
	
	public CotGivenDetail getGivenMapValue(HttpServletRequest request,String eleId) {
		Object obj = SystemUtil.getObjBySession(request.getSession(), "given");
		TreeMap<String, CotGivenDetail> givenMap = (TreeMap<String, CotGivenDetail>) obj;
		if (givenMap != null) {
			CotGivenDetail cotGivenDetail = (CotGivenDetail) givenMap.get(eleId
					.toLowerCase());
			return cotGivenDetail;
		}
		return null;
	}
	
	private void downFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//区分是报价还是订单.送样
		String tp = request.getParameter("tp");
		// 获得登录人
		CotEmps emp = (CotEmps) request.getSession().getAttribute("emp");
		if(tp!=null && ("custpc".equals(tp) || "orderpc".equals(tp))){
			
		}else{
			if (!"admin".equals(emp.getEmpsId())) {
				//最高权限
				boolean picSel = SystemUtil.isAction(request, "cotpicture.do", "SEL");
				if(picSel==false){
					response.setContentType("text/html");
					response.setCharacterEncoding("utf-8");
					PrintWriter printOut = response.getWriter();
					String alertStr = "Sorry,you can not DownLoad picture!";
					printOut.println("<script>alert('"+alertStr+"')</script>");
					printOut.flush();
					printOut.close();
					return;
				}
			}
		}
		String priceNo = request.getParameter("priceNo");
		String type = request.getParameter("type");//导出格式
		String page = request.getParameter("page");//是否导出表格全部
		//样品,报价.订单.送样,生产合同传递的字符串
		String rdms = request.getParameter("rdms");
		
		//查询条件
		StringBuffer queryString = new StringBuffer();
		queryString.append(" where 1=1");
		String eleId = request.getParameter("eleIdFind");// 样品编号
		String child = request.getParameter("childFind");// 子货号标识
		String eleName = request.getParameter("eleNameFind");// 中文名
		String eleNameEn = request.getParameter("eleNameEnFind");// 英文名
		String factoryId = request.getParameter("factoryIdFind");// 厂家
		String eleCol = request.getParameter("eleColFind");// 颜色
		String startTime = request.getParameter("startTime");// 起始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String eleTypeidLv1 = request.getParameter("eleTypeidLv1Find");// 大类
		String eleGrade = request.getParameter("eleGradeFind");// 等级
		String eleForPerson = request.getParameter("eleForPersonFind");// 开发对象
		String eleTypeidLv2 = request.getParameter("eleTypeidLv2");// 中类
		String eleTypeidLv3 = request.getParameter("eleTypeidLv3");// 小类
		String eleHsid = request.getParameter("eleHsid");// 海关编码
		String eleDesc = request.getParameter("eleDescFind");// 产品描述
		String eleTypenameLv2 = request.getParameter("eleTypenameLv2");// 所属年份
		String eleSizeDesc = request.getParameter("eleSizeDesc");// 中文规格
		String eleInchDesc = request.getParameter("eleInchDesc");// 英文规格
		String boxLS = request.getParameter("boxLS");// 产品起始长
		String boxLE = request.getParameter("boxLE");// 产品结束长
		String boxWS = request.getParameter("boxWS");// 产品起始宽
		String boxWE = request.getParameter("boxWE");// 产品结束宽
		String boxHS = request.getParameter("boxHS");// 产品起始高
		String boxHE = request.getParameter("boxHE");// 产品结束高
		
		if (eleTypeidLv2 != null && !eleTypeidLv2.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV2="
					+ eleTypeidLv2.toString());
		}
		if (eleTypeidLv3 != null && !eleTypeidLv3.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV3="
					+ eleTypeidLv3.toString());
		}
		if (eleHsid != null && !eleHsid.toString().equals("")) {
			queryString.append(" and e.HS_ID="
					+ eleHsid.toString());
		}
		if (eleDesc != null && !eleDesc.toString().equals("")) {
			queryString.append(" and e.ELE_DESC like '%"
					+ eleDesc.toString().trim() + "%'");
		}
		if (eleTypenameLv2 != null && !eleTypenameLv2.toString().equals("")) {
			queryString.append(" and e.ELE_TYPENAME_LV2=" + eleTypenameLv2.toString());
		}
		
		if (eleSizeDesc != null && !eleSizeDesc.toString().equals("")) {
			queryString.append(" and e.ELE_SIZE_DESC like '%"
					+ eleSizeDesc.toString().trim() + "%'");
		}
		if (eleInchDesc != null && !eleInchDesc.toString().equals("")) {
			queryString.append(" and e.ELE_INCH_DESC like '%"
					+ eleInchDesc.toString().trim() + "%'");
		}
		if (eleId != null && !eleId.toString().trim().equals("")) {
			queryString.append(" and e.ELE_ID like '%"
					+ eleId.toString().trim() + "%'");
		}
		if (child != null && child.toString().trim().equals("true")) {
			queryString.append(" and e.ELE_FLAG=2");
		}else{
			queryString.append(" and e.ELE_FLAG!=2");
		}
		if (eleName != null && !eleName.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME like '%"
					+ eleName.toString().trim() + "%'");
		}
		if (eleNameEn != null && !eleNameEn.toString().trim().equals("")) {
			queryString.append(" and e.ELE_NAME_EN like '%"
					+ eleNameEn.toString().trim() + "%'");
		}
		if (factoryId != null && !factoryId.toString().equals("")) {
			queryString.append(" and e.FACTORY_ID=" + factoryId.toString());
		}
		if (eleCol != null && !eleCol.toString().trim().equals("")) {
			queryString.append(" and e.ELE_COL like '%"
					+ eleCol.toString().trim() + "%'");
		}
		if (eleTypeidLv1 != null && !eleTypeidLv1.toString().equals("")) {
			queryString.append(" and e.ELE_TYPEID_LV1="
					+ eleTypeidLv1.toString());
		}
		if (eleGrade != null && !eleGrade.toString().equals("")) {
			queryString.append(" and e.ELE_GRADE like '%"
					+ eleGrade.toString().trim() + "%'");
		}
		
		if (eleForPerson != null && !eleForPerson.toString().equals("")) {
			queryString.append(" and e.ELE_FOR_PERSON like '%"
					+ eleForPerson.toString().trim() + "%'");
		}

		if (startTime != null && !"".equals(startTime.trim())) {
			queryString
					.append(" and e.ELE_ADD_TIME >='" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime.trim())) {
			queryString.append(" and e.ELE_ADD_TIME <='" + endTime
					+ " 23:59:59'");
		}
		
		if (boxLS != null && boxLE != null) {
			if (!"".equals(boxLS.trim()) && !"".equals(boxLE.trim())) {
				queryString.append(" and e.BOX_L between "
						+ boxLS + " and " + boxLE);
			}
		}
		
		if (boxWS != null && boxWE != null) {
			if (!"".equals(boxWS.trim()) && !"".equals(boxWE.trim())) {
				queryString.append(" and e.BOX_W between "
						+ boxWS + " and " + boxWE);
			}
		}
		
		if (boxHS != null && boxHE != null) {
			if (!"".equals(boxHS.trim()) && !"".equals(boxHE.trim())) {
				queryString.append(" and e.BOX_H between "
						+ boxHS + " and " + boxHE);
			}
		}
		
		if("all".equals(page)){
			CotElementsService elementsService = (CotElementsService)this.getService("CotElementsService");
			rdms=elementsService.findEles(queryString.toString());
		}
		
		if(tp.equals("custpc") || tp.equals("orderpc")){
			priceNo = request.getParameter("customerNo");
		}
		
		
		//弹出保存对话框
		response.setContentType("application/octet-stream; CHARSET=utf8");
		response.setHeader("Content-Disposition", "attachment; filename="+priceNo+"_pics.zip");	
		response.setHeader("Pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		
		//打包后文件名字 直接存到该地址
		//String zipFileName = "c:\\test.zip";
		//ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		
		CotOpImgService opImgService = (CotOpImgService)this.getService("CotOpImgService");
		
		if(tp.equals("custpc")){
			String str = request.getParameter("str");
			String[] filePaths = str.split(",");
			String tmp = "artWorkPdf/client/";
			for (int i = 0; i < filePaths.length; i++) {
				String filePath=filePaths[i];
				try {
					opImgService.setZipOutByCustPc(out, filePath,tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(tp.equals("orderpc")){
			String str = request.getParameter("str");
			String[] filePaths = str.split(",");
			String tmp = "artWorkPdf/order/";
			for (int i = 0; i < filePaths.length; i++) {
				String filePath=filePaths[i];
				try {
					opImgService.setZipOutByCustPc(out, filePath,tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			String[] rdmAry = rdms.split(",");
			for (int i = 0; i < rdmAry.length; i++) {
				String flag ="";
				Integer detailId =0;
				String fileName = null;
				if(tp.equals("elements")){
					flag="ele";
					detailId =Integer.parseInt(rdmAry[i]);
				}
				
				if(tp.equals("price")){
					CotPriceDetail priceDetail = this.getPriceMapValue(request,rdmAry[i]);
					flag =priceDetail.getType();
					if(flag==null){
						flag="price";
					}
					detailId =priceDetail.getSrcId();
					if(detailId==null){
						detailId = priceDetail.getId();
					}
					fileName=priceDetail.getEleId();
				}
				if(tp.equals("order")){
					CotOrderDetail orderDetail = this.getOrderMapValue(request,rdmAry[i]);
					flag =orderDetail.getType();
					if(flag==null){
						flag="order";
					}
					detailId =orderDetail.getSrcId();
					if(detailId==null){
						detailId = orderDetail.getId();
					}
					fileName=orderDetail.getEleId();
				}
				if(tp.equals("orderfac")){
					CotOrderFacdetail orderDetail = this.getOrderFacMapValue(request,rdmAry[i]);
					if(orderDetail!=null){
						fileName=orderDetail.getEleId();
						if(orderDetail.getId()==null){
							flag="elePic";
						}else{
							flag="orderfac";
							detailId = orderDetail.getId();
						}
					}
				}
				if(tp.equals("given")){
					CotGivenDetail givenDetail = this.getGivenMapValue(request,rdmAry[i]);
					if(givenDetail!=null){
						fileName=givenDetail.getEleId();
						if(givenDetail.getId()==null){
							flag="elePic";
						}else{
							flag="given";
							detailId = givenDetail.getId();
						}
					}
				}
				
				byte[] picImg = null;
				
				if(flag.equals("ele")){
					CotElePic cotElePic = opImgService.getElePicImgByEleId(detailId);
					if(cotElePic != null) {
						picImg =  cotElePic.getPicImg();
						fileName=cotElePic.getEleId();
					}
				}
				//送样通过货号获取样品图片
				else if (flag.equals("elePic")) {
					CotElePic cotElePic = opImgService.getElePicImgByEleName(fileName);
					if(cotElePic != null) {
						picImg =  cotElePic.getPicImg();	
					}
				}
				else if (flag.equals("given")) {
					CotGivenPic cotGivenPic = opImgService.getGivenPic(detailId);
					picImg = cotGivenPic.getPicImg();
				}
				else if (flag.equals("sign")) {
					CotSignPic cotElePic = opImgService.getSignPic(detailId);
					picImg = cotElePic.getPicImg();
				}
				else if (flag.equals("price")) {
					CotPricePic cotElePic = opImgService.getPricePic(detailId);
					picImg = cotElePic.getPicImg();
				}
				else if (flag.equals("order")) {
					CotOrderPic cotElePic = opImgService.getOrderPic(detailId);
					picImg = cotElePic.getPicImg();
				}
				else if (flag.equals("orderfac")) {
					CotOrderfacPic cotElePic = opImgService.getOrderFacPic(detailId);
					picImg = cotElePic.getPicImg();
				}
				//去掉"没有图片"的图片
				if(picImg!=null && picImg.length!=12055){
					String base = fileName+"."+type;
					out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
					out.write(picImg);
				}
			}
		}
		
		out.flush();
		out.close();
		out = null;
		
	}

}
