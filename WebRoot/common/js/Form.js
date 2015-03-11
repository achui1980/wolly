Ext.namespace('Ext.ux.form');
/**
 * 限制日期起始与结束范围
 * 必填id,startDateField,endDateField
 * @class Ext.form.DateRangeField
 * @extends Ext.form.DateField
 */
Ext.ux.form.DateRangeField = Ext.extend(Ext.form.DateField,{
	width:120,
//	emptyText:'Pls.Select Date',
	allowBlank:true,
	maxText:'Start time must be before {0}',
	minText:'End date must be after {0}',
	blankText:'Date can not be empty',
//	regex: /((^\d{4})-(\d{2})-(\d{2}$))|((^\d{4})年(\d{2})月(\d{2})日$)/,
//	regexText: '格式为(年-月-日)例:2009-02-17或2009年02月17日',
	vtype:'daterange'
});
Ext.reg('daterangefield',Ext.ux.form.DateRangeField);