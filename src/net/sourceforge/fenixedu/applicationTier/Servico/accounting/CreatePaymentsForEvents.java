package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class CreatePaymentsForEvents extends Service {

    public CreatePaymentsForEvents() {
        super();
    }

    public void run(final User responsibleUser, final List<EntryDTO> entryDTOs)
            throws FenixServiceException {

        final Map<Event, List<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);

        for (final Map.Entry<Event, List<EntryDTO>> entry : entryDTOsByEvent.entrySet()) {
            entry.getKey().process(responsibleUser, entry.getValue());
        }

    }

    private Map<Event, List<EntryDTO>> splitEntryDTOsByEvent(List<EntryDTO> entryDTOs) {
        final Map<Event, List<EntryDTO>> result = new HashMap<Event, List<EntryDTO>>();

        for (final EntryDTO entryDTO : entryDTOs) {
            List<EntryDTO> entryDTOsByEvent = result.get(entryDTO.getEvent());
            if (entryDTOsByEvent == null) {
                entryDTOsByEvent = new ArrayList<EntryDTO>();
            }

            entryDTOsByEvent.add(entryDTO);

            result.put(entryDTO.getEvent(), entryDTOsByEvent);
        }

        return result;
    }

}