MailContactWin = Ext.extend(Ext.Window,{
	detailIds:[],
	prsForm:null,
	title : 'Send Staffs',
	initComponent:function(){
		var me = this;
		DWREngine.setAsync(false);
		var facData;
		// 加载厂家表缓存
		baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(
						res) {
					facData = res;
				});
		DWREngine.setAsync(true);
		
		var rec = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "empsName"
				}, {
					name : "empsMail"
				}]);
		var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0,
						limit : 200
						//panId : cfg.panId
					}
				},
				baseParams : {
					empsStatusFind : 0,
					limit : 200
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotemps.do?method=queryList"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, rec)
			});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel({
				columns : [sm, {
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
		var toolBar = new Ext.PagingToolbar({
				pageSize : 200,
				store : ds,
				displayInfo : true,
				displaySize : '5|10|15|20|300|500',
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});
		// 采购厂家
//	var facComb = new BindCombox({
//		dataUrl : "servlet/DataSelvert?tbname=CotFactory&key=shortName&typeName=factroyTypeidLv1",
//		cmpId : 'factoryIdFindL',
//		emptyText : "Supplier",
//		editable : true,
//		valueField : "id",
//		displayField : "shortName",
//		pageSize : 10,
//		width : 90,
//		sendMethod : "post",
//		selectOnFocus : false,
//		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
//		listWidth : 350,// 下
//		triggerAction : 'all',
//		isSearchField : true,
//		searchName : 'factoryIdFindL'
//		
//	});
	var tb = new Ext.ux.SearchComboxToolbar({
		items:['->',{
			text:'Send Mail',
			iconCls:'email_go',
			handler:this.sendMail.createDelegate(me)
		}]
	})
		var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				region : 'center',
				id:'contactGrid',
				store : ds,
				cm : cm,
				sm : sm,
				loadMask : true,
				tbar : tb,
				bbar : toolBar,
				border : false,
				viewConfig : {
					forceFit : true
				}
		});
		//选择中的联系人邮箱
		var selSm = new Ext.grid.CheckboxSelectionModel();
		var selGrid = new Ext.grid.GridPanel({
			title:'Selected Contact',
			id:'selGrid',
			region:'east',
			margins:'0 0 0 5',
			width:200,
			store : {
				xtype : "arraystore",
				fields : [{
							name : "contactMail",
							type : "string"
						}, {
							name : "id",
							type : "int"
						}]
			},
			cm:new Ext.grid.ColumnModel({
				columns:[selSm,{
					header:'contactMail',
					dataIndex:'contactMail',
					width:160
				}]
			}),
			sm:selSm,
			tbar:new Ext.Toolbar({
				items:['->',{
					text:'Send Mail',
					iconCls:'email_go',
					handler:this.sendMail.createDelegate(me)
				},{
					text:'Del',
					iconCls:'page_del',
					handler:this.delSel
				}]
			})
		})
		this.layout='border';
		this.items=[grid];
		this.closeAction='close';
		this.width = 700;
		this.height = 400;
		MailContactWin.superclass.initComponent.call(this);
	},
	doSelect:function(){
		var contactGrid = Ext.getCmp('contactGrid');
		var records = contactGrid.getSelectionModel().getSelections();
		var grid = Ext.getCmp("selGrid");
		var store = grid.getStore();
		Ext.each(records,function(item){
			var contactMail = item.get('empsMail');
			if (store.findExact("contactMail", contactMail) == -1){
				var u = new store.recordType({
					contactMail : contactMail
				})
				store.add(u);
			}
		})
	},
	delSel:function(){
		var grid = Ext.getCmp("selGrid");
		var store = grid.getStore();
		var records = grid.getSelectionModel().getSelections();
		store.remove(records);
	},
	sendMail:function(){
		var companyType = 1;
//		if ($('flag').value == 2 || $('flag').value == 3) {
//			companyType = 2;
//		}
		var contactGrid = Ext.getCmp('contactGrid');
		var records = contactGrid.getSelectionModel().getSelections();
		if(records.length == 0){
			Ext.Msg.alert("INFO","Please select Staff");
			return;
		}
		var list = [];
		Ext.each(records,function(item){
			list.push(item.get('empsMail'));
		});
		var me = this;
		var jsonParam = {
			panId:document.getElementById('pId').value,
			ids:me.detailIds,
			prsNo:me.prsForm.prsNo
		}
		mask();
		cotPanService.sendMailToFacContact(list,companyType,Ext.encode(jsonParam),function(res){
			if(res){
				Ext.Msg.alert('INFO','Send Mail Success');
			}else{
				Ext.Msg.alert('INFO','Send Mail Fail');
			}
			unmask();
		})
	}
});

Ext.reg('mailcontactwin',MailContactWin);