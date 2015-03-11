Ext.onReady(function(){
	
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"givenName"},
		{name:"givenRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotgiventype.do?method=query"
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
			header:"Art Work", //表头
			dataIndex:"givenName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"Remark", //表头
			dataIndex:"givenRemark", //数据列索引，对应于recordType对象中的name属性
			width:300,
			sortable: true//是否排序
		},
		{
			header:"OP",
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
						pageSize : 15,
						store : ds,
						displayInfo : true,
						// //displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all'
						// emptyMsg : "No data to display"
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
		id:"giventypeGrid",
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
		title:"Create|Update|Search",
		region: 'east',
		id:"giventypeFormId",
		formId:"giventypeForm",
        frame: true,
        collapsible:true,
        monitorValid:true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 60,
        defaultType: 'textfield',
        buttonAlign:'center',
        defaults: {
            width: 220,
            allowBlank: false
        },
        items:[{
			fieldLabel:"Art Work",
			id:"givenName",
			name:"givenName",
			allowBlank:false,
			blankText:"Art Work is require",
			anchor:"100%"
			
		},{
			xtype:"textarea",
			fieldLabel:"Remark",
			id:"givenRemark",
			name:"givenRemark",
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
        Ext.getCmp('saveBtn').setText("Modify");
        Ext.getCmp('saveBtn').setIconClass('page_mod');
    });
    // 单击修改信息 end
});
//添加或修改记录
function saveOrUpdate()
{   
	//表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("givenName").getValue();
	var form = Ext.getCmp("giventypeFormId").getForm();
	if(!form.isValid())
		return;
	var obj = DWRUtil.getValues("giventypeForm");
	var list = new Array();
	list.push(obj); 
	if(id == null || id== "")
	{
	   	cotGivenTypeService.addGivenTypes(list,function(res){
			if(res){
				Ext.Msg.alert("Message","Save Successfuly");
				clearForm("giventypeFormId");
				reloadGrid("giventypeGrid");
			}else{
				Ext.Msg.alert("Message","Art Work already exists, please re-enter！");
			}
					
		})
	}
	else
	{
		cotGivenTypeService.modifyGivenTypes(list,function(res){
			if(res){
				Ext.Msg.alert("Message","Save Successfuly!");
				clearForm("giventypeFormId");
				reloadGrid("giventypeGrid");
			}else{
				Ext.Msg.alert("Message","Art Work already exists, please re-enter！");
			}
		})
	}
}
//删除
function del(id)
{
	//var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	var isPopedom = 1
	if(isPopedom == 0)//没有删除权限
	{
		Ext.Msg.alert("Message","You do not have permission to delete！")
		return;
	}
	var cotGivenType = new CotGivenType();
	var list = new Array();
	cotGivenType.id = id;
	list.push(cotGivenType);
	Ext.MessageBox.confirm('Message', 'Sure to delete the selected record ?', function(btn) {
		if (btn == 'yes') {
			cotGivenTypeService.deleteGivenTypes(list,function(res){
				if(res){
					Ext.Msg.alert("Message","Successfully deleted！");
					clearForm("giventypeFormId");
					reloadGrid("giventypeGrid");
				}else{
					Ext.Msg.alert("Message","Successfully failed！");
				}
			})
		}
	});
}
function getIds()
{
	var list = Ext.getCmp("giventypeGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotGivenType = new CotGivenType();
		cotGivenType.id = item.id
		res.push(cotGivenType);
	});
	return res;
}
function deleteBatch()
{
    var list = getIds();
    if(list.length == 0)
    {	
    	Ext.Msg.alert("Message","Please select records！");
    	return;
    }
	Ext.MessageBox.confirm('Message', 'Sure to delete the selected records ?', function(btn) {
		if(btn == 'yes'){
			cotGivenTypeService.deleteGivenTypes(list,function(res){			
				if(res)
				{
					Ext.Msg.alert("Message","Successfully deleted");
					clearForm("giventypeFormId");
					reloadGrid("giventypeGrid");
				}else{
					Ext.Msg.alert("Message","Successfully Failed")
					return; 
				}
			});				 
	    }else{
	       return;
	    }
	});
}
	 