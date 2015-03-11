Ext.namespace('Ext.mail.Menu');
/**
 * 添加编辑联系人菜单
 * @class Ext.mail.Menu.person
 * @extends Ext.menu.Menu
 */
Ext.mail.Menu.person = Ext.extend(Ext.menu.Menu,{
	person:'', // 当前触发的联系人,
	personId:'', // 如果已存在联系人则为联系人ID，否则为空;
	initComponent:function(){
		this.items = [{
			text:'添加为客户联系人',
			ref:'addCustContact',
			scope:this,
			handler:function(){
				//添加权限判断
				var isPopedom = getPopedomByOpType("custcontact.do","ADD",true);
				if(isPopedom == 0)//没有修改权限
				{
					Ext.Msg.alert("提示信息","您没有修改权限");
					return;
				}
				var name = encodeURI(encodeURI(this.person.name));
				openWindowBase(150,600,'custcontact.do?method=add&emailUrl='+this.person.emailUrl+'&name='+name);
			}
		},{
			text:'添加为厂家联系人',
			ref:'addFactoryContact',
			scope:this,
			handler:function(){
				var isPopedom = getPopedomByOpType("cotcontact.do", "ADD",true);
				if (isPopedom == 0)// 没有修改权限
				{
					Ext.Msg.alert("提示信息", "您没有修改权限");
					return;
				}
				var name = encodeURI(encodeURI(this.person.name));
				openWindowBase(240, 500, 'cotcontact.do?method=modify&emailUrl='+this.person.emailUrl+'&name='+name);
			}
		},{
			text:'编辑客户联系人',
			ref:'editCustContact',
			scope:this,
			handler:function(){
				//添加权限判断
				var isPopedom = getPopedomByOpType("custcontact.do","MOD",true);
				if(isPopedom == 0)//没有修改权限
				{
					Ext.Msg.alert("提示信息","您没有修改权限");
					return;
				}
				openWindowBase(150,600,'custcontact.do?method=modify&id='+this.personId);
			}
		},{
			text:'编辑厂家联系人',
			ref:'editFactoryContact',
			scope:this,
			handler:function(){
				var isPopedom = getPopedomByOpType("cotcontact.do", "MOD",true);
				if (isPopedom == 0)// 没有修改权限
				{
					Ext.Msg.alert("提示信息", "您没有修改权限");
					return;
				}
				openWindowBase(240, 500, 'cotcontact.do?method=modify&id=' + this.personId);
			}
		}];
		Ext.mail.Menu.person.superclass.initComponent.call(this);
	}
})