Ext.onReady(function(){
	
 	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 客户
	var customerBox = new BindCombox({
 		dataUrl : "./servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "Customer's Name",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		sendMethod : "post",
		pageSize:5,
		anchor : "99%",
		selectOnFocus:true,
		disabled:true,
		disabledClass : 'combo-disabled',
		emptyText : 'Please select one',
		tabIndex:3,
		typeAheadDelay:1000,// 默认延时查询250
		typeAhead:false, // 防止自动填充,有的地方说的延时查询
		minChars:1,  // 设置填充几个字节就去查一次,默认为4个字节
		listWidth:350,// 下
 		triggerAction : 'all'
 	});
	
	var form = new Ext.form.FormPanel({
	labelWidth:60,
	labelAlign:"right",
	region:"center",
	layout:"column",
	autoWidth:true,
	autoHeight:true,
	padding:"5px",
	renderTo:"modpage",
	formId:"claimForm",
	id:"claimFormId",
	frame:true,
	buttonAlign:"center",
	monitorValid:true,
	fbar:[
		{
			text:"Save",
			formBind: true,
			handler:mod,
			iconCls:"page_table_save"
		},
		{
			text:"Cancel",
			iconCls:"page_cancel",
			handler:function(){
				closeandreflashEC(true,'contactGrid',false)
			}
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
								xtype:"datefield",
								id:'claimdate',
								fieldLabel:"The Time Of Claim",
								anchor:"100%",
								allowBlank:false,
								blankText:"Please enter the time of claim",
								name:"claimTime",
								format:"Y-m-d",
								tabIndex:1
							}
						]
					},{
						xtype:"panel",
						title:"",
						columnWidth:0.4,
						layout:"form",
						items:[
							customerBox
						]
					},
					{
						xtype:"panel",
						title:"",
						columnWidth:0.25,
						layout:"form",
						items:[
							{
								xtype:"numberfield",
								fieldLabel:"<font color=red>Claim Amount</font>",
								anchor:"100%",
								allowBlank:false,
								blankText:"Please enter the claim amount",
								maxValue:999999,
								name:"claimMoney",
								id:"claimMoney",
								tabIndex:2
							}
						]
					},
					{
						xtype:"panel",
						title:"",
						columnWidth:1,
						layout:"form",
						items:[
							{
								xtype:"textarea",
								fieldLabel:"<font color=red>the reason for claim</font>",
								anchor:"98%",
								allowBlank:false,
								blankText:"Please enter the reason for the claim!",
								maxLength:500,
								name:"claimReason",
								id:"claimReason",
								tabIndex:4
							},
							{
								xtype:"textarea",
								fieldLabel:"Claim Results",
								anchor:"98%",
								allowBlank:true,
								maxLength:500,
								name:"claimDeal",
								id:"claimDeal",
								tabIndex:5
							},
							{
								xtype:"textarea",
								fieldLabel:"Remark",
								anchor:"98%",
								allowBlank:true,
								maxLength:500,
								name:"claimRemark",
								id:"claimRemark",
								tabIndex:6
							},
							{
								xtype:"hidden",
								id:'id',
								name:'id'
							}
						]
					}
				]
			}
		]
	})
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[form]
	})
//初始化页面
function initForm() {
	// 加载主表单
	var id = $("eId").value;
	var custId = $("cId").value;
	if(custId != 'null' && custId != ''){
		customerBox.bindPageValue('CotCustomer','id',custId);
	}
	if (id != 'null' && id != '') {
		 cotCustomerService.getClaimById(parseInt(id),function(res){
			if (res != null) {
				var obj = res;				
				DWRUtil.setValues(obj);
				// 索赔时间
				var sdate = new Date(res.claimTime);
				var claimdate = Ext.getCmp("claimdate");
				claimdate.setValue(sdate);
				customerBox.bindPageValue("CotCustomer","id",res.custId)
			}
		});
	}else{
		// 索赔时间
		var sdate = new Date();
		var claimdate = Ext.getCmp("claimdate");
		claimdate.setValue(sdate);
		customerBox.bindPageValue("CotCustomer","id",custId)
	}
}
//初始化页面
initForm();
});
function mod(){
	DWREngine.setAsync(false); 
	var obj = DWRUtil.getValues("claimForm");
	var cotClaim = new CotClaim();
	for(var p in cotClaim){
		cotClaim[p] = obj[p];
	} 
	if($('claimdate').value!=''){
		cotClaim.claimTime = getDateType($('claimdate').value);
	}else{
		cotClaim.claimTime = null;
	}
	var id = $("eId").value;
	if(id!= 'null'){
		cotClaim.id = id;
	} 
	cotCustomerService.saveOrUpdateClaim(cotClaim,function(res){
		Ext.Msg.alert("Message","Successfully saved!");
		closeandreflashEC(true,"claimGrid",false);
	});	 
	DWREngine.setAsync(true); 	
}