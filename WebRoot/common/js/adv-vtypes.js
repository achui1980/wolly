/*!
 * Ext JS Library 3.2.0
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
// Add the additional 'advanced' VTypes
Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
	    var date = field.parseDate(val);
	    
	    var dispUpd = function(picker) {
	      var ad = picker.activeDate;
	      picker.activeDate = null;
	      picker.update(ad);
	    };
	    
	    if (field.startDateField || field.startDateId) {
    	  var sd = '';
    	  if(field.startDateId){
			sd = Ext.getCmp(field.startDateId);
		  }else{
		    sd = field.startDateField.startDateField
		  }
	      sd.maxValue = date;
	      if (sd.menu && sd.menu.picker) {
	        sd.menu.picker.maxDate = date;
	        dispUpd(sd.menu.picker);
	      }
	    } else if (field.endDateField || field.endDateId) {
	      var ed = '';
		  if(field.endDateId){
			  ed = Ext.getCmp(field.endDateId);
		  }else{
			  ed = field.endDateField.endDateField;
		  }
	      ed.minValue = date;
	      if (ed.menu && ed.menu.picker) {
	        ed.menu.picker.minDate = date;
	        dispUpd(ed.menu.picker);
	      }
	    }
	    /* Always return true since we're only using this vtype
	     * to set the min/max allowed values (these are tested
	     * for after the vtype test)
	     */
	    return true;
    },

    password : function(val, field) {
        if (field.initialPassField) {
            var pwd = Ext.getCmp(field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },

    passwordText : 'The two passwords do not match！'
});