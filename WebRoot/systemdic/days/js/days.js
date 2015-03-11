Ext.onReady(function() {
	DWREngine.setAsync(false);
	var shipData;
	var trgData;
	baseDataUtil.getBaseDicDataMap("CotShipPort", "id", "shipPortNameEn",
			function(res) {
				shipData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTargetPort", "id", "targetPortEnName",
			function(res) {
				trgData = res;
			});
	DWREngine.setAsync(true);

	// 起运港
	var shipPortBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotShipPort&key=shipPortNameEn",
				cmpId : 'shipId',
				fieldLabel : "Port of Loading",
				editable : true,
				valueField : "id",
				allowBlank : true,
				displayField : "shipPortNameEn",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				tabIndex : 1,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 250
			});
	// 目的港
	var targetPortBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotTargetPort&key=targetPortEnName",
		cmpId : 'tarId',
		fieldLabel : "Port of destination",
		editable : true,
		valueField : "id",
		allowBlank : true,
		displayField : "targetPortEnName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 2,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 250
	});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "shipId"
			}, {
				name : "tarId"
			}, {
				name : 'days'
			}, {
				name : "remark"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotdays.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : "Port of Loading",
				sortable : true,
				dataIndex : "shipId",
				width : 200,
				renderer : function(value) {
					return shipData["" + value];
				}
			}, {
				header : "Port of destination",
				sortable : true,
				dataIndex : "tarId",
				width : 200,
				renderer : function(value) {
					return trgData["" + value];
				}
			}, {
				header : "Days",
				dataIndex : "days",
				width : 120,
				sortable : true
			}, {
				header : "Remark",
				dataIndex : "remark",
				width : 200,
				sortable : true
			}, {
				header : "Del",
				dataIndex : "id",
				renderer : function(value) {
					var nbsp = "&nbsp &nbsp &nbsp"
					var del = '<a href="javascript:del(' + value
							+ ')">Delete</a>';
					return del + nbsp;
				}
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 500,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|200|500|all'
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Bulk Delete",
							handler : deleteBatch,
							iconCls : "page_del"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				id : "daysGrid",
				margins : "0 5 0 0",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 500
				}
			});

	var form = new Ext.form.FormPanel({
				title : "Add|Edit",
				region : 'east',
				id : "daysFormId",
				formId : "addForm",
				frame : true,
				buttonAlign : "center",
				width : 260,
				autoHeight : true,
				labelAlign : 'right',
				labelWidth : 120,
				defaultType : 'textfield',
				defaults : {
					width : 200
				},
				monitorValid : true,
				items : [shipPortBox, targetPortBox, {
							fieldLabel : "Days",
							xtype:'numberfield',
							id : "days",
							name : "days",
							decimalPrecision : 0,
							maxValue : 32000,
							allowBlank : false,
							blankText : "Please Enter days",
							anchor : "100%"
						}, {
							xtype : "textarea",
							fieldLabel : "Remark",
							id : "remark",
							name : "remark",
							allowBlank : true,
							anchor : "100%"

						}, new Ext.form.Hidden({
									id : "id",
									name : "id"
								})],
				buttons : [{
							enableToggle : true,
							pressed : true,
							text : "Create",
							width : 65,
							iconCls : "page_add",
							id : "saveBtn",
							formBind : true,
							handler : saveOrUpdate
						}, {
							text : "Reset",
							width : 65,
							iconCls : "page_reset",
							handler : function() {
								form.getForm().reset();
								Ext.getCmp('saveBtn').setText("Create");
								Ext.getCmp('saveBtn').setIconClass('page_add');
							}
						}]
			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});

	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				form.getForm().loadRecord(record);
				shipPortBox.bindPageValue("CotShipPort", "id",record.data.shipId);
				targetPortBox.bindPageValue("CotTargetPort", "id",record.data.tarId);
				Ext.getCmp("saveBtn").setText("Update");
				Ext.getCmp("saveBtn").setIconClass("page_mod");
			});

});
// 添加或修改记录
function saveOrUpdate() {
	var formData = DWRUtil.getValues("addForm");
	
	// 判断编号是否重复
	cotDaysService.checkIsExist(formData.shipId, formData.tarId,formData.id, function(res) {
				if (res == true) {
					Ext.MessageBox.alert('Message',
							'This record already exist!');
				} else {
					cotDaysService.saveOrUpdate(formData, function(res) {
								clearForm("addFormId");
								reloadGrid("daysGrid");
								Ext.MessageBox.alert("Message",
										'Save Successfully！');
							});
				}
			});
}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox
				.alert("Message", 'You do not have permission to delete!');
		return;
	}
	Ext.MessageBox.confirm('Message',
			"Are you sure delete this Record？", function(btn) {
				if (btn == 'yes') {
					var obj = DWRUtil.getValues("addForm");
					var list = new Array();
					list.push(id);
					cotDaysService.deleteDays(list, function(res) {
						Ext.MessageBox.alert("Message", 'Delete Successfully！');
						reloadGrid("daysGrid");
						clearForm("daysFormId");
					});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("daysGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
function deleteBatch() {
	var list = getIds();
	if (list.length == 0) {
		Ext.MessageBox.alert("Message", 'Please select one record!');
		return;
	}
	Ext.MessageBox.confirm('Message', "Are you sure delete these Records？",
			function(btn) {
				if (btn == 'yes') {
					cotDaysService.deleteDays(list, function(res) {
						Ext.MessageBox.alert("Message", 'Delete Successfully!');
						reloadGrid("daysGrid");
						clearForm("daysFormId");
					});
				}
			});
}
