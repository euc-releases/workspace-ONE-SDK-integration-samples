// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import android.net.Uri
import com.airwatch.privacy.*
import com.airwatch.privacy.datamodels.AWPrivacyAppDataType
import com.airwatch.privacy.AWPrivacyConfig.Companion.APP_DATA_DIAGNOSTICS
import com.airwatch.privacy.AWPrivacyConfig.Companion.dataCollectionDefaultItems
import java.util.*
import kotlin.collections.ArrayList

typealias AWPrivacyList = ArrayList<AWPrivacyContent>

// Base class for privacy helpers. The only configuration it does is to set the
// application name. This class can be used as a minimal implementation so that
// the consent-checking and preview can be coded in an Activity.
open class PrivacyBase(
    private val context: Context,
    sharedPreferencesName: String = defaultSharedPreferencesName
) {
    init {
        // 1. Module initialization............................................
        AWPrivacyController.initialize(
            context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE
            )
        )
    }

    // Underlying property for the agreement configuration.
    private val agreement: AWPrivacyConfig by lazy {
        configureAgreement(context).apply {
            if (appPermissionItems == null) {
                appPermissionItems = AWPrivacyList()
            }
            if (dataCollectionItems == null) {
                dataCollectionItems = AWPrivacyList()
            }
        }
    }

    // Override this function and call super to configure the agreement in a
    // subclass.
    // 2. Content initialization..............................................
    protected open fun configureAgreement(context: Context): AWPrivacyConfig {
        return AWPrivacyConfig().apply {
            applicationName = context.packageManager.getApplicationLabel(
                context.applicationInfo).toString()
        }
    }
    // It might be nicer to call configureAgreement in the init() and so avoid
    // having a Context property. It seems in general to be a bad idea to retain
    // Context objects. However, Android Studio warns that the code is:
    //
    //     Calling non-final function configureAgreement in constructor
    //
    // So, the above code is used instead. It retains a Context as a property
    // boo.

    companion object {
        // Constant for "com.airwatch.privacy"
        val defaultSharedPreferencesName: String =
            AWPrivacyController::class.java.`package`!!.name

        // Utility function to create an AWPrivacyCallback object from a lambda.
        fun privacyCallback(
            callback: (result: AWPrivacyResult) -> Unit
        ): AWPrivacyCallback
        {
            // Next statement creates an anonymous object.
            // See https://kotlinlang.org/docs/reference/object-declarations.html
            // And TOTH https://stackoverflow.com/a/17516998/7657675
            return object : AWPrivacyCallback() {
                override fun onComplete(result: AWPrivacyResult) =
                    callback(result)
            }
        }

        // Factory functions for creating AWPrivacyContent instances.  
        // The following factory functions aren't included in the PrivacyBase
        // class as it appears in the integration guide.
        fun privacyContent(
            title: String, summary: String
        ) : AWPrivacyContent = AWPrivacyContent().apply {
            this.title = title
            this.summary = summary
        }
        fun privacyContent(
            title: String, summary: String, id: Int
        ) : AWPrivacyContent = privacyContent(title, summary).apply {
            this.id = id
        }
        fun privacyContent(
            title: String, summary: String, id: AWPrivacyPermissionType
        ) : AWPrivacyContent = privacyContent(
            title, summary, AWPrivacyController.getPermissionResource(id)
        )
        fun privacyContent(
            context: Context, titleID: Int, summaryID: Int, iconID: Int
        ) : AWPrivacyContent = privacyContent(
            context.getString(titleID),
            context.getString(summaryID),
            iconID
        )
    }

    // Utility properties and methods for an idiomatic Kotlin interface to the
    // AWPrivacyController object.
    // 3. Consent check..................................................................
    val consentRequired:Boolean get() = AWPrivacyController.getConsentRequired(agreement)

    // 4.  Consent user interface flow...................................................
    fun startPrivacyFlow(context: Context, callback: (result: AWPrivacyResult) -> Unit) {
        AWPrivacyController.startPrivacyFlow(
            context, agreement, privacyCallback(callback)
        )
    }

    fun reviewPrivacy(
        context: Context, callback: (result: AWPrivacyResult) -> Unit
    ) {
        AWPrivacyController.previewPrivacy(
            context, agreement, privacyCallback(callback)
        )
    }

}

class Privacy(
    context: Context,
    sharedPreferencesName: String = defaultSharedPreferencesName
) : PrivacyBase(context, sharedPreferencesName)
{
    companion object {
    }

    // Domain in a String.
    private val domain = "example.com"
    //
    // Properly built URI in a String.
    private val domainURI = Uri.Builder()
        .scheme("https").authority(domain).build().toString()

    override fun configureAgreement(context: Context): AWPrivacyConfig
    { return super.configureAgreement(context).apply {
        // Set application name
        applicationName = context.getString(R.string.privacy_applicationName)

        // Change to `true` to allow rejection of the privacy agreement.
        privacyRejectShow = false

        // Specify data collection items for the agreement, as follows.
        //
        // 1.  Copy the default set of data collection items.
        // 2.  Remove the APP_DATA_DIAGNOSTICS item.
        // 3.  Add a custom item for browsing history.
        // 4.  Add a built-in item, APP_DATA_NOTIFICATIONS, that isn't in the
        //     default set.
        dataCollectionItems = dataCollectionDefaultItems(context)
            .apply {
                removeAll { it.contentType == APP_DATA_DIAGNOSTICS }

                add(
                    // Simple resource-based configuration.
                    privacyContent(
                        context,
                        R.string.privacy_content_browsingHistory_title,
                        R.string.privacy_content_browsingHistory_summary,
                        R.drawable.privacy_placeholder_2
                    )
                )

                AWPrivacyController.getAppDataContent(
                    context, AWPrivacyAppDataType.APP_DATA_NOTIFICATIONS
                )?.also { add(it) }
            }

        // Alternative specification, starting with an empty list.
        //
        // dataCollectionItems = ArrayList(listOf(
        //    AWPrivacyController.getAppDataContent(
        //        context, AWPrivacyAppDataType.APP_DATA_DEVICE_HARDWARE)!!
        // ))

        // Specify app permissions. Some of the built-in permissions and a
        // custom one.
        appPermissionTitle = "Sample App Permissions"
        appPermissionItems = ArrayList( listOf(
            // Code-based configuration.
            // Comment and uncomment the first privacyContent item to change the
            // privacy agreement content and hence generate a new prompt for the
            // user to consent.
//            privacyContent(
//                "Camera", "Required for taking sample photos.",
//                AWPrivacyPermissionType.PERMISSION_CAMERA
//            ),

            privacyContent(
                "Phone", "Placing sample calls and sending text messages.",
                AWPrivacyPermissionType.PERMISSION_PHONE
            ),
            privacyContent(
                "Location", "Sampling GPS or another location service.",
                AWPrivacyPermissionType.PERMISSION_LOCATION_SERVICES
            ),
            privacyContent(
                "Network", "Connect to sample services on the internet.",
                AWPrivacyPermissionType.PERMISSION_NETWORK
            ),
            privacyContent(
                "AN Other Permission",
                "Another permission with a custom icon goes here.",
                //R./* mipmap or drawable for example */ . /* resource name */
                R.mipmap.privacy_placeholder_1
            ),
            privacyContent(
                "Custom permission",
                "Another permission, without an icon, goes here."
            )
        ) )

        // Specify the enterprise policy description and link, but leave the
        // default title.
        enterprisePolicyDescription = listOf(
            "Tap here to read your company's privacy policy.",
            "(Opens $domain in this sample.)"
        ).joinToString(" ")
        enterprisePolicyLink = domainURI

        // Don't show the additional data sharing opt-in.
        dataSharingShow = false
    } }
}

// PrivacyTellTale is a special privacy helper that sets tell-tale values into
// every configurable property. It is for diagnostic purposes only.
//
// Utility extensions used in the PrivacyTellTale special helper.
private fun String.ig() = "IG.$this"
private fun String.igd(repetitions: Int) =
    this.ig() + " $this".spaced().repeat(repetitions)
private fun String.igd() = this.igd(8)
private val spacedRegex = Regex("[\\[.A-Z]")
private fun String.spaced() = spacedRegex.replace(this) {" " + it.value}

private const val defaultResourcePrefix = "privacy_placeholder_"

class PrivacyTellTale(
    context: Context, sharedPreferencesName: String, resourcePrefix: String
) : PrivacyBase(context, sharedPreferencesName)
{
    companion object {
        fun Prefixed(
            context: Context, resourcePrefix: String
        ):PrivacyTellTale
        {
            return PrivacyTellTale(
                context, defaultSharedPreferencesName, resourcePrefix
            )
        }

        // Create a URI that will display the specified value. The trick is to
        // send the value to an internet search engine as a query parameter.
        fun exampleUri(displaying: String): String = Uri.Builder()
            .scheme("https")
            .authority("duckduckgo.com").appendQueryParameter("q", displaying)
            .build().toString()
    }

    constructor(context: Context) :
            this(context, defaultSharedPreferencesName, defaultResourcePrefix)

    constructor(context: Context, sharedPreferencesName: String) :
            this(context, sharedPreferencesName, defaultResourcePrefix)

    private val dataCollectionAll: AWPrivacyList
    private val appPermissionsAll: AWPrivacyList
    init {
        // Build a list of custom content items. They're found by looking for
        // drawable and mipmap resources with a name in the form:
        // reourcePrefixN where N starts from 1 and goes up.
        var phIndex = 1
        val customContentItems = mutableListOf<AWPrivacyContent>()
        do {
            var resourceID = 0
            val resourceType = listOf("mipmap", "drawable").firstOrNull {
                // TOTH: https://stackoverflow.com/a/6583926/7657675
                resourceID = context.resources.getIdentifier(
                    "$resourcePrefix$phIndex", it, context.packageName
                )

                resourceID != 0
            } ?: break
            // Elvis break on the above line will break out of the do loop if no
            // resource with the name is found.

            customContentItems.add(AWPrivacyContent().apply {
                id = resourceID
                title = resourceType
            })

            phIndex += 1
        } while (true)

        // Function to copy and modify the custom resources.
        fun customCopy(prefix: String): List<AWPrivacyContent> {
            return customContentItems.mapIndexed { index, custom ->
                AWPrivacyContent().apply {
                    id = custom.id
                    title = "$prefix[${index + 1}].title".ig()
                    summary = "${custom.title} ".plus(
                        "$prefix[${index + 1}].summary".igd(4)
                    )
                }
            }
        }

        // List of all data collection items.
        dataCollectionAll = ArrayList(customCopy("dataCollection").plus(
            AWPrivacyAppDataType.values().mapNotNull {
                AWPrivacyController.getAppDataContent(context, it)?.apply {
                    summary = title + "\n" + summary
                    title = it.name
                }
            }
        ))

        // List of all app permission items.
        appPermissionsAll = ArrayList(customCopy("appPermission").plus(
            AWPrivacyPermissionType.values().map {
                AWPrivacyContent().apply {
                    id = AWPrivacyController.getPermissionResource(it)
                    title = it.name
                    val summaryStub = it.name
                        .lowercase(Locale.ROOT)
                        .replaceFirst("permission_", "")
                    summary = "${it.name}.summary".ig().plus(
                        " $summaryStub summary".repeat(4)
                    )
                }
            }
        ))
    }

    override fun configureAgreement(context: Context): AWPrivacyConfig
    { return super.configureAgreement(context).apply {
        applicationName = "applicationName".ig()

        // Allow user to decline the agreement.
        privacyRejectShow = true

        dataSharingShow = true
        dataSharingTitle = "dataSharingTitle".ig()
        dataSharingDescription = "dataSharingDescription".igd()
        dataSharingNavigation = "dataSharingNavigation".ig()

        dataCollectionTitle = "dataCollectionTitle".ig()
        dataCollectionDescription = "dataCollectionDescription".igd()
        dataCollectionItems = dataCollectionAll

        appPermissionTitle = "appPermissionTitle".ig()
        appPermissionDescription = "appPermissionDescription".igd()
        appPermissionItems = appPermissionsAll

        enterprisePolicyShow = true
        enterprisePolicyTitle = "enterprisePolicyTitle".ig()
        enterprisePolicyDescription = "enterprisePolicyDescription".igd()
        enterprisePolicyLink = exampleUri("enterprisePolicyLink".ig())
    } }
}

// PrivacyGallery is a special privacy helper that shows:
//
// -   All the app permission image resources and their constant names.
// -   All the built-in data content privacy items and their constant names.
class PrivacyGallery(context: Context)
    : PrivacyBase(context, defaultSharedPreferencesName)
{
    override fun configureAgreement(context: Context): AWPrivacyConfig
    { return super.configureAgreement(context).apply {
        // Show the default data-sharing agreement.
        privacyRejectShow = true

        dataCollectionTitle = AWPrivacyAppDataType::class.simpleName
        dataCollectionItems = ArrayList(
            AWPrivacyAppDataType.values().mapNotNull {
                AWPrivacyController.getAppDataContent(context, it)?.apply {
                    summary = title + "\n" + summary
                    title = it.name
                }
            }
        )

        appPermissionTitle = AWPrivacyPermissionType::class.simpleName
        appPermissionItems = ArrayList(
            AWPrivacyPermissionType.values().map {
                AWPrivacyContent().apply {
                    id = AWPrivacyController.getPermissionResource(it)
                    title = it.name
                }
            }
        )

        dataSharingShow = true
        dataSharingDescription = context.resources.getString(
            R.string.privacy_dataSharing_description)
    } }
}

