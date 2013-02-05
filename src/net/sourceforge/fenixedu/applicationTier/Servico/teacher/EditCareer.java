package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;

public class EditCareer extends FenixService {

    public void run(Integer careerId, InfoCareer infoCareer) {
        if (infoCareer instanceof InfoTeachingCareer) {
            editCareer(careerId, (InfoTeachingCareer) infoCareer);
        } else if (infoCareer instanceof InfoProfessionalCareer) {
            editCareer(careerId, (InfoProfessionalCareer) infoCareer);
        }
    }

    private void editCareer(Integer careerId, InfoTeachingCareer infoTeachingCareer) {
        TeachingCareer teachingCareer = (TeachingCareer) rootDomainObject.readCareerByOID(careerId);
        ProfessionalCategory category =
                rootDomainObject.readProfessionalCategoryByOID(infoTeachingCareer.getInfoCategory().getIdInternal());

        // If it doesn't exist in the database, a new one has to be created
        if (teachingCareer == null) {
            Teacher teacher = rootDomainObject.readTeacherByOID(infoTeachingCareer.getInfoTeacher().getIdInternal());
            teachingCareer = new TeachingCareer(teacher, category.getName(), infoTeachingCareer);
        } else {
            teachingCareer.edit(infoTeachingCareer, category.getName());
        }
    }

    private void editCareer(Integer careerId, InfoProfessionalCareer infoProfessionalCareer) {
        ProfessionalCareer professionalCareer = (ProfessionalCareer) rootDomainObject.readCareerByOID(careerId);

        // If it doesn't exist in the database, a new one has to be created
        if (professionalCareer == null) {
            Teacher teacher = rootDomainObject.readTeacherByOID(infoProfessionalCareer.getInfoTeacher().getIdInternal());
            professionalCareer = new ProfessionalCareer(teacher, infoProfessionalCareer);
        } else {
            professionalCareer.edit(infoProfessionalCareer);
        }
    }

}
