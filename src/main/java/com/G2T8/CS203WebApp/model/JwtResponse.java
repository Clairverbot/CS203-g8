package com.G2T8.CS203WebApp.model;

import java.io.Serializable;

/*
* This is class is required for creating a response containing the JWT to be returned to the user.
*/
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final Boolean firstLogin;

    public JwtResponse(String jwttoken, Boolean firstLogin) {
        this.jwttoken = jwttoken;
        this.firstLogin = firstLogin;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Boolean getFirstLogin() {
        return this.firstLogin;
    }
}
