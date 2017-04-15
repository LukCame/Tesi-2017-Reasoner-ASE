<%-- 
    Document   : viewInverseReasoning
    Created on : 2-apr-2017, 13.10.14
    Author     : F.Camerlengo
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Inverse Reasoning</title>
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <% int i = 0;
        int j = 0;%>
        <c:set var="numTr" value="${newResBean.queriesResults.numberOfInferredTriples}"></c:set>
        <c:set var="numGr" value="${newResBean.queriesResults.numberOfInferredGraphs}"></c:set>
        <c:set var="staList" value="${newResBean.queriesResults.resultingStatement}"></c:set>
        <c:set var="grList" value="${newResBean.queriesResults.resultingGraph}"></c:set>   
        <div id="content">
            asbjhac dfkcs jbvncvk ${numTr}
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section class="backgroundHeader1" id="results">
                <c:choose>
                    <c:when test="${empty newResBean}">
                        <p>empty  </p>
                    </c:when>
                    <c:when test="${numTr<=0&&numGr<=0}">
                        
                    </c:when>     
                </c:choose>
                <c:choose>
                    <c:when test="${numTr>0}"> 
                        <p>dajeeeeee</p>
                        <c:choose>
                            <c:when test="${empty param.invReas}">
                                <p  class="numRes">${numTr}&nbsp; triple discoveries</p>
                            </c:when>
                            <c:otherwise>
                                <p  class="numRes">Choose one or more triple</p>
                            </c:otherwise>
                        </c:choose>
                        <div class="resWrap">
                            <ul class="resList">

                                <c:forEach var="elem" items="${staList}">
                                    <li class="statement" id="statement<%=i%>"><span onclick="<%if (request.getParameter("invReas") == null) {
                                            out.println("showOrHidePathSt(" + i + ")");
                                        } else {
                                            out.println("selectStatementOrGraph(statement" + i + ",'#87CEEB')");
                                        }%>">${elem.key.subject}&nbsp;&nbsp; ${elem.key.predicate}&nbsp;&nbsp; ${elem.key.object}</span></li>
                                    <div class="paths" id="path<%=i%>" style="display:none">

                                        <c:forEach var="path" items="${elem.value}">
                                            <c:set var="ruleId" value="null"></c:set>
                                                <div class="path">
                                                <c:forEach var="stat" items="${path.statements}">
                                                    <c:choose>
                                                        <c:when test="${!stat.isInferred&&!stat.isInvented}">
                                                            <c:set var="cl" value="ontology"></c:set>
                                                        </c:when>
                                                        <c:when test="${stat.isInferred}">
                                                            <c:set var="cl" value="inferred"></c:set>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="cl" value="invented"></c:set>
                                                        </c:otherwise>
                                                    </c:choose> 
                                                    <c:choose>
                                                        <c:when test="${ruleId=='null'}">
                                                            <li class="${cl}"><span>${stat.statement.subject}&nbsp;&nbsp;${stat.statement.predicate}&nbsp;&nbsp;${stat.statement.object}&nbsp;&nbsp; </span><span class="ruleId">rule id: ${path.inferenceRuleId}</span></li>
                                                                <c:set var="ruleId" value="set"></c:set>
                                                            </c:when>
                                                            <c:otherwise>
                                                            <li class="${cl}"><span>${stat.statement.subject}&nbsp;&nbsp;${stat.statement.predicate}&nbsp;&nbsp;${stat.statement.object}&nbsp;&nbsp; </span></li>
                                                            </c:otherwise> 
                                                        </c:choose>
                                                    </c:forEach>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <% i = i + 1;%>
                                </c:forEach>    
                            </ul>
                        </div> 
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${numGr>0}">
                        <c:choose>
                            <c:when test="${empty param.invReas}">
                                <p class="numRes">${numGr}&nbsp; graphs discovered</p>
                            </c:when>
                            <c:otherwise>
                                <p class="numRes">Choose one or more graphs</p>
                            </c:otherwise>
                        </c:choose>
                        <div class="resWrap">
                            <ul class="resList">
                                <c:forEach var="graph" items="${grList}">
                                    <div class="graph" id="graph<%= j%>">
                                        <c:forEach var="st" items="${graph.key.statementList}"> 
                                            <li><span onclick="<%if (request.getParameter("invReas") == null) {
                                                    out.println("showOrHidePathGr(" + j + ")");
                                                } else {
                                                    out.println("selectStatementOrGraph(graph" + j + ",'#6A5ACD')");
                                                }%>"  >${st.subject}&nbsp;&nbsp; ${st.predicate}&nbsp;&nbsp; ${st.object}</span></li>
                                            </c:forEach>
                                    </div>
                                    <div class="paths" id="pathGr<%=j%>" style="display:none">
                                        <c:forEach var="path" items="${graph.value}">
                                            <div class="path">
                                                <c:set var="ruleId" value="null"></c:set>
                                                <c:forEach var="stat" items="${path.statements}">
                                                    <c:choose>
                                                        <c:when test="${!stat.isInferred&&!stat.isInvented}">
                                                            <c:set var="cl" value="ontology"></c:set>
                                                        </c:when>
                                                        <c:when test="${stat.isInferred}">
                                                            <c:set var="cl" value="inferred"></c:set>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="cl" value="invented"></c:set>
                                                        </c:otherwise>
                                                    </c:choose> 
                                                    <c:choose>
                                                        <c:when test="${ruleId=='null'}">
                                                            <li class="${cl}"><span>${stat.statement.subject}&nbsp;&nbsp;${stat.statement.predicate}&nbsp;&nbsp;${stat.statement.object}&nbsp;&nbsp; </span><span class="ruleId">rule id: ${path.inferenceRuleId}</span></li>
                                                                <c:set var="ruleId" value="set"></c:set>
                                                            </c:when>
                                                            <c:otherwise>
                                                            <li class="${cl}"><span>${stat.statement.subject}&nbsp;&nbsp;${stat.statement.predicate}&nbsp;&nbsp;${stat.statement.object}&nbsp;&nbsp; </span></li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <% j = j + 1;%>
                                </c:forEach>
                            </ul>
                        </div>  
                    </c:when>
                </c:choose>
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
