Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"lvName"},
		{name:"lvRemark"}
		
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotcustomerlv.do?method=query" 
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
			header:"等级名称", //表头
			dataIndex:"lvName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"备注", //表头
			dataIndex:"lvRemark", //数据列索引，对应于recordType对象中的name属性
			width:200,
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
		iconCls:"gird_list",
		region:"center",
		id:"lvGrid",
		//autoHeight:true,
		stripeRows:true,
		margins:"0 5 0 0",
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
		id:"lvFormId",
		formId:"lvForm",
        frame: true,
        width: 260,
        buttonAlign:"center",
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 60,
        monitorValid:true,
        defaultType: 'textfield',
        defaults: {
            width: 200,
            allowBlank: false
        },
        items:[{
		
			//添加布局
			fieldLabel:"等级名称",
			id:"lvName",
			name:"lvName",
			allowBlank:false,
			blankText:"等级名称",
			anchor:"95%"
			
		},{
		
			//添加布局
			xtype:"textarea",
			fieldLabel:"备注",
			id:"lvRemark",
			name:"lvRemark",
			allowBlank:true,
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
			id:"saveBtn",
			iconCls:"page_add",
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
	    //表单验证
		var id = Ext.get("id").getValue();
		var roleName = Ext.get("lvName").getValue();
		var form = Ext.getCmp("lvFormId").getForm();
		if(!form.isValid())
			return;
		if(id == null || id == "")
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("lvForm");
		    var cotCustomerLv = new CotCustomerLv();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotCustomerLv[p] = obj[p];
		    }
		    list.push(cotCustomerLv);
		    var isExist = false;
		    DWREngine.setAsync(false); 
		    cotCustomerLvService.findExistByName(cotCustomerLv.lvName,function(res){
		    	isExist = res;
		    });
		    //判断是否同名
		    if(isExist)
		    {
		    	Ext.MessageBox.alert("提示信息",'已存在同名客户等级！');
		    	return;
		    }
		    //添加员工
		    cotCustomerLvService.addCustomerLv(list,function(res){
				reloadGrid("lvGrid");
			});	 
			DWREngine.setAsync(true); 
		}
		else
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("lvForm");
		     var cotCustomerLv = new CotCustomerLv();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotCustomerLv[p] = obj[p];
		    }
		    cotCustomerLv.id = id;
		    list.push(cotCustomerLv);
		    cotCustomerLvService.modifyCustomerLv(list,function(res){
		    	Ext.MessageBox.alert("提示信息",'修改成功！');
				reloadGrid("lvGrid");
			});	
		}
		
	
	}
//删除
function  del(id)
{
	//添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	if(isPopedom == 0)//没有删除权限
	{
		Ext.MessageBox.alert("提示信息",'您没有删除权限！');
		return;
	}
    var flag = window.confirm("确定删除?");
	if(flag){
    var obj = DWRUtil.getValues("lvForm");
   var cotCustomerLv = new CotCustomerLv();
    var list = new Array();
    cotCustomerLv.id = id;
    list.push(cotCustomerLv);
     cotCustomerLvService.deleteCustomerLv(list,function(res){
		result = res;
		if(result == -1)
		{
			Ext.MessageBox.alert("提示信息",'已有其它记录使用到该记录,无法删除！');
			return; 
		}
		Ext.MessageBox.alert("提示信息",'删除成功！');
		reloadGrid("lvGrid");
		clearForm("lvFormId");
	});
	}else{
		    return;
    }
}
function getIds()
{
	var list = Ext.getCmp("lvGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotCustomerLv = new CotCustomerLv();
		cotCustomerLv.id = item.id
		res.push(cotCustomerLv);
	});
	return res;
}	
function deleteBatch()
		{
		    var list = getIds();
		    if(list.length == 0)
		    	{	
		    		Ext.MessageBox.alert("提示信息",'请选择记录！');
		    		return;
		    	}
			var flag = window.confirm("确定删除?");
			if(flag){
			          
					  cotCustomerLvService.deleteCustomerLv(list,function(res){
					  		if(res == -1)
						    {
						    	Ext.MessageBox.alert("提示信息",'已有其它记录使用到该记录,无法删除！');
						    	return; 
						    }
							Ext.MessageBox.alert("提示信息",'删除成功！');
							reloadGrid("lvGrid");
							clearForm("lvFormId");
							
					});	
		    }else{
		       return;
		    }
		}
			 