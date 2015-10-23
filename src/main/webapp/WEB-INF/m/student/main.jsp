<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-10-12
  Time: 下午3:03
  To change this template use File | Settings | File Templates.
--%>
<template:basic htmlTitle="${projectName}" bodyTitle="">

  <jsp:attribute name="extraFootScriptConent">
      <script src="<c:url value="https://cdn.socket.io/socket.io-1.3.7.js" />"></script>
      <script src="<c:url value="/resources/js/models/student_main.js" />"></script>
  </jsp:attribute>

  <jsp:body>
    这是学生端首页
  </jsp:body>

</template:basic>
