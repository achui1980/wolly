Ext.namespace('QH.pan');
//$import('pan/js/panQueryForm.js');
$import('pan/js/supplierGrid.js');

var viewport=null;
Ext.onReady(function() {
	viewport = new Ext.Viewport({
				layout : 'fit',
				border : false,
				items : [
//					{
//							xtype : 'panqueryform',
//							region : 'west',
//							ref:'form',
//							width : 200
//						}, 
							{
							xtype:"suppliergrid",
							border:false,
							ref:'grid',
							cls : 'leftBorder',
							region : 'center'
						}]
			});
});
