package com.pixelpulse.health.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.text.Charsets.UTF_8

/**
 * Security utilities for handling encryption keys and secure storage
 */
object SecurityUtils {
    
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "pixel_pulse_db_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 16
    private const val PREFS_NAME = "secure_prefs"
    private const val DB_KEY_PREF = "database_key"
    
    /**
     * Generates or retrieves the database encryption key
     */
    fun getDatabaseKey(context: Context): String {
        return try {
            // Try to get existing key from secure storage
            getStoredDatabaseKey(context) ?: run {
                // Generate new key if none exists
                val newKey = generateSecureKey()
                storeDatabaseKey(context, newKey)
                newKey
            }
        } catch (e: Exception) {
            // Fallback to a generated key based on device characteristics
            generateFallbackKey(context)
        }
    }
    
    /**
     * Generates a cryptographically secure random key
     */
    private fun generateSecureKey(): String {
        val random = SecureRandom()
        val keyBytes = ByteArray(32) // 256-bit key
        random.nextBytes(keyBytes)
        return keyBytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Stores the database key securely using EncryptedSharedPreferences
     */
    private fun storeDatabaseKey(context: Context, key: String) {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            val encryptedPrefs = EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            
            encryptedPrefs.edit()
                .putString(DB_KEY_PREF, key)
                .apply()
        } catch (e: Exception) {
            // Log error but don't crash - fallback will be used
            android.util.Log.e("SecurityUtils", "Failed to store database key", e)
        }
    }
    
    /**
     * Retrieves the stored database key
     */
    private fun getStoredDatabaseKey(context: Context): String? {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            val encryptedPrefs = EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            
            encryptedPrefs.getString(DB_KEY_PREF, null)
        } catch (e: Exception) {
            android.util.Log.e("SecurityUtils", "Failed to retrieve database key", e)
            null
        }
    }
    
    /**
     * Generates a fallback key based on device characteristics
     * This is less secure but ensures the app can still function
     */
    private fun generateFallbackKey(context: Context): String {
        val deviceId = android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        ) ?: "default_device"
        
        val packageName = context.packageName
        val versionCode = try {
            context.packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: Exception) {
            1
        }
        
        // Create a deterministic but device-specific key
        val keySource = "$deviceId-$packageName-$versionCode-pixel_pulse_2025"
        return keySource.hashCode().toString()
    }
    
    /**
     * Generates a secure key in Android Keystore for additional encryption needs
     */
    fun generateKeystoreKey(): SecretKey? {
        return try {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        } catch (e: Exception) {
            android.util.Log.e("SecurityUtils", "Failed to generate keystore key", e)
            null
        }
    }
    
    /**
     * Encrypts data using Android Keystore
     */
    fun encryptData(data: String): Pair<ByteArray, ByteArray>? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            
            val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            
            val iv = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray(UTF_8))
            
            Pair(encryptedData, iv)
        } catch (e: Exception) {
            android.util.Log.e("SecurityUtils", "Failed to encrypt data", e)
            null
        }
    }
    
    /**
     * Decrypts data using Android Keystore
     */
    fun decryptData(encryptedData: ByteArray, iv: ByteArray): String? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            
            val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            
            val decryptedData = cipher.doFinal(encryptedData)
            String(decryptedData, UTF_8)
        } catch (e: Exception) {
            android.util.Log.e("SecurityUtils", "Failed to decrypt data", e)
            null
        }
    }
    
    /**
     * Clears all stored security data (for logout/reset)
     */
    fun clearSecurityData(context: Context) {
        try {
            // Clear encrypted preferences
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            val encryptedPrefs = EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            
            encryptedPrefs.edit().clear().apply()
            
            // Clear keystore key
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            keyStore.deleteEntry(KEY_ALIAS)
            
        } catch (e: Exception) {
            android.util.Log.e("SecurityUtils", "Failed to clear security data", e)
        }
    }
} 