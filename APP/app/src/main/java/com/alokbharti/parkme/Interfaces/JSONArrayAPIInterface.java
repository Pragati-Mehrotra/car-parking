package com.alokbharti.parkme.Interfaces;

import org.json.JSONArray;

public interface JSONArrayAPIInterface {
    void onSuccessfulHit(JSONArray response);
    void onFailureAPIHit();
}
