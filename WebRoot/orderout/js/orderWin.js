OrderWin = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var deNum = getDeNum("orderPricePrecision");

	// 查询材质的发票和报关品名
	DWREngine.setAsync(false);
	// 加载材质表缓存
	var typeData;
	baseDataUtil.getBaseDicList("CotTypeLv1", function(res) {
				typeData = res;
			});
	DWREngine.setAsync(true);

	var roleRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "orderNo"
			}, {
				name : "poNo"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {

					limit : 15
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorderout.do?method=queryOrder&custIdFind="
									+ cfg.custId
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, roleRecord)
			});
	// 创建需要在表格显示的列
	var cm = new Ext.grid.ColumnModel([{
				header : "ID",
				dataIndex : "id",
				width : 50,
				hidden : true
			}, {
				header : "Order No",
				dataIndex : "orderNo",
				width : 120,
				sortable : true
			}, {
				//header : "Po#",
				header:'ClientP/O',
				dataIndex : "poNo",
				width : 120,
				sortable : true
			}]);
	var toolBar = new Ext.PagingToolbar({
				pageSize : 15,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var typeStore = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['0', 'W&C P/O'], ['1', 'Po#']]
			});

	var typeCom = new Ext.form.ComboBox({
				editable : false,
				store : typeStore,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				hiddenName : 'flag',
				width : 90,
				hideLabel : true,
				labelSeparator : " ",
				value : '0',
				noClear : true,
				validateOnBlur : true,
				triggerAction : 'all',
				selectOnFocus : true,
				isSearchField : true,
				searchName : 'flag'
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', typeCom, {
							xtype : 'searchcombo',
							width : 90,
							isSearchField : true,
							searchName : 'orderNoFind',
							isJsonType : false,
							store : ds
						}]
			});

	var grid = new Ext.grid.GridPanel({
				stripeRows : true,
				bodyStyle : 'width:100%',
				store : ds, // 加载数据源
				cm : cm, // 加载列
				loadMask : true, // 是否显示正在加载
				tbar : tb,
				bbar : toolBar,
				region : 'west',
				margins : '0 5 0 0',
				width : 200,
				viewConfig : {
					forceFit : false
				}
			});

	// 明细数据
	var rdDetail = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "sortNo"
			}, {
				name : "eleId"
			}, {
				name : "custNo"
			}, {
				name : "eleNameEn"
			}, {
				name : "boxCount"
			}, {
				name : "boxObCount"
			}, {
				name : "unBoxCount"
			}, {
				name : "containerCount"
			}]);
	// 创建数据源
	var dsDetail = new Ext.data.Store({
				baseParams : {
					limit : 15
				},
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotorderout.do?method=queryOrderOut&orderId=0"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, rdDetail)
			});

	// 创建复选框列
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 创建需要在表格显示的列
	var cmDetail = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [sm, {
					header : "ID",
					dataIndex : "id",
					width : 50,
					hidden : true
				}, {
					header : "Sort No.",
					dataIndex : "sortNo",
					width : 60
				}, {
					header : "Art No.",
					dataIndex : "eleId",
					width : 80
				}, {
					header : "Cust No.",
					dataIndex : "custNo",
					width : 80
				}, {
					header : "Name",
					dataIndex : "eleNameEn",
					width : 100
				}, {
					header : "Quantity",
					dataIndex : "boxCount",
					width : 70
				}, {
					header : "Cartons",
					dataIndex : "containerCount",
					width : 70
				}, {
					header : "Remain Quantity",
					dataIndex : "unBoxCount",
					width : 70
				}, {
					header : "Remain Cartons",
					dataIndex : "unBoxCount",
					width : 70,
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						if (value % record.data.boxObCount != 0) {
							return parseInt(value / record.data.boxObCount) + 1;
						} else {
							return value / record.data.boxObCount;
						}
					}
				}]
	});
	var bbDetail = new Ext.PagingToolbar({
				pageSize : 15,
				store : dsDetail,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			});

	var tbDetail = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : 'searchcombo',
							width : 120,
							emptyText : 'Cust No.',
							isSearchField : true,
							searchName : 'eleIdFind',
							isJsonType : false,
							store : dsDetail
						}, '->', {
							text : "Export",
							iconCls : "page_add",
							handler : exportSelect
						}]
			});

	var gridDetail = new Ext.grid.GridPanel({
				region : "center",
				stripeRows : true,
				store : dsDetail, // 加载数据源
				cm : cmDetail, // 加载列
				sm : sm,
				loadMask : true, // 是否显示正在加载
				tbar : tbDetail,
				bbar : bbDetail,
				viewConfig : {
					forceFit : false
				}
			});

	this.orderNoChk;
	this.poNoChk;
	var orderId;
	// 行点击时加载右边折叠面板表单
	grid.on("rowclick", function(grid, rowIndex, e) {
				var rec = ds.getAt(rowIndex);
				_self.orderNoChk = rec.data.orderNo;
				_self.poNoChk = rec.data.poNo;
				
				orderId=rec.data.id;

				dsDetail.proxy
						.setApi({read : "cotorderout.do?method=queryOrderOut&orderId="
								+ rec.data.id});
				// 加载主订单
				dsDetail.load({
							params : {
								start : 0
							}
						});
			});

	// 导出选择的订单明细
	function exportSelect() {
		var ids = '';
		var cord = sm.getSelections();
		if (cord.length == 0) {
			Ext.MessageBox.alert('Message', 'Please Choose Order Detail!');
			return;
		}

		Ext.each(cord, function(item) {
					ids += item.data.id + ",";
				});
		DWREngine.setAsync(false);
		mask();
		// 将订单单号加到出货的合同号字段
		var orderNo = _self.orderNoChk;
		if ($("orderNumber").value != "") {
			var temp = $("orderNumber").value;
			if (temp.indexOf(orderNo) < 0) {
				$("orderNumber").value += "/" + orderNo;
			}
		} else {
			$("orderNumber").value = orderNo;
		}
		
		cotOrderService.getOrderById(orderId, function(res) {
				if (Ext.getCmp("quality").isVisible()) {
					$('quality').value=res.quality;
					$('colours').value=res.colours;
					$('saleUnit').value=res.saleUnit;
					$('handleUnit').value=res.handleUnit;
					$('assortment').value=res.assortment;
					$('comments').value=res.comments;
					$('shippingMark').value=res.shippingMark;
					$('buyer').value=res.buyer;
					$('seller').value=res.seller;
					$('agent').value=res.agent;
				}else{
					cfg.txtAreaAry.quality = res.quality;
					cfg.txtAreaAry.colours = res.colours;
					cfg.txtAreaAry.saleUnit = res.saleUnit;
					cfg.txtAreaAry.handleUnit = res.handleUnit;
					cfg.txtAreaAry.assortment = res.assortment;
					cfg.txtAreaAry.comments = res.comments;
					cfg.txtAreaAry.shippingMark = res.shippingMark;
					cfg.txtAreaAry.buyer = res.buyer;
					cfg.txtAreaAry.seller = res.seller;
					cfg.txtAreaAry.agent = res.agent;
				}
			
		});
		
		DWREngine.setAsync(true);
		// 查找所有订单明细
		cotOrderOutService.findDetailByIds(ids, function(res) {
					// 判断在新增时,同一订单的同一货号是否已存在,已存在时不添加
					var pds = Ext.getCmp("outDetailGrid").getStore();
					for (var i = 0; i < res.length; i++) {
						insertAgain(res[i], pds);
					}
					unmask();
				});
	}

	// 更新内存数据
	function updateMapValue(orderDetailId, property, value) {
		DWREngine.setAsync(false);
		cotOrderOutService.updateMapValueByEleId(orderDetailId, property,
				value, function(res) {
				});
		DWREngine.setAsync(true);
	}

	// 添加新记录
	function insertAgain(res, pds) {
		var exisRec;
		var flag = false;
		pds.each(function(item) {
					if (item.data.orderDetailId == res.id) {
						flag = true;
						exisRec = item;
						return false;
					}
				});
		var unSendNum = 0;// 未发货量
		var boxCount = res.unBoxCount;// 数量
		var obCount;// 箱数

		if (flag == true) {
			// 已存在该订单明细,并且该明细已保存,这时修改数量和箱数,金额和未出货数
			if (exisRec.id && !isNaN(exisRec.id)) {
				var eU = exisRec.data.unSendNum;
				var bO = exisRec.data.boxObCount;
				if (eU != 0) {
					var us = Number(eU).sub(boxCount);
					if (us < 0) {
						us = 0;
						boxCount = eU;
					}

					exisRec.set("unSendNum", us);
					var newCount = Number(exisRec.data.boxCount).add(boxCount);
					var tM = Number(exisRec.data.orderPrice).mul(newCount);
					exisRec.set("boxCount", newCount);

					// 重新计算总价
					$('totalLab').innerText = (parseFloat($('totalLab').innerText)
							- exisRec.data.totalMoney + tM).toFixed(deNum);

					exisRec.set("totalMoney", tM.toFixed(deNum));

					// 计算箱数
					if (bO != null && bO != 0) {
						if (newCount % bO != 0) {
							obCount = parseInt(newCount / bO) + 1;
						} else {
							obCount = newCount / bO;
						}
					} else {
						obCount = 0;
					}
					exisRec.set("containerCount", obCount);
					
					//未排载数
					var remainBoxCount = Number(exisRec.data.remainBoxCount).add(boxCount);
					exisRec.set("remainBoxCount", remainBoxCount);

					// 更新后台数据
					var dId = exisRec.data.orderDetailId;
					updateMapValue(dId, "unSendNum", us);
					updateMapValue(dId, "boxCount", newCount);
					updateMapValue(dId, "totalMoney", tM.toFixed(deNum));
					updateMapValue(dId, "containerCount", obCount);
					updateMapValue(dId, "remainBoxCount", remainBoxCount);
				}
			}
		} else {
			var totalMoney = (res.orderPrice * boxCount).toFixed(2);
			if (res.currencyId != $('currencyId').value) {
				// 重新换算价格
				DWREngine.setAsync(false);
				queryService.updatePrice(res.orderPrice, res.currencyId,
						$('currencyId').value, function(newPri) {
							res.orderPrice = newPri;
							res.currencyId = $('currencyId').value;
							// 总金额最多保留2位小数
							totalMoney = (res.orderPrice * boxCount).toFixed(2);
						});
				DWREngine.setAsync(true);
			}
			// 计算箱数
			if (res.boxObCount != null && res.boxObCount != 0) {
				if (boxCount % res.boxObCount != 0) {
					obCount = parseInt(boxCount / res.boxObCount) + 1;
				} else {
					obCount = boxCount / res.boxObCount;
				}
				res.containerCount = obCount;
			} else {
				res.containerCount = 0;
			}

			if (res.eleTypeidLv1 != null) {
				for (var i = 0; i < typeData.length; i++) {
					if (typeData[i].faName != null
							&& typeData[i].id == res.eleTypeidLv1) {
						res.orderName = typeData[i].faName;
						break;
					}
				}
			}

			// 获得该行的新序号(该列的最大值+1)
			var sortNo = 0;
			pds.each(function(rec) {
						if (rec.data.sortNo > sortNo)
							sortNo = rec.data.sortNo;
					});

			// 转成出货明细对象
			var cotOrderOutDetail = new CotOrderOutdetail();

			for (var p in cotOrderOutDetail) {
				cotOrderOutDetail[p] = res[p];
			}
			// 序列号
			cotOrderOutDetail.sortNo = sortNo + 1;
			cotOrderOutDetail.id = null;
			// 未出货量
			cotOrderOutDetail.unSendNum = unSendNum;
			// 数量
			cotOrderOutDetail.boxCount = boxCount;
			// 总价
			cotOrderOutDetail.totalMoney = totalMoney;
			// 将订单明细编号存到出货明细的orderDetailId
			cotOrderOutDetail.orderDetailId = res.id;

			// 将订单主单编号存到出货明细
			cotOrderOutDetail.orderNoid = res.orderId;
			cotOrderOutDetail.orderNo = _self.orderNoChk;
			cotOrderOutDetail.poNo = _self.poNoChk;
			
			//未排载数
			cotOrderOutDetail.remainBoxCount = boxCount;

			// 将添加的样品对象储存到后台map中
			DWREngine.setAsync(false);
			cotOrderOutService.findIsExistDetail(cotOrderOutDetail, function(
							asc) {
					});
			DWREngine.setAsync(true);

			var u = new pds.recordType(cotOrderOutDetail);
			Ext.getCmp("outDetailGrid").getStore().add(u);
			// 原总金额
			var oldLab = parseFloat($('totalLab').innerText);
			// 更改总金额
			$('totalLab').innerText = (oldLab + parseFloat(totalMoney))
					.toFixed(deNum);
		}
	}

	// 表单
	var con = {
		title : 'Import Orders',
		layout : 'border',
		width : 830,
		height : 400,
		border : false,
		modal : true,
		items : [grid, gridDetail],
		onEsc : function(k, e) {
			e.stopEvent();
		}
	};

	Ext.apply(con, cfg);
	OrderWin.superclass.constructor.call(this, con);
};
Ext.extend(OrderWin, Ext.Window, {});
