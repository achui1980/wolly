/**
 * 查询表单
 * @class QH.pan.PanQueryForm
 * @extends Ext.form.FormPanel
 */
QH.pan.PanQueryForm = Ext.extend(Ext.form.FormPanel, {
	labelWidth : 90,
	border : false,
	labelAlign : "right",
	layout : "form",
	padding : "5px",
	ctCls : 'x-panel-mc',
	buttonAlign : "center",
	initComponent : function() {
		var form = this;
		// 状态
		var statusStore = new Ext.data.SimpleStore({
					fields : ["id", "name"],
					data : [[-1, 'All'], [0, 'Waiting'], [1, 'Approved']]
				});
		var statusBox = new Ext.form.ComboBox({
					name : 'state',
					fieldLabel : 'Status',
					editable : false,
					store : statusStore,
					valueField : "id",
					displayField : "name",
					mode : 'local',
					validateOnBlur : true,
					triggerAction : 'all',
					anchor : "95%",
					emptyText : 'Choose',
					hiddenName : 'state',
					selectOnFocus : true
				});

		this.buttons = [{
					iconCls : "page_sel",
					width : 65,
					text : "Search",
					handler : function() {
						form.getForm().reset();
					}
				}, {
					text : "Reset",
					width : 65,
					iconCls : "page_reset",
					handler : function() {
						form.getForm().reset();
					}
				}];
		this.items = [{
					xtype : "textfield",
					fieldLabel : "Article name",
					anchor : "95%",
					name : "eleNameEn",
					tabIndex : 2
				},{
					xtype : "textfield",
					fieldLabel : "Manufactorer",
					anchor : "95%",
					name : "manufactorer",
					tabIndex : 1
				},statusBox]
		QH.pan.PanQueryForm.superclass.initComponent.call(this);
	}
});
Ext.reg('panqueryform', QH.pan.PanQueryForm);
