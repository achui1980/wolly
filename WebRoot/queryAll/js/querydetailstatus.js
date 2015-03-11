var displayType ="";
var globalNationId = 0;
var displayMap = {
	"SAMPLE" : ["Colour swatch approval Deadline",
			"Colour swatch approval Approval",
			"pre-production approval Deadline",
			"pre-production approval Approval"],
	"PACKAGE" : ["Artwork sent to Supplier Deadline",
			"Artwork sent to Supplier Approval",
			"Artwork Approval Deadline", "Artwork Approval Approval"],
	"SAMPLEOUT" : ["Photo Samples Deadline", "Photo Samples Approval",
			"Approval Deadline", "Approval Approval"],
	"QC" : ["Quality Control Deadline", "Quality Control Approval"],
	"OUT" : ["Shipment Deadline", "Shipment Approval",
			"Shipping advice Deadline", "Shipping advice Approval"]
}
// 明细状态与大状态的对应关系
var groupMap = {
	"SAMPLE" : ["Colour Approval/Swatch<br/>DeadLine", "Pre-production<br/>DeadLine"],
	"PACKAGE" : ["Artwork Approval<br/>DeadLine", "Artwork Physical Approval<br/>DeadLine"],
	"SAMPLEOUT" : ["Photo Samples<br/>DeadLine", "Shipment Sample<br/>DeadLine"],
	"QC" : ["Quality Control", "Quality Control"],
	"OUT" : ["Shipment Check<br/>DeadLine", "Shipping Advice<br/>DeadLine"]
}
// 与数据库字段的映射关系
var valueMap = {
	"SAMPLE" : ["simpleSampleDeadline", "simpleSampleApproval",
			"completeSampleDeadline", "completeSampleApproval"],
	"PACKAGE" : ["facDeadline", "facApproval", "pcaketDeadline",
			"pcaketApproval"],
	"SAMPLEOUT" : ["samplePicDeadline", "samplePicApproval",
			"sampleOutDeadline", "sampleOutApproval"],
	"QC" : ["qcDeadline", "qcApproval", "qcDeadline", "qcApproval"],
	"OUT" : ["shippingDeadline", "shippingApproval", "loadingDeadline",
			"loadingApproval"]
}
Ext.onReady(function() {

	DWREngine.setAsync(false);
	var factoryData;
	var custDate;
	var typeLv1Map;
	var empMap;
	var clauseData;
	// 加载厂家表
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
					res) {
				factoryData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
			function(res) {
				custData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotTypeLv1", "id", "typeEnName", function(
					res) {
				typeLv1Map = res;
			});
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empMap = res;
			});
	baseDataUtil.getBaseDicDataMap("CotClause", "id", "clauseName", function(
					res) {
				clauseData = res;
			});
	DWREngine.setAsync(true);
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
	/** ************** */
	displayType = $('type').value;
	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "orderfacNo"
			}, {
				name : "orderId"
			}, {
				name : "orderfacId"
			}, {
				name : valueMap[displayType][0],
				type : 'jsondate'
			}, {
				name : valueMap[displayType][1],
				type : 'jsondate'
			}, {
				name : valueMap[displayType][2],
				type : 'jsondate'
			}, {
				name : valueMap[displayType][3],
				type : 'jsondate'
			}, {
				name : 'allPinName'
			}, {
				name : 'sendTime',
				type : 'jsondate'
			}, {
				name : 'orderLcDate',
				type : 'jsondate'
			}, {
				name : 'custId'
			}, {
				name : 'factoryId'
			}, {
				name : "povalue",
				convert : numFormat.createDelegate(this, ["0.0"], 3)
			}, {
				name : "qcLocation"
			}, {
				name : "orderLcDelay",
				type : 'jsondate'
			}, {
				name : 'typeLv1Id'
			}, {
				name : 'empId'
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
				name : "clientNo"
			}, {
				name : "flag"
			}, {
				name : "orderFacText",
				type : 'jsondate'
			}, {
				name : "preNum"
			}, {
				name : "preText"
			}, {
				name : "artNum"
			}, {
				name : "artText"
			}, {
				name : "samNum"
			}, {
				name : "samText"
			}, {
				name : "qcNum"
			}, {
				name : "qcText"
			}, {
				name : "shipNum"
			}, {
				name : "shipText"
			}, {
				name : "preMan"
			}, {
				name : "artMan"
			}, {
				name : "samMan"
			}, {
				name : "qcMan"
			}, {
				name : "shipMan"
			},{
				name : "estConsumedTime"
			},{
				name : "estTime"
			},{
				name : "travelTime"
			},{
				name : "travelConsumedTime"
			},{
				name : "etdDate",
				type : 'jsondate'
			},{
				name : "qcPerson"
			},{
				name : "totalExpenseUsd"
			},{
				name:"clauseTypeId"
			},{
				name:"designTime",
				type : 'jsondate'
			},{
				name:'chk'
			}, {
				name : "preStaId"
			}, {
				name : "artStaId"
			}, {
				name : "shiStaId"
			}, {
				name : "qcStaId"
			}, {
				name : "spStaId"
			},{
				name:'zheType'
			},{
				name:'nationId'
			},{
				name:'zheNum'
			},{
				name:'themeStr'
			}
	]);
	// 创建数据源
	var detailds = new Ext.data.Store({
		autoLoad : {
			params : {
				start : 0
			}
		},
		baseParams : {
			limit : 300
		},
		proxy : new Ext.data.HttpProxy({
					url : "cotorderstatus.do?method=queryOrderStatusDetail&type="
							+ displayType
				}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "orderId"
				}, detailRecord),
		remoteSort : true
	});
	// 创建复选框列
	var detailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var detailcm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [detailsm, {
					header : "ID",
					dataIndex : "id",

					width : 50,
					hidden : true
				}, {
					header : "orderId",
					dataIndex : "orderId",
					width : 50,
					hidden : true
				}, {
					header : "Operation",
					dataIndex : "id",
					width : 80,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var orderFacId = record.get("orderfacId");
						var mod = ""
						if (orderFacId != null && orderFacId != "")
							mod = '<a href="javascript:updateOrderFac(' + value
									+ "," + orderFacId + ')">Save</a>';
						mod += '	<a href="javascript:showUploadWin()">Upload</a>';
						return mod;
					}
				}, {
					header : "Sales",
					hidden:true,
					//hidden:displayType == "QC" ? true : false,
					width : 50,
					dataIndex : "empId",
					renderer : function(value) {
						return empMap[value];
					}
				}, {
					header : "Department",
					width : 80,
					dataIndex : "typeLv1Id",
					hidden:displayType == "QC" ? true : false,
					renderer : function(value) {
						return typeLv1Map[value];
					}
				}, {
					header : "Client",
					dataIndex : "custId",
					width : 90,
					hidden:displayType == "QC" ? true : false,
					renderer : function(value) {
						return custData[value];
					}
				}, {
					header : "Suppiler",
					dataIndex : "factoryId",
					width : 90,
					renderer : function(value) {
						return factoryData[value];
					}
				}, {
					header : "Product",
					dataIndex : "allPinName",
					width : 90
				}, {
					header : "ETD",
					dataIndex : "orderLcDate",
					//hidden:displayType == "OUT" ? true : false,
					width : 120,
					renderer : Ext.util.Format.dateRenderer('d-m-Y')
				},{
					header : "Departure",
					align : "center",
					dataIndex : 'etdDate',
					width : 120,
					hidden : displayType == "OUT" ? false : true,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "Delivery Date",
					hidden:(displayType == "QC" || displayType == "PACKAGE") ? true : false,
					dataIndex : "sendTime",
					width : 120,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var chk = record.data.chk;
						if(chk==1){
							return "<font color=red>"+Ext.util.Format.date(value, 'd-m-Y')+"</font>";
						}else{
							return Ext.util.Format.date(value, 'd-m-Y');
						}
					}
				},{
					header : "ETA",
					align : "center",
					dataIndex : 'orderLcDelay',
					width : 120,
					hidden : displayType == "OUT" ? false : true,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "P.I NO.",
					align : "center",
					hidden:(displayType == "QC" || displayType == "PACKAGE") ? true : false,
					dataIndex : "orderNo",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						return "<a href='#' onclick='openFullWindow(\"cotorder.do?method=addOrder&id="
								+ record.get("orderId")
								+ "\");'>"
								+ value
								+ "</a>"
					},
					width : 100
				}, {
					header : "Client P/O",
					align : "center",
					dataIndex : 'clientNo',
					width : 100,
					hidden : displayType == "OUT" ? false : true
				}, {
					header : "P.O NO.",
					align : "center",
					dataIndex : "orderfacNo",
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						return "<a href='#' onclick='openFullWindow(\"cotorderfac.do?method=add&id="
								+ record.get("orderfacId")
								+ "&oId="
								+ record.get("orderId")
								+ "\");'>"
								+ value
								+ "</a>"
					},
					width : 100
				},{
					header : "On Board",
					align : "center",
					hidden : displayType == "OUT" ? false : true,
					dataIndex : "zheNum",
					width : 100,
					renderer:function(value){
						if(value == 1){
							return 'Y';
						}else if(value == 0){
							return 'N'
						}else
							return "";
						return value == 1?'Y':'N';
					},
					editor : new Ext.form.ComboBox({
						store : new Ext.data.SimpleStore({
							fields : ["tp", "name"],
							data : [[0, 'N'], [1, 'Y']]
						}),
						valueField : "tp",
						selectOnFocus : false,
						displayField : "name",
						triggerAction : 'all',
						mode : 'local'
					})
				},{
					header : "Del.Term",
					dataIndex : "clauseTypeId",
					width : 90,
					hidden : displayType == "OUT" ? false : true,
					renderer : function(value) {
						return clauseData[value];
					}
				}, {
					header : "PO VALUE",
					align : "center",
					hidden : displayType == "QC" ? false : true,
					dataIndex : "povalue",
					width : 100
				},{
					header : "QC Person",
					align : "center",
					hidden : displayType == "QC" ? false : true,
					dataIndex : "qcPerson",
					width : 100,
					editor : new Ext.form.TextField({})
				},{
					header : "QC by photos",
					align : "center",
					hidden : displayType == "QC" ? false : true,
					dataIndex : "zheType",
					width : 100,
					renderer:function(value){
						return value == 1?'Y':'N';
					},
					editor : new Ext.form.ComboBox({
						store : new Ext.data.SimpleStore({
							fields : ["tp", "name"],
							data : [[0, 'N'], [1, 'Y']]
						}),
						valueField : "tp",
						displayField : "name",
						disabled:window.getPopedomByOpType('cotorder.do','PICTUREQC')==0?true:false,
						mode : 'local'
					})
				}, {
					header : displayType == "QC" ? "Expected QC" : groupMap[displayType][0],
					align : "center",
					dataIndex : valueMap[displayType][0],
					width : 120,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				},{
					header : "Est.QC time",
					align : "center",
					dataIndex : 'estConsumedTime',
					width : 120,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.NumberField({

					})
				},{
					header : "Est.Travel<br/>time",
					align : "center",
					dataIndex : 'estTime',
					width : 100,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.NumberField({

					})
				},{
					header : "Time Consumed<br/>Travel",
					align : "center",
					dataIndex : 'travelConsumedTime',
					width : 120,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.NumberField({

					})
				},{
					header : "Time Consumed<br/>check",
					align : "center",
					dataIndex : 'travelTime',
					width : 120,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.NumberField({

					})
				},{
					header : "Total time",
					align : "center",
					dataIndex : 'id',
					width : 80,
					hidden : displayType == "QC" ? false : true,
					renderer:function(value,mata,record){
						var travelTime = record.data.travelTime;
						var travelConsumedTime = record.data.travelConsumedTime;
						return travelTime+travelConsumedTime;
					}
				},{
					header : "Total expense<br/>USD",
					align : "center",
					dataIndex : 'totalExpenseUsd',
					width : 100,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.NumberField({

					})
				}, {
					header : "QC Location",
					align : "center",
					dataIndex : 'qcLocation',
					width : 120,
					hidden : displayType == "QC" ? false : true,
					editor : new Ext.form.TextField({

					})
				}, {
					header : "Approval",
					align : "center",
					dataIndex : valueMap[displayType][1],
					width : 120,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				},{
					header : groupMap[displayType][1],
					dataIndex : valueMap[displayType][2],
					width : 120,
					align : "center",
					hidden : displayType == "QC" ? true : false,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "Approval",
					align : "center",
					dataIndex : valueMap[displayType][3],
					width : 120,
					hidden : displayType == "QC" ? true : false,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "design sent",
					align : "center",
					dataIndex :"designTime",
					width : 120,
					hidden : displayType == "SAMPLE" ? false : true,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "Product",
					dataIndex : "smapleStatus",
					width : 85,
					hidden : displayType == "SAMPLE" ? false : true,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var str = "";
						if (value == 1)
							str = "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						else
							str = "<img src='/CotSystem/common/ext/resources/images/extend/bell.png'>";
						var orderId = record.data.orderId;
						var orderNo = record.data.orderNo;
						var factoryId = record.data.factoryId;
						var custId = record.data.custId;
						var allPinName = record.data.allPinName;
						var orderLcDate = Ext.util.Format.date(
								record.data.orderLcDate, 'd-m-Y');

						var flag = record.data.flag;
						var icon = "application_tile_horizontal.png";
						// 有提问,没有全部答复
						if (flag == 2) {
							icon = "user.png";
						}
						// 全部答复
						if (flag == 3) {
							icon = "group.png";
						}
						var question = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ icon
								+ "' title='Show COMMENT' onmouseover=max(this) onmouseout=min(this) onclick=\"showFaqWin(1,"
								+ orderId
								+ ",'"
								+ orderNo
								+ "',"
								+ factoryId
								+ ","
								+ custId
								+ ",'"
								+ allPinName
								+ "','"
								+ orderLcDate + "')\">";

						// 查看评分按钮
						var num = record.data.preNum;
						var text = record.data.preText;
						var man = record.data.preMan;
						var fenIcon = "application.png";
						// 有提问,没有全部答复
						if (num != null) {
							fenIcon = "chart_bar.png";
						}
						var orderfacId = record.data.orderfacId;
						var fen = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ fenIcon
								+ "' title='Show Rating Of Supplier' onmouseover=max(this) onmouseout=min(this) onclick=\"showFenWin('"
								+ displayType
								+ "',"
								+ orderfacId
								+ ",'"
								+ orderNo
								+ "','"
								+ text
								+ "',"
								+ num
								+ ","
								+ value + "," + factoryId + "," + man + ")\">";

						return str + question + fen;
					}
				}, {
					header : "Packing",
					dataIndex : "packetStatus",
					width : 85,
					hidden : displayType == "PACKAGE" ? false : true,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var str = "";
						if (value == 1)
							str = "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						else
							str = "<img src='/CotSystem/common/ext/resources/images/extend/lorry_delete.png'>";

						var orderId = record.data.orderId;
						var orderNo = record.data.orderNo;
						var factoryId = record.data.factoryId;
						var custId = record.data.custId;
						var allPinName = record.data.allPinName;
						var orderLcDate = Ext.util.Format.date(
								record.data.orderLcDate, 'd-m-Y');
						var flag = record.data.flag;
						var icon = "application_tile_horizontal.png";
						// 有提问,没有全部答复
						if (flag == 2) {
							icon = "user.png";
						}
						// 全部答复
						if (flag == 3) {
							icon = "group.png";
						}
						var question = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ icon
								+ "' title='Show COMMENT' onmouseover=max(this) onmouseout=min(this) onclick=\"showFaqWin(2,"
								+ orderId
								+ ",'"
								+ orderNo
								+ "',"
								+ factoryId
								+ ","
								+ custId
								+ ",'"
								+ allPinName
								+ "','"
								+ orderLcDate + "')\">";

						// 查看评分按钮
						var num = record.data.artNum;
						var text = record.data.artText;
						var man = record.data.artMan;
						var fenIcon = "application.png";
						// 有提问,没有全部答复
						if (num != null) {
							fenIcon = "chart_bar.png";
						}
						var orderfacId = record.data.orderfacId;
						var fen = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ fenIcon
								+ "' title='Show Rating Of Supplier' onmouseover=max(this) onmouseout=min(this) onclick=\"showFenWin('"
								+ displayType
								+ "',"
								+ orderfacId
								+ ",'"
								+ orderNo
								+ "','"
								+ text
								+ "',"
								+ num
								+ ","
								+ value + "," + factoryId + "," + man + ")\">";

						return str + question + fen;

					}
				}, {
					header : "Samples",
					dataIndex : "sampleOutStatus",
					width : 85,
					hidden : displayType == "SAMPLEOUT" ? false : true,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var str = "";
						if (value == 1)
							str = "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						else
							str = "<img src='/CotSystem/common/ext/resources/images/extend/time.png'>";
						var orderId = record.data.orderId;
						var orderNo = record.data.orderNo;
						var factoryId = record.data.factoryId;
						var custId = record.data.custId;
						var allPinName = record.data.allPinName;
						var orderLcDate = Ext.util.Format.date(
								record.data.orderLcDate, 'd-m-Y');
						var flag = record.data.flag;
						var icon = "application_tile_horizontal.png";
						// 有提问,没有全部答复
						if (flag == 2) {
							icon = "user.png";
						}
						// 全部答复
						if (flag == 3) {
							icon = "group.png";
						}
						var question = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ icon
								+ "' title='Show COMMENT' onmouseover=max(this) onmouseout=min(this) onclick=\"showFaqWin(3,"
								+ orderId
								+ ",'"
								+ orderNo
								+ "',"
								+ factoryId
								+ ","
								+ custId
								+ ",'"
								+ allPinName
								+ "','"
								+ orderLcDate + "')\">";

						// 查看评分按钮
						var num = record.data.samNum;
						var text = record.data.samText;
						var man = record.data.samMan;
						var fenIcon = "application.png";
						// 有提问,没有全部答复
						if (num != null) {
							fenIcon = "chart_bar.png";
						}
						var orderfacId = record.data.orderfacId;
						var fen = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ fenIcon
								+ "' title='Show Rating Of Supplier' onmouseover=max(this) onmouseout=min(this) onclick=\"showFenWin('"
								+ displayType
								+ "',"
								+ orderfacId
								+ ",'"
								+ orderNo
								+ "','"
								+ text
								+ "',"
								+ num
								+ ","
								+ value + "," + factoryId + "," + man + ")\">";

						return str + question + fen;
					}
				}, {
					header : "Sent Client Approval",
					width : 125,
					dataIndex : "orderFacText",
					hidden : displayType == "SAMPLEOUT" ? false : true,
					renderer : Ext.util.Format.dateRenderer('d-m-Y'),
					editor : new CustomizeField({
								allowBlank : true,
								format : 'd-m-Y',
								listeners : {
									'focus' : function(com) {
										if (Ext.isEmpty(com.getValue())) {
											com.setValue(new Date());
										}
									}
								}
							})
				}, {
					header : "Quality",
					dataIndex : "qcStatus",
					width : 80,
					hidden : displayType == "QC" ? false : true,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var qcDeadline = record.get('qcDeadline');
						var qcApproval = record.get('qcApproval');
						var str = '';
						if (!Ext.isEmpty(qcApproval)) {
							str = "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						} else {
							if (Ext.isEmpty(qcDeadline)) {
								str = "<img src='/CotSystem/common/ext/resources/images/extend/zoom_out.png'>";
							} else {
								str = "<img src='/CotSystem/common/ext/resources/images/extend/zoom.png'>"
							}
						}
						var orderId = record.data.orderId;
						var orderNo = record.data.orderNo;
						var factoryId = record.data.factoryId;
						var custId = record.data.custId;
						var allPinName = record.data.allPinName;
						var orderLcDate = Ext.util.Format.date(
								record.data.orderLcDate, 'd-m-Y');

						var flag = record.data.flag;
						var icon = "application_tile_horizontal.png";
						// 有提问,没有全部答复
						if (flag == 2) {
							icon = "user.png";
						}
						// 全部答复
						if (flag == 3) {
							icon = "group.png";
						}
						var question = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ icon
								+ "' title='Show COMMENT' onmouseover=max(this) onmouseout=min(this) onclick=\"showFaqWin(4,"
								+ orderId
								+ ",'"
								+ orderNo
								+ "',"
								+ factoryId
								+ ","
								+ custId
								+ ",'"
								+ allPinName
								+ "','"
								+ orderLcDate + "')\">";

						// 查看评分按钮
						var num = record.data.qcNum;
						var text = record.data.qcText;
						var man = record.data.qcMan;
						var fenIcon = "application.png";
						// 有提问,没有全部答复
						if (num != null) {
							fenIcon = "chart_bar.png";
						}
						var orderfacId = record.data.orderfacId;
						var fen = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ fenIcon
								+ "' title='Show Rating Of Supplier' onmouseover=max(this) onmouseout=min(this) onclick=\"showFenWin('"
								+ displayType
								+ "',"
								+ orderfacId
								+ ",'"
								+ orderNo
								+ "','"
								+ text
								+ "',"
								+ num
								+ ","
								+ value + "," + factoryId + "," + man + ")\">";

						return str + question + fen;
					}
				}, {
					header : "Shipment",
					dataIndex : "outStatus",
					width : 80,
					hidden : displayType == "OUT" ? false : true,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var str = '';
						if (value == 1)
							str = "<img src='/CotSystem/common/ext/resources/images/extend/tick.png'>";
						else
							str = "<img src='/CotSystem/common/ext/resources/images/extend/world.png'>";

						var orderId = record.data.orderId;
						var orderNo = record.data.orderNo;
						var factoryId = record.data.factoryId;
						var custId = record.data.custId;
						var allPinName = record.data.allPinName;
						var orderLcDate = Ext.util.Format.date(
								record.data.orderLcDate, 'd-m-Y');

						var flag = record.data.flag;
						var icon = "application_tile_horizontal.png";
						// 有提问,没有全部答复
						if (flag == 2) {
							icon = "user.png";
						}
						// 全部答复
						if (flag == 3) {
							icon = "group.png";
						}
						var question = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ icon
								+ "' title='Show COMMENT' onmouseover=max(this) onmouseout=min(this) onclick=\"showFaqWin(5,"
								+ orderId
								+ ",'"
								+ orderNo
								+ "',"
								+ factoryId
								+ ","
								+ custId
								+ ",'"
								+ allPinName
								+ "','"
								+ orderLcDate + "')\">";

						// 查看评分按钮
						var num = record.data.shipNum;
						var text = record.data.shipText;
						var man = record.data.shipMan;
						var fenIcon = "application.png";
						// 有提问,没有全部答复
						if (num != null) {
							fenIcon = "chart_bar.png";
						}
						var orderfacId = record.data.orderfacId;
						var fen = "&nbsp;&nbsp;<img style='cursor:hand;' src='/CotSystem/common/ext/resources/images/extend/"
								+ fenIcon
								+ "' title='Show Rating Of Supplier' onmouseover=max(this) onmouseout=min(this) onclick=\"showFenWin('"
								+ displayType
								+ "',"
								+ orderfacId
								+ ",'"
								+ orderNo
								+ "','"
								+ text
								+ "',"
								+ num
								+ ","
								+ value + "," + factoryId + "," + man + ")\">";

						return str + question + fen;
					}
				},{
					header : "Data Sheet",
					dataIndex : "id",
					hidden:( displayType == "PACKAGE")?true:false,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var t=1;
						var m=record.get('preStaId');
						if(displayType=="PACKAGE"){
							t=2;
							m=record.get('artStaId');
						}
						if(displayType=="SAMPLEOUT"){
							t=3;
							m=record.get('shiStaId');
						}
						if(displayType=="QC"){
							t=4;
							m=record.get('qcStaId');
						}
						if(displayType=="OUT"){
							t=5;
							m=record.get('spStaId');
						}
						if (m == 1)
							return "<img style='cursor:hand;' " +
									"src='/CotSystem/common/ext/resources/images/extend/flag_red.png' " +
									"title='Markup' onmouseover=max(this) onmouseout=min(this) " +
									"onclick=showMark("+ record.get('id') + ","+t+")>";
						else
							return "<img style='cursor:hand;' " +
									"src='/CotSystem/common/ext/resources/images/extend/flag_clear.png' " +
									"title='Markup' onmouseover=max(this) onmouseout=min(this) " +
									"onclick=showMark("+ record.get('id')+ ","+t+")>";
					},
					width : 65
				},{
					header : "Theme Name",
					dataIndex : "themeStr",
					hidden:( displayType == "PACKAGE")?false:true,
					width : 100
				}]
	});
	var detailtoolBar = new Ext.PagingToolbar({
				pageSize : 300,
				store : detailds,
				displayInfo : true,
				// displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '200|300|500',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	// faq状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All COMMENT'], [1, 'No Comment'],
						[2, 'Comment CN'], [3, 'Comment DK']]
			});
	var shenBox = new Ext.form.ComboBox({
				name : 'flag',
				emptyText : 'All COMMENT',
				editable : false,
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'flag',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'flag'
			});
var zheStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'N'], [1, 'Y']]
			});
	var zheBox = new Ext.form.ComboBox({
				name : 'zheType',
				tabIndex : 20,
				fieldLabel : 'Picture QC',
				editable : false,
				hidden:displayType == 'QC'?false:true,
				// emptyText : 'Choose',
				store : zheStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width:90,
				hiddenName : 'zheType',
				emptyText:'Picture QC',
				isSearchField : true,
				searchName : 'zheType',
				selectOnFocus : true
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
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				editable : true,
				valueField : "empsName",
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
				triggerAction : 'all'
			});
	// 大类
//	var typeLv1IdBox = new BindCombox({
//				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
//				cmpId : 'typeLv1Id',
//				emptyText : "Department",
//				editable : true,
//				valueField : "id",
//				displayField : "typeEnName",
//				pageSize : 10,
//				width : 120,
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
	// 价格条款
	var clauseTypeBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotClause",
				cmpId : 'clauseTypeId',
				emptyText : "Del.Term",
				editable : true,
				valueField : "id",
				displayField : "clauseName",
				pageSize : 10,
				width : 120,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'clauseTypeIdFind'
			});
	// 出货状态
	var chuStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'Running'], [2, 'Shipped']]
			});
	var chuBox = new Ext.form.ComboBox({
				name : 'canOut',
				editable : false,
				emptyText : "Invoice Status",
				store : chuStore,
				value : 0,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hiddenName : 'canOut',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'canOut'
			});
	// 出货状态
	var queryTypeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[1, 'Expiry deadline'], [2, 'This Week'],[3,'Next Week']]
			});
	var queryTypeBox = new Ext.form.ComboBox({
				name : 'queryType',
				editable : false,
				emptyText : "Deadline",
				store : queryTypeStore,
				//value : 0,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				width : 100,
				hidden:displayType == "QC" ? true : false,
				hiddenName : 'queryType',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'queryType'
			});
	var me = this;
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', typeLv1IdBox, shenBox,clauseTypeBox,chuBox,facComb,zheBox,nationBox,{
							xtype : "datefield",
							emptyText : "Start Deadline",
							width : 90,
							format : "d-m-Y",
							id : 'startTime',
							vtype:'daterange',
							hidden:displayType == "QC" ? true : false,
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "end Deadline",
							width : 90,
							format : "d-m-Y",
							vtype:'daterange',
							startDateField : 'startTime',
							id : 'endTime',
							hidden:displayType == "QC" ? true : false,
							isSearchField : true,
							searchName : 'endTime'
						},queryTypeBox,{
							xtype : 'searchcombo',
							width : 100,
							emptyText : "P.I No.",
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : detailds
						},'->',{
							text:'Upload',
							handler:showUploadWin
						}]
			});
	var detailgrid = new Ext.grid.EditorGridPanel({
				id : "detailGrid",
				ref : 'detailGrid',
				stripeRows : true,
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				tbar : tb,
				region : "center",
				loadMask : true, // 是否显示正在加载
//				plugins : [new Ext.ux.plugins.GroupHeaderGrid({
//							rows : [[{}, {}, {}, {}, {}, {} ,{},{}, {}, {}, {},
//									{}, {}, {}, {}, {}, {},{},{},{},{},{},{},{}, {
//										header : groupMap[displayType][0],
//										colspan : 3,
//										align : 'center',
//										dataIndex : valueMap[displayType][0]
//									}, {
//										header : groupMap[displayType][1],
//										colspan : 2,
//										align : 'center',
//										dataIndex : valueMap[displayType][1]
//									}, {}, {}, {}, {}, {}, {},{}]],
//							hierarchicalColMenu : true
//						})],
				// tbar : detailtb,
				bbar : detailtoolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	detailgrid.on('rowclick',function(thisgrid,rowIdx){
		var row = detailgrid.getStore().getAt(rowIdx);
		if(row == null) return;
		var celIndex = detailgrid.getColumnModel().findColumnIndex('qcPerson');
		globalNationId = row.get('nationId');
		detailgrid.getColumnModel().setEditor(celIndex,new Ext.form.ComboBox({
			store : new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							method : 'post',
							url : './servlet/DataSelvert?tbname=CotEmps&typeName=roleId&type='+globalNationId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, [{
									name : 'id'
								}, {
									name : 'empsName'
								}]),
				autoLoad : true,
				remoteSort : false
			}),
			editable : true,
			valueField : "empsName",
			displayField : "empsName",
			mode : 'remote',// 默认local
			pageSize : 10,
			selectOnFocus : true,
			width : 90,
			emptyText : 'Sales',
			minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
			listWidth : 350,// 下
			triggerAction : 'all'
		}))
	})
	var viewport = new Ext.Viewport({
				layout : "border",
				items : [detailgrid]
			});
	viewport.doLayout();

	function query() {
		ds.reload()
	}

	// 弹出聊天记录
	window.showFaqWin = function(flag, orderId, orderNo, factoryId, custId,
			allPinName, orderLcDate) {
		var win = new FaqWin({
					'flag' : flag,
					'orderId' : orderId,
					'orderNo' : orderNo,
					'factoryId' : factoryId,
					'custId' : custId,
					'allPinName' : allPinName,
					'orderLcDate' : orderLcDate,
					dsPa : detailds,
					grid:detailgrid
				});
		win.show();
	}

	// 弹出评分窗口
	window.showFenWin = function(displayType, orderFacId, orderNo, remark, num,
			status, factoryId, man) {
		if (status != 1) {
			Ext.MessageBox.alert('Message',
					'Please save Deadline and Approval!');
			return;
		}
		var win = new Ext.Window({
			title : 'Rating of Supplier',
			width : 400,
			height : 360,
			layout : 'fit',
			modal : true,
			border : false,
			items : [{
				xtype : 'form',
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				paddings : '5px',
				buttonAlign : 'center',
				// monitorValid:true,
				// buttons : [{
				// text : "Save",
				// formBind:true,
				// iconCls : "page_table_save",
				// handler : function() {
				// cotOrderFacService.updateOrderFacFen(orderFacId,
				// displayType, $('fenStatusTxt').value,
				// $('fenRemarkTxt').value, function() {
				// reloadGrid('detailGrid');
				// win.close();
				// });
				// }
				// }, {
				// text : "Cancel",
				// iconCls : "page_cancel",
				// handler : function() {
				// win.close();
				// }
				// }],
				items : [{
							xtype : 'textfield',
							fieldLabel : "PI No.",
							anchor : "100%",
							value : orderNo,
							disabled : true,
							disabledClass : 'combo-disabled'
						}, {
							xtype : 'bindCombo',
							dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
							cmpId : 'fenFactoryIdTxt',
							fieldLabel : "Supplier",
							editable : true,
							valueField : "id",
							displayField : "shortName",
							emptyText : 'Choose',
							pageSize : 10,
							sendMethod : "post",
							anchor : "100%",
							selectOnFocus : true,
							minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
							listWidth : 350,// 下
							triggerAction : 'all',
							disabled : true,
							disabledClass : 'combo-disabled',
							listeners : {
								'afterrender' : function(box) {
									box.bindPageValue("CotFactory", "id",
											factoryId);
								}
							}
						}, {
							xtype : 'bindCombo',
							dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
							cmpId : 'fenManTxt',
							fieldLabel : "From",
							editable : true,
							valueField : "id",
							displayField : "empsName",
							emptyText : 'Choose',
							pageSize : 10,
							sendMethod : "post",
							anchor : "100%",
							selectOnFocus : true,
							minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
							listWidth : 350,// 下
							triggerAction : 'all',
							disabled : true,
							disabledClass : 'combo-disabled',
							listeners : {
								'afterrender' : function(box) {
									box.bindPageValue("CotEmps", "id", man);
								}
							}
						}, {
							xtype : 'combo',
							name : 'fenStatusTxt',
							fieldLabel : 'Rating',
							editable : false,
							store : new Ext.data.SimpleStore({
										fields : ["tp", "name"],
										data : [[1, 1], [2, 2], [3, 3], [4, 4],
												[5, 5]]
									}),
							value : num,
							valueField : "tp",
							displayField : "name",
							mode : 'local',
							validateOnBlur : true,
							disabled : true,
							disabledClass : 'combo-disabled',
							// allowBlank : false,
							// blankText : 'Please choose rate',
							triggerAction : 'all',
							anchor : "100%",
							hiddenName : 'fenStatusTxt',
							selectOnFocus : true
						}, {
							xtype : 'textarea',
							fieldLabel : "Remark",
							anchor : "100%",
							id : "fenRemarkTxt",
							name : "fenRemarkTxt",
							value : remark,
							disabled : true,
							disabledClass : 'combo-disabled',
							height : 80
						}, {
							xtype : 'panel',
							html : "<font color=red>Annotation:</font><ol><li>1.&nbsp;It is given for completely unacceptable cooperation</li>"
									+ "<li>2.&nbsp;It is given for inadequate cooperation</li>"
									+ "<li>3.&nbsp;It is given for adequate cooperation</li>"
									+ "<li>4.&nbsp;It is given for good cooperation</li>"
									+ "<li>5.&nbsp;It is given for outstanding cooperation</li>"
									+ "</ol>"
						}]
			}]
		});
		win.show();
	}

	// 判断当前修改的时间是否能使po approved
	function checkStatus() {
		var record = Ext.getCmp('detailGrid').getSelectionModel().getSelected();
		var sd = Ext.isEmpty(record.get(valueMap[displayType][0]));
		var sa = Ext.isEmpty(record.get(valueMap[displayType][1]));
		var cd = Ext.isEmpty(record.get(valueMap[displayType][2]));
		var ca = Ext.isEmpty(record.get(valueMap[displayType][3]));
		if (displayType == 'QC') {
			if (!sa) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((!sd && !sa) || (!cd && !ca)) {
				if ((sd && !sa) || (!sd && sa) || (cd && !ca) || (!cd && ca)) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	function saveOrderFac(id, orderFacId, win) {
		var record = Ext.getCmp('detailGrid').getSelectionModel().getSelected();
		var vDetailId = record.get('id');
		var orderFacId = record.get('orderfacId');
		var orderId = record.get('orderId');
		var orderNo = record.get('orderNo');
		var factoryId = record.get('factoryId');
		var orderFac = new CotOrderFac;
		cotOrderFacService.getOrderFacById(orderFacId, function(rs) {
			if (rs != null) {
				for (var po in rs) {
					if (po == valueMap[displayType][0]) {
						if (!Ext.isEmpty(record.get(valueMap[displayType][0]))) {
							rs[po] = new Date(Date.parse(record
									.get(valueMap[displayType][0]))+21600000);
						} else {
							delete rs[po];
						}
					} else if (po == valueMap[displayType][1]) {
						if (!Ext.isEmpty(record.get(valueMap[displayType][1]))) {
							rs[po] = new Date(Date.parse(record
									.get(valueMap[displayType][1]))+21600000);
						} else {
							delete rs[po];
						}
					} else if (po == valueMap[displayType][2]) {
						if (!Ext.isEmpty(record.get(valueMap[displayType][2]))) {
							rs[po] = new Date(Date.parse(record
									.get(valueMap[displayType][2]))+21600000);
						} else {
							delete rs[po];
						}
					} else if (po == valueMap[displayType][3]) {
						if (!Ext.isEmpty(record.get(valueMap[displayType][3]))) {
							rs[po] = new Date(Date.parse(record
									.get(valueMap[displayType][3]))+21600000);
						} else {
							delete rs[po];
						}
					} else if (po == "orderLcDelay") {
						if (!Ext.isEmpty(record.get("orderLcDelay"))) {
							rs[po] = new Date(Date.parse(record
									.get("orderLcDelay"))+21600000);
						} else {
							delete rs[po];
						}
					}else if(po == 'etdDate'){
						if (!Ext.isEmpty(record.get("etdDate"))) {
							rs[po] = new Date(Date.parse(record
									.get("etdDate"))+21600000);
						} else {
							delete rs[po];
						}
					}else if (po == "designTime") {
						if (!Ext.isEmpty(record.get("designTime"))) {
							rs[po] = new Date(Date.parse(record
									.get("designTime"))+21600000);
						} else {
							delete rs[po];
						}
					} else if (po == "oderFacText") {
						if (!Ext.isEmpty(record.get("orderFacText"))) {
							rs[po] = new Date(Date.parse(record
									.get("orderFacText"))+21600000);
						} else {
							delete rs[po];
						}
					}else {
						orderFac[po] = rs[po];
					}
				}
				if (displayType == "QC") {
					rs.qcLocation = record.get('qcLocation');
					rs.estConsumedTime = record.get('estConsumedTime');
					rs.estTime = record.get('estTime');
					rs.travelTime = record.get('travelTime');
					rs.travelConsumedTime = record.get('travelConsumedTime');
					rs.totalExpenseUsd = record.get('totalExpenseUsd');
					rs.qcPerson = record.get('qcPerson');
					rs.zheType = record.get('zheType');
				}
				rs.zheNum = record.get('zheNum');
//				if (displayType == "SAMPLEOUT") {
//					if (!Ext.isEmpty(record.get("orderFacText"))) {
//						rs[oderFacText] = new Date(Date.parse(record
//								.get("orderFacText")));
//					} else {
//						delete rs[oderFacText];
//					}
//				}

				cotOrderFacService.updateOrderFac(rs, function() {
					cotOrderFacService.updateOrderStatusCon(orderFacId,
							orderId, function(res) {
								reloadGrid('detailGrid');
								parent.window.frames['tab_home1'].location.href = "cotorderstatus.do?method=queryOrderStatus";
								if ((res[0] == 1 && displayType == "SAMPLE")
										|| (res[1] == 1 && displayType == "PACKAGE")
										|| (res[2] == 1 && displayType == "SAMPLEOUT")
										|| (res[3] == 1 && displayType == "QC")
										|| (res[4] == 1 && displayType == "OUT")) {
									cotOrderFacService.updateOrderFacFen(
											orderFacId, displayType,
											$('fenStatus').value,
											$('fenRemark').value,
											$('fenMan').value, function() {
												reloadGrid('detailGrid');
												win.close();
											});
								} else {
									Ext.Msg.alert("Message",
											"Successfully saved！");
								}
							})
				})
			}
		});
	}

	window.updateOrderFac = function(id, orderFacId) {
		var approve = checkStatus();
		if (!approve) {
			saveOrderFac(id, orderFacId);
		} else {
			var record = Ext.getCmp('detailGrid').getSelectionModel()
					.getSelected();
			var vDetailId = record.get('id');
			var orderFacId = record.get('orderfacId');
			var orderId = record.get('orderId');
			var orderNo = record.get('orderNo');
			var factoryId = record.get('factoryId');

			var win = new Ext.Window({
				title : 'Rating of Supplier',
				width : 400,
				height : 360,
				layout : 'fit',
				modal : true,
				border : false,
				items : [{
					xtype : 'form',
					labelAlign : 'right',
					frame : true,
					labelWidth : 80,
					paddings : '5px',
					buttonAlign : 'center',
					buttons : [{
								text : "Save",
								iconCls : "page_table_save",
								handler : function() {
									saveOrderFac(id, orderFacId, win);
								}
							}, {
								text : "Cancel",
								iconCls : "page_cancel",
								handler : function() {
									win.close();
								}
							}],
					items : [{
								xtype : 'textfield',
								fieldLabel : "PI No.",
								anchor : "100%",
								value : orderNo,
								disabled : true,
								disabledClass : 'combo-disabled'
							}, {
								xtype : 'bindCombo',
								dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1",
								cmpId : 'fenFactoryId',
								fieldLabel : "Supplier",
								editable : true,
								valueField : "id",
								displayField : "shortName",
								emptyText : 'Choose',
								pageSize : 10,
								sendMethod : "post",
								anchor : "100%",
								selectOnFocus : true,
								minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
								listWidth : 350,// 下
								triggerAction : 'all',
								disabled : true,
								disabledClass : 'combo-disabled',
								listeners : {
									'afterrender' : function(box) {
										box.bindPageValue("CotFactory", "id",
												factoryId);
									}
								}
							}, {
								xtype : 'bindCombo',
								dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
								cmpId : 'fenMan',
								fieldLabel : "From",
								editable : true,
								valueField : "id",
								displayField : "empsName",
								emptyText : 'Choose',
								pageSize : 10,
								sendMethod : "post",
								anchor : "100%",
								selectOnFocus : true,
								minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
								listWidth : 350,// 下
								triggerAction : 'all',
								disabled : true,
								disabledClass : 'combo-disabled',
								listeners : {
									'afterrender' : function(box) {
										box.bindPageValue("CotEmps", "id",
												logId);
									}
								}
							}, {
								xtype : 'combo',
								name : 'fenStatus',
								fieldLabel : 'Rating',
								editable : false,
								store : new Ext.data.SimpleStore({
											fields : ["tp", "name"],
											data : [[1, 1], [2, 2], [3, 3],
													[4, 4], [5, 5]]
										}),
								value : 1,
								valueField : "tp",
								displayField : "name",
								mode : 'local',
								validateOnBlur : true,
								triggerAction : 'all',
								anchor : "100%",
								hiddenName : 'fenStatus',
								selectOnFocus : true
							}, {
								xtype : 'textarea',
								fieldLabel : "Remark",
								anchor : "100%",
								id : "fenRemark",
								name : "fenRemark",
								height : 80
							}, {
								xtype : 'panel',
								html : "<font color=red>Annotation:</font><ol><li>1.&nbsp;It is given for completely unacceptable cooperation</li>"
										+ "<li>2.&nbsp;It is given for inadequate cooperation</li>"
										+ "<li>3.&nbsp;It is given for adequate cooperation</li>"
										+ "<li>4.&nbsp;It is given for good cooperation</li>"
										+ "<li>5.&nbsp;It is given for outstanding cooperation</li>"
										+ "</ol>"
							}]
				}]
			});
			win.show();
		}
	}
});
function showUploadWin(){
	var grid = Ext.getCmp('detailGrid');
	var row = grid.getSelectionModel().getSelected();
	var uploadGrid = new OrderStatusDetailUploadGrid({
		orderId:row.get('id'),
		orderStatus:displayType,
		region:'center'
	});
	var uploadWin = new Ext.Window({
		width : 450,
		height : 200,
		modal : true,
		layout:'border',
		closeAction:'close',
		items:[uploadGrid]
	})
	uploadWin.show();
}

// 打开备注
function showMark(orderId,t) {
	if(logId!=22 && logId!=47 && logId!=51 && logId!=56){
		Ext.MessageBox.alert("Message",'Do not have permission!');
		return;
	}
	// 判断po是否审核通过
	cotOrderService.getOrderById(orderId, function(res) {
		if (res) {
			var mark=res.staMark;
			var tId=res.staId;
			if(t==1){
				mark=res.preStaMark;
				tId=res.preStaId;
			}
			if(t==2){
				mark=res.artStaMark;
				tId=res.artStaId;
			}
			if(t==3){
				mark=res.shiStaMark;
				tId=res.shiStaId;
			}
			if(t==4){
				mark=res.qcStaMark;
				tId=res.qcStaId;
			}
			if(t==5){
				mark=res.spStaMark;
				tId=res.spStaId;
			}
			
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
						cotOrderService.saveOrderStaMark(orderId,mark,content,t, function(gut) {
							reloadGrid("detailGrid");
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
									checked : tId==1?true:false
								}, {
									boxLabel : "<img src='/CotSystem/common/ext/resources/images/extend/flag_clear.png'/>",
									inputValue : 0,
									name : 'showHeader',
									checked : tId==1?false:true
								}]
					},{
						xtype:"textarea",
						anchor:'95%',
						height:90,
						ref:'../content',
						value:mark,
						fieldLabel : "Comment"
					}]
				}]
			});
			win.show();
		}
	});
}
