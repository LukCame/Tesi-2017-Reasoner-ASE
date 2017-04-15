

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <title>ASE Reasoner</title>
    </head>
    <body>
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section id="contentArea" class="backgroundHeader1">
                <p> Welcome to ase reasoner</p>
                <form name="form" method="get" action="<%= request.getContextPath() %>/loadOntology">
                    <input type="hidden" name="stBut" value="true"/>
                    <input type="submit" id="button" value="Get Started Now Reasoning"/>
                </form>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
