package abstraction.eq7Transformateur4;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

//Auteur -> Aymeric et Paul
public class Transformateur4Vente extends Transformateur4Production implements IVendeurContratCadre {

    @Override
    public boolean vend(IProduit produit) {
        return this.getChocolatsProduits().contains(produit);
    }

    @Override
    public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
        return contrat.getEcheancier();//echeance;
    }

    @Override
    public double propositionPrix(ExemplaireContratCadre contrat) {
        // TODO Auto-generated method stub
        return 6500.;}

    @Override
    public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
        // TODO Auto-generated method stub
        return 6000.;
    }

    @Override
    public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
        //TODO Auto-generated method stub
        System.out.println("Contrat accepté : " + contrat);
    
    }

    @Override
    public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
        double alivrer = Math.min(quantite, this.get_StockChoco_BQ().getValeur());
        this.get_StockChoco_BQ().ajouter(this,-alivrer);
        System.out.println(quantite);
        return alivrer;
    }
    
}