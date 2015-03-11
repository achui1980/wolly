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

		<title>Customize Sample template file</title>
		<!-- 导入公共js和css -->
		<jsp:include page="/extcommon.jsp"></jsp:include>
		<!-- 导入左右移下拉框脚本 -->
		<script type="text/javascript"
			src="<%=path%>/common/js/leftRightSelect.js"></script>
		<script type="text/javascript">
			function downLoad(){
				//判断是否有选择字段
				if($('eleSelect').options.length==0 && $('boxSelect').options.length==0 && $('priceSelect').options.length==0){
					Ext.Msg.alert("提示信息",'您还没有选择要导入的Excel数据列！');
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
				//选中的报价字段
				var priceStr = '';
				for(var i=0;i<$('priceSelect').options.length;i++){
					priceStr+=$('priceSelect').options[i].value+",";
				}
				document.location.replace("../../downloadCustom.action?eleStr="+eleStr+"&boxStr="+boxStr+"&priceStr="+priceStr);
			}
			//关闭
			function closeWin(){
				window.opener=null;
				window.close();
			}
		</script>
	</head>

	<body style="background-color: #dfe8f6;">
		<div align="center" style="margin-top: 5px;">
			<h3>
				Please choose the columns to import
			</h3>
		</div>
		<div
			style="float: left; width: 46%; margin-left: 4%; margin-top: 5px;">
			<label style="color:blue;">
				Sample Import Data：
			</label>
			<form name="ele" onsubmit="return false">
				<table>
					<tr>
						<td>
							<select name=SrcSelect size=12
								style="height: 250px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.ele.SrcSelect,document.ele.ObjSelect)">
								<!--<option value="ELE_NAME">
									中文品名
								</option>-->
								<option value="ELE_NAME_EN">
									Description
								</option>
								<option value="FACTORY_NO">
									Supplier Article No.
								</option>
								<option value="SORT_NO">
									Sort
								</option>
								<option value="CUST_NO">
									Cust.No.
								</option>
								<!--<option value="ELE_FLAG">
									组成方式
								</option>-->
								<option value="ELE_UNITNUM">
									Pieces
								</option>
								<!--<option value="TAO_UNIT">
									报价单位
								</option>-->
								<option value="ELE_PARENT">
									Parent Article No.
								</option>
								<option value="ELE_FROM">
									Source
								</option>
								<!--<option value="ELE_TYPENAME_LV1">
									材质
								</option>-->
								<!--<option value="ELE_TYPEID_LV2">
									产品分类
								</option>
								<option value="ELE_GRADE">
									Level
								</option>-->
								<!--<option value="ELE_TYPENAME_LV2">
									所属年份
								</option>
								--><option value="ELE_PRO_TIME">
									Date
								</option>
								<!--<option value="ELE_FOR_PERSON">
									Designer
								</option>-->
								<option value="ELE_MOD">
									MOQ
								</option>
								<option value="LI_RUN">
									Profit(%)
								</option>
								<option value="ELE_COL">
									Color
								</option>
								<option value="BARCODE">
									Barcode
								</option>
								<option value="ELE_UNIT">
									Unit
								</option>
								<!--<option value="HS_ID">
									海关编码
								</option>
								--><!--<option value="TUI_LV">
									退税率
								</option>-->
								<option value="SHORT_NAME">
									Supplier
								</option>
								<!--<option value="ELE_SIZE_DESC">
									中文规格
								</option>-->
								<option value="ELE_INCH_DESC">
									Size Description
								</option>
								<option value="ELE_REMARK">
									Remarks
								</option>
							</select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.ele.SrcSelect,document.ele.ObjSelect)">
								>>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.ele.SrcSelect,document.ele.ObjSelect)">
								>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.ele.ObjSelect,document.ele.SrcSelect)">
								<
							</button>
							<br>
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
								<!--<option value="BOX_L_INCH">
									样品英寸长
								</option>
								<option value="BOX_W_INCH">
									样品英寸宽
								</option>
								<option value="BOX_H_INCH">
									样品英寸高
								</option>
								<option value="cube">
									容积
								</option>
								--><option value="BOX_L">
									Product_L
								</option>
								<option value="BOX_W">
									Product_W
								</option>
								<option value="BOX_H">
									Product_H
								</option>
								<!--<option value="BOX_HANDLEH">
									样品把高
								</option>
								<option value="ELE_DESC">
									产品描述
								</option>
								<option value="BOX_TDESC">
									口径描述
								</option>
								<option value="BOX_BDESC">
									底径描述
								</option>
								--><option value="BOX_PB_L">
									Packing_L
								</option>
								<option value="BOX_PB_W">
									Packing_W
								</option>
								<option value="BOX_PB_H">
									Packing_H
								</option>
								<option value="BOX_IB_L">
									Inner_L
								</option>
								<option value="BOX_IB_W">
									Inner_W
								</option>
								<option value="BOX_IB_H">
									Inner_H
								</option>
								<!--<option value="BOX_MB_L">
									中盒长
								</option>
								<option value="BOX_MB_W">
									中盒宽
								</option>
								<option value="BOX_MB_H">
									中盒高
								</option>
								--><option value="BOX_CBM">
									CBM
								</option>
								<option value="BOX_OB_L">
									Outer_L
								</option>
								<option value="BOX_OB_W">
									Outer_W
								</option>
								<option value="BOX_OB_H">
									Outer_H
								</option>
								<option value="BOX_WEIGTH">
									Weight
								</option>
								<option value="BOX_GROSS_WEIGTH">
									G.W
								</option>
								<option value="BOX_NET_WEIGTH">
									N.W
								</option>
								<option value="BOX_PB_COUNT">
									Product Box
								</option>
								<option value="BOX_IB_COUNT">
									Inner Box
								</option>
								<!--<option value="BOX_MB_COUNT">
									中盒装数
								</option>-->
								<option value="BOX_OB_COUNT">
									Outer Box
								</option>
								<option value="BOX_TYPE_ID">
									Packing Way
								</option>
								<!--<option value="BOX_REMARK">
									英文包装
								</option>-->
								<option value="BOX_REMARK_CN">
									Packing Description
								</option>
							</select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.box.SrcSelect,document.box.ObjSelect)">
								>>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.box.SrcSelect,document.box.ObjSelect)">
								>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.box.ObjSelect,document.box.SrcSelect)">
								<
							</button>
							<br>
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
		<div style="width: 96%; margin-left: 4%;">
			<label style="color:blue;">
				Price Information：
			</label>
			<form name="price" onsubmit="return false">
				<table>
					<tr>
						<td>
							<select name=SrcSelect size=6
								style="height: 150px; width: 120px;" multiple="multiple"
								ondblclick="moveLeftOrRight(document.price.SrcSelect,document.price.ObjSelect)">
								<option value="PRICE_UINT">
									Purchage Currency
								</option>
								<option value="PRICE_FAC">
									Purchage Price
								</option>
								<option value="PRICE_UNIT">
									Sale Currency
								</option>
								<option value="PRICE_OUT">
									Sale Prices
								</option>
								<option value="BOX_COUNT">
									Quantity
								</option>
								<option value="CONTAINER_COUNT">
									Cartons
								</option>
							</select>
						</td>
						<td width="30px">
							<button style="width: 60px;"
								onclick="moveLeftOrRightAll(document.price.SrcSelect,document.price.ObjSelect)">
								>>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.price.SrcSelect,document.price.ObjSelect)">
								>
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRight(document.price.ObjSelect,document.price.SrcSelect)">
								<
							</button>
							<br>
							<button style="width: 60px;margin-top: 8px;"
								onclick="moveLeftOrRightAll(document.price.ObjSelect,document.price.SrcSelect)">
								<<
							</button>
						</td>
						<td>
							<select name=ObjSelect size=6 id="priceSelect"
								style="height: 150px; width: 120px;" multiple="multiple"
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
		<div style="width: 100%; margin-top: 10;" align="center">
			<a onclick="downLoad()" style="cursor: hand;"><img id="addBtn"
					src="<%=path %>/common/images/_save.gif" border="0" height="21px"
					width="61px"> </a>&nbsp;
			<a onclick="closeWin()" style="cursor: hand;"><img
					src="<%=path %>/common/images/_cancel.gif" border="0" height="21px"
					width="61px"> </a>
		</div>
	</body>
</html>
