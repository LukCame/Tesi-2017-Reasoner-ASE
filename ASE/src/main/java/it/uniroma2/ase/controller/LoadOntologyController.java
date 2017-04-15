/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;


import it.uniroma2.ase.view.requestBean.FileUploadBean;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/loadOntology")
@SessionAttributes({"ontBean"})
public class LoadOntologyController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getLoadOntologyPage(HttpServletRequest request,@RequestParam(value="stBut",required=false) String stBut) {
       ModelAndView mv=new ModelAndView("loadOntology");
       if(stBut!=null){
        mv.addObject("stBut", stBut);
       }
       FileUploadBean fileUpload=new FileUploadBean();
       mv.addObject("fileUpload",fileUpload);
       return mv;
    }
  
    
}
