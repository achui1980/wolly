// 询盘记录表格导入
PanEleGrid = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	// 加载币种表缓存
	DWREngine.setAsync(false);
	var curData;
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "currencyId"
				}, {
					name : "modDate"
				}, {
					name : "modPerson"
				}, {
					name : "state"
				}, {
					name : "panId"
				}, {
					name : "eleId"
				}, {
					name : "size"
				}, {
					name : "boxCount"
				}, {
					name : "printed"
				}, {
					name : "dyed"
				}, {
					name : "yarnDyed"
				}, {
					name : "others"
				}, {
					name : "packaging"
				}, {
					name : "pack"
				}, {
					name : "productTime"
				}, {
					name : "canPrice"
				}, {
					name : "panPrice"
				}, {
					name : "material"
				}, {
					name : "construction"
				}, {
					name : "fillingMaterial"
				}, {
					name : "fillingWeight"
				}, {
					name : "colorRemark"
				}, {
					name : "packagingRemark"
				}, {
					name : "packRemark"
				}, {
					name : "priceRemark"
				}, {
					name : "canCurId"
				}, {
					name : "targetCurId"
				}, {
					name : "targetPrice"
				}, {
					name : "manufactorer"
				},{
					name:"eleNameEn"
				}, {
					name : "prsNo"
				}, {
					name : "priceNo"
				},{
					name : "manufactorer"
				},{
					name : "addTime"
				},{
					name : "empsName"
				},{
					name : "deptName"
				}]);
	// 创建数据源
	var ds = new Ext.data.Store({
		baseParams : {
			limit : 15,
			factoryIdFind:cfg.factoryId
		},
		proxy : new Ext.data.HttpProxy({
			url : "cotquery.do?method=queryPanAndEle"
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
						},{
							header : "Date",
							dataIndex : "addTime",
							width : 80
						},{
							header : "Request by",
							dataIndex : "empsName",
							width : 80
						},{
							header : "Department",
							dataIndex : "deptName",
							width : 80
						},{
							header : "Title",
							dataIndex : "priceNo",
							width : 80
						}, {
							header : "Prs No",
							dataIndex : "prsNo",
							width : 100
						},{
							header : "Manufactorer",
							dataIndex : "manufactorer",
							width : 100
						},{
							header : "W&C Art No.",
							dataIndex : "eleId",
							width : 80
						},{
							header : "Art Name",
							dataIndex : "eleNameEn",
							width : 120
						}, {
							header : "Price",
							width : 80,
							dataIndex : "panPrice",
							renderer : function(value) {
								return "<font color=red>"+value+"</font>";
							}
						}, {
							header : "Currency",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								if(value!=0){
									return "<font color=red>"+curData["" + value]+"</font>";
								}
							}
						}]
			});
			
		// 厂家
//	var facBox = new BindCombox({
//				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
//				cmpId : 'factoryIdFind',
//				editable : true,
//				valueField : "id",
//				displayField : "shortName",
//				emptyText : 'Supplier',
//				mode : 'remote',// 默认local
//				pageSize : 5,
//				width:100,
//				allowBlank : true,
//				sendMethod : "post",
//				selectOnFocus : false,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all',
//				isSearchField : true,
//				searchName : 'factoryIdFind',
//				listeners:{
//					"afterbindvalue":function(combo,value){
//						tb.searchField.onTrigger2Click();
//						ds.load();
//					}
//				}
//			});
			
	// 样品表格顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
				items : [{
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "d/m/Y",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "End Date",
							width : 90,
							format : "d/m/Y",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						},{
							xtype : 'bindCombo',
							dataUrl : "./servlet/DataSelvert?tbname=CotDept",
							valueField : "id",
							sendMethod : "post",
							displayField : "deptName",
							cmpId : "deptId",
							width : 95,
							emptyText : 'Department',
							isSearchField : true,
							searchName : 'deptIdFind'
						},{
							xtype : 'bindCombo',
							dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
							cmpId : 'addPerson',
							editable : true,
							valueField : "id",
							displayField : "empsName",
							emptyText : 'Request by',
							pageSize : 10,
							width : 95,
							sendMethod : "post",
							selectOnFocus : true,
							minChars : 1,
							listWidth : 350,
							triggerAction : 'all'
						},{
							xtype : 'textfield',
							width : 95,
							emptyText : "W&C Art No.",
							isSearchField : true,
							searchName : 'eleIdFind'
						},{
							xtype : 'textfield',
							width : 95,
							emptyText : "Manufactorer",
							isSearchField : true,
							searchName : 'manufactorer'
						},{
							xtype : 'textfield',
							width : 80,
							emptyText : "Art Name",
							isSearchField : true,
							searchName : 'eleNameFind'
						},{
							xtype : 'textfield',
							width : 80,
							emptyText : "Title",
							isSearchField : true,
							searchName : 'priceNoFind'
						},{
							xtype : 'searchcombo',
							width : 80,
							emptyText : "Prs No.",
							isSearchField : true,
							searchName : 'prsNo',
							isJsonType : false,
							store : ds
						},'->', {
							text : "Import",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function(){
								var list = getIds();
								if (list.length == 0) {
									Ext.MessageBox.alert("Message", 'Please select a record!');
									return;
								}
								mask();
								setTimeout(function(){
									insert();
								}, 500);
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|all',
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				border:false,
				viewConfig : {
					forceFit : false
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
		var ary = sm.getSelections();
		var ids='';
		Ext.each(ary, function(res) {
				ids+=res.data.id+",";
				});
		//查找所有报价明细
		DWREngine.setAsync(false);
		queryService.findPanEleByIds(ids,function(res){
			if(res.length!=0){
				for(var i=0;i<res.length;i++){
					if(!res[i].eleId){
						Ext.each(ary, function(record) {
							if(record.data.id==res[i].id){
								res[i].eleId=record.data.prsNo+"_"+Math.round(Math.random() * 10000);
								return true;
							}
						});
					}
					cfg.bar.insertByBatch(res[i],'panEle');
				}
			}
		});
		DWREngine.setAsync(true);
//		if(cfg.type=='order'){
//			cfg.bar.priceBlur();
//		}
		unmask();
	}
	
	this.load = function() {
//		facBox.bindPageValue("CotFactory", "id", cfg.factoryId);
		ds.load({
					params : {
						start : 0
					}
				});
	}

	// 表单
	var con = {
		layout : 'fit',
		width : 800,
		height : 490,
		border:false,
		items : [grid]
	};

	Ext.apply(con, cfg);
	PanEleGrid.superclass.constructor.call(this, con);
};
Ext.extend(PanEleGrid, Ext.Panel, {});
