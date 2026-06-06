package thesweetspot.patterns.nullobject;

import thesweetspot.*;
import java.sql.Date;
import thesweetspot.Data.productData;

public class nullProductData extends productData {

     public nullProductData() {
        super(
            -1,                    // id
            "N/A",                // productId
            "No Product",         // productName
            "None",               // type
            0,                    // stock
            0.0,                  // price
            "Unavailable",        // status
            "",                   // image path
            new Date(System.currentTimeMillis())            // date
        );
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
    
