EleTablePanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "eleName"
			}, {
				name : "eleFlag"
			}, {
				name : "eleUnitNum",
				type : "int"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryEle"
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
				header : "ID", // 表头
				dataIndex : "id",
				sortable : true,
				hidden : true
			}, {
				header : "Art No.", // 表头
				dataIndex : "eleId",
				width : 130,
				sortable : true
			}, {
				header : "Name-cn", // 表头
				dataIndex : "eleName",
				width : 140,
				sortable : true
			}, {
				header : "Flag", // 表头
				dataIndex : "eleFlag",
				sortable : true,
				hidden:true,
				width : 50,
				renderer : function(value) {
					if (value == 1) {
						return "套件"
					}
					if (value == 0 || value == 2) {
						return "单件"
					}
					if (value == 3) {
						return "组件"
					}
				}
			}, {
				header : "Pieces", // 表头
				dataIndex : "eleUnitNum",
				width : 50,
				sortable : true
			}]);

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Create",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler:insert
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 100,
				store : ds,
				displayInfo : true,
				//displayMsg : '共{2}条记录',
				displaySize : '100|200|300|all'
				//emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				cls:"leftBorder rightBorder",
				stripeRows : true,
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

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryIdFind',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				pageSize : 5,
				anchor : "86%",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 材质
	var typeLvBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1Find',
				fieldLabel : "Material",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 5,
				anchor : "86%",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 产品分类
	var typeLv2Box = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Categories",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 5,
				anchor : "86%",
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 表单
	var form = new Ext.form.FormPanel({
		labelWidth : 60,
		region : 'center',
		labelAlign : "right",
		layout : "form",
		autoScroll : true,
		bodyStyle : "overflow-x:hidden;",
		border : false,
		buttonAlign : 'center',
		keys:[{
			key:Ext.EventObject.ENTER,
			fn:function(){
				ds.reload();
			}
		}],
		buttons : [{
					text : "Search",
					iconCls : "page_sel",
					width : 60,
					handler : function() {
						ds.reload();
					}
				}, {
					text : "Reset",
					iconCls : "page_reset",
					width : 60,
					handler : function() {
						form.getForm().reset();
					}
				}],
		items : [{
			xtype : "checkbox",
			boxLabel : "只检索子货号",
			hidden:true,
			id : 'childFind',
			name : 'childFind',
			inputValue:"true",
			anchor : "86%",
			hideLabel : true,
			style : {
				marginLeft : '25px'
			},
			labelSeparator:" "
		},{
			xtype : "textfield",
			id : 'eleIdFind',
			name : 'eleIdFind',
			fieldLabel : "Art No.",
			anchor : "86%"
		}, {
			xtype : "textfield",
			fieldLabel : "name-cn",
			id : 'eleNameFind',
			name : 'eleNameFind',
			anchor : "86%"
		}, {
			xtype : "textfield",
			fieldLabel : "name",
			id : 'eleNameEnFind',
			name : 'eleNameEnFind',
			anchor : "86%"
		}, {
			xtype : "textfield",
			fieldLabel : "Design",
			id : 'eleColFind',
			name : 'eleColFind',
			anchor : "86%"
		}, {
			xtype : "numberfield",
			fieldLabel : "Year",
			id : 'eleTypenameLv2',
			name : 'eleTypenameLv2',
			anchor : "86%",
			maxValue : 9999
		}, facBox, typeLvBox, typeLv2Box, {
			xtype : "datefield",
			fieldLabel : "Create Date",
			anchor : "86%",
			id : 'startTime',
			name : 'startTime',
			vtype : 'daterange',
			endDateField : 'endTime'
		}, {
			xtype : "datefield",
			fieldLabel : "-------",
			anchor : "86%",
			id : 'endTime',
			name : 'endTime',
			vtype : 'daterange',
			labelSeparator : " ",
			startDateField : 'startTime'
		}]
	});

	// 单击修改信息 start
	grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				$("picPathSel").src = "./showPicture.action?flag=ele&elementId="
						+ record.data.id;
				DrawImage($("picPathSel"),135,120);
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
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("Message", 'Please select a record');
			return;
		}
		var ary = sm.getSelections();
		cfg.bar.insertSelect(ary);
		Ext.Msg.alert('Message','Successfully added');
	}

	this.load = function() {
		// 加载数据
		// 将查询面板的条件加到ds中
		ds.on('beforeload', function(store, options) {
					ds.baseParams = form.getForm().getValues();
				});
		// 加载表格
		ds.load({
					params : {
						start : 0,
						limit : 15
					}
				});
	}
	
	var picPnl=new Ext.Panel({
			region:'north',
			height : 135,
			layout : "hbox",
			autoScroll : true,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			items : [{
				xtype : "panel",
				width:135,
				html : '<div align="center" style="width: 135px; height: 120px;">'
						+ '<img src="common/images/zwtp.png" id="picPathSel" name="picPathSel"'
						+ 'onload="javascript:DrawImage(this,135,120)" /></div>'
			}]
		});
	
	var leftPnl=new Ext.Panel({
		title:"Query",
		margins:"0 5 0 0",
		collapsible:true,
		region:'west',
		layout:'border',
		frame:true,
		width : 200,
		items:[
			picPnl,form
		]
	});

	// 表单
	var con = {
		title : 'Choose Art No.',
		layout : 'border',
		width : 600,
		height : 490,
		frame : true,
		//border : true,
		padding : "0",
		tools : [{
					id : "close",
					handler : function(event, toolEl, p) {
						p.hide();
					}
				}],
		items : [grid, leftPnl]
	};

	// 第一次弹出时.如果没printDiv,则新建
	this.printName = 'eleTableDiv';
	if ($('eleTableDiv') == null) {
		// window默认在z-index为9000
		Ext.DomHelper.append(document.body, {
					html : '<div id="'
							+ this.printName
							+ '" style="position:absolute;z-index:8000;"></div>'
				}, true);
	}

	Ext.apply(con, cfg);
	EleTablePanel.superclass.constructor.call(this, con);
};
Ext.extend(EleTablePanel, Ext.Panel, {});
