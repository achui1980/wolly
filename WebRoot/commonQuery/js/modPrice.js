ModPriceWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var facMap;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				facMap = res;
			});
	// 币种下拉框
	var combox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				name : 'priceUnit',
				cmpId : "priceUnit",
				sendMethod : 'GET',
				hideLabel : true,
				labelSeparator : " ",
				//editable : true,
				autoLoad:true,
				valueField : "id",
				displayField : "curNameEn",
				emptyText : 'Choose',
				anchor : "100%",
				selectOnFocus : true,
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				allowBlank : false,
				blankText : 'Currency can not be empty!',
				triggerAction : 'all'

			});

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "modNum"
			}, {
				name : "price",
				type : "float"
			}, {
				name : "currencyId"
			}, {
				name : "remark"
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
								read : "cotmodprice.do?method=query&custId="+cfg.custId+"&eId="+cfg.eleId,
								create : "cotmodprice.do?method=add&custId="+cfg.custId+"&eId="+cfg.eleId,
								update : "cotmodprice.do?method=modify",
								destroy : "cotmodprice.do?method=remove"
							},
							listeners : {
								exception : function(proxy, type, action,
										options, response, arg) {
									ds.reload({params : {
										start : 0,
										limit : 20
									}});
									//refresh();

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
	var cm = new Ext.grid.ColumnModel([sm, {
				header : "ID",
				dataIndex : "id",
				width : 50,
				sortable : true,
				hidden : true
			}, {
				header : "Qty",
				dataIndex : "modNum",
				width : 80,
				editor : new Ext.form.NumberField({
							maxValue : 99999999,
							decimalPrecision :0
						}),
				sortable : true
			}, {
				header : "Price",
				dataIndex : "price",
				width : 80,
				editor : new Ext.form.NumberField({
							maxValue : 999999.99,
							decimalPrecision : 2
						}),
				sortable : true
			}, {
				header : "Currency",
				dataIndex : "currencyId",
				width : 90,
				editor : combox,
				sortable : true,
				renderer : function(v) {
					return facMap["" + v]
				}
			}, {
				header : "Remark",
				dataIndex : "remark",
				width : 150,
				editor : new Ext.form.TextArea({
							width : 500
						}),
				sortable : true
			}]);

	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "Create",
							iconCls : 'page_add',
							handler : addNewGrid
						}, '-', {
							text : "Del",
							iconCls : 'page_del',
							handler : onDel
						}, '-', {
							text : "Save",
							iconCls : 'page_mod',
							handler : function(){
								ds.save();
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true,
				////displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all'
				//emptyMsg : "No data to display"
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
			
	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				//获得view
				var view = grid.getView();
				//获得单元格
				var cell=view.getCell(rowIndex,columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	ds.load({
				params : {
					start : 0,
					limit : 20
				}
			});
	var currencyId = null;// 默认币种ID
	function addNewGrid() {
		DWREngine.setAsync(false);
		if (currencyId == null) {
			//默认调用该订单的主单币种,如果该订单没主单币种取业务配置的币种
			if(cfg.facCur==null || cfg.facCur==0){
				queryService.getList('CotPriceCfg', function(res) {
							if (res.length != 0 && res[0].currencyId != null) {
								currencyId = res[0].currencyId;
							}
						});
			}else{
				currencyId = cfg.facCur;
			}
		}
		var u = new ds.recordType({
					modNum : "",
					price : "",
					currencyId : currencyId,
					remark : ""
				})
		ds.add(u);
		DWREngine.setAsync(true);
	}

	// 删除
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					ds.remove(item);
				});
	}
	
	var con = {
		width : 460,
		height : 300,
		title : "Price",
		modal : true,
		id : "modPriceWin",
		layout:'fit',
		items : [grid]
	}
	Ext.apply(con, cfg);
	ModPriceWin.superclass.constructor.call(this, con);
};

Ext.extend(ModPriceWin, Ext.Window, {});