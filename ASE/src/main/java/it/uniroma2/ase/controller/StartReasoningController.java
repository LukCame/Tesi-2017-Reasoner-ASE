/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.domain.Graph;
import it.uniroma2.ase.domain.Path;
import it.uniroma2.ase.domain.QueriesResults;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.RulesValidationException;
import it.uniroma2.ase.model.exception.RunTimeReasoningException;
import it.uniroma2.ase.model.service.IServiceFactory;
import it.uniroma2.ase.model.utility.OntologyUtility;
import it.uniroma2.ase.view.responseBean.ConfigurationBean;
import it.uniroma2.ase.view.responseBean.InferenceRulesBean;
import it.uniroma2.ase.view.responseBean.OntologyBean;
import it.uniroma2.ase.view.responseBean.ResultsBean;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F.Camerlengo
 */
@Controller
@RequestMapping(value="/startReasoning")
@SessionAttributes({"ontBean", "rulesBean","confBean","resBean"})
public class StartReasoningController {
    
    @Autowired
    private IServiceFactory serviceFactory;
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView showStartReasoningPage(HttpServletRequest request){
        ModelAndView mv;
        if(request.getParameter("stBut")!=null){
            mv = new ModelAndView("startReasoning/start");
            return mv;
        }
        HttpSession session = request.getSession();
        if(session.getAttribute("ontBean")==null||session.getAttribute("rulesBean")==null||session.getAttribute("confBean")==null){
            mv= new ModelAndView("startReasoning");
            mv.addObject("outcome","redirect");
        }else{
            mv= new ModelAndView("startReasoning");
        }
        return mv;
    }
    
    @RequestMapping(value="/start",method=RequestMethod.GET)
    public ModelAndView startReasoning(HttpServletRequest request){
        ModelAndView mv;
        HttpSession session = request.getSession();
        OntologyBean ontBean =  (OntologyBean) session.getAttribute("ontBean");
        RepositoryConnection repository = ontBean.getRepository();
        InferenceRulesBean rulesBean = (InferenceRulesBean) session.getAttribute("rulesBean");
        Rules rules = rulesBean.getRules();
        ConfigurationBean confBean = (ConfigurationBean) session.getAttribute("confBean");
        Integer numberOfExecution = confBean.getNumberOfExecution();
        List<Integer> rulesId = confBean.getRulesId();
        ResultsBean pastResBean = (ResultsBean) session.getAttribute("resBean");
        if(pastResBean!=null){
            repository.clear(repository.getValueFactory().createIRI(OntologyUtility.SUPPORT_ONTOLOGY_GRAPH));
        }
        try{
            QueriesResults results = serviceFactory.getReasoningService().executeReasoning(repository, rules, numberOfExecution, rulesId);
            mv=new ModelAndView("redirect:/reasoningResults");
            ResultsBean resBean=new ResultsBean();
            resBean.setQueriesResults(results);
            mv.addObject("resBean",resBean);
            return mv;
        }catch(RunTimeReasoningException ex){
            System.err.println("An error was occurred");
            System.err.println(ex);
            System.err.println("Message: "+ex.getMessage());
            System.err.println("Cause: "+ex.getCause());
            mv=new ModelAndView("redirect:/startReasoning");
            mv.addObject("outcome","runErr");
            return mv;
        }
    }
   
}
