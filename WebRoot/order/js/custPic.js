this.refreGrid = function() {
	Ext.getCmp('orderPcGrid').getStore().reload();
}
function chkChoose(pId){
		var flag=$("check_pic_"+pId).checked;
		if(flag){
			$("check_pic_"+pId).checked=false;
		}else{
			$("check_pic_"+pId).checked=true;
		}
	}
//阻止事件向父类蔓延
function stopEvent(e){
		e.stopPropagation();
	}
//放大图片
function zoomPic(e,obj){
		e.stopPropagation();
		showBigPicDiv(obj);
	}
CustPic = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	var tipinfo_pic = new Ext.XTemplate(
			"<p><font color=blue>Category：</font>{category}</p>",
			"<p><font color=blue>TempLate:</font>{tempLate}</p>",
			"<p><font color=blue>Remark:</font>{pcRemark}</p>");
	tipinfo_pic.compile();
	var picRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "categoryId"
			}, {
				name : "tempLate"
			}, {
				name : "pcRemark"
			}, {
				name : "custId"
			}, {
				name : "category"
			}, {
				name : "filePath"
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				baseParams : {
					limit : 10
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotorder.do?method=queryCustPic&custId="+$('custId').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, picRecord)
			});
	// 样品表格顶部工具栏
	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['->', '-', {
							text : "Select All/Invert Selection",
							handler : selectAllPic,
							iconCls : "gird_save"
						},'-', {
							text : "Import",
							handler : importCustPc,
							iconCls : "gird_upload",
							cls : "SYSOP_ADD"
						}]
			});
	var toolbar_pic = new Ext.PagingToolbar({
				pageSize : 10,
				store : ds,
				displayInfo : true,
//				displayMsg : '显示第 {0} - {1}条记录 共{2}条记录',
				displaySize : '5|10|15|20|all',
//				emptyMsg : "无记录",
				listeners : {
					beforechange : function(pTbar, params) {
						pTbar.store.setBaseParam('limit', params.limit);
					}
				}
			})

	var formatData = function(data) {
		// data.eleName = data.eleName.ellipse(15);
		// data.eleId = data.eleId.ellipse(15);
		// data.eleNameEn = data.eleNameEn.ellipse(15);
		return data;
	};
	
	var thumbTemplate = new Ext.XTemplate(
			'<tpl for=".">',
			'<div class="thumb-wrap" id="{id}" onclick="chkChoose({id})"><div style="position:relative;width:150px;height:180px;">',
			'<div class="thumb"><img id="qc_img_{id}" src="./showPicture.action?flag=custpc&detailId={id}" onload="DrawImage(this,140,140)"></div>',
			'<span style="white-space: nowrap;text-overflow:ellipsis;overflow:hidden;">{tempLate}</span>' +
			'<input id="check_pic_{id}" type="checkbox" onclick="stopEvent(event)" style="position:absolute;bottom:0px;left:0px;"/>' +
			'<span style="position:absolute;bottom:0px;right:0px;">' +
			"<img style='cursor:hand;' src='common/ext/resources/images/extend/page_white_acrobat.png' title='Preview Pdf' onclick=openPdf('artWorkPdf/client/{filePath}')>"+
			'&nbsp;<img title="Zoom Picture" ' +
			'onclick=zoomPic(event,document.getElementById("qc_img_{id}")) ' +
			'src="common/ext/resources/images/extend/magnifier_zoom_in.png"></span>'
					+ '</div></div>', '</tpl>');
	thumbTemplate.compile();
	var picView = new Ext.DataView({
		tpl : thumbTemplate,
		multiSelect : true,
		autoScroll : true,
		bodyStyle : "overflow-x:hidden;",
		id : "eleGrid",
		loadMask : {
			msg : 'Loading...'
		},
		overClass : 'x-view-over',
		itemSelector : 'div.thumb-wrap',
		emptyText : '<div style="padding:10px;">No images match the specified filter</div>',
		plugins : new Ext.DataView.DragSelector(),
		store : ds,
		listeners : {
			'containerclick' : {
				fn : function() {
					// alert("ddfd")
				},
				scope : this
			},
			'click' : {
				// fn : batchGrid,
				scop : this
			},
			'dblclick' : {
				// fn : windowopenMod,
				scope : this
			},
			'beforeselect' : {
				fn : function(view, node) {
					if (view.isSelected(node))
						view.deselect(node);
				}
			}
		},
		prepareData : formatData.createDelegate(this)
	});
	// 鼠标移到节点，可以显示相应的信息
	picView.on('render', function(view) {
				picView.tip = new Ext.ToolTip({
							target : picView.getEl(), // 需要显示信息的组件
							delegate : '.x-view-over', // 一个domquery的selector，用来控制触发需要在哪里显示，通过CSS识别
							trackMouse : true, // Moving within the row should
							// not hide the tip.
							mouseOffset : [-100, 10],
							renderTo : document.body, // Render immediately so
							// that tip.body can be
							// referenced prior to
							// the first show.
							listeners : { // Change content dynamically
								// depending on which element
								// triggered the show.
								beforeshow : function updateTipBody(tip) {
									var record = view
											.getRecord(tip.triggerElement);
									// alert(record.eleId);
									tip.body.dom.innerHTML = tipinfo_pic
											.apply(record.data)
								}
							}

						})
			});

	// 全选
	function selectAllPic() {
		var nodes = picView.getNodes();
		for (var i = 0; i < nodes.length; i++) {
			chkChoose(nodes[i].id);
			var flag=$("check_pic_"+nodes[i].id).checked;
			if(flag){
				picView.select(i, true);
			}else{
				picView.deselect(i);
			}
		}
	}

	//导入
	function importCustPc() {
		var oSel = document.getElementsByTagName('input');
		var ids='';
        for( i = 0; i< oSel.length; i++ ){
        	if(oSel[i].id){
        		var flag=oSel[i].id.indexOf("check_pic_");
             	if(flag!=-1 && oSel[i].checked){
             		var id = oSel[i].id.substring(flag+10);
             		ids+=id+',';
             	}
        	}
        }
        if(ids==''){
        	Ext.MessageBox.alert("Message", 'Please select one record!');
        }else{
        	cotOrderService.saveOrderPcs(ids,$('orderId').value,dId,function(res){
        		refreGrid();
        		Ext.MessageBox.alert("Message", 'Successfully import！');
        	});
        }
	}
	var dId;
	this.loadGrid = function(detailId,categoryId) {
		dId=detailId;
		ds.proxy.setApi({
					read : "cotorder.do?method=queryCustPic&categoryId=" + categoryId+"&custId="+$('custId').value
				});
		ds.load({
					params : {
						start : 0
					}
				});
	}

	// 构造
	var con = {
		id : "img-chooser-view",
		tbar : tb,
		bbar : toolbar_pic,
		// frame : false,
		region : "center",
		layout : 'fit',
		items : [picView]
	};

	Ext.apply(con, cfg);
	CustPic.superclass.constructor.call(this, con);
};
Ext.extend(CustPic, Ext.Panel, {});