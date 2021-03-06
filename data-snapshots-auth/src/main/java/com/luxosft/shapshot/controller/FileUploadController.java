package com.luxosft.shapshot.controller;

import com.luxosft.shapshot.annotations.SnapshotApiVersion;
import com.luxosft.shapshot.exceptions.BrokenFileException;
import com.luxosft.shapshot.model.Entry;
import com.luxosft.shapshot.model.FileUploadWrapper;
import com.luxosft.shapshot.repository.EntryRepository;
import com.luxosft.shapshot.validator.EntryValidation;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@SnapshotApiVersion(version = 1)
@RequestMapping("/files")
public class FileUploadController {

  private final SimpleDateFormat simpleDateFormat;
  private final EntryValidation entryValidation;
  private final EntryRepository entryRepository;
  private final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

  public FileUploadController(
      EntryRepository entryRepository, EntryValidation entryValidation,
      SimpleDateFormat simpleDateFormat) {
    this.entryRepository = entryRepository;
    this.entryValidation = entryValidation;
    this.simpleDateFormat = simpleDateFormat;
  }


  @SneakyThrows
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/upload-file")
  public List<Entry> uploadFile(@Validated final FileUploadWrapper file,
      final BindingResult result) {
    if (result.hasErrors()) {
      throw new BrokenFileException();
    }
    String fileContent;
    try (InputStream ioFile = file.getFile().getInputStream()) {
      fileContent = IOUtils.toString(ioFile, StandardCharsets.UTF_8);
    }
    String[] snapshot = fileContent.split(System.lineSeparator());
    for (int i = 1; i < snapshot.length; i++) {
      String[] entry = snapshot[i].split(",");
      if (!entryValidation.isValid(entry)) {
        LOGGER.warn("skipping entry as non-complient " + Arrays.toString(entry));
      } else {
        Entry validEntry = new Entry(Long.parseLong(
            entry[0]), entry[1], entry[2],
            simpleDateFormat.parse(entry[3]));
        entryRepository.save(validEntry);
      }
    }
    return entryRepository.findAll();
  }

  @GetMapping("/entries")
  public List<Entry> getEntities() {
    return entryRepository.findAll();
  }

  @DeleteMapping(value = "/entries/{id}")
  public List<Entry> getEntities(@PathVariable Long id) {
    entryRepository.deleteById(id);
    return entryRepository.findAll();
  }
}

