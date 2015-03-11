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
			 
		function calButton(e) {
			 parent.setExpessionInForm(e);
			 parent.setExpressionOutForm(e);
			 parent.setCheckCalculation(e);
		}
			
		function setSelValue() {
		     var selectObj=document.getElementById("baseField"); 
			 //下拉框选中项的文本 
			 var selectOptionText= selectObj.options[selectObj.selectedIndex].innerText; 
			 //下拉框选中项的值 
			 var selectOptionValue=selectObj.options[selectObj.selectedIndex].value;
			 parent.setExpessionInForm('\#{'+selectOptionValue+'}');
			 parent.setExpressionOutForm(selectOptionText);
			 parent.setCheckCalculation("0.00001");
		}	 
		 
		function setSelCalculation() {
		     var selectObj=document.getElementById("calculation"); 
			 //下拉框选中项的文本 
			 var selectOptionText= selectObj.options[selectObj.selectedIndex].innerText; 
			 //下拉框选中项的值 
			 var selectOptionValue=selectObj.options[selectObj.selectedIndex].value;
			 parent.setExpessionInForm('('+selectOptionValue+')');
			 parent.setExpressionOutForm('('+selectOptionText+')');
			 parent.setCheckCalculation("0.00001");
		}
		
		function clearParentForm() {
			 parent.clearForm();
		} 
		 	 
		function closeWindow() {
			 parent.Hide();
		} 	
		
		//加载复合字段数据
		function bindSel()
		{
			cotCalculationService.getCalculationList(function(res){
				bindSelect('calculation',res,'expessionIn','calName',false);
				
			})
		}
		
		var simple = null;
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
			//覆盖父页面的数据
			parent.simple = simple;
		}	 
		</script>
	</head>
	<body onload="getOptionVal()">
		<center>
			<form name="calculator">
				<table bgcolor="#aaaaaa" width=220>
					<tr>
						<td>
							<table bgcolor="#cccccc" border=1>
							    <tr>
									<td align="center">
										<label>
											基本字符 
										</label>
								 	</td>
								 	<td align="center">
										<label >
											基本字段
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
																<input type="button"  class="blue" style="width: 68px"
																value="清除" onClick="clearParentForm(); return false;">
															</td>
															<td colspan=2>
																<input type="button"  class="blue" style="width: 68px"
																value="关闭" onClick="javascript:parent.closeCal();">
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
																    <option value="boxL"> 产品长</option>
																	<option value="boxW"> 产品宽</option>
																	<option value="boxH"> 产品高</option>
																	<option value="boxPbL"> 产品包装长</option>
																	<option value="boxPbW"> 产品包装宽</option>
																	<option value="boxPbH"> 产品包装高</option>
																	<option value="boxIbL">内盒长</option>
																	<option value="boxIbW">内盒宽</option>
																	<option value="boxIbH">内盒高</option>
																	<option value="boxMbL">中盒长</option>
																	<option value="boxMbW">中盒宽</option>
																	<option value="boxMbH">中盒高</option>
																	<option value="boxObL">外箱长</option>
																	<option value="boxObW">外箱宽</option>
																	<option value="boxObH">外箱高</option>
																	<option value="putL">摆放长</option>
																	<option value="putW">摆放宽</option>
																	<option value="putH">摆放高</option>
																	<option value="materialPrice">材料单价</option>
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
