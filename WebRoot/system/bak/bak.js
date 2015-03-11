Ext.onReady(function(){

	var bakWin=new Ext.Window({
	title:"Database backup",
	width:400,
	height:300,
	layout:"form",
	
	
	plain:false,
	hideBorders:true,
	padding:"10",
	closable:false,
	buttonAlign:"center",
	y:15,
	fbar:[
		{
			text:"Backup",
			handler:function(){
				var form = Ext.getCmp("backupFormId");
				form.getForm().submit({
					url:"backup.do?method=backup",
					waitMsg:"Backups, please wait ...",
					success: function(fp, o){
						Ext.Msg.show({
				            title: "Backup completed",
				            msg:  o.result.msg,
				            minWidth: 400,
				            modal: true,
				            icon: Ext.Msg.INFO,
				            buttons: Ext.Msg.OK
				        });
					}
				})
			}
		}
	],
	items:[
		{
			xtype:"form",
			title:"",
			labelWidth:100,
			labelAlign:"left",
			formId:"backupForm",
			id:"backupFormId",
			hideBorders:true,
			frame:true,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"Server IP",
					id:"serverIp",
					name:"serverIp",
					value:$('ip').value,
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"Login Name",
					id:"loginName",
					name:"loginName",
					value:"root",
					anchor:"100%"
				},
				{
					xtype:"textfield",
					fieldLabel:"Password",
					id:"loginPassword",
					name:"loginPassword",
					value:"xqsco.ltd~2008",
					anchor:"100%",
					inputType:"password"
				},
				{
					xtype:"textfield",
					fieldLabel:"Database Name",
					id:"databaseName",
					name:"databaseName",
					value:"cotSystem",
					anchor:"100%"
				},
				{
					xtype:"checkbox",
					fieldLabel:"",
					boxLabel:"Backup a table in which",
					id:"backupCheck",
					name:"backupCheck",
					anchor:"100%",
					listeners:{
						check:function(chk,flag){
							var panel = Ext.getCmp("tbDiv")
							if(flag)
								panel.setVisible(true);
							else
								panel.setVisible(false);
						}
					},
					labelSeparator:" "
				},
				{
					xtype:"panel",
					title:"",
					layout:"form",
					id:"tbDiv",
					frame:true,
					hidden:true,
					hideBorders:true,
					padding:"0",
					items:[
						{
							xtype:"textfield",
							fieldLabel:"Table",
							id:"tableName",
							name:"tableName",
							anchor:"100%"
						}
					]
				}
			]
		}
		
	]
});
bakWin.show()
})