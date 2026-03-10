package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.encheres.MiseAuxEncheres;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

/**
 * @author Théophile Trillat & Elise Dossal
 */
public class Producteur1AcheteurBourse extends Producteur1Acteur implements IAcheteurBourse {

    private double achatMaxParStep;

    public Producteur1AcheteurBourse(double achatMaxParStep) {
        super();
        this.achatMaxParStep = achatMaxParStep;
    }

    
    public double proposerPrix(MiseAuxEncheres miseAuxEncheres){
        return 0.0;
    }

    @Override
    public double demande(Feve f, double cours) {
        if (this.getFeve().equals(f)) {
            BourseCacao bourse = (BourseCacao) Filiere.LA_FILIERE.getActeur("BourseCacao");
            double max    = bourse.getCours(getFeve()).getMax();
            double min    = bourse.getCours(getFeve()).getMin();
            double valeur = bourse.getCours(getFeve()).getValeur();
            double pourcentage = (max - valeur) / (max - min);
            return achatMaxParStep * pourcentage;
        }
        return 0.0;
    }

    @Override
    public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
        this.stockFeve.setValeur(this, this.stockFeve.getValeur() + quantiteEnT);
    }

    @Override
    public void notificationBlackList(int dureeEnStep) {
        this.journal.ajouter("Aie... je suis blackliste... j'aurais du verifier "
                + "que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
    }
}