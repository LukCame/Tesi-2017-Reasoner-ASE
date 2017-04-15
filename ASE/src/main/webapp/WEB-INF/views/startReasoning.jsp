<%-- 
    Document   : startReasoning
    Created on : 23-mar-2017, 21.39.30
    Author     : F.Camerlengo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%= request.getContextPath()%>/resources/javascript/utility.js"></script>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <title>Start Reasoning</title>
    </head>
    <body> 
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section id="startPage" class="backgroundHeader1">
                <c:choose>
                    <c:when test="${outcome=='redirect'}">
                        <p>reasoning configuration in not complete </p>
                    </c:when>
                    <c:when test="${param.outcome=='runErr'}">
                        <p class="error">error in the reasoning operation</p>
                    </c:when>   
                    <c:otherwise>
                        <p>Now you can start reasoning</p>
                        <form method="get" action="<%= request.getContextPath()%>/startReasoning/start">
                            <input id="butSt" class="backgroundHeader2" onclick="startReasoning('butSt',1);" type="submit" value="Start"/>
                        </form>
                    </c:otherwise>
                </c:choose>
                <div class="loader"></div>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
