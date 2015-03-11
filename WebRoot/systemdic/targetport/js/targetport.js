Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"targetPortName"},
		{name:"targetPortEnName"},
		{name:"targetPortNation"},
		{name:"shipingLine"},
		{name:"targetPortRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cottargetport.do?method=query"
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
			header:"目的港(中文)", //表头
			dataIndex:"targetPortName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			hidden:true,
			sortable: true//是否排序
		},
		{
			header:"Destination", //表头
			dataIndex:"targetPortEnName", //数据列索引，对应于recordType对象中的name属性
			width:180,
			sortable: true//是否排序
		},
		{
			header:"Country",
			dataIndex:"targetPortNation",
			width:80,
			sortable:true
		},
		{
			header:"航线",
			dataIndex:"shipingLine",
			width:120,
			hidden:true,
			sortable:true
		},
		{
			header:"Remark", //表头
			dataIndex:"targetPortRemark", //数据列索引，对应于recordType对象中的name属性
			width:300,
			sortable: true//是否排序
		},
		{
			header:"Operation",
			dataIndex:"id",
			renderer :function(value){
				//var mod = '<a href="javascript:modTypeById('+value+')">修改</a>';
				
				var nbsp = "&nbsp &nbsp &nbsp"
				var del = '<a href="javascript:del('+value+')">Delete</a>';
				return del+nbsp;
			}
		}
	]);
	var toolBar = new Ext.PagingToolbar({
            pageSize: 15,
            store: ds,
            displayInfo: true,
            displayMsg: 'Display {0} - {1} of {2}',
            emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		'-',
    		{text:"Lots Del.",
    		 cls:"SYSOP_DEL",
    		 iconCls:"page_del",
    		 handler:deleteBatch
    		}
    	]
    });
	var grid = new Ext.grid.GridPanel({

		margins: '0 2 0 0',
		region:"center",
		id:"targetportGrid",
		//autoHeight:true,
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
			forceFit :false
		}
	});
	
	//分页基本参数
	ds.load({params:{start:0, limit:15}});
	/*----------------------------------------------------*/
	
	var form = new Ext.form.FormPanel({
		title:"Create|Edit|Search",
		region: 'east',
		id:"targetportFormId",
		formId:"targetportForm",
		collapsible:true,
        frame: true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 90,
        monitorValid:true,
        defaultType: 'textfield',
        buttonAlign:'center',
        defaults: {
            width: 220
        },
        items:[{
		
			//添加布局
			fieldLabel:"",
			id:"targetPortName",
			name:"targetPortName",
			//allowBlank:false,
			hidden:true,
			//blankText:"目的港中文名称不能为空",
			anchor:"100%"
			
		},{
			fieldLabel:"Destination*",
			id:"targetPortEnName",
			name:"targetPortEnName",
			//regex:/\w+$/, //正则表达式这里假设只允许输入数字如果输入的不是数字就会出现下面定义的Message
            //regexText:"只能够输入数字、字母、下划线的组合", //定义不符合正则表达式的Message
			allowBlank:false,
			blankText:"Destination Can not be empty!",
			anchor:"100%"
		},{
			fieldLabel:"Country",
			id:"targetPortNation",
			name:"targetPortNation",
			allowBlank:true,
			blankText:"Country Can not be empty!",
			anchor:"100%"
		},{
			fieldLabel:"",
			id:"shipingLine",
			hidden:true,
			name:"shipingLine",
			//allowBlank:true,
			//blankText:"航线不能为空",
			anchor:"100%"
		},{
			xtype:"textarea",
			fieldLabel:"Remark",
			id:"targetPortRemark",
			name:"targetPortRemark",
			allowBlank:true,
			anchor:"100%"
		},new Ext.form.Hidden({
			id:"id",
			name:"id"
		})],
		buttons:[
		{
			formBind:true,
			text:"Create",
			cls:"SYSOP_ADD",
			id:'saveBtn',
			iconCls : "page_add",
			handler:saveOrUpdate
		},{
			enableToggle:true,
			text:"Query",
			iconCls : "page_sel",
			handler:function(){ds.reload({params : {
												start : 0,
												limit : 15
											}})}
		},{
			text:"Reset",
			iconCls : "page_reset",
			handler:function(){
				form.getForm().reset();
				Ext.getCmp('saveBtn').setText("Create");
        		Ext.getCmp('saveBtn').setIconClass('page_add');
			}
		}]
	});
	
	 var viewport = new Ext.Viewport({
        layout: 'border',
        items: [grid,form]
    });
    
    ds.on('beforeload', function() {
	 
		ds.baseParams =form.getForm().getValues();
 
	});
    
     // 单击修改信息 start
    grid.on('rowclick', function(grid, rowIndex, event) {
        var record = grid.getStore().getAt(rowIndex);
        form.getForm().loadRecord(record);
        Ext.getCmp('saveBtn').setText("Edit");
        Ext.getCmp('saveBtn').setIconClass('page_mod');
    });
    // 单击修改信息 end
});
//添加或修改记录
function saveOrUpdate()
{   
	//表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("targetPortEnName").getValue();
	var form = Ext.getCmp("targetportFormId").getForm();
	if(!form.isValid())
		return;
	var obj = DWRUtil.getValues("targetportForm");
	var list = new Array();
	list.push(obj); 
	if(id == null || id== "")
	{
		var isExist = false;
		DWREngine.setAsync(false); 
		cotTargetPortService.findExistByName(name,function(res){
		    isExist = res;
		});
		//判断是否同名
		if(isExist)
		{
		    Ext.Msg.alert("Message","The Destination exists!");
		    return;
		}
	    cotTargetPortService.addTargetPort(list,function(res){
	    	Ext.Msg.alert("Message","Create Successful！");
	    	clearForm("targetportFormId");
	    	reloadGrid("targetportGrid");
		});
		DWREngine.setAsync(true); 
	}
	else
	{
		cotTargetPortService.modifyTargetPort(list,function(res){
			if(res){
				Ext.Msg.alert("Message","Eidt Successful！");
				clearForm("targetportFormId");
				reloadGrid("targetportGrid");
			}
		});
	}
}
//删除
function del(id)
{
	var isPopedom = getPopedomByOpType(vaildUrl,"Del");
	var isPopedom = 1
	if(isPopedom == 0)//没有删除权限
	{
		alert("You do not have permission to delete!");
		return;
	}
	var cotTargetPort = new CotTargetPort();
	var list = new Array();
	cotTargetPort.id = id;
	list.push(cotTargetPort);
	Ext.MessageBox.confirm('Message', 'Are sure to delete this record?', function(btn) {
		if (btn == 'yes') {
			cotTargetPortService.deleteTargetPort(list,function(res){
				if(res == 0){
					clearForm("targetportFormId");
					reloadGrid("targetportGrid");
					
				}else{
					Ext.Msg.alert("Message","Can't Delete this Record,Data cited!");
				}
			})
		}
	});
}
function getIds()
{
	var list = Ext.getCmp("targetportGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotTargetPort = new CotTargetPort();
		cotTargetPort.id = item.id
		res.push(cotTargetPort);
	});
	return res;
}
function deleteBatch()
{
    var list = getIds();
    if(list.length == 0)
    {	
    	Ext.Msg.alert("Message","Select Record Pls.");
    	return;
    }
	Ext.MessageBox.confirm('Message', 'Are you sure to delete this record?', function(btn) {
		if(btn == 'yes'){
			cotTargetPortService.deleteTargetPort(list,function(res){
				result = res;				
				if(result == 0)
				{
					Ext.Msg.alert("Message","Deleted Successful!");
					clearForm("targetportFormId");
					reloadGrid("targetportGrid");
					
				}else{
					Ext.Msg.alert("Message","Can't Delete this Record,Data cited!")
					return; 
				}
			});				 
	    }else{
	       return;
	    }
	});
}
	 