
Ext.onReady(function() {
	DWREngine.setAsync(false);
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	DWREngine.setAsync(true);

	/** ******EXT创建grid步骤******** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "empId",
				type : "int"
			}, {
				name : "relate"
			},{
				name : "ruleDesc" 
			},{
				name : "ruleName"
			},{
				name : "type"
			},{
				name : "xmlPath"
			}, {
				name : "ruleDefault",
				type : "int"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotmailrule.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm,{
							header : "业务员",
							dataIndex : "empId",
							width : 100,
							renderer : function(value) {
								return empsMap[value];
							}
						},  {
							header : "规则名称",
							dataIndex : "ruleName",
							width : 350

						}, {
							header : "规则描述",
							dataIndex : "ruleDesc",
							width : 350

						},{
							header : "规则执行",
							dataIndex : "ruleDefault",
							width : 200,
							renderer:function(value){
								if(value=='1'){
									return '按个人配置规则执行'
								}
								if(value=='0'){
									return '按公共配置规则执行'
								}
							}

						},{
							header : "xmlPath",
							dataIndex : "xmlPath",
							hidden : true

						},{
							header : "type",
							dataIndex : "type",
							hidden : true

						},{
							header : "操作",
							dataIndex : "id",
							width:300,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">修改</a>';
								var nbsp = "&nbsp &nbsp &nbsp"
								var del = '<a href="javascript:del(' + value
										+ ')">删除</a>';
							    var execute='<a href="javascript:switchRule('+ value +')">设置运行规则</a>';	
								var empsId =record.data.empId;
								return mod + nbsp + del+ nbsp+execute;
							}
						}]
			});

		
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "创建所有规则",
							handler : createRule,
							iconCls : "page_add"
						}, {
							text : "新建",
							handler : windowopenAdd,
							iconCls : "page_add"
						}, '-', {
							text : "修改",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod"
						}, '-', {
							text : "删除",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "ruleGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	ds.on('beforeload', function() {
				ds.baseParams = DWRUtil.getValues("queryForm");
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPersonFind',
				fieldLabel : "业务员",
				editable : true,
				valueField : "id",
				displayField : "empsName",
				pageSize : 5,
				anchor : "95%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : '请选择',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	var form = new Ext.FormPanel({
				region : 'north',
				height : 50,
				id : "fitFormId",
				formId : "queryForm",
				buttonAlign : 'right', // 按钮居右显示
				labelAlign : "right",
				layout : 'column',
				padding : '5',
				border : false,
				plain : true,
				frame : true,
				items : [{
							layout : 'form',
							columnWidth : .25,
							labelWidth : 55, // 标签宽度
							items : [busiBox]
						}, {
							layout : 'table',
							columnWidth : .15,
							layoutConfig : {
								columns : 2
							},
							items : [{
										xtype : 'button',
										text : "查询",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											ds.reload();
										}
									}, {
										xtype : 'button',
										text : "重置",
										iconCls : "page_reset",
										style : {
											marginLeft : '10px'
										},
										width : 65,
										handler : function() {
											form.getForm().reset()
										}
									}]
						}]
			});
	// 构造
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});
	viewport.doLayout();

	// 表格双击
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
	
	//创建所有规则
	function createRule(){
		if(ds.getCount() >0){
			var recordAll =Ext.getCmp("ruleGrid").getStore().getRange();
			var res = new Array();
			Ext.each(recordAll, function(item) {
						res.push(item.data.id);
					});
		}
		
		Ext.MessageBox.confirm('提示信息', '创建所有规则时会删除原有的规则,确定创建吗？', function(btn) {
			if (btn == 'yes') {
				    var panel =new Ext.Panel({
				    	frame:true,
				    	layout:'column',
				    	items:[
				    		{
				    			columnWidth:.5,
				    			items:[{
				    				xtype:'radio',
				    				boxLabel:'按个人配置规则执行',
				    				name:'rule',
				    				width:200,
				    				anchor:'100%',
				    				id:'privateRule',
				    				checked:true
				    			}]
				    		},{
				    			columnWidth:.5,
				    			items:[{
				    				xtype:'radio',
				    				name:'rule',
				    				anchor:'100%',
				    				width:200,
				    				id:'publicRule',
				    				boxLabel:'按公共配置规则执行'
				    			}]
				    		}
				    	]
				    
				    });
				    var wind=new Ext.Window({
				    	title:'规则执行方式',
				    	width:400,
				    	items:[panel],
				    	buttonAlign:'center',
				    	buttons:[{
				    		text:'确定',
				    		handler:function(){
				    			var privateRule=Ext.getCmp('privateRule').getValue();
				    		//	var publicRule=Ext.getCmp('publicRule').getValue();
				    			var ruleDf;
				    			if(privateRule){
				    				 ruleDf=1;
				    			}else{
				    				 ruleDf=0;
				    			}
				    			mailRuleService.deleteEmpsRuleAll(res,ruleDf,function(res){
									reloadGrid('ruleGrid');
									if(res==true){
										mailRuleService.createPublicMailRuleFile();
										Ext.Msg.alert("提示消息","创建成功");
									}else{
										Ext.Msg.alert("提示消息","创建失败");
										}
								});
				    			wind.close();
				    		}
				    	}]
				    
				    });
				    wind.show();
				}
		});
		
	}		
});
			
	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", "请选择记录");
			return;
		}
		Ext.MessageBox.confirm('提示信息', '确定删除勾选的规则吗?', function(btn) {
			if (btn == 'yes') {
				if (btn == 'yes') {
					mailRuleService.deleteEmpsRuleList(list,function(res){
							reloadGrid('ruleGrid');
							if(res==true){
								Ext.Msg.alert("提示消息","删除成功");
							}
					});
				}
			}
		});

	}
	
	
	
	// 新增
	function windowopenAdd() {
		
		openFullWindow('cotmailrule.do?method=addRule');
	}
	
	// 获得勾选的记录
	function getIds() {
		var list = Ext.getCmp("ruleGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}
	
	//切换邮件规则
	function switchRule(value){
		var ruleDf =Ext.getCmp("ruleGrid").getSelectionModel().getSelected().get('ruleDefault');
		mailRuleService.switchRule(value,function(){
			Ext.getCmp("ruleGrid").getStore().reload();
		});
	}
	
	// 打开订单编辑页面  
	function windowopenMod(obj) {
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.MessageBox.alert("提示消息", "请选择一条记录");
				return;
			} else if (ids.length > 1) {
				Ext.MessageBox.alert("提示消息", "只能选择一条记录!")
				return;
			} else
				obj = ids[0];
		}
		openFullWindow('cotmailrule.do?method=addRule&id=' + obj);
	}

	
	// 删除
	function del(id) {
//		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
//		if (isPopedom == 0) {
//			Ext.MessageBox.alert("提示消息", "您没有删除权限");
//			return;
//		}
		var list = new Array();
		list.push(id);
		Ext.MessageBox.confirm('提示信息', '确定删除此规则吗?', function(btn) {
					if (btn == 'yes') {
						mailRuleService.deleteEmpsRuleList(list,function(res){
								reloadGrid('ruleGrid');
								if(res==true){
									Ext.Msg.alert("提示消息","删除成功");
								}
						});
					}
				});
	}
