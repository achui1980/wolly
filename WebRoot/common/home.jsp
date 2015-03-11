<%@ page language="java" isErrorPage="true"  contentType="text/html;charset=utf-8"%>
<head>
  <title></title>
</head>

<script type="text/javascript">
function backOrClose(){
  if (window.parent == null) {
    window.close();
  } else {
    window.history.go(-1);
  }
}
</script>

<body>
<br><br>
<div align="center">
  <table width="426"  border="0" align="center" cellpadding="0" cellspacing="0" background="../images/quit_bg.gif">
    <tr>
      <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8%" height="108">&nbsp;</td>
          <td width="92%">&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>
<br>
<br><%=request.getAttribute("message") %>

   <p>&nbsp;</p>

    </td>
        </tr>
      </table></td>
    </tr>
  </table>
  <br>
  <br>

</body>
