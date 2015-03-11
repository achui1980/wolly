// 报价记录表格导入报价单
GivenWin = function(cfg) {
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
		}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryGivenToSign&givenId="+cfg.givenId
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
							width : 120
						}, {
							header : "客号",
							dataIndex : "custNo",
							width : 120
						}, {
							header : "英文名称",
							dataIndex : "eleNameEn",
							width : 200
						}]
			});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
		items : ['->', {
			text : "添加",
			iconCls : "page_add",
			cls : "SYSOP_ADD",
				handler : function(){
					mask();
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
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 加载表格
	ds.load({
		params : {
			start : 0,
			limit : 15
		}
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
		var list = getIds();
		if (list.length == 0) {
			unmask();
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary,'given');
	}

	// 表单
	var con = {
		title:'送样记录',
		layout : 'fit',
		width : 600,
		height : 490,
		border : false,
		modal : true,
		items : [grid]
	};

	Ext.apply(con, cfg);
	GivenWin.superclass.constructor.call(this, con);
};
Ext.extend(GivenWin, Ext.Window, {});
