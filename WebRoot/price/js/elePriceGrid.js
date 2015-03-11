// 报价记录表格导入报价单
ElePriceGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	var reflashFn = cfg.reflashFn;
	var facMap;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				facMap = res;
			});
	// 币种下拉框
	var combox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				name : 'priceUnit',
				cmpId : "priceUnit",
				sendMethod : 'GET',
				fieldLabel : "Currency",
				// mode:"local",
				editable : true,
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
								read : "cotprice.do?method=queryPriceElePrice"
										+ "&rdm=" + cfg.rdm,
								create : "cotprice.do?method=addElePrice&pFId="
										+ cfg.isHId + "&oId=" + cfg.pId,
								update : "cotprice.do?method=modifyElePrice&pFId="
										+ cfg.isHId + "&oId=" + cfg.pId,
								destroy : "cotprice.do?method=removeElePrice"
							},
							listeners : {
								exception : function(proxy, type, action,
										options, response, arg) {
									ds.reload();
									reflashFn();
									Ext.MessageBox.alert('Message','Successfully saved!');
									//Ext.getCmp('winpanel').close();
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
							header : "Cost name",
							dataIndex : "priceName",
							width : 200,
							summaryRenderer : function(v, params, data) {
								params.css = 'fg';
								return "Total：";
							},
							editor : new Ext.form.TextField({
										maxLength : 50,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "Price",
							dataIndex : "priceAmount",
							width : 100,
							editor : new Ext.form.NumberField({
										maxValue : 999.999,
										decimalPrecision : 3
									}),
							summaryType : 'sum'
							// summaryRenderer : Ext.util.Format.usMoney
					}	, {
							header : "Currency",
							dataIndex : "priceUnit",
							width : 100,
							editor : combox,
							renderer : function(v) {
								return facMap["" + v]
							}
						}, {
							header : "Remarks",
							dataIndex : "remark",
							width : 400,
							editor : new Ext.form.TextArea({
										width : 400
									})
						}]
			});

	var tb = new Ext.Toolbar({
				items : ['->', '-', {
							text : "New",
							iconCls : 'page_add',
							handler : addNewGrid
						}, '-', {
							text : "Delete",
							iconCls : 'page_del',
							handler : removeGrid
						}, '-', {
							text : "Save",
							iconCls : 'page_table_save',
							handler : save
						}],
				listeners : {
					'render' : function(tbar) {
						// 如果是新增行时,隐藏顶部工具栏
						if (cfg.isHId == null) {
							tbar.hide();
						}
					}
				}
			});
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Record of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No records"
			});

	// 合计行
	var summary = new Ext.ux.grid.GridSummary();
	var grid = new Ext.grid.EditorGridPanel({
				border : true,
				region : "center",
				id : "priceGrid",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				plugins : [summary],
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	grid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				// 获得该行高度
				var row = grid.getView().getRow(rowIndex);
				// 让编辑控件textarea适应行高度
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(50, row.scrollHeight);
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

	// 配件添加/修改/删除/确定按钮事件
	function save() {
		if (cfg.priceStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message','Sorry, the single has been reviewed parts can not be saved!');
			return;
		}
		var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
		if (isPopedom == 0) {// 没有删除权限
			Ext.MessageBox.alert('Message',"Sorry, you do not have Authority! Pls. contact the webmaster!");
			return;
		}
		ds.save();
	}
	this.load = function() {
		// 将查询面板的条件加到ds中
		ds.on('beforeload', function(store, options) {
					// if ($("custId").value == '') {
					// ds.baseParams.custId = _self.custId;
					// }
				});
		// 加载表格
		ds.load({
					params : {
						start : 0,
						limit : 15
					}
				});

	}
	var currencyId = null;
	function addNewGrid() {
		if (cfg.priceStatus == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('Message','Sorry, this one has the additional cost can not be audited!');
			return;
		}
		var isPopedom = getPdmByOtherUrl("cotprice.do", "MOD");
		if (isPopedom == 0) {// 没有删除权限
			Ext.MessageBox.alert('Message',"Sorry, this one has the additional cost can not be audited!");
			return;
		}
		DWREngine.setAsync(false);
		if (currencyId == null) {
			//默认调用该订单的生产价币种,如果该订单没生产价币种取业务配置的生产价币种
			if(cfg.facCur==null || cfg.facCur==0){
				cotPriceService.getList('CotPriceCfg', function(res) {
							if (res.length != 0 && res[0].facPriceUnit != null) {
								currencyId = res[0].facPriceUnit;
							}
						});
			}else{
				currencyId = cfg.facCur;
			}
		}
		var u = new ds.recordType({
					priceName : "",
					priceAmount : "",
					priceUnit : currencyId,// combox.bindValue(currencyId),
					eleId : cfg.eleId,
					remark : ""
				})
		ds.add(u);
		DWREngine.setAsync(true);
	}
	function removeGrid() {
		var arr = grid.getSelectionModel().getSelections();
		if (arr.length == 0) {
			Ext.MessageBox.alert('Message',"Please select records");
			return;
		}
		Ext.MessageBox.confirm('Message','Sure to delete the option?',function(btn){
			if(btn=='yes'){
				Ext.each(arr, function(r) {
						ds.remove(r);
					})
				ds.save();
			}
		});
	}

	// 表单
	var con = {
		layout : 'border',
		width : 600,
		height : 490,
		border : false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	ElePriceGrid.superclass.constructor.call(this, con);
};
Ext.extend(ElePriceGrid, Ext.Panel, {});
