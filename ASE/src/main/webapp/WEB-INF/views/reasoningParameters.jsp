<%-- 
    Document   : configurationParameters
    Created on : 23-mar-2017, 13.27.19
    Author     : F.Camerlengo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%= request.getContextPath()%>/resources/javascript/utility.js"></script>
        <title>Configuration Parameters</title>
    </head>
    <body>
        <div id="content">
            <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section id="reasParam" class="backgroundHeader1">
                <form:form id="configuration" modelAttribute="configuration" method="post" action="${contextPath}/reasoningParameters">
                    <c:choose>
                        <c:when test="${!empty param.stBut}">
                            <input type="hidden" name="startButton" value="true"/>
                        </c:when>
                    </c:choose>
                    <c:choose>
                        <c:when test="${!empty confBean}">
                            <c:choose>
                                <c:when test="${!empty confBean.numberOfExecution}">
                                    <c:set var="numEx" value="${confBean.numberOfExecution}"></c:set>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${!empty numEx}">
                                    <c:set var="unS" value="false"></c:set>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="unS" value="true"></c:set>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${!empty confBean.rulesId}">
                                    <c:set var="rules" value=""></c:set>
                                    <c:forEach var="rule" items="${confBean.rulesId}">
                                        <c:set var="rules" value="${rules.concat(rule).concat(',')}"></c:set>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${!empty rules}">
                                    <c:set var="unSR" value="false"></c:set>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="unSR" value="true"></c:set>  
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                    </c:choose>
                                                      <div><p>Unset Iteration</p><input class="checkbox" type="checkbox" id="unS" name="unset" onmouseout="validateReasoningActiveTextParamForm('unS', 'numEx');" onmouseover="validateReasoningCheckBoxParamForm('unS', 'numEx');" <% if (pageContext.getAttribute("unS") != null && pageContext.getAttribute("unS").equals("true")) {
                                    out.print("checked");
                                }%>/></div>
                    <div><p>Number of executions</p><input type="number" id="numEx" name="numberOfExecutions" placeholder="1 or 2 or 3 or 4 etc.." onblur="validateReasoningCheckBoxParamForm('unS', 'numEx');"onmouseover="validateReasoningTextParamForm('unS', 'numEx');"  value="<%if (pageContext.getAttribute("numEx") != null) {
                                         out.print(pageContext.getAttribute("numEx"));
                                     }%>"></div>  
                                                    <div><p>Use all Rules</p><input class="checkbox" type="checkbox" id="unSR" onmouseout="validateReasoningActiveTextParamForm('unSR', 'rules');" onmouseover="validateReasoningCheckBoxParamForm('unSR', 'rules');"  name="allRules" <%if (pageContext.getAttribute("unS") != null && pageContext.getAttribute("unSR").equals("true")) {
                            out.print("checked");
                        }%>/></div>
                    <div><p>Wich rules apply</p><input type="text" name="rulesApply" id="rules" onblur="validateReasoningCheckBoxParamForm('unSR', 'rules');"onmouseover="validateReasoningTextParamForm('unSR', 'rules');" placeholder="1,2,6,9.." value="<% if (pageContext.getAttribute("rules") != null) {
                            out.println(pageContext.getAttribute("rules"));
                        }%>" /><a href="${contextPath}/loadInferenceRules/show" target="_blank"><img src="${pageContext.request.contextPath}/resources/images/interPointer.jpg" alt="interrogationPointer"></a></div>        
                    <div><input id="subConf" class="backgroundHeader1" <c:if test="${!empty param.stBut}">onclick="startReasoning('subConf',2);"</c:if> type="submit" value="Save"/></div>
                    <div class="loader"></div>
                    <c:choose>
                        <c:when test="${param.outcome=='success'}">
                            <p class="success">configurations have been saved</p>
                        </c:when>
                    </c:choose>
                </form:form>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
