Ext.onReady(function() {
	var tipinfo_pic = new Ext.XTemplate(
			"<p>Art No.：{eleId}</p>",
			"<p>Description：{eleNameEn}({eleName})</p>",
			"<p>Sale Price:<font color='red'>{priceOut}</font>({priceOutUintName}),CBM:{boxCbm}</p>",
			"<p>Packing:{boxIbCount}/{boxMbCount}/{boxObCount},MOQ:{eleMod}</p>",
			"<p>Outer Carton Size:{boxObL}/{boxObW}/{boxObH}</p>");
	tipinfo_pic.compile();
	var picRecord = new Ext.data.Record.create([{
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
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "priceFacUint"
			}, {
				name : "priceOut",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "priceOutUint"
			}, {
				name : "eleName"
			}, {
				name : "eleNameEn"
			}, {
				name : "eleSizeDesc"
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
				convert : numFormat.createDelegate(this, ["0.000"], 3)
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
						}, picRecord)
			});
	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Save",
							handler : windowopen,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Select / deselect",
							handler : selectAllPic,
							iconCls : "gird_save"
						}, '-', {
							text : "Update",
							handler : showBatchPanel,
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "List",
							handler : changeMode,
							iconCls : "gird_list",
							id : "mode"
						}
				// '-',
				// {text:"打印",handler:ExtToDoFn,iconCls:"page_print",cls:"SYSOP_PRINT"}
				// '-',
				// {text:"盘点机",handler:exportCMData,iconCls:"page_excel",cls:"SYSOP_PAN"}
				]
			});
	var toolbar_pic = new Ext.PagingToolbar({
				pageSize : 10,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			})

	var formatData = function(data) {
		// data.eleName = data.eleName.ellipse(15);
		// data.eleId = data.eleId.ellipse(15);
		// data.eleNameEn = data.eleNameEn.ellipse(15);
		return data;
	};
	var thumbTemplate = new Ext.XTemplate(
			'<tpl for=".">',
			'<div class="thumb-wrap" id="{eleId}">',
			'<div class="thumb"><div><img src="../../showPicture.action?flag=ele&elementId={id}" onload="DrawImage(this,140,140)"></div></div>',
			'<span>{eleId}</span>' + '</div>', '</tpl>');
	thumbTemplate.compile();
	var picView = new Ext.DataView({
		tpl : thumbTemplate,
		multiSelect : true,
		autoScroll : true,
		bodyStyle : "overflow-x:hidden;",
		id : "eleGrid",
		loadMask : {
			msg : 'Data Loading....'
		},
		overClass : 'x-view-over',
		itemSelector : 'div.thumb-wrap',
		emptyText : '<div style="padding:10px;">No images match the specified filter</div>',
		plugins : new Ext.DataView.DragSelector(),
		store : ds,
		listeners : {
			'containerclick' : {
				fn : function() {
					//alert("ddfd")
				},
				scope : this
			},
			'click' : {
				fn : batchGrid,
				scop : this
			},
			'dblclick' : {
				fn : windowopenMod,
				scope : this
			},
			'beforeselect' : {
				fn : function(view, node) {
					if (view.isSelected(node))
						view.deselect(node);
				}
			}
		},
		prepareData : formatData.createDelegate(this)
	});
	// 鼠标移到节点，可以显示相应的信息
	picView.on('render', function(view) {
				picView.tip = new Ext.ToolTip({
							target : picView.getEl(), // 需要显示信息的组件
							delegate : '.x-view-over', // 一个domquery的selector，用来控制触发需要在哪里显示，通过CSS识别
							trackMouse : true, // Moving within the row should
												// not hide the tip.
							mouseOffset : [-100, 10],
							renderTo : document.body, // Render immediately so
														// that tip.body can be
														// referenced prior to
														// the first show.
							listeners : { // Change content dynamically
											// depending on which element
											// triggered the show.
								beforeshow : function updateTipBody(tip) {
									var record = view
											.getRecord(tip.triggerElement);
									// alert(record.eleId);
									tip.body.dom.innerHTML = tipinfo_pic
											.apply(record.data)
								}
							}

						})
			});
	var panel_pic = new Ext.Panel({
				id : "img-chooser-view",
				tbar : tb,
				bbar : toolbar_pic,
				//frame : false,
				border:false,
				cls:'leftBorder',
				region : "center",
				layout : 'fit',
				items : [picView]
			});
	// 将查询面板的条件加到ds中

	var viewport = new Ext.Viewport({
				layout : "border",
				items : [accordion, panel_pic]
			});
	ds.on('beforeload', function(store, options) {

				ds.baseParams = queryForm.getForm().getValues();
			});
	ds.load({
				params : {
					start : 0,
					limit : 10
				}
			});
	// 查询按钮事件
	Ext.getCmp("queryBtn").on('click', function() {
				ds.on('beforeload', function(store, options) {
							ds.baseParams = queryForm.getForm().getValues();
						});
				ds.reload({params : {
					start : 0,
					limit : 10
				}});
			});
	viewport.doLayout();

	function changeMode() {
		var panel = parent.Ext.getCmp("maincontent");
		var tabId = panel.getActiveTab().id;
		var iframe = parent.Ext.getCmp(tabId);
		iframe.setSrc("sample/elements/elementsframe.jsp");
	}
	function windowopen() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有添加权限
		{
			Ext.Msg.alert("Message", "Sorry, you do not have Authority");
			return;
		}
		openCustWindow('../../cotelements.do?method=addElements');
	}
	//全选
	function selectAllPic() {
		var nodes=picView.getNodes();
		for (var i = 0; i <nodes.length; i++) {
			if(picView.isSelected(nodes[i])){
				picView.deselect(i);
			}else{
				picView.select(i, true);
			}
		}
	}
	// 单击节点显示大图片
	var detailPic = null;
	var ddproxy = null;
	function showPic(view, index, node) {
		var data = view.getStore().getAt(index).data;
		var id = data.id
		var url = "../../showPicture.action?flag=ele&elementId=" + id;
		if ($('printDiv') == null) {
			Ext.DomHelper.append(document.body, {
				html : '<img id="printDiv" onload="DrawImage(this,400,400)" style="position:absolute;left:0;top:0;"></img>'
			}, true);
		}
		if (ddproxy == null) {
			ddproxy = new Ext.dd.DDProxy("printDiv");
		}
		$('printDiv').src = url;
		var div = Ext.fly("printDiv");
		div.on("click", function() {
					div.hide(true);
				});
		div.center();
		div.show(true);
	}
	var getIds = function() {

		var list = picView.getSelectedRecords();
		var res = new Array();
		Ext.each(list, function(record) {
					res.push(record.data.id);
				});
		return res;
	}
	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select a record！');
			return;
		}
		Ext.MessageBox.confirm('Message', 'are you sure to delete the selected items?', function(btn) {
					if (btn == 'yes') {
						cotElementsService.deleteElements(list, function(res) {
									clearForm("queryForm");
									ds.reload();
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								});
					}
				});
	}
});
// 点击厂家树和材质树调用
function queryByFacAndType(factoryId, typeId) {
	// 将查询面板的条件加到ds中
	var ds = Ext.getCmp("eleGrid").getStore()
	ds.on('beforeload', function(store, options) {
				ds.baseParams = new Object();
				ds.baseParams.factoryIdFind = factoryId;
				ds.baseParams.eleTypeidLv1Find = typeId;
			});
	// 加载表格
	ds.reload({params : {
		start : 0,
		limit : 10
	}});
}
// 修改
function windowopenMod(view, index, node) {
	// 添加权限判断

	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.Msg.alert("Message", "Sorry, you do not have Authority");
		return;
	}
	var record = view.getRecord(node);
	openCustWindow('../../cotelements.do?method=queryElements&id='
			+ record.get("id"));
}
function showBatchPanel() {
	if ($('batchPanel') == null) {
		Ext.DomHelper.append(document.body, {
					html : "<div id='batchPanel' style='width:500px'></div>"
				});
		var div = Ext.fly('batchPanel');
		div.setXY([parent.Ext.getBody().getSize().width / 2 - 100,
				parent.Ext.getBody().getSize().height / 2 - 200]);
		batchPanel.render("batchPanel");
		div.show();
		var dd = new Ext.dd.DDProxy("batchPanel");

		// var record = eleGrid.getStore().getAt(rowIndex);
		// var eleId = record.get("eleId");
		// var grid = Ext.getCmp("eleIdForReport");
		// var store = grid.getStore();
		// var sm = eleGrid.getSelectionModel();
		// if(sm.isSelected(rowIndex))
		// {
		// var u = new store.recordType({
		// id:record.get("id"),
		// eleId : record.get("eleId")
		// })
		// store.add(u);
		// }
	} else {
		var div = Ext.fly('batchPanel');
		div.show();
	}
}
function batchGrid(view, index, node, selections) {
	if (batchPanel.isVisible()) {
		var record = view.getRecord(node);
		if (view.isSelected(node)) {

			var eleId = record.get("eleId");
			var grid = Ext.getCmp("eleIdForReport");
			var store = grid.getStore();
			if (store.find("eleId", eleId) != -1)
				return;
			var u = new store.recordType({
						id : record.get("id"),
						eleId : record.get("eleId")
					})
			store.add(u);
			// Ext.getCmp("printType").setValue("XLS")
		} else {
			var eleId = record.get("eleId");
			var grid = Ext.getCmp("eleIdForReport");
			var store = grid.getStore();
			var index = store.findExact("eleId", eleId);
			store.remove(store.getAt(index));
		}
	}
}