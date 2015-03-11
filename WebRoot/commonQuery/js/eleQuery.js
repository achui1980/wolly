EleQueryWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var facNum = getDeNum("facPrecision");
	var outNum = getDeNum("outPrecision");
	var cbmNum = getDeNum("cbmPrecision");
	// 根据小数位生成"0.000"字符串
	var facStrNum = getDeStr(facNum);
	var outStrNum = getDeStr(outNum);
	var cbmStr = getDeStr(cbmNum);

	var curData;
	var fac = null;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				fac = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "factoryId"
			},  {
				name : "barcode"
			}, {
				name : "eleName"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "priceOut",
				convert : numFormat.createDelegate(this, [outStrNum], 3)
			}, {
				name : "priceOutUint"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, [facStrNum], 3)
			}, {
				name : "priceFacUint"
			},{
				name:"eleInchDesc"
			},{
				name:'eleNameEn'
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryElements"
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
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				sortable : true,
				hidden : true
			}, {
				header : "Art No.",
				dataIndex : "eleId",
				width : 120,
				sortable : true
			}, {
				header : "Supplier",
				dataIndex : "factoryId",
				width : 80,
				renderer:function(value){
					return fac[value];
				},
				sortable : true
			}, {
				header : "Barcode",
				dataIndex : "barcode",
				sortable : true,
				width : 110
			}, {
				header : "Descripiton",
				dataIndex : "eleNameEn",
				width : 120
			},{
				header : "Price",
				dataIndex : "priceOut",
				sortable : true,
				width : 60,
				renderer : function(value) {
					return "<span style='color:blue;font-weight:bold;'>"
							+ value + "</span>";
				}
			}, {
				header : "Currency",
				dataIndex : "priceOutUint",
				sortable : true,
				width : 60,
				renderer : function(value) {
					if (value != null && value != 0) {
						return "<span style='color:blue;font-weight:bold;'>"
								+ curData["" + value] + "</span>";
					} else {
						return "";
					}
				}
			}, {
				header : "P.O Price",
				dataIndex : "priceFac",
				sortable : true,
				width : 70,
				renderer : function(value) {
					return "<span style='color:red;font-weight:bold;'>" + value
							+ "</span>";
				}
			}, {
				header : "Currency",
				dataIndex : "priceFacUint",
				sortable : true,
				width : 60,
				renderer : function(value) {
					if (value != null && value != 0) {
						return "<span style='color:red;font-weight:bold;'>"
								+ curData["" + value] + "</span>";
					} else {
						return "";
					}
				}
			}]);

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								mask();
								setTimeout(function() {
											insert();
										}, 500);
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|100|200|all'
				//emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				//cls : "leftBorder rightBorder",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border:false,
				viewConfig : {
					forceFit : false
				},
				plugins:[new Ext.ux.grid.GridRowSelect()],
				selectFn:function(record){
					$("picPathSel").src = "./showPicture.action?flag=ele&elementId="
							+ record.data.id;
					DrawImage($("picPathSel"), 135, 120);
				}
			});

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryIdFind',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 5,
				anchor : "92%",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 大类
	var typeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv4&key=typeEnName",
				cmpId : 'query_eleTypeidLv1',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				disabledClass : 'combo-disabled',
				displayField : "typeEnName",
				emptyText : 'Select',
				pageSize : 10,
				anchor:'92%',
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type2Box.setDataUrl("servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="+rec.data.id);
//							type2Box.resetData();
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 中类
	var type2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'query_eleTypeidLv2',
				fieldLabel : "Group",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Select',
				anchor:'92%',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type3Box.setDataUrl("servlet/DataSelvert?tbname=CotTypeLv3&key=typeName&typeName=typeIdLv2&type="+rec.data.id);
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 小类
	var type3Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'query_eleTypeidLv3',
				fieldLabel : "Category",
				editable : true,
				anchor:'92%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Select',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 表单
	var form = new Ext.form.FormPanel({
				labelWidth : 90,
				region : 'center',
				labelAlign : "right",
				layout : "form",
				autoScroll : true,
				bodyStyle : "overflow-x:hidden;",
				border : false,
				buttonAlign : 'center',
				keys : [{
							key : Ext.EventObject.ENTER,
							fn : function() {
								var isPicMode = Ext.getCmp("chkPicMode")
										.getValue();
								if (isPicMode)
									showPicDs.reload();
								else
									ds.reload();
							}
						}],
				buttons : [{
							text : "Search",
							iconCls : "page_sel",
							width : 60,
							handler : function() {
								var isPicMode = Ext.getCmp("chkPicMode")
										.getValue();
								if (isPicMode)
									showPicDs.reload();
								else
									ds.reload();

							}
						}, {
							text : "Reset",
							iconCls : "page_reset",
							width : 60,
							handler : function() {
								form.getForm().reset();
							}
						}],
				items : [{
							xtype : "checkbox",
							boxLabel : "只检索子货号",
							id : 'childFind',
							name : 'childFind',
							inputValue : "true",
							anchor : "92%",
							hideLabel : true,
							hidden:true,
							style : {
								marginLeft : '25px'
							},
							labelSeparator : " "
						}, {
							xtype : "textfield",
							id : 'eleIdFind',
							name : 'eleIdFind',
							fieldLabel : "Art No.",
							anchor : "92%"
						}, facBox,{
							xtype : "textfield",
							fieldLabel : "Material",
							id : 'eleNameFind',
							name : 'eleNameFind',
							anchor : "92%"
						}, {
							xtype : "textfield",
							fieldLabel : "Description",
							id : 'eleNameEnFind',
							name : 'eleNameEnFind',
							anchor : "92%"
						}, {
							xtype : "textfield",
							fieldLabel : "Design",
							id : 'eleColFind',
							name : 'eleColFind',
							anchor : "92%"
						}, {
							xtype : "numberfield",
							fieldLabel : "Year",
							hideLabel : true,
							hidden:true,
							id : 'eleTypenameLv2Find',
							name : 'eleTypenameLv2Find',
							anchor : "92%",
							maxValue : 9999
						},{
							xtype : "textarea",
							fieldLabel : "Size(cm)",
							hideLabel : true,
							hidden:true,
							id : 'eleSizeDescFind',
							name : 'eleSizeDescFind',
							height:30,
							anchor : "92%"
						},{
							xtype : "textarea",
							fieldLabel : "Size(inch)",
							height:30,
							hideLabel : true,
							hidden:true,
							id : 'eleInchDescFind',
							name : 'eleInchDescFind',
							anchor : "92%"
						}, typeBox,type2Box,type3Box, {
							xtype : "datefield",
							fieldLabel : "Date",
							anchor : "92%",
							id : 'startTime',
							name : 'startTime',
							format : "Y-m-d",
							vtype : 'daterange',
							endDateField : 'endTime'
						}, {
							xtype : "datefield",
							fieldLabel : "-------",
							anchor : "92%",
							format : "Y-m-d",
							id : 'endTime',
							name : 'endTime',
							vtype : 'daterange',
							labelSeparator : " ",
							startDateField : 'startTime'
						}]
			});

	// 单击修改信息 start
//	grid.on('rowclick', function(grid, rowIndex, event) {
//				var record = grid.getStore().getAt(rowIndex);
//				$("picPathSel").src = "./showPicture.action?flag=ele&elementId="
//						+ record.data.id;
//				DrawImage($("picPathSel"), 135, 120);
//			});

	// 获得选择的记录
	var getIds = function() {
		var res = new Array();
		var list = null;
		var isPicMode = Ext.getCmp("chkPicMode").getValue();
		if (isPicMode) {
			list = picView.getSelectedRecords();
		} else {
			list = sm.getSelections();
		}
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 添加
	function insert() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select a record!');
			unmask();
			return;
		}
		var ary = null;
		var isPicMode = Ext.getCmp("chkPicMode").getValue();
		if (isPicMode) {
			ary = picView.getSelectedRecords();
		} else {
			ary = sm.getSelections();
		}
		cfg.bar.insertSelect(ary);
		//Ext.MessageBox.alert("Message", 'Import succeeded!');
	}

	var picPnl = new Ext.Panel({
		region : 'north',
		height : 135,
		layout : "hbox",
		autoScroll : true,
		layoutConfig : {
			padding : '5',
			pack : 'center',
			align : 'middle'
		},
		items : [{
			xtype : "panel",
			width : 135,
			html : '<div align="center" style="width: 135px; height: 120px;">'
					+ '<img src="common/images/zwtp.png" id="picPathSel" name="picPathSel"'
					+ 'onload="javascript:DrawImage(this,135,120)" /></div>'
		}]
	});

	var leftPnl = new Ext.Panel({
				title : "Query",
				margins : "0 5 0 0",
				collapsible : true,
				region : 'west',
				layout : 'border',
				frame : true,
				width : 220,
				items : [picPnl, form]
			});

	// 直接把该货号添加到表格
	var opForm = new Ext.form.FormPanel({
		region : 'north',
		height : 40,
		padding : '5',
		hidden:true,
		frame : true,
		layout : 'form',
		items : [{
			xtype : 'radiogroup',
			fieldLabel : "",
			hideLabel : true,
			columns : [190, 80, 50, 20, 150, 80],
			items : [{
						boxLabel : 'Added to the product,copy from',
						inputValue : 0,
						style : "marginLeft:8",
						name : 'noEleRdo',
						id : "noEleRdo1",
						checked : true
					}, {
						xtype : "textfield",
						id : 'canEle',
						name : 'canEle',
						labelSeparator : " ",
						fieldLabel : "",
						hideLabel : true,
						listeners : {
							"specialkey" : function(txt, eObject) {
								var temp = txt.getValue();
								if (temp != ""
										&& eObject.getKey() == Ext.EventObject.ENTER) {
									openNewEleWin();
								}
							}
						}
					}, {
						xtype : "button",
						text : 'Copy',
						handler : openNewEleWin
					}, {
						xtype : "label",
						text : ''
					}, {
						boxLabel : 'Only as New Art No.',
						id : "noEleRdo2",
						inputValue : 1,
						name : 'noEleRdo',
						listeners : {
							'check' : function(chk, check) {
								if (check) {
									cfg.bar.noEleAsNew(cfg.eleIdFind);
									_self.close();
								}
							}
						}

					}, {
						xtype : "checkbox",
						boxLabel : 'Pic View',
						id : "chkPicMode",
						inputValue : 1,
						disabled : true,
						name : 'chkPicMode',
						listeners : {
							'check' : function(chk, check) {
								changeToPicMode();
							}
						}

					}]
		}]
	});

	/** *********************图片模式****************************************** */
	// 创建图片数据源
	var showPicDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryElements"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});

	var toolbar_pic = new Ext.PagingToolbar({
				pageSize : 10,
				store : showPicDs,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all'
				//emptyMsg : "No data to display"
			})
	var thumbTemplate = new Ext.XTemplate(
			'<tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb" style="width: 150px; height: 150px;"><img src="showPicture.action?flag=ele&elementId={id}" onclick="showBigPicDiv(this)" onload="DrawImage(this,150,150)"></div>',
			'<span>{eleId}</span>' + '</div>', '</tpl>');
	thumbTemplate.compile();
	var picView = new Ext.DataView({
				id : "picView",
				tpl : thumbTemplate,
				multiSelect : true,
				autoScroll : true,
				bodyStyle : "overflow-x:hidden;",
				overClass : 'x-view-over',
				itemSelector : 'div.thumb-wrap',
				emptyText : '<div style="padding:10px;">No record</div>',
				plugins : new Ext.DataView.DragSelector(),
				store : showPicDs
			});
	// 样品表格顶部工具栏
	var tb_pic = new Ext.Toolbar({
				items : ['->', {
							text : "Select all／Not select",
							handler : selectAllPic,
							iconCls : "gird_save"
						}, '-', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								mask();
								setTimeout(function() {
											insert();
										}, 500);
							}
						}]
			});
	var panel_pic = new Ext.Panel({
				id : "img-chooser-view",
				tbar : tb_pic,
				bbar : toolbar_pic,
				border:false,
				region : "center",
				layout : 'fit',
				items : [picView]
			});

	// 切换到图片模式
	function changeToPicMode() {
	}

	// picView.on("click",function(picView,index,node){
	// // alert(index);
	// // var node1 = node;
	// // var nod = "aaa";
	// });
	var tbPnl = new Ext.TabPanel({
				region : "center",
				activeTab : 0,
				items : [{
							title : "List",
							id : "listMode",
							layout : "border",
							items : [grid]
						}, {
							title : "picture",
							id : "picMode",
							layout : "border",
							items : [panel_pic]
						}]
			});

	/** ********************************************************************* */

	var bottomPanel = new Ext.Panel({
				layout : 'border',
				region : 'center',
				border : false,
				items : [tbPnl, leftPnl]
			});

	// 前往样品添加页面
	function openNewEleWin() {
		if ($('canEle').value == '') {
			Ext.MessageBox.alert("Message", "请填写样品模版货号!");
			return;
		}
		var url = './cotquery.do?method=addElements&newEleId='
				+ $('canEle').value + '&wantEleId=' + cfg.eleIdFind
				+ '&parentType=' + cfg.bar.parentType;
		openMaxWindow(680, 1024, url);
		_self.close();
	}

	// 全选
	function selectAllPic() {
		var nodes = picView.getNodes();
		for (var i = 0; i < nodes.length; i++) {
			if (picView.isSelected(nodes[i])) {
				picView.deselect(i);
			} else {
				picView.select(i, true);
			}
		}
	}

	// 表单
	var con = {
		title : 'Choose Art No.',
		layout : 'border',
		width : 930,
		height : 520,
		border : false,
		modal : true,
		maximizable : true,
		items : [opForm, bottomPanel],
		listeners : {
			'afterrender' : function(win) {
				tbPnl.items.get(0).on('activate', function(pnl) {
							grid.setHeight(win.getHeight() - 100);
							ds.reload();
							Ext.getCmp("chkPicMode").setValue(false);
						});
				tbPnl.items.get(1).on('activate', function(pnl) {
					panel_pic.setHeight(win.getHeight() - 100);
					Ext.getCmp("chkPicMode").setValue(true);
					showPicDs.on('beforeload', function(store, options) {
						showPicDs.baseParams = form.getForm().getValues();
						if (typeof(showPicDs.baseParams.childFind) == "undefined")
							showPicDs.baseParams.childFind = "false";
						if ($("eleIdFind").value == '') {
							showPicDs.baseParams.eleIdFind = cfg.eleIdFind;
						}
						if(cfg.dp){
							showPicDs.baseParams.query_eleTypeidLv1 =cfg.dp;
						}
					});

					showPicDs.load({
								params : {
									start : 0,
									limit : 15
								}
							});
				});
				if(cfg.dp){
					typeBox.bindPageValue("CotTypeLv1", "id", cfg.dp);
					typeBox.hide();
//					type2Box.setDataUrl("servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="+cfg.dp);

				}
				// 将查询面板的条件加到ds中
				grid.setHeight(win.getHeight() - 100);
				ds.on('beforeload', function(store, options) {
							ds.baseParams = form.getForm().getValues();
							if (typeof(showPicDs.baseParams.childFind) == "undefined")
								ds.baseParams.childFind = "false";
							if ($("eleIdFind").value == '') {
								ds.baseParams.eleIdFind = cfg.eleIdFind;
							}
							if(cfg.dp){
								ds.baseParams.query_eleTypeidLv1 =cfg.dp;
							}
						});

				// 加载表格
				ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
			}
		}
	};
	Ext.apply(con, cfg);
	EleQueryWin.superclass.constructor.call(this, con);
};
Ext.extend(EleQueryWin, Ext.Window, {});
