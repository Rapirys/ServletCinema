package com.servlet.cinema.application.model.service;



import com.servlet.cinema.application.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 This class is used to validate values.
 Methods can return object validity status, object corrections, or error messages for composite objects.
 */

public class Validator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    public static final Pattern PASSWORD_REGEX = Pattern.compile("^[A-Za-z0-9]{6,40}");//"^[a-zA-Z][a-zA-Z0-9-_\\.]{6,20}$");


    public static boolean email(String s){
        return s!=null && s.matches(VALID_EMAIL_ADDRESS_REGEX.pattern());
    }
    public static boolean password(String s){
        return s!=null && s.matches(PASSWORD_REGEX.pattern());
    }
    public static boolean username(String s){
        return s!=null && s.length()>2 && s.length()<=20;
    }
    public static boolean cvv(String cvv){
       return cvv!=null && cvv.matches("^[0-9]{3,4}");
    }
    public static boolean cardNumber(String cardNumber){
         return cardNumber!=null && cardNumber.matches("^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}");
    }
    public static boolean year(Integer year){
       return year!=null && year>=2022 && year<=3000;
    }
    public static boolean month(Integer month){
        return month!=null && month>=0 && month<=12;
    }

    /**
     * @param user
     * @return List of errors to display in the view. Errors should have a translation in the resource bundle.
     */
    public static List<String> validUserFields(User user){
        List<String> s=new ArrayList<>();
        if (!email(user.getEmail()))
            s.add("Incorrect_email");
        if (!username(user.getUsername()))
            s.add("Username_too_short_or_too_long");
        if (!password(user.getPassword()))
            s.add("Password_problem");
        return s;
    }

    public static LocalDate toValidDate(LocalDate date,int shift) {
        if (date==null || date.isBefore(LocalDate.now()))
            date=LocalDate.now().plusDays(shift);
        return date;
    }

    public static boolean validCard(String cardNumber, String cvv, Integer dateM, Integer dateY, String holder) {
        return cardNumber(cardNumber) && cvv(cvv) && year(dateY) && month(dateM) && (holder!=null);
    }
}
