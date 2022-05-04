<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="../../static/css/login.css">
    <link rel="stylesheet" href="../../static/css/header.css">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<body>

<div class="sidenav">
    <header class="header">
        <div class="lang_buttons">
            <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png" alt="lang">
            <script>
                function swap_len(){$.get('/cinema/command/lang',function (data){location.reload()})}
            </script>
        </div>
    </header>
    <div class="login-main-text">
        <h2>${lang.gL("Cinema")}</h2>
        <h2>${lang.gL("Register_Page")}</h2>
        <p >${lang.gL("Register_from_here_to_access")}</p>

    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="register" method="post">

                <div class="form-group">
                    <label> ${lang.gL("User_Name")}</label>
                    <input type="text" name="username" class="form-control" placeholder=${lang.gL("User_Name")} placeholder="UserName">
                </div>
                <div class="form-group">
                    <label >${lang.gL("Email")}</label>
                    <input type="email" name="email" class="form-control" placeholder="${lang.gL("Email")}">
                </div>
                <div class="form-group">
                    <label>${lang.gL("Password")}</label>
                    <input type="password" name="password" id="password" class="form-control" placeholder="${lang.gL("Password")}" >
                </div>
                <div class="form-group">
                    <label>${lang.gL("Confirm_Password")}</label>
                    <input type="password" class="form-control" id="confirm_password"  placeholder="${lang.gL("Confirm_Password")}" onkeyup='check();'>
                    <span id='message'></span>
                </div>
                <button type="submit" class="btn btn-secondary">${lang.gL("Register")}</button>
                <a href="login" class="btn btn-black" >${lang.gL("Login")}</a>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">(function(){window['__CF$cv$params']={r:'6d5a86e8c8761622',m:'wxVC58W2Ck_KDJ3.M7X_TBEcFLBFrOBrIY3yqYgbcOM-1643543891-0-ATE8Sp5A6OcqsRbi7fme+bKto1+VgiY/ciB/rSJ6R3TISZWB2Q+V1bX/0J6uMaA/9verTEq4w0w25A8xGyhCU/thiVC8iz5XaegJvLrpu2D0OPl2fb01HjAluSmfneQtrcCPUCx9oqdofpDDjP94a0pe9VTajoRhLYHmtGEoWJEkh0XDUZvtW5uDtpKbAxSQz/6Hl/sdfbzInZ9tVX7JBuM=',s:[0x09b1881733,0xdc94061b33],}})();</script></body>
<script>
    var check = function() {
    if (document.getElementById('password').value ==
        document.getElementById('confirm_password').value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = 'matching';
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = 'not matching';
    }
}</script>
</html>
