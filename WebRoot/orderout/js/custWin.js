CustWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "customerNo"
			}, {
				name : "customerShortName"
			}, {
				name : "priContact"
			}, {
				name : "fullNameEn"
			}, {
				name : "contactNbr"
			}, {
				name : "customerFax"
			}, {
				name : "customerAddrEn"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryCustomerToContract"
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
							header : "Client No.",
							dataIndex : "customerNo",
							width : 120
						}, {
							header : "Name",
							dataIndex : "customerShortName",
							width : 80
						}, {
							header : "Contact",
							dataIndex : "priContact",
							width : 80
						}, {
							header : "Name",
							dataIndex : "fullNameEn",
							width : 100
						}, {
							header : "Phone",
							dataIndex : "contactNbr",
							width : 80
						}, {
							header : "Fax",
							dataIndex : "customerFax",
							width : 80
						}, {
							header : "Address",
							dataIndex : "customerAddrEn",
							width : 130
						}]
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
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
							emptyText : "Client No.",
							isSearchField : true,
							searchName : 'customerNoFind'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Short Name",
							isSearchField : true,
							searchName : 'customerShortNameFind',
							isJsonType : false,
							store : ds
						}, '->', {
							text : "Add Client",
							iconCls : "page_add",
							handler : addCust
						}]
			});
	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 行点击时加载右边折叠面板表单
	grid.on("rowdblclick", function(grid, rowIndex, e) {
				var rec = ds.getAt(rowIndex);
				if (cfg.flag == 1) {
					$('consignNotePerson').value = rec.data.priContact
							+ "\nTel:" + rec.data.contactNbr + "\nFax:"
							+ rec.data.customerFax + "\nAdd:"
							+ rec.data.customerAddrEn;
				} else {
					$('consignRecvPerson').value = rec.data.fullNameEn
							+ "\nAdd:" + rec.data.customerAddrEn + "\nAttn:"
							+ rec.data.priContact + "\nTel:"
							+ rec.data.contactNbr + "\nFax:"
							+ rec.data.customerFax;
				}
				_self.close();
			});

	// 表单
	var con = {
		title : 'Double-click the table row into the customer',
		layout : 'fit',
		width : 600,
		height : 250,
		border : false,
		modal : true,
		items : [grid]
	};

	Ext.apply(con, cfg);
	CustWin.superclass.constructor.call(this, con);
};
Ext.extend(CustWin, Ext.Window, {});
