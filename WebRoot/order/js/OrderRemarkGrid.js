OrderRemarkGrid = Ext.extend(Ext.grid.GridPanel,{
	/**
	 * 订单ID
	 * @type Number
	 */
	orderId:0,
	parentStore:null,
	module:'ORDER',
	initComponent:function(){
		var me = this;
		var roleRecordRight = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "orderId",type:'int'
				}, {
					name : "remark"
				},{
					name : "addPerson"
				},{
					name : "addTime"
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
				url : "cotorder.do?method=queryOrderRemark&orderId="+me.orderId
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
								header : "OrderId",
								dataIndex : "orderId",
								hidden:true
							}, {
								header : "Remark",
								dataIndex : "remark",
								width : 200
							},{
								header:"Add Time",
								dataIndex:'addTime',
								renderer:function(value){
									if (value != null) {
										return Ext.util.Format.date(new Date(value.year+1900,
											value.month, value.date), "d/m/Y");
									}
								}
							},{
								header:'Staff',
								dataIndex:'addPerson'
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
				text:'Add',
				hidden:me.module == 'ORDERFAC'?true:false,
				iconCls:'page_add',
				handler:me.addRemarkWin.createDelegate(me,[])
			}]
		});
		this.stripeRows = true;
		this.store = dsRight;
		this.cm = cmRight;
		this.sm = smRight;
		this.loadMask = true;
		this.tbar = tb;
		this.bbar = toolBarRight;
		this.border = false,
		this.viewConfig = {
			forceFit : true
		}
		OrderRemarkGrid.superclass.initComponent.call(this);
	},
	addRemarkWin:function(){
		var orderId = this.orderId;
		var me = this;
		var win = new Ext.Window({
			closeAction:'close',
			title:'Add Remark',
			layout:'form',
			width:400,
			height:100,
			frame:true,
			items:[{
				xtype:'textfield',
				fieldLabel:'remark',
				id:'newRemark',
				anchor:'100%'
			}],
			buttons:[{
				text:'Save',
				handler:function(){
					var remark = Ext.getCmp('newRemark').getValue();
					cotOrderStatusFileService.saveNewRemark(me.orderId,loginEmpId,remark,function(res){
						me.parentStore.reload();
						me.store.reload();
						win.close();
					});
				}
			}]
		});
		win.show()
	}
})