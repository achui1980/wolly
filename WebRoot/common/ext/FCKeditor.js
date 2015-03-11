Ext.form.FCKeditor = function(config) {
	Ext.form.FCKeditor.superclass.constructor.call(this, config);
};

Ext.extend(Ext.form.FCKeditor, Ext.form.TextArea, {
	onRender : function(ct, position) {
		if (!this.el) {
			this.defaultAutoCreate = {
				tag : "textarea",
				style : "width:100px;height:60px;",
				autocomplete : "off"
			};
		}
		Ext.form.TextArea.superclass.onRender.call(this, ct, position);
		if (this.grow) {
			this.textSizeEl = Ext.DomHelper.append(document.body, {
						tag : "pre",
						cls : "x-form-grow-sizer"
					});
			if (this.preventScrollbars) {
				this.el.setStyle("overflow", "hidden");
			}
			this.el.setHeight(this.growMin);
		}
		//setTimeout("loadFCKeditor('" + this.id + "'," + this.height + ");", 100);
		var fckobj = new FCKeditor(this.id);
		fckobj.BasePath = 'common/fckeditor/';
		fckobj.Height = this.height;
		fckobj.ReplaceTextarea();
	}
});
Ext.reg('fckeditor', Ext.form.FCKeditor);

//function loadFCKeditor(elementId, height) {
//	var fckobj = new FCKeditor(elementId);
//	fckobj.BasePath = 'common/fckeditor/';
//	// fckobj.SkinPath = fckobj.BasePath + 'edit/skins/office2003/' ;
//	fckobj.Height = height;
//	fckobj.ReplaceTextarea();
//
//}
