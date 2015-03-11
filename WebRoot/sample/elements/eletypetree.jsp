<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String webapp = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+webapp+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Left Side Of Products Page</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>

		<script type="text/javascript"
			src="<%=webapp%>/common/jquery/jquery.js"></script>
		<script type="text/javascript"
			src="<%=webapp%>/common/jquery/ddaccordion.js"></script>

		<script type="text/javascript">
		ddaccordion.init({
	headerclass: "submenuheader", //Shared CSS class name of headers group
	contentclass: "submenu", //Shared CSS class name of contents group
	revealtype: "click", //Reveal content when user clicks or onmouseover the header? Valid value: "click" or "mouseover
	mouseoverdelay: 200, //if revealtype="mouseover", set delay in milliseconds before header expands onMouseover
	collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
	defaultexpanded: [], //index of content(s) open by default [index1, index2, etc] [] denotes no content
	onemustopen: false, //Specify whether at least one header should be open always (so never all headers closed)
	animatedefault: true, //Should contents open by default be animated into view?
	persiststate: false, //persist state of opened contents within browser session?
	toggleclass: ["", ""], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
	togglehtml: ["suffix", "<img src='<%=webapp %>/common/jquery/images/plus.gif' class='statusicon' />", "<img src='<%=webapp %>/common/jquery/images/minus.gif' class='statusicon' />"], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
	animatespeed: 100, //speed of animation: integer in milliseconds (ie: 200), or keywords "fast", "normal", or "slow"
	oninit:function(headers, expandedindices){ //custom code to run when headers have initalized
//		myiframe=window.frames["myiframe"]
//		if (expandedindices.length>0) //if there are 1 or more expanded headers
//			myiframe.location.replace(headers[expandedindices.pop()].getAttribute('href')) //Get "href" attribute of final expanded header to load into IFRAME
		return;
	},
	onopenclose:function(header, index, state, isuseractivated){ //custom code to run whenever a header is opened or closed
		if (state=="none" && isuseractivated == true){ //if header is expanded and as the result of the user initiated action
//			myiframe.location.replace(header.getAttribute('href'))
			//alert(index);
			//var ul = header.parentNode.getElementsByTagName("ul")[index];
			//if(ul)
				//ul.innerHTML = "";
		}
		return;
	}
})

var isQueryQuery = false;//是否已经按材质查询过
var isQueryFac = false; //是否已经按厂家查询过
var isQueryType = false;//是否已经按材质查询过

  		  function getTreeHtml(type)
  		  {
  		  	if(type == "query" && isQueryQuery) return;
  		  	if(type == "factory" && isQueryFac) return;
  		  	if(type == "type" && isQueryType) return;
  		  	if(type == "query")
  		  	{
  		  		 isQueryQuery = true;
  		  	}
  		  	if(type == "factory")
  		  	{
  		  		 frame = document.myiframeFac;
  		  		 isQueryFac = true;
  		  		 frame.location.href = "<%=webapp %>/cotelements.do?method=query&type="+type;
  		  	}
  		  	else if(type == "type")
  		  	{
  		  		 frame = document.myiframeType;	
  		  		 isQueryType = true;
  		  		 frame.location.href = "<%=webapp %>/cotelements.do?method=query&type="+type;
  		  	}
  		  }
  		  
		//查询方法
		function query()
		{
			var params = {};
			//验证表单
			var formEle = document.forms['queryForm'];
			var b = false;
			b = Validator.Validate(formEle,1);
			if(!b){
				return;
			}
			
			var form = DWRUtil.getValues("queryForm");
			for(var p in form){
				params[p] = form[p];
			}
	   	 	parent.mainF.paramsQuery=params;
	   	 	//alert(form["eleTypeidLv2"]);
	   	 	//printObject(params);
	   	 	parent.mainF.querylist();
	   	 	//parent.mainF.insertQueryToSel(params);
		}
		
		//重置
		function rset()
		{
			clearForm("queryForm");
		}
		
		//点击树查询
		function setQueryValue(factoryId,typeId)
		{
			DWRUtil.setValue("factoryIdFind",factoryId);
			DWRUtil.setValue("eleTypeidLv1Find",typeId);
			query();
		}
		
		//按回车按货号查询
		function queryByEleId(){
			if(event.keyCode == 13){
				query();
			}
		}
		
		// 点击选择模糊查询结果后方法
		function handleComon(id, val, fno, txtId) {
			if(txtId=='factoryShortName'){
				document.getElementById('factoryIdFind').value = id;
				document.getElementById('factoryShortName').value = val;
			}
			if(txtId=='typeName'){
				document.getElementById('eleTypeidLv1Find').value = id;
				document.getElementById('typeName').value = val;
			}
			if (txtId == 'typeName2') {
				document.getElementById('eleTypeidLv2').value = id;
				document.getElementById('typeName2').value = val;
			}
			document.getElementById(txtId).style.background = '#E2F4FF';
			document.getElementById("search").style.display = "none";// 隐藏列表
		}			
</script>

		<style type="text/css">
.glossymenu {
	margin: 5px 0;
	padding: 0;
	width: 173px; /*width of menu*/
	border: 1px solid #9A9A9A;
	border-bottom-width: 0;
}

.glossymenu a.menuitem {
	background: black url(../../common/jquery/images/glossyback.gif)
		repeat-x bottom left;
	font: 12px "Lucida Grande", "Trebuchet MS", Verdana, Helvetica,
		sans-serif;
	color: black;
	display: block;
	position: relative;
	/*To help in the anchoring of the ".statusicon" icon image*/
	width: auto;
	padding: 4px 0;
	padding-left: 10px;
	text-decoration: none;
	cursor: hand;
}

.glossymenu a.menuitem:visited,.glossymenu .menuitem:active {
	color: white;
}

.glossymenu a.menuitem .statusicon {
	/*CSS for icon image that gets dynamically added to headers*/
	position: absolute;
	top: 5px;
	right: 5px;
	border: none;
}

.glossymenu a.menuitem:hover {
	background-image: url(../../common/jquery/images/glossyback2.gif);
}

.glossymenu div.submenu { /*DIV that contains each sub menu*/
	background: white;
}

.glossymenu div.submenu ul { /*UL of each sub menu*/
	list-style-type: none;
	margin: 0;
	padding: 0;
	height: expression(document .       body .       clientHeight-100 +       'px')
}

.glossymenu div.submenu ul li {
	border-bottom: 1px solid red;
	height: 1000px;
}

.glossymenu div.submenu ul li a:hover {
	background: #DFDCCB;
	color: white;
}
</style>
	</head>
	<body>
		<!-------- 下拉隐藏层 ----------->
		<div class="glossymenu">
			<a class="menuitem submenuheader" type="query"
				onclick="getTreeHtml('query')">Query</a>
			<div class="submenu">
				<div id="search"
			style="display: none; position: absolute; z-index: 800; width: 150; background-color: #E2F4FF; border: solid 1px; border-color: black;"></div>
				<ul id="query" style="background: #F5F4F5;">
					<div id="queryEleDiv" ></div>
				</ul>
			</div>
			<a class="menuitem submenuheader" type="factory"
				onclick="getTreeHtml('factory')">Suppliers</a>
			<div class="submenu">
				<ul id="factory">
					<iframe name="myiframeFac" frameBorder='no' scrolling=auto
						marginheight=0 marginwidth=0
						style="display: block; width: 100%; height: expression(document .       body .       clientHeight -120 +                   'px')"></iframe>

				</ul>
			</div>
			<a class="menuitem submenuheader" type="type"
				onclick="getTreeHtml('type')">Categorys</a>
			<div class="submenu">
				<ul id="type">
					<iframe name="myiframeType" frameBorder='no' scrolling=auto
						marginheight=0 marginwidth=0
						style="width: 100%; height: expression(document .       body .       clientHeight -120 +                   'px')"></iframe>

				</ul>
			</div>
		</div>
	</body>
</html>