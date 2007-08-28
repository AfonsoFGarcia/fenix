/*
 * Created on Dec 12, 2004
 *
 */
package net.sourceforge.fenixedu.tools.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.log4j.Logger;

import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author Luis Cruz
 * 
 */
public class ReverseFile {

    private static final Logger logger = Logger.getLogger(ReverseFile.class);

    public static void main(String[] args) {
        try {
            if (args == null || args.length < 1 || args.length > 2) {
                if (LogLevel.FATAL) {
                    logger.fatal("Invalid arguments.");
                }
                throw new InvalidArgumentException("Usage: <filename> [<output filename>]");
            }

            if (args.length == 1) {
                reverseFile(args[0], args[0]);
            } else {
                reverseFile(args[0], args[1]);
            }
        } catch (Exception ex) {
            if (LogLevel.FATAL) {
                logger.fatal("Encountered fatal exception: " + ex.getMessage(), ex);
            }
        }

        System.exit(0);
    }

    protected static void reverseFile(final String inputFilename, final String outputFilename)
            throws IOException {
        if ((new File(inputFilename)).exists()) {
            String filecontents = "###\n" + FileUtils.readFile(inputFilename);
            filecontents = filecontents.replaceAll(";\ninsert", ";\n###\ninsert");
            filecontents = filecontents.replaceAll(";\nupdate", ";\n###\nupdate");
            filecontents = filecontents.replaceAll(";\ndelete", ";\n###\ndelete");
            final String[] lines = filecontents.split("###\n");

            final FileWriter fileWriter = new FileWriter(outputFilename, false);
            for (int i = lines.length; i > 0; i--) {
                fileWriter.write(lines[i - 1]);
            }
            fileWriter.close();

            if (LogLevel.DEBUG) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Wrote " + lines.length + " lines to " + outputFilename);
                }
            }
        }
    }

}