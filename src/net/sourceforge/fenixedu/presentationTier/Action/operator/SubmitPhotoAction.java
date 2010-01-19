package net.sourceforge.fenixedu.presentationTier.Action.operator;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.StorePersonalPhoto;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;
import pt.ist.fenixWebFramework.servlets.filters.RequestWrapperFilter;

public class SubmitPhotoAction extends FenixDispatchAction {

    private static int outputPhotoWidth = 100;

    private static int outputPhotoHeight = 100;

    public ActionForward preparePhotoUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return mapping.findForward("chooseFile");
    }

    public ActionForward photoUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = UserView.getUser();

	DynaActionForm photoForm = (DynaActionForm) form;

	String fileName = (String) photoForm.get("theFile");
	String username = (String) photoForm.get("username");
	ActionMessages actionMessages = new ActionMessages();

	if (fileName == null || (fileName != null && fileName.length() == 0)) {
	    actionMessages.add("fileRequired", new ActionMessage("errors.fileRequired"));
	    saveMessages(request, actionMessages);
	    return mapping.findForward("chooseFile");
	}

	final UploadedFile formFile = ((Hashtable<String, UploadedFile>) request
		.getAttribute(RequestWrapperFilter.FenixHttpServletRequestWrapper.ITEM_MAP_ATTRIBUTE)).get("theFile");

	ContentType contentType = ContentType.getContentType(formFile.getContentType());
	if (contentType == null) {
	    actionMessages.add("unsupportedFile", new ActionMessage("errors.unsupportedFile"));
	    saveMessages(request, actionMessages);
	    return mapping.findForward("chooseFile");
	}

	// process image

	ByteArrayOutputStream outputStream = processImage(formFile.getFileData(), contentType);

	try {

	    StorePersonalPhoto.run(formFile.getFileData(), outputStream.toByteArray(), contentType, username);

	} catch (ExcepcaoInexistente e) {
	    actionMessages.add("unknownPerson", new ActionMessage("error.exception.nonExistingPerson", username));
	    saveMessages(request, actionMessages);
	    return mapping.findForward("chooseFile");
	}

	actionMessages.add("updateCompleted", new ActionMessage("label.operator.submit.ok", ""));
	saveMessages(request, actionMessages);
	return mapping.findForward("chooseFile");
    }

    private ByteArrayOutputStream processImage(byte[] content, ContentType contentType) throws IOException {
	ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
	BufferedImage photoImage = ImageIO.read(inputStream);

	// calculate resize factor
	double resizeFactor = Math.min((double) outputPhotoWidth / photoImage.getWidth(), (double) outputPhotoHeight
		/ photoImage.getHeight());

	if (resizeFactor == 1) {
	    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    outputStream.write(content);
	    return outputStream;
	}

	// resize image
	AffineTransform tx = new AffineTransform();
	tx.scale(resizeFactor, resizeFactor);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	photoImage = op.filter(photoImage, null);

	// set compression
	ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByMIMEType(contentType.getMimeType()).next();
	ImageWriteParam param = writer.getDefaultWriteParam();
	if (contentType.equals(ContentType.JPG)) {
	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    param.setCompressionQuality(1);
	}

	// write to stream
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	writer.setOutput(ImageIO.createImageOutputStream(outputStream));
	writer.write(null, new IIOImage(photoImage, null, null), param);

	return outputStream;

    }
}