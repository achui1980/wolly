Ext.namespace('Ext.ux.form');

/**
 * 自动补全，结合SearchField和Combox
 * 查询参数为query
 * queryStore为自动补全，store为要操作的数据
 * 可能需要重写paramFn，与clearParamFn
 * @class Ext.ux.form.SearchComboxField
 * @extends Ext.form.TwinTriggerField
 */
Ext.ux.form.SearchComboxField = Ext.extend(Ext.form.TwinTriggerField, {
    defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
    listClass : '',
    selectedClass : 'x-combo-selected',
    listEmptyText: '',
    triggerClass : 'x-form-arrow-trigger',
    shadow : 'sides',
    listAlign : 'tl-bl?',
    maxHeight : 300,
    minHeight : 90,
    triggerAction : 'all', // 默认 query
    minChars : 0, // 默认4
    autoSelect : false, // 默认true
    typeAhead : false,
    queryDelay : 500,
    pageSize : 10, // 默认0
    selectOnFocus : false,
    queryParam : 'query',
    loadingText : '加载中。。。', // Loading...
    resizable : false,
    handleHeight : 8,
    allQuery: '',
    mode: 'remote',
    minListWidth : 250, //默认70
    forceSelection : false,
    typeAheadDelay : 250,
    lazyInit : true,
    clearFilterOnReset : true,
    submitValue: undefined,
//    rendered:true, // 默认为false
    /**
     * 自动补全Store
     * @type 
     */
    queryStore : null,
    //
    displayField:'id',
    //
	valueField:'id',
	/**
	 * 是否自动补全，为true时，queryStore不能为空
	 * @type Boolean
	 */
	autoComplet:false,
	ref:'searchField',　//
	validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:130, //原来180
    hasSearch : false,
    paramName : 'query',
    /**
     * 与SearchComboxToolbar结合的属性
     * @type Boolean
     */
    isSearchField:true,
    isJsonType:true, // 传向后台是否采用JSON对象，否则以传参形式
    /**
     * true 验证数据是否修改，存在修改则提示数据未保存，不进行查询
     * @type Boolean
     */
    checkModified:false,
   	/**
   	 * 用来将值封装到对象中，并返回查询条件对象
   	 * 如果返回空则回复到原来不查询时状态
   	 * 该方法只简单回返回对象，对查询有特殊操作，需要重写
   	 * @param {String} v 查询中的值
   	 * @param {SearchField} searchField 查询Field本身
   	 * @return {Object}
   	 */
    paramFn : function(v,searchField){
    	return v;
    },
    /**
     * 清除查询时，将触发该函数
     * 例：用来清除与查询有关控件的值
     * @type 
     */
    clearParamFn : Ext.emptyFn,
    
    initComponent : function(){
        Ext.ux.form.SearchComboxField.superclass.initComponent.call(this);
        this.addEvents(
            'expand',
            'collapse',
            'beforeselect',
            'select',
            'beforequery'
        );
        if(this.transform){
            var s = Ext.getDom(this.transform);
            if(!this.hiddenName){
                this.hiddenName = s.name;
            }
            if(this.autoComplet&&!this.queryStore){
                this.mode = 'local';
                var d = [], opts = s.options;
                for(var i = 0, len = opts.length;i < len; i++){
                    var o = opts[i],
                        value = (o.hasAttribute ? o.hasAttribute('value') : o.getAttributeNode('value').specified) ? o.value : o.text;
                    if(o.selected && Ext.isEmpty(this.value, true)) {
                        this.value = value;
                    }
                    d.push([value, o.text]);
                }
                this.queryStore = new Ext.data.ArrayStore({
                    'id': 0,
                    fields: ['value', 'text'],
                    data : d,
                    autoDestroy: true
                });
                this.valueField = 'value';
                this.displayField = 'text';
            }
            s.name = Ext.id(); // wipe out the name in case somewhere else they have a reference
            if(!this.lazyRender){
                this.target = true;
                this.el = Ext.DomHelper.insertBefore(s, this.autoCreate || this.defaultAutoCreate);
                this.render(this.el.parentNode, s);
            }
            Ext.removeNode(s);
        }
        //auto-configure store from local array data
        else if(this.queryStore){
            this.queryStore = Ext.StoreMgr.lookup(this.queryStore);
            if(this.queryStore.autoCreated){
                this.displayField = this.valueField = 'field1';
                if(!this.queryStore.expandData){
                    this.displayField = 'field2';
                }
                this.mode = 'local';
            }
        }

        this.selectedIndex = -1;
        if(this.mode == 'local'){
            if(!Ext.isDefined(this.initialConfig.queryDelay)){
                this.queryDelay = 10;
            }
            if(!Ext.isDefined(this.initialConfig.minChars)){
                this.minChars = 0;
            }
        }
        this.on({
			'specialkey':{
				fn:function(f,e){
					 if(e.getKey() == e.ENTER){
		                this.onTrigger2Click();
		            }else if (e.getKey() == e.ESC){
				    	this.onTrigger1Click();
				    }
				},
				scope:this
			},
			'focus':{
				fn:function(f){
					this.selectText();
				},
				scope:this
			}
		});
    },

    // private
    onRender : function(ct, position){
        if(this.hiddenName && !Ext.isDefined(this.submitValue)){
            this.submitValue = false;
        }
        Ext.ux.form.SearchComboxField.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName,
                    id: (this.hiddenId||this.hiddenName)}, 'before', true);

        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }

        if(!this.lazyInit){
            this.initList();
        }else{
            this.on('focus', this.initList, this, {single: true});
        }
    },

    // private
    initValue : function(){
        Ext.ux.form.SearchComboxField.superclass.initValue.call(this);
        if(this.hiddenField){
            this.hiddenField.value =
                Ext.value(Ext.isDefined(this.hiddenValue) ? this.hiddenValue : this.value, '');
        }
    },

    getParentZIndex : function(){
        var zindex;
        if (this.ownerCt){
            this.findParentBy(function(ct){
                zindex = parseInt(ct.getPositionEl().getStyle('z-index'), 10);
                return !!zindex;
            });
        }
        return zindex;
    },

    // private
    initList : function(){
        if(!this.list){
            var cls = 'x-combo-list',
                listParent = Ext.getDom(this.getListParent() || Ext.getBody()),
                zindex = parseInt(Ext.fly(listParent).getStyle('z-index'), 10);

            if (!zindex) {
                zindex = this.getParentZIndex();
            }

            this.list = new Ext.Layer({
                parentEl: listParent,
                shadow: this.shadow,
                cls: [cls, this.listClass].join(' '),
                constrain:false,
                zindex: (zindex || 12000) + 5
            });

            var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
            this.list.setSize(lw, 0);
            this.list.swallowEvent('mousewheel');
            this.assetHeight = 0;
            if(this.syncFont !== false){
                this.list.setStyle('font-size', this.el.getStyle('font-size'));
            }
            if(this.title){
                this.header = this.list.createChild({cls:cls+'-hd', html: this.title});
                this.assetHeight += this.header.getHeight();
            }

            this.innerList = this.list.createChild({cls:cls+'-inner'});
            this.mon(this.innerList, 'mouseover', this.onViewOver, this);
            this.mon(this.innerList, 'mousemove', this.onViewMove, this);
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));

            if(this.pageSize){
                this.footer = this.list.createChild({cls:cls+'-ft'});
                this.pageTb = new Ext.PagingToolbar({
                    store: this.queryStore,
                    pageSize: this.pageSize,
                    renderTo:this.footer
                });
                this.assetHeight += this.footer.getHeight();
            }

            if(!this.tpl){
                this.tpl = '<tpl for="."><div class="'+cls+'-item">{' + this.displayField + '}</div></tpl>';
            }

            this.view = new Ext.DataView({
                applyTo: this.innerList,
                tpl: this.tpl,
                singleSelect: true,
                selectedClass: this.selectedClass,
                itemSelector: this.itemSelector || '.' + cls + '-item',
                emptyText: this.listEmptyText,
                deferEmptyText: false
            });

            this.mon(this.view, {
                containerclick : this.onViewClick,
                click : this.onViewClick,
                scope :this
            });

            this.bindStore(this.queryStore, true);

            if(this.resizable){
                this.resizer = new Ext.Resizable(this.list,  {
                   pinned:true, handles:'se'
                });
                this.mon(this.resizer, 'resize', function(r, w, h){
                    this.maxHeight = h-this.handleHeight-this.list.getFrameWidth('tb')-this.assetHeight;
                    this.listWidth = w;
                    this.innerList.setWidth(w - this.list.getFrameWidth('lr'));
                    this.restrictHeight();
                }, this);

                this[this.pageSize?'footer':'innerList'].setStyle('margin-bottom', this.handleHeight+'px');
            }
        }
    },

    getListParent : function() {
        return document.body;
    },
    getStore : function(){
        return this.queryStore;
    },

    // private
    bindStore : function(store, initial){
        if(this.queryStore && !initial){
            if(this.queryStore !== store && this.queryStore.autoDestroy){
                this.queryStore.destroy();
            }else{
                this.queryStore.un('beforeload', this.onBeforeLoad, this);
                this.queryStore.un('load', this.onLoad, this);
                this.queryStore.un('exception', this.collapse, this);
            }
            if(!store){
                this.queryStore = null;
                if(this.view){
                    this.view.bindStore(null);
                }
                if(this.pageTb){
                    this.pageTb.bindStore(null);
                }
            }
        }
        if(store){
            if(!initial) {
                this.lastQuery = null;
                if(this.pageTb) {
                    this.pageTb.bindStore(store);
                }
            }

            this.queryStore = Ext.StoreMgr.lookup(store);
            this.queryStore.on({
                scope: this,
                beforeload: this.onBeforeLoad,
                load: this.onLoad,
                exception: this.collapse
            });

            if(this.view){
                this.view.bindStore(store);
            }
        }
    },

    reset : function(){
        Ext.ux.form.SearchComboxField.superclass.reset.call(this);
        if(this.clearFilterOnReset && this.mode == 'local'){
            this.queryStore.clearFilter();
        }
    },

    // private
    initEvents : function(){
        Ext.ux.form.SearchComboxField.superclass.initEvents.call(this);


        this.keyNav = new Ext.KeyNav(this.el, {
        	// -- 查询下拉框List分页条事件 
        	"home" : function(e){
        		if(this.pageTb&&!this.pageTb.first.disabled)
        			this.pageTb.moveFirst();
        	},
        	"pageUp" : function(e){
        		if(this.pageTb&&!this.pageTb.prev.disabled)
        			this.pageTb.movePrevious();
        	},
        	"pageDown" : function(e){
        		if(this.pageTb&&!this.pageTb.next.disabled)
        			this.pageTb.moveNext();
        	},
        	"end" : function(e){
        		if(this.pageTb&&!this.pageTb.last.disabled)
        			this.pageTb.moveLast();
        	},
        	// --
            "up" : function(e){
                this.inKeyMode = true;
                this.selectPrev();
            },

            "down" : function(e){
                if(!this.isExpanded()){
                    this.onTriggerClick();
                }else{
                    this.inKeyMode = true;
                    this.selectNext();
                }
            },

            "enter" : function(e){
                this.onViewClick();
            },

            "esc" : function(e){
                this.collapse();
            },

            "tab" : function(e){
                if (this.forceSelection === true) {
                    this.collapse();
                } else {
                    this.onViewClick(false);
                }
                return true;
            },

            scope : this,

            doRelay : function(e, h, hname){
                if(hname == 'down' || this.scope.isExpanded()){
                    // this MUST be called before ComboBox#fireKey()
                    var relay = Ext.KeyNav.prototype.doRelay.apply(this, arguments);
                    if(!Ext.isIE && Ext.EventManager.useKeydown){
                        // call Combo#fireKey() for browsers which use keydown event (except IE)
                        this.scope.fireKey(e);
                    }
                    return relay;
                }
                return true;
            },

            forceKeyDown : true,
            defaultEventAction: 'stopEvent'
        });
        this.queryDelay = Math.max(this.queryDelay || 10,
                this.mode == 'local' ? 10 : 250);
        this.dqTask = new Ext.util.DelayedTask(this.initQuery, this);
        if(this.typeAhead){
            this.taTask = new Ext.util.DelayedTask(this.onTypeAhead, this);
        }
        if(!this.enableKeyEvents){
            this.mon(this.el, 'keyup', this.onKeyUp, this);
        }
    },


    // private
    onDestroy : function(){
        if (this.dqTask){
            this.dqTask.cancel();
            this.dqTask = null;
        }
        this.bindStore(null);
        Ext.destroy(
            this.resizer,
            this.view,
            this.pageTb,
            this.list
        );
        Ext.destroyMembers(this, 'hiddenField');
        Ext.ux.form.SearchComboxField.superclass.onDestroy.call(this);
    },

    // private
    fireKey : function(e){
        if (!this.isExpanded()) {
            Ext.ux.form.SearchComboxField.superclass.fireKey.call(this, e);
        }
    },

    // private
    onResize : function(w, h){
        Ext.ux.form.SearchComboxField.superclass.onResize.apply(this, arguments);
        if(!isNaN(w) && this.isVisible() && this.list){
            this.doResize(w);
        }else{
            this.bufferSize = w;
        }
    },

    doResize: function(w){
        if(!Ext.isDefined(this.listWidth)){
            var lw = Math.max(w, this.minListWidth);
            this.list.setWidth(lw);
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));
        }
    },

    // private
    onEnable : function(){
        Ext.ux.form.SearchComboxField.superclass.onEnable.apply(this, arguments);
        if(this.hiddenField){
            this.hiddenField.disabled = false;
        }
    },

    // private
    onDisable : function(){
        Ext.ux.form.SearchComboxField.superclass.onDisable.apply(this, arguments);
        if(this.hiddenField){
            this.hiddenField.disabled = true;
        }
    },

    // private
    onBeforeLoad : function(){
        if(!this.hasFocus){
            return;
        }
        this.innerList.update(this.loadingText ?
               '<div class="loading-indicator">'+this.loadingText+'</div>' : '');
        this.restrictHeight();
        this.selectedIndex = -1;
    },

    // private
    onLoad : function(){
        if(!this.hasFocus){
            return;
        }
        if(this.queryStore.getCount() > 0 || this.listEmptyText){
            this.expand();
            this.restrictHeight();
            if(this.lastQuery == this.allQuery){
                if(this.editable){
                    this.el.dom.select();
                }

                if(this.autoSelect !== false && !this.selectByValue(this.value, true)){
                    this.select(0, true);
                }
            }else{
                if(this.autoSelect !== false){
                    this.selectNext();
                }
                if(this.typeAhead && this.lastKey != Ext.EventObject.BACKSPACE && this.lastKey != Ext.EventObject.DELETE){
                    this.taTask.delay(this.typeAheadDelay);
                }
            }
        }else{
            this.collapse();
        }

    },

    // private
    onTypeAhead : function(){
        if(this.queryStore.getCount() > 0){
            var r = this.queryStore.getAt(0);
            var newValue = r.data[this.displayField];
            var len = newValue.length;
            var selStart = this.getRawValue().length;
            if(selStart != len){
                this.setRawValue(newValue);
                this.selectText(selStart, newValue.length);
            }
        }
    },

    // private
    assertValue  : function(){
        var val = this.getRawValue(),
            rec = this.findRecord(this.displayField, val);

        if(!rec && this.forceSelection){
            if(val.length > 0 && val != this.emptyText){
                this.el.dom.value = Ext.value(this.lastSelectionText, '');
                this.applyEmptyText();
            }else{
                this.clearValue();
            }
        }else{
            if(rec){
                // onSelect may have already set the value and by doing so
                // set the display field properly.  Let's not wipe out the
                // valueField here by just sending the displayField.
                if (val == rec.get(this.displayField) && this.value == rec.get(this.valueField)){
                    return;
                }
                val = rec.get(this.valueField || this.displayField);
            }
            this.setValue(val);
        }
    },

    // private
    onSelect : function(record, index){
        if(this.fireEvent('beforeselect', this, record, index) !== false){
            this.setValue(record.data[this.valueField || this.displayField]);
            this.collapse();
            this.fireEvent('select', this, record, index);
        }
    },

    // inherit docs
    getName: function(){
        var hf = this.hiddenField;
        return hf && hf.name ? hf.name : this.hiddenName || Ext.ux.form.SearchComboxField.superclass.getName.call(this);
    },

    getValue : function(){
        if(this.valueField){
            return Ext.isDefined(this.value) ? this.value : '';
        }else{
            return Ext.ux.form.SearchComboxField.superclass.getValue.call(this);
        }
    },

    clearValue : function(){
        if(this.hiddenField){
            this.hiddenField.value = '';
        }
        this.setRawValue('');
        this.lastSelectionText = '';
        this.applyEmptyText();
        this.value = '';
    },

    setValue : function(v){
        var text = v;
        if(this.valueField){
            var r = this.findRecord(this.valueField, v);
            if(r){
                text = r.data[this.displayField];
            }else if(Ext.isDefined(this.valueNotFoundText)){
                text = this.valueNotFoundText;
            }
        }
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = Ext.value(v, '');
        }
        Ext.ux.form.SearchComboxField.superclass.setValue.call(this, text);
        this.value = v;
        return this;
    },

    // private
    findRecord : function(prop, value){
        var record;
        if(this.autoComplet&&this.queryStore.getCount() > 0){
            this.queryStore.each(function(r){
                if(r.data[prop] == value){
                    record = r;
                    return false;
                }
            });
        }
        return record;
    },

    // private
    onViewMove : function(e, t){
        this.inKeyMode = false;
    },

    // private
    onViewOver : function(e, t){
        if(this.inKeyMode){ // prevent key nav and mouse over conflicts
            return;
        }
        var item = this.view.findItemFromChild(t);
        if(item){
            var index = this.view.indexOf(item);
            this.select(index, false);
        }
    },

    // private
    onViewClick : function(doFocus){
        var index = this.view.getSelectedIndexes()[0],
            s = this.queryStore,
            r = s.getAt(index);
        if(r){
            this.onSelect(r, index);
        }else {
            this.collapse();
        }
        if(doFocus !== false){
            this.el.focus();
        }
    },


    // private
    restrictHeight : function(){
        this.innerList.dom.style.height = '';
        var inner = this.innerList.dom,
            pad = this.list.getFrameWidth('tb') + (this.resizable ? this.handleHeight : 0) + this.assetHeight,
            h = Math.max(inner.clientHeight, inner.offsetHeight, inner.scrollHeight),
            ha = this.getPosition()[1]-Ext.getBody().getScroll().top,
            hb = Ext.lib.Dom.getViewHeight()-ha-this.getSize().height,
            space = Math.max(ha, hb, this.minHeight || 0)-this.list.shadowOffset-pad-5;

        h = Math.min(h, space, this.maxHeight);

        this.innerList.setHeight(h);
        this.list.beginUpdate();
        this.list.setHeight(h+pad);
        this.list.alignTo.apply(this.list, [this.el].concat(this.listAlign));
        this.list.endUpdate();
    },
    isExpanded : function(){
        return this.list && this.list.isVisible();
    },
    selectByValue : function(v, scrollIntoView){
        if(!Ext.isEmpty(v, true)){
            var r = this.findRecord(this.valueField || this.displayField, v);
            if(r){
                this.select(this.queryStore.indexOf(r), scrollIntoView);
                return true;
            }
        }
        return false;
    },
    select : function(index, scrollIntoView){
        this.selectedIndex = index;
        this.view.select(index);
        if(scrollIntoView !== false){
            var el = this.view.getNode(index);
            if(el){
                this.innerList.scrollChildIntoView(el, false);
            }
        }

    },

    // private
    selectNext : function(){
        var ct = this.queryStore.getCount();
        if(ct > 0){
            if(this.selectedIndex == -1){
                this.select(0);
            }else if(this.selectedIndex < ct-1){
                this.select(this.selectedIndex+1);
            }
        }
//        s = this.queryStore,
//        r = s.getAt(index);
    },

    // private
    selectPrev : function(){
        var ct = this.queryStore.getCount();
        if(ct > 0){
//            if(this.selectedIndex == -1){
//                this.select(0);
//            }else if(this.selectedIndex !== 0){
//                this.select(this.selectedIndex-1);
//            }
            if(this.selectedIndex >-1){
            	this.select(this.selectedIndex-1);	
            }
        }
    },

    // private
    onKeyUp : function(e){
        var k = e.getKey();
        if(this.editable !== false && this.readOnly !== true && (k == e.BACKSPACE || !e.isSpecialKey())){

            this.lastKey = k;
            this.dqTask.delay(this.queryDelay);
        }
        Ext.ux.form.SearchComboxField.superclass.onKeyUp.call(this, e);
    },

    // private
    validateBlur : function(){
        return !this.list || !this.list.isVisible();
    },

    // private
    initQuery : function(){
        this.doQuery(this.getRawValue());
    },

    // private
    beforeBlur : function(){
        this.assertValue();
    },

    // private
    postBlur  : function(){
        Ext.ux.form.SearchComboxField.superclass.postBlur.call(this);
        this.collapse();
        this.inKeyMode = false;
    },
    doQuery : function(q, forceAll){
    	if(!this.autoComplet)
    		return false;
        q = Ext.isEmpty(q) ? '' : q;
        var qe = {
            query: q,
            forceAll: forceAll,
            combo: this,
            cancel:false
        };
        if(this.fireEvent('beforequery', qe)===false || qe.cancel){
            return false;
        }
        q = qe.query;
        forceAll = qe.forceAll;
        if(forceAll === true || (q.length >= this.minChars)){
            if(this.lastQuery !== q){
                this.lastQuery = q;
                if(this.mode == 'local'){
                    this.selectedIndex = -1;
                    if(forceAll){
                        this.queryStore.clearFilter();
                    }else{
                        this.queryStore.filter(this.displayField, q);
                    }
                    this.onLoad();
                }else{
//                	this.queryStore.baseParams[this.queryParam] = q;
                	var v = this.paramFn(q,this);
                    this.queryStore.baseParams[this.queryParam] = Ext.encode(v);
                    this.queryStore.load({
                        params: this.getParams(q)
                    });
                    this.expand();
                }
            }else{
                this.selectedIndex = -1;
                this.onLoad();
            }
        }
    },

    // private
    getParams : function(q){
        var p = {};
        //p[this.queryParam] = q;
        if(this.pageSize){
            p.start = 0;
            p.limit = this.pageSize;
        }
        return p;
    },
    collapse : function(){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
        this.fireEvent('collapse', this);
    },

    // private
    collapseIf : function(e){
        if(!this.isDestroyed && !e.within(this.wrap) && !e.within(this.list)){
            this.collapse();
        }
    },
    expand : function(){
        if(this.isExpanded() || !this.hasFocus){
            return;
        }

        if(this.title || this.pageSize){
            this.assetHeight = 0;
            if(this.title){
                this.assetHeight += this.header.getHeight();
            }
            if(this.pageSize){
                this.assetHeight += this.footer.getHeight();
            }
        }

        if(this.bufferSize){
            this.doResize(this.bufferSize);
            delete this.bufferSize;
        }
        this.list.alignTo.apply(this.list, [this.el].concat(this.listAlign));

        // zindex can change, re-check it and set it if necessary
        var listParent = Ext.getDom(this.getListParent() || Ext.getBody()),
            zindex = parseInt(Ext.fly(listParent).getStyle('z-index') ,10);
        if (!zindex){
            zindex = this.getParentZIndex();
        }
        if (zindex) {
            this.list.setZIndex(zindex + 5);
        }
        this.list.show();
        if(Ext.isGecko2){
            this.innerList.setOverflow('auto'); // necessary for FF 2.0/Mac
        }
        this.mon(Ext.getDoc(), {
            scope: this,
            mousewheel: this.collapseIf,
            mousedown: this.collapseIf
        });
        this.fireEvent('expand', this);
    },
    onTriggerClick : function(){
        if(this.readOnly || this.disabled){
            return;
        }
        if(this.isExpanded()){
            this.collapse();
            this.el.focus();
        }else {
            this.onFocus({});
            if(this.triggerAction == 'all') {
                this.doQuery(this.allQuery, true);
            } else {
                this.doQuery(this.getRawValue());
            }
            this.el.focus();
        }
    },

    /**
     * @hide
     * @method autoSize
     */
    /**
     * @cfg {Boolean} grow @hide
     */
    /**
     * @cfg {Number} growMin @hide
     */
    /**
     * @cfg {Number} growMax @hide
     */
    
    
    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            // 触发
            var noClearObj = this.clearParamFn(this);
            var o = {start: 0};
            this.store.baseParams = this.store.baseParams || {};
//            this.store.baseParams[this.paramName] = ''; dwr用 '' 会报错A JSONObject text must begin with '{' at character 0 of 
            if(this.isJsonType)
	        	this.store.baseParams[this.paramName] = null;
	        else{
	        	var v = this.store.queryObj;
	        	for(var i in v){
	        		v[i] = null;
	        	}
	        	Ext.apply(this.store.baseParams,noClearObj,v);
	        	this.store.queryObj = '';
	        }
            this.store.reload({params:o});
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
    	if(this.checkModified&&this.store.getModifiedRecords().length>0){
    		Ext.Msg.show({
				title:'系统提示',
				msg:'修改的数据未保存',
				icon:Ext.Msg.WARNING,
				buttons:Ext.Msg.OK
			});
			return false;
		}
//		var v = this.getRawValue();
        var v = this.paramFn(this.getRawValue(),this);
//        if(v.length < 1){
        // 空则回复到查询前状态
        if(Ext.isEmpty(v)){
            this.onTrigger1Click();
            return;
        }
        var o = {start: 0};
        this.store.baseParams = this.store.baseParams || {};
        if(this.isJsonType)
        	this.store.baseParams[this.paramName] = Ext.encode(v); // 传向dwr需要encode
        else{
        	Ext.apply(this.store.baseParams,v);
        	this.store.queryObj = v;
        }
        this.store.reload({params:o});
        this.hasSearch = true;
        this.triggers[0].show();
    }

});

Ext.reg('searchcombo', Ext.ux.form.SearchComboxField);
/**
 * 查询工具条
 * 一般与Ext.ux.form.SearchComboxField结合查询
 * item项为查询项的必须属性：
 * isSearchField为true,searchName为传向后台的参数名
 * @class Ext.ux.SearchComboxToolbar
 * @extends Ext.Toolbar
 */
Ext.ux.SearchComboxToolbar = Ext.extend(Ext.Toolbar,{
	height:28,
	initComponent:function(){
		var toolbar = this;
		var searchItem;
		var searchItems = [];
		Ext.ux.SearchComboxToolbar.superclass.initComponent.call(this);
		var searchField = toolbar.searchField;
		if(searchField){
			if(this.items&&Ext.isArray(this.items.items))
				Ext.each(this.items.items,function(item){
					if(item.isSearchField&&item!=searchField){ // 属于查询Field
						item.on({
							'specialkey':{
								fn:function(field,e){
									var key = e.getKey();
								    if (key == e.ENTER) {
								        searchField.onTrigger2Click();
								    }else if (key == e.ESC){
								    	searchField.onTrigger1Click();
								    }
								}
							},
							'focus':{
								fn:function(field){
									field.selectText();
								}
							}
						})
						if(item!=searchField)
							searchItems.push(item);
					}
			})
			searchField.paramFn = function(v,field){
				var searchObj = {};
				var isSearch = false;
				Ext.each(searchItems,function(item){
					if(item.isValid()&&!Ext.isEmpty(item.getValue())){
						searchObj[item.searchName] = item.getValue();
						isSearch = true;
					}else{
						searchObj[item.searchName] = '';
						item.setValue('');
					}
				});
				if(v){
					isSearch = true;
					searchObj[field.searchName] = v;
				}else{
					searchObj[field.searchName] = '';
				}
				
				if(isSearch&&Ext.encode(searchObj)!='{}'){
					return searchObj;
				}else
					return '';
			};
			searchField.clearParamFn = function(searchField){
				var noClearObj = {};
				Ext.each(searchItems,function(item){
					if(!item.noClear){
						item.setValue('');
					}else{
						noClearObj[item.searchName] = item.getValue();
					}
				});
				return noClearObj;
			};
		}
	}
});
Ext.reg('searchcombotoolbar',Ext.ux.SearchComboxToolbar);
