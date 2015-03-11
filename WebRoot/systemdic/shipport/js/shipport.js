Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"shipPortName"},
		{name:"shipPortNameEn"},
		{name:"shipPortRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotshipport.do?method=query"
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
			header:"POL_CN", //表头
			dataIndex:"shipPortName", //数据列索引，对应于recordType对象中的name属性
			width:250,
			hidden:true,
			sortable: true//是否排序
		},
		{
			header:"Port of Loading", //表头
			dataIndex:"shipPortNameEn", //数据列索引，对应于recordType对象中的name属性
			width:250,
			//hidden:true,
			sortable: false//是否排序
		},
		{
			header:"Remark", //表头
			dataIndex:"shipPortRemark", //数据列索引，对应于recordType对象中的name属性
			width:300,
			sortable: false//是否排序
		},
		{
			header:"Operation",
			dataIndex:"id",
			renderer :function(value){
				//var mod = '<a href="javascript:modTypeById('+value+')">修改</a>';
				
				var nbsp = "&nbsp &nbsp &nbsp"
				var del = '<a href="javascript:del('+value+')">Del</a>';
				return del+nbsp;
			}
		}
	]);
	var toolBar = new Ext.PagingToolbar({
            pageSize: 15,
            store: ds,
            displayInfo: true
            //displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
            //emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		'-',
    		{text:"Del",
    		 cls:"SYSOP_DEL",
    		 iconCls:"page_del",
    		 handler:deleteBatch
    		}
    	]
    });
	var grid = new Ext.grid.GridPanel({
		
		margins: '0 2 0 0',
		region:"center",
		id:"shipportGrid",
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
			forceFit :true
		}
	});
	
	//分页基本参数
	ds.load({params:{start:0, limit:15}});
	/*----------------------------------------------------*/
	
	var form = new Ext.form.FormPanel({
		title:"Create|Updae|Search",
		region: 'east',
		id:"shipportFormId",
		formId:"shipportForm",
        frame: true,
        collapsible:true,
        monitorValid:true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 110,
        defaultType: 'textfield',
        buttonAlign:'center',
        defaults: {
            width: 220,
            allowBlank: true
        },
        items:[{
		
			//添加布局
			fieldLabel:"POL_CN*",
			id:"shipPortName",
			name:"shipPortName",
			//allowBlank:false,
			hidden:true,
			hideLabel:true,
			//blankText:"起运港中文名称不能为空",
			anchor:"100%"
			
		},{
			fieldLabel:"Port of Loading*",
			id:"shipPortNameEn",
			name:"shipPortNameEn",
			allowBlank:false,
			//blankText:"起运港英文名称不能为空",
			anchor:"100%"
		},{
			xtype:"textarea",
			fieldLabel:"Remark",
			id:"shipPortRemark",
			name:"shipPortRemark",
			allowBlank:true,
			anchor:"100%"
		},new Ext.form.Hidden({
			id:"id",
			name:"id"
		})],
		buttons:[
		{
			enableToggle:true,
			formBind:true,
			text:"Create",
			cls:"SYSOP_ADD",
			id:'saveBtn',
			iconCls : "page_add",
			handler:saveOrUpdate
		},{
			enableToggle:true,
			text:"Search",
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
        Ext.getCmp('saveBtn').setText("Update");
        Ext.getCmp('saveBtn').setIconClass('page_mod');
    });
    // 单击修改信息 end
});
//添加或修改记录
function saveOrUpdate()
{   
	//表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("shipPortNameEn").getValue();
	var form = Ext.getCmp("shipportFormId").getForm();
	if(!form.isValid())
		return;
	var obj = DWRUtil.getValues("shipportForm");
	var list = new Array();
	list.push(obj); 
	if(id == null || id== "")
	{
		var isExist = false;
		DWREngine.setAsync(false); 
		cotShipPortService.isExistShipPortName(name,function(res){
		    isExist = res;
		});
		//判断是否同名
		if(isExist)
		{
		    Ext.Msg.alert("Message","English name already exists with the Port of Loading");
		    return;
		}
	    cotShipPortService.addShipPorts(list,function(res){
	    	Ext.Msg.alert("Message","Successfully added！");
	    	clearForm("shipportFormId");
	    	reloadGrid("shipportGrid");
		});
		DWREngine.setAsync(true); 
	}
	else
	{
		cotShipPortService.modifyShipPorts(list,function(res){
			if(res){
				Ext.Msg.alert("Message","Successfully update！");
				clearForm("shipportFormId");
				reloadGrid("shipportGrid");
			}
		});
	}
}
//删除
function del(id)
{
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	var isPopedom = 1
	if(isPopedom == 0)//没有删除权限
	{
		Ext.Msg.alert("Message","You do not have permission to delete");
		return;
	}
	var cotShipPort = new CotShipPort();
	var list = new Array();
	cotShipPort.id = id;
	list.push(cotShipPort);
	Ext.MessageBox.confirm('Message', 'Port of Loading sure you delete the selected?', function(btn) {
		if (btn == 'yes') {
			cotShipPortService.deleteShipPorts(list,function(res){
				if(res){
					clearForm("shipportFormId");
					reloadGrid("shipportGrid");
					
				}else{
					Ext.Msg.alert("Message","Other records have been used to the record, can not be removed");
				}
			})
		}
	});
}
function getIds()
{
	var list = Ext.getCmp("shipportGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotShipPort = new CotShipPort();
		cotShipPort.id = item.id
		res.push(cotShipPort);
	});
	return res;
}
function deleteBatch()
{
    var list = getIds();
    if(list.length == 0)
    {	
    	Ext.Msg.alert("Message","Please select records");
    	return;
    }
	Ext.MessageBox.confirm('Message', 'Port of Loading sure you delete the selected?', function(btn) {
		if(btn == 'yes'){
			cotShipPortService.deleteShipPorts(list,function(res){
				result = res;				
				if(result)
				{
					Ext.Msg.alert("Message","Deleted successfully");
					clearForm("shipportFormId");
					reloadGrid("shipportGrid");
					
				}
				Ext.Msg.alert("Message","Other records have been used to the record, can not be removed")
				return; 
			});				 
	    }else{
	       return;
	    }
	});
}
	 