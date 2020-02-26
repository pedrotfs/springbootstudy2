package br.com.pedrotfs.maestro.service.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.repository.DrawRepository;
import br.com.pedrotfs.maestro.service.DrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DrawServiceImpl implements DrawService {

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Draw createDraw(String _id, String registerId, String date, String[] numbers, String[] dividends, String[] amount) {
        Draw draw = new Draw();
        populateDraw(_id, registerId, date, numbers, dividends, amount, draw);
        drawRepository.save(draw);
        return draw;
    }

    public void populateDraw(String _id, String registerId, String date, String[] numbers, String[] dividends, String[] amount, Draw draw) {
        draw.set_id(_id);
        draw.setRegisterId(registerId);
        draw.setDate(date);

        for (String number : numbers) {
            draw.getNumbers().add(Integer.parseInt(number));
        }
        for (String dividend : dividends) {
            draw.getWinnerCategoriesDividends().add(Long.parseLong(dividend));
        }
        for (String unit : amount) {
            draw.getWinnerCategoriesAmount().add(Integer.parseInt(unit));
        }
        Collections.sort(draw.getNumbers());
        Collections.sort(draw.getWinnerCategoriesAmount());
        Collections.sort(draw.getWinnerCategoriesDividends());
    }

    @Override
    public Draw getSingleDraws(String _id) throws EntityIdNotFoundException {
        final Optional<Draw> byId = drawRepository.findById(_id);
        if(byId.isPresent()) {
            return byId.get();
        } else {
            throw new EntityIdNotFoundException();
        }
    }

    @Override
    public List<Draw> getDraws() {
        return drawRepository.findAll();
    }

    @Override
    public List<Draw> getDrawsByRegisterId(String registerId) {
        return drawRepository.findByRegisterId(registerId);
    }

    @Override
    public Draw updateDraws(Draw draw) throws EntityIdNotFoundException {
        final Optional<Draw> byId = drawRepository.findById(draw.get_id());
        if(byId.isPresent()) {
            drawRepository.save(draw);
            return draw;
        } else {
            throw new EntityIdNotFoundException();
        }
    }

    @Override
    public String deleteDraws(String _id) throws EntityIdNotFoundException {
        final Optional<Draw> byId = drawRepository.findById(_id);
        if(byId.isPresent()) {
            drawRepository.delete(byId.get());
            return _id;
        } else {
            throw new EntityIdNotFoundException();
        }
    }

    @Override
    public List<Draw> findByRegisterIdAndNumberIn(String registerId, List<Integer> numbers) {
        Query query = new Query();
        Criteria registerId1 = Criteria.where("registerId").in(registerId);
        query.addCriteria(registerId1.and("numbers").all(numbers));
        return mongoTemplate.find(query, Draw.class);
    }
}
