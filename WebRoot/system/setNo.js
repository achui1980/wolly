Ext.onReady(function(){

	var html = '<div align="center" style="width: 100%;margin-top: 0">'+
			'<label style="color: green;font-size: 14;width: 100%"> '+
				'参考输入格式(字母大小写不限) '+
			'</label>'+
			'<div style="width:100%">'+
				'<label style="font-size: 12">其他参数：</label>'+
				'<label style="color: red;text-align:right;font-size: 12">'+
					'客户编号：[KH]； 厂家编号：[CH]；外销合同编号[OrderNo]；征样单编号[GivenNo]'+
				'</label>'+
			'</div>'+
			'<div style="width:100%">'+
				'<label style="font-size: 12">日期参数：</label>'+
				'<label style="color: red;text-align:right;font-size: 12">'+
					'四位数年份：[YYYY]； 两位数年份：[YY]； 月份：[MM]； 日期：[DD]；'+
				'</label>'+
			'</div>'+
			'<div style="width:100%">'+
				'<label style="font-size: 12">序号参数：</label>'+
				'<label style="color: red;text-align:right;font-size: 12" >'+
					'全局序列号：[nSEQ] (n表示序列位数，2<=n<=4);客户序列号：[nKHSEQ](2<=n<=4)'+
				'</label>'+
			'</div>'+
			'<div style="width:100%">'+
				'<label  style="font-size: 12">注：</label>'+
				'<label  style="color: red;text-align:right;font-size: 12" >'+
					'客户序列号必须与客户编号一起使用'+
			'</div>'+
		'</div>'
	var ds = new Ext.data.SimpleStore({
		fields : ["id", "name"],
		data : [['0', '系统'], ['1', '按年'],['2', '按月'],['3', '按日']]
	});
			
	var descPanel=new Ext.Panel({
		html:html,
		frame:true,
		height:120,
		hideBorders:true,
		region:"north"
	});
	var form=new Ext.form.FormPanel({
	title:"",
	hideBorders:true,
	labelWidth:120,
	autoScroll :true,
	formId:"setNoForm",
	labelAlign:"right",
	layout:"column",
	buttonAlign:"center",
	region:"center",
	frame:true,
	padding:"5",
	fbar:[
		{
			text:"保存",
			iconCls:"page_mod",
			handler:save
			
		},
		{
			text:"取消",
			iconCls:"page_cancel"
		}
	],
	items:[
		{
			xtype:"panel",
			title:"",
			columnWidth:0.4,
			layout:"form",
			items:[
				{
					xtype:"textfield",
					fieldLabel:"客户编号",
					name:"custNo",
					id:"custNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"厂家编号",
					name:"facNo",
					id:"facNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"报价单号",
					name:"priceNo",
					id:"priceNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"外销合同号",
					name:"orderNo",
					id:"orderNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"送样单号",
					name:"givenNo",
					id:"givenNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"征样单号",
					name:"signNo",
					id:"signNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"生产合同单号",
					name:"orderFacNo",
					id:"orderFacNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"生产合同分解单号",
					name:"autoOrderFacNo",
					id:"autoOrderFacNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"出货单号",
					name:"orderOutNo",
					id:"orderOutNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"排载编号",
					name:"containerNo",
					id:"containerNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"应收款单据编号",
					name:"fincaeaccountrecvNo",
					id:"fincaeaccountrecvNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"应付款单据编号",
					name:"fincaeaccountdealNo",
					id:"fincaeaccountdealNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"收款单编号",
					name:"finacerecvNo",
					id:"finacerecvNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"付款单编号",
					name:"finacegivenNo",
					id:"finacegivenNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"配件单编号",
					name:"accessNo",
					id:"accessNo",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"包材单编号",
					name:"packingNo",
					id:"packingNo",
					anchor:"100%"
				}
				
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.25,
			layout:"form",
			defaults:{
				readOnly :true
			},
			items:[
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"custNoSeq",
					id:"custNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"facNoSeq",
					id:"facNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"priceNoSeq",
					id:"priceNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"orderNoSeq",
					id:"orderNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"givenNoSeq",
					id:"givenNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"signNoSeq",
					id:"signNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"orderFacNoSeq",
					id:"orderFacNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"autoOrderFacNoSeq",
					id:"autoOrderFacNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"orderOutNoSeq",
					id:"orderOutNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"containerNoSeq",
					id:"containerNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"fincaeaccountrecvNoSeq",
					id:"fincaeaccountrecvNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"fincaeaccountdealNoSeq",
					id:"fincaeaccountdealNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"finacerecvNoSeq",
					id:"finacerecvNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"finacegivenNoSeq",
					id:"finacegivenNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"accessNoSeq",
					id:"accessNoSeq",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"当前序列号",
					name:"packingNoSeq",
					id:"packingNoSeq",
					anchor:"100%"
				}
				
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.25,
			layout:"form",
			items:[
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					editable : false,
					hiddenName :"custZeroType",
					//id:"custZeroType",
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					editable : false,
					store : ds,
					hiddenName :"facZeroType",
					//id:"facZeroType",
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"priceZeroType",
					//id:"priceZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"orderZeroType",
					//id:"orderZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"givenZeroType",
					//id:"givenZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"signZeroType",
					//id:"signZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"orderfacZeroType",
					//id:"orderfacZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"autoorderfacZeroType",
					//id:"autoorderfacZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName:"orderoutZeroType",
					//id:"orderoutZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"splitZeroType",
					//id:"splitZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"fincaeaccountrecvZeroType",
					//id:"fincaeaccountrecvZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"fincaeaccountdealZeroType",
					//id:"fincaeaccountdealZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"finacerecvZeroType",
					//id:"finacerecvZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"finacegivenZeroType",
					//id:"finacegivenZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",
					hiddenName :"accessZeroType",
					//id:"accessZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				},
				{
					xtype:"combo",
					fieldLabel:"归零方式",

					hiddenName :"packingZeroType",
					//id:"packingZeroType",
					editable : false,
					store : ds,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor:"100%"
				}
				
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.1,
			layout:"form",
			defaults:{
				style:"width:auto;float:none;margin-bottom:4px;position:static;"
			},
			items:[
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["custNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["facNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["priceNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["orderNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["givenNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["signNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["orderFacNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["autoOrderFacNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["orderOutNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["splitZeroType"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["fincaeaccountrecvNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["fincaeaccountdealNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["finacerecvNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["finacegivenNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["accessNoSeq"])
				},
				{
					xtype:"button",
					text:"归零",
					handler:toZero.createDelegate(this,["packingNoSeq"])
				}
			]
		}
	]
})
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[descPanel,form]
	})

		//初始化页面信息
initFn = function initForm(){
	setNoService.getNoMap(function(map){
		 form.getForm().setValues(map);
		 //DWRUtil.setValues(map);
	});
}
initFn();
});
var initFn = null;

				//保存
		function save(){
			var cotCfgNo = new CotCfgNo(); 
			var obj = DWRUtil.getValues("setNoForm");
			var hiddenForm = DWRUtil.getValues("hiddenForm");
			for(var p in cotCfgNo)
			{
				cotCfgNo[p] = obj[p];
			}
			for(var p in hiddenForm)
			{
				cotCfgNo[p] = hiddenForm[p];
			}
			
		 	setNoService.saveNo(cotCfgNo,function(res){
		 		Ext.Msg.alert("提示信息","保存成功！");
		 		//initFn();
		 	});
		}
		
				//归零
		function toZero(seqName){
			//var seqName = obj.name;
			//alert(seqName)
			var flag = window.confirm("确定将此序列归零吗?");
		    if(flag){
				setNoService.toZero(seqName,function(res){
			 		initFn();
			 	});
		 	}
		}
		