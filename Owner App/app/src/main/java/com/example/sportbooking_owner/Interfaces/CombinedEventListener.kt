package com.example.sportbooking_owner.Interfaces

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface CombinedEventListener:ValueEventListener {
    fun onCombinedDataChanged(dataSnapshot: DataSnapshot)
    fun onCombinedCancelled(databaseError: DatabaseError)

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        onCombinedDataChanged(dataSnapshot)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        onCombinedCancelled(databaseError)
    }
}