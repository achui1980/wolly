
Ext.ux.form.SearchTriggerField = Ext.extend(Ext.form.TriggerField,{
	triggerClass:'x-form-clear-trigger',
	hideTrigger:true,
	enableKeyEvents:true,
	seachStore:'', // 要被查询Store
	onTriggerClick:function(){
		this.setValue('');
		this.setHideTrigger(true);
		this.seachStore.setBaseParam('seachName',this.getValue());
		this.seachStore.load({
			params:{
				start:EXT_MAIL_HISTORY_PAGE_START,
				limit:EXT_MAIL_HISTORY_PAGE_LIMIT
			}
		});
	},
	listeners:{
		keyup:{
			fn:function(field,e){
				if(Ext.isEmpty(field.getValue())){
					field.setHideTrigger(true);
				}else{
					field.setHideTrigger(false);
				}
				this.seachStore.setBaseParam('seachName',this.getValue());
				this.seachStore.load({
					params:{
						start:EXT_MAIL_HISTORY_PAGE_START,
						limit:EXT_MAIL_HISTORY_PAGE_LIMIT
					}
				});
			},
			buffer:500
		}
	}
});

Ext.reg('searchTrigger',Ext.ux.form.SearchTriggerField);