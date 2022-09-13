// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { NativeModules,StyleSheet, View, Button,Platform, Text} from 'react-native';
const {WorkspaceOneSdk } = NativeModules;



export default class DLPSettings extends Component {

    constructor(){
 
        super();
   
        this.state = {
   
            AllowCopyPaste: false,
            OfflineAllowed: false,
            RestrictDocumentToApps: false,
            AllowedApplications: [],
            IsCompliant:false,
            IsCompromised:false
        }
   
    }


    componentDidMount = async() => {


            try {
              const allowCopyPaste = await WorkspaceOneSdk.allowCopyPaste();
                this.setState({
                  AllowCopyPaste : 'Enable Copy Paste  : ' + allowCopyPaste
                    });
              } catch (error) {
              console.error(error);
            }
   
            try {
              const allowOffline = await WorkspaceOneSdk.allowOffline();
                 this.setState({
                  OfflineAllowed : 'Allow Offline  : ' + allowOffline
                     });
              } catch (error) {
               console.error(error);
             }

              try {
                const restrictDocumentToApps = await WorkspaceOneSdk.restrictDocumentToApps();
                   this.setState({
                    RestrictDocumentToApps : 'Restrict Document To Apps : ' + restrictDocumentToApps
                       });
                } catch (error) {
                 console.error(error);
               }

              // try {
              //   const allowedApplications = await WorkspaceOneSdk.allowedApplications();
              //      this.setState({
              //       AllowedApplications : 'Allowed Applications : ' + allowedApplications
              //          });
              //   } catch (error) {
              //    console.error({error});
              //  }

              try {
                const isCompliant = await WorkspaceOneSdk.isCompliant();
                   this.setState({
                    IsCompliant: 'Is Compliant : ' + isCompliant
                       });
                } catch (error) {
                 console.error(error);
               }

              try {
                const isCompromised = await WorkspaceOneSdk.isCompromised();
                   this.setState({
                    IsCompromised: 'Is Compromised : ' + isCompromised
                       });
                } catch (error) {
                 console.error(error);
               }
    }

    

    render(){
        return(
            <View style={styles.screen}>
            <Text style={styles.title}>{ this.state.AllowCopyPaste}</Text>
            <Text style={styles.title}>{ this.state.OfflineAllowed}</Text>
            <Text style={styles.title}>{ this.state.RestrictDocumentToApps}</Text>
            <Text style={styles.title}>{ this.state.IsCompliant}</Text>
            <Text style={styles.title}>{ this.state.IsCompromised}</Text>
            <Text style={styles.title}>{ this.state.AllowedApplications}</Text>
            </View>
        )
    }
}

const styles = StyleSheet.create(
    {
     
    MainContainer:{
       justifyContent: 'center',
       alignItems: 'center',
       flex:1,
       marginTop: (Platform.OS) === 'ios' ? 20 : 0
      },
    title: {
        fontSize: 24,
        color: '#000000',
      },
    desc: {
        fontSize: 16,
        color: '#000000',
      },
    });