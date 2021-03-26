package br.com.pedrotfs.maestro.facade.impl;

import br.com.pedrotfs.maestro.assembler.Assembler;
import br.com.pedrotfs.maestro.domain.Draw;
import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import br.com.pedrotfs.maestro.facade.UpdateFacade;
import br.com.pedrotfs.maestro.file.Decompresser;
import br.com.pedrotfs.maestro.file.Downloader;
import br.com.pedrotfs.maestro.file.Parser;
import br.com.pedrotfs.maestro.repository.DrawRepository;
import br.com.pedrotfs.maestro.service.impl.DrawServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Value("${ltf.file.unzip.name}")
    private String extractTo;

    @Value("${ltf.file.zipped}")
    private String zippedName;

    @Value("${mgs.file.source.location}")
    private String fileLocationMgs;

    @Value("${mgs.file.target.name}")
    private String fileNameMgs;

    @Value("${mgs.file.unzip.name}")
    private String extractToMgs;

    @Value("${mgs.file.zipped}")
    private String zippedNameMgs;

    @Autowired
    private Downloader downloader;

    @Autowired
    private Decompresser decompresser;

    @Autowired
    @Qualifier("parserImpl")
    private Parser parser;

    @Autowired
    @Qualifier("parserMgsImpl")
    private Parser parserMgsImpl;

    @Autowired
    private Assembler assembler;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private DrawServiceImpl drawService;

    @Override
    public void update(String buttonId) throws EntityIdNotFoundException, InterruptedException {
        List<Draw> updateResults = new ArrayList<>();
        if(buttonId.equalsIgnoreCase(MGS)) {
            downloader.download(fileLocationMgs, fileNameMgs);
            decompresser.decompress(extractToMgs, fileNameMgs, zippedNameMgs);
            updateResults = assembler.assembleMgs(parserMgsImpl.parse(extractToMgs));

        } else if(buttonId.equalsIgnoreCase(LTF)) {
            downloader.download(fileLocation, fileName);
            decompresser.decompress(extractTo, fileName, zippedName);
            updateResults = assembler.assemble(parser.parse(extractTo));
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
