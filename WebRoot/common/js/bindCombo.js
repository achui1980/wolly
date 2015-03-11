/*******************************************************************************
 * 时间：2009/10/19 作者: achui 描述: 下拉框绑定通用类
 ******************************************************************************/
BindCombox = Ext.extend(Ext.form.ComboBox, {
			// 绑定数据的URL
			dataUrl : "",
			// 下拉框的ID，对应于Html中的id属性
			cmpId : "",
			needReset:true,
			// mode : "local",
			sendMethod : "GET",
			autoLoad : false,
			editable : false,
			typeAheadDelay : 100,// 默认延时查询250
			typeAhead : true, // 自动填充
			//是否需要加载请选择项
			//needDefault:true,
			// 初始化组件
			initComponent : function() {
				//var _self = this;
				Ext.form.ComboBox.superclass.initComponent.call(this);
				this.addEvents(
						/**
						 * @event afterbindvalue
						 *        给下拉框赋值后触发
						 * @param {BindCombox}
						 * @param value 绑定的值
						 */
						'afterbindvalue');
				
				// this.id="bind_"+this.cmpId;
				this.hiddenName = this.cmpId;
				// this.mode = "local";
				this.forceSelection = true;
				this.lazyRender = false;
				this.lazyInit = false;
				this.triggerAction = "all";
				this.minChars = 1;
				this.isInitDefault = true;
				// this.editable = false;
				// this.selectOnFocus = true;
				this.store = new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
										method : this.sendMethod,
										url : this.dataUrl
									}),
							reader : new Ext.data.JsonReader({
										root : "data",
										totalProperty : "totalCount",
										idProperty : "id"
									}, [{
												name : this.valueField
											}, {
												name : this.displayField
											}]),
							autoLoad : this.autoLoad,
							remoteSort : false
						});

				// 获得焦点时弹出下拉列表---1.IE点击箭头加载时会报错,2.鼠标第一次点击下列列表消失
//				this.on("focus", function(sel) {
//							if(!sel.isExpanded()){
//								sel.onTriggerClick();
//							}
//						});
				this.initDefaultFn = function(){
					if(!this.isInitDefault) return;
					var record1 = new Ext.data.Record();
					record1.data[this.valueField] = "";
					record1.data[this.displayField] = this.emptyText||"Pls.Select";
					if(this.store.findExact(this.displayField,this.emptyText||"Pls.Select") != -1)
						return;
					this.store.insert(0,record1);
				}
			},
			//重写onTypeAhead方法，使之能够适应要求
			onTypeAhead : function(){
		    if(this.store.getCount() > 0){
		            var r = this.store.getAt(0);
					//mod by achui 2010-06-21 如果遇到valueField的值是空，则往下一条记录（过滤请选择）
					var rdx = 0
					if(r.data[this.valueField] == "" || r.data[this.valueField] == this.emptyText||"Pls.Select"){
						r = this.store.getAt(1);
						rdx = 1;
					}
		            var newValue = r.data[this.displayField];
		            var len = newValue.length;
		            var selStart = this.getRawValue().length;
		            this.setRawValue(newValue);
	                this.selectText(selStart, newValue.length);
	                //对onselect事件延迟加载
	                var task = new Ext.util.DelayedTask(this.fireEvent, this,['select', this, r, rdx]);
	                task.delay(this.typeAheadDelay+20);
		            if(selStart != len){
//		                this.setRawValue(newValue);
//		                this.selectText(selStart, newValue.length);
//		                //对onselect事件延迟加载
//		                var task = new Ext.util.DelayedTask(this.fireEvent, this,['select', this, r, rdx]);
//		                task.delay(this.typeAheadDelay+20);
		            }
		        }
		    },
		    //重写beforeBlur,能够输入下拉框不存在的值，是之不重置
		     beforeBlur : function(){
		     	if(this.needReset)
		        	this.assertValue();
		        else{
		        	var val = this.getRawValue();
		        	this.setValue(val);
		        }
		    },
			// 下拉框绑定操作
			bindValue : function(value) {
				var _self = this;
				if (value != null) {
					this.store.on("load", function() {
								_self.setValue(value);
							});
					if (this.store.getCount() == 0) {
						this.store.load();
					}else{
						_self.setValue(value);
					}
				}
			},
			// 分页下拉框绑定
			bindPageValue : function(tbname, queryCol, value) {
				var _self = this;
				if (value != null) {
					if (this.store.getCount() == 0) {
						this.store.load();
					}
					this.store.load({
								params : {
									id : value,
									queryCol : queryCol,
									tbname : tbname
								},
								callback : function(r, option, success) {
									//_self.reset();
									_self.setValue(value);
									_self.fireEvent('afterbindvalue', _self,value);
									//_self.initDefault();
								}
							});
				}else{
					this.store.load();
				}
			},
			setLoadParams:function(tbname, queryCol, value,key)
			{
				this.store.baseParams = {
					id : value,
					queryCol : queryCol,
					tbname : tbname,
					key:key
				}
			},
			reflashData:function(){
				this.store.reload();
			},
			resetData:function(){
				this.reset();
				if(this.store.getCount()>0)
					this.store.reload();
			},
			setDataUrl:function(url){
				this.dataUrl=url;
				this.store.proxy.setUrl(this.dataUrl);
				
				this.store.on("beforeload", function(store, options) {
								store.proxy.setUrl(url);
							});
			},
			// 联动操作
			loadValueById : function(tbname, key, value,comVal) {
				var _self = this;
				if (value != null) {
//					if (this.store.getCount() == 0) {
//						this.store.load();
//					}
					this.store.on("load", function() {
								if(typeof(comVal)!='undefined'){
									_self.setValue(comVal);
								}
							});
//					this.setLoadParams(tbname, key, value)
					this.store.load({
								params : {
									id : value,
									key : key,
									tbname : tbname
								},
								callback : function(r, option, success) {
									//_self.setLoadParams(null, null, null);
									//printObject(_self.store.baseParams)
								}
							});
					//_self.prototype.hasFocus = true;
					//_self.expland();
				}
			}
		});
Ext.reg('bindCombo', BindCombox);
