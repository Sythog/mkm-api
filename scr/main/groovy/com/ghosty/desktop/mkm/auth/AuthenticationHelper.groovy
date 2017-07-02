package com.ghosty.desktop.mkm.auth

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import static java.lang.System.currentTimeMillis
import static java.net.URLEncoder.encode

class AuthenticationHelper {

    private static String appToken = 'RpNj8XJRwb5Qhdjt'
    private static String appSecret = 'tSiirAFYyA7dSVV3GYFWHNyfRDfwLFM0'
    private static String accessToken = 'YMpXqrUc23Lc9f7nUwAiyP7yGdJy3U4b'
    private static String accessTokenSecret = 'nJJ2duQtflVEq8JvAE6fO4kcdLrqVfML'

    static String requestProtectedResource(String url, String httpMethod) {
        def realm = url
        def oauthVersion = '1.0'
        def oauthConsumerKey = appToken
        def oauthToken = accessToken
        def oauthSignatureMethod = 'HMAC-SHA1'
        def oauthTimestamp = (currentTimeMillis() / 1000) as long
        def oauthNonce = UUID.randomUUID().toString().replace '-', ''

        def baseString = "$httpMethod&${urlEncode url}&"
        def paramString = "oauth_consumer_key=${urlEncode oauthConsumerKey}&" +
                "oauth_nonce=${urlEncode oauthNonce}&" +
                "oauth_signature_method=${urlEncode oauthSignatureMethod}&" +
                "oauth_timestamp=${urlEncode oauthTimestamp as String}&" +
                "oauth_token=${urlEncode oauthToken}&" +
                "oauth_version=${urlEncode oauthVersion}"
        baseString += urlEncode paramString

        def singingKey = "${urlEncode appSecret}&${urlEncode accessTokenSecret}"
        def mac = Mac.getInstance 'HmacSHA1'
        def secret = new SecretKeySpec(singingKey.bytes, mac.algorithm)
        mac.init secret
        def digest = mac.doFinal baseString.bytes
        def oauthSignature = digest.encodeBase64()

        def authHeader = "OAuth realm=\"$realm\", " +
                "oauth_version=\"$oauthVersion\", " +
                "oauth_timestamp=\"$oauthTimestamp\", " +
                "oauth_nonce=\"$oauthNonce\", " +
                "oauth_consumer_key=\"$oauthConsumerKey\", " +
                "oauth_token=\"$oauthToken\", " +
                "oauth_signature_method=\"$oauthSignatureMethod\", " +
                "oauth_signature=\"$oauthSignature\""

        url.toURL().getText requestProperties: [Authorization: authHeader]
    }

    private static String urlEncode(String str) {
        encode str, "UTF-8"
    }
}
