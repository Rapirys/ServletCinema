<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cinema</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="../../static/css/main.css">
</head>
<body>
<header class="header">

    <sec:authorize authority="Anonymous">
        <a href="/cinema/login"><img class="hbtn" src="../../static/img/login.png"></a>
    </sec:authorize>
    <sec:authorize authority="Authenticated">
        <form action="/cinema/logout" method="post">
            <input type="image" img class="hbtn" src="../../static/img/logout.png">
        </form>
    </sec:authorize>
    <input type="image" onclick="show_menu()" class="hbtn" src="../../static/img/menu.png" alt="menu">
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
    </div>
    <sec:authorize authority="ADMIN">
        <h2><a href="/cinema/admin/session" class="tbtn">
            <p style="position: relative; top: -4px;"> ${lang.gL("ADMIN")} </p></a>
        </h2>
    </sec:authorize>
    <script>
        function swap_len() {
            $.get('/cinema/command/lang', function (data) {
                location.reload()
            })
        }

        function show_menu() {
            var element = document.getElementById("menu");
            if (element.style.visibility === 'visible') {
                element.style.visibility = 'hidden';
            } else {
                element.style.visibility = 'visible';
            }
        }
    </script>
</header>
<div class="d2" id="menu" style="visibility: hidden">
    <form method="get" action="/cinema">
        <input name="search" type="text" placeholder="...">
        <button type="submit"></button>
        <div class="choose">
            <div class="availability">
                <input type="checkbox" name="availability">
                <label>${lang.gL("Availability")}</label>
            </div>
            <div class="sort_session">
                <p>${lang.gL("By_sessions")}</p>
                <input type="radio" name="session" value="occupancy">
                <label>"${lang.gL("Occupancy")}"</label>
                <input type="radio" name="session" value="time">
                <label>${lang.gL("Time")}</label>
            </div>
            <div class="sort_film">
                <p>${lang.gL("By_films")}</p>
                <input type="radio" name="films" value="title_${lang.gL("locale")}">
                <label>${lang.gL("Title")}</label>
                <input type="radio" name="films" value="duration">
                <label>${lang.gL("Duration")}</label>
            </div>
            <c:set var="now" value="<%=new java.util.Date()%>"/>
            <div class="date">
                <input id="date1" name=date1 type="date"
                       value="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />"
                       min="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />">
                <input name=date2 id="date2" type="date"
                       value="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />"
                       min="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />">
                <p>${lang.gL("Maximum_span")}</p>
            </div>
        </div>
    </form>
</div>

<div class="slider" id="slider">
    <div class="slItems">
        <c:forEach var="film" items="${films}" varStatus="i">
             <%--<div class="slItem" style="background-image: url(../../static/posters/${film.film_id}.jpeg)">--%>
            <div class="slItem" style="background-image: url(/upload/${film.film_id}.jpeg)">
                <div class="title">
                    <c:set var="title" scope="session" value="${lang.gL('locale')}"/>
                    <c:choose>
                        <c:when test="${title.equals('ru')}">
                            <h1>${film.titleRu}</h1>
                        </c:when>

                        <c:when test="${title.equals('en')}">
                            <h1>${film.titleEn}</h1>
                        </c:when>
                    </c:choose>
                </div>

                <div class="myTable">
                    <c:forEach var="row" items="${sessions.get(film)}" varStatus="j">
                        <div class="row">
                            <label class="time">${row.get(0).date}:</label>
                            <c:forEach var="element" items="${row}" varStatus="k">
                                <a class="time" href="/cinema/place?id=${element.session_id}">
                                        ${element.time}
                                </a>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>

    </div>
</div>
<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script src="../../static/js/slider.min.js"></script>
<script>
    $(function () {
        $('#slider').rbtSlider({
            height: '100vh',
            'dots': true,
            'arrows': true,
            'auto': 3
        });
    });
</script>
<script type="text/javascript">
</script>
</body>
</html>
