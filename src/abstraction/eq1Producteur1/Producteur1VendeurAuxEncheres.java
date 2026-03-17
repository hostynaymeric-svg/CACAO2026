
package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.encheres.MiseAuxEncheres;
import abstraction.eqXRomu.produits.Feve;

class Producteur1VendeurAuxEncheres extends Producteur1VendeurBourse implements IVendeurAuxEncheres{


    public Producteur1VendeurAuxEncheres(){
        super();
    }

    public void next(){
        super.next();
        if(this.getStock(Feve.F_BQ)>=170){
            Feve feve = Feve.F_BQ  ;
            MiseAuxEncheres mise = new MiseAuxEncheres(this, feve , 170.0, true);
        
        };
        
    }
}