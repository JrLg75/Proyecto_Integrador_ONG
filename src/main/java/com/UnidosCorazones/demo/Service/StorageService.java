package com.UnidosCorazones.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {
    // Definimos la carpeta raíz donde se guardarán los archivos
    private final Path rootLocation = Paths.get("uploads");

    public StorageService() {
        try {
            // Crea la carpeta "uploads" si no existe al iniciar la app
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta de almacenamiento", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Error: archivo vacío");
            }

            // Limpiar el nombre del archivo
            String originalFilename = file.getOriginalFilename();

            // Generar nombre único para evitar sobrescribir
            String filename = UUID.randomUUID().toString() + "_" + originalFilename;

            // Copiar el archivo a la carpeta destino
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Retornar la URL relativa para guardar en BD (esto es lo que se guardas)
            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Fallo al guardar archivo", e);
        }
    }
}
