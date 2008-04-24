package net.sourceforge.fenixedu.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import eu.ist.fenixframework.pstm.Transaction;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class File extends File_Base {

    public File() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(String filename, String displayName, String mimeType, String checksum,
	    String checksumAlgorithm, Integer size, String externalStorageIdentification,
	    Group permittedGroup) {
	setFilename(FileUtils.getFilenameOnly(filename));
	setDisplayName(FileUtils.getFilenameOnly(displayName));
	setMimeType(mimeType);
	setChecksum(checksum);
	setChecksumAlgorithm(checksumAlgorithm);
	setSize(size);
	setExternalStorageIdentification(externalStorageIdentification);
	setPermittedGroup(permittedGroup);
	setUploadTime(new DateTime());
    }

    public boolean isPersonAllowedToAccess(Person person) {
        final Group group = this.getPermittedGroup();
        return group == null || group.isMember(person);
    }

    /**
         * @return returns a public url that can be used by a client to download
         *         the associated file from the external file storage
         */
    public String getDownloadUrl() {
        // TODO: remove the dependancy between the domain and the dspace infrastructure
        return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(getExternalStorageIdentification(), getFilename());
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
   public static File readByExternalStorageIdentification(String externalStorageIdentification) {
	// For performance reasons...
	PreparedStatement stmt = null;
	try {
	    final Connection connection = Transaction.getNewJdbcConnection();
	    stmt = connection
		    .prepareStatement("SELECT ID_INTERNAL FROM FILE WHERE EXTERNAL_STORAGE_IDENTIFICATION = ?");

	    stmt.setString(1, externalStorageIdentification);
	    final ResultSet resultSet = stmt.executeQuery();
	    if (resultSet.next()) {
		return RootDomainObject.getInstance().readFileByOID(resultSet.getInt(1));
	    }

	    return null;
	} catch (SQLException e) {
	    throw new Error(e);
	} finally {
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		    throw new Error(e);
		}
	    }
	}
    }

}
