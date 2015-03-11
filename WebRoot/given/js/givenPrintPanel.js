GivenPrintPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};

	// 打印类型
	var printType = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : [['XLS', 'XLS'], ['PDF', 'PDF']]
			});
	this.typeBox = new Ext.form.ComboBox({
				name : 'printType1',
				id:'printType1',
				xtype : 'combo',
				fieldLabel : "导出类型",
				editable : false,
				store : printType,
				valueField : "id",
				displayField : "name",
				mode : 'local',
				anchor : '95%',
				triggerAction : 'all',
				selectOnFocus : false
			});
	
	
	if(!cfg.tplUrl){
		cfg.tplUrl = "./servlet/DataSelvert?tbname=CotRptFile&key=rptName&typeName=rptType&type="
				+ cfg.type;
	}

	// 导出模板
	var modelBox = new BindCombox({
		dataUrl : cfg.tplUrl,
		cmpId : 'reportTemple1',
		fieldLabel : "导出模板",
		editable : true,
		sendMethod : "post",
		valueField : "rptfilePath",
		displayField : "rptName",
		emptyText : '请选择',
		anchor : "95%",
		triggerAction : 'all'
	});
	
	//查询参数
	this.querySql="";

	
	//默认的导出action
	if(!cfg.exlAction){
		cfg.exlAction="./downRpt.action";
	}

	// 导出
	var exportRpt = function() {
		var formData = getFormValues(_self, true);
		if (formData.reportTemple1 == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}
		var headerFlag = true;
		if (formData.showHeader1 == 1) {
			headerFlag = false
		}
		var exlSheet;
		if (checkboxGroup.items.itemAt(0).checked)   
        {   
           exlSheet = true;            
        }else{
        	exlSheet = false;
        }
		var rptUrl = cfg.exlAction+"?reportTemple=" + formData.reportTemple1
				+ "&printType=" + formData.printType1 + "&headerFlag="
				+ headerFlag + "&exlSheet=" + exlSheet +_self.querySql;
		_self.hide();
		window.open(rptUrl);
	}
	//默认的预览action
	if(!cfg.viewAction){
		cfg.viewAction="./previewrpt.do";
	}

	// 预览方法
	var viewRpt = function() {
		var formData = getFormValues(_self, true);
		if (formData.reportTemple1 == '') {
			Ext.MessageBox.alert("提示信息", '请选择导出模板!');
			return;
		}
		var headerFlag = true;
		if (formData.showHeader1 == 1) {
			headerFlag = false
		}
		var _height = window.screen.height;
		var _width = window.screen.width;
		var url = cfg.viewAction+"?method=queryEleRpt&reportTemple="
				+ formData.reportTemple1 + "&headerFlag=" + headerFlag
				+_self.querySql;
		openMaxWindow(_height, _width,url);
		//预览后关闭页面,以防查询条件改变后querySql没改变
		_self.hide();
	}

	//用组来定义，否则ie一开不会显示
	var rda1 = new Ext.form.Radio({
		name:"showHeader1",
		inputValue:0,
		id:"singlePage1",
		boxLabel:"不分页显示"
	})
		
	var rda2 = new Ext.form.Radio({
		name:"showHeader1",
		inputValue:1,
		id:"allPage1",
		checked : true,
		boxLabel:"分页显示"
	})
		
	var grp = new Ext.form.RadioGroup({
		name:"page",
		items:[rda1,rda2]
	})
	
	var checkboxGroup = new Ext.form.CheckboxGroup({    
       id:'myGroup1',    
       xtype: 'checkboxgroup',              
       items: [     
            {
            	boxLabel: '分Sheet导出', 
            	name: 'exlSheet'
            }  
       ]    
    }); 
            
	// 表单
	var con = {
		title:"打印及导出",
		buttonAlign : 'right', // 按钮居右显示
		labelAlign : "right",
		layout : 'fit',
		width : 280,
		autoHeight:true,		
		fromId:"printGivenForm",	
		frame : true,
		tools : [{
			id:'close',
			handler : function(event, toolEl, p) {
				p.hide();
			}
		}],
		items : [{
					xtype : 'fieldset',
					title : '打印设置',					
					layout : 'column',
					autoHeight : true,
					labelWidth : 1, // 标签宽度
					items : [{
								columnWidth : 1,
								layout : 'form',
								items : [grp]
							}, {
								columnWidth : 1,
								layout : 'form',
								border : false,
								items : [checkboxGroup]
							}, {
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelWidth : 80,
								items : [modelBox]
							}, {
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelWidth : 80,
								items : [this.typeBox]
							}]

				}],
		buttonAlign : 'center',
		buttons : [{
					id:"export1",
					text : '导出',
					scope : this,
					width:65,
					iconCls:"page_excel",
					handler:exportRpt
				}, {
					id:"preview1",
					text : '打印预览',
					scope : this,
					width:80,
					iconCls:"page_print",
					handler:viewRpt
				}]

	};
	//第一次弹出时.如果没printDiv,则新建
	this.printName = 'printGivenDiv';
	if($(this.printName)==null){
		//window默认在z-index为9000
		Ext.DomHelper.append(document.body, {
			html : '<div id="'+this.printName+'" style="position:absolute;z-index:9500;"></div>'
		}, true);
	}

	Ext.apply(con, cfg);
	GivenPrintPanel.superclass.constructor.call(this, con);
};
Ext.extend(GivenPrintPanel, Ext.FormPanel, {});