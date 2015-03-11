<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String webapp=request.getContextPath();
%>
<style type="text/css">    
    .x-selectable, .x-selectable * {    
        -moz-user-select: text!important;  
        -webkit-user-select: text!important;    
        -khtml-user-select: text!important;    
    }
	.x-grid-record-red  tr{ 
	    /*background: darkgreen;*/
	    background:thistle;
	}
	.x-grid-record-yellow  tr{ 
	    /*background: darkgreen;*/
	    background:khaki;
	}
	.x-grid-record-qing  tr{ 
	    /*background: darkgreen;*/
	    background:khaki;
	}
	.x-grid-record-waitpi  tr{ 
	    /*background: darkgreen;*/
	    background:yellowgreen;
	}
	.x-grid-record-canOut  tr{ 
	    /*background: darkgreen;*/
	    background:beige;
	}
	.x-grid-record-noShen  tr{ 
	    /*background: darkgreen;*/
	    background:darkgray;
	}
	/*
	@media screen and (-webkit-min-device-pixel-ratio:0) { 
    .x-grid3-cell
    {
	    box-sizing: border-box;
    }
    */
</style>   
<link rel="stylesheet" type="text/css"
	href="<%=webapp %>/common/css/common.css" />
<link rel="stylesheet" type="text/css"
	href="<%=webapp %>/common/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="<%=webapp %>/common/ext/resources/css/ext-extend.css" />
<link rel="stylesheet" type="text/css" href="<%=webapp %>/common/ext/resources/css/ext-patch.css" />

<!-- LIBS -->
<script type="text/javascript" src="<%=webapp %>/common/js/Number.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/common.js"></script>
<script type="text/javascript"
	src="<%=webapp %>/common/ext/adapter/ext/ext-base.js"></script>

<!-- ENDLIBS -->
<script type="text/javascript" src="<%=webapp %>/common/ext/ext-all-debug.js"></script>

<script type='text/javascript' src='<%=webapp %>/dwr/engine.js'></script>
<script type="text/javascript" src="<%=webapp %>/dwr/util.js"></script>
<script type="text/javascript"
	src="<%=webapp %>/dwr/interface/baseDataUtil.js"></script>
<script type="text/javascript"
	src="<%=webapp %>/dwr/interface/contexUtil.js"></script>
<script type='text/javascript'
	src='<%=webapp %>/dwr/interface/cotPopedomService.js'></script>
<script type='text/javascript'
	src='<%=webapp %>/dwr/interface/cotModuelService.js'></script>
<script type='text/javascript'
	src='<%=webapp %>/dwr/interface/queryService.js'></script>
	
<script type='text/javascript'
	src='<%=webapp %>/dwr/interface/cotSeqService.js'></script>	
<!-- 模块列 -->
<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCustomizeService.js'></script>

<script type="text/javascript"
	src="<%=webapp %>/common/ext/miframe-min.js"></script>
<script type="text/javascript"
	src="<%=webapp %>/common/ext/daterangeAndPassword.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/validator.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/bindCombo.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/loginTree.js"></script>


<!-- 查询工具栏 -->
<script type="text/javascript" src="<%=webapp %>/common/js/SearchComboxField.js"></script>
<!-- 隐藏列 -->
<script type="text/javascript" src="<%=webapp %>/common/js/HideColumnModel.js"></script>
<script type="text/javascript" src="<%=webapp %>/common/js/GridRowSelect.js"></script>
<!-- 重写ext -->
<script type="text/javascript" src="<%=webapp %>/common/js/ext-override.js"></script>

<script type="text/javascript" src="<%=webapp %>/mail/common/common.js"></script>

<link rel="stylesheet" type="text/css"
	href="<%=webapp %>/common/xtree/xtree.css" />
<script type="text/javascript" src="<%=webapp %>/common/xtree/xtree.js"></script>

<script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%=webapp %>/common/ext/resources/images/default/s.gif'
    Ext.QuickTips.init();                       //为组件提供提示信息功能，form的主要提示信息就是客户端验证的错误信息。
	Ext.form.Field.prototype.msgTarget='qtip';         //提示的方式，枚举值为
	Ext.data.Connection.prototype.timeout = 60000;  
	//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());//设置cookie，保存布局
	if (!Ext.grid.GridView.prototype.templates) {    
    Ext.grid.GridView.prototype.templates = {};  
   
}    
//Grid可以选择单元格的补丁
    Ext.grid.GridView.prototype.templates.cell = new Ext.Template(    
	    '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>',    
	    '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>',    
	    '</td>'   
    );   
	FontFamilies=['宋体','黑体','隶书','楷体_GB2312','Arial','Courier New','Tahoma','Comic Sans MS','Times New Roman','Verdana'];
	if(Ext.form.DateField){
	   Ext.apply(Ext.form.DateField.prototype, {
	      format            : "Y-m-d"
	   });
	}
</script>



