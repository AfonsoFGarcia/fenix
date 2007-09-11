package net.sourceforge.fenixedu.applicationTier.Servico.library;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;

public class MarkLibraryCardListLettersAsEmited extends Service {

    public void run(List<LibraryCardDTO> libraryCardDTOList) {
        for (LibraryCardDTO libraryCardDTO : libraryCardDTOList) {
            libraryCardDTO.getLibraryCard().setIsLetterGenerated(Boolean.TRUE);   
        }        
    }
}
