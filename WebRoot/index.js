Ext.onReady(function() {
			Ext.BLANK_IMAGE_URL = 'common/ext/resources/images/default/s.gif'
			var loginForm = new Ext.form.FormPanel({
						labelWidth : 55,
						renderTo : "login-div",
						labelAlign : "left",
						layout : "form",
						width : 270,
						height : 130,
						padding : "15px",
						border : false,
						buttonAlign : "center",
						bodyStyle : "background:transparent",
						frame : false,
						formId : "loginForm",
						keys : [{
									key : Ext.EventObject.ENTER,
									fn : login
								}, {
									key : Ext.EventObject.ALT,
									shift : true,
									fn : showReg
								}],
						fbar : [{
									text : "Login",
									iconCls : "key",
									handler : login
								}, {
									text : "Reset",
									iconCls : "page_reset",
									handler : function() {
										loginForm.getForm().reset();
									}
								}, {
									text : "Regiest",
									width : 40,
									hidden : true,
									id : "regeditBtn",
									handler : regeditAgain
								}, {
									text : "同步",
									hidden : true,
									id : "tongBtn",
									width : 40,
									handler : tongAgain
								}],
						items : [{
									xtype : "textfield",
									fieldLabel : "Username",
									id : "username",
									name : "username",
									anchor : "90%",
									style : "background:black;color:white",
									allowBlank : false,
									value : "admin",
									blankText : "Please Enter Username"
								}, {
									xtype : "textfield",
									fieldLabel : "Password",
									id : "pwd",
									name : "pwd",
									anchor : "90%",
									value : "123456",
									style : "background:black;color:white",
									inputType : "password",
									blankText : "Please Enter Password"
								}, {
									xtype : "textfield",
									fieldLabel : "Random.Pw",
									id : "otp",
									name : "otp",
									anchor : "90%",
									style : "background:black;color:white"
								}]
					});
			var pwd = Ext.getCmp("pwd");
			pwd.focus();
			// 加载cookie,初始化表单
			init();
			function showReg() {
				var btn = Ext.getCmp("regeditBtn");
				if (btn.isVisible())
					btn.setVisible(false);
				else
					btn.setVisible(true);

				var btn = Ext.getCmp("tongBtn");
				if (btn.isVisible())
					btn.setVisible(false);
				else
					btn.setVisible(true)
			}

			// 同步动态密码窗口
			var fp = new Ext.form.FormPanel({
						padding : "15px",
						frame:true,
						labelAlign : "left",
						buttonAlign : "center",
						labelWidth : 90,
						formId : "tongForm",
						fbar : [{
									text : "同步",
									iconCls : "key",
									handler : tong
								}, {
									text : "重置",
									iconCls : "page_reset",
									handler : function() {

										fp.getForm().reset();
									}
								}],
						items : [{
									xtype : "textfield",
									fieldLabel : "用户名",
									id : "tongUser",
									name : "tongUser",
									anchor : "90%",
									allowBlank : false,
									blankText : "请输入用户名"
								},{
									xtype : "numberfield",
									fieldLabel : "第一个动态密码",
									allowNegative:false,
									allowDecimals:false,
									id : "oneOtp",
									name : "oneOtp",
									anchor : "90%",
									allowBlank : false,
									blankText : "请输入第一个动态密码"
								},{
									xtype : "numberfield",
									fieldLabel : "第二个动态密码",
									allowNegative:false,
									allowDecimals:false,
									id : "twoOtp",
									name : "twoOtp",
									anchor : "90%",
									allowBlank : false,
									blankText : "请输入第二个动态密码"
								}]
					});

			var tongWin = new Ext.Window({
						title:'请输入2分钟内连续生成的动态密码',
						width : 300,
						height : 200,
						layout : 'fit',
						modal : true,
						closeAction:'hide',
						items : [fp],
						listeners:{
							'afterrender':function(pnl){
								Ext.getCmp('tongUser').setValue(Ext.getCmp('username').getValue());
								if(Ext.getCmp('username').getValue()!=''){
									Ext.getCmp('oneOtp').focus();
								}else{
									Ext.getCmp('tongUser').focus();
								}
							}
						}
					});
					
			function tongAgain() {
				tongWin.show();
			}
			
			function tong() {
				// 验证表单
				var formData = getFormValues(fp, true);
				// 表单验证失败时,返回
				if (!formData) {
					return;
				}
				
				var username=Ext.getCmp('tongUser').getValue();
				var oneOtp=Ext.getCmp('oneOtp').getValue();
				var twoOtp=Ext.getCmp('twoOtp').getValue();
				cotEmpsService.tong(username,oneOtp,twoOtp,function(res){
					if(res==0){
						Ext.Msg.alert('提示消息','Unknow UserName!',function(){
							Ext.getCmp('tongUser').focus();
						});
					}
					if(res==1){
						Ext.Msg.alert('提示消息','该用户名不需要动态密码就能登录,不用同步!',function(){
							Ext.getCmp('tongUser').selectText();
						});
					}
					if(res==2){
						Ext.Msg.alert('提示消息','同步失败!请输入2分钟内连续生成的动态密码,再不行联系管理员!');
					}
					if(res==3){
						Ext.Msg.alert('提示消息','同步成功!');
					}
				});
			}

		});