Ext.namespace('Ext.mail.Form');
/**
 * 收件人Field键盘按下事件，调用该方法必须加上时间延迟执行
 * @param {} field
 * @param {} e
 */
Ext.mail.Form.PersonFieldKeyDow = function(field,e){
	var eKey = e.getKey();
	if(eKey==Ext.EventObject.CTRL){
		field.isCtrl = true;
		return ;
	}
	var urlRange = field.urlRange;
	var mailPersons = field.mailPersons;
	if(field.isCtrl&&(eKey==Ext.EventObject.C)){
		return ;
	}
	// 合并已保存符合条件的URL地址
	var urlIndexs = [-1];
	var urlsStr = '';
	Ext.each(mailPersons,function(mailPerson){
		urlsStr += mailPerson.toString()+";";
		urlIndexs.push(urlsStr.length-1);
	});
	var selectionStart = field.el.dom.selectionStart; //获得光标位置
	if(eKey == Ext.EventObject.LEFT){
		for(var i=0;i<urlIndexs.length;i++){
			if(urlIndexs[i]+1 > selectionStart){
				field.selectText(urlIndexs[i-1]+1,urlIndexs[i-1]+1)
				return false;
			}
		}
		return;
	}
	if(eKey == Ext.EventObject.RIGHT){
		if(selectionStart > urlIndexs[urlIndexs.length-1])
			return;
		for(var i=urlIndexs.length-2;i>=0;i--){
			if(urlIndexs[i]+1 < selectionStart){
				field.selectText(urlIndexs[i+1]+1,urlIndexs[i+1]+1)
				return false;
			}
		}
		return;
	}
	if(eKey == Ext.EventObject.UP||eKey == Ext.EventObject.DOWN)
		return;
	var newValue = field.getValue();
	urlRange.range(newValue);
	var result = false; // 是否符合条件，符合则修改，否则还原值
	if(urlRange.start>=urlRange.lastIndex){ // 在后面修改
		result = true;
		//当给最后面值加上分号时，则进行检查邮件格式，符合则保存，否则去掉
		if(newValue.charAt(newValue.length-1)==';'){
			var mailPerson = new Ext.mail.MailPerson();
			var lastValue = newValue.substring(urlRange.lastIndex,newValue.length-1);
			newValue = newValue.substring(0,urlRange.lastIndex)
			if(mailPerson.setUrl(lastValue)){
				var exist = false; // 判断是否已存在email，如果存在则不加
				Ext.each(mailPersons,function(mailPerson){
					if(lastValue==mailPerson.emailUrl){
						exist = true;
						return false;
					}
				});
				if(!exist){
					mailPersons.push(mailPerson);
					newValue += mailPerson.toString()+";";
				}
			}
			field.setValue(newValue);
		}
	}else if(urlRange.modifyOld==''){ // 增 只能是邮件格式
		var index = urlIndexs.indexOf(urlRange.start-1);
		if(index!=-1){ // 只能是邮件格式，并在已合格的邮件地址后面加入
			var regExp= /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
			if(regExp.test(urlRange.modifyNew)&&urlRange.oldValue.indexOf(urlRange.modifyNew)==-1){
				var exist = false; // 判断是否已存在email，如果存在则不加
				Ext.each(mailPersons,function(mailPerson){
					if(urlRange.modifyNew==mailPerson.emailUrl){
						exist = true;
						return false;
					}
				});
				if(!exist){
					result = true;
					var mailPerson = new Ext.mail.MailPerson();
					mailPerson.setUrl(urlRange.modifyNew);
					newValue = urlRange.oldValue.substring(0,urlRange.start)
							+mailPerson.toString()+';'
							+urlRange.oldValue.substring(urlRange.end+1);
					var selectIndex=(urlRange.oldValue.substring(0,urlRange.start)
							+mailPerson.toString()+';').length;
					field.selectText(selectIndex,selectIndex);
					var newMailPersons = [];
					for(var i=0;i<=mailPersons.length;i++){
						if(index>i)
							newMailPersons.push(mailPersons[i]);
						else if(index==i)
							newMailPersons.push(mailPerson);
						else
							newMailPersons.push(mailPersons[i-1]);
					}
					field.mailPersons = newMailPersons;
					field.setValue(newValue);
				}
			}
		}
	}else if(urlRange.modifyNew==''){ // 删  只能删除分号时，才能删除对象
		if(urlRange.modifyOld == ';'){
			result = true;
			var index = urlIndexs.indexOf(urlRange.start);
			var len = urlRange.start-mailPersons[index-1].toString().length;
			newValue = urlRange.oldValue.substring(0,len)
				+urlRange.oldValue.substring(urlRange.start+1);
			field.selectText(len,len);
			mailPersons.remove(mailPersons[index-1]);
			field.setValue(newValue);
		}
	}
	if(result){
		urlRange.saveValue(newValue);
	}else{
		field.setValue(urlRange.oldValue);
		field.selectText(urlRange.start,urlRange.start)
	}
}
/**
 * 收件人字段
 * @class Ext.mail.Form.PersonField
 * @extends Ext.FormPanel
 */
Ext.mail.Form.PersonField = Ext.extend(Ext.Panel,{
	layout:'column',
	name:'',
	textLabel:'',
	initComponent:function(){
		var personField = this;
		personField.items = [{ 
			columnWidth:.9,
			layout:'form',
			items:[{
				xtype:'textfield',
				fieldLabel:personField.textLabel,
				ref:'../personText',
				anchor:'99%',
				mailPersons:[],// 保存符合条件的MailPerson对象
				urlRange:new Ext.mail.UrlRange(), // 整个邮件地址
				name:this.name,
				enableKeyEvents:true,
				listeners:{
					keyup:{
						fn:function(field,e){
							if(e.getKey()==Ext.EventObject.CTRL)
								field.isCtrl = false;
						}
					},
					keydown:{
						fn:Ext.mail.Form.PersonFieldKeyDow,
						delay:1
					},
					change:{
						scope:this,
						fn:function(field,newValue,oldValue){
							this.fireEvent('fieldchange',field,field.mailPersons,newValue);
						}
					}
				},
				setValue : function(v){
			        this.value = v;
			        if(this.rendered){
			            this.el.dom.value = (v === null || v === undefined ? '' : v);
			            this.validate();
			        }
			        personField.fireEvent('fieldchange',this,this.mailPersons,v);
			    }
			}]
		},{
		    columnWidth:.1,
		    items:[{
		        xtype:'button',
		        text:personField.textLabel,
		        width:'90%',
		        ref:'../personBtn',
		        window:new Ext.Window({
	        		title:personField.textLabel,
	        		width:500,
	        		height:372,
	        		shadow:true,
	        		constrain:true,
	        		closeAction:'hide',
	        		layout:'fit',
	        		resizable:true,
	        		modal:true,
	        		items:[new Ext.mail.AddressTab({personField:personField})]
	        	}),
		        handler:function(btn,e){
		        	btn.window.show();
		        	var factoryGrid =btn.window.addressTab.factoryGrid;
		        	var customerGrid =btn.window.addressTab.customerGrid;
		        	if(!factoryGrid.isReload){
			        	factoryGrid.isReload = true;
			        	factoryGrid.getSelectionModel().clearSelections();
			        	factoryGrid.getStore().reload();
		        	}
		        	if(!customerGrid.isReload){
			        	customerGrid.isReload = true;
			        	customerGrid.getSelectionModel().clearSelections();
			        	customerGrid.getStore().reload();
		        	}
		        }
		    }] 
		}];
		Ext.mail.Form.PersonField.superclass.initComponent.call(this);
		/**
		 * 邮件地址改变
		 */
		this.addEvents('fieldchange');
	}
});
Ext.reg('personfield',Ext.mail.Form.PersonField);