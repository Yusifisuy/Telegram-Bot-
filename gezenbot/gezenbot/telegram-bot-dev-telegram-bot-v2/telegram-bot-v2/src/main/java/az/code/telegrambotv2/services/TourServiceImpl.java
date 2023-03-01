package az.code.telegrambotv2.services;

import az.code.telegrambotv2.bot.TourBot;
import az.code.telegrambotv2.configs.rabbit.CustomMessage;
import az.code.telegrambotv2.models.entities.*;
import az.code.telegrambotv2.models.redis.Session;
import az.code.telegrambotv2.repositories.OptionRepository;
import az.code.telegrambotv2.repositories.SessionHistoryRepository;
import az.code.telegrambotv2.repositories.UserRepository;
import az.code.telegrambotv2.repositories.redis.SessionRepository;
import az.code.telegrambotv2.services.interfaces.*;
import az.code.telegrambotv2.utils.Messages;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Component
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TourServiceImpl implements TourService {

    TourBot tourBot;
    LanguageService languageService;

    ProcessService processService;

    QuestionServiceImpl questionService;
    QuestionLocaleService questionLocaleService;

    OptionRepository optionRepository;

    UserRepository userRepository;

    SessionHistoryRepository sessionHistoryRepository;

    SessionRepository sessionRepository;


    public TourServiceImpl(@Lazy TourBot tourBot, LanguageService languageService, ProcessService processService,
                           OptionRepository optionRepository, QuestionLocaleService questionLocaleService,
                           QuestionServiceImpl questionService,UserRepository userRepository
    ,SessionHistoryRepository sessionHistoryRepository, SessionRepository sessionRepository) {

        this.tourBot = tourBot;
        this.languageService = languageService;
        this.processService = processService;
        this.optionRepository = optionRepository;
        this.questionLocaleService = questionLocaleService;
        this.questionService = questionService;
        this.userRepository=userRepository;
        this.sessionHistoryRepository = sessionHistoryRepository;
        this.sessionRepository = sessionRepository;

    }


    @Override
    public BotApiMethod<?> handleUpdate(Update update) throws TelegramApiException {
        log.error("----------------------------------------------------------------------");

        //all answers are given. must wait


        log.error("1");
        if (!validateUpdate(update)) {
            log.error(" 1 ???");
            return new SendMessage(update.getMessage().getChatId().toString(), Messages.incorrectCommand());
        }

        log.error("2");

        Message message = update.getMessage();
        long id = processService.getCurrentQuestionId(message.getChatId());
        String chatId = String.valueOf(message.getChatId());

        // handle commands
        if (message.getText().startsWith("/")) {
            return handleCommands(message);
        }

        // handle requests after the session has ended
        if (id == -1) {
            return new SendMessage(chatId, Messages.existRequestMessage());
        }

        // handle message before session activation
        if (!processService.hasActiveSession(message.getChatId())) {
            return new SendMessage(chatId, Messages.sessionMessage());
        }

        try {
            if (!isOption(update))
                return new SendMessage(chatId, Messages.incorrectCommandMessage());
        } catch (IllegalArgumentException e) {
            return new SendMessage(chatId, Messages.incorrectDateMessage());
        }

        return handleMessage(message);
    }

    private BotApiMethod<?> handleCommands(Message message) {
        String chatId = message.getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (message.getText()) {
            case "/start":
                if (processService.hasActiveSession(Long.parseLong(chatId))) {
                    return new SendMessage(chatId, Messages.activeSessionMessage());
                }
                registerUser(message);

                processService.createSession(message);
                return giveLanguageChoice(chatId);
            case "/stop":
                if (!processService.hasActiveSession(Long.parseLong(chatId))) {
                    return new SendMessage(chatId, Messages.sessionMessage());
                }
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                sendMessage.setText(Messages.stopMessage());
                // stop session
                processService.stopActivePoll(Long.parseLong(chatId));
                break;
            default:
                sendMessage.setText(Messages.incorrectCommand());
                break;
        }

        return sendMessage;
    }

    private BotApiMethod<?> handleMessage(Message message){

        long chatId = message.getChatId();
        String answer = message.getText();

        long currentQuestionId = processService.getCurrentQuestionId(chatId);
        Question question = questionService.findById(currentQuestionId);

        // set language
        if (currentQuestionId == 0) {

            processService.setSelectedLanguage(chatId, answer);
            // the language has been selected, go to the first question
            question = questionService.findById(1);
            processService.setCurrentQuestionId(chatId, 1);

            return giveQuestion(chatId, question);
        }

        if (question.getType().equals("DATE")) {
            //TODO date
            addUserAnswer(chatId, question, answer);
        }

        // last question
        if (question.getNextQuestionId() == -1) {
            // take the text of the question and save in Redis
            addUserAnswer(chatId, question, answer);
            // save session data to DB
            addUserAnswerToLog(chatId);
            // the questions are over
            processService.setCurrentQuestionId(chatId, -1);

            // TODO close session?
//                processService.stopActivePoll(Long.parseLong(chatId));
            // finish message

            //messageListener.handleNotification();

            return new SendMessage(String.valueOf(chatId), Messages.finishMessage());
        }

        // questions from 1 to n-1
        if (currentQuestionId > 0) {
            // take the text of the question and save in Redis
            addUserAnswer(chatId, question, answer);

            long id = question.getNextQuestionId();
            question = questionService.findById(id);
            // update current question in Redis
            processService.setCurrentQuestionId(chatId, id);

            return giveQuestion(chatId, question);
        }
        return null;
    }

    @SneakyThrows
    private BotApiMethod<?> giveLanguageChoice(String chatId){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(Messages.greetingMessage());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        languageService.getAll().forEach(language -> {
            KeyboardButton button = new KeyboardButton();
            button.setText(language.getName());
            row.add(button);
        });

        keyboardRowList.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    @SneakyThrows
    private BotApiMethod<?> giveQuestion(long chatId, Question question){

        QuestionLocale currentQuestionLocale = getLocaleOfQuestion(chatId, question);
        // set text of question
        SendMessage sendMessage = new SendMessage(Long.toString(chatId), currentQuestionLocale.getText());
        // set options of question
        sendMessage.setReplyMarkup(stageButton(chatId, question));
        // remove buttons for freetext
        if (question.getType().equals("FREETEXT")) {
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        }
        return sendMessage;
    }

    public ReplyKeyboardMarkup stageButton(long chatId, Question question) {

        List<Option> optionList = getLocaleOfQuestion(chatId, question).getOptions();

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (Option option : optionList) {
            KeyboardButton button = new KeyboardButton();
            button.setText(option.getAnswer());
            row.add(button);
        }

        keyboardRowList.add(row);

        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        return keyboardMarkup;
    }

    @SneakyThrows
    public Boolean validateUpdate(Update update){
        Long clientId = null;

        // TODO handle emoji
        if (update.getMessage().getText() == null) {
            return false;
        }

        if (update.hasCallbackQuery()) {
            clientId = update.getCallbackQuery().getFrom().getId();
        }  else if (update.hasMessage()) {
            if (!update.getMessage().hasText()) return false;
            if (update.getMessage().getText().startsWith("/")) return true;
            clientId = update.getMessage().getFrom().getId();
        }else {
            return false;
        }
        if (!processService.hasActiveSession(clientId)) {
            tourBot.execute(new SendMessage(clientId.toString(), Messages.sessionMessage()));
            return false;
        }
        return true;
    }
    @SneakyThrows
    public Boolean isOption(Update update) {

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        long currentQuestionId = processService.getCurrentQuestionId(chatId);

        Question currentQuestion = questionService.findById(currentQuestionId);

        // language check
        if (currentQuestionId == 0) {

            List<Language> languages = languageService.getAll();

            for (Language l : languages) {
                if (l.getName().equals(messageText)) {
                    return true;
                }
            }
            return false;
        } else {

            QuestionLocale currentQuestionLocale = getLocaleOfQuestion(chatId, currentQuestion);
            // FreeText
            if (currentQuestion.getType().equals("FREETEXT")) return true;
            // Date
            if (currentQuestion.getType().equals("DATE")) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                DateTime dt = formatter.parseDateTime(messageText);

                //TODO from  2024-02-10 to 2023-02-10
                if (dt.isAfter(DateTime.now().minusDays(1)) && dt.isBefore(DateTime.now().plusYears(1))) {
                    return true;
                } else {
                    throw new IllegalFieldValueException(DateTimeFieldType.year(),0,null,null);
                }
            }

            // Button
            for (Option x : currentQuestionLocale.getOptions()) {
                if (x.getAnswer().equals(messageText)) {
                    return true;
                }
            }
        }
        return false;
    }

    public QuestionLocale getLocaleOfQuestion(long chatId, Question currentQuestion) {

        List<QuestionLocale> questionLocaleList = currentQuestion.getTextOfQuestion();
        String lang = processService.getSelectedLanguage(chatId);

        QuestionLocale currentQuestionLocale = null;
        for (QuestionLocale x : questionLocaleList) {
            if (x.getLanguage().getName().equals(lang)) {
                currentQuestionLocale = x;
            }
        }
        return currentQuestionLocale;
    }

    public void addUserAnswer(long chatId, Question question, String answer) {
        QuestionLocale questionLocale = getLocaleOfQuestion(chatId, question);
        processService.saveUserAnswers(chatId, questionLocale.getText(), answer);
    }



    private User registerUser(Message msg){

        Optional<User> user = userRepository.findById(msg.getChatId());
        if (user.isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User newUser = User.builder()
                    .id(chatId)
                    .name(chat.getFirstName())
                    .surname(chat.getLastName())
                    .username(chat.getUserName())
                    .registeredAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            return userRepository.save(newUser);
        }else{
            return userRepository.findById(msg.getChatId()).get();
        }
    }

    private void addUserAnswerToLog(long chatId) {

        Session session = processService.getSession(chatId);
        JSONObject jsonObject = new JSONObject(session.getAnswers());
        SessionsHistory sessionsHistory = new SessionsHistory();

        Timestamp timestamp = new Timestamp(new Date().getTime());

        sessionsHistory.setUser(userRepository.getReferenceById(chatId));
        sessionsHistory.setTimestamp(timestamp);
        sessionsHistory.setAnswers(jsonObject.toString());
        sessionsHistory.setId(UUID.fromString(session.getUuid()));
        sessionsHistory.setLanguageId(session.getUserLanguage());

        sessionHistoryRepository.save(sessionsHistory);
    }
}
