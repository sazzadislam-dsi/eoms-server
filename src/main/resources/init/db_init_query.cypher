MATCH (n)
DETACH
DELETE n

CREATE (:Organization {name: 'Baitul Mokaddem', establishmentYear: 1980}),
       (:OrganizationInfo {founderName: 'Unknown Name', founderDescription: 'Unknown Desc'}),
       (:AppUser {
         username:    'admin',
         password:    '$2a$10$3mUSOw6gya8AeNnzL7qiaO2p9qeko.rWVpRpRdZQ4SoICglyGQVHa',
         authorities: 'ROLE_USER, ROLE_ADMIN'
       });


MATCH (a:Organization), (b:OrganizationInfo)
  WHERE a.name = 'Baitul Mokaddem' AND b.founderName = 'Unknown Name'
CREATE (a)-[r:organizationHasOrganizationInfo]->(b)
RETURN r;


MATCH (a:Organization), (b:AppUser)
  WHERE a.name = 'Baitul Mokaddem' AND b.username = 'admin'
CREATE (a)<-[r:appUserBelongsToAnOrganization]-(b)
RETURN r;

//CREATE CONSTRAINT ON (class:Class) ASSERT class.name IS UNIQUE
//DROP CONSTRAINT ON (class:Class) ASSERT class.name IS UNIQUE

//delete a orphan node
//Start n=node(1) Delete n;