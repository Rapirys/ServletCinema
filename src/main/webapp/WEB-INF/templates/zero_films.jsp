<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="../../static/css/header.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <style>
        body {
            background-image: url("../../static/img/zero.jpg");
        }

        .center {
            position: absolute;
            width: 100%;
            top: 10%;
            text-align: center;
            font-size: 400%;
            color: #110513;
        }
    </style>
</head>

<body>
<header class="header">
    <sec:authorize authority="Anonymous">
        <a href="/cinema/login"><img class="hbtn" src="../../static/img/login.png"></a>
    </sec:authorize>
    <sec:authorize authority="Authenticated">
        <form action="/cinema/logout" method="post"><input type="image" img class="hbtn" src="../../static/img/logout.png">
        </form>
    </sec:authorize>
    <div class="lang_buttons">
        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
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
    <p>${lang.gL("No_session")}</p>
</div>
</body>
</html>