/**
 * 生成勾选项控件
 * @class QH.controls.CheckBoxModule
 * @extends Ext.form.CheckboxGroup
 * @description：用法
			xtype:"checkboxmodule",
			id:"checkGroup",
			tbName:"CotModule",
			displayField:"moduleName",
			valueField:"id",
			//checkedField:"id",
			region:"east",
			columns:1,
			dataSource:baseSerivce.getList("CotModule"),
			width:"500"
 */
QH.controls.CheckBoxModule = Ext.extend(Ext.form.CheckboxGroup,{
	
	/**
	 * 需要查询的表
	 * @type String
	 */
	tbName:"",
	/**
	 * 需要显示的字段
	 * @type String
	 */
	displayField:"",
	/**
	 * 需要传递到后台的值
	 * @type String
	 */
	valueField:"",
	/**
	 * 需要设置的域，从设置列表中提取
	 * @type String
	 * 如果该属性不填，或者放空则默认认为，list数组的内容只是一个简单的字符串或数字集合，不是对象的集合如：[1,2,3,4,5,6]
	 */
	checkedField:"",
	/**
	 * 显示单选按钮框的数据源,如果不填，默认去tbName属性对应的数据源
	 * 1、函数：返回一个Array
	 * 2、URL：返回一个Json(暂未实现)
	 */
	dataSource:null,
	/**
	 * 是否dwr函数，如果是的话，则控件会自动加入一个回调函数
	 * @type Boolean
	 */
	isDWRFun:true,
	/**
	 * 传递到后台的参数
	 * @type 
	 */
	requestParam:null,
	/**
	 * 用于缓存已经记载过的数据
	 * @private
	 * @type {Map}
	 */
	cacheInitData:{},
	/**
	 * cacheKey：用于缓存数据的键
	 * @type{String}
	 */
	initComponent:function(){
		DWREngine.setAsync(false);
		if(!this.cacheInitData[this.cacheKey]){
			var checkGroup = this;
			var list = null;
			if(Ext.isFunction(this.dataSource)){
				var params = [];
				for(var param in this.requestParam){
					params.push(this.requestParam[param]);
				}
				if(this.isDWRFun){
					params.push(function(res){
						list = res;
					})
				}
				this.dataSource.apply(checkGroup.dataSource,params);
			}else if(Ext.isEmpty(this.dataSource)){
				baseSerivce.getList(this.tbName,function(res){
					list = res;
				});
			}else{
				list = this.dataSource;
			}
			this.items = [];
			Ext.each(list,function(item){
				var display = item[checkGroup.displayField];
				var value = item[checkGroup.valueField];
				var checkBox = {
					boxLabel: display, name: value
				}
				checkGroup.items.push(checkBox);
			})
			if(Ext.isEmpty(this.items)){
				this.items.push({boxLabel:"none",name:"none",hidden:true})
			}
			//缓存已经加载过的数据
			if(this.cacheKey){
				this.cacheInitData[this.cacheKey] = this.items;
			}
		}else{
			//从缓存去数据
			this.items = this.cacheInitData[this.cacheKey];
		}
		
		
		QH.controls.CheckBoxModule.superclass.initComponent.call(this);
		DWREngine.setAsync(true);
	},
	/**
	 * 设置单选项
	 * @param {Array} list：需要设置的列表
	 */
	setCheckedValue:function(list){
		if(list == null) return;
		var checkGroup = this;
		if(!Ext.isEmpty(this.checkedField)){
			Ext.each(list,function(item){
				checkGroup.setValue(item[checkGroup.checkedField],true);
			})
		}else{
			Ext.each(list,function(item){
				checkGroup.setValue(item,true);
			})
		}
	},
	/**
	 * 获取已选择的值
	 * @return {Array}
	 */
	getCheckedValue:function(){
		var checkedValue = [];
		var checkGroup = this;
		Ext.each(this.getValue(),function(item){
			checkedValue.push(item["name"]);
		})
		return checkedValue;
	},
	/**
	 * 全选/反选所有项
	 * @param {boolean}[可选] forceCheck:所有选项都区forceCheck的值
	 */
	checkedAll:function(forceCheck){
		var checkGroup = this;
		Ext.each(this.items.items,function(item){
			var checked = !item.checked;
			if(Ext.isEmpty(forceCheck))//不带改参数
				checkGroup.setValue(item["name"],checked);
			else
				checkGroup.setValue(item["name"],forceCheck);
		})
	},
	/**
	 * 重新绑定数据
	 * @param {Panel} parentCt：加载改控件的父容器
	 * @param {Object} cfg：需要修改的配置
	 */
	load:function(cfg){
		//获取加载该面板的容器
		var panel = this.getBubbleTarget();
		var defaultCfg = this.initialConfig;
		//缓存已经取得的数据
		Ext.apply(cfg,{cacheInitData:this.cacheInitData});
		Ext.apply(defaultCfg,cfg);
		panel.remove(this);
		panel.add(defaultCfg);
		panel.doLayout();
	}
});
Ext.reg("checkboxmodule",QH.controls.CheckBoxModule);