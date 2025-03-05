import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Bot extends TelegramLongPollingBot {
    //Кнопка для запуска тг-бота
    private InlineKeyboardButton buttonForStartTgBot = InlineKeyboardButton.builder()
            .text("Нажмите для запуска тг-бота!")
            .callbackData("запуск")
            .build();

    //Клавиатура для кнопки для запуска тг-бота
    private InlineKeyboardMarkup keyboardForButtonForStartTgBot = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(buttonForStartTgBot))
            .build();


    @Override
    public void onUpdateReceived(Update update) {
        forWorkWithText(update);
        forWorkWithButtons(update);
    }

    public void forWorkWithText(Update update) {
        if (update.hasMessage()) {
            String textMessage = update.getMessage().getText();
            Long idUser = update.getMessage().getFrom().getId();

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(idUser)
                    .text("")
                    .build();

            if (textMessage.equals("/start")) {
                sendMessage.setText("Вас приветствует тг-бот Огарь М. В.,\n" +
                        "кот-й поможет найти кандидатов на роль разработчиков\n" +
                        "в нужном городе и по нужной специальности");
                sendMessage.setReplyMarkup(keyboardForButtonForStartTgBot);
            } else if (textMessage.contains("зовут") && textMessage.contains("в городе") &&
                            textMessage.contains("на языке")) {
                /*
                    Меня зовут Михаил.
                    Я живу в городе Сочи.
                    Программирую на языке Java.
                */
                String templateForName = "зовут ";
                int leftIndexForName = textMessage.indexOf(templateForName);
                if (leftIndexForName == -1) {
                    sendMessage.setText("Ввод имени некорректен :(\n" +
                            "Повторите снова!");
                    return;
                }
                int rightIndexForName = textMessage.indexOf(".", leftIndexForName + templateForName.length());
                String name = textMessage.substring(leftIndexForName + templateForName.length(), rightIndexForName);

                System.out.println("Спарсенное имя: " + name);
            }

            try {
                execute(sendMessage);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }

    public void forWorkWithButtons(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("")
                    .build();

            if (callbackData.equals("запуск")) {
                editMessageText.setText("Напишите текст в следующем формате:\n" +
                        "\"Меня зовут \'Михаил\'.\n" +
                        "Я живу в городе \'Сочи\'.\n" +
                        "Программирую на языке \'Java\'." +
                        "\"");
            }

            try {
                execute(editMessageText);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "@ogar_search_developers_tg_bot";
    }

    @Override
    public String getBotToken() {
        return "7885891719:AAF6_2FwCpbsPWuTqNL_MmgG8rcnGrD6lAU";
    }
}
