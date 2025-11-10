# Compte-rendu pour le rendu — ParkMania

Ce document contient uniquement les éléments demandés pour la version finale du rendu :

1) Membres du binôme

- Nathan ROI
- Timéo CHATELAIN

Remplacez les deux lignes ci-dessus par les noms réels des personnes du binôme.

2) Choix d'implantation

- Architecture : application web Java EE / Jakarta EE avec EJB pour la logique métier et JSP pour la présentation.
- Déploiement : packaging en WAR via Maven, prévu pour un serveur Jakarta EE (Payara, TomEE, WildFly).
- Temps applicatif : ajout d'un `TimeService` (EJB Singleton) fournissant une horloge virtualisable (multiplicateur). Les services utilisent `TimeService.getNow()` au lieu de `LocalDateTime.now()` pour permettre l'accélération du temps (utile pour les tests).
- Interface utilisateur : pages JSP stylées avec `styles/main.css`. Modal JS pour afficher le ticket d'entrée via AJAX (servlet `/entree?ajax=true`).
- Simplicité et compatibilité : pas de dépendances front-end externes lourdes (pas de framework JS), styling CSS pur pour rester simple et portable.

**Détails d'architecture (composants principaux)**

- Entités (package `parkmania.entite`)
  - `Ticket` : représente un ticket d'entrée avec `id`, `dateEntree`, `dateSortie` et la relation OneToMany vers `Paiement`.
  - `Paiement` : représente un paiement lié à un ticket (`datePaiement`, `montant`, `typePaiement`).

- Services (package `parkmania.service`)
  - `TicketService` : création, lecture et mise à jour des tickets. Lors de la création il utilise `TimeService` pour fixer `dateEntree`.
  - `PaiementService` : calcul du montant dû, gestion des paiements, génération du justificatif. Utilise `TimeService` pour toutes les opérations temporelles (dates de paiements, calculs de durée, période de grâce).
  - `SortieService` : vérifie si la sortie peut être autorisée (solde payé), enregistre `dateSortie` via `TimeService`.
  - `TimeService` : EJB Singleton/Startup qui fournit l'heure virtuelle et permet de régler un multiplicateur (x1, x10...). Il garde une base réelle et une base virtuelle pour assurer la continuité temporelle lors des changements de multiplicateur.

- Servlets (package `parkmania.servlet`)
  - `BorneEntreeServlet` (`/entree`) : crée un ticket. Retourne HTML pour les accès directs et JSON si appelé avec `?ajax=true` (utilisé par la popup JS).
  - `BornePaiementServlet` (ou JSP controller) : gestion de l'affichage et de la validation des paiements (la vue principale est `bornePaiement.jsp`).
  - `TimeControlServlet` (`/time`) : endpoint GET/POST pour lire et modifier le multiplicateur du `TimeService` (utilisé par le switch côté client).

- Webapp (sous `src/main/webapp`)
  - JSP : `index.jsp` (page d'accueil, bouton Entrée, switch accélération, formulaires), `bornePaiement.jsp` (recherche et paiement), éventuellement `borneSortie.jsp` si présent.
  - Styles : `styles/main.css` centralise le thème, les composants (card, modal, boutons) et la responsivité.
  - JS inline léger : responsabilise les interactions (popup ticket, appel AJAX vers `/entree` et `/time`, contrôle de la modal). Pas de bundle JS complexe pour garder le projet simple.

Ces choix visent la lisibilité, la simplicité de déploiement sur un serveur Jakarta EE et la facilité de test (le `TimeService` permet d'accélérer les scénarios temporels sans modifier la logique métier).

3) Comment utiliser l'application (mode d'emploi rapide)

- Construire l'artefact

- Déployer le WAR (`target/parkmania.war`) sur un serveur Jakarta EE et ouvrir :

```
http://localhost:8080/parkmania/
```
