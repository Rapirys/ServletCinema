<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order unavailable</title>
    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <style>
        .center {
            position: absolute;
            width: 100%;
            top: 10%;
            text-align: center;
            font-size: 400%;
        }

        body {
            background: #ddeefc;
            font-family: "Source Sans Pro", sans-serif;
            font-size: 16px;
        }

        .btn {
            background-color: DodgerBlue;
            position: absolute;
            top: 200%;
            left: 50%;
            margin-right: -50%;
            transform: translate(-50%, -50%);
            border: none;
            color: white;
            padding: 12px 30px;
            font-size: 16px;
            cursor: pointer;
            font-size: 20px;
        }

        .btn:hover {
            background-color: RoyalBlue;
        }

        .btn a {
            /*display: inline-block;*/
            display: block;
            text-decoration: none;
            color: white;
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
            $.get('/cinema/command/lang', function (data) {
                location.reload()
            })
        }
    </script>
</header>
<div class="center">
    <p>${lang.gL("Order_unavailable")}</p>
</div>
</body>
</html>