<%--@elvariable id="lang" type="String"--%>
<%@include file="/WEB-INF/jspf/generalSetings.jspf" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Session</title>

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
                        ${lang.gL("Sessions")}
                        <b>${lang.gL("Details")}</b>
                    </h2>
                </div>
                <div class="col-sm-8">
                    <a href="/cinema/admin/film" class="btn btn-primary">
                        <i class="material-icons">&#xE863;</i>
                        <span>${lang.gL("Movie")}</span>
                    </a>
                    <form action="/cinema/logout" method="post"><input type="image" img class="hbtn"
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
        <c:set var="now" value="<%=new java.util.Date()%>"/>
        <div class="table-filter">
            <form method="post" action="/cinema/admin/session/add">
                <div class="row">
                    <div class="col-sm-10">
                    </div>
                    <div class="col-sm-12">
                        <button type="submit" class="btn btn-primary"><b>+</b></button>
                        <div class="filter-group">
                            <label>${lang.gL("Movie")}</label>
                            <input type="text" list="film" class="form-control" name="film_id" required>
                            <datalist id="film">
                                <c:forEach var="film" items="${films}">
                                    <option value="${film.film_id}">
                                            ${film.titleEn}
                                    </option>
                                </c:forEach>
                            </datalist>
                        </div>
                        <div class="filter-group">
                            <label text="${lang.gL("Price")}">Price</label>
                            <input type="number" class="form-control" name="price" required min="0">
                        </div>
                        <div class="filter-group">
                            <label text="${lang.gL("Time")}">Duration</label>
                            <input type="time" class="form-control" name="time" required>
                        </div>
                        <label text="${lang.gL("From")}">From</label>
                        <input name="date1" type="date"
                               value="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd" />"
                               min="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />">
                        <label text="${lang.gL("To")}">To</label>
                        <input name="date2" type="date"
                               value="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />"
                               min="<fmt:formatDate type = "date" value = "${now}" pattern = "yyyy-MM-dd"  />">
                    </div>
                </div>
            </form>
        </div>

        <c:if test="${error==null}">
            <div class="table-filter">
                <form id="filter" method="get" action="/cinema/admin/session">
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
                                <input name="search" type="text" class="form-control">
                            </div>
                            <div class="filter-group">
                                <label text="${lang.gL("Sorting")}">Sorting</label>
                                <select name="sort" class="form-control">
                                    <option value="film.titleEn">${lang.gL("Title")}</option>
                                    <option selected value="time">${lang.gL("Time")}</option>
                                    <option value="occupancy">${lang.gL("Occupancy")}</option>
                                    <option value="price">${lang.gL("Price")}</option>
                                </select>
                            </div>
                            <div class="filter-group">
                                <label>${lang.gL("Status")}</label>
                                <select name="status" class="form-control">
                                    <option value="Any">
                                            ${lang.gL("Any")}
                                    </option>
                                    <option value="movie_is_passed">
                                            ${lang.gL("Movie_is_passed")}
                                    </option>
                                    <option selected="selected" value="movie_will_be_shown">
                                            ${lang.gL("Movie_will_be_shown")}
                                    </option>
                                </select>
                            </div>
                            <div class="filter-group">
                                <label>${lang.gL("Direction")}</label>
                                <select name="direction" class="form-control">
                                    <option value="true">
                                            ${lang.gL("Ascending")}
                                    </option>
                                    <option selected="selected" value="false">
                                            ${lang.gL("Descending")}
                                    </option>
                                </select>
                            </div>
                            <span class="filter-icon"><i class="fa fa-filter"></i></span>
                        </div>
                    </div>
                </form>
            </div>
        </c:if>
        <c:if test="${error!=null}">
            <div class="table-filter">
                <div class="error">
                    <b>${lang.gL(error)}</b>
                </div>
            </div>
        </c:if>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>${lang.gL("Movie")}</th>
                <th>${lang.gL("Date")}</th>
                <th>${lang.gL("Time")}</th>
                <th>${lang.gL("Occupancy")}</th>
                <th>${lang.gL("Price")}</th>
                <th>${lang.gL("Status")}</th>
                <th>${lang.gL("Action")}</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="row" items="${sessions}" varStatus="i">
                <tr id="${i.index+1}">
                    <td>${i.index + (page - 1)*quantity}</td>
                    <td>
                        <a href="/cinema/admin/film?search=${row.film.titleEn}">${row.film.titleEn}</a>
                    </td>
                    <td>${row.date}</td>
                    <td>${row.time}</td>
                    <td>${row.occupancy}/${hallCapacity}</td>
                    <td>${row.price}</td>
                    <td>
                        <c:if test="${row.isOnNow()}">
                            <span class="status text-warning">&bull;</span>
                            <label>${lang.gL("Movie_is_on_now")}</label>
                        </c:if>
                        <c:if test="${row.isPassed()}">
                            <span class="status text-danger">&bull;</span>
                            <label>${lang.gL("Movie_is_passed")}</label>
                        </c:if>
                        <c:if test="${row.willBeShown()}">
                            <span class="status text-success">&bull;</span>
                            <label>${lang.gL("Movie_will_be_shown")}</label>
                        </c:if>

                    </td>

                    <td>
                        <a href="#" onclick="delete_session(${row.getSession_id()})" class="view"
                           title="${lang.gL("Delete")}" data-toggle="tooltip">
                            <i class="material-icons">delete</i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <script>
            function delete_session(id1) {
                $.get("/cinema/admin/session/delete?id=" + id1, function (data) {
                    // document.getElementById(id2).remove();
                    location.reload()
                })
            }</script>
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