$importCss('common/ext/resources/css/file-upload.css')
$import('common/ext/ux/FileUploadField.js')
$import('common/js/uploadpanel.js')
/**
 * 扩展上传图片的功能
 * @class HTMLEditor
 * @extends Ext.form.HtmlEditor
 */
HTMLEditor = Ext.extend(Ext.form.HtmlEditor, {
	defaultFont : 'Arial',
	noUploadPic:false,//默认可以上传图片
	//默认字体都转成小写
	initComponent : function(){
		this.defaultFont=this.defaultFont.toLowerCase();
 	},
 	//编辑时,使用下拉字体
 	onEditorEvent : function(e){
        this.updateToolbar();
        if(Ext.isWebKit){
        	var font = this.fontSelect.dom.value;
       		this.relayCmd('fontname', font);
        }
    },
 	//在初始化时,使用默认字体
	onFirstFocus : function(){
        this.activated = true;
        this.disableItems(this.readOnly);
        if(Ext.isGecko){ // prevent silly gecko errors
            this.win.focus();
            var s = this.win.getSelection();
            if(!s.focusNode || s.focusNode.nodeType != 3){
                var r = s.getRangeAt(0);
                r.selectNodeContents(this.getEditorBody());
                r.collapse(true);
                this.deferFocus();
            }
            try{
                this.execCmd('useCSS', true);
                this.execCmd('styleWithCSS', false);
            }catch(e){}
        }
        if(Ext.isWebKit){
        	var font = this.fontSelect.dom.value;
       		this.relayCmd('fontname', font);
        }
        this.fireEvent('activate', this);
    },
    //点击上传图片按钮
    addImage : function() {
        var editor = this;
        this.focus();
        var win = new UploadWin({
				waitMsg : "Loading......",
				opAction : 'editor',
				editor :editor,
				uploadType : "image",
				uploadUrl : './upload.action',
				validType : "jpg|png|bmp|gif"
			})
		win.show();
    },
    fixKeys : function(){ // load time branching for fastest keydown performance
        if(Ext.isIE){
            return function(e){
                var k = e.getKey(),
                    doc = this.getDoc(),
                        r;
                if(k == e.TAB){
                    e.stopEvent();
                    r = doc.selection.createRange();
                    if(r){
                        r.collapse(true);
                        r.pasteHTML('&nbsp;&nbsp;&nbsp;&nbsp;');
                        this.deferFocus();
                    }
                }else if(k == e.ENTER){
                    r = doc.selection.createRange();
                    if(r){
                        var target = r.parentElement();
                        if(!target || target.tagName.toLowerCase() != 'li'){
                            e.stopEvent();
                            r.pasteHTML('<br />');
                            r.collapse(false);
                            r.select();
                        }
                    }
                }
            };
        }else if(Ext.isOpera){
            return function(e){
                var k = e.getKey();
                if(k == e.TAB){
                    e.stopEvent();
                    this.win.focus();
                    this.execCmd('InsertHTML','&nbsp;&nbsp;&nbsp;&nbsp;');
                    this.deferFocus();
                }
            };
        }else if(Ext.isWebKit){
            return function(e){
                var k = e.getKey();
                if(k == e.TAB){
                    e.stopEvent();
                    this.execCmd('InsertText','\t');
                    this.deferFocus();
                }else if(k == e.ENTER){
                    e.stopEvent();
                    //----回车后不改变字体
                    var font = this.fontSelect.dom.value;
       				this.relayCmd('fontname', font);
       				//----------------------
       				//先判断未回车之前的文本末尾是否有<br>
       				var txtOld = this.getEditorBody().innerText;
       				var ppOld = txtOld.charAt(txtOld.length-1);
       				//加<br>
					var selection = this.win.getSelection();
					var rg=selection.getRangeAt(0); 
					var fragment = rg.createContextualFragment("<br/>");
					var oLastNode = fragment.lastChild;
					rg.insertNode(fragment);
					rg.setEndAfter(oLastNode);//设置末尾位置
					rg.collapse(false);//合并范围至末尾
					selection.removeAllRanges();//清除range
					selection.addRange(rg);//设置range
   				
       				//回车后的文本末尾是否有<br>
       				var txt = this.getEditorBody().innerText;
       				var ppNew = txt.charAt(txt.length-1);
       				//旧文本末尾没<br>,回车后变成有,则说明当时光标在文本末尾
       				if(ppOld!='\n' && ppNew=='\n'){
       					//如果是文本末尾还要再加一次<br>
						var rg2=selection.getRangeAt(0); 
						var fragment = rg2.createContextualFragment("<br/>");
						var oLastNode = fragment.lastChild;
						rg2.insertNode(fragment);
						rg2.setEndAfter(oLastNode);//设置末尾位置
						rg2.collapse(false);//合并范围至末尾
						selection.removeAllRanges();//清除range
						selection.addRange(rg2);//设置range
       				}
                }
             };
        }
    }(),
    //插入上传图片按钮
    createToolbar : function(editor) {
        HTMLEditor.superclass.createToolbar.call(this, editor);
        this.tb.insertButton(16, {
                    iconCls : "upload-icon",
                    handler : this.addImage,
                    tooltip:'Upload Picture',
                    hidden:this.noUploadPic,
                    scope : this
                });
    }
});
Ext.reg('starHtmleditor', HTMLEditor);