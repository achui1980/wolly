Ext.onReady(function(){
	/********EXT创建grid步骤*********/
	/*1、创建数据记录类型类型 Ext.data.Record.create*/
	/*2、创建数据存储对象(数据源) Ext.data.Store*/
	/*3、创建需要在表格显示的列 Ext.grid.ColumnModel*/
	/*4、创建表格对象，加载数据 Ext.grid.GridPanel*/
	/*****************/
	var roleRecord = new Ext.data.Record.create([
		{name:"id",type:"int"},
		{name:"commisionName"},
		{name:"commisionRemark"}
	]);
	//创建数据源
	var ds = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:"cotcommisiontype.do?method=query"
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
			dataIndex:"id", 
			width:50,
			sortable: true,//是否排序
			hidden:true
		},
		{
			header:"Themes", //表头
			dataIndex:"commisionName", 
			width:120,
			sortable: true//是否排序
		},
		{
			header:"Remark", //表头
			dataIndex:"commisionRemark", 
			width:300,
			sortable: true//是否排序
		},
		{
			header:"OP",
			dataIndex:"id",
			renderer :function(value, metaData, record, rowIndex, colIndex, store){
				//var popedom = '<a href="javascript:openRolePopedomWin('+value+')">权限</a>';
				var nbsp = "&nbsp &nbsp &nbsp"
				var del = '<a href="javascript:del('+value+')">Del</a>';
				return del+nbsp;
			}
		}
	]);
	var toolBar = new Ext.PagingToolbar({
            pageSize: 15,
            store: ds,
            displayInfo: true,
//            displayMsg: '显示第 {0} - {1}条记录 共{2}条记录',
            displaySize:'5|10|15|20|all'
//            emptyMsg: "No data to display"
        });
    var tb = new Ext.Toolbar({
    	items:[
    		'->',
    		{text:"Del",handler:deleteBatch,iconCls:"page_del"}
    	]
    });
	var grid = new Ext.grid.GridPanel({

		region:"center",
		id:"comGrid",
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
		title:"Create|Update",
		region: 'east',
		id:"comFormId",
		formId:"comForm",
        frame: true,
        width: 260,
        autoHeight: true,
        labelAlign: 'right',
        labelWidth: 60,
        defaultType: 'textfield',
        monitorValid:true,
        buttonAlign:'center',
        defaults: {
            width: 200,
            allowBlank: false
        },
        items:[{
		
			//添加布局
			fieldLabel:"Themes",
			id:"commisionName",
			name:"commisionName",
			allowBlank:false,
			blankText:"Themes can not Empty",
			anchor:"100%"
			
		},{
			xtype:"textarea",
			fieldLabel:"Remark",
			id:"commisionRemark",
			name:"commisionRemark",
			allowBlank:true,
			anchor:"100%"
		},new Ext.form.Hidden({
			id:"id",
			name:"id"
		})],
		buttons:[
			{
			enableToggle:true,
			text:"Create",
			iconCls:"page_add",
			id:'saveBtn',
			cls:"SYSOP_MOD",
			formBind: true,
			handler:saveOrUpdate
		},{
			text:"Reset",
			enableToggle:true,
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
        Ext.getCmp("saveBtn").setText("Modify");
		Ext.getCmp("saveBtn").setIconClass("page_mod");
    });
});
//添加或修改记录
	function saveOrUpdate()
	{   
	    //表单验证
		var id = Ext.get("id").getValue();
		var roleName = Ext.get("commisionName").getValue();
		var form = Ext.getCmp("comFormId").getForm();
		if(id == null || id == "")
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("comForm");
		   var cotCommision = new CotCommisionType();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotCommision[p] = obj[p];
		    }
		    list.push(cotCommision);
		    var isExist = false;
		    DWREngine.setAsync(false); 
		    cotCommisionService.findExistByName(roleName,function(res){
		    	isExist = res;
		    });
		    //判断是否同名
		    if(isExist)
		    {
		    	Ext.Msg.alert("Message","Themes already exists, please re-enter！");
		    	return;
		    }
		    //添加员工
		    cotCommisionService.addCommision(list,function(res){
				reloadGrid("comGrid");
			});	 
			DWREngine.setAsync(true); 
		}
		else
		{
		    //表单验证结束
		    var obj = DWRUtil.getValues("comForm");
		    var cotCommision = new CotCommisionType();
		    var list = new Array();
		    for(var p in obj)
		    {
		    	cotCommision[p] = obj[p];
		    }
		    cotCommision.id = id;
		    list.push(cotCommision);
		    cotCommisionService.modifyCommision(list,function(res){
			if(res){
				Ext.Msg.alert("Message","Successfully save！");
				reloadGrid("comGrid");
			}else{
				Ext.Msg.alert("Message","Save Failed");
			}
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
		Ext.Msg.alert("Message","You do not have permission to delete！");
		return;
	}
    var flag = window.confirm("Sure to delete the selected record ?");
	if(flag){
    var obj = DWRUtil.getValues("comForm");
    var cotCommision = new CotCommisionType();
    var list = new Array();
    for(var p in obj)
    {
    	cotCommision[p] = obj[p];
    }
    cotCommision.id = id;
    list.push(cotCommision);
    cotCommisionService.deleteCommision(list,function(res){
		result = res;
		if(result == -1)
		{
			Ext.Msg.alert("Message","Successfully failed！");
			return; 
		}
		Ext.Msg.alert("Message","Successfully deleted！");
		reloadGrid("comGrid");
		clearForm("comFormId");
	});
	}else{
		    return;
    }
}
function getIds()
{
	var list = Ext.getCmp("comGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list,function(item){
		var cotCommision = new CotCommisionType();
		cotCommision.id = item.id
		res.push(cotCommision);
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
			var flag = window.confirm("Sure to delete the selected record ?");
			if(flag){
			          
					 cotCommisionService.deleteCommision(list,function(res){
							Ext.Msg.alert("Message","Successfully deleted！");
							reloadGrid("comGrid");
							clearForm("comFormId");
							
					});	
		    }else{
		       return;
		    }
		}