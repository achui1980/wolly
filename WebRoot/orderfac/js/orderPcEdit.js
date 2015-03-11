Ext.onReady(function() {
	// 小类
	var shenBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : 'categoryId',
				fieldLabel : "Category",
				editable : true,
				anchor : '100%',
				valueField : "id",
				displayField : "typeName",
				emptyText : 'Choose',
				pageSize : 8,
				sendMethod : "post",
				selectOnFocus : true,
				allowBlank : false,
				blankText : 'Please choose Category!',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
			
	// 明细
	var detailBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotOrderDetail&key=barcode&typeName=orderId&type="+$('orderId').value,
				cmpId : 'orderDetailId',
				fieldLabel : "Barcode",
				editable : true,
				anchor : '100%',
				valueField : "id",
				displayField : "barcode",
				emptyText : 'Choose',
				pageSize : 8,
				sendMethod : "post",
				selectOnFocus : true,
				allowBlank : false,
				blankText : 'Please choose Barcode!',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all',
				listeners : {
					"select" : function(com, rec, index) {
						if (rec.data.id != '') {
							cotOrderService.getEleTypeidLv3ByEleName(rec.data.id,function(res){
								shenBox.bindPageValue("CotTypeLv3", "id",res);
							})
						}
					}
				}
			});

	var form = new Ext.form.FormPanel({
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		padding : "5px",
		frame : true,
		buttonAlign : "center",
//		monitorValid : true,
		fbar : [{
					text : "Save",
//					formBind : true,
					handler : mod,
					hidden:true,
					iconCls : "page_mod"
				}, {
					text : "Cancel",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC('true', 'orderPcGrid', false);
					}
				}],
		items : [{
			xtype : "fieldset",
			title : "Photo",
			anchor : '100%',
			layout : "hbox",
			labelWidth : 60,
			height : 210,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			columnWidth : 0.2,
			items : [{
				xtype : "panel",
				width : 240,
				buttonAlign : "center",
				html : '<div align="center" style="margin-left:0; margin-bottom: 0; width: 235px; height: 132px;">'
						+ '<img src="common/images/zwtp.png" id="picPath" name="picPath" onclick="showBigPicDiv(this)"'
						+ 'onload="javascript:DrawImage(this,235,132)" /></div>',
				buttons : [{
							width : 60,
							text : "Update",
							id : "upmod",
							hidden:true,
							iconCls : "upload-icon",
							handler : showUploadPanel
						}, {
							width : 60,
							text : "Delete",
							id : "updel",
							hidden:true,
							iconCls : "upload-icon-del",
							handler : delPic
						}]
			}]
		}, detailBox,shenBox, {
			xtype : "textarea",
			fieldLabel : "Remark",
			anchor : "100%",
			height : 60,
			id : "pcRemark",
			name : "pcRemark",
			maxLength : 200
		}]
	});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});

	// 初始化页面函数
	function initForm() {
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotOrderService.getOrderPcById(parseInt(id), function(res) {
				if (res != null) {
					DWRUtil.setValues(res);
					detailBox.bindPageValue("CotOrderDetail", "id", res.orderDetailId);
					shenBox.bindPageValue("CotTypeLv3", "id", res.categoryId);
					var rdm = Math.round(Math.random() * 10000);
					$('picPath').src = './showPicture.action?flag=orderpc&detailId='
							+ id + '&tmp=' + rdm;
				}
			});
		}
	}
	// 初始化页面
	initForm();

	// 保存
	function mod() {
		// 验证表单
		var formData = getFormValues(form, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		
		var fit = new CotOrderPc();
		fit.categoryId = $('categoryId').value;
		fit.pcRemark = $('pcRemark').value;
		fit.orderId = $('orderId').value;
		fit.orderDetailId = $('orderDetailId').value;
		var fPath = "";
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			fit.id = id;
		} else {
			var basePath = $('basePath').value;
			var filePath = $('picPath').src;
			var s = filePath.indexOf(basePath);
			fPath = filePath.substring(s + basePath.length, 111111);
			fPath = decodeURI(fPath);
		}

		// 更新
		cotOrderService.saveOrderPc(fit, fPath, function(res) {
					Ext.MessageBox.alert('Message', "保存成功！");
					// 刷新父页面表格
					closeandreflashEC('true', 'orderPcGrid', false);
				})
	}
	// 打开上传面板
	function showUploadPanel() {
		var id = $('eId').value;
		var opAction = "insert";
		if (id == 'null' || id == '')
			opAction = "insert";
		else
			opAction = "modify";
		var win = new UploadWin({
					params : {
						pEId : id
					},
					waitMsg : "Uploading......",
					opAction : opAction,
					imgObj : $('picPath'),
					imgUrl : './showPicture.action?flag=orderpc&detailId='
							+ $('eId').value + "&" + Math.random(),
					uploadType : "image",
					loadImgStream : true,
					uploadUrl : './uploadOrderPc.action',
					validType : "jpg|png|bmp|gif"
				})
		win.show();
	}
	// 删除图片
	function delPic() {
		var eId = $('eId').value;
		Ext.MessageBox.confirm('Message', "Are you sure delete the phone?",
				function(btn) {
					if (btn == 'yes') {
						if (eId == null || eId == '' || eId == 'null') {
							$('picPath').src = "common/images/zwtp.png";
						} else {
							cotOrderService.deleteOrderPcImg(parseInt(eId),
									function(res) {
										if (res) {
											$('picPath').src = "common/images/zwtp.png";
										} else {
											Ext.MessageBox.alert('Message',
													"Delete Failed!");
										}
									})
						}
					}
				});
	}
});
