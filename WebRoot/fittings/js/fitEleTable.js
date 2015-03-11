// 配件单价保留几位小数
var fitPriceNum = getDeNum("fitPrecision");
Ext.onReady(function() {
	var _self = this;
	// 厂家数据列表
	comboFac = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=factroyTypeidLv1&id=2",
		sendMethod : "POST",
		cmpId : "facId",
		valueField : "id",
		disabled : true,
		disabledClass : 'combo-disabled',
		displayField : "shortName",
		blankText : "请选择供应商",
		emptyText : "请选择",
		fieldLabel : "供应商",
		anchor : "100%"
	});
	var form = new Ext.form.FormPanel({
				title : "",
				labelWidth : 60,
				labelAlign : "right",
				layout : "column",
				padding : "0px",
				autoWidth : true,
				hideBorders : true,
				height : 40,
				renderTo : "fitInfo",
				frame : true,
				items : [{
							xtype : "panel",
							columnWidth : 0.25,
							layout : "form",
							hideBorders : false,
							items : [{
										xtype : "textfield",
										fieldLabel : "配件编号",
										anchor : "100%",
										readOnly : true,
										name : "fitNo"
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.25,
							layout : "form",
							hideBorders : false,
							items : [{
										xtype : "textfield",
										fieldLabel : "配件名称",
										anchor : "100%",
										readOnly : true,
										name : "fitName"
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.25,
							layout : "form",
							items : [comboFac]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.25,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "采购单位",
										readOnly : true,
										name : "buyUnit",
										anchor : "100%"
									}]
						}]
			});
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	var roleRecord = new Ext.data.Record.create([{
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "fitPrice"
			}, {
				name : "fitUsedCount"
			}, {
				name : "useUnit"
			}, {
				name : "fitCount"
			}, {
				name : "fitDesc"
			}, {
				name : "eId"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotfittings.do?method=queryBlank",
								create : "cotfittings.do?method=add"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord),
				writer : writer,
				listeners : {
					exception : function(proxy, type, action, options, res, arg) {
						if (action == Ext.data.Api.actions.create) {
							var o = Ext.decode(res.responseText);
							if (o["success"] == false) {
								Ext.Msg.alert("提示消息", "分发配件" + o["msg"] + "！");
							} else {
								ds.removeAll();
								// Ext.Msg.alert("提示消息", "分发配件"+o["msg"]+"！");
								parent.closeFen();
							}
						}
					}
				}
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, {
							header : "样品编号",
							dataIndex : "eleId",
							width : 50,
							hidden : true
						}, {
							header : "货号",
							dataIndex : "eId",
							width : 130
						}, {
							header : "中文名",
							dataIndex : "eleName",
							width : 130
						}, {
							header : "单价",
							dataIndex : "fitPrice",
							width : 120
						}, {
							header : "单位",
							dataIndex : "useUnit",
							width : 100
						}, {
							header : "<font color=blue>配件规格</font>",
							dataIndex : "fitDesc",
							width : 100,
							editor : new Ext.form.TextField({
										maxLength : 100,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color=blue>用量</font>",
							dataIndex : "fitUsedCount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "<font color=blue>数量</font>",
							dataIndex : "fitCount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 9999999.999,
										decimalPrecision : 3,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}]
			});
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "查看已分发",
							handler : parent.showPanelFen,
							iconCls : "page_sel"
						}, '-', {
							text : "选择货号",
							handler : showPanel,
							iconCls : "page_sel"
						}, '-', {
							text : "删除",
							handler : deleteSelectRow,
							iconCls : "page_del"
						}, '-', {
							text : "保存",
							handler : save,
							iconCls : "page_table_save",
							cls : "SYSOP_ADD"
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "fitGrid",
				height : 450,
				stripeRows : true,
				bodyStyle : 'width:100%',
				renderTo : "fitTbl",
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				// selModel: new
				// Ext.grid.RowSelectionModel({singleSelect:false}),
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				// layout:"fit",
				viewConfig : {
					forceFit : true
				}
			});

	// 显示样品查询面板
	var eleTablePanel;
	function showPanel(item, pressed) {
		if (eleTablePanel == null) {
			eleTablePanel = new EleTablePanel({
						bar : _self
					});
			var obj = Ext.fly(eleTablePanel.printName);
			var ary = obj.getCenterXY();
			obj.setX(ary[0] - 300);
			obj.setY(0);
			eleTablePanel.render(obj);
			eleTablePanel.load();
			// var proxy1 = new Ext.dd.DDProxy(eleTablePanel.printName);
		} else {
			if (!eleTablePanel.isVisible()) {
				eleTablePanel.show();
				eleTablePanel.load();
			} else {
				eleTablePanel.hide();
			}
		}
	};

	// 配件单号
	var fitNo;
	// 配件名称
	var fitName;
	// 配件规格
	var fitDesc;
	// 单价(采购价格/换算率)
	var onePrice;
	// 领用单位
	var useUnit;
	// 初始化
	var comboFac;
	function init() {
		var fitId = $('fitId').value;
		cotFittingsService.getFittingById(fitId, function(res) {
					DWRUtil.setValues(res);
					if (res.facId != null) {
						comboFac.bindValue(res.facId)
					}
					fitNo = res.fitNo;
					fitName = res.fitName;
					fitDesc = res.fitDesc;
					onePrice = (res.fitPrice / res.fitTrans).toFixed(fitPriceNum);
					useUnit = res.useUnit;
				});

	}
	init();
	// 根据输入的样品货号添加样品到可编辑表格
	this.insertSelect = function(list) {
		cotFittingsService.getEleFitIds($('fitId').value, function(ls) {
					var array = ls.split(",");
					var str = "";
					Ext.each(list, function(ary) {
								var idx = ds.find("eleId", ary.id);
								if (idx == -1) {
									// 判断该配件是否已经分发给该货号
									var flag = false;
									for (var i = 0; i < array.length; i++) {
										if (ary.data.id == array[i]) {
											str += ary.data.eleId + ",";
											flag = true;
										}
									}
									if (!flag) {
										var u = new ds.recordType({
													eId : ary.data.eleId,
													eleName : ary.data.eleName,
													fitPrice : onePrice,
													fitUsedCount : 1,
													useUnit : useUnit,
													fitDesc : fitDesc,
													fitCount : 1,
													eleId : ary.data.id
												});
										ds.add(u);
									}
								}
							});
					if (str != "") {
						Ext.MessageBox
								.alert("提示消息", "该配件已分发给" + str + "不能再分发！");
					}
				});

	}

	function save() {
		var urli = "cotfittings.do?method=add" + '&fitPri=' + onePrice
				+ '&fitId=' + $('fitId').value;
		ds.proxy.setApi({
					create : urli
				});
		ds.save();
	}
	function deleteSelectRow() {
		var list = Ext.getCmp("fitGrid").getSelectionModel().getSelections();
		var ds = Ext.getCmp("fitGrid").getStore();
		Ext.each(list, function(rec) {
					ds.remove(rec);
				})
	}
});
