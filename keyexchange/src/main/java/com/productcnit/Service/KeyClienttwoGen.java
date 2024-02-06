package com.productcnit.Service;

public class KeyClienttwoGen {
    public static void main(String[] args) {
        // Save publicKey and send to the other client

        String Publickeyown = "MIICKDCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgA4IBBQACggEABzRwOMhGb611iP6lryklHzFfKIAW9KbWnDWNuVUERYu1Vl5X+bEAf8jl8ZJyER32YgCAGNk68oqao3z8nepqMpanM7pQGepIeSnP0i2U5Z0TT9T9D2yH/eoPMwgPeK5DQ6DRdjdO1NJ8ijIEm6xmV8cICAVR4KObvQfCyducq3vYnGfpL/lsT1hAmGqlxN2UVpetfQ3ZFrz7zTrjTSbRsHNYJL6IWsCK/26R2AaeHr0T1iK4EJKvxozE72O1XEbN+ddBQ/cizN26P7mMVpDidQqMURkOhMOVzsL5JekXafW1p4+nW8jmPDNwI+NwcKt2q7TKnRBuKzznFMgF6cGbHQ==";

        String Privatekeypeer= "MIIBQwIBADCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgBB8CHQCoqyAzDEMoajjJoMWvHFSMQsaGD5jD1Rds5RtB";

        //In the other client's class (KeyRecService)

        String Publickeypeer="MIICKTCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgA4IBBgACggEBAOMP1I2SCuZexA7JcuKKetuUkK0R75Z+Qs8jDO+rlCoBQWivbf8Yka2wnRPYNmonp1IXh500sm83ojxAQw1yDcHQwblTKBgDB6SeYDYNVlkfFU5b1xKW2H0k5BuALjIB0lKmc9OVHunNRbFQyBJsXDSdbIKkwAlgOLV+SmTI1PlMoGDzF80abseiWhU06m+3FDzkPb7C2T6lOJXlAigiGUxca4/R5PE3vrzzEPeODK3wRzrnej5vNc45+J0CPpGoR5CNxxLGLlzpz56H1aIU9Rhrnj1K52ytrUzxh+1epkHW735P6u8hAGc1tn0IgoBc75TpMxPLbb4IQEuH0nwwdt8=";
        KeyManager keyManager1= new KeyManager();
//        keyManager1.generateKeyPair();
        keyManager1.initFromStringsPublickey(Publickeypeer);
        keyManager1.initFromStringsPrvkey(Privatekeypeer);
        String sharedKey = keyManager1.generateSharedSecret();
        System.out.println("Shared key: " + sharedKey);
    }
}
