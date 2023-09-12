package com.example.diagnalassessmenttest.diModules

import android.content.Context
import com.example.diagnalassessmenttest.utils.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
//object SingletonModules {
//
//    @Singleton
//    @Provides
//    fun providesUtility(@ApplicationContext context: Context): Utility {
//        return Utility(context)
//    }
//}