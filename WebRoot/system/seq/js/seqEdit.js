Ext.onReady(function() {
             
			// 审核状态
			var shenStore = new Ext.data.SimpleStore({
						fields : ["tp", "name"],
						data : [['ele', 'Art No.'],['barcode', 'Barcode No.'],['cust', 'Cust. No.'], ['factoryNo', 'Supplier No.'],
								['price', 'Quotation No.'], ['order', 'PI'],
								['autoOrderFacNo', 'PO'],
								['orderout', 'Invoice'],['pan', 'Price No.']]
					});
			var typeBox = new Ext.form.ComboBox({
						name : 'type',
						tabIndex : 21,
						fieldLabel : 'Number type of',
						editable : false,
						emptyText : 'Choose',
						store : shenStore,
						valueField : "tp",
						displayField : "name",
						mode : 'local',
						allowBlank : false,
						blankText : 'Please select the type  number',
						validateOnBlur : true,
						triggerAction : 'all',
						anchor : "100%",
						hiddenName : 'type',
						selectOnFocus : true
					});

			// 归0方式
			var guiStore = new Ext.data.SimpleStore({
						fields : ["tp", "name"],
						data : [[0, 'System'], [1, 'Year'], [2, 'Monthly'], [3, 'Daily']]
					});
			var guiBox = new Ext.form.ComboBox({
						name : 'zeroType',
						tabIndex : 21,
						emptyText : 'Choose',
						fieldLabel : 'Zero mode',
						editable : false,
						store : guiStore,
						value : 1,
						valueField : "tp",
						displayField : "name",
						mode : 'local',
						validateOnBlur : true,
						triggerAction : 'all',
						anchor : "100%",
						hiddenName : 'zeroType',
						selectOnFocus : true
					});

			var form = new Ext.form.FormPanel({
						labelWidth : 70,
						labelAlign : "right",
						region : "center",
						padding : "5px",
						formId : "cfgForm",
						frame : true,
						buttonAlign : "center",
						monitorValid : true,
						defaults : {
							allowBlank : false
						},
						fbar : [{
									text : "Save",
									formBind : true,
									handler : save,
									iconCls : "page_table_save"
								}, {
									text : "Cancel",
									iconCls : "page_cancel",
									handler : function() {
										closeandreflashEC(true, 'seqGrid',
												false)
									}
								}],
						items : [{
									xtype : "panel",
									layout : "form",
									labelWidth : 150,
									items : [typeBox, {
												xtype : "textfield",
												fieldLabel : "Expression",
												anchor : "100%",
												tabIndex : 3,
												id : "seqCfg",
												name : "seqCfg",
												allowBlank : false,
												maxLength : 200
											}, guiBox, {
												xtype : "numberfield",
												fieldLabel : "Current Sequence number",
												anchor : "100%",
												allowDecimals : false,
												allowNegative : false,
												nanText : 'Please enter a valid integer',
												tabIndex : 3,
												value : 0,
												//readOnly : true,
												id : "currentSeq",
												name : "currentSeq",
												maxLength : 200
											}

									]
								}, {
									xtype : 'hidden',
									name : 'id'
								}]
					});
			// 参数配置说明表格
			/** ******EXT创建grid步骤******** */
			/* 1、创建数据记录类型类型 Ext.data.Record.create */
			/* 2、创建数据存储对象(数据源) Ext.data.Store */
			/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
			/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */
			/** ************** */
			var roleRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "key"
					}, {
						name : "typeName"
					}

			]);
			// 创建数据源
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : "cotseq.do?method=queryCfg"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, roleRecord)
					});
			// 创建需要在表格显示的列
			var cm = new Ext.grid.ColumnModel([{
						header : "ID",
						dataIndex : "id",
						width : 50,
						sortable : true,
						hidden : true
					}, {
						header : "Configuration parameters",
						dataIndex : "key",
						width : 120,
						sortable : true
					}, {
						header : "Parameter Description",
						dataIndex : "typeName",
						width : 200,
						renderer : function(value, meta, rec, rowIdx, colIdx,
								ds) {
							meta.attr = ' ext:qtip="' + value + '"';
							return value;
						},
						sortable : true
					}]);
			var toolBar = new Ext.PagingToolbar({
						pageSize : 1000,
						store : ds,
						displayInfo : true,
						displayMsg : 'Displaying {0} - {1} of {2}',
						displaySize : 'NONE',
						emptyMsg : "No data to display"
					});
			var grid = new Ext.grid.GridPanel({
						title : "Configuration parameters",
						// collapsed:true,
						collapsible : true,
						region : "west",
						id : "cfgGrid",
						width : 450,
						// autoHeight:true,
						stripeRows : true,
						margins : "0 5 0 0",
						bodyStyle : 'width:100%',
						store : ds, // 加载数据源
						cm : cm, // 加载列
						// selModel: new
						// Ext.grid.RowSelectionModel({singleSelect:false}),
						loadMask : true, // 是否显示正在加载
						bbar : toolBar,
						// layout:"fit",
						viewConfig : {
							forceFit : true
						}
					});
			ds.on('beforeload', function() {

						ds.baseParams = {
						// TODO：
						};

					});
			// 分页基本参数
			ds.load({
						params : {
							start : 0,
							limit : 1000
						}
					});
			/*----------------------------------------------------*/
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [form, grid]
					});
			viewport.doLayout();

			// 初始化页面
			function initForm() {
				var id = $("cfgId").value;
				if (id != 'null' && id != '') {
					cotSeqService.getCotSeq(parseInt(id), function(res) {
								if (res != null) {
									DWRUtil.setValues(res);
									typeBox.setValue(res.type);
									guiBox.setValue(res.zeroType);
									$('hisDate').value = res.hisDay == null
											? ""
											: res.hisDay;
								}
							});
				}
			}
			// 初始化页面
			initForm();

			function save() {
				var seq = new CotSeq();
				var obj = DWRUtil.getValues("cfgForm");
				var list = new Array();
				for (var p in obj) {
					seq[p] = obj[p];
				}
				seq.name = typeBox.getRawValue();
			//	alert(typeBox.getRawValue());

				if ($('hisDate').value != "" && $('hisDate').value != 'null') {
					seq.hisDay = new Date($('hisDate').value);
				}
				list.push(seq);
				cotSeqService.saveOrUpdateList(list, function(res) {
							reflashParent('seqGrid');	
							Ext.Msg.alert("Message", "Successfully saved")
						});
			}
		});
