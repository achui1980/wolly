

	
var mapPopedom = null;
function checkPopedom(url, hasTabel) {
	//alert(url);
	if (typeof (url) == "undefined") {
		alert("no url");
		return;
	}
	//alert(mapPopedom);
	DWREngine.setAsync(false);
	if (mapPopedom == null) {
		cotPopedomService.getPopedomByMenu(url, function (res) {
			mapPopedom = res;
		});
	}
	
	if (loginEmpId == "admin") {//admin用户不做权限判断
		DWREngine.setAsync(true);
		return;
	}
	judgePopedom(mapPopedom);
	DWREngine.setAsync(true);
}
function getPopedomByOpType(url, opType,bReloadMap) {
	if (loginEmpId == "admin") {//admin用户不做权限判断
		return 1;
	}
	DWREngine.setAsync(false);
	var result = 1;
	if (mapPopedom == null) {
		cotPopedomService.getPopedomByMenu(url, function (res) {
			mapPopedom = res;
		});
	}
	if(bReloadMap){
		cotPopedomService.getPopedomByMenu(url, function (res) {
			mapPopedom = res;
		});
	}
	try{
		var res = mapPopedom[opType];
		if (typeof (res) == "undefined") {
			result = 0;
		}
	}catch(e){
		return 0;
	}
	DWREngine.setAsync(true);
	return result;
}
//同一个页面内查询别的模块权限
function getPdmByOtherUrl(url, opType) {
	if (loginEmpId == "admin") {//admin用户不做权限判断
		return 1;
	}
	DWREngine.setAsync(false);
	var result = 1;
	cotPopedomService.getPopedomByMenu(url, function (res) {
		try{
			var temp = res[opType];
			if (typeof (temp) == "undefined") {
				result = 0;
			}
		}catch(e){
			result = 0;
		}
	});
	DWREngine.setAsync(true);
	return result;
	
}

function judgePopedom(map) {
	if (map == null) {
		//alert(vaildUrl)
		Ext.MessageBox.alert('Message',"Sorry, you do not have Authority!");
		window.location.href = "/CotSystem/common/home.jsp";
		return;
	}
	if (typeof (map.SEL) == "undefined") {
		Ext.MessageBox.alert('Message',"Sorry, you do not have Authority!");
		window.location.href = "/CotSystem/common/home.jsp";
		return;
	}
	
	//获取需要进行权限判断的按钮，因为Ext的Button封装在table，故用该表达式获取
	//SYSOP，是系统规定的标识符，查询包含这个标识符的所有按钮
	var eles = Ext.query("table[class*=SYSOP]");
	for (var i = 0; i < eles.length; i++) {
		var btn = eles[i];
		var src = btn.className;
		//判断是否有添加权限
		if (typeof (map.ADD) == "undefined") {
			if (src.indexOf("SYSOP_ADD") > -1) {
				btn.style.display = "none";
			}
			//另存为 也不显示
			if (src.indexOf("_saveas") > -1) {
				btn.style.display = "none";
			}
		}
		//判断是否有修改权限
		if (typeof (map.MOD) == "undefined") {
			if (src.indexOf("SYSOP_MOD") > -1 ) {
				btn.style.display = "none";
			}
		}
		//判断是否有excel权限
		if (typeof (map.EXP) == "undefined") {
			if (src.indexOf("SYSOP_EXP") > -1 ) {
				btn.style.display = "none";
			}
		}
		
		//判断是否有添加修改权限
		if (typeof (map.ADD) == "undefined" && typeof (map.MOD) == "undefined") {
			if (src.indexOf("_save") > -1 ) {
				btn.style.display = "none";
			}
		}
			
		
		//判断是否有删除权限
		if (typeof (map.DEL) == "undefined") {
			if (src.indexOf("SYSOP_DEL") > -1) {
				btn.style.display = "none";
				
			}
//			if (src.indexOf("_delrows") > -1) {
//				btn.style.display = "none";
//				
//			}
		}
		//判断是否有打印权限
		if (typeof (map.PRINT) == "undefined") {
			if (src.indexOf("SYSOP_PRINT") > -1) {
				btn.style.display = "none";
				
			}
//			if (src.indexOf("bar_detail_print") > -1) {
//				btn.style.display = "none";
//				//img.parentNode.parentNode.style.display = "none";
//			}
//			if (src.indexOf("query_output") > -1) {
//				btn.style.display = "none";
//			}
//			if (src.indexOf("_prints") > -1) {
//				btn.style.display = "none";
//				
//			}
		}
		//判断是否有图片浏览权限
		if(typeof (map.PICLIST) == "undefined"){
			if (src.indexOf("bar_pic") > -1) {
				btn.style.display = "none";
			}	
		}
		//判断是否有分解订单权限
		if(typeof (map.FEN) == "undefined"){
			if (src.indexOf("SYSOP_FEN") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有Excel导入权限
		if(typeof(map.EXCEL) == "undefined"){
			if (src.indexOf("SYSOP_EXCEL") > -1) {
				btn.style.display = "none";
			}	
		}

		
		//是否有公共邮箱启动权限
		if(typeof(map.PUBLICSTART) == "undefined"){
			if (src.indexOf("SYSOP_PUBLICSTART") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有公共邮箱重启权限
		if(typeof(map.PUBLICRESTART) == "undefined"){
			if (src.indexOf("SYSOP_PUBLICRESTART") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有私人邮箱启动权限
		if(typeof(map.PRIVATESTART) == "undefined"){
			if (src.indexOf("SYSOP_PRIVATESTART") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有私人邮箱重启权限
		if(typeof(map.PRIVATERESTART) == "undefined"){
			if (src.indexOf("SYSOP_PRIVATERESTART") > -1) {
				btn.style.display = "none";
			}	
		}

		//是否有指派权限
		if(typeof(map.ASSIGN) == "undefined"){
			if (src.indexOf("SYSOP_ASSIGN") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有权限
		if(typeof(map.PRE) == "undefined"){
			if (src.indexOf("SYSOP_PRE") > -1) {
				btn.style.display = "none";
			}	
		}
		
		//是否有权限
		if(typeof(map.ARTWORK) == "undefined"){
			if (src.indexOf("SYSOP_ARTWORK") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有权限
		if(typeof(map.SAMPLES) == "undefined"){
			if (src.indexOf("SYSOP_SAMPLES") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有权限
		if(typeof(map.QC) == "undefined"){
			if (src.indexOf("SYSOP_QC") > -1) {
				btn.style.display = "none";
			}	
		}
		//是否有权限
		if(typeof(map.SHIPMENT) == "undefined"){
			if (src.indexOf("SYSOP_SHIPMENT") > -1) {
				btn.style.display = "none";
			}	
		}

	}
}
function getLoginEmpid() {
	DWREngine.setAsync(false);
	cotPopedomService.getLoginEmpId(function (res) {
		loginEmpId = res;
	});
	cotPopedomService.getLogId(function (res) {
		logId = res;
	});
	cotPopedomService.getLoginEmp(function (res) {
		loginEmp = res;
	});
	if (loginEmpId == null) {
		Ext.MessageBox.alert('Message',"Log out, please re-visit");
		DWREngine.setAsync(true);
		var url = document.location.href;
		var idx = url.indexOf("/");
		url = url.substring(0,idx);
		window.location.href = url+"/index.do?method=logoutAction"
		return;
	}
	DWREngine.setAsync(true);
}
var vaildUrl = "";
var loginEmpId = "";
var logId = "";
var loginEmp = null;
getLoginEmpid();
var soft_ver = -1;
//获取软件版本
function getSoftVer()
{
	DWREngine.setAsync(false);
	if(soft_ver == -1)
	{
		cotModuelService.getSoftVer(function(res){
			soft_ver = res;
		});
	}
	if(soft_ver != 4)
	{
		//不是企业标准版，取消邮件发送按钮
		var img = document.getElementById("sendMBtn");
		//不是企业版，屏蔽财务功能
		var finace = document.getElementById("finace");
		//屏蔽，其他费用中的生成按钮
		var genFinace = document.getElementById("genFinace");
		if(img)
		{
			var alink = img.parentNode;
			if(alink)
				alink.style.display = "none";
		}
		if(finace)
			finace.style.display="none";
		if(genFinace)
			genFinace.style.display = "none";
	}
	DWREngine.setAsync(true);
}
function oninitpageload() {
	vaildUrl = document.location.href;
	var endindex = vaildUrl.indexOf("?");
	var beginindex = vaildUrl.lastIndexOf("/");
	if(endindex == -1)
		vaildUrl = vaildUrl.substring(beginindex + 1);
	else{
		//统计分析的权限控制,?后面传递的参数含有"/"
		if(beginindex>endindex){
			 beginindex = vaildUrl.lastIndexOf("/",endindex);
		}
		vaildUrl = vaildUrl.substring(beginindex + 1, endindex);
	}
	//alert(vaildUrl);
	
	//样品权限判断
	if("elementsframe.jsp"==vaildUrl){
		vaildUrl="cotelements.do";
	}
	
	//样品图片权限判断
	if("elepiclist.jsp"==vaildUrl){
		vaildUrl="cotpicture.do";
	}
	
	//过滤菜单模块和通用控件查询模块
	if (vaildUrl == "cotmodule.do" || vaildUrl == "cotquery.do" || vaildUrl=="previewrpt.do" || vaildUrl=="cotElePicShow.do" || vaildUrl == "setNo.jsp" 
	|| vaildUrl == "importPicTemp.jsp" || vaildUrl == "eletypetree.jsp" || vaildUrl == "defineReport.jsp"
	|| vaildUrl == "customervisitedlog.do" || vaildUrl == "cotfaq.do") {
		return;
	}
	getSoftVer();//邮件权限判断
	
	//如果是配件分析,查看员工是否有配件合同管理的权限
	if("cotfitanys.do"==vaildUrl){
		vaildUrl="cotfittingorder.do";
	}
	//如果是包材分析,查看员工是否有包材合同管理的权限
	if("cotpackanys.do"==vaildUrl){
		vaildUrl="cotpackingorder.do";
	}
	
	if("cotmailsend.do"==vaildUrl){
		vaildUrl="cotmail.do";
	}
	
	checkPopedom(vaildUrl, true);
}
Ext.onReady(function(){
//初始化
oninitpageload();
});