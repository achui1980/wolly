<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>单据核销编辑页面</title>
		<!-- 导入公共js和css -->
				<jsp:include page="../extcommon.jsp"></jsp:include>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotAuditService.js'></script>
	<script type='text/javascript' src='<%=webapp %>/audit/js/auditEdit.js'></script>
	  <script type="text/javascript">
			//添加
			function save(){	
				var obj = DWRUtil.getValues('auditForm');
				var cotAudit = new CotAudit();
				for(var p in obj){
					if(p!='receiveDate' && p!='effectDate' && p!='usedDate' && p!='submitDate' && p!='auditDate' && p!='balanceDate'){
						cotAudit[p] = obj[p];
					}
					else
					{
						if(obj[p])
							cotAudit[p] = new Date(obj[p]);
					}
				}
				var id = $('mainId').value;
				if(id!='null' && id!=''){
					cotAudit.id = id;
				}
				cotAudit.invoiceNo = Ext.getCmp("invoiceNo_cmp").getRawValue();
				cotAuditService.savaOrUpdateAudit(cotAudit,function(res)
				{
					if(res==false){
						Ext.Msg.alert("提示信息","保存失败!");
					}else{
						Ext.Msg.alert("提示信息","保存成功！");
						closeandreflashEC('true','auditGrid',false);
					}
				})
			}
		</script>
	</head> 
	<%
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
	%>
	<body>
	
		<!-------------------------- 查找员工的记录的隐藏层(结束) ------------------------------------->
		<input type="hidden" name="mainId" id="mainId" value="<%=id%>" />
		<input type="hidden" name="flag" id="flag" value="<%=flag%>" />
	</body>
</html>
