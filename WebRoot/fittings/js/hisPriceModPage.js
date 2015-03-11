Ext.onReady(function(){
	//厂家数据列表
	var comboFac = new BindCombox({
		dataUrl:"./servlet/DataSelvert?tbname=CotFactory&key=factroyTypeidLv1&id=2",
		sendMethod:"POST",
		cmpId:"facId",
		valueField:"id",
		displayField:"shortName",
		allowBlank :false,
		blankText:"请选择供应商",
		emptyText :"请选择",
		fieldLabel:"<font color=red>供应商</font>",
		anchor:"100%"
	});
	
var form = new Ext.form.FormPanel({
	title:"",
	labelWidth:60,
	labelAlign:"right",
	layout:"column",
	padding:"5px",
	formId:"priceForm",
	id:"priceFormId",
	monitorValid:true,
	//autoHeight:true,
	autoWidth:true,
	hideBorders:true,
	frame:true,
	buttonAlign:"center",
	fbar:[
		{
			text:"保存",
			formBind:true,
			iconCls:"page_table_save",
			handler:save
		},
		{
			text:"取消",
			iconCls:"page_cancel",
			handler:function(){closeandreflashEC(true, 'hisGrid', false);}
		}
	],
	items:[
		{
			xtype:"panel",
			title:"",
			columnWidth:0.5,
			layout:"form",
			items:[
				{
					xtype:"textfield",
					fieldLabel:"配件名称",
					anchor:"100%",
					id:"fitName",
					name:"fitName",
					value:$('fittingname').value
				},
				comboFac
			]
		},
		{
			xtype:"panel",
			title:"",
			columnWidth:0.5,
			layout:"form",
			items:[
				{
					xtype:"datefield",
					id:"addTime",
					fieldLabel:"<font color=red>报价日期</font>",
					anchor:"100%",
					value:new Date(),
					allowBlank:false,
					blankText:" 请选择日期",
					emptyText:"请选择日期",
					format:"Y-m-d",
					name:"addTime"
				},
				{
					xtype:"numberfield",
					fieldLabel:"<font color=red>价格</font>",
					anchor:"100%",
					decimalPrecision:2,
					allowBlank:false,
					blankText:" 最大999.99,2位小数",
					emptyText:"最大999.99,2位小数",
					maxValue:999.99,
					id:"priceOut",
					name:"priceOut"
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
					fieldLabel:"备注",
					anchor:"100%",
					id:"remark",
					name:"remark"
				},new Ext.form.Hidden({
					name:"fitId",
					value:$('fitkey').value
				})
			]
		}
	]
});

var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();

//初始化
function initform(){
	var id = $('pId').value;
	if(id!='null' && id!=''){
		cotFittingsService.getCotPriceOutById(id,function(res){
			DWRUtil.setValues(res);
			if(res.facId!=null){
				comboFac.bindValue(res.facId);
			}
			Ext.getCmp("addTime").setValue(res.addTime);
		});
	}
}
//初始化表单
initform();
});

//保存主单
function save() {
	var price = DWRUtil.getValues('priceForm');
	DWREngine.setAsync(false);
	var cotPriceOut = new CotPriceOut();
	if ($('pId').value != 'null' && $('pId').value != '') {
		cotPriceOut.id=$('pId').value;
		
	}
	for (var p in cotPriceOut) {
		if (p!='id' && p != 'addTime' && p!='priceUnit') {
			cotPriceOut[p] = price[p];
		}
	}
	cotFittingsService.savePriceOut(cotPriceOut, $('addTime').value, function(
			res) {
		if (res != null) {
			Ext.MessageBox.alert('提示消息',"保存成功！");
			closeandreflashEC(true, 'hisGrid', false);
		} else {
			Ext.MessageBox.alert('提示消息','保存失败');
			return;
		}
	});
	DWREngine.setAsync(true);
}
