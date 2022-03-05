CREATE SEQUENCE bookSequence;
CREATE SEQUENCE clientSequence;
insert into Client (id, name, dni, address, email, codLibrary)
values (NEXTVAL('clientSequence'), 'Juan Pablo ', '50086979V', 'C/Los pardos', 'jpablo@hotmail.com', 0);
insert into Client (id, name, dni, address, email, codLibrary)
values (NEXTVAL('clientSequence'), 'Andres ', '2154582C', 'C/Los Espartales', 'andy@hotmail.com', 0);
insert into Client (id, name, dni, address, email, codLibrary)
values (NEXTVAL('clientSequence'), 'David ', '125487S', 'C/Los Lugareños', 'david@hotmail.com', 0);
insert into Client (id, name, dni, address, email, codLibrary)
values (NEXTVAL('clientSequence'), 'Cesar ', '45487956G', 'C/Los Ministros', 'cesar@hotmail.com', 0);
insert into Client (id, name, dni, address, email, codLibrary)
values (NEXTVAL('clientSequence'), 'Dima ', '9654785H', 'C/Los Tenientes', 'Dima@hotmail.com', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'Señor de los anillos |', 'J.R.R.Tolkien', 976418, '1955-10-12', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'Harry Potter', 'JK.Rowling', 65658, '1980-02-20', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'El niño con el pijama de rayas', 'J.Boyne', 35245,
        '2006-05-19', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'Don Quijote', 'M. de Cervantes', 615611, '1605-08-2', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'Las aventuras de Alicia en el país de las maravillas', 'L.Carroll', 948811,
        '1865-09-25', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'Heidi', 'J.Spyri', 396984,
        '1880-09-25', 0);
insert into Book (id, name, author, ISBN, created_at, isReserved)
values (NEXTVAL('bookSequence'), 'El Hobbit', 'J.R.R.Tolkien', 19198,
        '1937-09-25', 0);
