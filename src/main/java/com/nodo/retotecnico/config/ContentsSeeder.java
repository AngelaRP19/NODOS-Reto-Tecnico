package com.nodo.retotecnico.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nodo.retotecnico.model.Content;
import com.nodo.retotecnico.repository.ContentsRepository;

/**
 * Siembra los campos de formularios como filas de "contents", agrupadas por
 * "section". Cada fila = un campo del formulario (title = label del campo,
 * fieldName/fieldType/required/options/displayOrder describen cómo pedirlo
 * y de qué tipo de dato es), para que el front arme el formulario leyendo
 * de la base de datos en vez de tenerlo hardcodeado.
 *
 * "title" y "options" no son texto literal: son claves de mensaje
 * (content.register.firstName.title, etc.) que ContentsController resuelve
 * vía MessageSource según el locale de cada request. Por eso, a diferencia
 * de otros seeders, este hace upsert por (section, fieldName) en vez de
 * saltarse todo si la sección ya tiene filas: si quedaron filas viejas de
 * antes de este esquema de claves (con texto literal como title), nunca se
 * hubieran corregido con un guard de "ya existe algo" y la traducción se
 * habría quedado rota para siempre.
 */
@Component
@Order(2)
public class ContentsSeeder implements CommandLineRunner {

    private static final String SECTION_REGISTER = "register_form";
    private static final String SECTION_LOGIN = "login_form";

    @Autowired
    private ContentsRepository contentsRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<Content> toSave = new ArrayList<>();

        toSave.addAll(upsertSection(SECTION_REGISTER, List.of(
                field(SECTION_REGISTER, "content.register.firstName.title", "firstName", "TEXT", true, "", 1),
                field(SECTION_REGISTER, "content.register.lastName.title", "lastName", "TEXT", true, "", 2),
                field(SECTION_REGISTER, "content.register.username.title", "username", "TEXT", true, "", 3),
                field(SECTION_REGISTER, "content.register.email.title", "email", "EMAIL", true, "", 4),
                field(SECTION_REGISTER, "content.register.country.title", "country", "SELECT", true,
                        "content.register.country.options", 5),
                field(SECTION_REGISTER, "content.register.password.title", "password", "PASSWORD", true, "", 6),
                field(SECTION_REGISTER, "content.register.confirmPassword.title", "confirmPassword", "PASSWORD",
                        true, "", 7))));

        toSave.addAll(upsertSection(SECTION_LOGIN, List.of(
                field(SECTION_LOGIN, "content.login.username.title", "username", "TEXT", true, "", 1),
                field(SECTION_LOGIN, "content.login.password.title", "password", "PASSWORD", true, "", 2))));

        if (!toSave.isEmpty()) {
            contentsRepository.saveAll(toSave);
        }
    }

    private List<Content> upsertSection(String section, List<Content> desiredFields) {
        Map<String, Content> existingByFieldName = contentsRepository
                .findBySectionAndDeletedFalseOrderByDisplayOrderAsc(section).stream()
                .collect(Collectors.toMap(Content::getFieldName, c -> c, (a, b) -> a));

        List<Content> toSave = new ArrayList<>();
        for (Content desired : desiredFields) {
            Content existing = existingByFieldName.get(desired.getFieldName());
            if (existing == null) {
                toSave.add(desired);
                continue;
            }
            if (!existing.getTitle().equals(desired.getTitle())
                    || !existing.getOptions().equals(desired.getOptions())
                    || !existing.getFieldType().equals(desired.getFieldType())
                    || !existing.getDisplayOrder().equals(desired.getDisplayOrder())) {
                existing.setTitle(desired.getTitle());
                existing.setFieldType(desired.getFieldType());
                existing.setRequired(desired.getRequired());
                existing.setOptions(desired.getOptions());
                existing.setDisplayOrder(desired.getDisplayOrder());
                toSave.add(existing);
            }
        }
        return toSave;
    }

    private Content field(String section, String title, String fieldName, String fieldType,
            boolean required, String options, int displayOrder) {
        Content content = new Content();
        content.setSection(section);
        content.setTitle(title);
        content.setFieldName(fieldName);
        content.setFieldType(fieldType);
        content.setRequired(required);
        content.setOptions(options);
        content.setDisplayOrder(displayOrder);
        return content;
    }
}
