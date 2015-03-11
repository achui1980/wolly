/**
 * 供应商的报价情况
 * 
 * @class QH.pan.SupplierGrid
 * @extends Ext.grid.GridPanel
 */
QH.pan.SupplierGrid = Ext.extend(Ext.grid.GridPanel, {
	stripeRows : true,
	border : false,
	loadMask : true,
	initComponent : function() {
		var empData, curData
		DWREngine.setAsync(false);
		// 加载币种表缓存
		baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn",
				function(res) {
					curData = res;
				});
		baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsId",
				function(res) {
					empData = res;
				});
		DWREngine.setAsync(true);
		var roleRecord = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "eleNameEn"
				}, {
					name : "manufactorer"
				}, {
					name : 'priceNo'
				}, {
					name : "panId"// 询盘明细id
				}, {
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
				}, {
					name : 'remark'
				}, {
					name : 'willSupplier'
				}, {
					name : 'fileUrl'
				}, {
					name : 'uploadEmp',
					type : 'int'
				}]);
		// 创建数据源
		this.store = new Ext.data.Store({
					autoLoad : {
						params : {
							start : 0
						}
					},
					baseParams : {
						limit : 40
					},
					proxy : new Ext.data.HttpProxy({
								url : "cotpan.do?method=querySupplierPanDetail"
							}),
					reader : new Ext.data.JsonReader({
								root : "data",
								totalProperty : "totalCount",
								idProperty : "id"
							}, roleRecord)
				});
		this.sm = new Ext.grid.CheckboxSelectionModel();

		// 创建需要在表格显示的列
		this.cm = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true
			},
			columns : [this.sm, {
						header : "ID",
						dataIndex : "id",
						width : 25,
						hidden : true
					}, {
						header : "Title",
						align : 'center',
						dataIndex : "priceNo",
						width : 100
					}, {
						header : "Article name",
						align : 'center',
						dataIndex : "eleNameEn",
						width : 100
					},{
						header : "Picture",
						dataIndex : "panId",
						width : 70,
						renderer : function(value) {
							var rdm = Math.round(Math.random() * 10000);
							return '<img src="./showPicture.action?flag=pan&detailId='
									+ value
									+ '&tmp='
									+ rdm
									+ '" width="70" height="70px" onload="javascript:DrawImage(this,70,70)" onclick="showBigPicDiv(this)"/>'
						}
					}, {
						header : "Manufactorer",
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
					}, {
						header : "Attachment",
						align : 'center',
						dataIndex : "fileUrl",
						width : 200,
						renderer : function(value, metadata, record, rowIndex,
								colIndex, store) {
									var panId = record.id;
									var idx = value.lastIndexOf('\\');
									var name = value.substr(idx+1);
									//如果是pdf直接打开
									var pre = value.lastIndexOf('.');
									if(pre>=0){
										var preTxt = value.substr(pre+1);
										if(preTxt.toLowerCase()=='pdf'){
											var rdm = Math.round(Math.random() * 10000);
											//把"\"转换成"/"
											value=value.replace(/\\/g,"/");
											return '<a id="scSrc" onclick=openEleWindow(".'+value+'?tmp='+rdm+'") style="cursor: hand;text-decoration: underline;">'+name+'</a>'
										}
									}
									return '<a href="./servlet/DownPanAttachServlet?panId='+panId+'">'+name+'</a>'
								}
					}, {
						header : "Upload Staff",
						align : 'center',
						dataIndex : "uploadEmp",
						width : 100,
						renderer : function(value) {
							if (value != null) {
								return empData[value];
							}
						}
					}, {
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
					}]
		});

		// 样品表格顶部工具栏
		this.tbar = new Ext.ux.SearchComboxToolbar({
			items : [{
						xtype : 'bindCombo',
						dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
						cmpId : 'addPerson',
						emptyText : "Request By",
						width : 100,
						editable : true,
						valueField : "id",
						displayField : "empsName",
						pageSize : 10,
						selectOnFocus : true,
						sendMethod : "post",
						minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
						listWidth : 350,// 下
						triggerAction : 'all',
						isSearchField : true,
						searchName : 'addPerson'
					}, {
						xtype : 'bindCombo',
						cmpId : "deptId",
						dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
						valueField : "id",
						emptyText : "Department",
						sendMethod : "post",
						displayField : "typeEnName",
						width : 100,
						isSearchField : true,
						searchName : 'deptId'
					}, {
						xtype : "textfield",
						width : 120,
						emptyText : "Article name",
						isSearchField : true,
						searchName : 'eleNameEn'
					}, {
						xtype : "textfield",
						width : 120,
						emptyText : "Manufactorer",
						isSearchField : true,
						searchName : 'manufactorer'
					}, {
						xtype : "datefield",
						emptyText : "Upload Start",
						width : 90,
						format : "d/m/Y",
						id : 'startTime',
						vtype : 'daterange',
						endDateField : 'endTime',
						isSearchField : true,
						searchName : 'startTime'
					}, {
						xtype : "datefield",
						emptyText : "Upload End",
						width : 90,
						format : "d/m/Y",
						id : 'endTime',
						vtype : 'daterange',
						startDateField : 'startTime',
						isSearchField : true,
						searchName : 'endTime'
					}, {
						xtype : 'bindCombo',
						dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
						cmpId : 'uploadEmp',
						editable : true,
						valueField : "id",
						displayField : "empsName",
						emptyText : 'Upload Staff',
						pageSize : 10,
						width : 95,
						sendMethod : "post",
						selectOnFocus : true,
						minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
						listWidth : 350,// 下
						triggerAction : 'all',
						isSearchField : true,
						searchName : 'uploadEmp'
					}, {
						xtype : 'combo',
						name : 'state',
						editable : false,
						store : new Ext.data.SimpleStore({
									fields : ["id", "name"],
									data : [[0, 'Status'], [2, 'Waiting'],
											[1, 'Approved']]
								}),
						valueField : "id",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						width : 95,
						emptyText : 'Status',
						hiddenName : 'state',
						selectOnFocus : true,
						isSearchField : true,
						searchName : 'state'
					}, {
						xtype : 'searchcombo',
						width : 95,
						emptyText : "Title",
						isSearchField : true,
						searchName : 'priceNo',
						isJsonType : false,
						store : this.store
					}]
		});

		// 样品表格分页工具栏
		this.bbar = new Ext.PagingToolbar({
					pageSize : 40,
					store : this.store,
					displayInfo : true,
					displaySize : '5|10|15|20|300|500',
					listeners : {
						beforechange : function(pTbar, params) {
							pTbar.store.setBaseParam('limit', params.limit);
						}
					}
				});
		this.viewConfig = {
			forceFit : true,
			getRowClass : function(record, index) {
				if (record.get("state") == 1
						&& record.get("manufactorer") == record
								.get("willSupplier")) {
					return "x-grid-record-green";
				} else {
					return "x-grid-record-qing";
				}
			}
		};
		QH.pan.SupplierGrid.superclass.initComponent.call(this);
	}
});

Ext.reg('suppliergrid', QH.pan.SupplierGrid);