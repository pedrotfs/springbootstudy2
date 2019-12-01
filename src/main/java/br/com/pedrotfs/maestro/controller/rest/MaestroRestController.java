package br.com.pedrotfs.maestro.controller.rest;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.domain.Register;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.service.DrawService;
import br.com.pedrotfs.maestro.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MaestroRestController {

    Logger LOG = LoggerFactory.getLogger(MaestroRestController.class);

    @Autowired
    private RegisterService registerService;

    @Autowired
    private DrawService drawService;

    @GetMapping("/")
    public String getRootEndpoint() {
        return LocalDateTime.now().toString() + " get result on root API";
    }

    @GetMapping("/register")
    public List<Register> getRegisters() {
        LOG.info("recovering all registers");
        return registerService.getRegisters();
    }

    @GetMapping("/register/{id}")
    public Register getRegisters(@PathVariable String id) throws EntityIdNotFoundException {
        LOG.info("recovering register with id " + id);
        return registerService.getSingleRegister(id);
    }

    @PostMapping("/register")
    public Register createRegister(@RequestParam final String id, @RequestParam final String count, @RequestParam final String limit) {
        LOG.info("creating register " + id);
        return registerService.createRegister(id, count, limit);
    }

    @PutMapping("/register")
    public Register updateRegister(@RequestParam final String id, @RequestParam final String count, @RequestParam final String limit)
            throws EntityIdNotFoundException {
        LOG.info("updating register with id " + id);
        Register register = new Register();
        register.set_id(id);
        register.setLimit(Integer.parseInt(limit));
        register.setCount(Integer.parseInt(count));
        return registerService.updateRegister(register);
    }

    @DeleteMapping("/register")
    public String deleteRegister(@RequestParam final String id) throws EntityIdNotFoundException {
        LOG.info("deleting register with id " + id);
        return "deleted registry with id " + registerService.deleteRegister(id);
    }

    @GetMapping("/draw")
    public List<Draw> getDraws() {
        LOG.info("recovering all draws");
        return drawService.getDraws();
    }

    @GetMapping("/draw/getDrawsByRegisterIdAndNumber/")
    public List<Draw> getDrawsByRegisterIdAndNumber(@RequestParam final String registerId, @RequestParam final String numbers) {
        LOG.info("recovering all draws of " + registerId + "containing " + numbers);
        return drawService.findByRegisterIdAndNumberIn(registerId, numbers.split("-"));
    }

    @GetMapping("/draw/{id}")
    public Draw getDraws(@PathVariable String id) throws EntityIdNotFoundException {
        LOG.info("recovering draw with id " + id);
        return drawService.getSingleDraws(id);
    }

    @GetMapping("/draw/byRegister/{registerId}")
    public List<Draw> getDrawsByRegisterId(@PathVariable String registerId) throws EntityIdNotFoundException {
        LOG.info("recovering draw with register id " + registerId);
        return drawService.getDrawsByRegisterId(registerId);
    }

    @PostMapping("/draw")
    public Draw createDraw(@RequestParam final String id,
                           @RequestParam final String registerId,
                           @RequestParam final String date,
                           @RequestParam final String numbers,
                           @RequestParam final String amount,
                           @RequestParam final String dividends) {
        LOG.info("creating draw " + id);
        String[] numberArray = numbers.split("-");
        String[] amountArray = amount.split("-");
        String[] dividendArray = dividends.split("-");
        return drawService.createDraw(id, registerId, date, numberArray, amountArray, dividendArray);
    }

    @PutMapping("/draw")
    public Draw updateDraw(@RequestParam final String id,
                           @RequestParam final String registerId,
                           @RequestParam final String date,
                           @RequestParam final String numbers,
                           @RequestParam final String amount,
                           @RequestParam final String dividends)
            throws EntityIdNotFoundException {
        LOG.info("updating draw with id " + id);
        Draw draw = new Draw();
        drawService.populateDraw(id, registerId, date, numbers.split("-"), dividends.split("-"), amount.split("-"), draw);
        return drawService.updateDraws(draw);
    }

    @DeleteMapping("/draw")
    public String deleteDraw(@RequestParam final String id) throws EntityIdNotFoundException {
        LOG.info("deleting draw with id " + id);
        return "deleted registry with id " + drawService.deleteDraws(id);
    }
}
