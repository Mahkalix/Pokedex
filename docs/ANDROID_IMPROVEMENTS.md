# Corrections Android

## Construire et tester

Prérequis : JDK 17, SDK Android 34 et Build Tools 34.0.0. Configurer le chemin SDK dans Android Studio ou dans le fichier local non versionné local.properties.

Commandes :

    ./gradlew testDebugUnitTest assembleDebug
    ./gradlew connectedDebugAndroidTest

Le workflow Android validation exécute les tests JVM et produit un APK debug, puis vérifie la migration SQLite sur un émulateur Android API 24. L’APK debug est destiné aux essais et ne remplace pas une version de production signée.

## Comportements corrigés

- Le catalogue effectue une seule requête de liste, puis charge les détails uniquement à l’ouverture d’une fiche.
- Recherche par nom anglais fourni par PokéAPI, sans distinction de casse ou d’accents, et par numéro (25, 025, #025).
- Message en cas de résultat vide ou d’erreur réseau, avec bouton de nouvelle tentative.
- Annulation des requêtes à la destruction des vues et conservation du texte recherché lors d’une recréation.
- Statistiques affichées une seule fois et barres calculées sur la même échelle (255).
- Photos choisies via le sélecteur de documents avec permission persistante.
- Validation des identifiants et opérations SQLite/hachage en arrière-plan.

## Comptes locaux

La base passe de la version 1 à 2. Les comptes existants sont conservés et leurs mots de passe sont remplacés par des empreintes PBKDF2-HMAC-SHA1 (1 300 000 itérations, sel aléatoire de 16 octets, clé de 256 bits). SHA1 est utilisé dans PBKDF2 pour rester compatible avec le minimum Android API 24. Il ne s’agit pas d’un simple hachage SHA1. Références : https://developer.android.com/reference/javax/crypto/SecretKeyFactory et https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html.

La migration se produit à la première ouverture de la base par l’écran de connexion. Une session déjà ouverte ne déclenche pas cette migration tant que la base n’est pas ouverte. Les mots de passe ne sont plus écrits dans les préférences ; l’ancienne clé est retirée au démarrage des écrans concernés. Les comptes et sessions sont exclus des futures sauvegardes. Cela ne supprime pas les anciennes sauvegardes déjà créées. La migration ne garantit pas l’effacement physique de données anciennement présentes sur le stockage flash.

Les nouveaux mots de passe doivent contenir au moins 8 caractères. Les espaces ne sont plus supprimés. La connexion aux anciens comptes reste possible avec leur mot de passe enregistré.

L’authentification demeure locale à l’appareil, sans serveur ni synchronisation. Elle ne doit pas servir à protéger des données sensibles contre un utilisateur ayant le contrôle de l’appareil.

## Vérification manuelle avant diffusion

1. Installer une ancienne version, créer un compte, puis mettre à jour avec la même signature. Vérifier la connexion et la photo.
2. Activer le mode avion : vérifier le message et le bouton Réessayer, puis rétablir le réseau.
3. Rechercher Pikachu et #025, puis une valeur sans résultat.
4. Ouvrir/fermer rapidement des fiches et tourner l’appareil pendant un chargement.
5. Vérifier la mise en page sur petit écran, grande police, tablette et avec TalkBack.

Une preview Vercel ne peut pas exécuter l’application Android native. Utiliser l’APK debug ou Android Studio.
