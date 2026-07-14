package com.birthday.birthday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.thymeleaf.exceptions.TemplateInputException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUpload(MaxUploadSizeExceededException ex, HttpServletRequest request, Model model) {
        logger.warn("Upload size exceeded for request {}", request.getRequestURI(), ex);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 400);
        model.addAttribute("error", "Качените файлове са твърде големи");
        model.addAttribute("message", "Моля, качете по-малки файлове или намалете броя на снимките.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(MultipartException.class)
    public String handleMultipart(MultipartException ex, HttpServletRequest request, Model model) {
        logger.warn("Multipart error on {}", request.getRequestURI(), ex);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 400);
        model.addAttribute("error", "Грешка при качване на файлове");
        model.addAttribute("message", "Проверете формата и опитайте отново.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateError(TemplateInputException ex, HttpServletRequest request, Model model) {
        logger.error("Template parsing error for {}", request.getRequestURI(), ex);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Грешка при рендериране на страницата");
        model.addAttribute("message", "Възникна проблем при показване на тази страница. Ако проблемът продължава, свържете се с администратора.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, HttpServletRequest request, Model model) {
        logger.error("Database error on {}", request.getRequestURI(), ex);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Грешка при достъп до базата данни");
        model.addAttribute("message", "Опитваме се да възстановим връзката. Моля опитайте по-късно.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllOtherExceptions(Exception ex, HttpServletRequest request, Model model) {
        logger.error("Unhandled exception on {}", request.getRequestURI(), ex);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Възникна неочаквана грешка");
        model.addAttribute("message", "Опитайте отново. Ако проблемът продължава, свържете се с администратора.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }
}
