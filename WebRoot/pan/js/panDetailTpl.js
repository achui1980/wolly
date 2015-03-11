QH.panAdd.thumbTemplate = new Ext.XTemplate(
		'<tpl for=".">',
		'<div id="ele_form_{id}"  class="thumb-wrap-form">',
		'<form method="post" name="detailForm_{id}">',
		'<table cellpadding=0 cellspacing=0 border=1 class="x-form-item" style="font-size: 14px;">',
		'<tr class="<tpl if="!state">grid-head</tpl><tpl if="state">grid-head-confirm</tpl>">' +
			'<td width="200px"><b><font color=blue>No.{#}</font>&nbsp;<input  type="checkbox" name="check_pan_ele" id="check_{id}" />&nbsp;Article name</b></td>' +
			'<td align=center width="120px">' +
				'<button type="button" style="width:50px;height:22px" class="x-btn-text" onclick="showUploadPanel({id})">Upload</button>&nbsp;' +
				'<button type="button" style="width:50px;height:22px" class="x-btn-text" onclick="showOtherPic({id})">More</button>' +
			'</td>' +
			'<td align=center width="80px"><b>Size</b></td>' +
			'<td align=center width="80px"><b>Quantity expected</b></td>' +
			'<td align=center width="80px"><b>pcs<br/>20’/40’</b></td>' +
			'<td align=center width="80px"><b>Coloring</b></td>' +
			'<td align=center width="80px"><b>Packaging</b></td>' +
			'<td align=center width="80px"><b>Certificate</b></td>' +
			'<td align=center width="80px"><b>Packing</b></td>' +
			'<td align=center width="100px"><b>Expected shipment date</b></td>' +
			'<td align=center width="220px"><b>Price</b></td>' +
			'<td align=center width="120px" onmouseover="showDetail({id},{#})"><b>Manufactorer1</b></td>' +
		'</tr>',
		'<tr>' +
			'<td align=center rowspan="2">' +
				'<input type="hidden" name="eleId" value="{eleId}"/>' +
				'<textarea style="height:80px;" class="thumb-textfield" name="eleNameEn" />{eleNameEn}</textarea><br/>' +
//				'<button type="button" style="width:60;height:22" class="x-btn-text" onclick="savePanEle({id})">Save</button>&nbsp;' +
				'<button type="button" style="width:60px;height:22px" class="x-btn-text" onclick="deleteOnePanEle({id})">Del</button>' +
			'</td>' +
			'<td align=center rowspan="2">',
				'<img id="pan_ele_img_{id}" src="showPicture.action?flag=pan&detailId={id}" onload="DrawImage(this,110,110)" onclick="showBigPicDiv(this)">',
			'</td>' +
			'<td rowspan="2"><textarea name="size" style="width:100px;height:110px;" class="thumb-textfield" type="text">{size}</textarea></td>' +
			'<td rowspan="2"><input style="width:80px;" class="thumb-textfield"  type="text" name="boxCount" value="{boxCount}"/></td>' +
			'<td rowspan="2"><input style="width:80px;" class="thumb-textfield"  type="text" name="pcs2040" value="{pcs2040}"/></td>' +
			'<td>' +
				'<div style="margin-left:10px;width:80px;"><input  type="checkbox" name="printed"  <tpl if="printed==1">checked</tpl>/>Printed</div>' +
				'<div style="margin-left:10px;width:80px;"><input  type="checkbox" name="dyed"  <tpl if="dyed==1">checked</tpl>/>Dyed</div>' +
				'<div style="margin-left:10px;width:80px;"><input  type="checkbox" name="yarnDyed"  <tpl if="yarnDyed==1">checked</tpl>/>Yarn Dyed</div>' +
				'<div style="margin-left:10px;width:80px;"><input  type="checkbox" name="others"  <tpl if="others==1">checked</tpl>/>Others</div>' +
			'</td>' +
			'<td>' +
				'<select name="packaging" style="width:120px;">' +
					'<option value="" <tpl if="packaging==\'\'">selected</tpl>>Choose' +
					'<option value="Hangtag" <tpl if="packaging==\'Hangtag\'">selected</tpl>>Hangtag' +
					'<option value="Belly band" <tpl if="packaging==\'Belly band\'">selected</tpl>>Belly band' +
					'<option value="Gift box" <tpl if="packaging==\'Gift box\'">selected</tpl>>Gift box' +
					'<option value="Inlay" <tpl if="packaging==\'Inlay\'">selected</tpl>>Inlay' +
					'<option value="Others" <tpl if="packaging==\'Others\'">selected</tpl>>Others' +
					'<option value="hangtag + polybag" <tpl if="packaging==\'hangtag + polybag\'">selected</tpl>>hangtag + polybag' +
					'<option value="bellyband + polybag" <tpl if="packaging==\'bellyband + polybag\'">selected</tpl>>bellyband + polybag' +
				'</select>'+
			'</td>' +
			'<td rowspan="2">' +
				'<select name="certificate" style="width:120px;">' +
					'<option value="" <tpl if="certificate==\'\'">selected</tpl>>Choose' +
					'<option value="BSCI" <tpl if="certificate==\'BSCI\'">selected</tpl>>BSCI' +
					'<option value="Okotex" <tpl if="certificate==\'Okotex\'">selected</tpl>>Okotex' +
				'</select>'+
			'</td>' +
			'<td>' +
				'<select name="pack" style="width:120px;">' +
				'<option value="" <tpl if="pack==0">selected</tpl>>Choose' +
				'<option value="Slip Sheet" <tpl if="pack==\'Slip Sheet\'">selected</tpl>>Slip Sheet' +
				'<option value="Paper Pallet" <tpl if="pack==\'Paper Pallet\'">selected</tpl>>Paper Pallet' +
				'</select><br/>'+
				'<select name="packOther" style="width:120px;">' +
				'<option value="" <tpl if="packOther==0">selected</tpl>>Choose' +
				'<option value="Hand Press" <tpl if="packOther==\'Hand Press\'">selected</tpl>>Hand Press' +
				'<option value="Vaccum Packing" <tpl if="packOther==\'Vaccum Packing\'">selected</tpl>>Vaccum Packing' +
				'</select>'+
			'</td>' +
			'<td rowspan="2"><input type="date" name="productTime" value="{productTime}"/></td>' +
			'<td>' +
				'<div style="width:220px;">' +
					'Ref. Price&nbsp;&nbsp;&nbsp;<select name="canCurId" style="width:60px;">' +
					'<option value="0" <tpl if="!canCurId">selected</tpl>>Cur.' +
					'<option value="1" <tpl if="canCurId==1">selected</tpl>>USD' +
					'<option value="2" <tpl if="canCurId==2">selected</tpl>>EUR' +
					'<option value="3" <tpl if="canCurId==3">selected</tpl>>DKK' +
					'</select>'+
					'<input style="width:60px;" type="number" name="canPrice"  value="{canPrice}"/>' +
				'</div>' +
				'<div style="width:220px;">' +
					'Target Price&nbsp;<select name="targetCurId" style="width:60px;">' +
					'<option value="0" <tpl if="!targetCurId">selected</tpl>>Cur.' +
					'<option value="1" <tpl if="targetCurId==1">selected</tpl>>USD' +
					'<option value="2" <tpl if="targetCurId==2">selected</tpl>>EUR' +
					'<option value="3" <tpl if="targetCurId==3">selected</tpl>>DKK' +
					'</select>'+
					'<input style="width:60px;" type="number" name="targetPrice"  value="{targetPrice}"/>' +
				'</div>' +
			'</td>' +
			'<td rowspan="2" align=center>' +
				'<button type="button" style="width:60px;height:22px" class="x-btn-text" onclick="showDetail({id},{#})"">Offer</button>&nbsp;'+
				'<br/><br/>'+
				'<input disabled style="width:140px;" type="text" name="manufactorer" value="{manufactorer}"/><br/><br/>'+
				'<div style="width:140px;">' +
					'<select disabled name="currencyId" style="width:80px;">' +
					'<option value="0" <tpl if="!currencyId">selected</tpl>>Cur.' +
					'<option value="1" <tpl if="currencyId==1">selected</tpl>>USD' +
					'<option value="2" <tpl if="currencyId==2">selected</tpl>>EUR' +
					'<option value="3" <tpl if="currencyId==3">selected</tpl>>DKK' +
					'</select>'+
					'<input disabled style="width:60px;" type="text" name="panPrice"  value="{panPrice}"/>' +
				'</div>' +
			'</td>' +
		'</tr>',
			'<td><textarea style="height:50px;width:130px;" type="text" name="colorRemark">{colorRemark}</textarea></td>' +
			'<td><textarea style="height:50px;width:120px;" type="text" name="packagingRemark">{packagingRemark}</textarea></td>' +
			'<td><textarea style="height:50px;width:120px;" type="text" name="packRemark">{packRemark}</textarea></td>' +
			'<td><textarea style="height:50px;width:220px;" type="text" name="priceRemark">{priceRemark}</textarea></td>' +
		'<tr>' +
		'</tr>',
		'<tr>' +
			'<td><b>Material</b></td>' +
			'<td colspan="10"><textarea style="height:20px;" class="thumb-textfield" type="text" name="material">{material}</textarea></td>' +
		'</tr>',
		'<tr>' +
			'<td><b>Construction</b></td>' +
			'<td colspan="10"><textarea style="height:20px;" class="thumb-textfield" type="text" name="construction">{construction}</textarea></td>' +
		'</tr>',
		'<tr>' +
			'<td><b>Filling Material</b></td>' +
			'<td colspan="10"><textarea style="height:20px;" class="thumb-textfield" type="text" name="fillingMaterial">{fillingMaterial}</textarea></td>' +
		'</tr>',
		'<tr>' +
			'<td><b>Filling Weight</b></td>' +
			'<td colspan="10"><textarea style="height:20px;" class="thumb-textfield" type="text" name="fillingWeight">{fillingWeight}</textarea></td>' +
		'</tr>',
		'</table>',
		'</form>',
		'</div>', '</tpl>');
QH.panAdd.thumbTemplate.compile();