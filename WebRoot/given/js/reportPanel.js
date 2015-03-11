//下载模板页面
function downTpl(){
	downRpt("./downloadOrderTpl.action?flag=given");
}
//打开自定义模板页面
function openDefine(){
	openWindowBase(580,800,"sample/report/defineReport.jsp");
}

//成功条数
var susNum;
//失败条数
var failNum;

// 重新上传
function reportload(errorNum, rowNum) {
	var fp = Ext.getCmp('uploadFormK');
	if (fp.getForm().isValid()) {
		fp.getForm().submit({
					url : './uploadExl.action',
					method : 'get',
					waitTitle : '请等待',
					waitMsg : '上传Excel中...',
					success : function(fp, o) {
						var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
						// 上传成功并删除文件
						doActionAgain(picFile, errorNum, rowNum);
					},
					failure : function(fp, o) {
						Ext.MessageBox.alert("提示信息", o.result.msg);
					}
				});
	}
}

function doActionAgain(filename,errorNum,rowNum){
			var givenId = $("pId").value;
			var isCover = $('isCoverChx').checked;
			var showErrorDiv = 'err'+errorNum;
			cotGivenService.updateOneReport(filename,rowNum,givenId,isCover,function(msgList){
				if(msgList==null){
		 			$(showErrorDiv).innerHTML = "文件没找到或该文件不是excel！";
		 		}else{
		 			if(msgList.length==1){
		 				$(showErrorDiv).innerHTML = "<font color=green>第&nbsp;(" + (rowNum+1) + ")&nbsp行，上传成功</font>";
		 				susNum++;
		 				failNum--;
		 				//设置导出结果
		 				$('successNumLab').innerText = susNum;
		 				$('failNumLab').innerText = failNum;
		 				insertSelectExcel();
		 			}else if(msgList.length==0){
		 				$(showErrorDiv).innerHTML = "<font color=green>删除该行成功</font>";
		 				failNum--;
		 				//设置导出结果
		 				$('failNumLab').innerText = failNum;
		 			}else{
		 				$(showErrorDiv).innerHTML = "<div id=err"+errorNum+">第&nbsp(" + (msgList[0].rowNum + 1) + ")&nbsp行，第&nbsp("
						+ (msgList[0].colNum + 1) + ")&nbsp列的错误信息："
						+ msgList[0].msg
						+"<a href=# onclick=reportload("+errorNum+","+msgList[0].rowNum+")>【重新上传】</a></div>";
		 			}
		 		}
			});
		}
// 送样单记录表格导入样品单
ReportPanel = function(cfg) {
	var _self = this;
	if (!cfg)
		cfg = {};
	
	var fp = new Ext.FormPanel({
		id : 'uploadFormK',
		fileUpload : true,
		width : 500,
		//frame : true,
		padding : 5,
		region : 'center',
		margins : '0 5 0 0',
		border : false,
		cls:'rightBorder',
		baseCls : 'x-plain',
		labelWidth : 90,
		defaults : {
			allowBlank : false,
			labelAlign : "right",
			msgTarget : 'side'
		},
		items : [{
			xtype : 'fileuploadfield',
			emptyText : '',
			anchor : '93%',
			fieldLabel : '选择Excel文件',
			id : 'picFile',
			name : 'picFile',
			buttonText : '',
			buttonCfg : {
				iconCls : 'upload-icon'
			}
		}, {
			style : 'padding-left: 80',
			baseCls : 'x-plain',
			layout : 'table',
			style : {
				marginTop : '20px'
			},
			layoutConfig : {
				columns : 4
			},
			defaults : {
				frame : true,
				width : 100
			},
			items : [{
						width : 40,
						baseCls : 'x-plain'
					}, {
						layout : 'form',
						width : 100,
						labelWidth : 60,
						baseCls : 'x-plain',
						items : [{
							text : '导入',
							cls : "SYSOP_ADD",
							width : 65,
							iconCls : "page_mod",
							xtype : 'button',
							handler : function() {
								if (fp.getForm().isValid()) {
									fp.getForm().submit({
										url : './uploadExl.action',
										method : 'get',
										waitTitle : '请等待',
										waitMsg : '上传Excel中...',
										success : function(fp, o) {
											var picFile = Ext.util.Format.htmlDecode(o.result.fileName);
											// 上传成功并删除文件
											doAction(picFile);
										},
										failure:function(fp, o){
											Ext.MessageBox.alert("提示信息",o.result.msg);
										}
									});
								}
							}
						}]
					}, {
						layout : 'form',
						width : 100,
						baseCls : 'x-plain',
						items : [{
									text : '重置',
									width : 65,
									iconCls : "page_reset",
									xtype : 'button',
									handler : function() {
										fp.getForm().reset();
									}
								}]
					}]
		}, {
			style : 'padding-left: 10',
			html : '<div style="margin-top: 10px;"><font color="red">'
					+ '导入说明:</font><ul style="margin-top: 10px;">'
					+ '<li style="margin-left: 10px;margin-top: 5px;">1、<input type="checkbox" value="" id="isCoverChx" checked="checked"/><label style="margin-left: 5px;">当存在相同货号时是否覆盖。</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">2、<label>导入的数据文件必须是Excel（.xls），上传的文件不能超过100M</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">3、<label>第一列必须为货号，必须填写，一行显示一条数据，不能换行</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">4、<label>样品的规格和报价必须为数字</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">5、<label><font color=red>当部分关键参数没有填写时，将自动取样品默认配置进行填充</font></label></li>'
					+ '<li style="margin-left: 10px;margin-top: 5px;">6、<label>一次最多导入300条数据，只取Excel文件的第一个工作表数据</label></li>'
					+ '<li style="margin-left: 10px;margin-top: 10px;"> <a href=# onclick="javascript:downTpl()" style="cursor: hand;text-decoration: underline;color:blue;">【点击下载导入模板】</a>&nbsp;'
					+ '<a onclick="javascript:openDefine()" style="cursor: hand;text-decoration: underline;color:blue;">【自定义样品模板文件】</a></li>'
					+ '</ul>' + '</div>'
		}]
	});
	
	// 上传后的信息反馈
	var infoPanel = new Ext.Panel({
				id:'infoPanel',
				region : 'east',
				width : '60%',
				//frame : true,
				border : false,
				cls:'leftBorder',
				ctCls:'x-panel-mc',
				title : '上传结果'
			});
			
	function doAction(filename) {
		mask("正在加载数据中,请稍候... ...");
		var isCover = $('isCoverChx').checked;
		
		//先保存主单,如果明细表格有变更也保存
		cfg.bar.saveByExcel(filename,isCover);
	}

	// 表单
	var con = {
		layout : 'border',
		width : 600,
		height : 490,
		border:false,
		items : [fp, infoPanel]
	};

	Ext.apply(con, cfg);
	ReportPanel.superclass.constructor.call(this, con);
};
Ext.extend(ReportPanel, Ext.Panel, {});
