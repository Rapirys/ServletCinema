package com.spring.cinema.model.service;


import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.application.model.service.Validator;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void nullEmail() {
        assertFalse (Validator.email(null));
    }
    @Test
    void validEmail() {
        assertTrue (Validator.email("gregor@gmail.com"));
        assertTrue (Validator.email("gregordsadasfasfasfa@gmail.com"));
    }
    @Test
    void invalidEmail() {
        assertFalse (Validator.email("gregor@gmail.commm"));
        assertFalse (Validator.email("gregor@gmail..com"));
        assertFalse (Validator.email("gregor@гмаил.com"));
    }
    @Test
    void  validPassword(){
        assertFalse (Validator.password("111Q2"));
        assertFalse (Validator.password("abcd"));
        assertTrue (Validator.password("1233456q"));
    }


    @Test
    void invalidUsername() {
        assertFalse (Validator.email("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName"));
        assertFalse (Validator.email("Д"));
    }

    @Test
    void cvv() {
        assertFalse(Validator.cvv("aaa"));
        assertFalse(Validator.cvv("11111"));
        assertTrue(Validator.cvv("123"));
        assertTrue(Validator.cvv("1234"));
    }

    @Test
    void cardNumber() {
        assertFalse(Validator.cardNumber("1111 1111 1111"));
        assertFalse(Validator.cardNumber("1234567812345678"));
        assertFalse(Validator.cardNumber("1234 5a78 1234 5678"));
        assertTrue(Validator.cardNumber("1234 5678 1234 5678"));
    }

    @Test
    void year() {
        assertFalse(Validator.year(1000));
        assertFalse(Validator.year(10000));
        assertTrue(Validator.year(2025));
        assertTrue(Validator.year(2030));
    }

    @Test
    void month() {
        assertFalse(Validator.month(-10));
        assertFalse(Validator.month(13));
        assertTrue(Validator.month(1));
        assertTrue(Validator.month(5));
    }

    @Test
    void validCard() {
        String cardN="1111 1111 1111 1111";
        String name="Asd Jkc";
        String cvv="1111";
        Integer y = 2024;
        Integer m =12;
        assertTrue(Validator.validCard(cardN, cvv , m, y, name));

        assertFalse(Validator.validCard(cardN, cvv , m, 1000, name));
        assertFalse(Validator.validCard(cardN, cvv , -10, y, name));
        assertFalse(Validator.validCard(cardN, "aaa" , m, y, name));
        assertFalse(Validator.validCard("1234567812345678", cvv , m, y, name));
    }

    @Test
    void toValidDate() {
        assertThat(Validator.toValidDate(LocalDate.now().minusDays(7),7)).isEqualTo(LocalDate.now().plusDays(7));
    }


    @Test
    void validUserFields() {
        User user= new User();
        user.setPassword("12345678q").setEmail("gregor@gmail.com").setUsername("Name");
        assertEquals(0, Validator.validUserFields(user).size());
        user.setPassword("1111");
        assertTrue(Validator.validUserFields(user).size()!=0);
        user.setPassword("12345678q");
        user.setUsername("NameTooLongggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        assertTrue(Validator.validUserFields(user).size()!=0);
    }
}