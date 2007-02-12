package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;

public abstract class ResultPublicationService extends Service {

	protected static CreateResultPublication getCreateService() {
		return new CreateResultPublication();
	}

	protected static DeleteResultPublication getDeleteService() {
		return new DeleteResultPublication();
	}

	/*
	 * Methods used by CreateResultPublication and ImportBibtexPublication to
	 * create publications with one participator
	 */
	protected Book createBookFromBean(BookBean bean) {
		return new Book(bean.getPerson(), bean.getRole(), bean.getTitle(), bean.getKeywords(),
				getPublisher(bean), bean.getYear(), bean.getVolume(), bean.getSeries(), bean.getAddress(),
				bean.getEdition(), bean.getIsbn(), bean.getNumberPages(), bean.getLanguage(), bean
						.getCountry(), bean.getScope(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected BookPart createInbookFromBean(InbookBean bean) {
		return new BookPart(bean.getPerson(), bean.getRole(), BookPartType.Inbook, bean.getTitle(), bean
				.getKeywords(), bean.getChapter(), bean.getFirstPage(), bean.getLastPage(),
				getPublisher(bean), bean.getYear(), bean.getVolume(), bean.getSeries(), bean.getEdition(),
				bean.getCountry(), bean.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected BookPart createIncollectionFromBean(IncollectionBean bean) {
		return new BookPart(bean.getPerson(), bean.getRole(), BookPartType.Incollection, bean.getTitle(),
				bean.getKeywords(), bean.getBookTitle(), getPublisher(bean), bean.getYear(), bean
						.getFirstPage(), bean.getLastPage(), getOrganization(bean), bean.getCountry(), bean
						.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected Article createArticleFromBean(ArticleBean bean) {
		return new Article(bean.getPerson(), bean.getTitle(), bean.getKeywords(), bean.getJournal(), bean
				.getYear(), getOrganization(bean), bean.getVolume(), bean.getNumber(), bean.getFirstPage(),
				bean.getLastPage(), bean.getNote(), bean.getIssn(), bean.getLanguage(), bean.getCountry(),
				bean.getScope(), bean.getMonth(), bean.getUrl());
	}

	protected Inproceedings createInproceedingsFromBean(InproceedingsBean bean) {

		return new Inproceedings(bean.getPerson(), bean.getRole(), bean.getTitle(), bean.getKeywords(), bean
				.getYear(), bean.getConference(), bean.getScope(), getPublisher(bean), getOrganization(bean),
				bean.getAddress(), bean.getFirstPage(), bean.getLastPage(), bean.getNote(), bean
						.getLanguage(), bean.getMonth(), bean.getUrl());
	}

	protected Proceedings createProceedingsFromBean(ProceedingsBean bean) {

		return new Proceedings(bean.getPerson(), bean.getTitle(), bean.getKeywords(), bean.getYear(), bean
				.getConference(), bean.getScope(), getPublisher(bean), getOrganization(bean), bean
				.getAddress(), bean.getNote(), bean.getMonth(), bean.getUrl());
	}

	protected Thesis createThesisFromBean(ThesisBean bean) {
		return new Thesis(bean.getPerson(), bean.getThesisType(), bean.getTitle(), bean.getKeywords(),
				getOrganization(bean), bean.getYear(), bean.getAddress(), bean.getNote(), bean
						.getNumberPages(), bean.getLanguage(), bean.getMonth(), bean.getYearBegin(), bean
						.getMonthBegin(), bean.getUrl());
	}

	protected Manual createManualFromBean(ManualBean bean) {
		return new Manual(bean.getPerson(), bean.getTitle(), bean.getKeywords(), getOrganization(bean), bean
				.getYear(), bean.getAddress(), bean.getNote(), bean.getEdition(), bean.getMonth(), bean
				.getUrl());
	}

	protected TechnicalReport createTechnicalReportFromBean(TechnicalReportBean bean) {
		return new TechnicalReport(bean.getPerson(), bean.getTitle(), bean.getKeywords(),
				getOrganization(bean), bean.getYear(), bean.getTechnicalReportType(), bean.getNumber(), bean
						.getAddress(), bean.getNote(), bean.getNumberPages(), bean.getLanguage(), bean
						.getMonth(), bean.getUrl());
	}

	protected OtherPublication createOtherPublicationFromBean(OtherPublicationBean bean) {
		return new OtherPublication(bean.getPerson(), bean.getTitle(), bean.getKeywords(),
				getPublisher(bean), bean.getYear(), bean.getHowPublished(), bean.getNote(),
				bean.getAddress(), bean.getOtherPublicationType(), bean.getNumberPages(), bean.getLanguage(),
				bean.getCountry(), bean.getMonth(), bean.getUrl());
	}

	/*
	 * 
	 * 
	 */
	protected Unit getPublisher(ResultPublicationBean publicationBean) {
		Unit publisher = publicationBean.getPublisher();
		if ((publisher == null) && (publicationBean.getPublisherName() != null)
				&& (publicationBean.getPublisherName().length() > 0))
			publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
		return publisher;
	}

	protected Unit getOrganization(ResultPublicationBean publicationBean) {
		Unit organization = publicationBean.getOrganization();
		if ((organization == null) && (publicationBean.getOrganizationName() != null)
				&& (publicationBean.getOrganizationName().length() > 0))
			organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
		return organization;
	}

}
