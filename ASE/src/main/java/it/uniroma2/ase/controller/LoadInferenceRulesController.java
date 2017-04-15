/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;
import it.uniroma2.ase.model.exception.RulesValidationException;
import it.uniroma2.ase.model.service.IServiceFactory;
import it.uniroma2.ase.view.responseBean.InferenceRulesBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = "/loadInferenceRules")
@SessionAttributes({"rulesBean"})
public class LoadInferenceRulesController {

    @Autowired
    private IServiceFactory serviceFactory;

    @RequestMapping(method = RequestMethod.GET)
    public String showLoadInferenceRulesPage() {
        return "loadInferenceRules";
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public ModelAndView useDefaultInferenceRules(HttpServletRequest request) {
        ModelAndView mv;
        String path = request.getServletContext().getRealPath("");
        String projectName = request.getContextPath().substring(1);
        path = path.substring(0, path.indexOf(request.getContextPath().substring(1)));
        String filePath = path + projectName + "\\InferenceRules\\default\\NuoveRegole.txt";
        String dest = path + projectName + "\\InferenceRules\\default.txt";
        File directory = new File(path + projectName + "\\InferenceRules\\");
        File[] listFiles = directory.listFiles();
        for (File file : listFiles) {
            if (!file.getName().equals("default.txt")) {
                file.delete();
            }
        }
        File file = new File(filePath);
        try {
            try {
                FileUtils.copyFile(file, new File(dest));
            } catch (IOException ex) {
                System.err.println("an error was occurred");
                System.err.println(ex);
                System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
                mv = new ModelAndView("redirect:/loadInferenceRules");
                mv.addObject("outcome", "loadErr");
                if (request.getParameter("stBut") != null) {
                    mv.addObject("stBut", "true");
                }
                return mv;
            }
            Rules rules = serviceFactory.getInferenceRulesService().parseInferenceRulesFile(dest);
            rules = serviceFactory.getRuleGraphConverterService().convertOnRuleGraphFromRule(rules);
            try {
                serviceFactory.getValidationRulesService().validate(rules);
            } catch (RulesValidationException ex) {
                System.err.println("An error was occurred");
                System.err.println(ex);
                System.err.println("Message: " + ex.getMessage());
                System.err.println("Cause: " + ex.getCause());
                mv = new ModelAndView("redirect:/loadInferenceRules");
                mv.addObject("outcome", "valErr");
                return mv;
            }
            Rules rewriteRules = serviceFactory.getRewriteRulesService().rewriteRules(rules);
            if (request.getParameter("stBut") == null) {
                mv = new ModelAndView("redirect:/loadInferenceRules");
                mv.addObject("outcome", "success");
            } else {
                mv = new ModelAndView("redirect:/reasoningParameters");
                mv.addObject("stBut", "true");
            }
            InferenceRulesBean rulesBean = new InferenceRulesBean();
            rulesBean.setFileName("default.txt");
            rulesBean.setFilePath(dest);
            rulesBean.setRules(rewriteRules);
            mv.addObject("rulesBean", rulesBean);
            return mv;
        } catch (InferenceRulesParsingException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadErr");
            if (request.getParameter("stBut") != null) {
                mv.addObject("stBut", "true");
            }
            return mv;
        }
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public void showInferenceRulesFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        InferenceRulesBean rulesBean = (InferenceRulesBean) session.getAttribute("rulesBean");
        if (rulesBean == null) {
            return;
        }
        String filePath = rulesBean.getFilePath();
        response.setContentType("text/plain");
        try {
            PrintWriter out = response.getWriter();
            FileInputStream file = new FileInputStream(new File(filePath));
            int i;
            while ((i = file.read()) != -1) {
                out.write(i);
            }
            out.close();
            file.close();
        } catch (IOException ex) {

        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView loadEditInferenceRulesFile(HttpServletRequest request, @RequestParam("filePath") String filePath) {
        ModelAndView mv;
        HttpSession session = request.getSession();
        InferenceRulesBean rulesBean = (InferenceRulesBean) session.getAttribute("rulesBean");
        Rules rules=null;
        try {
            rules = serviceFactory.getInferenceRulesService().parseInferenceRulesFile(filePath);
        } catch (InferenceRulesParsingException ex) {
            File file = new File(filePath);
            file.delete();
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadErr");
            return mv;
        }
        rules=serviceFactory.getRuleGraphConverterService().convertOnRuleGraphFromRule(rules);
        try{
        serviceFactory.getValidationRulesService().validate(rules);
        }catch(RulesValidationException ex){
            System.err.println("An error was occurred");
            System.err.println(ex);
            System.err.println("Message: "+ex.getMessage());
            System.err.println("Cause: "+ex.getCause());
            mv=new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome","valErr");
            return mv;
        }
        Rules rewriteRules = serviceFactory.getRewriteRulesService().rewriteRules(rules);
        rulesBean.setRules(rules);
        try {
            FileUtils.copyFile(new File(filePath), new File(rulesBean.getFilePath()));
        } catch (IOException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadErr");
            return mv;
        }
        mv = new ModelAndView("redirect:/loadInferenceRules");
        mv.addObject("rulesBean", rulesBean);
        mv.addObject("outcome", "success");
        return mv;
    }
}
