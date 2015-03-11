Ext.onReady(function() {
	DWREngine.setAsync(false);
	var companyMap;
	baseDataUtil.getBaseDicDataMap("CotCompany", "id", "companyShortName",
			function(res) {
				companyMap = res;
			});
	var deptMap;
	baseDataUtil.getBaseDicDataMap("CotDept", "id", "deptName", function(res) {
				deptMap = res;
			});
	var roleMap;
	baseDataUtil.getBaseDicDataMap("CotRole", "id", "roleName", function(res) {
				roleMap = res;
			});
	DWREngine.setAsync(true);
	var companyName = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&flag=pic",
				valueField : "id",
				emptyText : "Affiliated companies",
				autoShow : true,
				allowBlank : true,
				width : 100,
				displayField : "companyShortName",
				cmpId : "companyId",
				isSearchField : true,
				searchName : 'companyId'
			});
	var deptName = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotDept",
				valueField : "id",
				emptyText : "Respective departments",
				autoShow : true,
				width : 100,
				allowBlank : true,
				displayField : "deptName",
				cmpId : "deptId",
				isSearchField : true,
				searchName : 'deptId'
			});

	// 下拉框级联操作
	companyName.on('select', function() {
				deptName.reset();
				deptName.loadValueById('CotDept', 'companyId',
						companyName.value)
			});

	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "empsId"
			}, {
				name : "empsName"
			}, {
				name : "empNameCn"
			}, {
				name : "companyId",
				type : "int"
			}, {
				name : "deptId",
				type : "int"
			}, {
				name : "roleId",
				type : "int"
			}, {
				name : "empsStatus",
				type : "int"
			}, {
				name : "empsMail"
			}, {
				name : "empsPhone"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotemps.do?method=queryList"
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
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				},{
					header : "Operation",
					dataIndex : "id",
					width : 200,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var r = '<a href=# onclick=javascript:openPopedomWin('
								+ value + ',"' + escape(record.data.empsName)
								+ '")>Right</a>';
						var t = '<a href=# onclick=javascript:openStatPopedomWin('
								+ value
								+ ',"'
								+ escape(record.data.empsName)
								+ '")>Data</a>';
						var del = '<a href=# onclick="javascript:deleteBatch('
								+ value + ')">Delete</a>';
						var copy='<a href=# onclick=javascritp:openCopyRrightWin('
								+ value + ',"' + escape(record.data.empsId)
								+ '")>Copy</a>';
						var nbsp = "&nbsp &nbsp &nbsp"
						return r + nbsp + t + nbsp + del + nbsp +copy;
					}
				}, {
					header : "Username",
					dataIndex : "empsName",
					width : 80
				}, {
					header : "Login",
					dataIndex : "empsId",
					width : 80
				}, {
					header : "name",
					dataIndex : "empNameCn",
					width : 110
				}, {
					header : "Affiliated companies",
					dataIndex : "companyId",
					width : 130,
					renderer : function(value) {
						return companyMap[value];
					}
				}, {
					header : "Department",
					dataIndex : "deptId",
					width : 80,
					renderer : function(value) {
						return deptMap[value];
					}
				}, {
					header : "Respective roles",
					dataIndex : "roleId",
					width : 110,
					renderer : function(value) {
						return roleMap[value];
					}
				}, {
					header : "Status",
					dataIndex : "empsStatus",
					width : 60,
					renderer : function(value) {
						if (value == 0) {
							return "At work"
						} else if (value == 1) {
							return "Not working"
						}
					}
				}, {
					header : "E-mail address",
					dataIndex : "empsMail",
					width : 180
				}, {
					header : "Phone",
					dataIndex : "empsPhone",
					width : 150
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	var statusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['', 'User status'], [-1, 'At work'], [1, 'Not working']]
			});

	var statusField = new Ext.form.ComboBox({
				name : 'empsStatusFind',
				emptyText : 'User status',
				editable : false,
				store : statusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'empsStatusFind',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'empsStatusFind'
			});
			
	var auditStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, 'All'], [1, 'Audit Person']]
			});

	var auditField = new Ext.form.ComboBox({
				name : 'shenFlag',
				emptyText : 'Audit',
				editable : false,
				store : auditStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'shenFlag',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'shenFlag'
			});
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', companyName, deptName, statusField, {
							xtype : "textfield",
							emptyText : "Username",
							width : 95,
							isSearchField : true,
							searchName : 'empsNameFind'
						},auditField, {
							xtype : 'searchcombo',
							width : 95,
							emptyText : "Login",
							isSearchField : true,
							searchName : 'empsIdFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : windowopenAdd
						}, '-', {
							text : "Update",
							cls : "SYSOP_MOD",
							iconCls : "page_mod",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "Delete",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var cb = new Ext.form.ComboBox()
	var grid = new Ext.grid.GridPanel({
				id : "empsGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	/*----------------------------------------------------*/
	// 绑定下拉框,需要设置同步，不然无法绑定
	// bindSel();
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});
	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
});

function getIds() {
	var list = Ext.getCmp("empsGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 批量删除
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.Msg.alert("Message", "Please select records");
		return;
	}
	Ext.MessageBox.confirm('Message', "Are you sure you delete the selected employees?", function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					var num = 0;
					for (var i = 0; i < list.length; i++) {
						cotEmpsService.deleteEmps(list[i], function(res) {
									if (res == false) {
										num++;
									}
								});
					}
					if (num > 0) {
						Ext.Msg.alert("Message", "Deleted successfully!There are" + num + "employees shall not remove cited!");
					} else {
						Ext.Msg.alert("Message", "Deleted successfully!");
					}
					reloadGrid('empsGrid');
					DWREngine.setAsync(true);
				}
			});

}

function openPopedomWin(obj, empname) {
	openWindowBase(400, 360, 'cotpopedom.do?method=query&id=' + obj
					+ '&empname=' + unescape(empname));
}

function openStatPopedomWin(obj, empname) {
	openWindowBase(400, 360, 'cotstatistics.do?method=query&isChkTree=true&id='
					+ obj + '&empname=' + unescape(empname));
}

// 打开新增页面
function windowopenAdd() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.MessageBox.alert('提示信息', "您没有添加权限");
		return;
	}
	openWindowBase(325, 700, 'cotemps.do?method=addEmps');
}

// 打开编辑页面
function windowopenMod(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.MessageBox.alert('提示信息', "您没有修改权限");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert('提示信息', "请选择一条记录");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert('提示信息', "只能选择一条记录!")
			return;
		} else
			obj = ids[0];

	}
	openWindowBase(311, 700, 'cotemps.do?method=addEmps&id=' + obj);
}
//权限复制
function openCopyRrightWin(idd,name){
	DWREngine.setAsync(false);
	var empsMap={};
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
		empsMap = res;
	});
	DWREngine.setAsync(true);
	var datas = [];
	for(var p in empsMap){
		if("admin"!=empsMap[p]){
		datas.push([p,empsMap[p]]);
		}
	}
	var empStore = new Ext.data.ArrayStore({
		fields:['id','name'],
		data:datas
	});
	var win = new Ext.Window({
				title : 'Copy Right：',
				width : 300,
				height : 160,
				layout : 'fit',
				modal : true,
				border : false,
				items : [{
							xtype : 'form',
							frame : true,
							padding : 5,
							labelWidth : 60,
							monitorValid : true,
							labelAlign : 'right',
							buttonAlign : 'center',
							fbar : [{
										text : 'Save',
										formBind : true,
										iconCls : "page_table_save",
										handler : function() {
											Ext.MessageBox.confirm("Info","Are you sure copy ["+$('emps').value+"]'s right to ["+name+"]",function(button,text){
												if(button=='yes'){
												cotEmpsService.copyEmpRight($('empsid').value,idd, function(res) {
													if(res==1){
														Ext.Msg.alert("Info", "Copy success!");
														win.close();
													}else{
														Ext.Msg.alert("Info","Copy fail!");
													}
												});
										    }
										})
										}
									}],
							items : [
									{
										xtype:'combo',
							    		anchor:'100%',
							    		ref:'empField',
							    		allowBlank:false,
							    		displayField:'name',
							    		triggerAction:'all',
							    		valueField:'id',
							    		fieldLabel : "User",
							    		mode:'local',
							    		forceSelection:true,
							    		isSearchField:true,
										searchName:'empId',
										id : 'emps',
										name : 'emps',
										hiddenId:'empsid',
										hiddenName:'empsid',
							    		store:empStore
									},{
										html:'<br><div align="center" style="color:red">Please choose a user for ['+name+']</div>'
										
									}]
				}]	
	})
	win.show();
}