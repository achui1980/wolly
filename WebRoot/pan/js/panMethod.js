// 保存主单
function save() {
	var popedom = checkAddMod($('pId').value);
	if (popedom == 1) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority!');
		return;
	} else if (popedom == 2) {
		Ext.MessageBox.alert('Message', 'Sorry, you do not have Authority!');
		return;
	}
	// 验证表单
	var formData = getFormValues(viewport.form, true);
	// 表单验证失败时,返回
	if (!formData) {
		return;
	}

	// 验证单号是否存在
	var shortflag = false;
	var priceNo = $('priceNo').value;
	DWREngine.setAsync(false);
	cotPanService.findIsExistPanNo(priceNo, $('pId').value, function(res) {
				if (res != null) {
					shortflag = true;
				}
			});
	if (shortflag) {
		Ext.MessageBox.alert("Message",
				"The Title already exists, enter a new one!");
		return;
	}
	DWREngine.setAsync(true);
	Ext.MessageBox.confirm('Message', 'Do you want to save the Inquiry?',
			function(btn) {
				if (btn == 'yes') {
					var price = DWRUtil.getValues('panForm');
					var cotOrder = {};
					// 如果编号存在时先查询出对象,再填充表单
					if ($('pId').value != 'null' && $('pId').value != '') {
						DWREngine.setAsync(false);
						cotPanService.getPanById($('pId').value, function(res) {
									for (var p in price) {
										if (p != 'addTime') {
											res[p] = price[p];
										}
									}
									cotOrder = res;
								});
						DWREngine.setAsync(true);
					} else {
						cotOrder = new CotPan();
						for (var p in cotOrder) {
							if (p != 'addTime') {
								cotOrder[p] = price[p];
							}
						}
					}
					DWREngine.setAsync(false);
					cotOrder.valDate = window.getDateType(cotOrder.valDate);
					cotPanService.saveOrUpdatePan(cotOrder, $('addTime').value,
							function(res) {
								$('pId').value = res;
								viewport.dataview.getStore().proxy.setApi({
											read : "cotpan.do?method=queryDetail&panId="+res
										});
								viewport.form.modId=res;
								reflashParent('panGrid');
								//保存明细
								saveAllDetails(res);
								Ext.Msg.alert("Message", "Successfully saved！");
							});
					DWREngine.setAsync(true);
				}
			});

}
//保存所有明细
function saveAllDetails(panId){
	cotPanService.getPanEleIds(panId,function(objs){
			for (var j=0; j<objs.length; j++ ) {
				var form=document.forms["detailForm_"+objs[j].id];
				for (var i=0; i<form.elements.length; i++ ) {
					var oField = form.elements[i];
					if(oField.name && oField.name!='check_pan_ele'){
						if(oField.name=='canCurId' || oField.name=='targetCurId' || oField.name=='currencyId'){
							if(oField.value==0){
								oField.value=null;
							}
						}
						if(oField.type=='checkbox'){
							if(oField.checked){
								objs[j][oField.name]=1;
							}else{
								objs[j][oField.name]=0;
							}
						}else{
							objs[j][oField.name]=oField.value;
						}
					}
				}
			}
			cotPanService.updatePanEles(objs,function(dd){})
		});
}
//保存某个询盘明细
function savePanEle(panEleId){
	var form=document.forms["detailForm_"+panEleId];
	DWREngine.setAsync(false);
	cotPanService.getPanEleById(panEleId, function(res) {
		for (var i=0; i<form.elements.length; i++ ) {
			var oField = form.elements[i];
			if(oField.name && oField.name!='check_pan_ele'){
				if(oField.name=='canCurId' || oField.name=='targetCurId' || oField.name=='currencyId'){
					if(oField.value==0){
						oField.value=null;
					}
				}
				if(oField.type=='checkbox'){
					if(oField.checked){
						res[oField.name]=1;
					}else{
						res[oField.name]=0;
					}
				}else{
					res[oField.name]=oField.value;
				}
			}
		}
		cotPanService.updatePanEle(res,function(obj){
			Ext.Msg.alert("Message", "Successfully saved！");
		})
	});
	DWREngine.setAsync(true);
}

function showModel(){
	getChoosePanEles();
}
//获得勾选择的明细id集合
function getChoosePanEles(){
	var chks=document.getElementsByName("check_pan_ele");
	var ary=[];
	for (var i=0; i<chks.length; i++ ) {
		if(chks[i].checked){
			ary.push(chks[i].id.substring(6))
		}
	}
	return ary;
}
//删除明细
function deletePanEles(ids){
	Ext.MessageBox.confirm('Message', 'Do you want to Delete these details?',
		function(btn) {
			if (btn == 'yes') {
				cotPanService.deletePanEles(ids, function(res) {
					viewport.dataview.getStore().reload();
					Ext.Msg.alert("Message", "Successfully saved！");
				});
			}
	});
}
//删除单条明细
function deleteOnePanEle(panEleId){
	deletePanEles([panEleId])
}

function showDetail(panEleId,no){
	cotPanService.getPanEleById(panEleId,function(res){
		var dataView = viewport.dataview;
		var panPriceHis = new PanPriceHis({
			panId:panEleId,
			dataView:dataView,
			manufactorer:res.manufactorer,
			currencyId:res.currencyId,
			panPrice:res.panPrice,
			state:res.state,
			modal:true,
			title : "No."+no+"&nbsp;<font color=blue>"+res.eleNameEn+"</font>&nbsp;Offer"
		});
		panPriceHis.show();
	})
}
//上传单张图片
function showUploadPanel(id) {
	var win = new UploadWin({
				params : {
					detailId : id,
					flag : 'pan'
				},
				waitMsg : "Photo upload......",
				opAction : "modify",
				imgObj : $('pan_ele_img_'+id),
				imgUrl : "./showPicture.action?detailId=" + id
							+ "&flag=pan&temp=" + Math.random(),
				uploadType : "image",
				loadImgStream : true,
				uploadUrl : './uploadTempPic.action',
				validType : "jpg|png|bmp|gif"
			})
	win.show();
}
//打开产品资料窗体,用于导入资料生成明细
function showEleTable() {
	var modeId = $('pId').value == 'null'?'':$('pId').value;
	if(!modeId){
		Ext.Msg.alert('INFO','Please Save Inquiry!');
		return;
	}
	var eleQueryWin = new EleQueryWin({
				bar : this,
				title : 'Find From Product Info',
				eleIdFind : " "
			});
	eleQueryWin.show();
};
//保存勾选的产品资料到明细表格中
function insertSelect(list) {
		var eleAry = new Array();
		Ext.each(list, function(item) {
					eleAry.push(item.data.id);
				});
		DWREngine.setAsync(false);
		cotPanService.savePanEleFromEles(eleAry,$('pId').value, function(res) {
					viewport.dataview.getStore().load({
						params : {
							start : 0,
							panId: $('pId').value,
							limit : 200
						}
					});
				})
		DWREngine.setAsync(true);
		unmask();
	
	}
//查看和上传其他图片
function showOtherPic(id) {
	var chooseWin = new PanOtherPicWin({
				id : id,
				type:'ops'
			});
	chooseWin.show();
}
//打开POEIY窗体,用于导入资料生成明细
function showPoTable() {
		var orderGrid = new OrderGrid({
					bar : this
//					,typeLv1Id:$('flag').value==2?8:6
				});
		orderGrid.show();
	};
//保存勾选的PO明细资料到明细表格中
function insertByBatch(list) {
		var eleAry = new Array();
		Ext.each(list, function(item) {
					eleAry.push(item.data.id);
				});
		DWREngine.setAsync(false);
		cotPanService.savePanEleFromPos(eleAry,$('pId').value, function(res) {
					viewport.dataview.getStore().load({
						params : {
							start : 0,
							panId: $('pId').value,
							limit : 200
						}
					});
				})
		DWREngine.setAsync(true);
		unmask();
	
	}
//销售部确认价格
function conFirmPanEle(id,manufactorer,currencyId,panPrice,state){
	//权限控制 如果登录人不是request by员工或者admin 就不能审核价格
	var addPerson=Ext.getCmp('addPersonCombo').getValue();
	if(logId!=addPerson && loginEmpId!='admin'){
		Ext.Msg.alert("Message", "Only Request By and Admin can Confirm!");
		return;
	}
	//判断是否有填写供应商名字和币种和价格
	if(!manufactorer || !currencyId || !panPrice){
		Ext.Msg.alert("Message", "Please Choose one Record from 'Detail' button！");
		return;
	}
	var str="Are you sure Approved this record?";
	var st=1;
	if(state==1){
		str="Do you want Approval this record?";
		st=0;
	}
	Ext.MessageBox.confirm('Message', str,
		function(btn) {
			if (btn == 'yes') {
				cotPanService.updatePanEleState(id,st, function(res) {
					viewport.dataview.getStore().reload();
					Ext.Msg.alert("Message", "Successfully Confirmed！");
				});
			}
	});
}

var printWin;
function showPrint(item) {
	var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("Message",
				'Sorry, you do not have Authority! ');
		return;
	}
	if ($('pId').value == 'null' || $('pId').value == '') {
		Ext.MessageBox.alert('Message','Please Save Inquiry!');
		return;
	}
	if (printWin == null) {
		printWin = new PrintWin({
					type : 'pan',
					pId : 'pId',
					pNo : 'priceNo',
					status : 'pId',
					mailSendId : 'pId'
				});
	}
	if (!printWin.isVisible()) {
		var po = item.getPosition();
		printWin.setPosition(po[0], po[1] - 185);
		printWin.show();
	} else {
		printWin.hide();
	}
};
