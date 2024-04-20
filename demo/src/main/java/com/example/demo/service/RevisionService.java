package com.example.demo.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Revision;
import com.example.demo.repositary.RevisionRepo;

@Service
public class RevisionService {
    @Autowired
    private RevisionRepo revisionrepo;

    public Revision storefile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Revision revision = new Revision(fileName, file.getContentType(), file.getBytes());
            return revisionrepo.save(revision);
        } catch (IOException ex) {
            throw new Exception("couldnt store file", ex);
        }
    }

    public Stream<Revision> getallmat() {
        return revisionrepo.findAll().stream();
    }

    public Optional<Revision> getFile(long id) {
        return revisionrepo.findById(id);
    }
}
