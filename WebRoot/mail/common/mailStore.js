Ext.namespace('Ext.mail');

Ext.mail.LocalInboxStore = Ext.extend(Ext.data.GroupingStore,{
	mappingGroupDate:'sendTime',
	autoDestroy: true,
//    groupOnSort: true,
	remoteSort:true,
	groupField:'groupDate',
    constructor:function(config){
    	config = config || {};
    	this.proxy = new Ext.data.HttpProxy({
			url: config.url?config.url:'cotmail.do?method=readLocalMails'
		});
		var record = [
			{name:'id',type:'string'},
			{name:'msgId',type:'string'},
			{name:'number',type:'int'},
			{name:'sender'},
			{name:"subject",type:'string'},
			{name:'body',type:'string'},
			{name:'to'},		
			{name:'cc'},		
			{name:'empId',type:'int'},
			{name:'facId',type:'int'},
			{name:'custId',type:'int'},
			{name:'size',type:'int'},
			{name:'isContainAttach',type:'boolean'},
			{name:'isNotification',type:'boolean'},
			{name:'errMessage',type:'string'},
			{name:'mailStatus',type:'int'},
			{name:'mailTag',type:'string'},
			{name:'attachs'},
			{name:'mailType',type:'int'},
			{name:'sendTime',sortType:timeSortType.createDelegate(this)},
			{name:'addTime',sortType:timeSortType.createDelegate(this)}
		];
		record.push({name: 'groupStatus',mapping:'mailStatus'});
		record.push({name: 'groupEmp',mapping:'empId'});
		record.push({name: 'groupSender',mapping:'sender'});
		record.push({name: 'groupSubject',mapping:'subject'});
		record.push({name: 'groupAttach',mapping:'isContainAttach'});
		record.push({name: 'groupDate',mapping:config.mappingGroupDate?config.mappingGroupDate:this.mappingGroupDate,sortType:timeSortType.createDelegate(this)});
		record.push({name: 'groupSize',mapping:'size'});
		this.reader = new Ext.data.JsonReader({
			root:'data',
			totalProperty:'totalCount',
			idProperty:'id'
		},Ext.data.Record.create(record));
		if(config && config.data){
            this.inlineData = config.data;
            delete config.data;
        }
        this.sortInfo = {field: config.mappingGroupDate?config.mappingGroupDate:this.mappingGroupDate, direction: 'DESC'},
        Ext.apply(this, config);
    	Ext.mail.LocalInboxStore.superclass.constructor.call(this);	
    	this.setBaseParam('limit',EXT_MAIL_PAGE_LIMIT);
    },
    listeners:{
    	beforeload:{
    		fn:function(store,options){
    			var params = options.params;
    			if(params.sort=='sendTime'||params.sort=='addTime'){
    				this.groupField = 'groupDate';
    			}else if(params.sort=='isContainAttach'){
    				this.groupField = 'groupAttach';
    			}else if(params.sort=='size'){
    				this.groupField = 'groupSize';
    			}else if(params.sort=='subject'){
    				this.groupField = 'groupSubject';
    			}else if(params.sort=='mailStatus'){
    				this.groupField = 'groupStatus';
    			}else if(params.sort=='sender'){
    				this.groupField = 'groupSender';
    			}else if(params.sort=='empId'){
    				this.groupField = 'empId';
    			}else if(params.sort=='custId'){
    				this.groupField = 'custId';
    			}
    			var viewport = Ext.getCmp(EXT_MAIL_ID_VIEWPORT);
    			if(viewport&&viewport.mailTree){
	    			var showField = viewport.mailTree.showChildMailField;
	    			if(showField)
	    				this.setBaseParam('isShowChildMail',showField.getValue());
    			}
    		}
    	},
    	load:{
    		fn:function(store,records,options){
				var mailGrid = EXT_VIEWPORT.mailGrid;
				if(mailGrid.mailType=='mail'){
					Ext.mail.ToolbarControl();
					// 历史
					Ext.mail.ComeAndGoMailControl();
				}
				if(mailGrid&&mailGrid.store){
					mailGrid.fireEvent('rowclick',mailGrid);
    			}
    		}
    	}
    }
});
Ext.mail.AutoCompleteStore = Ext.extend(Ext.data.Store,{
    constructor:function(config){
    	config = config||{};
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
    },
    listeners:{
    	beforeload:{
    		fn:function(store,options){
    			var viewport = Ext.getCmp(EXT_MAIL_ID_VIEWPORT);
    			var showField = viewport.mailTree.showChildMailField;
    			if(showField)
    				this.setBaseParam('isShowChildMail',showField.getValue());
    		}
    	}
    }
});
Ext.mail.HistoryStore = Ext.extend(Ext.data.Store,{
	idProperty:'id',
	totalProperty:'totalCount',
	root:'data',
	url:'',
	/**
	 * @cfg {Ext.data.Record} record 
	 */
	record:[],
    constructor:function(){
    	this.proxy = new Ext.data.HttpProxy({
			url: this.url
		});
		this.reader = new Ext.data.JsonReader({
			root:this.root,
			totalProperty:this.totalProperty,
			idProperty:this.idProperty
		},Ext.data.Record.create(this.record));
    	Ext.mail.HistoryStore.superclass.constructor.call(this);	
    }
});
	
Ext.mail.ComeAndGoStore = Ext.extend(Ext.mail.HistoryStore,{
	url:'cotmail.do?method=readComeAndGoMails',
	record:[
		{name:'id',type:'string'},
		{name:"subject",type:'string'},
		{name:'sendTime',sortType:timeSortType.createDelegate(this)},
		{name:'isContainAttach',type:'boolean'},
		{name:'mailType',type:'int'}
	],
	baseParams:{
     	limit : EXT_MAIL_HISTORY_PAGE_LIMIT
    }
});
Ext.mail.ComeAndGoAttachStore = Ext.extend(Ext.mail.HistoryStore,{
	url:'cotmail.do?method=readComeAndGoAttachs',
	record:[
		{name:'id',type:'string'},
		{name:"mailId",type:'string'},
		{name:"name",type:'string'},
		{name:"size",type:'int'},
		{name:"url",type:'string'},
		{name:'sendTime',sortType:timeSortType.createDelegate(this)},
		{name:"mailType",type:'int'}
	],
	baseParams:{
     	limit : EXT_MAIL_HISTORY_PAGE_LIMIT
    }
});
