/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partial;

import java.util.Map;

/**
 *
 * @author USER
 */
public class Validate {
    public boolean validateSubmission(Map<String, Object> data){
        if(!data.isEmpty()){
            for(Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if(value == "" || value == null)
                    return false;
            }
            return true;

        }
        return false;
    }
}
