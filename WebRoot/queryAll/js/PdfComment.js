PdfComment = Ext.extend(Ext.Window,{
	title:'General Comment',
	type:'',
	closeAction:'close',
	width:300,
	height:300,
	//modal : true,
	frame:true,
	layout:'fit',
	orderFacId:0,
	initComponent:function(){
		DWREngine.setAsync(false);
		var orderfac;
		cotOrderFacService.getOrderFacById(this.orderFacId,function(res){
			orderfac = res;
		});
		var me = this;
		this.items = [{
			xtype:'form',
			id:'commentForm',
			frame:true,
			labelWidth:60,
			items:[{
				xtype:'textarea',
				fieldLabel:'Comment',
				value:orderfac["approvalComment"+this.type],
				anchor : "98%",
				allowBlank:false,
				id:"approvalComment"+this.type
			},{
				xtype:'combo',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				store : new Ext.data.SimpleStore({
					fields : ["tp", "name"],
					data : [[9,''],[0, 'Yes'], [1, 'No']]
				}),
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				fieldLabel:'Wash',
				anchor : "98%",
				hidden:this.type == 'sampleOut'?false:true,
				value:orderfac[this.type+"Wash"],
				hideLabel:this.type == 'sampleOut'?false:true,
				id:this.type+"Wash",
				listeners:{
					'select':function( combo, record,  index){
						if(record.get('tp') == 0){
							Ext.getCmp('degree').setReadOnly(false);
							Ext.getCmp('degree').focus();
						}else{
							Ext.getCmp('degree').setReadOnly(true);
						}
					}
				}
			},{
				xtype:'numberfield',
				hidden:this.type == 'sampleOut'?false:true,
				id:'degree',
				anchor : "98%",
				hideLabel:this.type == 'sampleOut'?false:true,
				fieldLabel:'°C'
			},{xtype:'combo',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				store : new Ext.data.SimpleStore({
					fields : ["tp", "name"],
					data : [[9,''],[0, 'Yes'], [1, 'No']]
				}),
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				fieldLabel:'Passed',
				hideLabel:this.type == 'sampleOut'?false:true,
				hidden:this.type == 'sampleOut'?false:true,
				anchor : "98%",
				value:orderfac[this.type+"Passed"],
				id:this.type+"Passed"
			},{
				xtype:'combo',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				store : new Ext.data.SimpleStore({
					fields : ["tp", "name"],
					data : [[9,''],[0, 'ALL'], [1, 'Partial']]
				}),
				valueField : "tp",
				displayField : "name",
				mode : 'local',
				fieldLabel:'Approved',
				anchor : "98%",
				value:orderfac[this.type+"Approve"],
				id:this.type+"Approve"
			},{
				xtype:'textfield',
				id:'approvePassowrd',
				anchor : "98%",
				fieldLabel:'Password'
				
				
			}]
		}]
		this.buttons = [{
			text:'save'	,
			handler:me.doComment.createDelegate(me,[])
		},{
			text:'Detail',
			hidden:me.type == 'sampleOut'?false:true,
			handler:me.showDetail.createDelegate(me,[])
		}]
		PdfComment.superclass.initComponent.call(this);
		DWREngine.setAsync(true);
	},
	doComment:function(){
		//1、保存到生产合同
		//2、生成PDF
		//3、上传PDF
		//4、写入day report
		var form = Ext.getCmp('commentForm');
		if(!form.getForm().isValid())return;
		window.mask();
		var values = form.getForm().getValues();
		var comment = values["approvalComment"+this.type];
		var approve = {};
		approve['approve'] = values[this.type+"Approve"];
		approve['passed'] = values[this.type+"Passed"];
		approve['washed'] = values[this.type+"Wash"];
		approve['degree'] = values["degree"];
		//var approve = values[this.type+"Approve"];
		var password = values['approvePassowrd'];
		var me = this;
		//没有密码就默认保存
		if(Ext.isEmpty(password)){
			cotOrderFacService.doComment(this.orderFacId,comment,Ext.encode(approve),this.type,function(){
				console.log(approve)
				window.unmask();
				Ext.Msg.alert('Info','Update Success!');
				me.close();
			});
		}else{
			//有输入密码，就直接ｃｏｎｆｉｒｍ
			cotOrderFacService.doComment(this.orderFacId,comment,Ext.encode(approve),this.type,function(){
				me.confirmPassword(password);
			});
			
		}
	},
	confirmPassword:function(password){
		//权限验证
		var me = this;
		if(getPdmByOtherUrl('cotorderstatus.do','PASSWORDAPPROVAL') == 0){
			Ext.Msg.alert('Info','You do not have passowrd approval right');
			window.unmask()
			return;
		}else if(password !== loginEmp.passwordApproval){
			Ext.Msg.alert('Info','Confirm Password error! Please try again!');
			window.unmask()
			return;
		}else{
			cotOrderFacService.confirmByPassword(this.orderFacId,this.type,function(){
				Ext.Msg.alert('Info','Confirm Success!');
				window.unmask();
				me.close();
				if(me.type == 'sampleOut')
					window.reflashParent('orderfacGrid');
				else
					window.reloadGrid('orderfacGrid');
			});
		}
	},
	showDetail:function(){
		var me = this;
		var detailWin = new OrderfacdetailCopyWin({
			orderFacId:me.orderFacId,
			listeners:{
				'beforeclose':function(){
					var close = false;
					if(!detailWin.isShowCommentWin()){
//						Ext.Msg.confirm("Message","All deails should be approval.Do you want to close this window?",function(btn){
//							if(btn == 'yes'){
//								close = true;
//							}
//						});
						close = confirm('All deails should be approval.Do you want to close this window?');	
						return close;
					}
				},
				"close":function(){
					if(detailWin.isShowCommentWin()){
						me.show();
					}else
						me.close();
				}
			}
		});
		detailWin.show();
	}
})