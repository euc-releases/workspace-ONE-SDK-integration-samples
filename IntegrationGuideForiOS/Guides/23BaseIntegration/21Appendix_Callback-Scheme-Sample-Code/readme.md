# Appendix: Callback Scheme Sample Code
This code can be used to read the declared URL Type list to discover or check
the callback scheme for Workspace ONE integration. For sample usage of this
code, see either of these sets of instructions.

-   [Initialize from SwiftUI](../04Task_Initialize-the-software-development-kit-runtime/01Initialize-from-SwiftUI/readme.md).
-   [Initialize from Storyboard](../04Task_Initialize-the-software-development-kit-runtime/02Initialize-from-Storyboard/readme.md).

The sample code is also published here.  
[github.com/vmware-samples/…/SchemeCheck.swift](https://github.com/vmware-samples/workspace-ONE-SDK-integration-samples/blob/main/IntegrationGuideForiOS/Guides/23BaseIntegration/21Appendix_Callback-Scheme-Sample-Code/SchemeCheck.swift)

<p class="allow-page-break" />

    //  Copyright 2023 VMware, Inc.
    //  SPDX-License-Identifier: BSD-2-Clause

    import Foundation

    private let CFBundleURLTypes = "CFBundleURLTypes"
    private let CFBundleURLSchemes = "CFBundleURLSchemes"
    private let path = "://dummypath"

    /// Get the scheme of the first declared URL Type.
    ///
    /// This function returns the declared scheme on success, or throws an 
    /// error for any failure.
    ///
    /// - Throws: If the URL Type list is empty, or any properties are missing,
    /// or the value in the declaration can't be a URL scheme.
    /// - Returns: String containing the scheme of the first URL Type.
    func schemeForWS1() throws -> String {
        return try
        Bundle.main.urlTypes().first!.schemes().first!.validateAsURLScheme()
    }

    /// Checks if a specified scheme is in any declared URL Type.
    ///
    /// This function returns the specified scheme on success, or throws an 
    /// error for any failure.
    ///
    /// - Parameter expectedScheme: String containing the scheme.
    /// - Throws: If the URL Type list is empty, or any properties are missing,
    /// or if the specified value can't be found, or if the specified value
    /// can't be a URL scheme.
    /// - Returns: The expectedScheme parameter.
    func schemeForWS1(_ expectedScheme:String) throws -> String {
        var discoveredSchemes:[String] = []
        for urlType in try Bundle.main.urlTypes() {
            for scheme in try urlType.schemes() {
                if scheme == expectedScheme {
                    return try expectedScheme.validateAsURLScheme()
                }
                discoveredSchemes.append(scheme)
            }
        }

        throw NSError(domain: "", code: 9, userInfo: [
            NSLocalizedDescriptionKey:"Expected scheme \"\(expectedScheme)\""
            + " not found in main bundle URL Types."
            + " Schemes found \(discoveredSchemes.count): "
            + discoveredSchemes.joined(separator: " ")
            + "."
        ])
    }

    private extension Bundle {
        func urlTypes() throws -> [[String:Any]] {
            guard let infoDictionary = self.infoDictionary else {
                throw NSError(domain: "", code: 1, userInfo: [
                    NSLocalizedDescriptionKey:"Main bundle has no infoDictionary"
                ]) }
            guard let typesAny = infoDictionary[CFBundleURLTypes] else {
                throw NSError(domain: "", code: 2, userInfo: [
                    NSLocalizedDescriptionKey:"No \(CFBundleURLTypes) in main bundle"
                    + ". Keys \(infoDictionary.keys.count): "
                    + infoDictionary.keys.joined(separator: " ")
                ]) }
            guard let typesArray = typesAny as? [[String:Any]] else {
                throw NSError(domain: "", code: 3, userInfo: [
                    NSLocalizedDescriptionKey:
                        "Couldn't cast [\(CFBundleURLTypes)] to [[String:Any]]."
                    + " " + String(describing: typesAny)
                ]) }
            guard let _ = typesArray.first else {
                throw NSError(domain: "", code: 4, userInfo: [
                    NSLocalizedDescriptionKey:
                        "[\(CFBundleURLTypes)] is an empty array."
                ]) }
            return typesArray
        }
    }

    private extension Dictionary where Key == String {
        func schemes() throws -> [String] {
            guard let schemesAny = self[CFBundleURLSchemes] else {
                throw NSError(domain: "", code: 5, userInfo: [
                    NSLocalizedDescriptionKey:
                        "The " + CFBundleURLTypes + " dictionary has no "
                    + CFBundleURLSchemes + ". Keys \(self.keys.count): "
                    + self.keys.joined(separator: " ")
                ]) }
            guard let schemes = schemesAny as? [String] else {
                throw NSError(domain: "", code: 6, userInfo: [
                    NSLocalizedDescriptionKey:
                        "Couldn't cast " + CFBundleURLSchemes + " to [String]. "
                    + String(describing: schemesAny)
                ]) }
            guard let _ = schemes.first else {
                throw NSError(domain: "", code: 7, userInfo: [
                    NSLocalizedDescriptionKey:
                        "First " + CFBundleURLSchemes + " is an empty array."
                ]) }
            return schemes
        }

    }

    private extension String {
        func validateAsURLScheme() throws -> String {
            // Create a URL from the unchecked scheme to validate it. Note that URL
            // creation itself will succeed if the scheme is invalid but the .scheme
            // property will be nil.
            guard let _ = URL(string:self.appending(path))?.scheme else {
                let message = self.contains("_")
                ? " Scheme contains underscore _, which isn't allowed."
                : ""
                throw NSError(domain: "", code: 8, userInfo: [
                    NSLocalizedDescriptionKey:
                        "Scheme \"" + self
                    + "\" can't be used to construct a URL." + message
                ])
            }
            return self
        }
    }

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause