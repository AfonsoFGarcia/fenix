/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.xml.sax.SAXParseException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertExercise implements IService {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    private String path = new String();

    public List<String> run(Integer executionCourseId, FormFile metadataFile, FormFile xmlZipFile, String path) throws FenixServiceException,
            NotExecuteException {
        List<String> badXmls = new ArrayList<String>();
        int xmlNumber = 0;
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            ParseMetadata parseMetadata = new ParseMetadata();
            String metadataString = null;
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = DomainFactory.makeMetadata();
            metadata.setExecutionCourse(executionCourse);
            metadata.setVisibility(new Boolean("true"));
            try {
                if (metadataFile != null && metadataFile.getFileData().length != 0) {
                    metadataString = new String(metadataFile.getFileData(), "ISO-8859-1");
                    metadata.setMetadataFile(metadataString);
                    metadata = parseMetadata.parseMetadata(metadataString, metadata, this.path);
                }
            } catch (SAXParseException e) {
                badXmls.add(new String("badMetadata"));
                return badXmls;
            } catch (Exception e) {
                badXmls.add(new String("badMetadata"));
                return badXmls;
            }

            List<LabelValueBean> xmlFilesList = getXmlFilesList(xmlZipFile);
            if (xmlFilesList == null)
                throw new NotExecuteException("error.badMetadataFile");

            for (LabelValueBean labelValueBean : xmlFilesList) {
                String xmlFile = labelValueBean.getValue();
                String xmlFileName = labelValueBean.getLabel();

                try {
                    ParseQuestion parseQuestion = new ParseQuestion();
                    IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();

                    parseQuestion.parseQuestion(xmlFile, new InfoQuestion(), this.path);
                    IQuestion question = DomainFactory.makeQuestion();
                    question.setMetadata(metadata);
                    question.setXmlFile(xmlFile);
                    question.setXmlFileName(xmlFileName);
                    question.setVisibility(new Boolean("true"));
                    xmlNumber++;
                } catch (SAXParseException e) {
                    if (metadataString != null) {
                        metadata.setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName);
                } catch (ParseQuestionException e) {
                    if (metadataString != null) {
                        metadata.setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName + e);
                } catch (Exception e) {
                    if (metadataString != null) {
                        metadata.setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName);
                }
            }

            if (xmlNumber == 0) {
                persistentMetadata.deleteByOID(Metadata.class, metadata.getIdInternal());
            }

            return badXmls;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private String removeLocation(String metadataFile, String xmlName) throws FenixServiceException {
        TransformerFactory tf = TransformerFactory.newInstance();
        java.io.StringWriter result = new java.io.StringWriter();
        try {
            URL xsl = new URL("file:///" + path.concat("WEB-INF/ims/removeXmlLocation.xsl"));
            String doctypePublic = new String("-//Technical Superior Institute//DTD Test Metadata 1.1//EN");
            String doctypeSystem = new String("metadataFile://" + path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
            String auxFile = new String();

            // <!DOCTYPE questestinterop SYSTEM "ims_qtiasiv1p2p1.dtd" [
            // <!ELEMENT ims_render_object (response_label+)>
            // <!ATTLIST ims_render_object
            // shuffle (Yes|No) 'No'
            // orientation (Row|Column) #REQUIRED>
            // ]>

            int index = metadataFile.indexOf("<!DOCTYPE");
            if (index != -1) {
                auxFile = metadataFile.substring(0, index);
                int index2 = metadataFile.indexOf(">", index) + 1;
                auxFile = metadataFile.substring(index2, metadataFile.length());
            }
            metadataFile = auxFile;

            Transformer transformer = tf.newTransformer(new StreamSource(xsl.openStream()));
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctypeSystem);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
            transformer.setParameter("xmlDocument", xmlName);

            Source source = new StreamSource(new StringReader(metadataFile));

            transformer.transform(source, new StreamResult(result));

        } catch (javax.xml.transform.TransformerConfigurationException e) {
            throw new FenixServiceException(e);
        } catch (javax.xml.transform.TransformerException e) {
            throw new FenixServiceException(e);
        } catch (FileNotFoundException e) {
            throw new FenixServiceException(e);
        } catch (IOException e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return result.toString();
    }

    // private String changeDocumentType(String file, boolean metadata) throws
    // FenixServiceException
    // {
    // TransformerFactory tf = TransformerFactory.newInstance();
    // java.io.StringWriter result = new java.io.StringWriter();
    // try
    // {
    // URL xsl = new URL("file:///" +
    // path.concat("WEB-INF/ims/changeDocumentType.xsl"));
    // String doctypePublic = null;
    // String doctypeSystem = null;
    // if (metadata)
    // {
    // doctypePublic = new String("-//Technical Superior Institute//DTD Test
    // Metadata 1.1//EN");
    // doctypeSystem = new String("file:///" +
    // path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
    // }
    // else
    // {
    // doctypePublic =
    // new String("-//Technical Superior Institute//DTD Test XmlDocument
    // 1.1//EN");
    // doctypeSystem = new String("file:///" +
    // path.concat("WEB-INF/ims/qtiasiv1p2.dtd"));
    // }
    //
    // String auxFile = new String();
    // int index = file.indexOf("<!DOCTYPE");
    // if (index != -1)
    // {
    // auxFile = file.substring(0, index);
    // int index2 = file.indexOf(">", index) + 1;
    // auxFile = file.substring(index2, file.length());
    // }
    // file = auxFile;
    // Transformer transformer = tf.newTransformer(new
    // StreamSource(xsl.openStream()));
    // transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
    // transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctypeSystem);
    // transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
    // Source source = new StreamSource(new StringReader(file));
    // transformer.transform(source, new StreamResult(result));
    // }
    // catch (javax.xml.transform.TransformerConfigurationException e)
    // {
    // throw new FenixServiceException(e);
    // }
    // catch (javax.xml.transform.TransformerException e)
    // {
    // throw new FenixServiceException(e);
    // }
    // catch (FileNotFoundException e)
    // {
    // throw new FenixServiceException(e);
    // }
    // catch (IOException e)
    // {
    // throw new FenixServiceException(e);
    // }
    // catch (Exception e)
    // {
    // throw new FenixServiceException(e);
    // }
    //
    // return result.toString();
    // }

    private List<LabelValueBean> getXmlFilesList(FormFile xmlZipFile) {
        List<LabelValueBean> xmlFilesList = new ArrayList<LabelValueBean>();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml")) {
                if (xmlZipFile.getFileSize() <= FILE_SIZE_LIMIT) {
                    xmlFilesList.add(new LabelValueBean(xmlZipFile.getFileName(), new String(xmlZipFile.getFileData(), "ISO-8859-1")));
                    // changeDocumentType(new String(xmlZipFile.getFileData(),
                    // "ISO-8859-1"), false)));
                }
            } else {
                zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry()) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder.append(new String(b, 0, readed, "ISO-8859-1"))) {
                        // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        xmlFilesList.add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
                    }
                }
                zipFile.close();
            }
        } catch (Exception e) {
            return null;
        }
        return xmlFilesList;
    }
}