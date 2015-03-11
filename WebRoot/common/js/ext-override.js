Ext.util.JSON.encodeDate = function(o){
	var pad = function(n) {
            return n < 10 ? "0" + n : n;
        }
	return '"' + o.getFullYear() + "-" +
                pad(o.getMonth() + 1) + "-" +
                pad(o.getDate()) + " " +
                pad(o.getHours()) + ":" +
                pad(o.getMinutes()) + ":" +
                pad(o.getSeconds()) + '"';
}
Ext.apply(Ext.data.Types,{
	/**
	 * 将时间为JSON类型的对象转为Date
	 * 对象属性为year,month,day,hours,minutes,seconds,milliseconds
	 * @type 
	 */
	JSONDATE : {
	    convert: function(v){
	        if(!v){
	            return null;
	        }
	        if(Ext.isDate(v)){
	            return v;
	        }
	        if(v.time != null){
	        	return new Date(v.time);
	        }
	        return Ext.util.Format.date(new Date(v.time), "Y-m-d");
//	        return new Date(v.year,v.month,v.day,v.hours,v.minutes,v.seconds,v.milliseconds);
	    },
	    sortType: Ext.data.SortTypes.asDate,
	    type: 'jsondate'
	}
});