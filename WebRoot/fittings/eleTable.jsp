<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String webapp=request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>样品查询表格页面</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入ECTable -->
		<script type="text/javascript">
		Ext.onReady(function(){
		
		//厂家数据列表
		var comboFac = new BindCombox({
		dataUrl:"./servlet/DataSelvert?tbname=CotFactory&key=factroyTypeidLv1&id=1",
		sendMethod:"POST",
		cmpId:"factoryIdFind",
		valueField:"id",
		displayField:"shortName",
		emptyText :"请选择",
		fieldLabel:"厂家",
		width:100,
		anchor:"100%"
		
	});
			//材质数据列表
		var comboType = new BindCombox({
		dataUrl:"./servlet/DataSelvert?tbname=CotTypeLv1",
		cmpId:"eleTypeidLv1Find",
		valueField:"id",
		displayField:"typeName",
		fieldLabel:"材质",
		width:100,
		anchor:"100%"
	});
		var roleRecord = new Ext.data.Record.create([
		{name:"id", type:"int"},
		{name:"eleId"},
		{name:"eleName"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotfittings.do?method=queryEle&fitId="+$('fitId').value
		}),
		reader: new Ext.data.JsonReader({
			root:"data",
			totalProperty:"totalCount",
			idProperty:"id"
		},roleRecord)
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	//创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([
		sm,//添加复选框列
		{
			header:"ID", //表头
			dataIndex:"id", //数据列索引，对应于recordType对象中的name属性
			width:50,
			sortable: true,//是否排序
			hidden:true
		},
		{
			header:"产品货号", //表头
			dataIndex:"eleId", //数据列索引，对应于recordType对象中的name属性
			width:220,
			sortable: true//是否排序
		},
		{
			header:"中文名称", //表头
			dataIndex:"eleName", //数据列索引，对应于recordType对象中的name属性
			width:260,
			sortable: false//是否排序
		}
	]);	
		var toolBar = new Ext.PagingToolbar({
            pageSize: 7,
            store: ds,
            displayInfo: true,
            displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
            displaySize:'5|10|15|20|all',
            emptyMsg: "无记录"
        });
    var grid = new Ext.grid.GridPanel({
		columnWidth:1,
		id:"fitGrid",
		stripeRows:true,
		autoHeight:true,
		bodyStyle:'width:100%',
		store:ds, //加载数据源
		cm:cm,		//加载列
		sm:sm,
		//selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),
		loadMask: true, //是否显示正在加载
		bbar:toolBar,
		//layout:"fit",
		viewConfig:{
			forceFit :false
		}
	});
	ds.on('beforeload', function() {
		ds.baseParams =  DWRUtil.getValues("queryForm");
	});
	//分页基本参数
	ds.load({params:{start:0, limit:7}});
	 grid.on('rowclick', function(grid, rowIndex, event) {
        var record = grid.getStore().getAt(rowIndex);
    	showElePic(record.get("id"));
    });
		var MyPanel=new Ext.Panel({
			title:"",
			autoWidth:true,
			autoHeight:true,
			layout:"column",
			renderTo:"form",
			frame:true,
			hideBorders:true,
			items:[
				{
					xtype:"form",
					labelWidth:60,
					labelAlign:"right",
					layout:"column",
					columnWidth:0.7,
					formId:"queryForm",
					id:"queryFormId",
					hideBorders:true,
					fbar:[
						{
							text:"查询",
							iconCls:"page_sel",
							handler:function(){ds.reload()}
						},
						{
							text:"重置",
							iconCls:"page_reset",
							handler:function(){clearForm('queryFormId')}
							
						},
						{
							text:"添加",
							iconCls:"page_add",
							handler:returnIds
						},
						{
							text:"关闭",
							iconCls:"page_cancel",
							handler:function(){closeDiv()}
						}
					],
					items:[
						{
							xtype:"panel",
							title:"",
							columnWidth:0.5,
							layout:"form",
							items:[
								{
									xtype:"textfield",
									fieldLabel:"货号",
									anchor:"100%",
									name:"eleIdFind"
								},
								{
									xtype:"textfield",
									fieldLabel:"颜色",
									anchor:"100%",
									name:"eleColFind"
								}
							]
						},
						{
							xtype:"panel",
							title:"",
							columnWidth:0.5,
							layout:"form",
							items:[
								comboFac,
								comboType
							]
						}
					]
				},
				{
					xtype:"panel",
					html:'<div id="elePicDiv" style="margin-left: 5px; margin-top: 5px">'
						+'<img src="common/images/zwtp.png" id="elePicPath" width="100"'
						+'height="100" onload="javascript:DrawImage(this,100,100)" />'
						+'</div>',
					columnWidth:0.3
				},
				grid
			]
		})
	});
				//显示图片
	function showElePic(elementId){
		var isSelPic = getPopedomByOpType("cotpicture.do","SEL");
		if(isSelPic==0){//没有查看图片信息权限
			$('elePicPath').src = "common/images/noElePicSel.png";
		}else{
			$("elePicPath").src = "./showPicture.action?elementId="+elementId+"&flag=ele";
		}
	}
	function getEleIds(){ 
		var list = Ext.getCmp("fitGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list,function(item){
			var obj = new Object();
			obj.id = item.get("id");
			obj.eleId = item.get("eleId");
			obj.eleName = item.get("eleName");
			res.push(obj);
		});
		return res;
	}
	//表格添加事件,调用父页面的方法
	function returnIds(){
		var allAry = getEleIds(); 
		if(allAry.length == 0){	
			alert("请选择一条记录");
			return;
		}
		parent.insertSelect(allAry);
	}
				//关闭方法
	function closeDiv(){
		parent.document.getElementById('eee').style.display='none';
	}
	</script>
	<script type="text/javascript" src="<%=webapp %>/common/js/popedom.js"></script>
	</head>
	<%
		String fitId = request.getParameter("fitId");
	%>
	<body>
	<div id="form" style="width:100%"></div>
	<input type="hidden" name="fitId" id="fitId" value="<%=fitId%>">
	</body>

</html>
