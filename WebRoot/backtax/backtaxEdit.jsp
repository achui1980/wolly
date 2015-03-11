<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>退税单编辑页面</title>
		<!-- 导入公共js和css -->
				<jsp:include page="../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotBackTaxService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotAuditService.js'></script>
		<!-- 导入ECTable -->
				<!-- 合计行 -->
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/ux/css/GroupSummary.css" />
		<script type="text/javascript" src="<%=webapp %>/common/ext/ux/GroupSummary.js"></script>
		<style>
			.x-grid3-gridsummary-row-inner{overflow:hidden;width:100%;}/* IE6 requires width:100% for hori. scroll to work */
			.x-grid3-gridsummary-row-offset{width:10000px;}
			.x-grid-hide-gridsummary .x-grid3-gridsummary-row-inner{display:none;}
		</style>
	 	<script type='text/javascript' src='<%=webapp %>/backtax/js/backtaxEdit.js'></script>
	 	
	  <script type="text/javascript">
	  
			//添加
			function save(){
				var obj = DWRUtil.getValues('backtaxForm');
				var cotBackTax = new CotBacktax();
				for(var p in cotBackTax){
					if(p!='taxDate'){
						cotBackTax[p] = obj[p];
					}
				}
				
				var list = new Array();
				var gridPanel = Ext.getCmp("backtaxGrid");
				if(!gridPanel.isVisible())//新增操作
				{
					var isExist = checkTaxNo();
					if(isExist)
					{
						Ext.Msg.alert("提示信息","单号已存在");
						return;
					}
					cotBackTax.taxRealAmount = 0;
					cotBackTaxService.savaOrUpdateBackTax(cotBackTax,$('taxDate').value,list,function(res){
						if(res == -1)
							Ext.Msg.alert("信息提示","保存失败");
						else
						{
							Ext.Msg.alert("信息提示","保存成功");
							gridPanel.setVisible(true);
							$('mainId').value = res;
						}
					});
				}
				else//修改操作
				{
					var totalTui = summary.getSumTypeValue("totalTui");
					cotBackTax.taxRealAmount = totalTui;
					cotBackTax.id = $('mainId').value;
					cotBackTaxService.savaOrUpdateBackTax(cotBackTax,$('taxDate').value,list,function(res){
						if(res == -1)
							Ext.Msg.alert("信息提示","保存失败");
						else
						{
							Ext.Msg.alert("信息提示","保存成功");
						}
						var store = Ext.getCmp("backtaxGrid").getStore();
						store.proxy.setApi({
							read : "cotbacktax.do?method=addAudit&taxId="+$('mainId').value,
							create : "cotbacktax.do?method=modifAudit&taxId="+$('mainId').value,
							update : "cotbacktax.do?method=modifAudit&taxId="+$('mainId').value,
							destroy : "cotbacktax.do?method=delAudit&taxId="+$('mainId').value
						});
						
						store.save();
					});
				}
			}

			
			

			

	
			//验证退税单号是否存在
			function checkTaxNo(){
				DWREngine.setAsync(false)
				var isExist = false;
			    cotBackTaxService.findExistByNo($('taxNo').value,function(res){
						if(res!=null){
							isExist = true;
						}else{
							isExist = false;
						}
				})
				DWREngine.setAsync(true)
				return isExist;
			}
		</script>
	</head> 
	<%
		String id = request.getParameter("id");
	%>
	<body>
		<input type="hidden" name="mainId" id="mainId" value="<%=id%>" />
	</body>
</html>
