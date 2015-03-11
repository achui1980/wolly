<%@ page language="java" isErrorPage="true"  contentType="text/html;charset=utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>模块列表</title>
	<jsp:include page="../../common.jsp"></jsp:include>
	<script type="text/javascript" src="<%=webapp %>/common/jquery/jquery.js"></script> 
  	<link rel="stylesheet" href="<%=webapp %>/common/jquery/style.css" type="text/css" />
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotEmpsService.js'></script>
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotMessageService.js'></script>
	<script type='text/javascript' src='<%=webapp %>/dwr/interface/mailRecvService.js'></script>
	<script type="text/javascript" src="<%=webapp %>/common/js/msgbox.js"></script>	
	<style type="text/css">
	.menuCls{
		cursor:hand;
		heigth:20px;
		width :65px;
		font-family: "tahoma", "宋体";
		background-repeat:no-repeat;
		vertical-align: middle;
		text-indent: 0;
		text-align:center;
		font-size:10pt; 
		color: #ffffff;
	
	}
	.menuClsActive{
		cursor:hand;
		heigth:20px !important;
		width :69px !important;
		background-image:	url( "<%=webapp %>/common/images/menu.active.png" ) !important;
		background-repaet:	no-repeat;
		font-family: "tahoma", "宋体";
		vertical-align: middle;
		text-indent: 0;
		text-align:center;
		font-size:10pt; 
		padding: 1px;
		
	}
	.menuClsHover{
		cursor:hand;
		heigth:20px !important;
		width :69px !important;
		background-image:	url( "<%=webapp %>/common/images/menu.active.png" ) !important;
		background-repaet:	no-repeat;
		font-family: "tahoma", "宋体";
		vertical-align: middle;
		text-indent: 0;
		text-align:center;
		font-size:10pt; 
		padding: 1px;
		
	}
	.menuDiv{
		
	}
	.detailCls{
		text-decoration:  underline !important;
		font-size:10pt !important;
		color: #201B74 !important;
		cursor: hand;
	}
	#detail{
		white-space:nowrap;
		background-color: lightsteelblue;
	}
	.style1 {
	font-size: 11px;
	color: #FFFFFF;
	margin-right:15px;
	font:"宋体";
	}
	.style2 { 
	    color: #FFFFFF;
		font-size: 10px;
		font:"宋体";
	}
	
	a:link {
	color: #fff;
	text-decoration: none;
	font-size: 11px;
	font:"宋体";
	}
	a:visited { 
	color:#fff;
	text-decoration: none;
	font-size: 11px;
	font:"宋体";
	}
	a:hover { 
	color:#CC0000;
	text-decoration: none;
	font-size: 11px;
	font:"宋体";
	} 
	</style>
	<script type="text/javascript">
	$(document).ready(function(){
		   $("#zone-bar li span").click(function() {
		   		var hidden = $(this).parents("li").children("ul").is(":hidden");
		   		
				$("#zone-bar>ul>li>ul").hide()        
			   	$("#zone-bar>ul>li>a").removeClass();
			   		
			   	if (hidden) {
			   		$(this)
				   		.parents("li").children("ul").toggle()
				   		.parents("li").children("a").addClass("zoneCur");
				   	} 
			   });
			$("#zone-bar li ul li ").click(function() {
		   		var hidden = $(this).parents("li").children("ul").is(":hidden");
		   		
				$("#zone-bar>ul>li>ul").hide()        
			   	$("#zone-bar>ul>li>a").removeClass();
			   		
			   	if (hidden) {
			   		$(this)
				   		.parents("li").children("ul").toggle()
				   		.parents("li").children("a").addClass("zoneCur");
				   	} 
			   });
		});
	//表格显示前,通常在这注册单击，双击事件
var parentText = "";
var isPopedom = null;

		function getModuleName(modulename)
		{		
			parentText = modulename;
			setMenuContent(parentText);
			//setDefaultCls();
		}
		function setDefaultCls()
		{
			var eles = document.getElementsByTagName("label");
			for(var i=0; i<eles.length; i++)
			{	
				var ele = eles[i];
				var clsName = ele.className;
				if(clsName != "" && typeof(clsName) != "undefined" && clsName != "detailCls")
				{
					ele.className = "menuCls";
				}
			}
		}
		function doAction(url,id,modulename)
		{
			//获取当前选中的tab标签
			//var tabpage = webFXTabPane.getTabPage(webFXTabPane.getSelectedIndex());
			
			document.menu.location.href = url;
			//parent.parent.f2.location.href = url;
			var  text = parentText + " >> " + modulename;
			setMenuContent(text);
		}
		function index()
		{
			parentText = '首页';
			setMenuContent(parentText);
			document.menu.location.href = "cotmessage.do?method=queryPlatForm";
		}	
		function changPwd()
		{
			parentText = '密码修改';
			setMenuContent(parentText);
			document.menu.location.href = "cotpwd.do?method=modPwd";
		}
		function exitSystem()
		{
			var con = window.confirm('您是否确定退出系统?');
			if(con){
				queryService.deleteLoginInfos(function(res){});
				window.parent.close();
			}
		}
		function loginout()
		{
			parent.document.location.href = "index.do?method=logoutAction";
		}
		function regist()
		{
			document.menu.location.href = "<%=webapp %>/system/serverInfo/cotserverInfoUpdata.jsp";
		}
		function help()
		{
			parentText = '帮助';
			setMenuContent(parentText);
			var _height = window.screen.height;
			var _width = window.screen.width;
			cotModuelService.getSoftVer(function(res){
				if(res == 1){
					openMaxWindow(_height,_width,'<%=webapp %>/help/help_yp.swf')
				}
				if(res == 2){
					openMaxWindow(_height,_width,'<%=webapp %>/help/help_bj.swf')
				}
				if(res == 3 || res == 4){
					openMaxWindow(_height,_width,'<%=webapp %>/help/help_yw.swf')
				}
			});			
		}
		function init()
		{
		    showTime();
		    showMsgBox();
			cotEmpsService.getLoginEmp(function(res){
			
				if(res == null)
				{
					//document.getElementById("loginEmpId").innerHTML = "登陆工号:未知";
					document.getElementById("loginEmpName").innerHTML = "未知";
				}
				else
				{
					//document.getElementById("loginEmpId").innerHTML += "登陆工号:"+res.empsId;
					document.getElementById("loginEmpName").innerHTML += res.empsName;
				}
			})
		}
		function showTime(){ 
			var $dt = document.getElementById("date"); // 得到容器对象 
			var dt = new Date(); // 得到当前时间 
			var y = dt.getFullYear(); // 当前年份 
			var m = dt.getMonth() + 1; // 当前月份，getMonth 返回值是 0-11 对应 1-12月，因此全部加1 
				m = m > 9 ? m : "0"+m;
			var d = dt.getDate(); 
				d = d > 9 ? d : "0"+d;
			
			var h = dt.getHours(); 
				h = h > 9 ? h : "0"+h;
			var i = dt.getMinutes(); 
				i = i > 9 ? i : "0"+i;
			var s = dt.getSeconds(); 
				s = s > 9 ? s : "0"+s;
			var weekly=dt.getDay();
			var str = '当前时间是' + y + '年' + m + '月' + d + '日 '+ '星期' + Day[weekly]+' ' + h + ':' + i + ':' + s ;
			$dt.innerHTML = str; // 将格式化后的内容装载到容器中 
			} 
			Day=new Array(7);
			Day[0]="日"; Day[1]="一";
			Day[2]="二"; Day[3]="三";
			Day[4]="四"; Day[5]="五";
			Day[6]="六";
			window.setInterval(showTime, 1000); // 一秒钟重复一次 
			//设置当前位置
			function setMenuContent(content)
			{
				document.getElementById("treeText").innerHTML = "";
				document.getElementById("treeText").innerHTML = "当前位置 >> "
				document.getElementById("treeText").innerHTML += content 
			}	
			function showMsgBox()
			{
				cotMessageService.getUnReadMessage(function(res){
					if(res >0)
					{
						var MSG1 = new CLASS_MSN_MESSAGE("aa",210,126,"消息提示：","","您有"+res+"条未读消息");  
					    MSG1.oncommand = function(){
					    	  MSG1.hide();  
 							  window.open("<%=webapp %>/cotmessage.do?method=queryUnreadMessage");
					    }
					    MSG1.rect(null,null,null,screen.height-50); 
					    MSG1.speed    = 10; 
					    MSG1.step    = 5; 
					    MSG1.show();  
					}
				})
			}
			//每隔5分钟执行一次
			//setInterval(showMsgBox,5*60*1000);	
			//setInterval(showMailMsgBox,11000);
			//每隔5分钟执行一次登录时间更新
			//setInterval(updateLoginInfo,5*60*1000);
			function showMailMsgBox()
			{
				cotEmpsService.getLoginEmp(function(res){
					if(res==null){
						return;
					}
					var empId = res.id;
					mailService.getSendMsg(empId,function(msg){
						if(msg != "")
						{
							var MSG1 = new CLASS_MSN_MESSAGE("send",400,200,"消息提示：","",msg);  
						    MSG1.oncommand = function(){
						    	  MSG1.hide();  
	 							  window.open("<%=webapp %>/cotmail.do?method=query");
						    }
						    MSG1.rect(null,null,null,screen.height-50); 
						    MSG1.speed    = 10; 
						    MSG1.step    = 5; 
						    MSG1.show();  
						}
					});
					var flag = true;
					if(isPopedom == null)
						isPopedom = getPopedomByOpType("cotmailbox.do", "ASSIGN");
					if(isPopedom == 0) //没有权限查看
						flag = false;
					mailRecvService.getRecvMsg(empId,flag,function(msg){
						if(msg != "")
						{
							var MSG1 = new CLASS_MSN_MESSAGE("recv",400,200,"消息提示：","",msg);  
						    MSG1.oncommand = function(){
						    	  MSG1.hide();  
	 							  window.open("<%=webapp %>/cotmail.do?method=query");
						    }
						    MSG1.rect(null,null,null,screen.height-50); 
						    MSG1.speed    = 10; 
						    MSG1.step    = 5; 
						    MSG1.show();  
						}
					});
				})
			}
			function updateLoginInfo()
			{
				document.loginaction.location.href = "<%=webapp %>/index.do?method=modifyLoginInfo";
				//cotEmpsService.updateLoginInfo(function(res){})
			}
	</script>
</head>
<body onload="init()">
<iframe name="loginaction" style="display:none"></iframe>
	<div id="page-wrap">
		<div id="top-bar">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=webapp %>/common/jquery/images/banner.png" style="background-repeat:no-repeat; ">
			  <tr>
			    <td width="60%" height="70" rowspan="2" align="right">
			    	&nbsp;
			    </td>
			    <td width="39%" height="35" valign="bottom">
						<div style="margin-left: 15px;">
							<label id="treeText" style="color: white;">&nbsp;</label>
						</div>
						<div align="right" style="margin-right:15px; ">
							<a href="javascript:index()">首页</a>&nbsp;<span class="style2"> |</span>
						  	<a href="javascript:changPwd()">密码修改</a>&nbsp;<span class="style2"> |</span>		  	 
						  	<a href="javascript:loginout()">&nbsp;重新登陆</a>&nbsp;<span class="style2"> |</span>
						  	<a href="javascript:help()">&nbsp;帮助</a>&nbsp;<span class="style2"> |</span>	
						  	<a href="javascript:exitSystem();">&nbsp;退出</a> 
					    </div>
				</td>
			  </tr>
			  <tr>
			    <td height="35">
				    <div align="right">
					    <span class="style1">您好：
					   		 <span style="font-weight: bold;color: yellow;" class="first" id="loginEmpName"></span>&nbsp;&nbsp;
					    	 <span id="date"></span>
					    </span>
				   	</div>
			    </td>
			  </tr>
			</table>
	    </div>
	    <div id='zone-bar'>
		<%=request.getAttribute("moduleHtml") %>
		</div>
		<iframe name="menu" style="margin:0;" frameBorder='yes' style="width:100% ;height:expression(screen.availHeight -120  + 'px')"
				src="cotmessage.do?method=queryPlatForm" width=100%  scrolling=no marginheight=0 marginwidth=0></iframe>
	</div>
</body>