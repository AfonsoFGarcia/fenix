package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class PedagogicalCouncilMembersGroup extends RoleTypeGroup {

	/**
	 * Default serial id. 
	 */
	private static final long serialVersionUID = 1L;

	public PedagogicalCouncilMembersGroup() {
		super(RoleType.PEDAGOGICAL_COUNCIL);
	}

	@Override
	public String getName() {
		return RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName());
	}
	
}
