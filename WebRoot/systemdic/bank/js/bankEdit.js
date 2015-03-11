Ext.onReady(function(){
	
	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	var curUnit = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
		valueField : "id",
		fieldLabel : "Currency",
		autoShow : true,
		allowBlank : true,
		tabIndex : 9,
		displayField : "curNameEn",
		cmpId : "currencyId",
		emptyText : 'Choose',
		anchor : "100%"
	});
	var payType = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotPayType",
		valueField : "id",
		fieldLabel : "Payment Types",
		autoShow : true,
		allowBlank : true,
		tabIndex : 8,
		displayField : "payName",
		cmpId : "payTypeid",
		emptyText : 'Choose',
		anchor : "100%"
	});
	
	var form = new Ext.form.FormPanel({
	labelWidth:80,
	labelAlign:"right",
	layout:"column",
	autoWidth:true,
	autoHeight:true,
	padding:"5px",
	renderTo:"modpage",
	formId:"bankForm",
	id:"bankFormId",
	frame:true,
	buttonAlign:"center",
	monitorValid:true,
	fbar:[
		{
			text:"Save",
			formBind: true,
			handler:mod,
			iconCls:"page_mod"
		},
		{
			text:"Cancel",
			iconCls:"page_cancel",
			handler:function(){
				closeandreflashEC(true,'bankGrid',false)
			}
		}
	],
	items:[
			{
				xtype:"panel",
				columnWidth:0.5,
				layout:"form",
				items:[
					{
						xtype:"textfield",
						fieldLabel:"<font color=red>Bank  name</font>",
						name:'bankName',
						tabIndex:1,
						allowBlank:false,
						blankText:"Please enter bank  name",
						anchor:"100%"
					},
					{
						xtype:"textfield",
						fieldLabel:"Account",
						name:'bankAccount',
						tabIndex:4,
						anchor:"100%"
					},
					{
						xtype:"textfield",
						fieldLabel:"Address",
						name:'bankAddress',
						tabIndex:7,
						anchor:"100%"
					}
				]
			},
			{
				xtype:"panel",
				columnWidth:0.5,
				layout:"column",
				items:[
					{
						xtype:"panel",
						layout:"form",
						columnWidth:0.5,
						items:[
							{
								xtype:"textfield",
								fieldLabel:"<font color=red>Short name</font>",
								name:'bankShortName',
								tabIndex:2,
								allowBlank:false,
								blankText:"Please enter short name",
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Phone",
								name:'bankPhone',
								tabIndex:5,
								anchor:"100%"
							},
							payType
						]
					},
					{
						xtype:"panel",
						layout:"form",
						columnWidth:0.5,
						items:[
							{
								xtype:"textfield",
								fieldLabel:"Contact",
								name:'bankContact',
								tabIndex:3,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Fax",
								name:'bankFax',
								tabIndex:6,
								anchor:"100%"
							},
							curUnit
						]
					}
				]
			},
			{
				xtype:"panel",
				title:"",
				columnWidth:1,
				layout:"column",
				items:[
					{
						xtype:"panel",
						layout:"form",
						columnWidth:0.5,
						items:[
							{
								xtype:"textfield",
								fieldLabel:"Beneficiary",
								name:'bankBeneficiary',
								tabIndex:10,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Beneficiary Address",
								name:'beneficiaryAddress',
								tabIndex:12,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"SWIFT",
								name:'bankSwif',
								tabIndex:14,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Intermediary",
								name:'intermediaryBank',
								tabIndex:16,
								anchor:"100%"
							}
						]
					},
					{
						xtype:"panel",
						layout:"form",
						columnWidth:0.5,
						items:[
							{
								xtype:"textfield",
								fieldLabel:"Advising",
								name:'advisingBank',
								tabIndex:11,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Cable address",
								name:'cableAddress',
								tabIndex:13,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"TELEX",
								name:'bankTelex',
								tabIndex:15,
								anchor:"100%"
							},
							{
								xtype:"textfield",
								fieldLabel:"Intermediary swft",
								name:'intermediarySwft',
								tabIndex:17,
								anchor:"100%"
							}
						]
					}
				]
			},
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[
					{
						xtype:"textarea",
						fieldLabel:"Remark",
						name:'bankRemark',
						tabIndex:18,
						anchor:"100%"
					}
				]
			}, {
				xtype : 'hidden',
				name : 'id'
			}
		]
	})
//初始化页面
var emp = null;
function initForm() {
	// 加载主表单
	var id = $("eId").value;
	if (id != 'null' && id != '') {
		cotBankService.getBankById(parseInt(id), function(res) {
			if (res != null) {
				var obj = res;				
				DWRUtil.setValues(obj);
				curUnit.bindValue(res.currencyId);
				payType.bindValue(res.payTypeid);
			}
		});
	}else{
		//$('tabPane2').style.display='none';
	}
	$('bankName').focus();
}
//初始化页面
initForm();
});
function mod(){
	var popedom = checkAddMod($('eId').value);
	if(popedom==1){
		Ext.MessageBox.alert('Message','Sorry, you do not have Authority!');
		return;
	}else if(popedom==2){
		Ext.MessageBox.alert('Message','Sorry, you do not have Authority!');
		return;
	}
	// 表单信息
	var obj = DWRUtil.getValues("bankForm");
	var cotBank = new CotBank();
	for(var p in cotBank)
	{
		cotBank[p] = obj[p];
	} 
	var id = $("eId").value;
	if(id!='null' && id!=''){
		cotBank.id = id;
	}
	var list = new Array();
	list.push(cotBank);
	if(id != 'null' && id != ''){
		var isExist=false;
		DWREngine.setAsync(false); 
	    cotBankService.findExistNameById(id,cotBank.bankShortName,function(res){
	    	isExist = res;
	    });
	    //判断是否同名
	    if(isExist)
	    {
	    	Ext.Msg.alert('',"Short name  can not be repeated!");
	    	return;
	    }
		cotBankService.modifyTypeList(list,function(res){
			Ext.Msg.alert('',"Successfully modified！");
			closeandreflashEC(true,"bankGrid",false);
		});
		DWREngine.setAsync(true); 
	}else{
		var isExist=false;
		DWREngine.setAsync(false); 
	    cotBankService.findExistByName(cotBank.bankShortName,function(res){
	    	isExist = res;
	    });
	    //判断是否同名
	    if(isExist)
	    {
	    	Ext.Msg.alert('',"Short name  can not be repeated!");
	    	return;
	    }
		
		//添加联系人
	    cotBankService.addTypeList(list,function(res){
			Ext.Msg.alert('',"Successfully！");
			closeandreflashEC(true,"bankGrid",false);
		});	 
		DWREngine.setAsync(true); 
	}
}