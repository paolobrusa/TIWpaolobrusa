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

    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div class="error-message">
        <%= errorMessage %>
    </div>
    <%
        }
    %>

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
                            <th>Offerta Massima</th>
                            <th>Tempo rimanente</th>
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
                                            ${asta.timeLeft}
                                    </td>
                                    <td class="actions">
                                        <c:url value="/Dettaglio" var="regURL">
                                            <c:param name="idasta" value="${asta.id}" />
                                        </c:url>
                                        <a href="${regURL}" class="btn-dettaglio btn-active">
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
                            <th>Offerta Vincente</th>
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
                                        <c:url value="/Dettaglio" var="regURL">
                                            <c:param name="idasta" value="${asta.id}" />
                                        </c:url>
                                        <a href="${regURL}" class="btn-dettaglio btn-closed">
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

    <!-- Sezioni Form Affiancate -->
    <div class="forms-container">
        <!-- Form Creazione Articolo -->
        <div class="form-container">
            <div class="form-header">
                <h2 class="form-title article-title">Crea Articolo</h2>
                <p class="form-subtitle">Aggiungi un nuovo articolo</p>
            </div>

            <form action="${pageContext.request.contextPath}/AddArticolo" method="post" class="form-content">
                <div class="input-group">
                    <label for="nome" class="input-label">Nome Articolo</label>
                    <input type="text" id="nome" name="nome" class="form-input" required placeholder="Nome articolo">
                </div>

                <div class="input-group">
                    <label for="descrizione" class="input-label">Descrizione</label>
                    <textarea id="descrizione" name="descrizione" class="form-textarea" required placeholder="Descrizione" rows="3"></textarea>
                </div>

                <div class="input-group">
                    <label for="path" class="input-label">Path Immagine</label>
                    <input type="text" id="path" name="path" class="form-input" placeholder="URL o percorso dell'immagine">
                </div>

                <div class="input-group">
                    <label for="prezzo" class="input-label">Prezzo Base (€)</label>
                    <input type="number" id="prezzo" name="prezzo" class="form-input" step="1" min="1" required placeholder="0">
                </div>

                <button type="submit" class="btn-submit btn-article">
                    Crea Articolo
                </button>
            </form>
        </div>

        <!-- Form Creazione Asta -->
        <div class="form-container">
            <div class="form-header">
                <h2 class="form-title auction-title">Crea Asta</h2>
                <p class="form-subtitle">Crea una nuova asta</p>
            </div>
            <form action="${pageContext.request.contextPath}/CreateAsta" method="post" class="form-content">
                <div class="input-group">
                    <label class="input-label">Seleziona Articoli</label>
                    <div class="checkbox-container">
                        <c:choose>
                            <c:when test="${not empty articoli}">
                                <c:forEach var="articolo" items="${articoli}">
                                    <div class="checkbox-item">
                                        <input type="checkbox" id="articolo_${articolo.code}" name="codice" value="${articolo.code}" class="checkbox-input">
                                        <label for="articolo_${articolo.code}" class="checkbox-label">
                                            <span class="checkbox-name">${articolo.name}</span>
                                            <span class="checkbox-price">€<fmt:formatNumber value="${articolo.price}" pattern="#,##0.00"/></span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="no-articles-message">
                                    <p>Nessun articolo disponibile. Crea prima un articolo.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="input-group">
                    <label for="minBid" class="input-label">Rilancio Minimo (€)</label>
                    <input type="number" id="minBid" name="minBid" class="form-input" step="1" min="1" required placeholder="0">
                </div>

                <div class="input-group">
                    <label for="dataScadenza" class="input-label">Data Scadenza</label>
                    <input type="datetime-local" id="dataScadenza" name="date" class="form-input" required step="1">
                </div>

                <button type="submit" class="btn-submit btn-auction" ${empty articoli ? 'disabled' : ''}>
                    Crea Asta
                </button>
            </form>
        </div>
    </div>

    <!-- Bottone per tornare alla homepage -->
    <div class="homepage-button-container">
        <a href="${pageContext.request.contextPath}/Homepage" class="btn-homepage">
            Torna alla Homepage
        </a>
    </div>

<%--    <c:if test="${empty aste}">--%>
<%--        <div class="no-aste-message main-message">--%>
<%--            <div class="no-aste-icon">📭</div>--%>
<%--            <h2>Non ci sono aste</h2>--%>
<%--            <p>Non hai ancora creato nessuna asta</p>--%>
<%--            <a href="crea-asta.jsp" class="btn-create-auction">--%>
<%--                Crea la tua prima asta--%>
<%--                <span class="btn-arrow">+</span>--%>
<%--            </a>--%>
<%--        </div>--%>
<%--    </c:if>--%>
</div>
</body>
</html>