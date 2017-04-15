<%-- 
    Document   : changeConfirmation
    Created on : 28-mar-2017, 11.12.34
    Author     : F.Camerlengo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <title>Change Confirmation</title>
    </head>
    <body>
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section id="confirmation" class="backgroundHeader1">
                <p id="alert">do you want confirm the changes made to the file?</p>
                <form method="post" action="<%=request.getContextPath()%>/loadInferenceRules/edit">
                    <input type="hidden" name="filePath" value="${filePath}"/>
                    <button type="submit" class="backgroundHeader1">OK</button>
                </form>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
