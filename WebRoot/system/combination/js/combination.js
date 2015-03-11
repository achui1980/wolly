Ext.onReady(function(){
	
	// 客户
	var customerBox_Src = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
				cmpId : 'srcCustId',
				fieldLabel : "<font color='red'>Former client</font>",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : '请选择',
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 客户
	var customerBox_Des = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
				cmpId : 'desCustId',
				fieldLabel : "<font color='red'>目标客户</font>",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				mode : 'remote',// 默认local
				autoLoad : false,// 默认自动加载
				pageSize : 5,
				anchor : "100%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : 'Choose',
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 厂家
	var facBox_Src = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'srcFacId',
		fieldLabel : "<font color='red'>original supplier</font>",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		//allowBlank:false,
		blankText:"The original supplier can not be empty！",
		pageSize : 10,
		anchor : "100%",
		tabIndex : 1,
		selectOnFocus : true,
		sendMethod : "post",
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	// 厂家
	var facBox_Des = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName",
		cmpId : 'desFacId',
		fieldLabel : "<font color='red'>Target suppliers</font>",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		//allowBlank:false,
		blankText:"Suppliers can not be empty goal！",
		pageSize : 10,
		anchor : "100%",
		tabIndex : 1,
		selectOnFocus : true,
		sendMethod : "post",
		
		
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	var form=new Ext.form.FormPanel({
	labelWidth:70,
	id:'combinationId',
	labelAlign:"right",
	formId:"combinationForm",
	layout:"column",
	region:"center",
	padding:"5px",
	hideBorders:true,
	frame:true,
	items:[
		{
			xtype:"panel",
			title:"",
			columnWidth:0.33,
			layout:"form",
			items:[
				facBox_Src,
				customerBox_Src
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.34,
			layout:"form",
			items:[
				facBox_Des,
				customerBox_Des
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.33,
			layout:"form",
			defaults:{
				style:"width:auto;float:none;margin-bottom:4px;position:static;"
			},
			items:[
				{
					xtype:"button",
					text:"Merger",
					handler:combinationFac
				},
				{
					xtype:"button",
					text:"Merger",
					handler:combinationCust
				}
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:1,
			layout:"form",
			html:'<label style="margin-top: 15px;color: red;margin-left: 30px;">'
				 +'Note: Data Merge feature, please use caution, the data can not be restored once the merger will。</label>'
		}
		
	]
});
var viewport = new Ext.Viewport({
	layout:"border",
	items:[form]
});
});