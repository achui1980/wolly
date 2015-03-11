FitInputGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 配件单价保留几位小数
	var fitPriceNum = getDeNum("fitPrecision");
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "fitNo"
			}, {
				name : "fitName"
			}, {
				name : "fitDesc"
			}, {
				name : "fitPrice",
				type : "float"
			} ,{
				name : "fitTrans"
			}]);

	// 创建数据源
	var ds = new Ext.data.Store({
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
						}, roleRecord)
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				hidden : true
			}, {
				header : "Fitting No",
				dataIndex : "fitNo",
				width : 80,
				sortable : true
			}, {
				header : "Fitting Name",
				dataIndex : "fitName",
				width : 100,
				sortable : true
			}, {
				header : "Fitting Size",
				dataIndex : "fitDesc",
				width : 120,
				sortable : true
			}, {
				header : "Purchase Price",
				dataIndex : "fitPrice",
				width : 80,
				sortable : true
			}, {
				header : "sale Price",
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
				header : "Picture",
				sortable : true,
				dataIndex : "id",
				renderer : function(value) {
					var rdm = Math.round(Math.random() * 10000);
					return '<img src="showPicture.action?flag=fit&detailId='
							+ value
							+ '&tmp='
							+ rdm
							+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)" />'
				}
			}]);

	var toolBar = new Ext.PagingToolbar({
				pageSize : 5,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : 'textfield',
							width : 100,
							emptyText : "Fitting No",
							isSearchField : true,
							searchName : 'fitNoFind'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Fitting Name",
							isSearchField : true,
							searchName : 'fitNameFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Import",
							iconCls : 'gird_exp',
							handler : onExp
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				border : false,
				stripeRows : true,
				bodyStyle : 'width:100%',
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
	// 导入
	function onExp() {
		var list = sm.getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
					var cotFitting = new CotFittings();
					cotFitting.id = item.id;
					res.push(cotFitting);
				});
		if (res.length == 0) {
			Ext.MessageBox.alert("Message", "please select the imported fittings!");
			return;
		}
		insertSelect(res);
	}

	var con = {
		title : "Fitting List",
		width : 600,
		height : 400,
		layout : 'fit',
		modal : true,
		items : [grid],
		onEsc : function(k, e){
	        e.stopEvent();
	    }
	};

	Ext.apply(con, cfg);
	FitInputGrid.superclass.constructor.call(this, con);
};
Ext.extend(FitInputGrid, Ext.Window, {});
