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
    <%--    <script async src='/cdn-cgi/bm/cv/669835187/api.js'></script></head>--%>
</head>
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
        <h2>${lang.gL("Login_Page")}</h2>
        <p >${lang.gL("Login_here")}</p>
        <c:forEach var="item" items="${message}"  >
            <p> ${lang.gL(item)} </p>
        </c:forEach>
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="login" method="post">
                <div class="form-group">
                    <label> ${lang.gL("User_Name")}</label>
                    <input type="text" name="username" class="form-control" placeholder=${lang.gL("User_Name")} placeholder="UserName">
                </div>
                <div class="form-group">
                    <label> ${lang.gL("Password")}</label>
                    <input type="password" name="password" id="password" class="form-control" placeholder=" ${lang.gL("Password")}">
                </div>
                <button type="submit" class="btn btn-secondary" >${lang.gL("Login")}</button>
                <a href="register" class="btn btn-black" >${lang.gL("Unregistered")}</a>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">(function(){window['__CF$cv$params']={r:'6d5a872b8b8d1622',m:'i.N0ZiVBOBM7SlHANmBW4hYsR6I3aloryehfr3dF_1s-1643543902-0-AfPCccCtyV/PrtgFI/Lc9uygo9Mla1jjhF82QbYhlvbEtRDVhI6/9Jwr0MHEyDV329eAHG9cU2VHOSc6C43Bdvg12aFkZRH2CiHug9c+8l61Ekv3IRWcq6sUJW+0xxZVSopPrmOcYPl3jNkxaIiSGvqchC636Krv6quLh4tD8HgQezHpnB6w1+AOlxx3MsQAjA==',s:[0x681bed3199,0x57cc196c1f],}})();</script></body>
</html>

