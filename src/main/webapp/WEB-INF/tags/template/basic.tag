<%@ tag pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>
<%@ attribute name="bodyTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>
<%@ attribute name="extraHeadContent" fragment="true" required="false" %>
<%@ attribute name="extraNavigationContent" fragment="true" required="false" %>
<%@ attribute name="extraFootScriptConent" fragment="true" required="false" %>
<%@ include file="/WEB-INF/m/base.jspf" %>
<template:main htmlTitle="${htmlTitle}" bodyTitle="${bodyTitle}">
    <jsp:attribute name="headContent">
        <jsp:invoke fragment="extraHeadContent" />
    </jsp:attribute>
    <jsp:attribute name="navigationContent">
        <jsp:invoke fragment="extraNavigationContent" />
    </jsp:attribute>
    <jsp:attribute name="footScriptContent">
        <jsp:invoke fragment="extraFootScriptConent" />
    </jsp:attribute>
    <jsp:body>
        <jsp:doBody />
    </jsp:body>
</template:main>
