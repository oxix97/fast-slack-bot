package com.example.ddalkkak.converter.dto;

public record Test(
        String name,
        String title,
        String content
) {
    public static Test of(String name, String title, String content) {
        return new Test(name, title, content);
    }
}
