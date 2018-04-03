
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
        return "Pushksolutionsbot";
    }
    public String getBotToken() {
        return "586995962:AAEa9m2W4nCyH38MEtlBeJP3aMO0HURd5Xo";
    }

    //fil in our chat
    private String answers = "";
    private String[] questions = {
            "Как дела?",
            "Ну у меня тоже все ОК. Как тебя зовут?",
            "А меня Jack и я бот компании Push-K Solutions",
            "Давай заполним бриф",
            "Вообщем то спасибо тебе бро, вот наш сайт тебе на память: push-k.ua"
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
            emailSender.sendMail("Jackbot@push-k.ua", "j7887j", "kosykh@push-k.com.ua");
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
