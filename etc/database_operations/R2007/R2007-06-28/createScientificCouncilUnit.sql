UPDATE PARTY 
SET OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit'
WHERE NAME = 'Conselho Científico';

update PARTY set KEY_PARTY_TYPE = (select ID_INTERNAL from PARTY_TYPE where TYPE = 'SCIENTIFIC_COUNCIL') where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit';

INSERT INTO SITE (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, KEY_UNIT)
SELECT 'net.sourceforge.fenixedu.domain.ScientificCouncilSite', 1, U.ID_INTERNAL 
FROM PARTY U WHERE U.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit';

UPDATE PARTY U, SITE S
SET U.KEY_SITE = S.ID_INTERNAL
WHERE U.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit'
  AND S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';

INSERT INTO UNIT_SITE_MANAGERS (KEY_UNIT_SITE, KEY_PERSON)
SELECT S.ID_INTERNAL, PR.KEY_PERSON
FROM ROLE R, PERSON_ROLE PR, SITE S
WHERE R.ID_INTERNAL = PR.KEY_ROLE 
  AND R.ROLE_TYPE = 'SCIENTIFIC_COUNCIL'
  AND S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';

INSERT INTO PERSON_ROLE (KEY_PERSON, KEY_ROLE)
SELECT SUB.PERSON, SUB.ROLE
FROM (
	SELECT PR.KEY_PERSON AS PERSON, NR.ID_INTERNAL AS ROLE
	FROM ROLE R, PERSON_ROLE PR, ROLE NR
	WHERE R.ID_INTERNAL = PR.KEY_ROLE
	  AND R.ROLE_TYPE = 'SCIENTIFIC_COUNCIL'
      AND NR.ROLE_TYPE = 'WEBSITE_MANAGER'
) AS SUB
WHERE NOT EXISTS (
	SELECT * FROM PERSON_ROLE SPR
	WHERE SPR.KEY_PERSON = SUB.PERSON
	  AND SPR.KEY_ROLE = SUB.ROLE
);

INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, SECTION_ORDER, KEY_SITE) 
SELECT 'net.sourceforge.fenixedu.domain.Section', 1, 'en3:Toppt4:Topo', 1, 0, S.ID_INTERNAL
FROM SITE S WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';

INSERT INTO ACCESSIBLE_ITEM (OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, NAME, VISIBLE, SECTION_ORDER, KEY_SITE) 
SELECT 'net.sourceforge.fenixedu.domain.Section', 1, 'en4:Sidept7:Lateral', 1, 1, S.ID_INTERNAL
FROM SITE S WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';

INSERT INTO ANNOUNCEMENT_BOARD (CREATION_DATE, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, UNIT_PERMITTED_WRITE_GROUP_TYPE, UNIT_PERMITTED_READ_GROUP_TYPE, UNIT_PERMITTED_MANAGEMENT_GROUP_TYPE, NAME, KEY_PARTY, WRITERS, MANAGERS)
SELECT CURRENT_TIMESTAMP(),
	   'net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard',
	   1,
	   'UB_UNITSITE_MANAGERS',
	   'UB_PUBLIC',
	   'UB_MANAGER',
	   'Anúncios',
	   U.ID_INTERNAL,
	   concat("role('MANAGER') || unitSiteManagers($I(", S.ID_INTERNAL, ", 'ScientificCouncilSite'))"),
	   "role('MANAGER')"
FROM PARTY U, SITE S
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite'
  AND S.KEY_UNIT = U.ID_INTERNAL;

INSERT INTO ANNOUNCEMENT_BOARD (CREATION_DATE, OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT, UNIT_PERMITTED_WRITE_GROUP_TYPE, UNIT_PERMITTED_READ_GROUP_TYPE, UNIT_PERMITTED_MANAGEMENT_GROUP_TYPE, NAME, KEY_PARTY, WRITERS, MANAGERS)
SELECT CURRENT_TIMESTAMP(),
	   'net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard',
	   1,
	   'UB_UNITSITE_MANAGERS',
	   'UB_PUBLIC',
	   'UB_MANAGER',
	   'Eventos',
	   U.ID_INTERNAL,
	   concat("role('MANAGER') || unitSiteManagers($I(", S.ID_INTERNAL, ", 'ScientificCouncilSite'))"),
	   "role('MANAGER')"
FROM PARTY U, SITE S
WHERE S.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite'
  AND S.KEY_UNIT = U.ID_INTERNAL;
