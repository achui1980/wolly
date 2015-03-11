Ext.onReady(function() {
	var curData;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotTypeLv3", "id", "typeName", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);
	
	// 小类
	var shenBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'categoryIdFind',
				editable : true,
				width:120,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Category',
				pageSize : 8,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'categoryIdFind'
			});
			
	// 明细
	var detailBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotOrderDetail&key=barcode&typeName=orderId&type="+$('orderId').value,
				cmpId : 'orderDetailIdFind',
				editable : true,
				width:120,
				valueField : "id",
				displayField : "barcode",
				emptyText : 'Barcode',
				pageSize : 8,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'orderDetailIdFind'
			});
	
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "categoryId"
			},{
				name : "orderDetailId"
			}, {
				name : "pcRemark"
			},{
				name : "orderId"
			},{
				name : "eleId"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorderfac.do?method=queryOrderPc&orderId="
									+ $('orderId').value
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
					header : "Barcode",
					dataIndex : "eleId",
					width : 120
				}, {
					header : "Category",
					dataIndex : "categoryId",
					width : 100,
					renderer : function(value) {
						return curData[value];
					}
				},{
					header : "Photo",
					dataIndex : "id",
					width : 200,
					renderer : function(value) {
						var rdm = Math.round(Math.random() * 10000);
						return '<img src="./showPicture.action?flag=orderpc&detailId='
								+ value
								+ '&tmp='
								+ rdm
								+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)"/>'
					}
				},{
					header : "Remark",
					dataIndex : "pcRemark",
					width : 400
				}]
	});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-',detailBox,shenBox,{
									xtype : 'searchcombo',
									width : 1,
									cls : 'hideCombo',
									editable : false,
									isJsonType : false,
									store : ds
								},'->', '-',{
							text : "Print",
							handler : showPrint,
							iconCls : "page_print"
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	var grid = new Ext.grid.GridPanel({
				border : true,
				region : "center",
				id : "orderPcGrid",
				stripeRows : true,
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

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});
	// 双击行显示编辑页面
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				windowopenMod(record.get("id"));
			});

	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});

	// 添加
	function windowopenAdd() {
		openWindowBase(430, 400, 'cotorder.do?method=orderPcEdit&orderId='
						+ $('orderId').value);
	}

	// 获得勾选的记录编号
	function getIds() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					res.push(item.id);
				});
		return res;
	}

	// 修改
	function windowopenMod(obj) {
		if (obj == null) {
			var ids = getIds();
			if (ids.length == 0) {
				Ext.MessageBox.alert('Message', "Please choose one record!");
				return;
			} else if (ids.length > 1) {
				Ext.MessageBox.alert('Message', "Only choose one record!")
				return;
			} else
				obj = ids[0];

		}
		openWindowBase(430, 400, 'cotorderfac.do?method=orderPcEdit&eId=' + obj
						+ '&orderId=' + $('orderId').value);
	}

	//批量删除
	function deleteBatch() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert('Message', "Please choose one record!");
			return;
		}
		var flag = Ext.MessageBox.confirm('Message', "Are you sure delete those phones?",
				function(btn) {
					if (btn == 'yes') {
						cotOrderService.deleteOrderPcs(list, function(res) {
									Ext.MessageBox.alert('Message', "Suceess Delete!");
									reloadGrid("orderPcGrid");
								});
					}
				});
	}
	
	// 显示打印面板
	var printWin;
	function showPrint(item) {
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'orderPic',
						pId : 'orderId',
						pNo : 'orderId',
						mailSendId : 'orderId',
						status : 'orderId'
					});
		}
		if (!printWin.isVisible()) {
			var po = item.getPosition();
			printWin.setPosition(po[0] - 200, po[1] + 25);
			printWin.show();
		} else {
			printWin.hide();
		}
	};
	
});
