
Ext.mail.MailMoveTreeWindow = Ext.extend(Ext.Window,{
	width:300,
	height:300,
	shadow:true,
	constrain:true,
	title : "移动邮件到",
	resizable:true,
	layout:'fit',
	modal:true,
	empId:'',
	initComponent:function(){
		var treeWin = this;
		var tree = new Ext.mail.MailMoveTree({empId:treeWin.empId});
		tree.on('click',function(node,e){
			Ext.mail.NodeMailMoveFn(node)?treeWin.okBtn.enable():treeWin.okBtn.disable();
		});
		this.items = [tree];
		this.buttons = [{
			text:'确定',
			ref:'../okBtn',
			disabled:true,
			scope:this,
			handler:function(){
				var node = tree.getSelectionModel().getSelectedNode();
				Ext.mail.moveMailToNodeFn(node);
				treeWin.close();
			}
		},{
			text:'取消',
			handler:function(){
				treeWin.close();
			}
		}]
		Ext.mail.MailMoveTreeWindow.superclass.initComponent.call(this);
	}
});
Ext.mail.MailMoveTree = Ext.extend(Ext.tree.TreePanel,{
	ref:'mailTree',
	enableDD : false,// 是否可以拖拽
	border : true, // 边框
	useArrows : false,// 文件夹前显示的图标改变了不在是+号了
	animate : true,// 动画效果
	containerScroll : true,
	autoScroll:true,
	rootVisible : false,// 隐藏根节点
	empId:'',
	hiddenPkgs:[],
	initComponent:function(){
		var tree = this;
		tree.root = new Ext.tree.AsyncTreeNode({
			text : '员工邮箱',
			expanded : true,
			draggable : false,
			id : "root_fac"
		});
		tree.loader = new Ext.tree.TreeLoader({
			dataUrl : "./cotmail.do?method=queryMailTree&empId="+tree.empId
		}),
		tree.filter = new Ext.tree.TreeFilter(tree);
		Ext.mail.MailMoveTree.superclass.initComponent.call(this);
	}
});
Ext.mail.NodeMailMoveFn = function(target){
	if(target.attributes.flag=='null')
		return false;
	var node = EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
	target = Ext.mail.getParentNode(target);// 目标所属类型节点
	node = Ext.mail.getParentNode(node);// 选中所属类型节点
	var flag = node.attributes.flag;
	var tnFlag = target.attributes.flag;
	var empId = node.attributes.empId;
    if(target.attributes.empId==empId&&flag!='P'&&tnFlag!='P'){// 同员工节点下，节点都不为待发送，不为null
    	if(tnFlag=='null'||flag=='null')　// 员工下新建的节点
    		return true;
		if(tnFlag==flag)
			return true;
		else if(tnFlag=='D'&&flag=='R'
			||tnFlag=='D'&&flag=='S')
			return true;
		else
			return false;
    }
	else
		return false;
}
/**
 * 获得节点所属类型节点
 * @param {} node
 */
Ext.mail.getParentNode = function(node){
	if(node.attributes.updateFlag=='n')
		return node;
	return Ext.mail.getParentNode(node.parentNode);
}


Ext.mail.MailTree = Ext.extend(Ext.tree.TreePanel,{
	title : "员工邮箱",
	enableDD : false,// 是否可以拖拽
	border : true, // 边框
	useArrows : false,// 文件夹前显示的图标改变了不在是+号了
//	animate : true,// 动画效果
	enableDrop:true,
	dropConfig:{ 
		ddGroup: 'GridDD',
		getDropPoint:function(e, n, dd){
		    var tn = n.node;
		   	return Ext.mail.NodeMailMoveFn(tn)?"append":false;
		},
		appendOnly:true
	},
	containerScroll : true,
	rootVisible : false,// 隐藏根节点
	initComponent:function(){
		var treePanel = this;
		this.root = new Ext.tree.AsyncTreeNode({
			text : '员工邮箱',
			expanded : true,
			draggable : false,
			id : "root_fac"
		});
		
		
	
		this.loader =  new Ext.tree.TreeLoader({
			dataUrl : "./cotmail.do?method=queryMailTree",
			listeners:{
				load:{
					scope:this,
					fn:function(treeLoader,node,response){
						var newMsgEmpId = parseInt(Ext.getDom('newMsgEmpId').value);
						if(Ext.isNumber(newMsgEmpId)){
							mailTreeService.findMailEmpId(newMsgEmpId,function(nodeId){
								if(nodeId){
									var inboxNode = treePanel.getNodeById(nodeId);
									inboxNode.expand();
									if(inboxNode&&inboxNode.firstChild){
										var nodeFirstChild = inboxNode.firstChild;
										treePanel.getSelectionModel().select(nodeFirstChild);
										nodeFirstChild.fireEvent('click',nodeFirstChild)
									}
								}
							})
						}
					}
				}
			}
		});
		this.tbar=new Ext.Toolbar({
			enableOverflow: true,
			items:[ ' ',
				new Ext.form.TextField({
				width: 130,
				emptyText:'查找节点',
	            enableKeyEvents: true,
				listeners:{
	                keydown: {

	                    fn: function(t, e){
							var text = t.getValue();
							Ext.each(this.hiddenPkgs, function(n){
								n.ui.show();
							});
							var me = this;
				            me.hiddenPkgs = [];
				            var root = me.root
							var re = new RegExp(Ext.escapeRe(text), 'i');
							findChild(root);
							function findChild(node){
								var isShow = false;
								if(re.test(node.text))
									isShow = true;	
								if(node.expanded){
									Ext.each(node.childNodes,function(childNode){
										var childIsShow = findChild(childNode);
										if(childIsShow)
											isShow = true;
									});
								}
								if(isShow)
									return true;
								else{
									node.ui.hide();
									me.hiddenPkgs.push(node);
									return false;	
								}
							}
						},
	                    buffer: 350,
	                    scope: this
	                },
	                scope: this
				}
			}),'->',{
	            iconCls: 'icon-expand-all',
				tooltip: '扩展所有节点',
	            handler: function(){ this.root.expand(true); },
	            scope: this
	        }, '-', {
		        iconCls: 'icon-collapse-all',
		        tooltip: '关闭所有节点',
		        handler: function(){ this.root.collapse(true); },
		        scope: this
	        }]
        });
        this.bbar = {
        	enableOverflow: true,
        	items:[{
	        	xtype:'combo',
	        	value:0,
	        	width:100,
	        	ref:'../showChildMailField',
	        	isSearchField:true,
			    triggerAction: 'all',
				forceSelection:true,
			    mode: 'local',
			    editable:false,
			    store: new Ext.data.ArrayStore({
			        fields: ['id','name'],
			        data: [[0,'不显示子邮件'],[1, '显示子邮件']]
			    }),
			    valueField: 'id',
			    displayField: 'name',
			    listeners:{
			    	select:{
			    		fn:function(combo,record,index){
							var grid = EXT_VIEWPORT.mailGrid; // 被击活的grid
							if(grid&&grid.store){
								var store = grid.getStore();
								store.reload();
							}
			    		}
			    	}
			    }
	        },'->',{
	        	iconCls: 'email_view_body',
				tooltip: '只显示邮件主界面',
	            handler: function(){
	            	EXT_VIEWPORT.mailDefault.collapse(true);
	            	EXT_VIEWPORT.historyCard.collapse(true);
	            }
	        },'-',{
	        	iconCls: 'email_view_default',
				tooltip: '显示邮件内容详情面板',
	            handler: function(){
	            	EXT_VIEWPORT.mailDefault.expand(true);
	            	EXT_VIEWPORT.historyCard.collapse(true);
	            	
	            }
	        },'-',{
	        	iconCls: 'email_view_history',
				tooltip: '显示邮件历史面板',
	            handler: function(){
					EXT_VIEWPORT.mailDefault.collapse(true);
	            	EXT_VIEWPORT.historyCard.expand(true);
				}
	        }]
        };

			// 新建文件夹
		function addDir() {
			ctxMenu.hide();
			sn.expand();
			// 保存到后台
			var nodeNewName = Ext.getCmp('mailTreeNodeNewName').getValue();
			if(nodeNewName==''){
				nodeNewName='新邮箱';
			}
			mailTreeService.saveTreeNode(sn.id, nodeNewName,function(res) {
				if (res != null) {
					var newNode = new Ext.tree.TreeNode({
								iconCls:'folder',
								text : nodeNewName
							});
					newNode.id = res;
					newNode.attributes.empId = sn.attributes.empId;
					if(Ext.isEmpty(sn.attributes.flag)||sn.attributes.flag=='null')
						newNode.attributes.flag = 'R';
					else
						newNode.attributes.flag = sn.attributes.flag;
					sn.leaf = false;
					sn.appendChild(newNode);
				}
			});
		}
		// 删除
		function delDir() {
			Ext.MessageBox.confirm("提示消息", "真的要删除这个的邮箱？", function(button, text) {
				if (button == "yes") {
					mailTreeService.findNodeExistsMail(sn.id,function(res){
						if(res){
							Ext.Msg.show({
								title:'系统提示',
								msg:'节点下存在邮件，不能删除！',
								icon:Ext.Msg.WARNING,
								buttons:Ext.Msg.OK
							});	
							return ;
						}
						mailTreeService.delTreeNode(sn.id, function(res) {
							var tempNode = sn.parentNode;
							tempNode.removeChild(sn);
							if (!tempNode.hasChildNodes()) {
								//指定图标
								//tempNode.getUI().getIconEl().src = './common/ext/resources/images/default/tree/leaf.gif';
								//替换样式
								Ext.fly(tempNode.ui.elNode).replaceClass("x-tree-node-collapsed", "x-tree-node-leaf");
							}
						});
						
					});
					// 保存到后台
				}
			});
		}
		// 重命名
		function reName() {
			Ext.MessageBox.prompt('邮箱重命名', '将"' + sn.text + '" 修改为:', function(btn,text) {
				if (btn == 'ok') {
					if (text == '') {
						Ext.MessageBox.alert('提示消息', '节点名称不能为空');
						return false;
					}
					sn.setText(text);// 更新节点内容
					// 保存到后台
					mailTreeService.updateTreeNode(sn.id, text, function(
									res) {
							});
				}
			});
	
		}
		// 右键菜单
		var ctxMenu = new Ext.menu.Menu({
			items : [{
				text : "新建邮件夹",
				handler:addDir,
				menu : {
					layout : 'hbox',
					layoutConfig : {
						align : 'middle'
					},
					width : 200,
					items : [{
						xtype:'textfield',
						id:'mailTreeNodeNewName',
						maxLength : 100,
						selectOnFocus:true,
						value:'新邮箱'
					},{
						xtype : 'button',
						text : "添加",
						id : 'addMailBtn',
						flex : 1,
						iconCls : 'page_img',
						handler : addDir
					}]
				}
			},{
				text : "删除",
				id : 'delMailBtn',
				handler : delDir
			}, {
				text : "重命名",
				id : 'renMailBtn',
				handler : reName
			}]
		});
		// 被选中的节点
		var sn;
		// 显示不同右键菜单
		function prepareCtx(node, e) {
			node.select();
			this.fireEvent('click',node,e);
			if(node.attributes.flag&&(node.attributes.flag.indexOf('A')==0||node.attributes.flag=='P')){
				return;	
			}
			sn = node;
			if (node.attributes.parentId == 1 || node.attributes.updateFlag == 'n') {
				Ext.getCmp('delMailBtn').setDisabled(true);
				Ext.getCmp('renMailBtn').setDisabled(true);
			} else {
				Ext.getCmp('delMailBtn').setDisabled(false);
				Ext.getCmp('renMailBtn').setDisabled(false);
			}
			ctxMenu.showAt(e.getXY());
		}
	
		this.on("contextmenu", prepareCtx,this);
		Ext.mail.MailTree.superclass.initComponent.call(this);
	},
	listeners : {
		//加载邮件
		'click' : {
			fn:function(node,e) {
				var nodeFlag = node.attributes.flag;
	        	var mailGrid;
				
				// 对Grid操作
				var allGrid = EXT_VIEWPORT.mailAllGrid;
	        	if(!node.cardIndex){
	            	switch(nodeFlag){
	            		case 'R': // 收件箱
	            			mailGrid = new Ext.mail.InboxGrid();
	            			break;
	            		case 'S': // 发件箱
	            			mailGrid = new Ext.mail.SendGrid();
	            			break;
	            		case 'C': // 草稿箱
	            			mailGrid = new Ext.mail.DraftGrid();
	            			break;
	            		case 'D': // 废件箱
	            			mailGrid = new Ext.mail.DelGrid();
	            			break;
	            		case 'G': // 公共箱
	            			mailGrid = new Ext.mail.InboxGrid();
	            			break;
	            		case 'P': // 待发送
	            			mailGrid = new Ext.mail.CheckGrid();
	            			break;
	            		case 'AR':
	            			mailGrid = new Ext.mail.InboxGrid({
	            				showEmp:true
	            			});
	            			break;
	            		case 'AS':
	            			mailGrid = new Ext.mail.SendGrid({
	            				showEmp:true
	            			});
	            			break;
	            		default:  
	            			mailGrid = new Ext.Panel();
	            	}
	            	// 给mailGrid设置empId属性
	            	if(nodeFlag&&nodeFlag.indexOf('A')==0)
	            		mailGrid.empId = 'null';
	            	else
	            		mailGrid.empId = node.attributes.empId;
	            	// 给mailGrid设置flag属性
	            	mailGrid.flag = nodeFlag;
	            	
	            	allGrid.add(mailGrid); // 添加mailGrid
	        		node.cardIndex = allGrid.itemCount; // 用树节点记录mailCard为card第几项
	        		node.mailGrid = mailGrid; // 树节点记录对应的mailCard
	            	allGrid.itemCount++; // card自增Item
	        		allGrid.layout.setActiveItem(node.cardIndex); // card激活为新增项
	        		EXT_VIEWPORT.mailGrid = mailGrid // 给视图记录当前激活mailCard
	        		
	        		if(mailGrid.store){
	        			var store = mailGrid.getStore();
	        			node.gridStore = store;
	        			if(nodeFlag&&nodeFlag.indexOf('A')==0){
	        				store.setBaseParam('nodeId','null');
        					store.setBaseParam('mailType',nodeFlag=='AR'?2:0);
	        			}else{
	        				store.setBaseParam('nodeId',node.id);
	        			}
	        			store.setBaseParam('empId',mailGrid.empId);
	        			if(mailGrid.getTopToolbar()){
	        				var queryStore = mailGrid.getTopToolbar().searchField.queryStore;
	        				if(queryStore){
		        				if(nodeFlag&&nodeFlag.indexOf('A')==0)
			        				queryStore.setBaseParam('nodeId','null');
			        			else
			        				queryStore.setBaseParam('nodeId',node.id);
		        				queryStore.setBaseParam('empId',mailGrid.empId);
	        				}
	        			}
	            		store.load({
	            			params:{
	            				start:EXT_MAIL_PAGE_START,
	            				limit:EXT_MAIL_PAGE_LIMIT
	            			}
	            		});
	        		}
	        	}else{
	        		if(node.gridStore){
	        			node.gridStore.reload();
	        		}
	        		EXT_VIEWPORT.mailGrid = node.mailGrid;
	        		allGrid.layout.setActiveItem(node.cardIndex);
	        	}
	        	if(!node.gridStore){
	        		hideSelDefault(EXT_VIEWPORT);
	        		Ext.mail.ToolbarControl(node);
					// 历史
					Ext.mail.ComeAndGoMailControl();
	        	}
	        	EXT_VIEWPORT.mailCard.layout.setActiveItem(0)
			}
		},
		beforenodedrop:{
			fn:function(e){
				Ext.mail.moveMailToNodeFn(e.target);
			}
		}
	}
});
Ext.mail.moveMailToNodeFn = function(target){
	var node = EXT_VIEWPORT.mailTree.getSelectionModel().getSelectedNode(); // 初选中的节点
	node = Ext.mail.getParentNode(node);// 选中所属类型节点
	var flag = node.attributes.flag;
	var tnFlag = target.attributes.flag;
	var grid = EXT_VIEWPORT.mailGrid;	// 选中的grid
	var records = grid.getSelectionModel().getSelections();
	var mailIds = [];
	var data;
	Ext.each(records,function(record){
		data = record.data;
		// 如果选中的节点为员工节点下新建的节点，目标节点不为null和废件箱，则进行邮件类型比对，要同样的才能移动
		if(flag=='null'&&tnFlag!='D'&&tnFlag!='null'){
			if(tnFlag=='R'&&data.mailType==MAIL_LOCAL_TYPE_INBOX
				||tnFlag=='S'&&data.mailType==MAIL_LOCAL_TYPE_SEND
				||tnFlag=='C'&&data.mailType==MAIL_LOCAL_TYPE_DRAFT){
				mailIds.push(data.id);
			}
		}else{
			mailIds.push(data.id);
		}
	});
	if(mailIds.length>0)
		mailTreeService.moveMailToNode(target.id,mailIds,function(result){
			grid.getStore().reload();
		});
}
Ext.reg('mailtree',Ext.mail.MailTree);