package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Publication;

public class AuthorshipTest extends DomainTestBase {

    Person person1;
    Person person2;
    Person person3;
    Person person4;
    Person person5;
    
    List<Person> authors;
    
    Publication publication;
    
    Authorship authorship1;
    Authorship authorship2;
    Authorship authorship3;
    Authorship authorship4;
    
    
    Integer order;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        person1 = new Person();
        person1.setNome("Autor1");
        
        person2 = new Person();
        person2.setNome("Autor2");
        
        person3 = new Person();
        person3.setNome("Autor3");
        
        person4 = new Person();
        person4.setNome("Autor4");
        
        person5 = new Person();
        person5.setNome("Autor5");
        
        authors = new ArrayList<Person>();
        authors.add(person1);
        
        publication = new Publication();
        
        authorship1 = new Authorship();
        authorship1.setAuthor(person1);
        authorship1.setResult(publication);
        authorship1.setOrder(1);
        
        authorship2 = new Authorship();
        authorship2.setAuthor(person2);
        authorship2.setResult(publication);
        authorship2.setOrder(2);
        
        authorship3 = new Authorship();
        authorship3.setAuthor(person3);
        authorship3.setResult(publication);
        authorship3.setOrder(3);
        
        authorship4 = new Authorship();
        authorship4.setAuthor(person4);
        authorship4.setResult(publication);
        authorship4.setOrder(4);
        
    }

    public void testCreateAuthorship() {
    	try {
	        Authorship authorship = new Authorship(publication, person5, 5);
	        
	        assertEquals("Publication Expected", (Publication)authorship.getResult(), publication);
	        assertEquals("Person Expected", authorship.getAuthor(), person5);
	        assertEquals("Authorship's Order Unexpected", new Integer(5), authorship.getOrder());
	        
	        assertEquals("Authorships size unexpected", 5, publication.getResultAuthorshipsCount());
	        assertEquals("Teachers size unexpected", 0, publication.getPublicationTeachersCount());
    	} catch (DomainException domainException) {
    		fail("The authorship should have been sucessfully created");
    	}
    }
    
    public void testCreateAuthorshipWithExistingOrder() {
    	try {
    		new Authorship(publication, person2, 1);
    		fail("The authorship shouldn't have been sucessfully deleted");
    	} catch (DomainException domainException) {
    		//Caso em que se tenta criar uma autoria para uma determinada publica��o/pessoa com uma ordem j� existente
    	}
    	
    }

    public void testDeleteAuthorship() {
        assertEquals("Unexpected Authorships Size", 4, publication.getResultAuthorshipsCount());
        assertTrue("Publication doen't contain the authorship being deleted", publication.getResultAuthorships().contains(authorship2));
        
        authorship2.delete();
        
        assertEquals("Authorships Size Unexpected", 3, publication.getResultAuthorshipsCount());
        assertEquals("PublicationTeachers Size Unexpected", 0, publication.getPublicationTeachersCount());
        assertEquals("Unexpected Authorships Size", 1, person1.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 0, person2.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 1, person3.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 1, person4.getPersonAuthorshipsCount());
        
        //Verify if the remaining authorships order was updated
        //Note this rule enforcement is being done at the relation - PublicationAuthorship.remove
        assertEquals("Unexpected Order for Authorship1", new Integer(1), authorship1.getOrder());
        assertEquals("Unexpected Order for Authorship3", new Integer(2), authorship3.getOrder());
        assertEquals("Unexpected Order for Authorship4", new Integer(3), authorship4.getOrder());
        
        assertFalse("Publication still contains the authorship", publication.getResultAuthorships().contains(authorship2));
    }
    
}
