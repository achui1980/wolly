Ext.onReady(function() {     
    /**  
     * button  
     */  
	alert('dddd');
    var objArray = Ext.DomQuery.select("input[type=submit]");   
    Ext.each(objArray, function(obj) {   
        var btn = new Ext.Button({   
            text : obj.value,   
            applyTo : obj,   
            handler : obj.onclick,   
            type : obj.type   
        });   
        btn.getEl().replace(Ext.get(obj));   
    });   
    var objArray = Ext.DomQuery.select("input[type=reset]");   
    Ext.each(objArray, function(obj) {   
        var btn = new Ext.Button({   
            text : obj.value,   
            applyTo : obj,   
            handler : obj.onclick,   
            type : obj.type   
        });   
        btn.getEl().replace(Ext.get(obj));   
    });   
    var objArray = Ext.DomQuery.select("input[type=button]");   
    Ext.each(objArray, function(obj) {   
        var btn = new Ext.Button({   
            text : obj.value,   
            applyTo : obj,   
            handler : obj.onclick,   
            type : obj.type   
        });   
        btn.getEl().replace(Ext.get(obj));   
    });   
    /**  
     * text and textarea and password and file  
     */  
    var objArray = Ext.DomQuery.select("input[type=text]");   
    Ext.each(objArray, function(obj) {   
        var txtField = new Ext.form.TextField({   
            applyTo : obj 
        });   
    });   
    var objArray = Ext.DomQuery.select("input[type=password]");   
    Ext.each(objArray, function(obj) {   
        var txtField = new Ext.form.TextField({   
            applyTo : obj,   
            inputType : 'password'  
        });   
    });   
    var objArray = Ext.DomQuery.select("input[type=file]");   
    Ext.each(objArray, function(obj) {   
  
    });   
    var objArray = Ext.DomQuery.select("textarea");   
    Ext.each(objArray, function(obj) {   
        var txtArea = new Ext.form.TextArea({   
            applyTo : obj   
        });   
    });   
    /**  
     * checkbox and radio  
     */  
    var objArray = Ext.DomQuery.select("input[type=checkbox]");   
    Ext.each(objArray, function(obj) {   
        var checkbox = new Ext.form.Checkbox({   
            applyTo : obj   
        });   
    });   
    var objArray = Ext.DomQuery.select("input[type=radio]");   
    Ext.each(objArray, function(obj) {   
        var radio = new Ext.form.Radio({   
            applyTo : obj   
        });   
    });   
    /**  
     * select or comboBox  
     */  
    var objArray = Ext.DomQuery.select("select");   
    Ext.each(objArray, function(obj) {   
        var btn = new Ext.form.ComboBox({    
            triggerAction : 'all',   
            transform : obj.id,   
            forceSelection : true ,
            width:50,
            cls:obj.className
        });  
        alert(obj.className)
    });   
    
     /**  
     * fieldset  
     */  
    var objArray = Ext.DomQuery.select("fieldset");   
    Ext.each(objArray, function(obj) {   
        var fieldSet = new Ext.form.FieldSet({   
        	applyTo : obj,
        	autoHeight:true
        });   
    }); 
  
})  
