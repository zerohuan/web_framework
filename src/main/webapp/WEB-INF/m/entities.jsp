<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-10-4
  Time: 下午8:42
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>JPA简单实体使用</title>
</head>
<body>
  <c:forEach items="${fileList}" var="f">
      <p>
          ${f.filename}<br />
          ${f.size}<br />
      </p>
  </c:forEach>
</body>
</html>
