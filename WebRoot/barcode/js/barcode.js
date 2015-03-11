Ext.onReady(function() {
	var _self = this;
	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "eleUnitNum"
			}, {
				name : "count"
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
								read : "cotbarcode.do?method=query",
								create : "cotbarcode.do?method=add"
							},
							listeners : {
								exception : function(proxy, type, action, options, res, arg) {
									// 从异常中的响应文本判断是否成功
									if (res.status != 200) {
										Ext.Msg.alert("Message", "Save barcode fails, can not print!");
									} else {
										if(Ext.isEmpty($('flag').value)){
											viewRpt();
										}else{
											var printType=typeBox.getValue()
											var reportTemple=$('reportTemple').value;
											downRpt("./downBarcode.action?headerFlag=barcode&printType="+printType+"&reportTemple="+reportTemple);
										}
									}
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
				header : "ID", // 表头
				dataIndex : "id",
				sortable : true,
				hidden : true
			}, {
				header : "Item No.", // 表头
				dataIndex : "eleId",
				sortable : true
			}, {
				header : "Chinese name", // 表头
				dataIndex : "eleName",
				sortable : true
			}, {
				header : "Suite Number", // 表头
				dataIndex : "eleUnitNum",
				sortable : true
			}, {
				header : "<font color=blue>Print Counts</font>", // 表头
				dataIndex : "count",
				sortable : true,
				editable : true,
				editor : new Ext.form.NumberField({
							maxValue : 5000,
							decimalPrecision : 0
						})
			}]);

	var toolBar = new Ext.PagingToolbar({
				pageSize : 5,
				store : ds,
				displayInfo : true,
				displayMsg : 'Showing {0} - {1} {2} record total records',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No records"
			});

	// 打印模板
	var modelBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotRptFile&key=rptName&typeName=rptType&type=6",
				cmpId : 'reportTemple',
				fieldLabel : "Print Templates",
				editable : true,
				sendMethod : "post",
				valueField : "rptfilePath",
				displayField : "rptName",
				emptyText : 'Please select',
				width : 140
			});

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Check",
							iconCls : "page_jiao",
							handler:checkBarcode
						}, '-', {
							text : "Delete",
							iconCls : "page_del",
							handler : onDel
						}, '-', {
							xtype : "label",
							text : "Choose a template："
						}, modelBox, {
							text: "Export",
							iconCls : "page_excel",
							cls : "SYSOP_EXP",
							handler:exportData
						},{
							text : "打印",
							iconCls : "page_print",
							cls : "SYSOP_PRINT",
							handler : saveAndPrintBarcode
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				cls : 'rightBorder',
				stripeRows : true,
				clicksToEdit : 1,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				border:false,
				cls:'leftBorder',
				bbar : toolBar,
				split : true,
				viewConfig : {
					forceFit : true
				}
			});
			
	// 主表单
	var eleForm = new Ext.form.FormPanel({
				labelAlign : "right",
				layout : "form",
				padding : "5px",
				labelWidth : 70,
				buttonAlign : 'center',
				items : [{
					id : "eleInput",
					name : "eleInput",
					xtype : "textfield",
					fieldLabel : "Item No.",
					anchor : "100%",
					//allowBlank : false,
					//blankText : "Please enter the Item",
					listeners : {
						"specialkey" : function(field, e) {
							if (e.getKey() == Ext.EventObject.ENTER
									&& field.getValue() != "") {
								insertByInput();
							}
						},
						"render":function(obj){
							var tip = new Ext.ToolTip({
								target: obj.getEl(),
						        anchor: 'top',
						        maxWidth:160,
						        minWidth:160,
						        html: 'Input can be directly added after press enter!'
							});
						}
					}
				}],
				buttons : [{
							text : "Specify",
							iconCls : "page_add",
							width:60,
							handler : insertByInput

						}, {
							text : "Choose",
							iconCls : "gird_list",
							handler : showEleTable
						}]
			});

	/*----------------------------------------------------*/
	var form = new Ext.Panel({
		title : "Query and set",
		width : 200,
		height : 493,
		region : 'west',
		margins:'0 5 0 0',
		collapsible : true,
		padding:5,
		border:false,
		cls:'rightBorder',
		//bodyStyle:'background:#dfe8f6',
		ctCls:'x-panel-mc',
		items : [{
			xtype : "fieldset",
			title : "Sample Image",
			layout : "hbox",
			width : 180,
			height : 180,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			items : [{
				xtype : "panel",
				width : 140,
				html : '<div align="center" style="width: 135px; height: 132px;">'
						+ '<img src="common/images/zwtp.png"  id="picPath" name="picPath"'
						+ 'onload="javascript:DrawImage(this,135,132)" onclick="showBigPicDiv(this)"/></div>'
			}]
		}, {
			xtype : "fieldset",
			title : "Set",
			layout : "form",
			width : 180,
			labelWidth : 100,
			items : [{
						xtype : "checkbox",
						fieldLabel : "",
						boxLabel : "Suite Number",
						anchor : "100%",
						hideLabel : true,
						id : "isUnitNum",
						name : "isUnitNum",
						labelSeparator : " "
					}, {
						xtype : "numberfield",
						fieldLabel : "Default Counts",
						id : "defaultCount",
						name : "defaultCount",
						anchor : "100%",
						decimalPrecision : 0,
						maxLength : 100000000000000,
						value : 1
					}]
		}, {
			xtype : "fieldset",
			title : "Select Item",
			layout : "fit",
			width : 180,
			height:110,
			items : [eleForm]
		}]
	});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid, form]
			});
	viewport.doLayout();
	
	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				$("picPath").src = "./showPicture.action?flag=ele&elementId="
						+ record.data.id;
				DrawImage($("picPath"),135,120);
			});

	// 设置默认模板
	queryService.getRptDefault(6, function(def) {
				if (def != null) {
					modelBox.bindValue(def.rptfilePath);
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

	// 显示样品查询面板
	var eleTablePanel;
	function showEleTable(item, pressed) {
		if (eleTablePanel == null) {
			eleTablePanel = new EleTablePanel({
						bar : _self
					});
			var obj = Ext.fly(eleTablePanel.printName);
			var ary = obj.getCenterXY();
			obj.setX(ary[0] - 300);
			obj.setY(ary[1] - 250);
			eleTablePanel.render(obj);
			eleTablePanel.load();
			// var proxy1 = new Ext.dd.DDProxy(eleTablePanel.printName);
		} else {
			if (!eleTablePanel.isVisible()) {
				eleTablePanel.show();
				eleTablePanel.load();
			} else {
				eleTablePanel.hide();
			}
		}
	};

	// 删除表格数据
	function onDel() {
		var cord = sm.getSelections();
		Ext.each(cord, function(item) {
					ds.remove(item);
				});
	}

	// 根据输入的样品货号添加样品到可编辑表格
	function insertByInput() {
		var res = Ext.getCmp("eleInput").getValue();
		if (res == "") {
			Ext.MessageBox.alert("Message", "Please enter Item No.!");
			return;
		}
		var defaultCount = Ext.getCmp("defaultCount").getValue();
		if (defaultCount == "") {
			Ext.MessageBox.alert("Message", "Please enter the default number of prints!");
			return;
		}

		var flag = false;
		ds.each(function(rec) {
					if (rec.data.eleId.toLowerCase() == res.toLowerCase()) {
						flag = true;
						return;
					}
				});
		if (flag) {
			Ext.MessageBox.alert("Message", "The Item has been added!");
			return;
		}
		DWREngine.setAsync(false);
		// 通过货号获取套件数和中文名
		cotBarcodeService.getEleUnitNum(res, function(list) {
					if (list == null) {
						Ext.MessageBox.alert("Message", "The Item does not exist!");
					} else {
						insertToGrid(list);
					}
				});
		DWREngine.setAsync(true);
	}

	// 根据输入的样品货号添加样品到可编辑表格
	this.insertSelect = function(list) {
		var defaultCount = Ext.getCmp("defaultCount").getValue();
		if (defaultCount == "") {
			Ext.MessageBox.alert("Message", "Please enter the default number of prints!");
			return;
		}
		Ext.each(list, function(item) {
					var flag = false;
					ds.each(function(rec) {
								if (rec.data.eleId == item.data.eleId) {
									flag = true;
									return;
								}
							});
					if (!flag) {
						DWREngine.setAsync(false);
						// 通过货号获取套件数和中文名
						cotBarcodeService.getEleUnitNum(item.data.eleId,
								function(result) {
									if (result != null) {
										insertToGrid(result);
									}
								});
						DWREngine.setAsync(true);
					}
				});
	}

	// 插入表格
	function insertToGrid(list) {
		// 如果按套件数打印,则把套件数乘默认打印数量
		var chk = Ext.getCmp("isUnitNum").checked;
		var defaultCount = Ext.getCmp("defaultCount").getValue();
		var count = defaultCount;
		if (chk) {
			if (list[2] != null) {
				count = defaultCount * list[2];
			}
		}
		var u = new ds.recordType({
					eleId : list[0],
					eleName : list[1],
					eleUnitNum : list[2],
					id:list[3],
					count : count
				});
		ds.add(u);
		Ext.getCmp("eleInput").setValue("");
	}
	
	// 打印类型
	var printType = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['XLS', 'XLS'], ['PDF', 'PDF']]
			});
	var typeBox = new Ext.form.ComboBox({
				name : 'printType',
				id : 'printType',
				xtype : 'combo',
				fieldLabel : "Export Type",
				editable : false,
				store : printType,
				valueField : "id",
				value : "XLS",
				displayField : "name",
				mode : 'local',
				anchor : '95%',
				triggerAction : 'all',
				selectOnFocus : false
			});
    //导出
	function exportData(){
		if (ds.getCount() == 0) {
			Ext.MessageBox.alert("Message", "Please add Item!");
			return;
		}
		
		var formPanel = new Ext.form.FormPanel({
				layout : 'form',
				labelWidth : 60,
				frame : true,
				border : false,
				autoHeight : true,
				items :[typeBox]
		});
		var wind =new Ext.Window({
			title:'Export',
			width:300,
			autoHeight:true,
			layout:'fit',
			frame :true,
			closeAction:'hide',
			items:[formPanel],
			buttonAlign:'center',
			buttons:[{
				text:'Export',
				handler:function(){
					$('flag').value='export';
					ds.save();
					wind.hide();
				}
			},{
				text:'Cancel',
				handler:function(){
					wind.hide();
				}
			}]
		});
		wind.show();
	}
	
	// 保存并且打开预览页面
	function saveAndPrintBarcode() {
		if (ds.getCount() == 0) {
			Ext.MessageBox.alert("Message", "Please add Item!");
			return;
		}
		var model = $('reportTemple').value;
		if (model == '') {
			Ext.MessageBox.alert("Message", "Please select a print template!");
			return;
		}
		ds.save();
	}

	// 预览方法
	function viewRpt() {
//		var _height = window.screen.height;
//		var _width = window.screen.width;
		var model = $('reportTemple').value;
		openFullWindow("previewrpt.do?method=queryEleRpt&reportTemple=" + model
						+ "&headerFlag=barcode&childFind=none");
	}
	
	//校验
	function checkBarcode(){
		var isSuccess = true;
		var rec = new Array();
		ds.each(function(item) {
			DWREngine.setAsync(false);
			cotBarcodeService.checkBarcode(item.data.eleId,function(res){
				if(!res){
					isSuccess = false;
					rec.push(item);
				}	
			});
			DWREngine.setAsync(true);
		});
		if(!isSuccess){
			sm.selectRecords(rec);
			Ext.MessageBox.alert("Message", "No. samples contain illegal characters!\r\nDelete checked records to print bar codes, or modify the sample after the re-add the Item\r\nNo. samples only uppercase letters, numbers, spaces, and + - /. These characters");
		}else{
			Ext.MessageBox.alert("Message","Item no illegal characters, can continue to print bar codes");
		}
	}
});
