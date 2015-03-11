Ext.onReady(function(){

	var MyForm=new Ext.form.FormPanel({
	title:"System Registration",
	labelWidth:80,
	labelAlign:"left",
	layout:"form",
	id:"regFormId",
	formId:"regForm",
	width:669,
	height:350,
	padding:"10px",
	renderTo:"regedit",
	frame:true,
	hideBorders:true,
	buttonAlign:"center",
	monitorValid :true,
	fbar:[
		{
			text:"Regist",
			formBind:true,
			handler:add
		},
		{
			text:"Reset",
			handler:reloadForm
			
		}
	],
	items:[
		{
			xtype:"textfield",
			fieldLabel:"Registration serial number",
			name:"mechineKey",
			id:"mechineKey",
			anchor:"100%"
		},
		{
			xtype:"combo",
			triggerAction:"all",
			fieldLabel:"Registration Type",
			width:550,
			store:new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['0', 'Permanent'], ['1', 'Temporary']]
			}),
			mode:"local",
			editable:false,
			displayField:"name",
			valueField :"id",
			listeners:{
				select :function( combo,record,index  ){
					if(record.data.id == 0)
						Ext.getCmp("tempreg").setVisible(false);
					else
						Ext.getCmp("tempreg").setVisible(true);
//					//alert(record.data.id)
				}
			},
			//name:"isStangAlone",
			hiddenName  :"isStangAlone",
			value:"0",
			id:"isStangAloneCb",
			anchor:"100%"
		},
		{
			xtype:"textfield",
			fieldLabel:"License",
			allowBlank:false,
			blankText:"Please enter the registration code",
			emptyText:"Please enter the registration code",
			name:"regeditKey",
			id:"regeditKey",
			anchor:"100%"
		},
		{
			xtype:"textfield",
			//allowBlank:false,
			fieldLabel:"Module registration code",
			blankText:"Please enter the registration code",
			//emptyText:"请输入注册码",
			name:"regeditModule",
			id:"regeditModule",
			anchor:"100%"
		
		},
		{
			xtype:"numberfield",
			fieldLabel:"Number of clients",
			allowDecimals:false,
			allowNegative :false,
			decimalPrecision :0,
			emptyText:"Please enter the client number, value range(Registration Type - Temporary：【1-15】,Permanent: Unlimited)",
			allowBlank:false,
			blankText:"Please enter the number of clients",
			
			name:"serverNo",
			id:"serverNo",
			anchor:"100%"
		},
		{
			xtype:"panel",
			title:"",
			layout:"form",
			id:"tempreg",
			hidden:true,
			items:[
				{
					xtype:"numberfield",
					fieldLabel:"Use a long (months)",
					allowDecimals:false,
					allowNegative :false,
					decimalPrecision :0,
					emptyText:"Please enter the length of use, value range【0-15】",
					maxValue:15,
					minValue:0,
					//allowBlank:false,
					//blankText:"请输入使用时长",
					name:"month",
					id:"month",
					anchor:"100%"
				},
				{
					xtype:"numberfield",
					fieldLabel:"Frequency of use",
					allowDecimals:false,
					allowNegative :false,
					decimalPrecision :0,
					emptyText:"Please enter the frequency of use, value range【0-65535】",
					maxValue:65535,
					minValue:0,
					//allowBlank:false,
					//blankText:"请输入使用次数",
					name:"userTime",
					id:"userTime",			
					anchor:"100%"
				}
			]
		},
		{
			xtype: 'checkboxgroup',
			id:'attachModule',
			name:"attachModule",
	    	fieldLabel: 'Additional modules',
	    	layout:"column",
	    	padding:'0',
	    	margin:'0',
	    	items: [{
	    		xtyp:'panel',
	    		columnWidth:0.1,
	    		items:[{boxLabel: 'Mail', name: 'emailmodule',id:'emailmodule' ,inputValue:"A"}]
	    	} 
		    ]
		},
		{
			xtype:"panel",
			title:"",
			layout:"column",
			hideBorders:true,
			items:[
				{
					xtype:"radio",
					fieldLabel:"Tags",
					boxLabel:"Sample system",
					checked :true,
					inputValue :"sample",
					id:"sample",
					name:"softVer",
					columnWidth:0.2
				},
				{
					xtype:"radio",
					fieldLabel:"Tags",
					boxLabel:"Quotation System",
					inputValue :"price",
					id:"price",
					name:"softVer",
					columnWidth:0.2
				},
				{
					xtype:"radio",
					fieldLabel:"Tags",
					boxLabel:"Foreign Trade Management Standard Version",
					inputValue :"trade",
					id:"trade",
					name:"softVer",
					columnWidth:0.2
				},
				{
					xtype:"radio",
					fieldLabel:"Tags",
					boxLabel:"Enhanced version of the foreign trade management",
					inputValue :"trade_f",
					id:"trade_f",
					name:"softVer",
					columnWidth:0.2
				},
				{
					xtype:"radio",
					fieldLabel:"Tags",
					boxLabel:"E-mail version",
					inputValue :"email",
					id:"email",
					name:"softVer",
					columnWidth:0.2
				}
			]
		}
	]
});
//var win = new Ext.Window({
//	width:670,
//	heigth:350,
//	layout:"fit",
//	y:80,
//	draggable:false,
//	resizable:false,
//	closable:false,
//	items:MyForm
//});
//win.show();
initForm();
})