package com.example.ddalkkak.service;

import com.example.ddalkkak.converter.dto.Test;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.block.composition.TextObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TestService {
    @Value(value = "${slack.token}")
    private String token;
    @Value(value = "${slack.channel.monitor}")
    private String channel;

    public void postTest(Test dto) throws IOException {
        log.info("dto: {}", dto);
        // Slack 메세지 보내기
        try {
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText("*이름:*\n" + dto.name()));
            textObjects.add(markdownText("*문의 제목:*\n" + dto.title()));
            textObjects.add(markdownText("*문의내용:*\n" + dto.content()));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(dto.name()+"님이 문의를 남겨주셨습니다!")
                    .blocks(asBlocks(
                            header(header -> header.text(plainText(dto.name() + "님이 문의를 남겨주셨습니다!"))),
                            divider(),
                            section(section -> section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error("SlackApiException: {}", e.getMessage());
        }
    }

    public List<String> fetchAllMessagesFromTestChannel() {
        List<String> messages = new ArrayList<>();
        try {
            MethodsClient methods = Slack.getInstance().methods(token);
            ConversationsHistoryRequest request = ConversationsHistoryRequest.builder()
                    .token(token)
                    .channel("C02S66N7JP3") // #test 채널 ID
                    .build();

            ConversationsHistoryResponse response = methods.conversationsHistory(request);
            if (response.isOk()) {
                response.getMessages().forEach(message -> messages.add(message.getText()));
            } else {
                log.error("Failed to fetch messages: {}", response.getError());
            }
        } catch (IOException | SlackApiException e) {
            log.error("Error occurred while fetching messages from Slack: {}", e.getMessage());
        }
        return messages;
    }
}

