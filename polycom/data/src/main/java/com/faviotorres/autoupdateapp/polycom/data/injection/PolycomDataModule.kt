package com.faviotorres.autoupdateapp.polycom.data.injection

import com.faviotorres.autoupdateapp.polycom.data.repository.PolycomApiDataRepository
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PolycomDataModule {

    @Binds
    abstract fun providesPolycomApiDataRepository(
        repository: PolycomApiDataRepository
    ): PolycomApiRepository

}