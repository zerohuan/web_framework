<%--
  Created by IntelliJ IDEA.
  User: yjh
  Date: 15-10-12
  Time: 上午11:23
  To change this template use File | Settings | File Templates.
--%>
<%--@elvariable id="course" type="com.yjh.cg.site.entities.BCourseEntity" --%>
<form:form method="post" modelAttribute="course">
    <label for="courseName">课程名称</label>
    <form:input path="courseName" /><br />

    <input type="submit" value="提交" />
</form:form>