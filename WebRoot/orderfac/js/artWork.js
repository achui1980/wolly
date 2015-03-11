Ext.onReady(function() {
//	var artMap;
//	DWREngine.setAsync(false);
//	baseDataUtil.getBaseDicDataMap("CotGivenType", "id", "givenName", function(
//					res) {
//				artMap = res;
//			});
//	DWREngine.setAsync(true);

//	var artBox = new BindCombox({
//				dataUrl : "./servlet/DataSelvert?tbname=CotGivenType&key=givenName",
//				cmpId : 'awStrId',
//				editable : true,
//				hideLabel : true,
//				labelSeparator : " ",
//				valueField : "id",
//				displayField : "givenName",
//				sendMethod : "post",
//				emptyText : 'Choose',
//				pageSize : 10,
//				autoLoad : true,
//				anchor : "100%",
//				selectOnFocus : true,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all'
//			});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "artWork"
			}, {
				name : "artText"
			}, {
				name : "remark"
			}, {
				name : "eleId"
			}, {
				name : "orderId"
			}, {
				name : 'eleNameEn'
			}, {
				name : 'barcode'
			}, {
				name : 'custNo'
			}, {
				name : "awStr"
			}, {
				name : "sizeInch"
			}, {
				name : "comment"
			}, {
				name : "piDetailId"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderfac.do?method=queryArtWork&orderId="
										+ $('mainId').value,
								update : "cotorderfac.do?method=modifyArtWork"
							},
							listeners : {
								exception : function(proxy, type, action,
										options, response, arg) {
									ds.reload();
								}
							}

						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord),
				writer : writer
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "Description",
							dataIndex : "eleNameEn",
							width : 100
						}, {
							header : "<font color=blue>Art Work Text</font>",
							dataIndex : "artWork",
							width : 200,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						}, {
							header : "Barcode",
							dataIndex : "barcode",
							width : 100
						}, {
							header : "Client Article No.",
							dataIndex : "custNo",
							width : 100
						}, {
							header : "<font color=blue>Packaging</font>",
							dataIndex : "awStr",
							width : 100,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						}, {
							header : "<font color=blue>Size</font>",
							dataIndex : "sizeInch",
							width : 120,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						}, {
							header : "<font color=blue>Quality</font>",
							dataIndex : "artText",
							width : 180,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						}, {
							header : "<font color=blue>Produced For</font>",
							dataIndex : "remark",
							width : 300,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						},{
							header : "<font color=blue>Comment</font>",
							dataIndex : "comment",
							width : 300,
							editor : new Ext.form.TextArea({
										width : 400,
										height : 100
									})
						}]
			});

	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Save",
							iconCls : 'page_table_save',
							handler : function() {
								ds.save();
							}
						},'-', {
							text : "Print",
							handler : showPrint,
							iconCls : "page_print"
						},'-', {
							text : "Report",
							handler : createReport,
							iconCls : "page_excel"
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var grid = new Ext.grid.EditorGridPanel({
				border : false,
				region : "center",
				id : "artWorkGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				plugins : [new Ext.ux.grid.GridCellToolTip()],
				bbar : toolBar,
				viewConfig : {
					forceFit : true
				}
			});
	// grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
	// // 获得view
	// var view = grid.getView();
	// // 获得单元格
	// var cell = view.getCell(rowIndex, columnIndex);
	// // 获得该行高度
	// var row = view.getRow(rowIndex);
	// var editor = cm.getCellEditor(columnIndex, rowIndex);
	// editor.setSize(cell.offsetWidth, row.scrollHeight);
	// });

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

	ds.load({
				params : {
					start : 0,
					limit : 200
				}
			});

	function showPrint() {
		// 判断po是否审核通过
		cotOrderService.checkIsGuoShen($('mainId').value, function(res) {
					if (res) {
						var rdm = Math.round(Math.random() * 10000);
						openEleWindow("./saverptfile/home/Product/Art work Briefing_"
								+ parent.$('orderNo').value + ".pdf?tmp=" + rdm);
					} else {
						Ext.MessageBox.alert("Message",
								'Please PO have not be Approved!');
						return;
					}
				});
	}
	
	// 生成报表
	function createReport(){
		mask();
		cotOrderFacService.createArtWork($('mainId').value, function(res) {
					unmask();
					Ext.MessageBox.alert("Message",
								'Report Created!');
				});
	}

});
