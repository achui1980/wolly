Ext.ns('Ext.ux.grid');
/**
 * grid插件(给表格单元格加toolTip)
 * azan
 * toolTip:"aaa"---所有单元格都显示同一tooltip "aaa"
 * rowToolTip:"eleId"---每一行的单元格都显示该行 (dataIndex为"eleId")的值
 * column的属性toolTipTxt: 该列的显示文本
 * column的属性toolTipAnchor: tooltip方向
 * column的属性hideDelay: 鼠标移出目标之后，tooltip隐藏之前的延迟时间毫秒数(默认值为 200)。 设置为0，tooltip将立即隐藏。
 * column的属性noToolTip:false|true---默认为true 单元格是否显示tooltip
 * @param {} config
 */

Ext.ux.grid.GridCellToolTip = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
//    this.renderer = this.renderer.createDelegate(this);
};
Ext.ux.grid.GridCellToolTip.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mouseover', this.onMouseOver, this);
        }, this);
    },
    onMouseOver : function(e, t){
        if(Ext.fly(t).hasClass('x-grid3-cell-inner')){
	        e.stopEvent();
	    	var el = Ext.fly(t);
	    	var colIndex = this.grid.getView().findCellIndex(t);
	    	var cfg = this.grid.getView().cm.config;
	    	var txt = this.toolTip?this.toolTip:el.dom.innerText;
	    	
	    	var achor=cfg[colIndex].toolTipAnchor?cfg[colIndex].toolTipAnchor:'top';
	    	var hideDelay=cfg[colIndex].hideDelay?cfg[colIndex].hideDelay:200;
	    	
	    	//如果每行显示同一个tooltip
	    	if(this.rowToolTip){
	    		var header='';
	    		//查找rowToolTip对应的header
	    		for (var i = 0; i < cfg.length; i++) {
	    			if(cfg[i].dataIndex==this.rowToolTip){
	    				header=cfg[i].header;
	    				break;
	    			}
	    		}
	    		var index = this.grid.getView().findRowIndex(t);
            	var record = this.grid.store.getAt(index);
        		txt=header+"："+record.data[this.rowToolTip];
	    	}
	    	//如果某列需要固定显示文本
	    	if(cfg[colIndex].toolTipTxt){
	    		txt =cfg[colIndex].toolTipTxt;
	    		//如果文本中含有<button>
	    		if(txt.indexOf('<button>')>-1 && cfg[colIndex].toolTipFun){
	    			var index = this.grid.getView().findRowIndex(t);
            		var record = this.grid.store.getAt(index);
	    			var fun=cfg[colIndex].toolTipFun.toString();
	    			var id = record.data.id;
	    			if(!id){
	    				id=0;
	    			}
	    			fun=fun.replace(/sId/g,id);
	    			fun=fun.replace(/\n/g,'');
	    			fun=fun.replace(/\r/g,'');
	    			fun=fun.substring(fun.indexOf('{')+1,fun.length-1);
	    			txt = txt.substring(0,7)+' onclick="javascript:'+fun+'"'+txt.substring(7);
	    		}
	    	}
	    	if(!cfg[colIndex].noToolTip){
		        if(!t.tip && (this.toolTip || this.rowToolTip || el.dom.innerText.trim()!='' || cfg[colIndex].toolTipTxt)){
			        var tip = new Ext.ToolTip({
						target : t,
						anchor : achor,
						maxWidth : 200,
						autoHeight:true,
						hideDelay:hideDelay,
						html : txt
					});
					t.tip=tip;
					tip.show();
		        }
	    	}
        }
    }
};

// register ptype
Ext.preg('gridcelltooltip', Ext.ux.grid.GridCellToolTip);
