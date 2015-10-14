<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-10-12
  Time: 下午5:01
  To change this template use File | Settings | File Templates.
--%>
<template:basic htmlTitle="${applicationScope.get('projectName')}" bodyTitle="">
  <c:forEach items="${courses}" var="course">
      <div>
          <span>${course.courseId}</span>
      </div>
  </c:forEach>
</template:basic>
