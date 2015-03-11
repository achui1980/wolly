Ext.onReady(function() {
	var typeStore = new Ext.data.SimpleStore({
		fields : ["tp", "name"],
		data : [
			['XLS', 'XLS'], 
			['PDF', 'PDF']
		]
	});
	
	var typeField = new Ext.form.ComboBox({
		name : 'type',
		fieldLabel: 'Package Type',
		editable : true,
		store : typeStore,
		valueField : "tp",
		displayField : "name",
		mode : 'local',
		validateOnBlur : true,
		triggerAction : 'all',
		anchor:"95%",
		emptyText : 'Choose',
		hiddenName : 'type',
		selectOnFocus : true
	});
	
	var form = new Ext.FormPanel({
						region : 'north',
						height : 50,
						formId : "queryForm",
						id : "queryFormId",
						buttonAlign : 'right', // 按钮居右显示
						labelAlign : "right",
						padding:"5",
						layout : 'column',
						border : false,
						plain : true,
						frame:true,
						items : [{
									layout : 'form',
									columnWidth : .2,
									labelWidth : 60, // 标签宽度
									items : [{
												xtype : "datefield",
												fieldLabel : "Date",
												id : "startTime",
												name : "startTime",
												format : "Y-m-d",
												anchor : "95%"
											}]
								}, {
									layout : 'form',
									columnWidth : .13,
									labelWidth : 10, // 标签宽度
									items : [{
												xtype : "datefield",
												fieldLabel : "-",
												anchor : "95%",
												id : "endTime",
												name : "endTime",
												format : "Y-m-d",
												labelSeparator : " "
											}]
								}, {
									layout : 'table',
									columnWidth : .2,
									layoutConfig:{
								      columns:2
								    },
									items : [{
												xtype : 'button',
												text : "Search",
												width : 65,
												iconCls:"page_sel",
												handler : viewRpt
											}, {
												xtype : 'button',
												text : "Reset",
												iconCls : "page_reset",
												style:{marginLeft:'10px'},
												width : 65,
												handler : function() {
													form.getForm().reset()
												}
											}]
								},{
									layout : 'form',
									columnWidth : .2,
									labelWidth : 60, // 标签宽度
									items : [{
												xtype : "combo",
												name : 'printType',
												fieldLabel: 'Export Type',
												editable : true,
												store : typeStore,
												valueField : "tp",
												displayField : "name",
												mode : 'local',
												validateOnBlur : true,
												triggerAction : 'all',
												anchor:"95%",
												emptyText : 'Choose',
												hiddenName : 'printType',
												selectOnFocus : true
											}]
								}, {
									layout : 'table',
									columnWidth : .2,
									layoutConfig:{
								      columns:1
								    },
									items : [{
												xtype : 'button',
												text : "Export",
												width : 65,
												iconCls:"page_excel",
												handler:exportRpt
											}]
								}]
					});
	
			var MyViewport = new Ext.Viewport({
						layout : "border",
						items : [form, {
							xtype:"panel",
							autoHeight:true,
							region : "center"
						}]
					});
			
			// 获取报表文件路径
			function getRptFile() {
				var filePath = parent.contents.globleFilePath;
				if (filePath == null || filePath == "") {
					Ext.Msg.alert("Message","Report file not found");
				}
				return filePath;
			}

			function getOrderBy() {
				var orderBy = parent.contents.globleOrderBy;
				if (orderBy == null) {
					orderBy = "";
				}
				return orderBy;
			}

			// 导出方法
			function exportRpt() {
				var printType = $('printType').value;
				var startTime = $("startTime").value;
				var endTime = $("endTime").value;
				var globleName = parent.contents.globleName;
				if (globleName == null) {
					globleName = "excel";
				}
				var model = getRptFile();
				openWindow("../downPrice.action?reportTemple=" + model
						+ "&printType=" + printType + "&headerFlag=" + false
						+ "&exlSheet=" + false + "&priceNo=" + globleName
						+ "&startTime=" + startTime + "&endTime=" + endTime);
			}

			// 预览方法
			function viewRpt() {
				var _height = window.screen.height;
				var _width = window.screen.width;
				var startTime = $("startTime").value;
				var endTime = $("endTime").value;
				var reportTemple = getRptFile();
				
				openMaxWindow(_height, _width,
						"../previewrpt.do?method=queryEleRpt&startTime="
						+ startTime
						+ "&endTime="
						+ endTime
						+ "&reportTemple="
						+ reportTemple);
			}
		})