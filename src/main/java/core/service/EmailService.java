package core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Async
    public void sendRegistrationEmail(String email, String username) {
        try {
            log.info("=== АСИНХРОННАЯ ОТПРАВКА EMAIL ===");
            log.info("Отправка письма о регистрации на email: {}", email);
            log.info("Пользователь: {}", username);
            log.info("Тема: Добро пожаловать в Библиотеку!");
            log.info("Текст: Уважаемый {}, спасибо за регистрацию! Теперь вы можете брать книги.", username);
            Thread.sleep(1500); // имитация отправки
            log.info("Письмо успешно отправлено (асинхронно) на {}", email);
        } catch (Exception e) {
            log.error("Ошибка при отправке email регистрации для {}", email, e);
        }
    }

    @Async
    public void sendLoanNotification(String memberEmail, String memberName, String bookTitle) {
        try {
            log.info("=== АСИНХРОННАЯ ОТПРАВКА УВЕДОМЛЕНИЯ О ВЫДАЧЕ ===");
            log.info("Получатель: {} ({})", memberName, memberEmail);
            log.info("Книга: {}", bookTitle);
            log.info("Текст: Уважаемый {}, вам выдана книга '{}'. Пожалуйста, верните её вовремя.", memberName, bookTitle);

            Thread.sleep(1000); // имитация
            log.info("Уведомление о выдаче книги успешно отправлено (асинхронно)");
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления о выдаче для {}", memberEmail, e);
        }
    }

    @Async
    public void sendBookReturnNotification(String memberEmail, String memberName, String bookTitle) {
        try {
            log.info("=== АСИНХРОННАЯ ОТПРАВКА УВЕДОМЛЕНИЯ О ВОЗВРАТЕ ===");
            log.info("Получатель: {} ({})", memberName, memberEmail);
            log.info("Книга возвращена: {}", bookTitle);

            Thread.sleep(800);
            log.info("Уведомление о возврате книги успешно отправлено (асинхронно)");
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления о возврате", e);
        }
    }
}
