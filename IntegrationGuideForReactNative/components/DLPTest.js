// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { NativeModules,StyleSheet, View, Button,Platform, Text} from 'react-native';
import { TextInput } from 'react-native-gesture-handler';
const {WorkspaceOneSdk } = NativeModules;


export default class DLPTest extends Component {
    constructor(props) {
        super(props)
        this.state={
          pasting: false,
          beforePasting : ""
        };
       this.paste = this.paste.bind(this);
       this.change = this.change.bind(this);
      }
      
      paste(e){
          console.log('Inside paste')
        this.setState({pasting: false, beforePasting: e.target.value});
      }
      change(e){
        if(this.state.pasting){
          var oldval = this.state.beforePasting;
          var newval = e.target.value;
          var pastedValue = getDifference(oldval, newval);
          console.log(pastedValue);
          this.setState({pasting: false});
        }
       
      }
      
      render() {
        return (
          <View>
             <TextInput type="text" 
                onPaste={(e)=>{
                    e.persist();}}
                onChange={this.change} />
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