// 配件库
FittingGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 配件单价保留几位小数
	var fitPriceNum = getDeNum("fitPrecision");
	// 根据小数位生成"0.000"字符串
	var fitNumTemp = getDeStr(fitPriceNum);
	var curData;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotTypeLv3", "id", "typeName",
			function(res) {
				curData = res;
			});
	DWREngine.setAsync(true);

	var fitRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "typeLv3Id"
			}, {
				name : "fitDesc"
			}, {
				name : "fitTrans"
			}, {
				name : "useUnit"
			}, {
				name : "fitPrice",
				type : "float"
			}]);
	// 创建数据源
	var fitds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 5
				},
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
				header : "配件类型",
				dataIndex : "typeLv3Id",
				width : 100,
				sortable : true,
				renderer : function(value) {
					return curData["" + value];
				}
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
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
	// 配件类型列表
	var comboType = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : "typeLv3Id",
				valueField : "id",
				pageSize : 10,
				sendMethod : "post",
				editable : true,
				displayField : "typeName",
				emptyText : "类型",
				width:100,
				listWidth : 250,
				isSearchField : true,
				searchName : 'typeLv3Id'
			});
			
			
	var fittb = new Ext.ux.SearchComboxToolbar({
				items : ['-',{
							xtype : "textfield",
							emptyText : "配件号",
							width : 95,
							isSearchField : true,
							searchName : 'fitNoFind'
						}, comboType,{
							xtype : 'searchcombo',
							width : 95,
							emptyText : "配件名",
							isSearchField : true,
							searchName : 'fitNameFind',
							isJsonType : false,
							store : fitds
						},'->', '-', {
							text : "导入",
							iconCls : 'gird_exp',
							handler : function() {
								var arr = fitsm.getSelections();
								cfg.bar.onExp(arr);
								_self.close();
							}
						}]
			});

	var fitgrid = new Ext.grid.EditorGridPanel({
				border : false,
				id : "fittingGrid",
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

	// 表单
	var con = {
		title : "配件列表",
		width : 600,
		height : 400,
		layout : 'fit',
		modal : true,
		items : [fitgrid],
		onEsc : function(k, e){
	        e.stopEvent();
	    }
	};

	Ext.apply(con, cfg);
	FittingGrid.superclass.constructor.call(this, con);
};
Ext.extend(FittingGrid, Ext.Window, {});
