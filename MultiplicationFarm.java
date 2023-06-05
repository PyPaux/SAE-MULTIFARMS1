import extensions.CSVFile;
import extensions.File;
class MultiplicationFarm extends Program {

    final String FERME = "../ressources/ferme.txt";
    final String RAT = "../ressources/rat.txt";
    final String BOUTIQUE = "../ressources/boutique.txt";
    final String MENUCSV = "../ressources/menu csv.txt";
    final String COMMANDEINGAME = "../ressources/phaseDeJeu.txt";
    final String CHOIXNIVEAU = "../ressources/choixNiveauClassique.txt";
    String DARK_GREEN_BG="\033[48;138;226;52;0m";


//--------------------------------VARIABLES DE JEU---------------------------
    //lecture de sauvegarde
    int nbPiece=0;
    int nbChien=1;
    int nbGraineInventaire=0;
    int nbBarriereInventaire=0;
    int niveauAmeliorateurDePas = 1;
    int dernierNiveauAtteintSansFin=0;
    int carottePlantee=0;

    //Paramètre de jeu
    boolean etatJoueur=true; //true si le joueur n'est pas étourdit
    boolean etatChien=true; //true si le chien n'est pas étourdit
    int debutEtourdissementJoueur=0;
    int debutEtourdissementChien=0;

    //Paramètre de niveau
    boolean quitterLeJeu = false;
    boolean mort = false;
    boolean modeSansFin = false;
    String modeActuel = modeEnString(modeSansFin);
    int niveauModeSansFin = 1;
    int difficulte = 1;
    int tour = 0;
    boolean champModifie = false;
    int nbRongeursPrevus = 0;
    int rongeurRestant=0;
    int choixNiveauCLASSIQUE = 100;
    final int[] TempsDeReponse = new int[]{100000321,10000,7000,5000};

    //Coordonnées initiale dans le champs
    final int IDX_L_INITIAL_JOUEUR=4;
    final int IDX_C_INITIAL_JOUEUR=4;
    final int IDX_L_INITIAL_CHIEN=4;
    final int IDX_C_INITIAL_CHIEN=5;

    //coordonnées du chien en jeu
    int idxLChien=IDX_L_INITIAL_CHIEN;
    int idxCChien=IDX_C_INITIAL_CHIEN;
    int idxLJoueur=IDX_L_INITIAL_JOUEUR;
    int idxCJoueur=IDX_C_INITIAL_JOUEUR;

    //---------------------------------------------------------------------------------------FICHIER CSV----------------------------------------------------------------------------

    String[][] CSVtoString(CSVFile file){
        String[][] tab = new String [rowCount(file)][columnCount(file)];
        for(int idxL = 0;idxL < length(tab,1);idxL ++){
            for(int idxC = 0;idxC < length(tab,2);idxC ++){
                tab[idxL][idxC] = getCell(file,idxL,idxC);
            }
        }
        return tab;
    }

    boolean nomDejaPris(CSVFile file,String nom){
        String[][] stats = CSVtoString(file);
        for(int idxL = 0;idxL < length(stats,1);idxL ++){
            if(equals(stats[idxL][1],nom)){
                return true;
            }
        }
        return false;
    }

    boolean ajouterProfil(String filename,CSVFile file){
        String[] nombre = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
        String[][] tab = CSVtoString(file);
        int nb = 0;
        if(length(tab,1)<20){
            String[][] resultat = new String[length(tab,1)+1][length(tab,2)];
            for(int idxL = 0;idxL < length(resultat,1);idxL ++){
                for(int idxC = 0;idxC < length(resultat,2);idxC ++){
                    if(idxL < length(resultat,1)-1){
                        resultat[idxL][idxC] = tab[idxL][idxC];
                        if(idxL > 0 && idxC == 0){
                            nb = stringToInt(tab[idxL][idxC]);
                        }
                    }else{
                        if(idxC == 0){
                            resultat[idxL][0] = nombre[nb];
                        }else if(idxC == 1){
                            espacement();
                            println("Entre ton "+resultat[0][idxC]+" :");
                            String var = readString();
                            while(nomDejaPris(file,var)){
                                espacement();
                                println("Nom deja utilise !");
                                println("Entre ton "+resultat[0][idxC]+" :");
                                var = readString();
                            }
                            resultat[idxL][idxC] = var;
                        }else if(idxC == 2){
                            resultat[idxL][idxC] = "0";
                        }else if(idxC == 3){
                            boolean bon = false;
                            String choixPerso ="";
                            while(!bon){
                                espacement();
                                println("Difficulte :");
                                println(ANSI_GREEN+"1. facile"+ANSI_RESET);
                                println(ANSI_YELLOW+"2. intermediaire"+ANSI_RESET);
                                println(ANSI_RED+"3. difficile"+ANSI_RESET);
                                println();
                                choixPerso = readString();
                                if(equals(choixPerso,"1")||equals(choixPerso,"2")||equals(choixPerso,"3")){
                                    bon = true;
                                } 
                            }
                            resultat[idxL][idxC] =  choixPerso;
                        }else if(idxC == 4){
                            resultat[idxL][idxC] =  "1";
                        }else if(idxC == 5){
                            resultat[idxL][idxC] =  "1";
                        }else if(idxC == 6){
                            resultat[idxL][idxC] =  "0";
                        }else if(idxC == 7){
                            resultat[idxL][idxC] =  "0";
                        }else if(idxC == 8){
                            resultat[idxL][idxC] =  "0";
                        }else if(idxC == 9){
                            resultat[idxL][idxC] =  "1";
                        }else{
                            resultat[idxL][idxC] ="1";
                        }
                        
                    }
                }
            }
            saveCSV(resultat,filename);
            return true;
        }else{
            return false;
        }
    }

    void afficherCSV(CSVFile file){
        for(int idxL = 0;idxL < rowCount(file);idxL ++){
            if(idxL > 0){
                print(ANSI_RED+(idxL)+". ");
            }
            for(int idxC = 0;idxC < columnCount(file);idxC ++){
                print(ANSI_RESET+getCell(file,idxL,idxC)+" ");
            }
            println();
        }
    }

    void affichagePartieCSV(CSVFile file){
        String diff = "";
        for(int idxL = 0;idxL < rowCount(file);idxL ++){
            if(idxL > 0){
                print(ANSI_RED+(idxL)+". ");
                for(int idxC = 0;idxC < columnCount(file);idxC ++){
                    if(idxC != 0 && idxC<= 4){
                        if(idxC == 2){
                            print(ANSI_RESET+" Pieces : "+getCell(file,idxL,idxC)+" |");
                        }else if(idxC == 3){
                            diff = "";
                            print(ANSI_RESET+" Difficultee : ");
                            if(equals(getCell(file,idxL,idxC),"3")){
                                diff = "Difficile";
                            }else if(equals(getCell(file,idxL,idxC),"2")){
                                diff = "Normal";
                            }else if(equals(getCell(file,idxL,idxC),"1")){
                                diff = "Facile";
                            }
                            print(diff+" |");
                        }else if(idxC == 4){
                            print(ANSI_RESET+" Niveau : "+getCell(file,idxL,idxC)+" ");
                        }else{
                            print(ANSI_RESET+" "+getCell(file,idxL,idxC)+" |");
                        }
                        
                    }
                }
                println();
                println();
            }
        }
    }

    boolean premierePartie(CSVFile file,int perso){
        if(equals(getCell(file,perso,10),"1")){
            return true;
        }else{
            return false;
        }
    }

    void plusLaPremierePartie(String filename,CSVFile file,int perso){
        String[][] tab = CSVtoString(file);
        tab[perso][10] = "0";
        saveCSV(tab,filename);
    }

    void sauvegarderPlacable(String filename,CSVFile LastFile, Case[][] map,int ChoixPerso){
        String[][] ancien = CSVtoString(LastFile);
        String[][] tr1 = new String[length(ancien,1)-nombreDePlacableDansUnTableau(LastFile,ChoixPerso)][4];
        int FileLigne = 0;
        boolean bon = false;
        for(int idxL = 0;idxL < length(ancien,1);idxL ++){
            bon = false;
            for(int idxC = 0;idxC < length(ancien,2);idxC ++){
                if(!equals(ancien[idxL][0],ChoixPerso+"")){
                    tr1[FileLigne][idxC] = ancien[idxL][idxC];
                    bon = true;
                }
            }
            if(bon){
                FileLigne ++;
            }
        }
        String[][] resultat = new String[length(tr1)+nombreDePlacable(map)][4];
        FileLigne = length(tr1);
        bon = false;
        for(int idxL = 0;idxL < length(tr1,1);idxL ++){
            for(int idxC = 0;idxC < length(tr1,2);idxC ++){
                resultat[idxL][idxC] = tr1 [idxL][idxC];
            }
        }
        for(int idxL = 0;idxL < length(map,1);idxL ++){
                for(int idxC = 0;idxC < length(map,2);idxC ++){
                    bon = false;
                    if(estCarotte(map[idxL][idxC])){
                        resultat[FileLigne][0] = ChoixPerso+"";
                        resultat[FileLigne][1] = "C";
                        resultat[FileLigne][2] = idxC+"";
                        resultat[FileLigne][3] = idxL+"";
                        bon = true;
                    }else if(estBarriere(map[idxL][idxC])){
                        resultat[FileLigne][0] = ChoixPerso+"";
                        resultat[FileLigne][1] = "B";
                        resultat[FileLigne][2] = idxC+"";
                        resultat[FileLigne][3] = idxL+"";
                        bon = true;
                    }else if(estGraine(map[idxL][idxC])){
                        resultat[FileLigne][0] = ChoixPerso+"";
                        resultat[FileLigne][1] = "G";
                        resultat[FileLigne][2] = idxC+"";
                        resultat[FileLigne][3] = idxL+"";
                        bon = true;

                    }else{

                    }
                    if(bon){
                    FileLigne ++;
                    }
                
                }
        }
            saveCSV(resultat,filename);
    }

    int nombreDePlacableDansUnTableau(CSVFile file,int perso){
        String[][] placable = CSVtoString(file);
        int nb = 0;
        for(int idxL = 0;idxL < length(placable);idxL ++){
            if(equals(placable[idxL][0],perso+"")){
                nb ++;
            }
        }
        return nb;
    }

    int nombreDePlacable(Case[][]map){
        int nb = 0;
        for(int idxL = 0;idxL < length(map,1);idxL ++){
            for(int idxC = 0;idxC <length(map,2);idxC ++){
                if(estBarriere(map[idxL][idxC]) || estCarotte(map[idxL][idxC]) || estGraine(map[idxL][idxC])){
                    nb ++;
                }
            }
        }
        return nb;
    }

    void affichageTXT(String Filename){
        File f = newFile(Filename);
        while(ready(f)){ 
            println(readLine(f));
        }
    }
    


    Case[][] chargerDerniereMap(CSVFile file ,int choixPerso){
        String[][] obj = CSVtoString(file);
        Case[][] resultat = new Case[15][9];
        //------------------------------------
        for(int idxL= 0;idxL < length(resultat,1);idxL++){
            for(int idxC = 0 ; idxC < length(resultat,2);idxC ++){
                resultat [idxL][idxC] = newCase(Entite.VIDE,0);
            }
        }
        //------------------------------------
        for(int idxL = 0;idxL < length(obj);idxL ++){
            if(equals(obj[idxL][0], choixPerso+"")){
                if(equals(obj[idxL][1], "C")){
                    resultat[stringToInt(obj[idxL][3])][stringToInt(obj[idxL][2])] = newCase(Entite.CAROTTE,3);
                }else if(equals(obj[idxL][1], "B")){
                    resultat[stringToInt(obj[idxL][3])][stringToInt(obj[idxL][2])] = newCase(Entite.BARRIERE,3);
                }else{
                    resultat[stringToInt(obj[idxL][3])][stringToInt(obj[idxL][2])] = newCase(Entite.GRAINE,3);
                }              
            }
        }
        return resultat;
    }

    int recupNiveau(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][4]); 
            }
        } 
        return 0;
    }

    int recupPoints(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][2]); 
            }
        } 
        return 0;
    }

    int recupGraine(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][6]); 
            }
        } 
        return 0;
    }

    int recupTaillePas(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][9]); 
            }
        } 
        return 0;
    }

    int recupNBcarotte(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][5]); 
            }
        } 
        return 0;
    }

    int recupNBChien(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][7]); 
            }
        } 
        return 0;
    }

    int recupNBBarriere(CSVFile file,int choixPerso){
        String[][] stats = CSVtoString(file);
        for(int idxL =0;idxL < length(stats,1);idxL ++){
            if(idxL == choixPerso){
               return stringToInt(stats[idxL][8]); 
            }
        } 
        return 0;
    }
    
    int recupDifficulte(CSVFile file1,int choixPerso){
        String[][] stats = CSVtoString(file1);
        return stringToInt(stats[choixPerso][3]);
    }

    void sauvegardeDesStats(CSVFile file,String filename , int choixPerso,int nbGraineInventaire,int nbPiece,int niveau , int niveauAmeliorateurDePas,int NBcarotte , int nbChien,int nbBarriere){
        String [][] joueurs = CSVtoString(file);
        joueurs [choixPerso][2] = nbPiece+"";
        joueurs [choixPerso][4] = dernierNiveauAtteintSansFin+"";
        joueurs [choixPerso][5] = carottePlantee+"";
        joueurs [choixPerso][6] = nbGraineInventaire+"";
        joueurs [choixPerso][7] = nbChien+"";
        joueurs [choixPerso][8] = nbBarriereInventaire+"";
        joueurs [choixPerso][9] = niveauAmeliorateurDePas+"";
        saveCSV(joueurs,"../ressources/"+filename); 
    }

    void affichageDuClassement(CSVFile file){
        String[][] stats = CSVtoString(file);
        int[] nbPiece = new int[length(stats,1)-1];
        for(int idxC = 1 ;idxC < length(stats,1);idxC ++){
            nbPiece[idxC-1] = stringToInt(stats[idxC][2]);
            
        }
        int minIndex;
        int temp;
        for (int i = 0; i < length(nbPiece) - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < length(nbPiece); j++) {
                if (nbPiece[j] > nbPiece[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                temp = nbPiece[i];
                nbPiece[i] = nbPiece[minIndex];
                nbPiece[minIndex] = temp;
            }
        }
        for (int i = 0; i < length(nbPiece); i++) {
            print(nbPiece[i]+ " " ); 
            nomAvecPoints(file,nbPiece[i]);
            println();
        }
    }

    void nomAvecPoints(CSVFile joueurs ,int nbPiece){
        String[][] stats = CSVtoString(joueurs);
        for(int idxL = 1;idxL < length(stats,1);idxL ++){
            if(stringToInt(stats[idxL][2]) == nbPiece){
                print(stats[idxL][1]+" ");
            }
        }
    }

    boolean tailleSuffisantepourClassement(CSVFile file){
        String [][] stats = CSVtoString(file);
        if(length(stats,1)>1){
            return true;
        }else{
            return false;
        }
    }

    String[][] rempliDeVide(int longueur){
        String var = "VIDE";
        String[][] tabVide = new String[longueur][2];
        for(int idxL = 0;idxL < length(tabVide,1);idxL ++){
            for(int idxC = 0;idxC < length(tabVide,2);idxC ++){
                tabVide[idxL][idxC] = var;
            }
        }
        return tabVide;
    }

    int graineEnCarotte(Case[][] map){
        int nb = 0;
        for(int idxL = 0;idxL < length(map,1);idxL ++){
            for(int idxC = 0;idxC < length(map,2);idxC ++){
                if(estGraine(map[idxL][idxC])){
                    map[idxL][idxC] = newCase(Entite.CAROTTE,3);
                    nb ++;
                }
            }
        }
        nbGraineInventaire = nbGraineInventaire - nb;
        return nb;
    }


    //---------------------------------------------------------------------------------------------FIN FICHIER CSV---------------------------------------------------------------------------
    
    
    //--------------------------------------------------------------------------------------FONCTIONS DE MISES EN PAGES---------------------------------------------------------------------------------

    String affichageSansFin(boolean etat){
        if(etat){
            return ANSI_GREEN+"ON"+ANSI_RESET;
        }else{
            return ANSI_RED+"OFF"+ANSI_RESET;
        }
    }

    String affichageChienOuPas(int nbChien){
        if(nbChien == 1){
            return "OUI";
        }else{
            return "NON";
        }
    }

    boolean toggleModeSansFin(boolean modeSansFin){
        if(modeSansFin){
            return false;
        }else{
            return true;
        }
    }

    String affichageNomDuProfil(CSVFile file,int choixPerso){
        String[][] joueurs = CSVtoString(file);
        for(int idxL = 0;idxL < length(joueurs,1);idxL ++){
            if(equals(joueurs[idxL][0],choixPerso+"")){
                return joueurs[idxL][1];
            }
        }
        return "";
    }

    void affichageMenuTexte(boolean SANSFIN,int nbPiece,int dernierNiveauAtteintSansFin,int carottePlantee,int nbChien,int choixPerso,CSVFile file){
        println("Profil : "+affichageNomDuProfil(file,choixPerso));
        println("|Points total : "+nbPiece+" |Niveau (mode Sans Fin) : "+dernierNiveauAtteintSansFin+" |Nombre de carotte : "+carottePlantee+ " |Chien : "+affichageChienOuPas(nbChien)+"|");
        println();
        println("1 - Jouer une partie");
        println("2 - Mode sans fin : "+affichageSansFin(SANSFIN));
        println("3 - Aller a la boutique");
        println("4 - Revenir au choix des profils");
    }

    void espacement(){
        for(int cpt = 0;cpt < 50;cpt ++){
            println("");
        }
    }


    //Pour les dialogues de la fonction phaseDeJeu()
    void bibliothequeDeDialogue(String localisation, boolean erreur){
        String dialogue = "";               
        if(equals(localisation,"phaseDeJeu") && erreur==false){
            dialogue = "| x - quitter                                               |\n"+
                       "|                                                           |\n"+
                       "|                    Commandes :                            |\n"+
                       "|  z - haut       s - bas          m-modifier taille de pas |\n"+
                       "|  q - gauche     d - droite                                |\n"+
                       "|                                                           |\n";
        }else if(equals(localisation, "perdu")){
            dialogue = "               Terminé  !             \n";
        }else if(equals(localisation, "gagne")){
            dialogue = "               Gagné !             \n";
        }else if(equals(localisation, "deplacementJoueur")){
            dialogue = "           A toi de jouer !        \n";
        }else if(equals(localisation, "modificationTailleDePas")){
            dialogue = " Entre la taille de pas que tu souhaite :        \n";
        }else if(equals(localisation, "phaseDeJeu") && erreur==true){
            dialogue = dialogue + "\n   Hum... ça ne va pas le faire        \n";
        }
        println(dialogue);     
    }

    void affichageChamp(Case[][] champ, String niveau, int tour){
        
        if(modeSansFin){
            println("           niveau : "+dernierNiveauAtteintSansFin+"       ");
        }else{
            println("     niveau : La table de "+choixNiveauCLASSIQUE+"       ");
        }
        println("            tour : "+tour+"       ");
        println(DARK_GREEN_BG+ANSI_FAINT+" - - - - - - - - - - - - - - "+ ANSI_BG_DEFAULT_COLOR);
        for(int idxL=0; idxL<length(champ,1); idxL++){
            print(DARK_GREEN_BG+" |"+ ANSI_BG_DEFAULT_COLOR);
            for(int idxC=0; idxC<length(champ,2); idxC++){
                print(DARK_GREEN_BG + ANSI_FAINT + caseToString(champ[idxL][idxC])+" |" + ANSI_BG_DEFAULT_COLOR);
            }
            println("\n"+DARK_GREEN_BG+ ANSI_FAINT+" - - - - - - - - - - - - - - " + ANSI_BG_DEFAULT_COLOR);                
        }
        println(DARK_GREEN_BG+ANSI_RED+"        ^            ^       "+ ANSI_RESET);
    }

    void affichageChampMenuPlacable(Case[][] champ){
        println("-------------------------------------");
        for(int cpt1 = 0 ; cpt1 < length(champ,1);cpt1 ++){
            print("| ");
            for(int cpt2 = 0; cpt2 < length(champ,2);cpt2 ++){
                if(cpt1 == 4 && cpt2 == 4){
                    print(ANSI_RED+caseToString(champ[cpt1][cpt2])+ANSI_RESET+" | ");
                }else if(cpt1 == 14){
                    print(ANSI_RED+caseToString(champ[cpt1][cpt2])+ANSI_RESET+" | ");
                }else if(cpt1 == 4 &&  cpt2 == 5){
                    print(ANSI_RED+caseToString(champ[cpt1][cpt2])+ANSI_RESET+" | ");
                }else{
                    print(caseToString(champ[cpt1][cpt2])+ANSI_RESET+" | ");
                }
            }
            println(" ");
            println("-------------------------------------");
        }
    }








//-------------------------------------------------------------Fonction d'initialisation du champ---------------------------------------------------------------------------------

    void spawnDuJoueur(Case[][] champ){
        champ[IDX_L_INITIAL_JOUEUR][IDX_C_INITIAL_JOUEUR] = newCase("JOUEUR");
    }

    void spawnDuChien(Case[][] champ){
        champ[IDX_L_INITIAL_CHIEN][IDX_C_INITIAL_CHIEN] = newCase("CHIEN");
    }

    String caseToString(Case c){
        String contenuCase = "";
        if(c.type==Entite.VIDE){
            contenuCase = " ";
        }else if(c.type==Entite.CAROTTE){
            contenuCase = couleurSelonPV(c);
        }else if(c.type==Entite.RONGEUR){
            contenuCase = ANSI_RED+"!";
        }else if(c.type==Entite.JOUEUR){
            contenuCase = ANSI_BLUE+"J";
        }else if(c.type==Entite.GRAINE){
            contenuCase = ".";
        }else if(c.type==Entite.CHIEN){
            contenuCase = "#";
        }else if(c.type==Entite.BARRIERE){
            contenuCase = couleurSelonPV(c);
        }
        return ANSI_BOLD+ contenuCase +ANSI_RESET+DARK_GREEN_BG+ANSI_FAINT;
    }

    String couleurSelonPV(Case c){
        final String ANSI_RESET = "\u001B[0m";
        final String ORANGE = "\u001B[38;5;208m";
        String charEnCouleur="";
        if(c.type==Entite.CAROTTE){
            if(c.pv==3){
                charEnCouleur= ORANGE+"C";
            }else if(c.pv==2){
                charEnCouleur= ANSI_YELLOW+"C";
            }else{
                charEnCouleur= ANSI_RED+"C";
            }
        }else if(c.type==Entite.BARRIERE){
            if(c.pv==3){
                charEnCouleur= ANSI_BLACK+"=";
            }else if(c.pv==2){
                charEnCouleur= ANSI_YELLOW+"=";
            }else{
                charEnCouleur= ANSI_RED+"=";
            }
        }
        return charEnCouleur +ANSI_RESET+DARK_GREEN_BG+ANSI_FAINT;
    }










//------------------------------------------------------------Fonction d'initialisation des entités------------------------------------------------------------------------------
    
    
    Case newCase(String typeDeCase){
        if(typeDeCase=="VIDE"){
            return newVide();
        }else if(typeDeCase=="CAROTTE"){
            return newCarotte();
        }else if(typeDeCase=="RONGEUR"){
            return newRongeur();
        }else if(typeDeCase=="JOUEUR"){
            return newJoueur();
        }else if(typeDeCase=="GRAINE"){
            return newGraine();
        }else if(typeDeCase=="CHIEN"){
            return newChien();
        }else if(typeDeCase=="BARRIERE"){
            return newBarriere();
        }
        return newVide();
    }

    Case newVide(){
        Case c = new Case();
        c.pv=0;
        c.type=Entite.VIDE;
        return c;
    }

    Case newCarotte(){
        Case c = new Case();
        c.pv=3;
        c.type=Entite.CAROTTE;
        return c;
    }

    Case newRongeur(){
        Case c = new Case();
        c.pv=1;
        c.type=Entite.RONGEUR;
        return c;
    }

    Case newJoueur(){
        Case c = new Case();
        c.pv=0;
        c.type=Entite.JOUEUR;
        return c;
    }

    Case newGraine(){
        Case c = new Case();
        c.pv=1;
        c.type=Entite.GRAINE;
        return c;
    }

    Case newChien(){
        Case c = new Case();
        c.pv=0;
        c.type=Entite.CHIEN;
        return c;
    }

    Case newBarriere(){
        Case c = new Case();
        c.pv=3;
        c.type=Entite.BARRIERE;
        return c;
    }

    //--------------------FIN DES FONCTIONS DE MISES EN PAGES----------------------------
    //--------------------FONCTIONS DE JEUX------------------------
        //-------------TEST-----------------

    boolean estBarriere(Case e){
        Case entite = e;
        if(entite.type == Entite.BARRIERE){
            return true;
        }else{
            return false;
        }
    }

    boolean estVide(Case c){
        boolean vide = false;
        if(c.type==Entite.VIDE){
            vide = true;
        }
        return vide;
    }

    boolean estRongeur(Case c){
        boolean rongeur = false;
        if(c.type==Entite.RONGEUR){
            rongeur = true;
        }
        return rongeur;
    }

    boolean estGraine(Case c){
        boolean graine = false;
        if(c.type==Entite.GRAINE){
            graine = true;
        }
        return graine;
    }

    boolean estCarotte(Case c){
        boolean carotte = false;
        if(c.type==Entite.CAROTTE){
            carotte = true;
        }
        return carotte;
    }

    boolean estChien(Case c){
        boolean chien = false;
        if(c.type==Entite.CHIEN){
            chien = true;
        }
        return chien;
    }


    int NBDeCarotte (Case[][] champ){
        int nb = 0;
        for(int idxL = 0;idxL < length(champ,1);idxL ++){
            for(int idxC = 0;idxC < length(champ,2);idxC ++){
                if(estCarotte(champ[idxL][idxC])){
                    nb = nb + 1;
                }
            }
        }
        return nb;
    }

//--------------------FIN DE TESTS-----------------------

//--------------------GAMEPLAY-----------------------

void phaseDeJeu(Case[][] champ){ //récupère la donnée du niveau "Classique 1" ou "Mode Sans Fin"
        String localisation = "phaseDeJeu";
        boolean perdu = false;
        boolean gagne = false;
        quitterLeJeu = false;
        mort = false;
        spawnDuJoueur(champ);
        if(nbChien == 1){
            spawnDuChien(champ);
        }
        while(perdu==false && gagne==false){
            idxLChien=IDX_L_INITIAL_CHIEN;
            idxCChien=IDX_C_INITIAL_CHIEN;
            nbRongeursPrevus = rongeursPrevusAuNiveau();
            rongeurRestant=nbRongeursPrevus;
            
            while(rongeurRestant>0 && carottePlantee>0 && quitterLeJeu==false){
                tour = tour + 1;
                etatJoueurEtChien();
                espacement();
                affichageChamp(champ, modeActuel, tour);
                bibliothequeDeDialogue(localisation,false);
                if(nbRongeursPrevus>0){
                    champ = apparitionRongeur(champ, tour);
                    espacement();
                    affichageChamp(champ, modeActuel, tour);
                }
                champ = selectionRongeurADeplacer(champ);
                if(carottePlantee>0 && rongeurRestant>0){  
                    espacement();        
                    affichageChamp(champ, modeActuel, tour);
                    if(nbChien>0 && etatChien==true){
                        champ = deplacementAutomatiqueChien(champ);    
                    }
                    if(etatJoueur == true){
                        champModifie = false;
                        champ = deplacementJoueur(champ);
                        espacement();
                        affichageChamp(champ, modeActuel, tour);
                        delay(500);
                    }
                }
                
                if(carottePlantee==0 || quitterLeJeu==true){
                    if(carottePlantee == 0){
                        dernierNiveauAtteintSansFin = 1;
                    }
                    perdu = true;
                    
                    mort = true;
                    quitterLeJeu = true;
                    rongeurRestant = 0;
                    bibliothequeDeDialogue("perdu",false);
                    delay(2000);
                }else if(rongeurRestant==0){
                    gagne = true;
                    bibliothequeDeDialogue("gagne",false);
                    delay(2000);
                }
                if(modeSansFin && tour%10 == 0){
                dernierNiveauAtteintSansFin = dernierNiveauAtteintSansFin + 1;
                }
            }
            
            
        }
    }

    void etatJoueurEtChien(){
        if(etatJoueur==false && debutEtourdissementJoueur==tour-2){
            etatJoueur=true;
        }
        if(etatChien==false && debutEtourdissementChien==tour-2){
            etatChien=true;
        }
    }


    //---------------------------------------------------fonction en rapport avec le déplacemnt du joueur-----------------------------------------------------------
    
    
 //fonction en rapport avec le déplacement du joueur
    Case[][] deplacementJoueur(Case[][] champ){
        int tailleDePasVariable = 0;
        char choixPas=']';
        char direction=']'; //pour éviter erreur initialisation à vide
        String localisation="phaseDeJeu";
        champModifie = false;
        
        bibliothequeDeDialogue(localisation, false);

        tailleDePasVariable = 1; //le joueur aura par défaut une taille de pas à 1
        while(champModifie == false){
            direction=readBetterChar();
            //si les indices ne dépassent pas du tableau, des tentatives de déplacement seront faites
            if(direction=='z' || direction=='s' || direction=='q' || direction=='d'){
                if((direction=='z' && (idxLJoueur-tailleDePasVariable<0)) || (direction=='s' && (idxLJoueur+tailleDePasVariable>14)) || (direction=='q' && (idxCJoueur-tailleDePasVariable<0)) || (direction=='d' && (idxCJoueur+tailleDePasVariable>8))){     
                    tailleDePasVariable = modificationTailleDePasVariable(champ, direction, tailleDePasVariable);
                }
                if(tailleDePasVariable==0){
                    localisation="phaseDeJeu";
                    espacement();
                    affichageChamp(champ, modeActuel, tour);
                    bibliothequeDeDialogue(localisation, true);
                    tailleDePasVariable=1;
                }else if((direction=='z' && (idxLJoueur-tailleDePasVariable>=0)) || (direction=='s' && (idxLJoueur+tailleDePasVariable<=14)) || (direction=='q' && (idxCJoueur-tailleDePasVariable>=0)) || (direction=='d' && (idxCJoueur+tailleDePasVariable<=8))){
                    if(direction=='z'){
                        champ = regleDeplacementJoueur(champ, idxLJoueur-tailleDePasVariable, idxCJoueur, direction, tailleDePasVariable);
                    }else if(direction=='s'){
                        champ = regleDeplacementJoueur(champ, idxLJoueur+tailleDePasVariable, idxCJoueur, direction, tailleDePasVariable);
                    }else if(direction=='q'){
                        champ = regleDeplacementJoueur(champ, idxLJoueur, idxCJoueur-tailleDePasVariable, direction, tailleDePasVariable);
                    }else if(direction=='d'){
                        champ = regleDeplacementJoueur(champ, idxLJoueur, idxCJoueur+tailleDePasVariable, direction, tailleDePasVariable);
                    }
                }
            }else if(direction=='m'){ //choix de modification de la taille de pas selon ce qui a été débloqué
                localisation="modificationTailleDePas";
                espacement();
                affichageChamp(champ, modeActuel, tour);
                bibliothequeDeDialogue(localisation, false); 
                println("tu as le choix entre : "+ impressionPossibiliteTailleDePas());
                choixPas = readBetterChar();
                if(choixPas=='1'){
                    tailleDePasVariable = 1;
                }else if(choixPas=='2' && niveauAmeliorateurDePas>=2){
                    tailleDePasVariable = 2;
                }else if(choixPas=='3' && niveauAmeliorateurDePas==3){
                    tailleDePasVariable = 3;
                }else{
                    localisation="phaseDeJeu";
                    espacement();
                    affichageChamp(champ, modeActuel, tour);
                    bibliothequeDeDialogue(localisation, true); //message d'erreur de saisie ou compatibilté de la taille de pas selectionnée
                    delay(1500);
                }
                localisation="phaseDeJeu";
                espacement();
                affichageChamp(champ, modeActuel, tour);
                bibliothequeDeDialogue(localisation, false);
            }else if(direction=='t'){ //pour sauter son tour
                return champ;
            }else if(direction=='x'){ //pour quitter le niveau sans sauvegarder
                quitterLeJeu = true;
                return champ;
            }else{
                espacement();
                affichageChamp(champ, modeActuel, tour);
                bibliothequeDeDialogue(localisation, true); //message d'erreur de saisie de la direction
                delay(1500);
                espacement();
                localisation="phaseDeJeu";
                affichageChamp(champ, modeActuel, tour);
                bibliothequeDeDialogue(localisation, false);
            }
        }
        return champ;
    }

    //donne la selection des tailles de pas disponible selon ce qui a été débloqué
    String impressionPossibiliteTailleDePas(){
        String possibiliteTailleDePas = "";
        for(int cpt=1; cpt<=niveauAmeliorateurDePas; cpt++){
            possibiliteTailleDePas = possibiliteTailleDePas + cpt + " ";
        }
        return possibiliteTailleDePas;
    }

    //si la taille de pas fait sortir le joueur du tableau, la taille de pas sera réduite en conséquence
    int modificationTailleDePasVariable(Case[][] champ,  char direction, int tailleDePasVariable){ 
        boolean tailleDePasVariableInvalide = true;
        while(tailleDePasVariableInvalide==true){
            while((direction=='z' && (idxLJoueur-tailleDePasVariable<0)) || (direction=='s' && (idxLJoueur+tailleDePasVariable>14)) || (direction=='q' && (idxCJoueur-tailleDePasVariable<0)) || (direction=='d' && (idxCJoueur+tailleDePasVariable>8))){
                tailleDePasVariable=tailleDePasVariable-1;
            }     
            tailleDePasVariableInvalide = false;  
        }       
        return tailleDePasVariable;
    }



    //les règles régissant les déplacements du joueur
    Case[][] regleDeplacementJoueur(Case[][] champ, int idxLDir, int idxCDir, char direction, int tailleDePasVariable){
        int cpt=0;
        int[] coordRongeur = new int[]{-1, -1};
        boolean deplacementPossible = false;
            while(deplacementPossible==false && cpt<3){
                if(champ[idxLDir][idxCDir].type==Entite.VIDE){
                    deplacementPossible=true;
                    champModifie = true;
                    champ[idxLDir][idxCDir]=champ[idxLJoueur][idxCJoueur];
                    champ[idxLJoueur][idxCJoueur]=newCase("VIDE");
                    idxLJoueur=idxLDir;
                    idxCJoueur=idxCDir;
                }else if(champ[idxLDir][idxCDir].type==Entite.GRAINE){
                    deplacementPossible = false;
                }else if(champ[idxLDir][idxCDir].type==Entite.CAROTTE){
                    deplacementPossible = false;
                }else if(champ[idxLDir][idxCDir].type==Entite.CHIEN){
                    deplacementPossible = false;
                }else if(champ[idxLDir][idxCDir].type==Entite.BARRIERE){
                    deplacementPossible = false;
                }else if(champ[idxLDir][idxCDir].type==Entite.RONGEUR){
                    deplacementPossible = true;
                    champModifie = true;
                    champ = rencontreJoueurVersRongeur(champ, idxLDir, idxCDir);
                if(deplacementPossible == false && tailleDePasVariable>1){  //si la taille de pas est supérieure à 1, des tentatives de déplacement seront faites dans la direction souhaitée en retirant à chaque fois une taille de pas
                    if(direction =='z'){           
                        idxLDir=idxLDir+1;
                    }else if(direction =='s'){ 
                        idxLDir=idxLDir-1;
                    }else if(direction == 'q'){ 
                        idxCDir=idxCDir+1;
                    }else if(direction == 'd'){ 
                        idxCDir=idxCDir-1;
                    }
                    cpt++;
                }else{
                    deplacementPossible = true;
                }
            }
        }
        return champ;
    }









//------------------------------------------------------------------Fonctions en rapport avec l'apparition des rongeurs -----------------------------------------------------------------------------


    Case[][] apparitionRongeur(Case[][] champ, int tour){
        if(rongeursPrevusAuTour(tour)==1){
            if((int)(random()*10)>=5 && estVide(champ[14][2])){  //choisi au hasard la case d'apparition entre celle à gauche et à droite si elles sont vides
                champ[14][2]= newCase("RONGEUR");
            }else if(estVide(champ[14][6])){
                champ[14][6]= newCase("RONGEUR");
            }
            nbRongeursPrevus--;    //décrémente les rongeurs prévus
        }else if(rongeursPrevusAuTour(tour)==2 && estVide(champ[14][2]) && estVide(champ[14][6])){
            champ[14][2]= newCase("RONGEUR");
            champ[14][6]= newCase("RONGEUR");
            nbRongeursPrevus=nbRongeursPrevus-2;
        }
        return champ;
    }

    //si un rongeur est prévu d'apparaitre au tour
    int rongeursPrevusAuTour(int tour){
        if(tour==1){
            return 1;
        }else if(difficulte==1 && tour%2==0){
            return 0;
        }else if(difficulte==3 && tour%2==0){
            return 2;
        }else if(difficulte==2 && tour%3==0){
            return 2;
        }else if(difficulte==2 && tour%4==0){
            return 0;
        }else{
            return 1;
        }
    }

    //le nombre de rongeurs prévus dans tout le niveau
    int rongeursPrevusAuNiveau(){
        int nbRongeursPrevus = 0;
        double multiplicateurSelonDifficulte = 1;
        int nbSelonNiveau = dernierNiveauAtteintSansFin;

        if(dernierNiveauAtteintSansFin>15){
            nbSelonNiveau = 15;  //dans le mode sans fin il n'y aura pas plus de 15 rongeurs par niveau
        }

        if (difficulte == 1){
            multiplicateurSelonDifficulte = 1;
        }else if(difficulte == 2){
            multiplicateurSelonDifficulte = 1.5;
        }else if(difficulte == 3){
            multiplicateurSelonDifficulte = 2;
        }

        if(modeSansFin==false){
            nbRongeursPrevus = 10;
        }else{
            nbRongeursPrevus = 900;
        }
        return nbRongeursPrevus;
    }





//------------------------------------------------------------------------Fonction déplacement rongeur----------------------------------------------------------------------------------



    //parcours toutes les cases du champ et si la case est un rongeur, applique les règle de déplacement
    Case[][] selectionRongeurADeplacer(Case[][] champ){
        boolean sautDeTour = false;
        boolean attentionDroite = false;
        boolean attentionBas = false;
        int[][] tabIndicesASauter = new int[30][2];
        tabIndicesASauter = nettoyerTabIndicesASauter(tabIndicesASauter);
        for(int idxL=0; idxL<15; idxL++){
            for(int idxC=0; idxC<9; idxC++){
                if(carottePlantee==0){
                    return champ;
                }
                if((sautDeTour==false) && (listeIndicesASauter(idxL, idxC, tabIndicesASauter)==false)){    //condition pour qu'un rongeur qui soit allé à droite       
                    if(idxC!=8){                                                                           //ou en bas ne se déplace pas deux fois
                        if(estVide(champ[idxL][idxC+1])==true || estGraine(champ[idxL][idxC+1])==true){
                            attentionDroite=true;
                        }
                    }
                    if(idxL!=14){
                        if(estVide(champ[idxL+1][idxC])==true || estGraine(champ[idxL+1][idxC])==true){
                            attentionBas=true;
                        }
                    }
                    if(estRongeur(champ[idxL][idxC])==true){
                        champ = directionSelonCible(champ, idxL, idxC);
                        champ = etatChampApresRongeur(champ);
                        affichageChamp(champ, modeActuel, tour);
                        delay(100);///////////////////////////////////////////////// <= RAPIDITE DEPLACEMENT RONGEUR
                    }              
                    if(attentionDroite==true){
                        if(estRongeur(champ[idxL][idxC+1])==true){
                            sautDeTour=true;
                        }
                        attentionDroite=false;
                    }
                    if(attentionBas==true){   
                        if(estRongeur(champ[idxL+1][idxC])){
                            tabIndicesASauter=entrerIndicesDansTabIndicesASauter(idxL+1, idxC, tabIndicesASauter);
                        }
                        attentionBas=false;
                    }
                }else if(sautDeTour==true){
                    sautDeTour=false;
                    println("saut de tour");
                }
                if(listeIndicesASauter(idxL, idxC, tabIndicesASauter)==true){
                }       
            }
        }
        tabIndicesASauter=nettoyerTabIndicesASauter(tabIndicesASauter);
        return champ;
    }

    int[][] entrerIndicesDansTabIndicesASauter(int idxL, int idxC, int[][] tabIndicesASauter){
             for(int idx=0; idx<30; idx++){
                if(tabIndicesASauter[idx][0]==-1 && tabIndicesASauter[idx][1]==-1){
                    tabIndicesASauter[idx][0]=idxL;
                    tabIndicesASauter[idx][1]=idxC;
                    break;
                }
            }
        return tabIndicesASauter;
    }

    boolean listeIndicesASauter(int idxL, int idxC, int[][] tabIndicesASauter){ //liste des rongeurs s'étant déplacé vers le bas
        boolean sautDeTour = false;
        for(int idx=0; idx<30; idx++){
            if(tabIndicesASauter[idx][0]==idxL && tabIndicesASauter[idx][1]==idxC){
                sautDeTour=true;
                break;
            }
        }
        return sautDeTour;
    }

    int[][] nettoyerTabIndicesASauter(int[][] tabIndicesASauter){
        for(int idx=0; idx<30; idx++){
            tabIndicesASauter[idx][0]=-1;
            tabIndicesASauter[idx][1]=-1;
        }
        return tabIndicesASauter;
    }

    int[] balayerChampPourTrouverCarotte(Case[][] champ){  //balaye le champ de bas en haut pour trouver la ligne de la carotte la plus proche
        int[] coordCarotte = new int[2];
        //Déterminer sur quelle ligne est la carotte la plus proche
        for(int idxLCarotte=14; idxLCarotte>=0; idxLCarotte--){
            for(int idxCCarotte=0; idxCCarotte<9; idxCCarotte++){
                if(estCarotte(champ[idxLCarotte][idxCCarotte])){
                    coordCarotte[0]=idxLCarotte;
                    coordCarotte[1]=idxCCarotte;
                    return coordCarotte;
                }
            }
        }
        return coordCarotte;
    }
    
    Case[][] regleDeplacementRongeur (Case[][] champ, int idxL, int idxC, int idxLDirection, int idxCDirection){
        champModifie=false;
        if (champ[idxLDirection][idxCDirection].type==Entite.VIDE){
            champ[idxLDirection][idxCDirection]=newCase("RONGEUR");
            champ[idxL][idxC]=newCase("VIDE");
            champModifie=true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.BARRIERE){
            champ[idxLDirection][idxCDirection].pv=champ[idxLDirection][idxCDirection].pv-1;
            champModifie=true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.CAROTTE){
            champ[idxLDirection][idxCDirection].pv=champ[idxLDirection][idxCDirection].pv-1;
            champModifie=true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.JOUEUR){
            champ=rencontreRongeurVersJoueur(champ, idxL, idxC); 
        }else if(champ[idxLDirection][idxCDirection].type==Entite.CHIEN){            
            champ=rencontreChienRongeur(champ, idxL, idxC, idxLDirection, idxCDirection);
            champModifie=true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.GRAINE){
            champ[idxLDirection][idxCDirection]=newCase("RONGEUR");
            champ[idxL][idxC]=newCase("VIDE");
            champModifie=true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.RONGEUR){
            champModifie=false;
        }
        return champ;
    }

    int directionToCoord(char direction, int ligneOuColonne){ //ligneOuColonne=0 pour ligne et 1 pour colonne
        int coord=0;
        if(direction=='h'&&ligneOuColonne==0){
            coord = -1;
        }else if(direction=='b'&&ligneOuColonne==0){
            coord = 1;
        }else if((direction=='b'||direction=='h') && ligneOuColonne==1){
            coord = 0;
        }else if((direction=='d' || direction=='g') && ligneOuColonne==0){
            coord = 0;
        }else if(direction=='d' &&ligneOuColonne==1){
            coord = 1;
        }else if(direction=='g' &&ligneOuColonne==1){
            coord = -1;
        }else if(direction=='z'){
            coord = 0;
        }
        return coord;
    }

    Case[][] etatChampApresRongeur(Case[][] champ){        //met à jour l'état du champ après les déplacements des rongeurs
        for(int idxL=0; idxL<15; idxL++){
            for(int idxC=0; idxC<9; idxC++){
                if(champ[idxL][idxC].type==Entite.BARRIERE){
                    if(champ[idxL][idxC].pv==0){
                        champ[idxL][idxC]=newCase("VIDE");
                        //barriereInventaire=barriereInventaire-1;
                    }
                }else if(champ[idxL][idxC].type==Entite.CAROTTE){
                    if(champ[idxL][idxC].pv==0){
                        champ[idxL][idxC]=newCase("VIDE");
                        carottePlantee = carottePlantee-1;
                    }
                }
            }
        }
        return champ;
    }


//------------------------------------------------------Fonction en rapport avec les rencontres entre Joueur et Rongeurs -----------------------------------------------


    Case[][] rencontreRongeurVersJoueur(Case[][] champ, int idxL, int idxC){
        if(etatJoueur){
            if(phaseDeCombat(choixNiveauCLASSIQUE,50)){
                champ[idxL][idxC].type=Entite.VIDE;
                rongeurRestant=rongeurRestant-1;
                champModifie=true;
            }else{
                etatJoueur=false;
                debutEtourdissementJoueur=tour;
                champModifie=false;
            }
        }
        return champ;
    }

    Case[][] rencontreJoueurVersRongeur(Case[][] champ, int idxLDir, int idxCDir){ //cette fonction et celle au dessus se ressemble mais les signatures induisant en erreur et rendant le code peut clair nous avons préférer les séparer
        if(etatJoueur){
            if(phaseDeCombat(choixNiveauCLASSIQUE,50)){
                champ[idxLDir][idxCDir]=champ[idxLJoueur][idxCJoueur];
                champ[idxLJoueur][idxCJoueur]=newCase("VIDE");
                idxLJoueur=idxLDir;
                idxCJoueur=idxCDir;
                rongeurRestant=rongeurRestant-1;
            }else{
                etatJoueur=false;
                debutEtourdissementJoueur=tour;
            }
        }
        champModifie=true;
        return champ;
    }











//----------------------------------------------------------------------Fonction en rapport avec le déplacement du chien---------------------------------------------------------------------------------


    Case[][] deplacementAutomatiqueChien(Case[][] champ){
        int[] coordRongeurCible = new int[2];
        coordRongeurCible = balayerChampPourTrouverRongeur(champ);
        champ = directionSelonCible(champ, idxLChien, idxCChien);
        affichageChamp(champ, modeActuel, tour);
        delay(300);              //<========================================= VITESSE DEPLACEMENT CHIEN
        return champ;
    }

    int[] balayerChampPourTrouverRongeur(Case[][] champ){
        int[] coordRongeurCible = new int[2];
        for(int idxLRongeurCible=0; idxLRongeurCible<14; idxLRongeurCible++){
            for(int idxCRongeurCible=0; idxCRongeurCible<9; idxCRongeurCible++){
                if(estRongeur(champ[idxLRongeurCible][idxCRongeurCible])){
                    coordRongeurCible[0]=idxLRongeurCible;
                    coordRongeurCible[1]=idxCRongeurCible;
                    return coordRongeurCible;
                }
            }
        }
        return coordRongeurCible;
    }
        
    Case[][] regleDeplacementChien (Case[][] champ, int idxLDirection, int idxCDirection){ //regle établissant les interactions entre le chien et ce qu'il croise
        champModifie = false;                                                              //dans ses tentatives de déplacement
        if (champ[idxLDirection][idxCDirection].type==Entite.VIDE){
            champ[idxLDirection][idxCDirection]=newCase("CHIEN");
            champ[idxLChien][idxCChien]=newCase("VIDE");
            idxLChien = idxLDirection;
            idxCChien = idxCDirection;
            champModifie = true;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.BARRIERE){
            champModifie = false;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.CAROTTE){
            champModifie = false;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.JOUEUR){
            champModifie = false;
        }else if(champ[idxLDirection][idxCDirection].type==Entite.RONGEUR){                            
            champ=rencontreChienRongeur(champ, idxLChien, idxCChien, idxLDirection, idxCDirection);
        }else if(champ[idxLDirection][idxCDirection].type==Entite.GRAINE){
            champModifie = false;
        }
        return champ;
    }

    Case[][] rencontreChienRongeur(Case[][] champ, int idxLRongeur, int idxCRongeur, int idxLDirection, int idxCDirection){ //si le chien et un rongeur se rencontre le chien à 6/10 chance de 
        if(etatChien){                                                                    //manger le rongeur. Sinon étourdit pour un tour
            if(phaseDeCombatChien()){
                if(champ[idxLDirection][idxCDirection].type==Entite.CHIEN){
                    champ[idxLRongeur][idxCRongeur]=newCase("VIDE");
                }else{
                    champ[idxLDirection][idxCDirection].type=Entite.VIDE;
                    rongeurRestant=rongeurRestant-1;
                    champModifie=true;
                }
            }else{                
                etatChien=false;
                debutEtourdissementChien=tour;
                if(champ[idxLDirection][idxCDirection].type==Entite.RONGEUR){
                    champModifie=false;
                }
            }
        }
        return champ;
    }

    boolean phaseDeCombatChien(){ //Fonction qui détermine si le chien a réussi à manger le rongeur (7 chance sur 10)
        int alea = (int)(random()*10);
        if(alea<6){
            return true;
        }else{
            return false;
        }
    }













//------------------------------------------------------------------------Fonctions communes aux rongeurs et au chien-------------------------------------------------------------------------------------------
    
    
    //détermine la direction du rongeur en fonction de la position de la carotte la plus proche
    Case[][] directionSelonCible(Case[][] champ, int idxL, int idxC){
        int[] coordCarotte = ciblePlusProche(champ, idxL, idxC);
        char[] possibilites= new char[4];

        if(coordCarotte[0]==idxL){ //si la carotte est sur la même ligne que le rongeur
            if(coordCarotte[1]>idxC){ //si la carotte est à droite du rongeur
                possibilites = determinerPossibiliteDeDirection("droite", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }else if(coordCarotte[1]<idxC){ //si la carotte est à gauche du rongeur
                possibilites = determinerPossibiliteDeDirection("gauche",idxL,idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }
        }else if(coordCarotte[1]==idxC){  //si la carotte est sur la même colonne que le rongeur
            if(coordCarotte[0]>idxL){
                possibilites = determinerPossibiliteDeDirection("bas", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }else if(coordCarotte[0]<idxL){
                possibilites = determinerPossibiliteDeDirection("haut", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }
        }else if(coordCarotte[0]>idxL || coordCarotte[0]<idxL){  //si la carotte est sur une ligne différente du rongeur
            if(coordCarotte[0]<idxL && coordCarotte[1]>idxC){
                possibilites = determinerPossibiliteDeDirection("hautDroite", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }else if(coordCarotte[0]<idxL && coordCarotte[1]<idxC){
                possibilites = determinerPossibiliteDeDirection("hautGauche", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }else if(coordCarotte[0]>idxL && coordCarotte[1]>idxC){
                possibilites = determinerPossibiliteDeDirection("basDroite", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }else if(coordCarotte[0]>idxL && coordCarotte[1]<idxC){
                possibilites = determinerPossibiliteDeDirection("basGauche", idxL, idxC);
                    champ = deplacementAutomatiqueEntite(champ, idxL, idxC, possibilites);
            }
        }
        return champ;
    }

    int[] ciblePlusProche(Case[][] champ, int idxL, int idxC){ //selon si la fonction est appellée par le chien ou un rongeur
        boolean cibleTrouvee=false;
        int idxDroite=0;
        int idxGauche=0;
        int[] coordCible = new int[2];
        if(estRongeur(champ[idxL][idxC])){
            coordCible = balayerChampPourTrouverCarotte(champ);
        }else{
            coordCible = balayerChampPourTrouverRongeur(champ);
        }
        //déterminer sur quelle colonne est la cible (carotte et rongeur) la plus proche
        if(idxC!=8){
            idxDroite=idxC+1;
        }
        if(idxC!=0){
            idxGauche=idxC-1;
        }
        while(cibleTrouvee==false){
            if(coordCible[1]==idxC){
                cibleTrouvee=true;
            }else if(estRongeur(champ[idxL][idxC]) && estCarotte(champ[coordCible[0]][idxDroite]) || (estChien(champ[idxL][idxC]) && estRongeur(champ[coordCible[0]][idxDroite]))){
                coordCible[1]=idxDroite;
                cibleTrouvee=true;
            }else if(estRongeur(champ[idxL][idxC]) && estCarotte(champ[coordCible[0]][idxGauche]) || (estChien(champ[idxL][idxC]) && estRongeur(champ[coordCible[0]][idxGauche]))){
                coordCible[1]=idxGauche;
                cibleTrouvee=true;
            }
            if(idxDroite!=8){
                idxDroite++;
            }
            if(idxGauche!=0){
                idxGauche--;
            }
        }
        return coordCible;
    }

    char[] determinerPossibiliteDeDirection(String direction, int idxL, int idxC){
        char possibilite1='y'; //sinon erreur d'initialisation
        char possibilite2='y';
        char[] pAlea = new char[2];
        char[] possibilites = new char[4];
        if(idxL==0 || idxL==14 || idxC==0 || idxC==8){
                possibilites=determinerPossibiliteDeDirectionLimite(direction, idxL, idxC);
        }else{
            if(direction=="haut"){
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{'h', pAlea[0], pAlea[1], 'b'};
            }else if(direction=="bas"){
                possibilite1 = 'g';
                possibilite2 = 'd';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{'b', pAlea[0], pAlea[1], 'h'};
            }else if(direction=="gauche"){
                possibilite1 = 'h';
                possibilite2 = 'b';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{'g', pAlea[0], pAlea[1], 'd'};
            }else if(direction=="droite"){
                possibilite1 = 'h';
                possibilite2 = 'b';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{'d', pAlea[0], pAlea[1], 'g'};
            }else if(direction=="hautDroite"){
                possibilite1 = 'h';
                possibilite2 = 'd';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{pAlea[0], pAlea[1], 'g','b'};
            }else if(direction=="hautGauche"){
                possibilite1 = 'h';
                possibilite2 = 'g';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{pAlea[0], pAlea[1], 'd','b'};
            }else if(direction=="basDroite"){
                possibilite1 = 'b';
                possibilite2 = 'd';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{pAlea[0], pAlea[1], 'g','h'};
            }else if(direction=="basGauche"){
                possibilite1 = 'b';
                possibilite2 = 'g';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilites=new char[]{pAlea[0], pAlea[1], 'd','h'};
            }
        }
        return possibilites;
    }

    char[] determinerPossibiliteDeDirectionLimite(String direction, int idxL, int idxC){
        char possibilite1='y'; //sinon erreur d'initialisation
        char possibilite2='y';
        char[] pAlea = new char[2];
        char[] possibilite = new char[4];
        if(idxL==0){
            if(idxC==0){
                if(direction=="droite"){
                    possibilite=new char[]{'d', 'b', 'z', 'z'};
                }else if(direction=="bas"){
                    possibilite=new char[]{'b', 'd', 'z', 'z'};
                }
            }else if(idxC==8){
                if(direction=="gauche"){
                    possibilite=new char[]{'g', 'b', 'z', 'z'};
                }else if(direction=="bas"){
                    possibilite=new char[]{'b', 'g', 'z', 'z'};
                }
            }else{
                if(direction=="gauche"){
                    possibilite=new char[]{'g', 'b', 'd', 'z'}; 
                }else if(direction=="droite"){
                    possibilite=new char[]{'d', 'b', 'g', 'z'};
                }else if(direction=="bas"){
                    possibilite=new char[]{'b', 'g', 'd', 'z'};
                }else if(direction=="basGauche"){
                    possibilite=new char[]{'b', 'g', 'd', 'z'};
                }else if(direction=="basDroite"){
                    possibilite=new char[]{'b', 'd', 'g', 'z'};
                }
            }
        }else if(idxL==14){
            if(idxC==0){
                if(direction=="droite"){
                    possibilite=new char[]{'d', 'h', 'z', 'z'};
                }else if(direction=="haut"){
                    possibilite=new char[]{'h', 'd', 'z', 'z'};
                }else if(direction=="hautDroite"){
                    possibilite=new char[]{'h', 'd', 'z', 'z'};
                }
            }else if(idxC==8){
                if(direction=="gauche"){
                    possibilite=new char[]{'g', 'h', 'z', 'z'};
                }else if(direction=="haut"){
                    possibilite=new char[]{'h', 'g', 'z', 'z'};
                }else if(direction=="hautGauche"){
                    possibilite=new char[]{'h', 'g', 'z', 'z'};
                }
            }else{
                if(direction=="gauche"){
                    possibilite=new char[]{'g', 'h', 'd', 'z'};
                }else if(direction=="droite"){
                    possibilite=new char[]{'d', 'h', 'g', 'z'}; 
                }else if(direction=="haut"){
                    possibilite1 = 'g';
                    possibilite2 = 'd';
                    pAlea=(aleaPossibilite(possibilite1, possibilite2));
                    possibilite=new char[]{'h', pAlea[0], pAlea[1], 'z'};
                }else if(direction=="hautGauche"){
                    possibilite=new char[]{'h', 'g', 'd', 'z'}; 
                }else if(direction=="hautDroite"){
                    possibilite=new char[]{'h', 'd', 'g', 'z'};
                }
            }  
        }else if(idxC==0){
            if(direction=="droite"){
                possibilite1 = 'h';
                possibilite2 = 'b';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilite=new char[]{'d', pAlea[0], pAlea[1], 'z'}; 
            }else if(direction=="haut"){
                possibilite=new char[]{'h', 'd', 'b', 'z'}; 
            }else if(direction=="bas"){
                possibilite=new char[]{'b', 'd', 'h', 'z'};
            }else if(direction=="hautDroite"){
                possibilite=new char[]{'h', 'd', 'b', 'z'};
            }else if(direction=="basDroite"){
                possibilite=new char[]{'b', 'd', 'h', 'z'};
            }
        }else if(idxC==8){
            if(direction=="gauche"){
                possibilite1 = 'h';
                possibilite2 = 'b';
                pAlea=(aleaPossibilite(possibilite1, possibilite2));
                possibilite=new char[]{'g', pAlea[0], pAlea[1], 'z'};
            }else if(direction=="haut"){
                possibilite=new char[]{'h', 'g', 'b', 'z'};
            }else if(direction=="bas"){
                possibilite=new char[]{'b', 'g', 'h', 'z'};
            }else if(direction=="hautGauche"){
                possibilite=new char[]{'h', 'g', 'b', 'z'};
            }else if(direction=="basGauche"){
                possibilite=new char[]{'b', 'g', 'h', 'z'};
            }
        }
        return possibilite;
    }

/*
    //Ne fonctionne pas alors que ça devrait être bon ?
    void testDeterminerPossibiliteDeDirectionLimite(){
        assertEquals(new char[]{'d', 'b', 'z', 'z'}, determinerPossibiliteDeDirectionLimite("droite", 0, 0));
        assertEquals(new char[]{'g', 'h', 'z', 'z'}, determinerPossibiliteDeDirectionLimite("gauche", 0, 8));
    }
*/

    char[] aleaPossibilite(char possibilite1, char possibilite2){
        char[] pAlea = new char[2];
        int alea = (int)(random()*10);
        if(alea>=5){
            pAlea[0]=possibilite1;
            pAlea[1]=possibilite2;
        }else{
            pAlea[0]=possibilite2;
            pAlea[1]=possibilite1;
        }
        return pAlea;
    }
    
    Case[][] deplacementAutomatiqueEntite(Case[][] champ, int idxL, int idxC, char[] possibilites){
        int tentative=0;
        boolean deplacementEffectue=false;
        int idxLDir=0;
        int idxCDir=0;
        while(deplacementEffectue==false){
            idxLDir=idxL;
            idxCDir=idxC;
            idxLDir=idxLDir+directionToCoord(possibilites[tentative],0);
            idxCDir=idxCDir+directionToCoord(possibilites[tentative],1);
            if(estRongeur(champ[idxL][idxC])){
                champ=regleDeplacementRongeur(champ, idxL, idxC, idxLDir, idxCDir);
            }else{
                champ=regleDeplacementChien(champ, idxLDir, idxCDir);
            }
            if(champModifie==false && tentative!=3){
                tentative=tentative+1;
            }else if(tentative==3 || champModifie==true){
                deplacementEffectue=true;
            }
        }
        return champ;
    }






//--------------------FIN GAMEPLAY---------------------


    Case newCase(Entite type ,int pv){
        Case e = new Case();
        e.type = type;
        e.pv = pv;
        return e;
    }

    Case[][] initialiserPremierChamp(){
        Case[][] champ = new Case[15][9];
        for(int idxL = 0;idxL < length(champ,1);idxL ++){
            for(int idxC = 0 ;idxC < length(champ,2);idxC ++){
                champ[idxL][idxC] = newCase(Entite.VIDE,0);
            }
        }
        champ[2][4]= newCase(Entite.CAROTTE,3);
        champ[IDX_L_INITIAL_JOUEUR][IDX_C_INITIAL_JOUEUR]= newCase(Entite.JOUEUR,0);
        return champ;
    }

    int ChiffreRandom(){
        int[] nombre = new int[]{0,1,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10}; 
        return nombre[(int)(random()*length(nombre))];
    }

    boolean phaseDeCombat(int niveau,int piecesGagne){//piecesGagne = points gagné par bonne reponses
        espacement();
        int chiffre1 = 0;
        int chiffre2 = 0;
        if(!modeSansFin){
            if(niveau < 11){
                chiffre1 = niveau;
                chiffre2 = ChiffreRandom();
            }else{
                chiffre1 = ChiffreRandom();
                chiffre2 = ChiffreRandom();
            }
        }else{
            chiffre1 = ChiffreRandom();
            chiffre2 = ChiffreRandom();
        }
        affichageTXT(RAT);
        println("Resolvez la multiplication suivante pour gagner "+piecesGagne+" points : \n"+chiffre1+" x "+chiffre2+" (Vous avez "+substring(TempsDeReponse[difficulte]+"",0,length(TempsDeReponse[difficulte]+"")-3)+" secondes)");
        delay(500);
        long temps1 = getTime();
        int nb = readInt();
        long temps2 = getTime();
        long resultat = temps2 - temps1;
        if(resultat > TempsDeReponse[difficulte]){
            println("Trop tard !");
            delay(3000);
            espacement();
            return false;
        }else if(nb == chiffre1*chiffre2){
            println("Bonne reponse ! + "+piecesGagne+" point");
            nbPiece = nbPiece + piecesGagne;
            delay(3000);
            espacement();
            return true;
        }else{
            println("Mauvaise reponse !");
            delay(3000);
            espacement();
            return false;
        }
    }

    //-----------------------------SPAWNS----------------------------

    void SpawnDuneCarotte(Case[][] champ,int lig,int col){
        champ[lig][col] = newCase(Entite.CAROTTE,3);
    }
    
    void SpawnDuneGraine(Case[][] champ,int lig,int col){
        champ[lig][col] = newCase(Entite.GRAINE,3);
    }

    void SpawnDuneBarriere(Case[][] map,int Ligne,int Colonne){
        map[Ligne][Colonne] = newCase(Entite.BARRIERE,3);
    } 

    void phaseDePlacement(Case[][] map ,int NBdecarotteAplacer,int NBdebarriereAplacer,String filename,int choixPerso,CSVFile file){
        int choixPlace = 15;
        espacement();
        affichageChampMenuPlacable(map);
        int carotte = NBdecarotteAplacer;
        int barriere = NBdebarriereAplacer;
        boolean saisieValide = false;
        boolean finPlacement = false;
        boolean placement = false;
        boolean PasHorsTableau = false;
        int Ligne = 14;
        int Colonne = 8;
        int valideCarotte = 0;
        int valideBarriere = 0;
        while(!finPlacement){
            choixPlace = 15;
            PasHorsTableau = false;
            placement = false;
            saisieValide = false;
            espacement();
            affichageChampMenuPlacable(map);
            println("Vous pouvez placer "+carotte+" graines et "+barriere+" barrieres");
            println("1 : Placer les graines\n2 : Placer les barrières");
            while(!saisieValide){
                if(choixPlace == 1 || choixPlace == 2){
                    saisieValide = true;
                }else{
                    choixPlace = readInt();
                }
            }
            
            //choxi de ce quon veut
            if(choixPlace == 1 && carotte > 0){
                //place carotte
                while(!placement){
                    
                    valideCarotte = 0;
                    PasHorsTableau = false;
                    while(!PasHorsTableau){
                        espacement();
                        affichageChampMenuPlacable(map);
                        println("(INTERDIT en 4 - 4/5 ou sur toute la derniere ligne)\nGraine en : ");
                        println("Ligne ?");
                        Ligne = readInt();
                        println("Colonne ?");
                        Colonne = readInt();
                        if(Ligne >= 0 && Ligne < 14 && Colonne >= 0 && Colonne < 9 && !SPAWN(Ligne,Colonne)&& !SPAWNCHIEN(Ligne,Colonne)){
                            PasHorsTableau = true;
                        }else{
                            println(ANSI_RED+"PAS POSSIBLE"+ANSI_RESET);
                            delay(1000);
                        }    
                    }
                    if(!estCarotte(map[Ligne][Colonne])){
                        valideCarotte ++;
                    }
                    if(!estBarriere(map[Ligne][Colonne])){
                        valideCarotte ++;
                    }
                    if(!estGraine(map[Ligne][Colonne])){
                        valideCarotte ++;
                    }
                    if(valideCarotte == 3){
                        SpawnDuneGraine(map,Ligne,Colonne);
                        carotte = carotte -1;
                        placement = true;
                    }
                }
                
            }else if(choixPlace == 2 && barriere > 0){  
                //place barriere
                while(!placement){
                    
                    valideBarriere = 0;
                    PasHorsTableau = false;
                    while(!PasHorsTableau){
                        espacement();
                        affichageChampMenuPlacable(map);
                        println("(INTERDIT en 4 - 4/5 et sur toute la derniere ligne)\nBarriere en : ");
                        println("Ligne ?");
                        Ligne = readInt();
                        println("Colonne ?");
                        Colonne = readInt();
                        if(Ligne >= 0 && Ligne < 14 && Colonne >= 0 && Colonne < 9 && !SPAWN(Ligne,Colonne)&& !SPAWNCHIEN(Ligne,Colonne)){
                            PasHorsTableau = true;
                        }else{
                            println(ANSI_RED+"PAS POSSIBLE"+ANSI_RESET);
                            delay(1000);
                        }    
                    }
                    if(!estCarotte(map[Ligne][Colonne])){
                        valideBarriere ++;
                    }
                    if(!estBarriere(map[Ligne][Colonne])){
                        valideBarriere ++;

                    }
                    if(!estGraine(map[Ligne][Colonne])){
                        valideBarriere ++;

                    }
                    if(valideBarriere == 3){
                        SpawnDuneBarriere(map,Ligne,Colonne);
                        barriere = barriere -1;
                        placement = true;
                    }else{
                        println("erreur dans le placement");
                    }
                }
            }
            if(carotte == 0 && barriere == 0){
                finPlacement = true;
            }
        }
        println("Plus rien a placer");
        sauvegarderPlacable("../ressources/placable.csv",file,map,choixPerso);
    } 

    boolean SPAWN(int ligne,int col){
        if(ligne == 4 && col == 4){
            return true;
        }else{
            return false;
        }
    }

    boolean SPAWNCHIEN(int ligne,int col){
        if(ligne == 4 && col == 5){
            return true;
        }else{
            return false;
        }
    }

    String readBetterString(){
        String saisie = readString()+" ";
        return substring(saisie, 0, length(saisie));
    } 

    char readBetterChar(){
        String saisie = toLowerCase(readString()+" ");
        return charAt(saisie,0);
    }

    int venteDeCarotte(Case[][] map,int prixCarrote,String filename,CSVFile file,int choixPerso){
        boolean FinDesVentes = false;
        int argent = 0;
        int ligne = 0;
        int colonne = 0;
        boolean PasHorsTableau = false;
        boolean achat = false;
        while(!FinDesVentes){
            if(nombreDeCarotte(map) > 1){
                achat = true;
                while(!PasHorsTableau){
                    espacement();
                    affichageChampMenuPlacable(map);
                    println("Quelle carotte voulez-vous vendre ?\n(Entrez 99 et 99 pour sortir de la vente)\n");
                    println("Ligne ?");
                    ligne = readInt();
                    println("Colonne ?");
                    colonne = readInt();
                    if((ligne >= 0 && ligne < 14 && colonne >= 0 && colonne < 9 && !SPAWN(ligne,colonne) && !SPAWNCHIEN(ligne,colonne) && estCarotte(map[ligne][colonne])|| finVenteManuel(ligne,colonne))){
                        PasHorsTableau = true;
                    }else{
                        println(ANSI_RED+"PAS POSSIBLE"+ANSI_RESET);
                        delay(1000);
                    }      
                }
                PasHorsTableau = false;
                if(ligne == 99 && colonne == 99){
                    FinDesVentes = true;
                }else{
                    map[ligne][colonne] = newCase(Entite.VIDE,0);
                    argent = argent + prixCarrote;
                    carottePlantee = carottePlantee - 1;
                }
            }else{
                FinDesVentes = true;
            }
        }
        if(achat){
            println("Fin des ventes !");
            delay(2000);
        }else{
            println("Vous n'avez de carotte a vendre !");
            delay(2000);
        }
        sauvegarderPlacable(filename,file,map,choixPerso);
        return argent;
        
    }

    int nombreDeCarotte(Case[][] map){
        int nb = 0;
        for(int idxL = 0;idxL < length(map,1);idxL ++){
            for(int idxC = 0;idxC < length(map,2);idxC ++){
                if(estCarotte(map[idxL][idxC])){
                    nb ++;
                }
            }
        }
        return nb;
    }

    boolean finVenteManuel(int l,int c){
        if(l == 99 && c == 99){
            return true;
        }else{
            return false;
        }
    }

    boolean controleDeSaisie(int min,int max,int choix){
        if(choix < max && choix > min){
            return true;
        }else{
            return false;
        }
    }

    boolean argentPourAcheter(int prix,int argent){
        if(argent >=prix){
            return true;
        }else{
            return false;
        }
    }

    void affichageDuMenuBoutique(Case[][] map , CSVFile fileplacables,int choixPerso){
        boolean finBoutique = false;
        final int PRIX_CHIEN = 7000;
        final int PRIX_BARRIERE = 100;
        final int PRIX_BOTTES = 3000;
        final int PRIX_GRAINE = 50;
        int choixBoutique = 0;
        int barriereAchete = 0;
        int graineAchete = 0;
        while(!finBoutique){
            choixBoutique = 0;
            espacement();
            affichageChampMenuPlacable(map);
            affichageTXT(BOUTIQUE);
            println("Solde : "+nbPiece+"\n");
            println("1 ) Acheter le chien | 7000");
            println("2 ) Acheter une barriere | 100");
            println("3 ) Acheter l'amelioration des bottes | 3000");
            println("4 ) Acheter une graine de carotte | 50");
            println("5 ) Vendre des carottes");
            println("6 ) Quitter");
            println();
            while(!controleDeSaisie(0,7,choixBoutique)){
                choixBoutique = readInt();
            }
            if(choixBoutique == 1){
                if(argentPourAcheter(PRIX_CHIEN,nbPiece) && nbChien < 1){
                    nbPiece = nbPiece - PRIX_CHIEN;
                    nbChien = 1;
                    println("Chien acheté !");
                }else{
                    
                    println(ANSI_RED+"Probleme survenu a l'achat !"+ANSI_RESET);
                    delay(1000);
                }
            }else if(choixBoutique ==2){
                if(argentPourAcheter(PRIX_BARRIERE,nbPiece)){
                    nbPiece = nbPiece - PRIX_BARRIERE;
                    nbBarriereInventaire = nbBarriereInventaire + 1;
                    barriereAchete ++;
                    println("Barriere achete !");
                }else{
                    
                    println(ANSI_RED+"Pas assez d'argent !"+ANSI_RESET);
                    delay(1000);
                }
            }else if(choixBoutique == 3){
                if(argentPourAcheter(PRIX_BOTTES,nbPiece) && niveauAmeliorateurDePas < 3){
                    nbPiece = nbPiece - PRIX_BOTTES;
                    niveauAmeliorateurDePas = niveauAmeliorateurDePas + 1;
                    println("Bottes achete !");
                }else{
                    println(ANSI_RED+"Probleme survenu a l'achat !"+ANSI_RESET);
                    delay(1000);
                }
            }else if(choixBoutique == 4){
                if(argentPourAcheter(PRIX_GRAINE,nbPiece)){
                    nbPiece = nbPiece - PRIX_GRAINE;
                    nbGraineInventaire = nbGraineInventaire + 1;
                    graineAchete ++;
                    println("Graine achete !");
                }else{
                    
                    println(ANSI_RED+"Pas assez d'argent !"+ANSI_RESET);
                    delay(1000);
                }
            }else if(choixBoutique == 5){
                nbPiece = nbPiece + venteDeCarotte(map,100,"../ressources/placable.csv",fileplacables,choixPerso);
            }else{
                println("Vous quittez la boutique !");
                delay(1000);
                finBoutique = true;
            }
        }
        if(barriereAchete > 0 || graineAchete > 0){
            phaseDePlacement(map,graineAchete,barriereAchete,"../ressources/placable.csv",choixPerso,fileplacables);
        }
        espacement();
        println("a bientot !");
    }

    String modeEnString(boolean etat){
        if(etat){
            return "MODE SANS FIN";
        }else{
            return "MODE NORMAL";
        }
    }
    






    //-----------------------------------------------------------------------------------------VOID ALGORITHM--------------------------------------------------------------------------- 
   
    void algorithm(){
        boolean fin = false;
        while(!fin){
            CSVFile file1 = loadCSV("../ressources/JOUEURS.csv");
            CSVFile file2 = loadCSV("../ressources/placable.csv");
            espacement();
            affichageTXT(MENUCSV);
            println();
            int choix = readInt();
            if(choix == 1){ //--------------------------------------creation d'un profil
                if(ajouterProfil("../ressources/JOUEURS.csv",file1)){
                    println("Profil creer avec succés !");
                    delay(1000);
                    espacement();
                }else{
                    println("20 profils max !");
                    delay(1000);
                    espacement();
                }
            }else if(choix == 2){ // ------------------------------lancer partie avec un profil
                espacement();
                affichagePartieCSV(file1);
                println();
                println("Quelle partie charger ?");
                println("'0' pour revenir au menu");
                int choixPerso = readInt();
                boolean choixPartie = false;
                while(!choixPartie){
                    if(choixPerso < 0 || choixPerso >(rowCount(file1)-1)){
                        println("Choix non valide !");
                        choixPerso = readInt();
                    }else{
                        choixPartie = true;
                    }
                }
                if(choixPerso != 0){
                    Case[][] map;
                    if(premierePartie(file1,choixPerso)){//Test si cest la premiere partie du joueur , si oui il creer sa premiere map sinon il load deja ce qu'il a pour le joueur en parametres
                        plusLaPremierePartie("../ressources/JOUEURS.csv",file1,choixPerso);
                        println("\nCreation de la nouvelle carte !");
                        delay(3000);
                        map = initialiserPremierChamp();
                        sauvegarderPlacable("../ressources/placable.csv",file2,map,choixPerso);
                        file2 = loadCSV("../ressources/placable.csv");
                        file1 = loadCSV("../ressources/JOUEURS.csv");
                    }else{
                        println("\nChargement de la derniere carte !");
                        delay(3000);      
                    }
                    //--------recup des stats-----------
                    map = chargerDerniereMap(file2,choixPerso);
                    dernierNiveauAtteintSansFin = recupNiveau(file1,choixPerso);
                    nbPiece = recupPoints(file1,choixPerso);
                    nbGraineInventaire = recupGraine(file1,choixPerso);
                    niveauAmeliorateurDePas = recupTaillePas(file1,choixPerso);
                    carottePlantee = recupNBcarotte(file1,choixPerso);
                    nbChien = recupNBChien(file1,choixPerso);
                    nbBarriereInventaire = recupNBBarriere(file1,choixPerso);
                    difficulte = recupDifficulte(file1,choixPerso);
                    //-------fin de la recup des stats---------

                    //---------------------------------------------------------------------------MENU DU JEU-----------------------------------------------------------------
                    boolean FinDuMenu = false;
                    while(!FinDuMenu){
                        if(carottePlantee < 1 && nbPiece < 50){
                            println("GAME OVER");
                            delay(3000);
                            FinDuMenu = true;
                        }
                        choixNiveauCLASSIQUE = 0;
                        file2 = loadCSV("../ressources/placable.csv");
                        file1 = loadCSV("../ressources/JOUEURS.csv");
                        map = chargerDerniereMap(file2,choixPerso);
                        espacement();
                        affichageTXT(FERME);
                        println();
                        affichageMenuTexte(modeSansFin,nbPiece,dernierNiveauAtteintSansFin,carottePlantee,nbChien,choixPerso,file1);
                        int choixGAME = readInt();
                        if(choixGAME == 1){
                            if(!modeSansFin){
                                if(carottePlantee >= 1){
                                    //lancement d'une game avec niveau
                                    espacement();
                                    affichageTXT(CHOIXNIVEAU);
                                    println("Selectionnez votre table de multiplication !");
                                    println();
                                    choixNiveauCLASSIQUE = readInt();
                                    while(!controleDeSaisie(0,12,choixNiveauCLASSIQUE)){
                                    println("CHOIX IMPOSSIBLE");
                                    delay(1000);
                                    espacement();
                                    affichageTXT(CHOIXNIVEAU);println("\n");
                                    println("Selectionnez votre table de multiplication !");
                                    choixNiveauCLASSIQUE = readInt();
                                }
                                println("JEU LANCE");
                                delay(200);
                                phaseDeJeu(map);
                                tour = 0;
                                idxLJoueur = IDX_L_INITIAL_JOUEUR;
                                idxCJoueur = IDX_C_INITIAL_JOUEUR;
                                carottePlantee = carottePlantee + graineEnCarotte(map);
                                sauvegarderPlacable("../ressources/placable.csv",file2,map,choixPerso);
                                sauvegardeDesStats(file1,"JOUEURS.csv",choixPerso,nbGraineInventaire,nbPiece,dernierNiveauAtteintSansFin,niveauAmeliorateurDePas,carottePlantee,nbChien,nbBarriereInventaire);
                                }
                            }else{
                                println("mode sans fin");
                                delay(2000);
                                while(quitterLeJeu==false || mort==false){
                                    phaseDeJeu(map);
                                    mort = true;
                                }
                                tour = 0;
                                idxLJoueur = IDX_L_INITIAL_JOUEUR;
                                idxCJoueur = IDX_C_INITIAL_JOUEUR;
                                carottePlantee = carottePlantee + graineEnCarotte(map);
                                mort = false;
                                quitterLeJeu = false;
                                sauvegarderPlacable("../ressources/placable.csv",file2,map,choixPerso);
                                sauvegardeDesStats(file1,"JOUEURS.csv",choixPerso,nbGraineInventaire,nbPiece,dernierNiveauAtteintSansFin,niveauAmeliorateurDePas,carottePlantee,nbChien,nbBarriereInventaire);
                            }
                        }else if(choixGAME == 2){
                            //passer le mode sans fin sur ON
                            modeSansFin = toggleModeSansFin(modeSansFin);
                        }else if(choixGAME == 3){
                            // aller a l'affichage de la boutique
                            affichageDuMenuBoutique(map,file2,choixPerso);
                            sauvegardeDesStats(file1,"JOUEURS.csv",choixPerso,nbGraineInventaire,nbPiece,dernierNiveauAtteintSansFin,niveauAmeliorateurDePas,carottePlantee,nbChien,nbBarriereInventaire);
                        }else if(choixGAME == 4){
                            //REVENIR au menu csv
                            carottePlantee = carottePlantee + graineEnCarotte(map);
                            sauvegarderPlacable("../ressources/placable.csv",file2,map,choixPerso);
                            sauvegardeDesStats(file1,"JOUEURS.csv",choixPerso,nbGraineInventaire,nbPiece,dernierNiveauAtteintSansFin,niveauAmeliorateurDePas,carottePlantee,nbChien,nbBarriereInventaire);
                            FinDuMenu = true;
                        }else{
                            println(ANSI_RED+"Choix incorrect , veuillez recommencer"+ANSI_RESET);
                            delay(2000);
                            espacement();
                        }   
                    }
                    //--------------------------------------------------------------------------FIN DU MENU DU JEU----------------------------------------------------------    
                }
            }else if(choix == 3){ //affichage du classement si il y a assez de profils
                if(tailleSuffisantepourClassement(file1)){
                    espacement();
                    affichageDuClassement(file1);
                    println("Appuyez sur une touche pour quitter");
                    String test = readString();
                }else{
                    println(ANSI_RED+"AUCUN PROFIL N'EST ENCORE CREER POUR L'INSTANT"+ANSI_RESET);
                    delay(2000);
                }
                
            }else if(choix == 4){// quitte le jeu
                println("Au revoir !");
                fin = true;
                delay(2000);

            }else{// choix incorrecte
                println(ANSI_RED+"Choix incorrect , veuillez recommencer"+ANSI_RESET);
                delay(2000);
                espacement();
            }
        }
    }
}