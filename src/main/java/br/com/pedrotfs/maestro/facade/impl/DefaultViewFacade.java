package br.com.pedrotfs.maestro.facade.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.facade.ViewFacade;
import br.com.pedrotfs.maestro.service.DrawCalculationsService;
import br.com.pedrotfs.maestro.service.RegisterService;
import br.com.pedrotfs.maestro.service.impl.DrawServiceImpl;
import br.com.pedrotfs.maestro.util.CheckDrawsMaxHelper;
import br.com.pedrotfs.maestro.util.NumberGenerator;
import br.com.pedrotfs.maestro.util.ProbabilityDTO;
import br.com.pedrotfs.maestro.util.comparator.DrawIdComparator;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.pedrotfs.maestro.util.Constants.*;

@Component
public class DefaultViewFacade implements ViewFacade {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private NumberGenerator numberGenerator;

    @Autowired
    private DrawCalculationsService drawCalculationsService;

    @Autowired
    private DrawServiceImpl drawService;

    @Autowired
    private MongoTemplate mongoTemplate;

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


    @Override
    public String getView(final Model model) {
        final LocalDateTime now = LocalDateTime.now();
        final String time = now.getHour() + ":" + now.getMinute() + ":" +  now.getSecond();
        model.addAttribute("time", time);

        populateRegisterInfo(model);
        populateAuxiliaryInformation(model);
        fillModell(model);
        return PANEL;
    }

    private void populateAuxiliaryInformation(Model model) {
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
                doPredictions(model);
            }
        } else {
            selectedDraws = new ArrayList<>();
        }
        CheckDrawsMaxHelper.treatCheckDrawsToMax(lastCheckedDraws);
    }

    private void populateRegisterInfo(Model model) {
        if(registerList.isEmpty()) {
            registerList = registerService.getRegisters();
        }
        model.addAttribute("registers", registerList);

        if(currentRegister == null) {
            try {
                currentRegister = registerService.getSingleRegister("mgs");
            } catch(EntityIdNotFoundException e) {
                try {
                    populateEssentialData();
                } catch (EntityIdNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void fillModell(final Model model) {
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
    }

    private void populateEssentialData() throws EntityIdNotFoundException {
        Document document = new Document("_id", "ltf").append("limit", 15).append("count", 25);
        mongoTemplate.insert(document, "register");
        document = new Document("_id", "mgs").append("limit", 6).append("count", 60);
        mongoTemplate.insert(document, "register");
        currentRegister = registerService.getSingleRegister("ltf");
    }

    private void doPredictions(final Model model) {
        List<Integer> interval = new ArrayList<>();
        List<Integer> intervalGrowth = new ArrayList<>();
        double predict = 0D;
        interval.add(0);
        double adjust = 0D;
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
            model.addAttribute("predict", ((int) predict) + lastDrawId);
            final int aroundIn = (( ((int) predict) + lastDrawId) - count) / 2;
            model.addAttribute("aroundIn", aroundIn);
        }
    }

    @Override
    public void resetScreen(String buttonId) throws EntityIdNotFoundException {
        currentRegister = registerService.getSingleRegister(buttonId);
        selectedNumbers = new ArrayList<>();
        probabilityDTO = new ArrayList<>();
        adviceCommon = new ArrayList<>();
        adviceLesser = new ArrayList<>();
        selectedDraws = new ArrayList<>();
        lastCheckedDraws = new ArrayList<>();
        higherAmount = null;
        higherDividend = null;
        count = 0;
    }

    @Override
    public void setCurrentRegister(String registerId) throws EntityIdNotFoundException {
        for (Register register : registerList) {
            if(register.get_id().equalsIgnoreCase(registerId)) {
                resetScreen(registerId);
            }
        }
    }

    @Override
    public void toggleSelection(String buttonId) {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        } else {
            if(currentRegister.getLimit() > selectedNumbers.size()) {
                selectedNumbers.add(Integer.parseInt(buttonId));
                Collections.sort(selectedNumbers);
            }
        }
    }

    @Override
    public void clearSelection() {
        selectedNumbers = new ArrayList<>();
        selectedDraws = new ArrayList<>();
    }

    @Override
    public void removeFromSelection(String buttonId) {
        if(selectedNumbers.contains(Integer.parseInt(buttonId))) {
            selectedNumbers = numberGenerator.treatSelectedNumberRemoval(buttonId, selectedNumbers);
        }
    }

    @Override
    public void check(String drawId) {
        try {
            final String fullId = currentRegister.get_id() + drawId;
            final Draw singleRegister = drawService.getSingleDraws(fullId);

            if(lastCheckedDraws.stream().noneMatch(q -> q.get_id().equals(fullId))) {
                lastCheckedDraws.add(0, singleRegister);
            }
            lastCheckedDraws = CheckDrawsMaxHelper.treatCheckDrawsToMax(lastCheckedDraws);
            checkValue = drawId;
        } catch(EntityIdNotFoundException e) {
            System.out.println("register with id " + drawId + " not found. doing nothing");
        }
    }
}
