/*
 * Created on 30/Jul/2003
 *
 * 
 */

package fileSuport;

/**
 * @author Jo�o Mota
 *
 * 30/Jul/2003
 * fenix-head
 * fileSuport
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;

import org.apache.slide.authenticate.CredentialsToken;
import org.apache.slide.authenticate.SecurityToken;
import org.apache.slide.common.Domain;
import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.common.SlideException;
import org.apache.slide.common.SlideToken;
import org.apache.slide.common.SlideTokenImpl;
import org.apache.slide.content.Content;
import org.apache.slide.content.NodeRevisionContent;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionDescriptors;
import org.apache.slide.lock.Lock;
import org.apache.slide.security.Security;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.Structure;
import org.apache.slide.structure.SubjectNode;

import slidestore.mysql.MySQLContentStore;

public class FileSuport implements IFileSuport {

	private static FileSuport instance = null;
	private NamespaceAccessToken token;
	private Structure structure;
	private Security security;
	private Lock lock;
	private Content content;
	private SlideToken slideToken;
	private static final String CONFIG_SLIDE = "/slide.properties";
	private String MAX_FILE_SIZE = "";
	private String MAX_STORAGE_SIZE = "";

	public static synchronized FileSuport getInstance() {
		if (instance == null) {
			instance = new FileSuport();
		}
		
			return instance;
		
	}

	/** Creates a new instance of FileSuport */
	private FileSuport() {
		init();
	}

	private void init() {
		try {
			Domain.init(getConfigLocation());
		} catch (Exception e) {
			System.out.println("INIT FAILED");
		}
		String namespace = Domain.getDefaultNamespace();
		this.token =
			Domain.accessNamespace(new SecurityToken(new String()), namespace);
		this.structure = token.getStructureHelper();
		this.security = token.getSecurityHelper();
		this.lock = token.getLockHelper();
		this.content = token.getContentHelper();
		this.slideToken =
			new SlideTokenImpl(new CredentialsToken(new String("root")));
		this.MAX_FILE_SIZE = getMaxFileSize();
		this.MAX_STORAGE_SIZE = getMaxStorageSize();
	}

	/**
	 * @return
	 */
	private String getMaxFileSize() {
		Properties properties = new Properties();
		String maxFileSize = null;
		try {
			properties.load(
				getClass().getResourceAsStream(FileSuport.CONFIG_SLIDE));
			maxFileSize = properties.getProperty("maxFileSize");
		} catch (IOException e) {
			System.out.println("maxFileSize->NOT FOUND");
		}
		return maxFileSize;
	}

	/**
	 * @return
	 */
	private String getMaxStorageSize() {
		Properties properties = new Properties();
		String maxStorageSize = null;
		try {
			properties.load(
				getClass().getResourceAsStream(FileSuport.CONFIG_SLIDE));
			maxStorageSize = properties.getProperty("maxStorageSize");
		} catch (IOException e) {
			System.out.println("maxStorageSize->NOT FOUND");
		}
		return maxStorageSize;
	}
	
	
	public boolean isFileNameValid(FileSuportObject file) {
		String uri = "/files"+file.getUri()+"/"+file.getFileName();
		if (uri.length()>255) {
			return false;
		}
			return true;
	}
	
	
	public boolean isStorageAllowed(FileSuportObject file) {
		boolean result = isFileSizeAllowed(file);
		long sizeInByte = getDirectorySize("/files" + file.getRootUri());
		float dirSize = sizeInByte / (1024 * 1024);
		float fileSize = file.getContent().length / (1024 * 1024);
		if (result
			&& (dirSize + fileSize
				< (new Float(MAX_STORAGE_SIZE)).floatValue())) {
			result = true;
		}else {
			result=false;
		}
		return result;
	}
	

	public boolean isFileSizeAllowed(FileSuportObject file) {
		float size = file.getContent().length / (1024 * 1024);
		if (size < (new Float(MAX_FILE_SIZE)).floatValue()) {
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	private String getConfigLocation() {
		Properties properties = new Properties();
		String location = null;
		try {
			properties.load(
				getClass().getResourceAsStream(FileSuport.CONFIG_SLIDE));
			location = properties.getProperty("location");
		} catch (IOException e) {
			System.out.println("location->NOT FOUND");
		}
		return location;
	}

	/**
	 * initialize slideToken
	 * @param userid Credentials stored in this token
	 * @throws Exception
	 */
	private void init(String userid) throws Exception {
		CredentialsToken credToken = new CredentialsToken(new String(userid));
		slideToken = new SlideTokenImpl(credToken);
	}

	private void addContents(
		byte[] fileData,
		String path,
		String contenType,
		String linkName)
		throws SlideException {
		try {
			beginTransaction();
			
			SubjectNode file = 	new SubjectNode();
			structure.create(slideToken, file, path);
			content.create(slideToken, path, true);

			NodeRevisionDescriptor currentRevisionDescriptor =
				new NodeRevisionDescriptor(-1);
			currentRevisionDescriptor.setContentType(contenType);
			if (linkName == null || linkName.trim().equals("")) {
				linkName = path.split("/")[path.split("/").length - 1];
			}
			currentRevisionDescriptor.setProperty("linkName", linkName);
			NodeRevisionContent currentRevisionContent =
				new NodeRevisionContent();
			currentRevisionContent.setContent(fileData);
			content.create(
				slideToken,
				path,
				currentRevisionDescriptor,
				currentRevisionContent);
			commitTransaction();
		} catch (SlideException e) {
			try {
				abortTransaction();
			} catch (IllegalStateException e1) {
			
			} catch (SecurityException e1) {
			
			} catch (SystemException e1) {
				
			}
			throw e;
		} catch (Exception e) {
			try {
				abortTransaction();
			} catch (IllegalStateException e1) {
				
			} catch (SecurityException e1) {
				
			} catch (SystemException e1) {
				
			}
			throw new SlideException("runtime exception");
		}
	}
	
	private void beginTransaction()throws SlideException, NotSupportedException, SystemException{
				token.begin();	
	}

	private void commitTransaction() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException {
		if (token.getStatus()!=Status.STATUS_NO_TRANSACTION) {
			token.commit();	
		}		
		}
	
	private void abortTransaction() throws IllegalStateException, SecurityException, SystemException{
			if (token.getStatus()!=Status.STATUS_NO_TRANSACTION) {
				token.rollback();
			}
			
			
		}	
	/**
		* create Folder
		* @param folder folder path
		* @throws SlideException
		*/
	public void makeFolder(String folder) throws SlideException {
		makeFolder(getSlideToken(), folder);
	}

	/**
			* create Folder
			* @param folder folder path
			* @throws SlideException
			*/
	public void makeFolder(SlideToken slideToken, String folder)
		throws SlideException {
		try {

			token.begin();
			structure.create(slideToken, new SubjectNode(), folder);
			token.commit();
		} catch (Exception e) {
			throw new SlideException("");
		}
	}

	/**
		* Returns the children of a folder(node) 
		* @param folder folder path
		* @return Enumeration of ObjectNode objects
		* @throws SlideException
		*/
	private Enumeration getChildrenList(String folder) throws SlideException {
		return structure.getChildren(
			slideToken,
			structure.retrieve(slideToken, folder));
	}

	/**
		 * The ObjectNode is a Folder or Content(file)?. 
		 * @param objects$B!!(Bobject node
		 * @return ture is folder. false is Content(file).
		 */
	private boolean isDirectory(ObjectNode objects) {
		if (objects instanceof SubjectNode && !isFile(objects)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isFile(ObjectNode objectNode) {
		if (objectNode instanceof SubjectNode
			&& !objectNode.hasChildren()
			&& hasRevisionContent(objectNode)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param objectNode
	 * @return
	 */
	private boolean hasRevisionContent(ObjectNode objectNode) {

		try {
			NodeRevisionContent nodeRevisionContent =
				getRevContentData(objectNode.getUri());
			if (nodeRevisionContent == null) {
				return false;
			} else {
				return true;
			}
		} catch (SlideException e) {
			return false;
		}
	}

	/**
		* A specific Content Data is taken out. 
		* @param path content path(file path)
		* @return NodeRevisionContent object of specific Content
		* @throws SlideException
		*/
	public NodeRevisionContent getRevContentData(String path)
		throws SlideException {
		NodeRevisionDescriptors revDescriptors =
			content.retrieve(slideToken, path);
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);

		NodeRevisionContent revContent =
			content.retrieve(slideToken, revDescriptors, revDescriptor);

		return revContent;
	}

	/**
		* @return Content object
		*/
	private Content getContent() {
		return content;
	}

	/**
		* @return Lock object
		*/
	private Lock getLock() {
		return lock;
	}

	/**
		* @return Security object
		*/
	private Security getSecurity() {
		return security;
	}

	/**
		* @return SlideToken object
		*/
	private SlideToken getSlideToken() {
		return slideToken;
	}

	/**
		* @return Structure object
		*/
	private Structure getStructure() {
		return structure;
	}

	/**
		* @return NamespaceAccessToken object
		*/
	private NamespaceAccessToken getToken() {
		return token;
	}

	/**
	 * @param content
	 */
	private void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @param lock
	 */
	private void setLock(Lock lock) {
		this.lock = lock;
	}

	/**
	 * @param security
	 */
	private void setSecurity(Security security) {
		this.security = security;
	}

	/**
	 * @param slideToken
	 */
	private void setSlideToken(SlideToken slideToken) {
		this.slideToken = slideToken;
	}

	/**
	 * @param structure
	 */
	private void setStructure(Structure structure) {
		this.structure = structure;
	}

	/**
	 * @param token
	 */
	private void setToken(NamespaceAccessToken token) {
		this.token = token;
	}

	private NamespaceAccessToken getSlideNamespace() {
		return this.token;
	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#storeFile(java.lang.String, byte[])
	 */
	public void storeFile(
		String fileName,
		String path,
		byte[] fileData,
		String contentType,
		String linkName)
		throws SlideException {
		if (!existsPath("/files" + path)) {
			createPath("/files" + path);
		}
		addContents(
			fileData,
			"/files" + path + "/" + fileName,
			contentType,
			linkName);
	}

	/**
	 * @param string
	 */
	private void createPath(String path) throws SlideException {
		String[] pathArray = path.split("/");
		String fullPath = "";
		for (int i = 0; i < pathArray.length; i++) {
			String folder = pathArray[i];
			fullPath += "/" + folder;
			if (!existsPath(fullPath)) {
				makeFolder(fullPath);
			}
		}

	}

	/**
	 * @param string
	 * @return
	 */
	private boolean existsPath(String path) {
		try {
			ObjectNode objectNode = structure.retrieve(getSlideToken(), path);
			if (objectNode == null) {
				return false;
			} else {
				return true;
			}
		} catch (SlideException e) {
			return false;
		}

	}

	public void deleteFile(String filePath) throws SlideException {
		Structure structure = getStructure();
		Content content = getContent();
		if (!filePath.startsWith("/files")) {
			filePath="/files"+filePath;
		}
		try {
			token.begin();
			ObjectNode objectNode =
				structure.retrieve(getSlideToken(), filePath);
			NodeRevisionDescriptors nodeRevisionDescriptors =
				content.retrieve(getSlideToken(), filePath);
			NodeRevisionDescriptor nodeRevisionDescriptor =
				content.retrieve(getSlideToken(), nodeRevisionDescriptors);
			content.remove(getSlideToken(), filePath, nodeRevisionDescriptor);
			content.remove(getSlideToken(), nodeRevisionDescriptors);
			structure.remove(getSlideToken(), objectNode);
			token.commit();
		} catch (SlideException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new SlideException("runtime exception");
		} catch (Exception e) {
			throw new SlideException("runtime exception");
		}

	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#getLink(java.lang.String)
	 */
	public Object getLink(String path) {
		Structure structure = getStructure();
		try {
			ObjectNode objectNode = structure.retrieve(getSlideToken(), path);
			Enumeration links = objectNode.enumerateLinks();
			if (links.hasMoreElements()) {
				return links.nextElement();
			} else {
				//	structure.createLink(getSlideToken())
				return null;
			}
		} catch (SlideException e) {
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#getContentType(java.lang.String)
	 */
	public String getContentType(String path) throws SlideException {

		NodeRevisionDescriptors revDescriptors =
			content.retrieve(slideToken, path);
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);
		return revDescriptor.getContentType();
	}

	public NodeRevisionDescriptor getRevisionDescriptor(String path)
		throws SlideException {

		NodeRevisionDescriptors revDescriptors =
			content.retrieve(slideToken, path);
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);
		return revDescriptor;
	}

	/**
	 *	
	 * @param path folder path
	 * @throws SlideException
	 **/
	public List getDirectoryFiles(String path) throws SlideException {
		ObjectNode folder;
		List files = new ArrayList();
		try {
			if (path.startsWith("/files")) {
				folder = structure.retrieve(slideToken, path);
			} else {				
				folder = structure.retrieve(slideToken, "/files" + path);				
			}
		} catch (SlideException e) {
			return files;
		}
		Enumeration children = folder.enumerateChildren();
		while (children.hasMoreElements()) {
			String objectUri = (String) children.nextElement();
			ObjectNode object = structure.retrieve(slideToken, objectUri);
			if (isFile(object)) {
				FileSuportObject file = new FileSuportObject();
				NodeRevisionDescriptor nodeRevisionDescriptor =
					getRevisionDescriptor(objectUri);
				file.setFileName(
					object.getUri().split("/")[object.getUri().split(
						"/").length
						- 1]);
				file.setUri(object.getUri());
				file.setLinkName(
					(String) nodeRevisionDescriptor
						.getProperty("linkName")
						.getValue());
				files.add(file);

			}
		}

		return files;
	}

	public List getSubDirectories(String path) throws SlideException {
		List directories = new ArrayList();
		Enumeration lists;
		if (path.startsWith("/files")) {
			lists = this.getChildrenList(path);
		} else {
			lists = this.getChildrenList("/files" + path);
		}
		while (lists.hasMoreElements()) {
			ObjectNode list = (ObjectNode) lists.nextElement();
			if (isDirectory(list)) {
				directories.add(list.getUri());
			}
		}
		return directories;
	}

	public long getDirectorySize(String path) {
		long size = 0;
		try {
			ObjectNode folder = null;
			if (path.startsWith("/files")) {
				folder = structure.retrieve(slideToken, path);
			} else {
				folder = structure.retrieve(slideToken, "/files" + path);
			}
			Enumeration children = folder.enumerateChildren();
			while (children.hasMoreElements()) {

				String objectUri = (String) children.nextElement();
				ObjectNode object = structure.retrieve(slideToken, objectUri);
				if (isDirectory(object)) {
					size = size + getDirectorySize(object.getUri());
				} else {
					size = size + getFileSize(object);
				}
			}
		} catch (SlideException e) {
		}
		return size;
	}

	/**
	 * @param list
	 * @return
	 */
	private long getFileSize(ObjectNode file) throws SlideException {
		NodeRevisionDescriptors revDescriptors =
			content.retrieve(getSlideToken(), file.getUri());
		NodeRevisionDescriptor revDescriptor =
			content.retrieve(slideToken, revDescriptors);
		return revDescriptor.getContentLength();
	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#retrieveFile(java.lang.String)
	 */
	public FileSuportObject retrieveFile(String path) {
		if (!path.startsWith("/files")) {
			path="/files"+path;
		}
		FileSuportObject file=null;
		try {
			
			NodeRevisionContent nodeRevisionContent = getRevContentData(path);
			file = new FileSuportObject();
			file.setContent(nodeRevisionContent.getContentBytes());
			NodeRevisionDescriptor nodeRevisionDescriptor =
				getRevisionDescriptor(path);

			file.setContentType(nodeRevisionDescriptor.getContentType());
			file.setFileName(path.split("/")[path.split("/").length - 1]);
			file.setLinkName(
				(String) nodeRevisionDescriptor.getProperty("linkName").getValue());
		} catch (SlideException e) {
			file=null;
		}
		return file;
	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#storeFile(fileSuport.FileSuportObject)
	 */
	public boolean storeFile(FileSuportObject file) throws SlideException {
		boolean result = false;
		FileSuportObject fileInDb = retrieveFile(file.getUri()+"/"+file.getFileName());
		if (fileInDb==null) {
			storeFile(
						file.getFileName(),
						file.getUri(),
						file.getContent(),
						file.getContentType(),
						file.getLinkName());
			result=true;			
		}
		
		return result;	

	}

	/* (non-Javadoc)
	 * @see fileSuport.IFileSuport#deleteFolder(java.lang.String)
	 */
	public void deleteFolder(String path) throws SlideException {
		List files;
		List subFolders;
		if (path.startsWith("/files")) {
			 files = getDirectoryFiles(path);
			 subFolders = getSubDirectories(path);
		} else {
			 files = getDirectoryFiles("/files" + path);
			 subFolders = getSubDirectories("/files" + path);
		}
		Iterator iterFiles = files.iterator();
		while (iterFiles.hasNext()) {
			FileSuportObject file = (FileSuportObject) iterFiles.next();
			deleteFile(file.getUri());
		}
		Iterator iterFolders = subFolders.iterator();
		while (iterFolders.hasNext()) {

			deleteFolder((String) iterFolders.next());

		}

	}

}