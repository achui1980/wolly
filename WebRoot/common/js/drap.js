/*
	  该函数由mousedown事件处理调用
	  它为随后发生的mousemove和mouseup事件注册了临时的捕捉事件处理程序
	  并用这些事件处理程序拖动指定的文档元素
	  第二个参数必须是mousedown事件的事件对象
 */
function beginDrag(elementToDrag, event) {
	//该元素当前位于何处
	//该元素的样式性质必须具有left和top css属性
	//此外，我们假定他们用象素做单位
	//var x=parseInt(elementToDrag.style.left);
	//var y=parseInt(elementToDrag.style.top);

	//计算一个点和鼠标点击之间的距离，下面的嵌套的moveHandler函数需要这些值
	var deltaX = event.clientX - parseInt(elementToDrag.style.left);
	var deltaY = event.clientY - parseInt(elementToDrag.style.top);

	//  注册mousedown事件后发生的mousemove和mouseup事件的处理程序
	//  注意，它们被注册为文档的捕捉事件处理程序
	//  在鼠标按钮保持按下的状态的时候，这些事件处理程序保持活动的状态
	//  在按钮被释放的时候，它们被删除
	document.attachEvent("onmousemove", moveHandler);
	document.attachEvent("onmouseup", upHandler);

	//我们已经处理了该事件，不要让别的元素看到它
	event.cancelBubble = true;
	event.returnValue = false;

	/*
	这是在元素被拖动时候捕捉mousemove事件的处理程序，它响应移动的元素
	
	 */
	function moveHandler(e) {
		//把元素移动到当前的鼠标位置
		e = window.event;
		elementToDrag.style.left = (event.clientX - deltaX) + "px";
		elementToDrag.style.top = (event.clientY - deltaY) + "px";

		//不要让别的元素看到该事件
		event.cancelBubble = true;

	}

	/*
	该事件将捕捉拖动结束的时候发生的mouseup事件
	 */
	function upHandler(e) {
		//注销事件处理程序
		document.detachEvent("onmouseup", upHandler);
		document.detachEvent("onmousemove", moveHandler);
	}

	event.cancelBubble = true;
}