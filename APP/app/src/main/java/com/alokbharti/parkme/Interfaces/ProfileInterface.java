package com.alokbharti.parkme.Interfaces;

import com.alokbharti.parkme.Pojo.UserInfo;

public interface ProfileInterface {
    void onGetProfileInfo(UserInfo userInfo);
    void onGetProfileInfoFailed();
}
