/*
 * Created on Jul 31, 2004
 */

package Dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author joaosa-rmalo
 */

public class AttendsSet extends DomainObject implements IAttendsSet{
	private String name;
	private List attendInAttendsSet;
	private List studentGroups;
	private Integer keyGroupProperties;
	private IGroupProperties groupProperties;
	
	public AttendsSet () {}
	
	
	public AttendsSet (String name) {
		this.name=name;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name=name;
	}
	
	
	public List getAttendInAttendsSet () {
		return attendInAttendsSet;
	}
	
	
	public void setAttendInAttendsSet (List attendInAttendsSet) {
		this.attendInAttendsSet=attendInAttendsSet;
	}
	
	
	
	public List getStudentGroups () {
		return studentGroups;
	}
	
	
	public void setStudentGroups (List studentGroups) {
		this.studentGroups=studentGroups;
	}
	
	
	public Integer getKeyGroupProperties () {
		return keyGroupProperties;
	}
	
	
	public void setKeyGroupProperties (Integer keyGroupProperties) {
		this.keyGroupProperties = keyGroupProperties;
	}
	
	
	public IGroupProperties getGroupProperties () {
		return groupProperties;
	}
	
	
	public void setGroupProperties (IGroupProperties groupProperties) {
		this.groupProperties = groupProperties;
	}
	
	
	
	public String toString() {
		String result = "[ATTENDSSET";
		result += ", name=" + getName();
		result += "]";
		return result;
	}
	
	
	public void addAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		if( attendInAttendsSet==null){
			this.attendInAttendsSet=new ArrayList();
			this.attendInAttendsSet.add(attendInAttendsSet);
		}
		else{
			this.attendInAttendsSet.add(attendInAttendsSet);	
		}
	}
	
	
	public void removeAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		this.attendInAttendsSet.remove(attendInAttendsSet);
	}
	
		
	public boolean existsAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		return this.attendInAttendsSet.contains(attendInAttendsSet);
	}

	public void addStudentGroup (IStudentGroup studentGroup) {
		studentGroups.add(studentGroup);
	}
	
	
	public void removeStudentGroup (IStudentGroup studentGroup) {
		studentGroups.remove(studentGroup);
	}
	
		
	public boolean existsStudentGroup (IStudentGroup studentGroup) {
		return studentGroups.contains(studentGroup);
	}
	
	public IStudentGroup getStudentGroup(Integer groupNumber){
		Iterator iter=studentGroups.iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getGroupNumber()==groupNumber)
				return sg;
				} 
		return null;
		
	}
	
	
	public List getAttends() {
		List attends = new ArrayList();
		Iterator iterAttendInAttendsSet = 
			getAttendInAttendsSet().iterator();
		IAttendInAttendsSet attendInAttendsSet = null;
		while(iterAttendInAttendsSet.hasNext()){
			attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
			attends.add(attendInAttendsSet.getAttend());
		}
		return attends;
	}
	
	
	
	
	public IFrequenta getStudentAttend(IStudent student){
		IFrequenta attend=null;
		boolean found = false;
		Iterator iterAttends = getAttendInAttendsSet().iterator();
		while(iterAttends.hasNext() && !found)
		{
			attend = (IFrequenta)((IAttendInAttendsSet)iterAttends.next()).getAttend();
			if(attend.getAluno().getIdInternal().equals(student.getIdInternal())){
				return attend;
			}
			attend = null;
		}
		return attend;
	}
	
	public List getStudentGroupsWithoutShift(){
		List result = new ArrayList();
		Iterator iter=studentGroups.iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getShift()==null){
				result.add(sg);
			}
		} 
		return result;
	}
	
	public List getStudentGroupsWithShift(){
		List result = new ArrayList();
		Iterator iter=studentGroups.iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getShift()!=null){
				result.add(sg);
			}
		} 
		return result;
	}
}