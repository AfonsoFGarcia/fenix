/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SaveCandidacyDocumentFiles extends Service {

    public void run(List<CandidacyDocumentUploadBean> candidacyDocuments) {

        Group masterDegreeOfficeEmployeesGroup = new RoleGroup(Role
                .getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE));
        Group coordinatorsGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.COORDINATOR));
        Group permittedGroup = new GroupUnion(masterDegreeOfficeEmployeesGroup, coordinatorsGroup);

        for (CandidacyDocumentUploadBean candidacyDocumentUploadBean : candidacyDocuments) {
            if (candidacyDocumentUploadBean.getFileInputStream() != null) {

                String filename = candidacyDocumentUploadBean.getFilename() + ".pdf";
                CandidacyDocument candidacyDocument = candidacyDocumentUploadBean.getCandidacyDocument();
                Candidacy candidacy = candidacyDocument.getCandidacy();
                Person person = candidacy.getPerson();
                final FileMetadata fileMetadata = new FileMetadata(filename, person.getName());
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                        getFilePath(candidacy), filename, true, fileMetadata,
                        candidacyDocumentUploadBean.getFileInputStream());

                candidacyDocument.setFile(new CandidacyDocumentFile(filename, filename, fileDescriptor
                        .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                        .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                        new GroupUnion(permittedGroup, new PersonGroup(person))));

            }
        }

    }

    private FilePath getFilePath(Candidacy candidacy) {
        final FilePath filePath = new FilePath();
        filePath.addNode(new Node("Candidacies", "Candidacies"));
        filePath
                .addNode(new Node("CANDIDACY" + candidacy.getNumber(), candidacy.getNumber().toString()));
        return filePath;
    }

}
