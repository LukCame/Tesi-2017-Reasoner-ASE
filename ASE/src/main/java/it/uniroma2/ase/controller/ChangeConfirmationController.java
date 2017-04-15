/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F.Camerlengo
 */
@Controller
@RequestMapping(value="/confirmation")
public class ChangeConfirmationController {
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView showConfirmationPage(@RequestParam("filePath")String filePath){
        ModelAndView mv= new ModelAndView("/changeConfirmation");
        mv.addObject("filePath",filePath);
        return mv;
    }
    
}
