//类工具
var ClassUtils=Class.create();
ClassUtils.prototype={
	_ClassUtilsName:'ClassUtils',
	initialize:function(){
	},
	/**
	 * 给类的每个方法注册一个对类对象的自我引用
	 * @param reference 对类对象的引用
	 */
	registerFuncSelfLink:function(reference){
		for (var n in reference) {
        	var item = reference[n];                        
        	if (item instanceof Function) 
				item.$ = reference;
    	}
	}
}
//Ajax操作封装类：
//由于调用AjaxRequest类进行XMLHTTPRequest操作时，this引用（指向当前的对象）会出现了call stack问题，从而指向当前的对象。
//所以，对putRequest、callBackHandler、以及callback方法都要使用arguments.callee.$来获得正确的类对象引用
var AjaxWrapper=Class.create();
AjaxWrapper.prototype={
	debug_flag:false,
	xml_source:'',
	/**
	 * 初始化
	 * @param isDebug 是否显示调试信息
	 */
	initialize:function(isDebug){
		new ClassUtils().registerFuncSelfLink(this);
		this.debug_flag=isDebug;
		
	},
	/**
	 * 以get的方式向server发送request
	 * @param url
	 * @param params
	 * @param callBackFunction 发送成功后回调的函数或者函数名
	 */
	putRequest:function(url,params,callBackFunction,customizeRequestHeader){
		var funcHolder=arguments.callee.$;
		
	    var xmlHttp = new Ajax.Request(url,
			{
				method: 'get', 
	    		parameters: params, 
				requestHeaders:['my-header-encoding','utf-8'],
	    		onFailure: function(){
					alert('Sorry, network communication failure, please refresh!');
				},
				onSuccess: function(transport){
				},
				onComplete: function(transport){
					funcHolder.callBackHandler.apply(funcHolder,[transport,callBackFunction]);
				}
			});
	},
	/**
	 * 以post的方式向server发送xml请求
	 * @param url
	 * @param postDataBody
	 * @param callBackFunction 发送成功后回调的函数或者函数名
	 */
	pushRequest:function(url,postDataBody,callBackFunction){
		var funcHolder=arguments.callee.$;
		var options={
				method: 'post', 
		    	parameters:'',
		    	requestHeaders:['my-header-encoding','utf-8'],
		    	postBody: postDataBody,
				onFailure: function(transport){
					alert('Sorry, network communication failure, please re-send!');
				},
				onComplete: function(transport){
					funcHolder.callBackHandler.apply(funcHolder,[transport,callBackFunction]);
				}
			};
		var xmlHttp = new Ajax.Request(url,options);
	},
	/**
	 * 远程调用的回调处理
	 * @param transport xmlhttp的transport
	 * @param callBackFunction 回调时call的方法，可以是函数也可以是函数名
	 */
	callBackHandler:function(transport,callBackFunction){
		var funcHolder=arguments.callee.$;
		if(transport.status!=200){
			alert("获得回应失败,请求状态:"+transport.status);
		}
		else{
			funcHolder.xml_source=transport.responseText;
			if (funcHolder.debug_flag)
				alert('call callback function');
			if (typeof(callBackFunction)=='function'){
				if (funcHolder.debug_flag){
					alert('invoke callbackFunc');
				}
				callBackFunction(transport.responseText);
			}
			else{
				if (funcHolder.debug_flag){
					alert('evalFunc callbackFunc');
				}
				new execute().evalFunc(callBackFunction,transport.responseText);
			}
			if (funcHolder.debug_flag)
				alert('end callback function');
		}
	},
	//显示xml信息
	showXMLResponse:function(){
		var funcHolder=arguments.callee.$;
		alert(funcHolder.xml_source);
	},
	/**
	 * 获得事件发起者的id
	 */
	getEventInvokerId:function(evt){
		var elementId='';
		if (new OSDetector().detect()=='IE'){
			elementId=evt.srcElement.id;
		}
		else{
			elementId=evt.currentTarget.id;
		}
		return elementId;
	}
}

var XMLDomForAjax=Class.create();
XMLDomForAjax.prototype={
		isDebug:false,
	//dom节点类型常量
	ELEMENT_NODE:1,
	ATTRIBUTE_NODE:2,
    TEXT_NODE:3,
    CDATA_SECTION_NODE:4,
    ENTITY_REFERENCE_NODE:5,
    ENTITY_NODE:6,
    PROCESSING_INSTRUCTION_NODE:7,
    COMMENT_NODE:8,
    DOCUMENT_NODE:9,
    DOCUMENT_TYPE_NODE:10,
    DOCUMENT_FRAGMENT_NODE:11,
    NOTATION_NODE:12,
    
	initialize:function(isDebug){
		new ClassUtils().registerFuncSelfLink(this);
		this.isDebug=isDebug;
	},
	/**
	 * 建立跨平台的dom解析器
	 * @param xml xml字符串
	 * @return dom解析器
	 */
	createDomParser:function(xml){
		var funcHolder=arguments.callee.$;
		// code for IE
		if (window.ActiveXObject){
		  var doc=new ActiveXObject("Microsoft.XMLDOM");
		  doc.async="false";
		  doc.loadXML(xml);
		}
		// code for Mozilla, Firefox, Opera, etc.
		else{
			var parser=new DOMParser();
			var doc=null;
			try{
				doc=parser.parseFromString(xml,"text/xml");
			}
			catch(e){
				alert('Analysis xml:'+xml+'Error:'+e);
				return null;
			}
			//错误处理，详见http://developer.mozilla.org/en/docs/DOMParser
			if (doc.documentElement.nodeName == "parsererror"){
				var errorMsg='Analysis xml Error:'+doc.documentElement.firstChild.nodeValue+'\n Detailed source code:';
				for (var i=0; i<doc.documentElement.childNodes.length; i++) {
		    		//获得节点
		    		var node=doc.documentElement.childNodes[i];
		    		//取出其中的field元素进行处理
		        	if ((node.nodeType==funcHolder.ELEMENT_NODE) && (node.tagName == 'sourcetext')) {
		        		var nodeText=funcHolder.getNodeText(node);
			            errorMsg+=nodeText;
		        	}
				}
				alert(errorMsg);
			}
		}
		return doc;
	},
	/**
	 * 解析单节点：field
	 * @param node 节点
	 * @param obj 返回的bean对象
	 * @return obj
	 */
	parseSingleDOMNodeForField:function(node,obj){
		var funcHolder=arguments.callee.$;
		if (obj==null){
	    	obj=new Object();
	    }
	    try{
		    var nodeText=funcHolder.getNodeText(node);
		    if (funcHolder.isDebug){
			    alert(node.getAttribute('name')+' type:'+node.getAttribute('type')+' text:'+nodeText);
		    }

		    //如果为列表，建立数组
		    if (node.getAttribute('type')=='java.util.List'){
		      	if (!obj[node.getAttribute('name')]){
					obj[node.getAttribute('name')]=new Array();
		      	}
				if (nodeText.length>0){
					var propertyArray=obj[node.getAttribute('name')];
					propertyArray[propertyArray.length]=nodeText;
					obj[node.getAttribute('name')]=propertyArray;
				}
			}
			else{
				obj[node.getAttribute('name')]=nodeText;
			}
			//赋值给对象
			if (funcHolder.isDebug){
				alert(''+objFieldValue+'\n Assigned to object properties['+node.getAttribute('name')+']');
			}
	    }
	    catch(e){
	    	alert('Analysis XML error:'+e);
	    }
		return obj;
	},
	/**
	 * 解析多个节点：list-field
	 * @param node 节点
	 * @param obj 返回的bean对象
	 * @return obj
	 */
	parseMultiDOMNodeForListField:function(node,obj){
		var funcHolder=arguments.callee.$;
		if (obj==null){
	       	//alert('new Array');
		    obj=new Array();
	    }

	    var eleObj=new Object();
	    //查询list元素的子节点
	    for (var j=0; j<node.childNodes.length; j++) {
	    	var fieldNode=node.childNodes[j];
	    	//取出其中的field元素进行处理
		    if ((fieldNode.nodeType==funcHolder.ELEMENT_NODE) && (fieldNode.tagName == 'list-field')) {
		     	eleObj=funcHolder.parseSingleDOMNodeForField(fieldNode,eleObj);
			}  
	    }
	    obj[obj.length]=eleObj;
	    return obj;
	},
	/**
	 * 反向序列化XMLDOM节点
	 * @param startNode 起始节点
	 * @param obj 返回的bean对象
	 * @return obj
	 */
	deserializedBeanFromXMLDOMNode:function(startNode,obj){
		var funcHolder=arguments.callee.$;
		try{
			//取出其中的field元素进行处理
		    if ((startNode.nodeType==funcHolder.ELEMENT_NODE) && (startNode.tagName == 'field')) {
		      	obj=funcHolder.parseSingleDOMNodeForField(startNode,obj);
			}
		    //取出其中的list元素进行处理
		    else if ((startNode.nodeType==funcHolder.ELEMENT_NODE) && (startNode.tagName == 'list')) {
		       	obj=funcHolder.parseMultiDOMNodeForListField(startNode,obj)
			}
		    else if (startNode.nodeType == funcHolder.TEXT_NODE){
		       	if (funcHolder.isDebug){
			        //alert('TEXT_NODE');
		       	}
			}
		    else if (startNode.nodeType == funcHolder.CDATA_SECTION_NODE){
		       	if (funcHolder.isDebug){
			       	//alert('CDATA_SECTION_NODE');
		       	}
		    }
		}
	    catch(e){
	    	alert('Analysis XML error:'+e);
	    }
	    return obj;
	},
	/**
	 * 反向序列化xml到javascript Bean
	 * @param xml xml字符串
	 * @return javascript Bean
	 */
	deserializedBeanFromXML:function (xml){
		var funcHolder=arguments.callee.$;
		var doc=funcHolder.createDomParser(xml);
		// documentElement总表示文档的root
		var objDomTree=doc.documentElement;
		var resultBean=null;
	    for (var i=0; i<objDomTree.childNodes.length; i++) {
	    	//获得节点
	    	var node=objDomTree.childNodes[i];
	    	//反向序列化XMLDOM节点
	    	resultBean=funcHolder.deserializedBeanFromXMLDOMNode(node,resultBean);
	    }
	    return resultBean;
	},
	/**
	 * 获得dom节点的text
	 */
	getNodeText:function (node) {
		var funcHolder=arguments.callee.$;
	    // is this a text or CDATA node?
	    if (node.nodeType == funcHolder.TEXT_NODE || node.nodeType == funcHolder.CDATA_SECTION_NODE) {
	        return node.data;
	    }
	    var i;
	    var returnValue = [];
	    for (i = 0; i < node.childNodes.length; i++) {
	    	//采用递归算法
	        returnValue.push(funcHolder.getNodeText(node.childNodes[i]));
	    }
	    return returnValue.join('');
	}
}

//委托者类
var Dispatcher=Class.create();
Dispatcher.prototype={
	name:'Dispatcher',
	//对class中的每个function都赋值一个值为this的$属性
	initialize:function(){
		new ClassUtils().registerFuncSelfLink(this);
	},
	/**
	 * 委托调用
	 * @param caller 调用者，func的拥有者
	 * @param func 如果是function对象，则使用Dispatcher对象自己的name作为参数；否则直接调用func
	 */
	dispatch:function(caller,func){
		if (func instanceof Function){
			var funcArguments=new Array();
			funcArguments[0]=arguments.callee.$.name;
			func.apply(caller,funcArguments);
		}
		else{
			eval(func);
		}
	}
}
//祈祷者类
var Invoker=Class.create();
Invoker.prototype={
	name:'Invoker',
	initialize:function(){
	},
	invoke:function(showMsg){
		alert(showMsg+"——this.name="+this.name);
	}
}
//浏览器系统检测类
var OSDetector=Class.create();
OSDetector.prototype={
	initialize:function(){},
	detect:function(){
		if (window.navigator.appName.indexOf('Microsoft')>=0){
				return "IE";
		}
		else if (window.navigator.appName.indexOf('Netscape')>=0){
			return "Mozilla";
		}
	}
}
//系统工具类
var OSUtil=Class.create();
OSUtil.prototype={
	initialize:function(){},
	/**
	 * 从document中取出某种类型、某个name的input field
	 * @param doc 任何document（包括IFrame的）
	 * @param fieldType
	 * @param fieldName (可选)
	 */
	getInputs:function(doc,fieldType,fieldName){
		var inputFields=doc.getElementsByTagName('input');
		var ret=new Array();
		for(var i=0;i<inputFields.length;i++){
			if (inputFields[i].type==fieldType){
				if (fieldName){
					if (inputFields[i].name==fieldName){
						ret.push(inputFields[i]);
					}
				}
				else{
					ret.push(inputFields[i]);
				}
			}
		}
		return ret;
	},
	/**
	 * 从document中取出某种类型、某个name的input field的value
	 * @param doc 任何document（包括IFrame的）
	 * @param fieldType
	 * @param fieldId
	 */
	getInputValue:function(doc,fieldType,fieldId){
		var inputField=doc.getElementById(fieldId);
		if (inputField && inputField.type==fieldType){
			return inputField.value;
		}
		else{
			return null;
		}
	}
}
//对象工具类
var ObjectUtil=Class.create();
ObjectUtil.prototype={
	initialize:function(){},
	/**
	 * 对象是否具有某个属性
	 * @param obj
	 * @param propertyName 属性名
	 */
	hasProperty:function(obj,propertyName){
		var undefinedObj;
		try{
			return !(obj[propertyName]==undefinedObj);
		}
		catch(e){
			return false;
		}
	}
}
//request封装类
var requestWrapper=Class.create();
requestWrapper.prototype={
	href:'',
	request:null,
	reqW_Self:null,
	initialize:function(href){
		reqW_Self=this;
		var urlStr=href;
		reqW_Self.href=href;
//		alert(urlStr.indexOf("?"));
		reqW_Self.request = new Object();
		if(urlStr.indexOf("?")!=-1) {
			var paramStr = urlStr.substr(urlStr.indexOf("?")+1);
			paramStrs = paramStr.split("&");
			for(var i=0;i<paramStrs.length;i++) {
				reqW_Self.request[paramStrs[i].split("=")[0]]=unescape(paramStrs[i].split("=")[1]);
				//alert(paramStrs[i].split("=")[0]+":"+paramStrs[i].split("=")[1]);
			}
		}
	},
	getParamValue:function(paramName){
		if (reqW_Self.request[paramName]){
			return reqW_Self.request[paramName];
		}
		else{
			return null;
		}
	}
}