Ext.onReady(function(){
		var form = new Ext.form.FormPanel({
			title:'添加签名',
			labelSeparator :'：',
			labelWidth : 60,
			bodyStyle:'padding:5 5 5 5',
			frame : true,
			items:[{
				xtype:'htmleditor',
				fieldLabel:"签名内容",
				height:408,
				anchor:"99%",
				name:"body",
				fontFamilies : FontFamilies,//字体设置在extcommon.jsp
				enableSourceEdit:false,//源代码
				defaultFont : '宋体',//默认字体
				name:"htmleditorBody",
				id:"htmleditorBody"
			}],
			buttonAlign : 'center',
			buttons:[{
				text:'保存',
				handler:function(){
					var cotEmp =new CotEmps();
					var empsSign=$('htmleditorBody').value;
					var empId=$('empsId').value;
					mailTemplateService.savecotEmps(empId,empsSign,function(rs){
						if(rs){
							Ext.Msg.alert('提示消息','保存成功',function(bnt){
								if(bnt=='ok'||bnt=='cancel'){
									window.close();
									//openWindowBase(580,800,'cotmailsend.do?method=query');  
								}
							});
						}else{
							Ext.Msg.alert('提示消息','保存失败,输入框的签名设置过长')
						}
					});
				}
			},{
				text:'重置',
				handler:function(){
					form.getForm().reset();
					//window.close();
				}
			}]
		});
		var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [form]
			});
	   viewport.doLayout();
	   function initForm(){
	   		var empId=$('empsId').value;
	   		mailTemplateService.isExistEmpsSign(empId,function(rs){
	   			if(rs){
	   				form.setTitle('修改签名');
	   				$('htmleditorBody').value=rs;
	   			}
	   		})
	   }
	   initForm();
	});