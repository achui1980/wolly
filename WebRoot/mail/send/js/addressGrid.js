Ext.mail.AddressGridSelecte = function(field,isSelected,record){
	var mailPersons = field.mailPersons;
	var oldValue = field.urlRange.oldValue;
	var selValue = record.get('email');
	var selName = record.get('addressName');
	var mailPerson = new Ext.mail.MailPerson();
	if(!mailPerson.setUrl(selValue))
		return;
	mailPerson.setName(selName);
	var exist = false; // 判断是否已存在email
	var rangeMailPerson = '';
	Ext.each(mailPersons,function(mailPerson){
		if(selValue==mailPerson.getUrl()){
			exist = true;
			rangeMailPerson = mailPerson;
			return false;
		}
	});
	if(isSelected){		
		if(exist) // 存在则不再添加
			return;
		mailPersons.push(mailPerson);
		if(oldValue!=''&&oldValue.lastIndexOf(';')!=oldValue.length-1){
			var startOVlaue = oldValue.substring(0,oldValue.lastIndexOf(';')+1);
			var endOValue = oldValue.substring(oldValue.lastIndexOf(';')+1);
			field.setValue(startOVlaue+mailPerson.toString()+";"+endOValue);
		}else
			field.setValue(oldValue+mailPerson.toString()+";");
	}else{
		if(!exist) // 不存在则不去除
			return;
		mailPersons.remove(rangeMailPerson);
		field.setValue(oldValue.replace(rangeMailPerson.toString()+";",''));
	}
	field.urlRange.saveValue(field.getValue());
};
Ext.mail.AddressGrid = Ext.extend(Ext.grid.GridPanel,{
	viewConfig:{
		forceFit:true
	},
	storeC:'',
	isReload:true, // 判断是否是重新加载，主要用来防止rowdeselect冲突
	initComponent:function(){
		var grid = this;
		grid.store = new Ext.data.AddressStore({url:this.url});
		var sm = new Ext.grid.CheckboxSelectionModel({
			onMouseDown : function(e, t){
		        if(e.button === 0 ){ // 让单击grid的单元格就可以添加或去除选中项
		            e.stopEvent();
		            var row = e.getTarget('.x-grid3-row');
					if(row){
		                var index = row.rowIndex;
		                if(this.isSelected(index)){
		                    this.deselectRow(index);
		                }else{
		                    this.selectRow(index, true);
		                }
		            }
		        }
		        this.mouseHandled = false;
		    },
		    defaults:{
	            menuDisabled: true 
        	},
        	listeners:{
        		rowselect:{
        			fn:function(selModel,rowIndex,record){
        				var personText = grid.ownerCt.personField.personText;
        				Ext.mail.AddressGridSelecte(personText,true,record);
        			}
        		},
        		rowdeselect:{
        			fn:function(selModel,rowIndex,record){
        				if(!grid.isReload){
        					var personText = grid.ownerCt.personField.personText;
        				 	Ext.mail.AddressGridSelecte(personText,false,record);
        				}
        			}
        		}
        	}
		});
		grid.colModel = new Ext.grid.ColumnModel({
			columns:[
	    		sm,
				{header:this.title, dataIndex: 'name'},
				{header:this.title+"联系人", dataIndex: 'addressName'},
				{header:'邮箱', dataIndex: 'email'}
	    	]
		});
		grid.sm = sm;
		grid.bbar = grid.bbar || new Ext.PagingToolbar({
        	plugins:new Ext.ux.ProgressBarPager(),
        	pageSize:10,
        	store:grid.store,
        	displayInfo:true
        });
        grid.store.on('load',function(store,records,options){
        	var personText = grid.ownerCt.personField.personText;
			var mailPersons = personText.mailPersons;
			for(var i=0;i<records.length;i++){
				var email = records[i].get('email');
				var name = records[i].get('addressName');
				if(Ext.isEmpty(email))
					continue;
				// 判断是否已存在email，如果存在则设置名字
				Ext.each(mailPersons,function(mailPerson){
					if(email==mailPerson.emailUrl){
						grid.getSelectionModel().selectRow(i,true);
						return false;
					}
				});
			}
			grid.isReload = false;
		});
		grid.tbar = ['->',{
				xtype:'bindCombo',
				dataUrl : this.bindComboUrl,
				editable : true,
				valueField : "name",
				width:120,
				displayField : this.bindDisplayField,
				ref:'../nameField',
				pageSize : 10,
				anchor : "100%",
				allowBlank : true,
				emptyText : this.title,
				sendMethod : "post",
				selectOnFocus : true,
				isInitDefault:false,
				listWidth : 250// 下
			},{
        	xtype:'searchcombo',
        	width:150,
        	store:grid.store,
        	queryStore:new Ext.mail.AutoCompleteStore({url:grid.autoUrl}),
        	displayField:'name',
        	valueField:'name',
        	autoComplet:true,
        	ref:'../seachField',
        	emptyText:'联系人名称',
        	paramFn:function(v,searchField){
        		var nameField = searchField.refOwner.nameField;
        		var name = nameField.getValue();
        		if(v || name)
					return {
						name:name,
						addressName:v
					}
				else
					return '';
        	},
        	clearParamFn:function(searchField){
        		var nameField = searchField.refOwner.nameField;
        		searchField.setValue('');
        		nameField.setValue('');
        	}
        }];
		Ext.mail.AddressGrid.superclass.initComponent.call(this);
	},
	listeners:{
		render:{
			fn:function(grid){
				grid.getStore().load({params:{start:EXT_MAIL_SEND_PAGE_START,limit:EXT_MAIL_SEND_PAGE_LIMIT}});
			}
		}
	}
});
Ext.mail.CustomerAddressGrid = Ext.extend(Ext.mail.AddressGrid,{
	title:'客户',
	url:'cotmailsend.do?method=queryCusAddress',
	autoUrl:'cotmailsend.do?method=readAutoCustAddressName',
	bindComboUrl:'cotmailsend.do?method=readCustName',
	bindDisplayField:'name',
	ref:'customerGrid'
});
Ext.mail.FactoryAddressGrid = Ext.extend(Ext.mail.AddressGrid,{
	title:'工厂',
	url:'cotmailsend.do?method=queryFactoryAddress',
	autoUrl:'cotmailsend.do?method=readAutoFactoryAddressName',
	bindComboUrl:'cotmailsend.do?method=readFactoryName',
	bindDisplayField:'name',
	ref:'factoryGrid'
});