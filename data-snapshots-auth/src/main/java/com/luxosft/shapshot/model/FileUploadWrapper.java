package com.luxosft.shapshot.model;

import com.luxosft.shapshot.annotations.FileIntegrity;
import com.luxosft.shapshot.annotations.FileType;
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