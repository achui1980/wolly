<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/e3/table/E3Table.tld" prefix="e3t"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>模块列表</title>
	</head>
	<jsp:include page="../../common.jsp"></jsp:include>

	<script type="text/javascript">
	//表格显示前,通常在这注册单击，双击事件
function moduleTableRenderBeforeHandler(pGrid){

  pGrid.on("rowdblclick", function( pGrid, pRowIndex, pEventObj){
     var record = pGrid.getStore().getAt(pRowIndex);   //Get the Record
	 var obj = record.get("id");//获取单元格值
     windowopen(obj);
  });
  
  
}
		function windowopen(obj){
			openWindow('cotmodule.do?method=queryModule&id='+obj);
		}
	</script>
	<body>
		<e3t:table id="moduleTable" items="cotmodule" caption="模块列表"
			var="module" uri="cotmodule.do?method=queryList"
			noDataTip="没有记录，<span style='color:red;cursor:hand' onclick='windowopen(0)'>点击添加模块信息</span>!" mode="ajax">
			<e3t:column property="moduleName" title="模块名" />
			<e3t:column property="moduleUrl" title="作用路径" />
			<e3t:column property="moduleImgurl" title="关联图片" />
			<e3t:column property="moudleParent" title="父类模块"
				mappingItem="allModuleName" />
			<e3t:column property="moduleLv" title="模块级别" />
			<e3t:column property="moudleType" title="模块类型" />
			<e3t:column property="moudleOrder" title="模块排序" />
			<e3t:row>
				<e3t:attribute name="onmouseover"
					value="this.style.backgroundColor = '#CCCCFF'" />
				<e3t:attribute name="onmouseout"
					value="this.style.backgroundColor = ''" />
				<!-- 点击事件 -->
				<e3t:attribute name="ondblclick" value="windowopen(${module.id})" />
			</e3t:row>
		</e3t:table>

	</body>
</html>
