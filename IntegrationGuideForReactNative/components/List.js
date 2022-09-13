// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import React from 'react'
import { FlatList, StyleSheet, Text, TouchableOpacity } from 'react-native'

export default function List({ items, onPressItem }) {
  return (
    <FlatList
      data={items}
      keyExtractor={(item) => item.id}
      renderItem={({ item, index }) => (
        <TouchableOpacity
          style={[styles.item, { backgroundColor: itemColor(index) }]}
          onPress={() => onPressItem(item)}
        >
        <Text style={styles.title}>{item.title}</Text>
          <Text style={styles.desc}>{item.desc}</Text>
        </TouchableOpacity>
      )}
    />
  )
}

function itemColor(index) {
  return `rgba(59, 108, 212, ${Math.max(1 - index / 10, 0.4)})`
}

const styles = StyleSheet.create({
  item: {
    marginBottom: 1,
    padding: 15,
  },
  title: {
    fontSize: 24,
    color: 'white',
  },
  desc: {
    fontSize: 16,
    color: 'white',
  },
})