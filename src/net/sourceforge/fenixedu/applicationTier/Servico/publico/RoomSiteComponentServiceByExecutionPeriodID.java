/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 * 
 */
public class RoomSiteComponentServiceByExecutionPeriodID extends Service {

    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day, Integer executionPeriodID) throws Exception {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodID);
        return (executionPeriod != null) ? runService(bodyComponent, roomKey, day, executionPeriod) : RoomSiteComponentService.run(bodyComponent, roomKey, day);
    }

    public static Object runService(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day,
            ExecutionPeriod executionPeriod) throws Exception {
        SiteView siteView = null;

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        // IPersistentExecutionPeriod persistentExecutionPeriod =
        // persistentSupport.getIPersistentExecutionPeriod();

        Room room = persistentRoom.readByName(roomKey.getNomeSala());
        // ExecutionPeriod executionPeriod = (ExecutionPeriod)
        // persistentExecutionPeriod
        // .readByOID(ExecutionPeriod.class, infoExecutionPeriodCode);
        // if (executionPeriod == null) {
        // throw new NonExistingServiceException();
        // }
        RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, day, room, executionPeriod);

        siteView = new SiteView(bodyComponent);

        return siteView;
    }
}