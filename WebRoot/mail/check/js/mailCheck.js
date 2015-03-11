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


/**
 * 邮件主视图
 * @class Ext.mail.Viewport
 * @extends Ext.Viewport
 */
Ext.mail.Viewport = Ext.extend(Ext.Viewport,{
	layout:'fit',
	initComponent: function(){
		this.items = [{
	    	margins:'5 5 5 5',
	    	layout:'card',
	    	ref:'mailCard',
	    	border:false,
	    	activeItem:0,
	    	items:[{
	    		layout:'border',
	    		border:false,
	    		items:[{
		    		region:'center',
		    		xtype:'checkgrid',
		    		border:false,
		    		mailType:'check',
		    		ref:'../../mailGrid'
		    	},{
		    		xtype:'maildefault',
					ref:'../../mailDefault',
					mailType:'check',
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
				ref:'../dbMailDefault',
				mailType:'check'
	    	}]	
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
			'checkBtn':{
				fn:function(mailDefault,isCheck){
					var sendTypeText = '审核';
    				var record = getSelDatas(EXT_VIEWPORT.mailGrid)[0];// 选中的数据
    				var loadMask = new Ext.LoadMask(EXT_VIEWPORT.getEl(),{msg:sendTypeText+'中。。。'});
					loadMask.show();
    				mailSendService.checkSend(isCheck,record.id,null,function(result){
    					loadMask.hide();
    					Ext.mail.sendInfoFn(result,sendTypeText);
    					reloadSelData(EXT_VIEWPORT.mailGrid); // 重新加载选中GRID的数据
    					hideSelDefault(EXT_VIEWPORT); // 隐藏面板
    					EXT_VIEWPORT.mailCard.layout.setActiveItem(0);
					});
				}
			}
		});
		this.mailDefault.on({
			'pageBtn':{
				fn:function(mailDefault,hanType){
					navHandler(hanType,EXT_VIEWPORT.mailGrid,mailDefault);
				}
			},
			'checkBtn':{
				fn:function(mailDefault,isCheck){
					var sendTypeText = '审核';
    				var record = getSelDatas(EXT_VIEWPORT.mailGrid)[0];// 选中的数据
    				var loadMask = new Ext.LoadMask(EXT_VIEWPORT.getEl(),{msg:sendTypeText+'中。。。'});
					loadMask.show();
    				mailSendService.checkSend(isCheck,record.id,null,function(result){
    					loadMask.hide();
    					Ext.mail.sendInfoFn(result,sendTypeText);
    					reloadSelData(EXT_VIEWPORT.mailGrid); // 重新加载选中GRID的数据
    					hideSelDefault(EXT_VIEWPORT); // 隐藏面板
					});
				}
			}
		});
	}
});



Ext.onReady(function(){
	Ext.getBody().on({
		'contextmenu':{
			fn:function(e){
				e.preventDefault();
			}
		}
	});
	empsMap = '';
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
		empsMap = res;
	});
	EXT_VIEWPORT = new Ext.mail.Viewport({id:EXT_MAIL_ID_VIEWPORT});
	EXT_VIEWPORT.mailGrid.getStore().load({params:{start:EXT_MAIL_PAGE_START,limit:EXT_MAIL_PAGE_LIMIT}});
	EXT_VIEWPORT.mailDefault.collapse(false);
	EXT_VIEWPORT.mailDefault.hide();
	new Ext.mail.Menu.person({
		id:EXT_MAIL_PERSON_MENU
	});
});
