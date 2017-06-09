
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <title>Load Ontology</title>
    </head>
    <body>
        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section id="ontPage" class="backgroundHeader1">
                <p> choose an ontology to be loaded</p>
                <form:form modelAttribute="fileUpload" method="post" cssClass="backgroundHeader2" action="${contextPath}/upload/ontology" enctype="multipart/form-data">
                    <input type="file" name="file"/><br><br>
                    <c:choose>
                        <c:when test="${!empty stBut}">
                            <input type="hidden"  name="startButton" value="true"/>
                        </c:when>
                    </c:choose>
                    <input type="submit" class="upBut" value="Upload"/>
                    <c:choose>
                        <c:when test="${param.outcome=='uploadErr'}">
                            <p class="error">error on upload of ontology</p>
                        </c:when>
                        <c:when test="${param.outcome=='success'}">
                            <p class="success">ontology loaded correctly</p>
                        </c:when>
                    </c:choose>
                </form:form>
                <c:choose>
                    <c:when test="${!empty ontBean}">
                        <p id="lastOnt">last loaded ontology: <span>${ontBean.fileName}</span></p>
                    </c:when>
                    <c:otherwise>
                        <br><br>
                    </c:otherwise>
                </c:choose>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
