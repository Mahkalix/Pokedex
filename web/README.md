# Pokédex web

Version navigateur indépendante de l’application Android, réalisée avec JavaScript et Vite. Les 151 Pokémon de Kanto sont consultables en français, avec recherche par nom français/anglais ou numéro, filtres par type, tri, fiches de statistiques et favoris locaux.

## Développement

Node.js 22.12+ ou 24 recommandé.

```sh
cd web
npm ci
npm run dev
npm test
npm run build
```

## Vercel

- Root Directory : `web`
- Framework : Vite
- Install Command : `npm ci`
- Build Command : `npm run build`
- Output Directory : `dist`
- Aucune variable d’environnement requise.

Les branches de travail servent aux previews. Ne pas fusionner pour simplement consulter une preview.

## Données et limites

`public/pokemon.json` contient un instantané de PokéAPI. Pour l’actualiser : `npm run data:refresh` (réseau nécessaire, 302 requêtes limitées à 8 Pokémon simultanément). Le build et la consultation des textes/statistiques fonctionnent sans requêtes à PokéAPI. Les illustrations sont chargées depuis le dépôt de sprites de PokéAPI et les polices depuis Google Fonts ; ces ressources nécessitent un accès réseau.

Les favoris sont enregistrés dans le navigateur (`localStorage`), sans compte ni synchronisation. Le mode de navigation privée peut les effacer. Ce portage ne reprend pas l’authentification SQLite Android. L’application Android existante stocke des mots de passe en clair : ne pas l’utiliser pour de vrais identifiants sans corriger ce mécanisme.

Illustrations et univers Pokémon © Nintendo / Creatures / GAME FREAK. Projet de fan non affilié. Données : https://pokeapi.co/.
