package it.uniroma2.ase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Questa classe intercetta le richieste di accesso alla pagina principale (uri:
 * “/”) e restituisce quest’ultima all’interfaccia utente.
 *
 * @author Gruppo Talocci
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }
       
}
