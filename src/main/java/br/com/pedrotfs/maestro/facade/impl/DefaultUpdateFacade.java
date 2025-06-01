package br.com.pedrotfs.maestro.facade.impl;

import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.facade.UpdateFacade;
import br.com.pedrotfs.maestro.file.Downloader;
import br.com.pedrotfs.maestro.file.Parser;
import br.com.pedrotfs.maestro.repository.DrawRepository;
import br.com.pedrotfs.maestro.service.impl.DrawServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static br.com.pedrotfs.maestro.util.Constants.LTF;
import static br.com.pedrotfs.maestro.util.Constants.MGS;

@Component
public class DefaultUpdateFacade implements UpdateFacade {

    @Value("${ltf.file.source.location}")
    private String fileLocation;

    @Value("${ltf.file.target.name}")
    private String fileName;

    @Value("${mgs.file.source.location}")
    private String fileLocationMgs;

    @Value("${mgs.file.target.name}")
    private String fileNameMgs;

    @Autowired
    private Downloader downloader;

    @Autowired
    private Parser parser;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private DrawServiceImpl drawService;

    @Override
    public void update(String buttonId) throws EntityIdNotFoundException, InterruptedException {
        List<Draw> updateResults = new ArrayList<>();
        long count = drawRepository.findByRegisterId(buttonId).size();
        if(buttonId.equalsIgnoreCase(MGS)) {
            downloader.download(fileLocationMgs, fileNameMgs);
            updateResults = parser.parse(fileNameMgs, buttonId, count);
        } else if(buttonId.equalsIgnoreCase(LTF)) {
            downloader.download(fileLocation, fileName);
            updateResults = parser.parse(fileName, buttonId, count);
        }

        if(!updateResults.isEmpty()) {
            updateResults.stream().filter(r -> {
                try {
                    drawService.getSingleDraws(r.get_id());
                    return false;
                } catch (EntityIdNotFoundException e) {
                    return true;
                }
            }).forEach(d -> drawRepository.save(d));
        }
    }
}
