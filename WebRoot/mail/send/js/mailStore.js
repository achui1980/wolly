Ext.namespace('Ext.mail');

/**
 * 通用ActionStore
 * @class Ext.data.ActionStore
 * @extends Ext.data.GroupingStore
 */
Ext.data.AddressStore = Ext.extend(Ext.data.Store,{
    constructor:function(config){
    	config = config || {};
    	this.proxy = new Ext.data.HttpProxy({
			url: config.url
		});
		this.reader = new Ext.data.JsonReader({
			root:'data',
			totalProperty:'totalCount',
			idProperty:'id'
		},Ext.data.Record.create([
			{name:'id',type:'int'},
			{name:'name',type:'string'},
			{name:'addressName',type:'string'},
			{name:'email',type:'string'}
		]));
    	Ext.data.AddressStore.superclass.constructor.call(this);	
    	this.setBaseParam('limit',EXT_MAIL_SEND_PAGE_LIMIT);
    }
});
Ext.mail.AutoCompleteStore = Ext.extend(Ext.data.Store,{
    constructor:function(config){
    	config = config || {};
    	this.proxy = new Ext.data.HttpProxy({
			url:config.url
		});
		this.reader = new Ext.data.JsonReader({
			root:'data',
			totalProperty:'totalCount'
		},[{name:'name'}]);
		
		if(config && config.data){
            this.inlineData = config.data;
            delete config.data;
        }
        Ext.apply(this, config);
        
    	Ext.mail.AutoCompleteStore.superclass.constructor.call(this);	
    }
});
