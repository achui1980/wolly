var orderOutId;
var splitId;
Ext.onReady(function() {
	orderOutId = $("orderOutId").value;
	// 加载表格需要关联的外键名
	var orderNoMap;

	var custMap;
	baseDataUtil.getBaseDicDataMap("CotCustomer", "id", "customerShortName",
			function(res) {
				custMap = res;
			});

	var containerTypeMap;
	baseDataUtil.getBaseDicDataMap("CotContainerType", "id", "containerName",
			function(res) {
				containerTypeMap = res;
			});

	/***************************************************************************
	 * 描述: 下拉框
	 **************************************************************************/
	// 客户
	var customerBox = new BindCombox({
		dataUrl : "./servlet/DataSelvert?tbname=CotCustomer&key=customerShortName",
		cmpId : 'custId',
		fieldLabel : "客户名",
		editable : true,
		valueField : "id",
		displayField : "customerShortName",
		pageSize : 5,
		anchor : "95%",
		sendMethod : "post",
		selectOnFocus : true,
		emptyText : '请选择',
		minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
		listWidth : 350,// 下
		triggerAction : 'all'
	});

	// 集装箱类型
	var containerBox = new BindCombox({
				cmpId : 'containerType',
				dataUrl : "./servlet/DataSelvert?tbname=CotContainerType",
				emptyText : '请选择',
				fieldLabel : "<font color='red'>集装箱类型</font>",
				displayField : "containerName",
				valueField : "id",
				triggerAction : "all",
				allowBlank : false,
				blankText : "请选择集装箱类型！",
				anchor : "100%",
				tabIndex : 5
			});
			
	// 绑定集装箱体积
	var selectBind = function(id) {
		DWREngine.setAsync(false);
		var containerCube = 0;
		cotSplitService.getContainerCubeById(id, function(res) {
			if (res != null) {
				containerCube = res.containerCube;
			}
		});
		DWREngine.setAsync(true);
		return containerCube;
	};
	/** ****排载单************************************************* */
	var splitRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "containerNo"
			}, {
				name : "splitDate",
				sortType:timeSortType.createDelegate(this)
				//type:"date"
				//dateFormat:"Y-m-d",
				
			}, {
				name : "labelNo"
			}, {
				name : "orderOutId"
			}, {
				name : "containerType"
			}, {
				name : "containerCube",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			},{
				name : "totalCbm",
				convert : numFormat.createDelegate(this, ["0.00"], 3)
			}, {
				name : "splitRemark"
			}]);
	var writer = new Ext.data.JsonWriter({
		encode : true,
		writeAllFields : true
	});
	// 创建数据源
	var _dsplit = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotsplitinfo.do?method=query",
						create : "cotsplitinfo.do?method=addSplit&orderOutId="+orderOutId,
						update : "cotsplitinfo.do?method=modifySplit&orderOutId="+orderOutId
					},
					listeners : {
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								_dsplit.reload();
								
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							if(action == "destory") return;
							mask();
						}
					}
				}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, splitRecord),
				writer : writer
			});


	// 创建复选框列
	var split_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var split_cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true,
					editor:new Ext.form.TextField({
						listeners : {
							'focus' : function(txt) {
								txt.selectText();
							}
						}
					})
				},
				columns : [split_sm, {
							header : "ID",
							dataIndex : "id",
							width : 50,
							hidden : true
						}, {
							header : "柜号",
							dataIndex : "containerNo",
							width : 140
						}, {
							header : " 排载日期",
							dataIndex : "splitDate",
							width : 100,
							editor:new Ext.form.DateField(),
							renderer : function(value) {
								if (value!= null && value.year != null) {
									return Ext.util.Format.date(new Date(value.year,value.month,value.day),'Y-m-d');
								}else{
									return Ext.util.Format.date(new Date(value),'Y-m-d');
								}
							}
						}, {
							header : " 封签号",
							dataIndex : "labelNo",
							width : 120
						}, {
							header : " 发票编号",
							dataIndex : "orderOutId",
							width : 140,
							hidden:true
						},

						{
							header : " 集装箱类型",
							dataIndex : "containerType",
							width : 120,
							editor:containerBox,
							renderer : function(value) {
								return containerTypeMap[value];
							}
						}, {
							header : " 容积",
							dataIndex : "containerCube",
							width : 60,
							renderer : function(value) {
								return "<span style='color:red;font-weight:bold;'>"
										+ value + "</span>";
							}
						}, {
							header : " 备注",
							dataIndex : "splitRemark",
							width : 250
						}, {
							header : "操作",
							dataIndex : "id",
							width : 80,
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								var del = '<a href="javascript:del(' + value
										+ ')">删除</a>';
								return del;
							}
						}]
			});
	var split_toolBar = new Ext.PagingToolbar({
				pageSize : 10,
				store : _dsplit,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var split_tb = new Ext.Toolbar({
				items : [{
					text:"<font color=red>提示：除备注外，所有数据都要填写，否则无法保存</font>"
				},'->', '-', {
							text : "新增",
							iconCls : "page_add",
							handler : openAddSplit,
							cls : "SYSOP_ADD"
						}, '-', {
							text : "删除",
							cls : "SYSOP_DEL",
							iconCls : "page_del",
							handler : deleteBatch
						}, '-', {
							text : "保存",
							iconCls : "page_mod",
							//cls : "SYSOP_PRINT",
							handler : saveOrupdateSplit
						}, '-', {
							text : "打印",
							iconCls : "page_print",
							//cls : "SYSOP_PRINT",
							handler : showPrint
						}]
			});
	var split_grid = new Ext.grid.EditorGridPanel({
				region : "west",
				id : "splitGrid",
				stripeRows : true,
				width:600,
				collapseMode:'mini',
		    	split: true,
				height : 200,
				store : _dsplit, // 加载数据源
				cm : split_cm, // 加载列
				sm : split_sm,
				loadMask : true, // 是否显示正在加载
				tbar : split_tb,
				bbar : split_toolBar,
				viewConfig : {
					forceFit : false
				}
			});
	//查询排载明细
	_dsplit.on('beforeload', function() {
				_dsplit.baseParams.orderId = orderOutId;
			});
	// 分页基本参数
	_dsplit.load({
				params : {
					start : 0,
					limit : 20
				}
	});
	/** ****排载单明细************************************************* */
	var splitdetailRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			},/* 固定 */
			{
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleNameEn"
			},{
				name : "eleName"
			}, {
				name : "eleSizeDesc"
			},{
				name : "totalBoxCount"
			}, {
				name : "totalContainerCont"
			}, {
				name : "containerCount"
			}, {
				name : "boxCount"
			}, {
				name : "detailRemark"
			}, {
				name : "orderDetailId"
			}, {
				name : "splitId"
			}]);
	var writer = new Ext.data.JsonWriter({
			encode : true,
			writeAllFields : true
		});
	// 创建数据源
	var _dsplitdetail = new Ext.data.Store({
				autoSave : false,
				method : 'post',
				proxy : new Ext.data.HttpProxy({
					api : {
						read : "cotsplitinfo.do?method=querySplitDetail",
						create : "cotsplitinfo.do?method=addSplitDetail",
						update : "cotsplitinfo.do?method=modifySplitDetail",
						destroy : "cotsplitinfo.do?method=removeSplitDetail"
					},
					listeners : {
						beforeload : function(store, options) {
							_dsplitdetail.removed = [];
							cotSplitService.clearSplitMap(function(res) {
							});
						},
						// 添加和修改后进入
						exception : function(proxy, type, action, options, res,
								arg) {
							// 从异常中的响应文本判断是否成功
							if (res.status != 200) {
								Ext.Msg.alert("提示消息", "操作失败！");
							} else {
								_dsplitdetail.reload();
								_dsplit.reload();
								parent.reloadGrid("outDetailGrid");
							}
							unmask();
						},
						// 保存表格前显示提示消息
						beforewrite : function(proxy, action, rs, options, arg) {
							if(action == "destory") return;
							mask();
						}
					}
				}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, splitdetailRecord),
				writer:writer
			});
	var splitdetail_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var splitdetail_cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			editor:new Ext.form.TextField({
				readOnly:true,
				listeners : {
					'focus' : function(txt) {
						txt.selectText();
					}
				}
			})
		},
		columns : [splitdetail_sm,{
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : " 货号",
					dataIndex : "eleId",
					width : 100,
					editable:false,
					summaryRenderer : function(v, params, data) {
						return "合计：";
					}
				}, {
					header : " 客号",
					dataIndex : "custNo",
					editable:false,
					width : 100
				}, {
					header : " 英文品名",
					dataIndex : "eleNameEn",
					editable:false,
					width : 120
				},{
					header : " 发票总数量",
					dataIndex : "totalBoxCount",
					width : 70
				}, {
					header : " 发票总箱数",
					dataIndex : "totalContainerCont",
					width : 70
				}, {
					header : " 本柜箱数",
					dataIndex : "containerCount",
					width : 60,
					summaryType : 'sum',
//					renderer : function(value, metaData, record, rowIndex,
//							colIndex, store) {
//						if (value == record.data.totalContainerCont) {
//							return "<span style='color:green;font-weight:bold;'>"
//									+ value + "</span>";
//						} else {
//							return "<span style='color:red;font-weight:bold;'>"
//									+ value + "</span>";
//						}
//					},
					editor : new Ext.form.NumberField({
						maxValue : 99999999,
						allowDecimals : false,// 是否允许输入小数
						listeners : {
							"change" : function(txt, newVal, oldVal) {
								containerCountChange(txt, newVal, oldVal);
							},
							'focus' : function(txt) {
								txt.selectText();
							}
							
						}
					})
				}, {
					header : " 本柜数量",
					dataIndex : "boxCount",
					width : 60,
					summaryType : 'sum',
//					renderer : function(value, metaData, record, rowIndex,
//							colIndex, store) {
//						if (value == record.data.totalBoxCount) {
//							return "<span style='color:green;font-weight:bold;'>"
//									+ value + "</span>";
//						} else {
//							return "<span style='color:red;font-weight:bold;'>"
//									+ value + "</span>";
//						}
//					},
					editor : new Ext.form.NumberField({
						maxValue : 99999999,
						allowDecimals : false,// 是否允许输入小数
						listeners : {
							"change" : function(txt, newVal, oldVal) {
								boxCountChange(txt, newVal, oldVal);
							},
							'focus' : function(txt) {
								txt.selectText();
							}
						}
					})
				}, {
					header : " 中文品名",
					dataIndex : "eleName",
					editable:false,
					width : 120
				},{
					header : " 中文规格",
					dataIndex : "eleSizeDesc",
					editable:false,
					width : 120
				}, {
					header : " 备注",
					dataIndex : "detailRemark",
					width : 250
				}, {
					header : "操作",
					dataIndex : "id",
					width : 70,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var nbsp = "&nbsp;&nbsp;&nbsp;"
						var del = '<a href=javascript:delDetail(' + value + ','
								+ record.data.orderDetailId + ','
								+ record.data.splitId + ')>删除</a>';
						return del + nbsp;
					}
				}]
	});

	var splitdetail_toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : _dsplitdetail,
				displayInfo : true,
				//displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			});
	var splitdetail_tb = new Ext.Toolbar({
				items : ["->",{
							text : "添加货号",
							iconCls : "page_add",
							handler : showImportPanel
						}, '-', {
							text : "删除货号",
							iconCls : "page_del",
							tooltip :"<font color=red>删除货号后，请点击保存进行最后删除</font>",
							handler : onDelDetail
						},'-',{
							text:"保存",
							iconCls:"page_mod",
							handler:saveSplitDetail
						}]
			});
	var summary1 = new Ext.ux.grid.GridSummary();
	var splitdetail_grid = new Ext.grid.EditorGridPanel({
				region : "center",
				id : "splitdetailGrid",
				margins : "0 0 0 2",
				width:600,
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : _dsplitdetail, // 加载数据源
				cm : splitdetail_cm, // 加载列
				sm:splitdetail_sm,
				plugins : [summary1],
				loadMask : true, // 是否显示正在加载
				tbar : splitdetail_tb,
				bbar : splitdetail_toolBar,
				viewConfig : {
					forceFit : false
				}
			});

	// 分页基本参数
	// _dsigndetail.load({params:{start:0, limit:15}});
	split_grid.on("afteredit",function(obj){
		if(obj.field == "containerType"){
			var cube = selectBind(obj.record.get("containerType"));
			obj.record.set("containerCube",cube);
		}
	})
	split_grid.on('rowclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				splitId = record.get("id");
				//alert(splitId);
				if(splitId == null) return;
				$('totalLab').innerText = record.get("totalCbm");
				
//				_dsplitdetail.on("beforeload",function(){
//					alert("before:"+splitId)
//					_dsplitdetail.baseParams = new Object();
//					_dsplitdetail.baseParams.splitId = splitId;
//					_dsplitdetail.baseParams.orderOutId = orderOutId;
//					_dsplitdetail.baseParams.flag = 'queryDetail';
//				});
				var readUrl = "cotsplitinfo.do?method=querySplitDetail&splitId="+splitId+"&orderOutId="+orderOutId+"&flag=splitDetail";
				_dsplitdetail.proxy.setApi(Ext.data.Api.actions.read,readUrl);
				_dsplitdetail.load({
							params : {
								start : 0,
								limit : 15,
								flag : 'queryDetail'
							}
						});
			});


	// 单击修改信息 start
	split_grid.on('rowdblclick', function(grid, rowIndex, event) {
				var record = grid.getStore().getAt(rowIndex);
				//openEditSplit(record.get("id"));
			});


	//排载界面
	var vp = new Ext.Viewport({
				layout : 'border',
				items : [split_grid, splitdetail_grid]
			})
			
	//排载明细操作
	// 显示导入界面
	var _self = this;
	function showImportPanel() {
		var record = split_grid.getSelectionModel().getSelections()[0];
		if(record == null){
			Ext.Msg.alert("消息提示","请选择一条排载单");
			return;
		}
		var splitId = record.get("id");
		if(splitId == null){
			Ext.Msg.alert("消息提示","请选择一条已保存的排载单");
			return;
		}
		var cfg = {};
		cfg.orderOutId = orderOutId;
		cfg.bar = _self;
	
		var importPanel = new ImportPanel(cfg);
		importPanel.show();
	}
	// 添加选择的样品到可编辑表格
	this.insertSelect = function(list) {
		insertByBatch(list);
	}

	// 添加选择的样品到可编辑表格
	function insertByBatch(list) {

		var record = split_grid.getSelectionModel().getSelections()[0];
		var splitId = record.get("id");
		var idsAry = new Array();
		Ext.each(list, function(item) {
					idsAry.push(item.data.id);
				});
		for (var i = 0; i < idsAry.length; i++) {
			DWREngine.setAsync(false);
			cotSplitService.findDetail(splitId, parseInt(idsAry[i]), function(
							detail) {
						if (detail != null) {
							insertToGrid(detail);
						}
					});
			DWREngine.setAsync(true);
		}
		unmask();
	}
	// 根据产品货号查找样品数据
	function insertToGrid(detail) {
//		if(_dsplitdetail.findExact("eleId",detail.eleId) != -1){
//			Ext.Msg.alert("信息提示","货号:"+detail.eleId+"已存在，请在原来的基础上修改");
//			return;
//		}
		var orderDetailId = detail.id;
		var record = split_grid.getSelectionModel().getSelections()[0];
		var splitId = record.get("id");
		DWREngine.setAsync(false);
		var cotSplitDetail = new CotSplitDetail();

		cotSplitDetail.eleId = detail.eleId;
		cotSplitDetail.custNo = detail.custNo;
		cotSplitDetail.eleNameEn = detail.eleNameEn;
		cotSplitDetail.totalBoxCount = detail.boxCount;
		cotSplitDetail.totalContainerCont = detail.containerCount;
		cotSplitDetail.eleName = detail.eleName;
		cotSplitDetail.eleSizeDesc = detail.eleSizeDesc;

		cotSplitDetail.containerCount = Math.ceil((detail.remainBoxCount)
				* detail.containerCount / detail.boxCount);

		cotSplitDetail.boxCount = detail.remainBoxCount;
		cotSplitDetail.detailRemark = detail.eleRemark;
		cotSplitDetail.orderDetailId = detail.id;
		cotSplitDetail.splitId = splitId;

		// 重新计算总cbm
		//var orderOutId = orderOutId;
		cotSplitService.getTotalCBM(orderOutId, cotSplitDetail.orderDetailId,
				function(cbm) {
					if (cotSplitDetail.totalContainerCont != 0) {
						$('totalLab').innerText = (parseFloat($('totalLab').innerText) + (cotSplitDetail.containerCount / cotSplitDetail.totalContainerCont)
								* cbm).toFixed('2');
					}
				});
		// 将送样明细对象储存到后台GivenMap中
		cotSplitService.setSplitMap(orderDetailId, cotSplitDetail,
				function(res) {
				});

		setObjToGrid(cotSplitDetail);
		DWREngine.setAsync(true);
	}
	// 添加带有数据的record到表格中
	function setObjToGrid(obj) {
		var u = new _dsplitdetail.recordType(obj);
		_dsplitdetail.add(u);
	}
	// 单元格编辑后
	splitdetail_grid.on("afteredit", function(e) {
				cotSplitService.updateMapValueByEleId(
						e.record.data.orderDetailId, e.field, e.value,
						function(res) {
						});

			});
	//表格编辑前储存该行,用于editor中的一些事件处理		
	var editRec = {};
	splitdetail_grid.on('beforeedit', function(e) {
				editRec = e.record;
			});
	// 本柜箱数改变
	function containerCountChange(txt, newVal, oldVal) {
		var rec = editRec;
		var oldBoxCount = rec.get("boxCount");
		// 每箱的数量
		var count = rec.get("totalBoxCount");
		var container = rec.get("totalContainerCont");
		var res = Math.ceil(count / container);// 每箱的数量

		// 查看剩余数量
		var orderDetailId = rec.get("orderDetailId");
		var record = split_grid.getSelectionModel().getSelections()[0];
		var splitId = record.get("id");
		var containerCube = record.get("containerCube");
		if (splitId == 'null' || splitId == '') {
			splitId = -1;
		}

		DWREngine.setAsync(false);
		cotSplitService.getRemainCountAndExist(splitId, orderOutId,
				orderDetailId, function(outcount) {// res为剩余数量+已出货数量
					if (parseInt(outcount) / parseInt(res) < newVal) {
						Ext.Msg.alert("提示框", "未出货数量已不足！");
						txt.setValue(oldVal);
						return;
					} else {
						var boxCount = res * newVal;
						rec.set("boxCount", boxCount);
						cotSplitService.getBoxCBM(orderOutId, orderDetailId,
								function(cbm) {
									var temp = parseFloat($('totalLab').innerText);

									$('totalLab').innerText = (parseFloat($('totalLab').innerText)
											- oldVal * cbm + newVal * cbm)
											.toFixed('2');

									var totalCbm = parseFloat($('totalLab').innerText);

									if (totalCbm > containerCube) {
										Ext.Msg.alert("提示框", "超过集装箱容积,请修改数据!");
//										txt.setValue(oldVal);
//										rec.set("boxCount", oldBoxCount);
//										$('totalLab').innerText = temp;
//										return;
									} else {
										
									}
									cotSplitService.updateMapValueByEleId(
												rec.data.orderDetailId,
												"boxCount", boxCount, function(
														res) {
									});
								});
					}
				});
		DWREngine.setAsync(true);
	}

	// 本柜数量改变
	function boxCountChange(txt, newVal, oldVal) {
		var rec = editRec;
		var oldContainerCount = rec.get("containerCount");
		// 每箱的数量
		var count = rec.get("totalBoxCount");
		var container = rec.get("totalContainerCont");
		var res = Math.ceil(count / container);// 每箱的数量

		// 查看剩余数量
		var orderDetailId = rec.get("orderDetailId");
		var record = split_grid.getSelectionModel().getSelections()[0];
		var splitId = record.get("id");
		var containerCube = record.get("containerCube");
		if (splitId == 'null' || splitId == '') {
			splitId = -1;
		}

		DWREngine.setAsync(false);
		cotSplitService.getRemainCountAndExist(splitId, orderOutId,
				orderDetailId, function(outcount) {// res为剩余数量+已出货数量
					if (outcount < newVal) {
						Ext.Msg.alert("提示框", "未出货数量已不足！");
						txt.setValue(oldVal);
						return;
					} else {
						var containerCount = Math.ceil(newVal / res);
						rec.set("containerCount", containerCount);
						cotSplitService.getBoxCBM(orderOutId, orderDetailId,
								function(cbm) {
									var temp = parseFloat($('totalLab').innerText);

									$('totalLab').innerText = (parseFloat($('totalLab').innerText)
											- oldContainerCount * cbm + containerCount
											* cbm).toFixed('2');

									var totalCbm = parseFloat($('totalLab').innerText);

									if (totalCbm > containerCube) {
										Ext.Msg.alert("提示框", "超过集装箱容积,请修改数据!");
//										txt.setValue(oldVal);
//										rec.set("containerCount",
//												oldContainerCount);
//										$('totalLab').innerText = temp;
//										return;
									} else {
										
									}
									cotSplitService.updateMapValueByEleId(
												rec.data.orderDetailId,
												"containerCount",
												containerCount, function(res) {
												});
								});
					}
				});
		DWREngine.setAsync(true);
	}
	//TODO:需要修改
	function saveSplitDetail(){
		var record = split_grid.getSelectionModel().getSelections()[0];
		var splitId = record.get("id");
		// 更改添加action参数
		var urlAdd = '&splitPrimId=' + splitId+ '&orderOutId=' + orderOutId;
		// 更改修改action参数
		var urlMod = '&splitPrimId=' + splitId+ '&orderOutId=' + orderOutId;
		// 更改删除action参数
		var delMod = '&splitPrimId=' + splitId+ '&orderOutId=' + orderOutId;
		_dsplitdetail.proxy.setApi({
			read : "cotsplitinfo.do?method=querySplitDetail&splitId="+ splitId + "&flag=splitDetail",
			create : "cotsplitinfo.do?method=addSplitDetail"+ urlAdd,
			update : "cotsplitinfo.do?method=modifySplitDetail"+ urlMod,
			destroy : "cotsplitinfo.do?method=removeSplitDetail"+ delMod
		});
		_dsplitdetail.save();
	}
	// 删除
	function onDelDetail() {
		var record = split_grid.getSelectionModel().getSelections()[0];
		if(record == null){
			Ext.Msg.alert("消息提示","请选择记录");
			return;
		}
		var splitId = record.get("id");
		var cord = splitdetail_sm.getSelections();
		DWREngine.setAsync(false);
		Ext.each(cord, function(item) {
			//var orderOutId = orderOutId;
			var orderDetailId = item.data.orderDetailId;
			var containerCount = item.data.containerCount;
			//alert(orderOutId)
			cotSplitService.getBoxCBM(orderOutId, orderDetailId, function(res) {
				//alert(res);
				$('totalLab').innerText = (parseFloat($('totalLab').innerText) - containerCount
						* res).toFixed('2');
				//delDetail(item.data.id,orderDetailId,splitId)
				//_dsplitdetail.remove(item);
			});
			_dsplitdetail.remove(item);
		});
		//_dsplit.reload();
		DWREngine.setAsync(true);
	}
})

// //=========================操作====================================
// 获得排载表格选择的记录
function getSplitIds() {
	var list = Ext.getCmp("splitGrid").getSelectionModel().getSelections();
	var res = new Array();
	Ext.each(list, function(item) {
				var cotSplitInfo = new CotSplitInfo();
				cotSplitInfo.id = item.id;
				res.push(cotSplitInfo);
			});
	return res;
}

// 显示打印面板
var printWin;
function showPrint(item) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "PRINT");
	if (isPopedom == 0) {
		Ext.MessageBox.alert("提示信息", '您没有打印权限！');
		return;
	}
	if (printWin == null) {
		printWin = new PrintWin({
					type : 'split'
				});
	}
	if (!printWin.isVisible()) {
		var po = item.getPosition();
		printWin.setPosition(po[0] - 200, po[1] + 25);
		printWin.show();
	} else {
		printWin.hide();
	}
};
function saveOrupdateSplit(){
	var store = Ext.getCmp("splitGrid").getStore();
	store.save();
}
// 删除排载明细
function delDetail(id, orderDetailId, splitId,bReload) {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示框", "您没有删除权限");
		return;
	}

	var cotSplitDetail = new CotSplitDetail();
	var list = new Array();
	cotSplitDetail.id = id;
	cotSplitDetail.orderDetailId = orderDetailId;
	cotSplitDetail.splitId = splitId;
	list.push(cotSplitDetail);

	Ext.MessageBox.confirm('提示信息', '确定删除该排载明细吗?', function(btn) {
				if (btn == 'yes') {
					DWREngine.setAsync(false);
					// 查询该排载明细是否被删除
					cotSplitService.getSplitDetailById(id, function(res) {
								if (res != null) {
									// 获取主出货单号
									cotSplitService.getOrderOutIdBySplitId(
											splitId, function(res) {
												// 删除前将排载明细中该货号已出货的数量及cbm加回出货明细中对应货号的剩余数量及cbm中
												cotSplitService.addCountAndCbm(
														splitId, res, id,
														function(res) {
														});
												cotSplitService
														.modifySplitFlag(res,
																function(res) {
																});
											});
									//
									cotSplitService.modifyCotSplitCbm(splitId,
											orderDetailId, function(res) {
											});
									cotSplitService.deleteSplitDetailList(list,
											function(res) {
												if (res) {
													Ext.Msg
															.alert("提示框",
																	"删除成功");
													reloadGrid("splitdetailGrid");
												} else {
													Ext.Msg.alert("提示框",
															"删除失败，该排载明细已经被使用中");
												}
											})
								} else {
									reloadGrid("splitdetailGrid");
								}
							});
					parent.reloadGrid("outDetailGrid");
					DWREngine.setAsync(true);
				}
			});
}

// 打开排载新增页面
function openAddSplit() {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
	if (isPopedom == 0)// 没有添加权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	if(orderOutId == '' || orderOutId == null){
		alert("还未生成出货单");
		return;
	}
	var containerNo = "";
	var splitDate = getCurrentDate("yyyy-MM-dd")
	DWREngine.setAsync(false);
	cotSeqService.getContainerNo(splitDate, function(ps) {
		containerNo = ps;
	});
	var store = Ext.getCmp("splitGrid").getStore();
	var u = new store.recordType({
		"containerNo":containerNo,
		"splitDate":getDateType(splitDate),
		"labelNo":"",
		"orderOutId":orderOutId,
		"containerType":"",
		"containerCube":"",
		"splitRemark":""
	})
	store.add(u);
	DWREngine.setAsync(true);
	//openFullWindow('cotsplitinfo.do?method=add&orderOutId=' + orderOutId);

}

// 打开排载编辑页面
function openEditSplit(obj) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "MOD");
	if (isPopedom == 0)// 没有修改权限
	{
		Ext.Msg.alert("提示信息", "您没有修改权限");
		return;
	}
	if (obj == null) {
		var ids = getSplitIds();
		if (ids.length == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		} else if (ids.length > 1) {
			Ext.Msg.alert("提示信息", "只能选择一条记录!")
			return;
		} else
			obj = ids[0].id;
	}
	openFullWindow('cotsplitinfo.do?method=add&id=' + obj);
}

// 删除
function del(id) {
	// 添加权限判断
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示框", "您没有删除权限");
		return;
	}
	
	var cotSplit = new CotSplitInfo();
	var list = new Array();
	cotSplit.id = id;
	list.push(cotSplit);

	Ext.MessageBox.confirm('提示信息', '确定删除该排载单吗?', function(btn) {
				if (btn == 'yes') {
					cotSplitService.deleteSplit(list, function(res) {
								if (res == -1) {
									Ext.Msg.alert("提示框", "已有其它记录使用到该记录,无法删除");
									return;
								} else {
									Ext.Msg.alert("提示框", "删除成功");
									reloadGrid("splitGrid");
								}
							});
				} else {
					return;
				}
			});
}

// 批量删除
function deleteBatch() {
	var isPopedom = getPopedomByOpType(vaildUrl, "DEL");
	if (isPopedom == 0)// 没有删除权限
	{
		Ext.Msg.alert("提示框", "您没有删除权限");
		return;
	}
	var list = getSplitIds();
	if (list.length == 0) {
		Ext.Msg.alert("提示框", "请选择记录");
		return;
	}

	Ext.MessageBox.confirm('提示信息', '确定删除选中的排载单吗?', function(btn) {
				if (btn == 'yes') {
					cotSplitService.deleteSplit(list, function(res) {
								if (res == -1) {
									Ext.Msg.alert("提示框", "已有其它记录使用到该记录,无法删除");
									return;
								} else {
									Ext.Msg.alert("提示框", "删除成功");
									reloadGrid("splitGrid");
								}
							});
				} else {
					return;
				}
			});
	DWREngine.setAsync(true);
}