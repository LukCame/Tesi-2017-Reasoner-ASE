<header>
    <div id="headerTop" class="backgroundHeader2">
    </div>
    <div id="headerDiv" class="backgroundHeader1" >
        <div id="eyeDiv">
            <img src="${pageContext.request.contextPath}/resources/images/eye.png" alt="eye-image" >
        </div>
        <div id="contentHeaderDiv">
            <p> <span id="acr">ase</span><span id="contentHeader"> All Seeing Eye </span></p>
        </div>
    </div>
    <nav class="backgroundHeader2">
        <ul>
            <li><a href="<%=request.getContextPath() %>/loadOntology">load ontology</a></li>
            <li><a href="<%=request.getContextPath()%>/loadInferenceRules">load inference rules</a></li>
            <li><a href="<%=request.getContextPath()%>/reasoningParameters">reasoning parameters</a></li>
            <li><a href="<%=request.getContextPath()%>/startReasoning">start reasoning</a></li>   
            <li><a href="<%=request.getContextPath()%>/reasoningResults">reasoning results</a></li> 
        </ul>
    </nav>   
</header>