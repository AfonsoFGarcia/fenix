/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.commons.lang.CharEncoding;
import org.apache.fop.tools.IOUtil;
import org.apache.struts.util.LabelValueBean;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class InsertExercise extends FenixService {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    public List<String> run(Integer executionCourseId, UploadedFile xmlZipFile, String path) throws FenixServiceException {

	List<String> badXmls = new ArrayList<String>();
	String replacedPath = path.replace('\\', '/');
	boolean createAny = false;
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    throw new InvalidArgumentsServiceException();
	}

	Collection<List<LabelValueBean>> allXmlList = getListOfExercisesList(xmlZipFile);
	for (List<LabelValueBean> xmlFilesList : allXmlList) {
	    Map<String, Question> questionMap = new HashMap<String, Question>();
	    List<LabelValueBean> metadatas = new ArrayList<LabelValueBean>();
	    // if (xmlFilesList == null || xmlFilesList.size() == 0) {
	    // throw new InvalidXMLFilesException();
	    // }
	    for (LabelValueBean labelValueBean : xmlFilesList) {
		String xmlFile = labelValueBean.getValue();
		String xmlFileName = labelValueBean.getLabel();
		try {
		    ParseSubQuestion parseQuestion = new ParseSubQuestion();
		    parseQuestion.parseSubQuestion(xmlFile, replacedPath);
		    Question question = new Question();
		    question.setXmlFile(xmlFile);
		    question.setXmlFileName(xmlFileName);
		    question.setVisibility(new Boolean("true"));
		    questionMap.put(xmlFileName, question);
		} catch (DomainException domainException) {
		    throw domainException;
		} catch (ParseQuestionException e) {
		    metadatas.add(labelValueBean);
		}
	    }

	    if (questionMap.size() != 0) {
		Metadata metadata = null;
		for (LabelValueBean labelValueBean : metadatas) {
		    String xmlFile = labelValueBean.getValue();
		    String xmlFileName = labelValueBean.getLabel();
		    ParseMetadata parse = new ParseMetadata();
		    try {
			Vector<Element> vector = parse.parseMetadata(xmlFile, path);
			List<Question> listToThisMetadata = getListToThisMetadata(questionMap, parse.getMembers());
			if (listToThisMetadata.size() != 0) {
			    metadata = new Metadata(executionCourse, xmlFile, vector);
			    metadata.getQuestions().addAll(listToThisMetadata);
			} else {
			    badXmls.add(xmlFileName + ": Metadata sem exercício associado.");
			}
		    } catch (ParseException e) {
			badXmls.add(xmlFileName + e);
		    }
		}
		if (metadata == null) {
		    metadata = new Metadata(executionCourse, null, null);
		    metadata.getQuestions().addAll(questionMap.values());
		}
		createAny = true;
	    } else {
		for (LabelValueBean labelValueBean : metadatas) {
		    badXmls.add(labelValueBean.getLabel());
		}
	    }

	}
	if (!createAny) {
	    throw new InvalidXMLFilesException();
	}
	return badXmls;
    }

    private List<Question> getListToThisMetadata(Map<String, Question> questionMap, List<String> members) {
	List<Question> result = new ArrayList<Question>();
	if (members.size() != 0) {
	    for (String member : members) {
		Question question = questionMap.get(member);
		if (question != null) {
		    result.add(question);
		    questionMap.remove(member);
		}
	    }
	} else {
	    result.addAll(questionMap.values());
	    questionMap = new HashMap<String, Question>();
	}
	return result;
    }

    private Collection<List<LabelValueBean>> getListOfExercisesList(UploadedFile xmlZipFile) {
	Map<String, List<LabelValueBean>> xmlListMap = new HashMap<String, List<LabelValueBean>>();
	try {
	    if (xmlZipFile.getContentType().equals("application/x-zip-compressed")
		    || xmlZipFile.getContentType().equals("application/zip")) {
		xmlListMap = readFromZip(xmlListMap, xmlZipFile.getInputStream(), "");
	    } else {
		List<LabelValueBean> xmlList = new ArrayList<LabelValueBean>();
		if (xmlZipFile.getContentType().equals("text/xml") || xmlZipFile.getContentType().equals("application/xml")) {
		    if (xmlZipFile.getSize() <= FILE_SIZE_LIMIT) {
			xmlList.add(new LabelValueBean(xmlZipFile.getName(), new String(xmlZipFile.getFileData(), "ISO-8859-1")));
		    } else {
			xmlList.add(new LabelValueBean(xmlZipFile.getName(), null));
		    }
		}
		xmlListMap.put("", xmlList);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}

	return xmlListMap.values();
    }

    private Map<String, List<LabelValueBean>> readFromZip(Map<String, List<LabelValueBean>> xmlListMap,
	    InputStream zipInputStream, String dirBaseName) throws IOException {
	File tmpFile = File.createTempFile("tmpZipFile", "zip");
	FileOutputStream fout = new FileOutputStream(tmpFile);
	IOUtil.copyStream(zipInputStream, fout);
	ZipFile zip = new ZipFile(tmpFile, CharEncoding.UTF_8);
	Enumeration entries = zip.getEntries();
	while (entries.hasMoreElements()) {
	    ZipEntry entry = (ZipEntry) entries.nextElement();
	    final int posSlash = entry.getName().lastIndexOf('/');
	    final List<LabelValueBean> labelValueBeans;
	    final String dirName = (posSlash > 0) ? entry.getName().substring(0, posSlash) : dirBaseName;
	    if (xmlListMap.containsKey(dirName)) {
		labelValueBeans = xmlListMap.get(dirName);
	    } else {
		labelValueBeans = new ArrayList<LabelValueBean>();
		xmlListMap.put(dirName, labelValueBeans);
	    }

	    final StringBuilder stringBuilder = new StringBuilder();
	    final byte[] b = new byte[1000];
	    InputStream is = zip.getInputStream(entry);
	    if (entry.getName().endsWith("zip")) {
		xmlListMap = readFromZip(xmlListMap, is, entry.getName());
	    } else {
		for (int readed = 0; (readed = is.read(b)) > -1; stringBuilder.append(new String(b, 0, readed, "ISO-8859-1"))) {
		    // nothing to do :o)
		}
		if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
		    labelValueBeans.add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
		} else {
		    labelValueBeans.add(new LabelValueBean(entry.getName(), null));
		}
	    }
	}
	return xmlListMap;
    }
}