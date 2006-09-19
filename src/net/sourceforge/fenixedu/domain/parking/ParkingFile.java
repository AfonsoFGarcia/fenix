package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ParkingFile extends ParkingFile_Base {
    
    public static final long MAX_FILE_SIZE = 3145728;
    
    public  ParkingFile() {
        super();
    }
    
    public ParkingFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);

    }
}
