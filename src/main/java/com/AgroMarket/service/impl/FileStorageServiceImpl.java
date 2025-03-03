package com.AgroMarket.service.impl;

import com.AgroMarket.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  private final Path rootLocation = Paths.get("/app/images");

  public FileStorageServiceImpl() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new RuntimeException("Не удалось создать директорию для загрузки файлов", e);
    }
  }

  @Override
  public String saveFile(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("Файл пуст");
    }

    String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Files.copy(file.getInputStream(), rootLocation.resolve(filename));

    return "/images/" + filename;
  }

  @Override
  public void deleteFile(String fileName) throws IOException {
    String filename = fileName.substring(fileName.lastIndexOf("/") + 1);
    Path file = rootLocation.resolve(filename);
    Files.deleteIfExists(file);
  }
}