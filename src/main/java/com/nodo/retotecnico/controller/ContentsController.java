package com.nodo.retotecnico.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.Content;
import com.nodo.retotecnico.service.ContentsService;

@RestController
@RequestMapping("/nodos/contents")
public class ContentsController {

    @Autowired
    private ContentsService contentsService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public List<Content> getAllContents(Locale locale) {
        return contentsService.getAllContents().stream()
                .map(content -> localizeContent(content, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Content getContentsById(@PathVariable Integer id, Locale locale) {
        return localizeContent(contentsService.getContentsById(id), locale);
    }

    @GetMapping("/section/{section}")
    public List<Content> getContentsBySection(@PathVariable String section, Locale locale) {
        return contentsService.getContentsBySection(section).stream()
                .map(content -> localizeContent(content, locale))
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public Integer createContent(@RequestBody Content content) {
        try {
            return contentsService.createContent(content);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable Integer id, @RequestBody Content content) {
        return ResponseEntity.ok(contentsService.updateContent(id, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable Integer id) {
        contentsService.deleteContent(id);
        return ResponseEntity.ok("Content deleted successfully");
    }

    private Content localizeContent(Content content, Locale locale) {
        if (content == null) {
            return null;
        }

        String localizedTitle = messageSource.getMessage(content.getTitle(), null, content.getTitle(), locale);
        String localizedDescription = messageSource.getMessage(content.getDescription(), null, content.getDescription(), locale);
        String localizedOptions = messageSource.getMessage(content.getOptions(), null, content.getOptions(), locale);

        return new Content(
                content.getId(),
                content.getSection(),
                localizedTitle,
                localizedDescription,
                content.getImage(),
                content.getDeleted(),
                content.getFieldName(),
                content.getFieldType(),
                content.getRequired(),
                localizedOptions,
                content.getDisplayOrder());
    }
