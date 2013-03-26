package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import pt.ist.fenixframework.DomainObject;

public class EditGrantContractRegime extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject) {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;

        GrantContract grantContract = infoGrantContractRegime.getGrantContract();
        if (grantContract == null) {
            grantContract =
                    rootDomainObject.readGrantContractByOID(infoGrantContractRegime.getInfoGrantContract().getIdInternal());
        }

        GrantContractRegime grantContractRegime = (GrantContractRegime) domainObject;
        grantContractRegime.setGrantContract(grantContract);
        grantContractRegime.editTimeInterval(infoGrantContractRegime.getDateBeginContract(),
                infoGrantContractRegime.getDateEndContract());
        grantContractRegime.setDateDispatchCC(infoGrantContractRegime.getDateDispatchCC());
        grantContractRegime.setDateDispatchCD(infoGrantContractRegime.getDateDispatchCD());
        grantContractRegime.setDateSendDispatchCC(infoGrantContractRegime.getDateSendDispatchCC());
        grantContractRegime.setDateSendDispatchCD(infoGrantContractRegime.getDateSendDispatchCD());

        final GrantCostCenter grantCostCenter =
                (GrantCostCenter) rootDomainObject.readGrantPaymentEntityByOID(infoGrantContractRegime.getCostCenterKey());
        grantContractRegime.setGrantCostCenter(grantCostCenter);

        grantContractRegime.setState(infoGrantContractRegime.getState());
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return new GrantContractRegime();
    }

    @Override
    protected DomainObject readObjectByUnique(InfoObject infoObject) {
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        return rootDomainObject.readGrantContractRegimeByOID(infoGrantContractRegime.getIdInternal());
    }

    public void run(InfoGrantContractRegime infoGrantContractRegime) throws Exception {
        super.run(Integer.valueOf(0), infoGrantContractRegime);
    }

    @Override
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject) throws FenixServiceException {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        if (infoGrantContractRegime.getState().equals(InfoGrantContractRegime.getActiveState())) {
            // Active Contract Regime

            GrantContractRegime grantContractRegime = (GrantContractRegime) domainObjectLocked;
            // Set the correct grant orientation teacher

            GrantContract grantContract = infoGrantContractRegime.getGrantContract();
            if (grantContract == null) {
                grantContract =
                        rootDomainObject.readGrantContractByOID(infoGrantContractRegime.getInfoGrantContract().getIdInternal());
            }

            if (infoGrantContractRegime.getGrantCostCenterInfo() != null
                    && ((infoGrantContractRegime.getGrantCostCenterInfo().getNumber()).trim()).length() > 0) { // ||
                GrantCostCenter grantCostCenter =
                        GrantCostCenter.readGrantCostCenterByNumber(infoGrantContractRegime.getGrantCostCenterInfo().getNumber());
                if (grantCostCenter == null) {
                    throw new GrantOrientationTeacherNotFoundException();
                }
                grantContract.setGrantCostCenter(grantCostCenter);

            } else {
                grantContract.setGrantCostCenter(null);
            }

            grantContractRegime.setGrantCostCenter(grantContract.getGrantCostCenter());

            GrantOrientationTeacher grantOrientationTeacher = grantContract.readActualGrantOrientationTeacher();
            if (grantOrientationTeacher != null) {

                // If grantOrientationTeacher is filled in
                // grantContractRegime
                final Teacher teacher;
                if (infoGrantContractRegime.getInfoTeacher() != null) {
                    if (infoGrantContractRegime.getInfoTeacher().getTeacher()
                            .equals(grantOrientationTeacher.getOrientationTeacher())) {
                        // Update grant orientation teacher of contract

                        teacher = grantOrientationTeacher.getOrientationTeacher();
                    } else {

                        teacher = infoGrantContractRegime.getInfoTeacher().getTeacher();
                    }

                    grantOrientationTeacher.setOrientationTeacher(teacher);
                }
                grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
                grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());

                grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());

            }

            // Set all the others GrantContractRegime that are active to state
            // inactive

            List<GrantContractRegime> activeContractRegime =
                    grantContractRegime.getGrantContract().readGrantContractRegimeByGrantContractAndState(Integer.valueOf(1));
            if (activeContractRegime != null && !activeContractRegime.isEmpty()) {
                // Desactivate the contracts
                for (GrantContractRegime grantContractRegimeTemp : activeContractRegime) {
                    if (!grantContractRegimeTemp.equals(grantContractRegime)) {
                        grantContractRegimeTemp.setState(InfoGrantContractRegime.getInactiveState());
                    }
                }
            }
        }

    }

    @Override
    protected DomainObject readDomainObject(Integer idInternal) {
        return rootDomainObject.readGrantContractRegimeByOID(idInternal);
    }

}
