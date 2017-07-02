package com.ghosty.desktop.mkm

import static com.ghosty.desktop.mkm.auth.AuthenticationHelper.requestProtectedResource

class Main {

    static void main(String[] args) {
        println requestProtectedResource('https://www.mkmapi.eu/ws/v1.1/output.json/account', 'GET')
    }
}
