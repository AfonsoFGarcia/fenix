package net.sourceforge.fenixedu.domain.caseHandling;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import dml.DomainClass;

public abstract class Process extends Process_Base implements Comparable<Process> {

    private static Map<String, Activity<? extends Process>> startActivities;

    private static Integer lock = 0;

    private static void load() {
	startActivities = new HashMap<String, Activity<? extends Process>>();
	final Collection<DomainClass> domainClasses = FenixFramework.getDomainModel().getDomainClasses();

	for (final DomainClass domainClass : domainClasses) {
	    try {
		final Class<?> clazz = Class.forName(domainClass.getFullName());
		if (Process.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
		    final Activity<? extends Process> activity = getStartActivity((Class<? extends Process>) clazz);
		    if (activity != null) {
			startActivities.put(domainClass.getFullName(), activity);
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public static Activity<? extends Process> getStartActivity(Class<? extends Process> process) throws InstantiationException,
	    IllegalAccessException {
	for (Class<?> clazz : process.getDeclaredClasses()) {
	    if (Activity.class.isAssignableFrom(clazz)) {
		if (clazz.isAnnotationPresent(StartActivity.class)) {
		    return (Activity<? extends Process>) clazz.newInstance();
		}
	    }
	}
	return null;
    }

    public static Activity<?> getStartActivity(String processName) {
	synchronized (lock) {
	    if (startActivities == null) {
		load();
	    }
	}

	Activity<? extends Process> activity = startActivities.get(processName);
	if (activity == null) {
	    throw new RuntimeException("can't find process " + processName);
	}
	return activity;
    }

    public static <T extends Process> T createNewProcess(IUserView userView, Class<? extends Process> processClass, Object object) {
	try {
	    return (T) getStartActivity(processClass).execute(null, userView, object);
	} catch (InstantiationException e) {
	    throw new RuntimeException(e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public static <T extends Process> T createNewProcess(IUserView userView, String processName, Object object) {
	return (T) getStartActivity(processName).execute(null, userView, object);
    }

    abstract public List<Activity> getActivities();

    public Process() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public final Process executeActivity(IUserView userView, String activityId, Object object) {
	Activity activity = getActivity(activityId);
	if (activity == null) {
	    // throw exception
	}

	return activity.execute(this, userView, object);
    }

    private Activity getActivity(String activityId) {
	for (Activity activity : getActivities()) {
	    if (activity.getId().equals(activityId)) {
		return activity;
	    }
	}
	return null;
    }

    public int compareTo(Process process) {
	return getIdInternal().compareTo(process.getIdInternal());
    }

    public List<Activity> getAllowedActivities(final IUserView userView) {
	final List<Activity> result = new ArrayList<Activity>();
	for (final Activity activity : getActivities()) {
	    try {
		activity.checkPreConditions(this, userView);
		result.add(activity);
	    } catch (PreConditionNotValidException e) {
	    }
	}
	return result;
    }

    public DateTime getWhenCreated() {
	return Collections.min(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getWhenDateTime();
    }

    public DateTime getWhenUpdated() {
	return Collections.max(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getWhenDateTime();
    }

    public String getCreatedBy() {
	return Collections.min(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getUserName();
    }

    public String getUpdatedBy() {
	return Collections.max(getProcessLogs(), ProcessLog.COMPARATOR_BY_WHEN).getUserName();
    }

    public abstract boolean canExecuteActivity(IUserView userView);

    public abstract String getDisplayName();
}
