<%-- 
    Document   : loadinferenceRules
    Created on : 20-mar-2017, 21.22.04
    Author     : F.Camerlengo
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%= request.getContextPath()%>/resources/style/style.css" rel="stylesheet" type="text/css">
        <title>Load Inference Rules</title>
    </head>
    <body>
        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        <div id="content">
            <%@ include file="../../resources/staticPages/header.jsp"  %>
            <c:choose>
                <c:when test="${!empty param.loadFile}">
                    <section class="backgroundHeader1" id="infPageLoad">
                        <p>choose an inference rules file to be loaded</p>
                        <form:form cssClass="backgroundHeader2" modelAttribute="fileUpload" method="post" action="${contextPath}/upload/inferenceRules" enctype="multipart/form-data">
                            <c:choose>
                                <c:when test="${!empty param.stBut}">
                                    <input type="hidden" name="startButton" value="true"/>
                                </c:when>
                            </c:choose>
                            <input type="file" name="file"/><br>
                            <input type="submit" class="upBut" value="Upload">
                            <c:choose>
                                <c:when test="${param.outcome=='loadErr'}">
                                    <p class="error"> error on loading of inference rules file</p>
                                </c:when>
                                <c:when test="${param.outcome=='uploadErr'}">
                                    <p class="error">error on upload of inference rules file</p>
                                </c:when>
                            </c:choose>
                        </form:form>
                        <c:choose>
                            <c:when test="${!empty rulesBean}">
                                <p id="lastRule">last inference rules file loaded: <span>${rulesBean.fileName}</span></p>     
                            </c:when>
                            <c:otherwise>
                                <br><br>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </c:when>
                <c:when test="${param.outcome =='success'}">
                    <section class="backgroundHeader1" id="infPage">
                        <p class="success">inference rules file loaded correctly</p>
                    </section>
                </c:when>
                <c:when test="${param.outcome=='loadErr'}">
                    <section class="backgroundHeader1" id="infErrPage">
                        <p class="error" >error on loading of inference rules file</p>
                    </section>
                </c:when>
                <c:when test="${param.outcome=='valErr'}">
                    <section class="backgroundHeader1" id="infErrPage">
                        <p class="error">error in the validation of rules</p>
                    </section>
                </c:when>
                <c:when test="${param.outcome=='redirect'}">
                    <section class="backgroundHeader1" id="infErrPage">
                        <p class="error" >you can not edit the inference rules files before it is loaded</p>
                    </section>
                </c:when>
                <c:otherwise>   
                    <section class="backgroundHeader1" id="infPage">
                        <p>Choose an option</p>
                        <div id="inferenceRules">
                            <form method="get" action="${contextPath}/upload/inferenceRules">
                                <c:choose>
                                    <c:when test="${!empty param.stBut}">
                                        <input type="hidden" name="stBut" value="true"/>
                                    </c:when>
                                </c:choose>
                                <button type="submit" class="backgroundHeader1" >Load Inference Rules</button>
                            </form>   
                            <form method="get" action="${contextPath}/loadInferenceRules/default">
                                <c:choose>
                                    <c:when test="${!empty param.stBut}">
                                        <input type="hidden" name="stBut" value="true"/>
                                    </c:when>
                                </c:choose>
                                <button type="submit" class="backgroundHeader1" >Use Default Inference Rules</button>
                            </form>
                            <form method="get" action="${contextPath}/inferenceRules/edit">
                                <c:choose>
                                    <c:when test="${!empty param.stBut}">
                                        <input type="hidden" name="stBut" value="true"/>
                                    </c:when>
                                </c:choose>
                                <button class="backgroundHeader1" type="submit" >Edit Inference Rules</button>
                            </form>
                        </div>
                    </section>
                </c:otherwise> 
            </c:choose>
            <%@ include file="../../resources/staticPages/footer.jsp" %>
        </div>
    </body>
</html>
