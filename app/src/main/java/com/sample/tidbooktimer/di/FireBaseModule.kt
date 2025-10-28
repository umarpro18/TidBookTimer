package com.sample.tidbooktimer.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Real time db (Not using)
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    // Cloud db
    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}