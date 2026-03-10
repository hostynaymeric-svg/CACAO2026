package abstraction.eq1Producteur1;
import abstraction.eqXRomu.produits.Feve;

public class Lot {
    private Feve f;
    private Date dateCreation;
    private Date dataPeremption;
    private double quantite;
    
    public Lot(Feve f, Date dateCreation, Date dataPeremption, double quantite){
        this.f = f;
        this.dateCreation=dateCreation;
        this.dataPeremption = dataPeremption;
        this.quantite= quantite;

    }

    public double getQuantite(){
        return this.quantite;
    }

    public Feve getGamme(){

        return this.f;
    }

    public Date getPeremptionDate(){
        return this.dataPeremption;
    }

    public Date getCreationDate(){
        return this.dateCreation;
    }

}
