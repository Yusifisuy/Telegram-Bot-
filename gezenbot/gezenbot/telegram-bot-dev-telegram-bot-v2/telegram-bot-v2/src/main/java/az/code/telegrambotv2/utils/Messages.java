package az.code.telegrambotv2.utils;

import org.joda.time.LocalDate;

public class Messages {

    public static String greetingMessage() {
        return "Zəhmət olmasa dil seçin." + "\n" +
                "Please, select a language." + "\n" +
                "Пожалуйста, выберите язык.";
    }

    public static String stopMessage(){
        return "Your session was removed, you can restart with typing /start";
    }

    public static String sessionMessage(){
        return "You don't have active session, please type /start to start";
    }

    public static String activeSessionMessage() {
        return "You have active session, please first type /stop to restart";
    }

    public static String incorrectCommand(){
        return "Incorrect command";
    }

    public static String incorrectDateMessage(){
        return "Incorrect Date!" +
                "\nYou must select date in range of " + LocalDate.now() + " to " + LocalDate.now().plusYears(1) +
                "\nINPUT FORMAT YYYY-MM-DD";
    }
    public static String existRequestMessage() {
        return "Please give me some time and I will send you the best offers 😉\n" +
                "If you would like to create another request, you can end this request by typing '/stop'";
    }

    public static String incorrectCommandMessage(){
        return "Incorrect command";
    }

    public static String finishMessage() {
        return "Great! 👍" +
                "\nI received your request. My assistants are currently unavailable. " +
                "I will prepare and send you offers as soon as possible. 😊";
    }
}
