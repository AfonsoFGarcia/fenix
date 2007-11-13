package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent {

    protected abstract AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry);      
    protected abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);
    protected abstract boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry);
    protected abstract boolean areIntersectionsPossible();
    protected abstract boolean areOutOfBoundsPossible();
    protected void afterRedefineEntry() {}

    public static final Comparator<AcademicCalendarEntry> COMPARATOR_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    protected AcademicCalendarEntry() {
	super();	
	setRootDomainObject(RootDomainObject.getInstance());	
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public void delete(AcademicCalendarRootEntry rootEntry) {
	if(canBeDeleted(rootEntry)) {
	    getBasedEntries().clear();
	    super.setParentEntry(null);
	    super.setTemplateEntry(null);	
	    removeRootDomainObject();
	    deleteDomainObject();
	}	
	throw new DomainException("error.now.its.impossible.delete.entry.but.in.the.future.will.be.possible");
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end, 
	    AcademicCalendarRootEntry rootEntry, SeasonType seasonType, AcademicCalendarEntry templateEntry) {

	if(isRoot() || rootEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}
	
	if(!rootEntry.equals(getRootEntry())) {	    	    	    	
	    if(getBegin().isEqual(begin) && getEnd().isEqual(end)) {
		throw new DomainException("error.AcademicCalendarEntry.unchanged.dates");
	    }	    	    
	    AcademicCalendarEntry newParentEntry = createVirtualPathUntil(getParentEntry(), rootEntry);	    
	    AcademicCalendarEntry newEntry = createVirtualEntry(newParentEntry);
	    newEntry.edit(title, description, begin, end, rootEntry, seasonType, templateEntry);	    
	    return newEntry;

	} else {
	    boolean redefinedEntry = isVirtual();	    	    
	    setTitle(title);
	    setDescription(description);
	    setTimeInterval(begin, end, getParentEntry());	    
	    
	    if(redefinedEntry) {
		afterRedefineEntry();
	    }	    
	    return this;
	}
    }   
    
    protected void initEntry(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description, 
	    DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	if(rootEntry == null || parentEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}

	parentEntry = !parentEntry.getRootEntry().equals(rootEntry) ? createVirtualPathUntil(parentEntry, rootEntry) : parentEntry;

	setParentEntry(parentEntry);			
	setTitle(title);
	setDescription(description);
	setTimeInterval(begin, end, parentEntry);   
    }          

    protected void initVirtualEntry(AcademicCalendarEntry parentEntry, AcademicCalendarEntry templateEntry) {		
	setParentEntry(parentEntry);	
	setTemplateEntry(templateEntry);
    }

    private AcademicCalendarEntry createVirtualPathUntil(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntryDestination) {	
	if(!entry.isRoot()) {

	    List<AcademicCalendarEntry> entryPath = entry.getEntryFullPath();
	    entryPath.remove(0);//remove root entry

	    AcademicCalendarEntry parentEntry = rootEntryDestination;
	    AcademicCalendarEntry virtualOrRedefinedEntry = rootEntryDestination;

	    for (AcademicCalendarEntry entryToMakeCopy : entryPath) {
		if(virtualOrRedefinedEntry != null) {
		    virtualOrRedefinedEntry = entryToMakeCopy.getVirtualOrRedefinedEntryIn((AcademicCalendarRootEntry) rootEntryDestination);					
		} 
		if(virtualOrRedefinedEntry == null) {
		    parentEntry = entryToMakeCopy.createVirtualEntry(parentEntry);		
		} else {
		    parentEntry = virtualOrRedefinedEntry;
		}
	    }	
	    return parentEntry;

	} else {
	    return rootEntryDestination;
	}	
    }

    private AcademicCalendarEntry getVirtualOrRedefinedEntryIn(AcademicCalendarRootEntry rootEntry) {
	if(rootEntry != null) {
	    List<AcademicCalendarEntry> basedEntries = getBasedEntries();
	    for (AcademicCalendarEntry entry : basedEntries) {
		if(entry.getRootEntry().equals(rootEntry)) {
		    return entry;
		}
	    }
	}
	return null;
    }

    private boolean canBeDeleted(AcademicCalendarRootEntry rootEntry) {
	if(!getRootEntry().equals(rootEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.different.rootEntry");
	}
	if(!getChildEntries().isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.has.childs");
	}	
	return true;
    }

    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
	if(templateEntry != null && (!templateEntry.getClass().equals(getClass()) || getBasedEntries().contains(templateEntry))) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
	}
	super.setTemplateEntry(templateEntry);
    }

    public AcademicCalendarEntry getOriginalTemplateEntry() {
	if(isVirtual()) {
	    return getTemplateEntry().getOriginalTemplateEntry();	    
	}
	return this;
    }
    
    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
	if(parentEntry == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry");
	}
	if(isParentEntryInvalid(parentEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.parent.entry", getClass().getSimpleName(), parentEntry.getClass().getSimpleName());
	}
	if(parentEntry.exceededNumberOfChildEntries(this)) {
	    throw new DomainException("error.AcademicCalendarEntry.number.of.subEntries.exceeded");
	}
	super.setParentEntry(parentEntry);
    } 

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.title");
	}
	super.setTitle(title);    
    }

    public boolean isRedefined() {
	return hasTemplateEntry() && super.getBegin() != null;
    }

    public boolean isVirtual() {
	return hasTemplateEntry() && super.getBegin() == null;
    }

    public EntryState getEntryState() {
	return isVirtual() ? EntryState.VIRTUAL : isRedefined() ? EntryState.REDEFINED : EntryState.ORIGINAL;
    }

    public static enum EntryState {

	VIRTUAL, REDEFINED, ORIGINAL;

	public String getName() {
	    return name();
	}
    }

    public List<AcademicCalendarEntry> getEntryFullPath() {
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();	
	result.add(this);	
	AcademicCalendarEntry parentEntry = getParentEntry();
	while(parentEntry != null) {	    
	    result.add(0, parentEntry);
	    parentEntry = parentEntry.getParentEntry();
	}	
	return result;
    }

    private void setTimeInterval(DateTime begin, DateTime end, AcademicCalendarEntry parentEntry) {

	if (begin == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.begin.dateTime");
	}
	if (end == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.end.dateTime");
	}
	if (!end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}	

	if(parentEntry != null) {	    
	    if(!parentEntry.areOutOfBoundsPossible()) {
		if(parentEntry.getBegin().isAfter(begin) || parentEntry.getEnd().isBefore(end)) {
		    throw new DomainException("error.AcademicCalendarEntry.invalid.dates");
		}
	    }
	    if(!parentEntry.areIntersectionsPossible()) {
		for (AcademicCalendarEntry childEntry : parentEntry.getChildEntries()) {
		    if(!childEntry.equals(this) && childEntry.entriesTimeIntervalIntersection(begin, end)) {
			throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
		    }
		}
	    }
	}

	setBegin(begin);
	setEnd(end);
    }

    @Override
    public DateTime getEnd() {
	if(isVirtual()) {
	    return getTemplateEntry().getEnd();
	}
	return super.getEnd();
    }

    @Override
    public DateTime getBegin() {
	if(isVirtual()) {
	    return getTemplateEntry().getBegin();
	}
	return super.getBegin();
    }

    @Override
    public MultiLanguageString getTitle() {
	if(isVirtual() && !isRoot()) {
	    return getTemplateEntry().getTitle();
	}
	return super.getTitle();
    }

    @Override
    public MultiLanguageString getDescription() {
	if(isVirtual() && !isRoot()) {
	    return getTemplateEntry().getDescription();
	}
	return super.getDescription();
    }

    public String getPresentationTimeInterval() {
	if(!isRoot()) {
	    return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
	} else {
	    DateTime begin = getBegin();	    
	    return  begin != null ? begin.toString("dd-MM-yyyy HH:mm") + " - " + "**-**-**** **:**" : "";
	}
    }

    public MultiLanguageString getType() {
	MultiLanguageString type = new MultiLanguageString();
	String key = "label." + getClass().getSimpleName() + ".type";
	type.setContent(Language.pt, ResourceBundle.getBundle("resources/ManagerResources", new Locale("pt", "PT")).getString(key));	
	return type;
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {	
	return getRootEntry();
    }

    public AcademicCalendarRootEntry getRootEntry() {
	if(isRoot()) {
	    return (AcademicCalendarRootEntry) this;
	}
	return getParentEntry().getRootEntry();
    }

    public List<AcademicCalendarEntry> getAllChildEntriesOrderByDate(Class<? extends AcademicCalendarEntry> entryClass, 
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {

	List<AcademicCalendarEntry> allChildEntries = getAllChildEntries(entryClass, parentEntryClass);
	Collections.sort(allChildEntries, AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);
	return allChildEntries;
    }
    
    public List<AcademicCalendarEntry> getAllChildEntriesOrderByDateInReverseMode(Class<? extends AcademicCalendarEntry> entryClass, 
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {

	List<AcademicCalendarEntry> allChildEntries = getAllChildEntries(entryClass, parentEntryClass);
	Collections.sort(allChildEntries, new ReverseComparator(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE));
	return allChildEntries;	
    }   

    public List<AcademicCalendarEntry> getAllChildEntries(Class<? extends AcademicCalendarEntry> entryClass, Class<? extends AcademicCalendarEntry> parentEntryClass) {
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	for (AcademicCalendarEntry subEntry : getChildEntriesWithTemplateEntries()) {
	    if (subEntry.getClass().equals(entryClass) && subEntry.getParentEntry().getClass().equals(parentEntryClass)) {
		result.add(subEntry);		
	    }
	    result.addAll(subEntry.getAllChildEntries(entryClass, parentEntryClass));
	}		
	return result;	
    } 

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries() {	
	List<AcademicCalendarEntry> childEntriesWithTemplateEntries = getChildEntriesWithTemplateEntries(null, null);	
	return childEntriesWithTemplateEntries;
    }
       
    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntriesOrderByDate(DateTime begin, DateTime end) {	
	List<AcademicCalendarEntry> childEntriesWithTemplateEntries = getChildEntriesWithTemplateEntries(begin, end);
	Collections.sort(childEntriesWithTemplateEntries, COMPARATOR_BEGIN_DATE);
	return childEntriesWithTemplateEntries;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(DateTime begin, DateTime end) {

	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	Set<AcademicCalendarEntry> templateEntriesToRemove = new HashSet<AcademicCalendarEntry>();
	
	for (AcademicCalendarEntry entry : getChildEntries()) {
	    if(entry.isRedefined() || entry.isVirtual()) {		
		templateEntriesToRemove.add(entry.getTemplateEntry());		
	    }
	    if(begin == null || entry.belongsToPeriod(begin, end)) {
		result.add(entry);	    	    
	    }
	}	
	if(hasTemplateEntry()) {	    
	    for (AcademicCalendarEntry entry : getTemplateEntry().getChildEntriesWithTemplateEntries(begin, end)) {
		if(!templateEntriesToRemove.contains(entry)) {
		    result.add(entry);	    	    
		}
	    }	    	   	    
	}
	
	return result;
    }      
       
    public Set<AcademicCalendarEntry> getChildEntriesOrderByDate(){
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(COMPARATOR_BEGIN_DATE);
	result.addAll(getChildEntries());
	return result;
    }

    public Set<AcademicCalendarEntry> getChildEntriesOrderByDate(DateTime begin, DateTime end){	
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);	
	for (AcademicCalendarEntry academicCalendarEntry : getChildEntriesSet()) {
	    if(academicCalendarEntry.belongsToPeriod(begin, end)) {
		result.add(academicCalendarEntry);
	    }
	}	
	return result;
    }   

    public List<AcademicCalendarEntry> getChildEntries(Class<? extends AcademicCalendarEntry> entryClass){
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	for (AcademicCalendarEntry subEntry : getChildEntriesSet()) {
	    if (subEntry.getClass().equals(entryClass)) {
		result.add(subEntry);
	    }
	}
	return result;
    }  
    
    public List<AcademicCalendarEntry> getChildEntriesOrderByDateInReverseMode(Class<? extends AcademicCalendarEntry> entryClass){
	List<AcademicCalendarEntry> childEntries = getChildEntries(entryClass);
	Collections.sort(childEntries, new ReverseComparator(COMPARATOR_BEGIN_DATE));
	return childEntries;
    }
   
    public long getDurationMillis() {
	return getEnd().getMillis() - getBegin().getMillis();
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
	return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public boolean containsInstant(long instant) {
	return getBegin().getMillis() <= instant && getEnd().getMillis() >= instant;
    } 

    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
	return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public List<Interval> getGanttDiagramEventSortedIntervals() {
	List<Interval> result = new ArrayList<Interval>();
	result.add(new Interval(getBegin(), getEnd()));
	return result;
    }

    public MultiLanguageString getGanttDiagramEventName() {
	return getTitle();
    }

    public int getGanttDiagramEventOffset() {
	if(getParentEntry() == null) {
	    return 0;
	}
	return getParentEntry().getGanttDiagramEventOffset() + 1;
    }

    public String getGanttDiagramEventObservations() {
	return "-"; 
    }

    public String getGanttDiagramEventPeriod() {
	return getBegin().toString("dd/MM/yyyy HH:mm") + " - " + getEnd().toString("dd/MM/yyyy HH:mm"); 
    }

    public String getGanttDiagramEventIdentifier() {
	return getIdInternal().toString();
    }

    public boolean isAcademicYear() {
	return false;
    }

    public boolean isAcademicSemester() {
	return false;
    }

    public boolean isAcademicTrimester() {
	return false;
    }

    public boolean isLessonsPerid() {
	return false;
    }

    public boolean isExamsPeriod() {
	return false;
    }

    public boolean isEnrolmentsPeriod() {
	return false;
    }

    public boolean isGradeSubmissionPeriod() {
	return false;
    }

    public boolean isRoot() {
	return false;
    }

    protected AcademicCalendarEntry getEntryByInstant(final long instant,
	    final Class<? extends AcademicCalendarEntry> entryClass,
	    final Class<? extends AcademicCalendarEntry> parentEntryClass,
	    final AcademicCalendarEntry first) {

	if (containsInstant(instant)) {
	    if (this.getClass().equals(entryClass) && getParentEntry().getClass().equals(parentEntryClass) && !isRedefinedBy(this, first)) {
		return this;
	    } else {
		AcademicCalendarEntry result = null;

		for (final AcademicCalendarEntry childEntry : getChildEntriesSet()) {
		    if (this == first || !isRedefinedBy(childEntry, first)) {
			final AcademicCalendarEntry childEntryResult = childEntry.getEntryByInstant(instant, entryClass, parentEntryClass);
			if (childEntryResult != null && (result == null || childEntryResult.getBegin().isAfter(result.getBegin()))) {
			    result = childEntryResult;
			}
		    }
		}

		final AcademicCalendarEntry templateEntry = getTemplateEntry();
		if (templateEntry != null) {
		    final AcademicCalendarEntry templateEntryResult = templateEntry.getEntryByInstant(instant, entryClass, parentEntryClass, first);
		    if (templateEntry != null && (result == null || templateEntryResult.getBegin().isAfter(result.getBegin()))) {
			result = templateEntryResult;
		    }
		}

		return result;
	    }
	}
	return null;
    }

    public AcademicCalendarEntry getEntryByInstant(final long instant, final Class<? extends AcademicCalendarEntry> entryClass, final Class<? extends AcademicCalendarEntry> parentEntryClass) {
	return getEntryByInstant(instant, entryClass, parentEntryClass, this);
    }

    protected boolean isRedefinedBy(final AcademicCalendarEntry child, final AcademicCalendarEntry currentParentEntry) {
	for (final AcademicCalendarEntry redefinedEntry : getBasedEntriesSet()) {
	    if (currentParentEntry.hasChildEntriesOrTemplatedEntry(redefinedEntry)) {
		return true;
	    }
	}
	return false;
    }

    protected boolean hasChildEntriesOrTemplatedEntry(final AcademicCalendarEntry academicCalendarEntry) {
	return getChildEntriesSet().contains(academicCalendarEntry)
		|| (hasTemplateEntry() && getTemplateEntry().hasChildEntriesOrTemplatedEntry(academicCalendarEntry));
    }

    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
	return getBegin().get(AcademicSemesterOfAcademicYearDateTimeFieldType.academicSemesterOfAcademicYear());
    }

    public AcademicCalendarEntry getEntryForCalendar(final AcademicCalendarRootEntry academicCalendar) {
	for (AcademicCalendarEntry parent = getParentEntry(); parent != null; parent = parent.getParentEntry()) {
	    if (parent == academicCalendar) {
		return this;
	    }
	}
	for (final AcademicCalendarEntry basedEntry : getBasedEntriesSet()) {
	    final AcademicCalendarEntry basedEntryFor = basedEntry.getEntryForCalendar(academicCalendar);
	    if (basedEntryFor != null) {
		return basedEntryFor;
	    }
	}
	return null;
    }

}
