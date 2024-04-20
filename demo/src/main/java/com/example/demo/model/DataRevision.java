package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRevision {
    private String filename;
    private String downloadurl;
    private String filetype;
    private long filesize;
}
