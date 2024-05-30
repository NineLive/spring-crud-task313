package ru.spring.crudtask313.springcrudtask.service;

public interface EmailService {
    void send(String to, String from, String subject, String text);
}
