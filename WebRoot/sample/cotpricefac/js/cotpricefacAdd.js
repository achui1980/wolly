var facBox = null;
var facCurBox = null;
Ext.onReady(function() {
	// 厂家
	facBox = new BindCombox({
				dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName",
				cmpId : 'facId',
				fieldLabel : "<font color=red>Supplier</font>",
				editable : true,
				valueField : "id",
				displayField : "shortName",
				emptyText : 'Choose',
				mode : 'remote',// 默认local
				pageSize : 5,
				anchor : "100%",
				allowBlank : false,
				sendMethod : "post",
				selectOnFocus : false,

				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	// 生产币种
	facCurBox = new BindCombox({
				cmpId : 'priceUint',
				fieldLabel : "<font color=red>Currency</font>",
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				fieldLabel : "<font color=red>Currency</font>",
				anchor : "100%",
				allowBlank : false
			});
	var form = new Ext.form.FormPanel({
				labelWidth : 80,
				formId : "priceFacForm",
				labelAlign : "right",
				layout : "form",
				width : 400,
				height : 250,
				buttonAlign : "center",
				monitorValid : true,
				hideBorders : true,
				frame : true,
				// renderTo:document.body,
				padding : "10px",
				fbar : [{
							text : "Save",
							formBind : true,
							iconCls : "page_mod",
							handler : add
						}, {
							text : "Cancel",
							handler : function() {
								closeandreflashEC(true, "pricefacGrid", false);
							},
							iconCls : "page_cancel"
						}],
				items : [{
							xtype : "datefield",
							fieldLabel : "<font color=red>Date</font>",
							anchor : "100%",
							id : "addTime",
							name : "addTime",
							allowBlank : false,
							format : "Y-m-d",
							blankText : "Please choose date",
							emptyText : "Choose"
						}, facBox, facCurBox, {
							xtype : "numberfield",
							id : "priceFac",
							name : "priceFac",
							fieldLabel : "<font color=red>Price</font>",
							anchor : "100%"
						}, {
							xtype : "textarea",
							fieldLabel : "Remark",
							name : "priceRemark",
							id : "priceRemark",
							anchor : "100%"
						}]
			});
	var viewport = new Ext.Viewport({
		layout:'fit',
		items:[form]
	})
	initForm();
});
// 页面加载调用
function initForm() {
	// 表单验证

	var id = $('mainId').value;
	DWREngine.setAsync(false);
	if (id == 'null' || id == '') {
		// $('addTime').value = new Date();
		$('addTime').value = Ext.util.Format.date(new Date(), "Y-m-d");
		cotElementsService.getDefaultList(function(res) {
					if (res.length != 0) {
						if (res[0].elePriceFacUnit != null) {
							// alert(res[0].elePriceFacUnit);
							facCurBox.bindValue(res[0].elePriceFacUnit)
							// $('priceUint').value = res[0].elePriceFacUnit;
						}
					}
				});
	}
	if (id != 'null' && id != '') {
		cotPriceFacService.getPriceFacById(id, function(res) {
					DWRUtil.setValues(res);
					var addTime = new Date(res.addTime);
					$('addTime').value = Ext.util.Format.date(addTime, "Y-m-d");
					facBox.bindPageValue("CotFactory", "id", res.facId);
					facCurBox.bindValue(res.priceUint)

				});
	}
	DWREngine.setAsync(true);
}
// 添加
function add() {
	var obj = DWRUtil.getValues('priceFacForm');
	var priceFac = new CotPriceFac();
	for (var p in obj) {
		if (p != 'addTime') {
			priceFac[p] = obj[p];
		}
	}
	// 验证表单
	var id = $('mainId').value;
	if (id != 'null' && id != '') {
		priceFac.id = id;
	}
	priceFac.eleId = $('eleId').value;
	cotPriceFacService.addOrUpdatePriceFac(priceFac, $('addTime').value,
			function(res) {
				if (res == null) {
					Ext.Msg.alert("Message", 'Add Failed!');
				} else {
					Ext.Msg.alert("Message", "Successfully added!");
					closeandreflashEC("false", "pricefacGrid")
				}
			})
}