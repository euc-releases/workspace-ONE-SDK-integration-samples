// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { NativeModules,StyleSheet, View, Button,Platform, Text} from 'react-native';
const {WorkspaceOneSdk } = NativeModules;

// const [value, onChangeText] = React.useState('Useless Placeholder');

export default class Information extends Component {

    constructor(){
 
        super();
   
        this.state = {
   
            UserName:'User Not Found',
            GroupId: 'GroupId Not Found',
            ServerName: 'Server Name Not Found'
        }
   
    }


    componentDidMount = async() => {

          try {
           const  useName = await WorkspaceOneSdk.userName();
            this.setState({
                      UserName: 'User Name : ' + useName
                  });
          } catch (error) {
            console.error(error);
          }

          try {
            var groupId = await WorkspaceOneSdk.groupId();
            this.setState({
                      GroupId:  'Group Id : ' + groupId 
                  });
          } catch (error) {
            console.error(error);
          }

          try {
            const serverName = await WorkspaceOneSdk.serverName();
            this.setState({
              ServerName: 'Server Name : ' + serverName 
                  });
          } catch (error) {
            console.error(error);
          }

    }

    

    render(){
        return(
            <View style={styles.screen}>
            <Text style={styles.title}>{ this.state.UserName }</Text>
            <Text style={styles.title}>{this.state.GroupId}</Text>
            <Text style={styles.title}>{this.state.ServerName}</Text>
            {/* <TextInput
                style={{ height: 40, borderColor: 'gray', borderWidth: 1 }}
                onChangeText={text => onChangeText(text)}
                value={value}
            />    */}
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