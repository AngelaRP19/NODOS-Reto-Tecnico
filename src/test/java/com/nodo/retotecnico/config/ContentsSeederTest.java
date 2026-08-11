package com.nodo.retotecnico.config;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nodo.retotecnico.model.Content;
import com.nodo.retotecnico.repository.ContentsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContentsSeederTest {

    @Mock
    private ContentsRepository contentsRepository;

    @InjectMocks
    private ContentsSeeder contentsSeeder;

    @Captor
    private ArgumentCaptor<List<Content>> contentCaptor;

    @Test
    public void whenNothingIsSeeded_thenSeedsAllNineFields() {
        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("register_form"))
                .thenReturn(Collections.emptyList());
        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("login_form"))
                .thenReturn(Collections.emptyList());

        contentsSeeder.run();

        verify(contentsRepository, times(1)).saveAll(contentCaptor.capture());
        List<Content> saved = contentCaptor.getValue();

        // 7 register fields + 2 login fields = 9 fields total
        assertEquals(9, saved.size());

        long registerCount = saved.stream().filter(c -> "register_form".equals(c.getSection())).count();
        long loginCount = saved.stream().filter(c -> "login_form".equals(c.getSection())).count();

        assertEquals(7, registerCount);
        assertEquals(2, loginCount);
    }

    @Test
    public void whenExistingRowsAlreadyMatchTheCanonicalKeys_thenNoSavingOccurs() {
        // Filas ya correctas (title = clave de mensaje, no texto literal).
        Content firstName = correctField("register_form", "content.register.firstName.title", "firstName", "TEXT",
                "", 1);
        Content lastName = correctField("register_form", "content.register.lastName.title", "lastName", "TEXT", "",
                2);
        Content username = correctField("register_form", "content.register.username.title", "username", "TEXT", "",
                3);
        Content email = correctField("register_form", "content.register.email.title", "email", "EMAIL", "", 4);
        Content country = correctField("register_form", "content.register.country.title", "country", "SELECT",
                "content.register.country.options", 5);
        Content password = correctField("register_form", "content.register.password.title", "password", "PASSWORD",
                "", 6);
        Content confirmPassword = correctField("register_form", "content.register.confirmPassword.title",
                "confirmPassword", "PASSWORD", "", 7);

        Content loginUsername = correctField("login_form", "content.login.username.title", "username", "TEXT", "",
                1);
        Content loginPassword = correctField("login_form", "content.login.password.title", "password", "PASSWORD",
                "", 2);

        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("register_form"))
                .thenReturn(List.of(firstName, lastName, username, email, country, password, confirmPassword));
        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("login_form"))
                .thenReturn(List.of(loginUsername, loginPassword));

        contentsSeeder.run();

        verify(contentsRepository, never()).saveAll(any());
    }

    @Test
    public void whenExistingRowsHaveStaleLiteralTitles_thenTheyAreCorrectedToMessageKeys() {
        // Reproduce el bug real: filas sembradas antes de que "title" pasara a ser
        // una clave de mensaje. ContentsController nunca podía traducirlas porque
        // "Nombre" no matchea ninguna clave en messages*.properties.
        Content staleFirstName = staleField("register_form", "Nombre", "firstName", "TEXT", "", 1);
        Content staleLastName = staleField("register_form", "Apellido", "lastName", "TEXT", "", 2);
        Content username = correctField("register_form", "content.register.username.title", "username", "TEXT", "",
                3);
        Content email = correctField("register_form", "content.register.email.title", "email", "EMAIL", "", 4);
        Content country = correctField("register_form", "content.register.country.title", "country", "SELECT",
                "content.register.country.options", 5);
        Content password = correctField("register_form", "content.register.password.title", "password", "PASSWORD",
                "", 6);
        Content confirmPassword = correctField("register_form", "content.register.confirmPassword.title",
                "confirmPassword", "PASSWORD", "", 7);

        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("register_form"))
                .thenReturn(List.of(staleFirstName, staleLastName, username, email, country, password,
                        confirmPassword));
        when(contentsRepository.findBySectionAndDeletedFalseOrderByDisplayOrderAsc("login_form"))
                .thenReturn(Collections.emptyList());

        contentsSeeder.run();

        verify(contentsRepository, times(1)).saveAll(contentCaptor.capture());
        List<Content> saved = contentCaptor.getValue();

        // Las 2 filas viejas de register_form corregidas + las 2 de login_form nuevas.
        assertEquals(4, saved.size());

        Content correctedFirstName = saved.stream()
                .filter(c -> "firstName".equals(c.getFieldName()) && "register_form".equals(c.getSection()))
                .findFirst().orElseThrow();
        assertEquals("content.register.firstName.title", correctedFirstName.getTitle());

        assertTrue(saved.stream().anyMatch(c -> "login_form".equals(c.getSection())));
    }

    private Content correctField(String section, String title, String fieldName, String fieldType, String options,
            int displayOrder) {
        Content content = new Content();
        content.setSection(section);
        content.setTitle(title);
        content.setFieldName(fieldName);
        content.setFieldType(fieldType);
        content.setRequired(true);
        content.setOptions(options);
        content.setDisplayOrder(displayOrder);
        return content;
    }

    private Content staleField(String section, String literalTitle, String fieldName, String fieldType,
            String options, int displayOrder) {
        return correctField(section, literalTitle, fieldName, fieldType, options, displayOrder);
    }
}
