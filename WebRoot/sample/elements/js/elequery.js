	// 厂家
	var facBox = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'factoryIdFind',
				fieldLabel : "Supplier",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 5,
				anchor : "95%",
				hidden:hideFac,
				hideLabel:hideFac,
				allowBlank : true,
				tabIndex : 6,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
			
	// 大类
	var typeBox = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv4&key=typeEnName",
				cmpId : 'eleTypeidLv1Find',
				fieldLabel : "Dep. Of PRODUCT",
				editable : true,
				valueField : "id",
				displayField : "typeEnName",
				emptyText : 'Select',
				pageSize : 10,
				anchor:'95%',
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type2Box.setDataUrl("../../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName&typeName=typeIdLv1&type="+rec.data.id);
//							type2Box.resetData();
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 中类
	var type2Box = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Group",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Select',
				anchor:'95%',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
//				listeners : {
//					"select" : function(com, rec, index) {
//						if (rec.data.id != '') {
//							type3Box.setDataUrl("../../servlet/DataSelvert?tbname=CotTypeLv3&key=typeName&typeName=typeIdLv2&type="+rec.data.id);
//							type3Box.resetData();
//						}
//					}
//				}
			});

	// 小类
	var type3Box = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'eleTypeidLv3',
				fieldLabel : "Category",
				editable : true,
				anchor:'95%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Select',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
			
	// 海关编码
	var eleHsidBox = new BindCombox({
				dataUrl : "../../servlet/DataSelvert?tbname=CotEleOther&key=cnName",
				cmpId : 'eleHsid',
				fieldLabel : "Customs Code",
				editable : true,
				valueField : "id",
				allowBlank : true,
				hidden : true,
				hideLabel : true,
				displayField : "cnName",
				emptyText : 'Choose',
				mode : 'remote',
				pageSize : 10,
				anchor : "95%",
				tabIndex : 18,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});

	// 左边查询面板
	var queryForm = new Ext.form.FormPanel({
				id:"queryForm",
				labelWidth : 60,
				width : 200,
				autoScroll : true,
				bodyStyle : "overflow-x:hidden;",
				height : 800,
				border:false,
				labelAlign : "right",
				layout : "column",
				padding : "5px",
				//frame : true,
				ctCls:'x-panel-mc',
				buttonAlign : "center",
				keys:[{
					key:Ext.EventObject.ENTER,
					fn:function(){
						Ext.getCmp("queryBtn").fireEvent("click");
					}
				}],
				items : [{
							xtype : "panel",
							title : "",
							layout : "form",
							labelWidth : 90,
							columnWidth : 1,
							items : [{
										xtype : "textfield",
										fieldLabel : "Art No.",
										anchor : "95%",
										allowBlank : true,
										id : "eleIdFind",
										name : "eleIdFind",
										maxLength : 100,
										tabIndex : 1
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 0.1,
							layout : "form",
							labelWidth : 60,
							height : 22,
							hidden:true,
							width : 19,
							unstyled : true,
							items : [{
										xtype : "checkbox",
										fieldLabel : "",
										boxLabel : "",
										anchor : "93%",
										id : "childFind",
										name : "childFind",
										inputValue:"true",
										hideLabel : true,
										labelSeparator : " ",
										columnWidth : "",
										validateOnBlur : false,
										tabIndex : 2,
										listeners : {
											"render" : function(obj) {
												var tip = new Ext.ToolTip({
															target : obj.getEl(),
															anchor : 'top',
															maxWidth : 150,
															minWidth : 150,
															html : '勾选时表示只检索所有子货号。'
														});
											}
										}
									}]
						}, {
							xtype : "panel",
							title : "",
							columnWidth : 1,
							layout : "form",
							labelWidth : 90,
							items : [{
										xtype : "textfield",
										fieldLabel : "Supplier A.No.",
										anchor : "95%",
										allowBlank : true,
										name : "factoryNoFind",
										maxLength : 200,
										tabIndex : 3
									},{
										xtype : "textfield",
										fieldLabel : "Material",
										anchor : "95%",
										allowBlank : true,
										name : "eleNameFind",
										maxLength : 200,
										tabIndex : 4
									}, {
										xtype : "textfield",
										fieldLabel : "Description",
										anchor : "95%",
										allowBlank : true,
										name : "eleNameEnFind",
										maxLength : 200,
										tabIndex : 5
									}, facBox, typeBox,type2Box,type3Box, {
										xtype : "textfield",
										fieldLabel : "Design",
										anchor : "95%",
										allowBlank : true,
										name : "eleColFind",
										maxLength : 50,
										tabIndex : 9
									}, {
										xtype : "textfield",
										fieldLabel : "Level",
										anchor : "95%",
										allowBlank : true,
										hidden:true,
										hideLabel:true,
										name : "eleGradeFind",
										maxLength : 30,
										tabIndex : 10
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							labelWidth : 60,
							columnWidth : 0.63,
							items : [{
										xtype : "numberfield",
										fieldLabel : "Product_L",
										anchor : "95%",
										name : "boxLS",
										maxLength : "",
										decimalPrecision : 2,
										maxValue : 10000,
										allowDecimals : true,
										tabIndex : 11
									}, {
										xtype : "numberfield",
										fieldLabel : "Product_W",
										anchor : "95%",
										name : "boxWS",
										decimalPrecision : 2,
										maxValue : 10000,
										tabIndex : 13
									}, {
										xtype : "numberfield",
										fieldLabel : "Product_H",
										anchor : "95%",
										name : "boxHS",
										decimalPrecision : 2,
										maxValue : 10000,
										tabIndex : 15
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							columnWidth : 0.37,
							labelWidth : 3,
							items : [{
										xtype : "numberfield",
										fieldLabel : "-",
										anchor : "88%",
										labelSeparator : " ",
										name : "boxLE",
										maxLength : "",
										decimalPrecision : 2,
										maxValue : 10000,
										tabIndex : 12
									}, {
										xtype : "numberfield",
										fieldLabel : "-",
										anchor : "88%",
										labelSeparator : " ",
										name : "boxWE",
										decimalPrecision : 2,
										maxValue : 10000,
										tabIndex : 14
									}, {
										xtype : "numberfield",
										fieldLabel : "-",
										anchor : "88%",
										labelSeparator : " ",
										name : "boxHE",
										decimalPrecision : 2,
										maxValue : 10000,
										tabIndex : 16
									}]
						}, {
							xtype : "panel",
							title : "",
							layout : "form",
							labelWidth : 60,
							columnWidth : 1,
							items : [{
										xtype : "textfield",
										fieldLabel : "中文规格",
										anchor : "95%",
										allowBlank : true,
										name : "eleSizeDesc",
										hidden : true,
										hideLabel : true,
										maxLength : 500,
										tabIndex : 17
									}, {
										xtype : "textarea",
										height:35,
										fieldLabel : "Size Description",
										anchor : "95%",
										allowBlank : true,
										name : "eleInchDesc",
										maxLength : 500,
										tabIndex : 18
									},eleHsidBox, {
										xtype : "textfield",
										fieldLabel : "Description",
										anchor : "95%",
										hidden : true,
										hideLabel : true,
										allowBlank : true,
										name : "eleDescFind",
										maxLength : 500,
										tabIndex : 19
									}, {
										xtype : "textfield",
										fieldLabel : "Designer",
										anchor : "95%",
										allowBlank : true,
										hidden:true,
										hideLabel:true,
										name : "eleForPersonFind",
										maxLength : 50,
										tabIndex : 20
									}, {
										xtype : "textfield",
										fieldLabel : "YY-MM-Date",
										anchor : "95%",
										allowBlank : true,
										hidden:true,
										hideLabel:true,
										name : "eleTypenameLv2",
										tabIndex : 21
									}, {
										xtype : "datefield",
										fieldLabel : "Date",
										anchor : "95%",
										allowBlank : true,
										id : 'startTime',
										name : "startTime",
										vtype : 'daterange',
										endDateField : 'endTime',
										tabIndex : 22
									}, {
										xtype : "datefield",
										fieldLabel : "To",
										anchor : "95%",
										hideLabel : false,
										labelSeparator : " ",
										allowBlank : true,
										vtype : 'daterange',
										id : "endTime",
										name : "endTime",
										startDateField : 'startTime',
										tabIndex : 23
									}]
						}],
				buttons : [{
							iconCls : "page_sel",
							width : 65,
							text : "Search",
							id:"queryBtn",
							tabIndex : 24
						}, {
							text : "Reset",
							tabIndex : 25,
							width : 65,
							iconCls : "page_reset",
							handler : function() {
								queryForm.getForm().reset();
							}
						}]
			});

	var facIframe = new Ext.tree.TreePanel({      
        autoScroll:true,  
        title:"Suppliers",
        layout:"fit",
        width : 200,
        hidden:hideFac,
		height : 235,
        animate:true,   
        enableDD:false,   
        containerScroll: true,    
        root:new Ext.tree.AsyncTreeNode({   
             text: 'Supplier', 
             expanded :false,
             draggable:false,   
             id:"root_fac" }),   
        loader:new Ext.tree.TreeLoader({    
                   dataUrl:"../../cotelements.do?method=query&type=factory" 
               }),  
        listeners : {   
              'click' : function(node) {
              		doAction(node.id,"factory")
              		//this.baseParams.type = node.id; 
         		}
        }   
    });   
	var typeIframe = new Ext.tree.TreePanel({ 
		title:"Material",
        autoScroll:true,   
        layout:"fit",
        width : 200,
		height : 235,
        animate:true,   
        enableDD:false,   
        containerScroll: true,    
        root:new Ext.tree.AsyncTreeNode({   
             text: 'Material', 
             expanded :false,
             draggable:false,   
             id:"root_type" }),   
        loader:new Ext.tree.TreeLoader({    
                   dataUrl:"../../cotelements.do?method=query&type=type"  
               }),
        listeners : {   
              'click' : function(node) {
              		doAction(node.id,"type")
              		//this.baseParams.type = node.id; 
         		}
        }
    });   

			
	var mainPanel = new Ext.Panel({
				title : 'Condition Query',
				name : "mainPanel",
				layout : 'fit',
				//width : 380,
				//height : 235,
				items : [queryForm]
			})

	var accordion = new Ext.Panel({
				title : 'Query',
				region : 'west',
				//split : true,
				collapsible : true,
				width : 220,
				border:false,
				margins:'0 5 0 0',
				cls:'rightBorder',
				layout : 'accordion',
				defaults : {
					border:false
				},
				items : [mainPanel, facIframe]
			});
	  function doAction(obj,queryType)
  		  {
  		  	if(obj == "root_fac" || obj == "root_type") return;
  			var arr = obj.split("_");
  			var factoryId = "";
  			var typeId = "";
  			if(queryType == "factory")
  			{
	  			if(arr[0] == "root")
	  			{
	  				factoryId = arr[1];
	  			}
	  			else
	  			{
	  				factoryId = arr[0];
	  				typeId = arr[1];
	  			}
  			}
  			else if(queryType == "type")
  			{
  				if(arr[0] == "root")
	  			{
	  				typeId = arr[1];
	  			}
	  			else
	  			{
	  				typeId = arr[0];
	  				factoryId = arr[1];
	  			}
  			}
  			 queryByFacAndType(factoryId,typeId);
  		  }