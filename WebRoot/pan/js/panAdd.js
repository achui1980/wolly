Ext.namespace('QH.panAdd');
$import('dwr/interface/cotPanService.js');
$import('pan/js/panAddForm.js');
//$import('common/js/jquery-1.3.1.js');
$importCss('common/css/dataview-form.css');
$import('pan/js/panDetailTpl.js');
$import('pan/js/panMethod.js');
$import('pan/js/panDetailDataView.js');
$import('pan/js/mailWin.js');
$import('pan/js/mailContactWin.js');
$import('pan/js/panPriceHis.js');
$import('pan/js/panDetailPriceHisForm.js');
$import('common/js/printPanel.js');
//上传
$importCss('common/css/chooser.css');
$importCss('common/ext/ux/css/file-upload.css');
$import('common/js/Form.js');
$import('common/ext/ux/FileUploadField.js');
$import('common/js/uploadpanel.js');
$import('common/ext/ux/DataView-more.js');
$import('pan/js/orderGrid.js');
$import('pan/js/panOtherPicWin.js');
$import('commonQuery/js/eleQuery.js');
var viewport='';
Ext.onReady(function() {
	var modeId = $('pId').value == 'null'?'':$('pId').value;
	viewport = new Ext.Viewport({
				layout : 'border',
				items : [{
							xtype : 'panaddform',
							region : 'north',
							height : 90,
							modId: modeId,
							id:'panform',
							ref : 'form'
						}, {
							region : 'center',
							layout : 'fit',
							xtype:'panel',
							frame : true,
							id : "dataview-form",
							items : [{
										xtype : 'panadddataview',
										ref : '../dataview'
									}],
							buttonAlign : 'center',
							buttons : [{
										text : "Save",
										id : 'saveBtn',
										cls : "SYSOP_ADD",
										handler : save,
										iconCls : "page_table_save"
									}, {
										text : "Print",
										iconCls : "page_print",
										handler : showPrint,
										cls : "SYSOP_PRINT"
									}, {
										text : 'Mail',
										id : 'allMailBtn',
										hidden : true,
										handler : showAllContacts,
										iconCls : "email_go"
									}, {
										text : "Cancel",
										iconCls : "page_cancel",
										handler : function() {
											closeandreflashEC(true, 'panGrid',
													false);
										}
									}]
						}]
			});
	
			function showAllContacts() {
				var pid = document.getElementById('pId').value;
				if(pid == '' || pid == 'null'){
					Ext.Msg.alert('INFO','Please save PRS first!');
					return
				}
				var mailWin = new MailWin({
					closeAction:'close',
					prsForm:viewport.form.getForm().getValues()
				});
				mailWin.show();
			}


});
