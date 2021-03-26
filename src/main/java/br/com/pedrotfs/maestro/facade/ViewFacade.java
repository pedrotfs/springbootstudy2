package br.com.pedrotfs.maestro.facade;

import br.com.pedrotfs.maestro.exception.EntityIdNotFoundException;
import org.springframework.ui.Model;

public interface ViewFacade {

    String getView(Model model);

    void resetScreen(String buttonId) throws EntityIdNotFoundException;

    void setCurrentRegister(String registerId) throws EntityIdNotFoundException;

    void toggleSelection(String buttonId);

    void clearSelection();

    void removeFromSelection(String buttonId);

    void check(String drawId);
}
