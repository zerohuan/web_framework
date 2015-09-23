<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-9-20
  Time: 下午9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>
<h2>Create a Ticket</h2>
<form method="POST" action="upload" enctype="multipart/form-data">
    <input type="hidden" name="action" value="create"/> Your Name<br/>
    <input type="text" name="username"/><br/><br/>
    Subject<br/>
    <input type="text" name="subject"/><br/><br/>
    Body<br/>
    <textarea name="body" rows="5" cols="30">

    </textarea><br/><br/>
    <b>Attachments</b><br/>
    <input type="file"  name="attachment" /><br/><br/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>
