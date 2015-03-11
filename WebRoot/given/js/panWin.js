PanWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	// 插入一条流水号
	function insertCheck() {
		//判断是否有选流水号
		var count = Ext.getCmp('checkNo').view.getSelectionCount();
		if(count==0 || count>1){
			Ext.MessageBox.alert('提示消息','请先选择一条流水号');
			return;
		}
		var ds = Ext.getCmp('checkDetail').store;
		if (ds.getCount()==0) {
			Ext.MessageBox.alert('提示消息','该流水号没有货号,不能导入!');
			return;
		}
		mask();
		var task = new Ext.util.DelayedTask(function(){
		    savePan();
		});
		task.delay(500); 
	}
	
	function savePan(){
		var recs = Ext.getCmp('checkNo').view.getSelectedRecords()
		DWREngine.setAsync(false);
		// 得到该流水号的明细集合,并存入内存中
		cotGivenService.savePanList(recs[0].data.value,$('pId').value,function(res) {
						Ext.getCmp('givenDetailGrid').getStore().reload();
						_self.close();
						unmask();
				});
		DWREngine.setAsync(true);
	}

	var form = new Ext.form.FormPanel({
				labelWidth : 40,
				fileUpload : true,
				labelAlign : "right",
				autoWidth : true,
				autoHeight : true,
				padding : "10px",
				layout : 'column',
				baseCls : 'x-plain',
				buttonAlign : "center",
				fbar : [{
							text : "导入货号",
							iconCls : "page_excel",
							handler:insertCheck
						}],
				items : [{
					xtype : 'panel',
					columnWidth : .5,
					layout : 'form',
					labelWidth : 50,
					items : [{
						xtype : 'multiselect',
						fieldLabel : '流水号',
						id : 'checkNo',
						name : 'checkNo',
						width:125,
						height : 200,
						ddReorder : true,
						store : []
					}]
				}, {
					xtype : 'panel',
					columnWidth : .5,
					layout : 'form',
					items : [{
						xtype : 'multiselect',
						fieldLabel : '货号',
						id : 'checkDetail',
						name : 'checkDetail',
						width:135,
						height : 200,
						ddReorder : true,
						store : []
					}]
				}]
			});
			
	var con = {
		width : 400,
		title : "盘点机",
		modal : true,
		id : "panWin",
		items : [form],
		listeners:{
			'close':function(){
				cotGivenService.removePanSession(function(res) {});
			}
		}
	}
	Ext.apply(con, cfg);
	PanWin.superclass.constructor.call(this, con);
};

Ext.extend(PanWin, Ext.Window, {});