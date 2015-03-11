PanDetailWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	var empData,curData
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsId", function(
					res) {
				empData = res;
			});
	DWREngine.setAsync(true);
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {}, {
				name : "panId"// 询盘明细id
			},{
				name : "boxObcount"
			}, {
				name : "price",// 供应商报价
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "ccyId"// 供应商报价币种
			}, {
				name : "valDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'state'// 状态
			}, {
				name : 'eleId'
			}, {
				name : 'modDate',// 修改时间
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'modPerson'// 联系人
			},{
				name:'remark'
			},{
				name:'willSupplier'
			},{
				name:'fileUrl'
			},{
				name:'uploadEmp',type:'int'
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotqueryall.do?method=queryPanDetail&eleNameEn="+encodeURIComponent(cfg.eleNameEn)
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
							width : 25,
							hidden : true
						}, {
							header : "<font color=red>Supplier</font>",
							align : 'center',
							dataIndex : "willSupplier",
							width : 100
						}, {
							header : "<font color=red>Price</font>",
							dataIndex : "price",
							width : 80
						}, {
							header : "<font color=red>Currency</font>",
							dataIndex : "ccyId",
							width : 60,
							renderer : function(value) {
								if (value != null) {
									return curData[value];
								}
							}
						},{
							header : "Attachment",
							align : 'center',
							dataIndex : "fileUrl",
							width : 200,
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								var panId = record.id;
								var idx = value.lastIndexOf('\\');
								var name = value.substr(idx+1);
								return '<a href="./servlet/DownPanAttachServlet?panId='+panId+'">'+name+'</a>'
							}
						},{
							header : "Upload Staff",
							align : 'center',
							dataIndex : "uploadEmp",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return empData[value];
								}
							}
						},{
							header : "Upload Date",
							align : 'center',
							dataIndex : "modDate",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "d-m-Y");
								}
							}
						}]);

	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "'No data to display"
			});
	
	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.GridPanel({
				border : false,
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : summary,
				loadMask : true, // 是否显示正在加载
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});
			
	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});
	
	var con = {
		width : 700,
		height : 300,
		title : "PRS &nbsp;<font color=red>"+cfg.eleNameEn+"</font>",
		modal : true,
		layout:'fit',
		items : [grid]
	}
	Ext.apply(con, cfg);
	PanDetailWin.superclass.constructor.call(this, con);
};

Ext.extend(PanDetailWin, Ext.Window, {});