package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class PhdCandidacyDocumentUploadBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PhdIndividualProgramDocumentType type;

    private transient InputStream file;

    private byte[] fileContent;

    private String filename;

    public byte[] getFileContent() {
	return this.fileContent;
    }

    public PhdIndividualProgramDocumentType getType() {
	return type;
    }

    public void setType(PhdIndividualProgramDocumentType type) {
	this.type = type;
    }

    public InputStream getFile() {
	return file;
    }

    public void setFile(InputStream file) {
	this.file = file;

	if (file != null) {
	    final ByteArrayOutputStream result = new ByteArrayOutputStream();
	    try {
		FileUtils.copy(this.file, result);
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }

	    this.fileContent = result.toByteArray();
	} else {
	    this.fileContent = null;
	}
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public boolean hasAnyInformation() {
	return this.type != null || !StringUtils.isEmpty(this.filename) || this.fileContent != null;
    }
}
