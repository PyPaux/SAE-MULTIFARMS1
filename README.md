MultiplicationFarm
===========

Développé par Victor DHORME et Elise LEROY
Contact : victor.dhorme.etu@univ-lille.fr | elise.leroy2.etu@univ-lille.fr

# Présentation de MultiplicationFarm

Logiciel proposant aux éléves de défendre leurs plantations de carotte en résolvant des multiplications.
    -ils devront premièrement créer leur profil sur un jeu MultiplicationFarm (20 profils maximum par jeu)
    -le but principal est de se deplacer sur un champ vu de haut en utilisant les touches : 'z','q','s','d' pour aller en haut, à gauche, en bas et à droite. Pour aller attraper les rongeurs qui veulent détruire vos plantations. A chaque contact avec un rongeur, une multiplication est posé au joueur (avec un temps de réponse pour chaque difficulté : 10 sec pour le mode facile , 7 pour le mode normal et 5 pour le mode difficile). Si le joueur répond mal ou trop tard à la multiplication, il est bloqué pendant 2 tours ce qui laissera le temps aux rongeurs de se déplacer vers les carottes à defendre, OR si il repond bon, le rongeur disparait et le joueur gagne des points.
    - le mode classique qui se joue sur 10 niveaux reprend toutes les tables de multiplications de 1 a 10 
        et le niveau 11 permet au joueur de jouer à un niveau où les multiplications sont générées au hasard entre 1 et 10.
    -Le deuxième mode est le mode "Sans fin", où le but est de survivre le plus longtemps possible, et la partie s'arrête une fois que le joueur est mort.
    -Les points recoltés pendant la partie peuvent être utilisés pour acheter des améliorations dans la boutique, par exemple acheter un chien qui nous aide à attraper les rongeurs, ou acheter des barrières plaçables pour protéger les carottes, améliorer ses bottes pour faire des pas plus grand ou simplement acheter des graines de carotte pour avoir plus de carotte à defendre puis par la suite pouvoir les revendre et avoir plus d'argent.
    -Il est possible d'afficher un classement de tous les joueurs du jeu dans le menu de selection du profil pour donner un aspect de compétition au jeu où le but est de devenir le fermier le plus riche.


# Utilisation de MultiplicationFarm

Afin d'utiliser le projet, il suffit de taper les commandes suivantes dans un terminal :

```
./compile.sh
```
Permet la compilation des fichiers présents dans 'src' et création des fichiers '.class' dans 'classes'

```
./run.sh MultiplicationFarm
```
Permet le lancement du jeu
