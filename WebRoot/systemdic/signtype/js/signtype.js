Ext.onReady(function(){
	
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"signName"},
		{name:"signRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotsigntype.do?method=query"
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
			header:"征样方式名称", //表头
			dataIndex:"signName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"备注", //表头
			dataIndex:"signRemark", //数据列索引，对应于recordType对象中的name属性
			width:300,
			sortable: false//是否排序
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
            emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		'-',
    		{text:"批量删除",
    		 cls:"SYSOP_DEL",
    		 iconCls:"page_del",
    		 handler:deleteBatch
    		}
    	]
    });
	var grid = new Ext.grid.GridPanel({
		
		margins: '0 2 0 0',
		region:"center",
		id:"signtypeGrid",
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
		title:"新增|编辑",
		region: 'east',
		id:"signtypeFormId",
		formId:"signtypeForm",
        frame: true,
        collapsible:true,
        monitorValid:true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 85,
        defaultType: 'textfield',
        buttonAlign:'center',
        defaults: {
            width: 220,
            allowBlank: false
        },
        items:[{
			fieldLabel:"征样方式名称",
			id:"signName",
			name:"signName",
			allowBlank:false,
			blankText:"征样方式名称不能为空",
			anchor:"100%"
			
		},{
			xtype:"textarea",
			fieldLabel:"备注",
			id:"signRemark",
			name:"signRemark",
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
			text:"新增",
			cls:"SYSOP_ADD",
			id:'saveBtn',
			iconCls : "page_add",
			handler:saveOrUpdate
		},{
			text:"重置",
			iconCls : "page_reset",
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
    
    ds.on('beforeload', function() {
	 
		ds.baseParams =form.getForm().getValues();
 
	});
    
     // 单击修改信息 start
    grid.on('rowclick', function(grid, rowIndex, event) {
        var record = grid.getStore().getAt(rowIndex);
        form.getForm().loadRecord(record);
        Ext.getCmp('saveBtn').setText("修改");
        Ext.getCmp('saveBtn').setIconClass('page_mod');
    });
    // 单击修改信息 end
});
//添加或修改记录
function saveOrUpdate()
{   
	//表单验证
	var id = Ext.get("id").getValue();
	var name = Ext.get("signName").getValue();
	var form = Ext.getCmp("signtypeFormId").getForm();
	if(!form.isValid())
		return;
	var obj = DWRUtil.getValues("signtypeForm");
	var list = new Array();
	list.push(obj); 
	if(id == null || id== "")
	{
	   	cotSignTypeService.addSignTypes(list,function(res){
			if(res){
				Ext.Msg.alert("提示信息","添加成功！");
				clearForm("signtypeFormId");
				reloadGrid("signtypeGrid");
			}else{
				Ext.Msg.alert("提示信息","添加失败！征样方式名称已存在!");
			}
					
		})
	}
	else
	{
		cotSignTypeService.modifySignTypes(list,function(res){
			if(res){
				Ext.Msg.alert("提示信息","修改成功！");
				clearForm("signtypeFormId");
				reloadGrid("signtypeGrid");
			}else{
				Ext.Msg.alert("提示信息","该征样方式名称已存在！");
			}
		})
	}
}
//删除
function del(id)
{
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	var isPopedom = 1
	if(isPopedom == 0)//没有删除权限
	{
		Ext.Msg.alert("提示信息","您没有删除权限！");
		return;
	}
	var cotSignType = new CotSignType();
	var list = new Array();
	cotSignType.id = id;
	list.push(cotSignType);
	Ext.MessageBox.confirm('提示信息', '确定删除选中的征样方式吗?', function(btn) {
		if (btn == 'yes') {
			cotSignTypeService.deleteSignTypes(list,function(res){
				if(res){
					Ext.Msg.alert("提示信息","删除成功");
					clearForm("signtypeFormId");
					reloadGrid("signtypeGrid");
				}else{
					Ext.Msg.alert("提示信息","已有其它记录使用到该记录,无法删除");
				}
			})
		}
	});
}
function getIds()
{
	var list = Ext.getCmp("signtypeGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotSignType = new CotSignType();
		cotSignType.id = item.id
		res.push(cotSignType);
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
	Ext.MessageBox.confirm('提示信息', '确定删除选中的征样方式吗?', function(btn) {
		if(btn == 'yes'){
			cotSignTypeService.deleteSignTypes(list,function(res){				
				if(res)
				{
					Ext.Msg.alert("提示信息","删除成功");
					clearForm("signtypeFormId");
					reloadGrid("signtypeGrid");
					
				}else{
					Ext.Msg.alert("提示信息","已有其它记录使用到该记录,无法删除")
					return; 
				}
			});				 
	    }else{
	       return;
	    }
	});
}
	 