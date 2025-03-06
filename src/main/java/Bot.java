import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private String necessaryCity = "Сочи";
    private String necessaryLanguageProgramming = "Java";
    private ArrayList<User> listNecessaryUsers;

    public Bot() {
        listNecessaryUsers = new ArrayList<>();
    }


    //Кнопка для запуска теста
    private InlineKeyboardButton buttonForStartTgBot = InlineKeyboardButton.builder()
            .text("Нажмите для запуска!")
            .callbackData("запуск")
            .build();
    //Клавиатура для кнопки для запуска теста
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
                sendMessage.setText("Вас приветствует тг-бот для поиска и формирования ИТ-команды");
                sendMessage.setReplyMarkup(keyboardForButtonForStartTgBot);
            }
            /*
            Меня зовут Михаил.
            Я живу в городе Сочи.
            Моим основным ЯП является Java.
             */
            else if (textMessage.contains("зовут ") &&
                        textMessage.contains("в городе ") &&
                        textMessage.contains("ЯП является ")) {

                String templateForName = "зовут ";
                int leftIndexForName = textMessage.indexOf(templateForName) + templateForName.length();
                int rightIndexForName = textMessage.indexOf(".", leftIndexForName);
                String name = textMessage.substring(leftIndexForName,  rightIndexForName);

                String templateForCity = "в городе ";
                int leftIndexForCity = textMessage.indexOf(templateForCity) + templateForCity.length();
                int rightIndexForCity = textMessage.indexOf(".", leftIndexForCity);
                String city = textMessage.substring(leftIndexForCity, rightIndexForCity);

                String templateForLanguage = "ЯП является ";
                int leftIndexForLanguage = textMessage.indexOf(templateForLanguage) + templateForLanguage.length();
                int rightIndexForLanguage = textMessage.indexOf(".", leftIndexForLanguage);
                String languageProgramming = textMessage.substring(leftIndexForLanguage, rightIndexForLanguage);

                User currentUser = new User(idUser, name, city, languageProgramming);

                if (necessaryCity.compareToIgnoreCase(currentUser.getCity()) == 0 &&
                        necessaryLanguageProgramming.compareToIgnoreCase(currentUser.getLanguageProgramming()) == 0) {
                    listNecessaryUsers.add(currentUser);
                    sendMessage.setText("Пользователь \n\"" + currentUser + "\"\nдобавлен!");
                } else {
                    sendMessage.setText("Данный пользователь не смог пройти по параметрам :(");
                }
            } else if (textMessage.equals("/list_necessary_users")) {
                StringBuilder builderForNecessaryUsers = new StringBuilder();
                for (User currentNecessaryUser : listNecessaryUsers) {
                    builderForNecessaryUsers.append(currentNecessaryUser).append("\n");
                }
                sendMessage.setText(String.valueOf(builderForNecessaryUsers));
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
                editMessageText.setText("Напишите пожалуйта текст в следующем формате:" +
                        "\"\nМеня зовут \'Михаил\'.\n" +
                        "Я живу в городе \'Сочи\'.\n" +
                        "Моим основным ЯП является \'Java\'." +
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
