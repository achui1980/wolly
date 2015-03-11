var fitNum = "";
Ext.onReady(function() {
	// 厂家数据列表
	var comboFac = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1&type=2",
		sendMethod : "post",
		cmpId : "facId",
		editable : true,
		mode : 'remote',
		pageSize : 10,
		minChars : 1,
		valueField : "id",
		displayField : "shortName",
		allowBlank : false,
		blankText : "请选择供应商",
		emptyText : "请选择",
		fieldLabel : "<font color=red>供应商</font>",
		anchor : "97%",
		listWidth : 250,
		tabIndex : 3,
		listeners : {
			"change" : tongEleFitFac
		}
	});

	// 配件类型列表
	var comboType = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotTypeLv3&key=typeName",
				cmpId : "typeLv3Id",
				valueField : "id",
				mode : 'remote',
				pageSize : 10,
				sendMethod : "post",
				editable : true,
				displayField : "typeName",
				// allowBlank : false,
				// blankText : "请选择配件类型",
				emptyText : "请选择",
				fieldLabel : "类型",
				anchor : "97%",
				listWidth : 250,
				tabIndex : 4
			});
	var isExist = true
	var form = new Ext.form.FormPanel({
		labelWidth : 60,
		labelAlign : "right",
		layout : "form",
		region : "north",
		autoWidth : true,
		autoHeight : true,
		padding : "5px",
		renderTo : "modpage",
		formId : "cotFittingsForm",
		id : "cotFittingsFormId",
		frame : true,
		buttonAlign : "center",
		monitorValid : true,
		fbar : [{
					text : "保存",
					formBind : true,
					handler : mod,
					iconCls : "page_table_save"
				}, {
					text : "取消",
					iconCls : "page_cancel",
					handler : function() {
						closeandreflashEC(true, 'fitGrid', false)
					}
				}],
		items : [{
			xtype : "panel",
			title : "",
			layout : "column",
			items : [{
				xtype : "fieldset",
				title : "基本信息",
				layout : "column",
				columnWidth : 0.79,
				height : 210,
				items : [{
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 70,
					columnWidth : 0.25,
					items : [{
						xtype : "textfield",
						fieldLabel : "<font color=red>材料编号</font>",
						anchor : "97%",
						id : "fitNo",
						name : "fitNo",
						maxLength : 50,
						maxLengthText : "",
						tabIndex : 1,
						allowBlank : false,
						listeners : {
							"blur" : function(v) {
								if (v.getValue() == fitNum)
									return;
								cotFittingsService.findIsExistByFitNo(v
												.getValue(), function(res) {
											if (res) {
												Ext.Msg.alert("信息提示",
														"材料编号已存在,请重新输入");
												v.setValue("");
												v.focus();
											}
										});

							}
						},
						blankText : "请输入材料编号"
							// invalidText : "材料编号已存在,请重新输入！",
							// validationEvent : "change"
							// validator : function(thisText) {
							// if (thisText != '') {
							// cotFittingsService.findIsExistByFitNo(
							// thisText, function(res) {
							// if (res) {
							// isExist = true;
							// } else {
							// isExist = true;
							// }
							// });
							//														
							// }
							// alert("isExist:"+isExist)
							// return false;
							// }
					}, {
						xtype : "textfield",
						fieldLabel : "<font color=red>采购单位</font>",
						anchor : "97%",
						id : "buyUnit",
						name : "buyUnit",
						maxLength : 10,
						tabIndex : 5,
						blankText : "请输入采购单位",
						allowBlank : false
					}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 60,
					columnWidth : 0.25,
					items : [{
								xtype : "textfield",
								fieldLabel : "<font color=red>配件名称</font>",
								anchor : "97%",
								id : "fitName",
								name : "fitName",
								maxLength : 100,
								tabIndex : 2,
								blankText : "请输入配件名称",
								allowBlank : false
							}, {
								xtype : "textfield",
								fieldLabel : "<font color=red>领用单位</font>",
								anchor : "97%",
								id : "useUnit",
								name : "useUnit",
								maxLength : 10,
								tabIndex : 6,
								blankText : "请输入领用单位",
								allowBlank : false
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 60,
					columnWidth : 0.25,
					items : [comboFac, {
								xtype : "numberfield",
								fieldLabel : "<font color=red>换算率</font>",
								anchor : "97%",
								id : "fitTrans",
								name : "fitTrans",
								decimalPrecision : 4,
								maxValue : 10000000,
								tabIndex : 7,
								blankText : "请输入换算率",
								allowBlank : false,
								listeners : {
									"change" : function(txt, newVal, oldVal) {
										tongEleFit();
									}
								}
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 60,
					columnWidth : 0.25,
					items : [comboType, {
								xtype : "numberfield",
								fieldLabel : "<font color=red>采购价格</font>",
								anchor : "97%",
								id : "fitPrice",
								name : "fitPrice",
								decimalPrecision : 3,
								maxValue : 100000,
								tabIndex : 1,
								blankText : "请输入采购价格",
								allowBlank : false,
								listeners : {
									"change" : function(txt, newVal, oldVal) {
										tongEleFit();
									}
								}
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 70,
					columnWidth : 0.25,
					items : [{
								xtype : "numberfield",
								fieldLabel : "最小采购量",
								anchor : "97%",
								id : "fitMinCount",
								name : "fitMinCount",
								maxValue : 99999,
								tabIndex : 12,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 60,
					columnWidth : 0.25,
					items : [{
								xtype : "textfield",
								fieldLabel : "质量标准",
								anchor : "97%",
								id : "fitQualityStander",
								name : "fitQualityStander",
								maxLength : 100,
								tabIndex : 13,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 60,
					columnWidth : 0.5,
					items : [{
								xtype : "textfield",
								fieldLabel : "规格描述",
								anchor : "98.7%",
								id : "fitDesc",
								name : "fitDesc",
								maxLength : 500,
								tabIndex : 14,
								allowBlank : true
							}]
				}, {
					xtype : "panel",
					title : "",
					layout : "form",
					labelWidth : 70,
					columnWidth : 1,
					items : [{
								xtype : "textarea",
								fieldLabel : "备注",
								anchor : "99.2%",
								height : 80,
								id : "fitRemark",
								name : "fitRemark",
								maxLength : 500,
								tabIndex : 15,
								allowBlank : true
							}]
				}]
			}, {
				xtype : "fieldset",
				title : "图片信息",
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
					width : 140,
					buttonAlign : "center",
					html : '<div align="center" style="margin-left:0; margin-bottom: 0; width: 135px; height: 132px;">'
							+ '<img src="common/images/zwtp.png" id="picPath" name="picPath" onclick="showBigPicDiv(this)"'
							+ 'onload="javascript:DrawImage(this,135,132)" /></div>',
					buttons : [{
								width : 60,
								text : "更改",
								id : "upmod",
								iconCls : "upload-icon",
								handler : showUploadPanel
							}, {
								width : 60,
								text : "删除",
								id : "updel",
								iconCls : "upload-icon-del",
								handler : delPic
							}]
				}]
			}]
		}]
	});
	var tbl = new Ext.TabPanel({
				region : 'center',
				deferredRender : false,
				defaults : {
					autoScroll : true
				},
				defaultType : "iframepanel",
				heigth : 350,
				activeTab : -1,
				id : "maincontent",
				items : {
					title : "报价记录",
					itemId : 'priceRec',
					defaultSrc : '',
					frameConfig : {
						autoCreate : {
							id : 'priceInfo'
						}
					}, // iframe的Id
					loadMask : {
						msg : 'Loading...'
					},
					listeners : {
						"activate" : function(panel) {
							loadPriceInfo();
						}
					}
				}
			})
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [form, tbl]
			})
	// var ele = Ext.query("li[id*=priceRec]")[0];
	// // 单击标签函数
	// ele.onclick = loadPriceInfo;
	// 初始化页面函数
	function initForm() {
		var isMod = getPopedomByOpType("cotfittings.do", "MOD");
		if (isMod == 0) {// 没有修改权限
			Ext.getCmp("upmod").setVisible(false);
			Ext.getCmp("updel").setVisible(false);
		}
		// 加载主样品表单
		var id = $("eId").value;
		if (id != 'null' && id != '') {
			cotFittingsService.getFittingById(parseInt(id), function(res) {
				if (res != null) {
					DWRUtil.setValues(res);
					fitNum = res.fitNo;
					comboFac.bindPageValue("CotFactory", "id", res.facId);
					comboType.bindPageValue("CotTypeLv3", "id", res.typeLv3Id);
					var rdm = Math.round(Math.random() * 10000);
					$('picPath').src = './showPicture.action?flag=fit&detailId='
							+ id + '&tmp=' + rdm;
				}
				getLoginEmpid();
				var isSelFac = getPopedomByOpType("cotfittings.do", "PRICE");
				if (isSelFac != 0) {
					// TODO:价格信息修改
					// loadPriceInfo();
				} else {
					// $('tabPane2').style.display='none';
				}
			});
		} else {
			// $('tabPane2').style.display='none';
		}
		$('fitNo').focus();
	}
	// 初始化页面
	initForm();
});
function mod() {
	var popedom = checkAddMod($('eId').value);
	if (popedom == 1) {
		Ext.MessageBox.alert('提示消息', '对不起,您没有添加权限!请联系管理员!');
		return;
	} else if (popedom == 2) {
		Ext.MessageBox.alert('提示消息', '对不起,您没有修改权限!请联系管理员!');
		return;
	}
	// 主样品表单
	var ele = DWRUtil.getValues("cotFittingsForm");
	var fit = new CotFittings();
	for (var p in ele) {
		fit[p] = ele[p];
	}
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
	cotFittingsService.saveFittings(fit, fPath, function(res) {
				if (res) {
					Ext.MessageBox.alert('提示消息', "保存成功！");
					// 刷新父页面表格
					// closeandreflashEC(true, 'fittingsTable', false);
				} else {
					Ext.MessageBox.alert('提示消息', "修改失败！该配件编号已存在!");
					$('fitNo').select();
				}
				closeandreflashEC('true', 'fitGrid', false);
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
				waitMsg : "图片上传中......",
				opAction : opAction,
				imgObj : $('picPath'),
				imgUrl : './showPicture.action?flag=fit&detailId='
						+ $('eId').value + "&" + Math.random(),
				uploadType : "image",
				loadImgStream : true,
				uploadUrl : './uploadFittingPicture.action',
				validType : "jpg|png|bmp|gif"
			})
	win.show();
}
// 删除图片
function delPic() {
	var eId = $('eId').value;
	Ext.MessageBox.confirm('提示消息', "确定删除此图片吗?", function(btn) {
				if (btn == 'yes') {
					if (eId == null || eId == '' || eId == 'null') {
						$('picPath').src = "common/images/zwtp.png";
					} else {
						cotFittingsService.deletePicImg(parseInt(eId),
								function(res) {
									if (res) {
										$('picPath').src = "common/images/zwtp.png";
									} else {
										Ext.MessageBox.alert('提示消息',
												"图片不存在，删除图片失败!");
									}
								})
					}
				}
			});
}
// 加载报价信息
function loadPriceInfo() {
	var isPopedom = getPopedomByOpType(vaildUrl, "PRICE");
	if (isPopedom == 0) {
		Ext.MessageBox.alert('提示消息', "您没有操作配件报价信息的权限!");
		return;
	}
	var fitId = $("eId").value;
	if (fitId == 'null' || fitId == '') {
		Ext.MessageBox.alert('提示消息', "请先保存再操作报价信息!");
	} else {
		var fitName = $("fitName").value;
		var frame = document.priceInfo;
		if (frame.location.href == 'about:blank') {
			frame.location.href = "cotfittings.do?method=loadPriceInfo"
					+ "&fitId=" + fitId + '&fitName=' + encodeURI(fitName);
		}
	}
}
// 同步样品配件的价格
function tongEleFit() {
	var isMod = getPopedomByOpType(vaildUrl, "FEN");
	if (isMod == 0) {// 没有修改权限
		return;
	}
	// 编辑页面才做同步
	var id = $("eId").value;
	if (id != 'null' && id != '') {
		// 查询该配件是否已分配到样品
		var fitPrice = $('fitPrice').value;
		if (fitPrice == '' || fitPrice < 0) {
			Ext.MessageBox.alert('提示消息', '采购价不能为空!请输入!');
			$('fitPrice').focus();
			return;
		}
		var fitTrans = $('fitTrans').value;
		if (fitTrans > 1000000) {
			Ext.MessageBox.alert('提示消息', "换算率只能在[0,1000000]间!请重新输入!");
			$('fitTrans').focus();
			return;
		}

		var flag = '';
		DWREngine.setAsync(false);
		cotFittingsService.getEleFitIds(id, function(res) {
					if (res != '') {
						flag = res;
					}
				});
		DWREngine.setAsync(true);
		if (flag != '') {
			Ext.MessageBox.confirm('提示消息',
					'该配件已经分配到样品档案,您是否要保存配件并同步更新样品档案的价格?', function(btn) {
						if (btn == 'yes') {
							mod();
							DWREngine.setAsync(false);
							var onePrice = (fitPrice / fitTrans).toFixed("2");
							cotFittingsService.updateEleFitting(id, onePrice,
									function(res) {
										Ext.MessageBox.alert('提示消息', '同步更新成功!');
									});
							DWREngine.setAsync(true);
						}
					});
		}

	}
}

// 同步样品配件的厂家
function tongEleFitFac(com, newVal, oldVal) {
	var isMod = getPopedomByOpType(vaildUrl, "FEN");
	if (isMod == 0) {// 没有修改权限
		return;
	}
	// 编辑页面才做同步
	var id = $("eId").value;
	if (id != 'null' && id != '') {
		var flag = '';
		DWREngine.setAsync(false);
		cotFittingsService.getEleFitIds(id, function(res) {
					if (res != '') {
						flag = res;
					}
				});
		DWREngine.setAsync(true);
		if (flag != '') {
			Ext.MessageBox.confirm('提示消息',
					'该配件已经分配到样品档案,您是否要保存配件并同步更新样品档案的厂家?', function(btn) {
						if (btn == 'yes') {
							mod();
							DWREngine.setAsync(false);
							var onePrice = (fitPrice / fitTrans).toFixed("2");
							cotFittingsService.updateEleFittingFac(id, newVal,
									function(res) {
										Ext.MessageBox.alert('提示消息', '同步更新成功!');
									});
							DWREngine.setAsync(true);
						}
					});
		}

	}
}