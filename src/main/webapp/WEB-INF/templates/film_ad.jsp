<%--@elvariable id="lang" type="String"--%>
<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Films</title>

    <link rel="stylesheet" href="../../static/css/header.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../../static/css/table.css">
    <script>
        $(document).ready(function () {
            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>
</head>
<body>
<div class="table-responsive">
    <div class="table-wrapper">
        <div class="table-title">
            <div class="row">
                <div class="col-sm-4">
                    <h2>
                        <label> ${lang.gL("Sessions")}</label>
                        <b>${lang.gL("Details")}</b></h2>
                </div>
                <div class="col-sm-8">
                    <a href="/cinema/admin/session" class="btn btn-primary"><i class="material-icons">&#xE863;</i>
                        <span>${lang.gL("Session")}</span></a>
                    <form action="@{/logout}" method="post"><input type="image" img class="hbtn"
                                                                   src="../../static/img/logout.png"></form>
                    <div class="lang_buttons">
                        <input type="image" onclick="swap_len()" class="hbtn" src="../../static/img/lang.png"
                               alt="lang">
                    </div>
                    <script>
                        function swap_len() {
                            $.get('/cinema/command/lang', function (data) {
                                location.reload()
                            })
                        }
                    </script>
                </div>
            </div>
        </div>
        <!--            -->
        <div class="table-filter">
            <div class="row">
                <div class="col-sm-12">
                    <form method="post" action="/cinema/admin/film/add"
                          enctype="multipart/form-data">
                        <button type="submit" class="btn btn-primary"><b>+</b></button>
                        <div class="filter-group">
                            <label>${lang.gL("Title")} ${lang.gL("en")}</label>
                            <input type="text" class="form-control" name="title_en" required>
                        </div>
                        <div class="filter-group">
                            <label>${lang.gL("Title")} ${lang.gL("ru")}</label>
                            <input type="text" class="form-control" name="title_ru" required>
                        </div>

                        <div class="filter-group">
                            <label>${lang.gL("Duration")}</label>
                            <input type="time" class="form-control" name="duration" required>
                        </div>
                        <div class="filter-group">
                            <label>${lang.gL("Load_Poster")}</label>
                            <input type="file" accept="image/jpeg" name="image" required>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!--            -->
        <div class="table-filter">
            <form id="filter" method="get" action="/cinema/admin/film">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="show-entries">
                            <span>Show</span>
                            <select name="quantity" class="form-control">
                                <c:forEach var="i" items="5,10,15,20">
                                    <option value="${i}" ${i==quantity?"selected":""}> ${i} </option>
                                </c:forEach>
                            </select>
                            <span>entries</span>
                        </div>
                    </div>
                    <div class="col-sm-9">
                        <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i></button>
                        <div class="filter-group">
                            <label>Search</label>
                            <input name="search" value="${search}" type="text" class="form-control">
                        </div>
                        <div class="filter-group">
                            <label>${lang.gL("Sorting")}</label>
                            <select name="sort" class="form-control">
                                <option selected="selected" value="title_en">${lang.gL("Title")} ${lang.gL("en")}.
                                </option>
                                <option value="title_ru">${lang.gL("Title")} ${lang.gL("ru")}.</option>
                                <option value="duration">${lang.gL("Duration")}</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>${lang.gL("Status")}</label>
                            <select name="status" class="form-control">
                                <option value="Any">${lang.gL("Any")}</option>
                                <option selected="selected" value="at_box_office">
                                    ${lang.gL("At_box_office")}
                                </option>
                                <option value="archive">${lang.gL("Archive")}</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>${lang.gL("Direction")}</label>
                            <select name="direction" class="form-control">
                                <option value="true">${lang.gL("Ascending")}</option>
                                <option selected="selected" value="false">${lang.gL("Descending")}</option>
                            </select>
                        </div>
                        <span class="filter-icon"><i class="fa fa-filter"></i></span>
                    </div>
                </div>
            </form>
        </div>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>&#x2116;</th>
                <th>${lang.gL("Title")} ${lang.gL("en")}.</th>
                <th>${lang.gL("Title")} ${lang.gL("ru")}.</th>
                <th>${lang.gL("Duration")}</th>
                <th>${lang.gL("Status")}</th>
                <th>${lang.gL("Action")}</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="film" items="${films}" varStatus="i">
                <tr id="${i.index+1}">
                    <td>${i.index + (page - 1)*quantity+1}</td>
                    <td>${film.titleEn}</td>
                    <td>${film.titleRu}</td>
                    <td>${film.getFormatDuration()}</td>

                    <td>
                        <c:if test="${film.boxOffice}">
                        <span class="status text-success" id="${film.film_id}"
                              onclick="swap_status(${film.getFilm_id()},true)">&bull;</span>
                            <label onclick="swap_status(${film.getFilm_id()},true)">
                                    ${lang.gL("At_box_office")}
                            </label>
                        </c:if>
                        <c:if test="${!film.boxOffice}">
                        <span class="status text-danger" id="${film.film_id}"
                              onclick="swap_status(${film.getFilm_id()},false)">&bull;</span>
                            <label id="${film.film_id}" onclick="swap_status(${film.getFilm_id()},false)">
                                    ${lang.gL("Archive")}
                            </label>
                        </c:if>
                    </td>
                    <td>
                        <a href="#" onclick="delete_film(${film.getFilm_id()})" class="view"
                           title="${lang.gL("Delete")}" data-toggle="tooltip">
                            <i class="material-icons">delete</i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <script>
            function delete_film(id1) {
                $.get("/cinema/admin/film/delete?id=" + id1, function (data) {
                    /* document.getElementById(id2+1).remove();*/
                    location.reload()
                })
            }

            function swap_status(id, status) {
                $.get("/cinema/admin/film/swap_status?id=" + id + "&status=" + status, function (data) {
                    location.reload()
                })
            }
        </script>
        <%
            pageContext.setAttribute("url", request.getAttribute("javax.servlet.forward.request_uri") + "?" + request.getQueryString());
        %>
        <div class="clearfix">
            <ul class="pagination">
                <c:if test="${page>1}">
                    <li class="page-item disabled">
                        <a href="<c:url value = "${url}">
                                   <c:param name = "page" value ="${page-1}"/>
                                 </c:url>">
                                ${lang.gL("Previous")}
                        </a>
                    </li>
                </c:if>
                <li class="page-item active">
                    <button href="#" class="page-link" text="${page}"></button>
                </li>
                <li class="page-item disabled">
                    <a href="<c:url value = "${url}">
                              <c:param name = "page" value ="${page+1}"/>
                             </c:url>">
                        ${lang.gL("Next")}
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>