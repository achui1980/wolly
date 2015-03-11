Ext.onReady(function() {
	var picRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "picName"
			}]);
	var writer = new Ext.data.JsonWriter({
				encode : true,
				writeAllFields : true
			});
	// 创建数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							api : {
								read : "cotelements.do?method=showPicture&eId="
										+ $('eId').value,
								update : "cotelements.do?method=modifyPics"
							}
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, picRecord),
				writer : writer
			});
	// 样品表格顶部工具栏
	var tb = new Ext.Toolbar({
				items : ['->', {
							text : "Create",
							handler : showUploadPanel,
							iconCls : "page_add"
						},'-',{
							text : "Delete",
							handler : delPic,
							iconCls : "page_del"
						},'-',{
							text : "Select / deselect",
							handler : selectAllPic,
							iconCls : "gird_save"
						}
				]
			});
	var toolbar_pic = new Ext.PagingToolbar({
				pageSize : 10,
				store : ds,
				displayInfo : true,
				displayMsg : 'Displaying {0} - {1} of {2}',
				displaySize : '5|10|15|20|all',
				emptyMsg : "No data to display"
			})

	var formatData = function(data) {
		// data.eleName = data.eleName.ellipse(15);
		// data.eleId = data.eleId.ellipse(15);
		// data.eleNameEn = data.eleNameEn.ellipse(15);
		return data;
	};
	var thumbTemplate = new Ext.XTemplate(
			'<tpl for=".">',
			'<div class="thumb-wrap">',
			'<div class="thumb"><img title="{picName}" src="showPicture.action?flag=pic&picId={id}" onclick="showBigPicDiv(this)" onload="DrawImage(this,150,150)"></div>',
			'<span class="x-editable">{picName}</span>' + '</div>', '</tpl>');
	thumbTemplate.compile();
	var picView = new Ext.DataView({
				id : "picView",
				tpl : thumbTemplate,
				multiSelect : true,
				autoScroll : true,
				bodyStyle : "overflow-x:hidden;",
				overClass : 'x-view-over',
				itemSelector : 'div.thumb-wrap',
				emptyText : '<div style="padding:10px;">No data to display</div>',
				plugins : [new Ext.DataView.DragSelector(),
						new Ext.DataView.LabelEditor({
									dataIndex : 'picName',
									saveAuto : true
								})],
				store : ds
			});
	var panel_pic = new Ext.Panel({
				id : "img-chooser-view",
				tbar : tb,
				bbar : toolbar_pic,
				border: false,
				layout : 'fit',
				items : [picView]
			});
	// 将查询面板的条件加到ds中
	ds.load({
				params : {
					start : 0,
					limit : 10
				}
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [panel_pic]
			});

	viewport.doLayout();
	//全选
	function selectAllPic() {
		var nodes=picView.getNodes();
		for (var i = 0; i <nodes.length; i++) {
			if(picView.isSelected(nodes[i])){
				picView.deselect(i);
			}else{
				picView.select(i, true);
			}
		}
	}
});
// 打开上传面板
function showUploadPanel() {
	var id = $('eId').value;
	var opAction = "insert";
	if (id == 'null' || id == '')
		opAction = "insert";
	else
		opAction = "modify";
	var win = new UploadWin({
				params : {
					pEId : id
				},
				gridId : "picView",
				waitMsg : "Picture Loading...",
				opAction : opAction,
				uploadType : "file",
				uploadUrl : './uploadOtherPicture.action',
				validType : "jpg|png|bmp|gif"
			})
	win.show();
}
// 上传成功后的回调函数
function successFn() {
	var picView = Ext.getCmp("picView")
}
// 删除图片
function delPic() {
	// 设置权限为空
	mapPopedom = null;
	var isModPic = getPopedomByOpType("cotpicture.do", "DEL");
	if (isModPic == 0) {// 没有修改图片信息权限
		Ext.Msg.alert("Message", "Sorry, you do not have Authority!!");
		return;
	} else {
		var pic = Ext.getCmp("picView");
		var ds = pic.getStore();
		var id = pic.getSelectedRecords()[0].data.id;
		if (id == null) {
			Ext.Msg.alert("Message", "please select the deleted itmes");
			return;
		};
		Ext.MessageBox.confirm('Message', 'are you sure to delete the selected items?', function(btn) {
					if (btn == 'yes') {
						DWREngine.setAsync(false);
						Ext.each(pic.getSelectedRecords(), function(rec) {
									var tmpid = rec.data.id;
									cotElementsService.deletePictureByPicId(
											parseInt(tmpid), function(res) {
												if (res == false) {
													Ext.Msg.alert("Message",
															"deleted failed!");
												} else {

												}
											});
								})
						ds.reload();
						DWREngine.setAsync(true);
					}
				});
	}
}