Ext.onReady(function() {

	var custBox = new BindCombox({
		dataUrl : "servlet/DataPropedomServlet?tbname=CotCustomer&key=customerShortName&validUrl=cotcustomer.do",
		cmpId : 'customerId',
		autoLoad : false,
		fieldLabel : "Name",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		emptyText : ' Pls. select',
		sendMethod : "post",
		pageSize : 10,
		anchor : "100%",
		allowBlank : true,
		selectOnFocus : false,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		tabIndex : 3,
		triggerAction : 'all'
	});

	var form = new Ext.form.FormPanel({
		title : "",
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		// width:400,
		// height:190,
		padding : "5px",
		frame : true,
		// autoHeight:true,
		// autoWidth:true,
		formId : "queryCustContactForm",
		// renderTo:Ext.getBody(),
		buttonAlign : "center",
		// monitorValid:true,
		fbar : [{
					text : "Save",
					// formBind:true,
					iconCls : "page_table_save",
					handler : add,
					tabIndex : 7
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					tabIndex : 8,
					handler : function() {
						closeandreflashEC(true, "contactGrid", false);
					}
				}],
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
						xtype : "panel",
						title : "",
						columnWidth : 0.33,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "<font color='red'>Buyer</font>",
									anchor : "100%",
									allowBlank : false,
									blankText : "Please enter a contact",
									maxLength : 100,
									id : "contactPerson",
									name : "contactPerson",
									tabIndex : 1
								}, {
									xtype : "textfield",
									fieldLabel : "phone",
									anchor : "100%",
									allowBlank : true,
									maxLength : 100,
									id : "contactNbr",
									name : "contactNbr",
									tabIndex : 4
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.33,
						layout : "form",
						items : [{
									xtype : "textfield",
									fieldLabel : "Duties",
									anchor : "100%",
									allowBlank : true,
									maxLength : 100,
									id : "contactDuty",
									name : "contactDuty",
									tabIndex : 2
								}, {
									xtype : "textfield",
									fieldLabel : "Fax",
									anchor : "100%",
									allowBlank : true,
									maxLength : 21,
									id : "contactFax",
									name : "contactFax",
									tabIndex : 5
								}]
					}, {
						xtype : "panel",
						title : "",
						columnWidth : 0.34,
						layout : "form",
						items : [custBox, {
									xtype : "textfield",
									fieldLabel : "E-mail",
									anchor : "100%",
									allowBlank : true,
									regex : getEmailOrNullRegex(),
									regexText : 'Email format is incorrect，Formats such as： "user@example.com"',
									maxLength : 100,
									id : "contactEmail",
									name : "contactEmail",
									tabIndex : 6
								}]
					}]
		}]
	});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	viewport.doLayout();
	// 初始化表单
	init();
	function init() {
		var personName = $('personName').value;
		var personEmailUrl = $('personEmailUrl').value;
		if (personName)
			Ext.getCmp('contactPerson').setValue(personName);
		if (personEmailUrl)
			Ext.getCmp('contactEmail').setValue(personEmailUrl);
		Ext.getCmp('contactPerson').focus();
		var custId = $('custId').value;
		if (custId == "" || custId == 'null')
			return;
		custBox.bindPageValue("CotCustomer", "id", custId);
	}

})