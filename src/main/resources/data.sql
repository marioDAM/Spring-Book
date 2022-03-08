CREATE SEQUENCE bookSequence;
CREATE SEQUENCE clientSequence;
insert into Client (id, name,username, dni, address, email,password, codLibrary)
values (NEXTVAL('clientSequence'), 'Juan Pablo ','hg', '50086979V', 'C/Los pardos', 'jpablo@hotmail.com','555', 0);
insert into Client (id, name,username, dni, address, email,password, codLibrary)
values (NEXTVAL('clientSequence'), 'Andres ','ND', '2154582C', 'C/Los Espartales', 'andy@hotmail.com','156161', 1);
insert into Client (id, name,username, dni, address, email,password, codLibrary)
values (NEXTVAL('clientSequence'), 'David ','sg', '125487S', 'C/Los Lugareños', 'david@hotmail.com','rerre', 2);
insert into Client (id, name,username, dni, address, email,password, codLibrary)
values (NEXTVAL('clientSequence'), 'Cesar ','hl', '45487956G', 'C/Los Ministros', 'cesar@hotmail.com','12564', 3);
insert into Client (id, name,username, dni, address, email,password, codLibrary)
values (NEXTVAL('clientSequence'), 'Dima ','lk', '9654785H', 'C/Los Tenientes', 'Dima@hotmail.com','125', 4);
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'Señor de los anillos |', 'J.R.R.Tolkien', 976418, '1955-10-12', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'Harry Potter', 'JK.Rowling', 65658, '1980-02-20', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'El niño con el pijama de rayas', 'J.Boyne', 35245,
        '2006-05-19', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'Don Quijote', 'M. de Cervantes', 615611, '1605-08-2', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'Las aventuras de Alicia en el país de las maravillas', 'L.Carroll', 948811,
        '1865-09-25', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'Heidi', 'J.Spyri', 396984,
        '1880-09-25', 0,'Acción y aventuras','Descripcion');
insert into Book (id, name, author, ISBN, created_at, isReserved,category,description)
values (NEXTVAL('bookSequence'), 'El Hobbit', 'J.R.R.Tolkien', 19198,
        '1937-09-25', 0,'Acción y aventuras','Descripcion');
