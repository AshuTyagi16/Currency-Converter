package com.multicurrency.app.core_preferences.shared.impl

import com.multicurrency.app.core_preferences.shared.api.PreferenceApi
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings

@OptIn(ExperimentalSettingsApi::class)
internal class PreferenceApiImpl(
    settings: Settings,
) : PreferenceApi {

    private val suspendSettings = settings.toSuspendSettings()

    override suspend fun getString(key: String): String? {
        return suspendSettings.getStringOrNull(key)
    }

    override suspend fun getString(key: String, defaultValue: String): String {
        return suspendSettings.getString(key, defaultValue)
    }

    override suspend fun putString(key: String, value: String) {
        suspendSettings.putString(key, value)
    }

    override suspend fun getInt(key: String): Int? {
        return suspendSettings.getIntOrNull(key)
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return suspendSettings.getInt(key, defaultValue)
    }


    override suspend fun putInt(key: String, value: Int) {
        suspendSettings.putInt(key, value)
    }


    override suspend fun getLong(key: String): Long? {
        return suspendSettings.getLongOrNull(key)
    }


    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return suspendSettings.getLong(key, defaultValue)
    }


    override suspend fun putLong(key: String, value: Long) {
        suspendSettings.putLong(key, value)
    }

    override suspend fun getFloat(key: String): Float? {
        return suspendSettings.getFloatOrNull(key)
    }


    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return suspendSettings.getFloat(key, defaultValue)
    }


    override suspend fun putFloat(key: String, value: Float) {
        suspendSettings.putFloat(key, value)
    }

    override suspend fun getDouble(key: String): Double? {
        return suspendSettings.getDoubleOrNull(key)
    }


    override suspend fun getDouble(key: String, defaultValue: Double): Double {
        return suspendSettings.getDouble(key, defaultValue)
    }


    override suspend fun putDouble(key: String, value: Double) {
        suspendSettings.putDouble(key, value)
    }


    override suspend fun getBoolean(key: String): Boolean? {
        return suspendSettings.getBooleanOrNull(key)
    }


    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return suspendSettings.getBoolean(key, defaultValue)
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        suspendSettings.putBoolean(key, value)
    }

    override suspend fun remove(key: String) {
        suspendSettings.remove(key)
    }

    override suspend fun clearAll() {
        suspendSettings.clear()
    }
}