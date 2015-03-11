OrgHome = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	var empData;
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(
					res) {
				empData = res;
			});
	DWREngine.setAsync(true);
	
	// 业务员
	var busiBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotEmps&key=empsName",
				cmpId : 'businessPerson',
				editable : true,
				valueField : "id",
				displayField : "empsName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				selectOnFocus : true,
				sendMethod : "post",
				width : 120,
				emptyText : 'Uploader',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				isSearchField : true,
				searchName : 'empIdFind'
			});

	var hideAdd = false;
	var hideMod = false;
	var hideDel = false;
	DWREngine.setAsync(false);
	cotPopedomService.getLoginEmpId(function(empId) {
				if (empId != null && empId != 'admin') {
					cotPopedomService.getPopedomByMenu("cotorg.do", function(
									map) {
								if (!map || typeof(map.ADD) == "undefined") {
									hideAdd = true;
								}
								if (!map || typeof(map.MOD) == "undefined") {
									hideMod = true;
								}
								if (!map || typeof(map.DEL) == "undefined") {
									hideDel = true;
								}

							});
				}
			});
	DWREngine.setAsync(true);
	var questionRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "empId"
			}, {
				name : "uploadTime",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : "filePath"
			}, {
				name : "remark"
			}]);

	// 创建数据源
	var questionDs = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 20
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorg.do?method=query"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, questionRecord)
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
					header : "Uploader",
					dataIndex : "empId",
					width : 80,
					renderer : function(value) {
						return empData[value];
					}
				}, {
					header : "Upload Time",
					dataIndex : "uploadTime",
					width : 80,
					renderer : function(value) {
						if (value != null) {
							return Ext.util.Format.date(new Date(value.time),
									"d-m-Y H:i:s");
						}
					}
				}, {
					header : "File",
					dataIndex : "filePath",
					width : 100,
					renderer : function(value) {
						var fileName = value.substring(0, value.length - 13);
						var hou = value.substring(value.length - 4);
						var ph=encodeURIComponent("artWorkPdf/order/"+value);
						var pdf = "<a onclick=openPdf(\"./showOrg.action?filePath="
								+ ph
								+ "\") style='cursor: hand;text-decoration: underline;'><font color=blue>"
								+ fileName + hou + "</font></a>";
						
						var downUrl="./downPc.action?fileName="
								+ encodeURIComponent(fileName + hou)+'&filePath=' + ph;
						
						var download = "<img style='cursor:hand;' width=25 height=25  " +
								"src='common/ext/resources/images/extend/MinIcons_008.png' " +
								"title='DownLoad' onmouseover=max(this) onmouseout=min(this) " +
								"onclick=downRpt(\""+downUrl+"\")>";
						return pdf + "&nbsp;&nbsp;&nbsp;" + download;
					}
				}, {
					header : "Remark",
					dataIndex : "remark",
					width : 300
				}]
	});

	var pageBar = new Ext.PagingToolbar({
				pageSize : 20,
				store : questionDs,
				displayInfo : true,
				displaySize : '5|10|15|200|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var toolBar = new Ext.ux.SearchComboxToolbar({
				items : [{
							xtype : "datefield",
							emptyText : "Upload Start",
							width : 100,
							format : "d-m-Y",
							id : 'startTimeOrg',
							vtype : 'daterange',
							endDateField : 'endTimeOrg',
							isSearchField : true,
							// value : new Date(showdate(-1)),
							searchName : 'startTimeOrg'
						}, {
							xtype : "datefield",
							emptyText : "Upload End",
							width : 100,
							format : "d-m-Y",
							id : 'endTimeOrg',
							vtype : 'daterange',
							startDateField : 'startTimeOrg',
							isSearchField : true,
							searchName : 'endTimeOrg'
						},busiBox, {
							xtype : 'textfield',
							width : 100,
							emptyText : "File",
							isSearchField : true,
							searchName : 'filePathFind'
						}, {
							xtype : 'searchcombo',
							width : 100,
							emptyText : "Remark",
							isSearchField : true,
							searchName : 'remarkFind',
							isJsonType : false,
							store : questionDs
						}, '->', {
							text : "Import",
							iconCls : "page_add",
							hidden : hideAdd,
							handler : function() {
								openWindowBase(220, 400,
										'cotorg.do?method=orgEdit');
							}
						}, '-', {
							text : "Update",
							hidden : hideMod,
							iconCls : "page_mod",
							handler : function() {
								var list = sm.getSelections();
								var ids = new Array();
								Ext.each(list, function(item) {
											ids.push(item.id);
										});
								if (ids.length == 0) {
									Ext.MessageBox.alert('Message',
											"Please choose one record!");
									return;
								} else if (ids.length > 1) {
									Ext.MessageBox.alert('Message',
											"Only choose one record!")
									return;
								}
								openWindowBase(220, 400,
										'cotorg.do?method=orgEdit&eId='
												+ ids[0]);
							}
						}, '-', {
							text : "Delete",
							hidden : hideDel,
							iconCls : 'page_del',
							handler : function() {
								var records = sm.getSelections();
								var list = new Array();
								Ext.each(records, function(item) {
											list.push(item.id);
										});
								if (list.length == 0) {
									Ext.MessageBox.alert('Message',
											"Please choose one record!");
									return;
								}
								var flag = Ext.MessageBox.confirm('Message',
										"Are you sure delete those phones?",
										function(btn) {
											if (btn == 'yes') {
												cotOrgService.deleteOrgs(list,
														function(res) {
															Ext.MessageBox
																	.alert(
																			'Message',
																			"Suceess Delete!");
															reloadGrid("orgGrid");
														});
											}
										});
							}
						}]
			});
	var questionGrid = new Ext.grid.GridPanel({
				id : "orgGrid",
				stripeRows : true,
				border : false,
				cls : 'rightBorder',
				store : questionDs, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : toolBar,
				bbar : pageBar,
				viewConfig : {
					forceFit : true
//					,getRowClass : function(record, index) {
//						var uploadTime=record.get("uploadTime");
//						var date=Ext.util.Format.date(new Date(uploadTime.time),
//									"Y-m-d");
//						var now=Ext.util.Format.date(new Date(),
//									"Y-m-d");
//						if(now==date){
//							return "x-grid-record-qing";
//						}
//					}
				}
			});

	// 双击行显示编辑页面
	questionGrid.on('rowdblclick', function(grid, rowIndex, event) {
				if (!hideMod) {
					var record = grid.getStore().getAt(rowIndex);
					openWindowBase(220, 400, 'cotorg.do?method=orgEdit&eId='
									+ record.get("id"));
				}
			});

	// 表单
	var con = {
		layout : "fit",
		items : [questionGrid]
	};
	Ext.apply(con, cfg);
	OrgHome.superclass.constructor.call(this, con);
};
Ext.extend(OrgHome, Ext.Panel, {});