// 报价记录表格导入报价单
GivenGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	// 全局变量
	this.givenId;

	var roleRecord = new Ext.data.Record.create([
		{
			name : "id",
			type : "int"
		}, {
			name : "eleId"
		}, {
			name : "custNo"
		}, {
			name : "eleNameEn"
		}, {
			name : "boxCount"
		}, {
			name : "containerCount"
		}, {
			name : "remainBoxCount"
		},{
			name : "boxCbm",
			convert : numFormat.createDelegate(this, ["0.00"], 3)
		},{
			name : "totalCbm",
			convert : numFormat.createDelegate(this, ["0.00"], 3)
		},{
			name : "remainTotalCbm",
			convert : numFormat.createDelegate(this, ["0.00"], 3)
		}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotsplitinfo.do?method=queryOrderDetail"
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
							header : "货号",
							dataIndex : "eleId",
							width : 80
						}, {
							header : "客号",
							dataIndex : "custNo",
							width : 80
						}, {
							header : "英文名称",
							dataIndex : "eleNameEn",
							width : 120
						}, {
							header : "总数量",
							dataIndex : "boxCount",
							width : 60
						}, {
							header : "总箱数",
							dataIndex : "containerCount",
							width : 60
						}, {
							header : "CBM",
							dataIndex : "boxCbm",
							width : 60
						},{
							header : "总CBM",
							dataIndex : "totalCbm",
							width : 60
						},{
							header : "剩余数量",
							dataIndex : "remainBoxCount",
							width : 60
						},{
							header : "剩余CBM",
							dataIndex : "remainTotalCbm",
							width : 60
						}]
			});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
		items : ['->', {
			text : "添加",
			iconCls : "page_add",
			cls : "SYSOP_ADD",
				handler : function(){
					setTimeout(function(){
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
				emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				border : false,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	this.load = function() {
		// 将查询面板的条件加到ds中
		ds.on('beforeload', function(store, options) {
			ds.baseParams.orderId = _self.orderOutId;
		});
		// 加载表格
		ds.load({
			params : {
				start : 0,
				limit : 15
			}
		});
	}
	
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
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary);
	}

	// 表单
	var con = {
		layout : 'border',
		width : 650,
		height : 450,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	GivenGrid.superclass.constructor.call(this, con);
};
Ext.extend(GivenGrid, Ext.Panel, {});
