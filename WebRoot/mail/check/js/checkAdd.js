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
    var ruleCfgProData;
	baseDataUtil.getBaseDicDataMap("CotMailRuleCfg", "id", "property", function(
					res) {
				ruleCfgProData = res;
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
			    mailCheckService.getDefaultCotMailEmpsRule(combo.getValue(),function(res){
						if(res !=null){
							Ext.Msg.alert('提示消息','该业务员已生成审核规则配置',function(id){
								if(id == 'ok' || id =='cancel'){
									window.location.href ='cotmailcheck.do?method=addCheck&id='+res.id;							
//									ruleDs.load({params:{empId:combo.getValue().empId,type : 'CHECK',ruleId:res.Id}});
//								    excDs.load({params:{empId:combo.getValue(),type : 'CHECK',ruleId:res.Id}});
										}
							});
						}else{
							ruleDs.proxy.setApi({	
						                    read : "./cotmailcheck.do?method=queryRule&empId="+empBox.getValue()
										});
							ruleDs.load();
						    excDs.proxy.setApi({	
						                    read : "./cotmailcheck.do?method=queryExecute&empId="+empBox.getValue()
										});
						    excDs.load();
						}
				});
			}
		}
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
						columnWidth : 0.6,
						items : [{
									xtype : "textfield",
									fieldLabel : "规则名称",
									anchor : "100%",
									id : "ruleName",
									name : "ruleName",
									tabIndex : 25,
									maxLength : 100,
									allowBlank : false,
									blankTest : '请填写规则名称'
								}]
					},  {
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
		})
		]	
	});
	
	
	//----------------------------------------------------------------
	//是否审核
	function getCheckBox(){	
		var opStore = new Ext.data.SimpleStore({
					fields : ["id", "name"],
					data : [['true', '是'],['false', '否']]
				});
		var opBox = new Ext.form.ComboBox({
					name : 'op',
					editable : false,
					store : opStore,
					valueField : "name",
					displayField : "name",
					mode : 'local',
					triggerAction : 'all',
					anchor : "100%",
					emptyText : '请选择',
					hiddenName : 'op',
					selectOnFocus : true
				});
		return opBox;		
	}
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
			
			
    // 邮件规则  项目
	var ruleBox = new BindCombox({
		dataUrl : "servlet/DataSelvert?tbname=CotMailRuleCfg&key=property&type=CHECK&typeName=type",
		cmpId : 'leftTerm',
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
	
	function getEmpEmailGride(cfg){
		if (!cfg)
			cfg = {};
		var textGrid = new Ext.form.TextField({
			allowBlank :　false,
			listeners :{
				'focus' : function(txt){
					var custRecord = new Ext.data.Record.create([{
						name : "id",
						type : "int"
					}, {
						name : "empsMail"
					}, {
						name : "empsName"
					}]);
					
					var custDs = new Ext.data.Store({
						autoLoad :{params:{start : 0,limit : 10}},
						proxy : new Ext.data.HttpProxy({
									url : "./cotmailcheck.do?method=queryEmps"
								}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, custRecord)
					});
					
					var sm = new Ext.grid.CheckboxSelectionModel(cfg);
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
									header: '员工',
									dataIndex : 'empsName',
									width : 200
								},{
									header: '邮箱',
									dataIndex : 'empsMail',
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
						title : '员工表',
						width : 500,
						height : 400,
						layout:'fit',
						closeAction: 'hide',
						modal : true,
						items :[custGrid],
						buttonAlign : 'center',
						buttons :[{
									text:'确定',
									handler:function(){
										var ruleRecord =gridRule.getSelectionModel().getSelected();
										if(ruleRecord.get('ruleCfgId')==6 || ruleRecord.get('ruleCfgId')==7){
											var empsRs =custGrid.getSelectionModel().getSelections();
											var msg='';
											for(var i=0;i<empsRs.length;i++){
												var rs=empsRs[i];
												msg=msg+ rs.get('empsMail').toLowerCase()+",\n";
											}
											var op=msg.lastIndexOf(',');
											msg=msg.substring(0,op-1);
											ruleRecord.set('rightTerm',msg);
										}
										if(ruleRecord.get('ruleCfgId')==8 || ruleRecord.get('ruleCfgId')==9){
											var empsRs =custGrid.getSelectionModel().getSelected();
											ruleRecord.set('rightTerm',empsRs.get('id'));
										}
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
	
	function getCustAndFac(){
		var textGrid = new Ext.form.TextField({
			allowBlank :　false,
			listeners :{
				'focus' : function(txt){
					//客户
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
					
					var custDs = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							api : {
								read : "./cotmailrule.do?method=queryCustAndContact&comboValue="+empBox.getValue()
							}
						}),
						reader : new Ext.data.JsonReader({
									root : "data",
									totalProperty : "totalCount",
									idProperty : "id"
								}, custRecord)
					});
					
					var sm = new Ext.grid.CheckboxSelectionModel({
						listeners:{
							'rowselect' : function(se,rowIndex,rs){
								var ems =Ext.getCmp('emails').getValue();
								var msg =ems.split(";");
								var flag=false;
								for(var i=0;i<msg.length;i++){
									var st=msg[i];
									if(st==rs.get('customerEmail').toLowerCase()){
										flag=true;
									}
								}
								if(flag==false&&Ext.isEmpty(ems)){
								    Ext.getCmp('emails').setValue(ems+rs.get('customerEmail').toLowerCase()+";");
								}
								if(flag==false&&!Ext.isEmpty(ems)){
									var e =ems.substring(ems.length-1);
									if(e==';'){
									    Ext.getCmp('emails').setValue(ems+rs.get('customerEmail').toLowerCase()+";");
									}else{
										var s=ems+';';
										Ext.getCmp('emails').setValue(s+rs.get('customerEmail').toLowerCase()+";");
									}
								}
							}
						}
					});
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
						title: '客户',
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
					custDs.load({params:{start : 0,limit : 10}});
					
					//工厂
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
					var facSm = new Ext.grid.CheckboxSelectionModel({
						listeners:{
							'rowselect' : function(se,rowIndex,rs){
								var ems =Ext.getCmp('emails').getValue();
								var msg =ems.split(";");
								var flag=false;
								for(var i=0;i<msg.length;i++){
									var st=msg[i];
									if(st==rs.get('contactEmail').toLowerCase()){
										flag=true;
									}
								}
								if(flag==false&&Ext.isEmpty(ems)){
								    Ext.getCmp('emails').setValue(ems+rs.get('contactEmail').toLowerCase()+";");
								}
								if(flag==false&&!Ext.isEmpty(ems)){
									var e =ems.substring(ems.length-1);
									if(e==';'){
									    Ext.getCmp('emails').setValue(ems+rs.get('contactEmail').toLowerCase()+";");
									}else{
										var s=ems+';';
										Ext.getCmp('emails').setValue(s+rs.get('contactEmail').toLowerCase()+";");
									}
								}
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
									title: '工厂',
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
					facDs.load({params:{start : 0,limit : 10}});		
					var tabPanel = new Ext.TabPanel({
						title : '',
						region : 'center',
						margins : "0 5 0 0",
						activeTab :　0,
						items : [custGrid , GridFacAndCont]
					});
						
					var columPanel =new Ext.Panel({
						layout: 'column',
						frame :true,
						region : 'north',
						width : "100%",
						autoHeight:true,
						defaults:'panel',
						items: [
							new Ext.form.FormPanel({
								title:'',
								labelWidth : 70,
								labelSeparator:'：',
								columnWidth: .95,
								bodyStyle:'padding:5 5 5 5',
								items:[
									new Ext.form.TextArea({
										fieldLabel : "输入邮箱",
										id : "emails",
									    name : "emails",
									    autoScroll:true,
										allowBlank:true,
										regex:/^(((\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}(;((\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}))*);?|\s{0})$/,
										regexText:'输入多个邮箱时，用分号隔开',
										anchor : "100%"
									})
								]
							})
						]
					});
		
					var panel =new Ext.form.FormPanel({
						titile: '',
						layout : 'border',
						items :[columPanel,tabPanel]
					});
					
					
					var windGrid = new Ext.Window({
						title : '收件人',
						width : 500,
						height : 530,
						layout:'fit',
					//	layout : 'border',
						modal : true,
						items :[panel],
						buttonAlign : 'center',
						listeners:{
							'beforeshow':function(){
								var ruleRecord =gridRule.getSelectionModel().getSelected();
								Ext.getCmp('emails').setValue(ruleRecord.get('rightTerm'));
							}
						},
						buttons :[{
									text:'确定',
									handler:function(){
										if(!Ext.getCmp('emails').isValid())
											return;
										
										var ruleRecord =gridRule.getSelectionModel().getSelected();
										var msg =Ext.getCmp('emails').getValue();
										
										var e =msg.substring(msg.length-1);
										if(e==';'){
											msg=msg.substring(0,msg.length-1);
										}
										
										//验证邮箱
										if(!Ext.isEmpty(msg)){
											var reg = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
											var arr =msg.split(';');
								            for(var i=0;i<arr.length;i++){
													var em=arr[i];
													if(reg.test(em)==false){
										            	Ext.Msg.alert('提示消息','请输入正确格式的邮箱地址');
										            	return;
												    }
												}
										}
										ruleRecord.set('rightTerm',msg);
									    windGrid.close();
									}
								},{
									text : '取消',
									handler : function(){
										windGrid.close();
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
		dataUrl : "servlet/DataSelvert?tbname=CotMailExecuteCfg&key=name&type=CHECK&typeName=type",
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
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotmailcheck.do?method=queryRule",
								create : "cotmailcheck.do?method=add",
								update : "cotmailcheck.do?method=modify",
								destroy : "cotmailcheck.do?method=remove"
							}
							,
							listeners : {
								exception : function(proxy, type, action, options, res, arg){
									if (res.status != 200) {
										Ext.Msg.alert("提示消息", "操作失败！");
									} else {
										if($('ruleId').value==null || $('ruleId').value=="null" ||Ext.isEmpty($('flag').value)){
											return ;
										}
										if (excDs.getModifiedRecords().length == 0
										&& excDs.removed.length == 0){
												DWREngine.setAsync(false);
												mailCheckService.saveRuleXmlFile($('ruleId').value,function(r){
														reflashParent('ruleGrid');
														Ext.Msg.alert("提示消息", "保存成功！",function(id){
															if(id =='ok' || id =='cancel'){
																window.location.href ='cotmailcheck.do?method=addCheck&id='+$('ruleId').value;
																reflashParent('ruleGrid');
															}
															
														});
												});												
												DWREngine.setAsync(true);
											}
											else{
												excDs.proxy.setApi({
													read : "cotmailcheck.do?method=queryExecute",
													create : "cotmailcheck.do?method=addExc"
															+ '&empsRuleId=' + $('ruleId').value +'&custId='+ $('bussinessPerson').value+'&type=CHECK',
													update : "cotmailcheck.do?method=modifyExc&type=CHECK",
													destroy : "cotmailcheck.do?method=removeExc"
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
						},{
							header : '项目',
							dataIndex : 'ruleCfgId',
							editor :ruleBox,
							width : 200,
						    renderer : function(value, metaData, record, rowIndex, colIndex, store) {
								return ruleCfgNameData[value];
							}
						}, {
							header : "操作符",
							dataIndex : "op",
							width : 100,
							renderer: function(value){
								if(value=='containsatleastone'){
									value='包含';
									return value;
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
							header : "leftTerm",
							dataIndex : "leftTerm",
							hidden : true
						},{
							header : "内容",
							dataIndex : "rightTerm",
							width : 250 ,
							editor : new Ext.form.TextField({
								allowBlank : false,
								readOnly :true
							})
						}, {
							header : "关系",
							dataIndex : "relate",
							width : 100,
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
		title : '邮件审核规则',
		stripeRows : true,
		flex : 1,
		margins : "0 5 0 0",
		border : false,
		cls : 'rightBorder',
		store : ruleDs, // 加载数据源
		cm : ruleCm, // 加载列
		sm : ruleSm,
		loadMask : true, // 是否显示正在加载
		tbar : ruletlBar,
		viewConfig : {
			forceFit : true
		},
		listeners : {
			'celldblclick': function(g, rowIndex,columnIndex, e){
				var ruleCfgId =ruleDs.getAt(rowIndex).data.ruleCfgId;
				if(columnIndex==7){
					if(ruleCfgId==7){
						gridRule.getColumnModel().setEditor(7,getEmpEmailGride());
					}
					if(ruleCfgId==6){
						gridRule.getColumnModel().setEditor(7,getCustAndFac());
					}
					if(ruleCfgId==8||ruleCfgId==9){
						gridRule.getColumnModel().setEditor(7,getEmpEmailGride({singleSelect : true}));
					}
				}
			}
		}

	});
	
	gridRule.on("afteredit", function(e) {
				var dsList =ruleDs.getCount();
				var fcount =0;  //收件人
				var scount =0;  //发件人
				var zcount =0;  //发件员工
				var ncount =0;  //收件客户
				var socount =0; //所有邮件
				if(dsList == 1){
					if (e.field == 'ruleCfgId') {
						e.record.set('leftTerm',ruleCfgProData[e.value]);
						var ruleCfgId =e.value;
						//收件人
						if(ruleCfgId== 6){
							gridRule.getColumnModel().setEditor(7,getCustAndFac());
						}
						if(ruleCfgId==7){
							gridRule.getColumnModel().setEditor(7,getEmpEmailGride());
						}
						if(ruleCfgId==8){
							gridRule.getColumnModel().setEditor(7,getEmpEmailGride({singleSelect : true}));
						}
						if(ruleCfgId==9){
							gridRule.getColumnModel().setEditor(7,getEmpEmailGride({singleSelect : true}));
						}
						if(ruleCfgId==10){
							e.record.set('rightTerm','ALL_MAIL');
						}
					}
				}
				if(e.field == 'leftTerm'){
					if(dsList >1){
						var lfTerm =e.value;
						e.record.set('leftTerm',ruleCfgProData[e.value]);
						for(var i =0; i<dsList; i++){
							var ruleCfgId =ruleDs.getAt(i).data.ruleCfgId;
							if(ruleCfgId ==6){
								fcount++;
							}
							if(ruleCfgId ==7){
								scount++;
							}
							if(ruleCfgId ==8){
								zcount++;
							}
							if(ruleCfgId ==9){
								ncount++;
							}
							if(ruleCfgId ==10){
								socount++;
							}
						}					
						if(fcount >1 && lfTerm==6 || scount >1 && lfTerm==7 || zcount >1 && lfTerm==8 || ncount >1 && lfTerm==9 || socount >1 && lfTerm==10){
							if($('ruleId').value == 'null' || $('ruleId').value == ''){
								Ext.Msg.alert('提示消息','此项目已存在!',function(id){
										if(id =='ok' || id=='cancel'){
											ruleDs.removeAt(dsList-1);
										}
									});
							}else{
								Ext.Msg.alert('提示消息','此项目已存在!',function(id){
										if(id =='ok' || id=='cancel'){
											    ruleDs.removeAt(dsList-1);
										}
									});
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
				proxy : new Ext.data.HttpProxy({
						//	url : "cotmailrule.do?method=queryExecute"
							api : {
								read : "cotmailcheck.do?method=queryExecute",
								create : "cotmailcheck.do?method=addmailExc",
								update : "cotmailcheck.do?method=modifymailExc",
								destroy : "cotmailcheck.do?method=removeExc"
							},
							listeners : {
								exception : function(proxy, type, action, options, res, arg){
									if (res.status != 200) {
										Ext.Msg.alert("提示消息", "操作失败！");
									} else {
										if($('ruleId').value==null || $('ruleId').value=="null" ||Ext.isEmpty($('flag').value)){
											return;
										}
										$('flag').value = '';
										DWREngine.setAsync(false);
										mailCheckService.saveRuleXmlFile($('ruleId').value,function(r){
												reflashParent('ruleGrid');
												Ext.Msg.alert("提示消息", "保存成功！",function(id){
													if(id =='ok' || id =='cancel'){
														window.location.href ='cotmailcheck.do?method=addCheck&id='+$('ruleId').value;
														reflashParent('ruleGrid');
													}
													
												});
										});
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
							width : 150,
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
		flex : 1,
		stripeRows : true,
		border : true,
		margins : "0 0 0 0",
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
					if(excId ==4){
						gridExc.getColumnModel().setEditor(9,getBindCombox());
					}
					if(excId ==5){
						gridExc.getColumnModel().setEditor(9,getCheckBox());
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
				var eRecord =e.record;
				e.record.set('name',excuteNameMap[e.value]);
				e.record.set('method',methodData[e.value]);
				e.record.set('package_',packageData[e.value]);
				e.record.set('class_',classData[e.value]);
				var rs =e.value;
				if(excCount ==1){
					if(rs ==4){
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
					if(rs ==5){
						e.record.set('args','true');
						e.record.set('argsName','是');
					}
				}
				if(excCount >1){
						for(var e=0;e<excCount;e++){
							var dz=excDs.getAt(e).get('executeCfgId');
							if(dz ==4){
								zpCount++;
							}
							if(dz ==5){
								ydCount++;
							}
						}
				}
				if(rs==4&&zpCount ==1){
					eRecord.set('args',empBox.getValue());
					eRecord.set('argsName',empNameMap[empBox.getValue()]);
				}
				
				if(rs==5 && ydCount ==1){
					e.record.set('args','true');
					e.record.set('argsName','是');
				}
				if($('ruleId').value == 'null' || $('ruleId').value == ''){
					if(rs==4&&zpCount >1 || rs==5 && ydCount >1){
						
						Ext.Msg.alert('提示消息','此动作已存在',function(id){
							if(id =='ok' || id=='cancel'){
								excDs.removeAt(excCount-1);
							}
						});
					}
				}else{
					if(rs==4&&zpCount >1 || rs==5 && ydCount >1){
						Ext.Msg.alert('提示消息','此动作已存在',function(id){
							if(id =='ok' || id=='cancel'){
								excDs.reload();
							}
						});
					}
				}
				
			}
			if(e.field == 'argsName'){
				var rs =e.record;
				if(e.value=='是'){
					rs.set('args','true');
				}
				if(e.value=='否'){
					rs.set('args','false');
				}
				
			}
	});
	
		var tbl = new Ext.Panel({
			region : 'center',
			width : "100%",
			layout : "hbox",
			layoutConfig : {
				align : 'stretch'
			},
			items : [gridRule,gridExc],
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
					items : [ruleForm,tbl ]
				});
		viewport.doLayout();
		
	//---------------------------------函数
	// 初始化
	function initform() {
		var ruleId = $('ruleId').value;
		if(ruleId == 'null' || ruleId == ''){
			
		}else{
			DWREngine.setAsync(false);	
			mailCheckService.getMailEmpsRuleById(parseInt(ruleId), function(res){
				DWRUtil.setValues(res);
				empBox.bindPageValue("CotEmps", "id", res.empId);
				ruleDs.load({params:{empId:res.empId,type : 'CHECK',ruleId:ruleId}});
				excDs.load({params:{empId:res.empId,type : 'CHECK',ruleId:ruleId}});
			});
			DWREngine.setAsync(true);	
			
		}
	}
	initform();
	// 添加空白record到rule表格中
		function addNewGrid() {
			
				if(Ext.isEmpty(empBox.getValue())){
					Ext.Msg.alert('提示消息','请先选择业务员',function(id){
						if(id=='ok' || id=='cancel'){
							return ;
						}
					})
				}else{
					var u = new ruleDs.recordType({
							leftTerm : "",
							op : "containsatleastone",
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
			if(Ext.isEmpty(empBox.getValue())){
				Ext.Msg.alert('提示消息','请先选择业务员',function(id){
					if(id=='ok' || id=='cancel'){
						return ;
					}
				})
			}else{
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
	
	// 保存
	function save() {
		// 验证表单
		var formData = getFormValues(ruleForm, true);
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		if(!ruleBox.isValid()){
			Ext.Msg.alert('提示消息','请配置好审核规则再保存');
			return;
		}
		
		var form =ruleForm.getForm();
		
		var mailEmpsRule =new CotMailEmpsRule();
		
		 mailCheckService.getDefaultCotMailEmpsRule(empBox.getValue(),function(res){
						if(res !=null){
							mailEmpsRule.id= res.id;
			                mailEmpsRule.xmlPath =res.xmlPath;
						}
		});
		//alert(excDs.getCount());
		var ruleId = $('ruleId').value;

		mailEmpsRule.ruleDefault =1;
		mailEmpsRule.empId =form.findField('bussinessPerson').getValue();		
		mailEmpsRule.ruleName =form.findField('ruleName').getValue();
		mailEmpsRule.ruleDesc =form.findField('ruleDesc').getValue();
		mailEmpsRule.type='CHECK';
		mailEmpsRule.empName =empNameMap[form.findField('bussinessPerson').getValue()];		

			
		if(ruleDs.getCount()==0 || excDs.getCount()==0){
			Ext.Msg.alert('提示信息','请配置好审核规则再保存');
			return;
		}
		
		if(ruleDs.getCount()>0){
			ruleDs.each(function(rec){
				var rs =rec.get('rightTerm');
				if(Ext.isEmpty(rs)){
					Ext.Msg.alert('提示消息','请配置好审核规则再保存');
					return;
				}
			});
		}
		
		if(excDs.getCount()>0){
			if(Ext.isEmpty(excDs.getAt(0).get('executeCfgId'))){
				Ext.Msg.alert('提示消息','请配置好审核规则再保存');
				return;
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
			
		if(excDs.getCount()==1 &&excDs.getAt(0).get('executeCfgId')==5){
			Ext.Msg.alert('提示信息','请先添加邮件审核人再保存');
			return;
		}
		
		if(excDs.getCount()==1 &&excDs.getAt(0).get('executeCfgId')==4){
			var exc = new excDs.recordType({
							ruleId : "",
							executeCfgId:5,
							name : excuteNameMap[5],
						    method : methodData[5],
						    package_ :packageData[5],
						    class_ : classData[5],
						    args :"true" ,
							argsName : "是"
						});
		    excDs.add(exc);
		}	
			
		Ext.MessageBox.confirm('提示信息', '您是否要保存该邮件规则？', function(btn){
			if (btn == 'yes'){
						DWREngine.setAsync(false);	
						mailCheckService.saveOrUpdateMailEmpsRule(mailEmpsRule,function (resId){
							if(resId !=null){
								        $('ruleId').value = resId;
								        $('flag').value = 'flag';
							        	// 更改添加action参数
									    var urlAdd = '&empsRuleId=' + resId + '&custId='+$('bussinessPerson').value +'&type=CHECK';									        	
							        	if (ruleDs.getModifiedRecords().length == 0
															&& ruleDs.removed.length == 0){
													if (excDs.getModifiedRecords().length == 0
															&& excDs.removed.length == 0){
															 Ext.Msg.alert('提示消息','保存成功',function(id){
										                   		if(id == 'ok' || id =='cancel'){
										                   			reflashParent('ruleGrid');	
										                   			window.location.href ='cotmailcheck.do?method=addCheck&id='+resId;
										                   		}
										                   });
													}else{
														excDs.proxy.setApi({
																read : "cotmailcheck.do?method=queryExecute",
																create : "cotmailcheck.do?method=addExc"
																		+ urlAdd,
																update : "cotmailcheck.do?method=modifyExc&type=CHECK",
																destroy : "cotmailcheck.do?method=removeExc"
															});
											    		 excDs.save();
													}			
												}else{
													ruleDs.proxy.setApi({
														read :  "cotmailcheck.do?method=queryRule",
														create : "cotmailcheck.do?method=add"
																+ urlAdd,
														update : "cotmailcheck.do?method=modify&type=CHECK",
														destroy : "cotmailcheck.do?method=remove"
													});
													ruleDs.save();
												}
								       
							    }
							});
				 			DWREngine.setAsync(true);	
			      }
		     });
	}
});
