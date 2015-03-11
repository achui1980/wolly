EleQueryWin = function(cfg) {
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
				name : "eleSizeDesc"
			}, {
				name : "eleInchDesc"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryElements"
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
				sortable : true,
				hidden : true
			}, {
				header : "产品货号", 
				dataIndex : "eleId",
				width : 100,
				sortable : true
			}, {
				header : "中文名称", 
				dataIndex : "eleName",
				width : 140,
				sortable : true
			}, {
				header : "中文规格", 
				dataIndex : "eleSizeDesc",
				sortable : true,
				width : 140
			}, {
				header : "英文规格", 
				dataIndex : "eleInchDesc",
				sortable : true,
				width : 140
			}]);

	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "导入",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function(){
								//mask();
								setTimeout(function(){
									insert();
								}, 500);
							}
						}]
			});

	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : '共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				cls : "leftBorder rightBorder",
				stripeRows : true,
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				// split : true,
				viewConfig : {
					forceFit : false
				}
			});

	// 厂家
	var facBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryIdFind',
				fieldLabel : "厂家",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : '请选择',
				mode : 'remote',// 默认local
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
				fieldLabel : "材质",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				mode : 'remote',// 默认local
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
				fieldLabel : "产品分类",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : '请选择',
				mode : 'remote',// 默认local
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
				keys : [{
							key : Ext.EventObject.ENTER,
							fn : function() {
								ds.reload();
							}
						}],
				buttons : [{
							text : "查询",
							iconCls : "page_sel",
							width : 60,
							handler : function() {
								ds.reload();
							}
						}, {
							text : "重置",
							iconCls : "page_reset",
							width : 60,
							handler : function() {
								form.getForm().reset();
							}
						}],
				items : [{
							xtype : "checkbox",
							boxLabel : "只检索子货号",
							id : 'childFind',
							name : 'childFind',
							inputValue : "true",
							anchor : "86%",
							hideLabel : true,
							style : {
								marginLeft : '25px'
							},
							labelSeparator : " "
						}, {
							xtype : "textfield",
							id : 'eleIdFind',
							name : 'eleIdFind',
							fieldLabel : "货号",
							anchor : "86%"
						},{
							xtype : "textfield",
							fieldLabel : "中文名称",
							id : 'eleNameFind',
							name : 'eleNameFind',
							anchor : "86%"
						}, {
							xtype : "textfield",
							fieldLabel : "英文名称",
							id : 'eleNameEnFind',
							name : 'eleNameEnFind',
							anchor : "86%"
						},  {
							xtype : "textfield",
							fieldLabel : "颜色",
							id : 'eleColFind',
							name : 'eleColFind',
							anchor : "86%"
						}, {
							xtype : "numberfield",
							fieldLabel : "所属年份",
							id : 'eleTypenameLv2Find',
							name : 'eleTypenameLv2Find',
							anchor : "86%",
							maxValue : 9999
						},{
							xtype : "textarea",
							fieldLabel : "中文规格",
							id : 'eleSizeDescFind',
							name : 'eleSizeDescFind',
							height:30,
							anchor : "86%"
						},{
							xtype : "textarea",
							fieldLabel : "英文规格",
							height:30,
							id : 'eleInchDescFind',
							name : 'eleInchDescFind',
							anchor : "86%"
						}, facBox, typeLvBox, typeLv2Box, {
							xtype : "datefield",
							fieldLabel : "创建日期",
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
				DrawImage($("picPathSel"), 135, 120);
			});

	// 获得选择的记录
	var getIds = function() {
		var res = new Array();
		var list = null;
		var isPicMode = Ext.getCmp("chkPicMode").getValue();
		if(isPicMode){
			 list = picView.getSelectedRecords();
		}else {
		 list = sm.getSelections();
		}
		Ext.each(list, function(item) {
			res.push(item.id);
		});
		return res;
	}

	// 添加
	function insert() {
		var list = getIds();
		if (list.length == 0) {
			Ext.MessageBox.alert("提示信息", '请选择记录！');
			return;
		}
		var ary = null;
		var isPicMode = Ext.getCmp("chkPicMode").getValue();
		if(isPicMode){
			ary = picView.getSelectedRecords();
		}else {
		 	ary = sm.getSelections();
		}
		cfg.bar.insertSelect(ary,'ele');
	}

	var picPnl = new Ext.Panel({
		region : 'north',
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
			width : 135,
			html : '<div align="center" style="width: 135px; height: 120px;">'
					+ '<img src="common/images/zwtp.png" id="picPathSel" name="picPathSel"'
					+ 'onload="javascript:DrawImage(this,135,120)" /></div>'
		}]
	});

	var leftPnl = new Ext.Panel({
				title : "查询",
				margins : "0 5 0 0",
				collapsible : true,
				region : 'west',
				layout : 'border',
				frame : true,
				width : 180,
				items : [picPnl, form]
			});

	// 直接把该货号添加到表格
	var opForm = new Ext.form.FormPanel({
				region : 'north',
				height : 40,
				padding : '5',
				frame : true,
				layout : 'form',
				items : [{
							xtype : 'radiogroup',
							fieldLabel : "",
							hideLabel : true,
							columns : [190, 80, 50,20, 150,80],
							items : [{
										boxLabel : '同时添加到样品资料,从货号',
										inputValue : 0,
										style : "marginLeft:8",
										name : 'noEleRdo',
										id : "noEleRdo1",
										checked : true
									},  {
										xtype : "textfield",
										id : 'canEle',
										name : 'canEle',
										labelSeparator : " ",
										fieldLabel : "",
										hideLabel : true,
										listeners:{
											"specialkey" : function(txt, eObject) {
												var temp = txt.getValue();
												if (temp != ""
														&& eObject.getKey() == Ext.EventObject.ENTER) {
													openNewEleWin();
												}
											}
										}
									}, {
										xtype : "button",
										text : '添加',
										handler:openNewEleWin
									}, {
										xtype : "label",
										text : ''
									}, {
										boxLabel : '只添加到该单据中',
										id : "noEleRdo2",
										inputValue : 1,
										name : 'noEleRdo',
										listeners:{
											'check':function(chk,check){
												if(check){
													cfg.bar.noEleAsNew(cfg.eleIdFind);
													_self.close();
												}
											}
										}

									},{
										xtype:"checkbox",
										boxLabel : '图片模式',
										id : "chkPicMode",
										inputValue : 1,
										disabled:true,
										name : 'chkPicMode',
										listeners:{
											'check':function(chk,check){
												changeToPicMode();
											}
										}
									}]
						}]
			});
	/***********************图片模式*******************************************/
	//创建图片数据源
	var showPicDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "cotquery.do?method=queryElements"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
	});
	var toolbar_pic = new Ext.PagingToolbar({
		pageSize : 10,
		store : showPicDs,
		displayInfo : true,
		//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
		displaySize : '5|10|15|20|all',
		emptyMsg : "No data to display"
	})
	var thumbTemplate = new Ext.XTemplate(
		'<tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb"><img src="showPicture.action?flag=ele&elementId={id}" onclick="showBigPicDiv(this)" onload="DrawImage(this,150,150)"></div>',
			'<span>{eleId}</span>' +
			'</div>',
		'</tpl>'
	);
	thumbTemplate.compile();
	 var picView = new Ext.DataView({
	 			id:"picView",
				tpl: thumbTemplate,
				multiSelect   :true,
				autoScroll : true,
				bodyStyle : "overflow-x:hidden;",
				overClass:'x-view-over',
				itemSelector: 'div.thumb-wrap',
				emptyText : '<div style="padding:10px;">没有记录</div>',
				plugins : new Ext.DataView.DragSelector(),
				store: showPicDs
	});
		// 样品表格顶部工具栏
	var tb_pic = new Ext.Toolbar({
				items : ['->',{
							text : "全选／反选",
							handler : selectAllPic,
							iconCls : "gird_save"
						},'-',{
							text : "导入",
							iconCls : "page_add",
							cls : "SYSOP_ADD",
							handler : function() {
								mask();
								setTimeout(function() {
											insert();
										}, 500);
							}
						}]
			});
	var panel_pic = new Ext.Panel({
		id:"img-chooser-view",
		tbar:tb_pic,
		bbar:toolbar_pic,
		frame:false,
		region:"center",
		layout : 'fit',
		items:[picView]
	});
	//切换到图片模式
	function changeToPicMode(){
	}
	var tbPnl = new Ext.TabPanel({
		region:"center",
		activeTab:0,
		items:[{
			title:"列表模式",
			id:"listMode",
			layout:"border",
			items:[grid]
		},{
			title:"图片模式",
			id:"picMode",
			autoScroll:true,
			layout:"border",
			items:[panel_pic]
		}]
	});
	
	/************************************************************************/
	var bottomPanel = new Ext.Panel({
				layout : 'border',
				region : 'center',
				border : false,
				items : [tbPnl, leftPnl]
			});
			
	// 前往样品添加页面
	function openNewEleWin() {
		if($('canEle').value==''){
			Ext.MessageBox.alert("提示消息","请填写样品模版货号!");
			return;
		}
		var url = './cotquery.do?method=addElements&newEleId=' + $('canEle').value
				+ '&wantEleId=' + cfg.eleIdFind+ '&parentType=' + cfg.bar.parentType;
		openMaxWindow(680,1024,url);
		_self.close();
	}
	
	//全选
	function selectAllPic() {
		var nodes=picView.getNodes();
		for (var i = 0; i <nodes.length; i++) {
			if(picView.isSelected(nodes[i])){
				picView.deselect(i);
			}else{
				picView.select(i, true);
			}
		}
	}

	// 表单
	var con = {
		title : '选择货号',
		layout : 'border',
		width : 800,
		height : 520,
		border : false,
		modal:true,
		maximizable:true,
		items : [opForm, bottomPanel],
		listeners:{
			'afterrender':function(win){
				tbPnl.items.get(0).on('activate',function(pnl){
					grid.setHeight(win.getHeight()-100);
					ds.reload();
					Ext.getCmp("chkPicMode").setValue(false);
				});
				tbPnl.items.get(1).on('activate',function(pnl){
					panel_pic.setHeight(win.getHeight()-100);
					Ext.getCmp("chkPicMode").setValue(true);
					showPicDs.on('beforeload', function(store, options) {
							showPicDs.baseParams = form.getForm().getValues();
							if(typeof(showPicDs.baseParams.childFind) == "undefined")
								showPicDs.baseParams.childFind = "false";
							if ($("eleIdFind").value == '') {
								showPicDs.baseParams.eleIdFind = cfg.eleIdFind;
							}
						});
					showPicDs.load({
							params : {
								start : 0,
								limit : 15
							}
						});
				});
				grid.setHeight(win.getHeight()-100);
				// 将查询面板的条件加到ds中
				ds.on('beforeload', function(store, options) {
							ds.baseParams = form.getForm().getValues();
							if(typeof(ds.baseParams.childFind) == "undefined")
								ds.baseParams.childFind = "false";
							if ($("eleIdFind").value == '') {
								ds.baseParams.eleIdFind = cfg.eleIdFind;
							}
						});
				// 加载表格
				ds.load({
							params : {
								start : 0,
								limit : 15
							}
						});
			}
		}
	};

	Ext.apply(con, cfg);
	EleQueryWin.superclass.constructor.call(this, con);
};
Ext.extend(EleQueryWin, Ext.Window, {});
