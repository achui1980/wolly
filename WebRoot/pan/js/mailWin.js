MailWin = Ext.extend(Ext.Window, {
	title : 'Mail',
	width : 800,
	height : 500,
	modal : true,
	border : false,
	prsForm:null,
	initComponent : function() {
		var win = this;
		var roleRecordRight = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "empsName"
				}, {
					name : "empsMail"
				}]);
		var dsRight = new Ext.data.Store({
			autoLoad:{
				params : {
					start : 0,
					limit:20
				}			
			},
			method : 'post',
			proxy : new Ext.data.HttpProxy({
				url : "cotemps.do?method=queryList"
			}),
			reader : new Ext.data.JsonReader({
						root : "data",
						totalProperty : "totalCount",
						idProperty : "id"
					}, roleRecordRight)
		});

		// 创建复选框列
		var smRight = new Ext.grid.CheckboxSelectionModel();
		// 创建需要在表格显示的列
		var cmRight = new Ext.grid.ColumnModel({
					defaults : {
						sortable : true
					},
					columns : [smRight, {
								header : "编号",
								dataIndex : "id",
								width : 50,
								hidden : true
							}, {
								header : "UserName",
								dataIndex : "empsName",
								width : 90
							}, {
								header : "E-Mail",
								dataIndex : "empsMail",
								width : 120
							}]
				});
		var toolBarRight = new Ext.PagingToolbar({
					pageSize : 20,
					store : dsRight,
					displayInfo : true,
					displaySize : '5|10|15|20|300|500'
				});
		var tb = new Ext.Toolbar({
			items:['->',{
				text:'Send Mail',
				iconCls:'email_go',
				handler:win.sendMail.createDelegate(win,[win.prsForm])
			}]
		})
		var rightTopGrid = new Ext.grid.GridPanel({
					title : "Staff List",
					id:'empgrid',
					stripeRows : true,
					region : 'center',
					store : dsRight,
					cm : cmRight,
					sm : smRight,
					loadMask : true,
					tbar : tb,
					bbar : toolBarRight,
					border : false,
					viewConfig : {
						forceFit : true
					}
				});
//		var topPnl = new Ext.Panel({
//					layout : "border",
//					height : 200,
//					border : false,
//					items : [rightTopGrid]
//				});

		this.layout = "border";
		this.items = [rightTopGrid];
		MailWin.superclass.initComponent.call(this);
	},
	sendMail:function(prsOrder){
		var companyType = 1;
		if ($('flag').value == 2 || $('flag').value == 3) {
			companyType = 2;
		}
		var grid = Ext.getCmp("empgrid");
		var records = grid.getSelectionModel().getSelections();
		var list = [];
		Ext.each(records,function(record){
			var str = record.get("empsName")+","+record.get('empsMail');
			list.push(str);
		});
		mask();
		cotPanService.sendMailToEmps(list,this.prsForm.priceNo,companyType,function(res){
			if(res){
				Ext.Msg.alert('INFO','Send Mail Success');
			}else{
				Ext.Msg.alert('INFO','Send Mail Fail');
			}
			unmask();
		})
	}
})