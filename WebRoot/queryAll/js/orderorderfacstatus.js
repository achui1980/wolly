Ext.onReady(function() {

	DWREngine.setAsync(false);
	var fac = null;
	var payData;
	var clauseData;
	var currencyMap = null;
	var saleMap;
	var typeLv1Map;
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				fac = res;
			});
	// 加载付款方式表缓存
	baseDataUtil.getBaseDicDataMap("CotPayType", "id", "payName",
			function(res) {
				payData = res;
			});
	// 加载条款类型表缓存
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(
					res) {
				clauseData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				currencyMap = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(
					res) {
				typeLv1Map = res;
			});
	DWREngine.setAsync(true);

	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : "Currency",
				width : 80,
				isSearchField : true,
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				isSearchField : true,
				searchName : 'currencyId'
			});
	var completeBox = new Ext.form.ComboBox({
				tabIndex : 20,
				fieldLabel : '状态',
				editable : false,
				emptyText : 'Is Ready',
				store : completeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				value : "0",// 默认不打折
				hiddenName : 'canOut',
				selectOnFocus : true,
				width : 90,
				isSearchField : true,
				searchName : 'canOut'
			});

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
				name : "customerShortName"
			}, {
				name : "orderNo"
			}, {
				name : "staMark"
			}, {
				name : "staId"
			}, {
				name : "orderFacNo"
			}, {
				name : "orderTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "orderId"
			}, {
				name : "smapleStatus"
			}, {
				name : "sampleOutStatus"
			}, {
				name : "outStatus"
			}, {
				name : "qcStatus"
			}, {
				name : "packetStatus"
			}, {
				name : "orderStatus"
			}, {
				name : "allPinName"
			}, {
				name : "orderLcDate"
			}, {
				name : "orderLcDelay"
			}, {
				name : "payTypeId"
			}, {
				name : "clauseTypeId"
			}, {
				name : "empsName"
			}, {
				name : "facId"
			}, {
				name : "currencyId"
			}, {
				name : "orderfacId"
			}, {
				name : "orderTotal"
			}, {
				name : "orderFacTotal"
			}, {
				name : "canOut"
			}, {
				name : "poNo"
			}, {
				name : "currencyIdPO"
			}, {
				name : 'qcDeadline'
			}, {
				name : 'qcApproval'
			}, {
				name : 'sendTime'
			}, {
				name : 'typeLv1Id'
			}, {
				name : 'chk'
			},{
				name : 'scFileName'
			},{
				name : 'clientFile'
			}

	]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0

					}
				},
				baseParams : {
					limit : 300,
					canOut : 0
				},
				remoteSort : true,
				proxy : new Ext.data.HttpProxy({
							url : "cotorderstatus.do?method=queryOrderStatus"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "orderId"
						}, roleRecord)
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Sales",
					hidden:true,
					width : 50,
					dataIndex : "empsName"
				}, {
					header : "Department",
					width : 80,
					dataIndex : "typeLv1Id",
					renderer : function(value) {
						return typeLv1Map[value];
					}
				}, {
					header : "W&C P.I",
					dataIndex : "orderNo",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {

						var rpt = '<a href="javascript:showWin(\''
								+ record.get("orderNo") + '\','
								+ record.get("orderId") + ')">' + value
								+ '</a>';
						return rpt;
					},
					width : 100
				}, {
					header : "Client",
					dataIndex : "customerShortName",
					width : 100
				}, {
					header : "Client P/O",
					dataIndex : "poNo",
					width : 100,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value && value != '') {
							var rpt = '<a style="color:black;text-decoration:none;cursor: hand;" href="javascript:showWinComment('
									+ record.get("orderId")
									+ ')">'
									+ value
									+ '</a>';

							return rpt;
						}
					}
				}, {
					header : "Supplier",
					dataIndex : "facId",
					renderer : function(value) {
						return fac[value];
					}
				}, {
					header : "PI Amount",
					dataIndex : "orderTotal",
					hidden : getPopedomByOpType("cotorderstatus.do", "PIAmount") == 0
							? true
							: false,
					renderer : Ext.util.Format.numberRenderer("0,0.00"),
					summaryType : 'sum',
					summaryRenderer : Ext.util.Format.numberRenderer("0,0.00"),
					width : 75
				},{
					header : "Currency",
					dataIndex : "currencyId",
					hidden : getPopedomByOpType("cotorderstatus.do", "CURRENCY") == 0
							? true
							: false,
					renderer : function(value){
						if(getPopedomByOpType("cotorderstatus.do", "CURRENCY") == 0)
							return "***";
						else
							return currencyMap[value];
					},
					width : 75
				}, {
					header : "Product",
					dataIndex : "allPinName",
					width : 100
				}, {
					header : "Shipment",
					dataIndex : "orderLcDate",
					width : 75,
					renderer : function(value) {
						if (value && value.time)
							return Ext.util.Format.date(new Date(value.time),
									'd-m-Y');
					}
				}, {
					header : "Delivery Date",
					dataIndex : "sendTime",
					width : 75,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var chk = record.data.chk;
						if (chk == 1) {
							return "<font color=red>"
									+ Ext.util.Format.date(
											new Date(value.time), 'd-m-Y')
									+ "</font>";
						} else {
							return Ext.util.Format.date(new Date(value.time),
									'd-m-Y');
						}
					}
				}, {
					header : "PO Amount",
					dataIndex : "orderFacTotal",
					hidden : getPopedomByOpType("cotorderstatus.do", "POAmount") == 0
							? true
							: false,
					renderer : function(value) {
						return Ext.util.Format.number(value, "0,0.00")
					},
					summaryType : 'sum',
					summaryRenderer : Ext.util.Format.numberRenderer("0,0.00"),
					width : 100
				}, {
					header : "ETA",
					dataIndex : "orderLcDelay",
					width : 75,
					renderer : function(value) {
						if (value && value.time)
							return Ext.util.Format.date(new Date(value.time),
									'd-m-Y');
					}
				}, {
					header : "Del.Term",
					dataIndex : "clauseTypeId",
					width : 65,
					renderer : function(value) {
						return clauseData["" + value];
					}
				}, {
					header : "Pay.Term",
					dataIndex : "payTypeId",
					width : 65,
					renderer : function(value) {
						if (value == "6") {
							return "L/C<img src='/CotSystem/common/ext/resources/images/extend/lock.png'>";
						} else {
							return payData["" + value];
						}
					}
				}, {
					header : "Product",
					dataIndex : "smapleStatus",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var orderfacId = record.get('orderfacId');
						var orderFacNo = record.get('orderFacNo');

						if (value == 1)
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/tick.png' title='Color Approval Report' onmouseover=max(this) onmouseout=min(this) onclick=showPoRpt("
									+ orderfacId + ",'" + orderFacNo + "',true,'sample')>";
						else
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/bell.png' title='Color Approval Report' onmouseover=max(this) onmouseout=min(this) onclick=showPoRpt("
									+ orderfacId + ",'" + orderFacNo + "',false,'sample')>";
					},
					width : 65
				}, {
					header : "Artwork",
					dataIndex : "packetStatus",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var orderfacId = record.get('orderfacId');
						var orderFacNo = record.get('orderFacNo');

						if (value == 1)
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/tick.png' title='Artwork Briefing' onmouseover=max(this) onmouseout=min(this) onclick=showPoArtRpt("
									+ orderfacId + ",'" + orderFacNo + "')>";
						else
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/lorry_delete.png' title='Artwork Briefing' onmouseover=max(this) onmouseout=min(this) onclick=showPoArtRpt("
									+ orderfacId + ",'" + orderFacNo + "')>";
					},
					width : 65
				}, {
					header : "Samples",
					dataIndex : "sampleOutStatus",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var orderId = record.get('orderfacId');
						var orderNo = record.get('orderFacNo');
						var poId = record.get('orderId');
						if (value == 1)
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/tick.png' title='Shipment Sample Approval Report' onmouseover=max(this) onmouseout=min(this) onclick=showPiRpt("
									+ orderId + ",'" + orderNo + "',true,'sampleOut')>";
						else
							return "<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/time.png' title='Shipment Sample Approval Report' onmouseover=max(this) onmouseout=min(this) onclick=showPiRpt("
									+ orderId + ",'" + orderNo + "',false,'sampleOut',"+poId+")>";
					},
					width : 65
				}, {
					header : "Quality",
					dataIndex : "qcStatus",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var qcDeadline = record.get('qcDeadline');
						var qcApproval = record.get('qcApproval');
						if (!Ext.isEmpty(qcApproval)) {
							return "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						} else {
							if (Ext.isEmpty(qcDeadline)) {
								return "<img src='/CotSystem/common/ext/resources/images/extend/zoom_out.png'>";
							} else {
								return "<img src='/CotSystem/common/ext/resources/images/extend/zoom.png'>"
							}
						}
					},
					width : 65
				}, {
					header : "Shipment",
					dataIndex : "outStatus",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value == 1)
							return "<img  src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						else
							return "<img src='/CotSystem/common/ext/resources/images/extend/world.png'>";
					},
					width : 65
				},{
					header : "Data Sheet",
					dataIndex : "staId",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value == 1)
							return "<img style='cursor:hand;' " +
									"src='/CotSystem/common/ext/resources/images/extend/flag_red.png' " +
									"title='Markup' onmouseover=max(this) onmouseout=min(this) " +
									"onclick=showMark("+ record.get('id') + ")>";
						else
							return "<img style='cursor:hand;' " +
									"src='/CotSystem/common/ext/resources/images/extend/flag_clear.png' " +
									"title='Markup' onmouseover=max(this) onmouseout=min(this) " +
									"onclick=showMark("+ record.get('id') + ")>";
					},
					width : 65
				}]
	});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 300,
				store : ds,
				displayInfo : true,
				displaySize : '200|300|500',
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	var completeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['', 'All'], ['0', 'Running'], ['2', 'Closed']]
			});
	var completeBox = new Ext.form.ComboBox({
				// name : 'status',
				tabIndex : 20,
				fieldLabel : '状态',
				editable : false,
				emptyText : 'Is Ready',
				store : completeStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				anchor : "100%",
				value : "0",// 默认不打折
				hiddenName : 'canOut',
				selectOnFocus : true,
				width : 90,
				isSearchField : true,
				searchName : 'canOut'
			});
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'empId',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 10,
				selectOnFocus : true,
				sendMethod : "post",
				width : 90,
				emptyText : 'Sales',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'empId'
			});
	// 国别
	var nationBox = new BindCombox({
				cmpId : 'nationId',
				dataUrl : "servlet/DataSelvert?tbname=CotNation&key=nationName",
				emptyText : "Country",
				editable : true,
				isSearchField : true,
				valueField : "id",
				displayField : "nationName",
				pageSize : 10,
				selectOnFocus : true,
				sendMethod : "post",
				width : 90,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'nationId'
			});
	// 客户
	var customerBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		emptyText : "Client",
		editable : true,
		isSearchField : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 10,
		selectOnFocus : true,
		sendMethod : "post",
		width : 90,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'custId'
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
				width : 90,
				tabIndex : 5,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'companyId'
			});
// 条款类型
	var clauseBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotClause",
				cmpId : 'clauseTypeId',
				emptyText : "Del.Term",
				editable : false,
				valueField : "id",
				displayField : "clauseName",
				pageSize : 10,
				width : 90,
				tabIndex : 5,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'clauseTypeId'
			});
	// 采购厂家
	var facComb = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&validUrl=cotfactory.do",
		cmpId : 'factoryId',
		emptyText : "Supplier",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		pageSize : 10,
		width : 90,
		sendMethod : "post",
		selectOnFocus : false,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		isSearchField : true,
		searchName : 'factoryId'
	});
//	// 大类
//	var typeLv1IdBox = new BindCombox({
//				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
//				cmpId : 'typeLv1Id',
//				emptyText : "Department",
//				editable : true,
//				valueField : "id",
//				displayField : "typeEnName",
//				pageSize : 10,
//				width : 90,
//				sendMethod : "post",
//				selectOnFocus : false,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all',
//				isSearchField : true,
//				searchName : 'typeLv1IdFind'
//			});
	var typeLv1IdBox = {
		xtype : "textfield",
		emptyText : "Department",
		width : 95,
		isSearchField : true,
		searchName : 'typeLv1IdFind'
	}
	
	var tb = new Ext.ux.SearchComboxToolbar({
				items : [companyBox, {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "d-m-Y",
							id : 'startTime',
							vtype:'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "end Date",
							width : 90,
							format : "d-m-Y",
							vtype:'daterange',
							startDateField : 'startTime',
							id : 'endTime',
							isSearchField : true,
							searchName : 'endTime'
						}, busiBox, customerBox, facComb, typeLv1IdBox,curBox,nationBox,
						completeBox,clauseBox,{
							xtype:"textfield",
							id:"themeStr",
							width:90,
							emptyText : "Theme Name",
							isSearchField : true,
							searchName : 'themeStr'
						},{
							xtype:"textfield",
							id:"allPinName",
							width:90,
							emptyText : "Product",
							isSearchField : true,
							searchName : 'allPinName'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "P.I No.",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}
				],
				listeners:{
					render:function(){
						var tbar2 = new Ext.Toolbar({
										items:[{
											text:'Client P/O',
											handler:updateClientPo
										},'-',{
											text:'Report View',
											handler:show5TabsUploadFiles
										},'-',{
											text:'payment received',
											handler:doConfirmCash
										},'->', {
											text : "Pre-production",
											iconCls : "pre_product",
											cls : 'SYSOP_PRE',
											handler : queryDetailStatus.createDelegate(this, [
															"SAMPLE", "Pre-production"])
										}, {
											text : "Artwork Approval",
											iconCls : "pakcet_confirm",
											cls : 'SYSOP_ARTWORK',
											handler : queryDetailStatus.createDelegate(this, [
															"PACKAGE", "Artwork Approval"])
										}, {
											text : "Shipment Samples",
											iconCls : "ship_confirm",
											cls : 'SYSOP_SAMPLES',
											handler : queryDetailStatus.createDelegate(this, [
															"SAMPLEOUT", "Shipment Samples"])
										}, {
											text : "QC",
											iconCls : "qc",
											cls : 'SYSOP_QC',
											handler : queryDetailStatus.createDelegate(this, [
															"QC", "Quality Control"])
										}, {
											text : "Shipment",
											iconCls : "shipment",
											cls : 'SYSOP_SHIPMENT',
											handler : queryDetailStatus.createDelegate(this, [
															"OUT", "Shipment"])
										}]
						})
						tbar2.render(grid.tbar);
					}
				}
			});
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				id : "orderfacGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				region : "center",
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				plugins : [summary],
				border : false,
				viewConfig : {
					forceFit : false,
					getRowClass : function(record, index) {
						if (record.get("canOut") == 3)// 可以出货,但是还没填写出货单号
							return "x-grid-record-noShen";
						if (Ext.isEmpty(record.get("scFileName"))|| Ext.isEmpty(record.get("clientFile")))// 可以出货,但是还没填写出货单号
							return "x-grid-record-red";
					}
				}
			});

	var viewport = new Ext.Viewport({
				layout : "border",
				items : [grid]
			});
	viewport.doLayout();

	grid.on('rowdblclick', function(grid, rowIndex, event) {
			});

	function query() {
		ds.reload()
	}

	function queryDetailStatus(type, title) {
		parent.doAction("cotorderstatus.do?method=queryOrderStatusDetail&type="
						+ type, title, type);
	}

	// 保存到数据库
	function saveScFile(gt, picFile, orderId) {
		if (gt == 1) {
			cotOrderService.saveScFile(orderId, picFile, function(res) {
						// 显示上传文件的链接
						$('scSrc').innerText = picFile;
					});
		} else {
			cotOrderService.saveClientFile(orderId, picFile, function(res) {
						// 显示上传文件的链接
						$('clientSrc').innerText = picFile;
					});
		}
	}

	// 查看sc和client order
	window.openScOrClient = function(gt, txt) {
		if (gt == 1) {
			var isPopedom = getPopedomByOpType('cotorderstatus.do', "SC_DOWN");
			if (isPopedom == 0) {
				Ext.MessageBox.alert("Message",
						"You do not have permission to view SC!");
				return;
			}
		}
		if (gt == 2) {
			var isPopedom = getPopedomByOpType('cotorderstatus.do',
					"Client_DOWN");
			if (isPopedom == 0) {
				Ext.MessageBox.alert("Message",
						"You do not have permission to upload view Order!");
				return;
			}
		}
		openPdf("reportfile/orderSc/" + txt);
	}

	// 上传sc文件
	var winSc = null;
	window.uploadSc = function(gt, orderId, vt) {
		if (gt == 1) {
			var isPopedom = getPopedomByOpType('cotorderstatus.do', "SC_UPLOAD");
			if (isPopedom == 0) {
				Ext.MessageBox.alert("Message",
						"You do not have permission to upload SC!");
				return;
			}
		}
		if (gt == 2) {
			var isPopedom = getPopedomByOpType('cotorderstatus.do',
					"Client_UPLOAD");
			if (isPopedom == 0) {
				Ext.MessageBox.alert("Message",
						"You do not have permission to upload Client Order!");
				return;
			}
		}

		if (winSc == null) {
			// 上传后的文件名
			var picFile = "";
			var formSc = new Ext.form.FormPanel({
						labelWidth : 50,
						labelAlign : "right",
						layout : "form",
						padding : "5px",
						frame : true,
						fileUpload : true,
						buttonAlign : "center",
						monitorValid : true,
						fbar : [{
							text : "Save",
							formBind : true,
							iconCls : "page_mod",
							handler : function() {
								if (formSc.getForm().isValid()) {
									formSc.getForm().submit({
										url : './uploadOrderSc.action?flag='
												+ gt + '&vt=' + vt,
										waitTitle : 'waiting',
										waitMsg : 'uploading...',
										success : function(fp, o) {
											picFile = Ext.util.Format
													.htmlDecode(o.result.fileName);
											// 上传成功并保存
											saveScFile(gt, picFile, orderId);
											winSc.close();
										},
										failure : function(fp, o) {
											Ext.MessageBox.alert("Message",
													o.result.msg);
										}
									});
								}
							}
						}, {
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								winSc.close();
							}
						}],
						items : [{
									xtype : 'fileuploadfield',
									emptyText : 'upload file(.pdf)',
									anchor : '100%',
									allowBlank : false,
									blankText : "Please choose a Pdf",
									fieldLabel : 'PDF',
									id : 'filePath',
									name : 'filePath'
								}]
					});
			var winSc = new Ext.Window({
						title : 'Upload Pdf',
						layout : 'fit',
						modal : true,
						width : 300,
						height : 120,
						items : [formSc]
					});
		}
		winSc.show();
	}

	window.showWin = function(vt, orderId) {
		// 查询order的scFileName和clientFile
		cotOrderService.getFileNames(orderId, function(res) {
			var wcPI;
			var wcPO;
			var oId = vt;
			var order = 'order';
			var orderFac = 'orderfac';
			var isPopedom = checkPopedoms('cotorderstatus.do', "W&CPI");
			if (!isPopedom) {
				wcPI = '<div style="padding-left:10px;padding-top:10px;"><font class="wcPnl">PI:</font>You do not have Authority!</div>';
			} else {
				var url = downRptFile("PI-" + oId, "order");
				wcPI = '<div style="padding-left:10px;padding-top:10px;"><font class="wcPnl">PI:</font>'
						+ url + '</div>'
			}
			var isPopedomPO = checkPopedoms('cotorderstatus.do', "W&CPO");
			if (!isPopedomPO) {
				wcPO = '<div style="padding-left:10px;padding-top:10px;"><font class="wcPnl">PO:</font>You do not have Authority!</div>';
			} else {
				var url = downRptFile("PO-" + oId, "orderfac");
				wcPO = '<div style="padding-left:10px;padding-top:10px;"><font class="wcPnl">PO:</font>'
						+ url + '</div>'
			}
			var myPanel = new Ext.form.FormPanel({
				frame : true,
				height : 150,
				width : 300,
				items : [{
							xtype : 'panel',
							html : wcPI
						}, {
							xtype : 'panel',
							html : wcPO
						}, {
							xtype : 'panel',
							html : '<div style="padding-left:10px;padding-top:10px;">'
									+ '<span style="float:left;"><font class="wcPnl">SC:</font>&nbsp;&nbsp;&nbsp;'
									+ '<a id="scSrc" onclick=openScOrClient(1,this.innerText) style="cursor: hand;text-decoration: underline;">'
									+ res[0]
									+ '</a>'
									+ '</span>'
									+ '<span style="float:right;"><a href="javascript:uploadSc(1,'
									+ orderId
									+ ',\''
									+ oId
									+ '\')">Upload</a></span></div>'
						}, {
							xtype : 'panel',
							html : '<div style="padding-left:10px;padding-top:10px;">'
									+ '<span style="float:left;"><font class="wcPnl">Client order:</font>&nbsp;'
									+ '<a id="clientSrc" onclick=openScOrClient(2,this.innerText) style="cursor: hand;text-decoration: underline;">'
									+ res[1]
									+ '</a>'
									+ '</span>'
									+ '<span style="float:right;"><a href="javascript:uploadSc(2,'
									+ orderId
									+ ',\''
									+ oId
									+ '\')">Upload</a></span></div>'
						}]

			});
			var wind = new Ext.Window({
						title : 'W/C',
						layout : 'fit',
						modal : true,
						items : [myPanel]
					});
			wind.show();
		});
	}

	window.showWinComment = function(orderId) {
		// 查询order的scFileName和clientFile
		cotOrderService.getPIComment(orderId, function(res) {
			var myPanel = new Ext.form.FormPanel({
						frame : true,
						height : 150,
						width : 300,
						items : [{
							xtype : 'panel',
							html : '<div style="padding-left:10px;padding-top:10px;">'
									+ '<span style="float:left;">'
									+ res
									+ '</span></div>'
						}]

					});
			var wind = new Ext.Window({
						title : 'PI Comment',
						layout : 'fit',
						modal : true,
						items : [myPanel]
					});
			wind.show();
		});
	}
	
	function rightClickFn(client, rowIndex, e) {
		var row = grid.getStore().getAt(rowIndex);
		console.log(row);
		if(!row) return;
		// 右键菜单
		var rightMenu = new Ext.menu.Menu({
			//id : "rightMenu",
			items : [{
						text : "Show Upload File.",
						handler : show5TabsUploadFiles.createDelegate(this,[row.get('orderId')])
					},{
						text : "Update CLIENT P/O.",
						handler : updateClientPo.createDelegate(this,[row.get('orderId')])
					}]
		});
		e.preventDefault();
		rightMenu.orderId = row.get('orderId')
		rightMenu.showAt(e.getXY());
	}
	function doConfirmCash(){
		if(getPopedomByOpType('cotorderstatus.do','CASH') != 1){
			Ext.MessageBox.alert("Message","You do not have right.");
			return;
		}
		var row = grid.getSelectionModel().getSelected();
		if(!row){
			Ext.MessageBox.alert("Message","Please select a row");
			return;
		}
		var orderId = row.get("orderId");
		Ext.MessageBox.confirm('Message', 'Are you sure the payment has received for '+row.get('orderNo')+'?', function(btn){
			if(btn == 'yes'){
				//更新order状态为2
				cotOrderService.updateOrderStatusAndAddDayRpt(orderId,function(){
					Ext.Msg.alert("Info",'Update Success!');
					grid.getStore().reload();
				})
			}
		});
	}
	function show5TabsUploadFiles(orderId){
		var row = grid.getSelectionModel().getSelected();
		if(!row){
			Ext.MessageBox.alert("Message","Please select a row");
			return;
		}
		orderId = row.get("orderId");
		var tbl = new Ext.TabPanel({
		region : 'center',
		width : "100%",
		activeTab : 0,
		items : [{
					id : "productTab",
					name : "productTab",
					title : "Pre-production",
					layout : 'fit',
					items : [new OrderStatusDetailUploadGrid({
						title:'',
						showOptionBtn:false,
						orderId:orderId,
						orderStatus:'SAMPLE'
					})]
				},{
					id : "artworkTab",
					name : "artworkTab",
					title : "Artwork Approval",
					layout : 'fit',
					items : [new OrderStatusDetailUploadGrid({
						title:'',
						showOptionBtn:false,
						orderId:orderId,
						orderStatus:'PACKAGE'
					})]
				},{
					id : "samplesTab",
					name : "sampleTab",
					title : "Shipment Samples",
					layout : 'fit',
					items : [new OrderStatusDetailUploadGrid({
						title:'',
						orderId:orderId,
						showOptionBtn:false,
						orderStatus:'SAMPLEOUT'
					})]
				},{
					id : "qcTab",
					name : "qcTab",
					title : "QC",
					layout : 'fit',
					items : [new OrderStatusDetailUploadGrid({
						title:'',
						showOptionBtn:false,
						orderId:orderId,
						orderStatus:'QC'
					})]
				},{
					id : "shipmentTab",
					name : "shipmentTab",
					title : "Shipment",
					layout : 'fit',
					items : [new OrderStatusDetailUploadGrid({
						title:'',
						showOptionBtn:false,
						orderId:orderId,
						orderStatus:'OUT'
					})]
				}
		]
	});
	var win = new Ext.Window({
		title:'W&C PI-'+row.get('orderNo'),
		width:600,
		height:400,
		layout:'border',
		items:[tbl],
		closeAction:'close'
	});
	win.show();
	}
	//grid.on("rowcontextmenu", rightClickFn);
	
	function updateClientPo(orderId){
		var row = grid.getSelectionModel().getSelected();
		if(!row){
			Ext.MessageBox.alert("Message","Please select a row");
			return;
		}
		var win = new Ext.Window({
			title:'Update Client P/O-'+row.get('orderNo'),
			closeAction:'close',
			width:300,
			height:120,
			modal : true,
			frame:true,
			layout:'fit',
			buttons:[{
				text:'Save',
				handler:function(){
					var form = Ext.getCmp("clientPoForm");
					if(form.getForm().isValid()){
						var clientPo = Ext.getCmp("clientPo").getValue();
						cotOrderService.updateClientPo(row.get('orderId'),clientPo,function(res){
							if(res == -1){
								Ext.MessageBox.alert("Message","Update Client P/O Fail!");
							}else{
								Ext.MessageBox.alert("Message","Update Client P/O Success!");
								win.close();
								grid.getStore().reload();
							}
						})
					}
				}
			}],
			items:[{
				xtype:'form',
				id:"clientPoForm",
				items:[{
					xtype : 'textfield',
					id:'clientPo',
					emptyText : 'Client P/O',
					anchor : "100%",
					allowBlank : true,
					blankText : "Client P/O",
					fieldLabel : 'Client P/O'
				}]
			}]
		});
		win.show();
	}

});
// 生成出货单
function createInvoice(orderId) {
	var isPopedom = getPopedomByOpType('cotorderout.do', "ADD");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message",
				"You do not have permission to add Invoice!");
	} else {
		cotOrderService.saveInvoice(orderId, false, function(res) {
					reloadGrid("orderfacGrid");
					if (res != -1) {
						// 打开出货编辑页面
						openFullWindow('cotorderout.do?method=addOrder&id='
								+ res);
					} else {
						Ext.MessageBox.alert("Message",
								'create Invoice Failed!');
						return;
					}
				})
	}
}

// 打开PO报表
function showPoRpt(orderId, orderNo,isDone,type) {
	DWREngine.setAsync(false);
	if (orderId == null) {
		Ext.MessageBox.alert("Message", 'Please create PO First!');
		return;
	}
		// 判断po是否审核通过
	var poApproval = false;
	cotOrderService.checkIsGuoShen(orderId, function(res) {
		poApproval = res;
	});
	if(!poApproval){
		Ext.MessageBox.alert("Message",
					'Please PO have not be Approved!');
		return;
	}
	if(!isDone)
		showPdfComment(type,orderId);
	DWREngine.setAsync(true);
}

function showPdfComment(type,orderFacId){
	var comment = new PdfComment({
		type:type,
		orderFacId:orderFacId
	});
	comment.show();
}


// 打开备注
function showMark(orderId) {
//	if(logId!=22 && logId!=47 && logId!=51 && logId!=56){
//		Ext.MessageBox.alert("Message",'Do not have permission!');
//		return;
//	}
	// 判断po是否审核通过
	cotOrderService.getOrderById(orderId, function(res) {
		if (res) {
			var win=new Ext.Window({
				width:500,
				height:200,
				title:"Data Sheet",
				modal:true,
				padding:5,
				buttonAlign:'center',
				buttons:[{
					text:"Save",
					handler:function(){
						var mark=win.mark.getValue().getGroupValue();
						var content=win.content.getValue();
						cotOrderService.saveOrderStaMark(orderId,mark,content, 0,function(gut) {
							reloadGrid("orderfacGrid");
							win.close();
						})
					}
				},{
					text:"Close",
					handler:function(){
						win.close();
					}
				}],
				items:[{
					xtype:'form',
					labelWidth:60,
					labelAlign:"right",
					items:[{
						xtype:'radiogroup',
						anchor:'50%',
						ref:'../mark',
						fieldLabel : "Data Sheet",
						items : [{
									boxLabel : "<img src='/CotSystem/common/ext/resources/images/extend/flag_red.png'/>",
									inputValue : 1,
									name : 'showHeader',
									checked : res.staId==1?true:false
								}, {
									boxLabel : "<img src='/CotSystem/common/ext/resources/images/extend/flag_clear.png'/>",
									inputValue : 0,
									name : 'showHeader',
									checked : res.staId==1?false:true
								}]
					},{
						xtype:"textarea",
						anchor:'95%',
						height:90,
						ref:'../content',
						value:res.staMark,
						fieldLabel : "Comment"
					}]
				}]
			});
			win.show();
		}
	});
}

// 打开art报表
function showPoArtRpt(orderId, orderNo) {
	if (orderId == null) {
		Ext.MessageBox.alert("Message", 'Please create PO First!');
		return;
	}
	// 判断po是否已生成报表
	cotOrderService.checkIsHasArtWork(orderNo, function(res) {
				if (res) {
					var rdm = Math.round(Math.random() * 10000);
					openEleWindow("./saverptfile/home/Product/Art work Briefing_"
							+ orderNo + ".pdf?tmp=" + rdm);
				} else {
					Ext.MessageBox.alert("Message",
							'The Report have not be Created!');
					return;
				}
			});
}

// 打开PI报表
function showPiRpt(orderId, orderNo,isDone,type,piId) {
	DWREngine.setAsync(false);
	if (orderId == null) {
		Ext.MessageBox.alert("Message", 'Please create PO First!');
		return;
	}
	// 判断po是否审核通过
	var poApproval = false;
	cotOrderService.checkIsGuoShen(orderId, function(res) {
		poApproval = res;
	});
	if(!poApproval){
		Ext.MessageBox.alert("Message", 'Please PO have not be Approved!');
		return;
	}
	if(!isDone){
		Ext.Msg.confirm("Message","Do you want to make shipment samples report?",function(btn){
			if(btn ==='yes'){
				openFullWindow('cotorder.do?method=addOrder&id=' + piId+"&poId="+orderId+"&commentType="+type);	
			}
		})
		//showPdfComment(type,orderId);
	}
	DWREngine.setAsync(true);

}
