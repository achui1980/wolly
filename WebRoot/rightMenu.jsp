<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style> 
		/*定义菜单方框的样式2*/
		.skin{
		padding-top:4px;
		cursor:default;
		font:menutext;
		position:absolute;
		text-align:left;
		font-family: "宋体";
		z-index:100;
		font-size: 10pt;
		width:120px;              /*宽度，可以根据实际的菜单项目名称的长度进行适当地调整*/
		background-color:menu;    /*菜单的背景颜色方案，这里选择了系统默认的菜单颜色*/
		border:1 solid buttonface;
		display:none;        /*初始时，设置为不可见*/
		border:2 outset buttonhighlight;
		}
		
		/*定义菜单条的显示样式*/
		.menuitems {
		padding:4px 1px 2px 12px;
		margin-bottom: 1px;
		}
	</style> 
	<SCRIPT LANGUAGE="Javascript1.2" > 
		<!-- 
		//定义菜单显示的外观
		var menuskin = "skin"; 
		
		function showmenu() {
			//获取当前鼠标右键按下后的位置，据此定义菜单显示的位置
			var rightedge = document.body.clientWidth-event.clientX;
			var bottomedge = document.body.clientHeight-event.clientY;
			
			//如果从鼠标位置到窗口右边的空间小于菜单的宽度，就定位菜单的左坐标（Left）为当前鼠标位置向左一个菜单宽度
			if (rightedge <menu.offsetWidth)
				menu.style.left = document.body.scrollLeft + event.clientX - menu.offsetWidth;
			else
				//否则，就定位菜单的左坐标为当前鼠标位置
				menu.style.left = document.body.scrollLeft + event.clientX;
			
			//如果从鼠标位置到窗口下边的空间小于菜单的高度，就定位菜单的上坐标（Top）为当前鼠标位置向上一个菜单高度
			if (bottomedge <menu.offsetHeight)
				menu.style.top = document.body.scrollTop + event.clientY - menu.offsetHeight;
			else
				//否则，就定位菜单的上坐标为当前鼠标位置
				menu.style.top = document.body.scrollTop + event.clientY;
				
			//设置菜单可见
			menu.style.display = "block";
			return false;
		}
		
		function hidemenu() {
		//隐藏菜单
			//设置display为none
			menu.style.display = "none";
		}
		
		function highlight() {
		//高亮度鼠标经过的菜单条项目
		
			//如果鼠标经过的对象是menuitems，就重新设置背景色与前景色
			//event.srcElement.className表示事件来自对象的名称，必须首先判断这个值
			if (event.srcElement.className == "menuitems") {
				event.srcElement.style.backgroundColor = "highlight";
				event.srcElement.style.color = "white";
			 }
		}
		
		function lowlight() {
		//恢复菜单条项目的正常显示
		
			if (event.srcElement.className == "menuitems") {
				event.srcElement.style.backgroundColor = "";
				event.srcElement.style.color = "black";
				window.status = "";
		    }
		}
		//右键复制
		function copyToClipboard(txt) {
			if(window.clipboardData) {
				window.clipboardData.clearData();
				window.clipboardData.setData("Text", txt);
			} else if(navigator.userAgent.indexOf("Opera") != -1) {
				window.location = txt;
			} else if (window.netscape) {
				try {
					   netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
				} catch (e) {
					  alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
				}
				var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
				if (!clip)
				return;
							
				var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
				if (!trans)
				return;
							
				trans.addDataFlavor('text/unicode');
							
				var str = new Object();
				var len = new Object();
				var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
				var copytext = txt;
							
				str.data = copytext;
				trans.setTransferData("text/unicode",str,copytext.length*2);
						  	
				var clipid = Components.interfaces.nsIClipboard;
				if (!clip)
				return false;
							 
				clip.setData(trans,null,clipid.kGlobalClipboard);
			}
		}
		//选中的文本		
		function cp(){
			var g;
			if(document.all){
				g = document.selection.createRange().text;
			}else{
				g=document.getSelection();
			}
			return g;
		}		
		//-->
	</SCRIPT>
  </head>
</html>
