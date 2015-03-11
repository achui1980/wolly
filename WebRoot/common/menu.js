Ext.onReady(function(){

	var myTree = new Ext.tree.TreePanel({      
        el:'tree',     
        autoScroll:false,   
        animate:true,   
        width:'150px',   
        height:'400px',
        bodyBorder:false,
        border:false,
        ddScroll:false,
        enableDD:false,   
        containerScroll: false,    
        root:new Ext.tree.AsyncTreeNode({   
             text: '系统菜单',   
             draggable:false,   
             id:"100000" }),   
        loader:new Ext.tree.DWRTreeLoader({    
                   dataUrl:uphsRoleService.getAllChildren,    
                   listeners : {   
                              'beforeload' : function(node) {   
                                   myTree.getLoader().args[0]=(node.id!='root'?node.id:"100000");   
                                }
                   }   
               })   
    });   
       
    myTree.render();  
    
    myTree.on("click",function(node){
    	
    	if(node.isLeaf())
    		window.parent.document.getElementById("mainF").src = node.attributes.url;
    	else
    		window.parent.document.getElementById("mainF").src = "index.jsp";
//    	for(var p in node)
//    	{
//    		//alert(p+":"+window.parent[p])
//    	}
//    	
    })
});

function tt(){
	window.parent.innertHTML;
}