<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-9-28
  Time: 下午4:10
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Request基本信息</title>
</head>
<body>
  <h1>Request基本信息</h1>
  <h2>ClassLoader结构</h2>
    <c:forEach items="${classLoaderNames}" var="cn">
      <span>${cn}</span><br />
    </c:forEach>
  </p>
  <p></p>
</body>
</html>
