package br.com.pedrotfs.maestro.controller.web;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.kafka.services.RegisterConsumerService;
import br.com.pedrotfs.maestro.kafka.services.RequestProducerService;
import br.com.pedrotfs.maestro.service.DrawCalculationsService;
import br.com.pedrotfs.maestro.service.RegisterService;
import br.com.pedrotfs.maestro.service.impl.DrawServiceImpl;
import br.com.pedrotfs.maestro.util.NumberGenerator;
import br.com.pedrotfs.maestro.util.ProbabilityDTO;
import br.com.pedrotfs.maestro.util.comparator.DrawIdComparator;
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

    @Autowired
    private RequestProducerService requestProducerService;

    @Autowired
    private RegisterConsumerService registerConsumerService;

    @Autowired
    private DrawCalculationsService drawCalculationsService;

    @Autowired
    private DrawServiceImpl drawService;

    private List<Register> registerList = new ArrayList<>();

    private Register currentRegister = null;

    private List<Integer> selectedNumbers = new ArrayList<>();

    private List<ProbabilityDTO> probabilityDTO = new ArrayList<>();

    private List<ProbabilityDTO> adviceCommon = new ArrayList<>();

    private List<ProbabilityDTO> adviceLesser = new ArrayList<>();

    private Draw higherDividend = null;

    private Draw higherAmount = null;

    private List<Draw> selectedDraws = new ArrayList<>();

    private List<Draw> lastCheckedDraws = new ArrayList<>();

    private int count = 0;

    private String checkValue = "check";

    private final static String PANEL = "panel";

    private final static Integer MAX = 5;

    private static final Boolean PREDICT_MASTER_KEY = Boolean.TRUE;

    @GetMapping("/")
    public String getPanelPage(Model model) throws EntityIdNotFoundException {
        model.addAttribute("time", LocalDateTime.now());
        if(registerList.isEmpty()) {
            registerList = registerService.getRegisters();
        }
        model.addAttribute("registers", registerList);
        if(currentRegister == null) {
            try {
                currentRegister = registerService.getSingleRegister("ltf");
            } catch(EntityIdNotFoundException e) {
                if(currentRegister == null) {
                    populateEssentialData();
                }
                else {
                    throw e;
                }
            }
        }
        if(probabilityDTO.isEmpty()) {
            probabilityDTO = drawCalculationsService.getListWithProbabilities(currentRegister);
        }
        if(adviceCommon.isEmpty()) {
            adviceCommon = numberGenerator.getCommonAdvice(probabilityDTO, currentRegister, false);
        }
        if(adviceLesser.isEmpty()) {
            adviceLesser = numberGenerator.getCommonAdvice(probabilityDTO, currentRegister, true);
        }
        if(higherAmount == null) {
            higherAmount = drawCalculationsService.getHigherAmount(currentRegister.get_id());
        }
        if(higherDividend == null) {
            higherDividend = drawCalculationsService.getHigherDividends(currentRegister.get_id());
        }
        if(count == 0) {
            count = drawCalculationsService.countDrawsByRegister(currentRegister.get_id());
        }
        if(!selectedNumbers.isEmpty()) {
            selectedDraws = drawService.findByRegisterIdAndNumberIn(currentRegister.get_id(), selectedNumbers);
            selectedDraws.sort(new DrawIdComparator());
            if(PREDICT_MASTER_KEY) {
                List<Integer> interval = new ArrayList<>();
                List<Integer> intervalGrowth = new ArrayList<>();
                Double predict = 0D;
                interval.add(0);
                Double adjust = 0D;
                selectedDraws.forEach(d -> {
                    interval.add(Integer.parseInt(d.get_id().substring(3)));
                });
                for(int i = 0; i < interval.size(); i++) {
                    if(i == 0) {
                        adjust = interval.get(i) + 0D;
                        predict = adjust;
                    } else {
                        adjust = (interval.get(i) - interval.get(i - 1) + 0D);
                        predict += adjust;
                    }
                }
                if(!selectedDraws.isEmpty()) {
                    predict = predict / selectedDraws.size();
                    final Integer lastDrawId = Integer.parseInt(selectedDraws.get(selectedDraws.size() - 1).get_id().substring(3));
                    while(predict + lastDrawId <= count) {
                        predict = predict + adjust;
                    }
                    model.addAttribute("predict", predict.intValue() + lastDrawId);
                    final int aroundIn = ((predict.intValue() + lastDrawId) - count) / 2;
                    model.addAttribute("aroundIn", aroundIn);
                }
            }
        } else {
            selectedDraws = new ArrayList<>();
        }
        //predictive will use selectedDraws for now

        treatCheckDrawsToMax();
        model.addAttribute("currentRegister", currentRegister);
        model.addAttribute("numbers", numberGenerator.generateNumbers(currentRegister));
        model.addAttribute("selectedNumbers", selectedNumbers);
        model.addAttribute("probabilityDTO", probabilityDTO);
        model.addAttribute("possible", numberGenerator.getPossibilities(currentRegister));
        model.addAttribute("higherDividend", higherDividend);
        model.addAttribute("higherAmount", higherAmount);
        model.addAttribute("count", count);
        model.addAttribute("adviceCommon", adviceCommon);
        model.addAttribute("adviceLesser", adviceLesser);
        model.addAttribute("selectedDraws", selectedDraws);
        model.addAttribute("selectedDrawsSize", selectedDraws.size());
        model.addAttribute("lastCheckedDraws", lastCheckedDraws);
        model.addAttribute("checkValue", checkValue);
        return PANEL;
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
                resetScreen(registerId);
            }
        }
        return PANEL;
    }

    @GetMapping("/toggle-selection/")
    public String toggleSelection(@RequestParam final String buttonId) {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        } else {
            if(currentRegister.getLimit() > selectedNumbers.size()) {
                selectedNumbers.add(Integer.parseInt(buttonId));
                Collections.sort(selectedNumbers);
            }
        }
        return PANEL;
    }

    @GetMapping("/clear-selection/")
    public String clearSelection(@RequestParam final String buttonId) {
        selectedNumbers = new ArrayList<>();
        selectedDraws = new ArrayList<>();
        return PANEL;
    }

    @GetMapping("/remove-from-selection/")
    public String removeFromSelection(@RequestParam final String buttonId)  {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        }
        return PANEL;
    }

    @GetMapping("/update/")
    public String update(@RequestParam final String buttonId) throws EntityIdNotFoundException, InterruptedException {
        requestProducerService.produceRequest(buttonId);
        registerConsumerService.consumeRegisters();
        Thread.sleep(1000);
        requestProducerService.produceRequest("");
        resetScreen(buttonId);
        return PANEL;
    }

    @GetMapping("/check/")
    public String check(@RequestParam final String drawId) throws EntityIdNotFoundException, InterruptedException {
        try {
            final String fullId = currentRegister.get_id() + drawId;
            final Draw singleRegister = drawService.getSingleDraws(fullId);

            if(lastCheckedDraws.stream().noneMatch(q -> q.get_id().equals(fullId))) {
                lastCheckedDraws.add(0, singleRegister);
            }
            treatCheckDrawsToMax();
            checkValue = drawId;
        } catch(EntityIdNotFoundException e) {
            System.out.println("register with id " + drawId + " not found. doing nothing");
        }
        return PANEL;
    }

    private void resetScreen(@RequestParam String buttonId) throws EntityIdNotFoundException {
        currentRegister = registerService.getSingleRegister(buttonId);
        selectedNumbers = new ArrayList<>();
        probabilityDTO = new ArrayList<>();
        adviceCommon = new ArrayList<>();
        adviceLesser = new ArrayList<>();
        selectedDraws = new ArrayList<>();
        lastCheckedDraws = new ArrayList<>();
        higherDividend = null;
        higherAmount = null;
        count = 0;
    }

    private void populateEssentialData() throws EntityIdNotFoundException {
        Document document = new Document("_id", "ltf").append("limit", 15).append("count", 25);
        mongoTemplate.insert(document, "register");
        document = new Document("_id", "mgs").append("limit", 6).append("count", 60);
        mongoTemplate.insert(document, "register");
        currentRegister = registerService.getSingleRegister("ltf");
    }

    private void treatCheckDrawsToMax() {
        if(lastCheckedDraws.size() > MAX) {
            lastCheckedDraws = lastCheckedDraws.subList(0,MAX);
        }
    }
}
