/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.model.service.IServiceFactory;
import it.uniroma2.ase.view.requestBean.InverseReasoningBean;
import it.uniroma2.ase.view.responseBean.ResultsBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.rdf4j.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F.Camerlengo
 */
@Controller
@RequestMapping(value="/reasoningResults")
@SessionAttributes({"resBean"})
public class ReasoningResultsController {
    
    @Autowired
    private IServiceFactory serviceFactory;
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView showReasoningResults(){
        return new ModelAndView("reasoningResults");
    }
    
    @RequestMapping(value="/inverseReasoning",method=RequestMethod.GET)
    public ModelAndView startInverseReasoningProcess(@RequestParam("invReas")String invReas){
        ModelAndView mv;
        mv=new ModelAndView("redirect:/reasoningResults");
        mv.addObject("invReas",invReas);
        InverseReasoningBean invReasBean=new InverseReasoningBean();
        mv.addObject("invReasBean",invReasBean);
        return mv;
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView executeInverseReasoningProcess(HttpServletRequest request,@ModelAttribute("invReasBean")InverseReasoningBean invReasBean){
        ModelAndView mv;
        List<String> elementsId = invReasBean.getElementsId();
        List<Integer> statementId=new ArrayList<>();
        List<Integer> graphsId=new ArrayList<>();
        List<Integer> ontologyId=new ArrayList<>();
        if(elementsId==null){
            mv=new ModelAndView("redirect:/reasoningResults");
            mv.addObject("invReas","true");
            return mv;
        }
        for(String id:elementsId){
            if(id.startsWith("statement")){
                String[] split = id.split("[a-z]");
                statementId.add(Integer.parseInt(split[split.length-1]));
            }
            if(id.startsWith("graph")){
                String[] split=id.split("[a-z]");
                graphsId.add(Integer.parseInt(split[split.length-1]));
            }
            if(id.startsWith("ont")){
                String[] split=id.split("[a-z]");
                ontologyId.add(Integer.parseInt(split[split.length-1]));
            }
        }
        if(ontologyId.isEmpty()){
            mv=new ModelAndView("redirect:/reasoningResults");
            mv.addObject("invReas","true");
            return mv;
        }
        ResultsBean resBean = (ResultsBean) request.getSession().getAttribute("resBean");
        QueriesResults queriesResults = resBean.getQueriesResults();
        QueriesResults results = serviceFactory.getInverseReasoningService().executeInverseReasoningProcess(statementId, graphsId, ontologyId,queriesResults,invReasBean.isAgainInferable());
        ResultsBean newResBean= new ResultsBean();
        newResBean.setQueriesResults(results);
        mv=new ModelAndView("/reasoningResults");
        mv.addObject("newResBean",newResBean);
        if(statementId.isEmpty()){
            mv.addObject("pastNumSt",resBean.getQueriesResults().getNumberOfInferredTriples());
        }else{
            mv.addObject("pastNumSt",statementId.size());
        }
        if(graphsId.isEmpty()){
            mv.addObject("pastNumGr",resBean.getQueriesResults().getNumberOfInferredGraphs());
        }else{
            mv.addObject("pastNumGr",graphsId.size());
        }
        System.out.println(invReasBean.isAgainInferable());
        mv.addObject("againInferable",invReasBean.isAgainInferable());
        mv.addObject("exeInv","true");
        return mv;
    }
}
