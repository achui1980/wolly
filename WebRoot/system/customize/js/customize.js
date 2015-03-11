Ext.onReady(function(){

	  	var root =new Ext.tree.AsyncTreeNode({
	  		text:'Module Column',
	  		expanded :true,
	  		id : 'root'
	  	});

	    var treePanel = new Ext.tree.TreePanel({
			region:'west',
			collapsible :true,
			margins:'5 5 0 5',
			title:'Stencil',
			width : 200,
			border : false,
	   		root : root,
	   		rootVisible :true,
	   		loader : new Ext.tree.TreeLoader({
	   			dataUrl :'cotcustomize.do?method=queryTree'
	   		}),
	   		listeners:{
	   			'click':function(node ,e){
	   				if(node.text=='Module Column'){
	   					Ext.getCmp('saveBt').disable(); 
	   					mainPanel.layout.setActiveItem(Ext.getCmp('modelNo'));
	   				}else{
	   					Ext.getCmp('saveBt').enable(); 
		   				var obj=Ext.getCmp(node.id);
		   				obj.setTitle(node.text);
		   				mainPanel.layout.setActiveItem(obj);
		   				$('type').value=node.id;
		   				cotCustomizeService.getCotCustomizeFields($('empsId').value,node.id,function(rs){
								if(rs){
									for(var i=0;i<rs.length;i++){
										var str=rs[i];
										var obj=mainPanel.layout.activeItem
										var arr =obj.items.get(0).items;
										for(var y=0;y<arr.length;y++){
											if(arr.itemAt(y).name==str){
												arr.itemAt(y).setValue(false);
											}
										}
									}
								}
							});
	   				}
	   			}
	   		}
		});
	   
		var mainPanel= new Ext.Panel({
			layout : 'card',
			region: 'center',
			activeItem : 0,
			title:'',
			margins:'5 5 0 0',
			buttonAlign : 'center',
			items :[{
					title : '',
					id : 'modelNo',
					border:false
			}],
			buttons:[{
				text:'Save',
				disabled:true,
				id : 'saveBt',
				handler:function(){
					var empsId=$('empsId').value
					var ids=[];
					var type=$('type').value;
					var obj=mainPanel.layout.activeItem;
					
					var arr =obj.items.get(0).items;
					for(var i=0;i<arr.length;i++){
						if(!arr.itemAt(i).checked){
							ids.push(arr.itemAt(i).name);
						}
					}
					//alert(ids.length);
					cotCustomizeService.saveCotCustomize(ids,type,empsId);
					Ext.Msg.alert('Message','Successfully saved');
					
				}
			}]
		});	

	 
		var viewport = new Ext.Viewport({
				layout : 'border',
				items : [treePanel,mainPanel]
			});
	  
	   function initForm(){
	   		var list =root.childNodes;
	   		cotCustomizeService.initPanel(function(rs){
	   			if(rs){
	   			//	var ids=['CotPriceDetail','CotOrderDetail','CotOrderFacdetail','CotFittingsOrderdetail','CotPackingOrderdetail'];
	   				for(var i=0;i<list.length;i++){
	   					id=list[i].id;
		   				var arry=rs[id];
		   				var items=[];
		   				for(var x=0;x<arry.length;x++){
		   					var CustomizeVO=arry[x];
	   						var chk={boxLabel: CustomizeVO.name, name: CustomizeVO.value,checked: true}
	   						items.push(chk);
		   				}
		   				
	   					var checkGroup = new Ext.form.CheckboxGroup({
						    id:id+'no',
						    xtype: 'checkboxgroup',
						    columns: 7
		   				});
		   				checkGroup.items =items;
		   				
		   				var panel=new Ext.Panel({
		   					id : id,
		   					border:false,
		   					title: " ",
		   					padding:'5',
		   					items :[checkGroup]
		   				});
		   				mainPanel.add(panel);
	   					mainPanel.doLayout(); 
	   				}
	   			}  
	   		});
	   }
	   initForm.defer(1000);
	});