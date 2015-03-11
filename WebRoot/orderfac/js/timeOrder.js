Ext.onReady(function() {

	var otherRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "stateFlag"
			}, {
				name : "flagName"
			}, {
				name : "planDate",
				type:'jsondate'
			}, {
				name : "comleteDate",
				type:'jsondate'
			}, {
				name : "orderId"
			}, {
				name : "orderFacId"
			},{
				name : 'groupType'
			},{
				name : 'remark'
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var otherDs = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
			api : {
				read : "cotorderfac.do?method=queryTime&orderId="
						+ parent.$('pId').value,
				create : "cotorderfac.do?method=addTime",
				update : "cotorderfac.do?method=modifyTime"
			},
			listeners : {
				// 保存表格前显示提示消息
				beforewrite : function(proxy, action, rs, options, arg) {
					mask();
				},
				load : function(store, recs, options) {
					options.callback = function(r, ops, success) {
						if (success == false) {
							Ext.Msg.alert("Message", "Data Query Error! Please contact the administrator!");
						} else {}
					}
				},
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("Message", "Operation failed！");
					} else {
						otherDs.reload();
					}
					unmask();
				}
			}
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, otherRecord),
		writer : writer
	});
	// 创建复选框列
	var otherSm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [otherSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Type",
							dataIndex : "groupType",
							width : 120,
							renderer : function(value) {
								if (value == 'SAMPLE') {
									return 'Pre-production';
								}
								if (value == 'PACKAGE') {
									return 'Artwork Approval';
								}
								if (value == 'SAMPLEOUT') {
									return 'Shipment Samples';
								}
								if (value == 'QC') {
									return 'Quality Control';
								}
								if (value == 'OUT') {
									return 'Shipment';
								}
							}
						}, {
							header : "Project Title",
							dataIndex : "flagName",
							width : 200
						}, {
							header : "<font color=blue>Deadline</font>",
							dataIndex : "planDate",
							width : 120,
							editor : new Ext.form.DateField({
										format : 'Y-m-d',
										allowBlank:false,
										readOnly:true,
										blankText:'Please fill in the planned date!'
									}),
							renderer:Ext.util.Format.dateRenderer('Y-m-d')
						}, {
							header : "<font color=blue>Approval</font>",
							dataIndex : "comleteDate",
							width : 120,
							editor : new Ext.form.DateField({
										format : 'Y-m-d',
										allowBlank:false,
										blankText:'Please fill in the date of completion!'
									}),
							renderer:Ext.util.Format.dateRenderer('Y-m-d')
						}, {
							header : "orderId",
							dataIndex : "orderId",
							hidden : true
						}, {
							header : "Remark",
							dataIndex : "remark",
							hidden : true
						} ,{
							header : "orderFacId",
							dataIndex : "orderFacId",
							hidden : true
						}, {
							header : "stateFlag",
							dataIndex : "stateFlag",
							hidden : true
						}]
			});

	var toolBar = new Ext.Toolbar({
				items : [{
							text : "Save",
							handler : save,
							iconCls : "gird_save"
						}]
			});
	var otherGrid = new Ext.grid.EditorGridPanel({
				id : "timeGrid",
				flex : 1,
				stripeRows : true,
				margins : "0 5 0 0",
				border : false,
				cls : 'rightBorder',
				store : otherDs, // 加载数据源
				cm : cm, // 加载列
				sm : otherSm,
				loadMask : true, // 是否显示正在加载
				tbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	otherGrid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	/** *****布局********************************************************************************* */
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [otherGrid]
			});
	/** ************************************************************************************** */
	otherDs.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 添加空白record到表格中
	function save() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message', 'Sorry, the order has been reviewed can not be modified!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		otherDs.save();
	}
});