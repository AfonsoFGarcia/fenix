package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.File;
import pt.ist.fenixWebFramework.renderers.StringRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class FileAsPictureRenderer extends StringRenderer {

	private String classes;

	@Override
	public String getClasses() {
		return classes;
	}

	@Override
	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	public HtmlComponent render(Object object, Class type) {
		File file = (File) object;

		HtmlImage image = new HtmlImage();

		image.setSource(FileManagerFactory.getFactoryInstance().getFileManager()
				.formatDownloadUrl(file.getExternalStorageIdentification(), file.getFilename()));
		image.setTitle(file.getDisplayName());

		image.setClasses(classes);

		return image;
	}

}
