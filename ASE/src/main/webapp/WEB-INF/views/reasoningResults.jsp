<%-- 
    Document   : reasoningResults
    Created on : 26-mar-2017, 19.45.05
    Author     : F.Camerlengo
--%>

<%@page import="it.uniroma2.ase.view.responseBean.ResultsBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%= request.getContextPath()%>/resources/javascript/utility.js"></script>
        <title>Reasoning Results</title>
    </head>
    <body>
        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        <c:set var="results" value="${resBean}"></c:set>
        <c:set var="numTr" value="${results.queriesResults.numberOfInferredTriples}"></c:set>
        <c:set var="numGr" value="${results.queriesResults.numberOfInferredGraphs}"></c:set>
        <c:set var="staList" value="${results.queriesResults.resultingStatement}"></c:set>
        <c:set var="grList" value="${results.queriesResults.resultingGraph}"></c:set>
        <c:set var="inconsistencies" value="${results.queriesResults.inconsistenciesList}"></c:set>
        <c:set var="numInc" value="${results.queriesResults.numberOfInconsistencies}"></c:set>
        <c:set var="ontologyTriple" value="${results.queriesResults.ontologyTriple}"></c:set>
        <c:choose>
            <c:when test="${!empty exeInv}">
                <c:set var="staList" value="${newResBean.queriesResults.resultingStatement}"></c:set>
                <c:set var="grList" value="${newResBean.queriesResults.resultingGraph}"></c:set>
            </c:when>
        </c:choose>
        <% int i = 0; %>
        <% int j = 0;%>
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <section class="backgroundHeader1" id="results">
                <c:choose>
                    <c:when test="${!empty newResBean&&newResBean.queriesResults.numberOfInferredTriples==0&&newResBean.queriesResults.numberOfInferredGraphs==0}">
                        <c:choose>
                            <c:when test="${againInferable}">
                                <p class="results"> graphs and triples are not again inferable </p>
                            </c:when>
                            <c:otherwise>
                                <p class="results"> all graphs and triples are again inferable </p>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${!empty results&&(numTr>0||numGr>0||numInc>0)}">
                        <p class="results">The reasoning has produced results</p>
                        <c:choose>
                            <c:when test="${empty param.invReas}">
                                <section id="legend" class="backgroundHeader2">
                                    <p>Legend of the paths triple</p>
                                    <ul>
                                        <li class="invented"><span>Manual Triple</span></li>
                                        <li class="inferred"><span>Inferred Triple</span></li>
                                        <li class="ontology"><span>Ontology Triple</span></li>
                                    </ul>
                                </section>
                                <p style="text-align: right;margin-right:90px;text-transform: uppercase;font-family:Arial,Helvetica,sans-serif;"><a href="${contextPath}/loadInferenceRules/show" target="_blank" style="color:blue;">show Rules</a></p>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${!empty inconsistencies&&empty param.invReas&&empty exeInv}">
                                <p  class="numRes">${numInc}&nbsp; inconsistencies found</p>
                                <div class="resWrap"  >
                                    <ul class="resList" >
                                        <div class="paths">
                                            <c:forEach var="path" items="${inconsistencies}">
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
                                    </ul> 
                                </div>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${(numTr>0&&empty newResBean)||((!empty newResBean)&&newResBean.queriesResults.numberOfInferredTriples>0)}"> 
                                <c:choose>
                                    <c:when test="${!empty newResBean}">
                                        <p id="backLink" style="margin-left: 50px;font-size:20px;"><a href="<%=request.getContextPath()%>/reasoningResults?invReas=true">Back</a></p>
                                        <c:choose>
                                            <c:when test="${againInferable}">
                                                <p  class="numRes">${newResBean.queriesResults.numberOfInferredTriples}&nbsp; triples of ${pastNumSt} are again inferable</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p  class="numRes">${newResBean.queriesResults.numberOfInferredTriples}&nbsp; triples of ${pastNumSt} are not again inferable</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${empty param.invReas}">
                                        <p  class="numRes">${numTr}&nbsp; triples found</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p  class="numRes">Choose one or more triples (if you have not selected anything all will be considered by default)</p>
                                    </c:otherwise>
                                </c:choose>
                                <div class="resWrap">
                                    <ul class="resList">

                                        <c:forEach var="elem" items="${staList}">
                                            <li class="statement" id="statement<%=i%>"><span <c:if test="${!empty againInferable&&!againInferable}">style="cursor: default;"</c:if> <c:if test="${empty againInferable||againInferable}">onclick="<%if (request.getParameter("invReas") == null) {
                                                    out.println("showOrHidePathSt(" + i + ")");
                                                } else {
                                                    out.println("selectStatementOrGraph(statement" + i + ",'#87CEEB')");
                                                }%>"</c:if>>${elem.key.subject}&nbsp;&nbsp; ${elem.key.predicate}&nbsp;&nbsp; ${elem.key.object}</span></li>
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
                            <c:when test="${!empty newResBean}">
                                <c:choose>
                                    <c:when test="${!againInferable}">
                                        <p class="numRes">The triples are again inferable </p><br>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="numRes">The triples are not again inferable </p><br>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${(numGr>0&&empty newResBean)||(!empty newResBean&&newResBean.queriesResults.numberOfInferredGraphs>0)}">
                                <c:choose>
                                    <c:when test="${!empty newResBean}">
                                        <c:choose>
                                            <c:when test="${againInferable}">
                                                <p  class="numRes">${newResBean.queriesResults.numberOfInferredGraphs}&nbsp; graphs of ${pastNumGr} are again inferable</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p  class="numRes">${newResBean.queriesResults.numberOfInferredGraphs}&nbsp; graphs of ${pastNumGr} are not again inferable</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${empty param.invReas}">
                                        <p class="numRes">${numGr}&nbsp; graphs found</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="numRes">Choose one or more graphs(if you have not selected anything all will be considered by default)</p>
                                    </c:otherwise>
                                </c:choose>
                                <div class="resWrap">
                                    <ul class="resList">
                                        <c:forEach var="graph" items="${grList}">
                                            <div class="graph" id="graph<%= j%>">
                                                <c:forEach var="st" items="${graph.key.statementList}"> 
                                                    <li><span <c:if test="${!empty againInferable&&!againInferable}">style="cursor: default;"</c:if> <c:if test="${empty againInferable||againInferable}">onclick="<%if (request.getParameter("invReas") == null) {
                                                            out.println("showOrHidePathGr(" + j + ")");
                                                        } else {
                                                            out.println("selectStatementOrGraph(graph" + j + ",'#6A5ACD')");
                                                        }%>" </c:if> >${st.subject}&nbsp;&nbsp; ${st.predicate}&nbsp;&nbsp; ${st.object}</span></li>
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
                            <c:when test="${!empty newResBean}">
                                <c:choose>
                                    <c:when test="${!againInferable}">
                                        <p class="numRes">The graphs are again inferable </p><br>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="numRes">The graphs are not again inferable </p><br>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${!empty ontologyTriple&&! empty param.invReas}">
                                <p class="numRes">Choose one ore more ontology triples</p>
                                <div class="resWrap">
                                    <ul class="resList">
                                        <% int k = 0;%>
                                        <c:forEach var="ontTriple" items="${ontologyTriple}">
                                            <li class="ontStatement" id="ont<%=k%>" onmouseover="checkIfNeedDisableTriple(<%= "ont" + k%>)"><span onclick="<%out.println("selectStatementOrGraph(ont" + k + ",'#8cff3f')");%>">${ontTriple.subject}&nbsp;&nbsp;${ontTriple.predicate}&nbsp;&nbsp;${ontTriple.object}</span></li>
                                                <% k = k + 1;%>
                                            </c:forEach>
                                    </ul>
                                </div>
                            </c:when>
                        </c:choose>
                        <c:choose>
                            <c:when test="${!empty newResBean||(!(numTr>0)&&!(numGr>0))}">
                            </c:when>
                            <c:when test="${empty param.invReas}">
                                <form method="get" action="<%= request.getContextPath()%>/reasoningResults/inverseReasoning">
                                    <p class="results">do you want to see how the reasoning process changes after removal of ontology triples? </p>
                                    <input type="hidden" name="invReas" value="true"/>
                                    <button class="upBut" type="submit">Click Here</button> 
                                </form> 
                            </c:when>
                            <c:otherwise>
                                <div id="buttonDiv">
                                <form:form modelAttribute="invReasBean" class="formReas" method="post" action="${pageContext.request.contextPath}/reasoningResults">
                                    <input type="hidden" name="againInferable" value="true">
                                    <button class="upBut" type="submit">Which graphs and/or triples are again inferable?</button>
                                </form:form>
                                <form:form modelAttribute="invReasBean" class="formReas" method="post" action="${pageContext.request.contextPath}/reasoningResults">
                                    <input type="hidden" name="againInferable" value="false">
                                    <button class="upBut" type="submit">Which graphs and/or triples are not again inferable?</button>
                                </form:form> 
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${!empty results}">
                        <p class="results">Reasoning not produced results</p>
                    </c:when>
                    <c:otherwise>        
                        <p class="error">you need to begin the reasoning process</p>
                    </c:otherwise>
                </c:choose>   
            </section>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
