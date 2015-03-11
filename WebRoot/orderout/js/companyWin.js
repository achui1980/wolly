CompanyWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "companyShortName"
			}, {
				name : "companyCorporation"
			}, {
				name : "companyNbr"
			}, {
				name : "companyFax"
			}, {
				name : "companyAddr"
			}, {
				name : "companyEnName"
			}, {
				name : "companyEnAddr"
			},{
				name:"companyEmail"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryCompanyToContract"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
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
							header : "Short Name",
							dataIndex : "companyShortName",
							width : 80
						}, {
							header : "Contact",
							dataIndex : "companyCorporation",
							width : 80
						}, {
							header : "Phone",
							dataIndex : "companyNbr",
							width : 80
						}, {
							header : "Fax",
							dataIndex : "companyFax",
							width : 70
						}, {
							header : "Address",
							dataIndex : "companyAddr",
							width : 130
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});

	// 行点击时加载右边折叠面板表单
	grid.on("rowdblclick", function(grid, rowIndex, e) {
				var rec = ds.getAt(rowIndex);
				$('consignSendPerson').value = rec.data.companyEnName
						+ "\nTel:" + rec.data.companyNbr + "\nFax:"
						+ rec.data.companyFax + "\nAdd:"
						+ rec.data.companyEnAddr+ "\nEmail:"
							+ rec.data.companyEmail;
				_self.close();
			});

	// 分页基本参数
	ds.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	// 表单
	var con = {
		title : 'Double-click the table row into the company',
		layout : 'fit',
		width : 600,
		height : 250,
		border : true,
		modal : true,
		padding : "0",
		// closeAction : 'hide',
		items : [grid]
	};

	Ext.apply(con, cfg);
	CompanyWin.superclass.constructor.call(this, con);
};
Ext.extend(CompanyWin, Ext.Window, {});
