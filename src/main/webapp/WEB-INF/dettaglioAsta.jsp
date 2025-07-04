<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DettaglioAsta${asta.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dettaglioasta.css">
</head>
<body>
<div class="asta-container">
    <div class="asta-header">
        <h1>Dettaglio Asta</h1>
        <p>Asta selezionata</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
                ${errorMessage}
        </div>
    </c:if>
    <c:if test="${empty errorMessage}">
    <div class="main-content">
        <div class="asta-details-container">
            <div class="asta-details-header">
                <h2 class="asta-title">Asta #${asta.id}</h2>
                <div class="asta-status">
                    <c:choose>
                        <c:when test="${asta.state == 'attiva'}">
                            <span class="status-badge status-active">🟢 Attiva</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-closed">🔴 Chiusa</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="asta-info-grid">
                <div class="info-card">
                    <div class="info-label">ID Asta</div>
                    <div class="info-value asta-id">#${asta.id}</div>
                </div>

                <div class="info-card">
                    <div class="info-label">Prezzo Iniziale</div>
                    <div class="info-value price">€ <fmt:formatNumber value="${asta.initialPrice}"/></div>
                </div>

                <div class="info-card">
                    <div class="info-label">Offerta Minima</div>
                    <div class="info-value min-bid">€ <fmt:formatNumber value="${asta.minBid}"/></div>
                </div>

                <div class="info-card">
                    <div class="info-label">Data Asta</div>
                    <div class="info-value date">
                        <fmt:formatDate value="${asta.date}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                </div>

                <div class="info-card">
                    <div class="info-label">Stato</div>
                    <div class="info-value state">
                        <c:choose>
                            <c:when test="${asta.state == 'attiva'}">
                                <span class="state-active">Attiva</span>
                            </c:when>
                            <c:otherwise>
                                <span class="state-closed">Chiusa</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <c:if test="${asta.state == 'attiva'}">
                <div class="asta-actions">
                    <form method="post">
                        <input type="hidden" name="idAsta" value="${asta.id}">
                        <button type="submit" class="btn-close-auction">
                            Chiudi Asta
                        </button>
                    </form>
                </div>
            </c:if>

            <c:if test="${asta.state == 'chiusa' && not empty utente}">
                <div class="winner-section">
                    <h3 class="winner-title">Asta Aggiudicata</h3>
                    <div class="winner-card">
                        <div class="winner-info">
                            <h4>Aggiudicatario</h4>
                            <p class="winner-name">${utente.name} ${utente.surname}</p>
                            <p class="winner-address">${utente.address}</p>
                        </div>
                        <div class="winning-bid">
                            <h4>Prezzo finale</h4>
                            <p class="winning-amount">€ <fmt:formatNumber value="${offertaVincente.bid}"/></p>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <div class="offerte-container">
            <div class="offerte-header">
                <h3 class="offerte-title">Lista Offerte</h3>
                <div class="offerte-count">
                    <c:choose>
                        <c:when test="${not empty offerte}">
                            ${offerte.size()} offerte ricevute
                        </c:when>
                        <c:otherwise>
                            Nessuna offerta
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty offerte}">
                    <div class="offerte-table-container">
                        <table class="offerte-table">
                            <thead>
                            <tr>
                                <th>Utente</th>
                                <th>Offerta</th>
                                <th>Data</th>
                                <th>Stato</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="offerta" items="${offerte}" varStatus="status">
                                <tr class="offerta-row ${status.index == 0 && asta.state == 'chiusa' ? 'winning-row' : ''}">
                                    <td class="offerta-user">${offerta.usnUser}</td>
                                    <td class="offerta-bid">€ <fmt:formatNumber value="${offerta.bid}"/></td>
                                    <td class="offerta-date">
                                        <fmt:formatDate value="${offerta.date}" pattern="dd/MM/yyyy"/>
                                        <br>
                                        <small><fmt:formatDate value="${offerta.date}" pattern="HH:mm"/></small>
                                    </td>
                                    <td class="offerta-status">
                                        <c:choose>
                                            <c:when test="${status.index == 0 && asta.state == 'chiusa'}">
                                                <span class="status-winner">Vincente</span>
                                            </c:when>
                                            <c:when test="${status.index == 0 && asta.state == 'attiva'}">
                                                <span class="status-leading">Migliore</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-normal">Valida</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="no-offerte-message">
                        <div class="no-offerte-icon">Nessuna offerta ricevuta</div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    </c:if>
    <div class="homepage-button-container">
        <a href="${pageContext.request.contextPath}/Vendo" class="btn-homepage">
            Torna a Vendo
        </a>
    </div>
</div>
</body>
</html>