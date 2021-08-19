package com.faviotorres.autoupdateapp.polycom.data.injection

import android.content.Context
import androidx.work.WorkManager
import com.faviotorres.autoupdateapp.polycom.data.repository.PolycomApiDataRepository
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PolycomDataModule {

    @Binds
    abstract fun providesPolycomApiDataRepository(
        repository: PolycomApiDataRepository
    ): PolycomApiRepository

    companion object {

        @Provides
        fun providesWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}
