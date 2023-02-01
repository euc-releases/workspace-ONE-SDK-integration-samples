// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.airwatch.privacy.*
import com.airwatch.privacy.datamodels.AWPrivacyAppDataType
import com.airwatch.privacy.AWPrivacyConfig.Companion.dataCollectionDefaultItems
import org.xmlpull.v1.XmlPullParser

class PrivacyXML(
    context: Context, sharedPreferencesName: String,
    private val xmlResource: Int
) : PrivacyBase(context, sharedPreferencesName) {

    constructor(context: Context, xmlResource: Int) :
            this(context, defaultSharedPreferencesName, xmlResource)

    constructor(context: Context) :
            this(context, defaultSharedPreferencesName, R.xml.privacy_agreement)

    sealed class Node {
        data class Config(
            val body: AWPrivacyConfig, override val parent: Node? = null
        ) : Node()
        data class EnterprisePolicy(override val parent: Config) : Node()
        data class DataCollectionList(
            val body: AWPrivacyList, override val parent: Config) : Node()
        data class AppPermissionList(
            val body: AWPrivacyList, override val parent: Config) : Node()
        data class Item(
            val body: AWPrivacyContent, override val parent: Node) : Node()

        abstract val parent:Node?
    }

    data class State(val parser: XmlPullParser, val context: Context) {
        // Split the text from the XML into lines, trim whitespace from the
        // start and end of each line, join back together with spaces.
        fun text():String = parser.text.lines().joinToString(" ") { it.trim() }

        fun exception(vararg strings:String):Exception = Exception(listOf(
            *strings,
            " line:${parser.lineNumber}",
            if (parser.columnNumber < 0)
                "" else " column:${parser.columnNumber}",
            "."
        ).joinToString(""))
    }

    override fun configureAgreement(context: Context): AWPrivacyConfig
    { return super.configureAgreement(context).also {
        val state = State(context.resources.getXml(xmlResource), context)

        var parent:Node = Node.Config(it)
        val tags = mutableListOf<Tag>()
        while (true) { when (state.parser.next()) {
            XmlPullParser.END_DOCUMENT -> break
            XmlPullParser.START_DOCUMENT -> continue
            XmlPullParser.START_TAG -> {
                tags.add(Tag.valueOf(state.parser.name))
                parent = tags.last().start(parent, state)
            }
            XmlPullParser.END_TAG -> {
                parent = tags.last().end(parent) ?: parent
                tags.removeLast()
            }
            XmlPullParser.TEXT -> tags.last().text(parent, state)
        } }
    } }

    enum class Attribute {
        contentType, dataSharingShow, rejectShow, resource;

        fun get(state: State):String? {
            return state.parser.getAttributeValue(null, this.name)
        }
    }

    enum class Tag {
        privacyAgreement {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.Config -> node.apply { with(body) {
                    privacyRejectShow =
                        Attribute.rejectShow.get(state).toBoolean()
                    dataSharingShow =
                        Attribute.dataSharingShow.get(state).toBoolean()
                } }
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
        },
        applicationName {
            override fun text(node: Node, state: State) = when(node) {
                is Node.Config -> node.body.applicationName = state.text()
                else -> throw state.exception("Can't set text ${this.name} in $node")
            }
        },
        dataCollection {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.Config -> Node.DataCollectionList(AWPrivacyList(), node)
                    .apply { node.body.dataCollectionItems = body }
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
            override fun end(node:Node):Node? {return node.parent}
        },
        appPermissions {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.Config -> Node.AppPermissionList(AWPrivacyList(), node)
                    .apply { node.body.appPermissionItems = body }
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
            override fun end(node:Node):Node? {return node.parent}
        },
        defaultItems {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.DataCollectionList -> node.apply {
                    body.addAll(dataCollectionDefaultItems(state.context))
                }
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
        },
        remove {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.DataCollectionList -> node.apply {
                    val removeType = Attribute.contentType.get(state)?.let {
                        try {
                            AWPrivacyAppDataType.valueOf(it)
                        }
                        catch (_: Exception) {
                            throw state.exception(
                                "Unknown ", Attribute.contentType.name,
                                " value \"", it, "\".")
                        }
                    } ?: throw state.exception(
                        "Missing ", Attribute.contentType.name, " attribute.")
                    AWPrivacyController.getAppDataContent(
                        state.context, removeType)
                        ?.also { removeMatch -> body.removeAll {
                            it.contentType == removeMatch.contentType
                        } }
                    }

                else -> throw state.exception("Can't start ${this.name} in $node")
            }
        },
        item {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.DataCollectionList -> {
                    // If there's a contentType attribute, start the item as a
                    // copy of the built-in AWPrivacyContent.
                    // Otherwise, construct a new one.
                    val item = Attribute.contentType.get(state)?.let {
                        try {
                            AWPrivacyAppDataType.valueOf(it)
                        } catch (_: Exception) {
                            throw state.exception(
                                "Unknown ", Attribute.contentType.name,
                                " value \"", it, "\"."
                            )
                        }
                    }?.let {
                        AWPrivacyController.getAppDataContent(state.context, it)
                    } ?: AWPrivacyContent()

                    Node.Item(item, node).also {
                        node.body.add(it.body)
                    }
                }
                is Node.AppPermissionList -> Node.Item(AWPrivacyContent(), node)
                    .also { node.body.add(it.body) }
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
            override fun end(node:Node):Node? {return node.parent}
        },
        icon {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.Item -> node.apply {
                    Attribute.resource.get(state)?.also {
                        body.id = state.context.resources.getIdentifier(
                            it, "mipmap", state.context.packageName)
                    }
                    Attribute.contentType.get(state)?.also {
                        try {
                            AWPrivacyPermissionType.valueOf(it)
                        }
                        catch (_: Exception) {
                            throw state.exception(
                                "Unknown ", Attribute.contentType.name,
                                " value \"", it, "\".")
                        }.also { permissionType ->
                            body.id = AWPrivacyController
                                .getPermissionResource(permissionType)
                        }
                    }
                }
                else -> throw state.exception(
                    "Can't start ${this.name} in $node")
            }
        },
        title {
            override fun text(node: Node, state: State) = when(node) {
                is Node.Item -> node.body.title = state.text()
                is Node.DataCollectionList ->
                    node.parent.body.dataCollectionTitle = state.text()
                is Node.AppPermissionList ->
                    node.parent.body.appPermissionTitle = state.text()
                else -> throw state.exception("Can't set text ${this.name} in $node")
            }
        },
        summary {
            override fun text(node: Node, state: State) = when(node) {
                is Node.Item -> node.body.summary = state.text()
                else -> throw state.exception("Can't set text ${this.name} in $node")
            }
        },
        enterprisePolicy {
            override fun start(node: Node, state: State): Node = when(node) {
                is Node.Config -> Node.EnterprisePolicy(node)
                else -> throw state.exception("Can't start ${this.name} in $node")
            }
            override fun end(node:Node):Node? {return node.parent}
        },
        link {
            override fun text(node: Node, state: State) = when(node) {
                is Node.EnterprisePolicy -> {
                    val text = state.text()

                    // If the value parses into a URI with a scheme, then OK.
                    //
                    // Plan B is to use the built-in URL guesser to get a URL
                    // but change http to https in the scheme.
                    //
                    // Plan C in case the built-in guesser returns null is to
                    // add "https://" to the front of the value.
                    // The web view will at least attempt to open it and show an
                    // error screen that includes the URL.
                    fun guess(): String = URLUtil.guessUrl(text)?.run {
                            Uri.parse(this)?.run {
                                if (this.scheme == "http")
                                    this.buildUpon().scheme("https").build()
                                else
                                    this
                            }?.toString()
                        }
                        ?: "https://$text"

                    node.parent.body.enterprisePolicyLink =
                        Uri.parse(text).takeUnless { it.scheme.isNullOrEmpty() }
                            ?.toString()
                            ?: guess()
                }
                else -> throw state.exception(
                    "Can't set text ${this.name} in $node")
            }
        },
        description {
            override fun text(node: Node, state: State) = when(node) {
                is Node.EnterprisePolicy ->
                    node.parent.body.enterprisePolicyDescription = state.text()
                else -> throw state.exception(
                    "Can't set text ${this.name} in $node")
            }
        };

        open fun start(node: Node, state: State):Node {return node}
        open fun end(node:Node):Node? {return node}
        open fun text(node: Node, state: State) {}
    }
}
