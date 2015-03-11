ImportOtherWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	var curData;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);

	// 加载产品采购其他费用
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "source"
			}, {
				name : "finaceName"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "currencyId"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "cotorder.do?method=queryOrderFacOther&source=orderfac&orderId="
					+ parent.$('pId').value
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
					sortable : true,
					width : 50
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "单据编号",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "费用来源",
							dataIndex : "source",
							width : 80,
							renderer:function(obj){
								return "产品采购";
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 130
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curData[value];
							}
						}]
			});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "添加",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler:function(){
								var data = sm.getSelections();
								if(data.length==0){
									Ext.Msg.alert('提示消息','请先选择费用');
									return;
								}
								addMore(data,"产品采购");
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	// 加载配件采购其他费用
	var roleRecordFit = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "source"
			}, {
				name : "finaceName"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "currencyId"
			}]);
	// 创建数据源
	var dsFit = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "cotorder.do?method=queryOrderFacOther&source=fitorder&orderId="
					+ parent.$('pId').value
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, roleRecordFit)
	});

	// 创建复选框列
	var smFit = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cmFit = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [smFit, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "单据编号",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "费用来源",
							dataIndex : "source",
							width : 80,
							renderer:function(obj){
								return "配件采购";
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 130
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curData[value];
							}
						}]
			});

	// 样品表格顶部工具栏
	var tbFit = new Ext.Toolbar({
				items : ['->', {
							text : "添加",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler:function(){
								var data = smFit.getSelections();
								if(data.length==0){
									Ext.Msg.alert('提示消息','请先选择费用');
									return;
								}
								addMore(data,"配件采购");
							}
						}]
			});

	var toolBarFit = new Ext.PagingToolbar({
				pageSize : 15,
				store : dsFit,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var gridFit = new Ext.grid.GridPanel({
				stripeRows : true,
				store : dsFit,
				cm : cmFit,
				sm : smFit,
				loadMask : true,
				tbar : tbFit,
				bbar : toolBarFit,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	// 加载包材采购其他费用
	var roleRecordPack = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "source"
			}, {
				name : "finaceName"
			}, {
				name : "amount",
				convert : numFormat.createDelegate(this, ["0.00"],3)
			}, {
				name : "currencyId"
			}]);
	// 创建数据源
	var dsPack = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "cotorder.do?method=queryOrderFacOther&source=packorder&orderId="
					+ parent.$('pId').value
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, roleRecordPack)
	});

	// 创建复选框列
	var smPack = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cmPack = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					width : 50
				},
				columns : [smPack, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "单据编号",
							dataIndex : "orderNo",
							width : 180
						}, {
							header : "费用来源",
							dataIndex : "source",
							width : 80,
							renderer:function(obj){
								return "包材采购";
							}
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 130
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curData[value];
							}
						}]
			});

	// 样品表格顶部工具栏
	var tbPack = new Ext.Toolbar({
				items : ['->', {
							text : "添加",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler:function(){
								var data = smPack.getSelections();
								if(data.length==0){
									Ext.Msg.alert('提示消息','请先选择费用');
									return;
								}
								addMore(data,"包材采购");
							}
						}]
			});

	var toolBarPack = new Ext.PagingToolbar({
				pageSize : 15,
				store : dsPack,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var gridPack = new Ext.grid.GridPanel({
				stripeRows : true,
				store : dsPack,
				cm : cmPack,
				sm : smPack,
				loadMask : true,
				tbar : tbPack,
				bbar : toolBarPack,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	var tbl = new Ext.TabPanel({
				height : 450,
				activeTab : 0,
				items : [{
							title : '产品采购',
							name : 'priceTab',
							layout : 'fit',
							items : [grid]
						}, {
							title : '配件采购',
							name : 'orderTab',
							layout : 'fit',
							items : [gridFit]
						}, {
							title : '包材采购',
							name : 'givenTab',
							layout : 'fit',
							items : [gridPack]
						}]
			});
			
	ds.load({
		params:{
			start:0,
			limit : 20
		}
	});
	dsFit.load({
		params:{
			start:0,
			limit : 20
		}
	})
	dsPack.load({
		params:{
			start:0,
			limit : 20
		}
	})
			
	//一次添加多条费用
	function addMore(data,typeName){
		// 将价格转换成主单价格(要导的费用乘该币种汇率换成RMB,再除于主单汇率)
		var curAray;
		DWREngine.setAsync(false);
		baseDataUtil.getBaseDicList('CotCurrency', function(res) {
			curAray = res;
		});
		DWREngine.setAsync(true);
		
		//往父页面表格加数据
		var pDs = Ext.getCmp('otherGrid').getStore();
		var rate = parent.$('orderRate').value;
		Ext.each(data,function(rec){
			//判断是否已存在
			var chk = false;
			pDs.each(function(item){
				if(item.data.isImport==rec.id){
					chk = true;
					return false;
				}
			});
			if(!chk){
				//将价格转换成主单价格
				for (var i = 0; i < curAray.length; i++) {
					if (curAray[i].id == rec.data.currencyId) {
						var ra = curAray[i].curRate;
						var am=rec.data.amount;
						var u = new pDs.recordType({
							finaceName : rec.data.finaceName+"("+typeName+")",
							flag : 'A',
							currencyId : parent.$('currencyId').value,
							amount : (am * ra / rate).toFixed("2"),
							remainAmount : (am * ra/ rate).toFixed("2"),
							isImport : rec.id
						});
						pDs.add(u); 
						break;
					}
				}
			}
		});
		Ext.Msg.alert('提示消息','导入成功!');
	}

	// 表单
	var con = {
		title : '导入其他费用',
		layout : 'fit',
		width : 600,
		height : 400,
		border : true,
		modal : true,
		id : "importOtherWin",
		items : [tbl]
	};

	Ext.apply(con, cfg);
	ImportOtherWin.superclass.constructor.call(this, con);
};
Ext.extend(ImportOtherWin, Ext.Window, {});
