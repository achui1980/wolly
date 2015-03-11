/**
 * 询盘编辑页面主表单
 * 
 * @class QH.panAdd.FormPanel
 * @extends Ext.form.FormPanel
 */
QH.panAdd.DataView = Ext.extend(Ext.DataView, {
	// multiSelect : true,
	autoScroll : true,
	bodyStyle : "overflow-x:hidden;",
	emptyText : '<div style="padding:10px;">No data to display</div>',
	initComponent : function() {
		var dataView = this;
		this.itemSelector = 'div.thumb-wrap-form';
		var picRecord = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "currencyId"
				}, {
					name : "modDate"
				}, {
					name : "modPerson"
				}, {
					name : "state"
				}, {
					name : "panId"
				}, {
					name : "eleId"
				}, {
					name : "size"
				}, {
					name : "boxCount"
				}, {
					name : "printed"
				}, {
					name : "dyed"
				}, {
					name : "yarnDyed"
				}, {
					name : "others"
				}, {
					name : "packaging"
				},{
					name : "certificate"
				}, {
					name : "pack"
				},{
					name : "packOther"
				}, {
					name : "productTime"
				}, {
					name : "canPrice"
				}, {
					name : "panPrice"
				}, {
					name : "material"
				}, {
					name : "construction"
				}, {
					name : "fillingMaterial"
				}, {
					name : "fillingWeight"
				}, {
					name : "colorRemark"
				}, {
					name : "packagingRemark"
				}, {
					name : "packRemark"
				}, {
					name : "priceRemark"
				}, {
					name : "canCurId"
				}, {
					name : "targetCurId"
				}, {
					name : "targetPrice"
				}, {
					name : "manufactorer"
				},{
					name:"eleNameEn"
				},{
					name:"pcs2040"
				}]);
		var ds = new Ext.data.Store({
					autoLoad : {
						params : {
							start : 0,
							limit : 200
						}
					},
					proxy : new Ext.data.HttpProxy({
								api : {
									read : "cotpan.do?method=queryDetail&panId="+$('pId').value
								}
							}),
					reader : new Ext.data.JsonReader({
								root : "data",
								totalProperty : "totalCount",
								idProperty : "id"
							}, picRecord)
				});
		this.tpl = QH.panAdd.thumbTemplate;
		this.store = ds;
		this.ownerCt.tbar = new Ext.Toolbar({
					items : [{
						xtype:'checkbox',
						boxLabel:'Choose All',
						style:'margin-left:20px',
						id:'chooseAllChk',
						handler : dataView.chooseAll.createDelegate(this)
					}, '-',{
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:#9DBEE6">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 120,
			html : '<font color=blue>Waiting Approval</font>'
		}, {
			xtype : "panel",
			width : 30,
			html : '<div style="width: 20px; height: 20px;background-color:yellow">&nbsp;&nbsp;</div>'
		}, {
			xtype : "panel",
			width : 80,
			html : '<font color=blue>Approved</font>'
		},'->', {
								text : "Create",
								handler : dataView.addNew.createDelegate(this),
								iconCls : "page_add",
								cls : "SYSOP_ADD"
							}, '-', {
								text : 'Import',
								iconCls : "importEle",
								cls : "SYSOP_ADD",
								menu : new Ext.menu.Menu({
										items : [{
													text : 'Product',
													handler : dataView.showTable.createDelegate(this)
												}, {
													text : 'PO',
													handler : dataView.showPo.createDelegate(this)
												}]
									})
							}, '-', {
								text : 'Del',
								iconCls : "page_del",
								handler : dataView.delPans.createDelegate(this),
								cls : "SYSOP_DEL"
							}
//							, '-', {
//								text : "Additional picture",
//								// handler : showOtherPic,
//								iconCls : "picother",
//								cls : "SYSOP_ADD"
//							}
							, '-', {
								text : "Choose Staff",
								handler : this.showMailContactWin,
								iconCls : "page_lightning_go",
								cls : "SYSOP_ADD"
							},'-',{
								text : "Upload",
								handler : this.showUpload.createDelegate(this),
								iconCls : "page_lightning_go",
								cls : "SYSOP_ADD"
							}]
				}), this.ownerCt.bbar = new Ext.PagingToolbar({
					pageSize : 10,
					store : ds,
					displayInfo : true,
					displayMsg : 'Displaying {0} - {1} of {2}',
					displaySize : '5|10|15|20|all',
					emptyMsg : "No data to display"
				})
		QH.panAdd.DataView.superclass.initComponent.call(this);
	},
	showMailContactWin:function(){
		var ids = getChoosePanEles();
		if(ids.length == 0){
			Ext.Msg.alert("INFO","Please select detail!");
			return;
		}
		var win = new MailContactWin({
			detailIds:ids,
			modal:true,
			prsForm:viewport.form.getForm().getValues()		
		});
		win.show();
	},
	chooseAll:function(){
		var isChk=Ext.getCmp('chooseAllChk').getValue();
		var chks=document.getElementsByName("check_pan_ele");
		var ary=[];
		for (var i=0; i<chks.length; i++ ) {
			chks[i].checked=isChk;
		}
	},
	addNew:function(){
		var dv=this;
		var modeId = $('pId').value == 'null'?'':$('pId').value;
		if(!modeId){
			Ext.Msg.alert('INFO','Please Save Inquiry!');
			return;
		}

		var win = new Ext.Window({
					title : 'Create',
					width : 400,
					height : 100,
					padding : 5,
					modal:true,
					items : [{
								xtype : 'form',
								labelWidth : 120,
								labelAlign : 'right',
								items : [{
											xtype : 'textfield',
											name : 'eleNew',
											ref : '../eleId',
											anchor : '100%',
											fieldLabel : 'New Article name'
										}]
							}],
					buttonAlign : 'center',
					buttons : [{
						text : 'Save',
						handler : function() {
							var eleId = win.eleId.getValue();
							if (!eleId) {
								Ext.Msg.alert('INFO',
										'Please Enter New Article name');
							} else {
								//创建新货号保存时需要把所有明细都保存一下,不然刷新表格后会把其他没保存的货号信息刷没了
								DWREngine.setAsync(false);
								saveAllDetails(modeId);
								cotPanService.saveNewPanEle(eleId, modeId,
										function(res) {
											dv.getStore().load({
												params : {
													start : 0,
													panId: $('pId').value,
													limit : 200
												}
											});
											win.close();
										})
								DWREngine.setAsync(true);
							}
						}
					}, {
						text : 'Close',
						handler : function() {
							win.close();
						}
					}]
				})
		win.show(null,function(){
			win.eleId.focus(true,100);
		});
	},
	delPans : function() {
		var ids = getChoosePanEles();
		if (ids.length == 0) {
			Ext.Msg.alert('INFO', 'Please Choose Inquiry Detail!');
			return;
		}
		deletePanEles(ids);
	},
	showUpload:function(){
		var ids = getChoosePanEles();
		if (ids.length == 0) {
			Ext.Msg.alert('INFO', 'Please Choose Inquiry Detail!');
			return;
		}
		var win = new Ext.Window({
			layout:'fit',
			height:300,
			title:'Batch Upload',
			width:400,
			modal:true,
			items:[{
				xtype:'pandetailpriceform',
				detailIds:ids,
				batch:true
			}]
		});
		win.show();
	},
	showTable:function(){
		showEleTable();
	},
	showPo:function(){
		showPoTable();
	}
});

Ext.reg('panadddataview', QH.panAdd.DataView);