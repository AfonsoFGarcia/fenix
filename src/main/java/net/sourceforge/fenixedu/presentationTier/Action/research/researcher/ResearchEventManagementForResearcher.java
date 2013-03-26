package net.sourceforge.fenixedu.presentationTier.Action.research.researcher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/showEvents", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showEvent", path = "/researcher/showEvent.jsp") })
public class ResearchEventManagementForResearcher extends
        net.sourceforge.fenixedu.presentationTier.Action.research.ResearchEventManagement {
}