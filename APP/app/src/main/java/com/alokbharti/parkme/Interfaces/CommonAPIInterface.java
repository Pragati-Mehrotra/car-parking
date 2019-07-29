package com.alokbharti.parkme.Interfaces;

import org.json.JSONObject;

public interface CommonAPIInterface {
    void onSuccessfulHit(JSONObject response);
    void onFailureAPIHit();
}

