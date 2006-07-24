package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class FileItem extends FileItem_Base {

    static {
        ItemFileItem.addListener(new ItemFileItemListener());
    }

    public FileItem() {
        super();
    }

    public FileItem(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup, FileItemPermittedGroupType fileItemPermittedGroupType) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
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
