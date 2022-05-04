<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ticket_check</title>
    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <style>
        body {
            background: #ddeefc;
            font-family: "Source Sans Pro", sans-serif;
            font-size: 16px;
        }

        .center {
            position: absolute;
            width: 100%;
            top: 10%;
            text-align: center;
            font-size: 400%;
        }
    </style>
    <c:if test="${exist}">
        <style>
            body {
                background-color: green
            }
        </style>
    </c:if>
    <c:if test="${!exist}">
        <style>
            body {
                background-color: red
            }
        </style>
    </c:if>

</head>
<body>
<header class="header">
    <form th:action="@{/logout}" method="post"><input type="image" img class="hbtn" src="../static/img/logout.png">
    </form>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../static/img/lang.png" alt="lang">
    </div>
    <script>
        function swap_len() {
            $.get('/cinema/command/lang', function (data) {
                location.reload()
            })
        }
    </script>
</header>
<div class="center">
    <c:if test="${exist}">
        <p>${lang.gL("Ticket_exist")}</p>
        <p>${session.getFilm().getTitleEn()}</p>
        <p>${session.getFilm().getTitleRu()}</p>
        <p>${lang.gL("Row")} ${ticket.row} ${lang.gL("Place")} ${ticket.place}</p>
    </c:if>
    <c:if test="${!exist}">
        <p>${lang.gL("Ticket_does_not_exist")}</p>
    </c:if>

</div>
</body>
</html>