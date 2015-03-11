Ext.onReady(function() {
	DWREngine.setAsync(false);
	var curMap = null;
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curMap = res;
			});
	DWREngine.setAsync(true);

	// 价格币种
	var curGridBox = new BindCombox({
				cmpId : 'currencyId',
				dataUrl : "./servlet/DataSelvert?tbname=CotCurrency",
				emptyText : '请选择',
				autoLoad : true,
				hideLabel : true,
				labelSeparator : " ",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%"
			});

	// 加、减
	var flagStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['A', '加'], ['M', '减']]
			});
	var flagBox = new Ext.form.ComboBox({
				name : 'flag',
				editable : false,
				store : flagStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				triggerAction : 'all',
				anchor : "100%",
				emptyText : '请选择',
				hiddenName : 'flag',
				selectOnFocus : true
			});
	/** ******************************加减费用项目表格*********************************************** */
	var otherRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceName"
			}, {
				name : "flag"
			}, {
				name : "amount"
			}, {
				name : "remainAmount"
			}, {
				name : "currencyId"
			}, {
				name : "isImport"
			}, {
				name : "status"
			}, {
				name : "outFlag"
			}, {
				name : "source"
			}]);

	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});

	// 创建数据源
	var otherDs = new Ext.data.Store({
		autoSave : false,
		method : 'post',
		proxy : new Ext.data.HttpProxy({
			api : {
				read : "cotorderout.do?method=queryRecvOther&source=order&fkId="
						+ parent.$('pId').value,
				create : "cotorderout.do?method=addOther",
				update : "cotorderout.do?method=modifyOther"
			},
			listeners : {
				// 保存表格前显示提示消息
				beforewrite : function(proxy, action, rs, options, arg) {
					mask();
				},
				exception : function(proxy, type, action, options, res, arg) {
					// 从异常中的响应文本判断是否成功
					if (res.status != 200) {
						Ext.Msg.alert("提示消息", "操作失败！");
					} else {
						otherDs.reload();
						findNoImportNum();
					}
					unmask();
				}
			}
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, otherRecord),
		writer : writer
	});
	// 创建复选框列
	var otherSm = new Ext.grid.CheckboxSelectionModel({
				moveEditorOnEnter : false
			});
	// 储存数量单元格获得焦点事件的文本框坐标
	var ckX;
	var ckY;
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [otherSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "费用名称",
							dataIndex : "finaceName",
							width : 160,
							editor : new Ext.form.TextField({
										height : 25,
										maxLength : 200,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
											}
										}
									})
						}, {
							header : "加/减",
							dataIndex : "flag",
							width : 60,
							renderer : function(value) {
								if (value == 'A') {
									return '加';
								}
								if (value == 'M') {
									return '减';
								}
							},
							editor : flagBox

						}, {
							header : "金额",
							dataIndex : "amount",
							width : 80,
							editor : new Ext.form.NumberField({
										maxValue : 999999.99,
										decimalPrecision : 2,
										allowNegative:false,
										listeners : {
											'focus' : function(txt) {
												txt.selectText();
												var temp = txt.getPosition();
												ckX = temp[0] + txt.getWidth();
												ckY = temp[1];
											}
										}
									})
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 50,
							renderer : function(value) {
								return curMap[value];
							},
							editor : curGridBox
						}, {
							header : "来源",
							dataIndex : "source",
							width : 100,
							renderer : function(value) {
								if (value == 'orderOther') {
									return '订单加减费用';
								}
								if (value == 'orderRecv') {
									return '订单应收款';
								}
								if (value == 'yi') {
									return '客户溢收款';
								}
							}
						}]
			});

	var pageBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : otherDs,
				displayInfo : true,
				displaySize : 'NONE',
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				emptyMsg : "无记录"
			});
	var toolBar = new Ext.Toolbar({
				items : [{
							text : "新增",
							handler : addNewGrid,
							iconCls : "page_add"
						}, '-', {
							text : "删除",
							handler : onDel,
							iconCls : "page_del"
						},'-', {
							text : "生成应收款",
							handler : addMoreByOtherMoney,
							iconCls : "page_jiao"
						}, '->','-',  {
							text : "全部导入<font color=red>(<span id='noImportNum'>0</span>)</font>",
							handler : importAll,
							iconCls : "gird_exp"
						}, '-', {
							text : "选择导入",
							handler : showImportPanel,
							iconCls : "gird_exp"
						}]
			});
	var otherGrid = new Ext.grid.EditorGridPanel({
				title : "加/减费用项目",
				id : "otherGrid",
				flex : 1,
				stripeRows : true,
				margins : "0 5 0 0",
				border : false,
				cls : 'rightBorder',
				store : otherDs, // 加载数据源
				cm : cm, // 加载列
				sm : otherSm,
				loadMask : true, // 是否显示正在加载
				tbar : toolBar,
				bbar : pageBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 表格编辑前储存该行,用于editor中的一些事件处理
	var editRec = {};
	otherGrid.on('beforeedit', function(e) {
				editRec = e.record;
			});

	// 单元格编辑后,讲修改后的值储存到内存map中,并即时修改右边面板数据
	otherGrid.on("afteredit", function(e) {
				if (e.field == 'finaceName') {
					if (e.value == "") {
						editRec.set('finaceName', e.originalValue);
						Ext.MessageBox.alert('提示消息', '费用名称不能为空!');
					} else {
						var flag = true;
						// 判断store中是否含有该费用名称
						otherDs.each(function(item) {
									if (item.data.finaceName == e.value
											&& item.id != editRec.id) {
										flag = false;
										return false;
									}
								});
						if (flag == false) {
							editRec.set('finaceName', e.originalValue);
							Ext.MessageBox.alert('提示消息', '该费用名称已经存在!');
						} else {
							var id = parent.$('pId').value;
							if (id != "" && id != "null") {
								var recId = 0;
								if (!isNaN(editRec.id)) {
									recId = editRec.id;
								}
								// 判断是否重复
								cotOrderOutService.findIsExistName(e.value, id,
										recId, function(res) {
											if (res == true) {
												editRec.set('finaceName',
														e.originalValue);
												Ext.MessageBox.alert('提示消息',
														'该费用名称已经存在!');
											}
										});
							}
						}
					}
				}
				if (e.field == 'amount') {
					changeOtherAmount(e.value);
				}
				if (e.field == 'currencyId') {
					// 转换金额和剩余金额
					var curAray;
					DWREngine.setAsync(false);
					baseDataUtil.getBaseDicList('CotCurrency', function(res) {
								curAray = res;
							});
					DWREngine.setAsync(true);

					// 将价格转换成主单价格
					var ra;
					var newRa;
					for (var i = 0; i < curAray.length; i++) {
						if (curAray[i].id == e.originalValue) {
							ra = curAray[i].curRate;
						}
						if (curAray[i].id == e.value) {
							newRa = curAray[i].curRate;
						}
					}
					var am = editRec.data.amount;
					editRec.set('amount', (am * ra / newRa).toFixed("2"));
				}
			});

	// 单元格点击后,记住当前行
	otherGrid.on("celldblclick", function(grid, rowIndex, columnIndex, e) {
				var rec = otherDs.getAt(rowIndex);
				var dataIndex = cm.getDataIndex(columnIndex);
				if (rec.data.status == 1) {
					Ext.MessageBox.alert('提示消息', '该费用已经生成应收帐款,不能再修改!');
					return false;
				}
				if (dataIndex=='flag' && (rec.data.source == 'orderOther'
						|| rec.data.source == 'orderRecv'
						|| rec.data.source == 'yi')) {
					return false;
				}
				// 获得view
				var view = grid.getView();
				// 获得单元格
				var cell = view.getCell(rowIndex, columnIndex);
				// 获得该行高度
				var row = view.getRow(rowIndex);
				var editor = cm.getCellEditor(columnIndex, rowIndex);
				editor.setSize(cell.offsetWidth, row.scrollHeight);
			});

	/** ************************应收帐款表格***************************************************** */
	var recvRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "finaceName"
			}, {
				name : "amount"
			}, {
				name : "realAmount"
			}, {
				name : "currencyId"
			}, {
				name : "amountDate"
			}, {
				name : "finaceOtherId"
			}

	]);

	// 创建数据源
	var recvDs = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryRecv&fkId="
										+ parent.$('pId').value
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, recvRecord)
			});
	// 创建复选框列
	var recvSm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var recvCm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [recvSm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "帐款单号",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "应收项目名称",
							dataIndex : "finaceName",
							width : 180
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "已收金额",
							dataIndex : "realAmount",
							width : 60
						}, {
							header : "帐款日期",
							dataIndex : "amountDate",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}]
			});
	var recvToolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : recvDs,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var recvTb = new Ext.Toolbar({
				items : ['->', {
							text : "删除",
							handler : deleteByAccount,
							iconCls : "page_del"
						}]
			});
	var recvGrid = new Ext.grid.GridPanel({
				title : "应收帐款",
				anchor : '100% 50%',
				id : "recvOtherGrid",
				cls : 'leftBorder bottomBorder',
				stripeRows : true,
				border : false,
				store : recvDs, // 加载数据源
				cm : recvCm, // 加载列
				sm : recvSm,
				loadMask : true, // 是否显示正在加载
				tbar : recvTb,
				bbar : recvToolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *********************************收款记录表格*************************************************** */
	var detailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "finaceNo"
			}, {
				name : "addTime"
			}, {
				name : "currencyId"
			}, {
				name : "amount"
			}, {
				name : "currentAmount"
			},{
				name:'finaceRecvid'
			}]);

	// 创建数据源
	var detailds = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryRecvDetail"
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, detailRecord)
			});

	// 加上应收款的选择的记录id
	detailds.on('beforeload', function() {
				var cod = recvSm.getSelections();
				if (cod.length == 1) {
					detailds.baseParams.recvId = cod[0].id;
				} else {
					detailds.baseParams.recvId = 0;
				}

			});
	// 创建复选框列
	var detailsm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var detailcm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [detailsm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "收款单号",
							dataIndex : "finaceNo",
							width : 140
						}, {
							header : "冲帐日期",
							dataIndex : "addTime",
							width : 120,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "Y-m-d");
								}
							}
						}, {
							header : "币种",
							dataIndex : "currencyId",
							width : 60,
							renderer : function(value) {
								return curMap[value];
							}
						}, {
							header : "应收金额",
							dataIndex : "amount",
							width : 60
						}, {
							header : "本次收款",
							dataIndex : "currentAmount",
							width : 60
						}]
			});
	var detailtoolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : detailds,
				displayInfo : true,
				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "无记录"
			});
	var detailTb = new Ext.Toolbar({
				items : ['->', {
							text : "新增",
							handler : windowRecvAdd,
							iconCls : "page_add"
						},{
							text : "删除",
							handler : deleteByRecDetail,
							iconCls : "page_del"
						}]
			});
	var detailGrid = new Ext.grid.EditorGridPanel({
				title : "收款记录",
				// region : "center",
				anchor : '100% 50%',
				id : "detailGrid",
				stripeRows : true,
				border : false,
				cls : 'leftBorder',
				store : detailds, // 加载数据源
				cm : detailcm, // 加载列
				sm : detailsm,
				loadMask : true, // 是否显示正在加载
				tbar : detailTb,
				bbar : detailtoolBar,
				viewConfig : {
					forceFit : false
				}
			});

	/** *****布局********************************************************************************* */
	var panel = new Ext.Panel({
				layout : "anchor",
				flex : 1,
				border : false,
				items : [recvGrid, detailGrid]
			})
	var viewport = new Ext.Viewport({
				layout : "hbox",
				layoutConfig : {
					align : 'stretch'
				},
				items : [otherGrid, panel]
			});
			
	//查询未导入的应收款
	function findNoImportNum(){
		var id=parent.$('pId').value;
		var custId=parent.$('custId').value;
		if(id!='' && id!='null' && custId!='' && custId!='null'){
			cotOrderOutService.findNoImportNum(id,custId,function(res){
				$('noImportNum').innerText=res;
			});
		}
	}
	findNoImportNum();
	/** ************************************************************************************** */
	otherDs.load({
				params : {
					start : 0,
					limit : 2000
				}
			});

	recvDs.load({
				params : {
					start : 0,
					limit : 15
				}
			});

	recvGrid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				detailds.load({
							params : {
								start : 0,
								limit : 10,
								recvId : record.id
							}
						});
			});

	// 收款记录双击事件
	detailGrid.on("rowdblclick", function(grid, rowIndex, columnIndex, e) {
				var record = detailds.getAt(rowIndex);
				openFullWindow('cotfinacerecv.do?method=addFinacerecv&id=' + record.data.finaceRecvid);
			});

	// 添加空白record到表格中
	function addNewGrid() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		var u = new otherDs.recordType({
					finaceName : "",
					flag : "A",
					amount : "",
					currencyId : parent.$('currencyId').value
				});
		otherDs.add(u);
		// 货号获得焦点
		var cell = otherGrid.getView().getCell(otherDs.getCount() - 1, 2);
		if (Ext.isIE) {
			cell.fireEvent("ondblclick");
		} else {
			var e = document.createEvent("Events");
			e.initEvent("dblclick", true, false);
			cell.dispatchEvent(e)
		}
	}

	// 删除其他费用
	function onDel() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var temp = 0;

		var recs = otherSm.getSelections();
		Ext.each(recs, function(item) {
					if (!isNaN(item.id)) {
						var isImport = item.data.outFlag;
						var status = item.data.status;
						var source = item.data.source;
						// 如果状态为1,导过不能删除
						if (status != "1") {
							idsAry.push(item.id);
						}
					} else {
						otherDs.remove(item);
						temp++;
					}
				});
		if (idsAry.length == 0 && temp == 0) {
			Ext.MessageBox.alert('提示消息', "您选择的费用已生成应收帐,必须先删除应收帐才能删除!");
			return;
		}

		if (idsAry.length > 0) {
			Ext.MessageBox.confirm('提示消息', '您是否确定删除选择的费用?已生成应收帐不能删除!',
					function(btn) {
						if (btn == 'yes') {
							cotOrderOutService.deleteByIds(idsAry,
									function(res) {
										if (res == 0) {
											// 更高总金额
											// parent.$('totalLab').innerText=res.toFixed('2');
											Ext.MessageBox
													.alert('提示消息', "删除成功");
											findNoImportNum();
										}
										otherDs.reload();
									});
						}
					});
		}

	}
	
	// 打开添加页面
	function windowRecvAdd() {
		// 添加权限判断
		var isPopedom = getPopedomByOpType('cotfinacerecv.do', "ADD");
		if (isPopedom == 0){
			Ext.Msg.alert("提示信息", "您没有添加收款记录的权限!");
			return;
		}
		var custId = parent.$('custId').value;
		if(custId!=''){
			openFullWindow('cotfinacerecv.do?method=addFinacerecv&orderFlag='+custId);
		}else{
			Ext.Msg.alert("提示信息", "请先选择客户!");
		}
	}

	// 删除收款记录
	function deleteByRecDetail() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限!");
			return;
		}

		var idsAry = new Array();
		var recs = detailsm.getSelections();
		Ext.each(recs, function(item) {
					idsAry.push(item.id);
				});

		if (idsAry.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的记录!");
			return;
		}
		Ext.MessageBox.confirm('提示消息', '您是否确定立即删除选择的收款记录?', function(btn) {
					if (btn == 'yes') {
						cotOrderOutService.deleteByRecvDetail(idsAry, function(
										res) {
									if (res) {
										Ext.MessageBox.alert('提示消息', "删除成功!");
									} else {
										Ext.MessageBox.alert('提示消息', "删除失败!");
									}
									recvDs.reload();
									detailds.reload();
								})
					}
				});
	}

	// 保存其他费用(父页面调用)
	this.saveOther = function() {
		var mainId = parent.$('pId').value;
		var orderNo = parent.$('orderNo').value;

		// 更改添加action参数
		var urlAdd = '&mainId=' + mainId + '&mainNo=' + encodeURI(orderNo);

		// 更改修改action参数
		var urlMod = '&orderNo=' + encodeURI(orderNo);

		otherDs.proxy.setApi({
					read : "cotorderout.do?method=queryRecvOther&fkId="
							+ parent.$('pId').value,
					create : "cotorderout.do?method=addOther" + urlAdd,
					update : "cotorderout.do?method=modifyOther" + urlMod
				});
		otherDs.save();
	}

	// 保存应收涨
	this.saveAccountdeal = function(mainId) {

		// 更改删除action参数
		var urlDel = '&orderPrimId=' + mainId;

		recvDs.proxy.setApi({
					read : "cotorderfac.do?method=queryDeal",
					destroy : "cotorderfac.do?method=removeDeal" + urlDel
				});
		recvDs.save();
	}
	
	//全部导入
	function importAll(){
		var num=$('noImportNum').innerText;
		if(num==0){
			Ext.MessageBox.alert('提示消息', '没有可导入的应收款!');
			return;
		}
		DWREngine.setAsync(false);
		var id=parent.$('pId').value;
		var custId=parent.$('custId').value;
		if(id!='' && id!='null' && custId!='' && custId!='null'){
			// 获得币种集合
			var tempRes;
			var oCur;
			sysdicutil.getDicListByName('currency', function(res) {
					tempRes = res;
					for (var i = 0; i < res.length; i++) {
						if (res[i].id == parent.$('currencyId').value) {
							oCur = res[i].curRate;
							break;
						}
					}
				});
			
			cotOrderOutService.findNoImportAll(id,custId,function(res){
				if(res[0]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[0],function(alt){
						addOther(tempRes,oCur,alt);				
					});
				}
				if(res[1]!=''){
					cotOrderOutService.getListByTable('CotFinaceAccountrecv',res[1],function(alt){
						addRecv(tempRes,oCur,alt);						
					});
				}
				if(res[2]!=''){
					cotOrderOutService.getListByTable('CotFinaceOther',res[2],function(alt){
						addYi(tempRes,oCur,alt);						
					});
				}
			});
		}
		DWREngine.setAsync(true);
	}
	
	// 导入其他费用
	function addOther(tempRes,oCur,alt) {
		Ext.each(alt, function(item) {
					var finaceName = item.finaceName;
					var remainAmount = item.remainAmount;
					var currencyId = item.currencyId;
					var id = item.id;
					var flag = item.flag;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistOther(
								parent.$('pId').value, id, function(res) {
									if (res == true) {
										check = true;
									}
								});
						DWREngine.setAsync(true);
					}
					// 前台判断是否添加行中已存在该其他费用
					otherDs.each(function(itm) {
								if (isNaN(itm.id)) {
									if (itm.data.outFlag == id
											&& itm.data.source == 'orderOther') {
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								remainAmount = tempRes[i].curRate
										* remainAmount;
								break;
							}
						}
						var u = new otherDs.recordType({
									finaceName : finaceName,
									amount : (remainAmount / oCur).toFixed('2'),
									remainAmount : (remainAmount / oCur).toFixed('2'),
									currencyId : parent.$('currencyId').value,
									outFlag : id,
									flag : flag,
									source : 'orderOther'
								});
						otherDs.add(u);
					}
				});
	}

	// 导入应收帐
	function addRecv(tempRes,oCur,alt) {
		// 前台判断是否添加行中已存在该其他费用
		Ext.each(alt, function(item) {
					var finaceName = item.finaceName;
					var zhRemainAmount = item.zhRemainAmount;
					var currencyId = item.currencyId;
					var id = item.id;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistRecv(
								parent.$('pId').value, id, function(res) {
									if (res == true) {
										check = true;
									}
								});
						DWREngine.setAsync(true);
					}
					otherDs.each(function(itm) {
								if (isNaN(itm.id)) {
									if (itm.data.outFlag == id
											&& itm.data.source == 'orderRecv') {
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								zhRemainAmount = tempRes[i].curRate
										* zhRemainAmount;
								break;
							}
						}
						if (finaceName == '预收货款') {
							var u = new otherDs.recordType({
										finaceName : finaceName,
										amount : (zhRemainAmount / oCur)
												.toFixed('2'),
										remainAmount : (zhRemainAmount / oCur).toFixed('2'),
										currencyId : parent.$('currencyId').value,
										outFlag : id,
										flag : 'M',
										source : 'orderRecv'
									});
						} else {
							var u = new otherDs.recordType({
										finaceName : finaceName,
										amount : (zhRemainAmount / oCur)
												.toFixed('2'),
										remainAmount : (zhRemainAmount / oCur).toFixed('2'),
										currencyId : parent.$('currencyId').value,
										outFlag : id,
										flag : 'A',
										source : 'orderRecv'
									});
						}
						otherDs.add(u);
					}
				});
	}
	
	// 导入溢收款
	function addYi(tempRes,oCur,alt) {
		// 前台判断是否添加行中已存在该其他费用
		Ext.each(alt, function(item) {
					var finaceNo = item.finaceNo;
					var finaceName = item.finaceName;
					var amount = item.amount;
					var currencyId = item.currencyId;
					var id = item.id;
					var flag = item.flag;
					var pId = parent.$('pId').value;
					var check = false;
					if (pId != '' && pId != 'null') {
						// 后台判断是否已存在该其他费用
						DWREngine.setAsync(false);
						cotOrderOutService.findIsExistYi(parent.$('pId').value,
								id, function(res) {
									if (res == true) {
										check = true;
										
									}
								});
						DWREngine.setAsync(true);
					}
					otherDs.each(function(itm) {
								if (isNaN(itm.id)) {
									if (itm.data.outFlag == id
											&& itm.data.source == 'yi') {
										check = true;
										return false;
									}
								}
							});
					if (check == false) {
						// 币种转换
						for (var i = 0; i < tempRes.length; i++) {
							if (tempRes[i].id == currencyId) {
								amount = tempRes[i].curRate * amount;
								break;
							}
						}
						var u = new otherDs.recordType({
									finaceName : finaceName,
									amount : (amount / oCur).toFixed('2'),
									remainAmount : (amount / oCur).toFixed('2'),
									currencyId : parent.$('currencyId').value,
									outFlag : id,
									flag : 'M',
									source : 'yi'
								});
						otherDs.add(u);
					}
				});
	}

	// 显示导入界面
	function showImportPanel() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}

		// 判断出货单是否已保存
		var id = parent.$('pId').value;
		if (id == 'null' || id == '') {
			Ext.MessageBox.alert('提示消息', '请先保存出货单!');
		} else {
			// 查询该出货单的所有订单编号
			cotOrderOutService.findOrderId(id, function(res) {
						if (res == "") {
							res = "0,";
						}
						var cfg = {};
						cfg.recvIds = res;
						cfg.custId = parent.$('custId').value;
						var importOtherPanel = new ImportOtherPanel(cfg);
						importOtherPanel.show();
					});
		}
	}

	// 将其他费用添加到应收表格
	function addByOtherMoney(count, ids) {
		var orderNo = parent.$('orderNo').value;
		var custId = parent.$('custId').value;
		var busId = parent.$('businessPerson').value;

		if (orderNo == '' || custId == '' || busId == '') {
			return;
		}
		var detail = new CotFinaceAccountrecv();
		detail.orderNo = orderNo;
		detail.custId = custId;
		detail.businessPerson = busId;
		var companyId = parent.$('companyId').value;
		detail.companyId = companyId;

		// 查询目前是否有应收帐记录
		var pId = parent.$('pId').value;
		DWREngine.setAsync(false);
		cotOrderOutService.findAccountrecvByFkId(pId, function(res) {
					if (res != null) {
						detail = res;
						detail.amount = parseFloat(count);
						detail.remainAmount = parseFloat(count)
								- detail.realAmount;
						detail.zhRemainAmount = detail.remainAmount;
						if (detail.amount == detail.realAmount) {
							detail.status = 1;
						} else {
							detail.status = 0;
						}
					} else {
						detail.finaceName = '出货应收帐';
						var mainId = parent.$('pId').value;
						detail.fkId = mainId;
						detail.source = "orderout";
						detail.status = 0;
						// 主单币种
						var mainCur = parent.$('currencyId').value;
						detail.currencyId = mainCur;
						detail.amount = count;
						detail.realAmount = 0;
						detail.remainAmount = count;
						detail.zhRemainAmount = detail.remainAmount;
						// 生成应收帐款单号
						cotOrderOutService.createRecvNo(parseInt(custId),
								function(res) {
									detail.finaceNo = res;
								});
					}
				});
		DWREngine.setAsync(true);
		DWREngine.setAsync(false);
		// 保存应收帐款
		cotOrderOutService.saveAccountRecv(detail, ids,
				parent.$('orderTime').value, function(recv) {
				});
		DWREngine.setAsync(true);
	}

	// 生成应收帐款
	function addMoreByOtherMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断该出货单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该出货单还没保存,不能生成应收帐款!');
			return;
		}
		// 判断其他费用是否有保存
		if (otherDs.getModifiedRecords().length != 0) {
			Ext.MessageBox.alert('提示消息', '请先保存其他费用,再生成应收帐款!');
			return;
		}

		Ext.MessageBox.confirm('提示消息', '是否确定将所有加减费用生成应收帐款?', function(btn) {
			if (btn == 'yes') {
				var count = parseFloat(parent.$('totalLab').innerText);
				// 将总金额换算成RMB
				DWREngine.setAsync(false);
				var oCur;
				var tempRes;
				sysdicutil.getDicListByName('currency', function(res) {
							tempRes = res;
							for (var j = 0; j < res.length; j++) {
								if (res[j].id == parent.$('currencyId').value) {
									count = res[j].curRate * count;
									oCur = res[j].curRate;
									break;
								}
							}
						});
				DWREngine.setAsync(true);
				var ids = "";
				otherDs.each(function(item) {
							var id = item.id;
							var finaceName = item.data.finaceName;
							var amount = item.data.amount;
							var flag = item.data.flag;
							var chk = item.data.currencyId;
							var status = item.data.status;
							// 过滤掉新增条和已生成的费用
							if (!isNaN(id) && status != "1") {
								ids += id + ",";
								// 查找该币种的汇率
								for (var j = 0; j < tempRes.length; j++) {
									if (tempRes[j].id == chk) {
										var temp = tempRes[j].curRate
												* parseFloat(amount);
										if (flag == 'A') {
											count += temp;
										} else {
											count -= temp;
										}
										break;
									}
								}
							}
						});

				// 将总金额换算回来
				count = (count / oCur).toFixed("2");
				if (ids != '') {
					addByOtherMoney(count, ids);
					otherDs.reload();
					// 重新设置recvDs的查询路径
					recvDs.proxy.setApi({
								read : "cotorderout.do?method=queryRecv&fkId="
										+ parent.$('pId').value
							});
					recvDs.reload();
					Ext.MessageBox.alert('提示消息', '生成成功!');
				} else {
					if (recvDs.getCount() == 0) {
						// 把出货货款生成应收款
						addByOtherMoney(count, ids);
						// 重新设置recvDs的查询路径
						recvDs.proxy.setApi({
									read : "cotorderout.do?method=queryRecv&fkId="
											+ parent.$('pId').value
								});
						recvDs.reload();
						Ext.MessageBox.alert('提示消息', '生成成功!');
					} else {
						Ext.MessageBox.alert('提示消息', '所有其他费用已生成应收款!');
					}
				}
			}
		});
	}

	// 删除应收表格选择的行
	function deleteByAccount() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
		if (isPopedom == 0) {
			Ext.MessageBox.alert('提示消息', "您没有删除权限");
			return;
		}
		var idsAry = new Array();
		var list = recvSm.getSelections();
		// 判断是否有勾选记录
		if (list.length == 0) {
			Ext.MessageBox.alert('提示消息', "请先勾选要删除的应收帐款!");
			return;
		}

		Ext.each(list, function(item) {
					idsAry.push(item.id);
				});

		Ext.MessageBox.confirm('提示消息', '您是否确定删除这些应收帐?', function(btn) {
					if (btn == 'yes') {
						var mainId = parent.$('pId').value;
						cotOrderOutService.deleteByAccount(idsAry[0], mainId,
								function(res) {
									if (res) {
										Ext.MessageBox.alert('提示消息', "删除成功!");
									} else {
										Ext.MessageBox.alert('提示消息',
												"删除失败,应收帐存在收款记录,请先删除这些记录!");
									}
									otherDs.reload();
									recvDs.reload();
								});
					}
				});
	}

	// 预收货款添加到应收表格(生成预收货款后锁定比例和价格)
	function addByOrderMoney() {
		if (parent.$('orderStatus').value == 2 && loginEmpId != "admin") {
			Ext.MessageBox.alert('提示消息', '对不起,该单已经被审核不能再修改!');
			return;
		}

		// 验证表单
		var formData = parent.checkParent();
		// 表单验证失败时,返回
		if (!formData) {
			return;
		}
		// 判断该订单是否已保存
		var mainId = parent.$('pId').value;
		if (mainId == 'null' || mainId == '') {
			Ext.MessageBox.alert('提示消息', '该订单还没保存,不能生成预收帐款!');
			return;
		}
		var totalLab = parent.$('totalLab').innerText;
		if (isNaN(totalLab) || parseFloat(totalLab) == 0) {
			Ext.MessageBox.alert('提示消息', '该订单货款总金额为0!,不能添加预收货款！');
			return;
		}
		if ($('prePrice').value == '') {
			Ext.MessageBox.alert('提示消息', '请先填写预收货款金额！', function(btn) {
						if (btn == 'ok') {
							$('prePrice').focus();
						}
					});
			return;
		}
		// 判断是否已添加预收货款
		var ck = false;
		recvDs.each(function(item) {
					if (item.data.finaceName == '预收货款') {
						ck = true;
						return false;
					}
				});

		if (ck) {
			Ext.MessageBox.alert('提示消息', '您已经添加了预收货款,不能再次添加!');
			return;
		}

		var detail = new CotFinaceAccountrecv();
		var mainId = parent.$('pId').value;
		detail.fkId = mainId;
		detail.finaceName = '预收货款';
		detail.amount = $('prePrice').value;
		detail.currencyId = parent.$('currencyId').value;
		var orderNo = parent.$('orderNo').value;
		detail.orderNo = orderNo;
		// 添加到表格中
		// setObjToAccountGrid(detail);
		var custId = parent.$('custId').value;
		var busId = parent.$('bussinessPerson').value;
		var con = Ext.Msg.confirm('提示消息', '您是否确定将预收货款生成应收帐,并保存至该订单?', function(
				btn) {
			if (btn == 'yes') {
				mask();
				var task = new Ext.util.DelayedTask(function() {
							// 生成应收帐款单号
							cotOrderService.createRecvNo(parseInt(custId),
									function(res) {
										detail.finaceNo = res;
										detail.orderNo = orderNo;
										detail.custId = custId;
										detail.businessPerson = busId;
										var companyId = parent.$('companyId').value;
										detail.companyId = companyId;
										// 保存应收帐款
										cotOrderService.saveAccountRecv(detail,
												parent.$('orderTime').value,
												$('priceScal').value,
												$('prePrice').value, function(
														deal) {
												});
										recvDs.reload();
										unmask();
									});
						});
				task.delay(500);
			}
		});
	}

	// 其他费用金额改变
	function changeOtherAmount(newVal) {
		// 查询来源的单据还有多少金额可以导入!
		var source = editRec.data.source;
		if (source == 'orderOther' || source == 'orderRecv' || source == 'yi') {
			DWREngine.setAsync(false);
			var cur = editRec.data.currencyId;
			cotOrderOutService.findMaxMoney(cur, source, editRec.data.outFlag,
					function(res) {
						if (res != null) {
							var max = 0;
							if (isNaN(editRec.id)) {
								max = res.toFixed(2);
							} else {
								cotOrderOutService.findOldVal(cur, editRec.id,
										function(ov) {
											max = (ov + res).toFixed(2);
										});
							}
							if (newVal > max) {
								editRec.set('amount', max);
							}
							var tip = new Ext.ToolTip({
										title : '提示',
										anchor : 'left',
										html : '来源处剩余金额<font color=red>'
												+ res.toFixed(2)
												+ '</font>,可输入范围(0~' + max
												+ ")!"
									});
							tip.showAt([ckX, ckY]);
						}
					});
			DWREngine.setAsync(true);
		}
	}
});