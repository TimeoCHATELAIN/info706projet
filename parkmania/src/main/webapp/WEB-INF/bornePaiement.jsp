<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="parkmania.util.DateUtils" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Borne de paiement</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f6f6f6; margin: 40px; }
        h1 { color: #333; }
        .card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            width: 400px;
            margin: auto;
        }
        input, select, button {
            width: 100%; padding: 8px; margin: 5px 0; border-radius: 6px; border: 1px solid #ccc;
        }
        button { background-color: #007bff; color: white; border: none; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .message { color: green; }
        .erreur { color: red; }
    </style>
</head>
<body>
<div class="card">
    <h1>Borne de paiement</h1>

    <form method="get" action="bornePaiement">
        <label for="numero">Numéro du ticket</label>
        <input type="text" id="numero" name="numero"
               value="${param.numero != null ? param.numero : ''}"
               placeholder="Entrez le numéro du ticket" required />
        <button type="submit">Afficher le ticket</button>
    </form>

    <c:if test="${not empty erreur}">
        <p class="erreur">${erreur}</p>
    </c:if>
    <c:if test="${not empty message}">
        <p class="message">${message}</p>
    </c:if>

    <c:if test="${not empty ticket}">
        <hr>
        <h3>Informations du ticket</h3>
        <p><strong>Numéro :</strong> ${ticket.id}</p>
        <p><strong>Date d'entrée :</strong> ${dateEntree}</p>

        <h3>Paiement</h3>
        <p><strong>Total payé :</strong> ${totalPaye} €</p>
        <p><strong>Montant restant :</strong> ${montantRestant} €</p>

        <form method="post" action="bornePaiement">
            <input type="hidden" name="numero" value="${ticket.id}" />
            <label for="montant">Montant à payer (€)</label>
            <input type="number" step="0.01" id="montant" name="montant" value="${montantRestant}" required />

            <label for="typePaiement">Type de paiement</label>
            <select id="typePaiement" name="typePaiement" required>
                <option value="CB">Carte Bancaire</option>
                <option value="ESPECES">Espèces</option>
            </select>

            <button type="submit">Valider le paiement</button>
        </form>

        <c:if test="${not empty paiementsFormates}">
            <hr>
            <h3>Historique des paiements</h3>
            <ul>
                <c:forEach var="p" items="${paiementsFormates}">
                    <li>${p.datePaiement} — ${p.typePaiement} : ${p.montant} €</li>
                </c:forEach>
            </ul>
        </c:if>
    </c:if>
</div>
</body>
</html>
