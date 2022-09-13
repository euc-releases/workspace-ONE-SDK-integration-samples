// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { NativeModules,StyleSheet, View, Button,Platform, Text} from 'react-native';
const {WorkspaceOneSdk } = NativeModules;



export default class CustomSettings extends Component {

    constructor(){
 
        super();
   
        this.state = {
   
            CustomSettings:'Custom Settings Not Found',
            
        }
   
    }


    componentDidMount =  async() => {

      try {
        const customSettings = await WorkspaceOneSdk.customSettings();
           this.setState({
            CustomSettings: 'Custom Settings : ' + customSettings
               });
        } catch (error) {
         console.error({error});
       }
    }

    

    render(){
        return(
            <View style={styles.screen}>
            <Text style={styles.title}>{ this.state.CustomSettings}</Text>
          
                
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