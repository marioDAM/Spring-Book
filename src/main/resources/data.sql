insert into Book (id, name, autor, ISBN, created_at)
values (NEXTVAL('hibernate_sequence'), 'Señor de los anillos', 'J.R.R.Tolkien', 2115, NOW());