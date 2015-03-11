PanWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
		
	// 插入一条流水号
	function insertCheck() {
		//判断是否有选流水号
		var count = Ext.getCmp('checkNo').view.getSelectionCount();
		if(count==0 || count>1){
			Ext.MessageBox.alert('Message','Please select a serial number!');
			return;
		}
		var ds = Ext.getCmp('checkDetail').store;
		if (ds.getCount()==0) {
			Ext.MessageBox.alert('Message','The serial number has no intem NO., can not import!');
			return;
		}
		mask();
		var task = new Ext.util.DelayedTask(function(){
		    savePan();
		});
		task.delay(500); 
	}
	
	//保存盘点机
	function savePan(){
		var recs = Ext.getCmp('checkNo').view.getSelectedRecords()
		DWREngine.setAsync(false);
		// 得到该流水号的明细集合,并存入内存中
		cotPriceService.savePanList(recs[0].data.value,$('pId').value,function(res) {
						Ext.getCmp('priceDetailGrid').getStore().reload();
						_self.close();
						unmask();
				});
		DWREngine.setAsync(true);
	}

	var form = new Ext.form.FormPanel({
				labelWidth : 60,
				fileUpload : true,
				labelAlign : "right",
				autoWidth : true,
				autoHeight : true,
				padding : "10px",
				layout : 'column',
				baseCls : 'x-plain',
				buttonAlign : "center",
				fbar : [{
							text : "Import",
							iconCls : "page_excel",
							handler:insertCheck
						}],
				items : [{
					xtype : 'panel',
					columnWidth : .5,
					layout : 'form',
					labelWidth : 90,
					items : [{
						xtype : 'multiselect',
						fieldLabel : 'Serial number',
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
						fieldLabel : 'Art No.',
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
		title : "Barcode Machine",
		modal : true,
		id : "panWin",
		items : [form],
		listeners:{
			'close':function(){
				cotPriceService.removePanSession(function(res) {});
			}
		}
	}
	Ext.apply(con, cfg);
	PanWin.superclass.constructor.call(this, con);
};

Ext.extend(PanWin, Ext.Window, {});