<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>修改消息类型</title>
		<jsp:include page="../../common.jsp"></jsp:include>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotMsgTypeService.js'></script>
		<link rel="stylesheet" type="text/css" href="<%=webapp %>/ecside/css/ecside_style.css" />
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside.js" ></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/prototype_mini.js" ></script>
		<script type="text/javascript" src="<%=webapp %>/ecside/js/ecside_msg_utf8_cn.js" ></script>
		<script type="text/javascript">
 //权限判断变量，对应于cot_module表中的URL字段
var url = "cotmessagetype.do?method=query";

	function  del()
	{   
		var flag = window.confirm("确定删除此消息类型吗?");
	    if(flag){
	       	var obj = DWRUtil.getValues("queryMsgTypeForm");
		    var cotMsgType = new CotMsgType();
		    var list = new Array();
			for(var p in obj){
					cotMsgType[p] = obj[p];
				} 
		     cotMsgType.id = $("msgtypeid").value;
		     list.push(cotMsgType)
			 cotMsgTypeService.deleteMsgType(list,function(res){
				result = res;
				if(result == -1)
			    {
			    	Ext.Msg.alert("提示信息","已有其它记录使用到该记录,无法删除")
			    	return; 
			    }			 
				Ext.Msg.alert("提示信息","删除成功!");
				//ECSideUtil.reload('commisionTable');
				closeandreflashEC(true,"msgTypeTable",false);
			})
	    }else{
	      return;
	    }
	 }
	
   function mod()
	{
	    //表单验证
	    var form = document.forms["queryMsgTypeForm"];
		var b = false;
		b = Validator.Validate(form,1);
		if(!b)
		{
			return;
		}
		//表单验证结束
		    var obj = DWRUtil.getValues("queryMsgTypeForm");
	        var cotMsgType = new CotMsgType();
			var list = new Array();
			for(var p in obj)
			{
				cotMsgType[p] = obj[p];
			} 
		     cotMsgType.id =  DWRUtil.getValue("msgtypeid");
		  	 list.push(cotMsgType);
		     cotMsgTypeService.modifyMsgType(list,function(res){
		     if(res){
				 Ext.Msg.alert("提示信息","修改成功!");
				 closeandreflashEC(true,"msgTypeTable",false);
			　   // reflashLocal(true,"commisionTable");
			}else{
				Ext.Msg.alert("提示信息","修改失败，该消息类型已经存在！");
			}
			})
	 }

 
	//初始化表单验证字段
		function validForm()
			{
				appendattribute("msgTypeName","消息类型名称不能为空!","Require");
			}
		
	function initForm()
		{       
		      DWRUtil.useLoadingMessage("正在加载数据，请等待..........."); 
		      validForm();
		      DWREngine.setAsync(false); 
		      var id = $("msgtypeid").value;
			  cotMsgTypeService.getMsgTypeById(parseInt(id),function(res){
				 var obj = res;
				 DWRUtil.setValues(obj);
			  });
			 DWREngine.setAsync(true);
		}
	  
</script>
		<style type="text/css">
</style>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="initForm()">
		<table style="width: 100%;">
		<div class="navline">
			<label>
				&nbsp;
			</label>
		</div>			
			<tr>
				<td align="center">
					<form name="queryMsgTypeForm" id="queryMsgTypeForm">
						<div style="width: 100%;margin-top:10px;">
							<label class="label_25" style="color: red;text-align:right">
								消息类型：
							</label>
							<input class="input_65" type="text" name="msgTypeName"
								id="msgTypeName" />
						</div>
						<div style="width: 100%;">
							<label class="label_25" style="text-align:right;color:#000;">
								备注：
							</label>
							<textarea name="msgTypeRemark" rows="3" cols="50" class="input_65"
								id="msgTypeRemark"></textarea>
						</div>
					</form>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div style="margin-left:10%;margin-top:5px;">&nbsp;
						<a onclick="mod()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_modi.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="del()" style="cursor: hand;"><img
								src="<%=webapp %>/common/images/_del.gif" border="0"
								height="21px" width="61px"></a>	&nbsp;&nbsp;
						<a onclick="javascript:closeandreflashEC(true,'msgTypeTable',false);" 
							style="cursor: hand;">
						<img src="<%=webapp %>/common/images/_cancel.gif" border="0"
								height="21px" width="61px"></a>	
					<!-- 
						<button class="modBtn" onclick="mod()" />修改
						</button>
						<button class="delBtn" onclick="del()" />删除
						</button>
						<button class="cancelBtn"
							onclick="javascript:window.close();">关闭
						</button>
						-->
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="msgtypeid" id="msgtypeid" value="<%=id%>" />
	</body>
</html>
