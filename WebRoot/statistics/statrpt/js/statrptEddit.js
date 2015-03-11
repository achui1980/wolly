Ext.onReady(function() {

	// 报表类型
	var rpttypeComb = new BindCombox({
				dataUrl : "./servlet/DataSelvert?tbname=CotStatistics&key=statName&typeName=statFile&type=0",
				cmpId : "statName",
				valueField : "id",
				editable : true,
				sendMethod : "post",
				displayField : "statName",
				allowBlank : false,
				pageSize : 10,
				blankText : "Please choose the type of report",
				emptyText : "Please select",
				fieldLabel : "Report Type",
				anchor : "95%"
			});
	var myCheckboxGroup = new Ext.form.CheckboxGroup({
	    id:'myGroup',
	    xtype: 'checkboxgroup',
	    fieldLabel: 'Conditions',
	    itemCls: 'x-check-group-alt',
	    columns: 3,
	    items: [
	        {boxLabel: 'Start Time(STARTTIME)', name:"startTime"},
	        {boxLabel: 'End time(ENDTIME)', name: 'endTime'},
	        {boxLabel: 'Sale(EMPID)', name: 'empId'},
	        {boxLabel: 'Supplier(FACTORYID)', name: 'factoryId'},
	        {boxLabel: 'Department(TYPELV1ID)', name: 'typeLv1Id'},
	        {boxLabel: 'Customer(CUSTID)', name: 'custId'},
	        {boxLabel: 'Company(COMPANYID)', name: 'companyId'},
	     //   {boxLabel: '产品分类(ELETYPEIDLV2)', name: 'eleTypeidLv2'},
	      //  {boxLabel: '材质(ELETYPEIDLV1)', name: 'eleTypeidLv1'},
	    //    {boxLabel: '开发对象(ELEFORPERSON)', name: 'eleForPerson'},
	     //   {boxLabel: '国别(NATIONID)', name: 'nationId'},
	        {boxLabel: 'Currency(CURRENCYID)', name: 'currencyId'},
	        {boxLabel: 'Running/Closed(CANOUT)', name: 'canOut'},
	        {boxLabel: 'Waiting/Approved(ORDER)', name: 'orderStatus'},
	        {boxLabel: 'Group(ELETYPEIDLV2)', name: 'eleTypeidLv2'},
	        {boxLabel: 'Dept of Sale(DEPT)', name: 'dept'},
	        {boxLabel: 'Sum of Year(SUMYEAR)', name: 'sumYear'},
	        {boxLabel: 'Day of Month(CHECKDAY)', name: 'dayOfMonth'}
	      //  {boxLabel: '单号(ORDERNO)', name: 'orderNo'}
	    ]
	});

	//上传后的文件名	
	var picFile="";
	var from = new Ext.form.FormPanel({
				labelWidth : 100,
				labelAlign : "right",
				layout : "form",
				title:'Statistical Reports',
				fileUpload : true,
				padding : "10px",
//				width:670,
//				height:300,
				frame : true,
				buttonAlign : 'center',
				buttons : [{
					width : 60,
					text : "Save",
					iconCls : "page_table_save",
					handler : function() {
						if (from.getForm().isValid()) {
							if($('oldName').value==$('statFile').value){
								saveOrupdate();
							}else{
								from.getForm().submit({
											url : './uploadRptFile.action?flag=yes',
											method : 'get',
											waitTitle : 'wait',
											waitMsg : 'Uploading...',
											success : function(fp, o) {
												picFile = Ext.util.Format.htmlDecode(o.result.fileName);
												// 上传成功并保存
												saveOrupdate(picFile);
											},
											failure : function(fp, o) {
												Ext.MessageBox.alert("Message",o.result.msg);
											}
										});
							}
						}
					}
				}, {
					text : "Cancel",
					pressed : false,
					handler : function() {
						closeandreflashEC(true, "statisGrid", false);
					},
					iconCls : "page_cancel"
				}],
				items : [rpttypeComb, {
							xtype : "textfield",
							fieldLabel : "Report Name",
							allowBlank : false,
							blankText : "Please enter the name of the report",
							name : "statName",
							id : "statName",
							anchor : "95%"
						}, {
							xtype : 'fileuploadfield',
							emptyText : 'Upload file jrxml or jasper report',
							anchor : '95%',
							allowBlank : false,
							blankText : "Please select the report file",
							fieldLabel : 'Upload',
							id : 'statFile',
							name : 'statFile',
							buttonText : '',
							buttonCfg : {
								iconCls : 'upload-icon'
							}
						},myCheckboxGroup,{
							xtype:'label'
//							style:{color:'red'},
//							text:'系统提示：在设计报表的时候参数名称必须是大写！'
							//html:'<font color="red">系统提示：1.参数的内容为可选项。在报表的Where条件中根据如下格式填写（$P{EMPID}="" OR cot_emps.ID = $P{EMPID}）</br>2.在设计报表的时候参数名称必须是大写！</font>'
							//html:'<div style="color:red;"><div>系统提示：1.参数的内容为可选项。在报表的Where条件中根据如下格式填写（$P{EMPID}="" OR cot_emps.ID = <span style="padding-left:70px">$P{EMPID}）</span></div><div style="padding-left:60px">2.在设计报表的时候参数名称必须是大写！</div></div>'
						}]
			});
	var viewport = new Ext.Viewport({
					layout : 'fit',
					items : [from]
				});
	// 初始化表单
	function initForm() {
		DWREngine.setAsync(false);
		var id = $("statId").value;
		if (id == "" || id == "null" || id == null)
			return;
		
		cotStatPopedomService.getCotStatisticsById(parseInt(id), function(res) {
					
					rpttypeComb.bindPageValue("CotStatistics", "id", res.statParent);
					Ext.getCmp('statName').setValue(res.statName);
					var fileName =res.statFile;
					var io=fileName.lastIndexOf("/");
					$('statFile').value = fileName.substring(io+1);
					$('oldName').value = fileName.substring(io+1);
					var str = res.statOrderBy;
					var arry =str.split(',');
					for(var i=0;i<arry.length;i++){
						var atr=arry[i];
						var list=myCheckboxGroup.items;
						for(var y=0;y<list.length;y++){
							if(list.itemAt(y).name==atr){
								list.itemAt(y).setValue(true);
							}
						}
					}
				});
		DWREngine.setAsync(true);
	}
	initForm();

	//保存
	function saveOrupdate() {
		// 表单验证
		var cotStatistics = new CotStatistics();
		var statParent=rpttypeComb.getValue();
		cotStatistics.statParent = statParent;
		var stName=Ext.getCmp('statName').getValue();
		cotStatistics.statName = stName;
		cotStatistics.statUrl = 'statistics/querybyarea.jsp';
		cotStatistics.statType='SELL';
		
		if($('oldName').value!=$('statFile').value){
			cotStatistics.statFile = "reportfile/statistics/"+picFile;
		}else{
			cotStatistics.statFile ="reportfile/statistics/"+$('oldName').value;
		}
		
		var str='';
		var arr =Ext.getCmp('myGroup').items;
		for(var i=0;i<arr.length;i++){
			if(arr.itemAt(i).checked){
				str=str+arr.itemAt(i).name+',';
			}
		}
		var index=str.lastIndexOf(',');
		if(index > 0){
			 str=str.substring(0,index);
			 cotStatistics.statOrderBy=str;
		}
		var id = $("statId").value;
		var nodeId=0;
		if(id !='null' && id !=''){
			cotStatistics.id=id;
			nodeId=id;
		}
		DWREngine.setAsync(false);
		
		cotStatPopedomService.isExitStatName(stName,statParent,nodeId,function(res){
			if(res!=null){
				Ext.Msg.alert('Message','Report name already exists, please re-enter');
			}else{
				cotStatPopedomService.saveOrUpdateRptFile(cotStatistics,function(rs){
					if(rs){
						Ext.Msg.alert('Message','Successfully saved');
						closeandreflashEC(true, "statisGrid", false);
					}else{
						Ext.Msg.alert('Message','Save failed');
					}
				})
			}
			
		});
		
		DWREngine.setAsync(true);
	}
});
