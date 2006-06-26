package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class FileItem extends FileItem_Base {

    static {
        ItemFileItem.addListener(new ItemFileItemListener());
    }

    public FileItem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public FileItem(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String dspaceBitstreamIdentification,
            Group permittedGroup, FileItemPermittedGroupType fileItemPermittedGroupType) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                dspaceBitstreamIdentification, permittedGroup);
        setFileItemPermittedGroupType(fileItemPermittedGroupType);
    }

    public void delete() {

        if (this.getItems().size() != 0) {
            throw new DomainException("fileItem.cannotBeDeleted");
        }
        super.deleteDomainObject();
    }

    private static class ItemFileItemListener extends dml.runtime.RelationAdapter<FileItem, Item> {
        @Override
        public void afterRemove(FileItem fileItem, Item item) {
            fileItem.delete();
        }

    }

    public static FileItem readByOID(Integer idInternal) {
        return (FileItem) RootDomainObject.getInstance().readFileByOID(idInternal);
    }

}
