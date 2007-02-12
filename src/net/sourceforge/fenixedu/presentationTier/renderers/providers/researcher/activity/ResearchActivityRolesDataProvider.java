package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.activity;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchScientificJournalCreationBean;
import net.sourceforge.fenixedu.domain.research.activity.CooperationType;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ResearchActivityRolesDataProvider implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		List<ResearchActivityParticipationRole> roles = null;
		
		if(source instanceof ResearchActivityParticipantBean){
			ResearchActivityParticipantBean bean = (ResearchActivityParticipantBean)source;
			roles = bean.getResearchActivity().getAllowedRoles();
			
		} else if(source instanceof ResearchActivityParticipantEditionBean){
			ResearchActivityParticipantEditionBean bean = (ResearchActivityParticipantEditionBean)source;
			roles = bean.getResearchActivity().getAllowedRoles();

		} else if(source instanceof ResearchEventCreationBean) {
			roles = ResearchActivityParticipationRole.getAllEventParticipationRoles();
		} else if(source instanceof ResearchScientificJournalCreationBean) {
			roles = ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
			
		} else if (source instanceof ResearchCooperationCreationBean) {		
			ResearchCooperationCreationBean bean = (ResearchCooperationCreationBean)source;
			
			if(bean.getCooperation() != null) {
				roles = bean.getCooperation().getAllowedRoles();
			} else {
				roles = CooperationType.getParticipationRoles(bean.getType());
			}
		}
		
		return roles;
    }

    public Converter getConverter() {
        return new Converter() {

            @Override
            public Object convert(Class type, Object value) {
            	return ResearchActivityParticipationRole.valueOf((String) value);
            }

        };
    }

}
