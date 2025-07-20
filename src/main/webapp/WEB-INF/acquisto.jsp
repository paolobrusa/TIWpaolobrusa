<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ACQUISTO</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="aste-container">
    <div class="aste-header">
        <h1>Acquisto</h1>
        <p>Cerca articoli che ti interessano</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
                ${errorMessage}
        </div>
    </c:if>

    <!-- ricerca-->
    <div class="search-section">
        <form method="GET" class="search-form">
            <div class="search-container">
                <input type="text"
                       name="search"
                       placeholder="Cerca nelle aste..."
                       class="search-input">
                <button type="submit" class="search-button">
                    Cerca
                </button>
            </div>
        </form>
    </div>

    <div class="tables-container">
        <!-- aste-->
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title auction-title">Aste Ricercate</h2>
                <span class="section-count">${not empty aste ? aste.size() : 0} aste</span>
            </div>

            <c:choose>
                <c:when test="${not empty aste}">
                    <table class="aste-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Prezzo Iniziale</th>
                            <th>Offerta Minima</th>
                            <th>Tempo Rimanente</th>
                            <th>Azioni</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="asta" items="${aste}">
                            <tr class="asta-row">
                                <td class="asta-id">#${asta.id}</td>
                                <td class="price">
                                    <fmt:formatNumber value="${asta.initialPrice}" type="currency" currencySymbol="€" />
                                </td>
                                <td class="min-bid">
                                    <fmt:formatNumber value="${asta.minBid}" type="currency" currencySymbol="€" />
                                </td>
                                <td class="date">
                                        ${asta.timeLeft}
                                </td>
                                <td class="actions">
                                    <c:url value="/Offerta" var="regURL">
                                        <c:param name="idasta" value="${asta.id}" />
                                    </c:url>
                                    <a href="${regURL}" class="btn-dettaglio">
                                        Dettagli
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-aste-message">
                        <h2>Cerca articoli per visualizzare aste corrispondenti</h2>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!--aggiudicazioni-->
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title closed-title">Aggiudicazioni</h2>
                <span class="section-count">${not empty aggiud ? aggiud.size() : 0} aggiudicazioni</span>
            </div>

            <c:choose>
                <c:when test="${not empty aggiud}">
                    <table class="aste-table">
                        <thead>
                        <tr>
                            <th>Offerta</th>
                            <th>ID Asta</th>
                            <th>Data</th>
                            <th>Azioni</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="aggiudicazione" items="${aggiud}">
                            <tr class="asta-row">
                                <td class="asta-id">
                                    <fmt:formatNumber value="${aggiudicazione.idAsta}" />
                                </td>
                                <td class="min-bid">
                                    <fmt:formatNumber value="${aggiudicazione.bid}" type="currency" currencySymbol="€" />
                                </td>
                                <td class="date">
                                    <fmt:formatDate value="${aggiudicazione.date}" pattern="dd/MM/yyyy HH:mm" />
                                </td>
                                <td class="actions">
                                    <c:url value="/Offerta" var="regURL">
                                        <c:param name="idasta" value="${aggiudicazione.idAsta}" />
                                    </c:url>
                                    <a href="${regURL}">
                                        <span class="status-badge status-won">Dettagli</span>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-aste-message">
                        <h3>Nessuna aggiudicazione</h3>
                        <p>Non ci sono ancora aggiudicazioni nel sistema</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="homepage-button-container">
        <a href="${pageContext.request.contextPath}/Homepage" class="btn-homepage">
            Torna alla Homepage
        </a>
    </div>
</div>
</body>
</html>