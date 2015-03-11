Ext.namespace('Ext.mail');

$import('common/js/adv-vtypes.js');
$import('common/js/Form.js');
$import('common/js/SearchComboxField.js');

$import('mail/common/mailConstants.js');
$import('mail/common/mailFn.js');
$import('mail/common/mailStore.js');
$import('mail/common/SearchField.js');
$import('mail/common/mailDefault.js');
$import('mail/common/mailGrouping.js');
$import('mail/common/mailCard.js');
$import('mail/common/mailMenu.js');
$import('mail/common/mailRenderer.js'); 

$import('mail/inbox/js/mailTree.js');
$import('mail/inbox/js/mailToolbar.js');

/**
 * 邮件主视图
 * @class Ext.mail.Viewport
 * @extends Ext.Viewport
 */
Ext.mail.Viewport = Ext.extend(Ext.Viewport,{
	layout:'border',
	initComponent: function(){
		this.items=[{
			xtype:'mailtoolbar',
			region:'north',
			height:28,
			ref:'mailToolbar'
		},{
			xtype:'mailtree',
			region:'west',
		    ref:'mailTree',
		    width: 200,
		    maxSize:350,
		    minSize:165,
		    margins:'5 0 5 0',
		    collapseMode:'mini',
		    split: true,
			autoScroll : true
		},{
	    	region:'center',
	    	margins:'5 0 5 0',
	    	layout:'card',
	    	ref:'mailCard',
	    	border:false,
	    	activeItem:0,
	    	items:[{
	    		layout:'border',
	    		border:false,
	    		items:[{
		    		region:'center',
		    		layout:'card',
		    		border:false,
		    		ref:'../../mailAllGrid',
		    		itemCount:1, // card里面加入的Item数,在树点击时加入
		    		activeItem:0,
		    		items:[new Ext.Panel()]
		    	},{
		    		xtype:'maildefault',
					ref:'../../mailDefault',
					region:'south',
					split:true,
					collapseMode:'mini',
					height:200,
					maxSize:350,
		    		minSize:100,
					hideClose:true,
					listeners:{
			    		expand:function(panel){
			    			navHandler('this',EXT_VIEWPORT.mailGrid,EXT_VIEWPORT.mailDefault);
			    		}
	    			}
		    	}]
	    	},{
	    		xtype:'maildefault',
				ref:'../dbMailDefault'
	    	}]
	    },{
	    	xtype:'historycard',
	    	ref:'historyCard',
	    	region:'east',
	    	width:250,
	    	split:true,
	    	collapsed:false,
	    	maxSize:800,
	    	minSize:150,
	    	collapseMode:'mini',
	    	margins:'5 0 5 0',
	    	listeners:{
	    		expand:function(panel){
	    			Ext.mail.ComeAndGoMailControl();
	    		},
    		 	collapse:function(panel){
    		 		Ext.mail.ComeAndGoMailControl();
    		 	}
	    	}
	    }];
		Ext.mail.Viewport.superclass.initComponent.call(this);
		this.dbMailDefault.on({
			'closeBtn':{
				fn:function(mailDefault){
					EXT_VIEWPORT.mailCard.layout.setActiveItem(0);
					showSelDefault(EXT_VIEWPORT);
					if(EXT_VIEWPORT.mailDefault.collapsed){
						return;
					}
					navHandler('this',EXT_VIEWPORT.mailGrid,EXT_VIEWPORT.mailDefault);
				}
			},
			'pageBtn':{
				fn:function(mailDefault,hanType){
					navHandler(hanType,EXT_VIEWPORT.mailGrid,mailDefault);
				}
			},
			'modifyBtn':{
				fn:function(mailDefault){
					var record = getSelDatas(EXT_VIEWPORT.mailGrid)[0];// 选中的数据
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+record.id+'&status='+MAIL_SEND_TYPE_STATUS_UPDATE); 
				}
			}
		});
		this.mailDefault.on({
			'pageBtn':{
				fn:function(mailDefault,hanType){
					navHandler(hanType,EXT_VIEWPORT.mailGrid,mailDefault);
				}
			},
			'modifyBtn':{
				fn:function(mailDefault){
					var record = getSelDatas(EXT_VIEWPORT.mailGrid)[0];// 选中的数据
					openWindowBase(500,800,'cotmailsend.do?method=query&mailId='+record.id+'&status='+MAIL_SEND_TYPE_STATUS_UPDATE); 
				}
			}
		});
	}
});

Ext.onReady(function(){
	Ext.getBody().on({
		'contextmenu':{
			fn:function(e){
//				e.preventDefault();
			}
		}
	});
	empsMap = '';
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
		empsMap = res;
	});
	custMap = '';
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName", function(res) {
		custMap = res;
	});
	facMap = '';
	baseDataUtil.getBaseDicDataMap("CotFactory", "id", "shortName", function(res) {
		facMap = res;
	});
	EXT_VIEWPORT = new Ext.mail.Viewport({id:EXT_MAIL_ID_VIEWPORT});
	EXT_VIEWPORT.historyCard.collapse(false);
	EXT_VIEWPORT.mailDefault.collapse(false);
	EXT_VIEWPORT.mailDefault.hide();
	
	new Ext.mail.Menu.person({
		id:EXT_MAIL_PERSON_MENU
	});
});
