/*
 * Created on Jun 11, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoSitePublications;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.publication.IPublication;
import Dominio.publication.Publication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import constants.publication.PublicationConstants;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
public class DeletePublicationInTeacherList implements IServico {

	/**
	 *  
	 */
	public DeletePublicationInTeacherList() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		// TODO Auto-generated method stub
		return "DeletePublicationInTeacherList";
	}

	public SiteView run(Integer teacherId, Integer publicationId) throws FenixServiceException {
		InfoSitePublications infoSitePublications = new InfoSitePublications();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
			infoSitePublications.setInfoTeacher(infoTeacher);

			IPersistentPublication persistentPublication = sp.getIPersistentPublication();
			//IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
				//	publicationId);

			List infoPublications = getInfoPublications(teacher, persistentTeacher, publicationId,
					persistentPublication);

			List infoPublicationCientifics = getInfoPublicationsType(infoPublications,
					PublicationConstants.CIENTIFIC);

			List infoPublicationDidatics = getInfoPublicationsType(infoPublications,
					PublicationConstants.DIDATIC);

			infoSitePublications.setInfoCientificPublications(infoPublicationCientifics);
			infoSitePublications.setInfoDidaticPublications(infoPublicationDidatics);

			return new SiteView(infoSitePublications);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	public List getInfoPublications(ITeacher teacher, IPersistentTeacher persistentTeacher, Integer publicationId, IPersistentPublication persistentPublication)
			throws FenixServiceException, ExcepcaoPersistencia {

		List publications = teacher.getTeacherPublications();
		List newPublications = new ArrayList();
		List infoPublications = new ArrayList();
		Boolean contains = Boolean.FALSE;

		IPublication publicationToRemove = null;
		Iterator iterator = publications.iterator();
		while (iterator.hasNext()) {
			IPublication publicationTeacher2 = (IPublication) iterator.next();
			if (publicationTeacher2.getIdInternal().intValue() == publicationId.intValue()) {
				contains = Boolean.TRUE;
				publicationToRemove = (IPublication) publicationTeacher2;
			}
		}

		if (!contains.booleanValue()) {
			throw new NotExistingServiceException();
		}

//		IPublication publicationToRemove = null;
//		iterator = publications.iterator();
//		while (iterator.hasNext()) {
//			IPublication publicationTeacher = (IPublication) iterator.next();
//			if (publicationTeacher.getIdInternal().intValue() != publicationId.intValue()) {
//				newPublications.add(publicationTeacher);
//			} else {
//				publicationToRemove = (IPublication) publicationTeacher;
//			}
//		}

			//teacher.setTeacherPublications(newPublications);
			persistentTeacher.simpleLockWrite(teacher);
			persistentPublication.simpleLockWrite(publicationToRemove);
			
			
			publicationToRemove.getPublicationTeachers().remove(teacher);
			teacher.getTeacherPublications().remove(publicationToRemove);
			
			List newPublicationsEdited = teacher.getTeacherPublications();
			infoPublications = (List) CollectionUtils.collect(newPublicationsEdited, new Transformer() {
				public Object transform(Object object) {
					IPublication publication = (IPublication) object;
					IPublication publication2 = publication;
					publication2.setPublicationString(publication.toString());
					return Cloner.copyIPublication2InfoPublication(publication2);
				}
			});

		return infoPublications;
	}

	List getInfoPublicationsType(List infoPublications, Integer typePublication) {

		List newInfoPublications = new ArrayList();

		if (infoPublications != null || infoPublications.size() != PublicationConstants.ZERO_VALUE) {
			Iterator iterator = infoPublications.iterator();
			while (iterator.hasNext()) {
				InfoPublication infoPublication = (InfoPublication) iterator.next();
				if (infoPublication.getDidatic().intValue() == typePublication.intValue()) {
					newInfoPublications.add(infoPublication);
				}
			}
		}
		return newInfoPublications;
	}

}