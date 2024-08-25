<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Ventes</title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .container {
            padding: 40px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            max-width: 1200px;
            margin: 40px auto;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .header h2 {
            font-size: 2em;
            color: #5A9D78 ; /* Vert ajusté pour les titres */
        }
        .button-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: space-around;
        }
        .button {
            background-color: #5A9D78 ; /* Vert ajusté pour les boutons */
            border: none;
            color: white !important; /* Texte en blanc */
            padding: 20px 40px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 18px;
            border-radius: 8px;
            cursor: pointer;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            position: relative;
        }
        .button:hover {
            background-color: #1D6F3E; /* Vert légèrement plus sombre pour le survol */
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
        }
        .button i {
            display: block;
            margin: 0 auto 10px;
            font-size: 24px; /* Taille de l'icône */
        }
        .info-box {
            background-color: #e8f5e9;
            border-left: 6px solid #5A9D78 ; /* Bordure ajustée */
            padding: 20px;
            margin-top: 30px;
            font-size: 16px;
            color: #555;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        .frame {
            border: 2px solid #5A9D78 ; /* Bordure ajustée */
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Gestion des Ventes</h2>
        </div>
        <div class="button-container">
          <a href="${pageContext.request.contextPath}/module/mycashier/venteProduit.form" class="button">
              <i class="bi bi-plus-circle"></i> <!-- Icône pour créer une vente -->
              Créer une vente
          </a>

            <a href="/editSale" class="button">
                <i class="bi bi-pencil-square"></i> <!-- Icône pour éditer une vente -->
                Éditer une vente
            </a>
            <a href="/cashSale" class="button">
                <i class="bi bi-cash"></i> <!-- Icône pour encaisser une vente -->
                Encaisser une vente
            </a>
            <a href="/listStock" class="button">
                <i class="bi bi-box"></i> <!-- Icône pour liste des stocks -->
                Liste des stocks
            </a>
            <a href="/salesHistory" class="button">
                <i class="bi bi-clock-history"></i> <!-- Icône pour historique des ventes -->
                Historique des ventes
            </a>
        </div>
        <div class="info-box frame">
            <p>Bienvenue dans la section de gestion des ventes. Utilisez les boutons ci-dessus pour effectuer les différentes opérations de vente. Assurez-vous de suivre les procédures standard pour chaque opération.</p>
        </div>
    </div>
    <!-- Optional: Add Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>

<%@ include file="/WEB-INF/template/footer.jsp"%>
