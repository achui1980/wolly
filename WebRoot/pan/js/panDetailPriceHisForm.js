/**
 * 历史明细
 * @class PanDetailPriceForm
 * @extends Ext.form.FormPanel
 */
PanDetailPriceForm = Ext.extend(Ext.form.FormPanel,{
	detailIds:[],
	/**
	 * 是否是多个ID统一上传
	 * @type Boolean
	 */
	batch:false,
	/**
	 * 需要刷新的表格
	 * @type 
	 */
	grid:null,
	padding:5,
	labelAlign:'right',
	labelWidth:60,
	priceDetailId:0,
	initComponent:function(){
		this.fileUpload = true;
		var curBox = new BindCombox({
				id:'currency',
				cmpId : 'ccyId',
				dataUrl : "servlet/DataSelvert?tbname=CotCurrency",
				emptyText : 'Choose',
				fieldLabel : "<font color=red>Currency</font>",
				displayField : "curNameEn",
				valueField : "id",
				triggerAction : "all",
				anchor : "100%",
				allowBlank : false,
				blankText : "Please select currency！",
				tabIndex : 7
			});
		// 业务员
		var empBox = new BindCombox({
			dataUrl : "servlet/DataSelvert?tbname=CotEmps&key=empsName&flag=filter",
			cmpId : 'uploadEmp',
			id:'uploadEmpCtrl',
			fieldLabel : "Upload person",
			editable : false,
			disabled:true,
			valueField : "id",
			displayField : "empsName",
			emptyText : 'Choose',
			pageSize : 10,
			anchor : "100%",
			allowBlank : false,
			blankText : "Please select the sales！",
			tabIndex : 5,
			sendMethod : "post",
			selectOnFocus : true,
			minChars : 1, // 设置填充几个字节就去查一次,默认为4个字节
			listWidth : 350,// 下
			triggerAction : 'all'
		});
		this.items = [{
			xtype:'hidden',
			name:'id'
		},{
			xtype:'hidden',
			name:'panId'
		},{
			xtype:'textfield',
			name:'willSupplier',
			anchor : "100%",
			allowBlank:false,
			fieldLabel:'<font color=red>Supplier</font>'
		},{
			xtype:'numberfield',
			anchor : "100%",
			name:'price',
			hidden:this.batch?true:false,
			fieldLabel:'price'
		},curBox,empBox,{
			xtype : 'fileuploadfield',
			emptyText : 'upload file',
			anchor : "100%",
			allowBlank : true,
			blankText : "Please choose a file",
			fieldLabel : 'File',
			id : 'fileUrl',
			name : 'fileUrl',
			buttonText : '',
			buttonCfg : {
				iconCls : 'upload-icon'
			}
		},{
			xtype:'textarea',
			name:'remark',
			anchor : "100%",
			allowBlank:true,
			fieldLabel:'<font color=red>comment</font>'
		}];
		this.buttonAlign = 'center'
		this.fbar = [{
			text:'Save',
			iconCls:'page_add',
			handler:this.doSave.createDelegate(this)
		}]
		PanDetailPriceForm.superclass.initComponent.call(this);
		this.initForm();
	},
	doSave:function(){
		var me = this;
		if (this.getForm().isValid()) {
			var fileUrl = Ext.getCmp('fileUrl').getValue();
			//修改上传文件
			if(!me.batch){
				//费批量的时候
				if(me.oldFileUrl != fileUrl){
					this.getForm().submit({
						url : './servlet/UploadPanPriceHisServlet',
						method : 'post',
						waitTitle : 'waiting',
						waitMsg : 'uploading...',
						success : function(fp, o) {
							picFile = Ext.util.Format.htmlDecode(o.result.fileName);
							// 上传成功并保存
							me.saveOrupdate(me,o.result.filePath);
						},
						failure : function(fp, o) {
							Ext.MessageBox.alert("Message",o.result.msg);
						}
					});
				}else{
					this.saveOrupdate(this,fileUrl);
				}
			}else{
				//批量的时候，上传文件可以不必上传
				if(fileUrl == ''){
					this.saveOrupdate(this,fileUrl);
				}else{
					this.getForm().submit({
						url : './servlet/UploadPanPriceHisServlet',
						method : 'post',
						waitTitle : 'waiting',
						waitMsg : 'uploading...',
						success : function(fp, o) {
							var filePath = Ext.util.Format.htmlDecode(o.result.filePath);
							// 上传成功并保存
							me.saveOrupdate(me,filePath);
						},
						failure : function(fp, o) {
							Ext.MessageBox.alert("Message",o.result.msg);
						}
					});
				}
			}
		}
	},
	//保存
	saveOrupdate:function(form,filePath){
		var me = this;
		// 表单验证
		var obj = form.getForm().getValues();
		var list = [];
		if(form.batch && form.detailIds.length > 0){
			Ext.each(form.detailIds,function(detailId){
				var cotPanDetail = new CotPanDetail();
				Ext.apply(cotPanDetail,obj);
				cotPanDetail.panId = detailId;
				cotPanDetail.id = null;
				cotPanDetail.uploadEmp = logId;
				cotPanDetail.fileUrl = filePath;
				list.push(cotPanDetail)
			})
		}else{
			var empBox = Ext.getCmp("uploadEmpCtrl");
			var cotPanDetail = new CotPanDetail();
			Ext.apply(cotPanDetail,obj);
			cotPanDetail.uploadEmp = empBox.getValue();
			cotPanDetail.fileUrl = filePath;
			list.push(cotPanDetail)
		}
		cotPanService.savePanDetail(list, function(res) {
					me.ownerCt.close();
					Ext.MessageBox.alert("Message", "Successfully saved！");
					if(me.grid) 
						me.grid.getStore().reload();
				})
	},
	initForm:function(){
		var me = this;
		var empBox = Ext.getCmp("uploadEmpCtrl");
		var curBox = Ext.getCmp("currency");
		if(this.batch){
			empBox.bindPageValue("CotEmps", "id", logId);
		}else{
			//if(this.priceDetailId == null) return;
			cotPanService.getPanDetailById(this.priceDetailId,function(res){
				me.getForm().setValues(res);
				empBox.bindPageValue("CotEmps", "id", res.uploadEmp);
				curBox.bindValue(res.ccyId);
				//原始的URL，如果有变需要更改
				me.oldFileUrl = res.fileUrl;
			})
		}
	}
});
Ext.reg('pandetailpriceform',PanDetailPriceForm)