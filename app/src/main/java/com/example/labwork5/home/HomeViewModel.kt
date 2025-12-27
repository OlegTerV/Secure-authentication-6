package com.example.labwork5.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.core.net.toUri
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date


class HomeViewModel(): AppCompatActivity() {
    var healthConnectClient: HealthConnectClient? = null
    var availabilityStatus: Int? = null
    val PERMISSIONS =
        setOf(
            //HealthPermission.getReadPermission(HeartRateRecord::class),
            //HealthPermission.getWritePermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class)
        )

    fun initHealthConnect(context: Context) {
        healthConnectClient = HealthConnectClient.getOrCreate(context)
        availabilityStatus = HealthConnectClient.getSdkStatus(context, "com.google.android.apps.healthdata")
        if (healthConnectClient!!.features.getFeatureStatus(HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_IN_BACKGROUND) == HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE){
            throw Exception("Health Connect is unavailable.")
        }

        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            throw Exception("SDK HealthConnect is UNAVAILABLE. Application is not installed.")
        }

        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            val uriString = "market://details?id=com.google.android.apps.healthdata&url=healthconnect%3A%2F%2Fonboarding"
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = uriString.toUri()
                    putExtra("overlay", true)
                    putExtra("callerId", context.packageName)
                }
            )
        }
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
            } else {
                //Toast.makeText(this, "Health connect permission is required to use this feature.", Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun checkPermissions(): Boolean {
        val granted = healthConnectClient!!.permissionController.getGrantedPermissions()
        if (granted.containsAll(PERMISSIONS)) {
            return true
        } else {
            return false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getData(startTime: Long?, endTime: Long?): String {
        if (healthConnectClient == null) {
            return "-"
        }

        val dates = convertLongToInstant(startTime, endTime)

        var stepCount: Long? = null
            try {
            val response = healthConnectClient!!.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(dates[0]!!, dates[1]!!)
                )
            )
            stepCount = response[StepsRecord.COUNT_TOTAL]
        } catch (e: Exception) {
            throw Exception("Get data error")
        }
        return stepCount.toString()
    }

    suspend fun addRange(startTime: Long?, endTime: Long?, countOfSteps: Long) {
        if (healthConnectClient == null) {
            return
        }

        var currentStepsCount = getData(startTime, endTime)

        val dates = convertLongToInstant(startTime, endTime)

        try {
            val stepsRecord = StepsRecord(
                count = (countOfSteps + currentStepsCount.toLong()),
                startTime = dates[0]!!,
                endTime = dates[1]!!,
                startZoneOffset = ZoneOffset.UTC,
                endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.autoRecorded(
                    device = Device(type = Device.TYPE_PHONE)
                )
            )

            healthConnectClient!!.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            throw Exception("Add data error")
        }
    }

    @SuppressLint("Range")
    suspend fun deleteStepsInfo(startTime: Long?, endTime: Long?) {
        if (healthConnectClient == null) {
            return
        }
        val countOfSteps: Long = 0
        val dates = convertLongToInstant(startTime, endTime)

        try {
            val stepsRecord = StepsRecord(
                count = countOfSteps,
                startTime = dates[0]!!,
                endTime = dates[1]!!,
                startZoneOffset = ZoneOffset.UTC,
                endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.autoRecorded(
                    device = Device(type = Device.TYPE_PHONE)
                )
            )

            healthConnectClient!!.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            throw Exception("Add data error")
        }
    }

    @SuppressLint("Range")
    suspend fun reduceStepsCount(startTime: Long?, endTime: Long?, count: Long) {
        if (healthConnectClient == null) {
            return
        }
        var currentStepsCount = getData(startTime, endTime)

        val dates = convertLongToInstant(startTime, endTime)

        try {
            val stepsRecord = StepsRecord(
                count = (currentStepsCount.toLong() - count),
                startTime = dates[0]!!,
                endTime = dates[1]!!,
                startZoneOffset = ZoneOffset.UTC,
                endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.autoRecorded(
                    device = Device(type = Device.TYPE_PHONE)
                )
            )

            healthConnectClient!!.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            throw Exception("Add data error")
        }
    }

    //скатал со stackOverflow
    @SuppressLint("SimpleDateFormat")
    fun convertDateLongToString(date: Long?): String {
        if (date != null) {
            val date = Date(date)
            val format = SimpleDateFormat("dd.MM.yyyy")
            return format.format(date)
        }
        else {
            return ""
        }
    }

    //скатал откуда-то
    @SuppressLint("SimpleDateFormat")
    fun convertLongToInstant(dateStart: Long?, dateEnd: Long?): ArrayList<Instant?> {
        val result = arrayListOf<Instant?>()
        if (dateStart != null) {
            val date = Date(dateStart)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val defDate = format.format(date)
            val defTime = "00:00"
            result.add(ZonedDateTime.of(LocalDate.parse(defDate), LocalTime.parse(defTime), ZoneId.systemDefault()).toInstant())
        }
        if (dateEnd != null) {
            val date = Date(dateEnd)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val defDate = format.format(date)
            val defTime = "23:59"
            result.add(ZonedDateTime.of(LocalDate.parse(defDate), LocalTime.parse(defTime), ZoneId.systemDefault()).toInstant())
        }

        return result
    }

    //скатал откуда-то
    @SuppressLint("SimpleDateFormat")
    fun convertLongToInstant(date: Long?): Instant? {
        var result: Instant? = null
        if (date != null) {
            val date = Date(date)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val defDate = format.format(date)
            val defTime = "00:00"
            result = ZonedDateTime.of(LocalDate.parse(defDate), LocalTime.parse(defTime), ZoneId.systemDefault()).toInstant()
        }

        return result
    }
}