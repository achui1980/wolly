<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
	<head>
		<base href="<%=basePath%>">

		<title>同步</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入左右移下拉框脚本 -->
		<script type="text/javascript"
			src="<%=path%>/common/js/leftRightSelect.js"></script>
		<script type="text/javascript">
			function downLoad(){
				//是否同步图片
				var isPic=$('isPic').checked;
				//判断是否有选择字段
				if($('eleSelect').options.length==0 && $('boxSelect').options.length==0 && $('otherSelect').options.length==0 && isPic==false){
					Ext.Msg.alert("Message",'You do not choose to import Excel data colum！');
					return;
				}
				//选中的样品字段
				var eleStr = '';
				for(var i=0;i<$('eleSelect').options.length;i++){
					eleStr+=$('eleSelect').options[i].value+",";
				}
				//选中的包装信息字段
				var boxStr = '';
				for(var i=0;i<$('boxSelect').options.length;i++){
					boxStr+=$('boxSelect').options[i].value+",";
				}
				//选中的其他信息
				var otherStr = '';
				for(var i=0;i<$('otherSelect').options.length;i++){
					otherStr+=$('otherSelect').options[i].value+",";
				}
				var flag = $('flag').value;
				if(flag==1){
					//样品同步到报价或订单
					parent.updateToPrice(eleStr,boxStr,otherStr,isPic);
				}else if(flag == 2){
					//采购同步到订单
					parent.updateToOrder(eleStr,boxStr,otherStr,isPic);
				}else{
					//报价.订单同步到样品
					parent.updateToEle(eleStr,boxStr,otherStr,isPic);
				}
			}
			//关闭
			function closeWin(){
				parent.hideRightMenu();
			}
		</script>
	</head>
<%
	String flag = request.getParameter("flag");
%>
	<body style="background-color: #dfe8f6;">
		<div
			style="float: left; width: 46%; margin-left: 4%; margin-top: 5px;">
			<label style="color:blue;">
				Base Information：
			</label>
			<form name="ele" onsubmit="return false">
				<table>
					<tr>
						<td>
							<select name=SrcSelect size=12
								style="height: 250px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.ele.SrcSelect,document.ele.ObjSelect)">
								<!--<option value="eleTypeidLv1">
									材质
								</option>-->
								<option value="eleUnit">
									Unit
								</option>
								<!--<option value="eleFlag">
									组成方式
								</option>-->
								<option value="eleUnitNum">
									Pieces
								</option>
								<option value="eleNameEn">
									Description
								</option>
								<option value="factoryId">
									Supplier
								</option>
								<option value="factoryNo">
									Supplier Article No.
								</option>
								<!--<option value="eleName">
									中文品名
								</option>-->
								<option value="eleFrom">
									Source
								</option>
								<option value="eleCol">
									Color
								</option>
								<option value="eleProTime">
									Date
								</option>
								<option value="eleForPerson">
									Designer
								</option>
								<option value="eleGrade">
									Level
								</option>
								<option value="eleMod">
									MOQ
								</option>
								<!--<option value="eleTypeidLv2">
									产品分类
								</option>
								<option value="eleTypenameLv2">
									所属年份
								</option>
								--><!--<option value="eleHsid">
									海关编码
								</option>-->
								<option value="boxWeigth">
									Weight(g)
								</option>
								<!--<option value="cube">
									容积
								</option>-->
								<option value="priceFac">
									Purchage Price
								</option>
								<option value="priceOut">
									Sale Prices
								</option>
								<!--<option value="tuiLv">
									退税率
								</option>
								--><option value="liRun">
									Profit(%)
								</option>
								<option value="barcode">
									Barcode
								</option>
								<!--<option value="eleDesc">
									产品描述
								</option>-->
								<option value="eleRemark">
									Remarks
								</option>
							</select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.ele.SrcSelect,document.ele.ObjSelect)">
								>>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.ele.SrcSelect,document.ele.ObjSelect)">
								>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.ele.ObjSelect,document.ele.SrcSelect)">
								<
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRightAll(document.ele.ObjSelect,document.ele.SrcSelect)">
								<<
							</button>
						</td>
						<td>
							<select name=ObjSelect size=6 id="eleSelect"
								style="height: 250px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.ele.ObjSelect,document.ele.SrcSelect)">
							</select>
						</td>
						<td width="30px">
							<br>
							<br>
							<button style="height: 30px;" onclick="moveUp(document.ele.ObjSelect)">
								↑
							</button>
							<br>
							<br>
							<button style="height: 30px;margin-top: 8px;" onclick="moveDown(document.ele.ObjSelect)">
								↓
							</button>
							<br>
							<br>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div
			style="float: left; width: 46%; margin-left: 3%; margin-top: 5px;">
			<label style="color:blue;">
				Size Information：
			</label>
			<form name="box" onsubmit="return false">
				<table>
					<tr>
						<td>
							<select name=SrcSelect size=15
								style="height: 250px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.box.SrcSelect,document.box.ObjSelect)">
								<option value="boxL">
									Product_L
								</option>
								<option value="boxW">
									Product_W
								</option>
								<option value="boxH">
									Product_H
								</option>
								<option value="boxTypeId">
									Packing Way
								</option>
								<option value="boxPbL">
									Packing_L
								</option>
								<option value="boxPbW">
									Packing_W
								</option>
								<option value="boxPbH">
									Packing_H
								</option>
								<option value="boxPbCount">
									Product Box
								</option>
								<option value="boxPbTypeId">
									Product Material Type
								</option>
								<!--<option value="boxPbPrice">
									产品包装价格
								</option>

								--><option value="boxIbL">
									Inner_L
								</option>
								<option value="boxIbW">
									Inner_W
								</option>
								<option value="boxIbH">
									Inner_H
								</option>
								<option value="boxIbCount">
									Inner Box
								</option>
								<option value="boxIbTypeId">
									Inner Material Type
								</option>
								<!--<option value="boxIbPrice">
									内包装价格
								</option>
								--><!--<option value="boxMbL">
									中盒包装长
								</option>
								<option value="boxMbW">
									中盒包装宽
								</option>
								<option value="boxMbH">
									中盒包装高
								</option>
								--><!--<option value="boxMbCount">
									中包装数
								</option>
								<option value="boxMbTypeId">
									中包装类型
								</option>
								<option value="boxMbPrice">
									中包装价格
								</option>
								--><option value="boxObL">
									Outer_L
								</option>
								<option value="boxObW">
									Outer_W
								</option>
								<option value="boxObH">
									Outer_H
								</option>
								<option value="boxObCount">
									Outer Box
								</option>
								<option value="boxObTypeId">
									Outer Material Type
								</option>
								<!--<option value="boxObPrice">
									外包装价格
								</option>
								<option value="inputGridTypeId">
									插格类型
								</option>
								<option value="inputGridPrice">
									插格价格
								</option>
								<option value="putL">
									摆放方式长
								</option>
								<option value="putW">
									摆放方式宽
								</option>
								<option value="putH">
									摆放方式高
								</option>
								--><!--<option value="eleSizeDesc">
									中文规格
								</option>
								--><option value="eleInchDesc">
									Size Description
								</option>
								<!--<option value="boxTDesc">
									口径描述
								</option>
								<option value="boxBDesc">
									底径描述
								</option>
								<option value="packingPrice">
									单个价格
								</option>
								--><option value="boxCbm">
									CBM/CU'FT
								</option>
								<option value="boxNetWeigth">
									N.W/G.W
								</option>
								<option value="boxRemarkCn">
									Packing Description
								</option>
								<!--<option value="boxRemark">
									英文包装
								</option>
							--></select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.box.SrcSelect,document.box.ObjSelect)">
								>>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.box.SrcSelect,document.box.ObjSelect)">
								>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.box.ObjSelect,document.box.SrcSelect)">
								<
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRightAll(document.box.ObjSelect,document.box.SrcSelect)">
								<<
							</button>
						</td>
						<td>
							<select name=ObjSelect size=6 id="boxSelect"
								style="height: 250px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.box.ObjSelect,document.box.SrcSelect)">
							</select>
						</td>
						<td width="30px">
							<br>
							<br>
							<button style="height: 30px;" onclick="moveUp(document.box.ObjSelect)">
								↑
							</button>
							<br>
							<br>
							<button style="height: 30px;margin-top: 8px;" onclick="moveDown(document.box.ObjSelect)">
								↓
							</button>
							<br>
							<br>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="float: left; width: 46%; margin-left: 4%;">
			<label style="color:blue;">
				Other Information：
			</label>
			<form name="price" onsubmit="return false">
				<table>
					<tr>
						<td>
							<select name=SrcSelect size=6 class="ecv_autoSizeDropDown"
								style="height: 150px; width: 120px;" multiple
								ondblclick="moveLeftOrRight(document.price.SrcSelect,document.price.ObjSelect)">
								<option value="box20Count">
									20' Container
								</option>
								<option value="box40Count">
									40' Container
								</option>
								<option value="box40hqCount">
									40 HC Container
								</option>
								<option value="box45Count">
									45' Container
								</option>
							</select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.price.SrcSelect,document.price.ObjSelect)">
								>>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.price.SrcSelect,document.price.ObjSelect)">
								>
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.price.ObjSelect,document.price.SrcSelect)">
								<
							</button>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRightAll(document.price.ObjSelect,document.price.SrcSelect)">
								<<
							</button>
						</td>
						<td>
							<select name=ObjSelect size=6 class="ecv_autoSizeDropDown"
								id="otherSelect" style="height: 150px; width: 120px;" multiple
								ondblclick="moveLeftOrRight(document.price.ObjSelect,document.price.SrcSelect)">
							</select>
						</td>
						<td width="30px">
							<br>
							<br>
							<button style="height: 30px;" onclick="moveUp(document.price.ObjSelect)">
								↑
							</button>
							<br>
							<br>
							<button style="height: 30px;margin-top: 8px;" onclick="moveDown(document.price.ObjSelect)">
								↓
							</button>
							<br>
							<br>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<!----------------------- 保存取消按钮 ---------------------------------->
		<div style="float: left; width: 46%; margin-top: 40px;" align="center">
			<div style="width: 100%;">
				<input type="checkbox" id="isPic" checked="checked" />
				&nbsp;&nbsp;Is synchronized picture
			</div>
			<div style="width: 100%; margin-top: 10px;">
				<a onclick="downLoad()" style="cursor: hand;"><img id="addBtn"
						src="<%=path %>/common/images/_save.gif" border="0" height="21px"
						width="61px"> </a>&nbsp;
				<a onclick="closeWin()" style="cursor: hand;"><img
						src="<%=path %>/common/images/_cancel.gif" border="0"
						height="21px" width="61px"> </a>
			</div>
		</div>
		<input type="hidden" id="flag" name="flag" value="<%=flag %>"/>
	</body>
</html>
