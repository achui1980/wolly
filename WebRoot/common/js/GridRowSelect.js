Ext.ns('Ext.ux.grid');
/**
 * 做这个插件的原因:
 * 1.表格的rowclick事件 当按键盘上下选择行时不会触发
 * 2.sm的selectionchange和rowselect事件,当点击全选/反选或按shift选择多条时会触发多次
 * 3.在selectionchange或rowselect事件中加buffer也可以解决,但是可能性能会差点
 * 
 * 在grid中添加一个属性selectFn :选择行后要执行的方法
 * @param {} config
 */
Ext.ux.grid.GridRowSelect = function(config) {
	Ext.apply(this, config);
	if (!this.id) {
		this.id = Ext.id();
	}
};
Ext.ux.grid.GridRowSelect.prototype = {
	init : function(grid) {
		var gp=this;
		grid.on('rowclick', this.onRowclick, this);
		grid.on('render', function() {
					var rowNav = new Ext.KeyNav(grid.getGridEl(), {
								'up' : function(e) {
									gp.onUpDown(grid);
								},
								'down' : function(e) {
									gp.onUpDown(grid);
								}
							});
				}, this);
	},
	onRowclick : function(grid, rowIndex, e) {
		if (!grid.selectFn) {
			return;
		}
		var record = grid.getStore().getAt(rowIndex);
		var sm = grid.getSelectionModel();
		//当前点击的行是否被选择
		var isSelsingle = sm.isSelected(record);
		var flag=false;
		//当前总共选择了多少条
		var count = sm.getCount();
		if(count==1){
			flag=true;
			if(!isSelsingle){
				record =sm.getSelected();
			}
		}
		grid.selectFn(record,flag);
	},
	onUpDown : function(grid) {
		var rec = grid.getSelectionModel()
				.getSelected();
		if (grid.selectFn) {
			grid.selectFn(rec);
		}
	}
};

// register ptype
Ext.preg('gridrowselect', Ext.ux.grid.GridRowSelect);
