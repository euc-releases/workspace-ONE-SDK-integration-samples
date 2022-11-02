//
//  CryptoView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI


struct CryptoView: View {
    @State private var cryptoText: String = String(localized: "cryptoText")
    @State private var isEncrypted: Bool = false
    @State private var shouldShowAlert = false

    static var errorMessage: String = String(localized: "cryptoError")

    var body: some View {
        VStack{
            vwTitle
            txtEditorView
            if !isEncrypted {
                encryptionWarningNote
            }
            btnEncrypt
            Spacer()
        }
        .padding()
        .navigationBarTitle(String(localized: "cryptoNavigationTitle"))
    }
}

struct CryptoView_Previews: PreviewProvider {
    static var previews: some View {
        CryptoView()
    }
}

private extension CryptoView {
    var vwTitle: some View {
        Text(isEncrypted ?  String(localized: "decryptText") : String(localized: "encryptText"))
    }
    var txtEditorView: some View {
        TextEditor(text: $cryptoText)
            .disabled(isEncrypted)
            .frame( height: 250)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.2), lineWidth: 4)
            )
            .foregroundColor(isEncrypted ? .gray : .black)
    }
    var encryptionWarningNote : some View {
        Label(String(localized: "encryptionWarning"), systemImage: "info.circle")
            .foregroundColor(.orange)
    }
    var btnEncrypt: some View {
        Button{
            if cryptoText.isEmpty {
                shouldShowAlert = true
                CryptoView.errorMessage = String(localized: "cryptoEmptyErrorMessage")
            } else {
                btnDidSelect()
            }
        } label: {
            Text(isEncrypted ? String(localized: "Decrypt") : String(localized: "Encrypt"))
        }
        .padding(.all, 12)
        .background(.blue)
        .cornerRadius(12)
        .foregroundColor(.white)
        .alert(isPresented: $shouldShowAlert) {
            Alert(title: Text(isEncrypted ? String(localized: "DecryptFailed") : String(localized: "EncryptFailed")), message: Text(CryptoView.errorMessage), dismissButton: .default(Text(String(localized: "Ok"))))
        }
    }
    
}
private extension CryptoView {
    private func btnDidSelect() {
        do {
            if !isEncrypted {
                // Using Encrypt required user to unlock the container if it locked with a passcode or username/password.
                // Expect to see an error thrown if the container is not unlocked.
                cryptoText = try AWSDKHelper.shared.encrypt(cryptoText.data).hexString
                isEncrypted = true
            } else {
                cryptoText = try AWSDKHelper.shared.decrypt(cryptoText.hexData).string
                isEncrypted = false
            }
        } catch let error {
            shouldShowAlert = true
            CryptoView.errorMessage = "\(String(localized: "CryptoFailMessage")) \(isEncrypted ? String(localized: "Decrypt") : String(localized: "Encrypt") ):\n\(error.localizedDescription)"
        }
    }
}

