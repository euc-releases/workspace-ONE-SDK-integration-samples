//
//  EnterpriseBrandingView.swift
//  SwiftUISDKExampleApp
//
//  Copyright 2022 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//

import SwiftUI

struct EnterpriseBrandingView: View {
    @State var brandingList: [BrandingModel] = []
    @State var sectionWiseListNeeded: Bool = true

    var body: some View {
        VStack(alignment: .leading){
            HStack {
                Toggle("Grouped List", isOn: $sectionWiseListNeeded)
                    .frame(width: 200, alignment: .leading)
                Spacer()
            }
            
            if !sectionWiseListNeeded{
                List(brandingList) { brandingInfo in
                    BrandingRow(brandingInfo: brandingInfo)
                }
            } else {
                let sectionWiseList = getSectionWiseList()
                List {
                    Section(header: Text(String(localized: "Colors"))) {
                        ForEach(sectionWiseList.colors) { (brandingInfo) in
                            BrandingRow(brandingInfo: brandingInfo)
                        }
                    }

                    Section(header: Text(String(localized: "Images"))) {
                        ForEach(sectionWiseList.images) { (brandingInfo) in
                            BrandingRow(brandingInfo: brandingInfo)
                        }
                    }
                    
                    Section(header: Text(String(localized: "invalidValues"))) {
                        ForEach(sectionWiseList.strings) { (brandingInfo) in
                            BrandingRow(brandingInfo: brandingInfo)
                        }
                    }
                }
            }
        }
        .padding()
        .onAppear(perform: performFetch)
    }

    func performFetch() {
        retrieveBrandingInfo()
    }


    func getSectionWiseList() -> (colors:[BrandingModel], images:[BrandingModel], strings:[BrandingModel]) {
        
        var brandingColors: [BrandingModel] = []
        var brandingImages: [BrandingModel] = []
        var brandingStrings: [BrandingModel] = []
        
        for brandItem in brandingList {
            switch brandItem.brandValue {
            case .colorValue(_):
                brandingColors.append(brandItem)
            case .imageUrl(_):
                brandingImages.append(brandItem)
            case .strValue(_):
                brandingStrings.append(brandItem)
            }
        }
        return (colors:brandingColors, images:brandingImages, strings:brandingStrings)
    }
}

struct EnterpriseBrandingView_Previews: PreviewProvider {
    static var previews: some View {
        EnterpriseBrandingView()
    }
}


extension EnterpriseBrandingView {
    private func retrieveBrandingInfo() {
        var brandInfoList: [BrandingModel] = []
        guard let brandingPayload = AWSDKHelper.shared.brandingPayload else {
            return
        }
        for child in Mirror(reflecting: brandingPayload).children {
            print("key: \(child.label ?? "NA"), value: \(child.value)")
            if let strKey = child.label  {
                brandInfoList.append(BrandingModel(key: strKey, value: child.value))
            }
        }
        
        brandingList.removeAll()
        brandingList.append(contentsOf: brandInfoList)
    }

    private func getDummyBrandingInfo() {
        let dummyModels = [
            BrandingModel(key: "hello", value: "#ffff33"),
            BrandingModel(key: "image URl", value: "https://source.unsplash.com/user/c_v_r/50x50"),
            BrandingModel(key: "logo", value: "https://sdkbng2203.ssdevrd.com/AirWatch/Images/Error/page-not-found.svg")
        ]

        brandingList.append(contentsOf: dummyModels)
    }
}
