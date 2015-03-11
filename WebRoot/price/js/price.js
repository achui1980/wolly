Ext.onReady(function() {
	DWREngine.setAsync(false);
	var clauseData;
	var curData;
	// 加载价格条款表缓存
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(
					res) {
				clauseData = res;
			});
	DWREngine.setAsync(true);
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
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
				name : "priceTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "customerShortName"
			}, {
				name : "priceNo"
			}, {
				name : "empsName"
			}, {
				name : "clauseId"
			}, {
				name : "currencyId"
			}, {
				name : "priceStatus"
			}, {
				name : "validMonths"
			}, {
				name : "custId"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 200
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotprice.do?method=query"
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
							header : "Operation",
							dataIndex : "id",
							width : 120,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var mod = '<a href="javascript:windowopenMod('
										+ value + ')">Update</a>';
								var status = record.data.priceStatus;
								var nbsp = "&nbsp &nbsp &nbsp";
								var del = '<a href="javascript:del(' + value
										+ ',' + status + ')">Delete</a>';
//								var rpt = '<a href="javascript:downRptFile(\''+ record.get("priceNo")
//										+ '\',\'price\')">Report</a>';
								return mod + nbsp + del;
							}
						}, {
							header : "No.",
							dataIndex : "priceNo",
							width : 150
						}, {
							header : "Offer Date",
							dataIndex : "priceTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "d-m-Y");
								}
							}
						}, {
							header : "Client",
							dataIndex : "customerShortName",
							width : 130
						},{
							header : "Sales",
							hidden:true,
							dataIndex : "empsName",
							width : 90
						}, {
							header : "Delivery Terms",
							dataIndex : "clauseId",
							width : 150,
							renderer : function(value) {
								return clauseData["" + value];
							}

						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curData["" + value];
							}

						}, {
							header : "Valid Date",
							dataIndex : "validMonths",
							width : 80
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|300|500',
				//emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		mode : 'remote',// 默认local
		autoLoad : false,// 默认自动加载
		pageSize : 5,
		selectOnFocus : true,
		sendMethod : "post",
		emptyText : "Client",
		width : 120,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
	});
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPerson',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				width : 120,
				emptyText : 'Sales',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'businessPerson'
			});
	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				emptyText : "Company",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				pageSize : 10,
				width : 120,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'companyId'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-',companyBox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 100,
							format : "d-m-Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 100,
							format : "d-m-Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},customerBox, busiBox, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "No.",
							isSearchField : true,
							searchName : 'priceNoFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Create",
							handler : windowopenAdd,
							iconCls : "page_add",
							cls : "SYSOP_ADD"
						}, '-', {
							text : "Update",
							handler : windowopenMod
									.createDelegate(this, [null]),
							iconCls : "page_mod",
							cls : "SYSOP_MOD"
						}, '-', {
							text : "Delete",
							handler : deleteBatch,
							iconCls : "page_del",
							cls : "SYSOP_DEL"
						}, '-', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print",
							cls : "SYSOP_PRINT"
						}]
			});
	var grid = new Ext.grid.GridPanel({
				id : "priceGrid",
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
					forceFit : true
				},
				listeners : {
					rowdblclick : {
						// 表格双击
						fn : function(grid, rowIndex, event) {
							var record = grid.getStore().getAt(rowIndex);
							windowopenMod(record.get("id"));
						}
					}
				}
			});

	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [grid]
			});
	viewport.doLayout();

	// 批量删除
	function deleteBatch() {
		var list = Ext.getCmp("priceGrid").getSelectionModel().getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("INFO", "Please select a record!");
			return;
		}
		var ary = new Array();
		Ext.each(list, function(item) {
					var status = item.data.priceStatus;
					if (status == 2 && loginEmpId != "admin") {
					} else {
						ary.push(item.id);
					}
				});
		if (ary.length == 0) {
			Ext.MessageBox.alert('INFO', "Quotation of your choice have been approved, can not be deleted!");
			return;
		}
		Ext.MessageBox.confirm('Message', "Are you sure to delete the selected quotations?",
				function(btn) {
					if (btn == 'yes') {
						cotPriceService.deletePrices(ary, function(res) {
									Ext.MessageBox.alert("Message", "Deleted successfully!");
									reloadGrid("priceGrid");
								});
					}
				});
	}

	// 新增
	function windowopenAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0)// 没有添加权限
		{
			Ext.MessageBox.alert("Message", "You do not have permission to add");
			return;
		}
		openFullWindow('cotprice.do?method=addPrice');
	}
	
	//报价发邮件
	function sendMail(){
		var list = sm.getSelections();
		if (list.length == 0) {
			Ext.MessageBox.alert("INFO", "Please select a record!");
			return false;
		} else if (list.length > 1) {
			Ext.MessageBox.alert("INFO", "You must only one record!")
			return false;
		}
		var priceId = list[0].data.id;
		var priceNo = list[0].data.priceNo;
		//打印模版
		var reportTemple = $('reportTemple').value;
		if (reportTemple == '') {
			Ext.MessageBox.alert("INFO", 'Please select a Template!');
			return;
		}
		
		DWREngine.setAsync(false);
		mask();
		DWREngine.setAsync(true);
		cotPriceService.sendMail(priceId,priceNo,reportTemple,function(res){
			openEleWindow("./saverptfile/price/" + priceNo + ".pdf");
			unmask();
		});
	}

	// 显示打印面板
	var printWin;
	function showPrint(item) {
		// 添加权限判断
		var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
		if (isPopedom == 0) {
			Ext.MessageBox.alert("Message", 'You do not have permission to print！');
			return;
		}
		if (printWin == null) {
			printWin = new PrintWin({
						type : 'price',
						sendMail:sendMail
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
// 表格中删除
function del(id, status) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message", "You do not have permission to delete");
		return;
	}
	// 判断该单是否已被审核
	if (status == 2 && loginEmpId != "admin") {
		Ext.MessageBox.alert("Message", "Sorry, this one has already been audited, can not be removed!");
		return;
	}
	var list = new Array();
	list.push(id);
	Ext.MessageBox.confirm('Message', "Are you sure to delete this quote?", function(btn) {
				if (btn == 'yes') {
					cotPriceService.deletePrices(list, function(res) {
								if (res) {
									Ext.MessageBox.alert("Message", "Deleted successfully!");
									reloadGrid("priceGrid");
								} else {
									Ext.MessageBox.alert("Message", "The quotation has been in use!");
								}
							});
				}
			});
}
function getIds() {
	var list = Ext.getCmp("priceGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				res.push(item.id);
			});
	return res;
}

// 打开订单编辑页面
function windowopenMod(obj) {
	if (obj == null) {
		var ids = getIds();
		if (ids.length == 0) {
			Ext.MessageBox.alert("Message", "Please select a record!");
			return;
		} else if (ids.length > 1) {
			Ext.MessageBox.alert("Message", "Choose only one record!")
			return;
		} else
			obj = ids[0];

	}
	openFullWindow('cotprice.do?method=addPrice&id=' + obj);
}
