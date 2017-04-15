/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.InferenceRuleFileFormatException;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;
import it.uniroma2.ase.model.exception.LoadOntologyException;
import it.uniroma2.ase.model.exception.RulesValidationException;
import it.uniroma2.ase.model.exception.UploadFileException;
import it.uniroma2.ase.model.service.IServiceFactory;
import it.uniroma2.ase.view.requestBean.FileUploadBean;
import it.uniroma2.ase.view.responseBean.InferenceRulesBean;
import it.uniroma2.ase.view.responseBean.OntologyBean;
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F.Camerlengo
 */
@Controller
@RequestMapping("/upload")
@SessionAttributes({"ontBean", "rulesBean"})
public class UploadController {

    @Autowired
    IServiceFactory serviceFactory;

    @RequestMapping(value = "/ontology", method = RequestMethod.POST)
    public ModelAndView uploadOntology(HttpServletRequest request, @ModelAttribute("fileUpload") FileUploadBean fileUpload) {
        ModelAndView mv;
        if (fileUpload.getFile().isEmpty()) {
            mv = new ModelAndView("redirect:/loadOntology");
            mv.addObject("outcome", "redirect");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", fileUpload.getStartButton());
            }
            return mv;
        }
        String path = request.getServletContext().getRealPath("");
        String projectName = request.getContextPath().substring(1);
        path = path.substring(0, path.indexOf(request.getContextPath().substring(1)));
        path = path + projectName + "\\Ontology";
        File directory = new File(path + "\\");
        File[] listFiles = directory.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
        try {
            String fileName = serviceFactory.getUploadService().processUpload(fileUpload.getFile(), path);
            RepositoryConnection repository = serviceFactory.getOntologyService().loadOntology(path + "/" + fileName);
            if (fileUpload.getStartButton() == null) {
                mv = new ModelAndView("redirect:/loadOntology");
                mv.addObject("outcome", "success");
            } else {
                mv = new ModelAndView("redirect:/loadInferenceRules");
                mv.addObject("stBut", fileUpload.getStartButton());
            }
            OntologyBean ontBean = new OntologyBean();
            ontBean.setFileName(fileName);
            ontBean.setFilePath(path + "/" + fileName);
            ontBean.setRepository(repository);
            mv.addObject("ontBean", ontBean);
            return mv;
        } catch (UploadFileException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadOntology");
            mv.addObject("outcome", "uploadErr");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", "true");
            }
            return mv;
        } catch (LoadOntologyException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadOntology");
            mv.addObject("outcome", "loadErr");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", "true");
            }
            return mv;
        }
    }

    @RequestMapping(value = "/inferenceRules", method = RequestMethod.GET)
    public ModelAndView showInferenceRulesFileForm(HttpServletRequest request, @RequestParam(value = "stBut", required = false) String stBut) {
        ModelAndView mv = new ModelAndView("redirect:/loadInferenceRules");
        if (stBut != null) {
            mv.addObject("stBut", stBut);
        }
        mv.addObject("loadFile", "true");
        FileUploadBean fileUpload = new FileUploadBean();
        mv.addObject("fileUpload", fileUpload);
        return mv;
    }

    @RequestMapping(value = "/inferenceRules", method = RequestMethod.POST)
    public ModelAndView uploadInferenceRules(HttpServletRequest request, @ModelAttribute("fileUpload") FileUploadBean fileUpload) {
        ModelAndView mv;
        if (fileUpload.getFile().isEmpty()) {
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadRedirect");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", fileUpload.getStartButton());
            }
            return mv;
        } else if (!fileUpload.getFile().getContentType().equals("text/plain")) {
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "uploadErr");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", fileUpload.getStartButton());
            }
            InferenceRuleFileFormatException ex = new InferenceRuleFileFormatException("The inference rule file extension must be .txt");
            InferenceRulesParsingException exception = new InferenceRulesParsingException(ex);
            System.err.println("an error was occurred");
            System.err.println(exception);
            System.err.println("Message: " + exception.getMessage() + "\nCause: " + exception.getCause());
            return mv;
        }
        String path = request.getServletContext().getRealPath("");
        String projectName = request.getContextPath().substring(1);
        path = path.substring(0, path.indexOf(request.getContextPath().substring(1)));
        path = path + projectName + "\\InferenceRules";
        String fileName=null;
        Rules rules=null;
        try {
            fileName = serviceFactory.getUploadService().processUpload(fileUpload.getFile(), path);
            rules = serviceFactory.getInferenceRulesService().parseInferenceRulesFile(path + "/" + fileName);
        } catch (UploadFileException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "uploadErr");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", "true");
            }
            return mv;
        } catch (InferenceRulesParsingException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadErr");
            if (fileUpload.getStartButton() != null) {
                mv.addObject("stBut", "true");
            }
            return mv;
        }
        if (fileUpload.getStartButton() == null) {
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "success");
        } else {
            mv = new ModelAndView("redirect:/reasoningParameters");
            mv.addObject("stBut", fileUpload.getStartButton());
        }
        rules = serviceFactory.getRuleGraphConverterService().convertOnRuleGraphFromRule(rules);
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
        InferenceRulesBean rulesBean = new InferenceRulesBean();
        rulesBean.setFileName(fileName);
        File directory = new File(path + "\\");
        File[] listFiles = directory.listFiles();
        for (File file : listFiles) {
            if (!file.getName().equals(fileName)) {
                file.delete();
            }
        }
        rulesBean.setFilePath(path + "/" + fileName);
        rulesBean.setRules(rewriteRules);
        mv.addObject("rulesBean", rulesBean);
        return mv;

    }
}
