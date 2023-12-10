package com.example.ddalkkak.controller;

import com.example.ddalkkak.converter.dto.Test;
import com.example.ddalkkak.service.TestService;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.search.SearchAllResponse;
import com.slack.api.methods.response.search.SearchMessagesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TestController {
    private final TestService testService;

    @PostMapping("/test")
    public void test(
            @RequestBody Test dto
    ) throws IOException {
        log.info("dto: {}", dto);
        testService.postTest(dto);
    }

    @GetMapping("/search-all")
    public List<String> searchAll() {
        return testService.fetchAllMessagesFromTestChannel();
    }
}
