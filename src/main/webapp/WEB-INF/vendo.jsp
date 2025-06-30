<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VENDO</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aste.css">
</head>
<body>
<div class="aste-container">
    <div class="aste-header">
        <h1>Aste</h1>
        <p>Gestisci le tue aste</p>
    </div>

    <!-- Sezioni Aste Affiancate -->
    <div class="tables-container">
        <!-- Sezione Aste Aperte -->
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title active-title">🟢 Aste Aperte</h2>
                <div class="section-count">
                    <c:set var="asteAperte" value="0"/>
                    <c:forEach var="asta" items="${aste}">
                        <c:if test="${asta.state == 'attiva'}">
                            <c:set var="asteAperte" value="${asteAperte + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${asteAperte} aste
                </div>
            </div>

            <c:set var="haAsteAperte" value="false"/>
            <c:forEach var="asta" items="${aste}">
                <c:if test="${asta.state == 'attiva'}">
                    <c:set var="haAsteAperte" value="true"/>
                </c:if>
            </c:forEach>

            <c:choose>
                <c:when test="${haAsteAperte}">
                    <table class="aste-table">
                        <thead>
                        <tr>
                            <th>ID Asta</th>
                            <th>Prezzo Iniziale</th>
                            <th>Rilancio Minimo</th>
                            <th>Data Asta</th>
                            <th>Azioni</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="asta" items="${aste}">
                            <c:if test="${asta.state == 'attiva'}">
                                <tr class="asta-row active-row">
                                    <td class="asta-id">#${asta.id}</td>
                                    <td class="price">
                                        <fmt:formatNumber value="${asta.initialPrice}" type="currency" currencySymbol="€"/>
                                    </td>
                                    <td class="min-bid">
                                        <fmt:formatNumber value="${asta.minBid}" type="currency" currencySymbol="€"/>
                                    </td>
                                    <td class="date">
                                        <fmt:formatDate value="${asta.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="actions">
                                        <a href="dettaglioasta.jsp?id=${asta.id}" class="btn-dettaglio btn-active">
                                            Gestisci
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-aste-message">
                        <div class="no-aste-icon">🟢</div>
                        <h3>Nessuna asta aperta</h3>
                        <p>Non hai aste attualmente attive</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Sezione Aste Chiuse -->
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title closed-title">🔴 Aste Chiuse</h2>
                <div class="section-count">
                    <c:set var="asteChiuse" value="0"/>
                    <c:forEach var="asta" items="${aste}">
                        <c:if test="${asta.state == 'chiusa'}">
                            <c:set var="asteChiuse" value="${asteChiuse + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${asteChiuse} aste
                </div>
            </div>

            <c:set var="haAsteChiuse" value="false"/>
            <c:forEach var="asta" items="${aste}">
                <c:if test="${asta.state == 'chiusa'}">
                    <c:set var="haAsteChiuse" value="true"/>
                </c:if>
            </c:forEach>

            <c:choose>
                <c:when test="${haAsteChiuse}">
                    <table class="aste-table">
                        <thead>
                        <tr>
                            <th>ID Asta</th>
                            <th>Prezzo Iniziale</th>
                            <th>Rilancio Minimo</th>
                            <th>Data Asta</th>
                            <th>Azioni</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="asta" items="${aste}">
                            <c:if test="${asta.state == 'chiusa'}">
                                <tr class="asta-row closed-row">
                                    <td class="asta-id">#${asta.id}</td>
                                    <td class="price">
                                        <fmt:formatNumber value="${asta.initialPrice}" type="currency" currencySymbol="€"/>
                                    </td>
                                    <td class="min-bid">
                                        <fmt:formatNumber value="${asta.minBid}" type="currency" currencySymbol="€"/>
                                    </td>
                                    <td class="date">
                                        <fmt:formatDate value="${asta.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="actions">
                                        <a href="dettaglioasta.jsp?id=${asta.id}" class="btn-dettaglio btn-closed">
                                            Risultati
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-aste-message">
                        <div class="no-aste-icon">🔴</div>
                        <h3>Nessuna asta chiusa</h3>
                        <p>Non hai ancora aste terminate</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Messaggio se non ci sono aste -->
    <c:if test="${empty aste}">
        <div class="no-aste-message main-message">
            <div class="no-aste-icon">📭</div>
            <h2>Non ci sono aste</h2>
            <p>Non hai ancora creato nessuna asta</p>
            <a href="crea-asta.jsp" class="btn-create-auction">
                Crea la tua prima asta
                <span class="btn-arrow">+</span>
            </a>
        </div>
    </c:if>
</div>
</body>
</html>