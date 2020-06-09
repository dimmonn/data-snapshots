package com.luxosft.shapshot.auth.validator;

import com.luxosft.shapshot.auth.annotations.FileIntegrity;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class ContentIntegrityMultipartFileValidator implements
    ConstraintValidator<FileIntegrity, MultipartFile> {

  private String[] acceptedContentContent;

  @Override
  public void initialize(FileIntegrity constraintAnnotation) {
    this.acceptedContentContent = constraintAnnotation.content();
  }

  @Override
  @SneakyThrows
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    String snapshot = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
    String[] splitSnapshot = snapshot.split(System.lineSeparator());
    return Arrays.equals(splitSnapshot[0].split(","), acceptedContentContent);
  }

}