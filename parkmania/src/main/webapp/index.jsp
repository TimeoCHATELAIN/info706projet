<%--
  Created by IntelliJ IDEA.
  User: nathan
  Date: 13/10/2025
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Bienvenu chez parkmania</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <div class="center-viewport">
        <div class="card">
            <h1>Bienvenue au parking !</h1>
        <div class="top-actions" style="justify-content:flex-start;gap:12px;align-items:center">
            <button id="enter-btn">Entrer dans le parking</button>
            <div class="switch" style="margin-left:12px">
                <label for="time-accel">Accélérer x10</label>
                <input type="checkbox" id="time-accel" />
            </div>
            <div id="time-status" style="margin-left:10px;color:var(--muted);font-size:0.95rem">x1</div>
        </div>
        <form action="bornePaiement" method="get">
            <div>
                <label for="numero-paiement">Numéro du ticket</label>
                <input
                        type="text"
                        id="numero-paiement"
                        name="numero-paiement"
                        placeholder="numéro du ticket"
                        required
                />
            </div>
            <button type="submit">Payer</button>
        </form>
        <form action="sortie" methode="get">
            <div>
                <label for="numero-sortie">Numéro du ticket</label>
                <input
                        type="text"
                        id="numero-sortie"
                        name="numero-sortie"
                        placeholder="numéro du ticket"
                        required
                />
            </div>
            <button type="submit">Sortir du parking</button>
        </form>
        <c:if test="${peutSortir != null}">
            <c:choose>
                <c:when test="${!peutSortir}">
                    <p class="erreur">Vous devez payer avant de sortir !</p>
                </c:when>
                <c:otherwise>
                    <p class="message">Au revoir !</p>
                </c:otherwise>
            </c:choose>
        </c:if>
        </div>
    </div>
    <!-- Ticket modal -->
    <div id="ticket-modal" class="modal" aria-hidden="true">
        <div class="modal-overlay" id="modal-overlay"></div>
        <div class="modal-content card" role="dialog" aria-modal="true">
            <div class="modal-header">
                <h2 id="modal-title">Ticket créé</h2>
                <button class="btn secondary" id="modal-close">Fermer</button>
            </div>
            <div class="modal-body">
                <p><strong>Numéro :</strong> <span id="ticket-id"></span></p>
                <p><strong>Date d'entrée :</strong> <span id="ticket-date"></span></p>
            </div>
        </div>
    </div>

    <script>
        (function(){
            const enterBtn = document.getElementById('enter-btn');
            const modal = document.getElementById('ticket-modal');
            const overlay = document.getElementById('modal-overlay');
            const closeBtn = document.getElementById('modal-close');
            const ticketIdEl = document.getElementById('ticket-id');
            const ticketDateEl = document.getElementById('ticket-date');

            function showModal(id, date){
                ticketIdEl.textContent = id;
                ticketDateEl.textContent = date;
                modal.setAttribute('aria-hidden','false');
                modal.classList.add('open');
            }
            function hideModal(){
                modal.setAttribute('aria-hidden','true');
                modal.classList.remove('open');
            }

            enterBtn.addEventListener('click', function(e){
                e.preventDefault();
                const url = '${pageContext.request.contextPath}/entree?ajax=true';
                fetch(url, {headers: { 'Accept': 'application/json' }})
                    .then(r => r.json())
                    .then(data => {
                        showModal(data.id, data.dateEntree);
                    })
                    .catch(err => {
                        console.error('Erreur création ticket', err);
                        alert('Erreur lors de la création du ticket');
                    });
            });

            // Time acceleration switch handling
            const timeCheckbox = document.getElementById('time-accel');
            const timeStatus = document.getElementById('time-status');

            function setMultiplierOnServer(m) {
                const url = '${pageContext.request.contextPath}/time';
                const form = new URLSearchParams();
                form.append('multiplier', String(m));
                return fetch(url, {method: 'POST', body: form});
            }

            // initialize switch based on server value
            fetch('${pageContext.request.contextPath}/time')
                .then(r => r.json())
                .then(data => {
                    const m = Number(data.multiplier) || 1;
                    timeCheckbox.checked = (Math.abs(m - 10) < 1e-6);
                    timeStatus.textContent = 'x' + m;
                })
                .catch(() => {});

            timeCheckbox.addEventListener('change', function(){
                const m = this.checked ? 10 : 1;
                setMultiplierOnServer(m)
                    .then(()=>{
                        timeStatus.textContent = 'x' + m;
                    })
                    .catch(()=>{alert('Erreur lors du changement de vitesse');});
            });

            overlay.addEventListener('click', hideModal);
            closeBtn.addEventListener('click', hideModal);
            document.addEventListener('keydown', function(e){ if(e.key === 'Escape') hideModal(); });
        })();
    </script>
</body>
</html>
