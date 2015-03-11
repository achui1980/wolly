Ext.onReady(function() {
	DWREngine.setAsync(false);
	// 菜单栏
	var tbl = new Ext.Toolbar({
				width : "100%",
				height : 30
			});
	DWREngine.setAsync(false);
	cotModuelService.getModuleList(function(res) {
		Ext.each(res, function(item) {
					var menu = new Ext.menu.Menu({});
					cotModuelService.getModuleListByParentId(item.id, function(
									res1) {
								Ext.each(res1, function(item1) {
											menu.add({
														text : item1.moduleName,
														action : item1.moduleUrl,
														iconCls : item1.moduleImgurl,
														urlId : item1.id,
														handler : doAct
													});
										});
								tbl.add({
											text : item.moduleName,
											iconCls : item.moduleImgurl,
											menu : menu
										});
								tbl.addSeparator();

							})

				});

	});
	DWREngine.setAsync(true);
	// 查询默认公司的中英名名称
	var company;
	DWREngine.setAsync(false);
	queryService.findDefaultCompany(function(res) {
				company = res;
			});
	DWREngine.setAsync(true);

	// 顶部pnl
	var topPnl = new Ext.Panel({
		height : 58,
		border : false,
		layout : 'anchor',
		frame : true,
		items : [{
			layout : 'hbox',
			layoutConfig : {
				align : 'top'
			},
			defaults : {
				margins : '0 5 0 0'
			},
			items : [{
				xtype : "label",
				style : 'font-size:16;color:#0022cc;font-weight: bold;margin-left:10',
				text : 'Wolly & Co.'
			}, {
				xtype : 'spacer',
				flex : 1
			}, {
				xtype : 'button',
				text : 'Change Password',
				handler : changPwd,
				iconCls : "key"
			}, {
				xtype : 'button',
				text : 'Re-Login',
				handler : loginout,
				iconCls : "link"
			}, {
				xtype : 'button',
				text : 'Approve',
				handler : showApp,
				iconCls : "link"
			}, {
				xtype : 'button',
				text : 'Help',
				hidden : true,
				handler : help,
				iconCls : "help"
			}, {
				xtype : 'button',
				text : 'Online(0)',
				id : 'loginNumBtn',
				handler : showLoginTree,
				iconCls : "user",
				listeners : {
					'render' : function(btn) {
						cotModuelService.findLoginNum(function(res) {
									btn.setText("Online<font color=red>(" + res
											+ ")</font>");
								});
					}
				}
			}]
		}, {
			layout : 'hbox',
			layoutConfig : {
				padding : '3',
				align : 'top'
			},
			defaults : {
				margins : '0 5 0 0'
			},
			items : [{
				xtype : "label",
				style : 'font-size:12;color:#0022cc;font-weight: bold;margin-left:10',
				text : 'OPS 4.0'
			}, {
				xtype : "label",
				flex : 1,
				style : 'color:#0022cc;font-weight: bold',
				id : "loginEmpId"
			}, {
				xtype : "label",
				text : 'gg',
				width : 190,
				id : "curdate"
			}]
		}]
	});

	// 顶部
	var anchor = {
		id : 'anchor-panel',
		layout : 'anchor',
		border : false,
		items : [topPnl, tbl]
	};

	var main = new Ext.Panel({
		border : false,
		layout : 'fit',
		closable:false,
		items : [{
			xtype : 'iframepanel',
			id : "tab674",
			frameConfig : {
				autoCreate : {
					id : "tab_home1"
				}
			},
			loadMask : {
				msg : 'loading...'
			},
			defaultSrc : "cotorderstatus.do?method=queryOrderStatus",
			listeners : {
				"afterrender" : function(frame) {
					cotEmpsService.getLoginEmp(function(res) {
						if (res != null) {
							if (res.empsId == 'admin') {
								cotOrderService.getPIPO(function(rs) {
											if (rs) {
												var pi = rs[0];
												var po = rs[1];
												if (pi == 0 && po == 0) {

												} else {
													$('PICount').value = pi;
													$('POCount').value = po;
													showWindow.defer(1000);
												}
											}
										});
							} else {
								if (res.shenFlag == 1) {
									// 该员工有审核权限
									cotOrderService.getPIPO(function(rs) {
												if (rs) {
													var pi = rs[0];
													var po = rs[1];
													if (pi == 0 && po == 0) {

													} else {
														$('PICount').value = pi;
														$('POCount').value = po;
														showWindow.defer(1000);
													}
												}
											});
								}
							}
						}
					});

				}
			}
		}]
	});

	var view = new Ext.Viewport({
				layout : 'anchor',
				border : false,
				items : [{
							height : 88,
							anchor : "100%",
							border : false,
							margins : '0 0 0 0',
							items : anchor
						}, {
							xtype : 'tabpanel',
							border : false,
							defaults : {
								autoScroll : true
							},
							enableTabScroll : true,
							plugins : new Ext.ux.TabCloseMenu({
										closeTabText : 'Close Current Page',
										closeOtherTabsText : 'Close Other Pages',
										closeAllTabsText : 'Close All Pages'
									}),
							activeTab : 0,
							anchor : "100% -88",
							id : "maincontent",
							items : [{
										layout : 'fit',
										id : "homepage",
										title : 'Home',
										name : "main",
										border : false,
										items : [main]
									}, {
										layout : 'fit',
										id : "faqpage",
										title : 'Day Report',
										name : "faq",
										border : false,
										items : [new FaqWinHome()]
									}, {
										layout : 'fit',
										id : "orgpage",
										title : 'ORG.',
										name : "org",
										border : false,
										items : [new OrgHome()]
									}]
						}]
			});

	// 加载登陆人
	getEmpInfo();
	DWREngine.setAsync(true);
	var task = {
		run : function() {
			Ext.fly('curdate').update("<div style='font-size:12px'>"
					+ new Date().format('d-m-Y H:i:s l') + "</div>");
		},
		interval : 1000
	}
	Ext.TaskMgr.start(task);
	// 更新登录信息
	var loginInfo = {
		run : function() {
			document.loginaction.location.href = "servlet/OnlineServlet";
		},
		interval : 30 * 1000 // 1 second
	}
	Ext.TaskMgr.start(loginInfo);
});
function doAct(url) {
	doAction(url.action, url.text, url.urlId);
}
function doAction(url, nodename, id) {
	var panel = Ext.getCmp("maincontent");
	var iframe = Ext.getCmp("tab" + id);
	if (iframe != null) {
		// 重新刷新iframe
		panel.setActiveTab("tab" + id);
		iframe.setSrc(url);
	} else {
		var newpanel = panel.add({
					closable:true,
					xtype : 'iframepanel',
					id : "tab" + id,
					title : nodename,
					frameConfig : {
						autoCreate : {
							id : "tab_" + id
						}
					},
					loadMask : {
						msg : 'Loading...'
					},
					defaultSrc : url
				});
		panel.setActiveTab(newpanel);
	}
}

function showApp() {
	cotOrderService.getCountPI(function(res) {
				if (res > 0) {
					$('RePICount').value = res;
				} else {
					$('RePICount').value = 0;
				}
				cotOrderFacService.getCountPO(function(rs) {
							if (rs > 0) {
								$('RePOCount').value = rs;
							} else {
								$('RePOCount').value = 0;
							}

							Approved.defer(1000);
						});
			});

}
function Approved() {
	var PI = $('RePICount').value;
	var PO = $('RePOCount').value
	var approvedPanel = new Ext.form.FormPanel({
		frame : true,
		height : 100,
		width : 300,
		items : [{
			xtype : 'panel',
			width : 300,
			html : "<div style='padding-left:20px;padding-top:15px'>PI:<a href='#' onclick='doAction(\"cotorder.do?method=query&configValue=No"
					+ "\",\"PI\",263)'>need to approve &nbsp;"
					+ PI
					+ "&nbsp;document(s)</a></div>"
		}, {
			xtype : 'panel',
			width : 300,
			html : "<div style='padding-left:20px;padding-top:10px'>PO:<a href='#' onclick='doAction(\"cotorderfac.do?method=query&configValue=No"
					+ "\",\"PO\",355)'>need to approve &nbsp;"
					+ PO
					+ "&nbsp;document(s)</a></div>"
		}]
	});
	var wind = new Ext.Window({
				title : 'W/C Approved',
				layout : 'fit',
				items : [approvedPanel]
			});
	wind.show();
}

function showWindow() {
	var PI = $('PICount').value;
	var PO = $('POCount').value
	var approvedPanel = new Ext.form.FormPanel({
		frame : true,
		height : 100,
		width : 300,
		items : [{
			xtype : 'panel',
			width : 300,
			html : "<div style='padding-left:5px;padding-top:15px'>PI:<a href='#' onclick='doAction(\"cotorder.do?method=query&configValue=No"
					+ "\",\"PI\",263)'>need to approve &nbsp;"
					+ PI
					+ "&nbsp;document(s)</a></div>"
		}, {
			xtype : 'panel',
			width : 300,
			html : "<div style='padding-left:5px;padding-top:10px'>PO:<a href='#' onclick='doAction(\"cotorderfac.do?method=query&configValue=No"
					+ "\",\"PO\",355)'>need to approve &nbsp;"
					+ PO
					+ "&nbsp;document(s)</a></div>"
		}]
	});
	new Ext.ux.ToastWindow({
				title : 'W/C Approved',
				layout : 'fit',
				items : [approvedPanel]
			}).show(document);
}

function help() {
	var _height = window.screen.height;
	var _width = window.screen.width;
	cotModuelService.getSoftVer(function(res) {
				if (res == 1) {
					openMaxWindow(_height, _width, 'help/help_yp.swf')
				}
				if (res == 2) {
					openMaxWindow(_height, _width, 'help/help_bj.swf')
				}
				if (res == 3 || res == 4) {
					openMaxWindow(_height, _width, 'help/help_yp.swf')
				}
			});
}
// 弹出登录人树
var winLogin = null;
function showLoginTree(item, pressed) {
	if (winLogin == null) {
		winLogin = new Ext.Window({
					title : "Online",
					width : 250,
					height : 470,
					layout : "fit",
					items : [{
								xtype : 'logintree',
								ref : 'tree'
							}]
				});
		var tag = item.getEl();
		var left = Ext.Element.fly(tag).getX();
		var top = Ext.Element.fly(tag).getY();

		winLogin.setPosition(left - 170, top + 40);
		winLogin.show();
		// 展开
		winLogin.tree.root.expand(true);
	} else {
		winLogin.close();
		winLogin = null;
	}

	cotModuelService.findLoginNum(function(res) {
				Ext.getCmp('loginNumBtn').setText("Online<font color=red>("
						+ res + ")</font>");
			});
}
function changPwd() {
	doAction("cotpwd.do?method=modPwd", "Change Password");
}
function loginout() {
	if (Ext.isSafari) {
		(window.close()).defer(500);
	} else
		document.location.href = "index.do?method=logoutAction";
}
window.onbeforeunload = function() {
	if (Ext.isIE) {
		if (document.body.clientWidth - event.clientX < 170
				&& event.clientY < 0 || event.altKey) {
			return "Do you want to quit OPS?";
		} else if (event.clientY > document.body.clientHeight || event.altKey) { // 用户点击任务栏，右键关闭
			return "Do you want to quit OPS?";
		} else { // 其他情况为刷新
		}
	} else if (Ext.isChrome || Ext.isOpera) {
		return "Do you want to quit OPS?";
	} else if (Ext.isGecko) {
		window.open("http://www.g.cn")
		var o = window.open("index.do?method=logoutAction");
	}
}
Ext.EventManager.on(window, 'unload', function() {
			if (Ext.isIE) {
				window.location.reload("index.do?method=logoutAction");
			} else if (Ext.isChrome) {
				var o = window.open("index.do?method=logoutAction");
				Ext.Msg.alert("Message", "quiting....");
				o.close();
			} else if (Ext.isSafari) {
				window.open("index.do?method=logoutAction");
			} else if (Ext.isGecko) {
				window.open("index.do?method=logoutAction");
			}
		});
