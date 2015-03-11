<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String webapp = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Packing Way Information</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/common.jsp"></jsp:include>
		<!-- 导入dwr -->
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotElementsService.js'></script>
		<script type='text/javascript'
			src='<%=webapp %>/dwr/interface/cotBoxTypeService.js'></script>
		<script type="text/javascript">
			//加载尺寸信息表单
			function loadBox(){
				//因为这个初始化方法比popedom.js中的初始化事件早，在取权限时取不到登录人，所以另外再调用一次
				getLoginEmpid();
				//判断用户是否有权限修改包装信息
				var isModBox = getPopedomByOpType("cotboxinfo.do","MOD");
				if(isModBox==0){//没有修改包装信息权限
				    setAllElementsByContainer("box",true);
				    $("boxBtn").style.display ='none';
				}else{
					setAllElementsByContainer("box",false);
					$("boxBtn").style.display ='block';
				}
				validForm();
				var id = $("eId").value;
				 //加载包装类型
				cotElementsService.getBoxTypeList(function(res){
					bindSelect('boxTypeId',res,'id','typeName');
					//加载表单
			  		DWRUtil.setValues(parent.oldElement);
				})
				//加载内包装名称
				cotBoxTypeService.getBoxINameList(function(res){
		            composeBoxName(res,'boxIbType');
				});
				//加载中包装名称
				cotBoxTypeService.getBoxMNameList(function(res){
					composeBoxName(res,'boxMbType');
				});
				//加载外包装名称
				cotBoxTypeService.getBoxONameList(function(res){
					composeBoxName(res,'boxObType');
				});
			}
			
			//组合中英文包装类型名称
			function composeBoxName(res,boxType){
				//清空所有Option
				DWRUtil.removeAllOptions(boxType);
				var ary = new Array();
				var templist = {};
				templist.value = '';
				templist.key = 'Choose';
				ary.push(templist);
				if(res!=null){
					for(var i=0;i<res.length;i++){
						if(res[i].valueEn==null){
							res[i].valueEn = '';
						}
						var name = res[i].value+'('+res[i].valueEn+')';
						var temp = {};
						temp.value = name;
						temp.key = name;
						ary.push(temp);
					}
				}
				DWRUtil.addOptions(boxType,ary,'value','key');
			}
			
			//修改包装信息
			function modBox()
			{
				//验证表单
				var form = document.forms['cotBoxForm'];
				var b = false;
				b = Validator.Validate(form,1);
				if(!b)
				{
					return;
				}
				var obj = DWRUtil.getValues("cotBoxForm");
				var id = $("eId").value;
				for(var p in obj){
			    	parent.oldElement[p]= obj[p];
			    }
			   	parent.boxUpdate(parent.oldElement);
			}
			//尺寸表单验证
			function validForm()
			{
				appendattribute("boxL"," Length of product Size must be numeric type","Double",'false');
				appendattribute("boxW","Width of product Size must be numeric type","Double",'false');
				appendattribute("boxH","Height of product Size must be numeric type","Double",'false');
				
				appendattribute("boxLInch","Length of product Size must be numeric type","Double",'false');
				appendattribute("boxWInch","Width of product Size must be numeric type","Double",'false');
				appendattribute("boxHInch","Height of product Size must be numeric type","Double",'false');
				
				appendattribute("boxIbL","Length of inner box must be numeric type","Double",'false');
				appendattribute("boxIbW","Width of inner box must be numeric type","Double",'false');
				appendattribute("boxIbH","Height of inner box Size must be numeric type","Double",'false');
				
				appendattribute("boxIbLInch","Length of inner box must be numeric type","Double",'false');
				appendattribute("boxIbWInch","Width of inner box must be numeric type","Double",'false');
				appendattribute("boxIbHInch","Height of inner box must be numeric type","Double",'false');
				
				appendattribute("boxMbL","Length of middle box must be numeric type","Double",'false');
				appendattribute("boxMbW","Width of middle box must be numeric type","Double",'false');
				appendattribute("boxMbH","LHeight of middle box must be numeric type","Double",'false');
				
				appendattribute("boxMbLInch","Length of middle box must be numeric type","Double",'false');
				appendattribute("boxMbWInch","Width of middle box must be numeric type","Double",'false');
				appendattribute("boxMbHInch","Height of middle box must be numeric type","Double",'false');
				
				appendattribute("boxObLInch","Length of outer carton must be numeric type","Double",'false');
				appendattribute("boxObWInch","Width of outer carton must be numeric type","Double",'false');
				appendattribute("boxObHInch","Height of outer carton must be numeric type","Double",'false');
				
				appendattribute("boxObL","Length of outer carton must be numeric type","Double",'false');
				appendattribute("boxObW","Width of outer carton must be numeric type","Double",'false');
				appendattribute("boxObH","Height of outer carton must be numeric type","Double",'false');
				
				appendattribute("boxGrossWeigth","G.W. must be numeric type","Double",'false');
				appendattribute("boxNetWeigth","N.W. must be numeric type","Double",'false');
				
				appendattribute("boxIbCount","Packing must be numeric type","Double",'false');
				appendattribute("boxMbCount","Pacing must be numeric type","Double",'false');
				appendattribute("boxObCount","Packing must be numeric type","Double",'false');
				
				appendattribute("boxCuft","Cuft must be numeric type","Double",'false');
				appendattribute("boxCbm","CBM must be numeric type","Double",'false');
				appendattribute("boxHandleH","N.W. must be numeric type","Double",'false');
				appendattribute("boxWeigth","Weight must be numeric type","Double",'false');
			}
			
			//尺寸和英寸转换
			function changeInch(fromObject,toObject,flag){
				if(fromObject.value==''){
					$(toObject).value='';
					return;
				}else{
					if(isNaN(fromObject.value)){
						fromObject.value='';
						if(toObject=='boxObL' || toObject=='boxObW' || toObject=='boxObH' || toObject=='boxObLInch' || toObject=='boxObWInch' || toObject=='boxObHInch' ){
							$('boxCbm').value = '';
							$('boxCuft').value = '';
						}
						return;
					}
				}
				if(flag==0){
					$(toObject).value=(fromObject.value/2.54).toFixed("4");
				}else{
					$(toObject).value=round((fromObject.value*2.54),2);
				}
				if(toObject=='boxObL' || toObject=='boxObW' || toObject=='boxObH' || toObject=='boxObLInch' || toObject=='boxObWInch' || toObject=='boxObHInch' ){
					countCube();
				}
			}
			
			//计算样品体积和CUFT
			function countCube(){
				var L=parseFloat($('boxObL').value);
				var W=parseFloat($('boxObW').value);
				var H=parseFloat($('boxObH').value);
				var boxObCount=parseFloat($('boxObCount').value);
				if(isNaN(L) || $('boxObL').value!=L){
					$('boxObL').value='';
					$('boxCbm').value = '';
					$('boxCuft').value = '';
					return;
				}
				if(isNaN(W) || $('boxObW').value!=W){
					$('boxObW').value='';
					$('boxCbm').value = '';
					$('boxCuft').value = '';
					return;
				}
				if(isNaN(H) || $('boxObH').value!=H){
					$('boxObH').value='';
					$('boxCbm').value = '';
					$('boxCuft').value = '';
					return;
				}
				//计算cbm和cuft
				$('boxCbm').value = (L*W*H*0.000001).toFixed("4");
				$('boxCuft').value = (((L*W*H*0.000001).toFixed("4"))*35.3).toFixed("4");
				//如果外包装不为空的话,计算装箱数(20/40/40HQ的柜体积分别为24/54/68),计算公式=柜体积/cbm*外包装
				if(isNaN(boxObCount) || $('boxObCount').value!=boxObCount){
					$('box20Count').value='';
					$('box40Count').value = '';
					$('box40hqCount').value = '';
					return;
				}
				$('box20Count').value=parseInt((24/$('boxCbm').value)*boxObCount);
				$('box40Count').value = parseInt((54/$('boxCbm').value)*boxObCount);
				$('box40hqCount').value = parseInt((68/$('boxCbm').value)*boxObCount);
			}
			
			//CBM和CUFT转换
			function change(fromObject,toObject,flag){
				if(fromObject.value==''){
					$(toObject).value='';
					$('box20Count').value='';
					$('box40Count').value = '';
					$('box40hqCount').value = '';
					return;
				}else{
					if(isNaN(fromObject.value)){
						fromObject.value='';
						$(toObject).value='';
						$('box20Count').value='';
						$('box40Count').value = '';
						$('box40hqCount').value = '';
						return;
					}
				}
				if(flag==0){
					$(toObject).value=(fromObject.value*35.3).toFixed("4");
				}else{
					$(toObject).value=(fromObject.value/35.3).toFixed("4");
				}
			}
			
			//包装类型的值改变事件
			/*function showType(value){
				if(value==''){
					return;
				}
				cotElementsService.getBoxTypeById(value,function(res){
					if(res!=null){
						$('boxIbType').value = res.boxIName;
						$('boxMbType').value = res.boxMName;
						$('boxObType').value = res.boxOName;
					}
				})
			};*/
			
			//包装类型的值改变事件
			function showType(value,eleId){
				if(value==''){
					return;
				}
				DWREngine.setAsync(false);
				cotElementsService.getBoxTypeById(value,function(res){
					if(res!=null){
					    var iId = res.boxIName;
					    var mId = res.boxMName;
					    var oId = res.boxOName;
					    if(iId!=null){
					    	cotElementsService.getBoxNameById(parseInt(iId),function(iRes){
								$('boxIbType').value = iRes;
							});
					    }else{
					    	$('boxIbType').value = '';
					    }
						if(mId!=null){
					    	cotElementsService.getBoxNameById(parseInt(mId),function(mRes){
								$('boxMbType').value = mRes;
							});
					    }else{
					    	$('boxMbType').value = '';
					    }
					    if(oId!=null){
					    	cotElementsService.getBoxNameById(parseInt(oId),function(oRes){
								$('boxObType').value = oRes;
							});
					    }else{
					    	$('boxObType').value = '';
					    }
					}
				});
				DWREngine.setAsync(true);
			};
			
			//四舍五入(保留b位小数)
			function round(a,b){
				return((Math.round(a*Math.pow(10,b))*Math.pow(10,-b)).toFixed(b));
			}
			
			//尺寸和英寸转换(自动填写中文规格和英文规格)
			function changeInchForDesc(fromObject,toObject,flag){
				if(fromObject.value==''){
					$(toObject).value='';
					return;
				}else{
					if(isNaN(fromObject.value)){
						fromObject.value='';
					}
				}
				if(flag==0){
					$(toObject).value=(fromObject.value/2.54).toFixed("4");
				}else{
					$(toObject).value=round((fromObject.value*2.54),2);
				}
				$('eleInchDesc').value = round($('boxLInch').value,2)+"*"+round($('boxWInch').value,2)+"*"+round($('boxHInch').value,2);
				$('eleSizeDesc').value = round($('boxL').value,2)+"*"+round($('boxW').value,2)+"*"+round($('boxH').value,2);
			}
		</script>
	</head>
	<%
		String id = request.getParameter("id");
	%>
	<body onload="loadBox()">
		<div id="box" style="display: block; width: 90%;">
			<!-- 尺寸表单开始 -->
			<form name="cotBoxForm" id="cotBoxForm"
				action="/budgetmanager/executeRecord.do">
				<div id="box" style="width: 100%;">
					<!-- 尺寸表单开始 -->
					<div style="width: 100%;">
						<label class="label_11">
							Product Size CM
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxL" id="boxL"
							maxlength="8" onblur="changeInchForDesc(this,'boxLInch',0);" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxW" id="boxW"
							maxlength="8" onblur="changeInchForDesc(this,'boxWInch',0);" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxH" id="boxH"
							maxlength="8" onblur="changeInchForDesc(this,'boxHInch',0);" />
						<label class="label_11" style="margin-left: 27">
							Product Size INCH
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxLInch" id="boxLInch"
							maxlength="8" onblur="changeInchForDesc(this,'boxL',1);" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxWInch" id="boxWInch"
							maxlength="8" onblur="changeInchForDesc(this,'boxW',1);" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxHInch" id="boxHInch"
							maxlength="8" onblur="changeInchForDesc(this,'boxH',1);" />
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							中文规格：
						</label>
						<input type="text" name="eleSizeDesc" maxlength="30"
							style="margin-left: 10px; margin-top: 5px; float: left; width: 32.2%;"
							id="eleSizeDesc" />
						<label class="label_11" style="margin-left: 27">
							Size Description：
						</label>
						<input type="text" name="eleInchDesc" maxlength="30"
							style="margin-left: 8px; margin-top: 5px; float: left; width: 32.2%;"
							id="eleInchDesc" />
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							Inner Box(CM)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxIbL" id="boxIbL"
							maxlength="8" onblur="changeInch(this,'boxIbLInch',0);" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxIbW" id="boxIbW"
							maxlength="8" onblur="changeInch(this,'boxIbWInch',0);" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxIbH" id="boxIbH"
							maxlength="8" onblur="changeInch(this,'boxIbHInch',0);" />
						<label class="label_11" style="margin-left: 27">
							Inner box(INCH)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxIbLInch" maxlength="8"
							onblur="changeInch(this,'boxIbL',1);" id="boxIbLInch" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxIbWInch" maxlength="8"
							onblur="changeInch(this,'boxIbW',1);" id="boxIbWInch" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxIbHInch" maxlength="8"
							onblur="changeInch(this,'boxIbH',1);" id="boxIbHInch" />
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							Middle box(CM)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxMbL" id="boxMbL"
							maxlength="8" onblur="changeInch(this,'boxMbLInch',0);" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxMbW" id="boxMbW"
							maxlength="8" onblur="changeInch(this,'boxMbWInch',0);" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxMbH" id="boxMbH"
							maxlength="8" onblur="changeInch(this,'boxMbHInch',0);" />
						<label class="label_11" style="margin-left: 27">
							Middle box(INCH)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxMbLInch" maxlength="8"
							onblur="changeInch(this,'boxMbL',1);" id="boxMbLInch" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxMbWInch" maxlength="8"
							onblur="changeInch(this,'boxMbW',1);" id="boxMbWInch" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxMbHInch" maxlength="8"
							onblur="changeInch(this,'boxMbH',1);" id="boxMbHInch" />
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							outer carton(CM)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxObL" id="boxObL"
							maxlength="8" onblur="changeInch(this,'boxObLInch',0);" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxObW" id="boxObW"
							maxlength="8" onblur="changeInch(this,'boxObWInch',0);" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxObH" id="boxObH"
							maxlength="8" onblur="changeInch(this,'boxObHInch',0);" />
						<label class="label_11" style="margin-left: 27">
							outer carton(INCH)
						</label>
						<label class="label_4">
							Length：
						</label>
						<input class="input_7" type="text" name="boxObLInch" maxlength="8"
							onblur="changeInch(this,'boxObL',1);" id="boxObLInch" />
						<label class="label_4">
							Width：
						</label>
						<input class="input_7" type="text" name="boxObWInch" maxlength="8"
							onblur="changeInch(this,'boxObW',1);" id="boxObWInch" />
						<label class="label_4">
							Height：
						</label>
						<input class="input_7" type="text" name="boxObHInch" maxlength="8"
							onblur="changeInch(this,'boxObH',1);" id="boxObHInch" />
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							口径描述：
						</label>
						<input class="input_20" type="text" name="boxTDesc" maxlength="50"
							id="boxTDesc" />
						<label class="label_11">
							底径描述：
						</label>
						<input class="input_20" type="text" name="boxBDesc" maxlength="50"
							id="boxBDesc" />
						<label class="label_7" style="margin-left: 20">
							把 高：
						</label>
						<input class="input_17" type="text" name="boxHandleH"
							id="boxHandleH" />
						<label class="label_4">
							CM
						</label>
					</div>
					<div style="width: 100%;">
						<label class="label_11">
							包装类型：
						</label>
						<select class="select_13" id="boxTypeId" name="boxTypeId"
							onchange="showType(this.value);">
							<option value="">
								请选择
							</option>
						</select>
						<label class="label_7" style="margin-left: 28">
							Packing：
						</label>
						<input class="input_4" type="text" name="boxIbCount" maxlength="8"
							id="boxIbCount" />
						<label class="label_1">
							/
						</label>
						<select class="input_7" name="boxIbType" id="boxIbType" >
						</select>	
						<label class="label_7" style="margin-left: 28">
							Packing：
						</label>
						<input class="input_4" type="text" name="boxMbCount" maxlength="8"
							id="boxMbCount" />
						<label class="label_1">
							/
						</label>
						<select class="input_7" name="boxMbType" id="boxMbType" >
						</select>
						<label class="label_7" style="margin-left: 28">
							Packing：
						</label>
						<input class="input_4" type="text" name="boxObCount" maxlength="8"
							id="boxObCount" />
						<label class="label_1">
							/
						</label>
						<select class="input_7" name="boxObType" id="boxObType" >
						</select>
					</div>
					<div style="width: 100%">
						<label class="label_11">
							N.W./G.W：
						</label>
						<input class="input_7" type="text" name="boxNetWeigth"
							maxlength="8" id="boxNetWeigth" />
						<label class="label_1">
							/
						</label>
						<input class="input_7" type="text" name="boxGrossWeigth"
							maxlength="8" id="boxGrossWeigth" />
						<label class="label_1">
							/KG
						</label>
						<label class="label_11">
							Weight：
						</label>
						<input class="input_7" type="text" name="boxWeigth" id="boxWeigth"
							maxlength="10" />
						<label class="label_1">
							/KG
						</label>
						<div>
							<label class="label_11">
								CBM：
							</label>
							<input class="input_7" type="text" name="boxCbm" id="boxCbm"
								maxlength="10" onblur="change(this,'boxCuft',0);" />
							<label class="label_4">
								CU'FT
							</label>
							<input class="input_7" type="text" name="boxCuft" id="boxCuft"
								maxlength="10" onblur="change(this,'boxCbm',1);" />
						</div>
					</div>
					<div style="width: 100%">
						<label class="label_11">
							Packing Description：
						</label>
						<textarea rows="3" name="boxRemark" id="boxRemark"
							class="select_80"
							onKeyDown="if(this.value.length>200)this.value=this.value.substr(0,200)"
							onKeyUp="if(this.value.length>200)this.value=this.value.substr(0,200)"></textarea>
					</div>
					<div id="boxBtn" style="margin-top: 20px;" align="center">
						<button onclick="modBox()" class="confirmBtn">
							Edit
						</button>
						<button class="cancelBtn"
							onclick="javascript:$('box').style.display='none';">
							Close
						</button>
					</div>
					<!-- 尺寸表单结束 -->
				</div>
			</form>
			<!-- 主样品编号隐藏域 -->
			<input type="hidden" name="eId" id="eId" value="<%=id%>" />
		</div>
	</body>
</html>