Ext.onReady(function(){
	DWREngine.setAsync(false);	
	var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "empsName", "id", function(res) {
				empsMap = res;
			});
	var empNameMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empNameMap = res;
			});	
	var ruleCfgNameData;
	baseDataUtil.getBaseDicDataMap("CotMailRuleCfg", "id", "name", function(
					res) {
				ruleCfgNameData = res;
			});			
	var ruleCfgPropertyData;
	baseDataUtil.getBaseDicDataMap("CotMailRuleCfg", "id", "property", function(
					res) {
				ruleCfgPropertyData = res;
			});	
	var excuteNameMap;
	baseDataUtil.getBaseDicDataMap("CotMailExecuteCfg", "id", "name", function(res) {
				excuteNameMap = res;
			});	
	var methodData;
	baseDataUtil.getBaseDicDataMap("CotMailExecuteCfg", "id", "method", function(
					res) {
				methodData = res;
			});
	var classData;
	baseDataUtil.getBaseDicDataMap("CotMailExecuteCfg", "id", "class_", function(
					res) {
				classData = res;
			});
	var packageData;
	baseDataUtil.getBaseDicDataMap("CotMailExecuteCfg", "id", "package_", function(
					res) {
				packageData = res;
			});
	DWREngine.setAsync(true);
	
	// -----------------下拉框-----------------------------------------

	// 业务员
	var empBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName",
		cmpId : 'bussinessPerson',
		fieldLabel : "<font color=red>业务员</font>",
		editable : true,
		valueField : "id",
		displayField : "empsName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "请选择业务员！",
		tabIndex : 5,
		sendMethod : "post",
		selectOnFocus : true,

		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 520,
		triggerAction : 'all',
		listeners :{
			'select' : function(combo, record, index){
			    mailRuleService.getDefaultCotMailEmpsRule(combo.getValue(),function(res){
						if(res !=null){
							Ext.Msg.alert('提示消息','该业务员已生成规则配置',function(id){
								if(id == 'ok' || id =='cancel'){
									window.location.href ='cotmailrule.do?method=addRule&id='+res.id;
								}
							});
						}else{
							custStore.proxy.setApi({
						                    read : "./cotmailrule.do?method=queryCustAndContact&comboValue="+combo.getValue()		
										});
				           custStore.load({params : {start : 0,limit : 15}});
				           ruleDs.load({params :{custId : 1}});
						   excDs.load({params :{custId : 1}});
						}
				});
			}
		}
	});

		//规则执行方式	
	var swStore = new Ext.data.SimpleStore({
			fields : ["ruleDefault", "name"],
			data : [[0, '按公共规则执行'],[1, '按个人规则执行']]
		});
	var switchBox = new Ext.form.ComboBox({
				name : 'ruleDefault',
				fieldLabel:'规则执行',
				editable : false,
				store : swStore,
				valueField : "ruleDefault",
				allowBlank : false,
		        blankText : "请选择执行方式！",
				displayField : "name",
				mode : 'local',
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'ruleDefault',
				selectOnFocus : true
			});

	var ruleForm = new Ext.form.FormPanel({
		title : "邮件规则基本信息-(红色为必填项)",
		labelWidth : 60,
		collapsible : true,
		formId : "ruleForm",
		labelAlign : "right",
		region : 'north',
		layout : "form",
		width : "100%",
		height : 180,
		frame : true,
		items : [{
			xytpe :　"panel",
			layout : "form",
			items :[{
			xtype : "panel",
			layout : "column",
			items : [{
						xtype : "panel",
						layout : "form",
						columnWidth : 0.4,
						items : [{
									xtype : "textfield",
									fieldLabel : "规则名称",
									anchor : "98%",
									id : "ruleName",
									name : "ruleName",
									tabIndex : 25,
									maxLength : 100,
									allowBlank : false,
									blankTest : '请填写规则名称'
								}]
					},{
						xtype : "panel",
						layout : "form",
						columnWidth : 0.2,
						items : [switchBox]
					},{
						xtype : "panel",
						layout : "form",
						columnWidth : 0.4,
						items : [empBox]
					}]
		   }]
		},
		new Ext.form.TextArea({
			fieldLabel : '规则描述',
			width : "99%",
			height : 80,
			name : 'ruleDesc',
			id : 'ruleDesc'
			
		}),
		new Ext.form.Hidden({
			name : 'xmlPath',
			width : 150,
			fieldLabel : '路径'
		}),
		new Ext.form.Hidden({
			name : 'type',
			width : 150,
			fieldLabel : '类型'
		}),
		
		]	
	});
	
	
	//----------------------------------------------------------------
	//操作
	var opStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['contains', '包含'], ['containsatleastone', '至少包含一个'],['=', '等于'],['>=', '大于或等于'],['<=', '小于或等于'],['>', '大于'],['<', '小于']]
			});
	var opBox = new Ext.form.ComboBox({
				name : 'op',
				editable : false,
				store : opStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'op',
				selectOnFocus : true
			});
	
	//关系
		var relateBox=new Ext.form.ComboBox({
		hiddenName:'relate',
		id : 'relate' ,
		fieldLabel:'',
		editable : false,
		anchor : "100%",
		triggerAction: 'all',
		store : new Ext.data.SimpleStore({
				fields: ['relate', 'post'],
				data : [['AND','而且'],['OR','或者']]
		}),
		displayField:'post',
		valueField:'relate',
		mode: 'local',
		selectOnFocus : true,
		emptyText : '请选择',
		allowBlank : false,
		blankText : "请选择关系类型！"
	});
			
    // 邮件规则内容
	var ruleBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotMailRuleCfg&key=property&type=MAIL&typeName=type",
		cmpId : 'cfgId',
		fieldLabel : "",
		autoLoad:true,
		valueField : "id",
		displayField : "name",
		emptyText : '请选择',
		anchor : "100%",
		allowBlank : false,
		blankText : "请选择规则内容！",
		sendMethod : "post",
		selectOnFocus : true,
		triggerAction : 'all'
	});
	
	function getBindCombox(){
		var empBox1 = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotEmps",
		id : 'empBox1' ,
		cmpId : 'argsName',
		fieldLabel : "业务员",
		editable : false,
		valueField : "empsName",
		displayField : "empsName",
		emptyText : '请选择',
		pageSize : 10,
		anchor : "100%",
		allowBlank : false,
		blankText : "请选择业务员！",
		sendMethod : "post",
		selectOnFocus : true,
		triggerAction : 'all'
		
	});
	return empBox1;
	}
	
	function getEditTree(){
		var textFiled = new Ext.form.TextField({
			allowBlank :　false,
			listeners : {
						'focus' : function(txt) {
							var root = new Ext.tree.AsyncTreeNode({
								text : '员工邮箱',
								expanded : true,
								draggable : false,
								id : "root_fac",
								loader :new Ext.tree.TreeLoader({
									dataUrl : "./cotmail.do?method=queryMailTree&empId="+empBox.getValue()
								})
							});
							
							//创建Tree面板组件
							var empsTree = new Ext.tree.TreePanel({
								title : '',
								width : "100%",
								height : 700,
							    autoScroll: true,
							    animate: true,
							    enableDD: true,
							    containerScroll: true,
								root : root
							})
																	
							var wind = new Ext.Window({
								title : '员工树',
								width : 400,
								height: 500,
								layout : 'fit',
								closeAction: 'hide',
								items :[empsTree],
								modal : true,
								buttonAlign : 'center',
								buttons : [{
										text : '确定',
										handler:function(){
											var res =gridExc.getSelectionModel().getSelected();
											var node =empsTree.getSelectionModel().getSelectedNode();
											res.set('args',node.id);
											res.set('argsName',node.text);
											wind.hide();
										}
								}]
							});
							wind.show();
						}
					}
		});
		return textFiled;
	}
	
	function getCustGride(){
		var textGrid = new Ext.form.TextField({
			allowBlank :　false,
			listeners :{
				'focus' : function(txt){
					var custRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "customerShortName"
					}, {
						name : "customerEmail"
					}]);
					
					var custDs = new Ext.data.Store({
						autoLoad :{params:{start : 0,limit : 10}},
						proxy : new Ext.data.HttpProxy({
							//		url : "./cotmailrule.do?method=queryCust&flag=flag"
									url : "./cotmailrule.do?method=queryCust"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, custRecord)
					});
					
					var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
					var cm = new Ext.grid.ColumnModel({
							defaults : {
								sortable : true
							},
							columns : [sm,
								{
									header: 'ID',
									dataIndex : 'id',
									width : 50,
									hidden : true
								},{
									header: '客户',
									dataIndex : 'customerShortName',
									width : 200
								},{
									header: '邮箱',
									dataIndex : 'customerEmail',
									width : 200
								}
							]	
					});
					
					var toolBar = new Ext.PagingToolbar({
						pageSize : 10,
						store : custDs,
						displayInfo : true,
						//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display"
					});
					
					var custGrid =new Ext.grid.GridPanel({
						id : 'custGrid',
						stripeRows : true,
						bodyStyle : 'width:100%',
						store : custDs, // 加载数据源
						cm : cm, // 加载列
						sm : sm,
						loadMask : true, // 是否显示正在加载
						bbar : toolBar,
						viewConfig : {
							forceFit : true
						}
					});
					var windGrid = new Ext.Window({
						title : '客户表',
						width : 500,
						height : 400,
						layout:'fit',
						closeAction: 'hide',
						items :[custGrid],
						modal : true,
						buttonAlign : 'center',
						buttons :[{
									text:'确定',
									handler:function(){
										var custRecord =Ext.getCmp('custGrid').getSelectionModel().getSelected();
										var ruleRecord =gridExc.getSelectionModel().getSelected();
										ruleRecord.set('args',custRecord.get('id'));
										ruleRecord.set('argsName',custRecord.get('customerShortName'));
										windGrid.hide();
									}
								},{
									text : '取消',
									handler : function(){
										windGrid.hide();
									}
								}]
					});
					windGrid.show();
				}
			}
			
		});
		
		return textGrid;
	}
	
	
	 // 邮件规则条件
	var excBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotMailExecuteCfg&key=name&type=MAIL&typeName=type",
		cmpId : 'eId',
		fieldLabel : "",
		autoLoad:true,
		valueField : "id",
		displayField : "name",
		emptyText : '请选择',
		anchor : "100%",
		allowBlank : false,
		blankText : "请选择规则条件！",
		sendMethod : "post",
		selectOnFocus : true,
		triggerAction : 'all'
	});		
			
	//-------------------------------------------------------------------邮件规则Grid
	var ruleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "ruleId",
				type : "int"
			}, {
				name : "ruleCfgId"
			}, {
				name : "op"
			}, {
				name : "rightTerm"
			}, {
				name : "custId",
				type : 'int'
			}, {
				name : "relate"
			}, {
				name : "leftTerm"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ruleDs = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				//autoLoad:true,
				proxy : new Ext.data.HttpProxy({
					//		url : 'cotmailrule.do?method=queryRule'
							api : {
								read : "cotmailrule.do?method=queryRule",
								create : "cotmailrule.do?method=add",
								update : "cotmailrule.do?method=modify",
								destroy : "cotmailrule.do?method=remove"
							},
							listeners : {
								exception : function(proxy, type, action, options, res, arg){
									if (res.status != 200) {
										Ext.Msg.alert("提示消息", "操作失败！");
									} else {
										if($('ruleId').value==null || $('ruleId').value=="null" ||Ext.isEmpty($('custId').value)==true ){
											return ;
										}
									
										if (excDs.getModifiedRecords().length == 0
										&& excDs.removed.length == 0){
												
												DWREngine.setAsync(false);
												mailRuleService.saveRuleXmlFile($('ruleId').value,function(r){
														window.location.href ='cotmailrule.do?method=addRule&id='+$('ruleId').value;
														reflashParent('ruleGrid');
														
													});
												mailRuleService.createPublicMailRuleFile();
												DWREngine.setAsync(true);
											}
											else{
												excDs.proxy.setApi({
													read : "cotmailrule.do?method=queryExecute",
													create : "cotmailrule.do?method=addExc"
															+ '&empsRuleId=' + $('ruleId').value +'&custId='+ $('custId').value+'&type='+$('type').value,
													update : "cotmailrule.do?method=modifyExc&type="+$('type').value,
													destroy : "cotmailrule.do?method=removeExc"
												});
												excDs.save();
									
											}
											
									}
								}
							}
						}),
				
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, ruleRecord),
				writer : writer
			});
	
	// 创建复选框列
	var ruleSm = new Ext.grid.CheckboxSelectionModel();
	
	// 创建需要在表格显示的列
	var ruleCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [ruleSm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : '项目',
							dataIndex : 'ruleCfgId',
							width : 200,
							editor :ruleBox,
							renderer : function(value, metaData, record, rowIndex, colIndex, store) {
								return ruleCfgNameData[value];
							}
						}, {
							header : "leftTerm",
							dataIndex : "leftTerm",
							hidden : true
						}, {
							header : "操作符",
							dataIndex : "op",
							width : 100,
							editor : opBox,
							renderer: function(value){
								if(value=='containsatleastone'){
									value='至少包含一个';
									return value;
								}
								if(value=='contains'){
									value='包含';
									return value;
								}
								if(value=='='){
									value='等于';
									return value;
								}
								if(value=='>='){
									value='大于或等于';
									return value;
								}
								if(value=='>'){
									return '大于';
								}
								if(value=='<'){
									return '小于';
								}
								if(value=='<='){
									return '小于或等于';
								}
							}
						}, {
							header : 'RuleId',
							dataIndex : 'ruleId',
							hidden : true
						}, {
							header : 'custId',
							dataIndex : 'custId',
							hidden : true
						},{
							header : "内容",
							dataIndex : "rightTerm",
							width : 150 ,
							editor : new Ext.form.TextField({
								allowBlank : false
							})
						}, {
							header : "关系",
							dataIndex : "relate",
							width : 250,
							editor : relateBox,
							renderer : function(value){
								if(value=='AND'){
									return '而且';
								}
								if(value =='OR'){
									return '或者';
								}
							}
						}]
			});
	

	var ruletlBar = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDel,
							iconCls : "page_del"
						}]
			});	
			
	//创建Grid表格组件
	var gridRule = new Ext.grid.EditorGridPanel({
		id : "ruleGrid",
		title : '邮件规则',
		stripeRows : true,
		anchor : '100% 50%',
		border : false,
		cls : 'rightBorder',
		store : ruleDs, // 加载数据源
		cm : ruleCm, // 加载列
		sm : ruleSm,
		loadMask : true, // 是否显示正在加载
		tbar : ruletlBar,
		viewConfig : {
			forceFit : true
		}

	});
	
	gridRule.on("afteredit", function(e) {
				var dsList =ruleDs.getCount();
				var fcount =0;
				var scount =0;
				var zcount =0;
				var ncount =0;
				var socount =0;
				
				if(dsList == 1){
					if (e.field == 'ruleCfgId') {
						e.record.set('leftTerm',ruleCfgPropertyData[e.value]);
						if(e.value==1){
							var id =tabPanel.getActiveTab().title;
							if(id=='客户信息'){
								e.record.set('rightTerm',custSm.getSelected().get('customerEmail').toLowerCase());	
							}
							if(id=='工厂信息'){
								e.record.set('rightTerm',facSm.getSelected().get('contactEmail').toLowerCase());		
							}
						}
					}
				}
				if(e.field == 'ruleCfgId'){
					if(dsList >1){
						var lfTerm =e.value;
						e.record.set('leftTerm',ruleCfgPropertyData[e.value]);
						
						for(var i =0; i<dsList; i++){
							var ruleCfgId =ruleDs.getAt(i).data.ruleCfgId;
							if(ruleCfgId ==1){
								fcount++;
							}
							if(ruleCfgId ==2){
								scount++;
							}
							if(ruleCfgId ==3){
								zcount++;
							}
							if(ruleCfgId ==4){
								ncount++;
							}
							if(ruleCfgId ==5){
								socount++;
							}
						}
						
					if(fcount >1 && lfTerm==1 || scount >1 && lfTerm==2 || zcount >1 && lfTerm==3 || ncount >1 && lfTerm==4 || socount >1 && lfTerm==5){
								Ext.Msg.alert('提示消息','此项目已存在!',function(id){
										if(id =='ok' || id=='cancel'){
											ruleDs.removeAt(dsList-1);
										}
									});
						}
						
						if(e.value==1){
							var id =tabPanel.getActiveTab().title;
							if(id=='客户信息'){
								e.record.set('rightTerm',custSm.getSelected().get('customerEmail').toLowerCase());	
							}
							if(id=='工厂信息'){
								e.record.set('rightTerm',facSm.getSelected().get('contactEmail').toLowerCase());		
							}
						}
					}
				}
				
			});
	
	//-------------------------------------------------------------------规则条件Grid
	var excRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "ruleId"
			},{
				name : "executeCfgId",
				type : 'int'
			}, {
				name : "name"
			},{
				name : "custId",
				type : 'int'
			}, {
				name : "method"
			},{
				name : "argsName"
			},{
				name : "package_"
			},{
				name : "class_"
			},{
				name : "args"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var excDs = new Ext.data.Store({
				autoSave : false,
			//	autoLoad:{params : {start : 0,limit :20}},
				proxy : new Ext.data.HttpProxy({
						//	url : "cotmailrule.do?method=queryExecute"
							api : {
								read : "cotmailrule.do?method=queryExecute",
								create : "cotmailrule.do?method=addmailExc",
								update : "cotmailrule.do?method=modifymailExc",
								destroy : "cotmailrule.do?method=removeExc"
							},
							listeners : {
								exception : function(proxy, type, action, options, res, arg){
									if (res.status != 200) {
										Ext.Msg.alert("提示消息", "操作失败！");
									} else {
										if($('ruleId').value==null || $('ruleId').value=="null" ||Ext.isEmpty($('custId').value)==true){
											return;
										}
										
										DWREngine.setAsync(false);
										mailRuleService.saveRuleXmlFile($('ruleId').value,function(r){
												reflashParent('ruleGrid');
												Ext.Msg.alert("提示消息", "保存成功！",function(id){
													if(id =='ok' || id =='cancel'){
														window.location.href ='cotmailrule.do?method=addRule&id='+$('ruleId').value;
														reflashParent('ruleGrid');
													}
													
												});
										});
										mailRuleService.createPublicMailRuleFile();
										DWREngine.setAsync(true);
									}
								}
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, excRecord),
				writer : writer
			});
	
	// 创建复选框列
	var excSm = new Ext.grid.CheckboxSelectionModel();
	
	// 创建需要在表格显示的列
	var excCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [excSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						},{
							header : 'RuleId',
							dataIndex : 'ruleId',
							width : 50,
							hidden : true
						}, {
							header : 'custId',
							dataIndex : 'custId',
							width : 50,
							hidden :true
						}, {
							header : 'CfgName',
							dataIndex : 'name',
							width : 50,
							hidden : true
						},{
							header : "方法",
							dataIndex : "method",
							width : 150,
			        		hidden :　true
						},{
							header : "包",
							dataIndex : "package_",
							width : 150,
							hidden :　true
						}, {
							header : "类",
							dataIndex : "class_",

							width : 150,
							hidden :　true
						},  {
							header : "动作",
							dataIndex : "executeCfgId",
							width : 150,
							editor : excBox,
							renderer : function(value){
								return excuteNameMap[value]
							}
						},{
							header : "内容",
							dataIndex : "argsName",
							width : 250,
							editor :new Ext.form.TextField({
								allowBlank :　false,
								readOnly:true
							})
						}, {
							header : "args",
							dataIndex : "args",
							width : 180,
							hidden : true
						}]
			});
	
	var exctlBar = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : addNewExcGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDelExc,
							iconCls : "page_del"
						}]
			});	
			
	//创建Grid表格组件
	var gridExc = new Ext.grid.EditorGridPanel({
		id : "excGrid",
		title : '执行内容',
		anchor : '100% 50%',
		stripeRows : true,
		border : false,
		cls : 'rightBorder',
		store : excDs, // 加载数据源
		cm : excCm, // 加载列
		sm : excSm,
		loadMask : true, // 是否显示正在加载
		tbar : exctlBar,
		viewConfig : {
			forceFit : false
		},
		listeners : {
			'celldblclick': function(g, rowIndex,columnIndex, e){
				var excId =excDs.getAt(rowIndex).data.executeCfgId;
				if(columnIndex==9){
					if(excId ==1){
						gridExc.getColumnModel().setEditor(9,getEditTree());
					}
					if(excId ==2){
						gridExc.getColumnModel().setEditor(9,getBindCombox());
					}
					var id =tabPanel.getActiveTab().title;
					if(excId==3){
				        if(id=='工厂信息'){
							gridExc.getColumnModel().setEditor(9,getFacGride());
				        }
				        if(id=='客户信息'){
							gridExc.getColumnModel().setEditor(9,getCustGride());
				        }
					}
				}
			}
		}
	});
	
	gridExc.on('afteredit',function(e){
			if(e.field == 'executeCfgId'){
				var excCount =excDs.getCount();;
				var zpCount =0;
				var ydCount =0;
				var gdCount =0;
				var eRecord =e.record;
				e.record.set('name',excuteNameMap[e.value]);
				e.record.set('method',methodData[e.value]);
				e.record.set('package_',packageData[e.value]);
				e.record.set('class_',classData[e.value]);
				var rs =e.value;
				if(excCount ==1){
					if(rs ==2){
						if(Ext.isEmpty(empBox.getValue())){
							Ext.Msg.alert('提示消息','请先选择一个业务员!',function(id){
								if(id =='ok' || id=='cancel'){
									excDs.removeAt(excCount-1);
								}
							});
							return;
						}
						e.record.set('args',empBox.getValue());
						e.record.set('argsName',empNameMap[empBox.getValue()]);
					}
					if(rs == 1){
						gridExc.getColumnModel().setEditor(9,getEditTree());
					}
					
					if(rs == 3){
						eRecord.set('args',facSm.getSelected().get('factoryId'));
					    eRecord.set('argsName',facSm.getSelected().get('factoryName'));
						gridExc.getColumnModel().setEditor(9,getCustGride());
					}
				}
				if(excCount >1){
						for(var e=0;e<excCount;e++){
							var dz=excDs.getAt(e).get('executeCfgId');
							if(dz ==2){
								zpCount++;
							}
							
							if(dz ==1){
								ydCount++;
							}
							
							if(dz ==3){
								gdCount++;
							}
						}
				}
				if(rs==2&&zpCount ==1){
					if(Ext.isEmpty(empBox.getValue())){
							gridExc.getColumnModel().setEditor(9,getSimpText());
							return;
						}
					eRecord.set('args',empBox.getValue());
					eRecord.set('argsName',empNameMap[empBox.getValue()]);
				}
				
				if(rs==1 && ydCount ==1){
					gridExc.getColumnModel().setEditor(9,getEditTree());
				}
				
				if(rs ==3&&gdCount==1){
					eRecord.set('args',facSm.getSelected().get('factoryId'));
				    eRecord.set('argsName',facSm.getSelected().get('factoryName'));
					gridExc.getColumnModel().setEditor(9,getCustGride());
				}
				if($('ruleId').value == 'null' || $('ruleId').value == ''){
					if(rs==2&&zpCount >1 || rs==1 && ydCount >1 || rs==3&&gdCount >1){
						
						Ext.Msg.alert('提示消息','此动作已存在',function(id){
							if(id =='ok' || id=='cancel'){
								excDs.removeAt(excCount-1);
							}
						});
					}
				}else{
					if(rs==2&&zpCount >1 || rs==1 && ydCount >1 || rs==3&&gdCount >1){
						
						Ext.Msg.alert('提示消息','此动作已存在',function(id){
							if(id =='ok' || id=='cancel'){
								excDs.reload();
							}
							
						});
					}
				}
			}
			
			if(e.field == 'argsName'){
				var eRecord =e.record;
				if(eRecord.get('executeCfgId')==2){
					eRecord.set('args',empsMap[e.value]);
				}
			}
	});
		
		
	//-------------------------------------------------------------------客户信息Grid
	var custRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "empId",
				type : "int"
			},{
			   name : "custId",
			   type : "int"
			}, {
				name : "contactPerson"
			},{
				name : "type"
			},  {
				name : "customerEmail"
			}, {
				name : "customerShortName"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var custStore = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
					api : {
						read : "./cotmailrule.do?method=queryCustAndContact"
					}
				}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, custRecord),
				writer : writer
			});
	
	// 创建复选框列
	var custSm = new Ext.grid.CheckboxSelectionModel({
					singleSelect :true,
					listeners : {
						'beforerowselect':function(sm, rowIndex, keepExisting, record){
							if(ruleDs.getCount()==0){
								
							}else{
								for(var i=0;i<ruleDs.getCount();i++){
								    var record =ruleDs.getAt(i);
								 //   alert('dirty'+record.dirty);
									var data = record.data;
									if(Ext.isEmpty(data.leftTerm) || Ext.isEmpty(data.op) ||Ext.isEmpty(data.rightTerm)){
										Ext.Msg.alert('提示消息','数据填充不完整,请先保存');
											return false;
									} 
								}
							}
						},
						'rowselect': function(sm, rowIndex,r){
							var custId =GridCust.getSelectionModel().getSelected().data.id;
							var type =GridCust.getSelectionModel().getSelected().data.type;
							ruleDs.reload({params :{custId : custId , type : type ,ruleId : $('ruleId').value}});
							excDs.reload({params :{custId : custId , type :type ,ruleId : $('ruleId').value}});
						}
					}
	});
	
	
	// 创建需要在表格显示的列
	var custCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [custSm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : '客户简称',
							dataIndex : 'customerShortName'
						},{
							header : 'type',
							dataIndex : 'type',
							hidden: true
						},{
							header : 'custId',
							dataIndex : 'custId',
							hidden: true
						},{
							header : '联系人',
							dataIndex : 'contactPerson'
						}, {
							header : '邮箱地址',
							dataIndex : 'customerEmail'
						}]
			});
	
	var custpgBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : custStore,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var custtlBar = new Ext.Toolbar({
				items : ['->', {
							text : "初始化规则",
							handler : initCustGrid,
							iconCls : "page_add"
						}]
			});	
			
	//创建Grid表格组件
	var GridCust = new Ext.grid.EditorGridPanel({
		id : "CustGrid",
		title : '',
		region : 'center',
		stripeRows : true,
		border : false,
		cls : 'rightBorder',
		store : custStore, // 加载数据源
		cm : custCm, // 加载列
		sm : custSm,
		loadMask : true, // 是否显示正在加载
		bbar :custpgBar,
		tbar :custtlBar,
		viewConfig : {
			forceFit : true
		},
		listeners :{
			'click' : function (){
				GridCust.getSelectionModel().selectRow();		
			}
		}
	});
	
	//客户
	var custBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
				cmpId : 'custFind',
				fieldLabel : "客户",
				editable : true,
				valueField : "id",
				displayField : "customerShortName",
				pageSize : 5,
				anchor : "95%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : '请选择',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	
	var custForm = new Ext.FormPanel({
				height : 50,
				formId : "queryForm",
				region : 'north',
				buttonAlign : 'right', // 按钮居右显示
				labelAlign : "right",
				layout : 'column',
				padding : '5',
				border : false,
				plain : true,
				frame : true,
				items : [{
							layout : 'form',
							columnWidth : .3,
							labelWidth : 50, // 标签宽度
							items : [custBox]
						}, {
							layout : 'table',
							columnWidth : .3,
							layoutConfig : {
								columns : 2
							},
							items : [{
										xtype : 'button',
										text : "查询",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											custStore.reload();
										}
									}, {
										xtype : 'button',
										text : "重置",
										iconCls : "page_reset",
										style : {
											marginLeft : '10px'
										},
										width : 65,
										handler : function() {
											custForm.getForm().reset()
										}
									}]
						}]
			});
	
	var fixCustForm =new Ext.Panel({
		title: '客户信息',
		layout :'border',
		defaults :{width : '100%'},
		items : [custForm,GridCust]
	
	});	
	
	//-------------------------------------------------------------------工厂信息
	
	var facRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "contactEmail"
			}, {
				name : "contactPerson"
			},{
				name : "type"
			}, {
				name : "factoryId",
				type : "int"
			},{
				name : "factoryName"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var facDs = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotmailrule.do?method=queryFactoryAndContact"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, facRecord),
				writer : writer
			});
	
	// 创建复选框列
	var facSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect :true,
					listeners : {
						'beforerowselect':function(sm, rowIndex, keepExisting, record){
							if(ruleDs.getCount()==0){
								
							}else{
								for(var i=0;i<ruleDs.getCount();i++){
								    var record =ruleDs.getAt(i);
								 //   alert('dirty'+record.dirty);
									var data = record.data;
									if(Ext.isEmpty(data.leftTerm) || Ext.isEmpty(data.op) ||Ext.isEmpty(data.rightTerm)){
										Ext.Msg.alert('提示消息','数据填充不完整,请先保存');
											return false;
									} 
								}
							}
						},
						'rowselect': function(sm, rowIndex,r){
							var custId =facSm.getSelected().data.id;
							var type =facSm.getSelected().data.type
							ruleDs.reload({params :{custId : custId , type : type,ruleId : $('ruleId').value}});
							excDs.reload({params :{custId : custId, type : type,ruleId : $('ruleId').value}});
						}
					}
	});
	
	// 创建需要在表格显示的列
	var facCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [facSm, {
							header : "ID",
							dataIndex : "id",
							hidden : true
						}, {
							header : "工厂名称",
							dataIndex : "factoryName"
						}, {
							header : "工厂ID",
							dataIndex : "factoryId",
							hidden : true
						}, {
							header : '联系人',
							dataIndex : 'contactPerson'
						}, {
							header : 'type',
							dataIndex : 'type',
							hidden : true
						}, {
							header : '邮箱地址',
							dataIndex : 'contactEmail'
						}]
			});

	var factlBar = new Ext.Toolbar({
				items : ['->', {
							text : "选择联系人",
							handler : selectFacAndContact.createCallback(true),
							iconCls : "page_add"
						}]
			});	
			
	//创建Grid表格组件
	var GridFac = new Ext.grid.EditorGridPanel({
		id : "facGrid",
		title : '工厂信息',
		stripeRows : true,
		border : false,
		cls : 'rightBorder',
		store : facDs, // 加载数据源
		cm : facCm, // 加载列
		sm : facSm,
		loadMask : true, // 是否显示正在加载
		tbar : factlBar,
		viewConfig : {
			forceFit : true
		},
		listeners :{
			'click' : function (){
				GridCust.getSelectionModel().selectRow();		
			}
		}
	});
	facDs.load();
			
	var tabPanel = new Ext.TabPanel({
		title : '',
		flex : 1,
		margins : "0 5 0 0",
		activeTab :　0,
		items : [fixCustForm , GridFac],
		listeners: {
			'tabchange':function(tabs, newtab ){
				    var id =tabs.getActiveTab().id;
				    try{
						    if(id =='facGrid'){
								facSm.clearSelections();
								ruleDs.load({params :{custId : 1}});
								excDs.load({params :{custId : 1}});
								var ruleId = $('ruleId').value;
				    	        if(ruleId == 'null' || ruleId == ''){
				    	        
				    	        }else{
									mailRuleService.getCotMailExecuteFACByruleId(parseInt($('ruleId').value), function(res){
										if(res!=null){
											var custIds =new Array();
											for(var ou=0;ou<res.length;ou++){
												var exc =res[ou];
												var custId =exc.custId;
												custIds.push(custId);
												facDs.reload({params : {start : 0,limit : 10,custIds :custIds}});
											}
										}
									});
				    	        }
						    }else{
						    	custSm.clearSelections();
						    	ruleDs.load({params :{custId : 1}});
								excDs.load({params :{custId : 1}});
						    }
				    }catch(e){}
				}
		}
		
	});
	
		var combPanel = new Ext.Panel({
			layout : 'anchor',
			flex : 1,
			anchor : '100% 50%',
			items : [gridRule,gridExc]
		});
		
		var tbl = new Ext.Panel({
			region : 'center',
			width : "100%",
			layout : "hbox",
			layoutConfig : {
				align : 'stretch'
			},
			items : [tabPanel, combPanel],
			buttonAlign : 'center',
			buttons : [{
						text : "保存",
						cls : "SYSOP_ADD",
						iconCls : "page_table_save",
						handler : save
					},{
					text : "取消",
					iconCls : "page_cancel",
					handler : function() {
							closeandreflashEC(true, 'ruleGrid', false);
					}
				}]
		});
	
		var viewport = new Ext.Viewport({
					layout : 'border',
					items : [tbl, ruleForm]
				});
		viewport.doLayout();
		custStore.on('beforeload',function(){
			   custStore.baseParams=DWRUtil.getValues('queryForm');
		});
		
	//---------------------------------函数
	// 初始化
	function initform() {
		var ruleId = $('ruleId').value;
		if(ruleId == 'null' || ruleId == ''){
			
		}else{
			DWREngine.setAsync(false);	
			mailRuleService.getMailEmpsRuleById(parseInt(ruleId), function(res){
				DWRUtil.setValues(res);
				switchBox.setValue(res.ruleDefault);
				empBox.bindPageValue("CotEmps", "id", res.empId);
			//	switchBox.bindPageValue('CotMailEmpsRule','ruleDefault',res.ruleDefault);
				custStore.proxy.setApi({
						                    read : "./cotmailrule.do?method=queryCustAndContact&comboValue="+res.empId			
										});
				custStore.load({params : {start : 0,limit : 15}});
				
			});
			DWREngine.setAsync(true);	
			
		}
	}
	initform();
	// 添加空白record到rule表格中
		function addNewGrid() {	
			var id =tabPanel.getActiveTab().title;
			if(id =='工厂信息'){
				if(!facSm.hasSelection()){
					Ext.Msg.alert('提示消息','请先选中左边要初始化的信息');
					return;
				}
			}else{
				if(!custSm.hasSelection()){
					Ext.Msg.alert('提示消息','请先选中左边要初始化的信息');
					return;
				}
			}
			var u = new ruleDs.recordType({
					leftTerm : "",
					op : "contains",
					relate : "AND",
					rightTerm : ""
				});
			ruleDs.add(u);
			
			//动作获得焦点
			var cell = gridRule.getView().getCell(ruleDs.getCount() - 1, 2);
			if (Ext.isIE) {
				cell.fireEvent("ondblclick");
			} else {
				var e = document.createEvent("Events");
				e.initEvent("dblclick", true, false);
				cell.dispatchEvent(e)
			}
		}
		
		
	// 删除rule表格
	function onDel() {
		var recs = ruleSm.getSelections();
		if (recs.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		Ext.each(recs, function(item) {
						ruleDs.remove(item);
					});		
		
	};
				
	// 添加空白record到exc表格中
		function addNewExcGrid() {
			var id =tabPanel.getActiveTab().title;
			if(id =='工厂信息'){
				if(!facSm.hasSelection()){
					Ext.Msg.alert('提示消息','请先选中左边要初始化的信息');
					return;
				}
			}else{
				if(!custSm.hasSelection()){
					Ext.Msg.alert('提示消息','请先选中左边要初始化的信息');
					return;
				}
			}
			var exc = new excDs.recordType({
					ruleId : "",
					name : ""
				});
			excDs.add(exc);
			//动作获得焦点
			var cell = gridExc.getView().getCell(excDs.getCount() - 1, 8);
			if (Ext.isIE) {
				cell.fireEvent("ondblclick");
			} else {
				var e = document.createEvent("Events");
				e.initEvent("dblclick", true, false);
				cell.dispatchEvent(e)
			}
			
		}		
		
	// 删除exc表格
	function onDelExc() {
		var excs = excSm.getSelections();
		if (excs.length == 0) {
			Ext.Msg.alert("提示框", "请选择记录!");
			return;
		}
		Ext.each(excs, function(item) {
						excDs.remove(item);	
					});				
	};
	//归档到工厂
	function getFacGride(){
		var textGrid = new Ext.form.TextField({
			allowBlank :　false,
			listeners :{
				'focus' : function(txt) {
					selectFacAndContact();
				}
			}
			});
		return textGrid;
	}
	
	//选择工厂联系人
	function selectFacAndContact(flag){
		var flag=flag;
		var facRecord = new Ext.data.Record.create([{
					name : "id",
					type : "int"
				}, {
					name : "contactEmail"
				}, {
					name : "contactPerson"
				}, {
					name : "type"
				}, {
					name : "factoryId",
					type:'int'
				}, {
					name : "factoryName"
				}]);
		var writer = new Ext.data.JsonWriter({
					encode : true,
					writeAllFields : true
				});
		// 创建数据源
		var facDs = new Ext.data.Store({
					autoSave : false,
					method : 'post',
					url:'cotmailrule.do?method=queryFactoryAndContact',
					reader : new Ext.data.JsonReader({
								root : "data",
								totalProperty : "totalCount",
								idProperty : "id"
							}, facRecord),
					writer : writer
				});
		
		// 创建复选框列
		var facSm = new Ext.grid.CheckboxSelectionModel();
		
		// 创建需要在表格显示的列
		var facCm = new Ext.grid.ColumnModel({
					defaults : {
						sortable : true
					},
					columns : [facSm, {
								header : "ID",
								dataIndex : "id",
								hidden : true
							}, {
								header : "工厂名称",
								dataIndex : "factoryName"
							},{
								header : "工厂ID",
								dataIndex : "factoryName",
								hidden : true
							},  {
								header : '联系人',
								dataIndex : 'contactPerson'
							}, {
								header : 'type',
								dataIndex : 'type',
					            hidden : true
							},  {
								header : '邮箱地址',
								dataIndex : 'contactEmail'
							}]
				});
	
		var toolBar = new Ext.PagingToolbar({
						pageSize : 10,
						store : facDs,
						displayInfo : true,
						//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
						displaySize : '5|10|15|20|all',
						emptyMsg : "No data to display"
					});
		
		var GridFacAndCont =new Ext.grid.GridPanel({
						id : 'facAndContact',
						stripeRows : true,
						region : 'center',
						bodyStyle : 'width:100%',
						store : facDs, // 加载数据源
						cm : facCm, // 加载列
						sm : facSm,
						loadMask : true, // 是否显示正在加载
						bbar : toolBar,
						viewConfig : {
							forceFit : true
						}
					});
		//客户
	var facBox = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotFactory&key=factoryName",
				cmpId : 'factoryId',
				fieldLabel : "工厂名称",
				editable : true,
				valueField : "id",
				displayField : "factoryName",
				pageSize : 5,
				anchor : "95%",
				selectOnFocus : true,
				sendMethod : "post",
				emptyText : '请选择',
				minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
				listWidth : 350,// 下
				triggerAction : 'all'
			});
	
	var facForm = new Ext.FormPanel({
				height : 50,
				formId : "queryForms",
				region : 'north',
				buttonAlign : 'right', // 按钮居右显示
				labelAlign : "right",
				layout : 'column',
				padding : '5',
				border : false,
				plain : true,
				frame : true,
				items : [{
							layout : 'form',
							columnWidth : .6,
							labelWidth : 70, // 标签宽度
							items : [facBox]
						}, {
							layout : 'table',
							columnWidth : .3,
							layoutConfig : {
								columns : 2
							},
							items : [{
										xtype : 'button',
										text : "查询",
										width : 65,
										iconCls : "page_sel",
										handler : function() {
											facDs.reload();
										}
									}, {
										xtype : 'button',
										text : "重置",
										iconCls : "page_reset",
										style : {
											marginLeft : '10px'
										},
										width : 65,
										handler : function() {
											custForm.getForm().reset()
										}
									}]
						}]
			});
		facDs.on('beforeload', function() {
				//facDs.baseParams = DWRUtil.getValues("queryForms");
				facDs.baseParams = {factoryId:facBox.getValue()};
			});
		// 分页基本参数
		facDs.load({
					params : {
						start : 0,
						limit : 10
					}
				});
		var windGrid = new Ext.Window({
			title : '工厂表',
			width : 500,
			height : 500,
		//	layout:'fit',
			layout : 'border',
			modal : true,
			closeAction: 'hide',
			items :[facForm,GridFacAndCont],
			buttonAlign : 'center',
			buttons :[{
						text:'确定',
						handler:function(){
							if(flag){
								var facRes =Ext.getCmp('facAndContact').getSelectionModel().getSelections();
								if(Ext.getCmp('facAndContact').getSelectionModel().getCount() >0){
									for(var i=0;i<facRes.length;i++){
										var u = new facDs.recordType({
												id : facRes[i].get('id'),
												contactEmail: facRes[i].get('contactEmail'),
												contactPerson : facRes[i].get('contactPerson'),
												factoryName : facRes[i].get('factoryName'),
												type : facRes[i].get('type'),
												factoryId : facRes[i].get('factoryId')
											});
										Ext.getCmp('facGrid').getStore().add(u);
									}
								}
							}else{
								var facRes =Ext.getCmp('facAndContact').getSelectionModel().getSelected();
								excSm.getSelected().set('argsName',facRes.get('factoryName'));
								excSm.getSelected().set('args',facRes.get('factoryId'));
							}
							facDs.reload();
							windGrid.hide();
						}
					},{text : '取消',
						handler: function(){
							facDs.reload();
							windGrid.hide();
						}
					}]
		});
		windGrid.show();
	}
	
	
	//初始化客户信息
	function initCustGrid(){
		// 验证表单
		var formData = getFormValues(ruleForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		
		var form =ruleForm.getForm();
		if(custStore.getCount()==0){
			Ext.Msg.alert('提示消息','该业务员没有客户联系人，不能初始化');
			return;
		}
		
		var mailEmpsRule =new CotMailEmpsRule();
//		var ruleId = $('ruleId').value;
//		if(ruleId == 'null' || ruleId == ''){
//			
//		}else{
//			mailEmpsRule.id= ruleId;
//		}
		mailRuleService.getDefaultCotMailEmpsRule(empBox.getValue(),function(res){
						if(res !=null){
							mailEmpsRule.id= res.id;
			                mailEmpsRule.xmlPath =res.xmlPath;
						}
		});
		mailEmpsRule.ruleDefault =switchBox.getValue();
		mailEmpsRule.empId =form.findField('bussinessPerson').getValue();		
		mailEmpsRule.ruleName =form.findField('ruleName').getValue();
		mailEmpsRule.ruleDesc =form.findField('ruleDesc').getValue();
		mailEmpsRule.type='MAIL';
		mailEmpsRule.empName =empNameMap[form.findField('bussinessPerson').getValue()];	
	
		Ext.MessageBox.confirm('提示信息', '您确定要初始化邮件规则？', function(btn){
			if (btn == 'yes'){
				DWREngine.setAsync(false);
				mailRuleService.saveOrUpdateMailEmpsRule(mailEmpsRule,function (resId){
						if(resId !=null){							
							    mailRuleService.initRules(empBox.getValue(),resId,function(rs){
									if(rs =='success'){
							 			mailRuleService.saveRuleXmlFile(resId,function(r){
											if(r !=null){
												 Ext.Msg.alert('提示消息','初始化成功',function(id){
							                   		if(id == 'ok' || id =='cancel'){
							                   			reflashParent('ruleGrid');	
							                   			window.location.href ='cotmailrule.do?method=addRule&id='+resId;
							                   		}
							                   });
											}else{
												Ext.MessageBox.alert('系统消息', '初始化失败');
											}  
										});
										 mailRuleService.createPublicMailRuleFile();
							 			
							 		}
								});
						}
				});
				DWREngine.setAsync(true);
			}
		});
	}
	
	
	// 保存
	function save() {
		// 验证表单
		var formData = getFormValues(ruleForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		
		var form =ruleForm.getForm();
		
		var mailEmpsRule =new CotMailEmpsRule();
		var ruleId = $('ruleId').value;
		mailRuleService.getDefaultCotMailEmpsRule(empBox.getValue(),function(res){
						if(res !=null){
							mailEmpsRule.id= res.id;
			                mailEmpsRule.xmlPath =res.xmlPath;
						}
		});

		mailEmpsRule.ruleDefault =switchBox.getValue();
		mailEmpsRule.empId =form.findField('bussinessPerson').getValue();		
		mailEmpsRule.ruleName =form.findField('ruleName').getValue();
		mailEmpsRule.ruleDesc =form.findField('ruleDesc').getValue();
		mailEmpsRule.type= 'MAIL';
		mailEmpsRule.empName =empNameMap[form.findField('bussinessPerson').getValue()];		

        if(facSm.getCount() >0 && ruleDs.getCount()>0 && excDs.getCount() ==0 || facSm.getCount()>0 && excDs.getCount()>0 &&ruleDs.getCount()==0){
		   		Ext.MessageBox.alert('提示消息', '请先填写相应的规则信息，再保存');
		   		return;
		}
		
        if(GridCust.getSelectionModel().getCount()==1){
			var rCount= ruleDs.getCount();
			if(rCount >0){
				for(var i=0;i<rCount;i++){
					var record =ruleDs.getAt(i);
					var data = record.data;
					if(Ext.isEmpty(data.leftTerm) || Ext.isEmpty(data.op) ||Ext.isEmpty(data.rightTerm)){
						Ext.Msg.alert('提示消息','数据填充不完整不能保存');
						return
					} 
				}
			}
			var eCount =excDs.getCount();
			if(eCount >0){
				for(var e=0;e<eCount;e++){
					var excData =excDs.getAt(e).data;
					if(Ext.isEmpty(excData.executeCfgId) ||Ext.isEmpty(excData.argsName)){
						Ext.Msg.alert('提示消息','数据填充不完整不能保存');
						return ;
					}
				}
			}

		}
	    
		if(ruleDs.getCount()>1){
			for(var zx =0;zx<ruleDs.getCount();zx++){
					var relate=ruleDs.getAt(0).data.relate;
					if(relate=='AND'){
						var ruleRs =ruleDs.getAt(zx);
						ruleRs.set('relate','AND');
					}else{
						var ruleRs =ruleDs.getAt(zx);
						ruleRs.set('relate','OR');
					}
				}
			}
			
			//手动保存是，自动补上缺项
			var id =tabPanel.getActiveTab().title;
			if(id=='客户信息'){
				if(ruleDs.getCount()>0){
					var ruleC=0;
					for(var i=0;i<ruleDs.getCount();i++){
						var rs =ruleDs.getAt(i);
						if(rs.get('ruleCfgId')==1){
							ruleC++;
						}
					}
					if(ruleC==0){
						var u = new ruleDs.recordType({
								leftTerm : ruleCfgPropertyData[1],
								op : "contains",
								relate : "AND",
								ruleCfgId : 1 ,
								rightTerm : custSm.getSelected().get('customerEmail').toLowerCase()
							});
						ruleDs.add(u);
					}
				}
				if(excDs.getCount()>0){
					var excC=0;
					var excCls=0;
					for(var i=0;i<excDs.getCount();i++){
						var rs =excDs.getAt(i);
						if(rs.get('executeCfgId')==2){
							excC++;
						}
						if(rs.get('executeCfgId')==3){
							excCls++;
						}
					}
					if(excC==0){
						var exc = new excDs.recordType({
								executeCfgId : 2,
								name : excuteNameMap[2],
								method : methodData[2],
								package_:packageData[2],
								class_ : classData[2],
								argsName :empNameMap[empBox.getValue()],
								args : empBox.getValue()
							});
						excDs.add(exc);
					}
					if(excCls==0){
						var exc = new excDs.recordType({
								executeCfgId : 3,
								name : excuteNameMap[3],
								method : methodData[3],
								package_:packageData[3],
								class_ : classData[3],
								argsName :custSm.getSelected().get('customerShortName'),
								args : custSm.getSelected().get('custId')
							});
						excDs.add(exc);
					}
				}
			}
			if(id=='工厂信息'){
				if(ruleDs.getCount()>0){
					var ruleC=0;
					for(var i=0;i<ruleDs.getCount();i++){
						var rs =ruleDs.getAt(i);
						if(rs.get('ruleCfgId')==1){
							ruleC++;
						}
					}
					if(ruleC==0){
						var u = new ruleDs.recordType({
								leftTerm : ruleCfgPropertyData[1],
								op : "contains",
								relate : "AND",
								ruleCfgId : 1 ,
								rightTerm : facSm.getSelected().get('contactEmail').toLowerCase()
							});
						ruleDs.add(u);
					}
				}
				if(excDs.getCount()>0){
					var excC=0;
					var excCls=0;
					for(var i=0;i<excDs.getCount();i++){
						var rs =excDs.getAt(i);
						if(rs.get('executeCfgId')==2){
							excC++;
						}
						if(rs.get('executeCfgId')==3){
							excCls++;
						}
					}
					if(excC==0){
						var exc = new excDs.recordType({
								executeCfgId : 2,
								name : excuteNameMap[2],
								method : methodData[2],
								package_:packageData[2],
								class_ : classData[2],
								argsName :empNameMap[empBox.getValue()],
								args : empBox.getValue()
							});
						excDs.add(exc);
					}
					if(excCls==0){
						var exc = new excDs.recordType({
								executeCfgId : 3,
								name : excuteNameMap[3],
								method : methodData[3],
								package_:packageData[3],
								class_ : classData[3],
								argsName :facSm.getSelected().get('factoryName'),
								args : facSm.getSelected().get('factoryId')
							});
						excDs.add(exc);
					}
				}
				
			}
			
			Ext.MessageBox.confirm('提示信息', '您确定要保存该邮件规则？', function(btn){
				if (btn == 'yes'){
							DWREngine.setAsync(false);	
							mailRuleService.saveOrUpdateMailEmpsRule(mailEmpsRule,function (resId){
								if(resId !=null){
									        $('ruleId').value = resId;
									        if(ruleId == 'null' || ruleId == ''){
										    		if(facSm.getCount()>0&&ruleDs.getCount()>0&&excDs.getCount()>0){
										    			
										    			 var id =tabPanel.getActiveTab().title;
												         if(id=='工厂信息'){
															var custId =GridFac.getSelectionModel().getSelected().data.id;
															$('custId').value = custId;
														    $('type').value =facSm.getSelected().data.type;
												         }
										    			
										    			var urlAdd = '&empsRuleId=' + resId + '&custId='+$('custId').value+'&type='+$('type').value;

														if (ruleDs.getModifiedRecords().length == 0
																	&& ruleDs.removed.length == 0){
															if (excDs.getModifiedRecords().length == 0
																	&& excDs.removed.length == 0){
																	reflashParent('ruleGrid');
																	Ext.Msg.alert("提示消息", "保存成功！");
															}else{
																excDs.proxy.setApi({
																		read : "cotmailrule.do?method=queryExecute",
																		create : "cotmailrule.do?method=addExc"
																				+ urlAdd,
																		update : "cotmailrule.do?method=modifyExc&type="+$('type').value,
																		destroy : "cotmailrule.do?method=removeExc"
																	});
													    		 excDs.save();
															}			
														}else{
															ruleDs.proxy.setApi({
																read :  "cotmailrule.do?method=queryRule",
																create : "cotmailrule.do?method=add"
																		+ urlAdd,
																update : "cotmailrule.do?method=modify&type="+$('type').value,
																destroy : "cotmailrule.do?method=remove"
															});
															ruleDs.save();
														}
															
											        }else{
											        	//填写上面规则信息，直接保存
											        	if(ruleDs.getCount()==0&&excDs.getCount()==0){
														 mailRuleService.initRules(empBox.getValue(),resId,function(rs){
																if(rs =='success'){
													 			mailRuleService.saveRuleXmlFile(resId,function(r){
																	if(r !=null){
																		 Ext.Msg.alert('提示消息','保存成功',function(id){
													                   		if(id == 'ok' || id =='cancel'){
													                   			reflashParent('ruleGrid');	
													                   			window.location.href ='cotmailrule.do?method=addRule&id='+resId;
													                   		}
													                   });
																	}else{
																		Ext.MessageBox.alert('提示消息', '保存失败');
																	}  
																});
																 mailRuleService.createPublicMailRuleFile();
																 return;
													 		}
															});
											        	}else{
											        		//填写上面规则信息，且填写执行内容和条件
											        		var id =tabPanel.getActiveTab().title;
													         if(id=='工厂信息'){
																var custId =GridCust.getSelectionModel().getSelected().data.id;
																$('custId').value = custId;
															    $('type').value =custSm.getSelected().data.type;
													         }
											    			var urlAdd = '&empsRuleId=' + resId + '&custId='+$('custId').value+'&type='+$('type').value;	
															if (ruleDs.getModifiedRecords().length == 0
																		&& ruleDs.removed.length == 0){
																if (excDs.getModifiedRecords().length == 0
																		&& excDs.removed.length == 0){
																		reflashParent('ruleGrid');
																		Ext.Msg.alert("提示消息", "保存成功！");
																}else{
																	excDs.proxy.setApi({
																			read : "cotmailrule.do?method=queryExecute",
																			create : "cotmailrule.do?method=addExc"
																					+ urlAdd,
																			update : "cotmailrule.do?method=modifyExc&type="+$('type').value,
																			destroy : "cotmailrule.do?method=removeExc"
																		});
														    		 excDs.save();
																}			
															}else{
																ruleDs.proxy.setApi({
																	read :  "cotmailrule.do?method=queryRule",
																	create : "cotmailrule.do?method=add"
																			+ urlAdd,
																	update : "cotmailrule.do?method=modify&type="+$('type').value,
																	destroy : "cotmailrule.do?method=remove"
																});
																ruleDs.save();
															}
											        	}
											        }
									        }else{
											    var id =tabPanel.getActiveTab().title;
												if(id=='客户信息'){
													if(custSm.getCount()>0){
														var custId =GridCust.getSelectionModel().getSelected().data.id;
														$('custId').value = custId;
														$('type').value =custSm.getSelected().data.type;
													}
												}else{
													if(facSm.getCount()>0){
														var custId =GridFac.getSelectionModel().getSelected().data.id;
														$('custId').value = custId;
													    $('type').value =facSm.getSelected().data.type;
													}
												}
									        	// 更改添加action参数
											    var urlAdd = '&empsRuleId=' + resId + '&custId='+$('custId').value +'&type='+$('type').value;
									        	
									        	if (ruleDs.getModifiedRecords().length == 0
																	&& ruleDs.removed.length == 0){
															if (excDs.getModifiedRecords().length == 0
																	&& excDs.removed.length == 0){
																	 Ext.Msg.alert('提示消息','保存成功',function(id){
												                   		if(id == 'ok' || id =='cancel'){
												                   			reflashParent('ruleGrid');	
												                   			window.location.href ='cotmailrule.do?method=addRule&id='+resId;
												                   		}
												                   });
															}else{
																excDs.proxy.setApi({
																		read : "cotmailrule.do?method=queryExecute",
																		create : "cotmailrule.do?method=addExc"
																				+ urlAdd,
																		update : "cotmailrule.do?method=modifyExc&type="+$('type').value,
																		destroy : "cotmailrule.do?method=removeExc"
																	});
													    		 excDs.save();
															}			
														}else{
															ruleDs.proxy.setApi({
																read :  "cotmailrule.do?method=queryRule",
																create : "cotmailrule.do?method=add"
																		+ urlAdd,
																update : "cotmailrule.do?method=modify&type="+$('type').value,
																destroy : "cotmailrule.do?method=remove"
															});
															ruleDs.save();
														}
									        	
									        }
									       
								    }
								});
					 			DWREngine.setAsync(true);	
				      }
			     });
	}
});
