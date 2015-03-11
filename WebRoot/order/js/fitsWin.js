FitsWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 配件单价保留几位小数
	var fitPriceNum = getDeNum("fitPrecision");
	// 根据小数位生成"0.000"字符串
	var fitNumTemp = getDeStr(fitPriceNum);
	var fitRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "fitDesc"
			}, {
				name : "fitTrans"
			}, {
				name : "useUnit"
			}, {
				name : "fitPrice",
				type : "float",
				convert : numFormat.createDelegate(this, [fitNumTemp],3)
			}]);
	// 创建数据源
	var fitds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryFitting"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, fitRecord)
			});
	var fitsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var fitcm = new Ext.grid.ColumnModel([fitsm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : "配件号",
				dataIndex : "fitNo",
				width : 80,
				sortable : true
			}, {
				header : "配件名称",
				dataIndex : "fitName",
				width : 100,
				sortable : true
			}, {
				header : "配件规格",
				dataIndex : "fitDesc",
				width : 120,
				sortable : true
			}, {
				header : "领用单位",
				dataIndex : "useUnit",
				width : 60,
				sortable : true
			}, {
				header : "单价",
				dataIndex : "fitPrice",
				width : 80,
				sortable : true,
				renderer : function(value, metaData, record, rowIndex,
						colIndex, store) {
					var fitPrice = record.get("fitPrice");
					var fitTrans = record.get("fitTrans");
					return (fitPrice / fitTrans).toFixed(fitPriceNum);
				}
			}, {
				header : "图片", // 表头
				sortable : true,
				dataIndex : "id",
				width : 90,
				renderer : function(value) {
					var rdm = Math.round(Math.random() * 10000);
					return '<img src="showPicture.action?flag=fit&detailId='
							+ value
							+ '&tmp='
							+ rdm
							+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)" />'
				}
			}]);
	var fittoolBar = new Ext.PagingToolbar({
				pageSize : 5,
				store : fitds,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var fittb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "导入",
							iconCls : 'gird_exp',
							handler : onExp
						}]
			});

	// 导入配件表格
	function onExp() {
		var fitAnysGrid = Ext.getCmp("fitAnysGrid");
		var fitDs = fitAnysGrid.getStore();
		var fitRow = fitDs.getAt(cfg.rowCurrent);
		var arr = fitsm.getSelections();
		//统计成功插入条数
		var num=0;
		for (var i = 0; i < arr.length; i++) {
			var flag = false;
			fitDs.each(function(ft){
				if(ft.data.fittingId==arr[i].id && ft.data.orderdetailId==fitRow.data.orderdetailId){
					flag = true;
					return false;
				}
			});
			if(flag){
				continue;
			}
			DWREngine.setAsync(false);
			cotFitOrderService.getFittingById(arr[i].id, function(res) {
								var fitPrice = (res.fitPrice/res.fitTrans).toFixed(fitPriceNum);
								var totalAmount = (fitPrice*fitRow.data.boxCount).toFixed(fitPriceNum);
								if(num==0){
									fitRow.set("fitNo",res.fitNo);
									fitRow.set("fitName",res.fitName);
									fitRow.set("fitDesc",res.fitDesc);
									fitRow.set("fitUsedCount",1);
									fitRow.set("fitCount",1);
									fitRow.set("fitBuyUnit",res.useUnit);
									fitRow.set("orderFitCount",fitRow.data.boxCount);
									fitRow.set("facId",res.facId);
									fitRow.set("fitPrice",fitPrice);
									fitRow.set("totalAmount",totalAmount);
									fitRow.set("remark",res.fitRemark);
									fitRow.set("fittingId",res.id);
									num++;
								}else{
									var u = new fitDs.recordType({
												eleId : fitRow.data.eleId,
												eleName : fitRow.data.eleName,
												anyFlag : "U",
												fitNo : res.fitNo,
												fitName : res.fitName,
												fitDesc : res.fitDesc,
												boxCount : fitRow.data.boxCount,
												fitUsedCount : 1,
												fitCount : 1,
												fitBuyUnit : res.useUnit,
												orderFitCount : fitRow.data.boxCount,
												facId : res.facId,
												fitPrice : fitPrice,
												totalAmount : totalAmount,
												remark : res.fitRemark,
												fittingId : res.id,
												orderId : fitRow.data.orderId,
												orderdetailId : fitRow.data.orderdetailId
											});
									fitDs.add(u);
									num++;
								}
							});
					DWREngine.setAsync(true);
		}
		if(num==0){
			fitRow.set("fitNo","");
			Ext.MessageBox.alert('提示消息','该货号已添加此配件!');
		}
		_self.close();
	}

	var formFit = new Ext.form.FormPanel({
				title : "",
				labelWidth : 60,
				region : "north",
				labelAlign : "right",
				layout : "column",
				width : "100%",
				height : 35,
				formId : "fitAcForm",
				frame : true,
				keys : [{
							key : Ext.EventObject.ENTER,
							fn : function() {
								fitds.reload();
							}
						}],
				items : [{
							xtype : "panel",
							title : "",
							columnWidth : 0.4,
							layout : "form",
							items : [{
										xtype : "textfield",
										fieldLabel : "配件号",
										id : "fitNoFind",
										name : "fitNoFind",
										value : cfg.fitNo,
										anchor : "95%"
									}]
						}, {
							xtype : "panel",
							title : "",
							margins : "0 0 0 10",
							columnWidth : 0.6,
							layout : "column",
							items : [{
										xytpe : "label",
										columnWidth : 0.1,
										text : " "
									}, {
										xtype : "button",
										text : "查询",
										margins : "0 0 0 10",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											fitds.reload();
										}
									}, {
										xytpe : "label",
										columnWidth : 0.1,
										text : " "
									}, {
										xtype : "button",
										text : "重置",
										style : {
											marginLeft : '10px'
										},
										width : 65,
										iconCls : "page_reset",
										handler : function() {
											formFit.getForm().reset();
										}
									}]
						}]
			});

	var fitgrid = new Ext.grid.GridPanel({
				border : true,
				region : "center",
				id : "fittingGrid",
				margins : '0 0 0 0',
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : fitds, // 加载数据源
				cm : fitcm, // 加载列
				sm : fitsm,
				loadMask : true, // 是否显示正在加载
				tbar : fittb,
				bbar : fittoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	this.load = function() {
		fitds.load({
					params : {
						start : 0,
						limit : 20
					}
				})
	}

	var con = {
		title : "配件列表",
		width : 600,
		height : 400,
		layout : 'border',
		modal : true,
		items : [formFit, fitgrid],
		listeners : {
			'render' : function() {
				fitds.on('beforeload', function() {
							fitds.baseParams = DWRUtil.getValues("fitAcForm");
						});
			}
		}
	}
	Ext.apply(con, cfg);
	FitsWin.superclass.constructor.call(this, con);
};

Ext.extend(FitsWin, Ext.Window, {});