// 要保留几位小数
var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
var cbmNum = getDeNum("cbmPrecision");
// 根据小数位生成"0.000"字符串
var facStrNum = getDeStr(facNum);
var outStrNum = getDeStr(outNum);
var cbmStr = getDeStr(cbmNum);

var tipinfo = new Ext.XTemplate(
		"<p><img src='../../showPicture.action?flag=ele&elementId={id}' width='400' height='400' onload='javascript:DrawImage(this,400,400)'/></p>",
		"<p>英文名：{eleNameEn}({eleName}),尺寸描述：{eleSizeDesc}</p>",
		"<p>外销价:<font color='red'>{priceOut}</font>({priceOutUintName}),CBM:{boxCbm}</p>",
		"<p>装箱率:{boxIbCount}/{boxMbCount}/{boxObCount},外箱长宽高:{boxObL}/{boxObW}/{boxObH},最小订量:{eleMod}</p>");
Ext.onReady(function() {
	// Ext.apply(Ext.QuickTips.getQuickTip(), {
	// maxWidth : 400,
	// minWidth : 400
	// });
	var eleMap;
	DWREngine.setAsync(false);
	// 加载模块列
	cotCustomizeService.getCotCustomizeFieldMap('CotElements', function(rs) {
				eleMap = rs;
			});
	DWREngine.setAsync(true);
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleUnit"
			}, {
				name : "factoryId"
			}, {
				name : "eleCol"
			}, {
				name : "priceFac",
				convert : numFormat.createDelegate(this, [facStrNum], 3)
			}, {
				name : "priceFacUint"
			}, {
				name : "priceOut",
				convert : numFormat.createDelegate(this, [outStrNum], 3)
			}, {
				name : "priceOutUint"
			}, {
				name : "boxWeigth"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}, {
				name : "boxIbCount"
			}, {
				name : "boxMbCount"
			}, {
				name : "boxObCount"
			}, {
				name : "boxObL",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObW",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxObH",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "boxCbm",
				convert : numFormat.createDelegate(this, [cbmStr], 3)
			}, {
				name : "eleMod"
			}, {
				name : "eleRemark"
			}, {
				name : "priceFacUintName"
			}, {
				name : "priceOutUintName"
			}, {
				name : "facShortName"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "../../cotelements.do?method=queryList"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	var kk = 0;
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cm = new HideColumnModel({
		defaults : {
			sortable : true
		},
		hiddenCols : eleMap,
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					hidden : true
				},{
					header : "Operation",
					dataIndex : "id",
					width:105,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var mod = '<a href="javascript:windowopenMod(' + value
								+ ')">Update</a>';
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">Delete</a>';
						return mod + nbsp + del;
					}
				}, {
					header : "Art No.",
					dataIndex : "eleId",
					width:180
				}, {
					header : "Picture",
					dataIndex : "id",
					renderer : function(value) {
						var rdm = Math.round(Math.random() * 10000);
						return '<img src="../../showPicture.action?flag=ele&elementId='
								+ value
								+ '&tmp='
								+ rdm
								+ '" width="70" height="70px" onload="javascript:DrawImage(this,70,70)" onclick="showBigPicDiv(this)"/>'
					}
				}, {
					header : "Supplier",
					dataIndex : "facShortName",
					hidden : hideFac,
					renderer : function(value) {
						if (hideFac == true) {
							return "*";
						} else {
							return value;
						}
					}
				}, {
					header : "Supplie's No.",
					dataIndex : "factoryNo",
					hidden : true,
					renderer : function(value) {
						if (hideFac == true) {
							return "*";
						} else {
							return value;
						}
					}
				}, {
					header : "Barcode",
					dataIndex : "barcode",
					width : 100
				}, {
					header : "Purchase Price",
					dataIndex : "priceFac",
					width : 100,
					renderer : function(value) {
						return "<span style='color:red;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Currency",
					dataIndex : "priceFacUintName",
					width : 60,
					renderer : function(value) {
						return "<span style='color:red;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Sales Price",
					dataIndex : "priceOut",
					width : 80,
					renderer : function(value) {
						return "<span style='color:blue;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Currency",
					dataIndex : "priceOutUintName",
					width : 60,
					renderer : function(value) {
						return "<span style='color:blue;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Description",
					dataIndex : "eleNameEn",
					width : 100
				}, {
					header : "Category",
					dataIndex : "boxIbCount",
					hidden : true,
					width : 60
				}, {
					header : "Packing Way",
					dataIndex : "boxTypeId",
					hidden : true,
					width : 80
				}, {
					header : "Inner box",
					dataIndex : "boxIbCount",
					hidden : true,
					width : 80
				}, {
					header : "Outer carton",
					dataIndex : "boxObCount",
					width : 80
				}]
	});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Create",
							handler : showModel,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						},  {
							text : "Update",
							hidden:true,
							handler : windowopenMod.createDelegate(this, []),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Export",
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : function() {
								showBatchPanel(0);
							}
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "Pictures",
							handler : changeMode,
							iconCls : "page_img",
							id : "mode"
						}, '-', {
							text : "Barcode Machine",
							handler : function() {
								showBatchPanel(2);
							},
							iconCls : "page_calculator",
							cls : "SYSOP_PAN"
						}]
			});
	// 样品表格分页工具栏
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|100|200|all'
				//emptyMsg : "无记录"
			});
	// 样品表格
	var grid = new Ext.grid.GridPanel({
				id : "eleGrid",
				region : "center",
				cls : 'rightBorder',
				stripeRows : true,
				border : false,
				cls : 'leftBorder',
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				// split : true,
				viewConfig : {
					forceFit : false
				}
			});

	var viewport = new Ext.Viewport({
				id : "viewport",
				layout : 'border',
				border : false,
				items : [accordion, grid]
			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});
	// 单击节点显示大图片
	var detailPic = null;
	// 可以拖动的节点
	var ddproxy = null;
	grid.on("cellclick", function(eleGrid, rowIndex, columnIndex) {
		var dataIndex = eleGrid.getColumnModel().getColumnHeader(columnIndex);
		var rec = eleGrid.getStore().getAt(rowIndex);
		var id = rec.data.id;
		var eleId = rec.data.eleId;
		if (columnIndex == 0) {
			showBatchPanel(-1);
			if (batchPanel != null) {
				var grid = Ext.getCmp("eleIdForReport");
				var store = grid.getStore();
				// 判断该行是否被选中
				if (sm.isSelected(rec)) {
					if (store.findExact("eleId", eleId) != -1)
						return;
					var u = new store.recordType({
								id : id,
								eleId : eleId
							})
					store.add(u);
				} else {
					var index = store.findExact("eleId", eleId);
					store.remove(store.getAt(index));
				}
			}
		}

		// 单击图片列生效
		if (dataIndex != "Picture")
			return;
//		if ($('picDiv') == null) {
//			Ext.DomHelper.append(document.body, {
//				html : '<img id="picDiv" onload="DrawImage(this,400,400)" style=" zoom: 1 ;position:absolute;left:0;top:0;" onmousewheel="return onWheelZoomPic(this)"></img>'
//			}, true);
//		}
//		if (ddproxy == null)
//			ddproxy = new Ext.dd.DDProxy("picDiv");
//		var url = "../../showPicture.action?flag=ele&elementId=" + id;
//		$('picDiv').src = url;
//		var div = Ext.fly("picDiv");
//		div.on("click", function() {
//					div.hide();
//				});
//		div.setXY([parent.Ext.getBody().getSize().width / 2 - 100,
//				parent.Ext.getBody().getSize().height / 2 - 200]);
//		div.show();

	});
	// sm.on("rowselect",function(sm, rowIndex, record){
	// var grid = Ext.getCmp("eleIdForReport");
	// var store = grid.getStore();
	// var u = new store.recordType({
	// id:rec.data.id,
	// eleId : rec.data.eleId
	// })
	// store.add(u);
	// });
	// sm.on("rowdeselect",function(sm, rowIndex, record){
	// var grid = Ext.getCmp("eleIdForReport");
	// var store = grid.getStore();
	// var u = new store.recordType({
	// id:rec.data.id,
	// eleId : rec.data.eleId
	// })
	// store.remove(u)
	// });
	// 全选或反选时显示修改层
	sm.on("selectionchange", function(sm, rowIndex, record) {
				// if (sm.getCount() == ds.getCount()) {
				// showBatchPanel(-1);
				// }
				// 全选时
				if (sm.getCount() == ds.getCount()) {
					var grid = Ext.getCmp("eleIdForReport");
					if (!grid) {
						return
					}
					var store = grid.getStore();
					// store.removeAll();
					ds.each(function(rec) {
								if (store.findExact("id", rec.data.id) > -1)
									return;
								var u = new store.recordType({
											id : rec.data.id,
											eleId : rec.data.eleId
										})
								store.add(u);
							});
				}
				// 反选时
				if (sm.getCount() == 0) {
					if (batchPanel != null) {
						var grid = Ext.getCmp("eleIdForReport");
						var store = grid.getStore();
						ds.each(function(rec) {
									var idx = store
											.findExact("id", rec.data.id);
									if (idx > -1)
										store.removeAt(idx);
								})

					}
				}
			});

	// 将查询面板的条件加到ds中
	ds.on('beforeload', function(store, options) {
				ds.baseParams.factoryIdFind2 = "";
				ds.baseParams.eleTypeidLv1Find2 = "";
				ds.baseParams = queryForm.getForm().getValues();
			});
	// 加载表格
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 查询按钮事件
	Ext.getCmp("queryBtn").on('click', function() {
				ds.on('beforeload', function(store, options) {
							ds.baseParams.factoryIdFind2 = "";
							ds.baseParams.eleTypeidLv1Find2 = "";
							ds.baseParams = queryForm.getForm().getValues();
						});
				ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
				// 快捷操作面板
//				var report_grid = Ext.getCmp("eleIdForReport");
//				if (report_grid) {
//					var report_store = report_grid.getStore();
//					report_store.removeAll();
//				}
			})

	// 将左边查询条件转换成字符串
	function changeToStr() {
		var str = "";
		var paramsQuery = queryForm.getForm().getValues();
		for (var p in paramsQuery) {
			if (paramsQuery[p] != '') {
				str += "&" + p + "=" + paramsQuery[p];
			}
		}
		if (str.indexOf("childFind") < 0)
			str += "&childFind=false"
		return str;
	}

	var batchPanel;
	// 根据要设为活动抽屉的num显示快捷操作面板
	var showBatchPanel = function(num) {
		if (batchPanel == null) {
			batchPanel = new BatchPanel();
			// var obj = Ext.fly(batchPanel.printName);
			// var ary = obj.getCenterXY();
			// obj.setX(ary[0] - 200);
			// obj.setY(ary[1] - 210);
			// batchPanel.render(obj);
			// var proxy1 = new Ext.dd.DDProxy(batchPanel.printName);
			batchPanel.show();
		} else {
			if (!batchPanel.isVisible()) {
				batchPanel.show();
			}
			// else {
			// batchPanel.hide();
			// }
		}
		if (num >= 0) {
			batchPanel.selectActiveItem(num);
		}
	};

	// 新建
	function windowopen() {
		// 验证表单
		var formData = getFormValues(modlPanel, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		var byId=$('byId').value;
		var myId=$('myId').value;
		var syId=$('syId').value;
		var facId=$('facId').value;
		var tempoBtn=$('tempoBtn').checked;
		if(tempoBtn){
			var ps = "Temp_"+Math.round(Math.random() * 10000000);
			openWindowBase(500, 1020, '../../cotelements.do?method=addElements&eleNo='+ps+'&byId='+byId+'&myId='+myId+'&syId='+syId+'&facId='+facId+'&chk=1');
			modlPanel.hide();
		}else{
			if(byId=='' || myId=='' || syId=='' || facId==''){
				Ext.MessageBox.alert('Message','Please choose Dep.of PRODUCT and Group and Category and Supplier!')
			}else{
				cotSeqService.getEleNo(byId, myId,syId,facId, function(ps) {
							openWindowBase(500, 1020, '../../cotelements.do?method=addElements&eleNo='+ps+'&byId='+byId+'&myId='+myId+'&syId='+syId+'&facId='+facId+'&chk=1');
							modlPanel.hide();
						});
			}
		}
		
		
	}

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select records！');
			return;
		}
		Ext.MessageBox.confirm('Message', 'Are you sure to delete the selected sample data ?', function(btn) {
					if (btn == 'yes') {
						cotElementsService.deleteElements(list, function(res) {
									clearForm("queryForm");
									ds.reload();
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								});
					}
				});
	}

	// 表格列表和图片切换
	function changeMode() {
		var panel = parent.Ext.getCmp("maincontent");
		var tabId = panel.getActiveTab().id;
		var iframe = parent.Ext.getCmp(tabId);
		iframe.setSrc("sample/elements/elepiclist.jsp");
		// iframe.submitAsTarget({
		// url:"sample/elements/elepiclist.jsp"
		// //params: {param1: "foo", param2: "bar"}
		// })
		// alert(Ext.encode({param1: "foo", param2: "bar"}));
	}

	// 大类
	var typeBox = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv4&key=typeEnName",
				cmpId : 'byId',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor:'100%',
				sendMethod : "post",
				selectOnFocus : true,
//				allowBlank : false,
//				blankText : "Choose Dep. Of Sales！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type2Box.setDataUrl("../../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="+rec.data.id);
//							type2Box.resetData();
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 中类
	var type2Box = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'myId',
				fieldLabel : "Group",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				anchor:'100%',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
//				allowBlank : false,
//				blankText : "Choose Group！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type3Box.setDataUrl("../../servlet/DataSelvert?tbname=CotTypeLv3&key=typeName&typeName=typeIdLv2&type="+rec.data.id);
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 小类
	var type3Box = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'syId',
				fieldLabel : "Category",
				editable : true,
				anchor:'100%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
//				allowBlank : false,
//				blankText : "Choose Category！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 供应厂家
	var facBox = new BindCombox({
				//dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
				dataUrl : "../../servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
				cmpId : 'facId',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor:'100%',
//				allowBlank : false,
//				blankText : "Choose a Supplier！",
				tabIndex : 2,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
			
	// 显示模板面板
	var modlPanel;
	function showModel(item, pressed) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0){
			Ext.Msg.alert("Message", "You do not have permission to add");
			return;
		}
		
		if ($('importDiv') == null) {
			// window默认在z-index为9000
			Ext.DomHelper.append(document.body, {
				html : '<div id="importDiv" style="position:absolute;z-index:9500;"></div>'
			}, true);
		}
		if (modlPanel == null) {
			modlPanel = new Ext.form.FormPanel({
						labelWidth : 90,
						padding : "5px",
						height : 210,
						width : 290,
						layout : 'form',
						frame : true,
						title : "Create New No.",
						labelAlign : "right",
						tools : [{
									id : "close",
									handler : function(event, toolEl, p) {
										p.hide();
									}
								}],
						buttonAlign:'right',
						buttons : [{
							text : "Create",
							iconCls : "page_table_save",
							handler : windowopen
						}],
						items : [{
							xtype : "checkbox",
							boxLabel : "Temporary Art No.",
							hideLabel:true,
							anchor:"100%",
							id : "tempoBtn"
						},{
							xtype:'panel',
							layout:'form',
							id:'tempoPnl',
							items:[typeBox, type2Box, type3Box, facBox]
						}]
					});
			var tag = item.getEl();
			var left = Ext.Element.fly(tag).getX();
			var top = Ext.Element.fly(tag).getY();
			Ext.Element.fly("importDiv").setLeftTop(left - 80, top + 30);
			modlPanel.render("importDiv");
			//$('modelEle').focus();
		} else {
			if (!modlPanel.isVisible()) {
				modlPanel.show();
			} else {
				modlPanel.hide();
			}
		}
	};

});
// 获得选择的记录
var getIds = function() {
	var list = Ext.getCmp("eleGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}
// 修改
function windowopenMod(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.Msg.alert("Message", "You do not have permission to modify");
		return;
	}
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Please select a record");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "Choose only one record!")
			return;
		} else
			obj = ids[0];

	}
	openCustWindow('../../cotelements.do?method=queryElements&id=' + obj);
}

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", 'You do not have permission to delete！');
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', 'Are you sure to delete the selected sample data ?', function(btn) {
				if (btn == 'yes') {
					cotElementsService.deleteElements(list, function(res) {
								clearForm("queryForm");
								Ext.getCmp("eleGrid").getStore().reload();
								Ext.MessageBox.alert("Message", 'Deleted successfully!');
							});
				}
			});
}

// 点击厂家树和材质树调用
function queryByFacAndType(factoryId, typeId) {
	// 将查询面板的条件加到ds中
	var ds = Ext.getCmp("eleGrid").getStore();
	ds.on('beforeload', function(store, options) {
				ds.baseParams.factoryIdFind2 = factoryId;
				ds.baseParams.eleTypeidLv1Find2 = typeId;
			});
	// 加载表格
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			})
}
function onWheelZoomPic(obj) {
	var zoom = parseFloat(obj.style.zoom);
	var tZoom = zoom + (event.wheelDelta > 0 ? 0.05 : -0.05);

	if (tZoom <= 1) {
		return true;
	}
	obj.style.zoom = tZoom;
	return false;
}
