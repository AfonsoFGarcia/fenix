package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.util.ByteArray;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;
import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class BlueprintVersionManagmentService extends Service {

    protected Space getSpace(CreateBlueprintSubmissionBean blueprintSubmissionBean) throws FenixServiceException {
        final SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
        final Space space = spaceInformation.getSpace();
        if (space == null) {
            throw new FenixServiceException("error.blueprint.submission.no.space");
        }
        return space;
    }

    protected void editBlueprintVersion(CreateBlueprintSubmissionBean blueprintSubmissionBean,
            final Space space, final Person person, final Blueprint blueprint) throws IOException {
        final String filename = blueprintSubmissionBean.getSpaceInformation().getIdInternal()
                + String.valueOf(System.currentTimeMillis());
        final FileMetadata fileMetadata = new FileMetadata(filename, person.getName());

        final byte[] contents = readInputStream(blueprintSubmissionBean.getInputStream());
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contents);

        final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                getFilePath(space.getMostRecentSpaceInformation()), filename, true, fileMetadata,
                byteArrayInputStream);

        final String displayName = blueprintSubmissionBean.getFilename();
        final BlueprintFile blueprintFile = new BlueprintFile(blueprint, filename, displayName, fileDescriptor.getMimeType(), fileDescriptor
                .getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
                fileDescriptor.getUniqueId(), new RoleGroup(Role
                        .getRoleByRoleType(RoleType.SPACE_MANAGER)));
        blueprintFile.setContent(new ByteArray(contents));
    }

    private byte[] readInputStream(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileUtils.copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected FilePath getFilePath(SpaceInformation spaceInformation) {
        final FilePath filePath = new FilePath();
        filePath.addNode(new Node("Spaces", "Spaces"));
        filePath.addNode(new Node("Spaces" + spaceInformation.getSpace().getIdInternal(),
                spaceInformation.getPresentationName()));
        filePath.addNode(new Node("Blueprints", "Blueprints"));
        return filePath;
    }

}
