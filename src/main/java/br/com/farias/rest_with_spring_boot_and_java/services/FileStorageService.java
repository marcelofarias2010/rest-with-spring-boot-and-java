package br.com.farias.rest_with_spring_boot_and_java.services;

import br.com.farias.rest_with_spring_boot_and_java.config.FileStorageConfig;
import br.com.farias.rest_with_spring_boot_and_java.controllers.FileController;
import br.com.farias.rest_with_spring_boot_and_java.exception.FileNotFoundException;
import br.com.farias.rest_with_spring_boot_and_java.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().toAbsolutePath().normalize();

        this.fileStorageLocation = path;
        try {
            logger.info("Creating Directories");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename Contains a Invalid path Sequence");
                throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence" + fileName);
            }
            logger.info("Saving file in Disk");
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file " + fileName + ". Please try Again");
            throw new FileStorageException("Could not store file " + fileName + ". Please try Again", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                logger.error("File not found " + fileName);
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (Exception e) {
            logger.error("File not found " + fileName, e);
            throw new FileNotFoundException("File not found " + fileName, e);
        }
    }
}
