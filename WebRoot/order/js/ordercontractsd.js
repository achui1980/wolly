ContractSD=Ext.extend(Ext.form.FormPanel ,{
	xtype:"form",
	labelWidth:60,
	labelAlign:"left",
	layout:"form",
	width:722,
	height:368,
	padding:"10px",
	frame:true,
	initComponent: function(){
		this.items=[
			{
				xtype:"textarea",
				fieldLabel:"条款",
				id:"contractContent",
				name:"contractContent",
				anchor:"100%"
			},
			{
				xtype:"panel",
				title:"",
				layout:"column",
				items:[
					{
						xtype:"panel",
						title:"",
						layout:"form",
						columnWidth:0.15,
						items:[
							{
								xtype:"numberfield",
								id:"givenNum",
								name:"givenNum",
								fieldLabel:"要求样品",
								anchor:"100%"
							}
						]
					},
					{
						xtype:"panel",
						title:"",
						layout:"column",
						columnWidth:0.1,
						labelWidth:1,
						width:"",
						items:[
							{
								xtype:"combo",
								triggerAction:"all",
								fieldLabel:"",
								editable:false,
								store:new Ext.data.SimpleStore({
									fields:["id","value"],
									data:[["pack","包"],["dozen","箱"]]
								}),
								displayField:"value",
								valueField:"id",
								hiddenName:"givenUnit",
								mode:"local",
								id:"givenUnitCmp",
								name:"givenUnit",
								columnWidth:1
							}
						]
					},
					{
						xtype:"panel",
						layoutConfig:{
							labelSeparator:" "
						},
						layout:"form",
						labelWidth:10,
						items:[
							{
								xtype:"datefield",
								fieldLabel:"在",
								id:"givenDate",
								name:"givenDate",
								anchor:"100%"
							}
						]
					},
					{
						items:[
							{
								xtype:"label",
								text:"寄到"
							}
						]
					}
				]
			},
			{
				layout:"column",
				items:[
					{
						xtype:"label",
						text:"Insurance：",
						columnWidth:0.1
					},{
						html:'<input type="radio" id="seller" name="rdo"'+
								'value="To be affected by the sellers for 110% of invoice value against All Risks "'+
								'checked="checked" />'+
							'To be affected by the sellers for 110% of invoice value against'+
							'All Risks'+
							'<input type="radio" id="WA" name="childRdo" value="W.A."'+
								'checked="checked" />'+
							'W.A.'+
							'<input type="radio" id="risk" name="childRdo" value="all risks" />'+
							'all risks <div id="guDiv">and War Risk as per Ocean Marine Cargo Clauses of the PICC Dated 01/01/1981.</div>'+
							'<br>'+
							'<input type="radio" name="rdo" id="buyer" value="To be effected by the buyer" />To be effected by the buyer',
						columnWidth:0.9
					}
				]
			}
		]
		ContractSD.superclass.initComponent.call(this);
	}
})