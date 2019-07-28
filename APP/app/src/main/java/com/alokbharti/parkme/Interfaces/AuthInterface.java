package com.alokbharti.parkme.Interfaces;

public interface AuthInterface {

    void onSignUpSuccessful(int userId);
    void onSignInSuccessful(int userId);
    void onAuthFailed();
}
