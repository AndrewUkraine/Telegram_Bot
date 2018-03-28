
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;



public class SimpleBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new SimpleBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public String getBotUsername() {
        return "andr17_bot";
    }
    public String getBotToken() {
        return "487573927:AAESIxTXtzuI18Q6O0LpiRVOahu4MBBRO7Y";
    }

    //fil in our chat
    private String answers = "";
    private String[] questions = {
            "Ты кто такой?",
            "И че?",
            "Как тебя кличут то?",
            "А мобила есть?"
    };


    private int asked = -1;

    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();

        long chat_id = update.getMessage().getChatId();

        if (message.getText().equals("/start"))
            sendMsg(message, "Привет. Рады пообщаться с Вами. Мы вам предложим пройти заполнение брифа. Поехали?");

        else if (message != null && message.hasText() && (++asked < questions.length)) {
            answers += message.getText() + "\n";
            answers += questions[asked] + "\n";
            sendMsg(message, questions[asked]);
            System.out.println(answers);
        } else {
            answers += message.getText() + "\n";
            System.out.println(answers);
            sendMsg(message, "Спасибо, что заполнили бриф! Сотрудник агентства с вами свяжется");

//send fotos
            SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto("https://lh5.googleusercontent.com/-1Nz6QXCQ370/VS_Z9kdz5dI/AAAAAAAAdpo/bBkI9EN4NJg/s0/2015-04-16_184923.jpg")
                    .setCaption("ПаМаПарам");
            try {
                sendPhoto(msg); // Call method to send the photo
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            EmailSender emailSender = new EmailSender();
            emailSender.setMas(answers);
            emailSender.sendMail("shatov@push-k.com.ua", "?????????", "andrii0938630809@gmail.com");
            asked = -1;
        }

        return;

    }


    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
