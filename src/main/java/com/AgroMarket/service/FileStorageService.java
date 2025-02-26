package com.AgroMarket.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
  String saveFile(MultipartFile file) throws IOException;

  void deleteFile(String fileName) throws IOException;
}