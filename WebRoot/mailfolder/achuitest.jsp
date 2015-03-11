<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" -->
<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  -->
<%
String webapp=request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/gtgrid/gt_grid.css" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/gtgrid/skin/default/skinstyle.css" />

<script type="text/javascript" src="<%=webapp %>/common/gtgrid/gt_msg_cn.js"></script>


<script type="text/javascript" src="<%=webapp %>/common/gtgrid/gt_grid_all.js"></script>
<script type="text/javascript">
var gridid = "achui";
var dsOption= {

	fields :[
		{name : 'id'  },
		{name : 'areaName'  },
		{name : 'areaCode'  }
	]
}
var colsOption = [
		
		{id: 'id' , header: "编号" , width :50 },
		{id: 'areaName' , header: "地区" , width :80 },

		{id: 'areaCode' , header: "区号" , width :200, renderer : function(v){
				return '<a href="'+v+'">'+v+'</a>';
				
			}
		}

		
 ];
var gridOption={
	id : gridid,

	width: "600", 
	height: "300", 
	
	loadURL :"<%=webapp %>/cotarea.do?method=gtgrid",
	saveURL :"<%=webapp %>/cotarea.do?method=gtgrid",
	pageSize : 10,

	container : 'mygrid_container',

	//toolbarPosition : null,
	showGridMenu : true ,
	toolbarContent : 'nav | goto | pagesize | reload | add del save | state' ,
	pageSizeList : [ 1,5,10 ],
	showIndexColumn : true ,
	
	dataset : dsOption ,

	columns : colsOption ,
	clickStartEdit : true
	
};

var mygrid=new GT.Grid( gridOption );


GT.Utils.onLoad( GT.Grid.render(mygrid) );

//////////////////////////////////////////////////////////


</script>
</head>

<body style="padding:0px;margin:10px;">
<span style="font-size:14px;font-weight:bold;padding:3px;text-align:center;width:200px">GT-Grid 1.0 Demo List</span>

<div id="mygrid_container" style="border:0px solid #cccccc;background-color:#f3f3f3;padding:5px;width:100%;" >
</div>


 </body>
</html>
