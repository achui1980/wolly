Ext.onReady(function() {
    var empsMap;
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsName", function(res) {
				empsMap = res;
			});
	
	/** ******EXT创建grid步骤******** */
	/* 1、创建数据记录类型类型 Ext.data.Record.create */
	/* 2、创建数据存储对象(数据源) Ext.data.Store */
	/* 3、创建需要在表格显示的列 Ext.grid.ColumnModel */
	/* 4、创建表格对象，加载数据 Ext.grid.GridPanel */

	/** ************** */

	var fields = new Ext.data.Record.create([{
				name : "jobname"
			}, {
				name : "empId",
				type : "int"
			}, {
				name : "previousFireTime"
				
			}, {
				name : "nextFireTime"
			}, {
				name : "state",
				type : "int"
			}, {
				name : "mail"
			}, {
				name : "mailAccount"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
			//	autoLoad:true,
				proxy : new Ext.data.HttpProxy({
							url : "cotmailcontrol.do?method=startAllJobs"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount"
						}, fields),
				listeners: {
					'load' : function(s, res,options){
							Ext.each(res,function(item,index,allItems){
								var i =item.get("state");
								if(i=='0'){
							        Ext.getCmp("toolSt").setText('停止');
							        Ext.getCmp("toolSt").setIconClass('email_ctrl_stop');
							        
								}
							});
					}
				}
			});
	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [sm, {
							header : "任务名称",
							dataIndex : "jobname",
							width : 150,
							renderer : function(value){
								return empsMap[value];
							}
						}, {
							header : "上次执行时间",
							dataIndex : "previousFireTime",
							renderer:function(value){
								if(value)
									return Ext.util.Format.date(new Date(value.time),'Y-m-d H:i:s');
							},
							width : 120
						}, {
							header : "下次执行时间",
							dataIndex : "nextFireTime",
							renderer:function(value){
								return Ext.util.Format.date(new Date(value.time),'Y-m-d H:i:s');
							
							},
							width : 120
						}, {
							header : "状态",
							dataIndex : "state",
							width : 100,
							renderer:function(value){
								var co=value==0?'执行':'暂停';
								return co;
							}	
						}, {
							header : "邮箱地址",
							dataIndex : "mail",
							width : 150
						}, {
							header : "邮箱账号",
							dataIndex : "mailAccount",
							width : 150
						}, 
						{
							header : "操作",
							dataIndex : "empId",
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var pauseOrcont = '<input type="button" name="btn" value="'+(record.get('state')==0?'暂停':'继续')+'" onclick="javascript:pauseOrrestart('+rowIndex+')">';
								var nbsp = "&nbsp &nbsp &nbsp"
								var res = '<input type="button" value="重启" onclick="restartJobByName()">';
								return pauseOrcont + nbsp +res;
							}
						}
						]
			});

	var tb = new Ext.Toolbar({
				items : [{text:'私人邮箱服务'},'->','-',
					{id:'toolSt',cls : "SYSOP_PRIVATESTART" , iconCls: 'email_ctrl_start', text:"启动",
					handler:function(btn){
						var text = btn.getText();
						if(text=='启动'){
							startAllJobs();
							btn.setText('停止');
							btn.setIconClass('email_ctrl_stop');
						}else{
							stopAllJobs();
							btn.setIconClass('email_ctrl_start');
							btn.setText("启动");

						}
					}
					},'-',
					{text:"重启",cls : "SYSOP_PRIVATERESTART" ,iconCls: 'email_ctrl_restart',handler:restartAllJobs},
					'-',
					{text:'刷新',iconCls: 'page_refresh',handler:reloadPrivateMail}]

			});
	var grid = new Ext.grid.GridPanel({
				margins : '0 2 0 0',
				region : "center",
				id : "controlGrid",
			//	autoHeight:true,
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				viewConfig : {
					forceFit : true
				}
			});
	grid.setAutoScroll(true);	
	ds.load();
	
	/*公共邮箱----------------------------------------------------*/
	var tbPublic= new Ext.Toolbar({
				items : [{text:'公共邮箱服务'},'->','-',
					{id:'toolStPublic',cls : "SYSOP_PUBLICSTART",iconCls:'email_ctrl_start',text:"启动",
						handler:function(btn){
							var text = btn.getText();
							if(text=='启动'){
								startAllPublicJobs();
								btn.setText('停止');
								btn.setIconClass('email_ctrl_stop');
							}else{
								stopAllPublicJobs();
								btn.setText("启动");
								btn.setIconClass('email_ctrl_start');
							}
						}
						},'-',

					{text:"重启",iconCls:"email_ctrl_restart",cls : "SYSOP_PUBLICRESTART",handler:restartAllPublicJobs},
					'-',
					{text:'刷新',iconCls: 'page_refresh',handler:reloadPublicMail}]

			});
   // 创建复选框列
	var smPublic = new Ext.grid.CheckboxSelectionModel();
	// 创建数据源
	var dsPublic = new Ext.data.Store({
				autoLoad:true,
				proxy : new Ext.data.HttpProxy({
							url : "cotmailcontrol.do?method=startAllJobs&flag=public"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount"
						}, fields),
				listeners: {
					'load' : function(s, res,options){
							var count = document.getElementsByName("btnPublic");
							Ext.each(res,function(item,index,allItems){
								var i =item.get("state");
								if(i=='0'){
							        Ext.getCmp("toolStPublic").setText('停止');
							         Ext.getCmp("toolStPublic").setIconClass('email_ctrl_stop');
								}
							});
							
					}
				}
			});
	// 创建需要在表格显示的列
	var cmPublic = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [smPublic, {
							header : "任务名称",
							dataIndex : "jobname",
							width : 150
						}, {
							header : "上次执行时间",
							dataIndex : "previousFireTime",
							renderer:function(value){
								if(value)
									return Ext.util.Format.date(new Date(value.time),'Y-m-d H:i:s');
							},
							width : 120
						}, {
							header : "下次执行时间",
							dataIndex : "nextFireTime",
							renderer:function(value){
								return Ext.util.Format.date(new Date(value.time),'Y-m-d H:i:s');
							
							},
							width : 120
						}, {
							header : "状态",
							dataIndex : "state",
							width : 100,
							renderer:function(value){
								var co=value==0?'执行':'暂停';
								return co;
							}	
						}, {
							header : "邮箱地址",
							dataIndex : "mail",
							width : 150
						}, {
							header : "邮箱账号",
							dataIndex : "mailAccount",
							width : 150
						}, {
							header : "操作",
							dataIndex : "empId",
							hidden : true,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var name="public"   
								var pauseOrcont = '<input type="button" value="'+(record.get('state')==0?'暂停':'继续')+'" name="btnPublic" onclick=javascript:pauseOrrestartPublic("'+name+'",'+rowIndex+')>';
								var nbsp = "&nbsp &nbsp &nbsp"
						     	var res = '<input type="button" value="重启" onclick=javascript:restartJobByName("'+name+'")>';
								return pauseOrcont + nbsp +res;
							}
						}]
			});		
	var gridPublic = new Ext.grid.GridPanel({
                id : "controlPublicGrid",
				margins : '0 2 0 0',
				region : "north",
				height:100,
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : dsPublic, // 加载数据源
				cm : cmPublic, // 加载列
				sm : smPublic,
				loadMask : true, // 是否显示正在加载
				tbar : tbPublic,
				viewConfig : {
					forceFit : true
				}
			});
	gridPublic.setAutoScroll(true);

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid,gridPublic]
			});

	});
	/*私人邮箱服务----------------------------------------------------*/
    //启动全部任务
    function startAllJobs(){
    	var requestConfig={
			url : 'cotmailcontrol.do?method=startAllJobs',
			callback:function(options,success,response){
				Ext.getCmp('controlGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));
			}
		}
		Ext.Ajax.request(requestConfig);
    }
	
	//重启全部任务
	function restartAllJobs(){
		var requestConfig={
			url : 'cotmailcontrol.do?method=restartAllJobs',
			callback:function(options,success,response){
				Ext.getCmp('controlGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));
			}
		}
		Ext.Ajax.request(requestConfig);
	}
	
	//停止全部任务
	function stopAllJobs(){
		mailRecvSchedule.stopAll(function(){
			Ext.getCmp('controlGrid').getStore().removeAll();
		});
	}
	
	
	/*公共邮箱服务----------------------------------------------------*/
	
	function startAllPublicJobs(){
		var requestConfig={
			url : 'cotmailcontrol.do?method=startAllJobs',
			params:{flag:'public'},
			callback:function(options,success,response){
				Ext.getCmp('controlPublicGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));
			}
		}
		Ext.Ajax.request(requestConfig);
	}
	
	function restartAllPublicJobs(){
		var requestConfig={
			url : 'cotmailcontrol.do?method=restartAllJobs&flag=public',
			callback:function(options,success,response){
				Ext.getCmp('controlPublicGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));
			}
		}
		Ext.Ajax.request(requestConfig);
	}
	
	function stopAllPublicJobs(){
		mailRecvPublicSchedule.stopAll(function(){
			Ext.getCmp('controlPublicGrid').getStore().removeAll();
		});  
	}

	/*公共方法----------------------------------------------------*/
	// 暂停
	function pauseJobByName(o) {
	    var flag="P";
		if(o=="public"){
			var record =Ext.getCmp('controlPublicGrid').getSelectionModel().getSelected();
			var jobName =record.get("jobname");
	    	mailRecvPublicSchedule.pauseOrresumeJobByName(jobName,flag,function(res){
				record.set('state',1);
				record.commit();
		    });
	    }
	    if(o==undefined){
	    	var jobName =Ext.getCmp('controlGrid').getSelectionModel().getSelected().get("jobname");
			mailRecvSchedule.pauseOrresumeJobByName(jobName,flag,function(res){
				var record =Ext.getCmp('controlGrid').getSelectionModel().getSelected();
				record.set('state',1);
				record.commit();
		});
	    }
		
	}
	// 继续
	function contJobByName(o) {
		var flag="R";
		if(o=="public"){
			var record =Ext.getCmp('controlPublicGrid').getSelectionModel().getSelected();
			var jobName =record.get("jobname");
	    	mailRecvPublicSchedule.pauseOrresumeJobByName(jobName,flag,function(res){
				record.set('state',0);
				record.commit();
		    });
		}
		if(o==undefined){
	    	var jobName =Ext.getCmp('controlGrid').getSelectionModel().getSelected().get("jobname");
			mailRecvSchedule.pauseOrresumeJobByName(jobName,flag,function(res){
				var record =Ext.getCmp('controlGrid').getSelectionModel().getSelected();
				record.set('state',0);
				record.commit();
		  });
	    }
	}
	// 重启指定任务
	function restartJobByName(o) {
		if(o=="public"){
			var record =Ext.getCmp('controlPublicGrid').getSelectionModel().getSelected();
			var jobName =record.get('jobname');
			var empId =record.get('empId');
			var requestConfig={
				url : 'cotmailcontrol.do?method=restartJobByName&flag=public',
				callback:function(options,success,response){
					var res =Ext.util.JSON.decode(response.responseText);
					Ext.getCmp('controlPublicGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));	
				}
			}
			Ext.Ajax.request(requestConfig);
		}
			
		if(o==undefined){
			var record =Ext.getCmp('controlGrid').getSelectionModel().getSelected();
			var jobName =record.get('jobname');
			var empId =record.get('empId');
			var requestConfig={
				url : 'cotmailcontrol.do?method=restartJobByName',
				params : {jobName: jobName,empId : empId},
				callback:function(options,success,response){
					var res =Ext.util.JSON.decode(response.responseText);
					Ext.getCmp('controlGrid').getStore().loadData(Ext.util.JSON.decode(response.responseText));	
				}
			}
			Ext.Ajax.request(requestConfig);
		}

	}
	
	
	function pauseOrrestart(i){
		var count = document.getElementsByName("btn");
		if(count[i].value=='暂停'){
			count[i].value="继续";
			pauseJobByName();
			return;
		}
		if(count[i].value=='继续'){
			count[i].value="暂停";
			contJobByName();
			return;
		}	
	}
	
	function pauseOrrestartPublic(flag,i){
		var count = document.getElementsByName("btnPublic")
		if(count[i].value=='暂停'){
			count[i].value="继续";
			pauseJobByName(flag);
			return;
		}
		if(count[i].value=='继续'){
			count[i].value="暂停";
			contJobByName(flag);
			return;
		}
	}
	
	function reloadPublicMail(){
		dsPublic.reload();
	}

	function reloadPrivateMail(){
		ds.reload();	
	}