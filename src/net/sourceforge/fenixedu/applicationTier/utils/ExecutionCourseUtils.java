/*
 * Created on Jul 26, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExecutionCourseUtils {

    public static void copySectionsAndItems(Site siteFrom, Site siteTo) throws DomainException {

        if (siteFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = siteFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                Section sectionFrom = (Section) associatedSections.next();
                if (sectionFrom.getSuperiorSection() == null) {
                    Section sectionTo = siteTo.createSection(sectionFrom.getName(), null, sectionFrom
                            .getSectionOrder());
                    copyItemsFrom(sectionFrom, sectionTo);
                    copySubSectionsAndItemsFrom(sectionFrom, sectionTo, siteTo);
                }
            }
        }
    }

    private static void copySubSectionsAndItemsFrom(Section sectionFrom, Section sectionTo,
            Site siteTo) throws DomainException {
        if (sectionFrom.getAssociatedSectionsCount() > 0) {
            Iterator associatedSections = sectionFrom.getAssociatedSectionsIterator();
            while (associatedSections.hasNext()) {
                Section subSectionFrom = (Section) associatedSections.next();
                if (subSectionFrom.getSuperiorSection() != null) {
                    Section subSectionTo = siteTo.createSection(subSectionFrom.getName(), sectionTo,
                            subSectionFrom.getSectionOrder());
                    copyItemsFrom(subSectionFrom, subSectionTo);
                    copySubSectionsAndItemsFrom(subSectionFrom, subSectionTo, siteTo);
                }
            }
        }
    }

    private static void copyItemsFrom(Section sectionFrom, Section sectionTo) throws DomainException {
        if (sectionFrom.getAssociatedItemsCount() > 0) {
            Iterator associatedItems = sectionFrom.getAssociatedItemsIterator();
            while (associatedItems.hasNext()) {
                Item item = (Item) associatedItems.next();
                sectionTo.insertItem(item.getName(), item.getInformation(), item.getUrgent(), item
                        .getItemOrder());
            }
        }
    }

    public static void deleteSectionsAndItemsIfExistFrom(Site siteTo) throws DomainException {
        if (siteTo.getAssociatedSectionsCount() > 0) {                   
            final List<Section> associatedSections = new ArrayList();
            associatedSections.addAll(siteTo.getAssociatedSections());
            for(final Section section : associatedSections){
            	section.delete();
            }
        }
    }

    public static List<BibliographicReference> copyBibliographicReference(final ExecutionCourse executionCourseFrom,
            ExecutionCourse executionCourseTo) {
        final List<BibliographicReference> notCopiedBibliographicReferences = new ArrayList();
        
        if (executionCourseFrom.getAssociatedBibliographicReferencesCount() > 0) {
            Iterator bibliographicReferences = executionCourseFrom
                    .getAssociatedBibliographicReferencesIterator();
            while (bibliographicReferences.hasNext()) {
                BibliographicReference bibliographicReference = (BibliographicReference) bibliographicReferences
                        .next();
                if (canAddBibliographicReferenceTo(executionCourseTo, bibliographicReference)) {
                    executionCourseTo.createBibliographicReference(bibliographicReference.getTitle(),
                            bibliographicReference.getAuthors(), bibliographicReference.getReference(),
                            bibliographicReference.getYear(), bibliographicReference.getOptional());
                }
                else {
                    notCopiedBibliographicReferences.add(bibliographicReference);                
                }
            }
        }
        return notCopiedBibliographicReferences;
    }

    private static boolean canAddBibliographicReferenceTo(final ExecutionCourse executionCourse,
            final BibliographicReference bibliographicReferenceToAdd) {
        final Iterator associatedBibliographicReferences = executionCourse
                .getAssociatedBibliographicReferencesIterator();
        while (associatedBibliographicReferences.hasNext()) {
            BibliographicReference bibliographicReference = (BibliographicReference) associatedBibliographicReferences
                    .next();
            if (bibliographicReference.getTitle().equals(bibliographicReferenceToAdd.getTitle())) {
                return false;
            }
        }
        return true;
    }

    public static void copyEvaluationMethod(final ExecutionCourse executionCourseFrom,
            ExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            final EvaluationMethod evaluationMethodFrom = executionCourseFrom.getEvaluationMethod();
            final EvaluationMethod evaluationMethodTo = executionCourseTo.getEvaluationMethod();
            if (evaluationMethodTo == null) {
                executionCourseTo.createEvaluationMethod(evaluationMethodFrom.getEvaluationElements());
            } else {
                evaluationMethodTo.edit(evaluationMethodFrom.getEvaluationElements());
            }
        }
    }

}
