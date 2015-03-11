Ext.namespace('Ext.mail');

/**
 * 根据时间显示相应的时间格式
 * 如果是当年内，则只显示月日
 * @param {} date
 */
Ext.mail.dateRenderer = function(date){
	if(!date) return '';
	date = new Date(date.year+1900,date.month,date.date,date.hours,date.minutes,date.seconds);
	return '<span style="color:#888888">'+date.format('Y年m月d日 H:i(星期D)'+'</span>');
//	var nowDate = new Date();
//	var showDate = ''; // 显示整个时间提示
//	var showDateNumber = ''; // 时间的数字
//	var showDateStr = ''; // 时间后面的单位
//	var differDate = nowDate.getTime()-date.getTime(); // 以毫秒计算
//	if(differDate<0)
//		return '';
//	if(date.getYear()==nowDate.getYear()){
//		if(date.format('Y-m-d')==nowDate.format('Y-m-d')){ // 当天
//			if(date.between(nowDate.add(Date.HOUR,-1),nowDate)){// 一小时内
//				if(date.between(nowDate.add(Date.MINUTE,-1),nowDate)){// 一分钟内
//					showDateNumber = differDate/1000;
//					showDateStr = '秒';
//				}else{ // 一分钟外
//					showDateNumber = differDate/(1000*60);
//					showDateStr = '分钟';
//				}
//			}else{ // 一小时外
//				showDateNumber = differDate/(1000*60*60);
//				showDateStr = '小时';
//			}
//			showDate = showDateNumber.toFixed(0)+showDateStr+'前';
//		}else if(nowDate.getDayOfYear()-date.getDayOfYear()==1){
//			showDate = '昨天 '+date.format('H:i');
//		}else{
//			showDate = date.format('m月d日');
//		}
//	}else{
//		showDate = date.format('Y-m-d');
//	};
//	return '<span style="color:#888888">'+showDate+'</span>'
}
/**
 * 根据时间判断时间段
 * @param {} date 日期
 * @return {String} 日期分类信息
 */
Ext.mail.dateGroupingRenderer = function(date){
	if(!date) return '无时间';
	date = new Date(date.year+1900,date.month,date.date,date.hours,date.minutes,date.seconds);
	var nowDate = new Date();	
	if(date.getTime()>nowDate.getTime())
		return '超时';
	if(date.getYear()==nowDate.getYear()){
		if(date.format('Y-m-d')==nowDate.format('Y-m-d'))
			return '今天';
		else if(nowDate.getDayOfYear()-date.getDayOfYear()==1){
			return '昨天';
		}else if(nowDate.getWeekOfYear()-date.getWeekOfYear()==0){
			return '这周';
		}else if(nowDate.getWeekOfYear()-date.getWeekOfYear()==1){
			return '上周';
		}else if(nowDate.getMonth()-date.getMonth()==0){
			return '这月';
		}else if(nowDate.getMonth()-date.getMonth()==1){
			return '上月';
		}
	}
	return '更早';
};
Ext.mail.attachGroupingRenderer = function(isContainAttach){
	if(isContainAttach)
		return '带附件';
	else
		return '不带附件';
	
};
Ext.mail.sizeGroupingRenderer = function(size){
	if(!size) return '无大小';
	if(size<1024)
		return 'B';
	else if(size<1024*1024)
		return 'KB';
	else if(size<1024*1024*1024)
		return 'MB';
	else
		return 'GB';
};
Ext.mail.subjectGroupingRenderer = function(subject){
	if(!subject) return '无主题';
	return subject.substring(0,1);
};
Ext.mail.statusGroupingRenderer = function(status){
	if(status<0) return '已读';
	if(status==0||status==1)
		return '新邮件';
	else if(status==2)
		return '已读';
	else if(status==3||status==6)
		return '指派未读';
	else if(status==4)
		return '指派已读';
	else
		return '未接收';
}
Ext.mail.contactRenderer = function(custId,metaDate,record,rowIndex,colIndex,store){
	// 与分组兼容，因数据metaDate,record未
	record = store.getAt(rowIndex);
	var facId = '';
	if(custId)
		return custMap[custId]?'<span style="color:blue">'+custMap[custId]+'</span>':' ';
	else if(facId = record.get('facId')){
		return facMap[facId]?'<span style="color:green">'+facMap[facId]+'</span>':'';
	}
	return ' ';
}
Ext.mail.senderGroupingRenderer = function(sender){
	if(!sender) return '无发件人';
	if(!sender.emailUrl) return '无发件人';
	return sender.emailUrl;
}
Ext.mail.empGroupingRenderer = function(empId){
//	return empsMap[value];
	return empId;
}
/**
 * 根据时间显示相应的时间格式
 * 如果是当年内，则只显示月日
 * @param {} date
 */
Ext.mail.dateAllRenderer = function(date){
	if(!date) return '';
	date = new Date(date.year+1900,date.month,date.date,date.hours,date.minutes,date.seconds);
	return '<span style="color:#888888">'+date.format('Y年m月d日 H:i(星期D)'+'</span>');
}
Ext.mail.dateRenderer = function(date){
	if(!date) return '';
	date = new Date(date.year+1900,date.month,date.date,date.hours,date.minutes,date.seconds);
	var nowDate = new Date();
	var showDate = ''; // 显示整个时间提示
	var showDateNumber = ''; // 时间的数字
	var showDateStr = ''; // 时间后面的单位
	var differDate = nowDate.getTime()-date.getTime(); // 以毫秒计算
	if(differDate<0)
		return '';
	if(date.getYear()==nowDate.getYear()){
		if(date.format('Y-m-d')==nowDate.format('Y-m-d')){ // 当天
			if(date.between(nowDate.add(Date.HOUR,-1),nowDate)){// 一小时内
				if(date.between(nowDate.add(Date.MINUTE,-1),nowDate)){// 一分钟内
					showDateNumber = differDate/1000;
					showDateStr = '秒';
				}else{ // 一分钟外
					showDateNumber = differDate/(1000*60);
					showDateStr = '分钟';
				}
			}else{ // 一小时外
				showDateNumber = differDate/(1000*60*60);
				showDateStr = '小时';
			}
			showDate = showDateNumber.toFixed(0)+showDateStr+'前';
		}else if(nowDate.getDayOfYear()-date.getDayOfYear()==1){
			showDate = '昨天 '+date.format('H:i');
		}else{
			showDate = date.format('m月d日');
		}
	}else{
		showDate = date.format('Y-m-d');
	};
	return '<span style="color:#888888">'+showDate+'</span>'
}
/**
 * 如果包含附件则显示附件图标
 * @param {boolean} isContainAttach true为包含附件
 */
Ext.mail.attachRenderer = function(isContainAttach){
	if(isContainAttach)
		return '<span class="email_attach"></span>';
}
/**
 * 显示发件人姓名，如果发件人为空，则显示发件人邮箱
 * @param {} senderName
 * @param {} metaData
 * @param {} record
 * @return {}
 */
Ext.mail.senderNameRenderer = function(sender,metaData,record){
	var senderShow = '';
	if(sender)
		if(Ext.isEmpty(sender.name)&&Ext.isEmpty(sender.emailUrl)){
			senderShow = '<div style="color:#888888;text-align:center">(无联系人)</div>';
		}else if(Ext.isEmpty(sender.name))
			senderShow = sender.emailUrl;
		else
			senderShow = sender.name;
	else
		senderShow = '<div style="color:#888888;text-align:center">(无联系人)</div>';
	var status = record.get('mailStatus');
	if(status ==0||status ==1||status==3||status==6)
		senderShow = '<span style="font-weight: bold;">'+senderShow+'</span>';
	else if(status==5||status==MAIL_LOCAL_STATUS_CHECKNOTGO||status==MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO)
		senderShow = '<span style="color:red;text-align:center">'+senderShow+'</span>';
	return senderShow;
}
Ext.mail.subTimeRenderer = function(value,metadata,record){
	return (value || '<span style="color:#888888">(无主题)</span>')+'<br />'+Ext.mail.dateRenderer(record.get('sendTime'));
}
Ext.mail.nameTimeRenderer = function(value, metaData, record, rowIndex, colIndex, store){
	var nameTime = (value || '')+'<br />'+Ext.mail.dateRenderer(record.get('sendTime'));
	var mailRecord = store.mailRecord;
	if(mailRecord&&record.get('mailId')==mailRecord.get('id'))
		nameTime = '<span style="color:blue;font-weight: bold;">'+nameTime+'</span>';
	return nameTime;	
}
Ext.mail.toNameRenderer = function(to,metaData,record){
	var toShow = '';
	Ext.each(to,function(person){
		if(Ext.isEmpty(person.name))
			toShow += person.emailUrl+";&nbsp;";
		else 
			toShow += person.name+";&nbsp;";
	});
	if(toShow!='')
		toShow = toShow.substring(0,toShow.length-7);
	else
		toShow = '<div style="color:#888888;text-align:center">(无联系人)</div>';
	return toShow;
}
Ext.mail.mailStatusRenderer = function(mailStatus,metaData,record){
	var show = '';
	if(mailStatus==MAIL_LOCAL_STATUS_ERROR){ // 错误邮件，附件超过大小，未接收
		show += '<span class="email_error_1">&nbsp;</span>';
	}else{
		if(mailStatus==MAIL_LOCAL_STATUS_NOREAD||mailStatus==MAIL_LOCAL_STATUS_NOTIFICATION||mailStatus==MAIL_LOCAL_STATUS_ASSIGNNOREAD||mailStatus==MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION){	// 未读邮件
			show +='<span class="email_1">&nbsp;</span>';
		}else{
			show +='<span class="email_1" style="visibility:hidden">&nbsp;</span>'
		}
		if(mailStatus==MAIL_LOCAL_STATUS_ASSIGNNOREAD||mailStatus==MAIL_LOCAL_STATUS_ASSIGNREAD||mailStatus==MAIL_LOCAL_STATUS_ASSIGNNOTIFICATION) // 指派邮件
			show +='<span class="page_pencil_go_1">&nbsp;</span>';
		else
			show +='<span class="page_pencil_go_1" style="visibility:hidden">&nbsp;</span>';
			
	}
	var mailTag = record.get('mailTag');
	if(mailTag&&mailTag.indexOf(MAIL_TAG_REPALY)!=-1)
		show +='<span class="email_replay">&nbsp;</span>';
	else
		show +='<span class="email_replay" style="visibility:hidden">&nbsp;</span>';
	return show;
}
Ext.mail.mailTypeRenderer = function(mailType){
	if(mailType==2)
		return '<span class="email_inbox">&nbsp;</span>';
	else
		return '<span class="email_send">&nbsp;</span>';
}
Ext.mail.subjectRenderer = function(subject,metaData,record){
	var isEmpty = false;
	if(!subject){
		isEmpty = true;
		subject = '<div style="color:#888888;text-align:center">(无主题)</div>';
	}
	if(!isEmpty&&subject.toLowerCase().indexOf('re:')==0){
		subject = '回复：'+subject.substring(3);
		record.data.subject = subject;
	}
	else if(!isEmpty&&subject.toLowerCase().indexOf('fw:')==0){
		subject = '转发：'+subject.substring(3);
		record.data.subject = subject;
	}
	var status = record.get('mailStatus');
	if(status ==0||status ==1||status==3||status==6)
		subject = '<span style="font-weight: bold;">'+subject+'</span>';
	else if(status==5||status==MAIL_LOCAL_STATUS_CHECKNOTGO||status==MAIL_LOCAL_STATUS_PARTINGCHECKNOTGO)
		subject = '<span style="color:red;text-align:center">'+(isEmpty?'(无主题)':subject)+'</span>';
	return subject;
}
Ext.mail.sizeRenderer = function(size){
	if(size<1024){
		return size+' B';
	}else if(size<(1024*1024)){
		return (size/1024).toFixed(2) + ' KB';
	}else{
		return (size/1024/1024).toFixed(2) + ' MB';				
	}		
}