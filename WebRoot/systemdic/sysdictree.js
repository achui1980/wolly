Ext.onReady(function(){
	var myTree = new Ext.tree.TreePanel({      
        region:"center",
        autoScroll:true,   
        animate:true,   
        enableDD:true,   
        containerScroll: true,    
        root:new Ext.tree.AsyncTreeNode({   
             text: 'Data Dictionary', 
             expanded :true,
             draggable:false,   
             id:"1" }),   
        loader:new Ext.tree.TreeLoader({    
                   dataUrl:"cotmodule.do?method=querySysDic&&query=1" ,
                   
                   listeners : {   
                              'click' : function(loader,node) { 
                              	   //alert(node.href);
                                   this.baseParams.type = node.id; 
                                }
                   }   
               })   
    });   
    
    var viewport = new Ext.Viewport({
    	layout:"border",
    	items:[myTree]
    })
    
})