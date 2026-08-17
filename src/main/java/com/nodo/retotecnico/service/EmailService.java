package com.nodo.retotecnico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailService {

  @Value("${resend.token}")
  private String emailToken;

  @Value("${resend.email.from}")
  private String emailFrom = "onboarding@resend.dev";

  @Value("${backend.url:http://localhost:8081/auth}")
  private String backendUrl;

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
        "html", htmlContent);

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
    String resetLink = backendUrl.endsWith("/") ? backendUrl + "reset-password?token=" + token
        : backendUrl + "/reset-password?token=" + token;

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
        """
        .formatted(resetLink);

        sendEmail(to, "Recuperación de contraseña", htmlContent);
    }

    // Correo de bienvenida a beta testing
    public void sendBetaTesterWelcomeEmail(String to, String username) {
        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>¡Ya sos beta tester!</title>
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
                <h1>¡Gracias por sumarte, %s!</h1>
                <p>Quedaste inscripto como <strong>beta tester</strong> de la comunidad de Los Sims 4 🧪.</p>
                <p>Vas a tener acceso anticipado a nuevas funciones antes que el resto. ¡Gracias por ayudarnos a mejorar!</p>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(username);

        sendEmail(to, "¡Ya sos beta tester!", htmlContent);
    }

    // Correo de aviso de cambio de contraseña
    public void sendPasswordChangedEmail(String to, String username) {
        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>Tu contraseña fue actualizada</title>
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
                <h1>Hola, %s</h1>
                <p>Te avisamos que la contraseña de tu cuenta fue actualizada correctamente.</p>
                <p>Si vos no hiciste este cambio, por favor contactanos de inmediato para proteger tu cuenta.</p>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(username);

        sendEmail(to, "Tu contraseña fue actualizada", htmlContent);
    }

    // Correo de confirmación de compra
    public void sendPurchaseConfirmationEmail(String to, String username, List<String> itemLines, double total,
            String paymentMethod, Date purchaseDate) {
        String itemsHtml = itemLines.stream()
                .map(line -> "<li>" + line + "</li>")
                .collect(Collectors.joining());
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(purchaseDate);

        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>Confirmación de compra</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 8px; padding: 20px; }
                h1 { color: #2c3e50; }
                p { font-size: 16px; color: #555; }
                ul { padding-left: 20px; color: #555; }
                .total { font-size: 18px; font-weight: bold; color: #2c3e50; }
                .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }
              </style>
            </head>
            <body>
              <div class="container">
                <h1>¡Gracias por tu compra, %s!</h1>
                <p>Confirmamos tu compra del %s, pagada con <strong>%s</strong>:</p>
                <ul>%s</ul>
                <p class="total">Total: $%,.0f</p>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(username, formattedDate, paymentMethod, itemsHtml, total);

        sendEmail(to, "Confirmación de compra", htmlContent);
    }

    // Correo de anuncio de nueva expansión para beta testers
    public void sendExpansionAnnouncementEmail(String to, String username, String packName, String description,
            String publicationDate, String platforms, List<String> minimumRequirements) {
        String requirementsHtml = minimumRequirements == null ? "" : minimumRequirements.stream()
                .map(line -> "<li>" + line + "</li>")
                .collect(Collectors.joining());

        String htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <title>Nueva expansión para beta testing</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 8px; padding: 20px; }
                h1 { color: #2c3e50; }
                p { font-size: 16px; color: #555; }
                ul { padding-left: 20px; color: #555; }
                .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }
              </style>
            </head>
            <body>
              <div class="container">
                <h1>¡Hola, %s!</h1>
                <p>Como beta tester, sos de los primeros en enterarte: llega <strong>%s</strong>, disponible a partir del <strong>%s</strong> para <strong>%s</strong>.</p>
                <p>%s</p>
                <p><strong>Requisitos mínimos:</strong></p>
                <ul>%s</ul>
                <div class="footer">
                  <p>© 2026 Los Sims 4. Todos los derechos reservados.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(username, packName, publicationDate, platforms, description, requirementsHtml);

        sendEmail(to, "Nueva expansión para beta testers: " + packName, htmlContent);
    }
}
