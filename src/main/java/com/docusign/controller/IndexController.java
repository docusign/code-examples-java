package com.docusign.controller;

import com.docusign.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    Session session;

    @RequestMapping(path = "/",method = RequestMethod.GET)
    public String index(ModelMap model) {

        model.addAttribute("title","Home");
        return "pages/index";
    }

    @RequestMapping(path = "/ds/mustAuthenticate",method = RequestMethod.GET)
    public String mustAuthenticateController(ModelMap model) {
        model.addAttribute("title", "Authenticate with DocuSign");
        return "pages/ds_must_authenticate";
    }

    @RequestMapping(path = "/ds-return",method = RequestMethod.GET)
    public String returnController(@RequestParam(value="state", required = false) String state,
                                   @RequestParam(value="event", required = false) String event,
                                   @RequestParam(value="envelopeId", required = false) String envelopeId,
                                   ModelMap model) {
        model.addAttribute("title" , "Return from DocuSign");
        model.addAttribute("event", event);
        model.addAttribute("state", state);
        model.addAttribute("qpEnvelopeId", envelopeId);
        return "pages/ds_return";
    }
}
