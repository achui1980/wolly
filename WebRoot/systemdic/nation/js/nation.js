Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"nationName"},
		{name:"nationCode"}
		
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotnation.do?method=query"
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
			header:"Country Name", //表头
			dataIndex:"nationName", //数据列索引，对应于recordType对象中的name属性
			width:120,
			sortable: true//是否排序
		},
		{
			header:"Country Code", //表头
			dataIndex:"nationCode", //数据列索引，对应于recordType对象中的name属性
			width:200,
			sortable: true//是否排序
		},
		{
			header:"OP",
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
            //displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
            displaySize:'5|10|15|20|all'
            //emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		{text:"Bulk Delete",handler:deleteBatch,iconCls:"page_del"}
    	]
    });
	var grid = new Ext.grid.GridPanel({
		iconCls:"gird_list",
		region:"center",
		id:"nationGrid",
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
		title:"Add|Edit",
		region: 'east',
		id:"nationFormId",
		formId:"nationForm",
        frame: true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        buttonAlign:"center",
        labelWidth: 100,
        defaultType: 'textfield',
        monitorValid:true,
        defaults: {
            width: 200,
            allowBlank: false
        },
        items:[{
		
			//添加布局
			fieldLabel:"Country Name",
			id:"nationName",
			name:"nationName",
			allowBlank:false,
			blankText:"Please Enter Country Name",
			anchor:"95%"
			
		},{
		
			//添加布局
			fieldLabel:"Country Code",
			id:"nationCode",
			name:"nationCode",
			allowBlank:true,
			anchor:"95%"
			
		},{
		
			//添加布局
			xtype:"textarea",
			fieldLabel:"Remark",
			id:"nationRemark",
			name:"nationRemark",
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
			text:"Create",
			width:65,
			iconCls:"page_add",
			id:"saveBtn",
			formBind: true,
			handler:saveOrUpdate
		},{
			text:"Reset",
			width:65,
			iconCls:"page_reset",
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
    
     // 单击修改信息 start
    grid.on('rowclick', function(grid, rowIndex, event) {
        var record = grid.getStore().getAt(rowIndex);
        form.getForm().loadRecord(record);
        Ext.getCmp("saveBtn").setText("Update");
		Ext.getCmp("saveBtn").setIconClass("page_mod");
    });
});
//添加或修改记录
	function saveOrUpdate()
	{   
	    //表单验证
		var id = Ext.get("id").getValue();
		var roleName = Ext.get("nationName").getValue();
		var form = Ext.getCmp("nationFormId").getForm();
		if(!form.isValid())
			return;
		if(id == null || id == "")
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("nationForm");
		    var cotNation = new CotNation();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotNation[p] = obj[p];
		    }
		    list.push(cotNation);
		    var isExist = false;
		    DWREngine.setAsync(false); 
		    cotNationService.findExistByName(cotNation.lvName,function(res){
		    	isExist = res;
		    });
		    //判断是否同名
		    if(isExist)
		    {
		    	Ext.Msg.alert("Message","Country Name already exist!");
		    	return;
		    }
		    //添加员工
		    cotNationService.addNation(list,function(res){
				reloadGrid("nationGrid");
			});	 
			DWREngine.setAsync(true); 
		}
		else
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("nationForm");
		    var cotNation = new CotNation();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotNation[p] = obj[p];
		    }
		    cotNation.id = id;
		    list.push(cotNation);
		    cotNationService.modifyNation(list,function(res){
		    	Ext.Msg.alert("Message","Save Successfully!");
				reloadGrid("nationGrid");
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
		Ext.Msg.alert("Message","You do not have permission to delete!");
		return;
	}
    var flag = window.confirm("Are you sure delete this Country?");
	if(flag){
    var obj = DWRUtil.getValues("nationForm");
  	var cotNation = new CotNation();
    var list = new Array();
    cotNation.id = id;
    list.push(cotNation);
     cotNationService.deleteNation(list,function(res){
		result = res;
		if(result == -1)
		{
			Ext.Msg.alert("Message","Delete Failed!");
			return; 
		}
		Ext.Msg.alert("Message","Delete Successfully!");
		reloadGrid("nationGrid");
		clearForm("nationFormId");
	});
	}else{
		    return;
    }
}
function getIds()
{
	var list = Ext.getCmp("nationGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotNation = new CotNation();
		cotNation.id = item.id
		res.push(cotNation);
	});
	return res;
}	
function deleteBatch()
		{
		    var list = getIds();
		    if(list.length == 0)
		    	{	
		    		Ext.Msg.alert("Message","Please select one record!");
		    		return;
		    	}
			var flag = window.confirm("Are you sure delete these Countrys?");
			if(flag){
			          
					  cotNationService.deleteNation(list,function(res){
					  		if(res == -1)
						    {
						    	Ext.Msg.alert("Message","Delete Failed!");
						    	return; 
						    }
						    Ext.Msg.alert("Message","Delete Successfully!");
							reloadGrid("nationGrid");
							clearForm("nationFormId");
							
					});	
		    }else{
		       return;
		    }
		}
			 