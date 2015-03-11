AnswerWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'anwserPerson',
		fieldLabel : "From",
		disabled : true,
//		hidden:true,
//		hideLabel:true,
		disabledClass : 'combo-disabled',
		valueField : "id",
		displayField : "empsName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});
	
	var custBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'custId',
		fieldLabel : "Client",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : 'Choose',
		pageSize : 10,
		anchor : "100%",
		disabled : true,
		disabledClass : 'combo-disabled',
		sendMethod : "post",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 250,// 下
		triggerAction : 'all'
	});
	
	// 主单厂家
	var factoryBox = new BindCombox({
		dataUrl : "./servlet/DataPropedomServlet?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=1&validUrl=cotfactory.do",
		cmpId : 'factoryId',
		fieldLabel : "Supplier",
		editable : true,
		valueField : "id",
		displayField : "shortName",
		emptyText : 'Choose',
		pageSize : 10,
		sendMethod : "post",
		anchor : "100%",
		selectOnFocus : true,
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all',
		disabled : true,
		disabledClass : 'combo-disabled'
	});


	var form = new Ext.form.FormPanel({
				layout : 'form',
				frame : true,
				padding : '10px',
				labelWidth : 70,
				labelAlign : 'right',
				buttonAlign : 'center',
				buttons : [{
							text : "Save",
							iconCls : "page_table_save",
							handler : save
						},{
							text : "Cancel",
							iconCls : "page_cancel",
							handler : function() {
								_self.close();
							}
						}],
				items : [{
							xtype : 'textfield',
							fieldLabel : "PI No.",
							anchor : "100%",
							value:cfg.orderNo,
							disabled : true,
							disabledClass : 'combo-disabled'
						},empBox, {
							xtype : "datefield",
							fieldLabel : "Date",
							anchor : "100%",
							format : "d-m-Y H:i:s",
							id : "anwserTime",
							name : "anwserTime",
							value : new Date(),
							disabled : true,
							disabledClass : 'combo-disabled'
						},factoryBox,custBox,{
							xtype : 'textfield',
							fieldLabel : "Product",
							anchor : "100%",
							value:cfg.allPinName,
							disabled : true,
							disabledClass : 'combo-disabled'
						},{
							xtype : "datefield",
							fieldLabel : "Shipment",
							anchor : "100%",
							format : "d-m-Y",
							value : cfg.orderLcDate,
							disabled : true,
							disabledClass : 'combo-disabled'
						}, {
							xtype : 'textarea',
							fieldLabel : "Comment CN",
							anchor : "100%",
							value : cfg.queryText,
							disabled : true,
							disabledClass : 'combo-disabled',
							height : 80
						}, {
							xtype : 'textarea',
							fieldLabel : "Comment DK",
							anchor : "100%",
							id : "anwserText",
							name : "anwserText",
							height : 80
						}]
			})
			
	function save(){
		var obj = new CotAnwser();
		obj.anwserPerson = empBox.getValue();
		obj.anwserTime = Ext.getCmp("anwserTime").getValue();
		obj.anwserText = Ext.getCmp("anwserText").getValue();
		obj.questionId = cfg.questionId;
		obj.id = cfg.pId;
		cotFaqService.saveOrUpdateAnswer(obj,function(res){
//			cfg.ds.reload();
			if(typeof(cfg.pDs)!='undefined'){
				cfg.pDs.reload();
			}
			if(typeof(cfg.dsPa)!='undefined'){
				cfg.dsPa.reload();
			}
			_self.close();
		})
	}
	
	this.setFormValue=function(form){
		empBox.bindPageValue("CotEmps", "id", form.data.anwserPerson);
		var date = new Date(form.data.anwserTime.time);
		Ext.getCmp("anwserTime").setValue(date);
		Ext.getCmp("anwserText").setValue(form.data.anwserText);
	}

	// 表单
	var con = {
		title : 'Comment DK',
		layout : 'fit',
		width : 400,
		height : 420,
		modal : true,
		items : [form],
		listeners:{
			'afterrender':function(pnl){
				factoryBox.bindPageValue("CotFactory", "id", cfg.factoryId);
				custBox.bindPageValue("CotCustomer", "id", cfg.custId);
				if(typeof(logId)=='undefined'){
					cotPopedomService.getLogId(function (res) {
						empBox.bindPageValue("CotEmps", "id", res);
					});
				}else{
					empBox.bindPageValue("CotEmps", "id", logId);
				}
			}
		}
	};
	Ext.apply(con, cfg);
	AnswerWin.superclass.constructor.call(this, con);
};
Ext.extend(AnswerWin, Ext.Window, {});