<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
  <%
	String webapp=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type='text/javascript' src='<%=webapp %>/dwr/engine.js'></script>
        <script type="text/javascript" src="<%=webapp %>/dwr/util.js"></script>
		<script type='text/javascript' src='<%=webapp %>/dwr/interface/cotCalculationService.js'></script>
		<style type="text/css">
			body {font-family: helvetica}
			p {font-size: 12pt}
			.red {color: red}
			.blue {color: blue}
		</style>
		<script type="text/javascript">
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
			 
		function calButton(e) {
			 //parent.setExpessionInForm(e);
			 parent.setExpressionOutForm(e);
			 //parent.setCheckCalculation(e);
		}
			
		function setSelValue() {
		     var selectObj=document.getElementById("baseField"); 
			 //下拉框选中项的文本 
			 var selectOptionText= selectObj.options[selectObj.selectedIndex].innerText; 
			 //下拉框选中项的值 
			 var selectOptionValue=selectObj.options[selectObj.selectedIndex].value;
			 //parent.setExpessionInForm('\#{'+selectOptionValue+'}');
			 parent.setExpressionOutForm(selectOptionText);
			 //parent.setCheckCalculation("0.00001");
		}	 
		var simple = null;
		var comp = null;//复合字段缓存
		function getOptionVal()
		{
			if(simple != null) return ;
			var sel = document.getElementById("baseField"); 
			var simple = new Object();
			for(var i=0; i<sel.options.length; i++)
			{
				var text = sel.options[i].text;
				var value = sel.options[i].value;
				simple[text] = value;
			}
			sel = document.getElementById("calculation");
			comp = new Object();
			for(var i=0; i<sel.options.length; i++)
			{
				var text = sel.options[i].text;
				var value = sel.options[i].value;
				comp[text] = value;
			}
			//覆盖父页面的数据
			parent.simple = simple;
			parent.comp = comp
			//printObject(object);
		}
		function setSelCalculation() {
		     var selectObj=document.getElementById("calculation"); 
			 //下拉框选中项的文本 
			 var selectOptionText= selectObj.options[selectObj.selectedIndex].innerText; 
			 //下拉框选中项的值 
			 var selectOptionValue=selectObj.options[selectObj.selectedIndex].value;
			 //parent.setExpessionInForm('('+selectOptionValue+')');
			 parent.setExpressionOutForm('('+selectOptionText+')');
			 //parent.setCheckCalculation("0.00001");
		}
		
		function clearParentForm() {
			 parent.clearForm();
		} 
		 	 
		function closeWindow() {
			 parent.closeCalculatorDiv();
		} 	
		
		//加载复合字段数据
		function bindSel()
		{
			
			cotCalculationService.getCalculationList(function(res){
				bindSelect('calculation',res,'expessionIn','calName',false);
				getOptionVal();
			})
		}	 
		</script>
	</head>
	<body onload="bindSel()">
		<center>
			<form name="calculator">
				<table bgcolor="#aaaaaa" width=220>
					<tr>
						<td>
							<table bgcolor="#cccccc" border=1>
							    <tr>
									<td align="center">
										<label>
											Basic character 
										</label>
								 	</td>
								 	<td align="center">
										<label >
											Basic field
										</label>
									</td>
								  	<td align="center">
										<label >
											Composite field
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<table border=0 cellpadding=0>
										    <tr>
												<td>
													<table width="100%" border=0>
													    <tr>
													        <td>
																<input type="button" name="calc1" class="blue" 
																value="  1  " onClick="calButton('1'); return false;">
															</td>
															<td>
																<input type="button" name="calc2" class="blue" 
																value="  2  " onClick="calButton('2'); return false;">
															</td>
															<td>
																<input type="button" name="calc3" class="blue" 
																value="  3  " onClick="calButton('3'); return false;">
															</td>
															<td>
																<input type="button" name="plus" class="red" 
																value="  +  " onClick="calButton('+'); return false;">
															</td>
															<td>
																<input type="button" name="minus" class="red" 
																value="  -  " onClick="calButton('-'); return false;">
															</td>
															 
														</tr>
														<tr>
															<td>
																<input type="button" name="calc4" class="blue" 
																value="  4  " onClick="calButton('4'); return false;">
															</td>
															<td>
																<input type="button" name="calc5" class="blue" 
																value="  5  " onClick="calButton('5'); return false;">
															</td>
															<td>
																<input type="button" name="calc6" class="blue" 
																value="  6  " onClick="calButton('6'); return false;">
															</td>
															<td>
																<input type="button" name="multiply" class="red" 
																value="  *  " onClick="calButton('*'); return false;">
															</td>
															<td>
																<input type="button" name="divide" class="red" 
																value="  /  " onClick="calButton('/'); return false;">
															</td>
															 
														</tr>
														<tr>
															<td>
																<input type="button" name="calc7" class="blue" 
																value="  7  " onClick="calButton('7'); return false;">
															</td>
															<td>
																<input type="button" name="calc8" class="blue" 
																value="  8  " onClick="calButton('8'); return false;">
															</td>
															<td>
																<input type="button" name="calc9" class="blue" 
																value="  9  " onClick="calButton('9'); return false;">
															</td>
															<td>
																<input type="button" name="minLeft" class="red" 
																value="  (  " onClick="calButton('('); return false;">
															</td>
															<td>
																<input type="button" name="minRight" class="red" 
																value="  )  " onClick="calButton(')'); return false;">
															</td>
														</tr>
														<tr>
															<td>
																<input type="button" name="calc0" class="blue" 
																value="  0  " onClick="calButton('0'); return false;">
															</td>
															<td>
																<input type="button" name="norLeft" class="red" 
																value="  [  " onClick="calButton('['); return false;">
															</td>
															<td>
																<input type="button" name="norRight" class="red" 
																value="  ]  " onClick="calButton(']'); return false;">
															</td>
															<td>
																<input type="button" name="maxLeft" class="red" 
																value="  {  " onClick="calButton('{'); return false;">
															</td>
															<td>
																<input type="button" name="maxRight" class="red" 
																value="  }  " onClick="calButton('}'); return false;">
															</td>
														</tr>
														<tr>
															<td>
																<input type="button" name="maxLeft" class="red" 
																value="  .  " onClick="calButton('.'); return false;">
															</td>
															<td colspan=2>
																<input type="button"  class="blue" style="width: 70px"
																value="clear" onClick="clearParentForm(); return false;">
															</td>
															<td colspan=2>
																<input type="button" class="blue" style="width: 70px"
																value="close" onClick="javascript:parent.closeCal();">
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									<td>
										<table border=0 cellpadding=0>
											<tr>
												<td>
													<table width="100%" border=0>
														<tr>
															<td>
																<select  id="baseField" name="baseField" size="10" 
																     onclick="setSelValue()" style="width: 150px">
																	<!--<option value="rmb"> Product value</option>
																	<option value="priceRate"> Exchange Rate </option>
																	<option value="priceProfit"> Margin</option>
																	<option value="priceFob"> FOB cost</option>
																	<option value="cbm"> Sample CBM </option>
																	<option value="priceCharge"> Freight Cost </option>
																	<option value="boxObCount"> Packing ratio </option>
																	<option value="cube"> Cabinet size </option>
																	<option value="insureAddRate"> Insurance bonus rates </option>
																	<option value="insureRate"> Premium rate </option>
																	<option value="tuiLv"> Tax rate </option>
																	<option value="price"> Latest price </option>
																	<option value="facSum"> 采购总金额</option>
																	<option value="tax"> 进口税 </option>
																	-->
																	<option value="rmb">生产价</option>
																	<option value="priceRate">汇率</option>
																	<option value="priceProfit">利润率</option>
																	<option value="priceFob"> FOB费用 </option>
																	<option value="cbm"> 样品CBM </option>
																	<option value="priceCharge"> 运费 </option>
																	<option value="boxCount"> 样品数量 </option>
																	<option value="boxObCount"> 装箱率 </option>
																	<option value="cube"> 柜体积 </option>
																	<option value="insureAddRate"> 保险加成率 </option>
																	<option value="insureRate"> 保险费率 </option>
																	<option value="tuiLv"> 退税率 </option>
																	<option value="price"> 最新价 </option>
																	<option value="facSum"> 采购总金额</option>
																	<option value="importTax"> 进口税 </option>
																	<option value="fobSum"> FO_B价总和 </option>
																</select>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									<td>
										<table border=0 cellpadding=0>
											<tr>
												<td>
													<table width="100%" border=0>
														<tr>
															<td>
																<select  id="calculation" name="calculation" 
																	onclick="setSelCalculation()" size="10" style="width: 125px">
																</select>
															</td>
															 
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</center>
	</body>
</html>
