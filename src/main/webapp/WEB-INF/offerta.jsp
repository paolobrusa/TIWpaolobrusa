<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>OFFERTE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="aste-container">
    <div class="aste-header">
        <h1>Offerta</h1>
        <p>Fai la tua offerta</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <c:out value="${errorMessage}"/>
        </div>
    </c:if>

    <c:if test="${empty errorMessage}">
        <div class="tables-container">
            <div class="section-container">
                <div class="section-header">
                    <h2 class="section-title article-title">Articoli Disponibili</h2>
                    <span class="section-count">${not empty articoli ? articoli.size() : 0} articoli</span>
                </div>

                <c:choose>
                    <c:when test="${not empty articoli}">
                        <table class="aste-table">
                            <thead>
                            <tr>
                                <th>Codice</th>
                                <th>Nome</th>
                                <th>Descrizione</th>
                                <th>Immagine</th>
                                <th>Prezzo</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="articolo" items="${articoli}">
                                <tr class="asta-row">
                                    <td class="asta-id">
                                        <c:out value="${articolo.code}"/>
                                    </td>
                                    <td class="article-name">
                                        <c:out value="${articolo.name}"/>
                                    </td>
                                    <td class="article-description">
                                        <c:out value="${articolo.description}"/>
                                    </td>
                                    <td class="article-path">
                                        <img src="${pageContext.request.contextPath}/Image/${articolo.path}" alt="Errore"/>
                                    </td>
                                    <td class="price">
                                        <fmt:formatNumber value="${articolo.price}" type="currency" currencyCode="EUR"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-aste-message">
                            <div class="no-aste-icon">📦</div>
                            <h3>Nessun articolo disponibile</h3>
                            <p>Non ci sono articoli da visualizzare al momento.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="section-container">
                <div class="section-header">
                    <h2 class="section-title auction-title">Offerte Ricevute</h2>
                    <span class="section-count">${not empty offerte ? offerte.size() : 0} offerte</span>
                </div>

                <c:choose>
                    <c:when test="${not empty offerte}">
                        <table class="aste-table">
                            <thead>
                            <tr>
                                <th>Utente</th>
                                <th>Offerta</th>
                                <th>Data</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="offerta" items="${offerte}">
                                <tr class="asta-row">
                                    <td class="user-name">
                                        <c:out value="${offerta.usnUser}"/>
                                    </td>
                                    <td class="min-bid">
                                        <fmt:formatNumber value="${offerta.bid}" type="currency" currencyCode="EUR"/>
                                    </td>
                                    <td class="date">
                                        <fmt:formatDate value="${offerta.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-aste-message">
                            <div class="no-aste-icon">💰</div>
                            <h3>Nessuna offerta ricevuta</h3>
                            <p>Non ci sono offerte da visualizzare al momento.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- form offerta -->
        <c:if test="${stato == 'attiva'}">
            <div class="forms-container">
                <div class="form-container">
                    <div class="form-header">
                        <h2 class="form-title auction-title">Offerta</h2>
                        <p class="form-subtitle">Inserisci la tua offerta</p>
                    </div>

                    <form method="post" class="form-content">
                        <div class="input-group">
                            <label for="offertaprezzo" class="input-label">Importo Offerta (€)</label>
                            <input type="number"
                                   name="offertaprezzo"
                                   id="offertaprezzo"
                                   class="form-input"
                                   placeholder="0"
                                   step="1"
                                   min="1"
                                   required>
                        </div>
                        <input type="hidden" name="idAsta" value="${asta.id}">
                        <button type="submit" class="btn-submit">
                            Invia Offerta
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
    </c:if>

    <div class="homepage-button-container">
        <a href="${pageContext.request.contextPath}/Acquisto" class="btn-homepage">
            Torna ad Acquisto
        </a>
    </div>
</div>
</body>
</html>