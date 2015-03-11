Ext.namespace("Ext.Login");
Ext.Login.LoginTree = Ext.extend(Ext.tree.TreePanel, {
	// title:'员工邮箱',
	autoScroll : true,
	enableDD : false,// 是否可以拖拽
	border : true, // 边框
	useArrows : false,// 文件夹前显示的图标改变了不在是+号了
	enableDrop : true,
	pageCfg:'',// 页面配置
	containerScroll : true,
	rootVisible : false,// 隐藏根节点
	initComponent : function() {
		var treePanel = this;
		this.root = new Ext.tree.AsyncTreeNode({
					text : 'Online',
					expanded : true,
					draggable : false,
					id : "root_0"
				});
		this.loader = new Ext.tree.TreeLoader({
			dataUrl : "index.do?method=queryLoginTree&flag=0",
			listeners : {
				load : {
					scope : this,
					fn : function(treeLoader, node, response) {

					}
				}
			}
		});
		this.tbar = new Ext.Toolbar({
			enableOverflow : true,
			items : [' ', new Ext.form.ComboBox({
				ref : 'findField',
				width : 100,
				emptyText : 'By Name',
				displayField : 'name',
				valueField : 'id',
				mode : 'local',
				triggerAction : 'all',
				enableKeyEvents : true,
				store : new Ext.data.ArrayStore({
							fields : ['id', 'name'],
							data : [[0,'By Name'],[1,'By IP']]
						}),
				listeners : {
					select : {
						fn : function(t,record,index) {
							this.getLoader().dataUrl="index.do?method=queryLoginTree&flag="+record.data.id;
							this.getRootNode().reload();
							this.root.expand(true);
							cotModuelService.findLoginNum(function(res){
								Ext.getCmp('loginNumBtn').setText("Online<font color=red>("+res+")</font>");
							});
						},
						scope : this
					}
				}
			}), '->', {
				iconCls : 'page_refresh',
				tooltip : 'refresh',
				overflowText : 'refresh',
				handler : function() {
					this.root.reload();
					this.root.expand(true);
					cotModuelService.findLoginNum(function(res){
						Ext.getCmp('loginNumBtn').setText("Online<font color=red>("+res+")</font>");
					});
				},
				scope : this
			}, {
				iconCls : 'icon-expand-all',
				tooltip : 'Expand All',
				overflowText : 'Expand All',
				handler : function() {
					this.root.expand(true);
				},
				scope : this
			}, '-', {
				iconCls : 'icon-collapse-all',
				tooltip : 'Collapse All',
				overflowText : 'Collapse All',
				handler : function() {
					this.root.collapse(true);
				},
				scope : this
			}]
		});
		
		//qq发消息
		function qqSend(){
			var empId=sn.attributes.empId;
			cotModuelService.getQqNum(empId,function(qq){
				if(qq==null){
					Ext.Msg.alert('Message','该用户还没配置QQ号码不能发消息,请到员工管理配置!');
				}else if(qq=='exist'){
					Ext.Msg.alert('Message','不能给自己发QQ消息!');
				}else{
					downRpt("http://wpa.qq.com/msgrd?V=1&Uin="+qq+"&Menu=yes&site=显示的来源信息");
				}
			});
		}
		
		//msn发消息
		function msnSend(){
			var empId=sn.attributes.empId;
			cotModuelService.getMsnNum(empId,function(msn){
				if(msn==null){
					Ext.Msg.alert('Message','该用户还没配置MSN账户不能发消息,请到员工管理配置!');
				}else if(msn=='exist'){
					Ext.Msg.alert('Message','不能给自己发MSN消息!');
				}else{
					downRpt("msnim:chat?contact="+msn);
				}
			});
		}
		
		// 右键菜单
		var ctxMenu = new Ext.menu.Menu({
					items : [{
								text : "发QQ",
								id : 'qqBtn',
								handler : qqSend
							},{
								text : "发MSN",
								id : 'msnBtn',
								handler : msnSend
							}]
				});
		// 被选中的节点
		var sn;
		// 显示不同右键菜单
		function prepareCtx(node, e) {
//			node.select();
//			this.fireEvent('click', node, e);
//			sn = node;
//			if (node.attributes.empId==-1 || $('loginEmpId').value==node.attributes.empId) {
//				Ext.getCmp('qqBtn').setDisabled(true);
//				Ext.getCmp('msnBtn').setDisabled(true);
//			}else {
//				Ext.getCmp('qqBtn').setDisabled(false);
//				Ext.getCmp('msnBtn').setDisabled(false);
//			}
//			ctxMenu.showAt(e.getXY());
		}
		this.on("contextmenu", prepareCtx, this);
		Ext.Login.LoginTree.superclass.initComponent.call(this);
	},
	listeners : {
		'click' : {
			fn : function(node, e) {
			}
		}
	}
});
Ext.reg('logintree', Ext.Login.LoginTree);