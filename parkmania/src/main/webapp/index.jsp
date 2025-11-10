<%--
  Created by IntelliJ IDEA.
  User: nathan
  Date: 13/10/2025
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Bienvenu chez parkmania</title>
</head>
<body>
    <h1>Bienvenue au parking !</h1>
    <form action="entree" method="get">
        <button type="submit">Entrer dans le parking</button>
    </form>
    <form action="bornePaiement" method="get">
        <div>
            <label for="numero">Numéro du ticket</label>
            <input
                    type="text"
                    id="numero"
                    name="numero"
                    placeholder="numéro du ticket"
                    required
            />
        </div>
        <button type="submit">Payer</button>
    </form>
    <form action="sortie" methode="get">
        <button type="submit">Sortir du parking</button>
    </form>
</body>
</html>
