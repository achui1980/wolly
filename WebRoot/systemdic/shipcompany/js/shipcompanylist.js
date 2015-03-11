Ext.onReady(function() {
	
	
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"companyName"},
		{name:"companyNameEn"},
		{name:"companyContact"},
		{name:"companyNbr"},
		{name:"companyFax"},
		{name:"companyAddr"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotshipcompany.do?method=query"
		}),
		reader: new Ext.data.JsonReader({
			root:"data",
			totalProperty:"totalCount",
			idProperty:"id"
		},roleRecord)
	});
	//创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	//创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([sm,//添加复选框列
			{
				header : "ID", //表头
				dataIndex : "id", //数据列索引，对应于recordType对象中的name属性
				width : 50,
				sortable : true,//是否排序
				hidden : true
			},  {
				header : "中文名", //表头
				dataIndex : "companyName", //数据列索引，对应于recordType对象中的name属性
				width : 150,
				sortable : true
			//是否排序
			}, {
				header : "英文名", //表头
				dataIndex : "companyNameEn", //数据列索引，对应于recordType对象中的name属性
				width : 200,
				sortable : true
			//是否排序
			}, {
				header : "联系人", //表头
				dataIndex : "companyContact", //数据列索引，对应于recordType对象中的name属性
				width : 120,
				sortable : true
			//是否排序
			}, {
				header : "联系电话", //表头
				dataIndex : "companyNbr", //数据列索引，对应于recordType对象中的name属性
				width : 120,
				sortable : true
			//是否排序
			}, {
				header : "传真", //表头
				dataIndex : "companyFax", //数据列索引，对应于recordType对象中的name属性
				width : 80,
				sortable : true
			//是否排序
			}, {
				header : "地址", //表头
				dataIndex : "companyAddr", //数据列索引，对应于recordType对象中的name属性
				width : 200,
				sortable : false
			//是否排序
			}, {
				header : "操作",
				dataIndex : "id",
				width : 60,
				renderer : function(value) {
					//var mod = '<a href="javascript:modTypeById('+value+')">修改</a>';

					var nbsp = "&nbsp &nbsp &nbsp"
					var del = '<a href="javascript:del(' + value + ')">删除</a>';
					return del + nbsp;
				}
			}]);
	var toolBar = new Ext.PagingToolbar({
		pageSize : 15,
		store : ds,
		displayInfo : true,
		//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
		displaySize : '5|10|15|20|all',
		emptyMsg : "No data to display"
	});
	var tb = new Ext.Toolbar({
		items : [
			'->', 
			'-',
			{
				text : "新增",
				cls:"SYSOP_ADD",
    		 	iconCls:"gird_add",
				handler : windowopenAdd
			},
			'-',
    		{
    			text:"修改",
    			cls:"SYSOP_MOD",
    		 	iconCls:"gird_edit",
    		 	handler:windowopen.createDelegate(this,[null])
    		},
			'-', 
			{
				text : "删除",
				cls:"SYSOP_DEL",
    		 	iconCls:"page_del",
				handler : deleteBatch
			}]
	});
	var grid = new Ext.grid.GridPanel({
		
		border : true,
		region : "center",
		id : "shipGrid",
		margins: '0 2 0 0',
		//autoHeight:true,
		stripeRows : true,
		bodyStyle : 'width:100%',
		store : ds, //加载数据源
		cm : cm, //加载列
		sm : sm,
		//selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),
		loadMask : true, //是否显示正在加载
		tbar : tb,
		bbar : toolBar,
		split:false,
		//layout:"fit",
		viewConfig : {
			forceFit : false
		}
	});
	
	
	//将查询面板的条件加到ds中
	ds.on('beforeload', function(store,options) {
		
	});
	//分页基本参数
	ds.load({
		params : {
			start : 0,
			limit : 15
		}
	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [grid]
	});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
		var record = grid.getStore().getAt(rowIndex);
    	windowopen(record.get("id"));
	});

});

