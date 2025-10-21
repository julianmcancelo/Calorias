package com.calorias.ai.health

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.permission.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthConnectRepository(private val context: Context) {

    fun isAvailable(): Boolean = HealthConnectClient.isAvailable(context)

    fun permissions(): Set<Permission> = setOf(
        Permission.createReadPermission(NutritionRecord::class),
        Permission.createWritePermission(NutritionRecord::class)
    )

    fun client(): HealthConnectClient = HealthConnectClient.getOrCreate(context)

    suspend fun hasAllPermissions(): Boolean = withContext(Dispatchers.IO) {
        val granted = client().permissionController.getGrantedPermissions()
        permissions().all { granted.contains(it) }
    }

    suspend fun createPermissionsRequest(): IntentSenderRequest = withContext(Dispatchers.IO) {
        val controller: PermissionController = client().permissionController
        val pi = controller.createRequestPermissionIntent(permissions())
        IntentSenderRequest.Builder(pi).build()
    }
}
