/**
 * 询盘单明细报价历史
 * @class PanPriceHis
 * @extends Ext.Window
 */
PanPriceHis = Ext.extend(Ext.Window,{
	/**
	 * cot_pan_ele的id
	 * @type Number
	 */
	panId:0,
	/**
	 * 数据表格
	 * @type 
	 */
	dataView:null,
	initComponent:function(){
	var me = this;
	var empData,curData
	DWREngine.setAsync(false);
	// 加载币种表缓存
	baseDataUtil.getBaseDicDataMap("CotCurrency", "id", "curNameEn", function(
					res) {
				curData = res;
			});
	baseDataUtil.getBaseDicDataMap("CotEmps", "id", "empsId", function(
					res) {
				empData = res;
			});
	DWREngine.setAsync(true);
	// 厂家报价表格
	var roleFac = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {}, {
				name : "panId"// 询盘明细id
			},{
				name : "boxObcount"
			}, {
				name : "price",// 供应商报价
				type : "float",
				convert : numFormat.createDelegate(this, ["0.000"], 3)
			}, {
				name : "ccyId"// 供应商报价币种
			}, {
				name : "valDate",
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'state'// 状态
			}, {
				name : 'eleId'
			}, {
				name : 'modDate',// 修改时间
				sortType : timeSortType.createDelegate(this)
			}, {
				name : 'modPerson'// 联系人
			},{
				name:'remark'
			},{
				name:'willSupplier'
			},{
				name:'fileUrl'
			},{
				name:'uploadEmp',type:'int'
			}]);
	// 创建数据源
	var dsFac = new Ext.data.Store({
		autoLoad : {
			params : {
				start : 0,
				limit : 200
			}
		},
		method : 'post',
		proxy : new Ext.data.HttpProxy({
			url:'cotpan.do?method=queryPanDetail&panId='+me.panId
		}),
		reader : new Ext.data.JsonReader({
					root : "data",
					totalProperty : "totalCount",
					idProperty : "id"
				}, roleFac)
	});
	// 创建复选框列
	var smFac = new Ext.grid.CheckboxSelectionModel();
	// 创建需要在表格显示的列
	var cmFac = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
				columns : [smFac, {
							header : "ID",
							dataIndex : "id",
							width : 25,
							hidden : true
						}, {
							header : "<font color=red>Supplier</font>",
							align : 'center',
							dataIndex : "willSupplier",
							width : 100
						}, {
							header : "<font color=red>Price</font>",
							dataIndex : "price",
							width : 80
						}, {
							header : "<font color=red>Currency</font>",
							dataIndex : "ccyId",
							width : 60,
							renderer : function(value) {
								if (value != null) {
									return curData[value];
								}
							}
						},{
							header : "Attachment",
							align : 'center',
							dataIndex : "fileUrl",
							width : 200,
							renderer:function(value,metadata,record,rowIndex,colIndex,store){
								if(value){
									var panId = record.id;
									var idx = value.lastIndexOf('\\');
									var name = value.substr(idx+1);
									//如果是pdf直接打开
									var pre = value.lastIndexOf('.');
									if(pre>=0){
										var preTxt = value.substr(pre+1);
										if(preTxt.toLowerCase()=='pdf'){
											var rdm = Math.round(Math.random() * 10000);
											//把"\"转换成"/"
											value=value.replace(/\\/g,"/");
											return '<a id="scSrc" onclick=openEleWindow(".'+value+'?tmp='+rdm+'") style="cursor: hand;text-decoration: underline;">'+name+'</a>'
										}
									}
									return '<a href="./servlet/DownPanAttachServlet?panId='+panId+'">'+name+'</a>'
								}
								
							}
						},{
							header : "Upload Staff",
							align : 'center',
							dataIndex : "uploadEmp",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return empData[value];
								}
							}
						},{
							header : "Comment",
							align : 'center',
							dataIndex : "remark",
							width : 100
						},{
							header : "Upload Date",
							align : 'center',
							dataIndex : "modDate",
							width : 100,
							renderer : function(value) {
								if (value != null) {
									return Ext.util.Format.date(
											new Date(value.time), "d-m-Y");
								}
							}
						}]
			});
	var toolBarFac = new Ext.PagingToolbar({
				pageSize : 200,
				store : dsFac,
				displayInfo : true,
				displaySize : 'NONE'
			});
	var addPerson=Ext.getCmp('addPersonCombo').getValue();
	var tb = new Ext.Toolbar({
		hidden:(logId!=addPerson && loginEmpId!='admin')?true:false,
		items:['->',{
			text:'Use Seleted Price',
			iconCls : "page_add",
			handler:this.useSelPrice.createDelegate(this)
		}]
	})
	// 厂家报价表格
	var gridFac = new Ext.grid.GridPanel({
				region:'center',
//				id : "priceGrid",
				ref:'priceGrid',
				stripeRows : true,
				store : dsFac,
				cm : cmFac,
				sm : smFac,
				loadMask : true,
				tbar : tb,
				border : false,
				bbar : toolBarFac,
				viewConfig : {
					forceFit : true,
					getRowClass : function(record, index) {
						if (record.get("willSupplier")==me.manufactorer && record.get("ccyId")==me.currencyId && record.get("price")==me.panPrice){
							return "x-grid-record-green";
						}
					}
				},
				listeners:{
					'rowdblclick':function(grid,index){
						var rec = grid.getStore().getAt(index);
						if (logId!=addPerson && loginEmpId!='admin' && me.state==1 && rec.get("willSupplier")==me.manufactorer && rec.get("ccyId")==me.currencyId && rec.get("price")==me.panPrice){
							Ext.Msg.alert("Message", "Already Confirmed！");
							return;
						}
						if(rec != null)
							me.modData(rec.id,grid);
					}
				}
			});
		this.layout='border';
		this.items=[gridFac];
		//this.closeAction='hide';
		this.width = 800;
		this.height = 400;
		PanPriceHis.superclass.initComponent.call(this);
	},
	modData:function(id,grid){
		var win = new Ext.Window({
			modal:true,
			items:[{
				xtype:'pandetailpriceform',
				priceDetailId:id,
				grid:grid,
				batch:false
			}],
			width:400,
			height:300
		});
		win.show();
	},
	useSelPrice:function(){
		var win=this;
		var store = this.dataView.getStore();
		var record = store.getById(this.panId) ;
		var grid = this.priceGrid;
		var rec = grid.getSelectionModel().getSelected();
		if(!rec){
			Ext.Msg.alert("Message", "Please Choose One Record!");
			return;
		}
		var price = rec.get('price');
		var currencyId = rec.get('ccyId');
		//设置价格和币种
		record.set('panPrice',price);
		record.set('currencyId',currencyId);
		record.set('manufactorer',rec.get('willSupplier'));
		this.close();
		record.commit();
		mask();
		savePanEle(record.id);
		cotPanService.updatePanEleState(record.id,1, function(res) {
			viewport.dataview.getStore().reload();
			unmask();
		});
	}
})