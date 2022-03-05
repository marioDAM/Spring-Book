package com.example.library.book.services.uploads;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    // Inicia sl sistema de ficheros
    void init();

    // Almacena un fichero llegado como un contenido multiparte
    String store(MultipartFile file);

    // Devuleve un Stream con todos los ficheros
    Stream<Path> loadAll();

    // Devuleve el Path o ruta de un fichero
    Path load(String filename);

    // Devuelve el fichero como recurso
    Resource loadAsResource(String filename);

    // Borra un fichero
    void delete(String filename);

    // Borra todos los ficheros
    void deleteAll();

    // Obtiene la URL del fichero
    String getUrl(String filename);

}