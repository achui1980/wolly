				
Ext.mail.AddressTab = Ext.extend(Ext.TabPanel,{
	activeTab:0,
	ref:'addressTab',
	width:150,
	personField:'',
	initComponent:function(){
		this.items = [
			new Ext.mail.CustomerAddressGrid(),
			new Ext.mail.FactoryAddressGrid()
		]
		Ext.mail.AddressTab.superclass.initComponent.call(this);
	}
});