use iths;


drop table if exists UNF;

CREATE TABLE `UNF` (
    `Id` DECIMAL(38, 0) NOT NULL,
    `Name` VARCHAR(26) NOT NULL,
    `Grade` VARCHAR(11) NOT NULL,
    `Hobbies` VARCHAR(25),
    `City` VARCHAR(10) NOT NULL,
    `School` VARCHAR(30) NOT NULL,
    `HomePhone` VARCHAR(15),
    `JobPhone` VARCHAR(15),
    `MobilePhone1` VARCHAR(15),
    `MobilePhone2` VARCHAR(15)
)  ENGINE=INNODB;


LOAD DATA INFILE '/var/lib/mysql-files/denormalized-data.csv'
INTO TABLE UNF
CHARACTER SET latin1
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;



drop table if exists Student;


create table Student (
	StudentId int not null,
	FirstName varchar(255) not null,
	LastName varchar(255) not null,
	constraint primary key(StudentId)
) engine = innodb;

insert into Student(StudentId, FirstName, LastName) select distinct Id, substring_index(Name, ' ', 1), substring_index(Name, ' ', -1) from UNF;


drop table if exists School;

create table School as select distinct 0 as SchoolId, School as Name, City from UNF;

set @id = 0;

update School set SchoolId = (select @id := @id + 1);

alter table School add primary key(SchoolId);


drop table if exists StudentSchool;


create table StudentSchool select distinct UNF.Id as StudentId, SchoolId from UNF join School on UNF.School = School.Name;

alter table StudentSchool modify column StudentId int;
alter table StudentSchool add primary key(StudentId, SchoolId);



drop table if exists Phone;

create table Phone select distinct 0 as PhoneId, Id as StudentId, "Home" as Type, HomePhone as Number from UNF where HomePhone is not null and HomePhone !=''
union select distinct 0 as PhoneId, Id as StudentId, "Job" as Type, JobPhone as Number from UNF where JobPhone is not null and JobPhone !=''
union select distinct 0 as PhoneId, Id as StudentId, "Mobile" as Type, MobilePhone1 as Number from UNF where MobilePhone1 is not null and MobilePhone1 !=''
union select distinct 0 as PhoneId, Id as StudentId, "Mobile" as Type, MobilePhone2 as Number from UNF where MobilePhone2 is not null and MobilePhone2 !='';

set @id = 0;

update Phone set PhoneId = (select @Id := @id + 1);









