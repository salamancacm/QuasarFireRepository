package com.rebel.quasarfireoperation.services;

import com.rebel.quasarfireoperation.exception.MessageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {

    private List<String> getMessagePhrases(List<List<String>> msgList) {
        List<String> wordList = new ArrayList<>();
        for (List<String> msg : msgList) {
            wordList = Stream.concat(wordList.stream(), msg.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        wordList.remove("");
        return wordList;
    }

    private void removeEmptyWord(List<List<String>> msgList, int gapSize) {
        int s;
        for (int i = 0; i < msgList.size(); i++) {
            s = msgList.get(i).size();
            msgList.set(i, msgList.get(i).subList(s - gapSize, s));
        }
    }

    private String getCompleteMessage(List<List<String>> messageList) {

        String[] phraseArray = new String[messageList.get(0).size()];

        for (List<String> subList : messageList) {
            for (int i = 0; i < subList.size(); i++) {
                if (!subList.get(i).equals("")) {
                    if (i != subList.size() - 1) {
                        phraseArray[i] = subList.get(i) + " ";
                    } else {
                        phraseArray[i] = subList.get(i);
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        Arrays.stream(phraseArray).forEach(sb::append);

        return sb.toString();
    }

    public String getMessage(List<List<String>> msgList) throws MessageException {
        List<String> messagePhraseList = getMessagePhrases(msgList);
        if (!validateMessageSize(msgList, messagePhraseList.size())) {
            throw new MessageException("Message size is incorrect");
        }

        removeEmptyWord(msgList, messagePhraseList.size());
        String message = getCompleteMessage(msgList);

        if (!validateMessagePhraseList(messagePhraseList, message)) {
            throw new MessageException("Can't determine message content");
        }

        return message;
    }

    private boolean validateMessageSize(List<List<String>> messages, int size) {
        for (List<String> m : messages) {
            if (m.size() < size) {
                return false;
            }
        }
        return true;
    }

    private boolean validateMessagePhraseList(List<String> phrases, String message) {
        List<String> msg = Arrays.stream(message.split(" ")).collect(Collectors.toList());
        Collections.sort(phrases);
        Collections.sort(msg);
        return Arrays.equals(phrases.toArray(), msg.toArray());
    }

}
