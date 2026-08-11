package com.nodo.retotecnico.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver() {
            @Override
            public @NonNull Locale resolveLocale(@NonNull HttpServletRequest request) {
                String lang = request.getParameter("lang");
                if (lang != null && !lang.isBlank()) {
                    Locale requested = Locale.forLanguageTag(lang);
                    for (Locale supported : getSupportedLocales()) {
                        if (supported.getLanguage().equalsIgnoreCase(requested.getLanguage())) {
                            return supported;
                        }
                    }
                }
                return super.resolveLocale(request);
            }
        };
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(java.util.List.of(Locale.ENGLISH, new Locale("es"), new Locale("fr")));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        source.setFallbackToSystemLocale(false);
        return source;
    }

}
