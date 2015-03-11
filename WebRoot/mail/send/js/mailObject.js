Ext.namespace('Ext.mail');
/**
 * 邮件人对象
 * @class Ext.mail.MailPerson
 * @extends Object
 */
Ext.mail.MailPerson = Ext.extend(Object,{
	/**
	 * 名称
	 * @type String
	 */
	name:'',
	/**
	 * 邮件地址
	 * @type email
	 */
	emailUrl:'',
	/**
	 * 将MailPerson名称与邮件地址转为字符串
	 * @return {String} name＜emailUrl＞格式
	 */
	toString:function(){
		return this.name+'<'+this.emailUrl+'>';
	},
	/**
	 * 转换成对象
	 * @param {String} mailUrl 格式为 name＜emailUrl＞格式
	 * @return {JSON} name,emailUrl属性
	 */
	toObj:function(){
		return {
			name:this.name,
			emailUrl:this.emailUrl
		}
	},
	getMailUrlRegExp:function(){
		var regExp= /(.*)<((\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6})>$/;
		return regExp;
	},
	/**
	 * 传入mailUrl转成对应属性值
	 * @param {} mailUrl 格式为name＜emailUrl＞
	 * @return {Boolean} true为转换成功
	 */
	setObj:function(mailUrl){
		var result = this.getMailUrlRegExp.test(mailUrl);
		this.name = RegExp.$1;
		this.emailUrl = RegExp.$2;
		return result;
	},
	/**
	 * 设置URL值
	 * @param {email} emailUrl 
	 * @return {Boolean} true为转换成功
	 */
	setUrl:function(emailUrl){
		var result = Ext.form.VTypes.email(emailUrl);
		if(result) 
			this.emailUrl = emailUrl;
		return result;
	},
	getUrl:function(){
		return this.emailUrl;
	},
	setName:function(name){
		this.name = name;
	},
	getName:function(){
		return this.name;
	}
});

/**
 * 值改变对象
 * @class Ext.mail.UrlRange
 * @extends Object
 */
Ext.mail.UrlRange = Ext.extend(Object,{
	/**
	 * 原来输入的值
	 * @type String
	 */
	oldValue:'',
	/**
	 * 旧值被修改到的部分,当为增加时则为''
	 * @type String
	 */
	modifyOld:'',
	/**
	 * 新加入的值,当为删除时则为''
	 * @type String
	 */
	modifyNew:'',
	/**
	 * 最后面未加分号的值
	 * @type String
	 */
	lastValue:'',
	
	lastIndex:-1,
	/**
	 * 经过修改的起始位置
	 * @type Number
	 */
	start:0,
	/**
	 * 经过修改的结束位置
	 * @type Number
	 */
	end:0,
	/**
	 * 传入新值,当新值改变时,会修改属性值
	 * @param {String} newValue
	 */
	range : function(newValue){
		if(this.oldValue==newValue)
			return;
		var oldValue = this.oldValue;
		var newLen = newValue.length;
		var oldLen = oldValue.length;
		var len = newLen > oldLen ? newLen : oldLen;
		var result;
		var newChar;
		// 检查新值与旧值的第i个位置是否相同,result返回i则为true
		for (var i = 0; i < len; i++) {						
			newChar= newValue.charAt(i);
			result = newChar!='' ? oldValue.indexOf(newChar,i) : -1
			if(result==i)
				continue; // 一样则继续找
			this.start = i;
			break;
		}
		for (var i = 0; i < len; i++) {
			newChar= newValue.charAt(newLen-1-i);
			result = newChar!='' ? oldValue.lastIndexOf(newChar,oldLen-1-i) : -1
			if(result==oldLen-1-i&&result!=-1)
				continue;
			this.end = oldLen-1-i;
			break;
		}
		if(this.start-this.end>1) // 重复字符情况
			this.end = this.start;
		// 保存被修改和加入的值
		this.modifyOld = oldValue.substring(this.start,this.end+1);
		this.modifyNew = newValue.substring(this.start,newLen-oldLen+this.end+1);
//		Ext.example.msg(this.start+"---"+this.end,this.modifyOld+"---"+this.modifyNew);
	},
	/**
	 * 将符合条件的新值保存
	 * @param {} newValue
	 */
	saveValue:function(newValue){
		this.oldValue = newValue;
		this.lastIndex = newValue.lastIndexOf(';')+1;
		this.lastValue = newValue.substring(this.lastIndex);
	}
});