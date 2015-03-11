// 订单麦标导入到生产合同麦标
OrderMBGrid = function(cfg) {
	
	var _self = this;
	if (!cfg)
		cfg = {};
	
	var orderRecord = new Ext.data.Record.create([
		{name : "id",type : "int"},/* 固定 */
		{name : "orderNo"},
		{name : "orderTime"}
	]);
	// 创建数据源
	var _dorder = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "cotquery.do?method=queryOrderMb&orderIds="+cfg.orderIds
		}),
		reader : new Ext.data.JsonReader({
			root : "data",
			totalProperty : "totalCount",
			idProperty : "id"
		}, orderRecord)
	});

	// 创建复选框列
	var order_sm = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var order_cm = new Ext.grid.ColumnModel([
		order_sm,// 添加复选框列
		{
			header : "ID", 
			dataIndex : "id", 
			width : 50,
			sortable : true,
			hidden : true
		}, {
			header : " Order No.", 
			dataIndex : "orderNo", 
			width : 300,
			sortable : true
		}, {
			header : " Order date", 
			dataIndex : "orderTime", 
			width : 300,
			renderer : function(value) {
				if (value != null) {
					return Ext.util.Format.date(
						new Date(value.time), "Y-m-d");
				}
			},
			sortable : true
			
		}
	]);
	var order_toolBar = new Ext.PagingToolbar({
		pageSize : 20,
		store : _dorder,
		displayInfo : true,
		displayMsg : 'Displaying {0} - {1} of {2}',
		displaySize : '5|10|15|20|all',
		emptyMsg : "No data to display"
	});
	
 	var tb = new Ext.Toolbar({
		items : ['->', {
			text : "Import",
			handler : updateOrderFacMb,
			iconCls : "page_add",
			cls : "SYSOP_ADD"
		}]
	});	

	var order_grid = new Ext.grid.GridPanel({
		region : "north",
		id : "orderGrid",
		height : 160,
		stripeRows : true,
		bodyStyle : 'width:100%;',
		autoScroll : true,
		store : _dorder, // 加载数据源
		cm : order_cm,// 加载列
		sm : order_sm,
		loadMask : true, // 是否显示正在加载
		bbar : order_toolBar,
		tbar : tb,
		viewConfig : {
			forceFit : false
		}
	});

	// 分页基本参数
	_dorder.load({
		params : {
			start : 0,
			limit : 20
		}
	});
	
	// 行点击时加载右边折叠面板表单
	order_grid.on("rowclick", function(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		showMbInfo(record.get("id"));
	});
	
	
	// 唛图片
	var maiPicPanel = new Ext.Panel({
		layout : 'fit',
		anchor : '100% 50%',
		border : false,
		frame : true,
		items : [{
			xtype : "fieldset",
			//bodyStyle : 'padding-left:25px',
			title : "Mark",
			layout : "hbox",
			labelWidth : 60,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			buttons : [{
						text : '',
						hidden : true
					}],
			items : [{
				xtype : "panel",
				width : 140,
				html : '<div align="center" style="width: 100px; height: 100px;">'
						+ '<img src="common/images/zwtp.png" id="orderMb_MB" name="orderMb_MB"'
						+ 'onload="javascript:DrawImage(this,100,100)" onclick="showBigPicDiv(this)"/></div>'
			}]
		}]
	});
	
	// 产品标
	var productPicPanel = new Ext.Panel({
		layout : 'fit',
		anchor:'100% 50%',
		border : false,
		frame : true,
		items : [{
			xtype : "fieldset",
			title : "Product Mark",
			layout : "hbox",
			labelWidth : 60,
			layoutConfig : {
				padding : '5',
				pack : 'center',
				align : 'middle'
			},
			buttons : [{
						text : '',
						hidden : true
					}],
			items : [{
				xtype : "panel",
				width : 140,
				html : '<div align="center" style="width: 100px; height: 100px;">'
						+ '<img src="common/images/zwtp.png" id="productMb_MB" name="productMb_MB"'
						+ 'onload="javascript:DrawImage(this,100,100)" onclick="showBigPicDiv(this)"/></div>'
			}]
		}]
	});
	
	// 正册唛信息
	var maiInfoPanel = new Ext.form.FormPanel({
		layout : 'form',
		region : 'center',
		border : false,
		items : [{
			layout : 'hbox',
			anchor : "100% 50%",
			border : false,
				layoutConfig : {
					align : 'stretch'
				},
				items : [{
					title : "正唛",
					flex : 1,
					xtype : 'panel',
					layout : 'fit',
					margins : '0 5 0 0',
					border : false,
					items : [{
						xtype : 'textarea',
						id : "orderZMAreaMb",
						name : "orderZMAreaMb",
						maxLength : 500
					}]
				}, {
					title : "侧唛",
					flex : 1,
					layout : 'fit',
					border : false,
					xtype : 'panel',
					items : [{
						xtype : 'textarea',
						id : "orderCMAreaMb",
						name : "orderCMAreaMb",
						maxLength : 500
					}]
				}]
			}, {
				layout : 'hbox',
				anchor : "100% 50%",
				border : false,
				layoutConfig : {
					align : 'stretch'
				},
				items : [{
					title : "Center box mark",
					flex : 1,
					layout : 'fit',
					xtype : 'panel',
					margins : '0 5 0 0',
					border : false,
					items : [{
						xtype : 'textarea',
						id : "orderZHMAreaMb",
						name : "orderZHMAreaMb",
						maxLength : 500
					}]
				}, {
					title : "Inner box mark",
					flex : 1,
					layout : 'fit',
					xtype : 'panel',
					border : false,
					items : [{
						xtype : 'textarea',
						id : "orderNMAreaMb",
						name : "orderNMAreaMb",
						maxLength : 500
					}]
				}]
		}]
	});
	
	var tp = new Ext.Panel({
		layout : 'anchor',
		region : 'west',
		width:'30%',
		border : false,
		items : [maiPicPanel,productPicPanel]
	});
	
	// 唛明细
	var maiPanel = new Ext.Panel({
		layout : 'border',
		region : 'center',
		border : false,
		items : [tp,maiInfoPanel]
	});
	
	function showMbInfo(id){

		// 加载订单信息
		cotOrderService.getOrderById(parseInt(id), function(res) {
			// 加载麦标
			$('orderMb_MB').src = "./showPicture.action?detailId=" + id
					+ "&flag=orderMB";
			// 加载产品标
			$('productMb_MB').src = "./showPicture.action?detailId=" + id
					+ "&flag=productMB";
			$('orderZMAreaMb').value = res.orderZM;
			$('orderZHMAreaMb').value = res.orderZHM;
			$('orderCMAreaMb').value = res.orderCM;
			$('orderNMAreaMb').value = res.orderNM;
			var tip = new Ext.ToolTip({
				target : $('productMb_MB'),
				anchor : 'left',
				maxWidth : 190,
				minWidth : 190,
				title:'Product Standard',
				html : res.productM
			});
		});
	}	
	
	function getIds() {
		var list = Ext.getCmp("orderGrid").getSelectionModel().getSelections();
		var res = new Array();
		Ext.each(list, function(item) {
			res.push(item.id);
		});
		return res;
	}
	
	function updateOrderFacMb(){
	
		var ids = getIds();
		if(ids.length==0){
			Ext.MessageBox.alert('Message','Please select one Order!');
			return;
		}else if(ids.length>1){
			Ext.MessageBox.alert('Message','Choose only one order!');
			return;
		}
	
		var orderfacId = $('pId').value;

	  	 Ext.MessageBox.confirm('Message', 'Are you sure you import?', function(btn) {
			if(btn == 'yes'){
				DWREngine.setAsync(false);
				if(orderfacId != null && orderfacId != ''){
					cotOrderFacService.updateCotOrderFacMb(parseInt(orderfacId),parseInt(ids[0]),function(res){
						if(res){
							Ext.MessageBox.alert('Message','mport successful!');
							//setTimeout("reloadMb()", 600);
							reloadMb();
						}else{
							Ext.MessageBox.alert('Message','Import failed!');
						}
					});
				}
				DWREngine.setAsync(true);
			}
	  	 })
	}
	
	function reloadMb() {
		var id = $('pId').value;
		var rdm = getRandom();
		if(id != null && id !=''){
			DWREngine.setAsync(false);
			// 加载采购单信息
			cotOrderFacService.getOrderFacById(parseInt(id), function(res) {
				// 唛标
				$('order_MB').src = "./showPicture.action?flag=orderFacMB&rdm="+rdm+"&detailId="+ res.id;
				$('product_MB').src = "./showPicture.action?flag=productFacMB&rdm="+rdm+"&detailId="+ res.id;
				$('orderZMArea').value = res.orderZm;
				$('orderZHMArea').value = res.orderZhm;
				$('orderCMArea').value = res.orderCm;
				$('orderNMArea').value = res.orderNm;
				$('productArea').value = res.productM;
				_self.close();
			})
			DWREngine.setAsync(true);
		}
	}
	
	// 表单
	var con = {
		title : 'Mark header information into the order',
		layout : 'border',
		width : 700,
		height : 500,
		border : false,
		modal : true,
		padding : "0",
		//closeAction : 'hide',
		items : [order_grid,maiPanel]
	};
	Ext.apply(con, cfg);
	OrderMBGrid.superclass.constructor.call(this, con);
}
Ext.extend(OrderMBGrid, Ext.Window, {});