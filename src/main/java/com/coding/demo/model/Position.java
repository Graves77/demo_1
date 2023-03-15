package com.coding.demo.model;

import lombok.Data;

@Data
public class Position {

  private long id;
  private String name;
  private double latitude;
  private double longitude;
  private String title;
  private String remarks;
  private String phone;
  private boolean flag;



}
