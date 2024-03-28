package com.productcnit;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509ContentVerifierProviderBuilder;
import org.bouncycastle.operator.ContentVerifierProvider;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class BouncyCastleCertificateVerification {

    public static boolean verifyCertificate(X509Certificate certificateToVerify, X509Certificate caCertificate) {
        try {
            // Convert X.509 certificates to CertificateHolder objects
            X509CertificateHolder caCertHolder = new JcaX509CertificateHolder(caCertificate);
            X509CertificateHolder certToVerifyHolder = new JcaX509CertificateHolder(certificateToVerify);

            // Build ContentVerifierProvider for the CA's certificate
            JcaX509ContentVerifierProviderBuilder contentVerifierProviderBuilder = new JcaX509ContentVerifierProviderBuilder();
            ContentVerifierProvider contentVerifierProvider = contentVerifierProviderBuilder.build(caCertHolder.getSubjectPublicKeyInfo());

            // Verify if the certificate was signed by the CA
            return certToVerifyHolder.isSignatureValid(contentVerifierProvider);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        // Load CA's X.509 certificate
        X509Certificate caCertificate = loadCACertificate("ca-cert.pem");
        // Load the certificate to be verified
        X509Certificate certificateToVerify = loadCertificate("client-combined.pem");

        // Extract the public keys
        PublicKey caPublicKey = extractPublicKey(caCertificate);
        PublicKey certificatePublicKey = extractPublicKey(certificateToVerify);

        // Display the encoded public keys
        System.out.println("CA Public Key: " + encode(caPublicKey.getEncoded()));
        System.out.println("CA Public Key size: " + encode(caPublicKey.getEncoded()).length());
        System.out.println("Certificate Public Key: " + certificatePublicKey);

        // Verify the certificate
        boolean isCertificateValid = verifyCertificate(certificateToVerify, caCertificate);

        if (isCertificateValid) {
            System.out.println("Certificate is valid and signed by the CA.");
        } else {
            System.out.println("Certificate is NOT valid or not signed by the CA.");
        }
    }

    private static X509Certificate loadCACertificate(String path) {
        try (InputStream inputStream = BouncyCastleCertificateVerification.class.getClassLoader().getResourceAsStream("certs/" + path)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static X509Certificate loadCertificate(String path) {
        try (InputStream inputStream = BouncyCastleCertificateVerification.class.getClassLoader().getResourceAsStream("certs/" + path)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Method to extract the public key from an X.509 certificate
    public static PublicKey extractPublicKey(X509Certificate certificate) {
        return certificate.getPublicKey();
    }
    public static String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }
}
