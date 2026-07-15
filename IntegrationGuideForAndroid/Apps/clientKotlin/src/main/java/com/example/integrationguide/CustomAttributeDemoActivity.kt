// Copyright 2026 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.widget.Toast

class CustomAttributeDemoActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, CustomAttributePreferenceFragment())
            .commit()
    }

    class CustomAttributePreferenceFragment : PreferenceFragment() {
        private lateinit var usecases: CustomAttributeUsecases
        private var currentCount = 10

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences_custom_attribute)

            usecases = CustomAttributeUsecases(activity)

            setupSlider()
            setupReadAttributes()
            setupWriteAttributes()
            setupDeleteAttributes()
        }

        private fun setupSlider() {
            findPreference("kvp_count")?.let { pref ->
                (pref as? ListPreference)?.let { listPref ->
                    currentCount = listPref.value?.toIntOrNull() ?: 10
                    updateListSummary(listPref, currentCount)
                    listPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                        currentCount = (newValue as String).toInt()
                        updateListSummary(listPref, currentCount)
                        true
                    }
                }
            }
        }

        private fun updateListSummary(listPref: ListPreference, count: Int) {
            listPref.summary = getString(R.string.pref_kvp_count_summary_format, count)
        }

        private fun setupReadAttributes() {
            findPreference("read_attributes")?.setOnPreferenceClickListener {
                usecases.readAttributes(currentCount) { result ->
                    showToast(result)
                }
                true
            }
        }

        private fun setupWriteAttributes() {
            findPreference("write_attributes")?.setOnPreferenceClickListener {
                usecases.writeAttributes(currentCount) { result ->
                    showToast(result)
                }
                true
            }
        }

        private fun setupDeleteAttributes() {
            findPreference("delete_attributes")?.setOnPreferenceClickListener {
                usecases.deleteAttributes(currentCount) { result ->
                    showToast(result)
                }
                true
            }
        }

        private fun showToast(message: String) {
            activity?.runOnUiThread {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

