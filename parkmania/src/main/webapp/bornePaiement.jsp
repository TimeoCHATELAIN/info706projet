<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Borne de paiement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<div class="card">
    <h1>Borne de paiement</h1>

    <div class="top-actions">
        <a class="btn secondary" href="${pageContext.request.contextPath}/index.jsp">← Retour</a>
    </div>

    <!-- Recherche du ticket -->
    <form method="get" action="bornePaiement">
        <label for="numero-paiement">Numéro du ticket</label>
        <input type="text" id="numero-paiement" name="numero-paiement"
               value="${param['numero-paiement'] != null ? param['numero-paiement'] : ''}"
               placeholder="Entrez le numéro du ticket" required />
        <button type="submit">Afficher le ticket</button>
    </form>

    <!-- Messages -->
    <c:if test="${not empty erreur}">
        <p class="erreur">${erreur}</p>
    </c:if>
    <c:if test="${not empty message}">
        <p class="message">${message}</p>
    </c:if>

    <!-- Affichage du ticket -->
    <c:if test="${not empty ticket}">
        <hr>
        <h3>Informations du ticket</h3>
        <p><strong>Numéro :</strong> ${ticket.id}</p>
        <p><strong>Date d'entrée :</strong> ${dateEntree}</p>

        <h3>Paiement</h3>
        <p><strong>Total payé :</strong> ${totalPaye} €</p>
        <p><strong>Montant restant :</strong> ${montantRestant} €</p>

        <!-- Formulaire de paiement -->
        <form method="post" action="bornePaiement">
            <input type="hidden" name="numero-paiement" value="${ticket.id}" />
            <label for="montant">Montant à payer (€)</label>
            <input type="number" step="0.01" id="montant" name="montant"
                   value="${montantRestant}" required />

            <label for="typePaiement">Type de paiement</label>
            <select id="typePaiement" name="typePaiement" required>
                <option value="CB">Carte Bancaire</option>
                <option value="ESPECES">Espèces</option>
            </select>

            <button type="submit">Valider le paiement</button>
        </form>

        <!-- Historique des paiements -->
        <c:if test="${not empty paiementsFormates}">
            <hr>
            <h3>Historique des paiements</h3>
            <ul>
                <c:forEach var="p" items="${paiementsFormates}">
                    <li>
                            ${p.datePaiement}
                        —
                        <!-- affichage lisible du type -->
                        <c:choose>
                            <c:when test="${p.typePaiement == 'CB'}">Carte Bancaire</c:when>
                            <c:when test="${p.typePaiement == 'ESPECES'}">Espèces</c:when>
                            <c:otherwise>${p.typePaiement}</c:otherwise>
                        </c:choose>
                        : ${p.montant} €
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <!-- Justificatif de paiement (affiché après paiement) -->
        <c:if test="${not empty message}">
            <hr>
            <h3>Justificatif de paiement</h3>
            <p><strong>Numéro du ticket :</strong> ${ticket.id}</p>
            <p><strong>Date d'entrée :</strong> ${dateEntree}</p>

            <c:if test="${not empty dernierPaiement}">
                <p><strong>Dernier paiement :</strong> ${dernierPaiement}</p>
            </c:if>

            <p><strong>Montant total payé :</strong> ${totalPaye} €</p>

            <!-- Affiche le type du dernier paiement si fourni -->
            <c:if test="${not empty dernierTypePaiement}">
                <p><strong>Type du dernier paiement :</strong>
                    <c:choose>
                        <c:when test="${dernierTypePaiement == 'CB'}">Carte Bancaire</c:when>
                        <c:when test="${dernierTypePaiement == 'ESPECES'}">Espèces</c:when>
                        <c:otherwise>${dernierTypePaiement}</c:otherwise>
                    </c:choose>
                </p>
            </c:if>
        </c:if>
    </c:if>
</div>
</body>
</html>
