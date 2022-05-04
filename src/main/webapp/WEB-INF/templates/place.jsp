<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

    <meta charset="UTF-8">
    <title>Place</title>
    <style>
        body {
            background: #ddeefc;
            font-family: "Source Sans Pro", sans-serif;
            font-size: 16px;
        }

        input[type=checkbox] {
            display: none;
        }

        .title {
            text-align: center;
            font-size: 30px;
            font-weight: 600;
        }

        .place {
            display: inline-block;
        }

        .hall {
            position: absolute;
            top: 55%;
            left: 50%;
            margin-right: -50%;
            transform: translate(-50%, -50%)
        }

        .place_free {
            border: 2px solid rgb(7, 160, 236);
            height: 2.25rem;
            width: 1.5rem;
            border-radius: 4px;
        }

        .place_grey {
            background-color: #737171;
            border: 2px solid rgb(37, 59, 70);
            height: 2.25rem;
            width: 1.5rem;
            border-radius: 4px;
        }

        .place_white {
            height: 2.25rem;
            width: 1.5rem;
        }

        .poster {
            box-shadow: 0px 10px 15px 10px #000;
            position: absolute;
            width: 30%;
            top: 35%;
            left: 10px;
        }

        .batt {
            position: absolute;
            width: 300px;
            height: 300px;
            top: 30%;
            right: 40px;
            border-radius: 10px;
            background-color: rgb(7, 160, 236);
            text-align: center;
        }

        .batt:hover {
            background-color: rgb(17, 147, 211);
        }

        .batt label {
            position: center;
            font-size: 300px;
        }
    </style>
</head>
<body>
<header class="header">
    <form action="/cinema/logout" method="post"><input type="image" img class="hbtn" src="../../static/img/logout.png">
    </form>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
    </div>
    <script>
        function swap_len() {
            $.get('/command/lang', function (data) {
                location.reload()
            })
        }
    </script>
</header>
<img class="poster" src="../../static/posters/${mySession.film.film_id}.jpeg"/>
<div class="title">
    <c:set var="title" scope="session" value="${lang.gL('locale')}"/>
    <p>${lang.gL("Choice_of_seats")}</p>
    <c:choose>
        <c:when test="${title.equals('ru')}">
            <label>${mySession.getFilm().getTitleRu()}</label>
        </c:when>

        <c:when test="${title.equals('en')}">
            <label>${mySession.getFilm().getTitleEn()}</label>
        </c:when>
    </c:choose>
    <label>${mySession.getFilm().getFormatDuration()}</label>
    <p> ${mySession.date} ${mySession.time} ${mySession.price}₴</p>
</div>
<p id="session" hidden>${mySession.session_id}</p>
<div class="hall">
    <c:forEach var="row" items="${topology}" varStatus="i">
        <div class="row">
            <c:forEach var="place" items="${row}" varStatus="j">
                <div class="place">
                    <c:set var="type" scope="session" value="${place.type.toString()}"/>
                    <c:choose>
                        <c:when test="${type.equals('#')}">
                            <input type="checkbox" id="${place.row}_${place.place}"/>
                            <div class="place_free" onclick="choose(this)"
                                 id="${place.row}_${place.place}"></div>
                        </c:when>
                        <c:when test="${type.equals('0')}">
                            <div class="place_white"></div>
                        </c:when>
                        <c:when test="${type.equals('X')}">
                            <div class="place_grey"></div>
                        </c:when>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
<div onclick="submit()" class="batt">
    <label id="submit">0</label>
</div>
<script>
    var k = 0;

    function choose(el) {
        var check = document.getElementById(el.id);
        if (check.checked) {
            check.checked = false;
            el.style.backgroundColor = 'white';
            k--;

        } else {
            check.checked = true;
            el.style.backgroundColor = "rgba(56,207,255,0.99)";
            k++;
        }
        drow();
    }

    function drow() {
        document.getElementById("submit").innerText = k.toString();
    }

    function submit() {
        if (k > 0) {
            let l = [];
            $('input[type="checkbox"]:checked').each(function () {
                l.push(this.id)
                this.checked = false;
            });

            $.ajax({
                url: '/cinema/order',
                type: 'POST',
                cache: false,
                success: function (data) {
                    window.location.href = '/cinema/order?id=' + data;
                },
                error: function (data) {
                    window.location.reload();
                },
                data: 'data=' + l + '&session_id=' + document.getElementById('session').innerText
            })
        }
    }
</script>
</body>
</html>