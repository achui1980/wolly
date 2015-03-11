Ext.onReady(function(){
	
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"insureName"},
		{name:"insureNameEn"},
		{name:"remark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotinsurecontract.do?method=query"
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
			header:"契约名称(中文)", //表头
			dataIndex:"insureName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"契约名称(英文)", //表头
			dataIndex:"insureNameEn", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"备注", //表头
			dataIndex:"remark", //数据列索引，对应于recordType对象中的name属性
			width:300,
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
		
		//title:'保险契约列表',
		//frame:true,
		iconCls:"gird_list",
		margins: '0 2 0 0',
		region:"center",
		id:"insurecontractGrid",
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
		id:"insurecontractFormId",
		formId:"insurecontractForm",
        frame: true,
        collapsible:true,
        monitorValid:true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 90,
        defaultType: 'textfield',
        buttonAlign:'center',
        defaults: {
            width: 220,
            allowBlank: false
        },
        items:[{
			fieldLabel:"契约名称(中文)",
			id:"insureName",
			name:"insureName",
			allowBlank:false,
			blankText:"契约名称(中文)不能为空",
			anchor:"100%"
			
		},{
			fieldLabel:"契约名称(英文)",
			id:"insureNameEn",
			name:"insureNameEn",
			allowBlank:false,
			blankText:"契约名称(英文)不能为空",
			anchor:"100%"
			
		},{
			xtype:"textarea",
			fieldLabel:"备注",
			id:"remark",
			name:"remark",
			allowBlank:true,
			anchor:"100%"
		},new Ext.form.Hidden({
			id:"id",
			name:"id"
		})],
		buttons:[
		{
			enableToggle:true,
			//pressed:true,
			formBind:true,
			text:"新增",
			id:"saveBtn",
			cls:"SYSOP_ADD",
			iconCls : "page_add",
			handler:saveOrUpdate
		},{
			text:"重置",
			iconCls : "page_reset",
			handler:function()
			{
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
	var name = Ext.get("insureName").getValue();
	var form = Ext.getCmp("insurecontractFormId").getForm();
	if(!form.isValid())
		return;
	var obj = DWRUtil.getValues("insurecontractForm");
	
	var cotInsureContract = new CotInsureContract();
	for(p in cotInsureContract){
		cotInsureContract[p] = obj[p];
	}
	if(id == null || id== "")
	{
		var isExist = false;
		DWREngine.setAsync(false); 
		cotInsureContractService.isExistName(name,function(res){
		    isExist = res;
		});
		//判断是否同名
		if(isExist)
		{
		    Ext.Msg.alert("提示信息","已存在同名保险契约");
		    return;
		}
		
	   	cotInsureContractService.addInsureContract(cotInsureContract,function(res){
			Ext.Msg.alert("提示信息","添加成功！");
			clearForm("insurecontractFormId");
			reloadGrid("insurecontractGrid");
		})
		DWREngine.setAsync(true); 
	}
	else
	{
		var isExist = false;
		DWREngine.setAsync(false); 
		cotInsureContractService.findExistNameById(id,name,function(res){
		    isExist = res;
		});
		//判断是否同名
		if(isExist)
		{
		    Ext.Msg.alert("提示信息","已存在同名保险契约");
		    return;
		}
		
		cotInsureContractService.modifyInsureContract(cotInsureContract,function(res){
			Ext.Msg.alert("提示信息","修改成功！");
			clearForm("insurecontractFormId");
			reloadGrid("insurecontractGrid");
		})
		DWREngine.setAsync(true); 
	}
}
//删除
function del(id)
{
	var isPopedom = getPopedomByOpType(vaildUrl,"DEL");
	var isPopedom = 1
	if(isPopedom == 0)//没有删除权限
	{
		Ext.Msg.alert("提示信息","您没有删除权限");
		return;
	}
	var cotSignType = new CotSignType();
	var list = new Array();
	cotSignType.id = id;
	list.push(cotSignType);
	Ext.MessageBox.confirm('提示信息', '确定删除选中的保险契约吗?', function(btn) {
		if (btn == 'yes') {
			cotInsureContractService.deleteInsureContract(list,function(res){
				if(res){
					Ext.Msg.alert("提示信息","删除成功");
					clearForm("insurecontractFormId");
					reloadGrid("insurecontractGrid");
					
				}else{
					Ext.Msg.alert("提示信息","已有其它记录使用到该记录,无法删除");
				}
			})
		}
	});
}
function getIds()
{
	var list = Ext.getCmp("insurecontractGrid").getSelectionModel().getSelections();
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
	Ext.MessageBox.confirm('提示信息', '确定删除选中的保险契约吗?', function(btn) {
		if(btn == 'yes'){
			cotInsureContractService.deleteInsureContract(list,function(res){				
				if(res)
				{
					Ext.Msg.alert("提示信息","删除成功");
					clearForm("insurecontractFormId");
					reloadGrid("insurecontractGrid");
					
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
	 