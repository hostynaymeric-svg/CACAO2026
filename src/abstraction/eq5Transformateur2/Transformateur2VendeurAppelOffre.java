package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.bourseCacao.BourseCacao;

/**
 * @author Pierre GUTTIEREZ
 */
public class Transformateur2VendeurAppelOffre extends Transformateur2AchatAppelOffre implements IVendeurAO{

    public Transformateur2VendeurAppelOffre() {
        super();
    }

	public OffreVente proposerVente(AppelDOffre offre){
        IProduit p = offre.getProduit();
            
        // Si le produit n'est pas un chocolat de marque, on refuse
        if (!(p instanceof ChocolatDeMarque)) {
            return null;
        }
            
        ChocolatDeMarque cdm = (ChocolatDeMarque) p;
            
        // On s'assure qu'on ne vend que NOTRE marque
        if (!cdm.getNom().equals("Ferrara Rocher")) {
            return null;
        }

        double stockDispo = this.getStock_chocolatDeMarque(cdm);
        if (stockDispo < offre.getQuantiteT()) {
            return null; // Pas assez de stock, on passe notre tour
        }

        // 4. On calcule le prix et on fait l'offre (sans lancer aucune production ici !)
        double coursMQ = ((BourseCacao) (Filiere.LA_FILIERE.getActeur("BourseCacao"))).getCours(Feve.F_MQ).getValeur();
        double prixVente = coursMQ * 1.18 * offre.getQuantiteT();

        return new OffreVente(offre, this, cdm, prixVente);
    }

	public void notifierVenteAO(OffreVente propositionRetenue){
        ChocolatDeMarque cdm = (ChocolatDeMarque) propositionRetenue.getProduit();
        this.remove_chocolatDeMarque(cdm, propositionRetenue.getQuantiteT());
        this.getJournaux().get(7).ajouter(propositionRetenue.toString()+ "\n");
    }

	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee){
        this.getJournaux().get(7).ajouter(propositionRefusee.toString()+ "\n");
    }
}