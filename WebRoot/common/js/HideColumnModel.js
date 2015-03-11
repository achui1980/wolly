/**
 * 根据权限隐藏表格列
 * @class HideColumnModel
 * @extends Ext.grid.ColumnModel
 */
HideColumnModel = Ext.extend(Ext.grid.ColumnModel, {
	//默认为false，传进的参数格式为{‘要隐藏的dataIndex’:true}
	hiddenCols : false,
	setConfig : function(d, b) {
				var e, h, a;
				if (!b) {
					delete this.totalWidth;
					for (e = 0, a = this.config.length; e < a; e++) {
						h = this.config[e];
						if (h.setEditor) {
							h.setEditor(null)
						}
					}
				}
				this.defaults = Ext.apply({
							width : this.defaultWidth,
							sortable : this.defaultSortable
						}, this.defaults);
				this.config = d;
				this.lookup = {};
				for (e = 0, a = d.length; e < a; e++) {
					h = Ext.applyIf(d[e], this.defaults);
					if(this.hiddenCols!=false && this.hiddenCols[h.dataIndex]){
						h.hidden=this.hiddenCols[h.dataIndex];
					}
					if (Ext.isEmpty(h.id)) {
						h.id = e
					}
					if (!h.isColumn) {
						var g = Ext.grid.Column.types[h.xtype || "gridcolumn"];
						h = new g(h);
						d[e] = h
					}
					this.lookup[h.id] = h
				}
				if (!b) {
					this.fireEvent("configchange", this)
				}
			}
});
Ext.reg('hidecolumnmodel', HideColumnModel);