call mysql fenix < fixDbForDeploy1-31-01-2004.sql > temp1.sql
call mysql fenix < fixDbForDeploy2-31-01-2004.sql
call mysql fenix < temp1.sql
call mysql fenix < fixDbForDeploy3-31-01-2004.sql > temp2.sql
call mysql fenix < temp2.sql
call mysql fenix < fixDbForDeploy4-31-01-2004.sql
call mysql fenix < fixDbForDeploy5-31-01-2004.sql > temp3.sql
call mysql fenix < temp3.sql





