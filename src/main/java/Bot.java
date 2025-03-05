import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    //Кнопка для начала регистрации
    private InlineKeyboardButton buttonStartRegistration = InlineKeyboardButton.builder()
            .text("Нажмите для регистрации!")
            .callbackData("регистрация")
            .build();
    //Клавиатура для кнопки для начала регистрации
    private InlineKeyboardMarkup keyboardForButtonStartRegistration = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(buttonStartRegistration))
            .build();

    //Переменные для города и ЯП
    private final String cityForDeveloper = "Сочи";
    private final String languageProgrammingForDeveloper = "Java";

    //Список для кандидатов в команду разработчиков
    private ArrayList<User> listSuitableUsers = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        forWorkWithText(update);
        forWorkWithButtons(update);
    }

    public void forWorkWithText(Update update) {
        if (update.hasMessage()) {
            String textMessage = update.getMessage().getText();
            System.out.println("Текст: " + textMessage);
            String idUser = update.getMessage().getFrom().getId().toString();

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(idUser)
                    .text("")
                    .build();


            if (textMessage.equals("/start")) {
                sendMessage.setText("Для регистрации используйте кнопку ниже:");
                sendMessage.setReplyMarkup(keyboardForButtonStartRegistration);
            }
            /*
            Меня зовут Андрей.
            Я живу в городе Сочи.
            Я программирую на языке Java.
             */
            else if (textMessage.contains("зовут") && textMessage.contains("в городе") &&
                    textMessage.contains("на языке")) {

                String templateForName = "зовут ";
                int leftIndexForName = textMessage.indexOf(templateForName) + templateForName.length(); //индекс буквы А
                int rightIndexForName = textMessage.indexOf(".", leftIndexForName);
                String name = textMessage.substring(leftIndexForName, rightIndexForName);

                String templateForCity = "в городе ";
                int leftIndexForCity = textMessage.indexOf(templateForCity) + templateForCity.length();
                int rightIndexForCity = textMessage.indexOf(".", leftIndexForCity);
                String city = textMessage.substring(leftIndexForCity, rightIndexForCity);

                String templateForLanguage = "на языке ";
                int leftIndexForLanguage = textMessage.indexOf(templateForLanguage) + templateForLanguage.length();
                int rightIndexForLanguage = textMessage.indexOf(".", leftIndexForLanguage);
                String language = textMessage.substring(leftIndexForLanguage, rightIndexForLanguage);

                User suitableUser = new User();
                suitableUser.setId(Long.parseLong(idUser));
                suitableUser.setName(name);
                suitableUser.setCity(city);
                suitableUser.setLanguageProgramming(language);

                System.out.println("Город кандидата: " + suitableUser.getCity() + "\nШаблонный город: " + cityForDeveloper);
                if (suitableUser.getCity().equals(cityForDeveloper) &&
                        suitableUser.getLanguageProgramming().equals(languageProgrammingForDeveloper)) {
                    listSuitableUsers.add(suitableUser);
                }
            } else if (textMessage.equals("/list_suitable_users")) {
                StringBuilder builderForSuitableUsers = new StringBuilder();
                for (int indexCurrentSuitableUser = 0; indexCurrentSuitableUser < listSuitableUsers.size(); indexCurrentSuitableUser++) {
                    builderForSuitableUsers.append(listSuitableUsers.get(indexCurrentSuitableUser)).append("\n");
                }

                System.out.println("Список кандидатов: " + String.valueOf(builderForSuitableUsers));
                sendMessage.setText(String.valueOf(builderForSuitableUsers));

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
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("")
                    .build();

            if (callbackData.equals("регистрация")) {
                editMessageText.setText("Сформируйте сообщение в формате\n" +
                        "\"Меня зовут \'Андрей\'.\n" +
                        "Я живу в городе \'Сочи\'.\n" +
                        "Я программирую на языке \'Java\'.\"");
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
        return "@ogar_online_meetings_tg_bot";
    }

    @Override
    public String getBotToken() {
        return "7593532456:AAG0hoGZOlhXbAzGFP1HBKkezXKPamQ7qxI";
    }
}
