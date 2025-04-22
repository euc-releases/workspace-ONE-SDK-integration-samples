//
//  ScreenshotAVView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2025 Omnissa, LLC.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI
import AVKit

struct ScreenshotAVView: View {
    let player = AVPlayer(url: URL(string: "https://www.apple.com/media/us/ios/2021/6e4b26f0-cd2f-47e3-b105-8313b3b37042/films/intro/ios-15-intro-tpl-us-20210913.m3u8")!)
    
    var body: some View {
        VStack {
            Text("AVPlayerView")
                .font(.largeTitle)
                .padding()
            
            AVPlayerControllerRepresentable(player: player)
                .frame(width: 300, height: 200)
                .padding()
        }
    }
}

struct AVPlayerControllerRepresentable: UIViewControllerRepresentable {
    let player: AVPlayer
    
    func makeUIViewController(context: Context) -> AVPlayerViewController {
        let playerViewController = AVPlayerViewController()
        playerViewController.player = player
        return playerViewController
    }
    
    func updateUIViewController(_ uiViewController: AVPlayerViewController, context: Context) {}
}

