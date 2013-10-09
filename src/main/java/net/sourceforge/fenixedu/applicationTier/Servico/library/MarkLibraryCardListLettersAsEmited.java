package net.sourceforge.fenixedu.applicationTier.Servico.library;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class MarkLibraryCardListLettersAsEmited {

    @Atomic
    public static void run(List<LibraryCardDTO> libraryCardDTOList) {
        check(RolePredicates.LIBRARY_PREDICATE);
        for (LibraryCardDTO libraryCardDTO : libraryCardDTOList) {
            libraryCardDTO.getLibraryCard().setLetterGenerationDate(new DateTime());
        }
    }
}