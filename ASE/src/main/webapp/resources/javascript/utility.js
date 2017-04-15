function showOrHide(id){
    var path=document.getElementById(id);
    if(path.style.display==="none"){
        path.style.display="block";
    }else{
        path.style.display="none";
    }
}

function showOrHidePathSt(element){
    var id="path".concat(element);
    showOrHide(id);
}

function showOrHidePathGr(element){
    var id="pathGr".concat(element);
    showOrHide(id);
}

function validateReasoningCheckBoxParamForm(element1,element2){
    var input2=document.getElementById(element2);
    var input1=document.getElementById(element1);
    if(input2.value){    
        input1.style.pointerEvents="none";
    }else{
        input1.style.pointerEvents="auto";
    }
}

function validateReasoningTextParamForm(element1,element2){
    var input1=document.getElementById(element1);
    if(input1.checked===true){
        var input2=document.getElementById(element2);
        input2.style.pointerEvents="none";
    }
}

function validateReasoningActiveTextParamForm(element1,element2){
    var input1=document.getElementById(element1);
    if(input1.checked===false){
        var input2=document.getElementById(element2);
        input2.style.pointerEvents="auto";
    }
}

function selectStatementOrGraph(element,color){
    var form=document.getElementsByClassName("formReas");
    for(i=0;i<form.length;i++){
        var children=form[i].children;
        var find=false;
        for(j=0;j<children.length;j++){
            if(children[j].getAttribute("value")===element.getAttribute("id")){
                children[j].remove();
                find=true;
                element.style.backgroundColor=color;
                break;
            }
        }
        if(find===false){
            var node=document.createElement("input");
            node.type="hidden";
            node.name="elementsId";
            node.value=element.getAttribute("id");
            form[i].appendChild(node);
            element.style.backgroundColor="red";
        }
    }
}

function checkIfNeedDisableTriple(element){
    var text=element.innerHTML;
    if(text.search("http://www.w3.org/1999/02/22-rdf-syntax-ns#rest")!==-1||text.search("http://www.w3.org/1999/02/22-rdf-syntax-ns#first")!==-1){
        element.style.pointerEvents="none";
    }
}

function startReasoning(element,type){
    var button=document.getElementById(element);
    button.style.display="none";
    var loader=document.getElementsByClassName("loader");
    loader[0].style.display="block";
    if(type===2){
        var newElem=document.createElement("p");
        newElem.className="success";
        newElem.innerHTML="the reasoning process was started";
        button.parentNode.parentNode.appendChild(newElem);
    }
}
