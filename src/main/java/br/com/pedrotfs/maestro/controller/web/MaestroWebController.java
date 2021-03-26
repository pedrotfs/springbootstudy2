package br.com.pedrotfs.maestro.controller.web;

import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.facade.UpdateFacade;
import br.com.pedrotfs.maestro.facade.ViewFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import static br.com.pedrotfs.maestro.util.Constants.PANEL;

@Controller
public class MaestroWebController {

    @Autowired
    private ViewFacade viewFacade;

    @Autowired
    private UpdateFacade updateFacade;

    @GetMapping("/")
    public String getPanelPage(Model model) throws EntityIdNotFoundException {
        return viewFacade.getView(model);
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "register/register";
    }

    @GetMapping("/error")
    public String getErrorPage(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "error";
    }

    @GetMapping("/change-current/")
    public String setCurrentRegister(@RequestParam final String registerId) throws EntityIdNotFoundException {
        viewFacade.setCurrentRegister(registerId);
        return PANEL;
    }

    @GetMapping("/toggle-selection/")
    public String toggleSelection(@RequestParam final String buttonId) {
        viewFacade.toggleSelection(buttonId);
        return PANEL;
    }

    @GetMapping("/clear-selection/")
    public String clearSelection() {
        viewFacade.clearSelection();
        return PANEL;
    }

    @GetMapping("/remove-from-selection/")
    public String removeFromSelection(@RequestParam final String buttonId)  {
        viewFacade.removeFromSelection(buttonId);
        return PANEL;
    }

    @GetMapping("/update/")
    public String update(@RequestParam final String buttonId) throws EntityIdNotFoundException, InterruptedException {
        updateFacade.update(buttonId);
        Thread.sleep(1000);
        viewFacade.resetScreen(buttonId);

        return PANEL;
    }

    @GetMapping("/check/")
    public String check(@RequestParam final String drawId) {
        viewFacade.check(drawId);
        return PANEL;
    }

}
