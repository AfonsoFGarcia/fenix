package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.Space;

public class EditBlueprintVersion extends BlueprintVersionManagmentService {

    public Blueprint run(Blueprint blueprint, CreateBlueprintSubmissionBean blueprintSubmissionBean)
            throws FenixServiceException {

        blueprint.removeBlueprintFile();

        final Space space = getSpace(blueprintSubmissionBean);

        final Person person = AccessControl.getUserView().getPerson();

        editBlueprintVersion(blueprintSubmissionBean, space, person, blueprint);

        return blueprint;
    }

}
