package br.com.pedrotfs.maestro.controller.web;

import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.service.RegisterService;
import br.com.pedrotfs.maestro.util.NumberGenerator;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MaestroWebController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private NumberGenerator numberGenerator;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Register> registerList = new ArrayList<>();

    private Register currentRegister = null;

    private List<Integer> selectedNumbers = new ArrayList<>();

    @GetMapping("/")
    public String getPanelPage(Model model) throws EntityIdNotFoundException {
        model.addAttribute("time", LocalDateTime.now());
        if(registerList.isEmpty()) {
            registerList = registerService.getRegisters();
        }
        model.addAttribute("registers", registerList);
        if(currentRegister == null) {
            currentRegister = registerService.getSingleRegister("ltf");
            if(currentRegister == null) {
                populateEssentialData();
            }
        }
        model.addAttribute("currentRegister", currentRegister);
        model.addAttribute("numbers", numberGenerator.generateNumbers(currentRegister));
        model.addAttribute("selectedNumbers", selectedNumbers);
        model.addAttribute("possible", numberGenerator.getPossibilities(currentRegister));
        return "panel";
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
        for (Register register : registerList) {
            if(register.get_id().equalsIgnoreCase(registerId)) {
                currentRegister = registerService.getSingleRegister(registerId);
                selectedNumbers = new ArrayList<>();
            }
        }
        return "panel";
    }

    @GetMapping("/toggle-selection/")
    public String toggleSelection(@RequestParam final String buttonId) {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        } else {
            if(currentRegister.getLimit() >= selectedNumbers.size()) {
                selectedNumbers.add(Integer.parseInt(buttonId));
                Collections.sort(selectedNumbers);
            }
        }
        return "panel";
    }

    @GetMapping("/remove-from-selection/")
    public String removeFromSelection(@RequestParam final String buttonId)  {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        }
        return "panel";
    }

    @GetMapping("/update/")
    public String update(@RequestParam final String buttonId)  {
        return "panel";
    }

    private void populateEssentialData() throws EntityIdNotFoundException {
        Document document = new Document("_id", "ltf").append("limit", 15).append("count", 25);
        mongoTemplate.insert(document);
        currentRegister = registerService.getSingleRegister("ltf");
    }
}
