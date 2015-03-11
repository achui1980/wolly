Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"tradeCode"},
		{name:"tradeName"},
		{name:"tradeRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cottradetype.do?method=query"
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
			header:"贸易编码", //表头
			dataIndex:"tradeCode", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"贸易方式", //表头
			dataIndex:"tradeName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"备注", //表头
			dataIndex:"tradeRemark", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"操作",
			dataIndex:"id",
			renderer :function(value){
				//var mod = '<a href="javascript:modTypeById('+value+')">修改</a>';
				var nbsp = "&nbsp &nbsp &nbsp"
				var del = '<a href="javascript:del('+value+')">删除</a>';
				return del+nbsp;
			}
		}
	]);
	var toolBar = new Ext.PagingToolbar({
            pageSize: 15,
            store: ds,
            displayInfo: true,
            displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
            displaySize:'5|10|15|20|all',
            emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		{text:"批量删除",handler:deleteBatch,iconCls:"page_del"}
    	]
    });
	var grid = new Ext.grid.GridPanel({
		region:"center",
		id:"tradeGrid",
		margins:"0 5 0 0",
		stripeRows:true,
		bodyStyle:'width:100%',
		store:ds, //加载数据源
		cm:cm,		//加载列
		sm:sm,
		//selModel: new Ext.grid.RowSelectionModel({singleSelect:false}),
		loadMask: true, //是否显示正在加载
		tbar:tb,
		bbar:toolBar,
		//layout:"fit",
		viewConfig:{
			forceFit :true
		}
	});
	ds.on('beforeload', function() {
	 
		ds.baseParams = {
			//TODO：
		};
 
	});
	//分页基本参数
	ds.load({params:{start:0, limit:15}});
	/*----------------------------------------------------*/
	
	var form = new Ext.form.FormPanel({
		title:"新增|编辑",
		region: 'east',
		id:"tradeFormId",
		formId:"tradeForm",
        frame: true,
        width: 260,
        buttonAlign:"center",
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 60,
        monitorValid:true,
        defaultType: 'textfield',
        defaults: {
            width: 200
        },
        items:[{
		
			//添加布局
			fieldLabel:"<font color='red'>贸易编码</font>",
			id:"tradeCode",
			name:"tradeCode",
			allowBlank:false,
			blankText:"贸易编码不能为空",
			anchor:"95%"
			
		},{
		
			//添加布局
			fieldLabel:"<font color='red'>贸易方式</font>",
			id:"tradeName",
			name:"tradeName",
			allowBlank:false,
			blankText:"贸易方式不能为空",
			anchor:"95%"
			
		},{
		
			//添加布局
			xtype:"textarea",
			fieldLabel:"备注",
			id:"tradeRemark",
			name:"tradeRemark",
			anchor:"95%"
			
		},new Ext.form.Hidden({
			id:"id",
			name:"id"
		})],
		buttons:[
			{
			enableToggle:true,
			pressed:true,
			text:"新增",
			width:65,
			iconCls:"page_add",
			id:"saveBtn",
			formBind: true,
			handler:saveOrUpdate
		},{
			text:"重置",
			width:65,
			iconCls:"page_reset",
			handler:function(){
				form.getForm().reset();
				Ext.getCmp('saveBtn').setText("新增");
        		Ext.getCmp('saveBtn').setIconClass('page_add');
			}
		}]
	});
	
	 var viewport = new Ext.Viewport({
        layout: 'border',
        items: [grid,form]
    });
    
     // 单击修改信息 start
    grid.on('rowclick', function(grid, rowIndex, event) {
        var record = grid.getStore().getAt(rowIndex);
        form.getForm().loadRecord(record);
       	Ext.getCmp("saveBtn").setText("修改");
		Ext.getCmp("saveBtn").setIconClass("page_mod");
    });
});
//添加或修改记录
	function saveOrUpdate()
	{
		var id = $('id').value;
	    var obj = DWRUtil.getValues("tradeForm");
	    var cotTradeType = new CotTradeType();
	    for(var p in obj)
	    {
	    	cotTradeType[p] = obj[p];
	    }
	    if(id == null || id == '')
	    {
		    cotTradeTypeService.addTradeType(cotTradeType,function(res){
		    	Ext.Msg.alert("提示信息","添加成功");
				reloadGrid("tradeGrid");
			});
	    }
	    else
	    {
		    cotTradeTypeService.modifyTradeType(cotTradeType,function(res){
		    	Ext.Msg.alert("提示信息","修改成功");
				reloadGrid("tradeGrid");
			})
	    }
	}
//删除
function  del(id)
{
	//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	if(isPopedom == 0)//没有删除权限
	{
		Ext.Msg.alert("提示信息","您没有删除权限");
		return;
	}
    var flag = window.confirm("确定删除?");
	if(flag){
	    var obj = DWRUtil.getValues("tradeForm");
	    var cotTradeType = new CotTradeType();
		var list = new Array();
		for(var p in obj)
		{
			cotTradeType[p] = obj[p];
		}
		list.push(cotTradeType)
		cotTradeTypeService.deleteTradeType(list,function(res){
			Ext.Msg.alert("提示信息","删除成功");
			reloadGrid("tradeGrid");
			clearForm("tradeFormId");
		})		
	}
}
function getIds()
{
	var list = Ext.getCmp("tradeGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotTradeType = new CotTradeType();
		cotTradeType.id = item.id
		res.push(cotTradeType);
	});
	return res;
}	
function deleteBatch()
		{
		    var list = getIds();
		    if(list.length == 0)
		    	{	
		    		Ext.Msg.alert("提示信息","请选择记录");
		    		return;
		    	}
			var flag = window.confirm("确定删除?");
			if(flag){
			          
					  cotTradeTypeService.deleteTradeType(list,function(res){
							Ext.Msg.alert("提示信息","删除成功");
							reloadGrid("tradeGrid");
							clearForm("tradeFormId");
							
					});	
		    }else{
		       return;
		    }
		}
			 