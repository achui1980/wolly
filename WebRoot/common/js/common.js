/*******************************************************************************
 * code by achui 通用工具类
 ******************************************************************************/

/** ******************************************* */
// 对象察看工具，查看对象中的属性和值
// 参数 obj:需要查看的对象
/** ******************************************* */
function printObject(obj) {
	var res = "";
	for (var p in obj) {
		res += p + ":" + obj[p] + "\r\n";
	}
	alert(res);
}
function pomptObject(obj) {
	var res = "";
	for (var p in obj) {
		res += p + ":" + obj[p] + "\r\n";
	}
	// prompt("test", res);
	$('eleRemark').value = res;
}
// 克隆对象,避免同一个对象的指针引用
function cloneObj(obj) {
	var cloneObj = new Object();
	for (var p in obj) {
		cloneObj[p] = obj[p];
	}
	return cloneObj;
}
// 清空表单
function clearForm(formId) {
	var form = Ext.getCmp(formId);
	if (form == null)
		return;
	form.getForm().reset();
}
// 绑定下拉列表框
/** ******************************************* */
// 参数 selid:需要绑定的select的Id
// 参数 bindlist:从服务器获取的需要绑定到select的List
// 参数 valuepro:需要绑定的value值,对应到option的value值,一般对应到类里面的ID
// 参数 textpro:需要绑定的value值,对应到option的text值,一般对用到类里面的name
/** ******************************************* */
function bindSelect(selid, bindlist, valuepro, textpro, isSelect) {
	// 清空所有Option
	DWRUtil.removeAllOptions(selid);
	// 绑定Select
	var templist = new Array();
	if (typeof(isSelect) == "undefined" || isSelect == true)
		eval("templist=[{" + valuepro + ":''," + textpro + ":'请选择'}]");
	DWRUtil.addOptions(selid, templist, valuepro, textpro);
	DWRUtil.addOptions(selid, bindlist, valuepro, textpro);

}
// 刷新父页面
/** ******************************************* */
// 参数 isReflash:是否需要对父页面进行刷新 true:进行刷新,false:不进行刷新
// 参数 tableId:需要刷新的父页面的tableId
// 参数 reflashMenu:是否需要对右边树形结构进行刷新 true:进行刷新,false:不进行刷新
/** ******************************************* */
function closeandreflash(isReflash, tableId, reflashMenu) {
	if (isReflash) {
		// eval("self.opener.refresh_" + tableId + "();");
	}
	if (reflashMenu)
		self.opener.parent.contents.location.reload();
	window.opener = null;
	window.close();
}
// 刷新父页面(EcSide专用)
/** ******************************************* */
// 参数 isReflash:是否需要对父页面进行刷新 true:进行刷新,false:不进行刷新
// 参数 tableId:需要刷新的父页面的tableId
// 参数 reflashMenu:是否需要对右边树形结构进行刷新 true:进行刷新,false:不进行刷新
/** ******************************************* */
function closeandreflashEC(isReflash, tableId, reflashMenu) {
	if (isReflash) {
		// eval("self.opener.refresh_"+tableId+"();");
		try {
			self.opener.reloadGrid(tableId)
			// reloadGrid(tableId);
		} catch (e) {
			// printObject(e);
		}
	}
	if (reflashMenu)
		self.opener.parent.contents.location.reload();
	window.opener = null;
	window.close();
}

// 不关闭页面刷新父页面
function reflashParent(tableId) {
	if (self.opener != null) {
		var ecsideObj = self.opener.Ext.getCmp(tableId);
		if (typeof(ecsideObj) != 'undefined') {
			ecsideObj.getStore().reload();
		}
	}
}

// 刷新本页面
/** ******************************************* */
// 参数 isReflash:是否需要对父页面进行刷新 true:进行刷新,false:不进行刷新
// 参数 tableId:需要刷新的父页面的tableId
/** ******************************************* */
function reflashLocal(isReflash, tableId) {
	if (isReflash) {
		Ext.getCmp(tableId).getStore().reload();
	}
}
// 刷新本页面
/** ******************************************* */
// 参数 isReflash:是否需要对父页面进行刷新 true:进行刷新,false:不进行刷新
// 参数 tableId:需要刷新的父页面的tableId
/** ******************************************* */
function reloadGrid(tableId) {
	reflashLocal(true, tableId);
}

// 用正则表达式将前后空格
// 用空字符串替代。
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
function openWindow(url) {
	// alert("document.body.scrollHeight:"+document.body.scrollHeight);
	// alert("document.body.clientHeight:"+document.body.clientHeight);
	var param = "height=600,width=800,left=" + (screen.availWidth - 800) / 2
			+ ",top=" + (screen.availHeight - 600) / 2
			+ ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";
	window.open(url, "", param);// ,null++,
	// "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no");
}
function openEleWindow(url) {
	var param = "height=" + (screen.availHeight - 30) + ",width="
			+ (screen.availWidth - 10) + ",left=" + 0 + ",top=" + 0
			+ ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";
	window.open(url, "", param);// ,null++,
	// "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no");
}

function openWindowBase(heigth, width, url) {
	var param = "height=" + heigth + ",width=" + width + ",left="
			+ (screen.availWidth - width) / 2 + ",top="
			+ (screen.availHeight - heigth) / 2
			+ ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";
	window.open(url, "", param);
}
function openWindowBaseTop(heigth, width, url) {
	var param = "height=" + heigth + ",width=" + width + ",left="
			+ (screen.availWidth - width) / 2 + ",top=0"
			+ ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";
	window.open(url, "", param);
}
function openCustWindow(url) {
	var param = "height=690,width=1020,left=" + (screen.availWidth - 1024) / 2
			+ ",top=" + (screen.availHeight - 758) / 2
			+ ",status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";
	window.open(url, "", param);// ,null++,
	// "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no");
}
function openSmallWindow(url) {
	window
			.open(
					url,
					"",
					"height=500,width=500,left="
							+ (screen.availWidth - 500)
							/ 2
							+ ",top="
							+ (screen.availHeight - 500)
							/ 2
							+ ", status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
function openEmailWindow(url) {
	window
			.open(
					url,
					"",
					"height=296,width=512,left="
							+ (screen.availWidth - 500)
							/ 2
							+ ",top="
							+ (screen.availHeight - 500)
							/ 2
							+ ", status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
function openMaxWindow(height, width, url) {
	var param = "height=" + (height - 50) + ",width=" + (width - 10)
			+ ",top=0,left=0,scrollbars=yes"
	window.open(url, "", param);
}
function openFullWindow(url) {
	var param = "location=0,status=yes,menubar=0,toolbar=0,scrollbars=yes,height="
			+ (screen.availHeight - 50) + ",width=" + (screen.availWidth - 10)
	window.open(url, "", param);
}
/** ******************************************* */
// 函数功能:绑定表单验证属性
// 参数 eleId:表单元素的ID
// 参数 msg:验证不通过是需要提示的信息
// 参数 datatype:验证类型,具体参照Validator.chm
/** ******************************************* */
function appendattribute(eleId, msg, datatype, isrequire) {
	var obj = document.getElementById(eleId);
	obj.setAttribute('msg', msg);
	obj.setAttribute('datatype', datatype);
	if (typeof(isrequire) == "undefined")
		isrequire = 'true';
	obj.setAttribute("Require", isrequire);
}
/** ******************************************* */
// 函数功能:设置某个容器下面所有元素的状态(置灰或激活)
// 参数 container:需要设置的容器id (div,span等)
// 参数 isActive:是否激活 true:不激活 false:激活
/** ******************************************* */
function setAllElementsByContainer(container, isActive) {
	var elements = document.getElementById(container);
	eles_input = elements.getElementsByTagName("input");
	eles_sel = elements.getElementsByTagName("select");
	// 置灰所有input控件
	for (var i = 0; i < eles_input.length; i++) {
		var ele = eles_input[i];
		ele.disabled = isActive;
	}
	// 置灰所有select控件
	for (var i = 0; i < eles_sel.length; i++) {
		var ele = eles_sel[i];
		ele.disabled = isActive;
	}
}

/** ******************************************* */
// 函数功能:传入日期字符串,返回日期型数据
// 参数 strDate:传入的日期字符串
// 调用方法：var test = getDateType("2009-01-01")
/** ******************************************* */
function getDateType(strDate) {
	if (strDate == "" || strDate == "undefined" || strDate == null)
		return null;
	var arr = strDate.split("-");
	if (arr.length < 3) // 不是由年月日组成
		return null;
	var year = parseInt(arr[0], 10); // 获取年
	var month = parseInt(arr[1], 10);// 获取月
	var date = parseInt(arr[2], 10);// 获取日
	var dateType = new Date(year, month - 1, date);
	return dateType;
}

// 添加新客户
function addCust() {
	openCustWindow('cotcustomer.do?method=add&flag=0');
}
// 模糊查询------------------------

/*
 * loadbar.js:进度条
 */

function loadbar(fl) {
	var x = document.documentElement.clientWidth;
	var y = document.body.scrollHeight;
	var el = document.getElementById('loader2');
	if (!el) {
		document.body.insertAdjacentHTML("beforeEnd",
				"<div id='loader2'></div>");
		el = document.getElementById('loader2');
	}
	if (null != el) {
		if (el.innerHTML == "") {
			el.innerHTML = '<div  style="display : block; position : absolute; left :400px; top :250px; z-index : 800;word-break:break-all;word-wrap:break-word;FONT-FAMILY: Arial, Helvetica, Verdana, Sans-serif;color: black;FILTER: Alpha(opacity=100);background-color: #ffffff;padding: 5px;FONT-SIZE: 20pttext-align: left;border: 1px solid #dddddd;">'
					+ '  <img src="common/images/clocks.gif" alt="请等待"/>'
					+ '  <div>请稍候...</div>'
					+ '  <strong>数据载入中......</strong>'
					+ '</div>';
		}
		var top = 100;
		var left = 350;
		el.style.visibility = (fl == 1) ? 'visible' : 'hidden';
		el.style.display = (fl == 1) ? 'block' : 'none';
		el.style.left = left + "px"
		el.style.top = top + "px";
		el.style.zIndex = 100;
	}
}

function getCurrentDate(fomate) {
	var currdate = "";
	DWREngine.setAsync(false);
	contexUtil.getCurrentDate(fomate, function(res) {
				currdate = res;
			});
	DWREngine.setAsync(true);
	return currdate;
}

// 取得最大8位的随机数字
function getRandom() {
	return Math.round(Math.random() * 100000000);
}

// 编辑页面添加和修改的共用按钮的权限判断
function checkAddMod(mainId) {
	if (mainId != 'null' && mainId != '') {
		var isMod = getPopedomByOpType(vaildUrl, "MOD");
		if (isMod == 0) {
			return 2;
		} else {
			return 0;
		}
	} else {
		var isPopedom = getPopedomByOpType(vaildUrl, "ADD");
		if (isPopedom == 0) {
			return 1;
		} else {
			return 0;
		}
	}

}

// 显示日期
function showPicker(obj) {
	if (obj.value == '') {
		obj.value = getCurrentDate("yyyy-MM-dd");
	}
}

// 验证权限
function checkPopedoms(url, type) {
	if (loginEmpId == "admin") {
		return true;
	}
	var flag = true;
	DWREngine.setAsync(false);
	cotPopedomService.getPopedomByMenu(url, function(res) {
				mapPopedom = res;
				// 添加权限判断
				var isPopedom = getPopedomByOpType(url, type);
				if (isPopedom == 0) {
					flag = false;
				}
			});
	DWREngine.setAsync(true);
	return flag;
}

// 加入进度条
function mask(val) {
	Ext.getBody().mask((val ? val : "Processing,Please Waiting!"),
			'x-mask-loading');
}
// 去除进度条
function unmask() {
	Ext.getBody().unmask(Ext.getBody().removeMask);
}
/*
 * 获取当前表单数据 flag 表示是否在获取数据前执行验证，如果验证不成功返回false
 */
function getFormValues(formPanel, flag) {
	var redata = {};
	var items = formPanel.getForm().items;
	for (var i = 0; i < items.getCount(); i++) {
		var item = items.get(i);
		// 去掉无名的
		if (!item.getName())
			continue;
		// 执行验证,验证不成功直接返回空
		if (flag && !item.validate()) {
			item.focus();
			return false;
		}
		if (item.xtype == 'radio') {
			if (item.checked) {
				redata[item.getName()] = item.getRawValue();
			}
		} else {

			// if(item.getName()=='custId'){
			// alert(item.getValue())
			// alert(item.getRawValue())
			// }
			redata[item.getName()] = item.getValue();
		}
	}
	return redata;
}
// 正在修改中的函数（临时使用）
function ExtToDoFn() {
	alert("该功能尚在开发中......")
}

// 显示带时间的日期
function listTime(obj) {
	if (obj != null) {
		var date = new Date(obj);
		var y = date.getYear();
		var m = (date.getMonth() + 1) < 10
				? ("0" + (date.getMonth() + 1))
				: (date.getMonth() + 1);
		var d = date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate();
		var h = date.getHours() < 10 ? ("0" + date.getHours()) : date
				.getHours();
		var ms = date.getMinutes() < 10 ? ("0" + date.getMinutes()) : date
				.getMinutes();
		var ss = date.getSeconds() < 10 ? ("0" + date.getSeconds()) : date
				.getSeconds();

		return y + '-' + m + '-' + d + " " + h + ":" + ms + ":" + ss;
	} else {
		return "";
	}
}

/** ******************************************* */
// 函数功能:实现网页图片等比例缩放
// 参数 ImgD:图片对象
// 参数 FitWidth:需要适应的图片框宽度
// 参数 FitHeight:需要适应的图片框高度
// 调用方法：<img src="test.jpg" onload="javascript:DrawImage(this,100,100);" />
// 参数 minWidth为最小显示宽度,在弹出大图片时,一显示时,刚好整个工具栏按钮都能显示
/** ******************************************* */
function DrawImage(ImgD, FitWidth, FitHeight, minWidth) {
	var image = new Image();
	image.src = ImgD.src;
	if (Ext.isIE) {
		if (image.width > 0 && image.height > 0) {
			if (image.width / image.height >= FitWidth / FitHeight) {
				if (image.width > FitWidth) {
					ImgD.width = FitWidth;
					ImgD.height = (image.height * FitWidth) / image.width;
				} else {
					if (minWidth && image.width < minWidth) {
						ImgD.width = minWidth;
						ImgD.height = (image.height * minWidth) / image.width;
					} else {
						ImgD.width = image.width;
						ImgD.height = image.height;
					}
				}
			} else {
				if (image.height > FitHeight) {
					ImgD.height = FitHeight;
					ImgD.width = (image.width * FitHeight) / image.height;
				} else {
					if (minWidth && image.width < minWidth) {
						ImgD.width = minWidth;
						ImgD.height = (image.height * minWidth) / image.width;
					} else {
						ImgD.width = image.width;
						ImgD.height = image.height;
					}
				}
			}
		}
		if (customEl != null) {
			customEl.setSize(ImgD.width, ImgD.height + 35);
			// 储存一展现时的宽和高
			ImgD.oldW = ImgD.width;
			ImgD.oldH = ImgD.height;
		}
	} else {
		image.onload = function() {
			if (image.width > 0 && image.height > 0) {
				if (image.width / image.height >= FitWidth / FitHeight) {
					if (image.width > FitWidth) {
						ImgD.width = FitWidth;
						ImgD.height = (image.height * FitWidth) / image.width;
					} else {
						if (minWidth && image.width < minWidth) {
							ImgD.width = minWidth;
							ImgD.height = (image.height * minWidth)
									/ image.width;
						} else {
							ImgD.width = image.width;
							ImgD.height = image.height;
						}
					}
				} else {
					if (image.height > FitHeight) {
						ImgD.height = FitHeight;
						ImgD.width = (image.width * FitHeight) / image.height;
					} else {
						if (minWidth && image.width < minWidth) {
							ImgD.width = minWidth;
							ImgD.height = (image.height * minWidth)
									/ image.width;
						} else {
							ImgD.width = image.width;
							ImgD.height = image.height;
						}
					}
				}
			}
			if (customEl != null) {
				customEl.setSize(ImgD.width, ImgD.height + 35);
				// 储存一展现时的宽和高
				ImgD.oldW = ImgD.width;
				ImgD.oldH = ImgD.height;
			}
		}
	}
}

// -----双击放大图片
// 双击弹出拖拉图片层
var customEl = document.customEl;
function showBigPicDiv(pic) {
	if (customEl == null) {
		// 参数 DrawImage最小显示宽度220,在弹出大图片时,一显示时,刚好整个工具栏按钮都能显示
		Ext.DomHelper.append(document.body, {
			html : '<div id="tempPicDiv" style="zoom: 1;position:absolute;z-index:9500;left:0;top:0;">'
					+ '<div id="tbDiv"></div>'
					+ '<form id="picForm" name="picForm" onsubmit="return false">'
					+ '<img id="bigPicDiv" src="" width="300" height="200" onload="javascript:DrawImage(this,300,300,220)" style="zoom: 1;" onmousewheel="return onWheelZoom(this)"/>'
					+ '</form>' + '</div>'
		}, true);
		var custom = new Ext.Resizable('tempPicDiv', {
					wrap : true,
					pinned : true,
					minWidth : 50,
					minHeight : 50,
					preserveRatio : true,
					dynamic : true,
					handles : 'all', // shorthand for 'n s e w ne nw se sw'
					draggable : true,
					listeners : {
						'beforeresize' : function(rz, e) {
							$('tempPicDiv').style.zoom = 1;
							$('bigPicDiv').style.zoom = 1;
						},
						'resize' : function(rz, width, height, e) {
							var zoom = width / $('bigPicDiv').oldW;
							$('bigPicDiv').style.zoom = zoom;
						}
					}
				});

		var tb = new Ext.Toolbar({
					items : [{
								text : "Close",
								handler : function() {
									customEl.hide();
								}
							}, '-', {
								text : "Actual size",
								handler : function() {
									$('bigPicDiv').style.zoom = 1;
									customEl.setSize($('bigPicDiv').oldW,
											$('bigPicDiv').oldH + 30);
								}
							}, '-', {
								text : "Zoom 2",
								handler : function() {
									$('bigPicDiv').style.zoom = 2;
									customEl.setSize($('bigPicDiv').oldW * 2,
											$('bigPicDiv').oldH * 2 + 30);
								}
							}, '-', {
								text : "Save as",
								handler : function() {
									var path = $('bigPicDiv').src;
									var index = path.search(/.png/i);
									if (index == -1) {
										var form = document
												.getElementById("picForm");
										form.action = path.replace(
												/showPicture/, "downPicture");
										form.target = "";
										form.method = "post";
										form.submit();
										// var ul= path.replace(/showPicture/,
										// "downPicture");
										// downRpt(ul);

									} else {
										Ext.Msg.alert('Message', "No Picture!");
									}
								}
							}, '-']
				});
		tb.render('tbDiv');
		customEl = custom.getEl();
		customEl.on('dblclick', function() {
					custom.getEl().hide(true);
				});
		customEl.hide();
		document.customEl = customEl;// 存到document的一个属性中
		customEl.show(true);
		$('tempPicDiv').style.zoom = 1;
		$('bigPicDiv').style.zoom = 1;
		$('bigPicDiv').src = pic.src;
		customEl.setSize($('bigPicDiv').oldW, $('bigPicDiv').oldH + 30);
		customEl.center();
	}else{
		customEl.setSize($('bigPicDiv').oldW, $('bigPicDiv').oldH + 30);
		$('tempPicDiv').style.zoom = 1;
		$('bigPicDiv').style.zoom = 1;
		$('bigPicDiv').src = pic.src;
		customEl.center();
		customEl.show(true);
	}
}
function onWheelZoom(obj) {
	var zoom = obj.style.zoom;
	var tZoom;
	if (event.wheelDelta > 0) {
		tZoom = Number(zoom).add(0.05);
	} else {
		tZoom = Number(zoom).sub(0.05);
	}
	if (tZoom < 0.5) {
		return true;
	}
	obj.style.zoom = tZoom;
	// 升级谷歌版本后,在zoom后不会自动计算obj.width,obj.height
	var wd = Number(obj.oldW).mul(tZoom);
	var hd = Number(obj.oldH).mul(tZoom);

	customEl.setSize(wd, hd + 35);

	return false;
}

// 获取sysconfig.properties要保留几位小数
function getDeNum(key) {
	var temp = 3;// 默认为3位
	DWREngine.setAsync(false);
	contexUtil.getProperty('remoteaddr.properties', key, function(str) {
				if (str != null) {
					temp = str;
				}
			});
	DWREngine.setAsync(true);
	return temp;
}
// 通过小数位生成"0.000"字符串
function getDeStr(num) {
	var temp = "0";
	for (var i = 0; i < num; i++) {
		if (i == 0) {
			temp += ".";
		}
		temp += "0";
	}
	return temp;
}

// 为了下载时不弹出新窗口
function downRpt(url) {
	if ($("ckDiv") == null) {
		var html = '<div id="ckDiv" style="z-index:6000;">'
				+ '<iframe id="ckFrame" name="ckFrame">' + '</iframe>'
				+ '</div>'
		Ext.DomHelper.append(document.body, {
					html : html
				}, true);
	}
	var frame = window.frames["ckFrame"];
	frame.location.href = url;
}

// 清空表单和给表单赋值
function clearFormAndSet(formPnl, data) {
	var recdata = {};
	formPnl.getForm().items.each(function(item) {
				// 去掉无名的
				if (!item.getName())
					return;
				recdata[item.getName()] = null;
			});
	if (data)
		Ext.apply(recdata, data);
	// 设置表单数据
	formPnl.getForm().loadRecord(new Ext.data.Record(recdata, null));
	//
	formPnl.getForm().items.each(function(item) {
				if (item.xtype == 'radio') {
					if (item.getRawValue() == recdata[item.getName()]) {
						item.setValue(true);
						// alert(item.getName() +":" + item.getRawValue());
					}
				}
				if (item.xtype == 'combo') {
					var rec = recdata[item.getName()];
					if (rec) {
						if (rec.id) {
							item.setValue(rec.id);
						}
						if (rec.name) {
							item.setRawValue(rec.name);
						}
					}
				}
			});
	// 清除所有的验证信息
	formPnl.getForm().clearInvalid();
}

// 屏蔽所有iframe的刷新
function stopIframeUpdate() {
	if (event.keyCode == 116 && self.frameElement.tagName == "IFRAME") {
		event.keyCode = 0;
		event.cancelBubble = true;
		return false;
	}
}

// 因为Ext.util.Format.numberRenderer返回的是字符串,不能正常排序,所以要改写下
function numFormat(v, record, format) {
	// alert(v);
	// if(v == null) return 0;
	var temp = Ext.util.Format.number(v, format);
	if (!isNaN(temp)) {
		return Number(temp);
	} else {
		return 0;
	}
}

// 因为后台返回的时间对象是个json,不能正常排序,所以要改写下
function timeSortType(value) {
	if (value) {
		if (value.time) {
			return value.time;
		} else {
			// 后台java.sql.date转化后到前台day是日期,不是"一周的第几天"
			var date = new Date(value.year, value.month + 1, value.day);
			return date.getTime();
		}
	} else
		return 0;
}
// 获取当前员工
function getCurrentEmp() {
	DWREngine.setAsync(false);
	var currEmp;
	cotPopedomService.getLoginEmp(function(res) {
				currEmp = res;
			});
	if (currEmp == null) {
		alert("Please Re-Login!");
		return;
	}
	DWREngine.setAsync(true);
	return currEmp;
}

// fckeditor赋值
function fckSetValue(id, value) {
	var oEditor = FCKeditorAPI.GetInstance(id);
	if (oEditor != undefined)
		oEditor.SetData(value)
}
// fckeditor取值
function fckGetValue(id) {
	var oEditor = FCKeditorAPI.GetInstance(id);
	var data = '';
	if (oEditor != undefined)
		data = oEditor.GetData()
	return data;
}

/**
 * 获得可为空的邮件地址正则表达式
 * 
 * @return {}
 */
function getEmailOrNullRegex() {
	var emailOrNullRegex = /^(((\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6})|\s{0})$/;
	return emailOrNullRegex;
}
document.onkeydown = stopIframeUpdate;

// 是否隐藏生产价
var hidePri = false;
// 是否隐藏厂家
var hideFac = false;
// 查询是否有查看厂家权限
function getFacPodom(url) {
	DWREngine.setAsync(false);
	cotPopedomService.getLoginEmpId(function(empId) {
				if (empId != null && empId != 'admin') {
					cotPopedomService.getPopedomByMenu(url, function(map) {
								if (map != null) {
									// 是否隐藏厂家
									if (typeof(map.SELFAC) == "undefined") {
										hideFac = true;
									}
									// 是否隐藏生产价(样品模块除外)
									if (url != 'cotelements.do'
											&& typeof(map.SELPRI) == "undefined") {
										hidePri = true;
									}
								}
							});
				}
			});
	DWREngine.setAsync(true);
}

function openPdf(url) {
	var rdm = Math.round(Math.random() * 10000);
	openFullWindow(url + "?tmp=" + rdm);
}

function downRptFile(orderno, type) {
	var url = "";
	DWREngine.setAsync(false);
	contexUtil.checkComunication(orderno, type, function(res) {
		if (res) {
			// downRpt("./downChkRpt.action?orderNo="+orderno+"&type="+type);
			var temp = "saverptfile/" + type + "/" + orderno + ".pdf";
			url = "<a onclick=openPdf('"
					+ temp
					+ "') style='cursor: hand;text-decoration: underline;'><font color=blue>"
					+ orderno + "</font></a>";
		} else {
			// Ext.Msg.alert("Message","Report not generated");
			url = "";
		}
	});
	DWREngine.setAsync(true);
	return url;
}

// 加锁和解锁barcode
function lockBarcode(btn) {
	if (btn.iconCls == 'unlock') {
		btn.setIconClass('lock');
	} else {
		btn.setIconClass('unlock');
	}
}

// 设置条形码单号
function setBarcodeNo() {
	var bc = "";
	DWREngine.setAsync(false);
	baseDataUtil.getBarcodeNo(function(ps) {
				var a = 5;
				var b = 7;
				var c = 0;
				var d = 4;
				var e = 0;
				var f = 3;
				var g = 9;
				//取出[5SEQ]的每位数字分别存入(h,i,j,k,l)
				var h = parseInt(ps.substring(ps.length - 5, ps.length - 4));
				var i = parseInt(ps.substring(ps.length - 4, ps.length - 3));
				var j = parseInt(ps.substring(ps.length - 3, ps.length - 2));
				var k = parseInt(ps.substring(ps.length - 2, ps.length - 1));
				var l = parseInt(ps.substring(ps.length - 1));

				var AA = (b + d + f + h + j + l) * 3;
				var BB = (a + c + e + g + i + k) + AA;
				var temp = (BB + "");
				//取出temp最后一位数字
				var last = temp.substring(temp.length - 1);
				//用10-这个数字
				var CC = 10 - parseInt(last);
				//如果结果为10时,用0代替
				if (CC == 10) {
					CC = 0;
				}
				//得到最终结果bc
				bc = "5704039" + ps.substring(ps.length - 5) + CC;
				$('barcode').value = bc;
			});
	DWREngine.setAsync(true);
	return bc;
}

// 刷新主界面
function reloadHome() {
	if (self.opener != null) {
		self.opener.parent.window.frames['tab_home1'].location.href = "cotorderstatus.do?method=queryOrderStatus";
	}
}

// 判断字符串是否是undefined/null/""/0
function isBlank(str) {
	if (!str || str == null || str == "" || str == 0) {
		return true;
	} else {
		return false;
	}
}

// 判断字符串是否是undefined/null/""
function isNullStr(str) {
	if (!str || str == null || str == "") {
		return true;
	} else {
		return false;
	}
}

// 变大
function max(img) {
	img.style.zoom = 2;
}

// 变小
function min(img) {
	img.style.zoom = 1;
}

//日期加减几天
function showdate(n) {
	var uom = new Date();
	uom.setDate(uom.getDate() + n);
	uom = uom.getFullYear() + "-" + (uom.getMonth() + 1) + "-" + uom.getDate();
	return uom;
}

function hflip(obj1,obj2,width,height,flag){
	var j = jQuery.noConflict();
	obj1="#"+obj1;
	obj2="#"+obj2;
	var margin =width/2;
	j(obj2).stop().css({width:'0px',height:''+height+'px',marginLeft:''+margin+'px',opacity:'0'});
//	$(obj1).click(function(){
		j(flag?obj1:obj2).stop().animate({width:'0px',height:''+height+'px',marginLeft:''+margin+'px',opacity:'0'},{duration:500});
		window.setTimeout(function() {
			j(flag?obj2:obj1).stop().animate({width:''+width+'px',height:''+height+'px',marginLeft:'0px',opacity:'1'},{duration:500});
		},500);
//	});
}
/**
 * 引入js文件
 * 
 * @param {String}
 *            path js相对项目路径
 */
function $import(path) {
	document.write("<script type='text/javascript' src='" + path
			+ "'></script>");
}
/**
 * 引入CSS文件
 * 
 * @param {String}
 *            path CSS相对项目路径
 */
function $importCss(path) {
	document.write("<link rel='stylesheet' type='text/css' href='" + path
			+ "'/>");
}
