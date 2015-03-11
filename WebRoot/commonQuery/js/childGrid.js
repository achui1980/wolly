// 子货号记录表格导入
ChildGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 加载币种表缓存
	DWREngine.setAsync(false);
	var curData;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	var eId=-1;
	if(cfg.parentEleId && cfg.parentEleId!=''){
		queryService.findIsExistEleByEleId(cfg.parentEleId, function(res) {
							if (res != null) {
								eId=res;
							} 
						});
	}		
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "priceOut",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "priceOutUint"
			}, {
				name : "eleName"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 15,
					parentId:eId
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryChildEles"
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
					header : "子货号",
					dataIndex : "eleId",
					width : 60
				}, {
					header : "Pic", // 表头
					sortable : true,
					dataIndex : "id",
					renderer : function(value) {
						var rdm = Math.round(Math.random() * 10000);
						return '<img src="./showPicture.action?flag=ele&elementId='
								+ value
								+ '&tmp='
								+ rdm
								+ ' width="70" height="70px" onload="javascript:DrawImage(this,70,70)"/>'
					}
				}, {
					header : "name-cn",
					dataIndex : "eleName",
					width : 60
				}, {
					header : "Price",
					dataIndex : "priceOut",
					renderer : function(value) {
						return "<font color=red>" + value + "</font>";
					}
				}, {
					header : "Currency",
					dataIndex : "priceOutUint",
					width : 60,
					renderer : function(value) {
						return "<font color=green>" + curData["" + value]
								+ "</font>";
					}
				}]
	});

	// 父货号下拉框
	var parentBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotElementsNew&key=eleId",
				cmpId : 'parentId',
				emptyText : "父货号",
				editable : true,
				valueField : "id",
				displayField : "eleId",
				pageSize : 10,
				width : 100,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'parentId'
			});

	// 厂家
	var facBox = new BindCombox({
		        dataUrl : "servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
				//dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryId',
				emptyText : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				sendMethod : "post",
				pageSize : 10,
				width : 100,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'factoryId'
			});

	// 样品表格顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
		items : ['-', parentBox, facBox, {
					xtype : 'textfield',
					emptyText : "子货号",
					width : 100,
					isSearchField : true,
					searchName : 'childEleId'
				}, {
					xtype : 'searchcombo',
					width : 100,
					emptyText : "name-cn",
					isSearchField : true,
					searchName : 'eleName',
					isJsonType : false,
					store : ds
				}, '->', {
					xtype : "checkbox",
					style : 'margin-top: 0px;',
					fieldLabel : "标签",
					boxLabel : "<span ext:qtip='用/将所选子货号名组合成一个货号。'>合并所选子货号</span>",
					id : "comFlag",
					listeners : {
						"render" : function(obj) {
							var tip = new Ext.ToolTip({
										target : obj.getEl(),
										anchor : 'top',
										maxWidth : 150,
										minWidth : 150,
										html : '用/将所选子货号名组合成一个货号。'
									});
						}
					}
				}, '-', {
					text : "Create",
					iconCls : "page_add",
					cls : "SYSOP_ADD",
					handler : function() {
						var list = getIds();
						if (list.length == 0) {
							Ext.MessageBox.alert("提示信息", '请选择要导入的子货号！');
							return;
						}
						mask();
						setTimeout(function() {
									insert();
								}, 500);
					}
				}]
	});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
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
					forceFit : true
				}
			});

	grid.on("rowdblclick", function(grid, rowIndex, e) {
				insert();
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

	// 添加
	function insert() {
		var ary = sm.getSelections();
		if ($('comFlag').checked == true) {
			// 获得表格选择的记录,合并子货号
			var ids = '';
			Ext.each(ary, function(res) {
						ids += res.data.id + ",";
					});
			// 重新换算价格
			DWREngine.setAsync(false);
			queryService.getComChilds(ids, cfg.currencyId, function(res) {
						cfg.bar.insertChildSelect(res);
					});
			DWREngine.setAsync(true);
		} else {
			cfg.bar.insertSelect(ary, true);
		}
		unmask();
		Ext.MessageBox.alert('提示消息', '添加成功!');
	}

	// 加载数据
	this.load = function() {
		if(eId!=-1){
			parentBox.bindPageValue("CotElementsNew", "id", eId);
		}
		ds.load({
					params : {
						start : 0
					}
				});
	}

	// 表单
	var con = {
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	ChildGrid.superclass.constructor.call(this, con);
};
Ext.extend(ChildGrid, Ext.Panel, {});
