Ext.onReady(function() {

	// 加载表格需要关联的外键名
	var fromPersonMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				fromPersonMap = res;
			});
	DWREngine.setAsync(true);

	var toPersonMap;
	DWREngine.setAsync(false);
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				toPersonMap = res;
			});
	DWREngine.setAsync(true);

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
				name : "msgOrderNo"
			}, {
				name : "msgTypeId",
				type : "int"
			}, {
				name : "msgBeginDate"
			}, {
				name : "msgEndDate"
			}, {
				name : "msgContent"
			}, {
				name : "msgStatus",
				type : 'int'
			}, {
				name : "msgFlag",
				type : 'int'
			}, {
				name : "msgFromPerson",
				type : 'int'
			}, {
				name : "msgToPerson",
				type : 'int'
			}, {
				name : "addTime"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotmessage.do?method=query"
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
				}, {
					header : "提醒单号",
					dataIndex : "msgOrderNo",
					width : 160
				}, {
					header : "消息提醒类型",
					dataIndex : "msgTypeId",
					width : 100,
					renderer : function(value) {
						if (value == 1) {
							return '提醒';
						}
						if (value == 2) {
							return '互发';
						}
						if (value == 3) {
							return '工作计划';
						}
						if (value == 4) {
							return '群发';
						}
					}
				}, {
					header : "起始时间",
					dataIndex : "msgBeginDate",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "结束时间",
					dataIndex : "msgEndDate",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "提醒内容",
					dataIndex : "msgContent",
					width : 240
				}, {
					header : "消息状态",
					dataIndex : "msgStatus",
					width : 120,
					renderer : function(value) {
						if (value == 0) {
							return "<span style='color:red;font-weight:bold;'>未读</span>";
						}
						if (value == 1) {
							return '已读,未处理';
						}
						if (value == 2) {
							return '已读,已处理';
						}
						if (value == 3) {
							return '未处理,提醒结束';
						}
						if (value == 4) {
							return '只读';
						}
					}
				}, {
					header : "是否提醒",
					dataIndex : "msgFlag",
					width : 80,
					renderer : function(value) {
						if (value == 0) {
							return '否';
						}
						if (value == 1) {
							return "<span style='color:blue;font-weight:bold;'>是</span>";
						}
					}
				}, {
					header : "发送人",
					dataIndex : "msgFromPerson",
					width : 65,
					renderer : function(value) {
						return "<span style='color:green;'>"
								+ fromPersonMap[value] + "</span>";
					}
				}, {
					header : "接收人",
					dataIndex : "msgToPerson",
					width : 65,
					renderer : function(value) {
						if (value == 0) {
							return "<span style='color:red;font-weight:bold;'>ALL</span>";
						} else {
							return toPersonMap[value];
						}
					}
				}, {
					header : "添加时间",
					dataIndex : "addTime",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return value.year + "-" + (value.month + 1) + "-"
									+ value.day;
						}
					}
				}, {
					header : "操作",
					dataIndex : "id",
					width : 50,
					renderer : function(value) {
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">删除</a>';
						return del + nbsp;
					}
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				emptyMsg : "No data to display"
			});
	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "新增",
							cls : "SYSOP_ADD",
							iconCls : "page_add",
							handler : windowopenAdd
						}, '-', {
							text : "修改",
							cls : "SYSOP_MOD",
							iconCls : "gird_edit",
							handler : windowopenMod
									.createDelegate(this, [null])
						}, '-', {
							text : "删除",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}]
			});
	var grid = new Ext.grid.GridPanel({

				margins : '0 2 0 0',
				region : "center",
				id : "messageGrid",
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

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	/*----------------------------------------------------*/
	var typeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[1, '提醒'], [2, '互发'], [3, '工作计划'], [4, '群发']]
			});

	var typeField = new Ext.form.ComboBox({
				name : 'msgTypeIdFind',
				fieldLabel : '消息提醒类型',
				editable : true,
				store : typeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'msgTypeIdFind',
				selectOnFocus : true
			});

	var statusStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [[0, '未读'], [1, '已读，未处理'], [2, '已读，已处理'],
						[3, '未处理，提醒结束'], [4, '只读']]
			});

	var statusField = new Ext.form.ComboBox({
				name : 'msgStatusFind',
				fieldLabel : '消息状态',
				editable : true,
				store : statusStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'msgStatusFind',
				selectOnFocus : true
			});

	var form = new Ext.form.FormPanel({
				title : "查询",
				region : 'east',
				id : "messageQueryFormId",
				formId : "messageQueryForm",
				collapsible : true,
				frame : true,
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 100,
				monitorValid : true,
				defaultType : 'textfield',
				buttonAlign : 'center',
				defaults : {
					width : 220,
					allowBlank : true
				},
				items : [{
							// 添加布局
							fieldLabel : "提醒单号",
							id : "noFind",
							name : "noFind",
							anchor : "100%"
						}, typeField, statusField, {
							xtype : "datefield",
							fieldLabel : "开发时间",
							anchor : "100%",
							id : 'startTime',
							name : "startTime",
							vtype : 'daterange',
							endDateField : 'endTime'
						}, {
							xtype : "datefield",
							fieldLabel : "至",
							anchor : "100%",
							hideLabel : false,
							labelSeparator : " ",
							vtype : 'daterange',
							id : "endTime",
							name : "endTime",
							startDateField : 'startTime'
						}],
				buttons : [{
							enableToggle : true,
							// pressed:true,
							text : "查询",
							iconCls : "page_sel",
							handler : function() {
								ds.reload({params : {
												start : 0,
												limit : 15
											}})
							}
						}, {
							text : "重置",
							iconCls : "page_reset",
							handler : function() {
								form.getForm().reset()
							}
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});

	ds.on('beforeload', function() {

				ds.baseParams = form.getForm().getValues();

			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	// 获得选择的记录
	function getIds() {
		var list = Ext.getCmp("messageGrid").getSelectionModel()
				.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var cotMessage = new CotMessage();
					cotMessage.id = item.id
					res.push(cotMessage);
				});
		return res;
	}

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.Msg.alert("提示信息", "请选择记录");
			return;
		}
		Ext.MessageBox.confirm('提示信息', '是否确定删除所选的系统消息?', function(btn) {
			if (btn == 'yes') {
				// 如果是admin直接删除
				if (loginEmpId == 'admin') {
					cotMessageService.deleteMessage(list, function(res) {
								result = res;
								if (result == -1) {
									Ext.Msg.alert("提示信息", "删除失败!");
								} else {
									reloadGrid("messageGrid");
									clearForm("messageQueryFormId");
									Ext.Msg.alert("提示信息", "删除成功");
								}
							});
				} else {
					DWREngine.setAsync(false);
					cotMessageService.checkIsCurUser(list, function(res) {
								if (res == true) {
									cotMessageService.deleteMessage(list,
											function(res) {
												result = res;
												if (result == -1) {
													Ext.Msg
															.alert("提示信息",
																	"已有其它记录使用到该记录,无法删除");
													return;
												} else {
													Ext.Msg.alert("提示信息",
															"删除成功");
													reloadGrid("messageGrid");
													clearForm("messageQueryFormId");
												}
											});
								} else {
									Ext.MessageBox.show({
												title : '提示信息',
												msg : '部分消息还未被处理，不能删除！',
												height : 260,
												width : 200,
												buttons : Ext.MessageBox.OK,
												icon : Ext.MessageBox.WARNING
											});
									return;
								}
							});
					DWREngine.setAsync(true);
				}
			}
		});
	}

	// 打开新增页面
	function windowopenAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有添加权限
		{
			Ext.Msg.alert("提示信息", "您没有添加权限");
			return;
		}
		openWindowBase(260, 700, 'cotmessage.do?method=add&Flag=2');
	}

	// 打开编辑页面
	function windowopenMod(obj) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
		if (isPopedom == 0)// 没有修改权限
		{
			Ext.Msg.alert("提示信息", "您没有修改权限");
			return;
		}
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.Msg.alert("提示信息", "请选择一条记录");
				return;
			} else if (ids.length > 1) {
				Ext.Msg.alert("提示信息", "只能选择一条记录!")
				return;
			} else {
				obj = ids[0].id;
			}
		}
		DWREngine.setAsync(false);
		// 判断发送者是否为当前用户
		cotMessageService.recvIsCur(obj, function(recv) {
					if (recv == true) {
						openWindowBase(270, 700,
								'cotmessage.do?method=modify&id=' + obj);
					} else {
						// 判断发送者是否为消息接收者
						cotMessageService.sendIsCur(obj, function(send) {
									if (send == true) {
										openWindowBase(270, 700,
												'cotmessage.do?method=send&id='
														+ obj);
									}
								});
					}
				});
		DWREngine.setAsync(true);
	}
});

// 删除
function del(id) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示信息", "您没有删除权限");
		return;
	}
	var cotMessage = new CotMessage();
	var list = new Array();
	cotMessage.id = id;
	list.push(cotMessage);

	Ext.MessageBox.confirm('提示信息', '是否确定删除所选的系统消息?', function(btn) {
		if (btn == 'yes') {
			// 如果是admin直接删除
			if (loginEmpId == 'admin') {
				cotMessageService.deleteMessage(list, function(res) {
							result = res;
							if (result == -1) {
								Ext.Msg.alert("提示信息", "删除失败!");
							} else {
								reloadGrid("messageGrid");
								clearForm("messageQueryFormId");
								Ext.Msg.alert("提示信息", "删除成功");
							}
						});
			} else {
				cotMessageService.sendIsCur(id, function(res) {
					if (res == true) {
						cotMessageService.getMessageById(parseInt(id),
								function(msg) {
									if (msg.msgStatus >= 2) {
										cotMessageService.deleteMessage(list,
												function(res) {
													result = res;
													if (result == -1) {
														Ext.Msg.alert("提示信息",
																"删除失败");
														return;
													} else {
														reloadGrid("messageGrid");
														clearForm("messageQueryFormId");
														Ext.Msg.alert("提示信息",
																"删除成功");
													}
												});
									} else {
										Ext.Msg.alert("提示信息",
												"该消息还未被处理，您还不能执行删除操作！");
									}
								});
					} else {
						Ext.Msg.alert("提示信息", "您不能对该消息执行删除操作！");
					}
				});
			}
		}
	});
}
