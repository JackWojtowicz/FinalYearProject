package com.example.demo.controller;

import java.sql.Blob;
import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.demo.model.DataRevision;
import com.example.demo.model.Revision;
import com.example.demo.repositary.RevisionRepo;
import com.example.demo.service.RevisionService;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RevisionController {
    @Autowired
    RevisionService revisionService;
    @Autowired
    RevisionRepo revisionrepo;

    @GetMapping("/revision")
    public String revisionpage(Model model) {
        model.addAttribute("newmat", new Revision());
        if (revisionrepo.findAll().size() == 0) {
            return "revision";
        } else {
            model.addAttribute("allmats", revisionrepo.findAll());
        }
        return "revision";
    }

    @PostMapping(value = "/addMaterial")
    public String addMaterial(Model model, @RequestParam("file") MultipartFile file) throws Exception {
        String downloadurl = "";
        if (file.isEmpty()) {
            return "redirect:/revision";
        }
        Revision revision = revisionService.storefile(file);
        downloadurl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download")
                .path((revision.getStringId()))
                .toUriString();
        // return new DataRevision(revision.getName(), downloadurl,
        // file.getContentType(), file.getSize());
        return "redirect:/revision";
    }

    @GetMapping("/download/{revisionId}")
    public ResponseEntity<Resource> downloadRevision(@PathVariable int revisionId) {
        Revision revision = revisionService.getFile(revisionId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(revision.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "revision;filename=\"" + revision.getName() + "\"")
                .body(new ByteArrayResource(revision.getMaterial()));

    }

    @GetMapping("/delete/{fileid}")
    public String deleteMat(Model model, @PathVariable long fileid) {
        revisionrepo.deleteById(fileid);
        return "redirect:/revision";
    }

}
