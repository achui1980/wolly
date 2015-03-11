Ext.onReady(function(){
		var custBox = new BindCombox({
				dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
				cmpId : 'customerId',
				autoLoad : false,
				fieldLabel : "Name",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				emptyText : 'Pls. Select',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				sendMethod : "post",
				selectOnFocus : false,
				
				
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				tabIndex:3,
				triggerAction : 'all'
			});
			
	var form=new Ext.form.FormPanel({
	title:"",
	labelWidth:60,
	labelAlign:"right",
	layout:"form",
	width:400,
	height:250,
	padding:"5px",
	frame:true,
	//autoHeight:true,
	//autoWidth:true,
	id : '99999999999',
	formId:"queryCustContactForm",
	//renderTo:Ext.getBody(),
	buttonAlign:"center",
//	monitorValid:false,
	fbar:[
		{
			text:"Save",
		//	formBind:true,
			iconCls:"page_table_save",
			handler:mod,
			tabIndex:7
		},
		{
			text:"Delete",
			iconCls:"page_del",
			handler:del,
			tabIndex:8
		},
		{
			text:"Cancel",
			iconCls:"page_cancel",
			handler:function(){
				closeandreflashEC(true,"contactGrid",false);
			},
			tabIndex:9
		}
	],
	items:[
		{
			xtype:"panel",
			title:"",
			layout:"column",
			items:[
				{
					xtype:"panel",
					title:"",
					columnWidth:0.33,
					layout:"form",
					items:[
						{
							xtype:"textfield",
							fieldLabel:"<font color='red'>Buyer</font>",
							anchor:"100%",
							allowBlank:false,
							blankText:"Please enter a contact!",
							maxLength:100,
							id:"contactPerson",
							name:"contactPerson",
							tabIndex:1
						},
						{
							xtype:"textfield",
							fieldLabel:"Phone",
							anchor:"100%",
							allowBlank:true,
							maxLength:100,
							id:"contactNbr",
							name:"contactNbr",
							tabIndex:4
						}
					]
				},
				{
					xtype:"panel",
					title:"",
					columnWidth:0.33,
					layout:"form",
					items:[
						{
							xtype:"textfield",
							fieldLabel:"Duties",
							anchor:"100%",
							allowBlank:true,
							maxLength:100,
							id:"contactDuty",
							name:"contactDuty",
							tabIndex:2
						},
						{
							xtype:"textfield",
							fieldLabel:"Fax",
							anchor:"100%",
							allowBlank:true,
							maxLength:21,
							id:"contactFax",
							name:"contactFax",
							tabIndex:5
						}
					]
				},
				{
					xtype:"panel",
					title:"",
					columnWidth:0.34,
					layout:"form",
					items:[
						custBox,
						{
							xtype:"textfield",
							fieldLabel:"E-mail",
							anchor:"100%",
							regex:getEmailOrNullRegex(),
							regexText:'Email format is incorrect，Formats such as： "user@example.com"',
							allowBlank:true,
							maxLength:100,
							msgTarget : 'side',
							id:"contactEmail",
							name:"contactEmail",
							tabIndex:6
						}
					]
				}
			]
		}
	]
});

var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
viewport.doLayout();
	
function initForm()
	{       
       var id = $("custcontactid").value;
	   cotCustContactService.getCustContactById(parseInt(id),function(res){
			  DWRUtil.setValues(res);
			  if (res.customerId != null) {
			  	custBox.bindPageValue("CotCustomer","id",res.customerId);
			  }		
	   });
	}
initForm();
});
