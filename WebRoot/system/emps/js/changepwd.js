Ext.onReady(function(){

	var pwdWin=new Ext.Window({
	title:"Change Password",
	width:500,
	height:180,
	layout:"form",
	closable:false,
	plain:true,
	draggable:false,
	padding:"5px",
	labelAlign:"right",
	hideBorders:false,
	labelWidth:130,
	monitorValid:true,
	buttonAlign:"center",
	y:15,
	fbar:[
		{
			text:"Change Password",
			iconCls : "key",
			handler:changePwd
		}
	],
	items:[
		{
			xtype:"textfield",
			fieldLabel:"Old Password",
			id:"oldpwd",
			name:"oldpwd",
			inputType:"password",
			allowBlank:false,
			blankText:"Enter your old password",
			anchor:"100%"
		},
		{
			xtype:"textfield",
			fieldLabel:"New Password",
			id:"newpwd",
			name:"newpwd",
			inputType:"password",
			allowBlank:false,
			blankText:"Please enter a new password",
			anchor:"100%"
		},
		{
			xtype:"textfield",
			fieldLabel:"Confirm Password",
			id:"confirmpwd",
			name:"confirmpwd",
			inputType:"password",
			allowBlank:false,
			blankText:"Please re-enter new password",
			anchor:"100%",
			vtype: 'password',
			initialPassField: 'newpwd'
		}
	]
});
pwdWin.show();
})