package com.luxosft.shapshot.validator;

import com.luxosft.shapshot.annotations.FileType;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileTypeMultipartFileValidator implements
    ConstraintValidator<FileType, MultipartFile> {


  private String acceptedFileType;

  @Override
  public void initialize(FileType constraintAnnotation) {
    this.acceptedFileType = constraintAnnotation.fileType();
  }

  @Override
  public boolean isValid(MultipartFile multipartFile,
      ConstraintValidatorContext constraintValidatorContext) {
    return acceptedFileType.equals(multipartFile.getContentType());
  }
}
