CREATE TABLE OrganizationType (
                                  organizationTypeID SERIAL PRIMARY KEY,
                                  type VARCHAR(100) UNIQUE
);
INSERT INTO OrganizationType (type) VALUES ('PUBLIC');
INSERT INTO OrganizationType (type) VALUES ('GOVERNMENT');
INSERT INTO OrganizationType (type) VALUES ('PRIVATE_LIMITED_COMPANY');
INSERT INTO OrganizationType (type) VALUES ('OPEN_JOINT_STOCK_COMPANY');

CREATE TABLE ACCOUNT (
                         accountID serial PRIMARY KEY,
                         UserName VARCHAR(50) UNIQUE NOT NULL,
                         DisplayName VARCHAR(50) NULL,
                         PassWord VARCHAR(1000) NOT NULL,
                         Type int NULL
);


CREATE TABLE Coordinates (
                             CoordinatesId SERIAL PRIMARY KEY ,
                             x int NULL,
                             y float NULL
);

CREATE TABLE OfficeAddress (
                               AddressId SERIAL PRIMARY KEY ,
                               street VARCHAR(100) NOT NULL,
                               zipCode VARCHAR(100) NOT NULL
);

CREATE TABLE Organization(
                             id SERIAL PRIMARY KEY ,
                             name VARCHAR(200) NOT NULL,
                             CoordinatesId int NOT NULL,
                             FOREIGN KEY (CoordinatesId) REFERENCES Coordinates(CoordinatesId),
                             creationDate date NOT NULL,
                             AnnualTurnover int NOT NULL,
                             organizationTypeID int NULL,
                             FOREIGN KEY (organizationTypeID) REFERENCES OrganizationType(organizationTypeID),
                             AddressId int NULL,
                             FOREIGN KEY (AddressId) REFERENCES OfficeAddress(AddressId),
                             accountID int NOT NULL,
                             FOREIGN KEY (accountID) REFERENCES ACCOUNT(accountID)
);



