Ext.onReady(function() {
	var picRecord = new Ext.data.Record.create([{
				name : "id",
				type : "int"
			}, {
				name : "eleId"
			}, {
				name : "orderTime",
				convert : function(value) {
					return Ext.util.Format.date(new Date(value.time), "Y-m-d");
				}
			}]);
	// 创建数据源
	var ds = new Ext.data.Store({
				autoLoad : {
					params : {
						start : 0
					}
				},
				baseParams : {
					limit : 10
				},
				proxy : new Ext.data.HttpProxy({
							url : "cotcustomer.do?method=queryOrderPic&cid="
									+ $('cid').value
						}),
				reader : new Ext.data.JsonReader({
							root : "data",
							totalProperty : "totalCount",
							idProperty : "id"
						}, picRecord)
			});
	var toolbar_pic = new Ext.PagingToolbar({
				pageSize : 10,
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
			'<div class="thumb"><img src="showPicture.action?flag=order&detailId={id}" onload="DrawImage(this,150,150)"></div>',
			'<span>{eleId}</span>' + '<span>Date:{orderTime}</span>' + '</div>',
			'</tpl>');
	thumbTemplate.compile();
	var picView = new Ext.DataView({
				id : "picView",
				tpl : thumbTemplate,
				singleSelect : true,
				overClass : 'x-view-over',
				itemSelector : 'div.thumb-wrap',
				emptyText : '<div style="padding:10px;">No data to display</div>',
				store : ds
			});

	var tb = new Ext.ux.SearchComboxToolbar({
				items : ['-', {
							xtype : "datefield",
							emptyText : "Start Date",
							width : 90,
							format : "Y-m-d",
							id : 'startTime',
							vtype : 'daterange',
							endDateField : 'endTime',
							isSearchField : true,
							searchName : 'startTime'
						}, {
							xtype : "datefield",
							emptyText : "the end of time",
							width : 90,
							format : "Y-m-d",
							id : 'endTime',
							vtype : 'daterange',
							startDateField : 'startTime',
							isSearchField : true,
							searchName : 'endTime'
						}, {
							xtype : 'searchcombo',
							width : 1,
							cls : 'hideCombo',
							editable : false,
							isJsonType : false,
							store : ds
						}]
			});
	var panel_pic = new Ext.Panel({
				id : "img-chooser-view",
				autoScroll : true,
				bbar : toolbar_pic,
				border : false,
				region : "center",
				autoWidth : true,
				tbar:tb,
				items : [picView]
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				items : [panel_pic]
			});

	viewport.doLayout();
});
