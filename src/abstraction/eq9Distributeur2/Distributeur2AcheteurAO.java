package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import java.util.List;

public class Distributeur2AcheteurAO extends Distributeur2Acteur implements IAcheteurAO {

    // 1. Déclencher la recherche 
    public void faireUnAppelDOffre() {
        // On récupère le superviseur des appels d'offres
        SuperviseurVentesAO superviseurAO = (SuperviseurVentesAO) Filiere.LA_FILIERE.getActeur("Sup.AO");
        
        // On récupère la liste de tous les chocolats du marché
        List<ChocolatDeMarque> produits = Filiere.LA_FILIERE.getChocolatsProduits();

        // On fait l'inventaire : on vérifie notre stock pour CHAQUE chocolat
        for (ChocolatDeMarque choco : produits) {
            
            // Combien on en a en stock actuellement ?
            double stockActuel = this.stock.getOrDefault(choco, 0.0);
            
            // On se fixe une limite (par exemple, on veut toujours au moins 50 Tonnes en rayon)
            double seuilDeSecurite = 50.0; 

            // Si on passe sous le seuil, on déclenche l'Appel d'Offres !
            if (stockActuel < seuilDeSecurite) {
                
                // On calcule combien il faut acheter pour remonter à flot
                double quantiteAcheter = 100.0 - stockActuel;
                
                // Sécurité imposée par le simulateur : on ne peut pas faire un AO pour une toute petite quantité
                if (quantiteAcheter < AppelDOffre.AO_QUANTITE_MIN) {
                    quantiteAcheter = AppelDOffre.AO_QUANTITE_MIN;
                }
                
                this.journal.ajouter("Alerte stock bas pour " + choco.getNom() + ". Lancement d'un AO pour " + quantiteAcheter + "T");
                
                // On lance l'appel d'offre pour CE chocolat précis
                OffreVente offreRetenue = superviseurAO.acheterParAO(this, this.cryptogramme, choco, quantiteAcheter);
                
                // Si on a trouvé un vendeur qui respecte nos prix
                if (offreRetenue != null) {
                    this.journal.ajouter("Victoire : On a acheté à " + offreRetenue.getVendeur().getNom() + " pour " + offreRetenue.getPrixT() + "€/T");
                    
                    // IMPORTANT : On ajoute physiquement la marchandise dans notre rayon !
                    this.stock.put(choco, stockActuel + quantiteAcheter);
                    this.indicateurStockTotal.setValeur(this, getStockTotal());
                } else {
                    this.journal.ajouter("Échec : Aucune offre intéressante pour " + choco.getNom());
                }
            }
        }
    }

    // 2. Le superviseur donne la liste de toutes les propositions de vente
    public OffreVente choisirOV(List<OffreVente> propositions) {
        OffreVente meilleureOffre = null;
        double meilleurPrix = Double.MAX_VALUE;
        
        // On compare les offres concurrentes pour prendre la moins chère 
        for (OffreVente offre : propositions) {
            double prixPropose = offre.getPrixT();
            
            // Évaluation des conditions : dans un AO, le volume est déjà garanti par le vendeur
            // Mais on peut vérifier le prix maximum qu'on est prêt à payer 
            double prixMaxAcceptable = 2500.0; // À ajuster selon le marché et vos objectifs de marge
            
            if (prixPropose < meilleurPrix && prixPropose <= prixMaxAcceptable) {
                meilleurPrix = prixPropose;
                meilleureOffre = offre;
            }
        }
        
        // On renvoie la meilleure offre au superviseur (ou null si tout le monde est trop cher)
        return meilleureOffre;
    }
}