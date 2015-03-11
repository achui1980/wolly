<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="common.jsp"></jsp:include>
        <style type="text/css">
		.overlayer_ele{
			display:none;
			position:absolute;
			top:0px;
			z-index:6000;
			background-color:#99CCFF;
			width:100px;
		}
		</style>
  <script type="text/javascript">
  var aaa = "achui";
  function doAction(url)
  {
  	parent.f2.location.href = url;
  	//alert(tree_root.getSelected().parentNode.text);
  
  	//var content = getItem(tree_root.getSelected());
  	//alert(content);
  	var text = "";
  	arr = new Array();
  	arr = getItemText(tree_root.getSelected());
  	text = "";
  	for(var i=arr.length-1; i>=0; i--)
  	{
  		text += arr[i];
  		if(i != 0)
  			text += "-->";
  	}
  	
  	parent.title.setMenuContent(text);
  	
  }

  function getItem(node)
  {
  	var content = "";
  	//alert(typeof(node.parentNode.parentNode) != "undefined")
  	//alert(typeof(node.parentNode) != "undefined")
  	//alert(typeof(node) != "undefined")
  	if(typeof(node.parentNode.parentNode) != "undefined")
  	{
  		content += node.parentNode.parentNode.text;
  		content +="-->"
  	}
  	if(typeof(node.parentNode) != "undefined")
  	{
  		content += node.parentNode.text;
  		content +="-->"
  	}
  	content += node.text;
  	return content;
  }
  var arr = new Array();
  function getItemText(node)
  {
  	if(typeof(node) != "undefined")
  	{
  		arr.push(node.text);
  		getItemText(node.parentNode);
  	}
  	return arr;
  }
  function opentree()
  {
	var frame = document.typetree;
	frame.location.href = "cotelements.do?method=query&type=factory";
	${"overlayer"}.style.display = "block";
  }

  </script>      
  </head>

<body>
	<%= request.getAttribute("treeScript")%>
	<div id="overlayer" class="overlayer_ele">
		<iframe name="typetree" style="margin:0" width=160px height=600px frameBorder='no'   scrolling=yes marginheight=0 marginwidth=0  ></iframe>
	</div>
</body>
</html>