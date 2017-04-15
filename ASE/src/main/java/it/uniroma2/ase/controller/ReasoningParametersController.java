/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.view.requestBean.ReasoningConfigurationBean;
import it.uniroma2.ase.view.responseBean.ConfigurationBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F.Camerlengo
 */
@Controller
@RequestMapping(value = "/reasoningParameters")
@SessionAttributes({"confBean"})
public class ReasoningParametersController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showConfigurationParametersPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("reasoningParameters");
        ReasoningConfigurationBean configuration = new ReasoningConfigurationBean();
        mv.addObject("configuration", configuration);
        if (request.getParameter("stBut") != null) {
            mv.addObject("stBut", "true");
        }
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitConfigurationParameters(@ModelAttribute("configuration") ReasoningConfigurationBean configuration) {
        ModelAndView mv;
        if ((configuration.getNumberOfExecutions() == null && !configuration.isUnset()) || (configuration.getNumberOfExecutions() != null && configuration.isUnset())) {
            mv = new ModelAndView("redirect:/reasoningParameters");
            mv.addObject("outcome", "redirect");
            mv.addObject("motivation", "iteration");
            if (configuration.getStartButton() != null) {
                mv.addObject("stBut", configuration.getStartButton());
            }
            return mv;
        }
        if ((configuration.getRulesApply().isEmpty() && !configuration.isAllRules()) || (!configuration.getRulesApply().isEmpty() && configuration.isAllRules())) {
            mv = new ModelAndView("redirect:/reasoningParameters");
            mv.addObject("outcome", "redirect");
            mv.addObject("motivation", "rules");
            if (configuration.getStartButton() != null) {
                mv.addObject("stBut", configuration.getStartButton());
            }
            return mv;
        }
        List<Integer> rulesId=null;
        if (!configuration.getRulesApply().isEmpty()) {
            String[] rules = configuration.getRulesApply().split(",");
            rulesId = new ArrayList<>();
            for (String rule : rules) {
                try {
                    int ruleId = Integer.parseInt(rule);
                    rulesId.add(ruleId);
                } catch (NumberFormatException ex) {
                    mv = new ModelAndView("redirect:/reasoningParameters");
                    mv.addObject("outcome", "redirect");
                    mv.addObject("motivation", "rules");
                    if (configuration.getStartButton() != null) {
                        mv.addObject("stBut", configuration.getStartButton());
                    }
                    return mv;
                }
            }
        }
        ConfigurationBean confBean=new ConfigurationBean();
        if(configuration.getNumberOfExecutions()==null){
            confBean.setNumberOfExecution(null);
        }else{
            confBean.setNumberOfExecution(configuration.getNumberOfExecutions());
        }
        if(configuration.getRulesApply().isEmpty()){
            confBean.setRulesId(null);
        }else{
            confBean.setRulesId(rulesId);
        }
        if(configuration.getStartButton()!=null){
             mv=new ModelAndView("redirect:/startReasoning/start");
             mv.addObject("confBean",confBean);
             mv.addObject("stBut","true");
        }else{
            mv=new ModelAndView("redirect:/reasoningParameters");
            mv.addObject("confBean",confBean);
            mv.addObject("outcome","success");
        }
        return mv;
    }

}
