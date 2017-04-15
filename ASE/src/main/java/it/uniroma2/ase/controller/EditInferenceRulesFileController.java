/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import it.uniroma2.ase.view.responseBean.InferenceRulesBean;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
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
@RequestMapping(value = "/inferenceRules/edit")
@SessionAttributes({"rulesBean"})
public class EditInferenceRulesFileController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showInferenceRulesFile(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv;
        HttpSession session = request.getSession();
        InferenceRulesBean rulesBean = (InferenceRulesBean) session.getAttribute("rulesBean");
        if (rulesBean == null) {
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "redirect");
            return mv;
        }
        String filePath = rulesBean.getFilePath();
        String fileName = rulesBean.getFileName();
        String dest = filePath.replace(fileName, "buffer.txt");
        try {
            File file = new File(filePath);
            File fileDest = new File(dest);
            FileUtils.copyFile(file, fileDest);
            Desktop.getDesktop().edit(fileDest);
            mv = new ModelAndView("/changeConfirmation");
            mv.addObject("filePath", dest);
            return mv;
        } catch (IOException ex) {
            System.err.println("an error was occurred");
            System.err.println(ex);
            System.err.println("Message: " + ex.getMessage() + "\nCause: " + ex.getCause());
            mv = new ModelAndView("redirect:/loadInferenceRules");
            mv.addObject("outcome", "loadErr");
            return mv;
        }
    }
    
}
