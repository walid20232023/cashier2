<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire de Vente</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css">
    <style>
        /* Styles personnalisés */
        body {
            background-color: #f4f4f9;
        }
        .navbar {
            background-color: #29AB87;
        }
        .navbar-dark .navbar-nav .nav-link {
            color: #ffffff;
        }
        .navbar-dark .navbar-nav .nav-link:hover {
            color: #e8f5e9;
        }
        .card {
            margin: 20px 0;
        }
        .card-header {
            background-color: #29AB87;
            color: #ffffff;
        }
        .btn-custom {
            background-color: #29AB87;
            color: #ffffff;
        }
        .btn-custom:hover {
            background-color: #248739;
        }
        .icon-view, .icon-delete, .icon-edit {
            cursor: pointer;
            color: #29AB87;
        }
        .icon-view:hover, .icon-delete:hover, .icon-edit:hover {
            color: #248739;
        }
        .form-table th, .form-table td {
            text-align: center;
            vertical-align: middle;
        }
        .form-table th {
            background-color: #e8f5e9;
        }
        .medicament-col {
            width: 30%; /* Augmenter la largeur pour le champ Médicament */
        }
        .quantity-col, .pu-col, .total-col, .stock-ph-col, .stock-mg-col {
            width: 10%;
        }
        .form-table td, .form-table th {
            white-space: nowrap;
        }
        /* Style pour les listes déroulantes */
        #clientSearchResults, #drugSearchResults {
            position: absolute;
            width: 100%;
            max-height: 200px;
            overflow-y: auto;
            z-index: 1000;
            background-color: white;
            border: 1px solid #ccc;
        }
        .list-group-item {
            cursor: pointer;
        }
        .list-group-item:hover {
            background-color: #f0f0f0;
        }
        /* Style spécifique pour les champs d'assurance */
        .form-group.assurance input {
            background-color: #e0f7fa; /* Bleu clair */
            border: 1px solid #b2ebf2; /* Bordure légèrement plus foncée */
            border-radius: 4px; /* Coins légèrement arrondis */
        }
        .form-group.assurance {
            border: 1px solid #b2ebf2; /* Bordure autour du champ */
            border-radius: 4px; /* Coins légèrement arrondis */
            padding: 10px; /* Espacement intérieur */
            margin-bottom: 10px; /* Espacement entre les champs */
        }

        #partAssurance, #partClient, #total {
            font-weight: bold;
            color: #2c3e50;
            background-color: #ecf0f1;
            text-align: right;
        }

        button.btn-success {
            background-color: #27ae60;
            border-color: #27ae60;
        }

     #partAssurance, #partClient, #total {
         font-weight: bold;
         color: #2c3e50;
         background-color: #ecf0f1;
         text-align: right;
         border-radius: 6px; /* Ajout de bordures arrondies */
         padding: 8px; /* Ajout d'un peu d'espace intérieur */
         box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Ombre légère pour l'effet de relief */
     }

     button.btn-success {
         background-color: #27ae60;
         border-color: #27ae60;
         border-radius: 8px; /* Bordures arrondies */
         padding: 10px 20px; /* Augmentation de la taille du bouton */
         transition: background-color 0.3s ease, transform 0.3s ease; /* Transition douce */
     }

     button.btn-success:hover {
         background-color: #229954; /* Changement de couleur au survol */
         transform: scale(1.05); /* Léger zoom au survol */
     }

     /* Styles pour les champs de texte */
     .custom-input {
         font-weight: bold;
         color: #34495e; /* Couleur bleu-gris */
         background-color: #eaf2f8; /* Bleu très clair */
         border: 2px solid #5dade2; /* Bordure bleu moyen */
         border-radius: 8px; /* Bordures arrondies */
         text-align: right;
         padding: 10px; /* Espace intérieur pour plus de confort */
         box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1); /* Ombre intérieure pour un effet enfoncé */
     }

     /* Style pour le bouton */
     .custom-button {
         font-size: 1rem; /* Taille du texte réduite */
         padding: 8px 16px; /* Taille ajustée pour un bouton plus petit */
         background-color: #3498db; /* Bleu vif */
         border-color: #2980b9; /* Bleu foncé */
         border-radius: 8px; /* Bordures arrondies */
         transition: background-color 0.3s ease, transform 0.3s ease; /* Transition douce */
         align-self: center; /* Alignement centré verticalement */
     }

     .custom-button:hover {
         background-color: #2980b9; /* Couleur de survol */
         transform: scale(1.05); /* Léger zoom au survol */
     }




    </style>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h2>Formulaire de la vente</h2>
                </div>
                <div class="card-body">
                    <form method="post">
                        <div class="form-row">
                            <div class="form-group col-md-12">
                                <label for="searchClient">Rechercher Client</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="searchClient" placeholder="Rechercher par ID, Nom, Prénom" autocomplete="off">
                                    <div class="input-group-append">
                                        <a href="${pageContext.request.contextPath}/module/mycashier/client.form" class="btn btn-outline-secondary" id="addClient">
                                            <i class="bi bi-plus-circle icon-plus"></i> Ajouter Client
                                        </a>
                                    </div>
                                </div>
                                <ul class="list-group" id="clientSearchResults"></ul>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="client">Code Client</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="client" name="client" readonly>
                                    <div class="input-group-append">
                                        <a href="#" class="btn btn-outline-secondary icon-edit-link" id="editClient">
                                            <i class="bi bi-pencil icon-edit"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="nom">Nom</label>
                                <input type="text" class="form-control" id="nom" name="nom" readonly>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="prenom">Prénom</label>
                                <input type="text" class="form-control" id="prenom" name="prenom" readonly>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="dateNaissance">Date de naissance</label>
                                <input type="text" class="form-control" id="dateNaissance" name="dateNaissance" readonly>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="sexe">Sexe</label>
                                <input type="text" class="form-control" id="sexe" name="sexe" readonly>
                            </div>

                            <div class="form-group col-md-3 assurance">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="assurance1Checkbox" name="assurances" value="assurance1">
                                    <label class="form-check-label" for="assurance1Checkbox">Assurance 1</label>
                                </div>
                                <input type="text" class="form-control" id="assurance1" name="assurance1" readonly>
                            </div>

                            <div class="form-group col-md-3 assurance">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="assurance2Checkbox" name="assurances" value="assurance2">
                                    <label class="form-check-label" for="assurance2Checkbox">Assurance 2</label>
                                </div>
                                <input type="text" class="form-control" id="assurance2" name="assurance2" readonly>
                            </div>



                        </div>

                        <hr>
                        <h3>Médicaments</h3>
                        <div class="form-row">
                            <div class="form-group col-md-12">
                                <label for="searchDrug">Rechercher Médicament</label>
                                <input type="text" class="form-control" id="searchDrug" placeholder="Rechercher par Nom, DCI, Groupe thérapeutique">
                                <ul class="list-group" id="drugSearchResults"></ul>
                            </div>
                        </div>
                        <table class="table table-bordered form-table">
                            <thead>
                                <tr>
                                    <th class="medicament-col">Médicament</th>
                                    <th class="quantity-col">Quantité</th>
                                    <th class="pu-col">P.U</th>
                                    <th class="pu-col">Inam</th>
                                    <th class="total-col">Total</th>
                                    <th class="stock-ph-col">Stock PH</th>
                                    <th class="stock-mg-col">Stock MG</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="medicamentTableBody">
                                <!-- Les lignes de médicament apparaîtront ici -->
                            </tbody>
                        </table>

                    </form>
                <div class="row align-items-end mb-3">
                    <div class="col-md-3">
                        <label for="partAssurance" class="form-label">Part Assurance</label>
                        <input type="text" class="form-control custom-input" id="partAssurance" name="partAssurance" readonly>
                    </div>
                    <div class="col-md-3">
                        <label for="partClient" class="form-label">Part Client</label>
                        <input type="text" class="form-control custom-input" id="partClient" name="partClient" readonly>
                    </div>
                    <div class="col-md-3">
                        <label for="total" class="form-label">Total</label>
                        <input type="text" class="form-control custom-input" id="total" name="total" readonly>
                    </div>
                    <div class="col-md-3 d-flex justify-content-end align-items-center">
                        <button type="submit" class="btn btn-primary custom-button">
                            Enregistrer la vente
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.min.js"></script>
<script>
$(document).ready(function() {
    const contextPath = '${pageContext.request.contextPath}';

    // Fonction de recherche de client
    $('#searchClient').on('input', function() {
        var query = $(this).val();
        if (query.length > 2) {
            $.ajax({
                url: contextPath + '/module/mycashier/searchClient.form',
                method: 'GET',
                data: { query: query },
                success: function(response) {
                    $('#clientSearchResults').empty();
                    response.forEach(function(client) {
                        $('#clientSearchResults').append('<li class="list-group-item client-item" data-client=\'' + JSON.stringify(client) + '\'>' + client.id + ' ' + client.name + ' ' + client.firstnames + '</li>');
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Erreur lors de la récupération des clients:', textStatus, errorThrown);
                }
            });
        }
    });

    // Sélection d'un client
    $('#clientSearchResults').on('click', '.client-item', function() {
        var client = $(this).data('client');
        $('#client').val(client.id.toString());
        $('#nom').val(client.name);
        $('#prenom').val(client.firstnames);
        $('#dateNaissance').val(client.birthDate ? new Date(client.birthDate).toISOString().split('T')[0] : '');
        $('#sexe').val(client.sex === 'M' ? 'Masculin' : client.sex === 'F' ? 'Féminin' : '');
        $('#assurance1').val(client.assurance1 || '');
        $('#assurance2').val(client.assurance2 || '');



        $('#clientSearchResults').empty();
    });


    // Gestion des cases à cocher pour qu'une seule soit cochée à la fois
        $('#assurance1Checkbox').on('change', function() {
            if (this.checked) {
                $('#assurance2Checkbox').prop('checked', false);
            }
        });

        $('#assurance2Checkbox').on('change', function() {
            if (this.checked) {
                $('#assurance1Checkbox').prop('checked', false);
            }
        });


    // Redirection vers la page d'édition du client
    $('#editClient').on('click', function() {
        var clientId = $('#client').val();
        if (clientId) {
            window.location.href = contextPath + '/module/mycashier/client.form?id=' + clientId;
        }
    });

    // Fonction de recherche de médicament
    $('#searchDrug').on('input', function() {
        var query = $(this).val();
        if (query.length > 2) {
            $.ajax({
                url: contextPath + '/module/mycashier/searchDrug.form',
                method: 'GET',
                data: { query: query },
                success: function(response) {
                    $('#drugSearchResults').empty();
                    response.forEach(function(drug) {
                        $('#drugSearchResults').append('<li class="list-group-item drug-item" data-drug=\'' + JSON.stringify(drug) + '\'>' + drug.name + ' (' + drug.dci + ')</li>');
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Erreur lors de la récupération des médicaments:', textStatus, errorThrown);
                }
            });
        }
    });

    // Désactiver l'historique de saisie automatique pour le champ de recherche
    $('#searchDrug').attr('autocomplete', 'off');

    // Effacer le contenu du champ de recherche lors du focus
    $('#searchDrug').on('focus', function() {
        $(this).val('');
    });

    // Sélection d'un médicament
    $('#drugSearchResults').on('click', '.drug-item', function() {
        var drug = $(this).data('drug');
        var $newRow = $('<tr></tr>');

        $newRow.append('<td><input type="text" class="form-control" name="drugName" value="' + drug.name + '" readonly></td>');
        $newRow.append('<td><input type="number" class="form-control quantity" name="quantity" value="1"></td>');
        $newRow.append('<td><input type="text" class="form-control" name="pu" value="' + drug.price + '" readonly></td>');
        $newRow.append('<td><input type="text" class="form-control" name="Inam" value="' + drug.baseInam + '" readonly></td>');
        $newRow.append('<td><input type="text" class="form-control total" name="total" value="' + drug.price + '" readonly></td>');
        $newRow.append('<td><input type="text" class="form-control" name="stockPh" value="' + drug.stockLocal + '" readonly></td>');
        $newRow.append('<td><input type="text" class="form-control" name="stockMg" value="' + drug.stockMag + '" readonly></td>');
        $newRow.append('<td><i class="bi bi-eye icon-view"></i> <i class="bi bi-trash icon-delete"></i></td>');

        $('#medicamentTableBody').append($newRow);
        $('#drugSearchResults').empty();
        $('#searchDrug').val('');
        calculateTotals(); // Recalculer les totaux après ajout d'une nouvelle ligne
    });

    // Calculer le total lorsque la quantité est modifiée
    $('#medicamentTableBody').on('input', 'input[name="quantity"]', function() {
        var $row = $(this).closest('tr');
        var quantity = parseFloat($(this).val());
        var price = parseFloat($row.find('input[name="pu"]').val());
        var total = quantity * price;
        $row.find('input[name="total"]').val(total.toFixed(2));
        calculateTotals(); // Recalculer les totaux après modification de la quantité
    });

      function calculateTotals() {
          // Réinitialiser la somme à 0 avant chaque calcul
          let sum = 0;
          let partAssurance = 0;
          let partClient = 0;

          // Récupérer uniquement les champs 'total' visibles dans les lignes de médicaments
          const totalFields = $('#medicamentTableBody input[name="total"]:visible');

          // Calculer la somme totale des champs 'total'
          totalFields.each(function() {
              sum += parseFloat($(this).val()) || 0;
          });

          const inamChecked = $('#assurance1Checkbox').is(':checked');
          const gtaChecked = $('#assurance2Checkbox').is(':checked');

          if (inamChecked) {
              // Réinitialiser la base INAM avant chaque calcul
              let baseInamTotal = 0;

              // Récupérer les champs 'Inam' visibles dans les lignes de médicaments
              $('#medicamentTableBody input[name="Inam"]:visible').each(function() {
                  baseInamTotal += parseFloat($(this).val()) || 0;
              });

              // Calculer Part Assurance comme 80% de la base INAM
              partAssurance = baseInamTotal * 0.8;
          } else if (gtaChecked) {
              // Calculer Part Assurance comme 80% de la somme totale
              partAssurance = sum * 0.8;
          }

          // Calculer Part Client
          partClient = sum - partAssurance;

          // Mettre à jour les champs correspondants
          $('#partAssurance').val(partAssurance.toFixed(2));
          $('#partClient').val(partClient.toFixed(2));
          $('#total').val(sum.toFixed(2));
      }

      // Attacher les événements de changement pour recalculer les totaux
      $('#assurance1Checkbox, #assurance2Checkbox').on('change', calculateTotals);
      $('#medicamentTableBody').on('input', 'input[name="quantity"], input[name="total"], input[name="Inam"]', calculateTotals);

      // Initialiser les valeurs au chargement
      calculateTotals();


    // Supprimer une ligne de médicament
    $('#medicamentTableBody').on('click', '.icon-delete', function() {
        $(this).closest('tr').remove();
        calculateTotals(); // Recalculer les totaux après suppression d'une ligne
    });
});

</script>
</body>
</html>