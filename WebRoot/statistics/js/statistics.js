Ext.onReady(function(){
	var typeStore = new Ext.data.SimpleStore({
		fields : ["tp", "name"],
		data : [
			['XLS', 'XLS'], 
			['PDF', 'PDF']
		]
	});
	var displayCondition = parent.contents.globleOrderBy;
	//canout状态
	var completeStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [['0', 'Running'],['2', 'Closed']]
			});
	var completeBox = new Ext.form.ComboBox({
					cmpId : 'canOut',
					fieldLabel : '<font color="red">Running/Closed</font>',
					editable : false,
					store : completeStore,
					valueField : "tp",
					displayField : "name",
					mode : 'local',
					validateOnBlur : true,
					triggerAction : 'all',
					anchor : "100%",
					hiddenName : 'canOut',
					selectOnFocus : true
				});
	// 审核状态
	var shenStore = new Ext.data.SimpleStore({
				fields : ["tp", "name"],
				data : [[0, 'All'], [1, 'Waiting & Approval'],
						[2, 'Approved']]
			});
	var shenBox = new Ext.form.ComboBox({
				//name : 'orderStatus',
				cmpId : 'orderStatus',
				editable : false,
				fieldLabel : '<font color="red">Waiting/Approved</font>',
				store : shenStore,
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				validateOnBlur : true,
				triggerAction : 'all',
				listWidth : 200,
				hiddenName : 'orderStatus',
				selectOnFocus : true
			});
	// 业务员
	var empBox = new BindCombox({
		dataUrl : "../servlet/DataSelvert?tbname=CotEmps&key=empsName",
		cmpId : 'empId',
		fieldLabel : "Sales",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		blankText : "Choose sales！",
		tabIndex : 5,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	// 产品分类
//	var typeLv2Box = new BindCombox({
//				dataUrl : "../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
//				cmpId : 'eleTypeidLv2',
//				fieldLabel : "Categories",
//				editable : true,
//				valueField : "id",
//				allowBlank : true,
//				displayField : "typeName",
//				emptyText : 'Choose',
//				pageSize : 10,
//				anchor : "100%",
//				tabIndex : 38,
//				sendMethod : "post",
//				selectOnFocus : true,
//				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//				listWidth : 350,// 下
//				triggerAction : 'all'
//			});
	var type2Box = new BindCombox({
				dataUrl : "../servlet/DataSelvert?tbname=CotTypeLv2&key=typeName",
				cmpId : 'eleTypeidLv2',
				fieldLabel : "Group",
				editable : true,
				tabIndex : 11,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				anchor : '100%',
				mode : 'remote',
				pageSize : 10,
				sendMethod : "post",
				selectOnFocus : true,
				// allowBlank : false,
				// disabled : true,
				// disabledClass : 'combo-disabled',
				// blankText : "请先选择样品中类！",
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	//departmnet
	var typeLv1IdBox = new BindCombox({
			dataUrl : "../servlet/DataSelvert?tbname=CotTypeLv1&key=typeEnName",
			cmpId : 'typeLv1Id',
			fieldLabel : "Department",
			editable : true,
			valueField : "id",
			displayField : "typeEnName",
			emptyText : 'Choose',
			pageSize : 10,
			anchor:'100%',
			sendMethod : "post",
			selectOnFocus : true,
			minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
			listWidth : 350,// 下
			triggerAction : 'all'
		});
	// 币种
	var curBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "../servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				fieldLabel : "Currency",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				allowBlank : true,
				blankText : "Choose Currency！",
				tabIndex : 7
			});
	// 材质
	var typeLv1Box = new BindCombox({
				dataUrl : "../servlet/DataSelvert?tbname=CotTypeLv1&key=typeName",
				cmpId : 'eleTypeidLv1',
				fieldLabel : "Material",
				editable : true,
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 10,
				sendMethod : "post",
				anchor : "100%",
				tabIndex : 34,
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 厂家
	var facBox = new BindCombox({
		dataUrl : "../servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'factoryId',
		fieldLabel : "Supplier",
		editable : true,
		hidden:hideFac,
		hideLabel:hideFac,
		sendMethod : "post",
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		tabIndex : 35,
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	// 出口公司
	var companyBox = new BindCombox({
				dataUrl : "../servlet/DataSelvert?tbname=CotCompany&key=companyShortName",
				cmpId : 'companyId',
				fieldLabel : "Company",
				emptyText : "Choose",
				editable : true,
				valueField : "id",
				displayField : "companyShortName",
				pageSize : 10,
				width : 100,
				sendMethod : "post",
				selectOnFocus : true,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 客户名称
	var custBox = new BindCombox({
		dataUrl : "../servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "Client",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		blankText : "Choose client！",
		tabIndex : 1,
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	var nationBox = new BindCombox({
				dataUrl : "../servlet/DataSelvert?tbname=CotNation&key=nationName",
				cmpId : 'nationId',
				fieldLabel : "Country",
				editable : true,
				valueField : "id",
				displayField : "nationName",
				emptyText : 'Choose',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex : 20,
				triggerAction : 'all'
			});
	var form = new Ext.form.FormPanel({
	title:"",
	labelWidth:60,
	labelAlign:"right",
	layout:"column",
	formId:"queryForm",
	height:140,
	region:"north",
	padding:"5px",
	frame:true,
	buttonAlign:"left",
	fbar:[
		{
		    xtype:"label",
		    text:"Export Type:"
		},
		{
			xtype : "combo",
			name : 'printType',
			fieldLabel: 'Export Type:',
			editable : true,
			store : typeStore,
			valueField : "tp",
			displayField : "name",
			mode : 'local',
			validateOnBlur : true,
			triggerAction : 'all',
			anchor:"100%",
			emptyText : 'Choose',
			hiddenName : 'printType',
			selectOnFocus : true
		},
		{
			xtype : 'button',
			text : "Search",
			width : 65,
			iconCls:"page_sel",
			handler : viewRpt
		},{
			xtype : 'button',
			text : "Export",
			width : 65,
			iconCls:"page_excel",
			handler:exportRpt
		}],
	items:[
		{
			layout:"form",
			labelWidth:80,
	       labelAlign:"right",
			hidden:displayCondition.indexOf("startTime")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"Start Date",
					name:"startTime",
					id:"startTime",
					anchor:"100%"
				}
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("endTime")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"End Date",
					name:"endTime",
					id:"endTime",
					anchor:"100%"
				}
			]
		},
		//对比报表--------------开始
		{
			layout:"form",
			labelWidth:100,
	       labelAlign:"right",
			hidden:displayCondition.indexOf("compareFromStart")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"compareFromStart",
					name:"compareFromStart",
					id:"compareFromStart",
					anchor:"100%"
				}
			]
		},
		{
			layout:"form",
			labelWidth:100,
			hidden:displayCondition.indexOf("compareFromEnd")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"compareFromEnd",
					name:"compareFromEnd",
					id:"compareFromEnd",
					anchor:"100%"
				}
			]
		},
		{
			layout:"form",
			labelWidth:100,
	       labelAlign:"right",
			hidden:displayCondition.indexOf("compareToStart")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"compareToStart",
					name:"compareToStart",
					id:"compareToStart",
					anchor:"100%"
				}
			]
		},
		{
			layout:"form",
			labelWidth:100,
			hidden:displayCondition.indexOf("compareToEnd")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"datefield",
					fieldLabel:"compareToEnd",
					name:"compareToEnd",
					id:"compareToEnd",
					anchor:"100%"
				}
			]
		},
		//对比报表--------------结束
		{
			layout:"form",
			hidden:displayCondition.indexOf("companyId")== -1?true:false,
			columnWidth:0.25,
			items:[
				companyBox
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("empId")== -1?true:false,
			columnWidth:0.25,
			items:[
				empBox
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("factoryId")== -1?true:false,
			columnWidth:0.25,
			items:[
				facBox
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("custId")== -1?true:false,
			columnWidth:0.25,
			items:[
				custBox
			]
		},
		{   //eleTypeidLv2
			layout:"form",
			hidden:displayCondition.indexOf("typeLv1Id")== -1?true:false,
			columnWidth:0.25,
			items:[
				//typeLv2Box
				typeLv1IdBox
			]
		},
		{   
			layout:"form",
			hidden:displayCondition.indexOf("eleTypeidLv2")== -1?true:false,
			columnWidth:0.25,
			items:[
				type2Box
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("eleTypeidLv1")== -1?true:false,
			columnWidth:0.25,
			items:[
				typeLv1Box
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("canOut")== -1?true:false,
			labelWidth:90,
	        labelAlign:"right",
			columnWidth:0.25,
			items:[
			    completeBox
//				{
//					xtype:"textfield",
//					fieldLabel:"开发对象",
//					id:"eleForPerson",
//					name:"eleForPerson",
//					anchor:"100%"
//				}
			]
		},
		{
			layout:"form",
			labelWidth:95,
	       labelAlign:"right",
			hidden:displayCondition.indexOf("orderStatus")== -1?true:false,
			columnWidth:0.25,
			items:[shenBox
				//nationBox
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("currencyId")== -1?true:false,
			columnWidth:0.25,
			items:[
				curBox
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("orderNo")== -1?true:false,
			columnWidth:0.25,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"No.",
					name:"orderNo",
					id:"orderNo",
					anchor:"100%"
				}
			]
		},
		{
			layout:"form",
			hidden:displayCondition.indexOf("dept")== -1?true:false,
			columnWidth:0.25,
			items:[{
				xtype:"textfield",
				fieldLabel:"Department of Sale",
				name:"dept",
				id:"dept",
				anchor:"100%"
			}]
		},{
			layout:"form",
			hidden:displayCondition.indexOf("sumYear")== -1?true:false,
			columnWidth:0.25,
			items:[{
				xtype:"numberfield",
				fieldLabel:"SumYear",
				name:"sumYear",
				id:"sumYear",
				anchor:"100%"
			}]
		},{
			layout:"form",
			hidden:displayCondition.indexOf("dayOfMonth")== -1?true:false,
			columnWidth:0.25,
			items:[{
				xtype:"numberfield",
				fieldLabel:"Day of Month",
				name:"dayOfMonth",
				id:"dayOfMonth",
				anchor:"100%"
			}]
		}
	]
});

	var MyViewport = new Ext.Viewport({
		layout : "border",
		items : [form, {
			xtype:"panel",
			autoHeight:true,
			region : "center"
				}]
			});
})
// 获取报表文件路径
function getRptFile() {
	var filePath = parent.contents.globleFilePath;
	if (filePath == null || filePath == "") {
		Ext.Msg.alert("Message","Report file not found");
	}
	return filePath;
}
//如果结束日期没填,则默认加一年,反之则减一年
function autoDate(){
	var compareFromStart=Ext.getCmp('compareFromStart').getValue();
	var compareFromEnd=Ext.getCmp('compareFromEnd').getValue();
	var compareToStart=Ext.getCmp('compareToStart').getValue();
	var compareToEnd=Ext.getCmp('compareToEnd').getValue();
	//加一年
	if(!compareToStart && compareFromStart){
		compareFromStart.setFullYear(compareFromStart.getFullYear()+1);
		Ext.getCmp('compareToStart').setValue(compareFromStart);
	}
	//加一年
	if(!compareToEnd && compareFromEnd){
		compareFromEnd.setFullYear(compareFromEnd.getFullYear()+1);
		Ext.getCmp('compareToEnd').setValue(compareFromEnd);
	}
	//减一年
	if(compareToStart && !compareFromStart){
		compareToStart.setFullYear(compareToStart.getFullYear()-1);
		Ext.getCmp('compareFromStart').setValue(compareToStart);
	}
	//减一年
	if(compareToEnd && !compareFromEnd){
		compareToEnd.setFullYear(compareToEnd.getFullYear()-1);
		Ext.getCmp('compareFromEnd').setValue(compareToEnd);
	}
}
function viewRpt(){
	autoDate();
	var queryForm = $("queryForm");
	queryForm.action = "../previewrpt.do?method=query&random="+Math.random()+"&reportTemple="+getRptFile();
	queryForm.method = "POST";
	queryForm.target ="_blank";
	queryForm.submit();
}
// 导出方法
function exportRpt() {
	autoDate();
	var printType = $('printType').value;
	var startTime = $("startTime").value;
	var endTime = $("endTime").value;
	var globleName = parent.contents.globleName;
	if (globleName == null) {
		globleName = "excel";
	}
	var model = getRptFile();
	//alert(model+"#"+globleName)
	var queryForm = $("queryForm");
	queryForm.action = "../downStaticstic.action?reportTemple=" + model
			+ "&printType=" + printType +"&title=" + globleName+"&random="+Math.random();
	queryForm.method = "POST";
	queryForm.target ="_blank";
	queryForm.submit();
}