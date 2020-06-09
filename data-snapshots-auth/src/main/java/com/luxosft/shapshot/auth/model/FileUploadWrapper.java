package com.luxosft.shapshot.auth.model;

import com.luxosft.shapshot.auth.annotations.FileIntegrity;
import com.luxosft.shapshot.auth.annotations.FileType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadWrapper {

  @FileType(fileType = "text/plain")
  @FileIntegrity(content = {
      "PRIMARY_KEY", "NAME", "DESCRIPTION", "UPDATED_TIMESTAMP"}
  )
  private MultipartFile file;
}