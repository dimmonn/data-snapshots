package com.luxosft.shapshot.model;

import static javax.persistence.TemporalType.*;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Entry {

  @Id
  private long id;
  @EqualsAndHashCode.Exclude
  private String name;
  @EqualsAndHashCode.Exclude
  private String description;
  @EqualsAndHashCode.Exclude
  @Temporal(TIMESTAMP)
  private Date timestamp;

}