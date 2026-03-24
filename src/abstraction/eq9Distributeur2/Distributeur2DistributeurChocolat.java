package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation de la vente de chocolat aux clients finaux
 * @author Anass Ouisrani
 * @author Paul Juhel
 */
public class Distributeur2DistributeurChocolat extends Distributeur2Acteur implements IDistributeurChocolatDeMarque {

	// Stockage des quantités en tête de gondole par produit
	protected Map<ChocolatDeMarque, Double> stockTeteGondole;
	
	// Prix au kg pour chaque chocolat
	protected Map<ChocolatDeMarque, Double> prix;
	
	public Distributeur2DistributeurChocolat() {
		super();
		this.stockTeteGondole = new HashMap<>();
		this.prix = new HashMap<>();
	}

	/**
	 * Retourne le prix actuel au kg d'un chocolat
	 * Important : doit retourner la même valeur pendant une même étape
	 */

    public double prix(ChocolatDeMarque choco) {
		// Prix par défaut selon la qualité du chocolat
		// À adapter selon votre stratégie commerciale
		if (!prix.containsKey(choco)) {
			// Prix de base A MODIFIER
			switch (choco.getChocolat()) {
				case C_HQ_E: return 26000.0; // Haut de gamme éthique
				case C_HQ:   return 22000.0; // Haut de gamme
				case C_MQ_E: return 18000.0; // Milieu de gamme éthique
				case C_MQ:   return 16000.0; // Milieu de gamme
				case C_BQ_E: return 14000.0; // Bas de gamme éthique
				case C_BQ:   return 12000.0; // Bas de gamme
				default:     return 0.0;
			}
		}
		return prix.get(choco);
	}

	/**
	 * Retourne la quantité totale disponible à la vente (rayon + tête de gondole)
	 */
    public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		return stock.getOrDefault(choco, 0.0) + stockTeteGondole.getOrDefault(choco, 0.0);
	}

	/**
	 * Retourne la quantité disponible en tête de gondole
	 * Cette quantité ne peut pas dépasser 10% du total mis en vente
	 */
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		double totalDisponible = quantiteEnVente(choco, crypto);
		double maxTeteGondole = totalDisponible * 0.10; // Max 10%
		return Math.min(stockTeteGondole.getOrDefault(choco, 0.0), maxTeteGondole);
	}

	/**
	 * Effectue une vente à un client final
	 * Le client a déjà versé l'argent sur notre compte
	 */
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// Retirer la quantité du stock
		double stockActuel = stock.getOrDefault(choco, 0.0);
		if (stockActuel >= quantite) {
			stock.put(choco, stockActuel - quantite);
			journal.ajouter("Vente de " + quantite + " kg de " + choco.getNom() + 
						   " au client pour " + montant + "€");
		} else {
			journal.ajouter("Stock insuffisant pour la vente de " + choco.getNom());
		}

		// Mettre à jour le stock total
		indicateurStockTotal.setValeur(this, getStockTotal());
	}

	/**
	 * Notification quand le client n'a pas pu acheter toute la quantité désirée
	 * C'est une information importante pour ajuster notre stratégie de stock
	 */
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journal.ajouter("📉 ALERTE RUPTURE - " + choco.getNom() + 
					   " en rupture de stock. Les clients n'ont pas pu acheter la quantité souhaitée.");
		// À améliorer : augmenter les achats de ce produit à la prochaine étape
	}

	/**
	 * Initialiser les stocks de chocolats disponibles à la vente
	 */
	public void initialiser() {
		super.initialiser();
		this.stockTeteGondole.clear();
		
		// Initialiser les quantités en tête de gondole (10% du stock)
		List<ChocolatDeMarque> produits = Filiere.LA_FILIERE.getChocolatsProduits();
		for (ChocolatDeMarque choco : produits) {
			double stockTotal = stock.getOrDefault(choco, 0.0);
			stockTeteGondole.put(choco, stockTotal * 0.10);
		}
		
		journal.ajouter("Initialisation du distributeur de chocolat terminée");
	}
}
