var facNum = getDeNum("facPrecision");
var outNum = getDeNum("outPrecision");
// 获取生产价要保留几位小数
var facStrNum = "0.";
var facStrNumTemp = "";
for (var i = 0; i < facNum; i++) {
	facStrNumTemp += "0";
}
facStrNum = facStrNum + facStrNumTemp;
// 获取外销价要保留几位小数
var outStrNum = "0.";
var outStrNumTemp = "";
for (var i = 0; i < outNum; i++) {
	outStrNumTemp += "0";
}
outStrNum = outStrNum + outStrNumTemp;

Ext.onReady(function() {
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
				convert : numFormat.createDelegate(this, [facStrNum],3)
			}, {
				name : "priceFacUint"
			}, {
				name : "priceOut",
				convert : numFormat.createDelegate(this, [outStrNum],3)
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
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "boxObW",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "boxObH",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "boxCbm",
				convert : numFormat.createDelegate(this, ["0.000"],3)
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
				autoLoad : true,
				baseParams : {
					start : 0,
					limit : 15
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotelements.do?method=loadChildInfo&mainId="
									+ $('mainId').value
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
					hidden : true
				}, {
					header : "Art No.",
					dataIndex : "eleId"
				}, {
					header : "Picture",
					dataIndex : "id",
					renderer : function(value) {
						var rdm = Math.round(Math.random() * 10000);
						return '<img src="showPicture.action?flag=ele&elementId='
								+ value
								+ '&tmp='
								+ rdm
								+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)" onclick="function(){alert()}"/>'
					}
				}, {
					header : "Unit",
					dataIndex : "eleUnit",
					width : 50
				}, {
					header : "Supplier",
					dataIndex : "facShortName"
				}, {
					header : "Design",
					dataIndex : "eleCol",
					width : 51
				}, {
					header : "Purchase Price",
					dataIndex : "priceFac",
					width : 60,
					renderer : function(value) {
						return "<span style='color:red;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Currency",
					dataIndex : "priceFacUintName",
					width : 50,
					renderer : function(value) {
						return "<span style='color:red;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Sale Price",
					dataIndex : "priceOut",
					width : 60,
					renderer : function(value) {
						return "<span style='color:blue;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "Currency",
					dataIndex : "priceFacUintName",
					width : 50,
					renderer : function(value) {
						return "<span style='color:blue;font-weight:bold;'>"
								+ value + "</span>";
					}
				}, {
					header : "中文品名",
					dataIndex : "eleName",
					width : 130
				}, {
					header : "Description",
					dataIndex : "eleNameEn",
					width : 130
				}, {
					header : "Size Description",
					dataIndex : "eleSizeDesc"
				}, {
					header : "Packing",
					dataIndex : "boxIbCount",
					width : 40
				}, {
					header : "Packing",
					dataIndex : "boxMbCount",
					width : 40
				}, {
					header : "Packing",
					dataIndex : "boxObCount",
					width : 40
				}, {
					header : "Outer Carton Length",
					dataIndex : "boxObL",
					width : 60
				}, {
					header : "Outer Carton width",
					dataIndex : "boxObW",
					width : 60
				}, {
					header : "Outer Carton Height",
					dataIndex : "boxObH",
					width : 60
				}, {
					header : "CBM",
					dataIndex : "boxCbm",
					width : 60
				}, {
					header : "MOQ",
					dataIndex : "eleMod"
				}, {
					header : "Remark",
					dataIndex : "eleRemark"
				}, {
					header : "Opration",
					dataIndex : "id",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var mod = '<a href="javascript:windowopenMod(' + value
								+ ')">Update</a>';
						var nbsp = "&nbsp &nbsp &nbsp"
						var del = '<a href="javascript:del(' + value
								+ ')">Delete</a>';
						return mod + nbsp + del;
					}
				}]
	});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Create",
							handler : windowopen,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Update",
							handler : windowopenMod.createDelegate(this, []),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}]
			});
	// 样品表格分页工具栏
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	// 样品表格
	var grid = new Ext.grid.GridPanel({
				id : "eleChildGrid",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				border:false,
				viewConfig : {
					forceFit : false
				}
			});

	// 获得选择的记录
	var getIds = function() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	var viewport = new Ext.Viewport({
				id : "viewport",
				layout : 'fit',
				items : [grid]
			});

	// 单击修改信息 start
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	function windowopen() {
		openMaxWindow(680, 1024,
				'cotelements.do?method=queryChild&flag=new&parentId='
						+ parent.$('eId').value + '&PEleId='
						+ parent.$('eleId').value);
	}

	// 批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select a record');
			return;
		}
		Ext.MessageBox.confirm('Message', 'are you sure to delete it?', function(btn) {
					if (btn == 'yes') {
						cotElementsService.deleteElements(list, function(res) {
									ds.reload();
									Ext.MessageBox.alert("Message", 'Deleted successfully！');
								});
					}
				});
	}

});
// 修改
function windowopenMod(obj) {
	if (obj == null) {
		var list = Ext.getCmp("eleChildGrid").getSelectionModel().getSelections();
		var ids = new Array();
		Ext.each(list, function(item) {
					ids.push(item.id);
				});
		if (ids.length == 0) {
			Ext.Msg.alert("Message", "Sorry, you do not have Authority! ");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("Message", "Sorry,you can select only one record!")
			return;
		} else
			obj = ids[0];

	}
	openCustWindow('cotelements.do?method=queryChild&flag=mod&id=' + obj
			+ '&parentId=' + parent.$('eId').value + '&PEleId='
			+ parent.$('eleId').value);
}
// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", 'Sorry, you do not have Authority!');
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', 'are you sure to delete all samples?', function(btn) {
				if (btn == 'yes') {
					cotElementsService.deleteElements(list, function(res) {
								clearForm("queryForm");
								Ext.getCmp("eleChildGrid").getStore().reload();
								Ext.MessageBox.alert("Message", 'Deleted successfully！');
							});
				}
			});
}