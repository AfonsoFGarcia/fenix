package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateOrEditGrantContract extends Service {

    public void run(InfoGrantContract infoGrantContract) throws FenixServiceException,
            ExcepcaoPersistencia {

        final GrantOwner grantOwner = rootDomainObject.readGrantOwnerByOID(infoGrantContract.getGrantOwnerInfo().getIdInternal());
        final GrantContract grantContract;
        if (infoGrantContract.getContractNumber() == null) {
            final Integer maxNumber = grantOwner.readGrantContractWithMaximumContractNumber().getContractNumber();
            final Integer newContractNumber = Integer.valueOf(maxNumber + 1);
            infoGrantContract.setContractNumber(newContractNumber);
            grantContract = DomainFactory.makeGrantContract();
        } else {
            grantContract = grantOwner.readGrantContractByNumber(infoGrantContract.getContractNumber()); 
        }

        grantContract.setGrantOwner(grantOwner);

        GrantType grantType = GrantType.readBySigla(infoGrantContract.getGrantTypeInfo().getSigla());
        if (grantType == null) {
            throw new GrantTypeNotFoundException();
        }
        grantContract.setGrantType(grantType);

        GrantOrientationTeacher grantOrientationTeacher = rootDomainObject
                .readGrantOrientationTeacherByOID(infoGrantContract.getGrantOrientationTeacherInfo()
                        .getIdInternal());
        if (grantOrientationTeacher == null) {
            if (infoGrantContract.getIdInternal() != null
                    || !infoGrantContract.getIdInternal().equals(Integer.valueOf(0))) {                
                grantOrientationTeacher = grantContract.readActualGrantOrientationTeacher();
            }

            if (grantOrientationTeacher == null) {
                grantOrientationTeacher = createNewGrantOrientationTeacher(infoGrantContract
                        .getGrantOrientationTeacherInfo(), grantContract);
            } else {
                final Teacher teacher = Teacher
                        .readByNumber(infoGrantContract.getGrantOrientationTeacherInfo()
                                .getOrientationTeacherInfo().getTeacherNumber());

                grantOrientationTeacher.setOrientationTeacher(teacher);
            }
        }
        grantContract.addGrantOrientationTeachers(grantOrientationTeacher);

        if (infoGrantContract.getGrantCostCenterInfo() != null
                && infoGrantContract.getGrantCostCenterInfo().getNumber() != null) {
            GrantCostCenter grantCostCenter = GrantCostCenter
                    .readGrantCostCenterByNumber(infoGrantContract.getGrantCostCenterInfo().getNumber());
            if (grantCostCenter == null) {
                throw new InvalidGrantPaymentEntityException();
            }
            grantContract.setGrantCostCenter(grantCostCenter);
        } else {
            grantContract.setGrantCostCenter(null);
        }

        grantContract.setContractNumber(infoGrantContract.getContractNumber());
        grantContract.setDateAcceptTerm(infoGrantContract.getDateAcceptTerm());
        grantContract.setEndContractMotive(infoGrantContract.getEndContractMotive());
    }

    private GrantOrientationTeacher createNewGrantOrientationTeacher(
            InfoGrantOrientationTeacher grantOrientationTeacherInfo, GrantContract grantContract)
            throws FenixServiceException, ExcepcaoPersistencia {

        final Teacher teacher = Teacher.readByNumber(grantOrientationTeacherInfo
                .getOrientationTeacherInfo().getTeacherNumber());

        final GrantOrientationTeacher newGrantOrientationTeacher;
        if (teacher == null) {
            throw new GrantOrientationTeacherNotFoundException();
        }

        newGrantOrientationTeacher = DomainFactory.makeGrantOrientationTeacher();
        newGrantOrientationTeacher.setBeginDate(grantOrientationTeacherInfo.getBeginDate());
        newGrantOrientationTeacher.setEndDate(grantOrientationTeacherInfo.getEndDate());
        newGrantOrientationTeacher.setGrantContract(grantContract);
        newGrantOrientationTeacher.setOrientationTeacher(teacher);

        return newGrantOrientationTeacher;
    }

}
