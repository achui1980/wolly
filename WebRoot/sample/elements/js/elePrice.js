Ext.onReady(function() {

			var facMap;
			baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn",
					function(res) {
						facMap = res;
					});
			// 币种下拉框
			var combox = new BindCombox({
						dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
						name : 'priceUnit',
						cmpId : "priceUnit",
						fieldLabel : "Supplier",
						autoLoad:true,
						valueField : "id",
						displayField : "curNameEn",
						emptyText : 'Choose',
						anchor : "100%",
						selectOnFocus : true,
						minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
						allowBlank : false,
						blankText : 'Currency must not be null！',
						triggerAction : 'all'

					});

			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "priceName"
					}, {
						name : "priceAmount",
						type : "float"
					}, {
						name : "priceUnit"
					}, {
						name : "remark"
					}, {
						name : "eleId",
						type : "int"
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
										read : "coteleprice.do?method=query&mainId="
												+ $('mainId').value,
										create : "coteleprice.do?method=add",
										update : "coteleprice.do?method=modify",
										destroy : "coteleprice.do?method=remove"
									},
									listeners : {
										exception : function(proxy, type,
												action, options, response, arg) {
											ds.reload();
											refresh();

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

			// Ext.ux.grid.GroupSummary.Calculations['totalCost'] = function(v,
			// record, field){
			// alert("ddd");
			// return v + (record.data.priceAmount);
			// };
			var summary = new Ext.ux.grid.GridSummary();
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
									header : "Name Of Cost",
									dataIndex : "priceName",
									width : 200,
									editor : new Ext.form.TextField({
												maxLength : 50
											}),
									summaryRenderer : function(v, params, data) {
										return "Total：";
									}
								}, {
									header : "Price",
									dataIndex : "priceAmount",
									width : 100,
									editor : new Ext.form.NumberField({
												maxValue : 999.999,
												decimalPrecision : 3
											}),
									summaryType : 'sum'
								}, {
									header : "Currency",
									dataIndex : "priceUnit",
									width : 100,
									editor : combox,
									renderer : function(v) {
										return facMap["" + v]
									}
								}, {
									header : "Remark",
									dataIndex : "remark",
									width : 400,
									editor : new Ext.form.TextArea({
												width : 400
											})
								}]
					});

			var tb = new Ext.Toolbar({
						items : ['->', '-', {
									text : "Create",
									iconCls : 'page_add',
									handler : addNewGrid
								}, '-', {
									text : "Delete",
									iconCls : 'page_del',
									handler : removeGrid
								}, '-', {
									text : "Save",
									iconCls : 'page_table_save',
									handler : saveGrid
								}, '-', {
									text : "Import",
									iconCls : 'importEle',
									handler : inputCostName
								}]
					});

			var toolBar = new Ext.PagingToolbar({
						pageSize : 1000,
						store : ds,
						displayInfo : true,
						displayMsg : 'Displaying {0} - {1} of {2}',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display"
					});
			var grid = new Ext.grid.EditorGridPanel({
						border : false,
						region : "center",
						id : "priceGrid",
						stripeRows : true,
						store : ds, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						plugins : summary,
						loadMask : true, // 是否显示正在加载
						tbar : tb,
						bbar : toolBar,
						viewConfig : {
							forceFit : false
						}
					});
			grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
						// 获得view
						var view = grid.getView();
						// 获得单元格
						var cell = view.getCell(rowIndex, columnIndex);
						// 获得该行高度
						var row = view.getRow(rowIndex);
						var editor = cm.getCellEditor(columnIndex, rowIndex);
						editor.setSize(cell.offsetWidth, row.scrollHeight);
					});

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [grid]
					});

			ds.load({
						params : {
							start : 0,
							limit : 1000
						}
					});
			function addNewGrid() {
				var currencyId = parent.$('priceFacUint').value;
//				DWREngine.setAsync(false);
//				if (currencyId == null) {
//					cotElePriceService.getList('CotPriceCfg', function(res) {
//								if (res.length != 0
//										&& res[0].facPriceUnit != null) {
//									currencyId = res[0].facPriceUnit;
//								}
//							});
//				}
//				DWREngine.setAsync(true);
				var u = new ds.recordType({
							priceName : "",
							priceAmount : "",
							priceUnit : currencyId,// combox.bindValue(currencyId),
							eleId : $('mainId').value,
							remark : ""
						})
				ds.add(u);
				// 货号获得焦点
				var cell = grid.getView().getCell(ds.getCount() - 1, 2);
				if (Ext.isIE) {
					cell.fireEvent("ondblclick");
				} else {
					var e = document.createEvent("Events");
					e.initEvent("dblclick", true, false);
					cell.dispatchEvent(e);
				}
			}

			function saveGrid() {
				ds.save();
			}
			function removeGrid() {
				var arr = grid.getSelectionModel().getSelections();
				if (arr.length == 0) {
					Ext.Msg.alert("Message", "Please select a record");
					return;
				}
				Ext.MessageBox.confirm('Message','are you sure to delete all selected items?',function(btn){
					if(btn=='yes'){
						Ext.each(arr, function(r) {
								ds.remove(r);
							})
						ds.save();
					}
				})
			}
			function refresh() {
				// 刷新主页面的生产价
				cotElePriceService.modifyPriceFacByEleId(parent.$('eId').value,
						function(res) {
							parent.priceMoney = res[0];
							parent.$('priceFac').value = res[2];
						});
			}
			function inputCostName(){
				priceNames = ''
				var costNameStr;
				var costName;
				var currencyId = "";
				DWREngine.setAsync(false);
				cotElementsService.getDefaultList(function(res) {
					if(res != null){
						costNameStr = res[0].costName;
						currencyId = res[0].elePriceFacUnit;
					}
				});
				DWREngine.setAsync(true);
				if(costNameStr != null && costNameStr != ''){
					costName = costNameStr.split(";");
					var lastChar = costNameStr.lastIndexOf(";");
					var strLength;
					if(costNameStr.length == parseInt(lastChar)+1){
						strLength = costName.length-1;
					}else{
						strLength = costName.length;
					}
					var obj = new Object();
					for(i=0;i<strLength;i++){  
						if(ds.findExact("priceName",costName[i]) != -1) continue;
						var u = new ds.recordType({
							priceName : costName[i],
							priceAmount : 0.0,
							priceUnit : currencyId,// combox.bindValue(currencyId),
							eleId : $('mainId').value,
							remark : ""
						})
						ds.add(u);
					}
				}else{
					Ext.Msg.alert('消息提示','样品默认配置中还没有设置成本名称,不能导入!');
				}
				
			}
		});
