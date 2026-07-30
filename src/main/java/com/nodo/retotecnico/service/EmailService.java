package com.nodo.retotecnico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.token}")
    private String emailToken;

    @Value("${resend.email.from}")
    private String emailFrom = "onboarding@resend.dev";

    // Método genérico para enviar correos con Resend
    public void sendEmail(String to, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(emailToken);

        Map<String, Object> payload = Map.of(
            "from", emailFrom,
            "to", to,
            "subject", subject,
            "html", htmlContent
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    // Correo de bienvenida
    public void sendWelcomeEmail(String to, String username) {
        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>Bienvenido a la comunidad de los Sims 4</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 8px; padding: 20px; }
                h1 { color: #2c3e50; }
                p { font-size: 16px; color: #555; }
                .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }
              </style>
            </head>
            <body>
              <div class="container">
                <h1>¡Bienvenido, %s!</h1>
                <p>Gracias por unirte a la comunidad de <strong>Los Sims 4</strong> 🎮.</p>
                <p>Tu cuenta ha sido creada exitosamente. ¡Prepárate para vivir nuevas historias!</p>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(username);

        sendEmail(to, "Bienvenido a la comunidad de los Sims 4", htmlContent);
    }

    // Correo de recuperación de contraseña
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "https://tuapp.com/reset-password?token=" + token;

        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>Recuperación de contraseña</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 8px; padding: 20px; }
                h1 { color: #2c3e50; }
                p { font-size: 16px; color: #555; }
                a.button { display: inline-block; padding: 10px 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px; }
                .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }
              </style>
            </head>
            <body>
              <div class="container">
                <h1>Recuperación de contraseña</h1>
                <p>Haz clic en el siguiente botón para restablecer tu contraseña:</p>
                <p><a href="%s" class="button">Restablecer contraseña</a></p>
                <p>Este enlace expirará en 1 hora.</p>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(resetLink);

        sendEmail(to, "Recuperación de contraseña", htmlContent);
    }
}
