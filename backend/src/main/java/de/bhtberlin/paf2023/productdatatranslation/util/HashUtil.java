package de.bhtberlin.paf2023.productdatatranslation.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Utility class for hashing.
 */
public class HashUtil {

    /**
     * Get the sha256 hash for a String.
     *
     * @param str The String that should be hashed.
     * @return The hashed String.
     */
    public static @NotNull String sha256(@NotNull String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (Exception e) {
            return str;
        }
    }
}
