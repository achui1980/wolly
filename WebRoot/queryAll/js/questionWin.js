QuestionWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
		cmpId : 'queryPerson',
		fieldLabel : "From",
//		hidden:true,
//		hideLabel:true,
		disabled : true,
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
							id : "queryTime",
							name : "queryTime",
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
							fieldLabel : "Comment",
							anchor : "100%",
							id : "queryText",
							name : "queryText",
							height : 120
						},{
							xtype:'textfield',
							fieldLabel:'Password',
							//hidden:(displayType == 'SAMPLE' || displayType == 'SAMPLEOUT' )?false:true,
							anchor : "100%",
							disabled:true,
							id : "passwordApproval",
							name : "passwordApproval"
						}]
			})
			
	function save(){
		var obj = new CotQuestion();
		obj.queryPerson = empBox.getValue();
		obj.queryTime = Ext.getCmp("queryTime").getValue();
		obj.queryText = Ext.getCmp("queryText").getValue();
		obj.orderId = cfg.orderId;
		obj.flag =cfg.flag;
		obj.id = cfg.pId;
		//没有权限密码签名，直接跳过
		if(getPopedomByOpType('cotorderstatus.do','PASSWORDAPPROVAL') == 0){
			alert('You do not have passowrd approval right,click OK to continue!');
			saveQuestion(obj)
		}else{
			var pwd = Ext.getCmp("passwordApproval").getValue();
			if(Ext.isEmpty(pwd)){
				saveQuestion(obj);
			}
			else if(pwd !== loginEmp.passwordApproval){
				Ext.Msg.alert('Info','Approval Password incorrect!');
				return;
			}else{
				if(Ext.getCmp('detailGrid')){
					var record = Ext.getCmp('detailGrid').getSelectionModel().getSelected();
					var deadline1 = record.get(valueMap[displayType][0]);
					var deadline2 = record.get(valueMap[displayType][2]);
					var approval1 = record.get(valueMap[displayType][1]);
					var approval2 = record.get(valueMap[displayType][3]);
					if(Ext.isEmpty(deadline1)){
						record.set(valueMap[displayType][0],Ext.util.Format.date(new Date(), 'd-m-Y'));
					}
					if(Ext.isEmpty(deadline2)){
						record.set(valueMap[displayType][2],Ext.util.Format.date(new Date(), 'd-m-Y'));
					}
					if(Ext.isEmpty(approval1)){
						record.set(valueMap[displayType][1],Ext.util.Format.date(new Date(), 'd-m-Y'));
					}
					if(Ext.isEmpty(approval2)){
						record.set(valueMap[displayType][3],Ext.util.Format.date(new Date(), 'd-m-Y'));
					}
					window.updateOrderFac(record.get('id'),record.get('orderfacId'));
					saveQuestion.defer(500,this,obj);
				}else{
					saveQuestion(obj);
				}
			}
		}
	}
	function saveQuestion(question){
		cotFaqService.saveOrUpdateQuestion(question,function(res){
				cfg.ds.reload();
				cfg.dsPa.reload();
				_self.close();
			})
	} 
	this.setFormValue=function(form){
		empBox.bindPageValue("CotEmps", "id", form.data.queryPerson);
		var date = new Date(form.data.queryTime.time);
		Ext.getCmp("queryTime").setValue(date);
		Ext.getCmp("queryText").setValue(form.data.queryText);
	}

	// 表单
	var con = {
		title : 'Comment CN',
		layout : 'fit',
		width : 400,
		height : 440,
		modal : true,
		items : [form],
		listeners:{
			'afterrender':function(pnl){
				empBox.bindPageValue("CotEmps", "id", logId);
				factoryBox.bindPageValue("CotFactory", "id", cfg.factoryId);
				custBox.bindPageValue("CotCustomer", "id", cfg.custId);
				if(getPopedomByOpType('cotorderstatus.do','PASSWORDAPPROVAL') == 0){
					Ext.getCmp("passwordApproval").emptyText  = 'Do not have right';
					Ext.getCmp("passwordApproval").setDisabled(true);
				}
			}
		}
	};
	Ext.apply(con, cfg);
	QuestionWin.superclass.constructor.call(this, con);
};
Ext.extend(QuestionWin, Ext.Window, {});